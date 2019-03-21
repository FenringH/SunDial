import javafx.animation.*;
import javafx.beans.property.DoubleProperty;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;


public class Grid extends Group {

    private static final int DENSITY_X = 36;
    private static final int DENSITY_Y = 18;

    private static final int DIVISIONS = DENSITY_X;

    private static final int DEFAULT_ANIMATION_DURATION = 1000;

    private double longitude;
    private double latitude;

    private int animationDuration;

    private Group cylinderGroup;
    private Sphere sphere;

    private PhongMaterial ringMaterial;
    private PhongMaterial sphereMaterial;

    private Rotate rotateLongitude;
    private Rotate rotateLatitude;

    private Timeline rotateLongitudeTimeline;
    private Timeline rotateLatitudeTimeline;

    private AmbientLight ambientLight;

    private double sunTilt;
    private double sunPhase;

    private Rotate rotateTilt;
    private Rotate rotatePhase;

    public Grid(double radius, double width) {
        this(radius, width, Color.WHITE, DEFAULT_ANIMATION_DURATION);
    }

    public Grid(double radius, double width, Color color, int animationDuration) {

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

        sphereMaterial = new PhongMaterial();
        sphereMaterial.setDiffuseColor(Color.BLACK);


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

            Cylinder cylinderHwite = new Cylinder(meridianRadius, width, DIVISIONS);
            cylinderHwite.setMaterial(ringMaterial);
            cylinderHwite.setTranslateY(meridianHeight);
            cylinderHwite.setDrawMode(DrawMode.LINE);

            cylinderGroup.getChildren().addAll(cylinderHwite);
        }

        sphere = new Sphere(radius - width / 2, DIVISIONS);
        sphere.setMaterial(sphereMaterial);

        ambientLight = new AmbientLight(Color.WHITE);


        // Gyroscope
        Group ringGripper = new Group();
        ringGripper.getChildren().addAll(sphere, cylinderGroup, ambientLight);
        ringGripper.getTransforms().add(rotateTilt);

        Group ringHolder = new Group();
        ringHolder.getChildren().add(ringGripper);
        ringHolder.getTransforms().add(rotatePhase);

        Group globeGripper = new Group();
        globeGripper.getTransforms().add(rotateLongitude);
        globeGripper.getChildren().addAll(ringHolder);

        Group globeHolder = new Group();
        globeHolder.getTransforms().add(rotateLatitude);
        globeHolder.getChildren().addAll(globeGripper);

        super.getChildren().add(globeHolder);
    }


    public void rotateRing(double longitude, double latitude, boolean animated) {

        this.longitude = longitude;
        this.latitude = latitude;

        if (rotateLongitudeTimeline.getStatus().equals(Animation.Status.RUNNING)) { rotateLongitudeTimeline.stop(); }
        if (rotateLatitudeTimeline.getStatus().equals(Animation.Status.RUNNING)) { rotateLatitudeTimeline.stop(); }

        if (animated) {

            KeyValue keyValueLongitude = new KeyValue(this.rotateLongitude.angleProperty(), this.longitude, Interpolator.EASE_BOTH);
            KeyFrame keyFrameLongitude = new KeyFrame(Duration.millis(animationDuration), keyValueLongitude);

            KeyValue keyValueLatitude = new KeyValue(rotateLatitude.angleProperty(), this.latitude, Interpolator.EASE_BOTH);
            KeyFrame keyFrameLatitude = new KeyFrame(Duration.millis(animationDuration), keyValueLatitude);

            rotateLongitudeTimeline.getKeyFrames().clear();
            rotateLongitudeTimeline.getKeyFrames().add(keyFrameLongitude);

            rotateLatitudeTimeline.getKeyFrames().clear();
            rotateLatitudeTimeline.getKeyFrames().add(keyFrameLatitude);


            rotateLongitudeTimeline.play();
            rotateLatitudeTimeline.play();

        } else {
            rotateLongitude.setAngle(this.longitude);
            rotateLatitude.setAngle(this.latitude);
        }
    }

    public void setDayLightPosition(double phase, double tilt) {

        sunPhase = phase;
        sunTilt = 0d - tilt;

        rotatePhase.setAngle(sunPhase * 360 + 90);
        rotateTilt.setAngle(sunTilt);
    }

}
