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
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static java.lang.Math.*;

public class Sundial {

    public enum MouseCatcher { LOCAL, SCENE };

    public static final double DEFAULT_WIDTH = 440.0d;
    public static final double DEFAULT_HEIGHT = 440.0d;

    // variables
    private double sunTimeDialAngle;
    private double highNoonDialAngle;
    private double sunriseDialAngle;
    private double sunsetDialAngle;
    private double dialAngleLocalHour;
    private double nightCompression;
    private boolean timeWarning;
    private boolean timezoneWarning;
    private boolean longitudeWarning;
    private boolean latitudeWarning;

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
    private DoubleProperty globeLightScaler;


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
    private Rotate arcHourRotate;

    private ArrayList<Rotate> dialMarkerRotateList;
    private ArrayList<Rotate> cetusMarkerRotateList;
    private ArrayList<Double> cetusMarkerAngleList;
    private ArrayList<Rotate> orbVallisMarkerRotateList;
    private ArrayList<Double> orbVallisMarkerAngleList;
    private Scale sunHighNoonScale;

    private Arc dialArcNight;
    private Arc dialArcHalfNight;
    private Arc dialArcDayLength;
    private Arc dialLocalHourArcPast;
    private Arc dialLocalHourArcFuture;

    private Circle dialMarginCircle;
    private Circle dialMarginCircleRing;
    private Circle dialCircleBackground;
    private Circle dialCircleFrame;
    private Group controlNightCompression;
    private Group dialCircleCenterPoint;
    private Group dialLocalSecondGroup;
    private Group dialLocalMinuteGroup;
    private Group dialLocalHourGroup;
    private SuperNiceArc dialLocalHourSuperNiceArc;
    private Group dialMidnightGroup;
    private Group dialMiddayGroup;
    private Group dialHighNoonGroup;
    private ArrayList<Arc> cetusMarkerArcList;
    private ArrayList<Line> cetusMarkerLineList;
    private ArrayList<Arc> orbVallisMarkerArcList;
    private ArrayList<Line> orbVallisMarkerLineList;
    private ArcHour arcHour;
    private Group arcHourGroup;

    private Group dialLocalSecondLedList;
    private ArrayList<Boolean> dialLocalSecondOn;
    private Group dialLocalMinuteLedList;
    private ArrayList<Boolean> dialLocalMinuteOn;
    private ArrayList<Timeline> dialLocalSecondLedOffList;
    private ArrayList<Timeline> dialLocalMinuteLedOffList;
    private ArrayList<Timeline> dialLocalMinuteLedDimList;
    private ArrayList<Timeline> cetusMarkerHoverTransitionList;
    private ArrayList<Timeline> orbVallisMarkerHoverTransitionList;

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
    private DotMatrix orbVallisTimer;
    private ArrayList<DotMatrix> orbVallisTimeMatrixList;
    private DotMatrix matrixTimeZone;

    private SunHighNoon sunHighNoon;

    private ControlThingy controlThingyHelp;
    private ControlThingy controlThingyResize;
    private ControlThingy controlThingyClose;
    private ControlThingy controlThingyMaximize;
    private ControlThingy controlThingyMinimize;
    private ControlThingy controlThingyNightmode;
    private ControlThingy controlThingyAlwaysOnTop;
    private ControlThingy controlThingyAnimation;
    private ControlThingy controlThingyChart;
    private ControlThingy controlThingyCetus;
    private ControlThingy controlThingyOrbVallis;
    private ControlThingy controlThingyGlobeGrid;
    private ControlThingy controlThingyGlobeLines;
    private ControlThingy controlThingyDst;
    private ControlThingy controlThingyPinInfo;
    private Group outerControlsGroup;

    private Group tinyGlobeFrame;
    private Group tinyGlobeGroup;
    private Group tinyGlobeNightModeOverlay;
    private Group globeMasterGroup;
    private Scale tinyGlobeScale;
    private Group horizonGroup;
    private Group cetusMarkerGroup;
    private Group orbVallisMarkerGroup;
    private Group matrixTime;
    private Group matrixDate;
    private Group helpOverlay;
    private ArrayList<Group> helpMarkers;
    private Group dialHourLineMarkerGroupA;
    private Group dialHourLineMarkerGroupB;
    private Group nightModeOverlay;
    private Group dialMinuteMarkers;
    private Group dialHourMatrixMarkerGroup;

    private Group masterTimeGroup;
    private Group masterCoordinatesGroup;

    private Timeline tinyGlobeMoveOutTimeline;
    private Timeline tinyGlobeMoveInTimeline;
    private Timeline longitudeTimeline;
    private Timeline latitudeTimeline;
    private Timeline infoTextOpacityTimeline;
    private Timeline timeAndDateMoveOutTimeline;
    private Timeline timeAndDateMoveInTimeline;
    private Timeline coordinatesMoveOutTimeline;
    private Timeline coordinatesMoveInTimeline;
    private Timeline globeMoveOutTimeline;
    private Timeline globeMoveInTimeline;
    private Timeline horizonMoveOutTimeline;
    private Timeline horizonMoveInTimeline;
    private Timeline highNoonMoveOutTimeline;
    private Timeline highNoonMoveInTimeline;
    private Timeline outerControlsGroupTimeline;
    private Timeline outerControlsGroupQuickTimeline;
    private Timeline dialLocalHourSuperNiceArcOutTimeline;
    private Timeline dialLocalHourSuperNiceArcInTimeline;

    private Text helpText;
    private Group helpTextGroup;
    private Text infoText;
    private Group infoTextGroup;
    private Text miroText;
    private Group miroTextGroup;

    private boolean globeVisibleEh = false;
    private boolean cetusTimeVisibleEh = false;
    private boolean orbVallisTimeVisibleEh = false;
    private boolean ledAnimationOnEh = true;
    private boolean globeAnimationEh = true;
    private boolean helpEh = false;
    private boolean nightmodeEh = false;
    private boolean sunHighNoonVisibleEh = false;

    private BooleanProperty globeGridVisibleEh;
    private BooleanProperty globeLinesVisibleEh;
    private BooleanProperty animationProperty;
    private BooleanProperty pinInfoProperty;


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

        globeLinesVisibleEh = new SimpleBooleanProperty(false);
        globeLinesVisibleEh.addListener((observable, oldValue, newValue) -> {});

        animationProperty = new SimpleBooleanProperty(true);
        animationProperty.addListener((observable, oldValue, newValue) -> changeAnimation(animationProperty.get()));

        pinInfoProperty = new SimpleBooleanProperty(false);
        pinInfoProperty.addListener((observable, oldValue, newValue) -> {});

        longitude = new SimpleDoubleProperty(0f);
        latitude = new SimpleDoubleProperty(0f);
        tilt = new SimpleDoubleProperty(0f);
        phase = new SimpleDoubleProperty(0f);

        longitude.addListener((observable, oldValue, newValue) -> {});
        latitude.addListener((observable, oldValue, newValue) -> {});
        tilt.addListener((observable, oldValue, newValue) -> {});
        phase.addListener((observable, oldValue, newValue) -> {});

        globeLightScaler = new SimpleDoubleProperty(1f);
        globeLightScaler.addListener((observable, oldValue, newValue) -> {});

        longitudeTimeline = new Timeline();
        latitudeTimeline = new Timeline();

        infoTextOpacityTimeline = new Timeline();
        infoTextOpacityTimeline.setCycleCount(1);
        infoTextOpacityTimeline.setRate(1);
        infoTextOpacityTimeline.setAutoReverse(false);

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
        arcHourRotate = centerRotate.clone();

        // Master globe
        globeMasterGroup = Suncreator.createMasterGlobe(longitude, latitude, phase, tilt, globeLightScaler, globeGridVisibleEh, globeLinesVisibleEh);
//        globeMasterGroup.setVisible(false);
        globeMasterGroup.setOpacity(0);

        globeMoveOutTimeline = Suncreator.createGlobeTimeline(Suncreator.TimelineDirection.OUT, globeMasterGroup);
        globeMoveInTimeline = Suncreator.createGlobeTimeline(Suncreator.TimelineDirection.IN, globeMasterGroup);

        // Tiny globe
        tinyGlobeGroup = Suncreator.createTinyGlobe(longitude, latitude, phase, tilt);

        tinyGlobeFrame = Suncreator.createTinyGlobeFrame();
        tinyGlobeFrame.setOpacity(0.75);

        tinyGlobeNightModeOverlay = Suncreator.createTinyGlobeNightModeOverlay();
        tinyGlobeNightModeOverlay.setVisible(false);

        tinyGlobeGroup.getChildren().addAll(tinyGlobeFrame, tinyGlobeNightModeOverlay);
        tinyGlobeGroup.setOpacity(Sunconfig.TINYGLOBE_DEFAULT_OPACITY);

        tinyGlobeScale = new Scale();
        tinyGlobeScale.setPivotX(Sunconfig.CENTER_X);
        tinyGlobeScale.setPivotY(Sunconfig.CENTER_Y + Sunconfig.TINYGLOBE_OFFSET);
        tinyGlobeGroup.getTransforms().add(tinyGlobeScale);

        tinyGlobeMoveInTimeline = Suncreator.createTinyGlobeTimeline(Suncreator.TimelineDirection.IN, tinyGlobeGroup, tinyGlobeScale);
        tinyGlobeMoveOutTimeline = Suncreator.createTinyGlobeTimeline(Suncreator.TimelineDirection.OUT, tinyGlobeGroup, tinyGlobeScale);

