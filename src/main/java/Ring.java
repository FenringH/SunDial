import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;


public class Ring extends Group {

    private static final int DIVISIONS = 256;

    private double longitude = 0;
    private double latitude = 0;

    private Group ring;
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
        this(radius, height, Color.WHITE);
    }

    public Ring(double radius, double height, Color color) {
        super();
        ring = new Group();
        rotateLongitudeTimeline = new Timeline();
        rotateLatitudeTimeline = new Timeline();
        getChildren().add(getRing(color, radius, height));
    }

    private Group getRing(Color color, double radius, double height) {

        ring = new Group();

        rotateTilt = new Rotate();
        rotatePhase = new Rotate();

        rotateTilt.setAxis(Rotate.Z_AXIS);
        rotatePhase.setAxis(Rotate.Y_AXIS);

        rotateLongitude = new Rotate();
        rotateLatitude = new Rotate();

        rotateLongitude.setAxis(Rotate.Y_AXIS);
        rotateLatitude.setAxis(Rotate.X_AXIS);

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

        this.ring.getChildren().addAll(globeHolder);

        return ring;
    }

    public void rotateRing(double longitude, double latitude, int duration) {

        this.longitude = longitude;
        this.latitude = latitude;

        if (rotateLongitudeTimeline != null) { rotateLongitudeTimeline.stop(); }
        if (rotateLatitudeTimeline != null) { rotateLatitudeTimeline.stop(); }

        if (duration < 1) {
            rotateLongitude.setAngle(this.longitude);
            rotateLatitude.setAngle(this.latitude);
            return;
        }

        rotateLongitudeTimeline = new Timeline();
        rotateLongitudeTimeline.setCycleCount(1);
        rotateLongitudeTimeline.setRate(1);
        rotateLongitudeTimeline.setAutoReverse(false);

        KeyValue keyValueLongitude = new KeyValue(this.rotateLongitude.angleProperty(), this.longitude, Interpolator.EASE_BOTH);
        KeyFrame keyFrameLongitude = new KeyFrame(Duration.millis(duration), keyValueLongitude);

        rotateLongitudeTimeline.getKeyFrames().add(keyFrameLongitude);


        rotateLatitudeTimeline = new Timeline();
        rotateLatitudeTimeline.setCycleCount(1);
        rotateLatitudeTimeline.setRate(1);
        rotateLatitudeTimeline.setAutoReverse(false);

        KeyValue keyValueLatitude = new KeyValue(rotateLatitude.angleProperty(), this.latitude, Interpolator.EASE_BOTH);
        KeyFrame keyFrameLatitude = new KeyFrame(Duration.millis(duration), keyValueLatitude);

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
