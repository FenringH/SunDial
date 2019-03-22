import javafx.animation.*;
import javafx.beans.binding.Bindings;
import javafx.event.Event;
import javafx.scene.*;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;
import javafx.util.Duration;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static java.lang.Math.*;

public class Sundial {

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
    private ArrayList<Rotate> cetusMarkerRotateList;
    private ArrayList<Double> cetusMarkerAngleList;

    private Arc dialArcNight;
    private Arc dialArcMidnight;
    private Arc dialArcDayLength;

    private Circle dialCircleBackground;
    private Circle dialCircleFrame;
    private Circle controlNightCompression;
    private Line sunTimeDial;
    private Line dialLineHighNoon;
    private Line sunriseDial;
    private Line sunsetDial;
    private Group dialLocalMinuteGroup;
    private Group dialLocalHourGroup;
    private Group dialHighNoonGroup;
    private Rectangle dialLineLocalMinute;
    private ArrayList<Arc> cetusMarkerArcList;

    private ArrayList<Node> dialLocalSecondLedList;
    private ArrayList<Boolean> dialLocalSecondOn;
    private ArrayList<Node> dialLocalMinuteLedList;
    private ArrayList<Boolean> dialLocalMinuteOn;
    private ArrayList<Timeline> dialLocalSecondLedTransitionList;
    private ArrayList<Timeline> dialLocalMinuteLedTransitionList;
    private ArrayList<Timeline> cetusMarkerHoverTransitionList;

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
    private DotMatrix matrixHighNoon;
    private ArrayList<DotMatrix> hourMarkerMatrixList;
    private DotMatrix cetusTimer;
    private ArrayList<DotMatrix> cetusTimeMatrixList;
    private DotMatrix matrixTimeZone;

    private Globe dayGlobe;
    private Globe nightGlobe;
    private Grid dayGrid;
    private Ring dayTerminatorLine;
    private Ring dayTerminatorGlow;
    private Ring tinyDayTerminatorLine;
    private Circle globeAtmosphere;
    private Globe tinyGlobe;
    private Circle tinyGlobeFrame;
    private Circle tinyGlobeDot;
    private Group tinyGlobeGroup;
    private Group globeMasterGroup;
    private Scale tinyGlobeScale;
    private Group coordinatesGroup;
    private Group horizonGroup;
    private Group longitudeGroup;
    private Group latitudeGroup;
    private Circle controlThingyHelpCircle;
    private Group controlThingyHelp;
    private Group controlThingyResize;
    private Group controlThingyClose;
    private Group controlThingyMaximize;
    private Group controlThingyMinimize;
    private Group controlThingyNightmode;
    private Circle thingyAlwaysOnTopCircle;
    private Group controlThingyAlwaysOnTop;
    private Group backgroundGroup;
    private Group cetusArcGroup;
    private Group cetusLineGroup;
    private Group matrixTime;
    private Group matrixDate;
    private Group helpOverlay;
    private ArrayList<Group> helpMarkers;
    private Group nightModeOverlay;

    private Text helpText;
    private Group helpTextGroup;
    private Text infoText;
    private Group infoTextGroup;

    public boolean globeVisibleEh = false;
    private boolean cetusTimeVisibleEh = false;
    public boolean ledAnimationOnEh = true;
    public boolean globeAnimationOnEh = true;
    public boolean helpEh = false;
    public boolean nightmodeEh = false;

    private int ledOpacityDuration = ledAnimationOnEh ? Sunconfig.LED_OPACITY_DURATION : 0;
    private int globeRotateDuration = ledAnimationOnEh ? Sunconfig.GLOBE_ROTATE_DURATION : 0;
    private Integer cetusMarkerDuration = Sunconfig.CETUS_MARKER_DURATION;


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
            this.sunTimeDialAngle = Sunconfig.DEFAULT_sunTimeDialAngle;
            this.highNoonDialAngle = Sunconfig.DEFAULT_highNoonDialAngle;
            this.sunriseDialAngle = Sunconfig.DEFAULT_sunriseDialAngle;
            this.sunTimeDialAngle = Sunconfig.DEFAULT_sunsetDialAngle;
            this.localTimeDialAngle = Sunconfig.DEFAULT_localTimeDialAngle;
            this.localTimeText = Sunconfig.DEFAULT_localTimeText;
            this.nightCompression = Sunconfig.DEFAULT_nightCompression;
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

        dialsGroup = new Group();

        // Rotates
        centerRotate = new Rotate();
        centerRotate.setPivotX(Sunconfig.CENTER_X);
        centerRotate.setPivotY(Sunconfig.CENTER_Y);

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
        controlThingyHelp = new Group();

        controlThingyHelpCircle = new Circle(0, 0, Sunconfig.CONTROL_HELP_RADIUS);
        controlThingyHelpCircle.setFill(Sunconfig.Color_Of_ResizeFill);
        controlThingyHelpCircle.setStroke(Sunconfig.Color_Of_ResizeStroke);
        controlThingyHelpCircle.setStrokeWidth(Sunconfig.CONTROL_HELP_STROKE_WIDTH);

        DotMatrix thingyHelpQuestionMark = new DotMatrix("?", Color.WHITE);
        thingyHelpQuestionMark.setScaleX(Sunconfig.MATRIX_HELPMARK_SCALE);
        thingyHelpQuestionMark.setScaleY(Sunconfig.MATRIX_HELPMARK_SCALE);
        thingyHelpQuestionMark.setTranslateX(-thingyHelpQuestionMark.getLayoutBounds().getWidth() / 2);
        thingyHelpQuestionMark.setTranslateY(-thingyHelpQuestionMark.getLayoutBounds().getHeight() / 2);
        thingyHelpQuestionMark.setStyle(Sunconfig.MATRIX_SHADOW);

        controlThingyHelp.getChildren().addAll(controlThingyHelpCircle, thingyHelpQuestionMark);
        controlThingyHelp.setTranslateX(Sunconfig.CENTER_X + Sunconfig.CONTROL_CLOSE_OFFSET * cos(toRadians(Sunconfig.CONTROL_HELP_ANGLE)));
        controlThingyHelp.setTranslateY(Sunconfig.CENTER_Y - Sunconfig.CONTROL_CLOSE_OFFSET * sin(toRadians(Sunconfig.CONTROL_HELP_ANGLE)));
        controlThingyHelp.setStyle(Sunconfig.CONTROL_RESIZE_SHADOW);
        controlThingyHelp.setOpacity(Sunconfig.CONTROL_HELP_OPACITY);

        controlThingyResize = new Group();
        Polygon thingyResizeTriangle = new Polygon(
                Sunconfig.CONTROL_RESIZE_SIZE, 0,
                Sunconfig.CONTROL_RESIZE_SIZE, Sunconfig.CONTROL_RESIZE_SIZE,
                0, Sunconfig.CONTROL_RESIZE_SIZE
        );
        thingyResizeTriangle.setFill(Sunconfig.Color_Of_ResizeFill);
        thingyResizeTriangle.setStroke(Sunconfig.Color_Of_ResizeStroke);
        thingyResizeTriangle.setStrokeWidth(Sunconfig.CONTROL_RESIZE_STROKE_WIDTH);
        controlThingyResize.getChildren().add(thingyResizeTriangle);
        controlThingyResize.setTranslateX(Sunconfig.CENTER_X + Sunconfig.CONTROL_RESIZE_OFFSET);
        controlThingyResize.setTranslateY(Sunconfig.CENTER_Y + Sunconfig.CONTROL_RESIZE_OFFSET);
        controlThingyResize.setStyle(Sunconfig.CONTROL_RESIZE_SHADOW);
        controlThingyResize.setOpacity(Sunconfig.CONTROL_RESIZE_OPACITY);

        controlThingyClose = new Group();
        Circle thingyCloseCircle = new Circle(0, 0, Sunconfig.CONTROL_CLOSE_RADIUS);
        thingyCloseCircle.setFill(Sunconfig.Color_Of_CloseFill);
        thingyCloseCircle.setStroke(Sunconfig.Color_Of_CloseStroke);
        thingyCloseCircle.setStrokeWidth(Sunconfig.CONTROL_CLOSE_STROKE_WIDTH);
        controlThingyClose.getChildren().add(thingyCloseCircle);
        controlThingyClose.setTranslateX(Sunconfig.CENTER_X + Sunconfig.CONTROL_CLOSE_OFFSET * cos(toRadians(Sunconfig.CONTROL_CLOSE_ANGLE)));
        controlThingyClose.setTranslateY(Sunconfig.CENTER_Y - Sunconfig.CONTROL_CLOSE_OFFSET * sin(toRadians(Sunconfig.CONTROL_CLOSE_ANGLE)));
        controlThingyClose.setStyle(Sunconfig.CONTROL_CLOSE_SHADOW);
        controlThingyClose.setOpacity(Sunconfig.CONTROL_CLOSE_OPACITY);

        controlThingyMaximize = new Group();
        Circle thingyMaximizeCircle = new Circle(0, 0, Sunconfig.CONTROL_MAXIMIZE_RADIUS);
        thingyMaximizeCircle.setFill(Sunconfig.Color_Of_MaximizeFill);
        thingyMaximizeCircle.setStroke(Sunconfig.Color_Of_MaximizeStroke);
        thingyMaximizeCircle.setStrokeWidth(Sunconfig.CONTROL_MAXIMIZE_STROKE_WIDTH);
        controlThingyMaximize.getChildren().add(thingyMaximizeCircle);
        controlThingyMaximize.setTranslateX(Sunconfig.CENTER_X + Sunconfig.CONTROL_MAXIMIZE_OFFSET * cos(toRadians(Sunconfig.CONTROL_MAXIMIZE_ANGLE)));
        controlThingyMaximize.setTranslateY(Sunconfig.CENTER_Y - Sunconfig.CONTROL_MAXIMIZE_OFFSET * sin(toRadians(Sunconfig.CONTROL_MAXIMIZE_ANGLE)));
        controlThingyMaximize.setStyle(Sunconfig.CONTROL_MAXIMIZE_SHADOW);
        controlThingyMaximize.setOpacity(Sunconfig.CONTROL_MAXIMIZE_OPACITY);

        controlThingyMinimize = new Group();
        Circle thingyMinimizeCircle = new Circle(0, 0, Sunconfig.CONTROL_MINIMIZE_RADIUS);
        thingyMinimizeCircle.setFill(Sunconfig.Color_Of_MinimizeFill);
        thingyMinimizeCircle.setStroke(Sunconfig.Color_Of_MinimizeStroke);
        thingyMinimizeCircle.setStrokeWidth(Sunconfig.CONTROL_MINIMIZE_STROKE_WIDTH);
        controlThingyMinimize.getChildren().add(thingyMinimizeCircle);
        controlThingyMinimize.setTranslateX(Sunconfig.CENTER_X + Sunconfig.CONTROL_MINIMIZE_OFFSET * cos(toRadians(Sunconfig.CONTROL_MINIMIZE_ANGLE)));
        controlThingyMinimize.setTranslateY(Sunconfig.CENTER_Y - Sunconfig.CONTROL_MINIMIZE_OFFSET * sin(toRadians(Sunconfig.CONTROL_MINIMIZE_ANGLE)));
        controlThingyMinimize.setStyle(Sunconfig.CONTROL_MINIMIZE_SHADOW);
        controlThingyMinimize.setOpacity(Sunconfig.CONTROL_MINIMIZE_OPACITY);

        controlThingyNightmode = new Group();
        Circle thingyNightmodeCircle = new Circle(0, 0, Sunconfig.CONTROL_NIGHTMODE_RADIUS);
        thingyNightmodeCircle.setFill(Sunconfig.Color_Of_NightmodeFill);
        thingyNightmodeCircle.setStroke(Sunconfig.Color_Of_NightmodeStroke);
        thingyNightmodeCircle.setStrokeWidth(Sunconfig.CONTROL_NIGHTMODE_STROKE_WIDTH);
        controlThingyNightmode.getChildren().add(thingyNightmodeCircle);
        controlThingyNightmode.setTranslateX(Sunconfig.CENTER_X + Sunconfig.CONTROL_MINIMIZE_OFFSET * cos(toRadians(Sunconfig.CONTROL_NIGHTMODE_ANGLE)));
        controlThingyNightmode.setTranslateY(Sunconfig.CENTER_Y - Sunconfig.CONTROL_MINIMIZE_OFFSET * sin(toRadians(Sunconfig.CONTROL_NIGHTMODE_ANGLE)));
        controlThingyNightmode.setStyle(Sunconfig.CONTROL_NIGHTMODE_SHADOW);
        controlThingyNightmode.setOpacity(Sunconfig.CONTROL_NIGHTMODE_OPACITY);

        controlThingyAlwaysOnTop = new Group();
        thingyAlwaysOnTopCircle = new Circle(0, 0, Sunconfig.CONTROL_ALWAYSONTOP_RADIUS);
        thingyAlwaysOnTopCircle.setFill(Sunconfig.Color_Of_AlwaysOnTopFill);
        thingyAlwaysOnTopCircle.setStroke(Sunconfig.Color_Of_AlwaysOnTopStroke);
        thingyAlwaysOnTopCircle.setStrokeWidth(Sunconfig.CONTROL_ALWAYSONTOP_STROKE_WIDTH);
        controlThingyAlwaysOnTop.getChildren().add(thingyAlwaysOnTopCircle);
        controlThingyAlwaysOnTop.setTranslateX(Sunconfig.CENTER_X + Sunconfig.CONTROL_MINIMIZE_OFFSET * cos(toRadians(Sunconfig.CONTROL_ALWAYSONTOP_ANGLE)));
        controlThingyAlwaysOnTop.setTranslateY(Sunconfig.CENTER_Y - Sunconfig.CONTROL_MINIMIZE_OFFSET * sin(toRadians(Sunconfig.CONTROL_ALWAYSONTOP_ANGLE)));
        controlThingyAlwaysOnTop.setStyle(Sunconfig.CONTROL_ALWAYSONTOP_SHADOW);
        controlThingyAlwaysOnTop.setOpacity(Sunconfig.CONTROL_ALWAYSONTOP_OPACITY);

        // Master globe group
        dayGlobe = new Globe(Sunconfig.GLOBE_DAY_IMAGE, Sunconfig.CENTER_X - Sunconfig.MARGIN_X, Sunconfig.GLOBE_ROTATE_DURATION);
        dayGlobe.setLayoutX(Sunconfig.CENTER_X);
        dayGlobe.setLayoutY(Sunconfig.CENTER_Y);
        dayGlobe.setNightLightColor(Color.RED);

        nightGlobe = new Globe(Sunconfig.GLOBE_NIGHT_IMAGE, Sunconfig.CENTER_X - Sunconfig.MARGIN_X, Sunconfig.GLOBE_ROTATE_DURATION);
        nightGlobe.setLayoutX(Sunconfig.CENTER_X);
        nightGlobe.setLayoutY(Sunconfig.CENTER_Y);
        nightGlobe.setAmbientLightColor(Color.WHITE);

        dayGrid = new Grid(Sunconfig.CENTER_X - Sunconfig.MARGIN_X, 1, Color.WHITE, Sunconfig.GLOBE_ROTATE_DURATION);
        dayGrid.setLayoutX(Sunconfig.CENTER_X);
        dayGrid.setLayoutY(Sunconfig.CENTER_Y);

        dayTerminatorLine = new Ring(Sunconfig.CENTER_X - Sunconfig.MARGIN_X, Sunconfig.DAY_TERMINATOR_WIDTH, Sunconfig.Color_Of_TerminatorLine, Sunconfig.GLOBE_ROTATE_DURATION);
        dayTerminatorLine.setLayoutX(Sunconfig.CENTER_X);
        dayTerminatorLine.setLayoutY(Sunconfig.CENTER_Y);

        dayTerminatorGlow = new Ring(Sunconfig.CENTER_X - Sunconfig.MARGIN_X, Sunconfig.DAY_TERMINATOR_GLOW_WIDTH, Sunconfig.Color_Of_TerminatorGlow, Sunconfig.GLOBE_ROTATE_DURATION);
        dayTerminatorGlow.setLayoutX(Sunconfig.CENTER_X);
        dayTerminatorGlow.setLayoutY(Sunconfig.CENTER_Y);

        Group dayGlobeGroup = new Group();
        dayGlobeGroup.getChildren().add(dayGlobe);
        SubScene dayGlobeScene = new SubScene(dayGlobeGroup, Sunconfig.DIAL_WIDTH, Sunconfig.DIAL_HEIGHT, true, SceneAntialiasing.BALANCED);
        dayGlobeScene.setBlendMode(BlendMode.ADD);

        Group nightGlobeGroup = new Group();
        nightGlobeGroup.getChildren().add(nightGlobe);
        SubScene nightGlobeScene = new SubScene(nightGlobeGroup, Sunconfig.DIAL_WIDTH, Sunconfig.DIAL_HEIGHT, true, SceneAntialiasing.BALANCED);
        nightGlobeScene.setBlendMode(BlendMode.SCREEN);

