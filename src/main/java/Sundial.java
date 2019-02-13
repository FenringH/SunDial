import javafx.scene.Group;
import javafx.scene.input.ScrollEvent;
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

import static java.lang.Math.*;

public class Sundial {

    // DEFAULTS
    private static final double DEFAULT_sunTimeDialAngle      = 0;
    private static final double DEFAULT_highNoonDialAngle     = 10;
    private static final double DEFAULT_sunriseDialAngle      = 20;
    private static final double DEFAULT_sunsetDialAngle       = 30;
    private static final double DEFAULT_localTimeDialAngle    = -5000;

    private static final double MATRIX_SEPARATOR_OFFSET = -1.0d;
    private static final int MAX_MARKER = 96;

    private static final double DEFAULT_nightCompression      = 0;
    private static final double MAX_nightCompression          = 45;
    private static final double MIN_nightCompression          = -45;
    private static final double STEP_nightCompression         = 360d / MAX_MARKER;

    private static final String DEFAULT_localTimeText         = "MMM DDD dd hh:mm:ss ZZZ YYYY";

    public static final double DIAL_WIDTH = 430.0d;
    public static final double DIAL_HEIGHT = 430.0d;
    public static final double MARGIN_X = 25.0d;
    public static final double MARGIN_Y = 25.0d;
    private static final double SCALE_X = 1.0d;
    private static final double SCALE_Y = 1.0d;
    private static final double CENTER_X = DIAL_WIDTH / 2;
    private static final double CENTER_Y = DIAL_HEIGHT / 2;

    private static final double DOT_RADIUS = 10.0d;
    private static final double SUNTIME_DIAL_LENGTH = 100.0d;
    private static final double HIGHNOON_DIAL_LENGTH = 50.0d;
    private static final double SUNRISE_DIAL_LENGTH = DIAL_HEIGHT / 2;
    private static final double SUNSET_DIAL_LENGTH = DIAL_HEIGHT / 2;
    private static final double LOCALTIME_DIAL_LENGTH = DIAL_HEIGHT / 2 + 20;
    private static final double MARKER_DIAL_LENGTH = 10.0d;
    private static final double DAYLENGTH_ARC_RADIUS = 100.0d;
    private static final double BOXIE_SIZE = 40.0d;
    private static final double DAY_ARC_MARGIN = 10.0d;

    private static final double DAYLENGTH_STROKE_WIDTH = 2.00d;
    private static final double SUNTIME_STROKE_WIDTH = 2.00d;
    private static final double HIGHNOON_STROKE_WIDTH = 2.00d;
    private static final double LOCALTIME_STROKE_WIDTH = 2.00d;
    private static final double SUNRISE_STROKE_WIDTH = 1.00d;
    private static final double SUNSET_STROKE_WIDTH = 1.00d;
    private static final double MARKER_STROKE_WIDTH = 1.00d;

    private static final double DAYLENGTH_ARC_OPACITY = 0.65d;
    private static final double MARGIN_CIRCLE_OPACITY = 0.65d;

    private static final double MATRIX_MARKER_OFFSET = 5.0d;
    private static final double MATRIX_HORIZON_OFFSET = 77.0d;
    private static final double MATRIX_TIME_OFFSET = 0.0d;
    private static final double MATRIX_DATE_OFFSET = 45.0d;
    private static final double MATRIX_WEEK_OFFSET = 70.0d;
    private static final double MATRIX_LONGITUDE_OFFSET = 80.0d;
    private static final double MATRIX_LATITUDE_OFFSET = 105.0d;

    private static final double MATRIX_TIME_SCALE = 3.50d;
    private static final double MATRIX_DATE_SCALE = 1.50d;
    private static final double MATRIX_WEEK_SCALE = 1.25d;
    private static final double MATRIX_HORIZON_SCALE = 0.75d;
    private static final double MATRIX_DAYLENGTH_SCALE = 0.75d;
    private static final double MATRIX_LONGITUDE_SCALE = 1.25d;
    private static final double MATRIX_LATITUDE_SCALE = 1.25d;

    public static final Color Color_Of_Window    = new Color(0.65, 0.85, 0.85, 1.00);
    public static final Color Color_Of_Earth     = new Color(0.85, 0.85, 0.65, 1.00);
    public static final Color Color_Of_Darkness  = new Color(0.00, 0.00, 0.00, 1.00);
    public static final Color Color_Of_TextBack  = new Color(0.90, 0.90, 0.50, 1.00);
    public static final Color Color_Of_Void      = new Color(0.00, 0.00, 0.00, 0.00);
    public static final Color Color_Of_AlmostVoid= new Color(0.00, 0.00, 0.00, 0.10);

    public static final Color Color_Of_Nominal   = new Color(0.00, 0.65, 1.00, 0.35);
    public static final Color Color_Of_Warning   = new Color(1.00, 0.65, 0.00, 0.35);

