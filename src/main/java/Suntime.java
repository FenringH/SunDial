import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static java.lang.Math.*;

public class Suntime {

    // constants
    private static double DEFAULT_LONGITUDE = 15.9788553d;  // East
    private static double DEFAULT_LATITUDE = 45.7830997d;   // North
    private static double J2000 = 2451545.0d;               // UTC 01.01.2000. 12:00

    // inputs
    private double julianDate;
    private double observerLongitude;
    private double observerLatitude;

    // intermediaries
    private double julianDayNumber;
    private double meanAnomaly;
    private double equationOfCenter;
    private double eclipticalLongitude;
    private double rightAscension;
    private double declinationOfTheSun;
    private double hourAngle;
    private double solarTransit;
    private double localHourAngle;

    // outputs
    private double siderealTime;
    private double sunriseJulianDate;
    private double sunsetJulianDate;

    // Constructors - Builder
    private Suntime(Builder builder) {
        this.julianDate = builder.julianDayNumber;
        this.observerLongitude = builder.observerLongitude;
        this.observerLatitude = builder.observerLatitude;
        this.init();
    }

    // Constructors - normal
    public Suntime() { this(J2000, DEFAULT_LONGITUDE, DEFAULT_LATITUDE); }

    public Suntime(double julianDate) {
        this(julianDate, DEFAULT_LONGITUDE, DEFAULT_LATITUDE);
    }

    public Suntime(double julianDate, double observerLongitude, double observerLatitude) {
        this.julianDate = julianDate;
        this.observerLongitude = observerLongitude;
        this.observerLatitude = observerLatitude;
        this.init();
    }

    // Builder
    public static class Builder {
        private double julianDayNumber;
        private double observerLongitude;
        private double observerLatitude;

        public Builder() {
            this.julianDayNumber = J2000;
            this.observerLongitude = DEFAULT_LONGITUDE;
            this.observerLatitude = DEFAULT_LATITUDE;
        }

        public Builder julianDayNumber(double julianDayNumber) {
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

        public Suntime build() {
            return new Suntime(this);
        }
    }

    // Utilities
    public static double getJulianDayNumber(GregorianCalendar calendar) {

        int j = calendar.get(Calendar.YEAR);
        int m = calendar.get(Calendar.MONTH) + 1;
        int d = calendar.get(Calendar.DAY_OF_MONTH);

        double c0 = floor(((double) (m - 3)) / 12d);
        double x4 = j + c0;
        double x3 = floor(x4 / 100);
        double x2 = x4 % 100;
        double x1 = m - 12 * c0 - 3;

        return floor((146097d * x3) / 4d) + floor((36525d * x2) / 100d) + floor((153d * x1 + 2d) / 5d) + d + 1721119d;
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
//        calendar.setTimeZone(timeZone);

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
        return toDegrees( acos(
                (sin(toRadians(-0.83d)) - sin(toRadians(observerLatitude)) * sin(toRadians(declinationOfTheSun)) ) /
                        (cos(toRadians(observerLatitude)) * cos(toRadians(declinationOfTheSun)) )
            ) );
    }

    private double calcSolarTransit(double julianDate, double observerLongitude, double meanAnomaly, double eclipticalLongitude) {
        double nx = julianDate - J2000 - 0.0009d - (-1d * observerLongitude) / 360d;
        double n = (double) round(nx);
        double Jx = julianDate + (n - nx);
        return Jx + 0.0053d * sin(toRadians(meanAnomaly)) - 0.0068d * sin(toRadians(2 * eclipticalLongitude));
    }

    private double calcSunriseJulianDate(double solarTransit, double localHourAngle) {

        double estimateJulianDate = solarTransit - (localHourAngle / 360d);

        double JDcorrection = 1d;
        double newMeanAnomaly, newEquationOfCenter, newEclipticalLongitude,
                newDeclinationOfTheSun, newLocalHourAngle, newSolarTransit, newJulianDate = 0d;

        while(abs(JDcorrection * 10000) > 1) {
            newMeanAnomaly = this.calcMeanAnomaly(estimateJulianDate);
            newEquationOfCenter = this.calcEquationOfCenter(newMeanAnomaly);
            newEclipticalLongitude = this.calcEclipticalLongitude(newMeanAnomaly, newEquationOfCenter);
            newDeclinationOfTheSun = this.calcDeclinationOfTheSun(newEclipticalLongitude);
            newLocalHourAngle = this.calcLocalHourAngle(newDeclinationOfTheSun, this.observerLatitude);
            newSolarTransit = this.calcSolarTransit(estimateJulianDate, this.observerLongitude, newMeanAnomaly, newEclipticalLongitude);

            newJulianDate = newSolarTransit - (newLocalHourAngle / 360d);

            JDcorrection = newJulianDate - estimateJulianDate;
            estimateJulianDate = newJulianDate;
        }

        return estimateJulianDate;
    }

    private double calcSunsetJulianDate(double solarTransit, double localHourAngle) {

        double estimateJulianDate = solarTransit + (localHourAngle / 360d);

        double JDcorrection = 1d;
        double newMeanAnomaly, newEquationOfCenter, newEclipticalLongitude,
                newDeclinationOfTheSun, newLocalHourAngle, newSolarTransit, newJulianDate = 0d;

        while(abs(JDcorrection * 10000) > 1) {
            newMeanAnomaly = this.calcMeanAnomaly(estimateJulianDate);
            newEquationOfCenter = this.calcEquationOfCenter(newMeanAnomaly);
            newEclipticalLongitude = this.calcEclipticalLongitude(newMeanAnomaly, newEquationOfCenter);
            newDeclinationOfTheSun = this.calcDeclinationOfTheSun(newEclipticalLongitude);
            newLocalHourAngle = this.calcLocalHourAngle(newDeclinationOfTheSun, this.observerLatitude);
            newSolarTransit = this.calcSolarTransit(estimateJulianDate, this.observerLongitude, newMeanAnomaly, newEclipticalLongitude);

            newJulianDate = newSolarTransit + (newLocalHourAngle / 360d);

            JDcorrection = newJulianDate - estimateJulianDate;
            estimateJulianDate = newJulianDate;
        }

        return estimateJulianDate;
    }

    private void init() {
        this.julianDayNumber        = floor(this.julianDate);
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

    // get methods
    public double getSiderealTime() {
        return this.siderealTime;
    }

    public double getSunriseJulianDate() {
        return calcSunriseJulianDate(this.solarTransit, this.localHourAngle);
    }

    public double getSunsetJulianDate() {
        return calcSunsetJulianDate(this.solarTransit, this.localHourAngle);
    }

    // set methods
    public void setObserverTime(double julianDate) {
        if(this.julianDate != julianDate) {
            this.julianDate = julianDate;
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
