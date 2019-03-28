import javafx.animation.*;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;


public class GlobeGrid extends Group {

    private static final int DENSITY_X = 36;
    private static final int DENSITY_Y = 18;

    private static final int DIVISIONS = 128;

    private static final int DEFAULT_ANIMATION_DURATION = 1000;

    private DoubleProperty longitude;
    private DoubleProperty latitude;

    private DoubleProperty tilt;
    private DoubleProperty phase;

    private int animationDuration;

    private Group cylinderGroup;
    private Sphere sphere;

    private PhongMaterial ringMaterial;
    private PhongMaterial darkMaterial;

    private Rotate rotateLongitude;
    private Rotate rotateLatitude;

    private Timeline rotateLongitudeTimeline;
    private Timeline rotateLatitudeTimeline;

    private AmbientLight ambientLight;

    private Rotate rotateTilt;
    private Rotate rotatePhase;

    public GlobeGrid(double radius, double width) {
        this(radius, width, Color.WHITE, DEFAULT_ANIMATION_DURATION);
    }

    public GlobeGrid(double radius, double width, Color color, int animationDuration) {

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

        darkMaterial = new PhongMaterial(Color.BLACK);


        cylinderGroup = new Group();

        for (int i = 0; i < DENSITY_X; i++) {

            Cylinder cylinder = new Cylinder(radius, width, DIVISIONS);
            cylinder.setMaterial(ringMaterial);
            cylinder.setRotationAxis(Rotate.Z_AXIS);
            cylinder.setRotate(90);

            Group cylinderHolder = new Group(cylinder);
            cylinderHolder.setRotationAxis(Rotate.Y_AXIS);
            cylinderHolder.setRotate(i * (360d / DENSITY_X));

            cylinderGroup.getChildren().addAll(cylinderHolder);
        }

        for (int i = 0; i < DENSITY_Y; i++) {

            double angle = Math.toRadians((i * (180d / DENSITY_Y)) - 90);
            double meridianRadius = radius * Math.cos(angle);
            double meridianHeight = radius * Math.sin(angle);

            Cylinder lightCylinder = new Cylinder(meridianRadius, width, DIVISIONS);
            lightCylinder.setMaterial(ringMaterial);
            lightCylinder.setTranslateY(meridianHeight);

            Cylinder darkCylinder = new Cylinder(meridianRadius - width, width * 1.01, DIVISIONS);
            darkCylinder.setMaterial(darkMaterial);
            darkCylinder.setTranslateY(meridianHeight);

            cylinderGroup.getChildren().addAll(darkCylinder, lightCylinder);
        }

        sphere = new Sphere(radius - width / 2, DIVISIONS);
        sphere.setMaterial(darkMaterial);

        ambientLight = new AmbientLight(Color.WHITE);


        // Gyroscope
        Group gridTilter = new Group(sphere, cylinderGroup, ambientLight);
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