    public static final Color Color_Of_DaySky    = new Color(0.50, 0.75, 1.00, 1.00);
    public static final Color Color_Of_NightSky  = new Color(0.50, 0.35, 1.00, 1.00);
//    public static final Color Color_Of_NightSky  = new Color(0.50, 0.35, 1.00, 0.50);
    public static final Color Color_Of_Midnight  = new Color(0.00, 0.00, 0.00, 0.30);

    public static final Color Color_Of_SunTime   = new Color(1.00, 0.50, 0.00, 1.00);
    public static final Color Color_Of_HighNoon  = new Color(1.00, 1.00, 0.00, 1.00);
    public static final Color Color_Of_Horizon   = new Color(1.00, 0.90, 0.30, 1.00);
    public static final Color Color_Of_SunRise   = new Color(1.00, 0.00, 0.00, 1.00);
    public static final Color Color_Of_SunSet    = new Color(0.65, 0.00, 0.65, 1.00);
    public static final Color Color_Of_LocalTime = new Color(1.00, 1.00, 1.00, 1.00);

    public static final String MATRIX_GLOW      = "-fx-effect: dropshadow(three-pass-box, rgba(255,128, 32, 1.0),  4.0, 0.50, 0, 0);";
    public static final String MATRIX_GLOW2     = "-fx-effect: dropshadow(three-pass-box, rgba(255,128, 32, 1.0), 10.0, 0.50, 0, 0);";
    public static final String MATRIX_SHADOW    = "-fx-effect: dropshadow(three-pass-box, rgba( 32,128,255, 1.0),  4.0, 0.50, 0, 0);";
    public static final String MATRIX_BLOCK     = "-fx-effect: dropshadow(three-pass-box, rgba(  0,  0,  0, 1.0), 10.0, 0.50, 0, 0);";
    public static final String HORIZON_GLOW     = "-fx-effect: dropshadow(three-pass-box, rgba(255, 96, 32, 1.0), 15.0, 0.87, 0, 0);";
    public static final String LOCALTIME_SHADOW = "-fx-effect: dropshadow(three-pass-box, rgba( 32,128,255, 0.7), 15.0, 0.50, 0, 0);";

    private static final RadialGradient Warning_Glow = new RadialGradient(
            0, 0,
            CENTER_X, CENTER_Y, CENTER_Y,
            false,
            CycleMethod.NO_CYCLE,
            new Stop(0.75, Color_Of_Void),
            new Stop(1.00, Color_Of_Warning)
    );

    private static final RadialGradient Nominal_Glow = new RadialGradient(
            0, 0,
            CENTER_X, CENTER_Y, CENTER_Y,
            false,
            CycleMethod.NO_CYCLE,
            new Stop(0.75, Color_Of_Void),
            new Stop(1.00, Color_Of_Nominal)
    );


    private static final Font Font_Of_Info = new Font("Lucida Console", 14);
    private static final Font Font_Of_Dial = new Font("Lucida Console", 8);

    private static final String Path_Of_Earth = "M 100 100 L 300 100 L 200 300 Z M 150 150 L 100 250 L 350 150 Z";

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

    private Rectangle dialBox;
    private Rectangle dialMarginBox;
    private Rectangle dialMarginFillBox;
    private Rectangle dialResizeBoxie;
    private Rectangle dialResetSizeBoxie;
    private Circle dialMarginCircle;

    private Rotate centerRotate;
    private Rotate sunTimeDialRotate;
    private Rotate highNoonDialRotate;
    private Rotate sunriseDialRotate;
    private Rotate sunsetDialRotate;
    private Rotate localTimeDialRotate;
    private ArrayList<Rotate> dialMarkerRotateList;
    private ArrayList<DotMatrix> hourMarkerList;

    private Arc dialArcNight;
    private Arc dialArcMidnight;
    private Arc dialArcDayLength;

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
    private DotMatrix matrixDayLength;
    private DotMatrix matrixLongitude;
    private DotMatrix matrixLatitude;

    private Globe globe;
    private Group coordinatesGroup;


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
        centerRotate.setPivotX(CENTER_X);
        centerRotate.setPivotY(CENTER_Y);

        sunTimeDialRotate = centerRotate.clone();
        highNoonDialRotate = centerRotate.clone();
        sunriseDialRotate = centerRotate.clone();
        sunsetDialRotate = centerRotate.clone();
        localTimeDialRotate = centerRotate.clone();

        sunTimeDialRotate.setAngle(getNightCompressionAngle(sunTimeDialAngle));
        highNoonDialRotate.setAngle(getNightCompressionAngle(highNoonDialAngle));
        sunriseDialRotate.setAngle(getNightCompressionAngle(sunriseDialAngle));
        sunsetDialRotate.setAngle(getNightCompressionAngle(sunsetDialAngle));
        localTimeDialRotate.setAngle(getNightCompressionAngle(localTimeDialAngle));

        pathOfEarth = new SVGPath();
        pathOfEarth.setContent(Path_Of_Earth);
        pathOfEarth.setStroke(Color_Of_Darkness);
        pathOfEarth.setFill(Color_Of_Earth);

