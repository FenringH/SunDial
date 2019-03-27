import javafx.animation.*;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.Event;
import javafx.scene.*;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
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
    private double nightCompression;
    private boolean warning;

    private GregorianCalendar sunTime;
    private GregorianCalendar highNoon;
    private GregorianCalendar sunrise;
    private GregorianCalendar sunset;
    private GregorianCalendar localTime;
    private long daylength;                 // length of day in seconds

    private DoubleProperty longitude;
    private DoubleProperty latitude;
    private DoubleProperty tilt;
    private DoubleProperty phase;


    // graphical primitives
    private Group dialsGroup;

    private Rotate centerRotate;
    private Rotate sunTimeDialRotate;
    private Rotate highNoonDialRotate;
    private Rotate sunriseDialRotate;
    private Rotate sunsetDialRotate;
    private Rotate dialRotateLocalHour;
    private Rotate dialRotateLocalMinute;
    private Rotate dialRotateLocalSecond;
    private ArrayList<Rotate> dialMarkerRotateList;
    private ArrayList<Rotate> cetusMarkerRotateList;
    private ArrayList<Double> cetusMarkerAngleList;

    private Arc dialArcNight;
    private Arc dialArcMidnight;
    private Arc dialArcDayLength;

    private Circle dialMarginCircle;
    private Circle dialCircleBackground;
    private Circle dialCircleFrame;
    private Circle controlNightCompression;
    private Circle dialCircleCenterPoint;
    private Group dialLocalSecondGroup;
    private Group dialLocalMinuteGroup;
    private Group dialLocalHourGroup;
    private Group dialHighNoonGroup;
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

    private ControlThingy controlThingyHelp;
    private ControlThingy controlThingyResize;
    private ControlThingy controlThingyClose;
    private ControlThingy controlThingyMaximize;
    private ControlThingy controlThingyMinimize;
    private ControlThingy controlThingyNightmode;
    private ControlThingy controlThingyAlwaysOnTop;
    private ControlThingy controlThingyGlobeGrid;

    private Circle tinyGlobeFrame;
    private Group tinyGlobeGroup;
    private Group globeMasterGroup;
    private Scale tinyGlobeScale;
    private Group horizonGroup;
    private Group cetusMarkerGroup;
    private Group matrixTime;
    private Group matrixDate;
    private Group helpOverlay;
    private ArrayList<Group> helpMarkers;
    private ArrayList<Line> dialHourLineMarkerList;
    private Group nightModeOverlay;
    private Group dialMinuteMarkers;
    private Group dialHourMatrixMarkerGroup;

    private Timeline tinyGlobeMoveOutTimeline;
    private Timeline tinyGlobeMoveInTimeline;
    private Timeline longitudeTimeline;
    private Timeline latitudeTimeline;

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

    public BooleanProperty globeGridVisibleEh;


    // Constructor
    public Sundial(PleaseBuildSundial builder) {
        this.sunTimeDialAngle = builder.sunTimeDialAngle;
        this.highNoonDialAngle = builder.highNoonDialAngle;
        this.sunriseDialAngle = builder.sunriseDialAngle;
        this.sunsetDialAngle = builder.sunsetDialAngle;
        this.dialAngleLocalHour = builder.localTimeDialAngle;
        this.nightCompression = builder.nightCompression;
        this.sunTime = new GregorianCalendar();
        this.highNoon = new GregorianCalendar();
        this.sunrise = new GregorianCalendar();
        this.sunset = new GregorianCalendar();
        this.localTime = new GregorianCalendar();
        this.init();
    }

    // Builder
    public static class PleaseBuildSundial {
        private double sunTimeDialAngle;
        private double highNoonDialAngle;
        private double sunriseDialAngle;
        private double sunsetDialAngle;
        private double localTimeDialAngle;
        private double nightCompression;

        public PleaseBuildSundial() {
            this.sunTimeDialAngle = Sunconfig.DEFAULT_sunTimeDialAngle;
            this.highNoonDialAngle = Sunconfig.DEFAULT_highNoonDialAngle;
            this.sunriseDialAngle = Sunconfig.DEFAULT_sunriseDialAngle;
            this.sunTimeDialAngle = Sunconfig.DEFAULT_sunsetDialAngle;
            this.localTimeDialAngle = Sunconfig.DEFAULT_localTimeDialAngle;
            this.nightCompression = Sunconfig.DEFAULT_nightCompression;
        }

        public PleaseBuildSundial sunTimeDialAngle(double sunTimeDialAngle) {
            this.sunTimeDialAngle = sunTimeDialAngle;
            return this;
        }

        public PleaseBuildSundial highNoonDialAngle(double highNoonDialAngle) {
            this.highNoonDialAngle = highNoonDialAngle;
            return this;
        }

        public PleaseBuildSundial sunriseDialAngle(double sunriseDialAngle) {
            this.sunriseDialAngle = sunriseDialAngle;
            return this;
        }

        public PleaseBuildSundial sunsetDialAngle(double sunsetDialAngle) {
            this.sunsetDialAngle = sunsetDialAngle;
            return this;
        }

        public PleaseBuildSundial localTimeDialAngle(double localTimeDialAngle) {
            this.localTimeDialAngle = localTimeDialAngle;
            return this;
        }

        public PleaseBuildSundial nightCompression(double nightCompression) {
            this.nightCompression = nightCompression;
            return this;
        }

        public Sundial thankYou() {
            return new Sundial(this);
        }
    }

    // stuff
    private void init() {

        // variables
        dialsGroup = new Group();
        helpText = new Text();

        globeGridVisibleEh = new SimpleBooleanProperty(false);
        globeGridVisibleEh.addListener((observable, oldValue, newValue) -> {});

        longitude = new SimpleDoubleProperty(0f);
        latitude = new SimpleDoubleProperty(0f);
        tilt = new SimpleDoubleProperty(0f);
        phase = new SimpleDoubleProperty(0f);

        longitude.addListener((observable, oldValue, newValue) -> {});
        latitude.addListener((observable, oldValue, newValue) -> {});
        tilt.addListener((observable, oldValue, newValue) -> {});
        phase.addListener((observable, oldValue, newValue) -> {});

        longitudeTimeline = new Timeline();
        latitudeTimeline = new Timeline();

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

        // Master globe
        globeMasterGroup = Suncreator.createMasterGlobe(longitude, latitude, phase, tilt, globeGridVisibleEh);
        globeMasterGroup.setVisible(false);

        // Tiny globe
        tinyGlobeGroup = Suncreator.createTinyGlobe(longitude, latitude, phase, tilt);
        tinyGlobeFrame = Suncreator.createTinyGlobeFrame();

        tinyGlobeGroup.getChildren().add(tinyGlobeFrame);
        tinyGlobeGroup.setOpacity(Sunconfig.TINYGLOBE_DEFAULT_OPACITY);

        tinyGlobeScale = new Scale();
        tinyGlobeScale.setPivotX(Sunconfig.CENTER_X);
        tinyGlobeScale.setPivotY(Sunconfig.CENTER_Y + Sunconfig.TINYGLOBE_OFFSET);
        tinyGlobeGroup.getTransforms().add(tinyGlobeScale);

        tinyGlobeMoveInTimeline = Suncreator.createTinyGlobeTimeline(Suncreator.TimelineDirection.IN, tinyGlobeGroup, tinyGlobeScale);
        tinyGlobeMoveOutTimeline = Suncreator.createTinyGlobeTimeline(Suncreator.TimelineDirection.OUT, tinyGlobeGroup, tinyGlobeScale);

        // Cetus stuff
        cetusMarkerRotateList = new ArrayList<>();
        cetusMarkerArcList = new ArrayList<>();
        cetusMarkerAngleList = new ArrayList<>();
        cetusTimeMatrixList = new ArrayList<>();
        cetusMarkerHoverTransitionList = new ArrayList<>();

        cetusMarkerGroup = Suncreator.createCetusMarkerGroup(
                centerRotate,
                cetusMarkerAngleList,
                cetusMarkerRotateList,
                cetusMarkerArcList,
                cetusTimeMatrixList,
                cetusMarkerHoverTransitionList
        );

        cetusTimer = Suncreator.createCetusTimer();

        setCetusTimeVisibility(cetusTimeVisibleEh);

        // Hour markers
        dialHourMatrixMarkerGroup = new Group();
        dialHourMatrixMarkerGroup.setMouseTransparent(true);

        dialHourLineMarkerList = new ArrayList<>();
        dialMarkerRotateList = new ArrayList<>();
        hourMarkerMatrixList = new ArrayList<>();

        Suncreator.createDialHourMarkers(
                nightCompression,
                centerRotate,
                dialHourMatrixMarkerGroup,
                hourMarkerMatrixList,
                dialHourLineMarkerList,
                dialMarkerRotateList
        );

        // Dials
        dialHighNoonGroup = Suncreator.creatDialHighNoonGroup(highNoonDialRotate);
        dialLocalHourGroup = Suncreator.createDialLocalHourGroup(dialRotateLocalHour);
        dialLocalMinuteGroup = Suncreator.createDialLocalMinuteGroup(dialRotateLocalMinute);
        dialLocalSecondGroup = Suncreator.createDialLocalSecondGroup(dialRotateLocalSecond);

        matrixSunrise = Suncreator.createMatrixSunrise();
        matrixSunset = Suncreator.createMatrixSunset();

        horizonGroup = new Group(
                Suncreator.createSunriseGroup(sunriseDialRotate, matrixSunrise),
                Suncreator.createSunsetGroup(sunsetDialRotate, matrixSunset)
        );
        horizonGroup.setMouseTransparent(true);

        // Time and Date
        matrixDay = new DotMatrix("00", Sunconfig.Color_Of_LocalTime);
        matrixMonth = new DotMatrix("00", Sunconfig.Color_Of_LocalTime);
        matrixYear = new DotMatrix("0000", Sunconfig.Color_Of_LocalTime);
        matrixWeek = new DotMatrix("00", Sunconfig.Color_Of_LocalTime);
        matrixDate = Suncreator.createMatrixDate(matrixDay, matrixMonth, matrixYear, matrixWeek);

        matrixHour = new DotMatrix("00", Sunconfig.Color_Of_LocalTime);
        matrixMinute = new DotMatrix("00", Sunconfig.Color_Of_LocalTime);
        matrixSecond = new DotMatrix("00", Sunconfig.Color_Of_LocalTime);
        matrixTime = Suncreator.createMatrixTime(matrixHour, matrixMinute, matrixSecond);

        // LEDs with minutes and seconds markers
        dialLocalSecondLedList = new ArrayList<>();
        dialLocalSecondOn = new ArrayList<>();
        dialLocalMinuteLedList = new ArrayList<>();
        dialLocalMinuteOn = new ArrayList<>();
        dialLocalSecondLedTransitionList = new ArrayList<>();
        dialLocalMinuteLedTransitionList = new ArrayList<>();

        Suncreator.createLEDs(
                dialLocalSecondLedList,
                dialLocalSecondOn,
                dialLocalMinuteLedList,
                dialLocalMinuteOn,
                dialLocalSecondLedTransitionList,
                dialLocalMinuteLedTransitionList
        );

        dialMinuteMarkers = Suncreator.createDialMinuteMarkers();

        // Other stuff
        dialMarginCircle = Suncreator.createDialMarginCircle();
        dialArcNight = Suncreator.createDialArcNight();
        dialArcMidnight = Suncreator.createDialArcMidnight();
        dialCircleBackground = Suncreator.createDialCircleBackground();
        dialCircleFrame = Suncreator.createDialCircleFrame();
        dialCircleCenterPoint = Suncreator.createDialCircleCenterPoint();
        controlNightCompression = Suncreator.createControlNightCompression();
        dialArcDayLength = Suncreator.createDialArcDayLength();
        matrixDayLength = Suncreator.createMatrixDayLength();
        matrixLongitude = Suncreator.createMatrixLongitude();
        matrixLatitude = Suncreator.createMatrixLatitude();
        matrixHighNoon = Suncreator.createMatrixHighNoon();
        matrixTimeZone = Suncreator.createMatrixTimeZone();
        nightModeOverlay = Suncreator.createNightModeOverlay();

        // Control thingies
        controlThingyHelp = Suncreator.createControlThingy(Suncreator.ControlThingyType.HELP, helpText);
        controlThingyResize = Suncreator.createControlThingy(Suncreator.ControlThingyType.RESIZE, helpText);
        controlThingyClose = Suncreator.createControlThingy(Suncreator.ControlThingyType.CLOSE, helpText);
        controlThingyMaximize = Suncreator.createControlThingy(Suncreator.ControlThingyType.MAXIMIZE, helpText);
        controlThingyMinimize = Suncreator.createControlThingy(Suncreator.ControlThingyType.MINIMIZE, helpText);
        controlThingyNightmode = Suncreator.createControlThingy(Suncreator.ControlThingyType.NIGTMODE, helpText);
        controlThingyAlwaysOnTop = Suncreator.createControlThingy(Suncreator.ControlThingyType.ALWAYSONTOP, helpText);
        controlThingyGlobeGrid = Suncreator.createControlThingy(Suncreator.ControlThingyType.GLOBEGRID, helpText);

        // Info overlay
        infoText = new Text();
        infoTextGroup = Suncreator.createInfoTextGroup(infoText);

        // Help overlay
        helpMarkers = new ArrayList<>();
        helpMarkers.add(Suncreator.createHelpMarkerGroup(Sunconfig.CENTER_X, Sunconfig.HIGHNOON_DIAL_LENGTH / 2, dialHighNoonGroup, highNoonDialRotate));
        helpMarkers.add(Suncreator.createHelpMarkerGroup(getCenterX(matrixDayLength), getCenterY(matrixDayLength), matrixDayLength));
        helpMarkers.add(Suncreator.createHelpMarkerGroup(getCenterX(matrixHour), getCenterY(matrixHour), matrixHour));
        helpMarkers.add(Suncreator.createHelpMarkerGroup(getCenterX(matrixMinute), getCenterY(matrixMinute), matrixMinute));
        helpMarkers.add(Suncreator.createHelpMarkerGroup(getCenterX(matrixDay), getCenterY(matrixDay), matrixDay));
        helpMarkers.add(Suncreator.createHelpMarkerGroup(getCenterX(matrixMonth), getCenterY(matrixMonth), matrixMonth));
        helpMarkers.add(Suncreator.createHelpMarkerGroup(getCenterX(matrixYear), getCenterY(matrixYear), matrixYear));
        helpMarkers.add(Suncreator.createHelpMarkerGroup(getCenterX(matrixTimeZone), getCenterY(matrixTimeZone), matrixTimeZone));
        helpMarkers.add(Suncreator.createHelpMarkerGroup(getCenterX(matrixLongitude), getCenterY(matrixLongitude), matrixLongitude));
        helpMarkers.add(Suncreator.createHelpMarkerGroup(getCenterX(matrixLatitude), getCenterY(matrixLatitude), matrixLatitude));
        helpMarkers.add(Suncreator.createHelpMarkerGroup(0, 0, tinyGlobeFrame));
        helpMarkers.add(Suncreator.createHelpMarkerGroup(Sunconfig.CONTROL_RESIZE_SIZE * 0.666, Sunconfig.CONTROL_RESIZE_SIZE * 0.666, controlThingyResize));
        helpMarkers.add(Suncreator.createHelpMarkerGroup(0, 0, controlThingyMaximize));
        helpMarkers.add(Suncreator.createHelpMarkerGroup(0, 0, controlThingyMinimize));
        helpMarkers.add(Suncreator.createHelpMarkerGroup(0, 0, controlThingyClose));
        helpMarkers.add(Suncreator.createHelpMarkerGroup(0, 0, controlThingyNightmode));
        helpMarkers.add(Suncreator.createHelpMarkerGroup(0, 0, controlThingyAlwaysOnTop));
        helpMarkers.add(Suncreator.createHelpMarkerGroup(0, 0, controlThingyGlobeGrid));
        helpMarkers.add(Suncreator.createHelpMarkerGroup(Sunconfig.CENTER_X, Sunconfig.CENTER_Y, controlNightCompression));

        helpOverlay = Suncreator.createHelpOverlay(helpMarkers, globeMasterGroup);
        helpTextGroup = Suncreator.createHelpTextGroup(helpText);


        // LAYERS
        SubScene backgroundScene = Suncreator.createBackgroundSubScene(dialMarginCircle);

        Group foregroundGroup = new Group();
        foregroundGroup.getChildren().add(globeMasterGroup);
        foregroundGroup.getChildren().add(dialCircleBackground);
        foregroundGroup.getChildren().add(dialArcNight);
        foregroundGroup.getChildren().add(dialArcMidnight);
        foregroundGroup.getChildren().add(dialMinuteMarkers);
        foregroundGroup.getChildren().add(dialCircleFrame);
        foregroundGroup.getChildren().addAll(dialHourLineMarkerList);
        foregroundGroup.getChildren().add(cetusMarkerGroup);
        foregroundGroup.getChildren().add(dialLocalSecondGroup);
        foregroundGroup.getChildren().add(dialLocalMinuteGroup);
        foregroundGroup.getChildren().addAll(dialLocalSecondLedList);
        foregroundGroup.getChildren().addAll(dialLocalMinuteLedList);
        foregroundGroup.getChildren().add(dialHighNoonGroup);
        foregroundGroup.getChildren().add(horizonGroup);
        foregroundGroup.getChildren().add(dialLocalHourGroup);
        foregroundGroup.getChildren().add(dialHourMatrixMarkerGroup);
        foregroundGroup.getChildren().add(dialCircleCenterPoint);
        foregroundGroup.getChildren().add(controlNightCompression);
        foregroundGroup.getChildren().add(cetusTimer);
        foregroundGroup.getChildren().add(dialArcDayLength);
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
        foregroundGroup.getChildren().add(controlThingyGlobeGrid);

        foregroundGroup.getChildren().add(helpOverlay);
        foregroundGroup.getChildren().add(controlThingyHelp);
        foregroundGroup.getChildren().add(helpTextGroup);
        foregroundGroup.getChildren().add(infoTextGroup);

        foregroundGroup.getChildren().add(nightModeOverlay);

        SubScene foregroundScene = new SubScene(foregroundGroup, Sunconfig.DIAL_WIDTH, Sunconfig.DIAL_HEIGHT, true, SceneAntialiasing.DISABLED);

        dialsGroup.getChildren().addAll(backgroundScene, foregroundScene);
        dialsGroup.setScaleX(Sunconfig.SCALE_X);
        dialsGroup.setScaleY(Sunconfig.SCALE_Y);


        // EVENTS
        controlNightCompression.setOnMouseEntered(event -> { helpText.setText(Sunconfig.HELPTEXT_NIGHTCOMPRESSION); controlNightCompression.setCursor(Cursor.V_RESIZE); controlNightCompression.setStyle(Sunconfig.MATRIX_GLOW2); });
        controlNightCompression.setOnMouseExited(event -> { helpText.setText(Sunconfig.HELPTEXT_DEFAULT); controlNightCompression.setCursor(Cursor.DEFAULT); controlNightCompression.setStyle(Sunconfig.MATRIX_SHADOW2); });

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


    // Methods
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

    private double getAbsoluteAngle(GregorianCalendar calendar) {
        return Sunutil.getRemainder(Sunutil.getCleanAngle(calendar), 360d);
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
        dialRotateLocalSecond.setAngle(this.localTime.get(Calendar.SECOND) * 6);

        updateDialMarkers();
    }

    public void updateCetusTimer(ArrayList<ArrayList<GregorianCalendar>> nightList) {

        long offsetTime = 0;

        int i = 0;
        while (offsetTime <= 0 && (i / 2) < nightList.size()) {
            int nightIndex = i / 2;
            int nightStep = i % 2;
            offsetTime = nightList.get(nightIndex).get(nightStep).getTimeInMillis() - localTime.getTimeInMillis();
            i++;
        }

        cetusTimer.setString(Sunutil.getShortTimeLengthString(offsetTime / 1000d).substring(1));

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
        matrixHighNoon.setString(Sunutil.getShortTimeString(highNoon));
    }

    public void setHorizon(GregorianCalendar sunrise, GregorianCalendar sunset) {

        this.sunrise = sunrise;
        this.sunset = sunset;

        this.daylength = (this.sunset.getTimeInMillis() - this.sunrise.getTimeInMillis()) / 1000;

        long daySeconds = 24 * 60 * 60;
        long clampedDayLength = this.daylength >= daySeconds ? daySeconds : this.daylength;

        setHorizonDialAngle(getAbsoluteAngle(this.sunrise), getAbsoluteAngle(this.sunset));

        matrixSunrise.setString(Sunutil.getShorterTimeString(this.sunrise));
        matrixSunset.setString(Sunutil.getShorterTimeString(this.sunset));
        matrixDayLength.setString(Sunutil.getShortTimeLengthString(clampedDayLength));
    }

    public void setLocalTime(GregorianCalendar localTime) {

        this.localTime = localTime;

        setDialAngleLocalHour(getAbsoluteAngle(this.localTime));

        dialRotateLocalMinute.setAngle(this.localTime.get(Calendar.MINUTE) * 6);
        dialRotateLocalSecond.setAngle(this.localTime.get(Calendar.SECOND) * 6);

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

            matrixStart.setString(Sunutil.getShorterTimeString(startTime));
            matrixEnd.setString(Sunutil.getShorterTimeString(endTime));

            if (startAngle > 0 && startAngle <= 180) { matrixStart.setRotate(270); }
            else { matrixStart.setRotate(90); }

            if (endAngle > 0 && endAngle <= 180) { matrixEnd.setRotate(270); }
            else { matrixEnd.setRotate(90); }
        }

        updateDialMarkers();
    }

    public void setSunTimeDialAngle(double sunTimeDialAngle) {
        this.sunTimeDialAngle = Sunutil.getNightCompressionAngle(sunTimeDialAngle, nightCompression);
        sunTimeDialRotate.setAngle(this.sunTimeDialAngle);
    }

    public void setHighNoonDialAngle(double highNoonDialAngle) {
        this.highNoonDialAngle = Sunutil.getNightCompressionAngle(highNoonDialAngle, nightCompression);
        highNoonDialRotate.setAngle(this.highNoonDialAngle);
    }

    public void setHorizonDialAngle(double sunriseDialAngle, double sunsetDialAngle) {

        this.sunriseDialAngle = Sunutil.getNightCompressionAngle(sunriseDialAngle, nightCompression);
        this.sunsetDialAngle = Sunutil.getNightCompressionAngle(sunsetDialAngle, nightCompression);

        this.sunriseDialAngle = (720 + this.sunriseDialAngle) % 360;
        this.sunsetDialAngle = (720 + this.sunsetDialAngle) % 360;

        if (this.daylength > 24 * 60 * 60) {
            horizonGroup.setVisible(false);
            dialArcNight.setVisible(false);
            dialArcDayLength.setLength(360);
        } else if (this.daylength <= 0) {
            horizonGroup.setVisible(false);
            dialArcNight.setVisible(true);
            dialArcDayLength.setLength(0);
        } else {
            horizonGroup.setVisible(true);
            dialArcNight.setVisible(true);

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

        dialArcMidnight.setStartAngle(90 - Sunutil.getNightCompressionAngle(90, nightCompression));
        dialArcMidnight.setLength(-1 * (Sunutil.getNightCompressionAngle(270, nightCompression) - Sunutil.getNightCompressionAngle(90, nightCompression)));
    }

    public void setDialAngleLocalHour(double dialAngleLocalHour) {
        this.dialAngleLocalHour = Sunutil.getNightCompressionAngle(dialAngleLocalHour, nightCompression);
        dialRotateLocalHour.setAngle(this.dialAngleLocalHour);
    }

    public void updateDialMarkers() {

        int dialMarkerRotateListSize = dialMarkerRotateList.size();
        for (int i = 0; i < dialMarkerRotateListSize; i++) {

            dialMarkerRotateList.get(i).setAngle(Sunutil.getNightCompressionAngle(i * 360d / 96d, nightCompression));

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
            cetusMarkerRotateList.get(i).setAngle(Sunutil.getNightCompressionAngle(cetusMarkerAngleList.get(i), nightCompression));
        }

        int cetusMarkerArcListSize = cetusMarkerArcList.size();
        for  (int i = 0; i < cetusMarkerArcListSize; i++) {

            double startAngle = cetusMarkerAngleList.get(i * 2);
            double endAngle = cetusMarkerAngleList.get(i * 2 + 1);

            double adjustedStartAngle = Sunutil.getNightCompressionAngle(startAngle, nightCompression);
            double adjustedEndAngle = Sunutil.getNightCompressionAngle(endAngle, nightCompression);

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

        int tinyGlobeMoveRate = globeAnimationOnEh ? 1 : Sunconfig.TINY_GLOBE_DURATION;

        tinyGlobeMoveOutTimeline.setRate(tinyGlobeMoveRate);
        tinyGlobeMoveInTimeline.setRate(tinyGlobeMoveRate);

        controlNightCompression.setFill(isVisible ? Sunconfig.Color_Of_Void : Sunconfig.Color_Of_LocalTime);
        controlNightCompression.setStroke(isVisible ? Sunconfig.Color_Of_LocalTime : Sunconfig.Color_Of_Void);
        dialArcNight.setOpacity(isVisible ? 0 : 1);

        for (Line hourLineMarker : dialHourLineMarkerList) { hourLineMarker.setStroke(isVisible ? Color.WHITE : Color.BLACK); }

        dialArcMidnight.setVisible(!isVisible);
        dialCircleBackground.setVisible(!isVisible);

        globeMasterGroup.setVisible(isVisible);
        matrixLongitude.setVisible(isVisible);
        matrixLatitude.setVisible(isVisible);
        matrixTimeZone.setVisible(isVisible);
        controlThingyGlobeGrid.setVisible(isVisible);

        if (tinyGlobeMoveOutTimeline.getStatus().equals(Animation.Status.RUNNING)) { tinyGlobeMoveOutTimeline.stop(); }
        if (tinyGlobeMoveInTimeline.getStatus().equals(Animation.Status.RUNNING)) { tinyGlobeMoveInTimeline.stop(); }

        if (isVisible) {
            tinyGlobeMoveOutTimeline.play();
        }
        else {
            tinyGlobeMoveInTimeline.play();
        }

        setDialFrameWarning(warning);
    }

    public void setCoordinates(double longitude, double latitude) {
        matrixLongitude.setString(Sunutil.formatCoordinateToString(longitude, "E", "W"));
        matrixLatitude.setString(Sunutil.formatCoordinateToString(latitude, "N", "S"));
    }

    public void toggleGlobeVisibility() {
        globeVisibleEh = !globeVisibleEh;
        setGlobeVisibility(globeVisibleEh);
    }

    public void setGlobeDaylight(double phase, double tilt) {

        this.phase.set(phase);
        this.tilt.set(tilt);
    }

    public void rotateGlobe(double longitude, double latitude) {

        this.longitude.set(longitude);
        this.latitude.set(latitude);
    }

    public void rotateGlobeAnimated(double longitude, double latitude) {

        if (longitudeTimeline.getStatus().equals(Animation.Status.RUNNING)) { longitudeTimeline.stop(); }
        if (latitudeTimeline.getStatus().equals(Animation.Status.RUNNING)) { latitudeTimeline.stop();}

        longitudeTimeline.setCycleCount(1);
        longitudeTimeline.setRate(1);
        longitudeTimeline.setAutoReverse(false);

        latitudeTimeline.setCycleCount(1);
        latitudeTimeline.setRate(1);
        latitudeTimeline.setAutoReverse(false);

        KeyValue keyValueLongitude = new KeyValue(this.longitude, longitude, Interpolator.EASE_BOTH);
        KeyFrame keyFrameLongitude = new KeyFrame(Duration.millis(Sunconfig.GLOBE_ROTATE_DURATION), keyValueLongitude);

        KeyValue keyValueLatitude = new KeyValue(this.latitude, latitude, Interpolator.EASE_BOTH);
        KeyFrame keyFrameLatitude = new KeyFrame(Duration.millis(Sunconfig.GLOBE_ROTATE_DURATION), keyValueLatitude);

        longitudeTimeline.getKeyFrames().clear();
        longitudeTimeline.getKeyFrames().add(keyFrameLongitude);

        latitudeTimeline.getKeyFrames().clear();
        latitudeTimeline.getKeyFrames().add(keyFrameLatitude);

        longitudeTimeline.play();
        latitudeTimeline.play();
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

        cetusMarkerGroup.setVisible(visibleEh);
        cetusTimer.setVisible(visibleEh);

        tinyGlobeFrame.setStroke(cetusTimeVisibleEh ? Sunconfig.Color_Of_CetusFrame : Sunconfig.Color_Of_TinyFrame);
    }

    public void setTimeDisplayOpacity(double opacity) {

        matrixTime.setOpacity(opacity);
        matrixDate.setOpacity(opacity);
        matrixTimeZone.setOpacity(opacity);

        dialLocalHourGroup.setOpacity(opacity);
        dialLocalMinuteGroup.setOpacity(opacity);
        dialLocalSecondGroup.setOpacity(opacity);

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

        controlThingyHelp.toggle();
    }

    public void toggleNightmode() {
        nightmodeEh = !nightmodeEh;
        nightModeOverlay.setVisible(nightmodeEh);
        controlThingyNightmode.toggle();
    }

    public void toggleAlwaysOnTop() {
        controlThingyAlwaysOnTop.toggle();
    }

    public void toggleGlobeGrid() {
        globeGridVisibleEh.setValue(!globeGridVisibleEh.get());
        controlThingyGlobeGrid.toggle();
    }


    // Getterers
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

    public ControlThingy getControlThingyResize() {
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

    public ControlThingy getControlThingyClose() {
        return controlThingyClose;
    }

    public ControlThingy getControlThingyMaximize() {
        return controlThingyMaximize;
    }

    public ControlThingy getControlThingyMinimize() {
        return controlThingyMinimize;
    }

    public ControlThingy getControlThingyNightmode() {
        return controlThingyNightmode;
    }

    public ControlThingy getControlThingyAlwaysOnTop() {
        return controlThingyAlwaysOnTop;
    }

    public Group getHorizonGroup() {
        return horizonGroup;
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

    public Timeline getLongitudeTimeline() {
        return longitudeTimeline;
    }

    public Timeline getLatitudeTimeline() {
        return latitudeTimeline;
    }

    public ControlThingy getControlThingyGlobeGrid() {
        return controlThingyGlobeGrid;
    }
}