        Group dayGridGroup = new Group();
        dayGridGroup.getChildren().add(dayGrid);
        SubScene dayGridScene = new SubScene(dayGridGroup, Sunconfig.DIAL_WIDTH, Sunconfig.DIAL_HEIGHT, true, SceneAntialiasing.BALANCED);
        dayGridScene.setBlendMode(BlendMode.SCREEN);
        dayGridScene.setEffect(new GaussianBlur(1));
        dayGridScene.setOpacity(Sunconfig.DAY_TERMINATOR_OPACITY);

        Group dayTerminatorLineGroup = new Group();
        dayTerminatorLineGroup.getChildren().add(dayTerminatorLine);
        SubScene dayTerminatorLineScene = new SubScene(dayTerminatorLineGroup, Sunconfig.DIAL_WIDTH, Sunconfig.DIAL_HEIGHT, true, SceneAntialiasing.BALANCED);
        dayTerminatorLineScene.setBlendMode(BlendMode.SCREEN);
        dayTerminatorLineScene.setEffect(new GaussianBlur(Sunconfig.DAY_TERMINATOR_WIDTH));
        dayTerminatorLineScene.setOpacity(Sunconfig.DAY_TERMINATOR_OPACITY);

        Group dayTerminatorGlowGroup = new Group();
        dayTerminatorGlowGroup.getChildren().add(dayTerminatorGlow);
        SubScene dayTerminatorGlowScene = new SubScene(dayTerminatorGlowGroup, Sunconfig.DIAL_WIDTH, Sunconfig.DIAL_HEIGHT, true, SceneAntialiasing.BALANCED);
        dayTerminatorGlowScene.setBlendMode(BlendMode.SCREEN);
        dayTerminatorGlowScene.setEffect(new GaussianBlur(Sunconfig.DAY_TERMINATOR_GLOW_WIDTH));
        dayTerminatorGlowScene.setOpacity(Sunconfig.DAY_TERMINATOR_OPACITY);

        globeAtmosphere = new Circle(Sunconfig.CENTER_X, Sunconfig.CENTER_Y, Sunconfig.CENTER_X - Sunconfig.MARGIN_X + 2);
        globeAtmosphere.setFill(Sunconfig.GLOBE_ATMOSPHERE);
        globeAtmosphere.setStroke(Sunconfig.Color_Of_Void);
        globeAtmosphere.setMouseTransparent(true);

        globeMasterGroup = new Group(dayGlobeScene, nightGlobeScene, dayTerminatorGlowScene, dayTerminatorLineScene, globeAtmosphere);
        globeMasterGroup.setVisible(false);


        // Tiny globe group
        tinyGlobeFrame = new Circle(Sunconfig.TINYGLOBE_RADIUS);
        tinyGlobeFrame.setLayoutX(Sunconfig.CENTER_X);
        tinyGlobeFrame.setLayoutY(Sunconfig.CENTER_Y + Sunconfig.TINYGLOBE_OFFSET);
        tinyGlobeFrame.setFill(Sunconfig.Color_Of_Void);
        tinyGlobeFrame.setStroke(Sunconfig.Color_Of_TinyFrame);
        tinyGlobeFrame.setStrokeWidth(Sunconfig.TINYGLOBE_FRAME_STROKE_WIDTH);
        tinyGlobeFrame.setStyle(Sunconfig.MATRIX_SHADOW);

        tinyGlobeDot = new Circle(1.0d);
        tinyGlobeDot.setLayoutX(Sunconfig.CENTER_X);
        tinyGlobeDot.setLayoutY(Sunconfig.CENTER_Y + Sunconfig.TINYGLOBE_OFFSET);
        tinyGlobeDot.setFill(Sunconfig.Color_Of_TinyFrame);
        tinyGlobeDot.setStroke(Sunconfig.Color_Of_Void);

        tinyGlobe = new Globe(Sunconfig.GLOBE_DAY_IMAGE, Sunconfig.TINYGLOBE_RADIUS, Sunconfig.GLOBE_ROTATE_DURATION);
        tinyGlobe.setLayoutX(Sunconfig.CENTER_X);
        tinyGlobe.setLayoutY(Sunconfig.CENTER_Y + Sunconfig.TINYGLOBE_OFFSET);
        tinyGlobe.setDayLightColor(Sunconfig.Color_Of_TinyDay);
        tinyGlobe.setNightLightColor(Sunconfig.Color_Of_TinyNight);
        tinyGlobe.setAmbientLightColor(Sunconfig.Color_Of_TinyAmbient);
        tinyGlobe.setVisible(true);

        Group tinyGlobeSceneGroup = new Group();
        tinyGlobeSceneGroup.getChildren().addAll(tinyGlobe);
        SubScene tinyGlobeScene = new SubScene(tinyGlobeSceneGroup, Sunconfig.DIAL_WIDTH, Sunconfig.DIAL_HEIGHT, true, SceneAntialiasing.BALANCED);

        tinyDayTerminatorLine = new Ring(Sunconfig.TINYGLOBE_RADIUS, 1, Sunconfig.Color_Of_TerminatorLine, Sunconfig.GLOBE_ROTATE_DURATION);
        tinyDayTerminatorLine.setLayoutX(Sunconfig.CENTER_X);
        tinyDayTerminatorLine.setLayoutY(Sunconfig.CENTER_Y + Sunconfig.TINYGLOBE_OFFSET);

        Group tinyDayTerminatorLineGroup = new Group();
        tinyDayTerminatorLineGroup.getChildren().add(tinyDayTerminatorLine);
        SubScene tinyDayTerminatorLineScene = new SubScene(tinyDayTerminatorLineGroup, Sunconfig.DIAL_WIDTH, Sunconfig.DIAL_HEIGHT, true, SceneAntialiasing.BALANCED);
        tinyDayTerminatorLineScene.setBlendMode(BlendMode.SCREEN);
        tinyDayTerminatorLineScene.setEffect(new GaussianBlur(Sunconfig.DAY_TERMINATOR_WIDTH));
        tinyDayTerminatorLineScene.setOpacity(Sunconfig.DAY_TERMINATOR_OPACITY);

        tinyGlobeScale = new Scale();
        tinyGlobeScale.setPivotX(Sunconfig.CENTER_X);
        tinyGlobeScale.setPivotY(Sunconfig.CENTER_Y + Sunconfig.TINYGLOBE_OFFSET);

        tinyGlobeGroup = new Group();
        tinyGlobeGroup.getChildren().addAll(tinyGlobeScene, tinyDayTerminatorLineScene, tinyGlobeDot, tinyGlobeFrame);
        tinyGlobeGroup.getTransforms().add(tinyGlobeScale);
        tinyGlobeGroup.setOpacity(Sunconfig.TINYGLOBE_DEFAULT_OPACITY);


        // Stuff
        dialMarginBox = new Rectangle(Sunconfig.DIAL_WIDTH, Sunconfig.DIAL_HEIGHT);
        dialMarginBox.setTranslateX(0);
        dialMarginBox.setTranslateY(0);
        dialMarginBox.setFill(Sunconfig.Color_Of_Void);
        dialMarginBox.setStroke(Sunconfig.Color_Of_SunTime);
        dialMarginBox.setStrokeWidth(2.0d);
        dialMarginBox.setOpacity(1);

        dialMarginFillBox = new Rectangle(Sunconfig.DIAL_WIDTH, Sunconfig.DIAL_HEIGHT);
        dialMarginFillBox.setTranslateX(0);
        dialMarginFillBox.setTranslateY(0);
        dialMarginFillBox.setFill(Sunconfig.Color_Of_DaySky);
        dialMarginFillBox.setStroke(Sunconfig.Color_Of_Void);
        dialMarginFillBox.setOpacity(0);

        dialBox = new Rectangle(Sunconfig.DIAL_WIDTH, Sunconfig.DIAL_HEIGHT);
        dialBox.setFill(Sunconfig.Color_Of_Void);
        dialBox.setStroke(Sunconfig.Color_Of_Darkness);
        dialBox.setStrokeWidth(0.30d);
        dialBox.setOpacity(0.00d);

        dialArcNight = new Arc(Sunconfig.CENTER_X, Sunconfig.CENTER_Y, Sunconfig.DIAL_WIDTH / 2 - Sunconfig.MARGIN_X, Sunconfig.DIAL_HEIGHT / 2 - Sunconfig.MARGIN_Y, 90 - sunsetDialAngle, sunsetDialAngle - sunriseDialAngle);
        dialArcNight.setType(ArcType.ROUND);
        dialArcNight.setStroke(Sunconfig.Color_Of_Void);
        dialArcNight.setFill(Sunconfig.Color_Of_NightSky);

        dialArcMidnight = new Arc(Sunconfig.CENTER_X, Sunconfig.CENTER_Y, Sunconfig.DIAL_WIDTH / 2 - Sunconfig.MARGIN_X, Sunconfig.DIAL_HEIGHT / 2 - Sunconfig.MARGIN_Y, 0, -180);
        dialArcMidnight.setType(ArcType.ROUND);
        dialArcMidnight.setStroke(Sunconfig.Color_Of_Void);
        dialArcMidnight.setFill(Sunconfig.Color_Of_Midnight);

        dialMarginCircle = new Circle(Sunconfig.CENTER_X, Sunconfig.CENTER_Y, Sunconfig.DIAL_WIDTH / 2);
        dialMarginCircle.setFill(Sunconfig.Color_Of_Margin);
        dialMarginCircle.setStroke(Sunconfig.Color_Of_Void);
        dialMarginCircle.setOpacity(Sunconfig.MARGIN_CIRCLE_OPACITY);

        dialCircleBackground = new Circle(Sunconfig.CENTER_X, Sunconfig.CENTER_Y, Sunconfig.DIAL_WIDTH / 2 - Sunconfig.MARGIN_X);
        dialCircleBackground.setFill(Sunconfig.Color_Of_DaySky);
        dialCircleBackground.setStroke(Sunconfig.Color_Of_Void);
        dialCircleBackground.setStyle(Sunconfig.MATRIX_SHADOW);

        dialCircleFrame = new Circle(Sunconfig.CENTER_X, Sunconfig.CENTER_Y, Sunconfig.DIAL_WIDTH / 2 - Sunconfig.MARGIN_X);
        dialCircleFrame.setFill(Sunconfig.FRAME_DIAL_NOMINAL);
        dialCircleFrame.setStroke(Sunconfig.Color_Of_Void);
        dialCircleFrame.setStrokeWidth(Sunconfig.MARKER_FRAME_STROKE_WIDTH);


        cetusMarkerRotateList = new ArrayList<>();
        cetusMarkerArcList = new ArrayList<>();
        cetusMarkerAngleList = new ArrayList<>();
        cetusTimeMatrixList = new ArrayList<>();
        cetusMarkerHoverTransitionList = new ArrayList<>();

        cetusArcGroup = new Group();
        cetusArcGroup.setBlendMode(BlendMode.MULTIPLY);

        cetusLineGroup = new Group();

        for (int i = 0; i <= Cetustime.CYCLES_PER_DAY; i++) {

            double startAngle = (i * Cetustime.CYCLE_LENGTH * 360d) / (24d * 60 * 60 * 1000);
            double endAngle = ((i * Cetustime.CYCLE_LENGTH + Cetustime.NIGHT_LENGTH) * 360d) / (24d * 60 * 60 * 1000);

            Line markerLineStart = new Line(Sunconfig.CENTER_X, Sunconfig.CETUS_MARKER_LENGTH + Sunconfig.MARGIN_Y, Sunconfig.CENTER_X, Sunconfig.MARGIN_Y);
            markerLineStart.setStroke(Sunconfig.Color_Of_CetusMarker);
            markerLineStart.setStrokeWidth(Sunconfig.CETUS_MARKER_WIDTH);
            markerLineStart.setStyle(Sunconfig.CETUS_MARKER_SHADOW);
            markerLineStart.setMouseTransparent(true);

            Rotate markerLineStartRotate = centerRotate.clone();
            markerLineStartRotate.setAngle(startAngle);

            DotMatrix matrixStart = new DotMatrix("00:00", Sunconfig.Color_Of_CetusMarker);
            matrixStart.setScaleX(Sunconfig.CETUS_HORIZON_SCALE);
            matrixStart.setScaleY(Sunconfig.CETUS_HORIZON_SCALE);
            matrixStart.setLayoutX(Sunconfig.CENTER_X - matrixStart.getLayoutBounds().getWidth() / 2 - matrixStart.getLayoutBounds().getHeight() / 2);
            matrixStart.setLayoutY(Sunconfig.CETUS_HORIZON_OFFSET);
            matrixStart.setRotate(90d);
            matrixStart.setStyle(Sunconfig.CETUS_MARKER_SHADOW);
            matrixStart.setMouseTransparent(true);
            matrixStart.setOpacity(0);

            Group startHorizonGroup = new Group();
            startHorizonGroup.getChildren().addAll(markerLineStart, matrixStart);
            startHorizonGroup.getTransforms().add(markerLineStartRotate);

            Line markerLineEnd = new Line(Sunconfig.CENTER_X, Sunconfig.CETUS_MARKER_LENGTH + Sunconfig.MARGIN_Y, Sunconfig.CENTER_X, Sunconfig.MARGIN_Y);
            markerLineEnd.setStroke(Sunconfig.Color_Of_CetusMarker);
            markerLineEnd.setStrokeWidth(Sunconfig.CETUS_MARKER_WIDTH);
            markerLineEnd.setStyle(Sunconfig.CETUS_MARKER_SHADOW);
            markerLineEnd.setMouseTransparent(true);

            DotMatrix matrixEnd = new DotMatrix("00:00", Sunconfig.Color_Of_CetusMarker);
            matrixEnd.setScaleX(Sunconfig.CETUS_HORIZON_SCALE);
            matrixEnd.setScaleY(Sunconfig.CETUS_HORIZON_SCALE);
            matrixEnd.setTranslateX(Sunconfig.CENTER_X - matrixEnd.getLayoutBounds().getWidth() / 2 + matrixEnd.getLayoutBounds().getHeight() / 2);
            matrixEnd.setTranslateY(Sunconfig.CETUS_HORIZON_OFFSET);
            matrixEnd.setRotate(90d);
            matrixEnd.setStyle(Sunconfig.CETUS_MARKER_SHADOW);
            matrixEnd.setMouseTransparent(true);
            matrixEnd.setOpacity(0);

            Rotate markerLineEndRotate = centerRotate.clone();
            markerLineEndRotate.setAngle(endAngle);

            Group endHorizonGroup = new Group();
            endHorizonGroup.getChildren().addAll(markerLineEnd, matrixEnd);
            endHorizonGroup.getTransforms().add(markerLineEndRotate);

            Arc nightArc = new Arc(Sunconfig.CENTER_X, Sunconfig.CENTER_Y, Sunconfig.CENTER_X - Sunconfig.MARGIN_X, Sunconfig.CENTER_Y - Sunconfig.MARGIN_Y, 90 - startAngle, startAngle - endAngle);
            nightArc.setType(ArcType.ROUND);
            nightArc.setStroke(Sunconfig.Color_Of_Void);
            nightArc.setFill(Sunconfig.CETUS_ARC_GRADIENT);
            nightArc.setOpacity(Sunconfig.CETUS_ARC_OPACITY);

            cetusMarkerAngleList.add(startAngle);
            cetusMarkerAngleList.add(endAngle);
            cetusMarkerRotateList.add(markerLineStartRotate);
            cetusMarkerRotateList.add(markerLineEndRotate);
            cetusMarkerArcList.add(nightArc);
            cetusTimeMatrixList.add(matrixStart);
            cetusTimeMatrixList.add(matrixEnd);

            cetusArcGroup.getChildren().add(nightArc);
            cetusLineGroup.getChildren().addAll(startHorizonGroup, endHorizonGroup);

            Timeline cetusMarkerTransitionOn = new Timeline();
            cetusMarkerTransitionOn.setCycleCount(1);
            cetusMarkerTransitionOn.setRate(1);
            cetusMarkerTransitionOn.setAutoReverse(false);

            KeyValue keyValueStartOpacityOn = new KeyValue(matrixStart.opacityProperty(), 1.0, Interpolator.EASE_BOTH);
            KeyFrame keyFrameStartOpacityOn = new KeyFrame(Duration.millis(cetusMarkerDuration), keyValueStartOpacityOn);
            KeyValue keyValueEndOpacityOn = new KeyValue(matrixEnd.opacityProperty(), 1.0, Interpolator.EASE_BOTH);
            KeyFrame keyFrameEndOpacityOn = new KeyFrame(Duration.millis(cetusMarkerDuration), keyValueEndOpacityOn);
            KeyValue keyValueLineStartOn = new KeyValue(markerLineStart.startYProperty(), Sunconfig.CETUS_MARKER_LENGTH * 2, Interpolator.EASE_BOTH);
            KeyFrame keyFrameLineStartOn = new KeyFrame(Duration.millis(cetusMarkerDuration), keyValueLineStartOn);
            KeyValue keyValueLineEndOn = new KeyValue(markerLineEnd.startYProperty(), Sunconfig.CETUS_MARKER_LENGTH * 2, Interpolator.EASE_BOTH);
            KeyFrame keyFrameLineEndOn = new KeyFrame(Duration.millis(cetusMarkerDuration), keyValueLineEndOn);
            KeyValue keyValueArcFillOn = new KeyValue(nightArc.fillProperty(), Sunconfig.CETUS_ARC_GRADIENT_HOVER, Interpolator.EASE_BOTH);
            KeyFrame keyFrameArcFillOn = new KeyFrame(Duration.millis(cetusMarkerDuration), keyValueArcFillOn);

            cetusMarkerTransitionOn.getKeyFrames().addAll(keyFrameStartOpacityOn, keyFrameEndOpacityOn, keyFrameLineStartOn, keyFrameLineEndOn);

            Timeline cetusMarkerTransitionOff = new Timeline();
            cetusMarkerTransitionOff.setCycleCount(1);
            cetusMarkerTransitionOff.setRate(1);
            cetusMarkerTransitionOff.setAutoReverse(false);

            KeyValue keyValueStartOpacityOff = new KeyValue(matrixStart.opacityProperty(), 0.0, Interpolator.EASE_BOTH);
            KeyFrame keyFrameStartOpacityOff = new KeyFrame(Duration.millis(cetusMarkerDuration), keyValueStartOpacityOff);
            KeyValue keyValueEndOpacityOff = new KeyValue(matrixEnd.opacityProperty(), 0.0, Interpolator.EASE_BOTH);
            KeyFrame keyFrameEndOpacityOff = new KeyFrame(Duration.millis(cetusMarkerDuration), keyValueEndOpacityOff);
            KeyValue keyValueLineStartOff = new KeyValue(markerLineStart.startYProperty(), Sunconfig.CETUS_MARKER_LENGTH + Sunconfig.MARGIN_Y, Interpolator.EASE_BOTH);
            KeyFrame keyFrameLineStartOff = new KeyFrame(Duration.millis(cetusMarkerDuration), keyValueLineStartOff);
            KeyValue keyValueLineEndOff = new KeyValue(markerLineEnd.startYProperty(), Sunconfig.CETUS_MARKER_LENGTH + Sunconfig.MARGIN_Y, Interpolator.EASE_BOTH);
            KeyFrame keyFrameLineEndOff = new KeyFrame(Duration.millis(cetusMarkerDuration), keyValueLineEndOff);
            KeyValue keyValueArcFillOff = new KeyValue(nightArc.fillProperty(), Sunconfig.CETUS_ARC_GRADIENT, Interpolator.EASE_BOTH);
            KeyFrame keyFrameArcFillOff = new KeyFrame(Duration.millis(cetusMarkerDuration), keyValueArcFillOff);

            cetusMarkerTransitionOff.getKeyFrames().addAll(keyFrameStartOpacityOff, keyFrameEndOpacityOff, keyFrameLineStartOff, keyFrameLineEndOff);

            cetusMarkerHoverTransitionList.add(cetusMarkerTransitionOn);
            cetusMarkerHoverTransitionList.add(cetusMarkerTransitionOff);

            nightArc.setOnMouseEntered(event -> cetusMarkerTransitionOn.play());
            nightArc.setOnMouseExited(event -> cetusMarkerTransitionOff.play());

        }


