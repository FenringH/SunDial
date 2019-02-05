import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;

public class Sundial {

    // DEFAULTS
    private static double DEFAULT_sunTimeDialAngle      = 0;
    private static double DEFAULT_highNoonDialAngle     = 10;
    private static double DEFAULT_sunriseDialAngle      = 20;
    private static double DEFAULT_sunsetDialAngle       = 30;
    private static double DEFAULT_localTimeDialAngle    = 40;
    private static String DEFAULT_localTimeText         = "MMM DDD dd hh:mm:ss ZZZ YYYY";

    private static double dialWidth         = 200;
    private static double dialHeight        = 200;
    private static double globalScaleX      = 2.0;
    private static double globalScaleY      = 2.0;
    private static double dialCenterX       = dialWidth / 2;
    private static double dialCenterY       = dialHeight / 2;

    private static Color Color_Of_Window    = new Color(0.65, 0.85, 0.85, 1.00);
    private static Color Color_Of_Earth     = new Color(0.85, 0.85, 0.65, 1.00);
    private static Color Color_Of_Darkness  = new Color(0.00, 0.00, 0.00, 1.00);
    private static Color Color_Of_TextBack  = new Color(0.90, 0.90, 0.50, 1.00);
    private static Color Color_Of_Void      = new Color(0.00, 0.00, 0.00, 0.00);

    private static Color Color_Of_DaySky    = new Color(0.35, 0.75, 1.00, 1.00);
    private static Color Color_Of_NightSky  = new Color(0.30, 0.20, 1.00, 1.00);
    private static Color Color_Of_Midnight  = new Color(0.00, 0.00, 0.00, 0.35);

    private static Color Color_Of_SunTime   = new Color(1.00, 0.50, 0.00, 1.00);
    private static Color Color_Of_HighNoon  = new Color(1.00, 1.00, 0.00, 1.00);
    private static Color Color_Of_SunRise   = new Color(1.00, 0.00, 0.00, 1.00);
    private static Color Color_Of_SunSet    = new Color(0.65, 0.00, 0.65, 1.00);
    private static Color Color_Of_LocalTime = new Color(1.00, 1.00, 1.00, 1.00);

    private static Font Font_Of_Info = new Font("Lucida Console", 14);
    private static Font Font_Of_Dial = new Font("Lucida Console", 8);

    private static String Path_Of_Earth = "M 100 100 L 300 100 L 200 300 Z M 150 150 L 100 250 L 350 150 Z";

    // variables
    private double sunTimeDialAngle;
    private double highNoonDialAngle;
    private double sunriseDialAngle;
    private double sunsetDialAngle;
    private double localTimeDialAngle;
    private String localTimeText;

    // graphical primitives
    private Group dialsGroup;

    private Rectangle dialBox;

    private Rotate centerRotate;
    private Rotate sunTimeDialRotate;
    private Rotate highNoonDialRotate;
    private Rotate sunriseDialRotate;
    private Rotate sunsetDialRotate;
    private Rotate localTimeDialRotate;

    private Arc dialArcNight;
    private Arc dialArcMidnight;

    private Circle dialCircleBackground;
    private Circle dialCircleFrame;
    private Circle dialCircleCenterDot;

    private Line sunTimeDial;
    private Line highNoonDial;
    private Line sunriseDial;
    private Line sunsetDial;
    private Line localTimeDial;

    private Text dialTextDate;

    private SVGPath pathOfEarth;

    // Constructor
    public Sundial(Builder builder) {
        this.sunTimeDialAngle = builder.sunTimeDialAngle;
        this.highNoonDialAngle = builder.highNoonDialAngle;
        this.sunriseDialAngle = builder.sunriseDialAngle;
        this.sunsetDialAngle = builder.sunsetDialAngle;
        this.localTimeDialAngle = builder.localTimeDialAngle;
        this.localTimeText = builder.localTimeText;
        this.init();
    }

    // Builder
    public static class Builder {
        private double sunTimeDialAngle;
        private double highNoonDialAngle;
        private double sunriseDialAngle;
        private double sunsetDialAngle;
        private double localTimeDialAngle;
        private String localTimeText;

