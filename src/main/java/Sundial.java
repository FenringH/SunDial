import javafx.animation.*;
import javafx.scene.*;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.text.DecimalFormat;
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

    public static final double DIAL_WIDTH = 440.0d;
    public static final double DIAL_HEIGHT = 440.0d;
    public static final double MARGIN_X = 25.0d;
    public static final double MARGIN_Y = 25.0d;
    private static final double SCALE_X = 1.0d;
    private static final double SCALE_Y = 1.0d;
    private static final double CENTER_X = DIAL_WIDTH / 2;
    private static final double CENTER_Y = DIAL_HEIGHT / 2;

    private static final double DOT_RADIUS = 10.0d;
    private static final double SUNTIME_DIAL_LENGTH = 50.0d;
    private static final double HIGHNOON_DIAL_LENGTH = 50.0d;
    private static final double SUNRISE_DIAL_LENGTH = DIAL_HEIGHT / 2 - DOT_RADIUS;
    private static final double SUNSET_DIAL_LENGTH = DIAL_HEIGHT / 2 - DOT_RADIUS;
    private static final double LOCALTIME_DIAL_LENGTH = DIAL_HEIGHT / 2 - DOT_RADIUS;
    private static final double DAYLENGTH_ARC_RADIUS = 105.0d;
    private static final double DAY_ARC_MARGIN = 10.0d;
    private static final double LOCALMINUTE_WIDTH = 8;
    private static final double LOCALMINUTE_HEIGHT = 16;
    private static final double LOCALMINUTE_ROUND = 6;
    private static final double LOCALSECOND_WIDTH = 8;
    private static final double LOCALSECOND_HEIGHT = 16;
    private static final double LOCALSECOND_ROUND = 6;
    private static final double TINYGLOBE_RADIUS = 35;
    private static final double CONTROL_RESIZE_SIZE = 45.0d;
    private static final double CONTROL_CLOSE_RADIUS = 10.0d;
    private static final double CONTROL_MAXIMIZE_RADIUS = 10.0d;
    private static final double CONTROL_MINIMIZE_RADIUS = 10.0d;

    private static final double DAYLENGTH_STROKE_WIDTH = 2.00d;
    private static final double SUNTIME_STROKE_WIDTH = 2.00d;
    private static final double HIGHNOON_STROKE_WIDTH = 2.00d;
    private static final double LOCALTIME_STROKE_WIDTH = 2.50d;
    private static final double SUNRISE_STROKE_WIDTH = 1.00d;
    private static final double SUNSET_STROKE_WIDTH = 1.00d;
    private static final double MARKER_HOUR_STROKE_WIDTH = 1.00d;
    private static final double MARKER_FRAME_STROKE_WIDTH = 2.00d;
    private static final double TINYGLOBE_FRAME_STROKE_WIDTH = 2.00d;
    private static final double CONTROL_RESIZE_STROKE_WIDTH = 3.00d;
    private static final double CONTROL_CLOSE_STROKE_WIDTH = 3.00d;
    private static final double CONTROL_MAXIMIZE_STROKE_WIDTH = 3.00d;
    private static final double CONTROL_MINIMIZE_STROKE_WIDTH = 3.00d;

    private static final double DAYLENGTH_ARC_OPACITY = 0.65d;
    private static final double MARGIN_CIRCLE_OPACITY = 0.65d;
    private static final double TINYGLOBE_FRAME_OPACITY = 0.60d;
    private static final double TINYGLOBE_OPACITY = 0.50d;
    private static final double MARKER_MINUTE_OPACITY = 0.1d;
    private static final double MATRIX_MINUTE_OPACITY = 0.2d;
    private static final double CONTROL_RESIZE_OPACITY = 0.75d;
    private static final double CONTROL_CLOSE_OPACITY = 0.75d;
    private static final double CONTROL_MAXIMIZE_OPACITY = 0.75d;
    private static final double CONTROL_MINIMIZE_OPACITY = 0.75d;

    private static final double MATRIX_MARKER_OFFSET = 6.5d;
    private static final double MATRIX_HORIZON_OFFSET = 77.0d;
    private static final double MATRIX_TIME_OFFSET = 0.0d;
    private static final double MATRIX_DATE_OFFSET = 45.0d;
    private static final double MATRIX_WEEK_OFFSET = 70.0d;
    private static final double MATRIX_LONGITUDE_OFFSET = 0.0d;
    private static final double MATRIX_LATITUDE_OFFSET = 25.0d;
    private static final double COORDINATES_OFFSET = 70.0d;
    private static final double LOCALHOUR_OFFSET = 105.0d;
    private static final double TINYGLOBE_OFFSET = 100.0d;
    private static final double LOCALMINUTE_OFFSET = 50.0d;
    private static final double LOCALSECOND_OFFSET = 50.0d;
    private static final double MATRIX_MINUTE_OFFSET = 70.0d;
    private static final double CONTROL_RESIZE_OFFSET = 136.0d;
    private static final double CONTROL_CLOSE_OFFSET = 238.0d;
    private static final double CONTROL_MAXIMIZE_OFFSET = 238.0d;
    private static final double CONTROL_MINIMIZE_OFFSET = 238.0d;

    private static final double MARKER_HOUR_LENGTH = 10.0d;
    private static final double MARKER_MINUTE_LENGTH = 8.0d;
    private static final double MARKER_MINUTE_WIDTH = 8.0d;
    private static final double MARKER_MINUTE_OFFSET = 65.0d;

    private static final double MATRIX_TIME_SCALE = 3.50d;
    private static final double MATRIX_DATE_SCALE = 1.50d;
    private static final double MATRIX_WEEK_SCALE = 1.00d;
    private static final double MATRIX_HORIZON_SCALE = 0.75d;
    private static final double MATRIX_DAYLENGTH_SCALE = 0.75d;
    private static final double MATRIX_LONGITUDE_SCALE = 1.25d;
    private static final double MATRIX_LATITUDE_SCALE = 1.25d;
    private static final double MATRIX_HOUR_SCALE = 1.00d;
    private static final double MATRIX_MINUTE_SCALE = 0.75d;

    private static final double CONTROL_CLOSE_ANGLE = 37.0d;
    private static final double CONTROL_MAXIMIZE_ANGLE = 53.0d;
    private static final double CONTROL_MINIMIZE_ANGLE = 45.0d;

    private static final int LED_COOLDOWN = 500;

    public static final Color Color_Of_Window     = new Color(0.65, 0.85, 0.85, 1.00);
    public static final Color Color_Of_Earth      = new Color(0.85, 0.85, 0.65, 1.00);
    public static final Color Color_Of_Darkness   = new Color(0.00, 0.00, 0.00, 1.00);
    public static final Color Color_Of_TextBack   = new Color(0.90, 0.90, 0.50, 1.00);
    public static final Color Color_Of_Void       = new Color(0.00, 0.00, 0.00, 0.00);
    public static final Color Color_Of_AlmostVoid = new Color(0.00, 0.00, 0.00, 0.35);

    public static final Color Color_Of_Nominal    = new Color(0.00, 0.65, 1.00, 0.35);
    public static final Color Color_Of_Warning    = new Color(1.00, 0.65, 0.00, 0.35);

    public static final Color Color_Of_DaySky     = new Color(0.50, 0.75, 1.00, 1.00);
    public static final Color Color_Of_NightSky   = new Color(0.50, 0.35, 1.00, 1.00);
