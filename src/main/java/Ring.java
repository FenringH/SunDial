import javafx.animation.*;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;


public class Ring extends Group {

    private static final int DIVISIONS = 128;
    private static final int DEFAULT_ANIMATION_DURATION = 1000;

    private double longitude = 0;
    private double latitude = 0;

    private int animationDuration;

    private Cylinder cylinder;
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

    public Ring(double radius, double height) {
        this(radius, height, Color.WHITE, DEFAULT_ANIMATION_DURATION);
    }

    public Ring(double radius, double height, Color color, int animationDuration) {

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

        cylinder = new Cylinder(radius, height, DIVISIONS);
        cylinder.setMaterial(ringMaterial);
        cylinder.setRotationAxis(Rotate.Z_AXIS);
        cylinder.setRotate(90);

        sphere = new Sphere(radius - 1, DIVISIONS);
        sphere.setMaterial(sphereMaterial);

        ambientLight = new AmbientLight(Color.WHITE);


        // Gyroscope
        Group ringGripper = new Group();
        ringGripper.getChildren().addAll(sphere, cylinder, ambientLight);
        ringGripper.getTransforms().add(rotateTilt);

        Group ringHolder = new Group();
        ringHolder.getChildren().add(ringGripper);
        ringHolder.getTransforms().add(rotatePhase);

        Group globeGripper = new Group();
        globeGripper.getTransforms().add(rotateLongitude);
        globeGripper.getChildren().addAll(sphere, ringHolder);

        Group globeHolder = new Group();
        globeHolder.getTransforms().add(rotateLatitude);
        globeHolder.getChildren().addAll(globeGripper);

        super.getChildren().add(globeHolder);
    }


    public void rotateRing(double longitude, double latitude, boolean animated) {

        this.longitude = longitude;
        this.latitude = latitude;

        int animationDuration = animated ? this.animationDuration : 1;

        if (rotateLongitudeTimeline.getStatus().equals(Animation.Status.RUNNING)) { rotateLongitudeTimeline.stop(); }
        if (rotateLatitudeTimeline.getStatus().equals(Animation.Status.RUNNING)) { rotateLatitudeTimeline.stop(); }


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

    }

    public void setDayLightPosition(double phase, double tilt) {

        sunPhase = phase;
        sunTilt = 0d - tilt;

        rotatePhase.setAngle(sunPhase * 360 + 90);
        rotateTilt.setAngle(sunTilt);
    }

}