        // Dials in a box
        globe = new Globe(CENTER_X - MARGIN_X);
        globe.setLayoutX(CENTER_X);
        globe.setLayoutY(CENTER_Y);
        globe.setVisible(false);

        dialMarginBox = new Rectangle(DIAL_WIDTH, DIAL_HEIGHT);
        dialMarginBox.setTranslateX(0);
        dialMarginBox.setTranslateY(0);
        dialMarginBox.setFill(Color_Of_Void);
        dialMarginBox.setStroke(Color_Of_SunTime);
        dialMarginBox.setStrokeWidth(2.0d);
        dialMarginBox.setOpacity(1);

        dialMarginFillBox = new Rectangle(DIAL_WIDTH, DIAL_HEIGHT);
        dialMarginFillBox.setTranslateX(0);
        dialMarginFillBox.setTranslateY(0);
        dialMarginFillBox.setFill(Color_Of_DaySky);
        dialMarginFillBox.setStroke(Color_Of_Void);
        dialMarginFillBox.setOpacity(0);

        dialResizeBoxie = new Rectangle(BOXIE_SIZE, BOXIE_SIZE);
        dialResizeBoxie.setTranslateX(CENTER_X * (1 + cos(toRadians(45))) - dialResizeBoxie.getWidth() / 2);
        dialResizeBoxie.setTranslateY(CENTER_Y * (1 + sin(toRadians(45))) - dialResizeBoxie.getHeight() / 2);
        dialResizeBoxie.setFill(Color_Of_DaySky);
        dialResizeBoxie.setStroke(Color_Of_Void);
        dialResizeBoxie.setOpacity(0.20d);

        dialResetSizeBoxie = new Rectangle(BOXIE_SIZE, BOXIE_SIZE);
        dialResetSizeBoxie.setTranslateX(CENTER_X * (1 + cos(toRadians(225))) - dialResetSizeBoxie.getWidth() / 2);
        dialResetSizeBoxie.setTranslateY(CENTER_Y * (1 + sin(toRadians(225))) - dialResetSizeBoxie.getHeight() / 2);
        dialResetSizeBoxie.setFill(Color_Of_DaySky);
        dialResetSizeBoxie.setStroke(Color_Of_Void);
        dialResetSizeBoxie.setOpacity(0.20d);

        dialMarginCircle = new Circle(CENTER_X, CENTER_Y, DIAL_WIDTH / 2);
        dialMarginCircle.setFill(Color_Of_DaySky);
        dialMarginCircle.setStroke(Color_Of_Void);
        dialMarginCircle.setOpacity(MARGIN_CIRCLE_OPACITY);

        dialBox = new Rectangle(DIAL_WIDTH, DIAL_HEIGHT);
        dialBox.setFill(Color_Of_Void);
        dialBox.setStroke(Color_Of_Darkness);
        dialBox.setStrokeWidth(0.30d);
        dialBox.setOpacity(0.00d);

        dialArcNight = new Arc(CENTER_X, CENTER_Y, DIAL_WIDTH / 2 - MARGIN_X, DIAL_HEIGHT / 2 - MARGIN_Y, 90 - sunsetDialAngle, sunsetDialAngle - sunriseDialAngle);
        dialArcNight.setType(ArcType.ROUND);
        dialArcNight.setStroke(Color_Of_Void);
        dialArcNight.setFill(Color_Of_NightSky);

        dialArcMidnight = new Arc(CENTER_X, CENTER_Y, DIAL_WIDTH / 2 - MARGIN_X, DIAL_HEIGHT / 2 - MARGIN_Y, 0, -180);
        dialArcMidnight.setType(ArcType.ROUND);
        dialArcMidnight.setStroke(Color_Of_Void);
        dialArcMidnight.setFill(Color_Of_Midnight);

        dialArcDayLength = new Arc(CENTER_X, CENTER_Y, DAYLENGTH_ARC_RADIUS, DAYLENGTH_ARC_RADIUS, 90 - sunsetDialAngle, 360 - (sunsetDialAngle - sunriseDialAngle));
        dialArcDayLength.setType(ArcType.OPEN);
        dialArcDayLength.setStroke(Color_Of_LocalTime);
        dialArcDayLength.setStrokeWidth(DAYLENGTH_STROKE_WIDTH);
        dialArcDayLength.setFill(Color_Of_Void);
        dialArcDayLength.setOpacity(DAYLENGTH_ARC_OPACITY);

        dialCircleBackground = new Circle(CENTER_X, CENTER_Y, DIAL_WIDTH / 2 - MARGIN_X);
        dialCircleBackground.setFill(Color_Of_DaySky);
        dialCircleBackground.setStroke(Color_Of_Void);

        dialCircleFrame = new Circle(CENTER_X, CENTER_Y, DIAL_WIDTH / 2 - MARGIN_X);
        dialCircleFrame.setFill(Nominal_Glow);
        dialCircleFrame.setStroke(Color_Of_Darkness);
        dialCircleFrame.setStrokeWidth(MARKER_STROKE_WIDTH);

