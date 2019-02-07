import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static java.lang.Math.abs;
import static java.lang.Math.floor;

public class Sundial {

    // DEFAULTS
    private static final double DEFAULT_sunTimeDialAngle      = 0;
    private static final double DEFAULT_highNoonDialAngle     = 10;
    private static final double DEFAULT_sunriseDialAngle      = 20;
    private static final double DEFAULT_sunsetDialAngle       = 30;
    private static final double DEFAULT_localTimeDialAngle    = -5000;

    private static final double DEFAULT_nightCompression      = 1.0d;
    private static final double MAX_nightCompression          = 4.0d;
    private static final double MIN_nightCompression          = 0.6d;

    private static final String DEFAULT_localTimeText         = "MMM DDD dd hh:mm:ss ZZZ YYYY";

    private final double MATRIX_SEPARATOR_OFFSET = -1.0d;
    private final int MAX_MARKER = 96;

    private final double dialWidth         = 200;
    private final double dialHeight        = 200;
    private final double marginX           = 20;
    private final double marginY           = 20;
    private final double globalScaleX      = 2.0;
    private final double globalScaleY      = 2.0;
    private final double dialCenterX       = dialWidth / 2;
    private final double dialCenterY       = dialHeight / 2;

    public final Color Color_Of_Window    = new Color(0.65, 0.85, 0.85, 1.00);
    public final Color Color_Of_Earth     = new Color(0.85, 0.85, 0.65, 1.00);
    public final Color Color_Of_Darkness  = new Color(0.00, 0.00, 0.00, 1.00);
    public final Color Color_Of_TextBack  = new Color(0.90, 0.90, 0.50, 1.00);
    public final Color Color_Of_Void      = new Color(0.00, 0.00, 0.00, 0.00);

    public final Color Color_Of_Nominal   = new Color(0.00, 0.65, 1.00, 0.35);
    public final Color Color_Of_Warning   = new Color(1.00, 0.65, 0.00, 0.35);

    public final Color Color_Of_DaySky    = new Color(0.50, 0.75, 1.00, 1.00);
    public final Color Color_Of_NightSky  = new Color(0.50, 0.35, 1.00, 1.00);
    public final Color Color_Of_Midnight  = new Color(0.00, 0.00, 0.00, 0.35);

    public final Color Color_Of_SunTime   = new Color(1.00, 0.50, 0.00, 1.00);
    public final Color Color_Of_HighNoon  = new Color(1.00, 1.00, 0.00, 1.00);
    public final Color Color_Of_Horizon   = new Color(1.00, 0.90, 0.30, 1.00);
    public final Color Color_Of_SunRise   = new Color(1.00, 0.00, 0.00, 1.00);
    public final Color Color_Of_SunSet    = new Color(0.65, 0.00, 0.65, 1.00);
    public final Color Color_Of_LocalTime = new Color(1.00, 1.00, 1.00, 1.00);

    public final String MATRIX_GLOW   = "-fx-effect: dropshadow(three-pass-box, rgba(255,128, 32, 1.0),  4.0, 0.50, 0, 0);";
    public final String MATRIX_SHADOW = "-fx-effect: dropshadow(three-pass-box, rgba( 32,128,255, 1.0),  4.0, 0.50, 0, 0);";
    public final String MATRIX_BLOCK  = "-fx-effect: dropshadow(three-pass-box, rgba(  0,  0,  0, 1.0),  4.0, 0.50, 0, 0);";
    public final String HORIZON_GLOW  = "-fx-effect: dropshadow(three-pass-box, rgba(255, 96, 32, 1.0), 10.0, 0.85, 0, 0);";

    private final RadialGradient Warning_Glow = new RadialGradient(
            0, 0,
            dialCenterX, dialCenterY, 100,
            false,
            CycleMethod.NO_CYCLE,
            new Stop(0.75, Color_Of_Void),
            new Stop(1.00, Color_Of_Warning)
    );

    private final RadialGradient Nominal_Glow = new RadialGradient(
            0, 0,
            dialCenterX, dialCenterY, 100,
            false,
            CycleMethod.NO_CYCLE,
            new Stop(0.75, Color_Of_Void),
            new Stop(1.00, Color_Of_Nominal)
    );


