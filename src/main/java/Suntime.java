import static java.lang.Math.*;

public class Suntime {

    // constants
    private static double DEFAULT_LONGITUDE = 45.7830997d;
    private static double DEFAULT_LATITUDE = 15.9788553d;
    private static double J2000 = 2451545.0d;

    // inputs
    private double julianDayNumber;
    private double observerLongitude;
    private double observerLatitude;

    // intermediaries
    private double meanAnomaly;
    private double equationOfCenter;
    private double eclipticLongitude;
    private double rightAscension;
    private double declinationOfTheSun;
    private double siderealTime;
    private double hourAngle;
    private double solarTransit;

    // outputs
    private double sunsetJulianDay;
    private double sunriseJulianDay;

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
    private void calcMeanAnomaly() {
        double J = this.julianDayNumber;
        double M = (-3.59d + 0.98560d * (J - J2000)) % 360;
        this.meanAnomaly = M;
    }

    private void calcEquationOfCenter() {
        double M = this.meanAnomaly;
        double C = 1.9148d * sin(toRadians(M))
                + 0.0200d * sin(toRadians(2 * M))
                + 0.0003d * sin(toRadians(3 * M));
        this.equationOfCenter = C;
    }

    private void calcEclipticLongitude() {
        double M = this.meanAnomaly;
        double C = this.equationOfCenter;
        double lambda = (M + 102.9373d + C + 180.0d) % 360;
        this.eclipticLongitude = lambda;
    }

    private void calcRightAscension() {
        double lambda = this.eclipticLongitude;
        double alpha = toDegrees( atan2( sin(toRadians(lambda)) * cos(toRadians(23.4393d)), cos(toRadians(lambda)) ) );
        this.rightAscension = alpha;
    }

    private void calcDeclinationOfTheSun() {
        double lambda = this.eclipticLongitude;
        double delta = toDegrees( asin( sin(toRadians(lambda)) * sin(toRadians(23.4393d)) ) );
        this.declinationOfTheSun = delta;
    }

    private void calcSiderealTime() {
        double J = this.julianDayNumber;
        double Lw = this.observerLongitude;
        double theta = (280.1470d + 360.9856235d * (J - J2000) - Lw) % 360;
        this.siderealTime = theta;
    }

    private void calcSolarTransit() {
        double J = this.julianDayNumber;
        double Lw = this.observerLongitude;
        double M = this.meanAnomaly;
        double lambda = this.eclipticLongitude;
        double n = J - J2000 + 0.0009d;
        double Jstar = n - Lw / 360d;
        double Jtransit = J2000 + Jstar + 0.0053d * sin(toRadians(M)) - 0.0068d * sin(toRadians(2*lambda));
        this.solarTransit = Jtransit;
    }

    private void calcHourAngle() {
        double delta = this.declinationOfTheSun;
        double phi = this.observerLatitude;
        double H = toDegrees( acos(
                    (sin(toRadians(-0.83d)) - sin(toRadians(phi)) * sin(toRadians(delta)) ) /
                    (cos(toRadians(phi)) * cos(toRadians(delta)) )
                ) );
        this.hourAngle = H;
    }

    private void calcSunriseSunsetJulianDay() {
        double Jtransit = this.solarTransit;
        double H = this.hourAngle;
        this.sunriseJulianDay = Jtransit - (H / 360d);
        this.sunsetJulianDay = Jtransit + (H / 360d);
    }

    // public methods
    public boolean init() {
        this.calcMeanAnomaly();
        this.calcEquationOfCenter();
        this.calcEclipticLongitude();
        this.calcRightAscension();
        this.calcDeclinationOfTheSun();
        this.calcSiderealTime();
        this.calcSolarTransit();
        this.calcHourAngle();
        this.calcSunriseSunsetJulianDay();
        return true;
    }

    public double getSiderealTime() {
        return this.siderealTime;
    }

    public double getSunriseJulianDay() {
        return this.sunriseJulianDay;
    }

    public double getSunsetJulianDay() {
        return this.sunsetJulianDay;
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
