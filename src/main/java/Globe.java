import javafx.animation.*;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.util.Duration;

public class Globe extends Group {

    private static final int SPHERE_DIVISIONS = 128;

    private static final int DEFAULT_ANIMATION_DURATION = 1000;

    private DoubleProperty longitude;
    private DoubleProperty latitude;

    private DoubleProperty tilt;
    private DoubleProperty phase;

    private DoubleProperty lightScale;

    private int animationDuration;

    private Sphere sphere;
    private PhongMaterial globeMaterial;

    private Image dayDiffuseMap;

    private Rotate rotateLongitude;
    private Rotate rotateLatitude;

    private Timeline rotateLongitudeTimeline;
    private Timeline rotateLatitudeTimeline;

    private PointLight dayLight;
    private PointLight nightLight;
    private AmbientLight ambientLight;

    private Rotate rotateTilt;
    private Rotate rotatePhase;
    private Scale lightScaleTransform;

    public Globe(double radius) {
        this(Sunconfig.GLOBE_DAY_IMAGE, radius, DEFAULT_ANIMATION_DURATION);
    }

    public Globe(Image dayDiffuseMap, double radius, int animationDuration) {

        super();

        this.dayDiffuseMap = dayDiffuseMap;
        this.animationDuration = animationDuration;

        rotateTilt = new Rotate();
        rotatePhase = new Rotate();

        rotateTilt.setAxis(Rotate.X_AXIS);
        rotatePhase.setAxis(Rotate.Y_AXIS);

        rotateLongitude = new Rotate();
        rotateLatitude = new Rotate();

        rotateLongitude.setAxis(Rotate.Y_AXIS);
        rotateLatitude.setAxis(Rotate.X_AXIS);

        lightScaleTransform = new Scale();


        longitude = new SimpleDoubleProperty(0f);
        latitude = new SimpleDoubleProperty(0f);
        tilt = new SimpleDoubleProperty(0f);
        phase = new SimpleDoubleProperty(0f);

        longitude.addListener((observable, oldValue, newValue) -> rotateLongitude.setAngle(this.longitude.get()));
        latitude.addListener((observable, oldValue, newValue) -> rotateLatitude.setAngle(this.latitude.get()));
        tilt.addListener((observable, oldValue, newValue) -> rotateTilt.setAngle(this.tilt.get()));
        phase.addListener((observable, oldValue, newValue) -> rotatePhase.setAngle(this.phase.get()));

        lightScale = new SimpleDoubleProperty(1f);
        lightScale.addListener((observable, oldValue, newValue) -> lightScaleTransform.setZ(this.lightScale.get()));

        rotateLongitudeTimeline = new Timeline();
        rotateLongitudeTimeline.setCycleCount(1);
        rotateLongitudeTimeline.setRate(1);
        rotateLongitudeTimeline.setAutoReverse(false);

        rotateLatitudeTimeline = new Timeline();
        rotateLatitudeTimeline.setCycleCount(1);
        rotateLatitudeTimeline.setRate(1);
        rotateLatitudeTimeline.setAutoReverse(false);


        dayLight = new PointLight(Color.WHITE);
        dayLight.setTranslateZ(-100 * radius);

        nightLight = new PointLight(Color.BLACK);
        nightLight.setTranslateZ(100 * radius);

        ambientLight = new AmbientLight(Color.BLACK);

        globeMaterial = new PhongMaterial();
        globeMaterial.setDiffuseMap(dayDiffuseMap);

        sphere = new Sphere(radius, SPHERE_DIVISIONS);
        sphere.setMaterial(globeMaterial);


        // Gyroscope rotation system
        Group lightTilter = new Group(dayLight, nightLight, ambientLight);
        lightTilter.getTransforms().add(rotateTilt);

        Group lightPhaser = new Group(lightTilter);
        lightPhaser.getTransforms().add(rotatePhase);

        Group lightScaleGroup = new Group(lightPhaser);
//        lightScaleGroup.getTransforms().add(lightScaleTransform);

        Group sphereLongituder = new Group(sphere, lightScaleGroup);
        sphereLongituder.getTransforms().add(rotateLongitude);

        Group sphereLatituder = new Group(sphereLongituder);
        sphereLatituder.getTransforms().add(rotateLatitude);

        super.getChildren().addAll(sphereLatituder);
//        super.getTransforms().add(lightScaleTransform);
    }

    public void rotateGlobe(double longitude, double latitude, boolean animated) {

        this.longitude.set(longitude);
        this.latitude.set(latitude);

        if (rotateLongitudeTimeline.getStatus().equals(Animation.Status.RUNNING)) { rotateLongitudeTimeline.stop(); }
        if (rotateLatitudeTimeline.getStatus().equals(Animation.Status.RUNNING)) { rotateLatitudeTimeline.stop();}

        if (animated) {

            KeyValue keyValueLongitude = new KeyValue(rotateLongitude.angleProperty(), this.longitude.get(), Interpolator.EASE_BOTH);
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

    public void setDayDiffuseMap(Image map) {
        dayDiffuseMap = map;
        globeMaterial.setDiffuseMap(dayDiffuseMap);
    }

    public void setDayLightPosition(double phase, double tilt) {

        this.phase.set(phase);
        this.tilt.set(tilt);

        rotatePhase.setAngle(this.phase.get());
        rotateTilt.setAngle(this.tilt.get());
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

    public Sphere getSphere() {
        return sphere;
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

    public double getLightScale() {
        return lightScale.get();
    }

    public DoubleProperty lightScaleProperty() {
        return lightScale;
    }

    public Scale getLightScaleTransform() {
        return lightScaleTransform;
    }

    public void setSpecularMap(Image specularMap) {
        globeMaterial.setSpecularMap(specularMap);
    }

    public void setSpecularColor(Color color) {
        globeMaterial.setSpecularColor(color);
    }

    public void setSpecularPower(double power) {
        globeMaterial.setSpecularPower(power);
    }
}
