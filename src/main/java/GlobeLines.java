import javafx.animation.*;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import static java.lang.Math.*;


public class GlobeLines extends Group {

    private static final int DIVISIONS = 128;

    private static final int DEFAULT_ANIMATION_DURATION = 1000;

    private static final double TROPIC_LINE_ANGLE = 23.26d;
    private static final double POLAR_LINE_ANGLE = 66.33d;

    private DoubleProperty longitude;
    private DoubleProperty latitude;

    private DoubleProperty tilt;
    private DoubleProperty phase;

    private int animationDuration;

    private Group cylinderGroup;
    private Sphere sphere;

    private PhongMaterial ringMaterial;

    private Rotate rotateLongitude;
    private Rotate rotateLatitude;

    private Timeline rotateLongitudeTimeline;
    private Timeline rotateLatitudeTimeline;

    private AmbientLight ambientLight;

    private Rotate rotateTilt;
    private Rotate rotatePhase;

    public GlobeLines(double radius, double width) {
        this(radius, width, Color.WHITE, DEFAULT_ANIMATION_DURATION);
    }

    public GlobeLines(double radius, double width, Color color, int animationDuration) {

        super();

        this.animationDuration = animationDuration;

        rotateTilt = new Rotate();
        rotatePhase = new Rotate();

        rotateTilt.setAxis(Rotate.Z_AXIS);
        rotatePhase.setAxis(Rotate.Y_AXIS);

        rotateLongitude = new Rotate();
        rotateLatitude = new Rotate();

        rotateLongitude.setAxis(Rotate.Y_AXIS);
        rotateLatitude.setAxis(Rotate.X_AXIS);

        longitude = new SimpleDoubleProperty(0f);
        latitude = new SimpleDoubleProperty( 0f);
        tilt = new SimpleDoubleProperty(0f);
        phase = new SimpleDoubleProperty(0f);

        longitude.addListener((observable, oldValue, newValue) -> rotateLongitude.setAngle(longitude.get()));
        latitude.addListener((observable, oldValue, newValue) -> rotateLatitude.setAngle(latitude.get()));
        tilt.addListener((observable, oldValue, newValue) -> rotateTilt.setAngle(tilt.get()));
        phase.addListener((observable, oldValue, newValue) -> rotatePhase.setAngle(phase.get()));

        rotateLongitudeTimeline = new Timeline();
        rotateLongitudeTimeline.setCycleCount(1);
        rotateLongitudeTimeline.setRate(1);
        rotateLongitudeTimeline.setAutoReverse(false);

        rotateLatitudeTimeline = new Timeline();
        rotateLatitudeTimeline.setCycleCount(1);
        rotateLatitudeTimeline.setRate(1);
        rotateLatitudeTimeline.setAutoReverse(false);

        ringMaterial = new PhongMaterial();
        ringMaterial.setDiffuseColor(color);

        PhongMaterial equatorMaterial = new PhongMaterial(Color.WHITE);
        PhongMaterial tropicMaterial = new PhongMaterial(new Color(1.00, 0.35, 0.10, 1.00));
        PhongMaterial polarMaterial = new PhongMaterial(new Color(0.25, 0.65, 1.00, 1.00));
        PhongMaterial darkMaterial = new PhongMaterial(Color.BLACK);

        Cylinder equatorCylinder = new Cylinder(radius * cos(toRadians(0)), width, DIVISIONS);
        equatorCylinder.setMaterial(equatorMaterial);
        equatorCylinder.setTranslateY(radius * sin(toRadians(0)));

        Cylinder tropicCylinderN = new Cylinder(radius * cos(toRadians(TROPIC_LINE_ANGLE)), width, DIVISIONS);
        tropicCylinderN.setMaterial(tropicMaterial);
        tropicCylinderN.setTranslateY(radius * sin(toRadians(TROPIC_LINE_ANGLE)));

        Cylinder tropicCylinderS = new Cylinder(radius * cos(toRadians(-TROPIC_LINE_ANGLE)), width, DIVISIONS);
        tropicCylinderS.setMaterial(tropicMaterial);
        tropicCylinderS.setTranslateY(radius * sin(toRadians(-TROPIC_LINE_ANGLE)));

        Cylinder polarCylinderN = new Cylinder(radius * cos(toRadians(POLAR_LINE_ANGLE)), width, DIVISIONS);
        polarCylinderN.setMaterial(polarMaterial);
        polarCylinderN.setTranslateY(radius * sin(toRadians(POLAR_LINE_ANGLE)));

        Cylinder polarCylinderS = new Cylinder(radius * cos(toRadians(-POLAR_LINE_ANGLE)), width, DIVISIONS);
        polarCylinderS.setMaterial(polarMaterial);
        polarCylinderS.setTranslateY(radius * sin(toRadians(-POLAR_LINE_ANGLE)));

        cylinderGroup = new Group(equatorCylinder, tropicCylinderN, tropicCylinderS, polarCylinderN, polarCylinderS);


        Cylinder darkEquatorCylinder = new Cylinder(radius * cos(toRadians(0)) - width, width + 1, DIVISIONS);
        darkEquatorCylinder.setMaterial(darkMaterial);
        darkEquatorCylinder.setTranslateY(radius * sin(toRadians(0)));

        Cylinder darkTropicCylinderN = new Cylinder(radius * cos(toRadians(TROPIC_LINE_ANGLE)) - width, width + 1, DIVISIONS);
        darkTropicCylinderN.setMaterial(darkMaterial);
        darkTropicCylinderN.setTranslateY(radius * sin(toRadians(TROPIC_LINE_ANGLE)));

        Cylinder darkTropicCylinderS = new Cylinder(radius * cos(toRadians(-TROPIC_LINE_ANGLE)) - width, width + 1, DIVISIONS);
        darkTropicCylinderS.setMaterial(darkMaterial);
        darkTropicCylinderS.setTranslateY(radius * sin(toRadians(-TROPIC_LINE_ANGLE)));

        Cylinder darkPolarCylinderN = new Cylinder(radius * cos(toRadians(POLAR_LINE_ANGLE)) - width, width + 1, DIVISIONS);
        darkPolarCylinderN.setMaterial(darkMaterial);
        darkPolarCylinderN.setTranslateY(radius * sin(toRadians(POLAR_LINE_ANGLE)));

        Cylinder darkPolarCylinderS = new Cylinder(radius * cos(toRadians(-POLAR_LINE_ANGLE)) - width, width + 1, DIVISIONS);
        darkPolarCylinderS.setMaterial(darkMaterial);
        darkPolarCylinderS.setTranslateY(radius * sin(toRadians(-POLAR_LINE_ANGLE)));

        Group darkCylinderGroup = new Group(darkEquatorCylinder, darkTropicCylinderN, darkTropicCylinderS, darkPolarCylinderN, darkPolarCylinderS);

        sphere = new Sphere(radius - width, DIVISIONS);
        sphere.setMaterial(darkMaterial);

        ambientLight = new AmbientLight(Color.WHITE);

        // Gyroscope
        Group gridTilter = new Group(sphere, darkCylinderGroup, cylinderGroup, ambientLight);
        gridTilter.getTransforms().add(rotateTilt);

        Group gridPhaser = new Group(gridTilter);
        gridPhaser.getTransforms().add(rotatePhase);

        Group gridLongituder = new Group(gridPhaser);
        gridLongituder.getTransforms().add(rotateLongitude);

        Group gridLatituder = new Group(gridLongituder);
        gridLatituder.getTransforms().add(rotateLatitude);

        super.getChildren().add(gridLatituder);
    }