        dialCircleCenterDot = new Circle(CENTER_X, CENTER_Y, DOT_RADIUS);
        dialCircleCenterDot.setFill(Color_Of_LocalTime);
        dialCircleCenterDot.setStroke(Color_Of_Void);

        sunTimeDial = new Line(CENTER_X, SUNTIME_DIAL_LENGTH, CENTER_X, MARGIN_Y);
        sunTimeDial.setStroke(Color_Of_SunTime);
        sunTimeDial.setStrokeWidth(SUNTIME_STROKE_WIDTH);
        sunTimeDial.getTransforms().add(sunTimeDialRotate);

        highNoonDial = new Line(CENTER_X, HIGHNOON_DIAL_LENGTH, CENTER_X, MARGIN_Y);
        highNoonDial.setStroke(Color_Of_HighNoon);
        highNoonDial.setStrokeWidth(HIGHNOON_STROKE_WIDTH);
        highNoonDial.getTransforms().add(highNoonDialRotate);

        localTimeDial = new Line(CENTER_X, LOCALTIME_DIAL_LENGTH, CENTER_X, MARGIN_Y);
        localTimeDial.setStroke(Color_Of_LocalTime);
        localTimeDial.setStrokeWidth(LOCALTIME_STROKE_WIDTH);
        localTimeDial.setStyle(LOCALTIME_SHADOW);
        localTimeDial.getTransforms().add(localTimeDialRotate);

        Group sunriseGroup = new Group();

        sunriseDial = new Line(CENTER_X, SUNRISE_DIAL_LENGTH, CENTER_X, MARGIN_Y);
        sunriseDial.setStroke(Color_Of_Horizon);
        sunriseDial.setStrokeWidth(SUNRISE_STROKE_WIDTH);
//        sunriseDial.getTransforms().add(sunriseDialRotate);
        sunriseDial.setStyle(HORIZON_GLOW);
//        sunriseDial.setBlendMode(BlendMode.SCREEN);

        String sunriseTimeString = ""
                + sunrise.get(Calendar.HOUR_OF_DAY)
                + ":" + sunrise.get(Calendar.MINUTE)
                + ":" + sunrise.get(Calendar.SECOND)
                ;

        matrixSunrise = new DotMatrix("00:00:00", Color_Of_Horizon);
        matrixSunrise.setTranslateX(CENTER_X - matrixSunrise.getLayoutBounds().getWidth() / 2 + matrixSunrise.getLayoutBounds().getHeight() / 2);
        matrixSunrise.setTranslateY(MATRIX_HORIZON_OFFSET);
        matrixSunrise.setRotate(90d);
        matrixSunrise.setScaleX(MATRIX_HORIZON_SCALE);
        matrixSunrise.setScaleY(MATRIX_HORIZON_SCALE);
        matrixSunrise.setStyle(MATRIX_GLOW2);

        sunriseGroup.getChildren().addAll(sunriseDial, matrixSunrise);
        sunriseGroup.getTransforms().add(sunriseDialRotate);


        Group sunsetGroup = new Group();

        sunsetDial = new Line(CENTER_X, SUNSET_DIAL_LENGTH, CENTER_X, MARGIN_Y);
        sunsetDial.setStroke(Color_Of_Horizon);
        sunsetDial.setStrokeWidth(SUNSET_STROKE_WIDTH);
//        sunsetDial.getTransforms().add(sunsetDialRotate);
        sunsetDial.setStyle(HORIZON_GLOW);
//        sunsetDial.setBlendMode(BlendMode.SCREEN);

        String sunsetTimeString = ""
                + sunset.get(Calendar.HOUR_OF_DAY)
                + ":" + sunset.get(Calendar.MINUTE)
                + ":" + sunset.get(Calendar.SECOND)
                ;

        matrixSunset = new DotMatrix("00:00:00", Color_Of_Horizon);
        matrixSunset.setTranslateX(CENTER_X - matrixSunset.getLayoutBounds().getWidth() / 2 - matrixSunset.getLayoutBounds().getHeight() / 2);
        matrixSunset.setTranslateY(MATRIX_HORIZON_OFFSET);
        matrixSunset.setRotate(-90d);
        matrixSunset.setScaleX(MATRIX_HORIZON_SCALE);
        matrixSunset.setScaleY(MATRIX_HORIZON_SCALE);
        matrixSunset.setStyle(MATRIX_GLOW2);

        sunsetGroup.getChildren().addAll(sunsetDial, matrixSunset);
        sunsetGroup.getTransforms().add(sunsetDialRotate);


        dialTextDate = new Text();
        dialTextDate.setText(localTimeText);
        dialTextDate.setFill(Color_Of_LocalTime);
        dialTextDate.setFont(Font_Of_Dial);
        dialTextDate.setLayoutX(CENTER_X - dialTextDate.getLayoutBounds().getWidth() / 2);
        dialTextDate.setLayoutY(CENTER_Y - dialTextDate.getLayoutBounds().getHeight() / 2);


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
        matrixDate.setScaleX(MATRIX_DATE_SCALE);
        matrixDate.setScaleY(MATRIX_DATE_SCALE);
        matrixDate.setLayoutX(CENTER_X - matrixDate.getLayoutBounds().getWidth() / 2);
        matrixDate.setLayoutY(CENTER_Y - matrixDate.getLayoutBounds().getHeight() / 2 + MATRIX_DATE_OFFSET);