        cetusTimer = new DotMatrix("0h00m00s", Sunconfig.Color_Of_CetusNight);
        cetusTimer.setScaleX(Sunconfig.CETUS_TIMER_SCALE);
        cetusTimer.setScaleY(Sunconfig.CETUS_TIMER_SCALE);
        cetusTimer.setLayoutX(Sunconfig.CENTER_X - cetusTimer.getLayoutBounds().getWidth() / 2);
        cetusTimer.setLayoutY(Sunconfig.CETUS_TIMER_OFFSET);
        cetusTimer.setStyle(Sunconfig.CETUS_MATRIX_SHADOW_NIGHT);
        cetusTimer.setVisible(false);


        Group dialMinuteMarkers = new Group();
        dialMinuteMarkers.setBlendMode(BlendMode.COLOR_BURN);
        dialMinuteMarkers.setMouseTransparent(true);

        for(int i = 0; i < 60; i++) {

            Group markerGroup = new Group();

            double opacity = Sunconfig.MARKER_MINUTE_OPACITY;

            Rectangle markerMinute = new Rectangle(Sunconfig.LOCALMINUTE_WIDTH, Sunconfig.LOCALMINUTE_HEIGHT);
            markerMinute.setArcWidth(Sunconfig.LOCALMINUTE_ROUND);
            markerMinute.setArcHeight(Sunconfig.LOCALMINUTE_ROUND);
            markerMinute.setTranslateX(Sunconfig.CENTER_X - Sunconfig.LOCALMINUTE_WIDTH / 2);
            markerMinute.setTranslateY(Sunconfig.LOCALMINUTE_OFFSET);
            markerMinute.setFill(Sunconfig.MINUTE_MARKER_GRADIENT);
//            markerMinute.setFill(Color.BLACK);
//            markerMinute.setStrokeWidth(0.5d);
            markerMinute.setOpacity(opacity);
//            markerMinute.setBlendMode(BlendMode.OVERLAY);

            Polygon markerMinutePoly = new Polygon(
                    -1, Sunconfig.LOCALMINUTE_POLY_HEIGHT,
                    -Sunconfig.LOCALMINUTE_POLY_WIDTH, Sunconfig.LOCALMINUTE_POLY_WIDTH / 2,
                    -Sunconfig.LOCALMINUTE_POLY_WIDTH / 2, 0,
                    +Sunconfig.LOCALMINUTE_POLY_WIDTH / 2, 0,
                    +Sunconfig.LOCALMINUTE_POLY_WIDTH, Sunconfig.LOCALMINUTE_POLY_WIDTH / 2,
                    1, Sunconfig.LOCALMINUTE_POLY_HEIGHT
            );
            markerMinutePoly.setTranslateX(Sunconfig.CENTER_X);
            markerMinutePoly.setTranslateY(Sunconfig.LOCALMINUTE_OFFSET);
            markerMinutePoly.setFill(Sunconfig.MINUTE_POLY_GRADIENT);
            markerMinutePoly.setOpacity(opacity);

            markerGroup.getChildren().add(markerMinutePoly);

            if (i % 5 == 0) {
                DotMatrix markerMatrix = new DotMatrix("" + i, Sunconfig.Color_Of_Darkness);
                markerMatrix.setTranslateX(Sunconfig.CENTER_X - markerMatrix.getLayoutBounds().getWidth() / 2);
                markerMatrix.setTranslateY(Sunconfig.LOCALMINUTE_OFFSET + Sunconfig.LOCALMINUTE_POLY_HEIGHT + Sunconfig.LOCALMINUTE_POLY_WIDTH);
                markerMatrix.setOpacity(Sunconfig.MATRIX_MINUTE_OPACITY);

                double rotationAdjust = i * -6;
                markerMatrix.setRotate(rotationAdjust);
                markerMatrix.setScaleX(Sunconfig.MATRIX_MINUTE_SCALE);
                markerMatrix.setScaleY(Sunconfig.MATRIX_MINUTE_SCALE);
                markerGroup.getChildren().add(markerMatrix);
            }

            Rotate markerHourRotate = new Rotate();
            markerHourRotate.setPivotX(Sunconfig.CENTER_X);
            markerHourRotate.setPivotY(Sunconfig.CENTER_Y);
            markerHourRotate.setAngle(i * 360d / 60d);

            markerGroup.getTransforms().add(markerHourRotate);

            dialMinuteMarkers.getChildren().add(markerGroup);
        }


        Group dialHourLineMarkers = new Group();
        dialHourLineMarkers.setMouseTransparent(true);

        Group dialHourMatrixMarkers = new Group();
        dialHourMatrixMarkers.setMouseTransparent(true);

        dialMarkerRotateList = new ArrayList<>();
        hourMarkerMatrixList = new ArrayList<>();

        for(int i = 0; i < Sunconfig.MAX_MARKER; i++) {

            double lineLength = Sunconfig.MARKER_HOUR_LENGTH * 0.50d;
            double strokeWidth = Sunconfig.MARKER_HOUR_STROKE_WIDTH;
            double opacity = 0.35d;

            if (i % 2 == 0) { lineLength = Sunconfig.MARKER_HOUR_LENGTH * (0.75d); opacity = 0.5d;}
            if (i % 4 == 0) { lineLength = Sunconfig.MARKER_HOUR_LENGTH; opacity = 1.0d; }
//            if ((i + 24) % 48 == 0) { lineLength = Sunconfig.CENTER_X - Sunconfig.MARGIN_X - Sunconfig.DOT_RADIUS; strokeWidth *= 1.0d;  }
//            if (i % 48 == 0) { lineLength = Sunconfig.MARKER_HOUR_LENGTH * 4.0d; }

            Rotate markerRotate = centerRotate.clone();
            markerRotate.setAngle(getNightCompressionAngle(i * 360d / 96d));

            Line markerLine = new Line(Sunconfig.CENTER_X, lineLength  + Sunconfig.MARGIN_Y, Sunconfig.CENTER_X, Sunconfig.MARGIN_Y + 1);
            markerLine.setStroke(Sunconfig.Color_Of_Darkness);
            markerLine.setStrokeWidth(strokeWidth);
            markerLine.setOpacity(opacity);
            markerLine.getTransforms().add(markerRotate);

            if (i % 4 == 0) {

                Group matrixMarkerGroup = new Group();

                Line matrixMarkerLine = new Line(Sunconfig.CENTER_X, lineLength  + Sunconfig.MARGIN_Y, Sunconfig.CENTER_X, Sunconfig.MARGIN_Y + 1);
                matrixMarkerLine.setStroke(Sunconfig.Color_Of_Darkness);
                matrixMarkerLine.setVisible(false);

                DotMatrix markerMatrix = new DotMatrix("" + ((12 + i / 4) % 24), Sunconfig.Color_Of_LocalTime);
                markerMatrix.setTranslateX(Sunconfig.CENTER_X - markerMatrix.getLayoutBounds().getWidth() / 2);
                markerMatrix.setTranslateY(Sunconfig.MATRIX_MARKER_OFFSET);
                markerMatrix.setStyle(Sunconfig.MATRIX_SHADOW);

                double rotationAdjust = i * -3.75d;
                markerMatrix.setRotate(rotationAdjust);
                markerMatrix.setScaleX(Sunconfig.MATRIX_HOUR_SCALE);
                markerMatrix.setScaleY(Sunconfig.MATRIX_HOUR_SCALE);

                matrixMarkerGroup.getChildren().addAll(matrixMarkerLine, markerMatrix);
                matrixMarkerGroup.getTransforms().add(markerRotate);

                dialHourMatrixMarkers.getChildren().add(matrixMarkerGroup);
                hourMarkerMatrixList.add(markerMatrix);
            }

            dialHourLineMarkers.getChildren().add(markerLine);
            dialMarkerRotateList.add(markerRotate);
        }

        Circle dialCircleCenterPoint = new Circle(Sunconfig.CENTER_X, Sunconfig.CENTER_Y, 1);
        dialCircleCenterPoint.setFill(Sunconfig.Color_Of_LocalTime);
        dialCircleCenterPoint.setStroke(Sunconfig.Color_Of_Void);

        controlNightCompression = new Circle(Sunconfig.CENTER_X, Sunconfig.CENTER_Y, Sunconfig.DOT_RADIUS);
        controlNightCompression.setFill(Sunconfig.Color_Of_LocalTime);
        controlNightCompression.setStroke(Sunconfig.Color_Of_Void);
        controlNightCompression.setStyle(Sunconfig.MATRIX_SHADOW2);

        sunTimeDial = new Line(Sunconfig.CENTER_X, Sunconfig.SUNTIME_DIAL_LENGTH, Sunconfig.CENTER_X, Sunconfig.MARGIN_Y);
        sunTimeDial.setStroke(Sunconfig.Color_Of_SunTime);
        sunTimeDial.setStrokeWidth(Sunconfig.SUNTIME_STROKE_WIDTH);
        sunTimeDial.getTransforms().add(sunTimeDialRotate);


        dialHighNoonGroup = new Group();

        Polygon dialHighNoonPoly = new Polygon(
                Sunconfig.CENTER_X - Sunconfig.HIGHNOON_STROKE_WIDTH, Sunconfig.MARGIN_Y + Sunconfig.MARKER_HOUR_LENGTH,
                Sunconfig.CENTER_X - Sunconfig.HIGHNOON_DIAL_WIDTH / 2, Sunconfig.MARGIN_Y,
                Sunconfig.CENTER_X - Sunconfig.HIGHNOON_STROKE_WIDTH / 2, Sunconfig.MARGIN_Y / 2,
                Sunconfig.CENTER_X - Sunconfig.HIGHNOON_STROKE_WIDTH / 2, Sunconfig.MARGIN_Y + Sunconfig.MARKER_HOUR_LENGTH,
                Sunconfig.CENTER_X + Sunconfig.HIGHNOON_STROKE_WIDTH / 2, Sunconfig.MARGIN_Y + Sunconfig.MARKER_HOUR_LENGTH,
                Sunconfig.CENTER_X + Sunconfig.HIGHNOON_STROKE_WIDTH / 2, Sunconfig.MARGIN_Y / 2,
                Sunconfig.CENTER_X + Sunconfig.HIGHNOON_DIAL_WIDTH / 2, Sunconfig.MARGIN_Y,
                Sunconfig.CENTER_X + Sunconfig.HIGHNOON_STROKE_WIDTH, Sunconfig.MARGIN_Y + Sunconfig.MARKER_HOUR_LENGTH
        );
        dialHighNoonPoly.setFill(Sunconfig.Color_Of_HighNoon);
        dialHighNoonPoly.setStroke(Sunconfig.Color_Of_Void);

        Polygon dialHighNoonPolyBackground = new Polygon(
                Sunconfig.CENTER_X - Sunconfig.HIGHNOON_STROKE_WIDTH, Sunconfig.MARGIN_Y + Sunconfig.MARKER_HOUR_LENGTH,
                Sunconfig.CENTER_X - Sunconfig.HIGHNOON_DIAL_WIDTH / 2, Sunconfig.MARGIN_Y,
                Sunconfig.CENTER_X - Sunconfig.HIGHNOON_STROKE_WIDTH / 2, Sunconfig.MARGIN_Y / 2,
                Sunconfig.CENTER_X + Sunconfig.HIGHNOON_STROKE_WIDTH / 2, Sunconfig.MARGIN_Y / 2,
                Sunconfig.CENTER_X + Sunconfig.HIGHNOON_DIAL_WIDTH / 2, Sunconfig.MARGIN_Y,
                Sunconfig.CENTER_X + Sunconfig.HIGHNOON_STROKE_WIDTH, Sunconfig.MARGIN_Y + Sunconfig.MARKER_HOUR_LENGTH
        );
        dialHighNoonPolyBackground.setFill(Sunconfig.Color_Of_Void);
        dialHighNoonPolyBackground.setStroke(Sunconfig.Color_Of_Void);

        dialHighNoonGroup.getChildren().addAll(dialHighNoonPolyBackground, dialHighNoonPoly);
        dialHighNoonGroup.getTransforms().add(highNoonDialRotate);
        dialHighNoonGroup.setStyle(Sunconfig.MATRIX_GLOW);
        dialHighNoonGroup.setBlendMode(BlendMode.SCREEN);


        dialLocalHourGroup = new Group();