        public Builder() {
            this.sunTimeDialAngle = DEFAULT_sunTimeDialAngle;
            this.highNoonDialAngle = DEFAULT_highNoonDialAngle;
            this.sunriseDialAngle = DEFAULT_sunriseDialAngle;
            this.sunTimeDialAngle = DEFAULT_sunsetDialAngle;
            this.localTimeDialAngle = DEFAULT_localTimeDialAngle;
            this.localTimeText = DEFAULT_localTimeText;
        }

        public Builder sunTimeDialAngle(double sunTimeDialAngle) {
            this.sunTimeDialAngle = sunTimeDialAngle;
            return this;
        }

        public Builder highNoonDialAngle(double highNoonDialAngle) {
            this.highNoonDialAngle = highNoonDialAngle;
            return this;
        }

        public Builder sunriseDialAngle(double sunriseDialAngle) {
            this.sunriseDialAngle = sunriseDialAngle;
            return this;
        }

        public Builder sunsetDialAngle(double sunsetDialAngle) {
            this.sunsetDialAngle = sunsetDialAngle;
            return this;
        }

        public Builder localTimeDialAngle(double localTimeDialAngle) {
            this.localTimeDialAngle = localTimeDialAngle;
            return this;
        }

        public Builder localTimeText(String localTimeText) {
            this.localTimeText = localTimeText;
            return this;
        }

        public Sundial build() {
            return new Sundial(this);
        }
    }

    // stuff
    private void init() {

        centerRotate = new Rotate();
        centerRotate.setPivotX(dialCenterX);
        centerRotate.setPivotY(dialCenterY);

        sunTimeDialRotate = centerRotate.clone();
        highNoonDialRotate = centerRotate.clone();
        sunriseDialRotate = centerRotate.clone();
        sunsetDialRotate = centerRotate.clone();
        localTimeDialRotate = centerRotate.clone();

        sunTimeDialRotate.setAngle(sunTimeDialAngle);
        highNoonDialRotate.setAngle(highNoonDialAngle);
        sunriseDialRotate.setAngle(sunriseDialAngle);
        sunsetDialRotate.setAngle(sunsetDialAngle);
        localTimeDialRotate.setAngle(localTimeDialAngle);

        pathOfEarth = new SVGPath();
        pathOfEarth.setContent(Path_Of_Earth);
        pathOfEarth.setStroke(Color_Of_Darkness);
        pathOfEarth.setFill(Color_Of_Earth);

        // Dials in a box
        dialBox = new Rectangle(dialWidth, dialHeight);
        dialBox.setFill(Color_Of_Darkness);
        dialBox.setOpacity(0.1d);

        dialArcNight = new Arc(dialCenterX, dialCenterY, dialWidth / 2, dialHeight / 2, 90 - sunsetDialAngle, sunsetDialAngle - sunriseDialAngle);
        dialArcNight.setType(ArcType.ROUND);
        dialArcNight.setStroke(Color_Of_Void);
        dialArcNight.setFill(Color_Of_NightSky);

        dialArcMidnight = new Arc(dialCenterX, dialCenterY, dialWidth / 2, dialHeight / 2, 0, -180);
        dialArcMidnight.setType(ArcType.ROUND);
        dialArcMidnight.setStroke(Color_Of_Void);
        dialArcMidnight.setFill(Color_Of_Midnight);

        dialCircleBackground = new Circle(dialCenterX, dialCenterY, 100);
        dialCircleBackground.setFill(Color_Of_DaySky);
        dialCircleBackground.setStroke(Color_Of_Void);

        dialCircleFrame = new Circle(dialCenterX, dialCenterY, 100);
        dialCircleFrame.setFill(Color_Of_Void);
        dialCircleFrame.setStroke(Color_Of_Darkness);

        dialCircleCenterDot = new Circle(dialCenterX, dialCenterY, 5);
        dialCircleCenterDot.setFill(Color_Of_LocalTime);
        dialCircleCenterDot.setStroke(Color_Of_Void);

        sunTimeDial = new Line(dialCenterX, 50, dialCenterX, 0);
        sunTimeDial.setStroke(Color_Of_SunTime);
        sunTimeDial.getTransforms().add(sunTimeDialRotate);

        highNoonDial = new Line(dialCenterX, 30, dialCenterX, 0);
        highNoonDial.setStroke(Color_Of_HighNoon);
        highNoonDial.getTransforms().add(highNoonDialRotate);

        sunriseDial = new Line(dialCenterX, 100, dialCenterX, 0);
        sunriseDial.setStroke(Color_Of_SunRise);
        sunriseDial.getTransforms().add(sunriseDialRotate);

        sunsetDial = new Line(dialCenterX, 100, dialCenterX, 0);
        sunsetDial.setStroke(Color_Of_SunRise);
        sunsetDial.getTransforms().add(sunsetDialRotate);

        localTimeDial = new Line(dialCenterX, 110, dialCenterX, 0);
        localTimeDial.setStroke(Color_Of_LocalTime);
        localTimeDial.getTransforms().add(localTimeDialRotate);

        dialTextDate = new Text();
        dialTextDate.setText(localTimeText);
        dialTextDate.setFill(Color_Of_LocalTime);
        dialTextDate.setFont(Font_Of_Dial);
        dialTextDate.setLayoutX(dialCenterX - dialTextDate.getLayoutBounds().getWidth() / 2);
        dialTextDate.setLayoutY(dialCenterY * 1.5d - dialTextDate.getLayoutBounds().getHeight() / 2);

        // Add layers
        dialsGroup = new Group();

        dialsGroup.getChildren().add(dialBox);
        dialsGroup.getChildren().add(dialCircleBackground);
        dialsGroup.getChildren().add(dialArcNight);
        dialsGroup.getChildren().add(dialArcMidnight);
        dialsGroup.getChildren().add(dialCircleFrame);

        for(int i = 0; i < 96; i++) {

            double lineLength = 5;
            if (i % 2 == 0) { lineLength = 7.5; }
            if (i % 4 == 0) { lineLength = 10; }
            if (i % 24 == 0) { lineLength = 90; }
            if (i % 48 == 0) { lineLength = 20; }

            Line hourMarkerLine = new Line(dialCenterX, lineLength, dialCenterX, 0);
            Rotate hourMarkerRotate = centerRotate.clone();
            hourMarkerRotate.setAngle(i * 360d / 96d);
            hourMarkerLine.getTransforms().add(hourMarkerRotate);
            hourMarkerLine.setStroke(Color_Of_Darkness);
            hourMarkerLine.setStrokeWidth( 1 / ((globalScaleX + globalScaleY) / 2) );

            dialsGroup.getChildren().add(hourMarkerLine);
        }

        dialsGroup.getChildren().add(sunTimeDial);
        dialsGroup.getChildren().add(highNoonDial);
        dialsGroup.getChildren().add(sunriseDial);
        dialsGroup.getChildren().add(sunsetDial);
        dialsGroup.getChildren().add(localTimeDial);
        dialsGroup.getChildren().add(dialCircleCenterDot);
        dialsGroup.getChildren().add(dialTextDate);

        // Apply scale
        dialsGroup.setScaleX(globalScaleX);
        dialsGroup.setScaleY(globalScaleY);
    }

