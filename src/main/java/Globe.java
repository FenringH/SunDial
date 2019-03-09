import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class Globe extends Group {

    private static final int SPHERE_DIVISIONS = 256;
    private static final int NUMBER_OF_PARALLELS = 360 / 10;
    private static final int NUMBER_OF_MERIDIANS = 180 / 10;

    private static final double MAP_WIDTH  = 8192 / 2d;
    private static final double MAP_HEIGHT = 4092 / 2d;

    public static final Color Color_Of_Void         = new Color(0.00, 0.00, 0.00, 0.00);
    public static final Color Color_Of_Light        = new Color(1.00, 1.00, 1.00, 1.00);
    public static final Color Color_Of_Transparency = new Color(1.00, 1.00, 1.00, 0.50);

    private static final String DEFAULT_DAY_MAP = "maps/earth_diffuse_gall-peters_02.jpg";
    private static final String DEFAULT_NIGHT_MAP = "maps/earth_night_v1.png";

    private static final Image DEFAULT_DAY_IMAGE = new Image(DEFAULT_DAY_MAP, 1003, 639, true, false);
    private static final Image DEFAULT_NIGHT_IMAGE = new Image(DEFAULT_NIGHT_MAP, 1003, 639, true, false);

    private double longitude = 0;
    private double latitude = 0;

    private Group globe;
    private Sphere daySphere;
    private Sphere nightSphere;
    private PhongMaterial globeMaterial;

    private Image dayDiffuseMap;
    private Image nightDiffuseMap;

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
        this(DEFAULT_DAY_IMAGE, radius);
    }

    public Globe(Image dayDiffuseMap, double radius) {
        super();
        this.globe = new Group();
        this.dayDiffuseMap = dayDiffuseMap;
        this.nightDiffuseMap = nightDiffuseMap;
        this.rotateLongitudeTimeline = new Timeline();
        this.rotateLatitudeTimeline = new Timeline();
        getChildren().add(getGlobe(radius));
    }

    private Group getGlobe(double radius) {

        this.globe = new Group();

        this.rotateLongitude = new Rotate();
        this.rotateLatitude = new Rotate();

        this.rotateLongitude.setAxis(Rotate.Y_AXIS);
        this.rotateLatitude.setAxis(Rotate.X_AXIS);

        this.rotateSunTilt = new Rotate();
        this.rotateSunPhase = new Rotate();

        this.rotateSunTilt.setAxis(Rotate.X_AXIS);
        this.rotateSunPhase.setAxis(Rotate.Y_AXIS);

        dayLight = new PointLight(Color_Of_Light);
        dayLight.setTranslateZ(-100000);

        nightLight = new PointLight(Color.BLACK);
        nightLight.setTranslateZ(100000);

        ambientLight = new AmbientLight(Color.BLACK);

        Group dayLightGripper = new Group();
        dayLightGripper.getChildren().addAll(dayLight, nightLight, ambientLight);
        dayLightGripper.getTransforms().add(rotateSunPhase);

        Group dayLightHolder = new Group();
        dayLightHolder.getChildren().add(dayLightGripper);
        dayLightHolder.getTransforms().add(rotateSunTilt);

        globeMaterial = new PhongMaterial();
        globeMaterial.setDiffuseMap(dayDiffuseMap);

        daySphere = new Sphere(radius, SPHERE_DIVISIONS);
        daySphere.setMaterial(globeMaterial);

        Group daySphereGripper = new Group();
        daySphereGripper.getTransforms().add(this.rotateLongitude);
        daySphereGripper.getChildren().addAll(daySphere, dayLightHolder);

        Group daySphereHolder = new Group();
        daySphereHolder.getTransforms().add(this.rotateLatitude);
        daySphereHolder.getChildren().addAll(daySphereGripper);

        this.globe.getChildren().addAll(daySphereHolder);

        return globe;
    }

    public void rotateGlobe(double longitude, double latitude, int duration) {

        this.longitude = longitude;
        this.latitude = latitude;

        if (rotateLongitudeTimeline != null) { rotateLongitudeTimeline.stop(); }
        if (rotateLatitudeTimeline != null) { rotateLatitudeTimeline.stop(); }

        if (duration < 1) {
            this.rotateLongitude.setAngle(this.longitude);
            this.rotateLatitude.setAngle(this.latitude);
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

        KeyValue keyValueLatitude = new KeyValue(this.rotateLatitude.angleProperty(), this.latitude, Interpolator.EASE_BOTH);
        KeyFrame keyFrameLatitude = new KeyFrame(Duration.millis(duration), keyValueLatitude);

        rotateLatitudeTimeline.getKeyFrames().add(keyFrameLatitude);


        rotateLongitudeTimeline.play();
        rotateLatitudeTimeline.play();

    }

    public void setDayDiffuseMap(Image map) {
        dayDiffuseMap = map;
        globeMaterial.setDiffuseMap(dayDiffuseMap);
    }

    public void setDayLightPosition(double phase, double tilt) {
        sunPhase = phase;
        sunTilt = -1 * tilt;

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

}