        Polygon dialLocalHourPoly = new Polygon(
                Sunconfig.CENTER_X - Sunconfig.LOCALTIME_HOUR_STROKE_WIDTH, Sunconfig.LOCALTIME_DIAL_LENGTH,
                Sunconfig.CENTER_X - Sunconfig.LOCALTIME_HOUR_WIDTH / 2, Sunconfig.LOCALTIME_DIAL_LENGTH * 0.75,
                Sunconfig.CENTER_X - Sunconfig.LOCALTIME_HOUR_STROKE_WIDTH, Sunconfig.MARGIN_Y + 5,
                Sunconfig.CENTER_X, Sunconfig.MARGIN_Y,
                Sunconfig.CENTER_X - Sunconfig.LOCALTIME_HOUR_STROKE_WIDTH, Sunconfig.LOCALTIME_DIAL_LENGTH * 0.75,
                Sunconfig.CENTER_X - Sunconfig.LOCALTIME_HOUR_STROKE_WIDTH, Sunconfig.LOCALTIME_DIAL_LENGTH,
                Sunconfig.CENTER_X + Sunconfig.LOCALTIME_HOUR_STROKE_WIDTH, Sunconfig.LOCALTIME_DIAL_LENGTH,
                Sunconfig.CENTER_X + Sunconfig.LOCALTIME_HOUR_STROKE_WIDTH, Sunconfig.LOCALTIME_DIAL_LENGTH * 0.75,
                Sunconfig.CENTER_X, Sunconfig.MARGIN_Y,
                Sunconfig.CENTER_X + Sunconfig.LOCALTIME_HOUR_STROKE_WIDTH, Sunconfig.MARGIN_Y + 5,
                Sunconfig.CENTER_X + Sunconfig.LOCALTIME_HOUR_WIDTH / 2, Sunconfig.LOCALTIME_DIAL_LENGTH * 0.75,
                Sunconfig.CENTER_X + Sunconfig.LOCALTIME_HOUR_STROKE_WIDTH, Sunconfig.LOCALTIME_DIAL_LENGTH
        );
        dialLocalHourPoly.setFill(Color.WHITE);
        dialLocalHourPoly.setStroke(Sunconfig.Color_Of_Void);
        dialLocalHourPoly.setOpacity(1);

        dialLocalHourGroup.getChildren().addAll(dialLocalHourPoly);
        dialLocalHourGroup.getTransforms().add(dialRotateLocalHour);
        dialLocalHourGroup.setStyle(Sunconfig.LOCALHOUR_GLOW);
//        dialLocalHourGroup.setBlendMode(BlendMode.SCREEN);
        dialLocalHourGroup.setMouseTransparent(true);


        dialLocalMinuteGroup = new Group();

        Polygon dialLocalMinutePoly = new Polygon(
                Sunconfig.CENTER_X, Sunconfig.LOCALTIME_DIAL_LENGTH * 0.75,
                Sunconfig.CENTER_X - Sunconfig.LOCALTIME_MINUTE_STROKE_WIDTH * 1.50, Sunconfig.LOCALTIME_DIAL_LENGTH,
                Sunconfig.CENTER_X - Sunconfig.LOCALTIME_MINUTE_STROKE_WIDTH * 2.00, Sunconfig.LOCALTIME_DIAL_LENGTH,
                Sunconfig.CENTER_X - Sunconfig.LOCALTIME_MINUTE_WIDTH / 2, Sunconfig.LOCALTIME_DIAL_LENGTH * 0.85,
                Sunconfig.CENTER_X, Sunconfig.LOCALMINUTE_OFFSET + Sunconfig.LOCALMINUTE_POLY_HEIGHT,
                Sunconfig.CENTER_X + Sunconfig.LOCALTIME_MINUTE_WIDTH / 2, Sunconfig.LOCALTIME_DIAL_LENGTH * 0.85,
                Sunconfig.CENTER_X + Sunconfig.LOCALTIME_MINUTE_STROKE_WIDTH * 2.00, Sunconfig.LOCALTIME_DIAL_LENGTH,
                Sunconfig.CENTER_X + Sunconfig.LOCALTIME_MINUTE_STROKE_WIDTH * 1.50, Sunconfig.LOCALTIME_DIAL_LENGTH,
                Sunconfig.CENTER_X, Sunconfig.LOCALTIME_DIAL_LENGTH * 0.75
        );
        dialLocalMinutePoly.setFill(Sunconfig.Color_Of_Void);
        dialLocalMinutePoly.setStroke(Color.WHITE);
        dialLocalMinutePoly.setStrokeWidth(Sunconfig.LOCALTIME_MINUTE_STROKE_WIDTH);
        dialLocalMinutePoly.setOpacity(1);

        dialLocalMinuteGroup.getChildren().addAll(dialLocalMinutePoly);
        dialLocalMinuteGroup.getTransforms().add(dialRotateLocalMinute);
        dialLocalMinuteGroup.setStyle(Sunconfig.HELP_MARKER_GLOW);
        dialLocalMinuteGroup.setMouseTransparent(true);


        dialLocalSecondLedList = new ArrayList<>();
        dialLocalSecondOn = new ArrayList<>();
        dialLocalMinuteLedList = new ArrayList<>();
        dialLocalMinuteOn = new ArrayList<>();
        dialLocalSecondLedTransitionList = new ArrayList<>();
        dialLocalMinuteLedTransitionList = new ArrayList<>();

        for (int i = 0; i < 60; i++) {

            Rectangle localSecond = new Rectangle(Sunconfig.LOCALSECOND_WIDTH, Sunconfig.LOCALSECOND_HEIGHT);
            localSecond.setArcWidth(Sunconfig.LOCALSECOND_ROUND);
            localSecond.setArcHeight(Sunconfig.LOCALSECOND_ROUND);
            localSecond.setTranslateX(Sunconfig.CENTER_X - Sunconfig.LOCALSECOND_WIDTH / 2);
            localSecond.setTranslateY(Sunconfig.LOCALSECOND_OFFSET);
            localSecond.setFill(Sunconfig.Color_Of_Seconds);
            localSecond.setStroke(Sunconfig.Color_Of_Void);
            localSecond.setStyle(Sunconfig.LOCALSECOND_GLOW);
            localSecond.setMouseTransparent(true);

            Polygon localSecondPoly = new Polygon(
                    -1, Sunconfig.LOCALMINUTE_POLY_HEIGHT,
                    -Sunconfig.LOCALMINUTE_POLY_WIDTH, Sunconfig.LOCALMINUTE_POLY_WIDTH / 2,
                    -Sunconfig.LOCALMINUTE_POLY_WIDTH / 2, 0,
                    +Sunconfig.LOCALMINUTE_POLY_WIDTH / 2, 0,
                    +Sunconfig.LOCALMINUTE_POLY_WIDTH, Sunconfig.LOCALMINUTE_POLY_WIDTH / 2,
                    1, Sunconfig.LOCALMINUTE_POLY_HEIGHT
            );
            localSecondPoly.setTranslateX(Sunconfig.CENTER_X);
            localSecondPoly.setTranslateY(Sunconfig.LOCALMINUTE_OFFSET);
            localSecondPoly.setFill(Sunconfig.Color_Of_Seconds);
            localSecondPoly.setStroke(Sunconfig.Color_Of_Void);
            localSecondPoly.setStyle(Sunconfig.LOCALSECOND_GLOW);
            localSecondPoly.setMouseTransparent(true);

            Rotate localSecondRotate = new Rotate();
            localSecondRotate.setPivotX(Sunconfig.CENTER_X);
            localSecondRotate.setPivotY(Sunconfig.CENTER_Y);
            localSecondRotate.setAngle(i * 6);

            Group secondGroup = new Group(localSecondPoly);
            secondGroup.setOpacity(0.0);
            secondGroup.getTransforms().add(localSecondRotate);
            secondGroup.setBlendMode(BlendMode.SCREEN);

            Rectangle localMinute = new Rectangle(Sunconfig.LOCALMINUTE_WIDTH, Sunconfig.LOCALMINUTE_HEIGHT);
            localMinute.setArcWidth(Sunconfig.LOCALMINUTE_ROUND);
            localMinute.setArcHeight(Sunconfig.LOCALMINUTE_ROUND);
            localMinute.setTranslateX(Sunconfig.CENTER_X - Sunconfig.LOCALMINUTE_WIDTH / 2);
            localMinute.setTranslateY(Sunconfig.LOCALMINUTE_OFFSET);
            localMinute.setFill(Sunconfig.Color_Of_Minutes);
            localMinute.setStroke(Sunconfig.Color_Of_Void);
            localMinute.setStyle(Sunconfig.LOCALMINUTE_GLOW);
            localMinute.setMouseTransparent(true);

            Polygon localMinutePoly = new Polygon(
                    -1, Sunconfig.LOCALMINUTE_POLY_HEIGHT,
                    -Sunconfig.LOCALMINUTE_POLY_WIDTH, Sunconfig.LOCALMINUTE_POLY_WIDTH / 2,
                    -Sunconfig.LOCALMINUTE_POLY_WIDTH / 2, 0,
                    +Sunconfig.LOCALMINUTE_POLY_WIDTH / 2, 0,
                    +Sunconfig.LOCALMINUTE_POLY_WIDTH, Sunconfig.LOCALMINUTE_POLY_WIDTH / 2,
                    1, Sunconfig.LOCALMINUTE_POLY_HEIGHT
            );
            localMinutePoly.setTranslateX(Sunconfig.CENTER_X);
            localMinutePoly.setTranslateY(Sunconfig.LOCALMINUTE_OFFSET);
            localMinutePoly.setFill(Sunconfig.Color_Of_Minutes);
            localMinutePoly.setStroke(Sunconfig.Color_Of_Void);
            localMinutePoly.setStyle(Sunconfig.LOCALMINUTE_GLOW);
            localMinutePoly.setMouseTransparent(true);

            Rotate localMinuteRotate = new Rotate();
            localMinuteRotate.setPivotX(Sunconfig.CENTER_X);
            localMinuteRotate.setPivotY(Sunconfig.CENTER_Y);
            localMinuteRotate.setAngle(i * 6);

            Group minuteLedGroup = new Group(localMinutePoly);
            minuteLedGroup.setOpacity(0.0);
            minuteLedGroup.getTransforms().add(localMinuteRotate);
            minuteLedGroup.setBlendMode(BlendMode.SCREEN);

            dialLocalSecondLedList.add(secondGroup);
            dialLocalMinuteLedList.add(minuteLedGroup);

            dialLocalSecondOn.add(false);
            dialLocalMinuteOn.add(false);

            dialLocalSecondLedTransitionList.add(createTimelineForLED(secondGroup, ledOpacityDuration));
            dialLocalMinuteLedTransitionList.add(createTimelineForLED(minuteLedGroup, ledOpacityDuration));
        }


        Group sunriseGroup = new Group();

        sunriseDial = new Line(Sunconfig.CENTER_X, Sunconfig.SUNRISE_DIAL_LENGTH, Sunconfig.CENTER_X, Sunconfig.MARGIN_Y);
        sunriseDial.setStroke(Sunconfig.Color_Of_Horizon);
        sunriseDial.setStrokeWidth(Sunconfig.SUNRISE_STROKE_WIDTH);
//        sunriseDial.getTransforms().add(sunriseDialRotate);
        sunriseDial.setStyle(Sunconfig.HORIZON_GLOW);
//        sunriseDial.setBlendMode(BlendMode.SCREEN);

        String sunriseTimeString = ""
                + sunrise.get(Calendar.HOUR_OF_DAY)
                + ":" + sunrise.get(Calendar.MINUTE)
                + ":" + sunrise.get(Calendar.SECOND)
                ;

        matrixSunrise = new DotMatrix("00:00", Sunconfig.Color_Of_Horizon);
        matrixSunrise.setTranslateX(Sunconfig.CENTER_X - matrixSunrise.getLayoutBounds().getWidth() / 2 + matrixSunrise.getLayoutBounds().getHeight() / 2 + Sunconfig.MATRIX_HORIZON_SLIDE);
        matrixSunrise.setTranslateY(Sunconfig.MATRIX_HORIZON_OFFSET);
        matrixSunrise.setRotate(90d);
        matrixSunrise.setScaleX(Sunconfig.MATRIX_HORIZON_SCALE);
        matrixSunrise.setScaleY(Sunconfig.MATRIX_HORIZON_SCALE);
        matrixSunrise.setStyle(Sunconfig.MATRIX_GLOW2);

        sunriseGroup.getChildren().addAll(sunriseDial, matrixSunrise);
        sunriseGroup.getTransforms().add(sunriseDialRotate);


        Group sunsetGroup = new Group();

        sunsetDial = new Line(Sunconfig.CENTER_X, Sunconfig.SUNSET_DIAL_LENGTH, Sunconfig.CENTER_X, Sunconfig.MARGIN_Y);
        sunsetDial.setStroke(Sunconfig.Color_Of_Horizon);
        sunsetDial.setStrokeWidth(Sunconfig.SUNSET_STROKE_WIDTH);
//        sunsetDial.getTransforms().add(sunsetDialRotate);
        sunsetDial.setStyle(Sunconfig.HORIZON_GLOW);
//        sunsetDial.setBlendMode(BlendMode.SCREEN);

        String sunsetTimeString = ""
                + sunset.get(Calendar.HOUR_OF_DAY)
                + ":" + sunset.get(Calendar.MINUTE)
                + ":" + sunset.get(Calendar.SECOND)
                ;

        matrixSunset = new DotMatrix("00:00", Sunconfig.Color_Of_Horizon);
        matrixSunset.setTranslateX(Sunconfig.CENTER_X - matrixSunset.getLayoutBounds().getWidth() / 2 - matrixSunset.getLayoutBounds().getHeight() / 2 - Sunconfig.MATRIX_HORIZON_SLIDE);
        matrixSunset.setTranslateY(Sunconfig.MATRIX_HORIZON_OFFSET);
        matrixSunset.setRotate(-90d);
        matrixSunset.setScaleX(Sunconfig.MATRIX_HORIZON_SCALE);
        matrixSunset.setScaleY(Sunconfig.MATRIX_HORIZON_SCALE);
        matrixSunset.setStyle(Sunconfig.MATRIX_GLOW2);

        sunsetGroup.getChildren().addAll(sunsetDial, matrixSunset);
        sunsetGroup.getTransforms().add(sunsetDialRotate);

        horizonGroup = new Group();
        horizonGroup.getChildren().addAll(sunriseGroup, sunsetGroup);


        matrixDay = new DotMatrix("00", Sunconfig.Color_Of_LocalTime);

        DotMatrix matrixSeparatorDayToMonth = new DotMatrix(".", Sunconfig.Color_Of_LocalTime);
        matrixSeparatorDayToMonth.setTranslateX(matrixDay.getLayoutBounds().getWidth() + Sunconfig.MATRIX_SEPARATOR_OFFSET);

        matrixMonth = new DotMatrix("00", Sunconfig.Color_Of_LocalTime);
        matrixMonth.setTranslateX(matrixSeparatorDayToMonth.getLayoutBounds().getWidth() + matrixSeparatorDayToMonth.getTranslateX() + Sunconfig.MATRIX_SEPARATOR_OFFSET);

        DotMatrix matrixSeparatorMonthToYear = new DotMatrix(".", Sunconfig.Color_Of_LocalTime);
        matrixSeparatorMonthToYear.setTranslateX(matrixMonth.getLayoutBounds().getWidth() + matrixMonth.getTranslateX() + Sunconfig.MATRIX_SEPARATOR_OFFSET);

        matrixYear = new DotMatrix("0000", Sunconfig.Color_Of_LocalTime);
        matrixYear.setTranslateX(matrixSeparatorMonthToYear.getLayoutBounds().getWidth() + matrixSeparatorMonthToYear.getTranslateX() + Sunconfig.MATRIX_SEPARATOR_OFFSET);

        matrixDate = new Group();
        matrixDate.getChildren().addAll(matrixDay, matrixSeparatorDayToMonth, matrixMonth, matrixSeparatorMonthToYear, matrixYear);
        matrixDate.setScaleX(Sunconfig.MATRIX_DATE_SCALE);
        matrixDate.setScaleY(Sunconfig.MATRIX_DATE_SCALE);
        matrixDate.setLayoutX(Sunconfig.CENTER_X - matrixDate.getLayoutBounds().getWidth() / 2);
        matrixDate.setLayoutY(Sunconfig.CENTER_Y - matrixDate.getLayoutBounds().getHeight() / 2 + Sunconfig.MATRIX_DATE_OFFSET);


        matrixWeek = new DotMatrix("00", Sunconfig.Color_Of_LocalTime);
        matrixWeek.setScaleX(Sunconfig.MATRIX_WEEK_SCALE);
        matrixWeek.setScaleY(Sunconfig.MATRIX_WEEK_SCALE);
        matrixWeek.setLayoutX(Sunconfig.CENTER_X - matrixWeek.getLayoutBounds().getWidth() / 2);
        matrixWeek.setLayoutY(Sunconfig.CENTER_Y - matrixWeek.getLayoutBounds().getHeight() / 2 + Sunconfig.MATRIX_WEEK_OFFSET);


        matrixHour = new DotMatrix("00", Sunconfig.Color_Of_LocalTime);

        DotMatrix matrixSeparatorHourToMinute = new DotMatrix(":", Sunconfig.Color_Of_LocalTime);
        matrixSeparatorHourToMinute.setTranslateX(matrixHour.getLayoutBounds().getWidth() + Sunconfig.MATRIX_SEPARATOR_OFFSET);

        matrixMinute = new DotMatrix("00", Sunconfig.Color_Of_LocalTime);
        matrixMinute.setTranslateX(matrixSeparatorHourToMinute.getLayoutBounds().getWidth() + matrixSeparatorHourToMinute.getTranslateX()/* + Sunconfig.MATRIX_SEPARATOR_OFFSET*/);

