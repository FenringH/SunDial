import javafx.animation.*;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.concurrent.Task;
import javafx.geometry.Rectangle2D;
import javafx.scene.*;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Math.*;

public class Sunface extends Application {


    private int fpsSetting = (int) floor(1000 / Sunconfig.DEFAULT_FPS);

    private GregorianCalendar currentLocalTime;
    private GregorianCalendar offsetLocalTime;
    private GregorianCalendar timeZonedCalendar;

    private double longitude = Sunconfig.DEFAULT_LONGITUDE;
    private double latitude = Sunconfig.DEFAULT_LATITUDE;
    private double customLongitude = Sunconfig.DEFAULT_LONGITUDE;
    private double customLatitude = Sunconfig.DEFAULT_LATITUDE;

    private enum PositionType {LONGITUDE, LATITUDE, BOTH, GOOGLE_MAPS};
    private enum OffsetType {YEAR, MONTH, DAY, HOUR, MINUTE, SECOND, WEEK};
    private enum WindowType { PRIMARY, CHART, DEBUG }

    private double deltaX;
    private double deltaY;
    private double savedMouseX;
    private double savedMouseY;
    private double savedWindowPositionX;
    private double savedWindowPositionY;
    private double savedWindowSizeX;
    private double savedWindowSizeY;
    private HashMap<WindowType, Double> unmaximizedWindowPositionX;
    private HashMap<WindowType, Double> unmaximizedWindowPositionY;
    private HashMap<WindowType, Double> unmaximizedWindowSizeX;
    private HashMap<WindowType, Double> unmaximizedWindowSizeY;
    private Node savedNode;

    private int timeZoneOffset;
    private int localTimeZoneOffset;
    private long timeZoneCorrection;

    private HashMap <WindowType, Boolean> maximizedEh;
    private boolean snapToCenterEh = true;
    private boolean inCenterEh = false;
    private boolean firstShowPrimary = true;

    private TextArea debugTextArea;
    private String debugErrorMessage;

    private Sundial sundial;
    private Suntime suntimeLocal;
    private Suntime suntimeGlobal;

    private KriegsrahmenZeit cetusTime;
    private KriegsrahmenZeit orbVallisTime;

    private Sunchart sunchart;
    private Sunyear sunyear;

    private double sunyearDefaultWidth;
    private double sunyearDefaultHeight;

    private Stage debugWindow;
    private Stage sunchartWindow;

    private Clipboard clipboard;

    private ArrayList<ArrayList<GregorianCalendar>> cetusCycleList;
    private ArrayList<ArrayList<GregorianCalendar>> orbVallisCycleList;

