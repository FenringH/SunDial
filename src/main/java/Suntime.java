import static java.lang.Math.*;

public class Suntime {

    // constants
    private static double DEFAULT_LONGITUDE = 45.7830997;
    private static double DEFAULT_LATITUDE = 15.9788553;
    private static double J2000 = 2451545.0;

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

    // Constructors
    public Suntime(double julianDayNumber) {
        this(julianDayNumber, DEFAULT_LONGITUDE, DEFAULT_LATITUDE);
    }

    public Suntime(double julianDayNumber, double observerLongitude, double observerLatitude) {
        setJulianDayNumber(julianDayNumber);
        setObserverLongitude(observerLongitude);
        setObserverLatitude(observerLatitude);
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
        double Lw = this.observerLongitude;
        double M = this.meanAnomaly;
        double lambda = this.eclipticLongitude;
        double Jtransit = J2000 + 0.0009d + Lw / 360.0d + 0.0053d * sin(toRadians(M)) - 0.0068d * sin(toRadians(2*lambda));
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