        matrixWeek = new DotMatrix("00", Color_Of_LocalTime);
        matrixWeek.setScaleX(MATRIX_WEEK_SCALE);
        matrixWeek.setScaleY(MATRIX_WEEK_SCALE);
        matrixWeek.setLayoutX(CENTER_X - matrixWeek.getLayoutBounds().getWidth() / 2);
        matrixWeek.setLayoutY(CENTER_Y - matrixWeek.getLayoutBounds().getHeight() / 2 + MATRIX_WEEK_OFFSET);


        matrixHour = new DotMatrix("00", Color_Of_LocalTime);

        DotMatrix matrixSeparatorHourToMinute = new DotMatrix(":", Color_Of_LocalTime);
        matrixSeparatorHourToMinute.setTranslateX(matrixHour.getLayoutBounds().getWidth() + MATRIX_SEPARATOR_OFFSET);

        matrixMinute = new DotMatrix("00", Color_Of_LocalTime);
        matrixMinute.setTranslateX(matrixSeparatorHourToMinute.getLayoutBounds().getWidth() + matrixSeparatorHourToMinute.getTranslateX()/* + MATRIX_SEPARATOR_OFFSET*/);

        DotMatrix matrixSeparatorMinuteToSecond = new DotMatrix(":", Color_Of_LocalTime);
        matrixSeparatorMinuteToSecond.setTranslateX(matrixMinute.getLayoutBounds().getWidth() + matrixMinute.getTranslateX() + MATRIX_SEPARATOR_OFFSET);

        matrixSecond = new DotMatrix("00", Color_Of_LocalTime);
        matrixSecond.setTranslateX(matrixSeparatorMinuteToSecond.getLayoutBounds().getWidth() + matrixSeparatorMinuteToSecond.getTranslateX() + MATRIX_SEPARATOR_OFFSET);

        Group matrixTime = new Group();
        matrixTime.getChildren().addAll(matrixHour, /*matrixSeparatorHourToMinute,*/  matrixMinute/*, matrixSeparatorMinuteToSecond, matrixSecond*/);
        matrixTime.setScaleX(MATRIX_TIME_SCALE);
        matrixTime.setScaleY(MATRIX_TIME_SCALE);
        matrixTime.setLayoutX(CENTER_X - matrixTime.getLayoutBounds().getWidth() / 2);
        matrixTime.setLayoutY(CENTER_Y - matrixTime.getLayoutBounds().getHeight() / 2 + MATRIX_TIME_OFFSET);

        matrixDayLength = new DotMatrix("00h00m00s", Color_Of_LocalTime);
        matrixDayLength.setScaleX(MATRIX_DAYLENGTH_SCALE);
        matrixDayLength.setScaleY(MATRIX_DAYLENGTH_SCALE);
        matrixDayLength.setLayoutX(CENTER_X - matrixDayLength.getLayoutBounds().getWidth() / 2);
        matrixDayLength.setLayoutY(CENTER_Y + matrixDayLength.getLayoutBounds().getHeight() - DAYLENGTH_ARC_RADIUS);
        matrixDayLength.setStyle(LOCALTIME_SHADOW);

        matrixLongitude= new DotMatrix("-000.00", Color_Of_LocalTime);
        matrixLongitude.setScaleX(MATRIX_LONGITUDE_SCALE);
        matrixLongitude.setScaleY(MATRIX_LONGITUDE_SCALE);
        matrixLongitude.setLayoutX(CENTER_X - matrixLongitude.getLayoutBounds().getWidth() / 2);
        matrixLongitude.setLayoutY(CENTER_Y + matrixLongitude.getLayoutBounds().getHeight() + MATRIX_LONGITUDE_OFFSET);

        matrixLatitude= new DotMatrix(" -00.00", Color_Of_LocalTime);
        matrixLatitude.setScaleX(MATRIX_LATITUDE_SCALE);
        matrixLatitude.setScaleY(MATRIX_LATITUDE_SCALE);
        matrixLatitude.setLayoutX(CENTER_X - matrixLatitude.getLayoutBounds().getWidth() / 2);
        matrixLatitude.setLayoutY(CENTER_Y + matrixLatitude.getLayoutBounds().getHeight() + MATRIX_LATITUDE_OFFSET);

        coordinatesGroup = new Group();
        coordinatesGroup.getChildren().addAll(matrixLongitude, matrixLatitude);

        Rectangle coordinatesBackdrop = new Rectangle(
                coordinatesGroup.getLayoutBounds().getMinX(),
                coordinatesGroup.getLayoutBounds().getMinY(),
                coordinatesGroup.getLayoutBounds().getWidth(),
                coordinatesGroup.getLayoutBounds().getHeight());

        coordinatesBackdrop.setOpacity(0);

        coordinatesGroup.getChildren().add(coordinatesBackdrop);