//    public static final Color Color_Of_NightSky  = new Color(0.50, 0.35, 1.00, 0.50);
    public static final Color Color_Of_Midnight   = new Color(0.00, 0.00, 0.00, 0.20);
    public static final Color Color_Of_Margin     = new Color(0.15, 0.30, 0.70, 1.00);

    public static final Color Color_Of_SunTime    = new Color(1.00, 0.50, 0.00, 1.00);
    public static final Color Color_Of_HighNoon   = new Color(1.00, 1.00, 0.00, 1.00);
    public static final Color Color_Of_Horizon    = new Color(1.00, 0.90, 0.30, 1.00);
    public static final Color Color_Of_SunRise    = new Color(1.00, 0.00, 0.00, 1.00);
    public static final Color Color_Of_SunSet     = new Color(0.65, 0.00, 0.65, 1.00);
    public static final Color Color_Of_LocalTime  = new Color(1.00, 1.00, 1.00, 1.00);
    public static final Color Color_Of_TinyFrame  = new Color(1.00, 1.00, 1.00, 1.00);

    public static final Color Color_Of_Seconds    = new Color(1.00, 1.00, 1.00, 1.00);
    public static final Color Color_Of_Minutes    = new Color(1.00, 1.00, 1.00, 1.00);

    public static final Color Color_Of_ResizeFill   = new Color(0.25, 0.50, 1.00, 0.01);
    public static final Color Color_Of_ResizeStroke = new Color(0.50, 0.75, 1.00, 1.00);
    public static final Color Color_Of_CloseFill    = new Color(1.00, 0.25, 0.25, 0.01);
    public static final Color Color_Of_CloseStroke  = new Color(1.00, 0.50, 0.50, 1.00);
    public static final Color Color_Of_MaximizeFill   = new Color(0.25, 1.00, 0.25, 0.01);
    public static final Color Color_Of_MaximizeStroke = new Color(0.50, 1.00, 0.50, 1.00);
    public static final Color Color_Of_MinimizeFill   = new Color(1.00, 1.00, 0.00, 0.01);
    public static final Color Color_Of_MinimizeStroke = new Color(0.90, 0.90, 0.20, 1.00);

    public static final String MATRIX_GLOW             = "-fx-effect: dropshadow(three-pass-box, rgba(255,128, 32, 1.0),  4.0, 0.50, 0, 0);";
    public static final String MATRIX_GLOW2            = "-fx-effect: dropshadow(three-pass-box, rgba(255,128, 32, 1.0), 10.0, 0.50, 0, 0);";
    public static final String MATRIX_SHADOW           = "-fx-effect: dropshadow(three-pass-box, rgba( 32,128,255, 1.0),  4.0, 0.50, 0, 0);";
    public static final String MATRIX_SHADOW2          = "-fx-effect: dropshadow(three-pass-box, rgba( 32,128,255, 1.0), 10.0, 0.50, 0, 0);";
    public static final String MATRIX_BLOCK            = "-fx-effect: dropshadow(three-pass-box, rgba(  0,  0,  0, 1.0), 10.0, 0.50, 0, 0);";
    public static final String HORIZON_GLOW            = "-fx-effect: dropshadow(three-pass-box, rgba(255, 96, 32, 1.0), 15.0, 0.87, 0, 0);";
    public static final String LOCALTIME_SHADOW        = "-fx-effect: dropshadow(three-pass-box, rgba( 32,128,255, 1.0), 15.0, 0.50, 0, 0);";
    public static final String LOCALSECOND_GLOW        = "-fx-effect: dropshadow(three-pass-box, rgba(255,  0,  0, 1.0), 10.0, 0.60, 0, 0);";
    public static final String LOCALMINUTE_GLOW        = "-fx-effect: dropshadow(three-pass-box, rgba(  0,255,  0, 1.0), 10.0, 0.60, 0, 0);";
    public static final String LOCALTIME_GLOW          = "-fx-effect: dropshadow(three-pass-box, rgba(  0,  0,255, 1.0), 10.0, 0.60, 0, 0);";

    public static final String HORIZON_HOVER_GLOW      = "-fx-effect: dropshadow(three-pass-box, rgba(255,128, 32, 0.5), 4.0, 0.50, 0, 0);";

    public static final String CONTROL_RESIZE_SHADOW   = "-fx-effect: dropshadow(three-pass-box, rgba( 32,128,255, 1.0),  4.0, 0.50, 0, 0);";
    public static final String CONTROL_RESIZE_GLOW     = "-fx-effect: dropshadow(three-pass-box, rgba(255,128, 32, 1.0),  4.0, 0.50, 0, 0);";
    public static final String CONTROL_CLOSE_SHADOW    = "-fx-effect: dropshadow(three-pass-box, rgba(128, 32, 32, 1.0),  4.0, 0.50, 0, 0);";
    public static final String CONTROL_CLOSE_GLOW      = "-fx-effect: dropshadow(three-pass-box, rgba(255, 64, 64, 1.0),  4.0, 0.50, 0, 0);";
    public static final String CONTROL_MAXIMIZE_SHADOW = "-fx-effect: dropshadow(three-pass-box, rgba( 32,128, 32, 1.0),  4.0, 0.50, 0, 0);";
    public static final String CONTROL_MAXIMIZE_GLOW   = "-fx-effect: dropshadow(three-pass-box, rgba( 64,255, 64, 1.0),  4.0, 0.50, 0, 0);";
    public static final String CONTROL_MINIMIZE_SHADOW = "-fx-effect: dropshadow(three-pass-box, rgba(112,112, 32, 1.0),  4.0, 0.50, 0, 0);";
    public static final String CONTROL_MINIMIZE_GLOW   = "-fx-effect: dropshadow(three-pass-box, rgba(224,224, 64, 1.0),  4.0, 0.50, 0, 0);";

    private static final Image GLOBE_IMAGE = new Image("maps/earth_diffuse_gall-peters_02.jpg",
            1003, 639, true, false);
