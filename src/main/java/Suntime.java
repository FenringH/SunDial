import static java.lang.Math.*;

public class Suntime {

    // constants
    private static double DEFAULT_LONGITUDE = 15.9788553d;  // East
    private static double DEFAULT_LATITUDE = 45.7830997d;   // North
    private static double J2000 = 2451545.0d;               // UTC 01.01.2000. 12:00

    // inputs
    private double julianDayNumber;
    private double observerLongitude;
    private double observerLatitude;

    // outputs
    private double siderealTime;
    private double sunriseJulianDate;
    private double sunsetJulianDate;

    // Constructors - Builder
    private Suntime(Builder builder) {
        this.julianDayNumber = builder.julianDayNumber;
        this.observerLongitude = builder.observerLongitude;
        this.observerLatitude = builder.observerLatitude;
        this.init();
    }

    // Constructors - normal
    public Suntime() { this(J2000, DEFAULT_LONGITUDE, DEFAULT_LATITUDE); }

    public Suntime(double julianDayNumber) {
        this(julianDayNumber, DEFAULT_LONGITUDE, DEFAULT_LATITUDE);
    }

    public Suntime(double julianDayNumber, double observerLongitude, double observerLatitude) {
        this.julianDayNumber = julianDayNumber;
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

    // calculation methods
    private double calcMeanAnomaly(double J) {
        return (357.5291d + 0.98560028d * (J - J2000)) % 360;
    }

    private double calcEquationOfCenter(double M) {
        return 1.9148d * sin(toRadians(M)) + 0.0200d * sin(toRadians(2 * M)) + 0.0003d * sin(toRadians(3 * M));
    }

    private double calcEclipticalLongitude(double M, double C) {
        return (M + 102.9373d + C + 180.0d) % 360;
    }

    private double calcRightAscension(double lambda) {
        return toDegrees( atan2( sin(toRadians(lambda)) * cos(toRadians(23.4393d)), cos(toRadians(lambda)) ) );
    }

    private double calcDeclinationOfTheSun(double lambda) {
        return toDegrees( asin( sin(toRadians(lambda)) * sin(toRadians(23.4393d)) ) );
    }

    private double calcSiderealTime(double J, double Lw) {
        return (280.1470d + 360.9856235d * (J - J2000) - Lw) % 360;
    }

    private double calcHourAngle(double alpha, double theta) {
        return theta - alpha;
    }

    private double calcSolarTransit(double J, double Lw, double M, double lambda) {

        double nx = J - J2000 - 0.0009d - Lw / 360d;
        double n = (double) round(nx);
        double Jx = J + (n - nx);

        return Jx + 0.0053d * sin(toRadians(M)) - 0.0068d * sin(toRadians(2*lambda));
    }

    private double calcLocalHourAngle(double delta, double phi) {
        return toDegrees( acos(
                (sin(toRadians(-0.83d)) - sin(toRadians(phi)) * sin(toRadians(delta)) ) /
                        (cos(toRadians(phi)) * cos(toRadians(delta)) )
            ) );
    }

    private double calcSunriseJulianDate(double Jtransit, double Ht) {
        return Jtransit - (Ht / 360d);
    }

    private double calcSunsetJulianDate(double Jtransit, double Ht) {
        return Jtransit + (Ht / 360d);
    }

    // public methods
    public boolean init() {

        double J = this.julianDayNumber;
        double Lw = -1d * this.observerLongitude;
        double phi = this.observerLatitude;

        double M        = this.calcMeanAnomaly(J);
        double C        = this.calcEquationOfCenter(M);
        double lambda   = this.calcEclipticalLongitude(M, C);
        double alpha    = this.calcRightAscension(lambda);
        double delta    = this.calcDeclinationOfTheSun(lambda);
        double theta    = this.calcSiderealTime(J, Lw);
        double H        = this.calcHourAngle(alpha, theta);
        double Jtransit = this.calcSolarTransit(J, Lw, M, lambda);
        double Ht       = this.calcLocalHourAngle(delta, phi);

        double Jrise = this.calcSunriseJulianDate(Jtransit, Ht);
        J = Jrise;
        M        = this.calcMeanAnomaly(J);
        C        = this.calcEquationOfCenter(M);
        lambda   = this.calcEclipticalLongitude(M, C);
        alpha    = this.calcRightAscension(lambda);
        delta    = this.calcDeclinationOfTheSun(lambda);
        theta    = this.calcSiderealTime(J, Lw);
        H        = this.calcHourAngle(alpha, theta);
        Jtransit = this.calcSolarTransit(J, Lw, M, lambda);
        Ht       = this.calcLocalHourAngle(delta, phi);
        Jrise = this.calcSunriseJulianDate(Jtransit, Ht);

        double sunsetJDate = this.calcSunsetJulianDate(Jtransit, Ht);

        this.siderealTime = theta;
        this.sunriseJulianDate = Jrise;
        this.sunsetJulianDate = sunsetJDate;

        return true;
    }

    public double getSiderealTime() {
        return this.siderealTime;
    }

    public double getSunriseJulianDay() {
        return this.sunriseJulianDate;
    }

    public double getSunsetJulianDay() {
        return this.sunsetJulianDate;
    }

    // set methods
    public void setJulianDayNumber(double julianDayNumber) {
        this.julianDayNumber = julianDayNumber;
    }

    public void setObserverLongitude(double observerLongitude) {
        this.observerLongitude = observerLongitude;
    }

    public void setObserverLatitude(double observerLatitude) {
        this.observerLatitude = observerLatitude;
    }
}
