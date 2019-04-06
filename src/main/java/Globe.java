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

import static java.lang.Math.*;

public class Globe extends Group {

    private static final int SPHERE_DIVISIONS = 128;

    private static final int DEFAULT_ANIMATION_DURATION = 1000;

    private int animationDuration;

    private double radius;
    private double lightDistance;
    private Sphere sphere;
    private PhongMaterial globeMaterial;

    private Color dayLightColor;
    private Color nightLightColor;
    private Color ambientLightColor;

    private Color dayReverseLightColor;
    private double specularPower;
    private double reverseSpecularPower;

    private Image dayDiffuseMap;
    private Image daySpecularMap;

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

    private DoubleProperty longitude;
    private DoubleProperty latitude;

    private DoubleProperty tilt;
    private DoubleProperty phase;

    private DoubleProperty lightScale;

    public Globe(double radius) {
        this(Sunconfig.GLOBE_DAY_IMAGE, radius, DEFAULT_ANIMATION_DURATION);
    }

    public Globe(Image dayDiffuseMap, double radius, int animationDuration) {

        super();

        this.radius = radius;
        this.dayDiffuseMap = dayDiffuseMap;
        this.animationDuration = animationDuration;
        this.flipEh = false;

        lightDistance = this.radius * 100;

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

        longitude.addListener((observable, oldValue, newValue) -> setRotations(longitude, latitude, phase, tilt));
        latitude.addListener((observable, oldValue, newValue) -> setRotations(longitude, latitude, phase, tilt));
        tilt.addListener((observable, oldValue, newValue) -> setRotations(longitude, latitude, phase, tilt));
        phase.addListener((observable, oldValue, newValue) -> setRotations(longitude, latitude, phase, tilt));

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
        dayLight.setTranslateZ(-lightDistance);

        nightLight = new PointLight(Color.BLACK);
        nightLight.setTranslateZ(lightDistance);

        ambientLight = new AmbientLight(Color.BLACK);

        globeMaterial = new PhongMaterial();
        globeMaterial.setDiffuseMap(dayDiffuseMap);

        sphere = new Sphere(this.radius, SPHERE_DIVISIONS);
        sphere.setMaterial(globeMaterial);
        sphere.setRotationAxis(Rotate.Y_AXIS);


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

    private void setRotations(DoubleProperty longitude, DoubleProperty latitude, DoubleProperty phase, DoubleProperty tilt) {

        rotateLongitude.setAngle(longitude.get());
        rotateLatitude.setAngle(latitude.get());
        rotatePhase.setAngle(phase.get());
        rotateTilt.setAngle(tilt.get());

        if (dayReverseLightColor == null) { return; }

        double dayLightSceneX = dayLight.getLocalToSceneTransform().getTx();
        double dayLightSceneY = dayLight.getLocalToSceneTransform().getTy();
        double dayLightSceneZ = dayLight.getLocalToSceneTransform().getTz();

        if (dayLightSceneZ > 0) {

            double changeFactor = pow((sqrt(pow(dayLightSceneX, 2) + pow(dayLightSceneY, 2)) / lightDistance), 0.5);
            if (changeFactor > 1) { changeFactor = 1; }

            if (changeFactor == 0) {
                dayLight.setColor(dayReverseLightColor);
            } else {

                double r = dayLightColor.getRed() * changeFactor + dayReverseLightColor.getRed() * (1 - changeFactor);
                double g = dayLightColor.getGreen() * changeFactor + dayReverseLightColor.getGreen() * (1 - changeFactor);
                double b = dayLightColor.getBlue() * changeFactor + dayReverseLightColor.getBlue() * (1 - changeFactor);
                double a = dayLightColor.getOpacity() * changeFactor + dayReverseLightColor.getOpacity() * (1 - changeFactor);

                Color intermediateColor = new Color(r, g, b, a);
                double intermidiateSpecularPower = specularPower * changeFactor + reverseSpecularPower * (1 - changeFactor);

                dayLight.setColor(intermediateColor);
                globeMaterial.setSpecularPower(intermidiateSpecularPower);
            }
        } else {
            dayLight.setColor(dayLightColor);
            globeMaterial.setSpecularPower(specularPower);
        }
    }

    // PUBLIC

    public void setDayDiffuseMap(Image map) {
        dayDiffuseMap = map;
        globeMaterial.setDiffuseMap(dayDiffuseMap);
    }

    public void setDayLightColor(Color dayLightColor) {
        this.dayLightColor = dayLightColor;
        dayLight.setColor(this.dayLightColor);
    }

    public void setNightLightColor(Color nightLightColor) {
        this. nightLightColor = nightLightColor;
        nightLight.setColor(this.nightLightColor);
    }

    public void setAmbientLightColor(Color ambientLightColor) {
        this.ambientLightColor = ambientLightColor;
        ambientLight.setColor(this.ambientLightColor);
    }

    public void setDayReverseLightColor(Color dayReverseLightColor) {
        this.dayReverseLightColor = dayReverseLightColor;
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
        this.daySpecularMap = specularMap;
        globeMaterial.setSpecularMap(specularMap);
    }

    public void setSpecularColor(Color color) {
        globeMaterial.setSpecularColor(color);
    }

    public void setSpecularPower(double power) {
        this.specularPower = power;
        globeMaterial.setSpecularPower(power);
    }

    public void setReverseSpecularPower(double power) {
        this.reverseSpecularPower = power;
    }

}