        setGroupGlow(matrixYear, MATRIX_SHADOW);
        setGroupGlow(matrixMonth, MATRIX_SHADOW);
        setGroupGlow(matrixDay, MATRIX_SHADOW);
        setGroupGlow(matrixHour, MATRIX_SHADOW);
        setGroupGlow(matrixMinute, MATRIX_SHADOW);
        setGroupGlow(matrixSecond, MATRIX_SHADOW);
        setGroupGlow(matrixWeek, MATRIX_SHADOW);
//        setGroupGlow(matrixLongitude, MATRIX_SHADOW);
//        setGroupGlow(matrixLatitude, MATRIX_SHADOW);
        setGroupGlow(coordinatesGroup, MATRIX_SHADOW);

        matrixSeparatorDayToMonth.setStyle(MATRIX_SHADOW);
        matrixSeparatorHourToMinute.setStyle(MATRIX_SHADOW);
        matrixSeparatorMinuteToSecond.setStyle(MATRIX_SHADOW);
        matrixSeparatorMonthToYear.setStyle(MATRIX_SHADOW);


        // Add events
        dialCircleCenterDot.setOnScroll(event -> changeNightCompression(event));
        dialCircleCenterDot.setOnMouseClicked(event -> resetNightCompression());
        dialCircleCenterDot.setOnMouseEntered(event -> dialCircleCenterDot.setStyle(MATRIX_GLOW2));
        dialCircleCenterDot.setOnMouseExited(event -> dialCircleCenterDot.setStyle(""));

        dialResizeBoxie.setOnMouseEntered(event -> dialResizeBoxie.setFill(Sundial.Color_Of_SunTime));
        dialResizeBoxie.setOnMouseExited(event -> dialResizeBoxie.setFill(Sundial.Color_Of_DaySky));

        dialResetSizeBoxie.setOnMouseEntered(event -> dialResetSizeBoxie.setFill(Sundial.Color_Of_SunTime));
        dialResetSizeBoxie.setOnMouseExited(event -> dialResetSizeBoxie.setFill(Sundial.Color_Of_DaySky));

        matrixYear.setOnMouseEntered(event -> setGroupGlow(matrixYear, MATRIX_GLOW));
        matrixYear.setOnMouseExited(event -> setGroupGlow(matrixYear, MATRIX_SHADOW));

        matrixMonth.setOnMouseEntered(event -> setGroupGlow(matrixMonth, MATRIX_GLOW));
        matrixMonth.setOnMouseExited(event -> setGroupGlow(matrixMonth, MATRIX_SHADOW));

        matrixDay.setOnMouseEntered(event -> setGroupGlow(matrixDay, MATRIX_GLOW));
        matrixDay.setOnMouseExited(event -> setGroupGlow(matrixDay, MATRIX_SHADOW));

        matrixHour.setOnMouseEntered(event -> setGroupGlow(matrixHour, MATRIX_GLOW));
        matrixHour.setOnMouseExited(event -> setGroupGlow(matrixHour, MATRIX_SHADOW));

        matrixMinute.setOnMouseEntered(event -> setGroupGlow(matrixMinute, MATRIX_GLOW));
        matrixMinute.setOnMouseExited(event -> setGroupGlow(matrixMinute, MATRIX_SHADOW));

        matrixSecond.setOnMouseEntered(event -> setGroupGlow(matrixSecond, MATRIX_GLOW));
        matrixSecond.setOnMouseExited(event -> setGroupGlow(matrixSecond, MATRIX_SHADOW));

        matrixWeek.setOnMouseEntered(event -> setGroupGlow(matrixWeek, MATRIX_GLOW));
        matrixWeek.setOnMouseExited(event -> setGroupGlow(matrixWeek, MATRIX_SHADOW));

//        matrixLongitude.setOnMouseEntered(event -> setGroupGlow(matrixLongitude, MATRIX_GLOW));
//        matrixLongitude.setOnMouseExited(event -> setGroupGlow(matrixLongitude, MATRIX_SHADOW));
//
//        matrixLatitude.setOnMouseEntered(event -> setGroupGlow(matrixLatitude, MATRIX_GLOW));
//        matrixLatitude.setOnMouseExited(event -> setGroupGlow(matrixLatitude, MATRIX_SHADOW));

//        coordinatesGroup.setOnMouseEntered(event -> setGroupGlow(coordinatesGroup, MATRIX_GLOW));
//        coordinatesGroup.setOnMouseExited(event -> setGroupGlow(coordinatesGroup, MATRIX_SHADOW));


        // Add layers
        dialsGroup = new Group();

        dialsGroup.getChildren().add(dialMarginFillBox);
        dialsGroup.getChildren().add(dialResizeBoxie);
        dialsGroup.getChildren().add(dialResetSizeBoxie);
//        dialsGroup.getChildren().add(dialMarginBox);
        dialsGroup.getChildren().add(dialMarginCircle);
//        dialsGroup.getChildren().add(dialBox);
        dialsGroup.getChildren().add(dialCircleBackground);
        dialsGroup.getChildren().add(globe);
        dialsGroup.getChildren().add(dialArcNight);
        dialsGroup.getChildren().add(dialArcMidnight);
        dialsGroup.getChildren().add(dialArcDayLength);
        dialsGroup.getChildren().add(matrixDayLength);
        dialsGroup.getChildren().add(dialCircleFrame);