    // getters
    public Group getDialsGroup() {
        return dialsGroup;
    }

    // setters
    public void setSunTimeDialAngle(double sunTimeDialAngle) {
        this.sunTimeDialAngle = sunTimeDialAngle;
        sunTimeDialRotate.setAngle(sunTimeDialAngle);
    }

    public void setHighNoonDialAngle(double highNoonDialAngle) {
        this.highNoonDialAngle = highNoonDialAngle;
        highNoonDialRotate.setAngle(highNoonDialAngle);
    }

    public void setSunriseSunsetDialAngle(double sunriseDialAngle, double sunsetDialAngle) {
        this.sunriseDialAngle = sunriseDialAngle;
        this.sunsetDialAngle = sunsetDialAngle;
        sunriseDialRotate.setAngle(sunriseDialAngle);
        sunsetDialRotate.setAngle(sunsetDialAngle);
        dialArcNight.setStartAngle(90 - sunsetDialAngle);
        dialArcNight.setLength(sunsetDialAngle - sunriseDialAngle);
    }

    public void setLocalTimeDialAngle(double localTimeDialAngle) {
        this.localTimeDialAngle = localTimeDialAngle;
        localTimeDialRotate.setAngle(localTimeDialAngle);
    }

    public void setLocalTimeText(String localTimeText) {
        this.localTimeText = localTimeText;
        dialTextDate.setText(localTimeText);
    }
}
