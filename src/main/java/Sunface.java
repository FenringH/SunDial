import javafx.animation.*;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.geometry.Rectangle2D;
import javafx.scene.*;
import javafx.scene.Cursor;
import javafx.scene.chart.LineChart;
import javafx.scene.control.TextArea;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Scale;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Math.*;

public class Sunface extends Application {

    private final static String A_BEGINNING =
            "A beginning is a very delicate time." +
            "Know then, that is is the year 10191. The known universe is ruled by the Padishah Emperor Shaddam the Fourth, my father. " +
            "In this time, the most precious substance in the universe is the spice Melange. " +
            "The spice extends life. The spice expands consciousness. " +
            "A product of the Spice, the red Sapho juice, stains the lips of the Mentats but allows them to be human computers, " +
            "as thinking machines have been outlawed. The spice is vital to space travel. " +
            "The Spacing Guild and its navigators, who the spice has mutated over 4000 years, use the orange spice gas, " +
            "which gives them the ability to fold space. That is, travel to any part of the universe without moving. " +
            "Because the Guild controls all interplanetary travel, they are the highest power in the Universe. " +
            "The Spice also plays a very secret role in the Bene Gesserit sisterhood, of which I am a part. " +
            "The sisterhood has been interfering with the marriages, and the children thereof, " +
            "of the great Houses of the Universe, cleverly intermixing one bloodline with another to form the Kwisatz Haderach, a super being. " +
            "They plan to control this super being and use his powers for their own selfish purposes. " +
            "The breeding plan has been carried out in a strict manner for 90 generations. " +
            "The goal of the super being is in sight.";

    private static final double DEFAULT_FPS = 30.0;
    private static final double DEFAULT_LONGITUDE = round(Suntime.DEFAULT_LONGITUDE * 100d) / 100d;
    private static final double DEFAULT_LATITUDE = round(Suntime.DEFAULT_LATITUDE * 100d) / 100d;

    private static final double MIN_WIDTH = 150;
    private static final double MIN_HEIGHT = 150;

    private static final int OFFSET_BY_YEAR = Calendar.YEAR;
    private static final int OFFSET_BY_MONTH = Calendar.MONTH;
    private static final int OFFSET_BY_DAY = Calendar.DAY_OF_MONTH;
    private static final int OFFSET_BY_HOUR = Calendar.HOUR_OF_DAY;
    private static final int OFFSET_BY_MINUTE = Calendar.MINUTE;
    private static final int OFFSET_BY_SECOND = Calendar.SECOND;
    private static final int OFFSET_BY_WEEK = Calendar.WEEK_OF_YEAR;

    private static final double NORMAL_STEP_SIZE = 20.0d;
    private static final double FAST_STEP_SIZE = 2.0d;

    public static final String BUTTON_SHADOW  = "-fx-effect: dropshadow(three-pass-box, rgba(  0,  0,  0, 1.0), 5.0, 0.50, 0, 0);";
    public static final String BUTTON_GLOW    = "-fx-effect: dropshadow(three-pass-box, rgba(255,128, 32, 0.5), 5.0, 0.50, 0, 0);";

    private static final Color Color_Of_Window = new Color(0.65, 0.85, 0.85, 1.00);
    private static final Font Font_Of_Info = new Font("Lucida Console", 14);

    private static final DecimalFormat julianDateFormat = new DecimalFormat("###,###,###.00000000");

    private static final String GOOGLEMAPS_REGEX = ".*\\/@([\\+\\-0-9]+\\.[0-9]*),([\\+\\-0-9]+\\.[0-9]*),.*";

    private int fpsSetting = (int) floor(1000 / DEFAULT_FPS);

    private GregorianCalendar currentLocalTime;
    private GregorianCalendar offsetLocalTime;
    private double longitude = DEFAULT_LONGITUDE;
    private double latitude = DEFAULT_LATITUDE;
    private double customLongitude = DEFAULT_LONGITUDE;
    private double customLatitude = DEFAULT_LATITUDE;

    private enum Position {LONGITUDE, LATITUDE, BOTH, GOOGLE_MAPS};

    private double deltaX;
    private double deltaY;
    private double savedMouseX;
    private double savedMouseY;
    private int dX;
    private int dY;
    private double offsetX;
    private double offsetY;
    private double savedWindowPositionX;
    private double savedWindowPositionY;
    private double savedWindowSizeX;
    private double savedWindowSizeY;
    private double savedLongitude = longitude;
    private double savedLatitude = latitude;
    private boolean maximizedEh = false;

    private TextArea debugTextArea;
    private String debugErrorMessage;

    private Suntime suntimeLocal;
    private Suntime suntimeGlobal;

    private TimeZone currentTimezone;
    private TimeZone offsetTimezone;
    private int timeZoneOffset;

    private Cetustime cetustime;
    private Sunchart sunchart;

    private Stage debugWindow;
    private Stage statsWindow;

    private ArrayList<ArrayList<GregorianCalendar>> cetusNightList;