//    private static final Image TINYGLOBE_IMAGE = new Image("maps/tinyGlobe_diffuse_02.png",
//            800, 508, true, false);
    private static final Image TINYGLOBE_IMAGE = new Image("maps/earth_diffuse_gall-peters_02.jpg",
        1003, 639, true, false);


    private static final RadialGradient WARNING_RADIAL_GRADIENT = new RadialGradient(
            0, 0,
            CENTER_X, CENTER_Y, CENTER_Y - MARGIN_Y,
            false,
            CycleMethod.NO_CYCLE,
            new Stop(0.75, Color_Of_Void),
            new Stop(1.00, Color_Of_Warning)
    );

    private static final RadialGradient NOMINAL_RADIAL_GRADIENT = new RadialGradient(
            0, 0,
            CENTER_X, CENTER_Y, CENTER_Y - MARGIN_Y,
            false,
            CycleMethod.NO_CYCLE,
            new Stop(0.75, Color_Of_Void),
            new Stop(1.00, Color_Of_Nominal)
    );

    private static final RadialGradient MINUTE_MARKER_GRADIENT = new RadialGradient(
            0, 0,
            LOCALMINUTE_WIDTH / 2, LOCALMINUTE_HEIGHT / 2, LOCALMINUTE_HEIGHT,
            false,
            CycleMethod.NO_CYCLE,
            new Stop(0.10, Color_Of_AlmostVoid),
            new Stop(0.50, Color_Of_Darkness)
    );

    private static final RadialGradient TINYGLOBE_FRAME_GRADIENT = new RadialGradient(
            0, 0,
            0, 0, TINYGLOBE_RADIUS,
            false,
            CycleMethod.NO_CYCLE,
            new Stop(0.80, Color_Of_Void),
            new Stop(1.00, Color_Of_TinyFrame)
    );

    private static final Font Font_Of_Info = new Font("Lucida Console", 14);
    private static final Font Font_Of_Dial = new Font("Lucida Console", 8);

    private static final String Path_Of_Earth = "M 100 100 L 300 100 L 200 300 Z M 150 150 L 100 250 L 350 150 Z";

    // variables
    private double sunTimeDialAngle;
    private double highNoonDialAngle;
    private double sunriseDialAngle;
    private double sunsetDialAngle;
    private double dialAngleLocalHour;
    private double dialAngleLocalMinute;
    private double dialAngleLocalSecond;
    private String localTimeText;
    private double nightCompression;
    private boolean warning;

    private GregorianCalendar sunTime;
    private GregorianCalendar highNoon;
    private GregorianCalendar sunrise;
    private GregorianCalendar sunset;
    private GregorianCalendar localTime;
    private long daylength;                 // length of day in seconds

    // graphical primitives
    private Group dialsGroup;

    private Rectangle dialBox;
    private Rectangle dialMarginBox;
    private Rectangle dialMarginFillBox;
    private Circle dialMarginCircle;

    private Rotate centerRotate;
    private Rotate sunTimeDialRotate;
    private Rotate highNoonDialRotate;
    private Rotate sunriseDialRotate;
    private Rotate sunsetDialRotate;
    private Rotate dialRotateLocalHour;
    private Rotate dialRotateLocalMinute;
    private Rotate dialRotateLocalSecond;
    private Rotate dialRotateLocalSecondTrail;
    private ArrayList<Rotate> dialMarkerRotateList;
    private ArrayList<DotMatrix> hourMarkerList;

    private Arc dialArcNight;
    private Arc dialArcMidnight;
    private Arc dialArcDayLength;

    private Circle dialCircleBackground;
    private Circle dialCircleFrame;
    private Circle dialCircleCenterDot;
    private Line sunTimeDial;
    private Line dialLineHighNoon;
    private Line sunriseDial;
    private Line sunsetDial;
    private Line dialLineLocalHour;
    private Rectangle dialLineLocalMinute;

    private ArrayList<Rectangle> dialLocalSecondList;
    private ArrayList<Boolean> dialLocalSecondOn;
    private ArrayList<Rectangle> dialLocalMinuteList;
    private ArrayList<Boolean> dialLocalMinuteOn;
    private ArrayList<Timeline> dialLocalSecondTransitionList;
    private ArrayList<Timeline> dialLocalMinuteTransitionList;

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
    private Globe tinyGlobe;
    private Circle tinyGlobeFrame;
    private Circle tinyGlobeDot;
    private Group coordinatesGroup;
    private Group horizonGroup;

    private Group controlThingyResize;
    private Group controlThingyClose;
    private Group controlThingyMaximize;
    private Group controlThingyMinimize;

    public boolean globeVisibleEh = false;


    // Constructor
    public Sundial(Builder builder) {
        this.sunTimeDialAngle = builder.sunTimeDialAngle;
        this.highNoonDialAngle = builder.highNoonDialAngle;
        this.sunriseDialAngle = builder.sunriseDialAngle;
        this.sunsetDialAngle = builder.sunsetDialAngle;
        this.dialAngleLocalHour = builder.localTimeDialAngle;
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

        // Rotates
        centerRotate = new Rotate();
        centerRotate.setPivotX(CENTER_X);
        centerRotate.setPivotY(CENTER_Y);

        sunTimeDialRotate = centerRotate.clone();
        highNoonDialRotate = centerRotate.clone();
        sunriseDialRotate = centerRotate.clone();
        sunsetDialRotate = centerRotate.clone();
        dialRotateLocalHour = centerRotate.clone();
        dialRotateLocalMinute = centerRotate.clone();
        dialRotateLocalSecond = centerRotate.clone();
        dialRotateLocalSecondTrail = centerRotate.clone();

        sunTimeDialRotate.setAngle(getNightCompressionAngle(sunTimeDialAngle));
        highNoonDialRotate.setAngle(getNightCompressionAngle(highNoonDialAngle));
        sunriseDialRotate.setAngle(getNightCompressionAngle(sunriseDialAngle));
        sunsetDialRotate.setAngle(getNightCompressionAngle(sunsetDialAngle));
        dialRotateLocalHour.setAngle(getNightCompressionAngle(dialAngleLocalHour));
        dialRotateLocalMinute.setAngle(getNightCompressionAngle(dialAngleLocalMinute));
        dialRotateLocalSecond.setAngle(getNightCompressionAngle(dialAngleLocalSecond));

        // Control thingies
        controlThingyResize = new Group();
        Polygon thingyResizeTriangle = new Polygon(
                CONTROL_RESIZE_SIZE, 0,
                CONTROL_RESIZE_SIZE, CONTROL_RESIZE_SIZE,
                0, CONTROL_RESIZE_SIZE
        );
        thingyResizeTriangle.setFill(Color_Of_ResizeFill);
        thingyResizeTriangle.setStroke(Color_Of_ResizeStroke);
        thingyResizeTriangle.setStrokeWidth(CONTROL_RESIZE_STROKE_WIDTH);
        controlThingyResize.getChildren().add(thingyResizeTriangle);
        controlThingyResize.setTranslateX(CENTER_X + CONTROL_RESIZE_OFFSET);
        controlThingyResize.setTranslateY(CENTER_Y + CONTROL_RESIZE_OFFSET);
        controlThingyResize.setStyle(CONTROL_RESIZE_SHADOW);
        controlThingyResize.setOpacity(CONTROL_RESIZE_OPACITY);

        controlThingyClose = new Group();
        Circle thingyCloseCircle = new Circle(0, 0, CONTROL_CLOSE_RADIUS);
        thingyCloseCircle.setFill(Color_Of_CloseFill);
        thingyCloseCircle.setStroke(Color_Of_CloseStroke);
        thingyCloseCircle.setStrokeWidth(CONTROL_CLOSE_STROKE_WIDTH);
        controlThingyClose.getChildren().add(thingyCloseCircle);
        controlThingyClose.setTranslateX(CENTER_X + CONTROL_CLOSE_OFFSET * cos(toRadians(CONTROL_CLOSE_ANGLE)));
        controlThingyClose.setTranslateY(CENTER_Y - CONTROL_CLOSE_OFFSET * sin(toRadians(CONTROL_CLOSE_ANGLE)));
        controlThingyClose.setStyle(CONTROL_CLOSE_SHADOW);
        controlThingyClose.setOpacity(CONTROL_CLOSE_OPACITY);

        controlThingyMaximize = new Group();
        Circle thingyMaximizeCircle = new Circle(0, 0, CONTROL_MAXIMIZE_RADIUS);
        thingyMaximizeCircle.setFill(Color_Of_MaximizeFill);
        thingyMaximizeCircle.setStroke(Color_Of_MaximizeStroke);
        thingyMaximizeCircle.setStrokeWidth(CONTROL_MAXIMIZE_STROKE_WIDTH);
        controlThingyMaximize.getChildren().add(thingyMaximizeCircle);
        controlThingyMaximize.setTranslateX(CENTER_X + CONTROL_MAXIMIZE_OFFSET * cos(toRadians(CONTROL_MAXIMIZE_ANGLE)));
        controlThingyMaximize.setTranslateY(CENTER_Y - CONTROL_MAXIMIZE_OFFSET * sin(toRadians(CONTROL_MAXIMIZE_ANGLE)));
        controlThingyMaximize.setStyle(CONTROL_MAXIMIZE_SHADOW);
        controlThingyMaximize.setOpacity(CONTROL_MAXIMIZE_OPACITY);

        controlThingyMinimize = new Group();
        Circle thingyMinimizeCircle = new Circle(0, 0, CONTROL_MINIMIZE_RADIUS);
        thingyMinimizeCircle.setFill(Color_Of_MinimizeFill);
        thingyMinimizeCircle.setStroke(Color_Of_MinimizeStroke);
        thingyMinimizeCircle.setStrokeWidth(CONTROL_MINIMIZE_STROKE_WIDTH);
        controlThingyMinimize.getChildren().add(thingyMinimizeCircle);
        controlThingyMinimize.setTranslateX(CENTER_X + CONTROL_MINIMIZE_OFFSET * cos(toRadians(CONTROL_MINIMIZE_ANGLE)));
        controlThingyMinimize.setTranslateY(CENTER_Y - CONTROL_MINIMIZE_OFFSET * sin(toRadians(CONTROL_MINIMIZE_ANGLE)));
        controlThingyMinimize.setStyle(CONTROL_MINIMIZE_SHADOW);
        controlThingyMinimize.setOpacity(CONTROL_MINIMIZE_OPACITY);

        // Dials in a box
        globe = new Globe(GLOBE_IMAGE,CENTER_X - MARGIN_X);
        globe.setLayoutX(CENTER_X);
        globe.setLayoutY(CENTER_Y);
        globe.setVisible(false);

        tinyGlobeFrame = new Circle(TINYGLOBE_RADIUS);
        tinyGlobeFrame.setLayoutX(CENTER_X);
        tinyGlobeFrame.setLayoutY(CENTER_Y + TINYGLOBE_OFFSET);
        tinyGlobeFrame.setFill(Color_Of_Void);
        tinyGlobeFrame.setStroke(Color_Of_TinyFrame);
        tinyGlobeFrame.setStrokeWidth(TINYGLOBE_FRAME_STROKE_WIDTH);
        tinyGlobeFrame.setOpacity(TINYGLOBE_FRAME_OPACITY);
        tinyGlobeFrame.setStyle(MATRIX_SHADOW);
        tinyGlobeFrame.setVisible(true);

        tinyGlobeDot = new Circle(1.0d);
        tinyGlobeDot.setLayoutX(CENTER_X);
        tinyGlobeDot.setLayoutY(CENTER_Y + TINYGLOBE_OFFSET);
        tinyGlobeDot.setFill(Color_Of_TinyFrame);
        tinyGlobeDot.setStroke(Color_Of_Void);
        tinyGlobeDot.setOpacity(TINYGLOBE_FRAME_OPACITY);

        tinyGlobe = new Globe(TINYGLOBE_IMAGE, TINYGLOBE_RADIUS);
        tinyGlobe.setLayoutX(CENTER_X);
        tinyGlobe.setLayoutY(CENTER_Y + TINYGLOBE_OFFSET);
        tinyGlobe.setVisible(true);

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

        dialMarginCircle = new Circle(CENTER_X, CENTER_Y, DIAL_WIDTH / 2);
        dialMarginCircle.setFill(Color_Of_Margin);
        dialMarginCircle.setStroke(Color_Of_Void);
        dialMarginCircle.setOpacity(MARGIN_CIRCLE_OPACITY);

        dialCircleBackground = new Circle(CENTER_X, CENTER_Y, DIAL_WIDTH / 2 - MARGIN_X);
        dialCircleBackground.setFill(Color_Of_DaySky);
        dialCircleBackground.setStroke(Color_Of_Void);
        dialCircleBackground.setStyle(MATRIX_SHADOW);

        dialCircleFrame = new Circle(CENTER_X, CENTER_Y, DIAL_WIDTH / 2 - MARGIN_X);
        dialCircleFrame.setFill(NOMINAL_RADIAL_GRADIENT);
        dialCircleFrame.setStroke(Color_Of_Void);
        dialCircleFrame.setStrokeWidth(MARKER_FRAME_STROKE_WIDTH);


        Group dialMinuteMarkers = new Group();

        for(int i = 0; i < 60; i++) {

            Group markerGroup = new Group();

            double opacity = MARKER_MINUTE_OPACITY;

            Rectangle markerMinute = new Rectangle(LOCALMINUTE_WIDTH, LOCALMINUTE_HEIGHT);
            markerMinute.setArcWidth(LOCALMINUTE_ROUND);
            markerMinute.setArcHeight(LOCALMINUTE_ROUND);
            markerMinute.setTranslateX(CENTER_X - LOCALMINUTE_WIDTH / 2);
            markerMinute.setTranslateY(LOCALMINUTE_OFFSET);
            markerMinute.setFill(MINUTE_MARKER_GRADIENT);
//            markerMinute.setStroke(Color.BLACK);
//            markerMinute.setStrokeWidth(0.5d);
            markerMinute.setOpacity(opacity);
//            markerMinute.setBlendMode(BlendMode.OVERLAY);

            markerGroup.getChildren().add(markerMinute);

            if (i % 5 == 0) {
                DotMatrix markerMatrix = new DotMatrix("" + i, Color_Of_Darkness);
                markerMatrix.setTranslateX(CENTER_X - markerMatrix.getLayoutBounds().getWidth() / 2);
                markerMatrix.setTranslateY(MATRIX_MINUTE_OFFSET);
                markerMatrix.setOpacity(MATRIX_MINUTE_OPACITY);

                double rotationAdjust = i * -6;
                markerMatrix.setRotate(rotationAdjust);
                markerMatrix.setScaleX(MATRIX_MINUTE_SCALE);
                markerMatrix.setScaleY(MATRIX_MINUTE_SCALE);
                markerGroup.getChildren().add(markerMatrix);
            }

            Rotate markerHourRotate = new Rotate();
            markerHourRotate.setPivotX(CENTER_X);
            markerHourRotate.setPivotY(CENTER_Y);
            markerHourRotate.setAngle(i * 360d / 60d);

            markerGroup.getTransforms().add(markerHourRotate);

            dialMinuteMarkers.getChildren().add(markerGroup);
        }


        Group dialHourMarkers = new Group();
        dialMarkerRotateList = new ArrayList<>();
        hourMarkerList = new ArrayList<>();

        for(int i = 0; i < MAX_MARKER; i++) {

            double lineLength = MARKER_HOUR_LENGTH;
            double strokeWidth = MARKER_HOUR_STROKE_WIDTH;
            double opacity = 0.50d;

            if (i % 2 == 0) { lineLength = MARKER_HOUR_LENGTH * 1.5d; }
            if (i % 4 == 0) { lineLength = MARKER_HOUR_LENGTH * 2.0d;  opacity = 1; }
//            if ((i + 24) % 48 == 0) { lineLength = CENTER_X - MARGIN_X - DOT_RADIUS; strokeWidth *= 1.0d;  }
//            if (i % 48 == 0) { lineLength = MARKER_HOUR_LENGTH * 4.0d; }

            Group markerGroup = new Group();

            Rotate markerRotate = centerRotate.clone();
            markerRotate.setAngle(getNightCompressionAngle(i * 360d / 96d));

            Line markerLine = new Line(CENTER_X, lineLength  + MARGIN_Y, CENTER_X, MARGIN_Y + 1);
            markerLine.setStroke(Color_Of_Darkness);
            markerLine.setStrokeWidth(strokeWidth);
            markerLine.setOpacity(opacity);
//            markerLine.getTransforms().add(markerRotate);
            markerGroup.getChildren().add(markerLine);

            if (i % 4 == 0) {

                DotMatrix markerMatrix = new DotMatrix("" + ((12 + i / 4) % 24), Color_Of_LocalTime);
                markerMatrix.setTranslateX(CENTER_X - markerMatrix.getLayoutBounds().getWidth() / 2);
                markerMatrix.setTranslateY(MATRIX_MARKER_OFFSET);
                markerMatrix.setStyle(MATRIX_SHADOW);

                double rotationAdjust = i * -3.75d;
                markerMatrix.setRotate(rotationAdjust);
                markerMatrix.setScaleX(MATRIX_HOUR_SCALE);
                markerMatrix.setScaleY(MATRIX_HOUR_SCALE);
                markerGroup.getChildren().add(markerMatrix);

                hourMarkerList.add(markerMatrix);
            }

            markerGroup.getTransforms().add(markerRotate);

            dialHourMarkers.getChildren().add(markerGroup);
            dialMarkerRotateList.add(markerRotate);
        }

        Circle dialCircleCenterPoint = new Circle(CENTER_X, CENTER_Y, 1);
        dialCircleCenterPoint.setFill(Color_Of_LocalTime);
        dialCircleCenterPoint.setStroke(Color_Of_Void);

        dialCircleCenterDot = new Circle(CENTER_X, CENTER_Y, DOT_RADIUS);
        dialCircleCenterDot.setFill(Color_Of_LocalTime);
        dialCircleCenterDot.setStroke(Color_Of_Void);
        dialCircleCenterDot.setStyle(MATRIX_SHADOW2);

        sunTimeDial = new Line(CENTER_X, SUNTIME_DIAL_LENGTH, CENTER_X, MARGIN_Y);
        sunTimeDial.setStroke(Color_Of_SunTime);
        sunTimeDial.setStrokeWidth(SUNTIME_STROKE_WIDTH);
        sunTimeDial.getTransforms().add(sunTimeDialRotate);

        dialLineHighNoon = new Line(CENTER_X, HIGHNOON_DIAL_LENGTH, CENTER_X, MARGIN_Y);
        dialLineHighNoon.setStroke(Color_Of_HighNoon);
        dialLineHighNoon.setStrokeWidth(HIGHNOON_STROKE_WIDTH);
        dialLineHighNoon.getTransforms().add(highNoonDialRotate);

        dialLineLocalHour = new Line(CENTER_X, LOCALTIME_DIAL_LENGTH, CENTER_X, MARGIN_Y);
        dialLineLocalHour.setStroke(Color_Of_LocalTime);
        dialLineLocalHour.setStrokeWidth(LOCALTIME_STROKE_WIDTH);
        dialLineLocalHour.setStyle(LOCALTIME_SHADOW);
        dialLineLocalHour.getTransforms().add(dialRotateLocalHour);
        dialLineLocalHour.setBlendMode(BlendMode.ADD);

        dialLineLocalMinute = new Rectangle(LOCALMINUTE_WIDTH, LOCALMINUTE_HEIGHT);
        dialLineLocalMinute.setArcWidth(LOCALMINUTE_ROUND);
        dialLineLocalMinute.setArcHeight(LOCALMINUTE_ROUND);
        dialLineLocalMinute.setTranslateX(CENTER_X - LOCALMINUTE_WIDTH / 2);
        dialLineLocalMinute.setTranslateY(LOCALMINUTE_OFFSET);
        dialLineLocalMinute.setFill(Color.WHITE);
        dialLineLocalMinute.setStroke(Color_Of_Void);
        dialLineLocalMinute.setStyle(LOCALMINUTE_GLOW);
        dialLineLocalMinute.setBlendMode(BlendMode.SCREEN);
        dialRotateLocalMinute.setPivotX(LOCALMINUTE_WIDTH / 2);
        dialRotateLocalMinute.setPivotY(CENTER_Y - LOCALMINUTE_OFFSET);
        dialLineLocalMinute.getTransforms().add(dialRotateLocalMinute);

        dialLocalSecondList = new ArrayList<>();
        dialLocalSecondOn = new ArrayList<>();
        dialLocalMinuteList = new ArrayList<>();
        dialLocalMinuteOn = new ArrayList<>();
        dialLocalSecondTransitionList = new ArrayList<>();
        dialLocalMinuteTransitionList = new ArrayList<>();

        for (int i = 0; i < 60; i++) {

            Rectangle localSecond = new Rectangle(LOCALSECOND_WIDTH, LOCALSECOND_HEIGHT);
            localSecond.setArcWidth(LOCALSECOND_ROUND);
            localSecond.setArcHeight(LOCALSECOND_ROUND);
            localSecond.setTranslateX(CENTER_X - LOCALSECOND_WIDTH / 2);
            localSecond.setTranslateY(LOCALSECOND_OFFSET);
            localSecond.setFill(Color_Of_Seconds);
            localSecond.setStroke(Color_Of_Void);
            localSecond.setStyle(LOCALSECOND_GLOW);
            localSecond.setBlendMode(BlendMode.SCREEN);
//            localSecond.setFill(Color_Of_Void);
            localSecond.setOpacity(0.0);

            Rotate localSecondRotate = new Rotate();
            localSecondRotate.setPivotX(LOCALSECOND_WIDTH / 2);
            localSecondRotate.setPivotY(CENTER_Y - LOCALSECOND_OFFSET);
            localSecondRotate.setAngle(i * 6);

            localSecond.getTransforms().add(localSecondRotate);

            Timeline timelineSecond = createTimelineForLED(localSecond);

            Rectangle localMinute = new Rectangle(LOCALMINUTE_WIDTH, LOCALMINUTE_HEIGHT);
            localMinute.setArcWidth(LOCALMINUTE_ROUND);
            localMinute.setArcHeight(LOCALMINUTE_ROUND);
            localMinute.setTranslateX(CENTER_X - LOCALMINUTE_WIDTH / 2);
            localMinute.setTranslateY(LOCALMINUTE_OFFSET);
            localMinute.setFill(Color_Of_Minutes);
            localMinute.setStroke(Color_Of_Void);
            localMinute.setStyle(LOCALMINUTE_GLOW);
            localMinute.setBlendMode(BlendMode.SCREEN);
//            localMinute.setFill(Color_Of_Void);
            localMinute.setOpacity(0.0);

            Rotate localMinuteRotate = new Rotate();
            localMinuteRotate.setPivotX(LOCALMINUTE_WIDTH / 2);
            localMinuteRotate.setPivotY(CENTER_Y - LOCALMINUTE_OFFSET);
            localMinuteRotate.setAngle(i * 6);

            localMinute.getTransforms().add(localMinuteRotate);

            Timeline timelineMinute = createTimelineForLED(localMinute);

            dialLocalSecondList.add(localSecond);
            dialLocalMinuteList.add(localMinute);

            dialLocalSecondOn.add(false);
            dialLocalMinuteOn.add(false);

            dialLocalSecondTransitionList.add(timelineSecond);
            dialLocalMinuteTransitionList.add(timelineMinute);
        }

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

        horizonGroup = new Group();
        horizonGroup.getChildren().addAll(sunriseGroup, sunsetGroup);

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


        matrixLongitude= new DotMatrix("000.00E", Color_Of_LocalTime);
        matrixLongitude.setScaleX(MATRIX_LONGITUDE_SCALE);
        matrixLongitude.setScaleY(MATRIX_LONGITUDE_SCALE);
        matrixLongitude.setLayoutX(CENTER_X - matrixLongitude.getLayoutBounds().getWidth() / 2);
        matrixLongitude.setLayoutY(CENTER_Y + matrixLongitude.getLayoutBounds().getHeight() + MATRIX_LONGITUDE_OFFSET);

        matrixLatitude= new DotMatrix("000.00N", Color_Of_LocalTime);
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
        coordinatesGroup.setLayoutY(COORDINATES_OFFSET);
        coordinatesGroup.setVisible(false);


        setGroupGlow(matrixYear, MATRIX_SHADOW);
        setGroupGlow(matrixMonth, MATRIX_SHADOW);
        setGroupGlow(matrixDay, MATRIX_SHADOW);
        setGroupGlow(matrixHour, MATRIX_SHADOW);
        setGroupGlow(matrixMinute, MATRIX_SHADOW);
        setGroupGlow(matrixSecond, MATRIX_SHADOW);
        setGroupGlow(matrixWeek, MATRIX_SHADOW);
        setGroupGlow(coordinatesGroup, MATRIX_SHADOW);

        matrixSeparatorDayToMonth.setStyle(MATRIX_SHADOW);
        matrixSeparatorHourToMinute.setStyle(MATRIX_SHADOW);
        matrixSeparatorMinuteToSecond.setStyle(MATRIX_SHADOW);
        matrixSeparatorMonthToYear.setStyle(MATRIX_SHADOW);


        // LAYERS
        dialsGroup = new Group();

        Group backgroundGroup = new Group();
        backgroundGroup.getChildren().add(dialMarginFillBox);
        backgroundGroup.getChildren().add(controlThingyResize);
        backgroundGroup.getChildren().add(controlThingyClose);
        backgroundGroup.getChildren().add(controlThingyMaximize);
        backgroundGroup.getChildren().add(controlThingyMinimize);
        backgroundGroup.getChildren().add(dialMarginCircle);
        SubScene backgroundScene = new SubScene(backgroundGroup, DIAL_WIDTH, DIAL_HEIGHT, true, SceneAntialiasing.DISABLED);

        Group globeGroup = new Group();
        globeGroup.getChildren().add(globe);
        SubScene globeScene = new SubScene(globeGroup, DIAL_WIDTH, DIAL_HEIGHT, true, SceneAntialiasing.BALANCED);

        Group tinyGlobeGroup = new Group();
        tinyGlobeGroup.getChildren().add(tinyGlobe);
        SubScene tinyGlobeScene = new SubScene(tinyGlobeGroup, DIAL_WIDTH, DIAL_HEIGHT, true, SceneAntialiasing.BALANCED);
        tinyGlobeScene.setOpacity(TINYGLOBE_OPACITY);

        Group foregroundGroup = new Group();
        foregroundGroup.getChildren().add(globeScene);
        foregroundGroup.getChildren().add(dialCircleBackground);
        foregroundGroup.getChildren().add(dialArcNight);
        foregroundGroup.getChildren().add(dialArcMidnight);
        foregroundGroup.getChildren().add(dialMinuteMarkers);
        foregroundGroup.getChildren().addAll(dialLocalSecondList);
        foregroundGroup.getChildren().addAll(dialLocalMinuteList);
        foregroundGroup.getChildren().add(dialHourMarkers);
        foregroundGroup.getChildren().add(dialArcDayLength);
        foregroundGroup.getChildren().add(matrixDayLength);
        foregroundGroup.getChildren().add(dialCircleFrame);
        foregroundGroup.getChildren().add(tinyGlobeScene);
        foregroundGroup.getChildren().add(sunTimeDial);
        foregroundGroup.getChildren().add(dialLineHighNoon);
//        foregroundGroup.getChildren().add(sunriseGroup);
//        foregroundGroup.getChildren().add(sunsetGroup);
        foregroundGroup.getChildren().add(horizonGroup);
        foregroundGroup.getChildren().add(dialLineLocalHour);
        foregroundGroup.getChildren().add(tinyGlobeDot);
        foregroundGroup.getChildren().add(tinyGlobeFrame);
        foregroundGroup.getChildren().add(dialCircleCenterPoint);
        foregroundGroup.getChildren().add(dialCircleCenterDot);
        foregroundGroup.getChildren().add(matrixTime);
        foregroundGroup.getChildren().add(matrixDate);
//        foregroundGroup.getChildren().add(matrixWeek);
        foregroundGroup.getChildren().add(coordinatesGroup);
        SubScene foregroundScene = new SubScene(foregroundGroup, DIAL_WIDTH, DIAL_HEIGHT, true, SceneAntialiasing.DISABLED);

        dialsGroup.getChildren().addAll(backgroundScene, foregroundScene);

        // Apply scale global scale
        dialsGroup.setScaleX(SCALE_X);
        dialsGroup.setScaleY(SCALE_Y);


        // EVENTS
        dialCircleCenterDot.setOnMouseEntered(event -> { dialCircleCenterDot.setCursor(Cursor.V_RESIZE); dialCircleCenterDot.setStyle(MATRIX_GLOW2); });
        dialCircleCenterDot.setOnMouseExited(event -> { dialCircleCenterDot.setCursor(Cursor.DEFAULT); dialCircleCenterDot.setStyle(MATRIX_SHADOW2); });

        controlThingyResize.setOnMouseEntered(event -> { controlThingyResize.setCursor(Cursor.NW_RESIZE); setGroupGlow(controlThingyResize, CONTROL_RESIZE_GLOW); });
        controlThingyResize.setOnMouseExited(event -> { controlThingyResize.setCursor(Cursor.DEFAULT); setGroupGlow(controlThingyResize, CONTROL_RESIZE_SHADOW); });

        controlThingyClose.setOnMouseEntered(event -> { controlThingyClose.setCursor(Cursor.CROSSHAIR); setGroupGlow(controlThingyClose, CONTROL_CLOSE_GLOW); });
        controlThingyClose.setOnMouseExited(event -> { controlThingyClose.setCursor(Cursor.DEFAULT); setGroupGlow(controlThingyClose, CONTROL_CLOSE_SHADOW); });

        controlThingyMaximize.setOnMouseEntered(event -> { controlThingyMaximize.setCursor(Cursor.N_RESIZE); setGroupGlow(controlThingyMaximize, CONTROL_MAXIMIZE_GLOW); });
        controlThingyMaximize.setOnMouseExited(event -> { controlThingyMaximize.setCursor(Cursor.DEFAULT); setGroupGlow(controlThingyMaximize, CONTROL_MAXIMIZE_SHADOW); });

        controlThingyMinimize.setOnMouseEntered(event -> { controlThingyMinimize.setCursor(Cursor.HAND); setGroupGlow(controlThingyMinimize, CONTROL_MINIMIZE_GLOW); });
        controlThingyMinimize.setOnMouseExited(event -> { controlThingyMinimize.setCursor(Cursor.DEFAULT); setGroupGlow(controlThingyMinimize, CONTROL_MINIMIZE_SHADOW); });

        matrixYear.setOnMouseEntered(event -> { matrixYear.setCursor(Cursor.V_RESIZE); setGroupGlow(matrixYear, MATRIX_GLOW); });
        matrixYear.setOnMouseExited(event -> { matrixYear.setCursor(Cursor.DEFAULT); setGroupGlow(matrixYear, MATRIX_SHADOW); });

        matrixMonth.setOnMouseEntered(event -> { matrixMonth.setCursor(Cursor.V_RESIZE); setGroupGlow(matrixMonth, MATRIX_GLOW); });
        matrixMonth.setOnMouseExited(event -> { matrixMonth.setCursor(Cursor.DEFAULT); setGroupGlow(matrixMonth, MATRIX_SHADOW); });

        matrixDay.setOnMouseEntered(event -> { matrixDay.setCursor(Cursor.V_RESIZE); setGroupGlow(matrixDay, MATRIX_GLOW); });
        matrixDay.setOnMouseExited(event -> { matrixDay.setCursor(Cursor.DEFAULT); setGroupGlow(matrixDay, MATRIX_SHADOW); });

        matrixHour.setOnMouseEntered(event -> { matrixHour.setCursor(Cursor.V_RESIZE); setGroupGlow(matrixHour, MATRIX_GLOW); });
        matrixHour.setOnMouseExited(event -> { matrixHour.setCursor(Cursor.DEFAULT); setGroupGlow(matrixHour, MATRIX_SHADOW); });

        matrixMinute.setOnMouseEntered(event -> { matrixMinute.setCursor(Cursor.V_RESIZE); setGroupGlow(matrixMinute, MATRIX_GLOW); });
        matrixMinute.setOnMouseExited(event -> { matrixMinute.setCursor(Cursor.DEFAULT); setGroupGlow(matrixMinute, MATRIX_SHADOW); });

        matrixWeek.setOnMouseEntered(event -> { matrixWeek.setCursor(Cursor.V_RESIZE); setGroupGlow(matrixWeek, MATRIX_GLOW); });
        matrixWeek.setOnMouseExited(event -> { matrixWeek.setCursor(Cursor.DEFAULT); setGroupGlow(matrixWeek, MATRIX_SHADOW); });

        coordinatesGroup.setOnMouseEntered(event -> { coordinatesGroup.setCursor(Cursor.HAND); setGroupGlow(coordinatesGroup, MATRIX_GLOW); });
        coordinatesGroup.setOnMouseExited(event -> { coordinatesGroup.setCursor(Cursor.DEFAULT); setGroupGlow(coordinatesGroup, MATRIX_SHADOW); });

        tinyGlobeFrame.setOnMouseEntered(event -> { tinyGlobeFrame.setCursor(Cursor.HAND); tinyGlobeFrame.setStyle(MATRIX_GLOW); });
        tinyGlobeFrame.setOnMouseExited(event -> { tinyGlobeFrame.setCursor(Cursor.DEFAULT); tinyGlobeFrame.setStyle(MATRIX_SHADOW); });

        dialCircleFrame.setOnMouseEntered(event -> dialCircleFrame.setCursor(Cursor.MOVE));
        dialCircleFrame.setOnMouseExited(event -> dialCircleFrame.setCursor(Cursor.DEFAULT));

        horizonGroup.setOnMouseEntered(event -> { horizonGroup.setCursor(Cursor.HAND); setGroupGlow(horizonGroup, HORIZON_HOVER_GLOW); });
        horizonGroup.setOnMouseExited(event -> { horizonGroup.setCursor(Cursor.HAND); setGroupGlow(horizonGroup, ""); });

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

    public void increaseNightCompression() {
        updateNightCompression(1);
    }

    public void decreaseNightCompression() {
        updateNightCompression(-1);
    }

    public void resetNightCompression() {
        if (this.nightCompression != 0) {
            this.nightCompression = 0;
            updateRotations();
        }
    }

    private void updateNightCompression(int direction) {

        this.nightCompression += direction * STEP_nightCompression;

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

    private void updateRotations() {
        setSunTimeDialAngle(getAngle(this.sunTime));
        setHighNoonDialAngle(getAngle(this.highNoon));
        setHorizonDialAngle(getAngle(this.sunrise), getAngle(this.sunset));
        setDialAngleLocalHour(getAngle(this.localTime));
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

    private String formatCoordinateToString(double coordinate, String suffixPositive, String suffixNegative) {

        DecimalFormat coordinateFormat = new DecimalFormat("#0.00");

        String coordinateString = coordinateFormat.format(coordinate);

        String whole = "   " + coordinateString.replace("-", "").split("[.,]")[0];
        whole = whole.substring(whole.length() - 3, whole.length());

        String decimal = coordinateString.split("[.,]")[1].substring(0, 2);

        String result = whole + "." + decimal;

        if (coordinate < 0) {
            result += suffixNegative;
        } else {
            result += suffixPositive;
        }

        return result;
    }

    private String getTimeLengthString(double seconds) {

        String result = "24h00m00s";

        double precisionDays = seconds / (24 * 60 * 60);
        int days = (int) floor(precisionDays);
        double precisionHours = (precisionDays - days) * 24;
        int hours = (int) floor(precisionHours);
        double precisionMins = (precisionHours - hours) * 60;
        int mins = (int) floor(precisionMins);
        double precisionSecs = (precisionMins - mins) * 60;
        int secs = (int) floor(precisionSecs);

        if (days < 1) {
//            String daysString = ("00" + days);
//            daysString = daysString.substring(daysString.length() - 2, daysString.length());
            String hoursString = ("00" + hours);
            hoursString = hoursString.substring(hoursString.length() - 2, hoursString.length());
            String minsString = ("00" + mins);
            minsString = minsString.substring(minsString.length() - 2, minsString.length());
            String secsString = ("00" + secs);
            secsString = secsString.substring(secsString.length() - 2, secsString.length());

            result = "";
//        result += daysString + "d";
            result += hoursString + "h";
            result += minsString + "m";
            result += secsString + "s";
        }

        return result;
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

    public Group getControlThingyResize() {
        return controlThingyResize;
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

    public Globe getTinyGlobe() {
        return tinyGlobe;
    }

    public Circle getTinyGlobeFrame() {
        return tinyGlobeFrame;
    }

    public Group getCoordinatesGroup() {
        return coordinatesGroup;
    }

    public Group getControlThingyClose() {
        return controlThingyClose;
    }

    public Group getControlThingyMaximize() {
        return controlThingyMaximize;
    }

    public Group getControlThingyMinimize() {
        return controlThingyMinimize;
    }

    public Group getHorizonGroup() {
        return horizonGroup;
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
        this.daylength = (this.sunset.getTimeInMillis() - this.sunrise.getTimeInMillis()) / 1000;

        String daylengthString = getTimeLengthString(this.daylength);

        setHorizonDialAngle(getAngle(this.sunrise), getAngle(this.sunset));

        matrixSunrise.setString(getShortTime(this.sunrise));
        matrixSunset.setString(getShortTime(this.sunset));

        matrixDayLength.setString(daylengthString);
    }

    public void setLocalTime(GregorianCalendar localTime) {

        this.localTime = localTime;

        setDialAngleLocalHour(getAngle(this.localTime));

        updateLEDs(dialLocalSecondList, dialLocalSecondOn, dialLocalSecondTransitionList, localTime.get(Calendar.SECOND), true);
        updateLEDs(dialLocalMinuteList, dialLocalMinuteOn, dialLocalMinuteTransitionList, localTime.get(Calendar.MINUTE), true);

    }

    private void updateLEDs(ArrayList<Rectangle> ledList, ArrayList<Boolean> ledOn, ArrayList<Timeline> timelineList, int indexOn, boolean animate) {

        for (int i = 0; i < ledList.size(); i++) {

            if (ledOn.get(i) == true) {

                if(i == indexOn) { continue; }

                if (animate) {
                    timelineList.get(i).play();
                } else {
                    ledList.get(i).setOpacity(0.0);
//                    ledList.get(indexOn).setFill(Color_Of_Void);
                }

                ledOn.set(i, false);
            }
        }

        if (animate) { timelineList.get(indexOn).stop(); }
        ledList.get(indexOn).setOpacity(1.0);
//        ledList.get(indexOn).setFill(Color_Of_Seconds);
        ledOn.set(indexOn, true);
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

        this.sunriseDialAngle = (720 + this.sunriseDialAngle) % 360;
        this.sunsetDialAngle = (720 + this.sunsetDialAngle) % 360;

        if (this.daylength > 24 * 60 * 60) {
            sunriseDial.setVisible(false);
            sunsetDial.setVisible(false);
            matrixSunrise.setVisible(false);
            matrixSunset.setVisible(false);
            dialArcNight.setVisible(false);
            dialArcDayLength.setVisible(false);
        } else if (this.daylength == 0) {
            sunriseDial.setVisible(false);
            sunsetDial.setVisible(false);
            matrixSunrise.setVisible(false);
            matrixSunset.setVisible(false);
            dialArcNight.setVisible(true);
            dialArcDayLength.setVisible(false);
        } else {
            sunriseDial.setVisible(true);
            sunsetDial.setVisible(true);
            matrixSunrise.setVisible(true);
            matrixSunset.setVisible(true);
            dialArcNight.setVisible(true);
            dialArcDayLength.setVisible(true);
        }

        sunriseDialRotate.setAngle(this.sunriseDialAngle);
        sunsetDialRotate.setAngle(this.sunsetDialAngle);

        if (this.sunriseDialAngle >= 0 && this.sunriseDialAngle < 180) {
            matrixSunrise.setTranslateX(CENTER_X - matrixSunrise.getLayoutBounds().getWidth() / 2 - matrixSunrise.getLayoutBounds().getHeight() / 2);
            matrixSunrise.setRotate(-90d);
        } else {
            matrixSunrise.setTranslateX(CENTER_X - matrixSunrise.getLayoutBounds().getWidth() / 2 + matrixSunrise.getLayoutBounds().getHeight() / 2);
            matrixSunrise.setRotate(90d);
        }

        if (this.sunsetDialAngle >= 0 && this.sunsetDialAngle < 180) {
            matrixSunset.setTranslateX(CENTER_X - matrixSunset.getLayoutBounds().getWidth() / 2 - matrixSunset.getLayoutBounds().getHeight() / 2);
            matrixSunset.setRotate(-90d);
        } else {
            matrixSunset.setTranslateX(CENTER_X - matrixSunset.getLayoutBounds().getWidth() / 2 + matrixSunset.getLayoutBounds().getHeight() / 2);
            matrixSunset.setRotate(90d);
        }

        double dayLengthDeg = (720 + this.sunsetDialAngle - this.sunriseDialAngle) % 360;

        dialArcNight.setStartAngle(90 - this.sunsetDialAngle);
        dialArcNight.setLength(dayLengthDeg - 360);

        dialArcDayLength.setStartAngle(90 - this.sunriseDialAngle - DAY_ARC_MARGIN);
        dialArcDayLength.setLength(-1 * (dayLengthDeg - DAY_ARC_MARGIN * 2));

        dialArcMidnight.setStartAngle(90 - getNightCompressionAngle(90));
        dialArcMidnight.setLength(-1 * (getNightCompressionAngle(270) - getNightCompressionAngle(90)));
    }

    public void setDialAngleLocalHour(double dialAngleLocalHour) {
        this.dialAngleLocalHour = getNightCompressionAngle(dialAngleLocalHour);
        dialRotateLocalHour.setAngle(this.dialAngleLocalHour);
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

        this.warning = warning;

        if (this.warning) {
            dialCircleFrame.setFill(WARNING_RADIAL_GRADIENT);
        } else {
            dialCircleFrame.setFill(NOMINAL_RADIAL_GRADIENT);
        }
    }

    public void setGroupGlow(Group group, String style) {
        if (group == null || style == null) { return; }
        group.setStyle(style);
    }

    public void setGlobeVisibility(boolean isVisible) {

        if (isVisible) {

            dialCircleCenterDot.setFill(Color_Of_Void);
            dialCircleCenterDot.setStroke(Color_Of_LocalTime);

            dialArcMidnight.setOpacity(0.35);
            dialArcNight.setOpacity(0.5);

            dialCircleBackground.setVisible(false);
            globe.setVisible(true);
            tinyGlobe.setVisible(false);
            tinyGlobeFrame.setVisible(false);
            tinyGlobeDot.setVisible(false);
            coordinatesGroup.setVisible(true);
        }
        else {

            dialCircleCenterDot.setFill(Color_Of_LocalTime);
            dialCircleCenterDot.setStroke(Color_Of_Void);

            dialArcMidnight.setOpacity(1);
            dialArcNight.setOpacity(1);

            dialCircleBackground.setVisible(true);
            globe.setVisible(false);
            tinyGlobe.setVisible(true);
            tinyGlobeFrame.setVisible(true);
            tinyGlobeDot.setVisible(true);
            coordinatesGroup.setVisible(false);
        }
    }

    public void setCoordinates(double longitude, double latitude) {
        matrixLongitude.setString(formatCoordinateToString(longitude, "E", "W"));
        matrixLatitude.setString(formatCoordinateToString(latitude, "N", "S"));
    }

    public void toggleGlobeVisibility() {

        if (globeVisibleEh) {
            globeVisibleEh = false;
        } else {
            globeVisibleEh = true;
        }

        setGlobeVisibility(globeVisibleEh);
    }

    public void rotateGlobe(double longitude, double latitude) {
        globe.rotateGlobe(longitude, latitude);
        tinyGlobe.rotateGlobe(longitude, latitude);
    }

    private Timeline createTimelineForLED(Rectangle rectangle) {

        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        timeline.setRate(1);
        timeline.setAutoReverse(false);
        KeyValue keyValue = new KeyValue(rectangle.opacityProperty(), 0.0, Interpolator.EASE_IN);
//        KeyValue keyValue = new KeyValue(rectangle.fillProperty(), Color_Of_Void, Interpolator.EASE_IN);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(LED_COOLDOWN), keyValue);
        timeline.getKeyFrames().add(keyFrame);

        return timeline;
    }
}