        // Cetus stuff
        cetusMarkerRotateList = new ArrayList<>();
        cetusMarkerLineList = new ArrayList<>();
        cetusMarkerArcList = new ArrayList<>();
        cetusMarkerAngleList = new ArrayList<>();
        cetusTimeMatrixList = new ArrayList<>();
        cetusMarkerHoverTransitionList = new ArrayList<>();

        cetusMarkerGroup = Suncreator.createKriegsrahmenZeitMarkerGroup(
                KriegsrahmenZeit.Location.CETUS,
                centerRotate,
                cetusMarkerAngleList,
                cetusMarkerRotateList,
                cetusMarkerLineList,
                cetusMarkerArcList,
                cetusTimeMatrixList,
                cetusMarkerHoverTransitionList
        );

        cetusTimer = Suncreator.createCetusTimer();
        cetusTimer.setMouseTransparent(true);

        setCetusTimeVisibility(cetusTimeVisibleEh);

        // Orb Vallis stuff
        orbVallisMarkerRotateList = new ArrayList<>();
        orbVallisMarkerLineList = new ArrayList<>();
        orbVallisMarkerArcList = new ArrayList<>();
        orbVallisMarkerAngleList = new ArrayList<>();
        orbVallisTimeMatrixList = new ArrayList<>();
        orbVallisMarkerHoverTransitionList = new ArrayList<>();

        orbVallisMarkerGroup = Suncreator.createKriegsrahmenZeitMarkerGroup(
                KriegsrahmenZeit.Location.ORB_VALLIS,
                centerRotate,
                orbVallisMarkerAngleList,
                orbVallisMarkerRotateList,
                orbVallisMarkerLineList,
                orbVallisMarkerArcList,
                orbVallisTimeMatrixList,
                orbVallisMarkerHoverTransitionList
        );
        orbVallisMarkerGroup.setMouseTransparent(true);

        orbVallisTimer = Suncreator.createOrbVallisTimer();
        orbVallisTimer.setMouseTransparent(true);

        setOrbVallisTimeVisibility(orbVallisTimeVisibleEh);

        // Bind timer position to adversary's visibility
        cetusTimer.translateXProperty().bind(Bindings.createDoubleBinding(() -> {
            double deltaX = 0;
            if (orbVallisTimer.visibleProperty().get()) {
                deltaX = -cetusTimer.getLayoutBounds().getWidth() / 2 - 5;
            }
            return deltaX;
        }, orbVallisTimer.visibleProperty()));

        orbVallisTimer.translateXProperty().bind(Bindings.createDoubleBinding(() -> {
            double deltaX = 0;
            if (cetusTimer.visibleProperty().get()) {
                deltaX = orbVallisTimer.getLayoutBounds().getWidth()/ 2 + 5;
            }
            return deltaX;
        }, cetusTimer.visibleProperty()));

        // Hour markers
        dialHourMatrixMarkerGroup = new Group();
        dialHourMatrixMarkerGroup.setMouseTransparent(true);

        dialMarkerRotateList = new ArrayList<>();
        hourMarkerMatrixList = new ArrayList<>();

        dialHourLineMarkerGroupA = new Group();

        dialHourLineMarkerGroupB = new Group();

        Suncreator.createDialHourMarkers(
                nightCompression,
                centerRotate,
                dialHourMatrixMarkerGroup,
                hourMarkerMatrixList,
                dialHourLineMarkerGroupA,
                dialHourLineMarkerGroupB,
                dialMarkerRotateList
        );

        // Dials
        dialHighNoonGroup = Suncreator.createDialHighNoonGroup(highNoonDialRotate);
        dialLocalMinuteGroup = Suncreator.createDialLocalMinuteGroup(dialRotateLocalMinute);
        dialLocalSecondGroup = Suncreator.createDialLocalSecondGroup(dialRotateLocalSecond);
        dialLocalHourGroup = Suncreator.createDialLocalHourGroup(dialRotateLocalHour);

        dialLocalHourSuperNiceArc = Suncreator.createDialLocalHourSuperNiceArc();

        dialLocalHourSuperNiceArc.opacityProperty().bind(Bindings.createDoubleBinding(() ->
                        1 - globeMasterGroup.opacityProperty().get() * 0.5,
                globeMasterGroup.opacityProperty()
        ));

        dialMidnightGroup = Suncreator.createDialMidnightGroup(Color.WHITE, Sunconfig.LOCALMIDNIGHT_DIAL_GLOW, 0);

        dialMiddayGroup = Suncreator.createDialMidnightGroup(Color.BLACK, Sunconfig.MATRIX_SHADOW, 180);
        dialMiddayGroup.setBlendMode(BlendMode.OVERLAY);
        dialMiddayGroup.setOpacity(0.75);

        arcHour = new ArcHour(Sunconfig.CENTER_X, Sunconfig.CENTER_Y, Sunconfig.MINUTE_ARC_RADIUS);
        arcHour.setWidth(Sunconfig.ARCHOUR_STROKE_WIDTH);
        arcHour.setMinuteStroke(Sunconfig.Color_Of_Minutes);
        arcHour.setMinuteStyle(Sunconfig.LOCALMINUTE_GLOW);
        arcHour.setSecondStroke(Sunconfig.Color_Of_Seconds);
        arcHour.setSecondStyle(Sunconfig.LOCALSECOND_GLOW);
        arcHour.setSecondWidth(Sunconfig.ARCHOUR_STROKE_WIDTH * 2);
        arcHour.setLineStartStroke(Sunconfig.Color_Of_Minutes);
        arcHour.setLineStartStyle(Sunconfig.LOCALMINUTE_GLOW);
        arcHour.setProgressOpacity(Sunconfig.ARCHOUR_PROGRESS_OPACITY);
        arcHour.setShadowOpacity(Sunconfig.ARCHOUR_SHADOW_OPACITY);
        arcHour.setMouseTransparent(true);

        arcHourGroup = new Group(arcHour);
        arcHourGroup.getTransforms().addAll(arcHourRotate);

        arcHourGroup.opacityProperty().bind(Bindings.createDoubleBinding(() ->
                        1 - globeMasterGroup.opacityProperty().get() * 0.5,
                globeMasterGroup.opacityProperty()
        ));

        dialLocalHourArcPast = Suncreator.createDialLocalHourArc();
        dialLocalHourArcPast.setStroke(Sunconfig.Color_Of_MinutesArc);
        dialLocalHourArcPast.setStyle(Sunconfig.LOCALHOUR_PAST_GLOW);
        dialLocalHourArcPast.setBlendMode(BlendMode.LIGHTEN);
        dialLocalHourArcPast.setOpacity(Sunconfig.DIAL_LOCAL_ARC_PAST_OPACITY);

        dialLocalHourArcFuture = Suncreator.createDialLocalHourArc();
        dialLocalHourArcFuture.setStroke(Color.BLACK);
        dialLocalHourArcFuture.setBlendMode(BlendMode.OVERLAY);
        dialLocalHourArcFuture.setOpacity(Sunconfig.DIAL_LOCAL_ARC_FUTURE_OPACITY);

        matrixSunrise = Suncreator.createMatrixSunrise();
        matrixSunset = Suncreator.createMatrixSunset();

        horizonGroup = new Group(
                Suncreator.createHorizonGroup(sunriseDialRotate, matrixSunrise),
                Suncreator.createHorizonGroup(sunsetDialRotate, matrixSunset)
        );
        horizonGroup.setMouseTransparent(true);

//        horizonMoveOutTimeline = Suncreator.createHorizonTimeline(Suncreator.TimelineDirection.OUT, horizonGroup);
//        horizonMoveInTimeline = Suncreator.createHorizonTimeline(Suncreator.TimelineDirection.IN, horizonGroup);

//        dialLocalHourSuperNiceArcOutTimeline = Suncreator.createDialLocalHourSuperNiceArcTimeline(Suncreator.TimelineDirection.OUT, dialLocalHourSuperNiceArc);
//        dialLocalHourSuperNiceArcInTimeline = Suncreator.createDialLocalHourSuperNiceArcTimeline(Suncreator.TimelineDirection.IN, dialLocalHourSuperNiceArc);

        // Sun High Noon extra information
        sunHighNoon = new SunHighNoon(
                Sunconfig.CENTER_X
                , Sunconfig.CENTER_Y
                , Sunconfig.DAYLENGTH_ARC_RADIUS
                , Sunconfig.MATRIX_SUNHIGHNOON_SCALE
                , highNoonDialRotate
        );
        sunHighNoon.setArcLook(Color.YELLOW, 1, Sunconfig.MATRIX_GLOW, BlendMode.SRC_OVER);
        sunHighNoon.setHorizonLook(Sunconfig.Color_Of_Horizon, Sunconfig.SUNRISE_STROKE_WIDTH, Sunconfig.HORIZON_GLOW);
        sunHighNoon.setSunLineLook(Sunconfig.Color_Of_Horizon, Sunconfig.SUNRISE_STROKE_WIDTH, Sunconfig.MATRIX_GLOW, BlendMode.SRC_OVER);
        sunHighNoon.setMarkerLook(Color.WHITE, 1, Sunconfig.MATRIX_SHADOW);
        sunHighNoon.setSunDotLook(Color.LIGHTYELLOW, Sunconfig.SUNDOT_GLOW, BlendMode.SRC_OVER);
        sunHighNoon.setMatrixTimeLook(Sunconfig.Color_Of_HighNoon, Sunconfig.MATRIX_GLOW);
        sunHighNoon.setMatrixAngleLook(Sunconfig.Color_Of_HighNoon, Sunconfig.MATRIX_SHADOW4);
        sunHighNoon.setMatrixDayLengthLook(Color.WHITE, Sunconfig.MATRIX_SHADOW);
        sunHighNoon.setOpacity(Sunconfig.HIGHNOON_NORMAL_OPACITY);
        sunHighNoon.setVisible(sunHighNoonVisibleEh);