        DotMatrix matrixSeparatorMinuteToSecond = new DotMatrix(":", Sunconfig.Color_Of_LocalTime);
        matrixSeparatorMinuteToSecond.setTranslateX(matrixMinute.getLayoutBounds().getWidth() + matrixMinute.getTranslateX() + Sunconfig.MATRIX_SEPARATOR_OFFSET);

        matrixSecond = new DotMatrix("00", Sunconfig.Color_Of_LocalTime);
        matrixSecond.setTranslateX(matrixSeparatorMinuteToSecond.getLayoutBounds().getWidth() + matrixSeparatorMinuteToSecond.getTranslateX() + Sunconfig.MATRIX_SEPARATOR_OFFSET);


        matrixTime = new Group();
        matrixTime.getChildren().addAll(matrixHour, matrixMinute);
        matrixTime.setScaleX(Sunconfig.MATRIX_TIME_SCALE);
        matrixTime.setScaleY(Sunconfig.MATRIX_TIME_SCALE);
        matrixTime.setLayoutX(Sunconfig.CENTER_X - matrixTime.getLayoutBounds().getWidth() / 2);
        matrixTime.setLayoutY(Sunconfig.CENTER_Y - matrixTime.getLayoutBounds().getHeight() / 2 + Sunconfig.MATRIX_TIME_OFFSET);


        dialArcDayLength = new Arc(Sunconfig.CENTER_X, Sunconfig.CENTER_Y, Sunconfig.DAYLENGTH_ARC_RADIUS, Sunconfig.DAYLENGTH_ARC_RADIUS, 90 - sunsetDialAngle, 360 - (sunsetDialAngle - sunriseDialAngle));
        dialArcDayLength.setType(ArcType.OPEN);
        dialArcDayLength.setStroke(Sunconfig.Color_Of_LocalTime);
        dialArcDayLength.setStrokeWidth(Sunconfig.DAYLENGTH_STROKE_WIDTH);
        dialArcDayLength.setFill(Sunconfig.Color_Of_Void);
        dialArcDayLength.setOpacity(Sunconfig.DAYLENGTH_ARC_OPACITY);
        dialArcDayLength.setMouseTransparent(true);

        matrixDayLength = new DotMatrix("00h00m00s", Sunconfig.Color_Of_LocalTime);
        matrixDayLength.setScaleX(Sunconfig.MATRIX_DAYLENGTH_SCALE);
        matrixDayLength.setScaleY(Sunconfig.MATRIX_DAYLENGTH_SCALE);
        matrixDayLength.setLayoutX(Sunconfig.CENTER_X - matrixDayLength.getLayoutBounds().getWidth() / 2);
        matrixDayLength.setLayoutY(Sunconfig.CENTER_Y + matrixDayLength.getLayoutBounds().getHeight() - Sunconfig.DAYLENGTH_ARC_RADIUS * 0.95);
        matrixDayLength.setStyle(Sunconfig.LOCALTIME_SHADOW);


        matrixLongitude = new DotMatrix("000.00E", Sunconfig.Color_Of_LocalTime);
        matrixLongitude.setScaleX(Sunconfig.MATRIX_LONGITUDE_SCALE);
        matrixLongitude.setScaleY(Sunconfig.MATRIX_LONGITUDE_SCALE);
        matrixLongitude.setLayoutX(Sunconfig.CENTER_X + Sunconfig.MATRIX_LONGITUDE_SLIDE - matrixLongitude.getLayoutBounds().getWidth() / 2);
        matrixLongitude.setLayoutY(Sunconfig.CENTER_Y + matrixLongitude.getLayoutBounds().getHeight() + Sunconfig.MATRIX_LONGITUDE_OFFSET);

        Rectangle longitudeBackdrop = new Rectangle(
                matrixLongitude.getLayoutBounds().getMinX(),
                matrixLongitude.getLayoutBounds().getMinY(),
                matrixLongitude.getLayoutBounds().getWidth(),
                matrixLongitude.getLayoutBounds().getHeight());
        longitudeBackdrop.setOpacity(0);

        matrixLongitude.getChildren().add(longitudeBackdrop);
        matrixLongitude.setVisible(false);


        matrixLatitude = new DotMatrix("000.00N", Sunconfig.Color_Of_LocalTime);
        matrixLatitude.setScaleX(Sunconfig.MATRIX_LATITUDE_SCALE);
        matrixLatitude.setScaleY(Sunconfig.MATRIX_LATITUDE_SCALE);
        matrixLatitude.setLayoutX(Sunconfig.CENTER_X + Sunconfig.MATRIX_LATITUDE_SLIDE - matrixLatitude.getLayoutBounds().getWidth() / 2);
        matrixLatitude.setLayoutY(Sunconfig.CENTER_Y + matrixLatitude.getLayoutBounds().getHeight() + Sunconfig.MATRIX_LATITUDE_OFFSET);

        Rectangle latitudeBackdrop = new Rectangle(
                matrixLatitude.getLayoutBounds().getMinX(),
                matrixLatitude.getLayoutBounds().getMinY(),
                matrixLatitude.getLayoutBounds().getWidth(),
                matrixLatitude.getLayoutBounds().getHeight());
        latitudeBackdrop.setOpacity(0);

        matrixLatitude.getChildren().add(latitudeBackdrop);
        matrixLatitude.setVisible(false);


        matrixHighNoon = new DotMatrix("00:00:00", Sunconfig.Color_Of_HighNoon);
        matrixHighNoon.setScaleX(Sunconfig.MATRIX_HIGHNOON_SCALE);
        matrixHighNoon.setScaleY(Sunconfig.MATRIX_HIGHNOON_SCALE);
        matrixHighNoon.setLayoutX(Sunconfig.CENTER_X - matrixHighNoon.getLayoutBounds().getWidth() / 2);
        matrixHighNoon.setLayoutY(Sunconfig.CENTER_Y - matrixHighNoon.getLayoutBounds().getHeight() * 1.5d - Sunconfig.DAYLENGTH_ARC_RADIUS * 1.1);
        matrixHighNoon.setStyle(Sunconfig.MATRIX_GLOW);
        matrixHighNoon.setMouseTransparent(true);
        matrixHighNoon.setVisible(false);


        matrixTimeZone = new DotMatrix("GMT+00", Sunconfig.Color_Of_LocalTime);
        matrixTimeZone.setScaleX(Sunconfig.MATRIX_TIMEZONE_SCALE);
        matrixTimeZone.setScaleY(Sunconfig.MATRIX_TIMEZONE_SCALE);
        matrixTimeZone.setLayoutX(Sunconfig.CENTER_X - matrixTimeZone.getLayoutBounds().getWidth() / 2);
        matrixTimeZone.setLayoutY(Sunconfig.MATRIX_TIMEZONE_OFFSET);
        matrixTimeZone.setStyle(Sunconfig.MATRIX_SHADOW);
        matrixTimeZone.setVisible(false);


        setGroupGlow(matrixYear, Sunconfig.MATRIX_SHADOW);
        setGroupGlow(matrixMonth, Sunconfig.MATRIX_SHADOW);
        setGroupGlow(matrixDay, Sunconfig.MATRIX_SHADOW);
        setGroupGlow(matrixHour, Sunconfig.MATRIX_SHADOW);
        setGroupGlow(matrixMinute, Sunconfig.MATRIX_SHADOW);
        setGroupGlow(matrixSecond, Sunconfig.MATRIX_SHADOW);
        setGroupGlow(matrixWeek, Sunconfig.MATRIX_SHADOW);
        setGroupGlow(matrixLongitude, Sunconfig.MATRIX_SHADOW);
        setGroupGlow(matrixLatitude, Sunconfig.MATRIX_SHADOW);
        setGroupGlow(longitudeGroup, Sunconfig.MATRIX_SHADOW);
        setGroupGlow(latitudeGroup, Sunconfig.MATRIX_SHADOW);

        matrixSeparatorDayToMonth.setStyle(Sunconfig.MATRIX_SHADOW);
        matrixSeparatorHourToMinute.setStyle(Sunconfig.MATRIX_SHADOW);
        matrixSeparatorMinuteToSecond.setStyle(Sunconfig.MATRIX_SHADOW);
        matrixSeparatorMonthToYear.setStyle(Sunconfig.MATRIX_SHADOW);


        nightModeOverlay = new Group();

        Rectangle nightModeRectangle = new Rectangle(Sunconfig.DIAL_WIDTH, Sunconfig.DIAL_HEIGHT);
        nightModeRectangle.setArcWidth(Sunconfig.NIGHTMODE_RECTANGLE_ROUND);
        nightModeRectangle.setArcHeight(Sunconfig.NIGHTMODE_RECTANGLE_ROUND);
        nightModeRectangle.setFill(Color.BLACK);
        nightModeRectangle.setStroke(Sunconfig.Color_Of_Void);
        nightModeRectangle.setBlendMode(BlendMode.BLUE);
        nightModeRectangle.setOpacity(Sunconfig.NIGHTMODE_RECTANGLE_OPACITY);
        nightModeRectangle.setMouseTransparent(true);

        nightModeOverlay.getChildren().addAll(nightModeRectangle);
        nightModeOverlay.setVisible(false);


        helpOverlay = new Group();
        helpMarkers = new ArrayList<>();

        Rectangle helpBackdrop = new Rectangle(Sunconfig.DIAL_WIDTH, Sunconfig.DIAL_HEIGHT);
        helpBackdrop.setArcWidth(Sunconfig.HELP_OVERLAY_ROUND);
        helpBackdrop.setArcHeight(Sunconfig.HELP_OVERLAY_ROUND);
        helpBackdrop.setFill(Color.BLACK);
        helpBackdrop.setStroke(Sunconfig.Color_Of_Void);
        helpBackdrop.setBlendMode(BlendMode.MULTIPLY);
        helpBackdrop.setOpacity(Sunconfig.HELP_OVERLAY_OPACITY);
        helpBackdrop.setMouseTransparent(true);

        Circle helpWindowMarker = new Circle(Sunconfig.CENTER_X, Sunconfig.CENTER_Y, Sunconfig.DIAL_WIDTH / 2 - 1);
        helpWindowMarker.setFill(Sunconfig.Color_Of_Void);
        helpWindowMarker.setStroke(Color.WHITE);
        helpWindowMarker.setStyle(Sunconfig.HELP_MARKER_GLOW);
        helpWindowMarker.setMouseTransparent(true);

        Circle helpGlobeMarker = new Circle(Sunconfig.CENTER_X, Sunconfig.CENTER_Y, Sunconfig.DIAL_WIDTH / 2 - Sunconfig.MARGIN_X);
        helpGlobeMarker.setFill(Sunconfig.Color_Of_Void);
        helpGlobeMarker.setStroke(Color.WHITE);
        helpGlobeMarker.setStyle(Sunconfig.HELP_MARKER_GLOW);
        helpGlobeMarker.setMouseTransparent(true);
        helpGlobeMarker.visibleProperty().bind(globeMasterGroup.visibleProperty());

        helpMarkers.add(createHelpMarkerGroup(Sunconfig.CENTER_X, Sunconfig.HIGHNOON_DIAL_LENGTH / 2, dialHighNoonGroup, highNoonDialRotate));
        helpMarkers.add(createHelpMarkerGroup(getCenterX(matrixDayLength), getCenterY(matrixDayLength), matrixDayLength));
        helpMarkers.add(createHelpMarkerGroup(getCenterX(matrixHour), getCenterY(matrixHour), matrixHour));
        helpMarkers.add(createHelpMarkerGroup(getCenterX(matrixMinute), getCenterY(matrixMinute), matrixMinute));
        helpMarkers.add(createHelpMarkerGroup(getCenterX(matrixDay), getCenterY(matrixDay), matrixDay));
        helpMarkers.add(createHelpMarkerGroup(getCenterX(matrixMonth), getCenterY(matrixMonth), matrixMonth));
        helpMarkers.add(createHelpMarkerGroup(getCenterX(matrixYear), getCenterY(matrixYear), matrixYear));
        helpMarkers.add(createHelpMarkerGroup(getCenterX(matrixTimeZone), getCenterY(matrixTimeZone), matrixTimeZone));
        helpMarkers.add(createHelpMarkerGroup(getCenterX(matrixLongitude), getCenterY(matrixLongitude), matrixLongitude));
        helpMarkers.add(createHelpMarkerGroup(getCenterX(matrixLatitude), getCenterY(matrixLatitude), matrixLatitude));
        helpMarkers.add(createHelpMarkerGroup(0, 0, tinyGlobeFrame));
        helpMarkers.add(createHelpMarkerGroup(Sunconfig.CONTROL_RESIZE_SIZE * 0.666, Sunconfig.CONTROL_RESIZE_SIZE * 0.666, controlThingyResize));
        helpMarkers.add(createHelpMarkerGroup(0, 0, controlThingyMaximize));
        helpMarkers.add(createHelpMarkerGroup(0, 0, controlThingyMinimize));
        helpMarkers.add(createHelpMarkerGroup(0, 0, controlThingyClose));
        helpMarkers.add(createHelpMarkerGroup(0, 0, controlThingyNightmode));
        helpMarkers.add(createHelpMarkerGroup(0, 0, controlThingyAlwaysOnTop));
        helpMarkers.add(createHelpMarkerGroup(Sunconfig.CENTER_X, Sunconfig.CENTER_Y, controlNightCompression));

        helpOverlay.getChildren().addAll(helpBackdrop);
        helpOverlay.getChildren().addAll(helpWindowMarker, helpGlobeMarker);
        helpOverlay.getChildren().addAll(helpMarkers);
        helpOverlay.setVisible(false);


        helpText = new Text();
        helpText.setFont(new Font(12));
        helpText.setStroke(Color.WHITE);
        helpText.setFill(Sunconfig.Color_Of_Void);
        helpText.setText(Sunconfig.HELPTEXT_DEFAULT);
        helpText.setTranslateX(5);
        helpText.setTranslateY(15);

        Rectangle helpTextRectangle = new Rectangle(0, 0, 20, 20);
        helpTextRectangle.setArcWidth(10);
        helpTextRectangle.setArcHeight(10);
        helpTextRectangle.setStroke(Sunconfig.Color_Of_Void);
        helpTextRectangle.setFill(Color.BLACK);
        helpTextRectangle.setOpacity(0.50);

        helpTextRectangle.widthProperty().bind(Bindings.createDoubleBinding(() -> {
            double size = helpText.layoutBoundsProperty().get().getWidth();
            return size + 10;
        }, helpText.layoutBoundsProperty()));

        helpTextRectangle.heightProperty().bind(Bindings.createDoubleBinding(() -> {
            double size = helpText.layoutBoundsProperty().get().getHeight();
            return size + 5;
        }, helpText.layoutBoundsProperty()));

        helpTextGroup = new Group(helpTextRectangle, helpText);
        helpTextGroup.setMouseTransparent(true);
        helpTextGroup.setVisible(false);


        infoText = new Text();
        infoText.setFont(new Font(12));
        infoText.setStroke(Color.WHITE);
        infoText.setFill(Sunconfig.Color_Of_Void);
        infoText.setText(Sunconfig.HELPTEXT_DEFAULT);
        infoText.setTranslateX(5);
        infoText.setTranslateY(15);

        Rectangle infoTextRectangle = new Rectangle(0, 0, 20, 20);
        infoTextRectangle.setArcWidth(10);
        infoTextRectangle.setArcHeight(10);
        infoTextRectangle.setStroke(Sunconfig.Color_Of_Void);
        infoTextRectangle.setFill(Color.BLACK);
        infoTextRectangle.setOpacity(0.50);

        infoTextRectangle.widthProperty().bind(Bindings.createDoubleBinding(() -> {
            double size = infoText.layoutBoundsProperty().get().getWidth();
            return size + 10;
        }, infoText.layoutBoundsProperty()));

        infoTextRectangle.heightProperty().bind(Bindings.createDoubleBinding(() -> {
            double size = infoText.layoutBoundsProperty().get().getHeight();
            return size + 5;
        }, infoText.layoutBoundsProperty()));

        infoTextGroup = new Group(infoTextRectangle, infoText);
        infoTextGroup.setMouseTransparent(true);
        infoTextGroup.setVisible(false);


        // LAYERS
        backgroundGroup = new Group();
        backgroundGroup.getChildren().add(dialMarginFillBox);
        backgroundGroup.getChildren().add(dialMarginCircle);
        SubScene backgroundScene = new SubScene(backgroundGroup, Sunconfig.DIAL_WIDTH, Sunconfig.DIAL_HEIGHT, true, SceneAntialiasing.DISABLED);

