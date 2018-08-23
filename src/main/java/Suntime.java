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
        this.julianDate = builder.julianDate;
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
        private double julianDate;
        private double observerLongitude;
        private double observerLatitude;

        public Builder() {
            this.julianDate = J2000;
            this.observerLongitude = DEFAULT_LONGITUDE;
            this.observerLatitude = DEFAULT_LATITUDE;
        }

        public Builder julianDate(double julianDate) {
            this.julianDate = julianDate;
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
        return solarTransit - (localHourAngle / 360d);
    }

    private double calcSunsetJulianDate(double solarTransit, double localHourAngle) {
        return solarTransit + (localHourAngle / 360d);
    }

    private void init() {
        this.meanAnomaly            = this.calcMeanAnomaly(this.julianDate);
        this.equationOfCenter       = this.calcEquationOfCenter(this.meanAnomaly);
        this.eclipticalLongitude    = this.calcEclipticalLongitude(this.meanAnomaly, this.equationOfCenter);
        this.rightAscension         = this.calcRightAscension(this.eclipticalLongitude);
        this.declinationOfTheSun    = this.calcDeclinationOfTheSun(this.eclipticalLongitude);
        this.siderealTime           = this.calcSiderealTime(this.julianDate, this.observerLongitude);
        this.hourAngle              = this.calcHourAngle(this.rightAscension, this.siderealTime);
        this.solarTransit           = this.calcSolarTransit(this.julianDate, this.observerLongitude, this.meanAnomaly, this.eclipticalLongitude);
        this.localHourAngle         = this.calcLocalHourAngle(this.declinationOfTheSun, this.observerLatitude);
    }

    // get methods
    public double getSiderealTime() {
        return this.siderealTime;
    }

    public double getSunriseJulianDay() {
        return calcSunriseJulianDate(this.solarTransit, this.localHourAngle);
    }

    public double getSunsetJulianDay() {
        return calcSunsetJulianDate(this.solarTransit, this.localHourAngle);
    }

    // set methods
    public void set(double julianDate) {
        if(this.julianDate != julianDate) {
            this.julianDate = julianDate;
            init();
        }
    }

    public void setObserverLongitude(double observerLongitude) {
        if(this.observerLongitude != observerLongitude) {
            this.observerLongitude = observerLongitude;
            init();
        }
    }

    public void setObserverLatitude(double observerLatitude) {
        if(this.observerLatitude != observerLatitude) {
            this.observerLatitude = observerLatitude;
            init();
        }
    }
}