        sunHighNoonScale = new Scale();
        sunHighNoonScale.setPivotX(Sunconfig.CENTER_X);
        sunHighNoonScale.setPivotY(Sunconfig.CENTER_Y);

        sunHighNoon.getTransforms().add(sunHighNoonScale);

        highNoonMoveOutTimeline = Suncreator.createHighNoonTimeline(Suncreator.TimelineDirection.OUT, sunHighNoon, sunHighNoonScale);
        highNoonMoveInTimeline = Suncreator.createHighNoonTimeline(Suncreator.TimelineDirection.IN, sunHighNoon, sunHighNoonScale);


        // Control thingies
        controlThingyHelp = Suncreator.createControlThingy(Suncreator.ControlThingyType.HELP, helpText);
        controlThingyResize = Suncreator.createControlThingy(Suncreator.ControlThingyType.RESIZE, helpText);
        controlThingyClose = Suncreator.createControlThingy(Suncreator.ControlThingyType.CLOSE, helpText);
        controlThingyMaximize = Suncreator.createControlThingy(Suncreator.ControlThingyType.MAXIMIZE, helpText);
        controlThingyMinimize = Suncreator.createControlThingy(Suncreator.ControlThingyType.MINIMIZE, helpText);
        controlThingyNightmode = Suncreator.createControlThingy(Suncreator.ControlThingyType.NIGHTMODE, helpText);
        controlThingyAlwaysOnTop = Suncreator.createControlThingy(Suncreator.ControlThingyType.ALWAYSONTOP, helpText);
        controlThingyAnimation = Suncreator.createControlThingy(Suncreator.ControlThingyType.ANIMATION, helpText);
        controlThingyPinInfo = Suncreator.createControlThingy(Suncreator.ControlThingyType.PIN_INFO, helpText);
        controlThingyChart = Suncreator.createControlThingy(Suncreator.ControlThingyType.CHART, helpText);
        controlThingyCetus = Suncreator.createControlThingy(Suncreator.ControlThingyType.CETUS, helpText);
        controlThingyOrbVallis = Suncreator.createControlThingy(Suncreator.ControlThingyType.ORBVALLIS, helpText);

        controlThingyGlobeGrid = Suncreator.createControlThingy(Suncreator.ControlThingyType.GLOBEGRID, helpText);
        controlThingyGlobeLines = Suncreator.createControlThingy(Suncreator.ControlThingyType.GLOBELINES, helpText);

        outerControlsGroup = new Group(
                controlThingyResize,
                controlThingyClose,
                controlThingyMaximize,
                controlThingyMinimize,
                controlThingyNightmode,
                controlThingyAlwaysOnTop,
                controlThingyAnimation,
                controlThingyPinInfo,
                controlThingyChart,
                controlThingyCetus,
                controlThingyOrbVallis,
                controlThingyHelp,
                controlThingyGlobeGrid,
                controlThingyGlobeLines
        );

        outerControlsGroupTimeline = Suncreator.createOuterControlsGroupTimeline(outerControlsGroup);
        outerControlsGroupQuickTimeline = Suncreator.createOuterControlsGroupQuickTimeline(outerControlsGroup);

        controlThingyCetus.stateProperty().bind(cetusTimer.visibleProperty());
        controlThingyOrbVallis.stateProperty().bind(orbVallisTimer.visibleProperty());

        controlThingyDst = Suncreator.createControlThingy(Suncreator.ControlThingyType.DST, helpText);

        controlThingyAnimation.stateProperty().bind(animationProperty);
        controlThingyPinInfo.stateProperty().bind(pinInfoProperty);
        controlThingyGlobeGrid.visibleProperty().bind(globeMasterGroup.visibleProperty());
        controlThingyGlobeGrid.opacityProperty().bind(globeMasterGroup.opacityProperty());
        controlThingyGlobeLines.visibleProperty().bind(globeMasterGroup.visibleProperty());
        controlThingyGlobeLines.opacityProperty().bind(globeMasterGroup.opacityProperty());

        tinyGlobeGroup.opacityProperty().bind(outerControlsGroup.opacityProperty());

        // Time and Date
        controlNightCompression = Suncreator.createControlNightCompression();

        matrixDay = new DotMatrix("00", Sunconfig.Color_Of_LocalTime);
        matrixMonth = new DotMatrix("00", Sunconfig.Color_Of_LocalTime);
        matrixYear = new DotMatrix("0000", Sunconfig.Color_Of_LocalTime);
        matrixWeek = new DotMatrix("00", Sunconfig.Color_Of_LocalTime);
        matrixDate = Suncreator.createMatrixDate(matrixDay, matrixMonth, matrixYear, matrixWeek);

        matrixDate.opacityProperty().bind(Bindings.createDoubleBinding(() ->
                        outerControlsGroup.opacityProperty().get() * (1 - Sunconfig.DATE_OFF_OPACITY) + Sunconfig.DATE_OFF_OPACITY,
                outerControlsGroup.opacityProperty()
        ));

        matrixHour = new DotMatrix("00", Sunconfig.Color_Of_LocalTime);
        matrixMinute = new DotMatrix("00", Sunconfig.Color_Of_LocalTime);
        matrixSecond = new DotMatrix("00", Sunconfig.Color_Of_LocalTime);
        matrixTime = Suncreator.createMatrixTime(matrixHour, matrixMinute, matrixSecond);

        matrixTimeZone = Suncreator.createMatrixTimeZone();
        matrixTimeZone.opacityProperty().bind(outerControlsGroup.opacityProperty());

        controlThingyDst.setTranslateX(Sunconfig.CENTER_Y + matrixTimeZone.getLayoutBounds().getWidth() / 2 + controlThingyDst.getLayoutBounds().getWidth() + 3);
        controlThingyDst.setTranslateY(matrixTimeZone.getLayoutY() + controlThingyDst.getLayoutBounds().getHeight() / 2);
        controlThingyDst.opacityProperty().bind(matrixTimeZone.opacityProperty());

        masterTimeGroup = new Group(matrixTimeZone, matrixDate, matrixTime, controlNightCompression, controlThingyDst);

        timeAndDateMoveOutTimeline = Suncreator.createTimeAndDateTimeline(Suncreator.TimelineDirection.OUT, masterTimeGroup);
        timeAndDateMoveInTimeline = Suncreator.createTimeAndDateTimeline(Suncreator.TimelineDirection.IN, masterTimeGroup);

        sunHighNoon.opacityProperty().bind(Bindings.createDoubleBinding(() -> {
                    double outerControlsGroupOpacity = outerControlsGroup.opacityProperty().get();
                    double masterTimeGroupOpacity = masterTimeGroup.opacityProperty().get();
                    return (masterTimeGroupOpacity < outerControlsGroupOpacity && outerControlsGroupOpacity > 0.5) ?
                            0.5 : outerControlsGroupOpacity;
                },
                outerControlsGroup.opacityProperty(), masterTimeGroup.opacityProperty()
        ));

        // LEDs with minutes and seconds markers
        dialLocalSecondLedList = new Group();
        dialLocalSecondOn = new ArrayList<>();
        dialLocalMinuteLedList = new Group();
        dialLocalMinuteOn = new ArrayList<>();
        dialLocalSecondLedOffList = new ArrayList<>();
        dialLocalMinuteLedOffList = new ArrayList<>();
        dialLocalMinuteLedDimList = new ArrayList<>();

        Suncreator.createLEDs(
                dialLocalSecondLedList,
                dialLocalSecondOn,
                dialLocalMinuteLedList,
                dialLocalMinuteOn,
                dialLocalSecondLedOffList,
                dialLocalMinuteLedOffList,
                dialLocalMinuteLedDimList
        );

        dialMinuteMarkers = Suncreator.createDialMinuteMarkers();

        // Coordinates group
        matrixLongitude = Suncreator.createMatrixLongitude();
        matrixLatitude = Suncreator.createMatrixLatitude();

        matrixLongitude.opacityProperty().bind(outerControlsGroup.opacityProperty());
        matrixLatitude.opacityProperty().bind(outerControlsGroup.opacityProperty());

        masterCoordinatesGroup = new Group(matrixLongitude, matrixLatitude);
        masterCoordinatesGroup.setOpacity(0);
//        masterCoordinatesGroup.setVisible(false);
        masterCoordinatesGroup.setTranslateY(150);

        coordinatesMoveOutTimeline = Suncreator.createCoordinatesTimeline(Suncreator.TimelineDirection.OUT, masterCoordinatesGroup);
        coordinatesMoveInTimeline = Suncreator.createCoordinatesTimeline(Suncreator.TimelineDirection.IN, masterCoordinatesGroup);