        dialMarkerRotateList = new ArrayList<>();
        hourMarkerList = new ArrayList<>();

        for(int i = 0; i < MAX_MARKER; i++) {

            double lineLength = MARKER_DIAL_LENGTH;
            double strokeWidth = MARKER_STROKE_WIDTH;
            double opacity = 0.50d;

            if (i % 2 == 0) { lineLength = MARKER_DIAL_LENGTH * 1.5d; }
            if (i % 4 == 0) { lineLength = MARKER_DIAL_LENGTH * 2.0d;  opacity = 1; }
            if (i % 24 == 0) { lineLength = DIAL_HEIGHT / 2 - 20.0d; strokeWidth *= 1.2d;  }
            if (i % 48 == 0) { lineLength = MARKER_DIAL_LENGTH * 4.0d; }

            Group markerGroup = new Group();

            Rotate markerRotate = centerRotate.clone();
            markerRotate.setAngle(getNightCompressionAngle(i * 360d / 96d));

            Line markerLine = new Line(CENTER_X, lineLength  + MARGIN_Y, CENTER_X, MARGIN_Y);
            markerLine.setStroke(Color_Of_Darkness);
            markerLine.setStrokeWidth(strokeWidth);
            markerLine.setOpacity(opacity);
//            markerLine.getTransforms().add(markerRotate);
            markerGroup.getChildren().add(markerLine);

            if (i % 4 == 0) {

                DotMatrix markerMatrix = new DotMatrix("" + ((12 + i / 4) % 24), Color_Of_Darkness);
                markerMatrix.setTranslateX(CENTER_X - markerMatrix.getLayoutBounds().getWidth() / 2);
                markerMatrix.setTranslateY(MATRIX_MARKER_OFFSET);

                double rotationAdjust = i * -3.75d;
                markerMatrix.setRotate(rotationAdjust);
                markerMatrix.setScaleX(1.0d);
                markerMatrix.setScaleY(1.0d);
                markerGroup.getChildren().add(markerMatrix);

                hourMarkerList.add(markerMatrix);
            }

            markerGroup.getTransforms().add(markerRotate);

            dialsGroup.getChildren().add(markerGroup);
            dialMarkerRotateList.add(markerRotate);
        }

        dialsGroup.getChildren().add(sunTimeDial);
        dialsGroup.getChildren().add(highNoonDial);
        dialsGroup.getChildren().add(sunriseGroup);
        dialsGroup.getChildren().add(sunsetGroup);
        dialsGroup.getChildren().add(localTimeDial);
        dialsGroup.getChildren().add(dialCircleCenterDot);
        dialsGroup.getChildren().add(matrixTime);
        dialsGroup.getChildren().add(matrixDate);
        dialsGroup.getChildren().add(matrixWeek);
        dialsGroup.getChildren().add(coordinatesGroup);
//        dialsGroup.getChildren().add(matrixLongitude);
//        dialsGroup.getChildren().add(matrixLatitude);


        // Apply scale
        dialsGroup.setScaleX(SCALE_X);
        dialsGroup.setScaleY(SCALE_Y);
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

    private double getNightCompressionAngle(double angle) {

        double newAngle = angle;

        if (angle > 0 && angle <= 90) { newAngle = angle + angle * nightCompression / 90; }
        if (angle > 90 && angle <= 180) { newAngle = angle + (180 - angle) * nightCompression / 90; }
        if (angle > 180 && angle <= 270) { newAngle = angle - (angle - 180) * nightCompression / 90; }
        if (angle > 270 && angle <= 360) { newAngle = angle - (360 - angle) * nightCompression / 90; }

        return newAngle;
    }

    private void changeNightCompression(ScrollEvent event) {

        double offsetFactor = 0;

        if (event.getDeltaY() < 0) { offsetFactor = STEP_nightCompression; }
        else if (event.getDeltaY() > 0) { offsetFactor = -1 * STEP_nightCompression; }

        this.nightCompression += offsetFactor;

        if (this.nightCompression < MIN_nightCompression) {
            this.nightCompression = MIN_nightCompression;
            return;
        }
        if (this.nightCompression > MAX_nightCompression) {
            this.nightCompression = MAX_nightCompression;
            return;
        }

        updateRotations();
    }

    private void resetNightCompression() {
        if (this.nightCompression != 0) {
            this.nightCompression = 0;
            updateRotations();
        }
    }

    private void updateRotations() {
        setSunTimeDialAngle(getAngle(this.sunTime));
        setHighNoonDialAngle(getAngle(this.highNoon));
        setHorizonDialAngle(getAngle(this.sunrise), getAngle(this.sunset));
        setLocalTimeDialAngle(getAngle(this.localTime));
        updateDialMarkers();
    }