    private ArrayList<MouseButton> mouseButtonList = new ArrayList<>();


    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) {

        clipboard = Clipboard.getSystemClipboard();

        // Init time
        currentLocalTime = new GregorianCalendar();
        offsetLocalTime = new GregorianCalendar();
        timeZonedCalendar = new GregorianCalendar();

        timeZoneOffset = currentLocalTime.getTimeZone().getRawOffset();
        localTimeZoneOffset = timeZoneOffset;

        // Create 'sun' objects
        suntimeLocal = new Suntime.PleaseBuildSuntime()
                .localTime(currentLocalTime)
                .observerLongitude(longitude)
                .observerLatitude(latitude)
                .thankYou();

        suntimeGlobal = new Suntime.PleaseBuildSuntime()
                .localTime(currentLocalTime)
                .observerLongitude(longitude)
                .observerLatitude(latitude)
                .thankYou();

        sundial = new Sundial.PleaseBuildSundial()
                .nightCompression(0)
                .thankYou();

        sundial.rotateGlobe(longitude, latitude);
        sundial.getControlThingyDst().toggleState();

        cetusTime = new KriegsrahmenZeit(KriegsrahmenZeit.Platform.PC, KriegsrahmenZeit.Location.CETUS);
        orbVallisTime = new KriegsrahmenZeit(KriegsrahmenZeit.Platform.PC, KriegsrahmenZeit.Location.ORB_VALLIS);

        sunchart = new Sunchart(longitude, latitude, currentLocalTime.get(Calendar.YEAR));
        sunyear = new Sunyear(longitude, latitude, currentLocalTime, timeZoneOffset);

        unmaximizedWindowPositionX = new HashMap<>();
        unmaximizedWindowPositionY = new HashMap<>();
        unmaximizedWindowSizeX = new HashMap<>();
        unmaximizedWindowSizeY = new HashMap<>();

        maximizedEh = new HashMap<>();


        // Scene
        Group dialsGroup = sundial.getDialsGroup();

        Scene mainScene = new Scene(dialsGroup, dialsGroup.getLayoutBounds().getWidth(), dialsGroup.getLayoutBounds().getHeight(), true, SceneAntialiasing.DISABLED);
        mainScene.setFill(Color.TRANSPARENT);


        // Setup dialsGroup scale transform and bind to primaryStage size
        Scale dialsScale = new Scale();

        dialsGroup.getTransforms().add(dialsScale);

        dialsScale.xProperty().bind(Bindings.createDoubleBinding(() ->
                (primaryStage.widthProperty().get() / Sundial.DEFAULT_WIDTH),
                primaryStage.widthProperty())
        );

        dialsScale.yProperty().bind(Bindings.createDoubleBinding(() ->
                (primaryStage.heightProperty().get() / Sundial.DEFAULT_HEIGHT),
                primaryStage.heightProperty())
        );

        dialsScale.zProperty().bind(Bindings.createDoubleBinding(() -> {
            // For Z scale pick smaller value between width and height
            double stageWidth = primaryStage.widthProperty().get();
            double stageHeight = primaryStage.heightProperty().get();
            double stageSize = (stageWidth > stageHeight) ? stageHeight : stageWidth;
            double dialsSize = (Sundial.DEFAULT_WIDTH > Sundial.DEFAULT_HEIGHT) ? Sundial.DEFAULT_HEIGHT : Sundial.DEFAULT_WIDTH;
            return stageSize / dialsSize;
        },
                primaryStage.widthProperty(),
                primaryStage.heightProperty())
        );

        // Fix for Z-axis scale not propagating through SubScene in globe
        sundial.globeLightScalerProperty().bind(Bindings.createDoubleBinding(() ->
                (primaryStage.widthProperty().get() / Sundial.DEFAULT_WIDTH),
                primaryStage.widthProperty())
        );


        // App icons
        Image appIconSun = new Image(Sunconfig.ICON_APP_URL, 512, 512, true, true);


        // Debug window
        Button copyButton = new Button("Copy text to clipboard");
        copyButton.setMinHeight(26);
        copyButton.setOnMouseClicked(event -> sendTextToClipboard(debugTextArea.getText()));

        debugTextArea = new TextArea();
        debugTextArea.setFont(Sunconfig.FONT_DEBUG);
        debugTextArea.setMinWidth(600);
        debugTextArea.setMinHeight(800);
        debugTextArea.setEditable(false);
        debugTextArea.setWrapText(true);
        debugTextArea.setText(Sunconfig.A_BEGINNING);
        debugTextArea.setTranslateY(28);

        Group debugGroup = new Group();
        debugGroup.getChildren().addAll(debugTextArea, copyButton);

        Scene debugScene = new Scene(debugGroup, debugGroup.getLayoutBounds().getWidth() + 6, debugGroup.getLayoutBounds().getHeight() + 2 + 28);
        debugScene.setFill(Color.LIGHTSKYBLUE);

        debugWindow = new Stage();
        debugWindow.setTitle("Debug window");
        debugWindow.setScene(debugScene);
        debugWindow.setWidth(debugScene.getWidth());
        debugWindow.setHeight(debugScene.getHeight());
        debugWindow.setX(0);
        debugWindow.setY(0);
        debugWindow.setResizable(false);
        debugWindow.getIcons().add(appIconSun);


        // Chart window
        Group sunyearChart = sunyear.getChart();

        sunyearDefaultWidth = sunyearChart.getLayoutBounds().getWidth();
        sunyearDefaultHeight = sunyearChart.getLayoutBounds().getHeight();

        Scene chartScene = new Scene(sunyearChart, sunyearDefaultWidth, sunyearDefaultHeight);
        chartScene.setFill(Color.TRANSPARENT);

        sunchartWindow = new Stage();
        sunchartWindow.setTitle("Sunchart");
        sunchartWindow.setScene(chartScene);
        sunchartWindow.setX(0);
        sunchartWindow.setY(0);
        sunchartWindow.setWidth(chartScene.getWidth());
        sunchartWindow.setHeight(chartScene.getHeight());
        sunchartWindow.getIcons().add(appIconSun);
        sunchartWindow.initStyle(StageStyle.TRANSPARENT);

        Scale chartScale = new Scale();

        sunyearChart.getTransforms().add(chartScale);

        chartScale.xProperty().bind(Bindings.createDoubleBinding(() ->
                        (sunchartWindow.widthProperty().get() / sunyearDefaultWidth),
                sunchartWindow.widthProperty())
        );

        chartScale.yProperty().bind(Bindings.createDoubleBinding(() ->
                        (sunchartWindow.heightProperty().get() / sunyearDefaultHeight),
                sunchartWindow.heightProperty())
        );

        sundial.getControlThingyChart().stateProperty().bind(sunchartWindow.showingProperty());


        // Primary window
        primaryStage.setTitle("Sunface");
        primaryStage.setScene(mainScene);
        primaryStage.setMinWidth(Sunconfig.MIN_WIDTH);
        primaryStage.setMinHeight(Sunconfig.MIN_HEIGHT);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.getIcons().add(appIconSun);

        // Playtime
        KeyFrame keyframeClockTick = new KeyFrame(
                Duration.millis(fpsSetting),
                event -> updateCurrentTime());

        Timeline timeline = new Timeline(keyframeClockTick);
        timeline.setCycleCount(Animation.INDEFINITE);


        // *** MOUSE EVENTS ***

        // PRIMARY STAGE
        primaryStage.setOnHidden(event -> timeline.pause());
        primaryStage.setOnShown(event -> timeline.play());

        mainScene.setOnMouseEntered(event -> sundial.showOuterControlsGroup());
        mainScene.setOnMouseExited(event -> sundial.hideOuterControlsGroup());


        // SUNDIAL WINDOW
        sundial.getControlThingyClose().setOnMouseClicked(event -> System.exit(0));
        sundial.getControlThingyMaximize().setOnMouseClicked(event -> maximizeActions(primaryStage, WindowType.PRIMARY));

        sundial.getControlThingyMinimize().setOnMousePressed(event -> saveMouse(primaryStage, event));
        sundial.getControlThingyMinimize().setOnMouseReleased(event -> { minimizeActions(primaryStage, event); killMouse(); });

        sundial.getControlThingyResize().setOnMousePressed(event -> saveMouse(primaryStage, event));
        sundial.getControlThingyResize().setOnMouseReleased(event -> { resizeActions(primaryStage, WindowType.PRIMARY, event); killMouse(); });
        sundial.getControlThingyResize().setOnMouseDragged(event -> resizeWindow(primaryStage, WindowType.PRIMARY, event));

        sundial.getControlThingyHelp().setOnMouseClicked(event -> sundial.toggleHelp());
        sundial.getControlThingyAlwaysOnTop().setOnMouseClicked(event -> toggleAlwaysOnTop(primaryStage));
        sundial.getControlThingyNightmode().setOnMouseClicked(event -> sundial.toggleNightmode());
        sundial.getControlThingyAnimation().setOnMouseClicked(event -> sundial.toggleAnimation());
        sundial.getControlThingyPinInfo().setOnMouseClicked(event -> sundial.togglePinInfo());

        sundial.getControlThingyChart().setOnMouseClicked(event -> toggleSunchartWindow());
        sundial.getControlThingyCetus().setOnMouseClicked(event -> toggleKriegsrahmenZeit(KriegsrahmenZeit.Location.CETUS, event));
        sundial.getControlThingyOrbVallis().setOnMouseClicked(event -> toggleKriegsrahmenZeit(KriegsrahmenZeit.Location.ORB_VALLIS, event));

        sundial.getControlThingyGlobeGrid().setOnMouseClicked(event -> sundial.toggleGlobeGrid());
        sundial.getControlThingyGlobeLines().setOnMouseClicked(event -> sundial.toggleGlobeLines());

        sundial.getControlNightCompression().setOnMousePressed(event -> saveMouse(primaryStage, event));
        sundial.getControlNightCompression().setOnMouseReleased(event -> { nightCompressionActions(primaryStage, event); killMouse(); });
        sundial.getControlNightCompression().setOnMouseDragged(event -> nightCompressionDrag(sundial, event));
        sundial.getControlNightCompression().setOnScroll(event -> nightCompressionDrag(sundial, event));

        sundial.getControlThingyDst().setOnMouseClicked(event -> toggleDst());

        sundial.getDialMarginCircle().setOnMousePressed(event -> saveMouse(primaryStage, event));
        sundial.getDialMarginCircle().setOnMouseReleased(event -> { /* NO ACTIONS */ killMouse(); });
        sundial.getDialMarginCircle().setOnMouseDragged(event -> changeWindowPosition(primaryStage, event));

        sundial.getDialCircleFrame().setOnMousePressed(event -> { saveMouse(primaryStage, event); globeCheck(); });
        sundial.getDialCircleFrame().setOnMouseReleased(event -> { frameActions(primaryStage, event); killMouse(); globeCheck(); });
        sundial.getDialCircleFrame().setOnMouseDragged(event -> frameDrag(primaryStage, event) );
        sundial.getDialCircleFrame().setOnDragOver(event -> checkDragAndDropString(event));
        sundial.getDialCircleFrame().setOnDragDropped(event -> rotateGlobe(PositionType.GOOGLE_MAPS, event));

        sundial.getCetusMarkerGroup().setOnMousePressed(event -> { saveMouse(primaryStage, event); globeCheck(); });
        sundial.getCetusMarkerGroup().setOnMouseReleased(event -> { frameActions(primaryStage, event); killMouse(); globeCheck(); });
        sundial.getCetusMarkerGroup().setOnMouseDragged(event -> frameDrag(primaryStage, event) );

//        sundial.getTinyGlobeGroup().setOnMouseClicked(event -> tinyGlobeActions(event));
        sundial.getTinyGlobeGroup().setOnMousePressed(event -> saveMouse(primaryStage, event));
        sundial.getTinyGlobeGroup().setOnMouseReleased(event -> { tinyGlobeActions(event); killMouse(); });
        sundial.getTinyGlobeGroup().setOnMouseDragged(event -> tinyGlobeDrag(event) );
        sundial.getTinyGlobeGroup().setOnDragOver(event -> checkDragAndDropString(event));
        sundial.getTinyGlobeGroup().setOnDragDropped(event -> rotateGlobe(PositionType.GOOGLE_MAPS, event));

        sundial.getMatrixTimeZone().setOnMousePressed(event -> saveMouse(primaryStage, event));
        sundial.getMatrixTimeZone().setOnMouseReleased(event -> { timeZoneActions(sundial, event); killMouse(); });
        sundial.getMatrixTimeZone().setOnScroll(event -> timeZoneScroll(sundial, event));

        sundial.getMatrixLongitude().setOnMousePressed(event -> { saveMouse(primaryStage, event); globeCheck(); });
        sundial.getMatrixLongitude().setOnMouseReleased(event -> { coordinateActions(primaryStage, PositionType.LONGITUDE, event); killMouse(); globeCheck(); });
        sundial.getMatrixLongitude().setOnMouseDragged(event -> rotateGlobe(sundial, PositionType.LONGITUDE, event));
        sundial.getMatrixLongitude().setOnScroll(event -> rotateGlobe(sundial, PositionType.LONGITUDE, event));

        sundial.getMatrixLatitude().setOnMousePressed(event -> { saveMouse(primaryStage, event); globeCheck(); });
        sundial.getMatrixLatitude().setOnMouseReleased(event -> { coordinateActions(primaryStage, PositionType.LATITUDE, event); killMouse(); globeCheck(); });
        sundial.getMatrixLatitude().setOnMouseDragged(event -> rotateGlobe(sundial, PositionType.LATITUDE, event));
        sundial.getMatrixLatitude().setOnScroll(event -> { rotateGlobe(sundial, PositionType.LATITUDE, event); });

        sundial.getMatrixYear().setOnMousePressed(event -> saveMouse(primaryStage, event));
        sundial.getMatrixYear().setOnMouseReleased(event -> { timeControlActions(event); killMouse(); });
        sundial.getMatrixYear().setOnMouseDragged(event -> offsetTimeByEvent(OffsetType.YEAR, event));
        sundial.getMatrixYear().setOnScroll(event -> offsetTimeByEvent(OffsetType.YEAR, event));

        sundial.getMatrixMonth().setOnMousePressed(event -> saveMouse(primaryStage, event));
        sundial.getMatrixMonth().setOnMouseReleased(event -> { timeControlActions(event); killMouse(); });
        sundial.getMatrixMonth().setOnMouseDragged(event -> offsetTimeByEvent(OffsetType.MONTH, event));
        sundial.getMatrixMonth().setOnScroll(event -> offsetTimeByEvent(OffsetType.MONTH, event));

        sundial.getMatrixDay().setOnMousePressed(event -> saveMouse(primaryStage, event));
        sundial.getMatrixDay().setOnMouseReleased(event -> { timeControlActions(event); killMouse(); });
        sundial.getMatrixDay().setOnMouseDragged(event -> offsetTimeByEvent(OffsetType.DAY, event));
        sundial.getMatrixDay().setOnScroll(event -> offsetTimeByEvent(OffsetType.DAY, event));

        sundial.getMatrixHour().setOnMousePressed(event -> saveMouse(primaryStage, event));
        sundial.getMatrixHour().setOnMouseReleased(event -> { timeControlActions(event); killMouse(); });
        sundial.getMatrixHour().setOnMouseDragged(event -> offsetTimeByEvent(OffsetType.HOUR, event));
        sundial.getMatrixHour().setOnScroll(event -> offsetTimeByEvent(OffsetType.HOUR, event));

        sundial.getMatrixMinute().setOnMousePressed(event -> saveMouse(primaryStage, event));
        sundial.getMatrixMinute().setOnMouseReleased(event -> { timeControlActions(event); killMouse(); });
        sundial.getMatrixMinute().setOnMouseDragged(event -> offsetTimeByEvent(OffsetType.MINUTE, event));
        sundial.getMatrixMinute().setOnScroll(event -> offsetTimeByEvent(OffsetType.MINUTE, event));

        sundial.getMatrixWeek().setOnMousePressed(event -> saveMouse(primaryStage, event));
        sundial.getMatrixWeek().setOnMouseReleased(event -> { timeControlActions(event); killMouse(); });
        sundial.getMatrixWeek().setOnMouseDragged(event -> offsetTimeByEvent(OffsetType.WEEK, event));
        sundial.getMatrixWeek().setOnScroll(event -> offsetTimeByEvent(OffsetType.WEEK, event));

        sundial.getDialHighNoonGroup().setOnMouseClicked(event -> sundial.toggleSunHighNoon());

        sundial.getMiroTextGroup().setOnMouseClicked(event -> openBrowser(event, Sunconfig.MIRO_URL));

        // CHART WINDOW
        sunyear.getChartFrame().setOnMouseEntered(event -> sunyearChart.setCursor(Cursor.MOVE));
        sunyear.getChartFrame().setOnMouseExited(event -> sunyearChart.setCursor(Cursor.DEFAULT));
        sunyear.getChartFrame().setOnMousePressed(event -> saveMouse(sunchartWindow, event));
        sunyear.getChartFrame().setOnMouseReleased(event -> killMouse());
        sunyear.getChartFrame().setOnMouseDragged(event -> changeWindowPosition(sunchartWindow, event));

        sunyear.getControlThingyResize().setOnMousePressed(event -> saveMouse(sunchartWindow, event));
        sunyear.getControlThingyResize().setOnMouseReleased(event -> { resizeActions(sunchartWindow, WindowType.CHART, event); killMouse(); });
        sunyear.getControlThingyResize().setOnMouseDragged(event -> resizeWindow(sunchartWindow, WindowType.CHART, event));

        sunyear.getControlThingyClose().setOnMouseClicked(event -> sunchartWindow.close());

        sunyear.getControlThingyMaximize().setOnMouseClicked(event -> maximizeActions(sunchartWindow, WindowType.CHART));

        sunyear.getControlThingyMinimize().setOnMousePressed(event -> saveMouse(primaryStage, event));
        sunyear.getControlThingyMinimize().setOnMouseClicked(event -> { minimizeActions(sunchartWindow, event); killMouse(); });


        // *** SHOWTIME ***

        initCurrentTime();
        timeline.play();

        sundial.hideOuterControlsGroup();
//        sundial.toggleSunHighNoon();

        primaryStage.show();

        primaryStage.setAlwaysOnTop(true);
        primaryStage.setAlwaysOnTop(false);

        saveMouse(primaryStage, null);

        unmaximizedWindowPositionX.put(WindowType.PRIMARY, savedWindowPositionX);
        unmaximizedWindowPositionY.put(WindowType.PRIMARY, savedWindowPositionY);
        unmaximizedWindowSizeX.put(WindowType.PRIMARY, savedWindowSizeX);
        unmaximizedWindowSizeY.put(WindowType.PRIMARY, savedWindowSizeY);

        unmaximizedWindowPositionX.put(WindowType.CHART, 0d);
        unmaximizedWindowPositionY.put(WindowType.CHART, 0d);
        unmaximizedWindowSizeX.put(WindowType.CHART, sunyearDefaultWidth);
        unmaximizedWindowSizeY.put(WindowType.CHART, sunyearDefaultHeight);

        maximizedEh.put(WindowType.PRIMARY, false);
        maximizedEh.put(WindowType.CHART, false);

        Rectangle2D currentScreen = getCurrentScreen(primaryStage);
        if (currentScreen != null) {
            debugWindow.setX(currentScreen.getMinX());
            debugWindow.setY(currentScreen.getMinY());
            sunchartWindow.setX(currentScreen.getMinX());
            sunchartWindow.setY(currentScreen.getMinY());
        }
    }


    // ***************************************************************
    // *** Methods ***

    private void initCurrentTime() {
        updateCurrentTime(true);
        setWarnings();
    }

    private void updateCurrentTime() {
        updateCurrentTime(false);
    }

    private void updateCurrentTime(boolean initialize) {

        GregorianCalendar newLocalTime = new GregorianCalendar();

        // Store current Julian Day Number before updating current time
        long oldJulianDayNumber = Suntime.getJulianDayNumber(offsetLocalTime);

        long newTimeInSeconds = newLocalTime.getTimeInMillis() / 1000;
        long currentTimeInSeconds = currentLocalTime.getTimeInMillis() / 1000;

        if (newTimeInSeconds == currentTimeInSeconds && !initialize) { return; }

        // Update current and offset local time
        long offsetSeconds = (offsetLocalTime.getTimeInMillis() - currentLocalTime.getTimeInMillis()) / 1000;
        currentLocalTime = newLocalTime;
        offsetLocalTime.setTimeInMillis(currentLocalTime.getTimeInMillis() + offsetSeconds * 1000);

        // Update suntime and sundial objects
        timeZoneCorrection = offsetLocalTime.getTimeZone().getOffset(offsetLocalTime.getTimeInMillis());

        timeZonedCalendar.setTimeInMillis(offsetLocalTime.getTimeInMillis() + timeZoneCorrection);

        GregorianCalendar globalCalendar = new GregorianCalendar();
        globalCalendar.setTimeInMillis(timeZonedCalendar.getTimeInMillis() - timeZoneCorrection);

        suntimeLocal.setObserverTime(timeZonedCalendar);
        suntimeGlobal.setObserverTime(globalCalendar);

        long newJulianDayNumber = suntimeLocal.getJulianDayNumber();

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

        // Update daily data only if it's a new day, or forced initialization event
        if (newJulianDayNumber != oldJulianDayNumber || initialize) {

            suntimeLocal.setObserverPosition(longitude, latitude);
            suntimeGlobal.setObserverPosition(longitude, latitude);

            cetusCycleList = cetusTime.getCycleList(timeZonedCalendar);
            orbVallisCycleList = orbVallisTime.getCycleList(timeZonedCalendar);

            double highNoonJulianDate = suntimeLocal.getHighnoonJulianDate();
            double sunriseJulianDate = suntimeLocal.getSunriseJulianDate();
            double sunsetJulianDate = suntimeLocal.getSunsetJulianDate();

            GregorianCalendar highNoonDate = Suntime.getCalendarDate(highNoonJulianDate, offsetLocalTime.getTimeZone());
            GregorianCalendar sunriseDate = Suntime.getCalendarDate(sunriseJulianDate, offsetLocalTime.getTimeZone());
            GregorianCalendar sunsetDate = Suntime.getCalendarDate(sunsetJulianDate, offsetLocalTime.getTimeZone());

            double noonAngle = latitude - suntimeGlobal.getRealTimeDeclinationOfTheSun(newJulianDayNumber - 0.5);

            sundial.setHorizon(sunriseDate, sunsetDate);
            sundial.setCoordinates(longitude, latitude);
            sundial.setCetusTime(cetusCycleList, timeZonedCalendar, timeZoneCorrection);
            sundial.setOrbVallisTime(orbVallisCycleList, timeZonedCalendar, timeZoneCorrection);
            sundial.setTimeZone(offsetLocalTime.getTimeZone());
            sundial.setHighNoon(highNoonDate, noonAngle);
            sundial.setArcHourRotate(offsetLocalTime);

            sunyear.setLocalDate(offsetLocalTime);
        }

        sundial.setLocalTime(offsetLocalTime);
        sundial.updateCetusTimer(cetusCycleList);
        sundial.updateOrbVallisTimer(orbVallisCycleList);
        sundial.updateDialMarkers();

        double phase = (suntimeGlobal.getJulianDate() - suntimeGlobal.getJulianDayNumber()) * 360;
        double tilt = -suntimeGlobal.getRealTimeDeclinationOfTheSun(Suntime.getJulianDate(globalCalendar));

        sundial.setGlobeDaylight(phase, tilt);

        sundial.getMatrixHour().setString(hourString);
        sundial.getMatrixMinute().setString(minuteString);
        sundial.getMatrixSecond().setString(secondString);
        sundial.getMatrixDay().setString(dayString);
        sundial.getMatrixMonth().setString(monthString);
        sundial.getMatrixYear().setString(yearString);
        sundial.getMatrixWeek().setString(weekString);

        updateSunchart(sunchart);
        updateDebugWindow(sundial);
    }

    public void openBrowser(MouseEvent event, String uri) {

        if(Desktop.isDesktopSupported())
        {
            try {
                Desktop.getDesktop().browse(new URI(uri));
            } catch (IOException|URISyntaxException e) {
                sundial.getInfoText().setText("Unable to open web browser.");
                showInfoText(event);
                hideInfoTextWithDelay();
            }
        }

/*
        try {
            HostServicesDelegate hostServices = HostServicesFactory.getInstance(this);
            hostServices.showDocument(uri);
        } catch (Exception e) {
            sundial.getInfoText().setText("Error opening web browser.");
            showInfoText(event);
            hideInfoTextWithDelay();
        }
*/
    }

    private void sendTextToClipboard(String string) {
        ClipboardContent content = new ClipboardContent();
        content.putString(string);
        clipboard.setContent(content);
    }

    private void resetTime() {
        offsetLocalTime.setTimeInMillis(currentLocalTime.getTimeInMillis());
        sundial.setCustomTimeWarning(false);
        initCurrentTime();
    }

    private void offsetTimeByEvent(OffsetType offsetType, ScrollEvent event) {

        if (suntimeLocal == null || sundial == null || event == null) { return; }

        if (!mouseButtonList.isEmpty()) { return; }

        int offsetFactor;

        if (event.getDeltaY() < 0) { offsetFactor = -1; }
        else { offsetFactor = 1; }

        offsetTimeAction(offsetType, offsetFactor);
    }

    private void offsetTimeByEvent(OffsetType offsetType, MouseEvent event) {

        if (sundial == null || event == null) { return; }

        if (getLastButton().equals(MouseButton.MIDDLE)) { return; }

        double mouseX = event.getScreenX();
        double mouseY = event.getScreenY();

        double deltaMouseX = mouseX - savedMouseX;
        double deltaMouseY = mouseY - savedMouseY;

        int offsetFactor;

        double stepSize = Sunconfig.NORMAL_STEP_SIZE;
        if (event.isSecondaryButtonDown()) { stepSize = Sunconfig.FAST_STEP_SIZE; }

        if (deltaMouseY >= stepSize) {
            offsetFactor = -1;
            savedMouseY = mouseY;
        } else if (deltaMouseY <= -1 * stepSize) {
            offsetFactor = 1;
            savedMouseY = mouseY;
        } else {
            return;
        }

        offsetTimeAction(offsetType, offsetFactor);
    }

    private void offsetTimeAction(OffsetType offsetType, int offsetFactor) {

        int offsetYear = 0;
        int offsetMonth = 0;
        int offsetDay = 0;
        int offsetHour = 0;
        int offsetMinute = 0;
        int offsetSecond = 0;
        int offsetWeek = 0;

        switch (offsetType) {
            case YEAR   : offsetYear = offsetFactor; break;
            case MONTH  : offsetMonth = offsetFactor; break;
            case DAY    : offsetDay = offsetFactor; break;
            case HOUR   : offsetHour = offsetFactor; break;
            case MINUTE : offsetMinute = offsetFactor; break;
            case SECOND : offsetSecond = offsetFactor; break;
            case WEEK   : offsetWeek = offsetFactor; break;
            default: {}
        }

        offsetLocalTime.set(
                offsetLocalTime.get(Calendar.YEAR) + offsetYear,
                offsetLocalTime.get(Calendar.MONTH) + offsetMonth,
                offsetLocalTime.get(Calendar.DAY_OF_MONTH) + offsetDay,
                offsetLocalTime.get(Calendar.HOUR_OF_DAY),
                offsetLocalTime.get(Calendar.MINUTE),
                offsetLocalTime.get(Calendar.SECOND)
        );

        offsetLocalTime.setTimeInMillis(
                offsetLocalTime.getTimeInMillis()
                + offsetWeek * (7 * 24 * 60 * 60 * 1000)
                + offsetHour * (60 * 60 * 1000)
                + offsetMinute * (60 * 1000)
                + offsetSecond * 1000
        );

        initCurrentTime();
    }

    private void resetGlobePosition(Sundial sundial, PositionType type) {

        if (type == PositionType.LONGITUDE) {
            longitude = customLongitude;
        } else if (type == PositionType.LATITUDE) {
            latitude = customLatitude;
        } else {
            longitude = customLongitude;
            latitude = customLatitude;
        }

        if (sundial.getGlobeAnimationEh()) {
            sundial.getLongitudeTimeline().setOnFinished(event -> initCurrentTime());
            sundial.rotateGlobeAnimated(longitude, latitude);
        } else {
            sundial.rotateGlobe(longitude, latitude);
            initCurrentTime();
        }
    }

    private class SleepTask extends Task<Boolean> {

        Sundial sundial;
        long millis;

        public SleepTask(Sundial sundial, long millis) {
            this.sundial = sundial;
            this.millis = millis;
        }

        @Override
        protected Boolean call() {

            try {
                Thread.sleep(millis);
            } catch (InterruptedException e) {
                debugTextArea.setText("Error while calling sleep for InfoText:\n" + e.getMessage());
            }

            return true;
        }
    }

    private void moveWindowToScreenCenter(Stage stage) {

        Rectangle2D currentScreen = getCurrentScreen(stage);
        if (currentScreen == null) { return; }

        double windowSizeX = stage.getWidth();
        double windowSizeY = stage.getHeight();

        double currentScreenMinX = currentScreen.getMinX();
        double currentScreenMaxX = currentScreen.getMaxX();
        double currentScreenMinY = currentScreen.getMinY();
        double currentScreenMaxY = currentScreen.getMaxY();

        double screenCenterX = currentScreenMinX + (currentScreenMaxX - currentScreenMinX) / 2;
        double screenCenterY = currentScreenMinY + (currentScreenMaxY - currentScreenMinY) / 2;

        double newPositionX = screenCenterX - windowSizeX / 2;
        double newPositionY = screenCenterY - windowSizeY / 2;

        stage.setX(newPositionX);
        stage.setY(newPositionY);
    }

    private void resetWindowSize(Stage stage, WindowType windowType) {

        if (windowType.equals(WindowType.PRIMARY)) {
            stage.setWidth(sundial.getDialsGroup().getLayoutBounds().getWidth());
            stage.setHeight(sundial.getDialsGroup().getLayoutBounds().getHeight());
        }

        if (windowType.equals(WindowType.CHART)) {
            stage.setWidth(sunyearDefaultWidth);
            stage.setHeight(sunyearDefaultHeight);
        }

        if (snapToCenterEh && inCenterEh) {
            moveWindowToScreenCenter(stage);
        }
    }

    private Rectangle2D getCurrentScreen(Stage stage) {

        Rectangle2D currentScreen;

        Rectangle2D recCenterOfPointer = new Rectangle2D(stage.getX() + stage.getWidth() / 2, stage.getY() + stage.getHeight() / 2, 0, 0);

        if (Screen.getScreensForRectangle(recCenterOfPointer).size() > 0) {
            currentScreen = Screen.getScreensForRectangle(recCenterOfPointer).get(0).getVisualBounds();
        } else {
            return null;
        }

        return currentScreen;
    }

    private void toggleDebugWindow(Stage stage) {

        if (debugWindow.isShowing()) {
            debugWindow.close();
        } else {
            debugWindow.show();
        }
    }

    private void toggleSunchartWindow() {

        if (sunchartWindow.isShowing()) {
            sunchartWindow.close();
        } else {
            sunyear.setSpaceTime(longitude, latitude, offsetLocalTime, timeZoneOffset);
            sunchartWindow.show();
        }
    }

    private void refreshKriegsrahmenZeit(KriegsrahmenZeit.Location location, MouseEvent mouseEvent) {

        sundial.moveGroup(sundial.getInfoTextGroup(), mouseEvent, Sundial.MouseCatcher.SCENE);

        KriegsrahmenZeit kriegsrahmenZeit;

        switch (location) {
            case CETUS: kriegsrahmenZeit = cetusTime; break;
            case ORB_VALLIS: kriegsrahmenZeit = orbVallisTime; break;
            default: return;
        }

        RefreshKriegsrahmenZeitDataTask refreshDataTask = new RefreshKriegsrahmenZeitDataTask(kriegsrahmenZeit);

        refreshDataTask.setOnScheduled(refreshEvent -> {
            sundial.getInfoText().setText("Syncing with " + location.getFullName() + "...");
            showInfoText(mouseEvent);
        });

        refreshDataTask.setOnFailed(refreshEvent -> {
            sundial.getInfoText().setText(kriegsrahmenZeit.getShortResult());
            showInfoText(mouseEvent);
            hideInfoTextWithDelay();
        });

        refreshDataTask.setOnSucceeded(refreshEvent -> {
            sundial.getInfoText().setText(kriegsrahmenZeit.getShortResult());
            showKriegsrahmenZeit(location, mouseEvent);
            hideInfoTextWithDelay();
        });

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(refreshDataTask);
        executorService.shutdown();
    }

    private void showInfoText(MouseEvent event) {
        sundial.moveGroup(sundial.getInfoTextGroup(), event, Sundial.MouseCatcher.SCENE);
        showInfoText();
    }

    private void showInfoText(DragEvent event) {
        sundial.moveGroup(sundial.getInfoTextGroup(), event, Sundial.MouseCatcher.SCENE);
        showInfoText();
    }

    private void showInfoText() {
        sundial.getInfoTextOpacityTimeline().stop();
        sundial.getInfoTextGroup().setOpacity(1);
        sundial.getInfoTextGroup().setVisible(true);
    }

    private void hideInfoTextWithDelay() {

        // Keep Info Text visible for some time then fade out

        if (sundial.getInfoTextGroup().isVisible()) {

            SleepTask sleepTask = new SleepTask(sundial, 2000);

            sleepTask.setOnSucceeded(event -> sundial.fadeOutInfoText(3000));

            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(sleepTask);
            executorService.shutdown();
        }
    }

    private void showKriegsrahmenZeit(KriegsrahmenZeit.Location location, MouseEvent mouseEvent) {

        KriegsrahmenZeit kriegsrahmenZeit;
        ArrayList<ArrayList<GregorianCalendar>> cycleList;

        switch (location) {
            case CETUS:
                kriegsrahmenZeit = cetusTime;
                if (cetusCycleList.isEmpty()) { cetusCycleList = kriegsrahmenZeit.getCycleList(offsetLocalTime); }
                cycleList = cetusCycleList;
                break;
            case ORB_VALLIS:
                kriegsrahmenZeit = orbVallisTime;
                if (orbVallisCycleList.isEmpty()) { orbVallisCycleList = kriegsrahmenZeit.getCycleList(offsetLocalTime); }
                cycleList = orbVallisCycleList;
                break;
            default: return;
        }

        if (kriegsrahmenZeit.getStatusOkEh()) {
            sundial.setKriegsrahmenZeit(location, cycleList, timeZonedCalendar, timeZoneCorrection);
            sundial.updateKriegsrahmenTimer(location, cycleList);
            sundial.setKriegsrahmenTimeVisibility(location, true);
        } else {
            sundial.getInfoText().setText(location.getFullName() + " time unavailable: \n" + kriegsrahmenZeit.getShortResult());
            sundial.moveGroup(sundial.getInfoTextGroup(), mouseEvent, Sundial.MouseCatcher.LOCAL);
            showInfoText(mouseEvent);
        }

        hideInfoTextWithDelay();

        updateDebugWindow(sundial);
    }

    private class RefreshKriegsrahmenZeitDataTask extends Task<Boolean> {

        KriegsrahmenZeit kriegsrahmenZeit;

        public RefreshKriegsrahmenZeitDataTask(KriegsrahmenZeit kriegsrahmenZeit) {
            this.kriegsrahmenZeit = kriegsrahmenZeit;
        }

        @Override
        protected Boolean call() {
            this.kriegsrahmenZeit.requestNewData();
            return kriegsrahmenZeit.getStatusOkEh();
        }
    }

    private void updateSunchart(Sunchart sunchart) {

        if (sunchartWindow.isShowing() &&
                (
                        longitude != sunyear.getLongitude() ||
                        latitude != sunyear.getLatitude() ||
                        offsetLocalTime.get(Calendar.YEAR) != sunyear.getYear() ||
                        timeZoneOffset != sunyear.getTimeZoneOffset() ||
                        ! offsetLocalTime.getTimeZone().equals(sunyear.getTimeZone())
                )
        ) {

            sunyear.setTimeZone(offsetLocalTime.getTimeZone());
            sunyear.setSpaceTime(longitude, latitude, offsetLocalTime, timeZoneOffset);
        }
    }

    private void setWarnings() {
        sundial.setCustomTimeWarning(offsetLocalTime.getTimeInMillis() != currentLocalTime.getTimeInMillis());
        sundial.setCustomTimezoneWarning(timeZoneOffset != localTimeZoneOffset);
        sundial.setCustomLongitudeWarning(longitude != customLongitude);
        sundial.setCustomLatitudeWarning(latitude != customLatitude);
    }

    // ***************************************************************
    // *** DEBUG window contents ***

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

        for (String key : cetusTime.getDataMap().keySet()) {
            cetusDataString.append(key).append(" = ").append(cetusTime.getDataMap().get(key)).append("\n");
        }

        StringBuilder cetusNightListString = new StringBuilder();
        for(int i = 0; i < cetusCycleList.size(); i++) {
            String nightStart = cetusCycleList.get(i).get(0).getTime().toString();
            String nightEnd = cetusCycleList.get(i).get(1).getTime().toString();
            cetusNightListString.append("\nnight ").append(i+1).append(": start = ").append(nightStart).append(", end = ").append(nightEnd);
        }

        StringBuilder orbVallisDataString = new StringBuilder();

        for (String key : orbVallisTime.getDataMap().keySet()) {
            orbVallisDataString.append(key).append(" = ").append(orbVallisTime.getDataMap().get(key)).append("\n");
        }

        StringBuilder orbVallisNightListString = new StringBuilder();
        for(int i = 0; i < orbVallisCycleList.size(); i++) {
            String nightStart = orbVallisCycleList.get(i).get(0).getTime().toString();
            String nightEnd = orbVallisCycleList.get(i).get(1).getTime().toString();
            orbVallisNightListString.append("\nwarm ").append(i+1).append(": start = ").append(nightStart).append(", end = ").append(nightEnd);
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
                + "Local time               : " + offsetLocalTime.getTime().toString() + "\n"
                + "Day of the year          : " + offsetLocalTime.get(Calendar.DAY_OF_YEAR) + "\n"
                + "Julian Date              : " + Sunconfig.julianDateFormat.format(julianDate) + " (UTC)" + "\n"
                + "Gregorian Date           : " + Suntime.getCalendarDate(julianDate, offsetLocalTime.getTimeZone()).getTime().toString() + "\n"
                + "Julian Day Number        : " + julianDayNumber + "\n"
                + "\n"
                + "Longitude                : " + longitude + "\n"
                + "Latitude                 : " + latitude + "\n"
                + "TimeZone String          : " + timeZoneString + "\n"
                + "High Noon                : " + highNoonDate.getTime().toString() + "\n"
                + "Sunrise                  : " + sunriseDate.getTime().toString() + "\n"
                + "Sunset                   : " + sunsetDate.getTime().toString() + "\n"
                + "Day Length               : " + Suntime.printSecondsToTime(Suntime.convertFractionToSeconds(dayLength)) + "\n"
                + "\n"
                + "meanAnomaly              = " + suntimeLocal.getMeanAnomaly() + "\n"
                + "equationOfCenter         = " + suntimeLocal.getEquationOfCenter() + "\n"
                + "eclipticalLongitude      = " + suntimeLocal.getEclipticalLongitude() + "\n"
                + "rightAscension           = " + suntimeLocal.getRightAscension() + "\n"
                + "declinationOfTheSun      = " + suntimeLocal.getDeclinationOfTheSun() + "\n"
                + "siderealTime             = " + suntimeLocal.getSiderealTime() + "\n"
                + "hourAngle                = " + suntimeLocal.getHourAngle() + "\n"
                + "solarTransit             = " + suntimeLocal.getSolarTransit() + "\n"
                + "localHourAngle           = " + suntimeLocal.getLocalHourAngle() + "\n"
//                + "localHourAngle dividend  = " + dividend + "\n"
//                + "localHourAngle divisor   = " + divisor + "\n"
                + "\n"
                + "Cetus okEh = " + cetusTime.getStatusOkEh() + "\n"
//                + "Cetus result = " + cetusTime.getResult() + "\n"
//                + "Cetus shortResult = " + cetusTime.getShortResult() + "\n"
//                + "Cetus data expired = " + cetusTime.dataExpiredEh() + "\n"
//                + "Cetus expiry calendar = " + cetusTime.getExpiry().getTime() + "\n"
//                + "Cetus reloadCounter: " + cetusTime.getReloadCounter() + "\n"
                + "Cetus dataMap: \n" + cetusDataString
                + "\n"
                + "Orb Vallis okEh = " + orbVallisTime.getStatusOkEh() + "\n"
//                + "Orb Vallis result = " + orbVallisTime.getResult() + "\n"
//                + "Orb Vallis shortResult = " + orbVallisTime.getShortResult() + "\n"
//                + "Orb Vallis data expired = " + orbVallisTime.dataExpiredEh() + "\n"
//                + "Orb Vallis expiry calendar = " + orbVallisTime.getExpiry().getTime() + "\n"
//                + "Orb Vallis reloadCounter: " + orbVallisTime.getReloadCounter() + "\n"
                + "Orb Vallis dataMap: \n" + orbVallisDataString
                ;

        if (debugErrorMessage != null && !debugErrorMessage.isEmpty()) {
            debugText = debugErrorMessage + "\n" + debugText;
        }

        debugTextArea.setText(debugText);
    }


    // ***************************************************************
    // *** EVENT METHODS ***


    // *** Mouse HELPERS

    private void globeCheck() {
        if (sundial.getGlobeVisibleEh()) {
            if (getLastButton().equals(MouseButton.PRIMARY) || getLastButton().equals(MouseButton.SECONDARY)) {
                sundial.setTimeDisplayOpacity(Sunconfig.TIMEDATE_TRANSPARENT_OPACITY);
            } else {
                sundial.setTimeDisplayOpacity(Sunconfig.TIMEDATE_DEFAULT_OPACITY);
            }
        }
    }

    private void saveMouse(Stage stage, MouseEvent event) {

        killMouse();

        if (event != null) {

            mouseButtonList.add(event.getButton());

            savedMouseX = event.getScreenX();
            savedMouseY = event.getScreenY();
            savedNode = event.getPickResult().getIntersectedNode();
        }

        savedWindowPositionX = stage.getX();
        savedWindowPositionY = stage.getY();

        savedWindowSizeX = stage.getWidth();
        savedWindowSizeY = stage.getHeight();

        deltaX = stage.getX() - savedMouseX;
        deltaY = stage.getY() - savedMouseY;
    }

    private void killMouse() {
        mouseButtonList.clear();
    }

    private boolean sameNodeEh(MouseEvent event) {
        Node pickedNode = event.getPickResult().getIntersectedNode();
        return (pickedNode != null && pickedNode.equals(savedNode));
    }

    private MouseButton getLastButton() {
        if (!mouseButtonList.isEmpty()) {
            return mouseButtonList.get(mouseButtonList.size() - 1);
        } else {
            return MouseButton.NONE;
        }
    }


    // *** Mouse ACTIONS (onMouseRelease)

    private void minimizeActions(Stage stage, MouseEvent event) {

        // Do no action if mouse left original control surface (node)
        if (!sameNodeEh(event)) { return; }

        // LMB action -> minimize stage
        if (getLastButton().equals(MouseButton.PRIMARY)) {
            stage.setIconified(true);
            return;
        }

        // RMB action -> toggle debug window
        if (getLastButton().equals(MouseButton.SECONDARY)) {
            toggleDebugWindow(stage);
            return;
        }
    }

    private void resizeActions(Stage stage, WindowType windowType, MouseEvent event) {

        // Do no action if mouse left original control surface (node)
        if (!sameNodeEh(event)) { return; }

        // MMB action -> reset window size
        if(getLastButton().equals(MouseButton.MIDDLE)) {
            resetWindowSize(stage, windowType);
            return;
        }
    }

    private void nightCompressionActions(Stage stage, MouseEvent event) {

        // Do no action if mouse left original control surface (node)
        if (!sameNodeEh(event)) { return; }

/*
        // LMB action -> toggleDST
        if(getLastButton().equals(MouseButton.PRIMARY)) {
            toggleDst();
            return;
        }
*/

        // MMB action -> reset window size
        if(getLastButton().equals(MouseButton.MIDDLE)) {
            sundial.resetNightCompression();
            return;
        }
    }

    private void frameActions(Stage stage, MouseEvent event) {

        // Do no action if mouse left original control surface (node)
        if (!sameNodeEh(event)) { return; }

        // LMB action -> update Sunchart
        if (getLastButton().equals(MouseButton.PRIMARY)) {
            updateSunchart(sunchart);
        }

        // RMB action -> update Sunchart
        if (getLastButton().equals(MouseButton.SECONDARY)) {
            updateSunchart(sunchart);
        }

        // MMB action -> reset globe coordinates
        if (getLastButton().equals(MouseButton.MIDDLE)) {
            resetGlobePosition(sundial, PositionType.BOTH);
        }
    }

    private void timeZoneActions(Sundial sundial, MouseEvent event) {

        // Do no action if mouse left original control surface (node)
        if (!sameNodeEh(event)) { return; }

        // MMB action -> reset time zone to local time zone
        if (getLastButton().equals(MouseButton.MIDDLE)) {

            TimeZone localTimeZone = (new GregorianCalendar()).getTimeZone();

            timeZoneOffset = localTimeZone.getRawOffset();
            currentLocalTime.getTimeZone().setRawOffset(timeZoneOffset);
            offsetLocalTime.getTimeZone().setRawOffset(timeZoneOffset);

            initCurrentTime();
        }
    }

    private void coordinateActions(Stage stage, PositionType positionType, MouseEvent event) {

        if (sundial.getGlobeVisibleEh()) { sundial.setTimeDisplayOpacity(Sunconfig.TIMEDATE_DEFAULT_OPACITY); }

        // Do no action if mouse left original control surface (node)
        if (!sameNodeEh(event)) { return; }

        // LMB action -> update Sunchart
        if (getLastButton().equals(MouseButton.PRIMARY)) {
            updateSunchart(sunchart);
        }

        // RMB action -> update Sunchart
        if (getLastButton().equals(MouseButton.SECONDARY)) {
            updateSunchart(sunchart);
        }

        // MMB action -> reset globe coordinates
        if (getLastButton().equals(MouseButton.MIDDLE)) {
            resetGlobePosition(sundial, positionType);
        }
    }

    private void timeControlActions(MouseEvent event) {

        // Do no action if mouse left original control surface (node)
        if (!sameNodeEh(event)) { return; }

        // MMB action -> reset time to local time
        if (getLastButton().equals(MouseButton.MIDDLE)) {
            resetTime();
            return;
        }

        updateSunchart(sunchart);
    }


    // *** Mouse CLICK

    private void toggleDst() {

        TimeZone timeZone;

        sundial.getControlThingyDst().toggleState();

        if (sundial.getControlThingyDst().getState()) {
            timeZone = (new GregorianCalendar()).getTimeZone();
        } else {
            timeZone = TimeZone.getTimeZone("UTC");
        }

        currentLocalTime.setTimeZone(timeZone);
        offsetLocalTime.setTimeZone(timeZone);

        currentLocalTime.get(Calendar.HOUR_OF_DAY);
        offsetLocalTime.get(Calendar.HOUR_OF_DAY);

//        int rawOffset= timeZone.getRawOffset();

        currentLocalTime.getTimeZone().setRawOffset(timeZoneOffset);
        offsetLocalTime.getTimeZone().setRawOffset(timeZoneOffset);

        initCurrentTime();
    }

    private void toggleAlwaysOnTop(Stage stage) {
        sundial.getControlThingyAlwaysOnTop().toggleState();
        stage.setAlwaysOnTop(sundial.getControlThingyAlwaysOnTop().getState());
    }

    private boolean getKriegsrahmenZeitDataExpired(KriegsrahmenZeit.Location location) {
        switch (location) {
            case CETUS: return cetusTime.dataExpiredEh();
            case ORB_VALLIS: return orbVallisTime.dataExpiredEh();
            default: return false;
        }
    }

    private void toggleKriegsrahmenZeit(KriegsrahmenZeit.Location location, MouseEvent mouseEvent) {

        if (sundial.getKriegsrahmenZeitVisibleEh(location)) {
            sundial.setKriegsrahmenTimeVisibility(location,false);
        } else {

            if (getKriegsrahmenZeitDataExpired(location)) {
                refreshKriegsrahmenZeit(location, mouseEvent);
            } else {
                showKriegsrahmenZeit(location, mouseEvent);
            }
        }
    }

    private void tinyGlobeActions(MouseEvent mouseEvent) {

        // LMB action (toggle Globe)
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
            sundial.toggleGlobeVisibility();
            return;
        }

        // MMB action (reset Coordinates)
        if (mouseEvent.getButton().equals(MouseButton.MIDDLE)) {
            resetGlobePosition(sundial, PositionType.BOTH);
            return;
        }
    }

    private void maximizeActions(Stage stage, WindowType windowType) {

        double maxWidth, maxHeight;
        double screenWidth, screenHeight;

        Rectangle2D currentScreen = getCurrentScreen(stage);
        if (currentScreen == null) { return; }

        screenWidth = currentScreen.getMaxX() - currentScreen.getMinX();
        screenHeight = currentScreen.getMaxY() - currentScreen.getMinY();

        maxWidth = screenWidth;
        maxHeight = screenHeight;

        double aspectRatio = 1;

        if (windowType.equals(WindowType.CHART)) {
            aspectRatio = sunyearDefaultWidth / sunyearDefaultHeight;
        }

        if (windowType.equals(WindowType.PRIMARY)) {
            aspectRatio = Sundial.DEFAULT_WIDTH / Sundial.DEFAULT_HEIGHT;
        }

        double currentScreenAspectRatio = screenWidth / screenHeight;

        if (maximizedEh.get(windowType)) {

            stage.setX(unmaximizedWindowPositionX.get(windowType));
            stage.setY(unmaximizedWindowPositionY.get(windowType));

            stage.setWidth(unmaximizedWindowSizeX.get(windowType));
            stage.setHeight(unmaximizedWindowSizeY.get(windowType));

            maximizedEh.put(windowType, false);

        } else {

            unmaximizedWindowPositionX.put(windowType, stage.getX());
            unmaximizedWindowPositionY.put(windowType, stage.getY());

            unmaximizedWindowSizeX.put(windowType, stage.getWidth());
            unmaximizedWindowSizeY.put(windowType, stage.getHeight());

            if (aspectRatio >= currentScreenAspectRatio) {
                maxHeight = maxWidth / aspectRatio;
            } else {
                maxWidth = maxHeight * aspectRatio;
            }

            stage.setX(currentScreen.getMaxX() - screenWidth / 2 - maxWidth / 2);
            stage.setY(currentScreen.getMaxY() - screenHeight / 2 - maxHeight / 2);

            stage.setWidth(maxWidth);
            stage.setHeight(maxHeight);

            maximizedEh.put(windowType, true);
        }

        if (windowType.equals(WindowType.PRIMARY)) {
            sundial.getControlThingyMaximize().toggleState();
        }

        if (windowType.equals(WindowType.CHART)) {
            sunyear.getControlThingyMaximize().toggleState();
        }
    }


    // *** Mouse DRAG

    private void checkDragAndDropString(DragEvent event) {

        if (event.getGestureSource() != sundial.getTinyGlobeGroup() && event.getDragboard().hasString()) {
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }

        event.consume();
    }

    private void resizeWindow(Stage stage, WindowType windowType, MouseEvent event) {

        if(getLastButton().equals(MouseButton.MIDDLE)) { return; }

        double aspectRatio = 1;

        if (windowType.equals(WindowType.CHART)) {
            aspectRatio = sunyearDefaultWidth / sunyearDefaultHeight;
        }

        if (windowType.equals(WindowType.PRIMARY)) {
            aspectRatio = Sundial.DEFAULT_WIDTH / Sundial.DEFAULT_HEIGHT;
        }

        double mouseX = event.getScreenX();
        double mouseY = event.getScreenY();

        double deltaSizeX = mouseX - savedMouseX;
        double deltaSizeY = mouseY - savedMouseY;

        if (snapToCenterEh && inCenterEh) {
            deltaSizeX *= 2;
            deltaSizeY *= 2;
        }

        double windowSizeX = savedWindowSizeX + deltaSizeX;
        double windowSizeY = savedWindowSizeY + deltaSizeY;

        double minWidth, minHeight, maxWidth, maxHeight;
        double currentScreenMinX, currentScreenMaxX, currentScreenMinY, currentScreenMaxY, currentScreenAspectRatio;

        minWidth = Sunconfig.MIN_WIDTH * aspectRatio;
        minHeight = Sunconfig.MIN_HEIGHT;

        Rectangle2D recCenterOfPointer = new Rectangle2D(savedWindowPositionX + savedWindowSizeX / 2, savedWindowPositionY + savedWindowSizeY / 2, 0, 0);

        if (Screen.getScreensForRectangle(recCenterOfPointer).size() > 0) {

            Rectangle2D currentScreen = Screen.getScreensForRectangle(recCenterOfPointer).get(0).getVisualBounds();

            currentScreenMinX = currentScreen.getMinX();
            currentScreenMinY = currentScreen.getMinY();
            currentScreenMaxX = currentScreen.getMaxX();
            currentScreenMaxY = currentScreen.getMaxY();

            maxWidth = currentScreen.getMaxX() - stage.getX();
            maxHeight = currentScreen.getMaxY() - stage.getY();

        } else {

            currentScreenMinX = 0;
            currentScreenMinY = 0;
            currentScreenMaxX = windowSizeX;
            currentScreenMaxY = windowSizeY;

            maxWidth = windowSizeX;
            maxHeight = windowSizeY;
        }

        currentScreenAspectRatio = maxWidth / maxHeight;

        if (event.isPrimaryButtonDown()) {

            if (aspectRatio >= currentScreenAspectRatio) {
                maxHeight = maxWidth / aspectRatio;
            } else {
                maxWidth = maxHeight * aspectRatio;
            }

            if (windowSizeX / aspectRatio >= windowSizeY) {
                windowSizeX = windowSizeY * aspectRatio;
            }
            if (windowSizeY * aspectRatio >= windowSizeX) {
                windowSizeY = windowSizeX / aspectRatio;
            }
        }

        if (windowSizeX < minWidth) { windowSizeX = minWidth; }
        if (windowSizeY < minHeight) { windowSizeY = minHeight; }
        if (windowSizeX > maxWidth) { windowSizeX = maxWidth; }
        if (windowSizeY > maxHeight) { windowSizeY = maxHeight; }

        if (snapToCenterEh && inCenterEh) {
            moveWindowToScreenCenter(stage);
        }

        stage.setWidth(windowSizeX);
        stage.setHeight(windowSizeY);

        updateDebugWindow(sundial);
    }

    private void nightCompressionDrag(Sundial sundial, MouseEvent event) {

        if (suntimeLocal == null || sundial == null || event == null) { return; }

        if (getLastButton().equals(MouseButton.MIDDLE)) { return; }

        double mouseX = event.getScreenX();
        double mouseY = event.getScreenY();

        double deltaMouseX = mouseX - savedMouseX;
        double deltaMouseY = mouseY - savedMouseY;

        double stepSize = Sunconfig.NORMAL_STEP_SIZE;
        if (event.isSecondaryButtonDown()) { stepSize = Sunconfig.FAST_STEP_SIZE; }

        if (abs(deltaMouseY) >= stepSize) {
            if (deltaMouseY > 0) {
                sundial.increaseNightCompression();
            } else {
                sundial.decreaseNightCompression();
            }
            savedMouseY = mouseY;
        }
    }

    private void frameDrag(Stage stage, MouseEvent event) {

        if (sundial.getGlobeVisibleEh()) {
            rotateGlobe(sundial, event, 1.0d);
        } else {
            changeWindowPosition(stage, event);
        }
    }

    private void tinyGlobeDrag(MouseEvent event) {

        if (!mouseButtonList.isEmpty() && mouseButtonList.contains(MouseButton.SECONDARY)) {
            rotateGlobe(sundial, event, Sunconfig.TINYGLOBE_ROTATE_MODIFIER);
        }
    }

    private void changeWindowPosition(Stage stage, MouseEvent event) {

        if(!getLastButton().equals(MouseButton.PRIMARY)) { return; }

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

        double screenCenterX = currentScreenMinX + (currentScreenMaxX - currentScreenMinX) / 2;
        double screenCenterY = currentScreenMinY + (currentScreenMaxY - currentScreenMinY) / 2;

        debugTextArea.setText(""
                + "\nnewPositionX = " + newPositionX
                + "\nnewPositionY = " + newPositionY
                + "\nscreenCenterX = " + screenCenterX
                + "\nscreenCenterY = " + screenCenterY
                + "\ncenterPositionX = " + centerPositionX
                + "\ncenterPositionY = " + centerPositionY
        );

        // snap to screen center
        if (snapToCenterEh) {

            inCenterEh = false;

            if (abs(abs(screenCenterX) - abs(centerPositionX)) < Sunconfig.SNAP_TO_CENTER_RADIUS && abs(abs(screenCenterY) - abs(centerPositionY)) < Sunconfig.SNAP_TO_CENTER_RADIUS) {
                newPositionX = screenCenterX - winSizeX / 2;
                newPositionY = screenCenterY - winSizeY / 2;
                inCenterEh = true;
            }
        }

        // stop at screen border
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

    private void rotateGlobe(Sundial sundial, MouseEvent event, double precisionModifier) {

        if(getLastButton().equals(MouseButton.MIDDLE)) { return; }

        double mouseScreenX = event.getScreenX();
        double mouseScreenY = event.getScreenY();

        double deltaLongitude = savedMouseX - mouseScreenX;
        double deltaLatitude = savedMouseY - mouseScreenY;

        savedMouseX = mouseScreenX;
        savedMouseY = mouseScreenY;

        double precision = Sunconfig.GLOBE_ROTATE_PRECISION_NORMAL * precisionModifier;

        if (event.isSecondaryButtonDown()) {
            precision = Sunconfig.GLOBE_ROTATE_PRECISION_FINE * precisionModifier;
        }

        longitude += round((deltaLongitude / precision) * 100) / 100d;
        latitude -= round((deltaLatitude / precision) * 100) / 100d;

        if (longitude < Suntime.MIN_LONGITUDE) { longitude = Suntime.MAX_LONGITUDE - (Suntime.MIN_LONGITUDE - longitude); }
        if (longitude > Suntime.MAX_LONGITUDE) { longitude = Suntime.MIN_LONGITUDE - (Suntime.MAX_LONGITUDE - longitude); }
        if (latitude < Suntime.MIN_LATITUDE) { latitude = Suntime.MIN_LATITUDE; }
        if (latitude > Suntime.MAX_LATITUDE) { latitude = Suntime.MAX_LATITUDE; }

        initCurrentTime();
        sundial.rotateGlobe(longitude, latitude);
    }

    private void rotateGlobe(PositionType positionType, DragEvent dragEvent) {

        if (positionType != PositionType.GOOGLE_MAPS) { return; }

        String string;

        if (dragEvent.getDragboard().hasString()) {
            string = dragEvent.getDragboard().getString();
        } else {
            return;
        }

        Pattern pattern = Pattern.compile(Sunconfig.GOOGLEMAPS_REGEX);
        Matcher matcher = pattern.matcher(string);

        if (matcher.matches()) {
            try {

                latitude = Double.parseDouble(matcher.group(1));
                longitude = Double.parseDouble(matcher.group(2));

                customLongitude = longitude;
                customLatitude = latitude;

            } catch (NumberFormatException e) {
                sundial.getInfoText().setText("Catasptrophic error while parsing coordinates!\nPlease don't try again.");
                sundial.moveGroup(sundial.getInfoTextGroup(), dragEvent, Sundial.MouseCatcher.LOCAL);
                showInfoText(dragEvent);
                debugErrorMessage = "NumberFormatException while parsing string: " + string + "\n" + e.getMessage();
            }
        } else {
            sundial.getInfoText().setText("Unable to match coordinates.\nPlease try again.");
            sundial.moveGroup(sundial.getInfoTextGroup(), dragEvent, Sundial.MouseCatcher.LOCAL);
            showInfoText(dragEvent);
        }

        hideInfoTextWithDelay();

        initCurrentTime();
        sundial.rotateGlobeAnimated(longitude, latitude);
    }

    private void rotateGlobe(Sundial sundial, PositionType positionType, MouseEvent event) {

        if(getLastButton().equals(MouseButton.MIDDLE)) { return; }

        double mouseScreenX = event.getScreenX();
        double mouseScreenY = event.getScreenY();
        double deltaMouse;

        if (positionType == PositionType.LONGITUDE) {
            deltaMouse = savedMouseX - mouseScreenX;
        } else {
            deltaMouse = savedMouseY - mouseScreenY;
        }

        savedMouseX = mouseScreenX;
        savedMouseY = mouseScreenY;

        double precision = 4;
        if (event.isSecondaryButtonDown()) { precision = 100; }

        double delta = round((deltaMouse / precision) * 100) / 100d;

        if (positionType == PositionType.LONGITUDE) {
            longitude += delta;
        } else {
            latitude -= delta;
        }

        if (longitude < Suntime.MIN_LONGITUDE) { longitude = Suntime.MAX_LONGITUDE - (Suntime.MIN_LONGITUDE - longitude); }
        if (longitude > Suntime.MAX_LONGITUDE) { longitude = Suntime.MIN_LONGITUDE - (Suntime.MAX_LONGITUDE - longitude); }
        if (latitude < Suntime.MIN_LATITUDE) { latitude = Suntime.MIN_LATITUDE; }
        if (latitude > Suntime.MAX_LATITUDE) { latitude = Suntime.MAX_LATITUDE; }

        initCurrentTime();
        sundial.rotateGlobe(longitude, latitude);
    }


    // *** Mouse SCROLL

    private void nightCompressionDrag(Sundial sundial, ScrollEvent event) {

        if (suntimeLocal == null || sundial == null || event == null) { return; }

        if (!mouseButtonList.isEmpty()) { return; }

        if (event.getDeltaY() < 0) {
            sundial.increaseNightCompression();
        } else {
            sundial.decreaseNightCompression();
        }
    }

    private void timeZoneScroll(Sundial sundial, ScrollEvent event) {

        if (sundial == null || event == null) { return; }

        if (!mouseButtonList.isEmpty()) { return; }

        if (event.getDeltaY() < 0) {
            timeZoneOffset -= (60 * 60 * 1000);
        }
        else if (event.getDeltaY() > 0) {
            timeZoneOffset += (60 * 60 * 1000);
        }
        else { return; }

        timeZoneOffset = (int) Sunutil.rotateTimeZoneOffset(timeZoneOffset);

        currentLocalTime.getTimeZone().setRawOffset(timeZoneOffset);
        offsetLocalTime.getTimeZone().setRawOffset(timeZoneOffset);

        initCurrentTime();
    }

    private void rotateGlobe(Sundial sundial, PositionType type, ScrollEvent event) {

        if(getLastButton().equals(MouseButton.MIDDLE)) {
            resetGlobePosition(sundial, type);
            return;
        }

        double step = 0;

        if (event.getDeltaY() < 0) { step = -0.01; }
        if (event.getDeltaY() > 0) { step = 0.01; }

        if (type == PositionType.LONGITUDE) { longitude += step; }
        else { latitude += step; }

        if (longitude < Suntime.MIN_LONGITUDE) { longitude = Suntime.MAX_LONGITUDE - (Suntime.MIN_LONGITUDE - longitude); }
        if (longitude > Suntime.MAX_LONGITUDE) { longitude = Suntime.MIN_LONGITUDE - (Suntime.MAX_LONGITUDE - longitude); }
        if (latitude < Suntime.MIN_LATITUDE) { latitude = Suntime.MIN_LATITUDE; }
        if (latitude > Suntime.MAX_LATITUDE) { latitude = Suntime.MAX_LATITUDE; }

        initCurrentTime();
        sundial.rotateGlobe(longitude, latitude);
    }
}
