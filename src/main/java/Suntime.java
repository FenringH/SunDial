import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.TimeZone;

import static java.lang.Math.*;

public class Suntime {

    // constants
    public static double DEFAULT_LONGITUDE = 15.9788553d;  // East
    public static double DEFAULT_LATITUDE = 45.7830997d;   // North

    public static double MIN_LONGITUDE = -180d;
    public static double MAX_LONGITUDE = 180d;
    public static double MIN_LATITUDE = -90d;
    public static double MAX_LATITUDE = 90d;

    private final static long J2000 = 2451545;                    // UTC 01.01.2000. 12:00
    private final static long DEFAULT_PRECISION = 10000;          // Refine calculation until deviation is less than 1/10000
    private final static long MAX_ITERATIONS = 100;               // when using precision don't iterate more than this many times

    private final static int SUNRISE_HORIZON = -1;
    private final static int SUNSET_HORIZON = 1;

    // inputs
    private GregorianCalendar localTime;
    private double julianDate;
    private double observerLongitude;
    private double observerLatitude;
    private long precision;

    // intermediaries
    private long julianDayNumber;
    private double meanAnomaly;
    private double equationOfCenter;
    private double eclipticalLongitude;
    private double rightAscension;
    private double declinationOfTheSun;
    private double hourAngle;
    private double solarTransit;
    private double localHourAngle;
    private double siderealTime;

    // Constructors - Builder
    private Suntime(Builder builder) {
        this.localTime = builder.localTime;
        this.julianDate = builder.julianDate;
        this.julianDayNumber = builder.julianDayNumber;
        this.observerLongitude = builder.observerLongitude;
        this.observerLatitude = builder.observerLatitude;
        this.precision = builder.precision;
        this.init();
    }

    private void init() {
        this.meanAnomaly            = this.calcMeanAnomaly(this.julianDayNumber);
        this.equationOfCenter       = this.calcEquationOfCenter(this.meanAnomaly);
        this.eclipticalLongitude    = this.calcEclipticalLongitude(this.meanAnomaly, this.equationOfCenter);
        this.rightAscension         = this.calcRightAscension(this.eclipticalLongitude);
        this.declinationOfTheSun    = this.calcDeclinationOfTheSun(this.eclipticalLongitude);
        this.siderealTime           = this.calcSiderealTime(this.julianDayNumber, this.observerLongitude);
        this.hourAngle              = this.calcHourAngle(this.rightAscension, this.siderealTime);
        this.solarTransit           = this.calcSolarTransit(this.julianDayNumber, this.observerLongitude, this.meanAnomaly, this.eclipticalLongitude);
        this.localHourAngle         = this.calcLocalHourAngle(this.declinationOfTheSun, this.observerLatitude);
    }

    // Builder
    public static class Builder {
        private GregorianCalendar localTime;
        private double julianDate;
        private long julianDayNumber;
        private double observerLongitude;
        private double observerLatitude;
        private long precision;

        public Builder() {
            this.localTime = new GregorianCalendar();
            this.julianDate = J2000;
            this.julianDayNumber = J2000;
            this.observerLongitude = DEFAULT_LONGITUDE;
            this.observerLatitude = DEFAULT_LATITUDE;
            this.precision = DEFAULT_PRECISION;
        }

        public Builder localTime(GregorianCalendar localTime) {
            if (localTime == null) { return this; }
            this.localTime = localTime;
            this.julianDate = Suntime.getJulianDate(localTime);
            this.julianDayNumber = Suntime.getJulianDayNumber(localTime);
            return this;
        }

        public Builder julianDate(double julianDate) {
            this.julianDate = julianDate;
            this.localTime = Suntime.getCalendarDate(julianDate, this.localTime.getTimeZone());
            this.julianDayNumber = Suntime.getJulianDayNumber(this.localTime);
            return this;
        }

        public Builder julianDayNumber(long julianDayNumber) {
            this.julianDayNumber = julianDayNumber;
            return this;
        }