        // Other stuff
        dialMarginCircle = Suncreator.createDialMarginCircle();
        dialMarginCircleRing = Suncreator.createDialMarginCircleRing();
        dialArcNight = Suncreator.createDialArcNight();
        dialArcHalfNight = Suncreator.createDialArcHalfNight();
        dialCircleBackground = Suncreator.createDialCircleBackground();
        dialCircleFrame = Suncreator.createDialCircleFrame();
        dialCircleCenterPoint = Suncreator.createDialCircleCenterPoint();
        dialArcDayLength = Suncreator.createDialArcDayLength();
        matrixDayLength = Suncreator.createMatrixDayLength();
        matrixHighNoon = Suncreator.createMatrixHighNoon();

        nightModeOverlay = Suncreator.createNightModeOverlay();

        dialCircleFrame.opacityProperty().bind(Bindings.createDoubleBinding(() ->
                        (1 - globeMasterGroup.opacityProperty().get())
                , globeMasterGroup.opacityProperty()
        ));

//        dialArcDayLength.opacityProperty().bind(outerControlsGroup.opacityProperty());
        matrixDayLength.opacityProperty().bind(outerControlsGroup.opacityProperty());
        dialHourMatrixMarkerGroup.opacityProperty().bind(outerControlsGroup.opacityProperty());
        dialHourLineMarkerGroupB.opacityProperty().bind(outerControlsGroup.opacityProperty());

        // Info overlay
        infoText = new Text();
        infoTextGroup = Suncreator.createInfoTextGroup(infoText);

        // Help overlay
        helpMarkers = new ArrayList<>();
//        helpMarkers.add(Suncreator.createHelpMarker(dialHighNoonGroup, null,  null));
        helpMarkers.add(Suncreator.createHelpMarker(matrixHour, null, null));
        helpMarkers.add(Suncreator.createHelpMarker(matrixMinute, null, null));
        helpMarkers.add(Suncreator.createHelpMarker(matrixDay, null, null));
        helpMarkers.add(Suncreator.createHelpMarker(matrixMonth, null, null));
        helpMarkers.add(Suncreator.createHelpMarker(matrixYear, null, null));
        helpMarkers.add(Suncreator.createHelpMarker(matrixTimeZone, null, null));
        helpMarkers.add(Suncreator.createHelpMarker(matrixLongitude, masterCoordinatesGroup.visibleProperty(), masterCoordinatesGroup.opacityProperty()));
        helpMarkers.add(Suncreator.createHelpMarker(matrixLatitude, masterCoordinatesGroup.visibleProperty(), masterCoordinatesGroup.opacityProperty()));
        helpMarkers.add(Suncreator.createHelpMarker(tinyGlobeFrame, null, null));
        helpMarkers.add(Suncreator.createHelpMarker(controlThingyResize, null, null));
        helpMarkers.add(Suncreator.createHelpMarker(controlThingyMaximize, null, null));
        helpMarkers.add(Suncreator.createHelpMarker(controlThingyMinimize, null, null));
        helpMarkers.add(Suncreator.createHelpMarker(controlThingyClose, null, null));
        helpMarkers.add(Suncreator.createHelpMarker(controlThingyNightmode, null, null));
        helpMarkers.add(Suncreator.createHelpMarker(controlThingyAlwaysOnTop, null, null));
        helpMarkers.add(Suncreator.createHelpMarker(controlThingyAnimation, null, null));
        helpMarkers.add(Suncreator.createHelpMarker(controlThingyPinInfo, null, null));
        helpMarkers.add(Suncreator.createHelpMarker(controlThingyChart, null, null));
        helpMarkers.add(Suncreator.createHelpMarker(controlThingyCetus, null, null));
        helpMarkers.add(Suncreator.createHelpMarker(controlThingyOrbVallis, null, null));
        helpMarkers.add(Suncreator.createHelpMarker(controlThingyHelp, null, null));
        helpMarkers.add(Suncreator.createHelpMarker(controlThingyGlobeGrid, null, null));
        helpMarkers.add(Suncreator.createHelpMarker(controlThingyGlobeLines, null, null));
        helpMarkers.add(Suncreator.createHelpMarker(controlThingyDst, null, null));
        helpMarkers.add(Suncreator.createHelpMarker(controlNightCompression, null, null));

        helpOverlay = Suncreator.createHelpOverlay(helpMarkers);
        helpTextGroup = Suncreator.createHelpTextGroup(helpText);

        miroText = new Text();
        miroTextGroup = Suncreator.createMiroTextGroup(miroText);
        miroTextGroup.visibleProperty().bind(helpOverlay.visibleProperty());


        // LAYERS
        Group foregroundGroup = new Group(
                dialMarginCircle
                ,dialCircleBackground
//                ,dialHourLineMarkerGroupA
                ,dialArcNight
                ,dialArcHalfNight
                ,globeMasterGroup
                ,dialCircleFrame
//                ,dialMidnightGroup
                ,dialMiddayGroup
                ,cetusMarkerGroup
                ,orbVallisMarkerGroup
//                ,dialMinuteMarkers
//                ,dialLocalMinuteLedList
//                ,dialLocalSecondLedList
                ,dialHourMatrixMarkerGroup
                ,dialArcDayLength
                ,dialHighNoonGroup
                ,dialLocalHourGroup
                ,sunHighNoon
                ,horizonGroup
                ,dialLocalHourArcFuture
                ,dialLocalHourArcPast
                ,dialHourLineMarkerGroupB
                ,arcHourGroup
                ,dialLocalHourSuperNiceArc
                ,dialCircleCenterPoint
                ,cetusTimer
                ,orbVallisTimer
                ,matrixDayLength
                ,masterTimeGroup
                ,masterCoordinatesGroup
                ,outerControlsGroup
                ,tinyGlobeGroup
                ,helpOverlay
                ,miroTextGroup
                ,helpTextGroup
                ,infoTextGroup
                ,nightModeOverlay
        );

        SubScene foregroundScene = new SubScene(foregroundGroup, DEFAULT_WIDTH, DEFAULT_HEIGHT, true, SceneAntialiasing.DISABLED);

        dialsGroup.getChildren().addAll(foregroundScene);
        dialsGroup.setScaleX(Sunconfig.SCALE_X);
        dialsGroup.setScaleY(Sunconfig.SCALE_Y);


        // EVENTS
        cetusMarkerGroup.setOnMouseEntered(event -> { cetusMarkerGroup.setCursor(globeVisibleEh ? Cursor.OPEN_HAND : Cursor.MOVE); });
        cetusMarkerGroup.setOnMouseExited(event -> { cetusMarkerGroup.setCursor(Cursor.DEFAULT); });

        controlNightCompression.setOnMouseEntered(event -> { helpText.setText(Sunconfig.HELPTEXT_NIGHTCOMPRESSION); controlNightCompression.setCursor(Cursor.V_RESIZE); controlNightCompression.setStyle(Sunconfig.MATRIX_GLOW); });
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

        dialMarginCircle.setOnMouseEntered(event -> { helpText.setText(Sunconfig.HELPTEXT_WINDOW); dialMarginCircle.setCursor(Cursor.MOVE);/* dialMarginCircle.setFill(Sunconfig.Color_Of_Margin_Hover); */});
        dialMarginCircle.setOnMouseExited(event -> {  helpText.setText(Sunconfig.HELPTEXT_DEFAULT); dialMarginCircle.setCursor(Cursor.DEFAULT);/* dialMarginCircle.setFill(Sunconfig.Color_Of_MarginEnd); */});

        dialCircleFrame.setOnMouseEntered(event -> { helpText.setText(globeVisibleEh ? Sunconfig.HELPTEXT_GLOBE : Sunconfig.HELPTEXT_WINDOW); dialCircleFrame.setCursor(globeVisibleEh ? Cursor.OPEN_HAND : Cursor.MOVE); });
        dialCircleFrame.setOnMouseExited(event -> { helpText.setText(Sunconfig.HELPTEXT_DEFAULT); dialCircleFrame.setCursor(Cursor.DEFAULT); });

//        matrixDayLength.setOnMouseEntered(event -> { helpText.setText(Sunconfig.HELPTEXT_HORIZON); matrixDayLength.setCursor(Cursor.HAND); setGroupGlow(matrixDayLength, Sunconfig.MATRIX_GLOW); dialArcDayLength.setStyle(Sunconfig.MATRIX_GLOW); });
//        matrixDayLength.setOnMouseExited(event -> { helpText.setText(Sunconfig.HELPTEXT_DEFAULT); matrixDayLength.setCursor(Cursor.DEFAULT); setGroupGlow(matrixDayLength, Sunconfig.MATRIX_SHADOW); dialArcDayLength.setStyle(Sunconfig.MATRIX_SHADOW); });

//        dialHighNoonGroup.setOnMouseEntered(event -> { helpText.setText(Sunconfig.HELPTEXT_HIGHNOON); dialHighNoonGroup.setCursor(Cursor.HAND); setGroupGlow(dialHighNoonGroup, Sunconfig.LOCALNOON_DIAL_HOT); });
//        dialHighNoonGroup.setOnMouseExited(event -> { helpText.setText(Sunconfig.HELPTEXT_DEFAULT); dialHighNoonGroup.setCursor(Cursor.DEFAULT); setGroupGlow(dialHighNoonGroup, Sunconfig.LOCALNOON_DIAL_GLOW); });