        Group foregroundGroup = new Group();
        foregroundGroup.getChildren().add(globeMasterGroup);
        foregroundGroup.getChildren().add(dialCircleBackground);
        foregroundGroup.getChildren().add(dialArcNight);
        foregroundGroup.getChildren().add(dialArcMidnight);
        foregroundGroup.getChildren().add(dialMinuteMarkers);
        foregroundGroup.getChildren().add(dialArcDayLength);
        foregroundGroup.getChildren().add(dialCircleFrame);
        foregroundGroup.getChildren().add(dialHourLineMarkers);
        foregroundGroup.getChildren().add(cetusArcGroup);
        foregroundGroup.getChildren().add(cetusLineGroup);
        foregroundGroup.getChildren().add(dialLocalMinuteGroup);
        foregroundGroup.getChildren().addAll(dialLocalSecondLedList);
        foregroundGroup.getChildren().addAll(dialLocalMinuteLedList);
        foregroundGroup.getChildren().add(dialHighNoonGroup);
        foregroundGroup.getChildren().add(dialLocalHourGroup);
        foregroundGroup.getChildren().add(horizonGroup);
        foregroundGroup.getChildren().add(dialHourMatrixMarkers);
        foregroundGroup.getChildren().add(dialCircleCenterPoint);
        foregroundGroup.getChildren().add(controlNightCompression);
        foregroundGroup.getChildren().add(cetusTimer);
        foregroundGroup.getChildren().add(matrixDayLength);
        foregroundGroup.getChildren().add(matrixHighNoon);
        foregroundGroup.getChildren().add(matrixTimeZone);
        foregroundGroup.getChildren().add(tinyGlobeGroup);
        foregroundGroup.getChildren().add(matrixTime);
        foregroundGroup.getChildren().add(matrixDate);
        foregroundGroup.getChildren().add(matrixLongitude);
        foregroundGroup.getChildren().add(matrixLatitude);

        foregroundGroup.getChildren().add(controlThingyResize);
        foregroundGroup.getChildren().add(controlThingyClose);
        foregroundGroup.getChildren().add(controlThingyMaximize);
        foregroundGroup.getChildren().add(controlThingyMinimize);
        foregroundGroup.getChildren().add(controlThingyNightmode);
        foregroundGroup.getChildren().add(controlThingyAlwaysOnTop);

        foregroundGroup.getChildren().add(helpOverlay);
        foregroundGroup.getChildren().add(controlThingyHelp);
        foregroundGroup.getChildren().add(helpTextGroup);
        foregroundGroup.getChildren().add(infoTextGroup);

        foregroundGroup.getChildren().add(nightModeOverlay);

        SubScene foregroundScene = new SubScene(foregroundGroup, Sunconfig.DIAL_WIDTH, Sunconfig.DIAL_HEIGHT, true, SceneAntialiasing.DISABLED);

        dialsGroup.getChildren().addAll(backgroundScene, foregroundScene);

        setCetusTimeVisibility(cetusTimeVisibleEh);

        // Apply scale global scale
        dialsGroup.setScaleX(Sunconfig.SCALE_X);
        dialsGroup.setScaleY(Sunconfig.SCALE_Y);


        // EVENTS
        controlNightCompression.setOnMouseEntered(event -> { helpText.setText(Sunconfig.HELPTEXT_NIGHTCOMPRESSION); controlNightCompression.setCursor(Cursor.V_RESIZE); controlNightCompression.setStyle(Sunconfig.MATRIX_GLOW2); });
        controlNightCompression.setOnMouseExited(event -> { helpText.setText(Sunconfig.HELPTEXT_DEFAULT); controlNightCompression.setCursor(Cursor.DEFAULT); controlNightCompression.setStyle(Sunconfig.MATRIX_SHADOW2); });

        controlThingyHelp.setOnMouseEntered(event -> { helpText.setText(Sunconfig.HELPTEXT_DEFAULT); controlThingyHelp.setCursor(Cursor.HAND); setGroupGlow(controlThingyHelp, Sunconfig.CONTROL_RESIZE_GLOW); });
        controlThingyHelp.setOnMouseExited(event -> { helpText.setText(Sunconfig.HELPTEXT_DEFAULT); controlThingyHelp.setCursor(Cursor.DEFAULT); setGroupGlow(controlThingyHelp, Sunconfig.CONTROL_RESIZE_SHADOW); });

        controlThingyResize.setOnMouseEntered(event -> { helpText.setText(Sunconfig.HELPTEXT_RESIZE); controlThingyResize.setCursor(Cursor.NW_RESIZE); setGroupGlow(controlThingyResize, Sunconfig.CONTROL_RESIZE_GLOW); });
        controlThingyResize.setOnMouseExited(event -> { helpText.setText(Sunconfig.HELPTEXT_DEFAULT); controlThingyResize.setCursor(Cursor.DEFAULT); setGroupGlow(controlThingyResize, Sunconfig.CONTROL_RESIZE_SHADOW); });

        controlThingyClose.setOnMouseEntered(event -> { helpText.setText(Sunconfig.HELPTEXT_CLOSE); controlThingyClose.setCursor(Cursor.HAND); setGroupGlow(controlThingyClose, Sunconfig.CONTROL_CLOSE_GLOW); });
        controlThingyClose.setOnMouseExited(event -> { helpText.setText(Sunconfig.HELPTEXT_DEFAULT); controlThingyClose.setCursor(Cursor.DEFAULT); setGroupGlow(controlThingyClose, Sunconfig.CONTROL_CLOSE_SHADOW); });

        controlThingyMaximize.setOnMouseEntered(event -> { helpText.setText(Sunconfig.HELPTEXT_MAXIMIZE); controlThingyMaximize.setCursor(Cursor.HAND); setGroupGlow(controlThingyMaximize, Sunconfig.CONTROL_MAXIMIZE_GLOW); });
        controlThingyMaximize.setOnMouseExited(event -> { helpText.setText(Sunconfig.HELPTEXT_DEFAULT); controlThingyMaximize.setCursor(Cursor.DEFAULT); setGroupGlow(controlThingyMaximize, Sunconfig.CONTROL_MAXIMIZE_SHADOW); });

        controlThingyMinimize.setOnMouseEntered(event -> { helpText.setText(Sunconfig.HELPTEXT_MINIMIZE); controlThingyMinimize.setCursor(Cursor.HAND); setGroupGlow(controlThingyMinimize, Sunconfig.CONTROL_MINIMIZE_GLOW); });
        controlThingyMinimize.setOnMouseExited(event -> { helpText.setText(Sunconfig.HELPTEXT_DEFAULT); controlThingyMinimize.setCursor(Cursor.DEFAULT); setGroupGlow(controlThingyMinimize, Sunconfig.CONTROL_MINIMIZE_SHADOW); });

        controlThingyNightmode.setOnMouseEntered(event -> { helpText.setText(Sunconfig.HELPTEXT_NIGHTMODE); controlThingyNightmode.setCursor(Cursor.HAND); setGroupGlow(controlThingyNightmode, Sunconfig.CONTROL_NIGHTMODE_GLOW); });
        controlThingyNightmode.setOnMouseExited(event -> { helpText.setText(Sunconfig.HELPTEXT_DEFAULT); controlThingyNightmode.setCursor(Cursor.DEFAULT); setGroupGlow(controlThingyNightmode, Sunconfig.CONTROL_NIGHTMODE_SHADOW); });

        controlThingyAlwaysOnTop.setOnMouseEntered(event -> { helpText.setText(Sunconfig.HELPTEXT_ALWAYSONTOP); controlThingyAlwaysOnTop.setCursor(Cursor.HAND); setGroupGlow(controlThingyAlwaysOnTop, Sunconfig.CONTROL_ALWAYSONTOP_GLOW); });
        controlThingyAlwaysOnTop.setOnMouseExited(event -> { helpText.setText(Sunconfig.HELPTEXT_DEFAULT); controlThingyAlwaysOnTop.setCursor(Cursor.DEFAULT); setGroupGlow(controlThingyAlwaysOnTop, Sunconfig.CONTROL_ALWAYSONTOP_SHADOW); });

        matrixYear.setOnMouseEntered(event -> { helpText.setText(Sunconfig.HELPTEXT_YEAR); matrixYear.setCursor(Cursor.V_RESIZE); setGroupGlow(matrixYear, Sunconfig.MATRIX_GLOW); });
        matrixYear.setOnMouseExited(event -> { helpText.setText(Sunconfig.HELPTEXT_DEFAULT); matrixYear.setCursor(Cursor.DEFAULT); setGroupGlow(matrixYear, Sunconfig.MATRIX_SHADOW); });

        matrixMonth.setOnMouseEntered(event -> { helpText.setText(Sunconfig.HELPTEXT_MONTH); matrixMonth.setCursor(Cursor.V_RESIZE); setGroupGlow(matrixMonth, Sunconfig.MATRIX_GLOW); });
        matrixMonth.setOnMouseExited(event -> { helpText.setText(Sunconfig.HELPTEXT_DEFAULT); matrixMonth.setCursor(Cursor.DEFAULT); setGroupGlow(matrixMonth, Sunconfig.MATRIX_SHADOW); });

        matrixDay.setOnMouseEntered(event -> { helpText.setText(Sunconfig.HELPTEXT_DAY); matrixDay.setCursor(Cursor.V_RESIZE); setGroupGlow(matrixDay, Sunconfig.MATRIX_GLOW); });
        matrixDay.setOnMouseExited(event -> { helpText.setText(Sunconfig.HELPTEXT_DEFAULT); matrixDay.setCursor(Cursor.DEFAULT); setGroupGlow(matrixDay, Sunconfig.MATRIX_SHADOW); });

        matrixHour.setOnMouseEntered(event -> { helpText.setText(Sunconfig.HELPTEXT_HOUR); matrixHour.setCursor(Cursor.V_RESIZE); setGroupGlow(matrixHour, Sunconfig.MATRIX_GLOW); });
        matrixHour.setOnMouseExited(event -> { helpText.setText(Sunconfig.HELPTEXT_DEFAULT); matrixHour.setCursor(Cursor.DEFAULT); setGroupGlow(matrixHour, Sunconfig.MATRIX_SHADOW); });

        matrixMinute.setOnMouseEntered(event -> { helpText.setText(Sunconfig.HELPTEXT_MINUTE); matrixMinute.setCursor(Cursor.V_RESIZE); setGroupGlow(matrixMinute, Sunconfig.MATRIX_GLOW); });
        matrixMinute.setOnMouseExited(event -> { helpText.setText(Sunconfig.HELPTEXT_DEFAULT); matrixMinute.setCursor(Cursor.DEFAULT); setGroupGlow(matrixMinute, Sunconfig.MATRIX_SHADOW); });

        matrixWeek.setOnMouseEntered(event -> { matrixWeek.setCursor(Cursor.V_RESIZE); setGroupGlow(matrixWeek, Sunconfig.MATRIX_GLOW); });
        matrixWeek.setOnMouseExited(event -> { matrixWeek.setCursor(Cursor.DEFAULT); setGroupGlow(matrixWeek, Sunconfig.MATRIX_SHADOW); });

        matrixLongitude.setOnMouseEntered(event -> { helpText.setText(Sunconfig.HELPTEXT_LONGITUDE); matrixLongitude.setCursor(Cursor.H_RESIZE); setGroupGlow(matrixLongitude, Sunconfig.MATRIX_GLOW); });
        matrixLongitude.setOnMouseExited(event -> { helpText.setText(Sunconfig.HELPTEXT_DEFAULT); matrixLongitude.setCursor(Cursor.DEFAULT); setGroupGlow(matrixLongitude, Sunconfig.MATRIX_SHADOW); });

        matrixLatitude.setOnMouseEntered(event -> { helpText.setText(Sunconfig.HELPTEXT_LATITUDE); matrixLatitude.setCursor(Cursor.V_RESIZE); setGroupGlow(matrixLatitude, Sunconfig.MATRIX_GLOW); });
        matrixLatitude.setOnMouseExited(event -> { helpText.setText(Sunconfig.HELPTEXT_DEFAULT); matrixLatitude.setCursor(Cursor.DEFAULT); setGroupGlow(matrixLatitude, Sunconfig.MATRIX_SHADOW); });

        tinyGlobeFrame.setOnMouseEntered(event -> { helpText.setText(Sunconfig.HELPTEXT_TINYGLOBE); tinyGlobeFrame.setCursor(Cursor.HAND); tinyGlobeFrame.setStyle(Sunconfig.MATRIX_GLOW); });
        tinyGlobeFrame.setOnMouseExited(event -> { helpText.setText(Sunconfig.HELPTEXT_DEFAULT); tinyGlobeFrame.setCursor(Cursor.DEFAULT); tinyGlobeFrame.setStyle(Sunconfig.MATRIX_SHADOW); });

        dialMarginCircle.setOnMouseEntered(event -> { helpText.setText(Sunconfig.HELPTEXT_WINDOW); dialMarginCircle.setCursor(Cursor.MOVE); });
        dialMarginCircle.setOnMouseExited(event -> {  helpText.setText(Sunconfig.HELPTEXT_DEFAULT); dialMarginCircle.setCursor(Cursor.DEFAULT); });

        dialCircleFrame.setOnMouseEntered(event -> { helpText.setText(globeVisibleEh ? Sunconfig.HELPTEXT_GLOBE : Sunconfig.HELPTEXT_WINDOW); dialCircleFrame.setCursor(globeVisibleEh ? Cursor.OPEN_HAND : Cursor.MOVE); });
        dialCircleFrame.setOnMouseExited(event -> { helpText.setText(Sunconfig.HELPTEXT_DEFAULT); dialCircleFrame.setCursor(Cursor.DEFAULT); });

        matrixDayLength.setOnMouseEntered(event -> { helpText.setText(Sunconfig.HELPTEXT_HORIZON); matrixDayLength.setCursor(Cursor.HAND); setGroupGlow(matrixDayLength, Sunconfig.MATRIX_GLOW); });
        matrixDayLength.setOnMouseExited(event -> { helpText.setText(Sunconfig.HELPTEXT_DEFAULT); matrixDayLength.setCursor(Cursor.DEFAULT); setGroupGlow(matrixDayLength, Sunconfig.MATRIX_SHADOW); });

        dialHighNoonGroup.setOnMouseEntered(event -> { helpText.setText(Sunconfig.HELPTEXT_HIGHNOON); dialHighNoonGroup.setCursor(Cursor.HAND); matrixHighNoon.setVisible(true); setGroupGlow(dialHighNoonGroup, Sunconfig.MATRIX_GLOW2); });
        dialHighNoonGroup.setOnMouseExited(event -> { helpText.setText(Sunconfig.HELPTEXT_DEFAULT); dialHighNoonGroup.setCursor(Cursor.DEFAULT); matrixHighNoon.setVisible(false); setGroupGlow(dialHighNoonGroup, Sunconfig.MATRIX_GLOW); });

        matrixTimeZone.setOnMouseEntered(event -> { helpText.setText(Sunconfig.HELPTEXT_TIMEZONE); matrixTimeZone.setCursor(Cursor.V_RESIZE); matrixTimeZone.setStyle(Sunconfig.MATRIX_GLOW); });
        matrixTimeZone.setOnMouseExited(event -> { helpText.setText(Sunconfig.HELPTEXT_DEFAULT); matrixTimeZone.setCursor(Cursor.DEFAULT); matrixTimeZone.setStyle(Sunconfig.MATRIX_SHADOW); });