    public void rotateRing(double longitude, double latitude, boolean animated) {

        this.longitude.set(longitude);
        this.latitude.set(latitude);

        if (rotateLongitudeTimeline.getStatus().equals(Animation.Status.RUNNING)) { rotateLongitudeTimeline.stop(); }
        if (rotateLatitudeTimeline.getStatus().equals(Animation.Status.RUNNING)) { rotateLatitudeTimeline.stop(); }

        if (animated) {

            KeyValue keyValueLongitude = new KeyValue(this.rotateLongitude.angleProperty(), this.longitude.get(), Interpolator.EASE_BOTH);
            KeyFrame keyFrameLongitude = new KeyFrame(Duration.millis(animationDuration), keyValueLongitude);

            KeyValue keyValueLatitude = new KeyValue(rotateLatitude.angleProperty(), this.latitude.get(), Interpolator.EASE_BOTH);
            KeyFrame keyFrameLatitude = new KeyFrame(Duration.millis(animationDuration), keyValueLatitude);

            rotateLongitudeTimeline.getKeyFrames().clear();
            rotateLongitudeTimeline.getKeyFrames().add(keyFrameLongitude);

            rotateLatitudeTimeline.getKeyFrames().clear();
            rotateLatitudeTimeline.getKeyFrames().add(keyFrameLatitude);


            rotateLongitudeTimeline.play();
            rotateLatitudeTimeline.play();

        } else {
            rotateLongitude.setAngle(this.longitude.get());
            rotateLatitude.setAngle(this.latitude.get());
        }
    }

    public void setDayLightPosition(double phase, double tilt) {

        this.phase.set(phase);
        this.tilt.set(tilt);

        rotatePhase.setAngle(this.phase.get());
        rotateTilt.setAngle(this.tilt.get());
    }

    public double getLongitude() {
        return longitude.get();
    }

    public DoubleProperty longitudeProperty() {
        return longitude;
    }

    public double getLatitude() {
        return latitude.get();
    }

    public DoubleProperty latitudeProperty() {
        return latitude;
    }

    public double getTilt() {
        return tilt.get();
    }

    public DoubleProperty tiltProperty() {
        return tilt;
    }

    public double getPhase() {
        return phase.get();
    }

    public DoubleProperty phaseProperty() {
        return phase;
    }
}