    private ArrayList<MouseButton> mouseButtonList = new ArrayList<>();


    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) {

        // Init time
        currentLocalTime = new GregorianCalendar();
        offsetLocalTime = new GregorianCalendar();

        currentTimezone = currentLocalTime.getTimeZone();
        offsetTimezone = offsetLocalTime.getTimeZone();

        timeZoneOffset = currentLocalTime.getTimeZone().getRawOffset();

        // Create 'sun' objects
        suntimeLocal = new Suntime.Builder()
                .localTime(currentLocalTime)
                .observerLongitude(longitude)
                .observerLatitude(latitude)
                .build();

        suntimeGlobal = new Suntime.Builder()
                .localTime(currentLocalTime)
                .observerLongitude(longitude)
                .observerLatitude(latitude)
                .build();

        Sundial sundial = new Sundial.Builder()
                .nightCompression(0)
                .build();

        sundial.getDayGlobe().rotateGlobe(longitude, latitude, 0);
        sundial.getNightGlobe().rotateGlobe(longitude, latitude, 0);
        sundial.getDayTerminatorLine().rotateRing(longitude, latitude, 0);
        sundial.getDayTerminatorGlow().rotateRing(longitude, latitude, 0);
        sundial.getTinyGlobe().rotateGlobe(longitude, latitude, 0);

        cetustime = new Cetustime();
        cetusNightList = cetustime.getNightList(currentLocalTime);

        sunchart = new Sunchart(longitude, latitude, currentLocalTime.get(Calendar.YEAR));


        // Scene
        Group dialsGroup = sundial.getDialsGroup();

        Group rootGroup = new Group();
        rootGroup.getChildren().add(dialsGroup);

        Scene mainScene = new Scene(rootGroup, rootGroup.getLayoutBounds().getWidth(), rootGroup.getLayoutBounds().getHeight(), true, SceneAntialiasing.DISABLED);
        mainScene.setFill(Color.TRANSPARENT);


        // Setup dialsGroup scale transform and bind to primaryStage size
        Scale dialsScale = new Scale();
        dialsGroup.getTransforms().add(dialsScale);

        dialsScale.xProperty().bind(Bindings.createDoubleBinding(() ->
                (primaryStage.widthProperty().get() / Sundial.DIAL_WIDTH), primaryStage.widthProperty()));

        dialsScale.yProperty().bind(Bindings.createDoubleBinding(() ->
                (primaryStage.heightProperty().get() / Sundial.DIAL_HEIGHT), primaryStage.heightProperty()));

        dialsScale.zProperty().bind(Bindings.createDoubleBinding(() -> {
            // For Z scale pick smaller value between width and height
            double stageWidth = primaryStage.widthProperty().get();
            double stageHeight = primaryStage.heightProperty().get();
            double stageSize = (stageWidth > stageHeight) ? stageHeight : stageWidth;
            double dialsSize = (Sundial.DIAL_WIDTH > Sundial.DIAL_HEIGHT) ? Sundial.DIAL_HEIGHT : Sundial.DIAL_WIDTH;
            return stageSize / dialsSize;
        }, primaryStage.widthProperty(), primaryStage.heightProperty()));


        // Debug window
        debugTextArea = new TextArea();
        debugTextArea.setMinWidth(600);
        debugTextArea.setMinHeight(800);
        debugTextArea.setEditable(false);
        debugTextArea.setWrapText(true);
        debugTextArea.setText(A_BEGINNING);

        Group debugGroup = new Group();
        debugGroup.getChildren().add(debugTextArea);

        Scene debugScene = new Scene(debugGroup, debugGroup.getLayoutBounds().getWidth(), debugGroup.getLayoutBounds().getHeight());

        debugWindow = new Stage();
        debugWindow.setTitle("Debug window");
        debugWindow.setScene(debugScene);
        debugWindow.setWidth(debugScene.getWidth());
        debugWindow.setHeight(debugScene.getHeight());
        debugWindow.setX(0);
        debugWindow.setY(0);
        debugWindow.setResizable(true);


        // Chart window
        LineChart lineChart = sunchart.getChart();
        Scene chartScene = new Scene(lineChart, 800, 600);

        statsWindow = new Stage();
        statsWindow.setTitle("Sunchart");
        statsWindow.setScene(chartScene);
        statsWindow.setX(0);
        statsWindow.setY(0);
        statsWindow.setWidth(chartScene.getWidth());
        statsWindow.setHeight(chartScene.getHeight());


        // Primary window
        primaryStage.setTitle("Sunface");
        primaryStage.setScene(mainScene);
        primaryStage.setMinWidth(MIN_WIDTH);
        primaryStage.setMinHeight(MIN_HEIGHT);
        primaryStage.initStyle(StageStyle.TRANSPARENT);


        // Playtime
        KeyFrame keyframeClockTick = new KeyFrame(
                Duration.millis(fpsSetting),
                event -> updateCurrentTime(sundial));

        Timeline timeline = new Timeline(keyframeClockTick);
        timeline.setCycleCount(Animation.INDEFINITE);


        // Events
        sundial.getControlThingyHelp().setOnMouseClicked(event -> sundial.toggleHelp());
        sundial.getControlThingyClose().setOnMouseClicked(event -> System.exit(0));
        sundial.getControlThingyMaximize().setOnMouseClicked(event -> toggleMaximizeWindow(primaryStage, event));
        sundial.getControlThingyMinimize().setOnMousePressed(event -> mouseButtonList.add(event.getButton()));
        sundial.getControlThingyMinimize().setOnMouseReleased(event -> { minimizeWindow(primaryStage, timeline, event); mouseButtonList.clear(); });
        sundial.getControlThingyNightmode().setOnMouseClicked(event -> sundial.toggleNightmode());

        sundial.getMatrixTimeZone().setOnMousePressed(event -> {
            mouseButtonList.add(event.getButton());
            changeTimeZone(sundial, event);
        });
        sundial.getMatrixTimeZone().setOnMouseReleased(event -> mouseButtonList.clear());
        sundial.getMatrixTimeZone().setOnMouseDragged(event -> changeTimeZone(sundial, event));
        sundial.getMatrixTimeZone().setOnScroll(event -> changeTimeZone(sundial, event));

        sundial.getControlNightCompression().setOnMousePressed(event -> recordNightCompressionPosition(sundial, event));
        sundial.getControlNightCompression().setOnMouseReleased(event -> mouseButtonList.clear());
        sundial.getControlNightCompression().setOnMouseDragged(event -> changeNightCompression(sundial, event));
        sundial.getControlNightCompression().setOnScroll(event -> changeNightCompression(sundial, event));

        sundial.getTinyGlobeGroup().setOnMousePressed(event -> mouseButtonList.add(event.getButton()));
        sundial.getTinyGlobeGroup().setOnMouseClicked(event -> toggleGlobe(primaryStage, sundial, dialsGroup));

        sundial.getTinyGlobeGroup().setOnDragOver(event -> {
            if (event.getGestureSource() != sundial.getTinyGlobeGroup() && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });
        sundial.getTinyGlobeGroup().setOnDragDropped(event -> rotateGlobe(sundial, Position.GOOGLE_MAPS, event));

        sundial.getMatrixLongitude().setOnMousePressed(event -> recordGlobePosition(sundial, Position.LONGITUDE, event));
        sundial.getMatrixLongitude().setOnMouseReleased(event -> mouseButtonList.clear());
        sundial.getMatrixLongitude().setOnMouseDragged(event -> rotateGlobe(sundial, Position.LONGITUDE, event));
        sundial.getMatrixLongitude().setOnScroll(event -> rotateGlobe(sundial, Position.LONGITUDE, event));

        sundial.getMatrixLongitude().setOnMousePressed(event -> recordGlobePosition(sundial, Position.LATITUDE, event));
        sundial.getMatrixLongitude().setOnMouseReleased(event -> mouseButtonList.clear());
        sundial.getMatrixLongitude().setOnMouseDragged(event -> rotateGlobe(sundial, Position.LATITUDE, event));
        sundial.getMatrixLongitude().setOnScroll(event -> rotateGlobe(sundial, Position.LATITUDE, event));

        sundial.getControlThingyResize().setOnMousePressed(event -> recordWindowSize(primaryStage, dialsGroup, event));
        sundial.getControlThingyResize().setOnMouseReleased(event -> mouseButtonList.clear());
        sundial.getControlThingyResize().setOnMouseDragged(event -> resizeWindow(primaryStage, sundial, event));

        sundial.getDialMarginCircle().setOnMousePressed(event -> recordWindowPosition(primaryStage, dialsGroup, event));
        sundial.getDialMarginCircle().setOnMouseReleased(event -> mouseButtonList.clear());
        sundial.getDialMarginCircle().setOnMouseDragged(event -> changeWindowPosition(primaryStage, event));

        sundial.getDialCircleFrame().setOnMousePressed(event -> recordWindowPosition(primaryStage, dialsGroup, event));
        sundial.getDialCircleFrame().setOnMouseReleased(event -> mouseButtonList.clear());
        sundial.getDialCircleFrame().setOnMouseDragged(event -> changeWindowPosition(primaryStage, event));

        sundial.getMatrixYear().setOnMousePressed(event -> recordCalendarPosition(sundial, event));
        sundial.getMatrixYear().setOnMouseReleased(event -> mouseButtonList.clear());
        sundial.getMatrixYear().setOnMouseDragged(event -> offsetTime(sundial, OFFSET_BY_YEAR, event));
        sundial.getMatrixYear().setOnScroll(event -> offsetTime(sundial, OFFSET_BY_YEAR, event));

        sundial.getMatrixMonth().setOnMousePressed(event -> recordCalendarPosition(sundial, event));
        sundial.getMatrixMonth().setOnMouseReleased(event -> mouseButtonList.clear());
        sundial.getMatrixMonth().setOnMouseDragged(event -> offsetTime(sundial, OFFSET_BY_MONTH, event));
        sundial.getMatrixMonth().setOnScroll(event -> offsetTime(sundial, OFFSET_BY_MONTH, event));

        sundial.getMatrixDay().setOnMousePressed(event -> recordCalendarPosition(sundial, event));
        sundial.getMatrixDay().setOnMouseReleased(event -> mouseButtonList.clear());
        sundial.getMatrixDay().setOnMouseDragged(event -> offsetTime(sundial, OFFSET_BY_DAY, event));
        sundial.getMatrixDay().setOnScroll(event -> offsetTime(sundial, OFFSET_BY_DAY, event));

        sundial.getMatrixHour().setOnMousePressed(event -> recordCalendarPosition(sundial, event));
        sundial.getMatrixHour().setOnMouseReleased(event -> mouseButtonList.clear());
        sundial.getMatrixHour().setOnMouseDragged(event -> offsetTime(sundial, OFFSET_BY_HOUR, event));
        sundial.getMatrixHour().setOnScroll(event -> offsetTime(sundial, OFFSET_BY_HOUR, event));

        sundial.getMatrixMinute().setOnMousePressed(event -> recordCalendarPosition(sundial, event));
        sundial.getMatrixMinute().setOnMouseReleased(event -> mouseButtonList.clear());
        sundial.getMatrixMinute().setOnMouseDragged(event -> offsetTime(sundial, OFFSET_BY_MINUTE, event));
        sundial.getMatrixMinute().setOnScroll(event -> offsetTime(sundial, OFFSET_BY_MINUTE, event));

        sundial.getMatrixWeek().setOnMousePressed(event -> recordCalendarPosition(sundial, event));
        sundial.getMatrixWeek().setOnMouseReleased(event -> mouseButtonList.clear());
        sundial.getMatrixWeek().setOnMouseDragged(event -> offsetTime(sundial, OFFSET_BY_WEEK, event));
        sundial.getMatrixWeek().setOnScroll(event -> offsetTime(sundial, OFFSET_BY_WEEK, event));

        sundial.getDialHighNoonGroup().setOnMouseClicked(event -> sundial.toggleAnimation());

        sundial.getHorizonGroup().setOnMouseClicked(event -> {
            if (statsWindow.isShowing()) { statsWindow.close(); }
            else { statsWindow.show(); }
        });

        primaryStage.setOnHidden(event -> timeline.pause());
        primaryStage.setOnShown(event -> timeline.play());

        // Showtime
        initCurrentTime(sundial);
        primaryStage.show();
        timeline.play();
        recordWindowPosition(primaryStage, dialsGroup, null);

    }

    // Methods
    private void resetTime(Sundial sundial) {
        offsetLocalTime.set(
                currentLocalTime.get(Calendar.YEAR),
                currentLocalTime.get(Calendar.MONTH),
                currentLocalTime.get(Calendar.DAY_OF_MONTH),
                currentLocalTime.get(Calendar.HOUR_OF_DAY),
                currentLocalTime.get(Calendar.MINUTE),
                currentLocalTime.get(Calendar.SECOND)
        );
        sundial.setDialFrameWarning(false);
        initCurrentTime(sundial);
    }

    private void offsetTime(Sundial sundial, int offset, ScrollEvent event) {

        if (suntimeLocal == null || sundial == null || event == null) { return; }

        if (!mouseButtonList.isEmpty()) { return; }

        int offsetFactor = 0;
        int offsetYear = 0;
        int offsetMonth = 0;
        int offsetDay = 0;
        int offsetHour = 0;
        int offsetMinute = 0;
        int offsetSecond = 0;
        int offsetWeek = 0;

        if (event.getDeltaY() < 0) { offsetFactor = -1; }
        else if (event.getDeltaY() > 0) { offsetFactor = 1; }

        switch (offset) {
            case OFFSET_BY_YEAR   : offsetYear = offsetFactor; break;
            case OFFSET_BY_MONTH  : offsetMonth = offsetFactor; break;
            case OFFSET_BY_DAY    : offsetDay = offsetFactor; break;
            case OFFSET_BY_HOUR   : offsetHour = offsetFactor; break;
            case OFFSET_BY_MINUTE : offsetMinute = offsetFactor; break;
            case OFFSET_BY_SECOND : offsetSecond = offsetFactor; break;
            case OFFSET_BY_WEEK   : offsetWeek = offsetFactor; break;
            default: {}
        }

        offsetLocalTime.set(
                offsetLocalTime.get(Calendar.YEAR) + offsetYear,
                offsetLocalTime.get(Calendar.MONTH) + offsetMonth,
                offsetLocalTime.get(Calendar.DAY_OF_MONTH) + offsetDay,
                offsetLocalTime.get(Calendar.HOUR_OF_DAY) + offsetHour,
                offsetLocalTime.get(Calendar.MINUTE) + offsetMinute,
                offsetLocalTime.get(Calendar.SECOND) + offsetSecond
        );

        offsetLocalTime.setTimeInMillis(offsetLocalTime.getTimeInMillis() + offsetWeek * 7 * 24 * 60 * 60 * 1000);

        if (offsetLocalTime.equals(currentLocalTime)) { sundial.setDialFrameWarning(false); }
        else { sundial.setDialFrameWarning(true); }

        initCurrentTime(sundial);
    }

    private void offsetTime(Sundial sundial, int offsetType, MouseEvent event) {

        if (sundial == null || event == null) { return; }

        MouseButton mouseButton = mouseButtonList.get(mouseButtonList.size() - 1);
        if (mouseButton == null || mouseButton.equals(MouseButton.MIDDLE)) { return; }

        double mouseX = event.getScreenX();
        double mouseY = event.getScreenY();

        double deltaMouseX = mouseX - savedMouseX;
        double deltaMouseY = mouseY - savedMouseY;

        int offsetFactor = 0;
        int offsetYear = 0;
        int offsetMonth = 0;
        int offsetDay = 0;
        int offsetHour = 0;
        int offsetMinute = 0;
        int offsetSecond = 0;
        int offsetWeek = 0;

        double stepSize = NORMAL_STEP_SIZE;
        if (event.isSecondaryButtonDown()) { stepSize = FAST_STEP_SIZE; }

        if (deltaMouseY >= stepSize) {
            offsetFactor = -1;
            savedMouseY = mouseY;
        } else if (deltaMouseY <= -1 * stepSize) {
            offsetFactor = 1;
            savedMouseY = mouseY;
        } else {
            return;
        }

        switch (offsetType) {
            case OFFSET_BY_YEAR   : offsetYear = offsetFactor; break;
            case OFFSET_BY_MONTH  : offsetMonth = offsetFactor; break;
            case OFFSET_BY_DAY    : offsetDay = offsetFactor; break;
            case OFFSET_BY_HOUR   : offsetHour = offsetFactor; break;
            case OFFSET_BY_MINUTE : offsetMinute = offsetFactor; break;
            case OFFSET_BY_SECOND : offsetSecond = offsetFactor; break;
            case OFFSET_BY_WEEK   : offsetWeek = offsetFactor; break;
            default: {}
        }

        offsetLocalTime.set(
                offsetLocalTime.get(Calendar.YEAR) + offsetYear,
                offsetLocalTime.get(Calendar.MONTH) + offsetMonth,
                offsetLocalTime.get(Calendar.DAY_OF_MONTH) + offsetDay,
                offsetLocalTime.get(Calendar.HOUR_OF_DAY) + offsetHour,
                offsetLocalTime.get(Calendar.MINUTE) + offsetMinute,
                offsetLocalTime.get(Calendar.SECOND) + offsetSecond
        );

        offsetLocalTime.setTimeInMillis(offsetLocalTime.getTimeInMillis() + offsetWeek * 7 * 24 * 60 * 60 * 1000);

        if (offsetLocalTime.equals(currentLocalTime)) { sundial.setDialFrameWarning(false); }
        else { sundial.setDialFrameWarning(true); }

        initCurrentTime(sundial);
    }

    private void changeTimeZone(Sundial sundial, MouseEvent event) {

        if (sundial == null || event == null) { return; }

        if (!mouseButtonList.isEmpty()) {
            if (mouseButtonList.get(mouseButtonList.size() - 1).equals(MouseButton.MIDDLE)) {
                TimeZone localTimeZone = (new GregorianCalendar()).getTimeZone();
                timeZoneOffset = localTimeZone.getRawOffset();
            }
        }

        currentLocalTime.getTimeZone().setRawOffset(timeZoneOffset);
        offsetLocalTime.getTimeZone().setRawOffset(timeZoneOffset);

        initCurrentTime(sundial);
    }

    private void changeTimeZone(Sundial sundial, ScrollEvent event) {

        if (sundial == null || event == null) { return; }

        if (!mouseButtonList.isEmpty()) { return; }

        if (event.getDeltaY() < 0) {
            timeZoneOffset -= (60 * 60 * 1000);
        }
        else if (event.getDeltaY() > 0) {
            timeZoneOffset += (60 * 60 * 1000);
        }
        else { return; }

        if (timeZoneOffset > (12 * 60 * 60 * 1000)) {
            timeZoneOffset = -11 * 60 * 60 * 1000;
        }

        if (timeZoneOffset <= (-12 * 60 * 60 * 1000)) {
            timeZoneOffset =  12 * 60 * 60 * 1000;
        }

        currentLocalTime.getTimeZone().setRawOffset(timeZoneOffset);
        offsetLocalTime.getTimeZone().setRawOffset(timeZoneOffset);

        initCurrentTime(sundial);
    }

    private void changeNightCompression(Sundial sundial, ScrollEvent event) {

        if (suntimeLocal == null || sundial == null || event == null) { return; }

        if (!mouseButtonList.isEmpty()) { return; }

        if (event.getDeltaY() < 0) { sundial.increaseNightCompression(); }
        else if (event.getDeltaY() > 0) { sundial.decreaseNightCompression(); }
        else { return; }

    }

    private void changeNightCompression(Sundial sundial, MouseEvent event) {

        if (suntimeLocal == null || sundial == null || event == null) { return; }

        MouseButton mouseButton = mouseButtonList.get(mouseButtonList.size() - 1);
        if (mouseButton == null || mouseButton.equals(MouseButton.MIDDLE)) { return; }

        double mouseX = event.getScreenX();
        double mouseY = event.getScreenY();

        double deltaMouseX = mouseX - savedMouseX;
        double deltaMouseY = mouseY - savedMouseY;

        double stepSize = NORMAL_STEP_SIZE;
        if (event.isSecondaryButtonDown()) { stepSize = FAST_STEP_SIZE; }

        if (deltaMouseY >= stepSize) {
            sundial.increaseNightCompression();
            savedMouseY = mouseY;
        } else if (deltaMouseY <= -1 * stepSize) {
            sundial.decreaseNightCompression();
            savedMouseY = mouseY;
        } else {
            return;
        }

    }

    private void initCurrentTime(Sundial sundial) {
        updateCurrentTime(sundial, true);
    }

    private void updateCurrentTime(Sundial sundial) {
        updateCurrentTime(sundial, false);
    }

    private void updateCurrentTime(Sundial sundial, boolean initialize) {

        GregorianCalendar newLocalTime = new GregorianCalendar();
        GregorianCalendar oldLocalTime = (GregorianCalendar) currentLocalTime.clone();

        // Store current Julian Day Number before updating current time
        long oldJulianDayNumber = Suntime.getJulianDayNumber(offsetLocalTime);

        long newTimeInSeconds = newLocalTime.getTimeInMillis() / 1000;
        long currentTimeInSeconds = currentLocalTime.getTimeInMillis() / 1000;

        if (newTimeInSeconds == currentTimeInSeconds && !initialize) { return; }

        // Update current and offset local time
        long offsetSeconds = (offsetLocalTime.getTimeInMillis() - currentLocalTime.getTimeInMillis()) / 1000;
        currentLocalTime = newLocalTime;
        offsetLocalTime.setTimeInMillis(currentLocalTime.getTimeInMillis() + offsetSeconds * 1000);

        // Update suntimeLocal and sundial objects
//        suntimeLocal.setObserverTime(offsetLocalTime);
        long timeZoneCorrection = offsetLocalTime.getTimeZone().getOffset(offsetLocalTime.getTimeInMillis());

        GregorianCalendar timeZonedCalendar = new GregorianCalendar();
        timeZonedCalendar.setTimeInMillis(offsetLocalTime.getTimeInMillis() + timeZoneCorrection);

        GregorianCalendar globalCalendar = new GregorianCalendar();
        globalCalendar.setTimeInMillis(timeZonedCalendar.getTimeInMillis() - timeZoneCorrection);

        suntimeLocal.setObserverTime(timeZonedCalendar);
        suntimeGlobal.setObserverTime(globalCalendar);

        long newJulianDayNumber = suntimeLocal.getJulianDayNumber();

        sundial.setLocalTime(offsetLocalTime);
        sundial.updateCetusTimer(cetustime);

        double phase = suntimeGlobal.getJulianDate() - suntimeGlobal.getJulianDayNumber();
        double tilt = suntimeGlobal.getRealTimeDeclinationOfTheSun(Suntime.getJulianDate(globalCalendar));

        sundial.getDayGlobe().setDayLightPosition(phase, tilt);
        sundial.getNightGlobe().setDayLightPosition(phase, tilt);
        sundial.getTinyGlobe().setDayLightPosition(phase, tilt);
        sundial.getDayTerminatorLine().setDayLightPosition(phase, tilt);
        sundial.getDayTerminatorGlow().setDayLightPosition(phase, tilt);

        String yearString = ("0000" + offsetLocalTime.get(Calendar.YEAR));
        yearString = yearString.substring(yearString.length() - 4);
        String monthString = ("00" + (offsetLocalTime.get(Calendar.MONTH) + 1));
        monthString = monthString.substring(monthString.length() - 2);
        String dayString = ("00" + offsetLocalTime.get(Calendar.DAY_OF_MONTH));
        dayString = dayString.substring(dayString.length() - 2);
        String hourString = ("00" + offsetLocalTime.get(Calendar.HOUR_OF_DAY));
        hourString = hourString.substring(hourString.length() - 2);
        String minuteString = ("00" + offsetLocalTime.get(Calendar.MINUTE));
        minuteString = minuteString.substring(minuteString.length() - 2);
        String secondString = ("00" + offsetLocalTime.get(Calendar.SECOND));
        secondString = secondString.substring(secondString.length() - 2);
        String weekString = ("00" + offsetLocalTime.get(Calendar.WEEK_OF_YEAR));
        weekString = weekString.substring(weekString.length() - 2);

        sundial.getMatrixHour().setString(hourString);
        sundial.getMatrixMinute().setString(minuteString);
        sundial.getMatrixSecond().setString(secondString);
        sundial.getMatrixDay().setString(dayString);
        sundial.getMatrixMonth().setString(monthString);
        sundial.getMatrixYear().setString(yearString);
        sundial.getMatrixWeek().setString(weekString);

        // Update daily data only if it's a new day, or forced initialization event
        if (newJulianDayNumber != oldJulianDayNumber || initialize) {

            suntimeLocal.setObserverPosition(longitude, latitude);
            cetusNightList = cetustime.getNightList(timeZonedCalendar);

            double highNoonJulianDate = suntimeLocal.getHighnoonJulianDate();
            double sunriseJulianDate = suntimeLocal.getSunriseJulianDate();
            double sunsetJulianDate = suntimeLocal.getSunsetJulianDate();

            GregorianCalendar highNoonDate = Suntime.getCalendarDate(highNoonJulianDate, offsetLocalTime.getTimeZone());
            GregorianCalendar sunriseDate = Suntime.getCalendarDate(sunriseJulianDate, offsetLocalTime.getTimeZone());
            GregorianCalendar sunsetDate = Suntime.getCalendarDate(sunsetJulianDate, offsetLocalTime.getTimeZone());

            sundial.setHighNoon(highNoonDate);
            sundial.setHorizon(sunriseDate, sunsetDate);
            sundial.setCoordinates(longitude, latitude);
            sundial.setCetusTime(cetusNightList, timeZonedCalendar);
            sundial.setTimeZone(offsetLocalTime.getTimeZone());

            // update chart
            if (statsWindow.isShowing()) {
                sunchart.setSpacetimePosition(longitude, latitude, offsetLocalTime.get(Calendar.YEAR));
            }

            // update debug info
            updateDebugWindow(sundial);

        }
    }

    private void resetGlobePosition(Sundial sundial, Position type) {

        if (type == Position.LONGITUDE) { longitude = DEFAULT_LONGITUDE; }
        else if (type == Position.LATITUDE) { latitude = DEFAULT_LATITUDE; }
        else if (type == Position.BOTH){
            longitude = DEFAULT_LONGITUDE;
            latitude = DEFAULT_LATITUDE;
        }

        initCurrentTime(sundial);
        sundial.rotateGlobeAnimated(longitude, latitude);
    }

    private void recordGlobePosition(Sundial sundial, Position type, MouseEvent event) {

        mouseButtonList.add(event.getButton());
        if (event.getButton().equals(MouseButton.MIDDLE)) {
            resetGlobePosition(sundial, type);
        }

        savedMouseX = event.getScreenX();
        savedMouseY = event.getScreenY();

        savedLongitude = longitude;
        savedLatitude = latitude;
    }

    private void rotateGlobe(Sundial sundial, Position type, DragEvent event) {

        if (type != Position.GOOGLE_MAPS) { return; }

        String string;

        if (event.getDragboard().hasString()) {
            string = event.getDragboard().getString();
        } else {
            return;
        }

        Pattern pattern = Pattern.compile(GOOGLEMAPS_REGEX);
        Matcher matcher = pattern.matcher(string);

        if (matcher.matches()) {
            try {
                latitude = Double.parseDouble(matcher.group(1));
                longitude = Double.parseDouble(matcher.group(2));
            } catch (NumberFormatException e) {
                debugErrorMessage = "NumberFormatException while parsing string: " + string + "\n" + e.getMessage();
            }
        }

        initCurrentTime(sundial);
        sundial.rotateGlobeAnimated(longitude, latitude);
    }

    private void rotateGlobe(Sundial sundial, MouseEvent event) {

        if (!mouseButtonList.isEmpty()) {
            if(mouseButtonList.get(mouseButtonList.size() - 1).equals(MouseButton.MIDDLE)) {
                return;
            }
        }

        double mouseScreenX = event.getScreenX();
        double mouseScreenY = event.getScreenY();

        double deltaLongitude = savedMouseX - mouseScreenX;
        double deltaLatitude = savedMouseY - mouseScreenY;

        savedMouseX = mouseScreenX;
        savedMouseY = mouseScreenY;

        double precision = 4;
        if (event.isSecondaryButtonDown()) { precision = 100; }

        longitude += round((deltaLongitude / precision) * 100) / 100d;
        latitude -= round((deltaLatitude / precision) * 100) / 100d;

        if (longitude < Suntime.MIN_LONGITUDE) { longitude = Suntime.MAX_LONGITUDE - (Suntime.MIN_LONGITUDE - longitude); }
        if (longitude > Suntime.MAX_LONGITUDE) { longitude = Suntime.MIN_LONGITUDE - (Suntime.MAX_LONGITUDE - longitude); }
        if (latitude < Suntime.MIN_LATITUDE) { latitude = Suntime.MIN_LATITUDE; }
        if (latitude > Suntime.MAX_LATITUDE) { latitude = Suntime.MAX_LATITUDE; }

        initCurrentTime(sundial);
        sundial.rotateGlobe(longitude, latitude);
    }

    private void rotateGlobe(Sundial sundial, Position type, MouseEvent event) {

        if (!mouseButtonList.isEmpty()) {
            if(mouseButtonList.get(mouseButtonList.size() - 1).equals(MouseButton.MIDDLE)) {
                return;
            }
        }

        double mouseScreenX = event.getScreenX();
        double mouseScreenY = event.getScreenY();

        double delta = savedMouseY - mouseScreenY;

        savedMouseX = mouseScreenX;
        savedMouseY = mouseScreenY;

        double precision = 4;
        if (event.isSecondaryButtonDown()) { precision = 100; }

        if (type == Position.LONGITUDE) { longitude += round((delta / precision) * 100) / 100d; }
        else { latitude -= round((delta / precision) * 100) / 100d; }

        if (longitude < Suntime.MIN_LONGITUDE) { longitude = Suntime.MAX_LONGITUDE - (Suntime.MIN_LONGITUDE - longitude); }
        if (longitude > Suntime.MAX_LONGITUDE) { longitude = Suntime.MIN_LONGITUDE - (Suntime.MAX_LONGITUDE - longitude); }
        if (latitude < Suntime.MIN_LATITUDE) { latitude = Suntime.MIN_LATITUDE; }
        if (latitude > Suntime.MAX_LATITUDE) { latitude = Suntime.MAX_LATITUDE; }

        initCurrentTime(sundial);
        sundial.rotateGlobe(longitude, latitude);
    }

    private void rotateGlobe(Sundial sundial, Position type, ScrollEvent event) {

        if (!mouseButtonList.isEmpty()) {
            if(mouseButtonList.get(mouseButtonList.size() - 1).equals(MouseButton.MIDDLE)) {
                return;
            }
        }

        double step = 0;

        if (event.getDeltaY() < 0) { step = -0.01; }
        if (event.getDeltaY() > 0) { step = 0.01; }

        if (type == Position.LONGITUDE) { longitude += step; }
        else { latitude += step; }

        if (longitude < Suntime.MIN_LONGITUDE) { longitude = Suntime.MAX_LONGITUDE - (Suntime.MIN_LONGITUDE - longitude); }
        if (longitude > Suntime.MAX_LONGITUDE) { longitude = Suntime.MIN_LONGITUDE - (Suntime.MAX_LONGITUDE - longitude); }
        if (latitude < Suntime.MIN_LATITUDE) { latitude = Suntime.MIN_LATITUDE; }
        if (latitude > Suntime.MAX_LATITUDE) { latitude = Suntime.MAX_LATITUDE; }

        initCurrentTime(sundial);
        sundial.rotateGlobe(longitude, latitude);
    }

    private void resetWindowSize(Stage stage, Group dialsGroup) {

        stage.setWidth(dialsGroup.getLayoutBounds().getWidth());
        stage.setHeight(dialsGroup.getLayoutBounds().getHeight());

    }

    private void recordCalendarPosition(Sundial sundial, MouseEvent event) {

        MouseButton mouseButton = event.getButton();
        mouseButtonList.add(event.getButton());

        if (mouseButton.equals(MouseButton.MIDDLE)) {
            resetTime(sundial);
            return;
        }

        savedMouseX = event.getScreenX();
        savedMouseY = event.getScreenY();

        offsetX = 0;
        offsetY = 0;

    }

    private void recordNightCompressionPosition(Sundial sundial, MouseEvent event) {

        MouseButton mouseButton = event.getButton();
        mouseButtonList.add(event.getButton());

        if (mouseButton.equals(MouseButton.MIDDLE)) {
            sundial.resetNightCompression();
            return;
        }

        savedMouseX = event.getScreenX();
        savedMouseY = event.getScreenY();

        offsetX = 0;
        offsetY = 0;

    }

    private void recordWindowPosition(Stage stage, Group dialsGroup, MouseEvent event) {

        if (event != null) {
            mouseButtonList.add(event.getButton());
            savedMouseX = event.getScreenX();
            savedMouseY = event.getScreenY();
        }

        savedWindowPositionX = stage.getX();
        savedWindowPositionY = stage.getY();

        savedWindowSizeX = stage.getWidth();
        savedWindowSizeY = stage.getHeight();

        deltaX = stage.getX() - savedMouseX;
        deltaY = stage.getY() - savedMouseY;

        offsetX = 0;
        offsetY = 0;

        if (savedWindowPositionX + savedWindowSizeX / 2 - savedMouseX > 0 && savedWindowPositionY + savedWindowSizeY / 2 - savedMouseY > 0) { dX = -1; dY = -1; }
        if (savedWindowPositionX + savedWindowSizeX / 2 - savedMouseX < 0 && savedWindowPositionY + savedWindowSizeY / 2 - savedMouseY > 0) { dX = 1; dY = -1; }
        if (savedWindowPositionX + savedWindowSizeX / 2 - savedMouseX > 0 && savedWindowPositionY + savedWindowSizeY / 2 - savedMouseY < 0) { dX = -1; dY = 1; }
        if (savedWindowPositionX + savedWindowSizeX / 2 - savedMouseX < 0 && savedWindowPositionY + savedWindowSizeY / 2 - savedMouseY < 0) { dX = 1; dY = 1; }

    }

    private void recordWindowSize(Stage stage, Group dialsGroup, MouseEvent event) {

        if (event != null) {
            MouseButton mouseButton = event.getButton();
            mouseButtonList.add(event.getButton());

            if (mouseButton.equals(MouseButton.MIDDLE)) {
                resetWindowSize(stage, dialsGroup);
                return;
            }

            savedMouseX = event.getScreenX();
            savedMouseY = event.getScreenY();
        }

        savedWindowPositionX = stage.getX();
        savedWindowPositionY = stage.getY();

        savedWindowSizeX = stage.getWidth();
        savedWindowSizeY = stage.getHeight();

        deltaX = stage.getX() - savedMouseX;
        deltaY = stage.getY() - savedMouseY;

        offsetX = 0;
        offsetY = 0;

        if (savedWindowPositionX + savedWindowSizeX / 2 - savedMouseX > 0 && savedWindowPositionY + savedWindowSizeY / 2 - savedMouseY > 0) { dX = -1; dY = -1; }
        if (savedWindowPositionX + savedWindowSizeX / 2 - savedMouseX < 0 && savedWindowPositionY + savedWindowSizeY / 2 - savedMouseY > 0) { dX = 1; dY = -1; }
        if (savedWindowPositionX + savedWindowSizeX / 2 - savedMouseX > 0 && savedWindowPositionY + savedWindowSizeY / 2 - savedMouseY < 0) { dX = -1; dY = 1; }
        if (savedWindowPositionX + savedWindowSizeX / 2 - savedMouseX < 0 && savedWindowPositionY + savedWindowSizeY / 2 - savedMouseY < 0) { dX = 1; dY = 1; }

    }

    private void resizeWindow(Stage stage, Sundial sundial, MouseEvent event) {

        if (!mouseButtonList.isEmpty()) {
            if(mouseButtonList.get(mouseButtonList.size() - 1).equals(MouseButton.MIDDLE)) {
                return;
            }
        }

        double mouseX = event.getScreenX();
        double mouseY = event.getScreenY();

        double deltaSizeX = mouseX - savedMouseX;
        double deltaSizeY = mouseY - savedMouseY;

        double windowSizeX = savedWindowSizeX + deltaSizeX;
        double windowSizeY = savedWindowSizeY + deltaSizeY;

        double minWidth, minHeight, maxWidth, maxHeight;

        minWidth = MIN_WIDTH;
        minHeight = MIN_HEIGHT;

        Rectangle2D recCenterOfPointer = new Rectangle2D(savedWindowPositionX + savedWindowSizeX / 2, savedWindowPositionY + savedWindowSizeY / 2, 0, 0);

        if (Screen.getScreensForRectangle(recCenterOfPointer).size() > 0) {
            Rectangle2D currentScreen = Screen.getScreensForRectangle(recCenterOfPointer).get(0).getVisualBounds();
            maxWidth = currentScreen.getMaxX() - savedWindowPositionX;
            maxHeight = currentScreen.getMaxY() - savedWindowPositionY;
        } else {
            maxWidth = windowSizeX;
            maxHeight = windowSizeY;
        }

        if (event.isPrimaryButtonDown()) {
            if (maxWidth >= maxHeight) { maxWidth = maxHeight; }
            else { maxHeight = maxWidth; }
            if (windowSizeX >= windowSizeY) { windowSizeY = windowSizeX; }
            else { windowSizeX = windowSizeY; }
        }

        if (windowSizeX < minWidth) { windowSizeX = MIN_WIDTH; }
        if (windowSizeY < minHeight) { windowSizeY = MIN_HEIGHT; }
        if (windowSizeX > maxWidth) { windowSizeX = maxWidth; }
        if (windowSizeY > maxHeight) { windowSizeY = maxHeight; }

        stage.setWidth(windowSizeX);
        stage.setHeight(windowSizeY);

        updateDebugWindow(sundial);
    }


    private void toggleMaximizeWindow(Stage stage, MouseEvent event) {

        double maxWidth, maxHeight;
        double screenWidth, screenHeight;
        Rectangle2D currentScreen;

        Rectangle2D recCenterOfPointer = new Rectangle2D(stage.getX() + stage.getWidth() / 2, stage.getY() + stage.getHeight() / 2, 0, 0);
        if (Screen.getScreensForRectangle(recCenterOfPointer).size() > 0) {
            currentScreen = Screen.getScreensForRectangle(recCenterOfPointer).get(0).getVisualBounds();
        } else {
            return;
        }

        screenWidth = currentScreen.getMaxX() - currentScreen.getMinX();
        screenHeight = currentScreen.getMaxY() - currentScreen.getMinY();

        if (maximizedEh) {

            stage.setX(savedWindowPositionX);
            stage.setY(savedWindowPositionY);

            stage.setWidth(savedWindowSizeX);
            stage.setHeight(savedWindowSizeY);

            maximizedEh = false;

        } else {
            savedWindowPositionX = stage.getX();
            savedWindowPositionY = stage.getY();

            savedWindowSizeX = stage.getWidth();
            savedWindowSizeY = stage.getHeight();

            maxWidth = min(screenWidth, screenHeight);
            maxHeight = min(screenWidth, screenHeight);

            stage.setX(currentScreen.getMaxX() - screenWidth / 2 - maxWidth / 2);
            stage.setY(currentScreen.getMaxY() - screenHeight / 2 - maxHeight / 2);

            stage.setWidth(maxWidth);
            stage.setHeight(maxHeight);

            maximizedEh = true;
        }

    }

    private void minimizeWindow(Stage stage, Timeline timeline, MouseEvent event) {

        if (!mouseButtonList.isEmpty()) {

            if (mouseButtonList.get(mouseButtonList.size() - 1).equals(MouseButton.SECONDARY)) {

                if (debugWindow.isShowing()) {
                    debugWindow.close();
                } else {
                    debugWindow.show();
                }

                return;
            }
        }

        stage.setIconified(true);
    }

    private void changeWindowPosition(Stage stage, MouseEvent event) {

        double winSizeX = stage.getWidth();
        double winSizeY = stage.getHeight();

        double mouseScreenX = event.getScreenX();
        double mouseScreenY = event.getScreenY();

        double positionX = mouseScreenX + deltaX;
        double positionY = mouseScreenY + deltaY;

        double centerPositionX = positionX + (winSizeX / 2.0);
        double centerPositionY = positionY + (winSizeY / 2.0);

        Rectangle2D recCenterOfPointer = new Rectangle2D(centerPositionX, centerPositionY, 0, 0);
        if (Screen.getScreensForRectangle(recCenterOfPointer).size() <= 0) { return; }
        Rectangle2D currentScreen = Screen.getScreensForRectangle(recCenterOfPointer).get(0).getVisualBounds();
        double currentScreenMinX = currentScreen.getMinX();
        double currentScreenMaxX = currentScreen.getMaxX();
        double currentScreenMinY = currentScreen.getMinY();
        double currentScreenMaxY = currentScreen.getMaxY();

        double newPositionX = positionX;
        double newPositionY = positionY;

        if (positionX < currentScreenMinX)
            newPositionX = currentScreenMinX;
        if (positionX > (currentScreenMaxX - winSizeX))
            newPositionX = currentScreenMaxX - winSizeX;
        if (positionY < currentScreenMinY)
            newPositionY = currentScreenMinY;
        if (positionY > (currentScreenMaxY - winSizeY))
            newPositionY = currentScreenMaxY - winSizeY;

        stage.setX(newPositionX);
        stage.setY(newPositionY);
    }

    private void toggleGlobe(Stage stage, Sundial sundial, Group dialsGroup) {

        if (!mouseButtonList.isEmpty()) {

            MouseButton mouseButton = mouseButtonList.get(mouseButtonList.size() - 1);
            mouseButtonList.clear();

            if (mouseButton.equals(MouseButton.SECONDARY)) {
                sundial.toggleCetusTime();
                return;
            }

            if (mouseButton.equals(MouseButton.MIDDLE)) {
                resetGlobePosition(sundial, Position.BOTH);
                return;
            }
        }

        sundial.toggleGlobeVisibility();

        if (sundial.globeVisibleEh) {
//            sundial.getDialCircleFrame().setOnMouseEntered(event -> sundial.getDialCircleFrame().setCursor(Cursor.OPEN_HAND));
            sundial.getDialCircleFrame().setOnMousePressed(event -> {
                if (!event.isMiddleButtonDown()) { sundial.setTimeDisplayOpacity(0.2); }
                recordGlobePosition(sundial, Position.BOTH, event);
            });
            sundial.getDialCircleFrame().setOnMouseReleased(event ->  {
                sundial.setTimeDisplayOpacity(1);
                mouseButtonList.clear();
            });
            sundial.getDialCircleFrame().setOnMouseDragged(event -> rotateGlobe(sundial, event));

        } else {
//            sundial.getDialCircleFrame().setOnMouseEntered(event -> sundial.getDialCircleFrame().setCursor(Cursor.MOVE));
            sundial.getDialCircleFrame().setOnMousePressed(event -> recordWindowPosition(stage, dialsGroup, event));
            sundial.getDialCircleFrame().setOnMouseReleased(event -> mouseButtonList.clear());
            sundial.getDialCircleFrame().setOnMouseDragged(event -> changeWindowPosition(stage, event));

        }
    }

    private void updateDebugWindow(Sundial sundial) {

        if (!debugWindow.isShowing()) { return; }

        double dividend = sin(toRadians(-0.83d)) - sin(toRadians(latitude)) * sin(toRadians(suntimeLocal.getDeclinationOfTheSun()));
        double divisor = cos(toRadians(latitude)) * cos(toRadians(suntimeLocal.getDeclinationOfTheSun()));

        double julianDate = suntimeLocal.getJulianDate();
        double julianDayNumber = suntimeLocal.getJulianDayNumber();
        double highNoonJulianDate = suntimeLocal.getHighnoonJulianDate();
        double sunriseJulianDate = suntimeLocal.getSunriseJulianDate();
        double sunsetJulianDate = suntimeLocal.getSunsetJulianDate();
        double dayLength = sunsetJulianDate - sunriseJulianDate;

        GregorianCalendar highNoonDate = Suntime.getCalendarDate(highNoonJulianDate, offsetLocalTime.getTimeZone());
        GregorianCalendar sunriseDate = Suntime.getCalendarDate(sunriseJulianDate, offsetLocalTime.getTimeZone());
        GregorianCalendar sunsetDate = Suntime.getCalendarDate(sunsetJulianDate, offsetLocalTime.getTimeZone());

        StringBuilder cetusDataString = new StringBuilder();

        Iterator cetusDataMapIterator = cetustime.getDataMap().keySet().iterator();
        while (cetusDataMapIterator.hasNext()) {
            String key = (String) cetusDataMapIterator.next();
            cetusDataString.append(key).append(" = ").append(cetustime.getDataMap().get(key)).append("\n");
        }

        String cetusExpiryDate = ""
                + cetustime.getCetusExpiry().get(Calendar.HOUR_OF_DAY) + ":"
                + cetustime.getCetusExpiry().get(Calendar.MINUTE) + ":"
                + cetustime.getCetusExpiry().get(Calendar.SECOND)
                + " " + cetustime.getCetusExpiry().getTimeZone().getDisplayName()
                ;

        StringBuilder cetusNightListString = new StringBuilder();
        for(int i = 0; i < cetusNightList.size(); i++) {
            String nightStart = cetusNightList.get(i).get(0).getTime().toString();
            String nightEnd = cetusNightList.get(i).get(1).getTime().toString();
            cetusNightListString.append("\nnight ").append(i+1).append(": start = ").append(nightStart).append(", end = ").append(nightEnd);
        }

        long timeZoneOffset = offsetLocalTime.getTimeZone().getOffset(offsetLocalTime.getTimeInMillis());

        String timeZoneNumberString = "00" + abs(timeZoneOffset / (1000 * 60 * 60));
        timeZoneNumberString = timeZoneNumberString.substring(timeZoneNumberString.length() - 2);

        StringBuilder timeZoneString = new StringBuilder()
                .append("GMT")
                .append((timeZoneOffset < 0) ? "-" : "+")
                .append(timeZoneNumberString)
                ;

        String debugText = ""
                + "Day[9] date              : " + offsetLocalTime.getTime().toString() + "\n"
                + "Day[9] day of the year   : " + offsetLocalTime.get(Calendar.DAY_OF_YEAR) + "\n"
                + "Day[9] Julian Date       : " + julianDateFormat.format(julianDate) + " (UTC)" + "\n"
                + "Day[9] Gregorian Date    : " + Suntime.getCalendarDate(julianDate, offsetLocalTime.getTimeZone()).getTime().toString() + "\n"
                + "Day[9] Julian Day Number : " + julianDayNumber + "\n"
                + "TimeZone String : " + timeZoneString + "\n"
                + "High Noon  : " + highNoonDate.getTime().toString() + "\n"
                + "Sunrise    : " + sunriseDate.getTime().toString() + "\n"
                + "Sunset     : " + sunsetDate.getTime().toString() + "\n"
                + "Day Length : " + Suntime.printSecondsToTime(Suntime.convertFractionToSeconds(dayLength)) + "\n"
                + "meanAnomaly = " + suntimeLocal.getMeanAnomaly() + "\n"
                + "equationOfCenter = " + suntimeLocal.getEquationOfCenter() + "\n"
                + "eclipticalLongitude = " + suntimeLocal.getEclipticalLongitude() + "\n"
                + "rightAscension = " + suntimeLocal.getRightAscension() + "\n"
                + "declinationOfTheSun = " + suntimeLocal.getDeclinationOfTheSun() + "\n"
                + "siderealTime = " + suntimeLocal.getSiderealTime() + "\n"
                + "hourAngle = " + suntimeLocal.getHourAngle() + "\n"
                + "solarTransit = " + suntimeLocal.getSolarTransit() + "\n"
                + "localHourAngle = " + suntimeLocal.getLocalHourAngle() + "\n"
                + "localHourAngle dividend = " + dividend + "\n"
                + "localHourAngle divisor = " + divisor + "\n"
                + "longitude = " + longitude + "\n"
                + "latitude = " + latitude + "\n"
                + "Tx =  " + sundial.getDialHighNoonGroup().getLocalToParentTransform().getTx() + "\n"
                + "Ty =  " + sundial.getDialHighNoonGroup().getLocalToParentTransform().getTy() + "\n"
                + "Mxx = " + sundial.getDialHighNoonGroup().getLocalToParentTransform().getMxx() + "\n"
                + "Mxy = " + sundial.getDialHighNoonGroup().getLocalToParentTransform().getMxy() + "\n"
                + "Myx = " + sundial.getDialHighNoonGroup().getLocalToParentTransform().getMyx() + "\n"
                + "Myy = " + sundial.getDialHighNoonGroup().getLocalToParentTransform().getMyy() + "\n"
//                + "Cetus nightList = " + cetusNightListString + "\n"
//                + "Cetus okEh = " + cetustime.isOkEh() + "\n"
//                + "Cetus result = " + cetustime.getResult() + "\n"
//                + "Cetus isDay = " + cetustime.cetusDayEh() + "\n"
//                + "Cetus expiry calendar = " + cetustime.getCetusExpiry().getTime() + "\n"
//                + "Cetus expiry string = " + cetusExpiryDate + "\n"
                + "Cetus dataMap: \n" + cetusDataString + "\n"
                ;

        if (debugErrorMessage != null && !debugErrorMessage.isEmpty()) {
            debugText = debugErrorMessage + "\n" + debugText;
        }

        debugTextArea.setText(debugText);
    }
}