        public Builder observerLongitude(double observerLongitude) {
            this.observerLongitude = observerLongitude;
            return this;
        }

        public Builder observerLatitude(double observerLatitude) {
            this.observerLatitude = observerLatitude;
            return this;
        }

        public Builder precision(long precision) {
            this.precision = precision;
            return this;
        }

        public Suntime build() {
            return new Suntime(this);
        }
    }

    // Utilities
    public static double convertFractionToSeconds(double fraction) {
        return fraction * (24 * 60 * 60);
    }

    public static String printSecondsToTime(double seconds) {

        double precisionDays = seconds / (24 * 60 * 60);
        int days = (int) floor(precisionDays);
        double precisionHours = (precisionDays - days) * 24;
        int hours = (int) floor(precisionHours);
        double precisionMins = (precisionHours - hours) * 60;
        int mins = (int) floor(precisionMins);
        double precisionSecs = (precisionMins - mins) * 60;
        int secs = (int) floor(precisionSecs);

        String daysString = ("00" + days);
        daysString = daysString.substring(daysString.length() - 2, daysString.length());
        String hoursString = ("00" + hours);
        hoursString = hoursString.substring(hoursString.length() - 2, hoursString.length());
        String minsString = ("00" + mins);
        minsString = minsString.substring(minsString.length() - 2, minsString.length());
        String secsString = ("00" + secs);
        secsString = secsString.substring(secsString.length() - 2, secsString.length());

        String result = secsString + "s";
        if (mins > 0 || hours > 0 || days > 0) { result = minsString + "m " + result; }
        if (hours > 0 || days > 0) { result = hoursString + "h " + result; }
        if (days > 0) { result = daysString + "d " + result; }

        return result;
    }

    public static GregorianCalendar convertToUtc(GregorianCalendar calendar) {

        GregorianCalendar utcTime = new GregorianCalendar();
        utcTime.set(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND)
        );
        utcTime.get(Calendar.HOUR_OF_DAY);
        utcTime.setTimeZone(TimeZone.getTimeZone("UTC"));