        dialsGroup.setOnMouseMoved(event -> {
            if (helpTextGroup.isVisible()) { moveGroup(helpTextGroup, event); }
            if (infoTextGroup.isVisible()) { moveGroup(infoTextGroup, event); }
        });

    }


    // Utility
    public void moveGroup(Node node, Event event) {

        double mouseX, mouseY;

        if (event != null) {
            if (event instanceof DragEvent) {
                mouseX = ((DragEvent) event).getX();
                mouseY = ((DragEvent) event).getY();
            } else if (event instanceof MouseEvent) {
                mouseX = ((MouseEvent) event).getX();
                mouseY = ((MouseEvent) event).getY();
            } else {
                return;
            }
        } else {
            mouseX = node.getLayoutBounds().getMaxX();
            mouseY = node.getLayoutBounds().getMaxY();
        }

        double width = node.getLayoutBounds().getWidth();
        double height = node.getLayoutBounds().getHeight();

        double deltaX, deltaY;

        if (mouseX + width > Sunconfig.DIAL_WIDTH) {
            deltaX = mouseX - (width + Sunconfig.HELP_TEXT_OFFSET);
        } else {
            deltaX = mouseX + Sunconfig.HELP_TEXT_OFFSET;
        }
        if (mouseY + height > Sunconfig.DIAL_HEIGHT) {
            deltaY = mouseY - (height + Sunconfig.HELP_TEXT_OFFSET);
        } else {
            deltaY = mouseY + Sunconfig.HELP_TEXT_OFFSET;
        }

        if (deltaX < 0) { deltaX = 0; }
        if (deltaY < 0) { deltaY = 0; }
        if (deltaX > Sunconfig.DIAL_WIDTH - width) { deltaX = Sunconfig.DIAL_WIDTH - width; }
        if (deltaY > Sunconfig.DIAL_HEIGHT - height) { deltaY = Sunconfig.DIAL_HEIGHT - height; }

        node.setTranslateX(deltaX);
        node.setTranslateY(deltaY);
    }

    private double getCenterX(Node node) {
        return (node.getLocalToSceneTransform().getMxx() * node.getLayoutBounds().getWidth()) / 2d;
    }

    private double getCenterY(Node node) {
        return (node.getLocalToSceneTransform().getMyy() * node.getLayoutBounds().getHeight()) / 2d;
    }

    private Group createHelpMarker(double centerX, double centerY, Node node) {

        double sizeX = node.getLocalToSceneTransform().getMxx() * node.getLayoutBounds().getWidth() + Sunconfig.HELP_MARKER_MARGIN;
        double sizeY = node.getLocalToSceneTransform().getMyy() * node.getLayoutBounds().getHeight() + Sunconfig.HELP_MARKER_MARGIN;

        Rectangle rectangle = new Rectangle(sizeX, sizeY);
        rectangle.setArcWidth(Sunconfig.HELP_MARKER_ROUND);
        rectangle.setArcHeight(Sunconfig.HELP_MARKER_ROUND);
        rectangle.setFill(Sunconfig.Color_Of_Void);
        rectangle.setStroke(Color.WHITE);
        rectangle.setStyle(Sunconfig.HELP_MARKER_GLOW);
        rectangle.setTranslateX(-rectangle.getWidth() / 2);
        rectangle.setTranslateY(-rectangle.getHeight() / 2);

        Circle circle = new Circle(Sunconfig.HELP_MARKER_RADIUS);
        circle.setFill(Sunconfig.Color_Of_Void);
        circle.setStroke(Color.WHITE);
        circle.setStyle(Sunconfig.HELP_MARKER_GLOW);

        Group marker = new Group(rectangle, circle);
        marker.setMouseTransparent(true);

        marker.setTranslateX(centerX);
        marker.setTranslateY(centerY);

        marker.visibleProperty().bind(node.visibleProperty());

        return marker;
    }

    private Group createHelpMarkerGroup(double offsetX, double offsetY, Node node) {

        Group markerGroup = new Group(createHelpMarker(offsetX, offsetY, node));

        markerGroup.translateXProperty().bind(Bindings.createDoubleBinding(() -> {
            double t = node.localToSceneTransformProperty().get().getTx();
            return t;
        }, node.localToSceneTransformProperty(), node.layoutBoundsProperty()));

        markerGroup.translateYProperty().bind(Bindings.createDoubleBinding(() -> {
            double t = node.localToSceneTransformProperty().get().getTy();
            return t;
        }, node.localToSceneTransformProperty(), node.layoutBoundsProperty()));

        return markerGroup;
    }

    private Group createHelpMarkerGroup(double offsetX, double offsetY, Node node, Transform transform) {

        Group markerGroup = new Group(createHelpMarker(offsetX, offsetY, node));

        if (transform != null) {
            markerGroup.getTransforms().add(transform);
        }
        return markerGroup;
    }

    private double getAbsoluteAngle(GregorianCalendar calendar) {
        return getRemainder(getCleanAngle(calendar), 360d);
    }

    private double getCleanAngle(GregorianCalendar calendar) {

        if(calendar == null) { return 0; }

        double hour = (double) calendar.get(Calendar.HOUR_OF_DAY);
        double minute = (double) calendar.get(Calendar.MINUTE);
        double second = (double) calendar.get(Calendar.SECOND);

        double angle = (hour / 24d + minute / (24d * 60d) + second / (24d * 60d * 60d)) * 360d + 180d;

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

        this.nightCompression += direction * Sunconfig.STEP_nightCompression;

        if (this.nightCompression < Sunconfig.MIN_nightCompression) {
            this.nightCompression = Sunconfig.MIN_nightCompression;
            return;
        }
        if (this.nightCompression > Sunconfig.MAX_nightCompression) {
            this.nightCompression = Sunconfig.MAX_nightCompression;
            return;
        }

        updateRotations();
    }

    private void updateRotations() {

        setSunTimeDialAngle(getAbsoluteAngle(this.sunTime));
        setHighNoonDialAngle(getAbsoluteAngle(this.highNoon));
        setHorizonDialAngle(getAbsoluteAngle(this.sunrise), getAbsoluteAngle(this.sunset));
        setDialAngleLocalHour(getAbsoluteAngle(this.localTime));

        dialRotateLocalMinute.setAngle(this.localTime.get(Calendar.MINUTE) * 6);

        updateDialMarkers();
    }

    public static String getShortTimeString(GregorianCalendar calendar) {

        String hourString = ("00" + calendar.get(Calendar.HOUR_OF_DAY));
        hourString = hourString.substring(hourString.length() - 2);

        String minuteString = ("00" + calendar.get(Calendar.MINUTE));
        minuteString = minuteString.substring(minuteString.length() - 2);

        String secondString = ("00" + calendar.get(Calendar.SECOND));
        secondString = secondString.substring(secondString.length() - 2);

        return hourString + ":" + minuteString + ":" + secondString;
    }

    public static String getShorterTimeString(GregorianCalendar calendar) {

        int minutes = calendar.get(Calendar.MINUTE) + round(calendar.get(Calendar.SECOND) / 60f);

        String hourString = ("00" + calendar.get(Calendar.HOUR_OF_DAY));
        hourString = hourString.substring(hourString.length() - 2);

        String minuteString = ("00" + minutes);
        minuteString = minuteString.substring(minuteString.length() - 2);

        return hourString + ":" + minuteString;
    }

    public static String getShortTimeLengthString(double inputSeconds) {

        double precisionHours = inputSeconds / (60 * 60);
        int hours = (int) floor(precisionHours);

        double precisionMins = (precisionHours - hours) * 60;
        int minutes = (int) floor(precisionMins);

        double precisionSecs = (precisionMins - minutes) * 60;
        int seconds = (int) floor(precisionSecs);

        String hoursString = ("00" + hours);
        hoursString = hoursString.substring(hoursString.length() - 2);

        String minutesString = ("00" + minutes);
        minutesString = minutesString.substring(minutesString.length() - 2);

        String secondsString = ("00" + seconds);
        secondsString = secondsString.substring(secondsString.length() - 2);

        return hoursString + "h" + minutesString + "m" + secondsString + "s";
    }

    public static String getShorterTimeLengthString(double inputSeconds) {

        double precisionHours = inputSeconds / (60 * 60);
        int hours = (int) floor(precisionHours);

        double precisionMins = (precisionHours - hours) * 60;
        int minutes = (int) round(precisionMins);

        String hoursString = ("00" + hours);
        hoursString = hoursString.substring(hoursString.length() - 2);

        String minutesString = ("00" + minutes);
        minutesString = minutesString.substring(minutesString.length() - 2);

        return hoursString + "h" + minutesString + "m";
    }

    public static String formatCoordinateToString(double coordinate, String suffixPositive, String suffixNegative) {

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


    // Getters
    public Group getDialsGroup() {
        return dialsGroup;
    }

    public Circle getControlNightCompression() {
        return controlNightCompression;
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

    public Globe getDayGlobe() {
        return dayGlobe;
    }

    public Globe getNightGlobe() {
        return nightGlobe;
    }

    public Grid getDayGrid() {
        return dayGrid;
    }

    public Ring getDayTerminatorLine() {
        return dayTerminatorLine;
    }

    public Ring getDayTerminatorGlow() {
        return dayTerminatorGlow;
    }

    public Globe getTinyGlobe() {
        return tinyGlobe;
    }

    public Ring getTinyDayTerminatorLine() {
        return tinyDayTerminatorLine;
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

    public Group getControlThingyNightmode() {
        return controlThingyNightmode;
    }

    public Group getControlThingyAlwaysOnTop() {
        return controlThingyAlwaysOnTop;
    }

    public Group getHorizonGroup() {
        return horizonGroup;
    }

    public Group getLongitudeGroup() {
        return longitudeGroup;
    }

    public Group getLatitudeGroup() {
        return latitudeGroup;
    }

    public Group getBackgroundGroup() {
        return backgroundGroup;
    }

    public Group getTinyGlobeGroup() {
        return tinyGlobeGroup;
    }

    public Group getDialHighNoonGroup() {
        return dialHighNoonGroup;
    }

    public Group getMatrixTimeZone() {
        return matrixTimeZone;
    }

    public Group getControlThingyHelp() {
        return controlThingyHelp;
    }

    public Group getHelpTextGroup() {
        return helpTextGroup;
    }

    public Group getInfoTextGroup() {
        return infoTextGroup;
    }

    public Text getInfoText() {
        return infoText;
    }

    // Setters
    public void updateCetusTimer(Cetustime cetustime) {

        ArrayList<ArrayList<GregorianCalendar>> nightList = cetustime.getNightList(localTime);

        long offsetTime = 0;

        int i = 0;
        while (offsetTime <= 0 && (i / 2) < nightList.size()) {
            int nightIndex = i / 2;
            int nightStep = i % 2;
            offsetTime = nightList.get(nightIndex).get(nightStep).getTimeInMillis() - localTime.getTimeInMillis();
            i++;
        }

        cetusTimer.setString(getShortTimeLengthString(offsetTime / 1000d).substring(1));

        if (i % 2 == 0) {
            cetusTimer.setFill(Sunconfig.Color_Of_CetusNight);
            cetusTimer.setStyle(Sunconfig.CETUS_MATRIX_SHADOW_NIGHT);
        } else {
            cetusTimer.setFill(Sunconfig.Color_Of_CetusDay);
            cetusTimer.setStyle(Sunconfig.CETUS_MATRIX_SHADOW_DAY);
        }
    }

    public void setSunTime(GregorianCalendar sunTime) {
        this.sunTime = sunTime;
        setSunTimeDialAngle(getAbsoluteAngle(this.sunTime));
    }

    public void setHighNoon(GregorianCalendar highNoon) {
        this.highNoon = highNoon;
        setHighNoonDialAngle(getAbsoluteAngle(this.highNoon));
        matrixHighNoon.setString(getShortTimeString(highNoon));
    }

    public void setHorizon(GregorianCalendar sunrise, GregorianCalendar sunset) {

        this.sunrise = sunrise;
        this.sunset = sunset;

        this.daylength = (this.sunset.getTimeInMillis() - this.sunrise.getTimeInMillis()) / 1000;

        long daySeconds = 24 * 60 * 60;
        long clampedDayLength = this.daylength >= daySeconds ? daySeconds : this.daylength;

        setHorizonDialAngle(getAbsoluteAngle(this.sunrise), getAbsoluteAngle(this.sunset));

        matrixSunrise.setString(getShorterTimeString(this.sunrise));
        matrixSunset.setString(getShorterTimeString(this.sunset));
        matrixDayLength.setString(getShortTimeLengthString(clampedDayLength));
    }

    public void setLocalTime(GregorianCalendar localTime) {

        this.localTime = localTime;

        setDialAngleLocalHour(getAbsoluteAngle(this.localTime));

        dialRotateLocalMinute.setAngle(this.localTime.get(Calendar.MINUTE) * 6);

        updateLEDs(dialLocalSecondLedList, dialLocalSecondOn, dialLocalSecondLedTransitionList, localTime.get(Calendar.SECOND));
        updateLEDs(dialLocalMinuteLedList, dialLocalMinuteOn, dialLocalMinuteLedTransitionList, localTime.get(Calendar.MINUTE));

    }

    private void updateLEDs(ArrayList<Node> ledList, ArrayList<Boolean> ledOn, ArrayList<Timeline> timelineList, int indexOn) {

        for (int i = 0; i < ledList.size(); i++) {

            if (ledOn.get(i)) {

                if(i == indexOn) { continue; }

                if (ledAnimationOnEh) {
                    timelineList.get(i).play();
                } else {
                    ledList.get(i).setOpacity(0);
                }

                ledOn.set(i, false);
            }
        }

        timelineList.get(indexOn).stop();
        ledList.get(indexOn).setOpacity(1.0);
        ledOn.set(indexOn, true);
    }

    public void setCetusTime(ArrayList<ArrayList<GregorianCalendar>> nightList, GregorianCalendar calendar) {

        if (nightList == null || nightList.isEmpty()) { return; }

        int cetusMarkerAngleListSize = cetusMarkerAngleList.size();
        int nightListSize = nightList.size();

        GregorianCalendar localTimeUtc = (GregorianCalendar) calendar.clone();
        localTimeUtc.get(Calendar.HOUR_OF_DAY);
        localTimeUtc.setTimeZone(TimeZone.getTimeZone("UTC"));
        localTimeUtc.get(Calendar.HOUR_OF_DAY);

        int currentDay = localTimeUtc.get(Calendar.DAY_OF_YEAR);

        GregorianCalendar currentDayStart = new GregorianCalendar();
        currentDayStart.set(
                localTimeUtc.get(Calendar.YEAR),
                localTimeUtc.get(Calendar.MONTH),
                localTimeUtc.get(Calendar.DAY_OF_MONTH),
                0,0,0
        );

        GregorianCalendar currentDayEnd = (GregorianCalendar) currentDayStart.clone();
        currentDayEnd.set(Calendar.DAY_OF_YEAR, localTimeUtc.get(Calendar.DAY_OF_YEAR) + 1);

        for (int i = 0; i < nightListSize; i++) {

            if ((i * 2) + 1 > cetusMarkerAngleListSize) { continue; }

            GregorianCalendar startTime = nightList.get(i).get(0);
            GregorianCalendar endTime = nightList.get(i).get(1);

            int startTimeDay = startTime.get(Calendar.DAY_OF_YEAR);
            int endTimeDay = endTime.get(Calendar.DAY_OF_YEAR);

            if (startTimeDay != currentDay) {
                startTime = currentDayStart;
                if (endTimeDay != currentDay) {
                    endTime = currentDayStart;
                }
            }

            if (endTimeDay != currentDay) {
                endTime = currentDayEnd;
                if (startTimeDay != currentDay) {
                    startTime = currentDayEnd;
                }
            }

            double startAngle = getAbsoluteAngle(startTime);
            double endAngle = getAbsoluteAngle(endTime);

            cetusMarkerAngleList.set((i * 2), startAngle);
            cetusMarkerAngleList.set((i * 2) + 1, endAngle);

            DotMatrix matrixStart = cetusTimeMatrixList.get(i * 2);
            DotMatrix matrixEnd = cetusTimeMatrixList.get((i * 2) + 1);

            matrixStart.setString(getShorterTimeString(startTime));
            matrixEnd.setString(getShorterTimeString(endTime));

            if (startAngle > 0 && startAngle <= 180) { matrixStart.setRotate(270); }
            else { matrixStart.setRotate(90); }

            if (endAngle > 0 && endAngle <= 180) { matrixEnd.setRotate(270); }
            else { matrixEnd.setRotate(90); }
        }

        updateDialMarkers();
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
            dialArcDayLength.setLength(360);
//            dialArcDayLength.setVisible(false);
        } else if (this.daylength <= 0) {
            sunriseDial.setVisible(false);
            sunsetDial.setVisible(false);
            matrixSunrise.setVisible(false);
            matrixSunset.setVisible(false);
            dialArcNight.setVisible(true);
            dialArcDayLength.setLength(0);
//            dialArcDayLength.setVisible(false);
        } else {
            sunriseDial.setVisible(true);
            sunsetDial.setVisible(true);
            matrixSunrise.setVisible(true);
            matrixSunset.setVisible(true);
            dialArcNight.setVisible(true);
//            dialArcDayLength.setVisible(true);

            double dayLengthDeg = (720 + this.sunsetDialAngle - this.sunriseDialAngle) % 360;

            dialArcNight.setStartAngle(90 - this.sunsetDialAngle);
            dialArcNight.setLength(dayLengthDeg - 360);

            dialArcDayLength.setStartAngle(90 - this.sunriseDialAngle - Sunconfig.DAY_ARC_MARGIN);
            dialArcDayLength.setLength(-1 * (dayLengthDeg - Sunconfig.DAY_ARC_MARGIN * 2));

        }

        sunriseDialRotate.setAngle(this.sunriseDialAngle);
        sunsetDialRotate.setAngle(this.sunsetDialAngle);

        if (this.sunriseDialAngle >= 0 && this.sunriseDialAngle < 180) {
            matrixSunrise.setTranslateX(Sunconfig.CENTER_X - matrixSunrise.getLayoutBounds().getWidth() / 2 - matrixSunrise.getLayoutBounds().getHeight() / 2 - Sunconfig.MATRIX_HORIZON_SLIDE);
            matrixSunrise.setRotate(-90d);
        } else {
            matrixSunrise.setTranslateX(Sunconfig.CENTER_X - matrixSunrise.getLayoutBounds().getWidth() / 2 + matrixSunrise.getLayoutBounds().getHeight() / 2 + Sunconfig.MATRIX_HORIZON_SLIDE);
            matrixSunrise.setRotate(90d);
        }

        if (this.sunsetDialAngle >= 0 && this.sunsetDialAngle < 180) {
            matrixSunset.setTranslateX(Sunconfig.CENTER_X - matrixSunset.getLayoutBounds().getWidth() / 2 - matrixSunset.getLayoutBounds().getHeight() / 2 - Sunconfig.MATRIX_HORIZON_SLIDE);
            matrixSunset.setRotate(-90d);
        } else {
            matrixSunset.setTranslateX(Sunconfig.CENTER_X - matrixSunset.getLayoutBounds().getWidth() / 2 + matrixSunset.getLayoutBounds().getHeight() / 2 + Sunconfig.MATRIX_HORIZON_SLIDE);
            matrixSunset.setRotate(90d);
        }

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
                int hourIndex = i / 4;
                double angle = dialMarkerRotateList.get(i).getAngle();
                hourMarkerMatrixList.get(hourIndex).setRotate(-1 * angle);
                hourMarkerMatrixList.get(hourIndex).setStyle(Sunconfig.MATRIX_SHADOW);
            }
        }


        int localHour = localTime.get(Calendar.HOUR_OF_DAY);
        int localMinute = localTime.get(Calendar.MINUTE);

        int hourIndexStart = (localHour + 12) % 24;
        int hourIndexEnd = (localHour + 12 + 1) % 24;

        float partialStart = (float) sqrt(1 - localMinute / 60f);       // sqrt progression (non-linear)
        float partialEnd = (float) sqrt(localMinute / 60f);             // sqrt progression (non-linear)

        int componentR = (int) floor(partialStart * (255 - 32) + 32);   // 255 -> 32
        int componentG = 128;
        int componentB = (int) floor(partialEnd * (255 - 32) + 32);     // 32 -> 255

        float glowRadiusStart = partialStart * 2.50f + 5.00f;           // 7.50 -> 5.00
        float glowStrengthStart = partialStart * 0.25f + 0.60f;           // 0.85 -> 0.60

        float glowRadiusEnd = partialEnd * 2.50f + 5.00f;             // 5.00 -> 7.50
        float glowStrengthEnd = partialEnd * 0.25f + 0.60f;             // 0.60 -> 0.85

        StringBuilder hourStyleStart = new StringBuilder()
                .append("-fx-effect: dropshadow(three-pass-box, rgba(")
                .append(componentR).append(",")
                .append(componentG).append(",")
                .append(componentB).append(",")
                .append("1.0)").append(",")
                .append(glowRadiusStart).append(",")
                .append(glowStrengthStart).append(",")
                .append("0, 0);");

        StringBuilder hourStyleEnd = new StringBuilder()
                .append("-fx-effect: dropshadow(three-pass-box, rgba(")
                .append(componentB).append(",")
                .append(componentG).append(",")
                .append(componentR).append(",")
                .append("1.0)").append(",")
                .append(glowRadiusEnd).append(",")
                .append(glowStrengthEnd).append(",")
                .append("0, 0);");

        hourMarkerMatrixList.get(hourIndexStart).setStyle(hourStyleStart.toString());
        hourMarkerMatrixList.get(hourIndexEnd).setStyle(hourStyleEnd.toString());


        int cetusMarkerRotateListSize = cetusMarkerRotateList.size();
        for (int i = 0; i < cetusMarkerRotateListSize; i++) {
            cetusMarkerRotateList.get(i).setAngle(getNightCompressionAngle(cetusMarkerAngleList.get(i)));
        }

        int cetusMarkerArcListSize = cetusMarkerArcList.size();
        for  (int i = 0; i < cetusMarkerArcListSize; i++) {

            double startAngle = cetusMarkerAngleList.get(i * 2);
            double endAngle = cetusMarkerAngleList.get(i * 2 + 1);

            double adjustedStartAngle = getNightCompressionAngle(startAngle);
            double adjustedEndAngle = getNightCompressionAngle(endAngle);

            double length = adjustedStartAngle - adjustedEndAngle;
            if (length > 0) { length = -1 * ((360 - adjustedStartAngle) + adjustedEndAngle); }

            cetusMarkerArcList.get(i).setStartAngle(90 - adjustedStartAngle);
            cetusMarkerArcList.get(i).setLength(length);
        }
    }

    public void setDialFrameWarning(boolean warning) {

        this.warning = warning;

        if (this.warning) {
            if (globeVisibleEh) {
                dialCircleFrame.setFill(Sunconfig.Color_Of_Void);
            } else {
                dialCircleFrame.setFill(Sunconfig.FRAME_DIAL_WARNING);
            }
        } else {
            if (globeVisibleEh) {
                dialCircleFrame.setFill(Sunconfig.Color_Of_Void);
            } else {
                dialCircleFrame.setFill(Sunconfig.FRAME_DIAL_NOMINAL);
            }
        }
    }

    public void setGroupGlow(Group group, String style) {
        if (group == null || style == null) { return; }
        group.setStyle(style);
    }

    public void setGlobeVisibility(boolean isVisible) {

        int tinyGlobeDuration = globeAnimationOnEh ? Sunconfig.TINY_GLOBE_DURATION : 1;

        if (isVisible) {

            controlNightCompression.setFill(Sunconfig.Color_Of_Void);
            controlNightCompression.setStroke(Sunconfig.Color_Of_LocalTime);

            dialArcNight.setOpacity(0);
            dialArcMidnight.setVisible(false);

            dialCircleBackground.setVisible(false);
            globeMasterGroup.setVisible(true);

            matrixLongitude.setVisible(true);
            matrixLatitude.setVisible(true);

            matrixTimeZone.setVisible(true);

            double tinyGlobeSlideX = -1 * Sunconfig.CENTER_X + Sunconfig.TINYGLOBE_RADIUS + Sunconfig.TINYGLOBE_SLIDE;
            double tinyGlobeSlideY = Sunconfig.DIAL_HEIGHT - Sunconfig.CENTER_Y - Sunconfig.TINYGLOBE_OFFSET - Sunconfig.TINYGLOBE_RADIUS - Sunconfig.TINYGLOBE_SLIDE;

            Timeline timeline = new Timeline();
            timeline.setCycleCount(1);
            timeline.setRate(1);
            timeline.setAutoReverse(false);

            KeyValue keyValueScaleX = new KeyValue(tinyGlobeScale.xProperty(), Sunconfig.TINYGLOBE_DOWNSCALE, Interpolator.EASE_BOTH);
            KeyFrame keyFrameScaleX = new KeyFrame(Duration.millis(tinyGlobeDuration), keyValueScaleX);
            KeyValue keyValueScaleY = new KeyValue(tinyGlobeScale.yProperty(), Sunconfig.TINYGLOBE_DOWNSCALE, Interpolator.EASE_BOTH);
            KeyFrame keyFrameScaleY = new KeyFrame(Duration.millis(tinyGlobeDuration), keyValueScaleY);

            KeyValue keyValueSlideX = new KeyValue(tinyGlobeGroup.translateXProperty(), tinyGlobeSlideX, Interpolator.EASE_BOTH);
            KeyFrame keyFrameSlideX = new KeyFrame(Duration.millis(tinyGlobeDuration), keyValueSlideX);
            KeyValue keyValueSlideY = new KeyValue(tinyGlobeGroup.translateYProperty(), tinyGlobeSlideY, Interpolator.EASE_OUT);
            KeyFrame keyFrameSlideY = new KeyFrame(Duration.millis(tinyGlobeDuration), keyValueSlideY);

            KeyValue keyValueOpacity = new KeyValue(tinyGlobeGroup.opacityProperty(), Sunconfig.TINYGLOBE_OFFSET_OPACITY, Interpolator.EASE_BOTH);
            KeyFrame keyFrameOpacity = new KeyFrame(Duration.millis(tinyGlobeDuration), keyValueOpacity);

            timeline.getKeyFrames().addAll(keyFrameScaleX, keyFrameScaleY, keyFrameSlideX, keyFrameSlideY, keyFrameOpacity);
            timeline.play();

        }
        else {

            controlNightCompression.setFill(Sunconfig.Color_Of_LocalTime);
            controlNightCompression.setStroke(Sunconfig.Color_Of_Void);

            dialArcNight.setOpacity(1);
            dialArcMidnight.setVisible(true);

            dialCircleBackground.setVisible(true);
            globeMasterGroup.setVisible(false);

            matrixLongitude.setVisible(false);
            matrixLatitude.setVisible(false);

            matrixTimeZone.setVisible(false);

            double tinyGlobeSlideX = 0;
            double tinyGlobeSlideY = 0;

            Timeline timeline = new Timeline();
            timeline.setCycleCount(1);
            timeline.setRate(1);
            timeline.setAutoReverse(false);

            KeyValue keyValueScaleX = new KeyValue(tinyGlobeScale.xProperty(), 1, Interpolator.EASE_BOTH);
            KeyFrame keyFrameScaleX = new KeyFrame(Duration.millis(tinyGlobeDuration), keyValueScaleX);
            KeyValue keyValueScaleY = new KeyValue(tinyGlobeScale.yProperty(), 1, Interpolator.EASE_BOTH);
            KeyFrame keyFrameScaleY = new KeyFrame(Duration.millis(tinyGlobeDuration), keyValueScaleY);

            KeyValue keyValueSlideX = new KeyValue(tinyGlobeGroup.translateXProperty(), tinyGlobeSlideX, Interpolator.EASE_BOTH);
            KeyFrame keyFrameSlideX = new KeyFrame(Duration.millis(tinyGlobeDuration), keyValueSlideX);
            KeyValue keyValueSlideY = new KeyValue(tinyGlobeGroup.translateYProperty(), tinyGlobeSlideY, Interpolator.EASE_OUT);
            KeyFrame keyFrameSlideY = new KeyFrame(Duration.millis(tinyGlobeDuration), keyValueSlideY);

            KeyValue keyValueOpacity = new KeyValue(tinyGlobeGroup.opacityProperty(), Sunconfig.TINYGLOBE_DEFAULT_OPACITY, Interpolator.EASE_BOTH);
            KeyFrame keyFrameOpacity = new KeyFrame(Duration.millis(tinyGlobeDuration), keyValueOpacity);

            timeline.getKeyFrames().addAll(keyFrameScaleX, keyFrameScaleY, keyFrameSlideX, keyFrameSlideY, keyFrameOpacity);
            timeline.play();

        }

        setDialFrameWarning(warning);
    }

    public void setCoordinates(double longitude, double latitude) {
        matrixLongitude.setString(formatCoordinateToString(longitude, "E", "W"));
        matrixLatitude.setString(formatCoordinateToString(latitude, "N", "S"));
    }

    public void toggleGlobeVisibility() {
        globeVisibleEh = !globeVisibleEh;
        setGlobeVisibility(globeVisibleEh);
    }

    public void setGlobeDaylight(double phase, double tilt) {

        dayGlobe.setDayLightPosition(phase, tilt);
        nightGlobe.setDayLightPosition(phase, tilt);
        dayTerminatorLine.setDayLightPosition(phase, tilt);
        dayTerminatorGlow.setDayLightPosition(phase, tilt);

        tinyGlobe.setDayLightPosition(phase, tilt);
        tinyDayTerminatorLine.setDayLightPosition(phase, tilt);
    }

    public void rotateGlobe(double longitude, double latitude) {

        dayGlobe.rotateGlobe(longitude, latitude, false);
        nightGlobe.rotateGlobe(longitude,latitude, false);
        dayGrid.rotateRing(longitude, latitude, false);
        dayTerminatorLine.rotateRing(longitude, latitude, false);
        dayTerminatorGlow.rotateRing(longitude, latitude, false);

        tinyGlobe.rotateGlobe(longitude, latitude, false);
        tinyDayTerminatorLine.rotateRing(longitude, latitude, false);
    }

    public void rotateGlobeAnimated(double longitude, double latitude) {
        dayGlobe.rotateGlobe(longitude, latitude, globeAnimationOnEh);
        nightGlobe.rotateGlobe(longitude, latitude, globeAnimationOnEh);
        dayGrid.rotateRing(longitude, latitude, globeAnimationOnEh);
        dayTerminatorLine.rotateRing(longitude, latitude, globeAnimationOnEh);
        dayTerminatorGlow.rotateRing(longitude, latitude, globeAnimationOnEh);

        tinyGlobe.rotateGlobe(longitude, latitude, globeAnimationOnEh);
        tinyDayTerminatorLine.rotateRing(longitude, latitude, globeAnimationOnEh);
    }

    public void toggleCetusTime() {
        cetusTimeVisibleEh = !cetusTimeVisibleEh;
        setCetusTimeVisibility(cetusTimeVisibleEh);
    }

    public boolean cetusTimeVisibleEh() {
        return cetusTimeVisibleEh;
    }

    public void setCetusTimeVisibility(boolean visibleEh) {

        cetusTimeVisibleEh = visibleEh;

        cetusArcGroup.setVisible(visibleEh);
        cetusLineGroup.setVisible(visibleEh);
        cetusTimer.setVisible(visibleEh);

        if (visibleEh) {
            tinyGlobeFrame.setStroke(Sunconfig.Color_Of_CetusFrame);
        } else {
            tinyGlobeFrame.setStroke(Sunconfig.Color_Of_TinyFrame);
        }
    }

    private Timeline createTimelineForLED(Node node, int duration) {

        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        timeline.setRate(1);
        timeline.setAutoReverse(false);
        KeyValue keyValue = new KeyValue(node.opacityProperty(), 0.0, Interpolator.EASE_IN);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(Sunconfig.LED_OPACITY_DURATION), keyValue);
        timeline.getKeyFrames().add(keyFrame);

        return timeline;
    }

    public void setTimeDisplayOpacity(double opacity) {

        matrixTime.setOpacity(opacity);
        matrixDate.setOpacity(opacity);
        matrixTimeZone.setOpacity(opacity);
        dialLocalHourGroup.setOpacity(opacity);
        dialLocalMinuteGroup.setOpacity(opacity);

        if (opacity < 0.5) {
            matrixTime.setMouseTransparent(true);
            matrixDate.setMouseTransparent(true);
            matrixTimeZone.setMouseTransparent(true);
            for (Node dialSecond : dialLocalSecondLedList) { dialSecond.setVisible(false); }
            for (Node dialMinute : dialLocalMinuteLedList) { dialMinute.setVisible(false); }
        } else {
            matrixTime.setMouseTransparent(false);
            matrixDate.setMouseTransparent(false);
            matrixTimeZone.setMouseTransparent(false);
            for (Node dialSecond : dialLocalSecondLedList) { dialSecond.setVisible(true); }
            for (Node dialMinute : dialLocalMinuteLedList) { dialMinute.setVisible(true); }
        }
    }

    public void setTimeZone(TimeZone timeZone) {

        long timeZoneOffset = timeZone.getOffset(localTime.getTimeInMillis());

        String timeZoneNumberString = "00" + abs(timeZoneOffset / (1000 * 60 * 60));
        timeZoneNumberString = timeZoneNumberString.substring(timeZoneNumberString.length() - 2);

        StringBuilder timeZoneString = new StringBuilder()
                .append("GMT")
                .append((timeZoneOffset < 0) ? "-" : "+")
                .append(timeZoneNumberString)
                ;

        matrixTimeZone.setString(timeZoneString.toString());
    }

    public void toggleAnimation() {

        ledAnimationOnEh = !ledAnimationOnEh;
        globeAnimationOnEh = !globeAnimationOnEh;

        for (Timeline timeline : cetusMarkerHoverTransitionList) {
            if (ledAnimationOnEh) {
                timeline.setRate(1);
            } else {
                timeline.setRate(Sunconfig.CETUS_MARKER_DURATION);
            }
        }

    }

    public void toggleHelp() {

        helpEh = !helpEh;

        helpOverlay.setVisible(helpEh);
        helpTextGroup.setVisible(helpEh);

        if (helpEh) {
            controlThingyHelpCircle.setStroke(Color.WHITE);
        } else {
            controlThingyHelpCircle.setStroke(Sunconfig.Color_Of_ResizeStroke);
        }
    }

    public void toggleNightmode() {
        nightmodeEh = !nightmodeEh;
        nightModeOverlay.setVisible(nightmodeEh);
    }

    public void setAlwaysOnTop(boolean alwaysOnTopEh) {
        if (alwaysOnTopEh) {
            thingyAlwaysOnTopCircle.setStroke(Color.WHITE);
        } else {
            thingyAlwaysOnTopCircle.setStroke(Sunconfig.Color_Of_AlwaysOnTopStroke);
        }
    }
}