        matrixTimeZone.setOnMouseEntered(event -> { helpText.setText(Sunconfig.HELPTEXT_TIMEZONE); matrixTimeZone.setCursor(Cursor.V_RESIZE); matrixTimeZone.setStyle(Sunconfig.MATRIX_GLOW); });
        matrixTimeZone.setOnMouseExited(event -> { helpText.setText(Sunconfig.HELPTEXT_DEFAULT); matrixTimeZone.setCursor(Cursor.DEFAULT); matrixTimeZone.setStyle(Sunconfig.MATRIX_SHADOW); });

        miroTextGroup.setOnMouseEntered(event -> { helpText.setText(Sunconfig.HELPTEXT_MIRO); miroTextGroup.setCursor(Cursor.HAND); miroText.setUnderline(true); });
        miroTextGroup.setOnMouseExited(event -> { helpText.setText(Sunconfig.HELPTEXT_DEFAULT); miroTextGroup.setCursor(Cursor.DEFAULT); miroText.setUnderline(false); });

        dialsGroup.setOnMouseMoved(event -> {
            if (helpTextGroup.isVisible()) { moveGroup(helpTextGroup, event, MouseCatcher.LOCAL); }
            if (infoTextGroup.isVisible()) { moveGroup(infoTextGroup, event, MouseCatcher.LOCAL); }
        });