        return utcTime;
    }

    public static long getJulianDayNumber(GregorianCalendar calendar) {

        GregorianCalendar utcTime = convertToUtc(calendar);
//        GregorianCalendar utcTime = calendar;

        int j = utcTime.get(Calendar.YEAR);
        int m = utcTime.get(Calendar.MONTH) + 1;
        int d = utcTime.get(Calendar.DAY_OF_MONTH);

        double c0 = floor(((double) (m - 3)) / 12d);
        double x4 = j + c0;
        double x3 = floor(x4 / 100);
        double x2 = x4 % 100;
        double x1 = m - 12 * c0 - 3;

        return (long) floor(
            floor((146097d * x3) / 4d)
            + floor((36525d * x2) / 100d)
            + floor((153d * x1 + 2d) / 5d)
            + d + 1721119d
        );
    }

    public static double getJulianDate(GregorianCalendar calendar) {

        GregorianCalendar utcTime = convertToUtc(calendar);

        int hour = utcTime.get(Calendar.HOUR_OF_DAY);
        int min = utcTime.get(Calendar.MINUTE);
        int sec = utcTime.get(Calendar.SECOND);

        return getJulianDayNumber(calendar)
            + ((hour * 60d * 60d + min * 60d + sec) / (24d * 60d * 60d)) - 0.5d
            ;
   }

    public static GregorianCalendar getCalendarDate(double JulianDate, TimeZone timeZone) {

        double CJDN = floor(JulianDate + 0.5d);
        double dayFraction = JulianDate + 0.5d - CJDN;

        double a3 = (4d * CJDN) - 6884477d;
        double x3 = floor(a3 / 146097d);
        double r3 = a3 % 146097;

        double a2 = 100d * floor(r3 / 4d) + 99d;
        double x2 = floor(a2 / 36525d);
        double r2 = a2 % 36525;

        double a1 = 5 * floor(r2 / 100d) + 2d;
        double x1 = floor(a1 / 153d);
        double r1 = a1 % 153;

        int c0 = (int) floor((x1 + 2d) / 12d);

        int d = (int) floor(r1 / 5d) + 1;
        int m = (int) x1 - 12 * c0 + 3;
        int j = (int) x3 * 100 + (int) x2 + c0;

        double hourFraction = dayFraction * 24d;
        int hour = (int) floor(hourFraction);

        double minFraction = (hourFraction - hour) * 60d;
        int min = (int) floor(minFraction);

        double secFraction = (minFraction - min) * 60d;
        int sec = (int) floor(secFraction);

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        calendar.set(j, m - 1, d, hour, min, sec);
        calendar.get(Calendar.HOUR);
        calendar.setTimeZone(timeZone);

        return calendar;
    }

    // calculation methods
    private double calcMeanAnomaly(double juliandDate) {
        return (357.5291d + 0.98560028d * (juliandDate - J2000)) % 360;
    }

    private double calcEquationOfCenter(double meanAnomaly) {
        return 1.9148d * sin(toRadians(meanAnomaly)) +
                0.0200d * sin(toRadians(2 * meanAnomaly)) +
                0.0003d * sin(toRadians(3 * meanAnomaly));
    }

    private double calcEclipticalLongitude(double meanAnomaly, double equationOfCenter) {
        return (meanAnomaly + 102.9373d + equationOfCenter + 180.0d) % 360;
    }

    private double calcRightAscension(double eclipticalLongitude) {
        return toDegrees( atan2( sin(toRadians(eclipticalLongitude)) * cos(toRadians(23.4393d)),
                cos(toRadians(eclipticalLongitude)) ) );
    }

    private double calcDeclinationOfTheSun(double eclipticalLongitude) {
        return toDegrees( asin( sin(toRadians(eclipticalLongitude)) * sin(toRadians(23.4393d)) ) );
    }

    private double calcSiderealTime(double julianDate, double observerLongitude) {
        return (280.1470d + 360.9856235d * (julianDate - J2000) - (-1d * observerLongitude)) % 360;
    }

    private double calcHourAngle(double rightAscension, double siderealTime) {
        return siderealTime - rightAscension;
    }

    private double calcLocalHourAngle(double declinationOfTheSun, double observerLatitude) {

        double dividend = sin(toRadians(-0.83d)) - sin(toRadians(observerLatitude)) * sin(toRadians(declinationOfTheSun));
        double divisor = cos(toRadians(observerLatitude)) * cos(toRadians(declinationOfTheSun));
        double division = dividend / divisor;

        if (division <= -1) { return 180.0d; }
        if (division >= 1) { return 0.0d; }

        return toDegrees(acos(dividend / divisor));
    }

    private double calcSolarTransit(double julianDate, double observerLongitude, double meanAnomaly, double eclipticalLongitude) {
        double nx = julianDate - J2000 - 0.0009d - (-1d * observerLongitude) / 360d;
        double n = (double) round(nx);
        double Jx = julianDate + (n - nx);
        return Jx + 0.0053d * sin(toRadians(meanAnomaly)) - 0.0068d * sin(toRadians(2 * eclipticalLongitude));
    }

    private double calcSunriseJulianDate(double solarTransit, double localHourAngle) {
        return calcHorizonJulianDate(solarTransit, localHourAngle, SUNRISE_HORIZON);
    }

    private double calcSunsetJulianDate(double solarTransit, double localHourAngle) {
        return calcHorizonJulianDate(solarTransit, localHourAngle, SUNSET_HORIZON);
    }

    private double calcHorizonJulianDate(double solarTransit, double localHourAngle, int horizonFactor) {

        if (horizonFactor != SUNRISE_HORIZON && horizonFactor != SUNSET_HORIZON) { return 0; }

        // iterate for better precision
        double estimateJulianDate = solarTransit + horizonFactor * (localHourAngle / 360d);

        double JDcorrection = 1d;
        double newMeanAnomaly, newEquationOfCenter, newEclipticalLongitude,
                newDeclinationOfTheSun, newLocalHourAngle, newSolarTransit, newJulianDate = 0d;

        for (int i = 0; i < MAX_ITERATIONS && abs(JDcorrection * this.precision) > 1; i++) {
            newMeanAnomaly = this.calcMeanAnomaly(estimateJulianDate);
            newEquationOfCenter = this.calcEquationOfCenter(newMeanAnomaly);
            newEclipticalLongitude = this.calcEclipticalLongitude(newMeanAnomaly, newEquationOfCenter);
            newDeclinationOfTheSun = this.calcDeclinationOfTheSun(newEclipticalLongitude);
            newLocalHourAngle = this.calcLocalHourAngle(newDeclinationOfTheSun, this.observerLatitude);
            newSolarTransit = this.calcSolarTransit(estimateJulianDate, this.observerLongitude, newMeanAnomaly, newEclipticalLongitude);

            newJulianDate = newSolarTransit + horizonFactor * (newLocalHourAngle / 360d);

            JDcorrection = newJulianDate - estimateJulianDate;
            estimateJulianDate = newJulianDate;
        }

        return estimateJulianDate;
    }

    // Getters
    public double getMeanAnomaly() {
        return meanAnomaly;
    }

    public double getEquationOfCenter() {
        return equationOfCenter;
    }

    public double getEclipticalLongitude() {
        return eclipticalLongitude;
    }

    public double getRightAscension() {
        return rightAscension;
    }

    public double getDeclinationOfTheSun() {
        return declinationOfTheSun;
    }

    public double getHourAngle() {
        return hourAngle;
    }

    public double getSolarTransit() {
        return solarTransit;
    }

    public double getLocalHourAngle() {
        return localHourAngle;
    }

    public double getSiderealTime() {
        return this.siderealTime;
    }

    public long getJulianDayNumber() {
        return this.julianDayNumber;
    }

    public double getJulianDate() {
        return this.julianDate;
    }

    public double getSunriseJulianDate() {
        return calcSunriseJulianDate(this.solarTransit, this.localHourAngle);
    }

    public double getSunsetJulianDate() {
        return calcSunsetJulianDate(this.solarTransit, this.localHourAngle);
    }

    public double getHighnoonJulianDate() {
        return this.solarTransit;
    }

    public double getSunTime() {

        GregorianCalendar highNoon = getCalendarDate(this.solarTransit, this.localTime.getTimeZone());
        GregorianCalendar localNoon = new GregorianCalendar(this.localTime.getTimeZone());
        localNoon.set(
                highNoon.get(Calendar.YEAR),
                highNoon.get(Calendar.MONTH),
                highNoon.get(Calendar.DAY_OF_MONTH),
                12, 0, 0
        );

        long offsetInMillis = highNoon.getTimeInMillis() - localNoon.getTimeInMillis();

        return this.julianDate - ((float) offsetInMillis / (1000 * 60 * 60 * 24));
    }

    // set methods
    public void setObserverTime(GregorianCalendar localTime) {
        double newJulianDate = Suntime.getJulianDate(localTime);
        setObserverTime(newJulianDate);
    }

    public void setObserverTime(double julianDate) {
        if(this.julianDate != julianDate) {
            this.julianDate = julianDate;
            this.localTime = Suntime.getCalendarDate(julianDate, this.localTime.getTimeZone());
            this.julianDayNumber = Suntime.getJulianDayNumber(this.localTime);
            init();
        }
    }

    public void setObserverPosition(double observerLongitude, double observerLatitude) {
        if(this.observerLongitude != observerLongitude || this.observerLatitude != observerLatitude) {
            this.observerLongitude = observerLongitude;
            this.observerLatitude = observerLatitude;
            init();
        }
    }

}