    private final Font Font_Of_Info = new Font("Lucida Console", 14);
    private final Font Font_Of_Dial = new Font("Lucida Console", 8);

    private final String Path_Of_Earth = "M 100 100 L 300 100 L 200 300 Z M 150 150 L 100 250 L 350 150 Z";

    // variables
    private double sunTimeDialAngle;
    private double highNoonDialAngle;
    private double sunriseDialAngle;
    private double sunsetDialAngle;
    private double localTimeDialAngle;
    private String localTimeText;
    private double nightCompression;

    private GregorianCalendar sunTime;
    private GregorianCalendar highNoon;
    private GregorianCalendar sunrise;
    private GregorianCalendar sunset;
    private GregorianCalendar localTime;

    // graphical primitives
    private Group dialsGroup;

    private Rectangle dialMarginBox;
    private Rectangle dialBox;

    private Rotate centerRotate;
    private Rotate sunTimeDialRotate;
    private Rotate highNoonDialRotate;
    private Rotate sunriseDialRotate;
    private Rotate sunsetDialRotate;
    private Rotate localTimeDialRotate;
    private ArrayList<Rotate> dialMarkerRotateList;


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

    private DotMatrix matrixYear;
    private DotMatrix matrixMonth;
    private DotMatrix matrixDay;
    private DotMatrix matrixHour;
    private DotMatrix matrixMinute;
    private DotMatrix matrixSecond;
    private DotMatrix matrixWeek;
    private DotMatrix matrixSunrise;
    private DotMatrix matrixSunset;