        // additional tweeks
        tinyGlobeMoveOutTimeline.setRate(Sunconfig.TINY_GLOBE_DURATION);
        tinyGlobeMoveOutTimeline.play();

    }


    // Methods
    public void moveGroup(Node node, Event event, MouseCatcher mouseCatcher) {

        double mouseLocalX, mouseLocalY;
        double mouseSceneX, mouseSceneY;
        double mouseX, mouseY;

        double scaleX = 1.0;
        double scaleY = 1.0;

        Transform transform = dialsGroup.getTransforms().get(0);
        if (transform != null && transform instanceof Scale) {
            scaleX = ((Scale) transform).getX();
            scaleY = ((Scale) transform).getY();
        }

        if (event != null) {
            if (event instanceof DragEvent) {
                mouseLocalX = ((DragEvent) event).getX();
                mouseLocalY = ((DragEvent) event).getY();
                mouseSceneX = ((DragEvent) event).getSceneX() / scaleX;
                mouseSceneY = ((DragEvent) event).getSceneY() / scaleY;
            } else if (event instanceof MouseEvent) {
                mouseLocalX = ((MouseEvent) event).getX();
                mouseLocalY = ((MouseEvent) event).getY();
                mouseSceneX = ((MouseEvent) event).getSceneX() / scaleX;
                mouseSceneY = ((MouseEvent) event).getSceneY() / scaleY;
            } else {
                return;
            }

            switch (mouseCatcher) {
                case LOCAL:
                    mouseX = mouseLocalX;
                    mouseY = mouseLocalY;
                    break;
                case SCENE:
                    mouseX = mouseSceneX;
                    mouseY = mouseSceneY;
                    break;
                default:
                    mouseX = 0;
                    mouseY = 0;
            }

        } else {
            mouseX = node.getLayoutBounds().getMaxX();
            mouseY = node.getLayoutBounds().getMaxY();
        }

        double width = node.getLayoutBounds().getWidth();
        double height = node.getLayoutBounds().getHeight();

        double deltaX, deltaY;

        if (mouseX + width > DEFAULT_WIDTH) {
            deltaX = mouseX - (width + Sunconfig.HELP_TEXT_OFFSET);
        } else {
            deltaX = mouseX + Sunconfig.HELP_TEXT_OFFSET;
        }
        if (mouseY + height > DEFAULT_HEIGHT) {
            deltaY = mouseY - (height + Sunconfig.HELP_TEXT_OFFSET);
        } else {
            deltaY = mouseY + Sunconfig.HELP_TEXT_OFFSET;
        }

        if (deltaX < 0) { deltaX = 0; }
        if (deltaY < 0) { deltaY = 0; }
        if (deltaX > DEFAULT_WIDTH - width) { deltaX = DEFAULT_WIDTH - width; }
        if (deltaY > DEFAULT_HEIGHT - height) { deltaY = DEFAULT_HEIGHT - height; }

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

    public void showOuterControlsGroup() {

        outerControlsGroupTimeline.stop();
        outerControlsGroupQuickTimeline.stop();

        outerControlsGroup.setOpacity(1.0);
        outerControlsGroup.setVisible(true);
    }

    public void hideOuterControlsGroup() {

        if (!helpOverlay.isVisible() && !pinInfoProperty.get()) {

            if (animationProperty.get()) {
                outerControlsGroupTimeline.play();
            } else {
                outerControlsGroupQuickTimeline.play();
            }

        }
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
        setHourMarkerLocalHour(this.localTime);
        setArcHourRotate(this.localTime);

        dialRotateLocalMinute.setAngle(this.localTime.get(Calendar.MINUTE) * 6);
        dialRotateLocalSecond.setAngle(this.localTime.get(Calendar.SECOND) * 6);

        updateDialMarkers();
    }

    private void setHourMarkerLocalHour(GregorianCalendar gregorianCalendar) {

        int i = 0;
        int hour = ((gregorianCalendar.get(Calendar.HOUR_OF_DAY) + 12) % 24) * 4;

        for (Node node : dialHourLineMarkerGroupB.getChildren()) {

            if (i >= hour && i <= (hour + 4)) {
                node.setVisible(true);
            } else {
                node.setVisible(false);
            }

            i++;
        }

        if (gregorianCalendar.get(Calendar.HOUR_OF_DAY) == 11) {
            dialHourLineMarkerGroupB.getChildren().get(0).setVisible(true);
        }
    }

    public void updateCetusTimer(ArrayList<ArrayList<GregorianCalendar>> cycleList) {
        updateKriegsrahmenTimer(KriegsrahmenZeit.Location.CETUS, cycleList);
    }

    public void updateOrbVallisTimer(ArrayList<ArrayList<GregorianCalendar>> cycleList) {
        updateKriegsrahmenTimer(KriegsrahmenZeit.Location.ORB_VALLIS, cycleList);
    }

    public void updateKriegsrahmenTimer(
            KriegsrahmenZeit.Location location,
            ArrayList<ArrayList<GregorianCalendar>> cycleList
    ) {

        DotMatrix dotMatrix;
        Color mainColor, lastColor;
        String mainStyle, lastStyle;

        switch (location) {
            case CETUS:
                dotMatrix = cetusTimer;
                mainColor = Sunconfig.Color_Of_CetusNight;
                lastColor = Sunconfig.Color_Of_CetusDay;
                mainStyle = Sunconfig.CETUS_MATRIX_SHADOW_NIGHT;
                lastStyle = Sunconfig.CETUS_MATRIX_SHADOW_DAY;
                break;
            case ORB_VALLIS:
                dotMatrix = orbVallisTimer;
                mainColor = Sunconfig.Color_Of_OrbVallisWarm;
                lastColor = Sunconfig.Color_Of_OrbVallisCold;
                mainStyle = Sunconfig.ORBVALLIS_MATRIX_SHADOW_WARM;
                lastStyle = Sunconfig.ORBVALLIS_MATRIX_SHADOW_COLD;
                break;
            default: return;
        }

        long offsetTime = 0;

        int i = 0;
        while (offsetTime <= 0 && (i / 2) < cycleList.size()) {
            int cycleIndex = i / 2;
            int cyclePhase = i % 2;
            offsetTime = cycleList.get(cycleIndex).get(cyclePhase).getTimeInMillis() - localTime.getTimeInMillis();
            i++;
        }

        dotMatrix.setString(Sunutil.getShortTimeLengthString(offsetTime / 1000d).substring(1));

        if (i % 2 == 0) {
            dotMatrix.setFill(mainColor);
            dotMatrix.setStyle(mainStyle);
        } else {
            dotMatrix.setFill(lastColor);
            dotMatrix.setStyle(lastStyle);
        }
    }

    public void setHighNoon(GregorianCalendar highNoon, double angle) {

        this.highNoon = highNoon;

        setHighNoonDialAngle(getAbsoluteAngle(this.highNoon));

        matrixHighNoon.setString(Sunutil.getShortTimeString(highNoon));
        sunHighNoon.setParameters(this.highNoon, angle, daylength);
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

    public void setArcHourRotate(GregorianCalendar localTime) {

        GregorianCalendar fullHourTime = new GregorianCalendar();
        fullHourTime.setTime(localTime.getTime());
        fullHourTime.set(GregorianCalendar.HOUR_OF_DAY, localTime.get(GregorianCalendar.HOUR_OF_DAY) + 1);
        fullHourTime.set(GregorianCalendar.MINUTE, 0);
        fullHourTime.set(GregorianCalendar.SECOND, 0);

        double angle = getAbsoluteAngle(fullHourTime);
        arcHourRotate.setAngle(Sunutil.getNightCompressionAngle(angle, nightCompression) + 90);
    }

    public void setLocalTime(GregorianCalendar localTime) {

        this.localTime = localTime;

        int second = this.localTime.get(Calendar.SECOND);
        int minute = this.localTime.get(Calendar.MINUTE);
        int hour = this.localTime.get(Calendar.HOUR_OF_DAY);

        setDialAngleLocalHour(getAbsoluteAngle(this.localTime));
        updateHourArc(this.localTime);

        dialRotateLocalMinute.setAngle(minute * 6);
        dialRotateLocalSecond.setAngle(second * 6);

        arcHour.setTime(minute, second);
        setArcHourRotate(this.localTime);

//        updateSingleLEDs(dialLocalSecondLedList, dialLocalSecondOn, dialLocalSecondLedOffList, localTime.get(Calendar.SECOND));
//        updateRowLEDs(dialLocalMinuteLedList, dialLocalMinuteOn, dialLocalMinuteLedOffList, dialLocalMinuteLedDimList, localTime.get(Calendar.MINUTE));
//        updateSingleLEDs(dialLocalMinuteLedList, dialLocalMinuteOn, dialLocalMinuteLedOffList, localTime.get(Calendar.MINUTE));
    }

    private void updateSingleLEDs(Group ledList, ArrayList<Boolean> ledOn, ArrayList<Timeline> timelineList, int indexOn) {

        for (int i = 0; i < ledList.getChildren().size(); i++) {

            if (ledOn.get(i)) {

                if(i == indexOn) { continue; }

                if (ledAnimationOnEh) {
                    timelineList.get(i).play();
                } else {
                    ledList.getChildren().get(i).setOpacity(Sunconfig.SECOND_LED_OFF_OPACITY);
                }

                ledOn.set(i, false);
            }
        }

        timelineList.get(indexOn).stop();
        ledList.getChildren().get(indexOn).setOpacity(Sunconfig.SECOND_LED_ON_OPACITY);
        ledOn.set(indexOn, true);
    }

    private void updateRowLEDs(Group ledList, ArrayList<Boolean> ledOn, ArrayList<Timeline> timelineOffList, ArrayList<Timeline> timelineDimList, int indexOn) {

        for (int i = 0; i < ledList.getChildren().size(); i++) {

            if(i < indexOn) {

                if (ledAnimationOnEh) {
                    timelineOffList.get(i).stop();
                    timelineDimList.get(i).stop();
                    timelineDimList.get(i).play();
                } else {
                    ledList.getChildren().get(i).setOpacity(Sunconfig.MINUTE_LED_DIM_OPACITY);
                }

            } else if (i > indexOn) {

                if (ledAnimationOnEh) {
                    timelineOffList.get(i).stop();
                    timelineDimList.get(i).stop();
                    timelineOffList.get(i).play();
                } else {
                    ledList.getChildren().get(i).setOpacity(Sunconfig.MINUTE_LED_OFF_OPACITY);
                }
            }
        }

        timelineOffList.get(indexOn).stop();
        timelineDimList.get(indexOn).stop();
        ledList.getChildren().get(indexOn).setOpacity(Sunconfig.MINUTE_LED_ON_OPACITY);
        ledOn.set(indexOn, true);
    }

    public void setCetusTime(
            ArrayList<ArrayList<GregorianCalendar>> cycleList,
            GregorianCalendar calendar,
            long timeZoneCorrection)
    {
        setKriegsrahmenZeit(KriegsrahmenZeit.Location.CETUS, cycleList, calendar, timeZoneCorrection);
    }

    public void setOrbVallisTime(
            ArrayList<ArrayList<GregorianCalendar>> cycleList,
            GregorianCalendar calendar,
            long timeZoneCorrection)
    {
        setKriegsrahmenZeit(KriegsrahmenZeit.Location.ORB_VALLIS, cycleList, calendar, timeZoneCorrection);
    }

    public void setKriegsrahmenZeit(
            KriegsrahmenZeit.Location location,
            ArrayList<ArrayList<GregorianCalendar>> cycleList,
            GregorianCalendar calendar,
            long timeZoneCorrection)
    {

        if (cycleList == null || cycleList.isEmpty()) { return; }

        ArrayList<Double> angleList;
        ArrayList<Line> lineList;
        ArrayList<Arc> arcList;
        ArrayList<DotMatrix> matrixList;

        switch (location) {
            case CETUS:
                angleList = cetusMarkerAngleList;
                lineList = cetusMarkerLineList;
                arcList = cetusMarkerArcList;
                matrixList = cetusTimeMatrixList;
                break;
            case ORB_VALLIS:
                angleList = orbVallisMarkerAngleList;
                lineList = orbVallisMarkerLineList;
                arcList = orbVallisMarkerArcList;
                matrixList = orbVallisTimeMatrixList;
                break;
            default: return;
        }

        int angleListSize = angleList.size();
        int cycleListSize = cycleList.size();

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

        for (int i = 0; i < cycleListSize; i++) {

            if ((i * 2) + 1 > angleListSize) { continue; }

            GregorianCalendar startTime = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
            startTime.setTimeInMillis(cycleList.get(i).get(0).getTimeInMillis() + timeZoneCorrection);

            GregorianCalendar endTime = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
            endTime.setTimeInMillis(cycleList.get(i).get(1).getTimeInMillis() + timeZoneCorrection);

            int startTimeDay = startTime.get(Calendar.DAY_OF_YEAR);
            int endTimeDay = endTime.get(Calendar.DAY_OF_YEAR);

            Line lineStart = lineList.get(i * 2);
            Line lineEnd = lineList.get((i * 2) + 1);

            Arc arc = arcList.get(i);

            if (startTimeDay != currentDay) {
                lineStart.setVisible(false);
                startTime = currentDayStart;
                if (endTimeDay != currentDay) {
                    endTime = currentDayStart;
                }
            } else {
                lineStart.setVisible(true);
            }

            if (endTimeDay != currentDay) {
                lineEnd.setVisible(false);
                endTime = currentDayEnd;
                if (startTimeDay != currentDay) {
                    startTime = currentDayEnd;
                }
            } else {
                lineEnd.setVisible(true);
            }

            double startAngle = getAbsoluteAngle(startTime);
            double endAngle = getAbsoluteAngle(endTime);

            angleList.set((i * 2), startAngle);
            angleList.set((i * 2) + 1, endAngle);

            DotMatrix matrixStart = matrixList.get(i * 2);
            DotMatrix matrixEnd = matrixList.get((i * 2) + 1);

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
            dialArcNight.setLength(0);
            dialArcDayLength.setLength(360);
        } else if (this.daylength <= 0) {
            horizonGroup.setVisible(false);
            dialArcNight.setVisible(true);
            dialArcNight.setLength(360);
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

        dialArcHalfNight.setStartAngle(90 - Sunutil.getNightCompressionAngle(90, nightCompression));
        dialArcHalfNight.setLength(-1 * (Sunutil.getNightCompressionAngle(270, nightCompression) - Sunutil.getNightCompressionAngle(90, nightCompression)));
    }

    public void setDialAngleLocalHour(double dialAngleLocalHour) {
        this.dialAngleLocalHour = Sunutil.getNightCompressionAngle(dialAngleLocalHour, nightCompression);
        dialRotateLocalHour.setAngle(this.dialAngleLocalHour);
        dialLocalHourSuperNiceArc.setEndAngle(this.dialAngleLocalHour);
    }

    public void updateDialMarkers() {

        updateHourArc(this.localTime);
        setHourMarkerLocalHour(this.localTime);

        int dialMarkerRotateListSize = dialMarkerRotateList.size();
        for (int i = 0; i < dialMarkerRotateListSize; i++) {

            dialMarkerRotateList.get(i).setAngle(Sunutil.getNightCompressionAngle(i * 360d / 96d, nightCompression));

/*
            if (i % 4 == 0) {
                int hourIndex = i / 4;
                double angle = dialMarkerRotateList.get(i).getAngle();
                double opacity = (hourIndex % 6 == 0) ? Sunconfig.LOCAL_HOUR_MARKER_OPACITY : Sunconfig.LOCAL_HOUR_MARKER_OFF_OPACITY;
                hourMarkerMatrixList.get(hourIndex).setRotate(-1 * angle);
                hourMarkerMatrixList.get(hourIndex).setStyle(Sunconfig.MATRIX_SHADOW);
                hourMarkerMatrixList.get(hourIndex).setOpacity(opacity);
            }
*/

            if (i % 4 == 0) {
                int hourIndex = i / 4;
                double angle = dialMarkerRotateList.get(i).getAngle();
                double opacity = Sunconfig.LOCAL_HOUR_MARKER_OFF_OPACITY;
                hourMarkerMatrixList.get(hourIndex).setRotate(-1 * angle);
                hourMarkerMatrixList.get(hourIndex).setStyle(Sunconfig.MATRIX_SHADOW);
                hourMarkerMatrixList.get(hourIndex).setOpacity(opacity);
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

        float glowRadiusStart = partialStart * 5.00f + 5.00f;           // 10.00 -> 5.00
        float glowStrengthStart = partialStart * 0.15f + 0.60f;         // 0.75 -> 0.60

        float glowRadiusEnd = partialEnd * 5.00f + 5.00f;               // 5.00 -> 10.00
        float glowStrengthEnd = partialEnd * 0.15f + 0.60f;             // 0.60 -> 0.75

/*
        double opacityStart = (hourIndexStart % 6 == 0) ?
                (partialStart * (1 - Sunconfig.LOCAL_HOUR_MARKER_OPACITY) + Sunconfig.LOCAL_HOUR_MARKER_OPACITY) * Sunconfig.LOCAL_HOUR_MARKER_ON_OPACITY :
                (partialStart * (1 - Sunconfig.LOCAL_HOUR_MARKER_OFF_OPACITY) + Sunconfig.LOCAL_HOUR_MARKER_OFF_OPACITY) * Sunconfig.LOCAL_HOUR_MARKER_OFF_ON_OPACITY;

        double opacityEnd = (hourIndexEnd % 6 == 0) ?
                (partialEnd * (1 - Sunconfig.LOCAL_HOUR_MARKER_OPACITY) + Sunconfig.LOCAL_HOUR_MARKER_OPACITY) * Sunconfig.LOCAL_HOUR_MARKER_ON_OPACITY :
                (partialEnd * (1 - Sunconfig.LOCAL_HOUR_MARKER_OFF_OPACITY) + Sunconfig.LOCAL_HOUR_MARKER_OFF_OPACITY) * Sunconfig.LOCAL_HOUR_MARKER_OFF_ON_OPACITY;
*/

        double opacityStart = (partialStart * (1 - Sunconfig.LOCAL_HOUR_MARKER_OFF_OPACITY) + Sunconfig.LOCAL_HOUR_MARKER_OFF_OPACITY) * Sunconfig.LOCAL_HOUR_MARKER_OFF_ON_OPACITY;
        double opacityEnd = (partialEnd * (1 - Sunconfig.LOCAL_HOUR_MARKER_OFF_OPACITY) + Sunconfig.LOCAL_HOUR_MARKER_OFF_OPACITY) * Sunconfig.LOCAL_HOUR_MARKER_OFF_ON_OPACITY;

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

        hourMarkerMatrixList.get(hourIndexStart).setOpacity(opacityStart);
        hourMarkerMatrixList.get(hourIndexEnd).setOpacity(opacityEnd);

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

        int orbVallisMarkerRotateListSize = orbVallisMarkerRotateList.size();
        for (int i = 0; i < orbVallisMarkerRotateListSize; i++) {
            orbVallisMarkerRotateList.get(i).setAngle(Sunutil.getNightCompressionAngle(orbVallisMarkerAngleList.get(i), nightCompression));
        }

        int orbVallisMarkerArcListSize = orbVallisMarkerArcList.size();
        for  (int i = 0; i < orbVallisMarkerArcListSize; i++) {

            double startAngle = orbVallisMarkerAngleList.get(i * 2);
            double endAngle = orbVallisMarkerAngleList.get(i * 2 + 1);

            double adjustedStartAngle = Sunutil.getNightCompressionAngle(startAngle, nightCompression);
            double adjustedEndAngle = Sunutil.getNightCompressionAngle(endAngle, nightCompression);

            double length = adjustedStartAngle - adjustedEndAngle;
            if (length > 0) { length = -1 * ((360 - adjustedStartAngle) + adjustedEndAngle); }

            orbVallisMarkerArcList.get(i).setStartAngle(90 - adjustedStartAngle);
            orbVallisMarkerArcList.get(i).setLength(length);
        }
    }

    private void updateHourArc(GregorianCalendar gregorianCalendar) {

        double startAngle = ((gregorianCalendar.get(Calendar.HOUR_OF_DAY) + 12) % 24) * 15;
        double midAngle = startAngle + (gregorianCalendar.get(Calendar.MINUTE) / 60d) * 15;
        double endAngle = startAngle + 15;

        double adjustedStartAngle = Sunutil.getNightCompressionAngle(startAngle, nightCompression);
        double adjustedMidAngle = Sunutil.getNightCompressionAngle(midAngle, nightCompression);
        double adjustedEndAngle = Sunutil.getNightCompressionAngle(endAngle, nightCompression);

        double lengthPast = adjustedStartAngle - adjustedMidAngle;
        if (lengthPast > 0) { lengthPast = -1 * ((360 - adjustedStartAngle) + adjustedMidAngle); }

        double lengthFuture = adjustedMidAngle - adjustedEndAngle;
        if (lengthFuture > 0) { lengthFuture = -1 * ((360 - adjustedMidAngle) + adjustedEndAngle); }

        dialLocalHourArcPast.setStartAngle(90 - adjustedStartAngle);
        dialLocalHourArcPast.setLength(lengthPast);

        dialLocalHourArcFuture.setStartAngle(90 - adjustedMidAngle);
        dialLocalHourArcFuture.setLength(lengthFuture);
    }

    public void setCustomTimeWarning(boolean warning) {

        this.timeWarning = warning;

        matrixHour.setFill(this.timeWarning ? Color.YELLOW : Color.WHITE);
        matrixMinute.setFill(this.timeWarning ? Color.YELLOW : Color.WHITE);
        matrixDay.setFill(this.timeWarning ? Color.YELLOW : Color.WHITE);
        matrixMonth.setFill(this.timeWarning ? Color.YELLOW : Color.WHITE);
        matrixYear.setFill(this.timeWarning ? Color.YELLOW : Color.WHITE);
    }

    public void setCustomTimezoneWarning(boolean warning) {

        this.timezoneWarning = warning;

        matrixTimeZone.setFill(this.timezoneWarning ? Color.YELLOW : Color.WHITE);
    }

    public void setCustomLongitudeWarning(boolean warning) {

        this.longitudeWarning = warning;

        matrixLongitude.setFill(this.longitudeWarning ? Color.YELLOW : Color.WHITE);
    }

    public void setCustomLatitudeWarning(boolean warning) {

        this.latitudeWarning = warning;

        matrixLatitude.setFill(this.latitudeWarning ? Color.YELLOW : Color.WHITE);
    }

    public void setGroupGlow(Group group, String style) {
        if (group == null || style == null) { return; }
        group.setStyle(style);
    }

    public void toggleGlobeVisibility() {
        globeVisibleEh = !globeVisibleEh;
        setGlobeVisibility(globeVisibleEh);
    }

    public void setGlobeVisibility(boolean visibleEh) {

        if (visibleEh) {
//            masterCoordinatesGroup.setVisible(true);
//            globeMasterGroup.setVisible(true);
        }

        int animationRate = globeAnimationEh ? 1 : Sunconfig.TINY_GLOBE_DURATION;

        MorphingPolygon dialLocalHourMorphingPolygon = (MorphingPolygon) dialLocalHourGroup.getChildren().get(0);
        MorphingPolygon dialHighNoonMorphingPolygon = (MorphingPolygon) dialHighNoonGroup.getChildren().get(1);

        MorphingPolygon dialSunriseMorphingPolygon = (MorphingPolygon) ((Group) horizonGroup.getChildren().get(0)).getChildren().get(0);
        MorphingPolygon dialSunsetMorphingPolygon = (MorphingPolygon) ((Group) horizonGroup.getChildren().get(1)).getChildren().get(0);

//        tinyGlobeMoveOutTimeline.setRate(animationRate);
//        tinyGlobeMoveInTimeline.setRate(animationRate);
        timeAndDateMoveOutTimeline.setRate(animationRate);
        timeAndDateMoveInTimeline.setRate(animationRate);
        coordinatesMoveOutTimeline.setRate(animationRate);
        coordinatesMoveInTimeline.setRate(animationRate);
        globeMoveOutTimeline.setRate(animationRate);
        globeMoveInTimeline.setRate(animationRate);
//        horizonMoveOutTimeline.setRate(animationRate);
//        horizonMoveInTimeline.setRate(animationRate);
        dialSunriseMorphingPolygon.setRate(animationRate);
        dialSunsetMorphingPolygon.setRate(animationRate);
        dialLocalHourMorphingPolygon.setRate(animationRate);
        dialHighNoonMorphingPolygon.setRate(animationRate);
//        highNoonMoveOutTimeline.setRate(animationRate);
//        highNoonMoveInTimeline.setRate(animationRate);
//        dialLocalHourSuperNiceArcOutTimeline.setRate(animationRate);
//        dialLocalHourSuperNiceArcInTimeline.setRate(animationRate);

//        tinyGlobeMoveOutTimeline.stop();
//        tinyGlobeMoveInTimeline.stop();
        timeAndDateMoveOutTimeline.stop();
        timeAndDateMoveInTimeline.stop();
        coordinatesMoveOutTimeline.stop();
        coordinatesMoveInTimeline.stop();
        globeMoveOutTimeline.stop();
        globeMoveInTimeline.stop();
//        horizonMoveOutTimeline.stop();
//        horizonMoveInTimeline.stop();
        dialSunriseMorphingPolygon.stop();
        dialSunsetMorphingPolygon.stop();
        dialLocalHourMorphingPolygon.stopOut();
        dialLocalHourMorphingPolygon.stopIn();
        dialHighNoonMorphingPolygon.stopOut();
        dialHighNoonMorphingPolygon.stopIn();
//        highNoonMoveOutTimeline.stop();
//        highNoonMoveInTimeline.stop();
//        dialLocalHourSuperNiceArcOutTimeline.stop();
//        dialLocalHourSuperNiceArcInTimeline.stop();

        if (visibleEh) {
//            tinyGlobeMoveOutTimeline.play();
            timeAndDateMoveOutTimeline.play();
            coordinatesMoveOutTimeline.play();
            globeMoveOutTimeline.play();
//            horizonMoveOutTimeline.play();
            dialSunriseMorphingPolygon.playOut();
            dialSunsetMorphingPolygon.playOut();
//            dialLocalHourMorphingPolygon.playOut();
            dialHighNoonMorphingPolygon.playOut();
//            highNoonMoveOutTimeline.play();
//            dialLocalHourSuperNiceArcOutTimeline.play();
        }
        else {
//            tinyGlobeMoveInTimeline.play();
            timeAndDateMoveInTimeline.play();
            coordinatesMoveInTimeline.play();
            globeMoveInTimeline.play();
//            horizonMoveInTimeline.play();
            dialSunriseMorphingPolygon.playIn();
            dialSunsetMorphingPolygon.playIn();
//            dialLocalHourMorphingPolygon.playIn();
            dialHighNoonMorphingPolygon.playIn();
//            highNoonMoveInTimeline.play();
//            dialLocalHourSuperNiceArcInTimeline.play();
        }

        int i = 0;
        for (Node hourMarkerGroup : dialHourLineMarkerGroupA.getChildren()) {
            if (hourMarkerGroup instanceof Group) {
                for (Node markerPoly : ((Group) hourMarkerGroup).getChildren()) {
                    if (markerPoly instanceof Polygon) {
                        ((Polygon) markerPoly).setFill(visibleEh ? Color.WHITE : Color.BLACK);
                    }
                    if (markerPoly instanceof Line && (i % 4 != 0)) {
                        ((Line) markerPoly).setStroke(visibleEh ? Color.WHITE : Color.BLACK);
                    }
                }
            }
            i++;
        }

    }

    public void setCoordinates(double longitude, double latitude) {
        matrixLongitude.setString(Sunutil.formatCoordinateToString(longitude, "E", "W"));
        matrixLatitude.setString(Sunutil.formatCoordinateToString(latitude, "N", "S"));
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

    public void setKriegsrahmenTimeVisibility(KriegsrahmenZeit.Location location, boolean visibleEh) {

        switch (location) {
            case CETUS:
                cetusTimeVisibleEh = visibleEh;
                cetusMarkerGroup.setVisible(visibleEh);
                cetusTimer.setVisible(visibleEh);
                break;
            case ORB_VALLIS:
                orbVallisTimeVisibleEh = visibleEh;
                orbVallisMarkerGroup.setVisible(visibleEh);
                orbVallisTimer.setVisible(visibleEh);
                break;
            default: {}
        }
    }

    public void setCetusTimeVisibility(boolean visibleEh) {
        setKriegsrahmenTimeVisibility(KriegsrahmenZeit.Location.CETUS, visibleEh);
    }

    public void setOrbVallisTimeVisibility(boolean visibleEh) {
        setKriegsrahmenTimeVisibility(KriegsrahmenZeit.Location.ORB_VALLIS, visibleEh);
    }

    public void setTimeDisplayOpacity(double opacity) {

        masterTimeGroup.setOpacity(opacity);

        if (opacity < 0.5) {
            masterTimeGroup.setMouseTransparent(true);
        } else {
            masterTimeGroup.setMouseTransparent(false);
        }
    }

    public void setTimeZone(TimeZone timeZone) {

        long timeZoneOffset = timeZone.getOffset(localTime.getTimeInMillis());

        timeZoneOffset = (int) Sunutil.rotateTimeZoneOffset(timeZoneOffset);

        String timeZoneNumberString = "00" + abs(timeZoneOffset / (1000 * 60 * 60));
        timeZoneNumberString = timeZoneNumberString.substring(timeZoneNumberString.length() - 2);

        StringBuilder timeZoneString = new StringBuilder()
                .append("UTC")
                .append((timeZoneOffset < 0) ? "-" : "+")
                .append(timeZoneNumberString)
                ;

        matrixTimeZone.setString(timeZoneString.toString());
    }

    public void toggleAnimation() {
        animationProperty.set(!animationProperty.get());
        changeAnimation(animationProperty.get());
    }

    public void togglePinInfo() {
        pinInfoProperty.set(!pinInfoProperty.get());
    }

    public void changeAnimation(boolean animation) {

        ledAnimationOnEh = animation;
        globeAnimationEh = animation;

        for (Timeline timeline : cetusMarkerHoverTransitionList) {
            if (ledAnimationOnEh) {
                timeline.setRate(1);
            } else {
                timeline.setRate(Sunconfig.CETUS_MARKER_DURATION);
            }
        }

        for (Timeline timeline : orbVallisMarkerHoverTransitionList) {
            if (ledAnimationOnEh) {
                timeline.setRate(1);
            } else {
                timeline.setRate(Sunconfig.ORBVALLIS_MARKER_DURATION);
            }
        }
    }

    public void toggleHelp() {

        helpEh = !helpEh;

        helpOverlay.setVisible(helpEh);
        helpTextGroup.setVisible(helpEh);

        controlThingyHelp.toggleState();
    }

    public void toggleNightmode() {

        nightmodeEh = !nightmodeEh;

        nightModeOverlay.setVisible(nightmodeEh);
        tinyGlobeNightModeOverlay.setVisible(nightmodeEh);

        for (Node node : outerControlsGroup.getChildren()) {
            ((ControlThingy) node).toggleNightMode();
        }

        controlThingyNightmode.toggleState();
    }

    public void toggleGlobeGrid() {
        globeGridVisibleEh.setValue(!globeGridVisibleEh.get());
        controlThingyGlobeGrid.toggleState();
    }

    public void toggleGlobeLines() {
        globeLinesVisibleEh.setValue(!globeLinesVisibleEh.get());
        controlThingyGlobeLines.toggleState();
    }

    public void toggleSunHighNoon() {

        sunHighNoonVisibleEh = !sunHighNoonVisibleEh;

        matrixDayLength.setVisible(!sunHighNoonVisibleEh);
//        dialArcDayLength.setVisible(!sunHighNoonVisibleEh);

        sunHighNoon.setVisible(sunHighNoonVisibleEh);
    }

    public void fadeOutInfoText(long millis) {

        if (infoTextOpacityTimeline.getStatus().equals(Animation.Status.RUNNING)) {
            infoTextOpacityTimeline.stop();
        }

        infoTextOpacityTimeline.getKeyFrames().clear();

        KeyValue keyValue = new KeyValue(infoTextGroup.opacityProperty(), 0, Interpolator.LINEAR);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(millis), keyValue);

        infoTextOpacityTimeline.getKeyFrames().addAll(keyFrame);
        infoTextOpacityTimeline.setOnFinished(event -> infoTextGroup.setVisible(false));

        infoTextOpacityTimeline.play();
    }


    // Getterers
    public boolean getKriegsrahmenZeitVisibleEh(KriegsrahmenZeit.Location location) {
        switch (location) {
            case CETUS: return getCetusTimeVisibleEh();
            case ORB_VALLIS: return getOrbVallisTimeVisibleEh();
            default: return false;
        }
    }

    public Group getCetusMarkerGroup() {
        return cetusMarkerGroup;
    }

    public boolean getCetusTimeVisibleEh() {
        return cetusTimeVisibleEh;
    }

    public boolean getOrbVallisTimeVisibleEh() {
        return orbVallisTimeVisibleEh;
    }

    public boolean getGlobeVisibleEh() {
        return globeVisibleEh;
    }

    public boolean getLedAnimationOnEh() {
        return ledAnimationOnEh;
    }

    public boolean getGlobeAnimationEh() {
        return globeAnimationEh;
    }

    public boolean getHelpEh() {
        return helpEh;
    }

    public boolean getNightmodeEh() {
        return nightmodeEh;
    }

    public boolean getSunHighNoonVisibleEh() {
        return sunHighNoonVisibleEh;
    }

    public Group getDialsGroup() {
        return dialsGroup;
    }

    public Group getControlNightCompression() {
        return controlNightCompression;
    }

    public Circle getDialCircleFrame() {
        return dialCircleFrame;
    }

    public Arc getDialArcNight() {
        return dialArcNight;
    }

    public Arc getDialArcHalfNight() {
        return dialArcHalfNight;
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

    public DotMatrix getMatrixDayLength() {
        return matrixDayLength;
    }

    public DotMatrix getMatrixLongitude() {
        return matrixLongitude;
    }

    public DotMatrix getMatrixLatitude() {
        return matrixLatitude;
    }

    public ControlThingy getControlThingyResize() {
        return controlThingyResize;
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

    public ControlThingy getControlThingyAnimation() {
        return controlThingyAnimation;
    }

    public ControlThingy getControlThingyPinInfo() {
        return controlThingyPinInfo;
    }

    public ControlThingy getControlThingyChart() {
        return controlThingyChart;
    }

    public ControlThingy getControlThingyCetus() {
        return controlThingyCetus;
    }

    public ControlThingy getControlThingyOrbVallis() {
        return controlThingyOrbVallis;
    }

    public ControlThingy getControlThingyDst() {
        return controlThingyDst;
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

    public ControlThingy getControlThingyGlobeLines() {
        return controlThingyGlobeLines;
    }

    public double getGlobeLightScaler() {
        return globeLightScaler.get();
    }

    public DoubleProperty globeLightScalerProperty() {
        return globeLightScaler;
    }

    public Timeline getInfoTextOpacityTimeline() {
        return infoTextOpacityTimeline;
    }

    public Group getMiroTextGroup() {
        return miroTextGroup;
    }

    public Group getOuterControlsGroup() {
        return this.outerControlsGroup;
    }
}