    private String getShortTime(GregorianCalendar calendar) {
        String hourString = ("00" + calendar.get(Calendar.HOUR_OF_DAY));
        hourString = hourString.substring(hourString.length() - 2, hourString.length());
        String minuteString = ("00" + calendar.get(Calendar.MINUTE));
        minuteString = minuteString.substring(minuteString.length() - 2, minuteString.length());
        String secondString = ("00" + calendar.get(Calendar.SECOND));
        secondString = secondString.substring(secondString.length() - 2, secondString.length());

        return hourString + ":" + minuteString + ":" + secondString;
    }

    // Getters
    public Group getDialsGroup() {
        return dialsGroup;
    }

    public Circle getDialCircleCenterDot() {
        return dialCircleCenterDot;
    }

    public Circle getDialCircleFrame() {
        return dialCircleFrame;
    }

    public Arc getDialArcNight() {
        return dialArcNight;
    }

    public Arc getDialArcMidnight() {
        return dialArcMidnight;
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

    public Circle getDialMarginCircle() {
        return dialMarginCircle;
    }

    public Rectangle getDialMarginFillBox() {
        return dialMarginFillBox;
    }

    public Rectangle getDialResizeBoxie() {
        return dialResizeBoxie;
    }

    public Rectangle getDialResetSizeBoxie() {
        return dialResetSizeBoxie;
    }

    public DotMatrix getMatrixDayLength() {
        return matrixDayLength;
    }

    public DotMatrix getMatrixLongitude() {
        return matrixLongitude;
    }

    public DotMatrix getMatrixLatitude() {
        return matrixLatitude;
    }

    public Globe getGlobe() {
        return globe;
    }

    public Group getCoordinatesGroup() {
        return coordinatesGroup;
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
        matrixSunrise.setString(getShortTime(this.sunrise));
        matrixSunset.setString(getShortTime(this.sunset));
    }

    public void setLocalTime(GregorianCalendar localTime) {
        this.localTime = localTime;
        setLocalTimeDialAngle(getAngle(this.localTime));
    }

    public void setSunTimeDialAngle(double sunTimeDialAngle) {
        this.sunTimeDialAngle = getNightCompressionAngle(sunTimeDialAngle);
        sunTimeDialRotate.setAngle(this.sunTimeDialAngle);
    }

    public void setHighNoonDialAngle(double highNoonDialAngle) {
        this.highNoonDialAngle = getNightCompressionAngle(highNoonDialAngle);
        highNoonDialRotate.setAngle(this.highNoonDialAngle);
    }

    public void setHorizonDialAngle(double sunriseDialAngle, double sunsetDialAngle) {

        this.sunriseDialAngle = getNightCompressionAngle(sunriseDialAngle);
        this.sunsetDialAngle = getNightCompressionAngle(sunsetDialAngle);

        sunriseDialRotate.setAngle(this.sunriseDialAngle);
        sunsetDialRotate.setAngle(this.sunsetDialAngle);

        dialArcNight.setStartAngle(90 - this.sunriseDialAngle);
        dialArcNight.setLength(-1 * (this.sunsetDialAngle - this.sunriseDialAngle));

        dialArcDayLength.setStartAngle(90 - (this.sunsetDialAngle - DAY_ARC_MARGIN));
        dialArcDayLength.setLength(360 - (this.sunriseDialAngle - this.sunsetDialAngle + (DAY_ARC_MARGIN * 2)));

        dialArcMidnight.setStartAngle(90 - getNightCompressionAngle(90));
        dialArcMidnight.setLength(-1 * (getNightCompressionAngle(270) - getNightCompressionAngle(90)));
    }

    public void setLocalTimeDialAngle(double localTimeDialAngle) {
        this.localTimeDialAngle = getNightCompressionAngle(localTimeDialAngle);
        localTimeDialRotate.setAngle(this.localTimeDialAngle);
    }

    public void updateDialMarkers() {
        int dialMarkerRotateListSize = dialMarkerRotateList.size();
        for (int i = 0; i < dialMarkerRotateListSize; i++) {
            dialMarkerRotateList.get(i).setAngle(getNightCompressionAngle(i * 360d / 96d));
            if (i % 4 == 0) {
                double angle = dialMarkerRotateList.get(i).getAngle();
                hourMarkerList.get(i / 4).setRotate(-1 * angle);
            }
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

    public void setGroupGlow(Group group, String style) {
        if (group == null || style == null) { return; }
        group.setStyle(style);
    }

    public void setGlobeVisibility(boolean isVisible) {

        if (isVisible) {
            dialCircleCenterDot.setScaleX(0.2);
            dialCircleCenterDot.setScaleY(0.2);

            dialArcMidnight.setOpacity(0.35);
            dialArcNight.setOpacity(0.35);

            globe.setVisible(true);
        } else {
            dialCircleCenterDot.setScaleX(1);
            dialCircleCenterDot.setScaleY(1);

            dialArcMidnight.setOpacity(1);
            dialArcNight.setOpacity(1);

            globe.setVisible(false);
        }
    }

}