    // Constructor
    public Sundial(Builder builder) {
        this.sunTimeDialAngle = builder.sunTimeDialAngle;
        this.highNoonDialAngle = builder.highNoonDialAngle;
        this.sunriseDialAngle = builder.sunriseDialAngle;
        this.sunsetDialAngle = builder.sunsetDialAngle;
        this.localTimeDialAngle = builder.localTimeDialAngle;
        this.localTimeText = builder.localTimeText;
        this.nightCompression = builder.nightCompression;
        this.sunTime = new GregorianCalendar();
        this.highNoon = new GregorianCalendar();
        this.sunrise = new GregorianCalendar();
        this.sunset = new GregorianCalendar();
        this.localTime = new GregorianCalendar();
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
        private double nightCompression;

        public Builder() {
            this.sunTimeDialAngle = DEFAULT_sunTimeDialAngle;
            this.highNoonDialAngle = DEFAULT_highNoonDialAngle;
            this.sunriseDialAngle = DEFAULT_sunriseDialAngle;
            this.sunTimeDialAngle = DEFAULT_sunsetDialAngle;
            this.localTimeDialAngle = DEFAULT_localTimeDialAngle;
            this.localTimeText = DEFAULT_localTimeText;
            this.nightCompression = DEFAULT_nightCompression;
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

        public Builder nightCompression(double nightCompression) {
            this.nightCompression = nightCompression;
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

        sunTimeDialRotate.setAngle(applyNightCompression(sunTimeDialAngle));
        highNoonDialRotate.setAngle(applyNightCompression(highNoonDialAngle));
        sunriseDialRotate.setAngle(applyNightCompression(sunriseDialAngle));
        sunsetDialRotate.setAngle(applyNightCompression(sunsetDialAngle));
        localTimeDialRotate.setAngle(applyNightCompression(localTimeDialAngle));

        pathOfEarth = new SVGPath();
        pathOfEarth.setContent(Path_Of_Earth);
        pathOfEarth.setStroke(Color_Of_Darkness);
        pathOfEarth.setFill(Color_Of_Earth);

        // Dials in a box
        dialBox = new Rectangle(dialWidth, dialHeight);
        dialBox.setFill(Color_Of_Darkness);
        dialBox.setOpacity(0.1d);

        dialMarginBox = new Rectangle(dialWidth + marginX, dialHeight + marginY);
        dialMarginBox.setTranslateX(-1 * marginX / 2);
        dialMarginBox.setTranslateY(-1 * marginY / 2);
        dialMarginBox.setFill(Color_Of_Void);
        dialMarginBox.setStroke(Color_Of_Darkness);
        dialMarginBox.setStrokeWidth(0.5d);

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
        dialCircleFrame.setFill(Nominal_Glow);
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
        sunriseDial.setStroke(Color_Of_Horizon);
        sunriseDial.setStrokeWidth( 1.4 / ((globalScaleX + globalScaleY) / 2) );
        sunriseDial.getTransforms().add(sunriseDialRotate);
        sunriseDial.setStyle(HORIZON_GLOW);
//        sunriseDial.setBlendMode(BlendMode.SCREEN);

        String sunriseTimeString = ""
                + sunrise.get(Calendar.HOUR_OF_DAY)
                + ":" + sunrise.get(Calendar.MINUTE)
                + ":" + sunrise.get(Calendar.SECOND)
                ;

        matrixSunrise = new DotMatrix(sunriseTimeString, Color_Of_Horizon);
        matrixSunrise.setTranslateX(dialCenterX - matrixSunrise.getLayoutBounds().getWidth() / 2);
        matrixSunrise.setTranslateY(dialCenterY - matrixSunrise.getLayoutBounds().getHeight() / 2 - 70);
        matrixSunrise.setRotate(90d);
//        matrixSunrise.setTranslateY(-70d);
//        Rotate matrixSunriseRotate = new Rotate();
//        matrixSunriseRotate.setPivotX(marginX);
//        matrixSunriseRotate.setPivotY(marginY);
//        matrixSunriseRotate.setAngle(90d);
//        matrixSunrise.getTransforms().add(matrixSunriseRotate);
//        matrixSunrise.setScaleX(0.35d);
//        matrixSunrise.setScaleY(0.35d);
//        matrixSunrise.getTransforms().add(sunriseDialRotate);

        sunsetDial = new Line(dialCenterX, 100, dialCenterX, 0);
        sunsetDial.setStroke(Color_Of_Horizon);
        sunsetDial.setStrokeWidth( 1.4 / ((globalScaleX + globalScaleY) / 2) );
        sunsetDial.getTransforms().add(sunsetDialRotate);
        sunsetDial.setStyle(HORIZON_GLOW);
//        sunsetDial.setBlendMode(BlendMode.SCREEN);

        localTimeDial = new Line(dialCenterX, 110, dialCenterX, 0);
        localTimeDial.setStroke(Color_Of_LocalTime);
        localTimeDial.getTransforms().add(localTimeDialRotate);

        dialTextDate = new Text();
        dialTextDate.setText(localTimeText);
        dialTextDate.setFill(Color_Of_LocalTime);
        dialTextDate.setFont(Font_Of_Dial);
        dialTextDate.setLayoutX(dialCenterX - dialTextDate.getLayoutBounds().getWidth() / 2);
        dialTextDate.setLayoutY(dialCenterY * 1.5d - dialTextDate.getLayoutBounds().getHeight() / 2);


        matrixDay = new DotMatrix("00", Color_Of_LocalTime);

        DotMatrix matrixSeparatorDayToMonth = new DotMatrix(".", Color_Of_LocalTime);
        matrixSeparatorDayToMonth.setTranslateX(matrixDay.getLayoutBounds().getWidth() + MATRIX_SEPARATOR_OFFSET);

        matrixMonth = new DotMatrix("00", Color_Of_LocalTime);
        matrixMonth.setTranslateX(matrixSeparatorDayToMonth.getLayoutBounds().getWidth() + matrixSeparatorDayToMonth.getTranslateX() + MATRIX_SEPARATOR_OFFSET);

        DotMatrix matrixSeparatorMonthToYear = new DotMatrix(".", Color_Of_LocalTime);
        matrixSeparatorMonthToYear.setTranslateX(matrixMonth.getLayoutBounds().getWidth() + matrixMonth.getTranslateX() + MATRIX_SEPARATOR_OFFSET);

        matrixYear = new DotMatrix("0000", Color_Of_LocalTime);
        matrixYear.setTranslateX(matrixSeparatorMonthToYear.getLayoutBounds().getWidth() + matrixSeparatorMonthToYear.getTranslateX() + MATRIX_SEPARATOR_OFFSET);

        Group matrixDate = new Group();
        matrixDate.getChildren().addAll(matrixDay, matrixSeparatorDayToMonth, matrixMonth, matrixSeparatorMonthToYear, matrixYear);
        matrixDate.setScaleX(1.0d);
        matrixDate.setScaleY(1.0d);
        matrixDate.setLayoutX(dialCenterX - matrixDate.getLayoutBounds().getWidth() / 2);
        matrixDate.setLayoutY(dialCenterY * 1.5d - matrixDate.getLayoutBounds().getHeight() / 2);


        matrixWeek = new DotMatrix("00", Color_Of_LocalTime);
        matrixWeek.setScaleX(0.5d);
        matrixWeek.setScaleY(0.5d);
        matrixWeek.setLayoutX(matrixDate.getLayoutBounds().getWidth() + matrixDate.getLayoutX() + MATRIX_SEPARATOR_OFFSET);
        matrixWeek.setLayoutY(matrixDate.getLayoutY());


        matrixHour = new DotMatrix("00", Color_Of_LocalTime);

        DotMatrix matrixSeparatorHourToMinute = new DotMatrix(":", Color_Of_LocalTime);
        matrixSeparatorHourToMinute.setTranslateX(matrixHour.getLayoutBounds().getWidth() + MATRIX_SEPARATOR_OFFSET);

        matrixMinute = new DotMatrix("00", Color_Of_LocalTime);
        matrixMinute.setTranslateX(matrixSeparatorHourToMinute.getLayoutBounds().getWidth() + matrixSeparatorHourToMinute.getTranslateX() + MATRIX_SEPARATOR_OFFSET);

        DotMatrix matrixSeparatorMinuteToYear = new DotMatrix(":", Color_Of_LocalTime);
        matrixSeparatorMinuteToYear.setTranslateX(matrixMinute.getLayoutBounds().getWidth() + matrixMinute.getTranslateX() + MATRIX_SEPARATOR_OFFSET);

        matrixSecond = new DotMatrix("00", Color_Of_LocalTime);
        matrixSecond.setTranslateX(matrixSeparatorMinuteToYear.getLayoutBounds().getWidth() + matrixSeparatorMinuteToYear.getTranslateX() + MATRIX_SEPARATOR_OFFSET);

        Group matrixTime = new Group();
        matrixTime.getChildren().addAll(matrixHour, matrixSeparatorHourToMinute,  matrixMinute, matrixSeparatorMinuteToYear, matrixSecond);
        matrixTime.setScaleX(1.0d);
        matrixTime.setScaleY(1.0d);
        matrixTime.setLayoutX(dialCenterX - matrixTime.getLayoutBounds().getWidth() / 2);
        matrixTime.setLayoutY(dialCenterY * 1.3d - matrixTime.getLayoutBounds().getHeight() / 2);

        setMatrixGlow(matrixYear, MATRIX_SHADOW);
        setMatrixGlow(matrixMonth, MATRIX_SHADOW);
        setMatrixGlow(matrixDay, MATRIX_SHADOW);
        setMatrixGlow(matrixHour, MATRIX_SHADOW);
        setMatrixGlow(matrixMinute, MATRIX_SHADOW);
        setMatrixGlow(matrixSecond, MATRIX_SHADOW);
        setMatrixGlow(matrixWeek, MATRIX_SHADOW);

        matrixSeparatorDayToMonth.setStyle(MATRIX_SHADOW);
        matrixSeparatorHourToMinute.setStyle(MATRIX_SHADOW);
        matrixSeparatorMinuteToYear.setStyle(MATRIX_SHADOW);
        matrixSeparatorMonthToYear.setStyle(MATRIX_SHADOW);


        // Add events
        matrixYear.setOnMouseEntered(event -> setMatrixGlow(matrixYear, MATRIX_GLOW));
        matrixYear.setOnMouseExited(event -> setMatrixGlow(matrixYear, MATRIX_SHADOW));

        matrixMonth.setOnMouseEntered(event -> setMatrixGlow(matrixMonth, MATRIX_GLOW));
        matrixMonth.setOnMouseExited(event -> setMatrixGlow(matrixMonth, MATRIX_SHADOW));

        matrixDay.setOnMouseEntered(event -> setMatrixGlow(matrixDay, MATRIX_GLOW));
        matrixDay.setOnMouseExited(event -> setMatrixGlow(matrixDay, MATRIX_SHADOW));

        matrixHour.setOnMouseEntered(event -> setMatrixGlow(matrixHour, MATRIX_GLOW));
        matrixHour.setOnMouseExited(event -> setMatrixGlow(matrixHour, MATRIX_SHADOW));

        matrixMinute.setOnMouseEntered(event -> setMatrixGlow(matrixMinute, MATRIX_GLOW));
        matrixMinute.setOnMouseExited(event -> setMatrixGlow(matrixMinute, MATRIX_SHADOW));

        matrixSecond.setOnMouseEntered(event -> setMatrixGlow(matrixSecond, MATRIX_GLOW));
        matrixSecond.setOnMouseExited(event -> setMatrixGlow(matrixSecond, MATRIX_SHADOW));

        matrixWeek.setOnMouseEntered(event -> setMatrixGlow(matrixWeek, MATRIX_GLOW));
        matrixWeek.setOnMouseExited(event -> setMatrixGlow(matrixWeek, MATRIX_SHADOW));


        // Add layers
        dialsGroup = new Group();

        dialsGroup.getChildren().add(dialMarginBox);
        dialsGroup.getChildren().add(dialBox);
        dialsGroup.getChildren().add(dialCircleBackground);
        dialsGroup.getChildren().add(dialArcNight);
        dialsGroup.getChildren().add(dialArcMidnight);
        dialsGroup.getChildren().add(dialCircleFrame);

        dialMarkerRotateList = new ArrayList<Rotate>();

        for(int i = 0; i < MAX_MARKER; i++) {

            double lineLength = 5;
            if (i % 2 == 0) { lineLength = 7.5; }
            if (i % 4 == 0) { lineLength = 10; }
            if (i % 24 == 0) { lineLength = 90; }
            if (i % 48 == 0) { lineLength = 20; }

            Line hourMarkerLine = new Line(dialCenterX, lineLength, dialCenterX, 0);
            Rotate hourMarkerRotate = centerRotate.clone();
            hourMarkerRotate.setAngle(applyNightCompression(i * 360d / 96d));
            hourMarkerLine.getTransforms().add(hourMarkerRotate);
            hourMarkerLine.setStroke(Color_Of_Darkness);
            hourMarkerLine.setStrokeWidth( 1 / ((globalScaleX + globalScaleY) / 2) );

            dialsGroup.getChildren().add(hourMarkerLine);
            dialMarkerRotateList.add(hourMarkerRotate);
        }

        dialsGroup.getChildren().add(sunTimeDial);
        dialsGroup.getChildren().add(highNoonDial);
        dialsGroup.getChildren().add(sunriseDial);
        dialsGroup.getChildren().add(matrixSunrise);
        dialsGroup.getChildren().add(sunsetDial);
        dialsGroup.getChildren().add(localTimeDial);
        dialsGroup.getChildren().add(dialCircleCenterDot);

//        dialsGroup.getChildren().add(dialTextDate);
        dialsGroup.getChildren().add(matrixTime);
        dialsGroup.getChildren().add(matrixDate);
        dialsGroup.getChildren().add(matrixWeek);

        // Apply scale
        dialsGroup.setScaleX(globalScaleX);
        dialsGroup.setScaleY(globalScaleY);
    }

    // Utility
    private double getAngle(GregorianCalendar calendar) {

        if(calendar == null) { return 0; }

        double hour = (double) calendar.get(Calendar.HOUR_OF_DAY);
        double minute = (double) calendar.get(Calendar.MINUTE);
        double second = (double) calendar.get(Calendar.SECOND);

        double angle = getRemainder((hour / 24d + minute / (24d * 60d) + second / (24d * 60d * 60d)) * 360d + 180d, 360d);

        return angle;
    }

    private double getRemainder(double a, double b) {
        double division = a / b;
        return (division - floor(division)) * b;
    }

    private double applyNightCompression(double angle) {

        double cleanAngle = angle;

        double newAngle = cleanAngle;
        if (cleanAngle > 0 && cleanAngle <= 90) { newAngle = cleanAngle * (2 - 1 / nightCompression); }
        if (cleanAngle > 90 && cleanAngle <= 180) { newAngle = 180 - (180 - cleanAngle) * (1 / nightCompression); }
        if (cleanAngle > 180 && cleanAngle <= 270) { newAngle = 180 + (cleanAngle - 180) * (1 / nightCompression); }
        if (cleanAngle > 270 && cleanAngle <= 360) { newAngle = 360 - (360 - cleanAngle) * (2 - 1 / nightCompression); }

        return newAngle;
    }

    // Getters
    public Group getDialsGroup() {
        return dialsGroup;
    }

    public Circle getDialCircleCenterDot() {
        return dialCircleCenterDot;
    }

    public DotMatrix getMatrixYear() {
        return matrixYear;
    }

    public DotMatrix getMatrixMonth() {
        return matrixMonth;
    }

    public DotMatrix getMatrixDay() {
        return matrixDay;
    }

    public DotMatrix getMatrixHour() {
        return matrixHour;
    }

    public DotMatrix getMatrixMinute() {
        return matrixMinute;
    }

    public DotMatrix getMatrixSecond() {
        return matrixSecond;
    }

    public DotMatrix getMatrixWeek() {
        return matrixWeek;
    }

    // Setters
    public void setSunTime(GregorianCalendar sunTime) {
        this.sunTime = sunTime;
        setSunTimeDialAngle(getAngle(this.sunTime));
    }

    public void setHighNoon(GregorianCalendar highNoon) {
        this.highNoon = highNoon;
        setHighNoonDialAngle(getAngle(this.highNoon));
    }

    public void setHorizon(GregorianCalendar sunrise, GregorianCalendar sunset) {
        this.sunrise = sunrise;
        this.sunset = sunset;
        setHorizonDialAngle(getAngle(this.sunrise), getAngle(this.sunset));
    }

    public void setLocalTime(GregorianCalendar localTime) {
        this.localTime = localTime;
        setLocalTimeDialAngle(getAngle(this.localTime));
    }

    public void setSunTimeDialAngle(double sunTimeDialAngle) {
        this.sunTimeDialAngle = applyNightCompression(sunTimeDialAngle);
        sunTimeDialRotate.setAngle(this.sunTimeDialAngle);
    }

    public void setHighNoonDialAngle(double highNoonDialAngle) {
        this.highNoonDialAngle = applyNightCompression(highNoonDialAngle);
        highNoonDialRotate.setAngle(this.highNoonDialAngle);
    }

    public void setHorizonDialAngle(double sunriseDialAngle, double sunsetDialAngle) {
        this.sunriseDialAngle = applyNightCompression(sunriseDialAngle);
        this.sunsetDialAngle = applyNightCompression(sunsetDialAngle);
        sunriseDialRotate.setAngle(this.sunriseDialAngle);
        sunsetDialRotate.setAngle(this.sunsetDialAngle);
        dialArcNight.setStartAngle(90 - this.sunriseDialAngle);
        dialArcNight.setLength(-1 * (this.sunsetDialAngle - this.sunriseDialAngle));
    }

    public void setLocalTimeDialAngle(double localTimeDialAngle) {
        this.localTimeDialAngle = applyNightCompression(localTimeDialAngle);
        localTimeDialRotate.setAngle(this.localTimeDialAngle);
    }

    public void updateDialMarkers() {
        int size = dialMarkerRotateList.size();
        for (int i = 0; i < size; i++) {
            dialMarkerRotateList.get(i).setAngle(applyNightCompression(i * 360d / 96d));
        }
    }

    public void setLocalTimeText(String localTimeText) {
        if (localTimeText == null) { return; }
        this.localTimeText = localTimeText;
        dialTextDate.setText(localTimeText);
    }

    public void setDialFrameWarning(boolean warning) {
        if (warning) {
            dialCircleFrame.setFill(Warning_Glow);
        } else {
            dialCircleFrame.setFill(Nominal_Glow);
        }
    }

    public void setMatrixGlow(DotMatrix matrix, String style) {
        if (matrix == null || style == null) { return; }
        matrix.setStyle(style);
    }

    public void changeNightCompression(double nightCompression) {

        this.nightCompression += nightCompression;

        if (this.nightCompression < MIN_nightCompression) { this.nightCompression = MIN_nightCompression; }
        if (this.nightCompression > MAX_nightCompression) { this.nightCompression = MAX_nightCompression; }

        updateDialMarkers();
    }

}
