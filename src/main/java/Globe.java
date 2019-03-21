import javafx.animation.*;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableDoubleValue;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class Globe extends Group {

    private static final int SPHERE_DIVISIONS = 128;
    private static final int NUMBER_OF_PARALLELS = 360 / 10;
    private static final int NUMBER_OF_MERIDIANS = 180 / 10;

    private static final int DEFAULT_ANIMATION_DURATION = 1000;

    private static final double MAP_WIDTH  = 8192 / 2d;
    private static final double MAP_HEIGHT = 4092 / 2d;

    public static final Color Color_Of_Void         = new Color(0.00, 0.00, 0.00, 0.00);
    public static final Color Color_Of_Light        = new Color(1.00, 1.00, 1.00, 1.00);
    public static final Color Color_Of_Transparency = new Color(1.00, 1.00, 1.00, 0.50);

    private static final String DEFAULT_DAY_MAP = "maps/earth_diffuse_gall-peters_02.jpg";
    private static final String DEFAULT_NIGHT_MAP = "maps/earth_night_v1.png";

    private static final Image DEFAULT_DAY_IMAGE = new Image(DEFAULT_DAY_MAP, 1003, 639, true, false);
    private static final Image DEFAULT_NIGHT_IMAGE = new Image(DEFAULT_NIGHT_MAP, 1003, 639, true, false);

    private double longitude;
    private double latitude;

    private int animationDuration;

    private Sphere daySphere;
    private PhongMaterial globeMaterial;

    private Image dayDiffuseMap;

    private Rotate rotateLongitude;
    private Rotate rotateLatitude;

    private Timeline rotateLongitudeTimeline;
    private Timeline rotateLatitudeTimeline;

    private PointLight dayLight;
    private PointLight nightLight;
    private AmbientLight ambientLight;

    private double sunTilt;
    private double sunPhase;

    private Rotate rotateSunTilt;
    private Rotate rotateSunPhase;

    public Globe(double radius) {
        this(DEFAULT_DAY_IMAGE, radius, DEFAULT_ANIMATION_DURATION);
    }

    public Globe(Image dayDiffuseMap, double radius, int animationDuration) {

        super();

        this.dayDiffuseMap = dayDiffuseMap;
        this.animationDuration = animationDuration;


        rotateSunTilt = new Rotate();
        rotateSunPhase = new Rotate();

        rotateSunTilt.setAxis(Rotate.X_AXIS);
        rotateSunPhase.setAxis(Rotate.Y_AXIS);

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


        dayLight = new PointLight(Color_Of_Light);
        dayLight.setTranslateZ(-100 * radius);

        nightLight = new PointLight(Color.BLACK);
        nightLight.setTranslateZ(100 * radius);

        ambientLight = new AmbientLight(Color.BLACK);

        globeMaterial = new PhongMaterial();
        globeMaterial.setDiffuseMap(dayDiffuseMap);

        daySphere = new Sphere(radius, SPHERE_DIVISIONS);
        daySphere.setMaterial(globeMaterial);


        // Gyroscope rotation system
        Group dayLightGripper = new Group();
        dayLightGripper.getChildren().addAll(dayLight, nightLight, ambientLight);
        dayLightGripper.getTransforms().add(rotateSunTilt);

        Group dayLightHolder = new Group();
        dayLightHolder.getChildren().add(dayLightGripper);
        dayLightHolder.getTransforms().add(rotateSunPhase);

        Group daySphereGripper = new Group();
        daySphereGripper.getTransforms().add(rotateLongitude);
        daySphereGripper.getChildren().addAll(daySphere, dayLightHolder);

        Group daySphereHolder = new Group();
        daySphereHolder.getTransforms().add(rotateLatitude);
        daySphereHolder.getChildren().addAll(daySphereGripper);


        super.getChildren().add(daySphereHolder);
    }

    public void rotateGlobe(double longitude, double latitude, boolean animated) {

        this.longitude = longitude;
        this.latitude = latitude;

        if (rotateLongitudeTimeline.getStatus().equals(Animation.Status.RUNNING)) { rotateLongitudeTimeline.stop(); }
        if (rotateLatitudeTimeline.getStatus().equals(Animation.Status.RUNNING)) { rotateLatitudeTimeline.stop();}

        if (animated) {

            KeyValue keyValueLongitude = new KeyValue(rotateLongitude.angleProperty(), this.longitude, Interpolator.EASE_BOTH);
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

    public void setDayDiffuseMap(Image map) {
        dayDiffuseMap = map;
        globeMaterial.setDiffuseMap(dayDiffuseMap);
    }

    public void setDayLightPosition(double phase, double tilt) {

        sunPhase = phase;
        sunTilt = -tilt;

        rotateSunPhase.setAngle(sunPhase * 360);
        rotateSunTilt.setAngle(sunTilt);
    }

    public void setDayLightColor(Color dayLightColor) {
        dayLight.setColor(dayLightColor);
    }

    public void setNightLightColor(Color nightLightColor) {
        nightLight.setColor(nightLightColor);
    }

    public void setAmbientLightColor(Color ambientLightColor) {
        ambientLight.setColor(ambientLightColor);
    }

    public Sphere getDaySphere() {
        return daySphere;
    }

    public PointLight getDayLight() {
        return dayLight;
    }

    public PointLight getNightLight() {
        return nightLight;
    }

    public Timeline getRotateLongitudeTimeline() {
        return rotateLongitudeTimeline;
    }

    public Timeline getRotateLatitudeTimeline() {
        return rotateLatitudeTimeline;
    }


}
