import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.*;
import javafx.scene.Cursor;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.sql.Time;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

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

    private int fpsSetting = (int) floor(1000 / DEFAULT_FPS);

    private GregorianCalendar currentLocalTime;
    private GregorianCalendar offsetLocalTime;
    private long timeOffset;
    private double longitude = DEFAULT_LONGITUDE;
    private double latitude = DEFAULT_LATITUDE;
    private double customLongitude = DEFAULT_LONGITUDE;
    private double customLatitude = DEFAULT_LATITUDE;

    private int TYPE_LONGITUDE = 1;
    private int TYPE_LATITUDE = 2;

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

    private Text startupInfoText;
    private Text sunTimeInfoText;
    private Text calculatedInfoText;
    private TextArea debugTextArea;

    private Sunchart sunchart;

    private Stage debugWindow;
    private Stage statsWindow;

    private ArrayList<MouseButton> mouseButtonList = new ArrayList<>();


    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) {

        // Info text
        startupInfoText = new Text();
        startupInfoText.setFont(Font_Of_Info);
        startupInfoText.setLayoutX(5d);
        startupInfoText.setLayoutY(20d);

        sunTimeInfoText = new Text();
        sunTimeInfoText.setFont(Font_Of_Info);
        sunTimeInfoText.setLayoutX(5d);
        sunTimeInfoText.setLayoutY(525d);

        calculatedInfoText = new Text();
        calculatedInfoText.setFont(Font_Of_Info);
        calculatedInfoText.setLayoutX(5d);
        calculatedInfoText.setLayoutY(540d);

        // Sun objects
        currentLocalTime = new GregorianCalendar();
        offsetLocalTime = new GregorianCalendar();
        timeOffset = 0;

        Suntime suntime = new Suntime.Builder()
//                .julianDayNumber(2453097)
                .localTime(currentLocalTime)
                .observerLongitude(longitude)
                .observerLatitude(latitude)
                .build();

        Sundial sundial = new Sundial.Builder()
                .nightCompression(0)
                .build();

        sunchart = new Sunchart(longitude, latitude, currentLocalTime.get(Calendar.YEAR));

        debugTextArea = new TextArea();

        // Scene
        Group rootGroup = new Group();

        Group dialsGroup = new Group();
        dialsGroup.getChildren().add(sundial.getDialsGroup());
        sundial.getGlobe().rotateGlobe(longitude, latitude);
        sundial.getTinyGlobe().rotateGlobe(longitude, latitude);

        rootGroup.getChildren().addAll(dialsGroup/*, controlsGroup*/);

        double sizeX = rootGroup.getLayoutBounds().getWidth();
        double sizeY = rootGroup.getLayoutBounds().getHeight();

        ParallelCamera sceneCamera = new ParallelCamera();
//        sceneCamera.setFarClip(2000);

        Scene mainScene = new Scene(rootGroup, sizeX, sizeY, true, SceneAntialiasing.DISABLED);
        mainScene.setFill(Color.TRANSPARENT);
        mainScene.setCamera(sceneCamera);

        double dialsMinX = dialsGroup.getLayoutBounds().getMinX();
        double dialsMinY = dialsGroup.getLayoutBounds().getMinY();

        double dialsMaxX = dialsGroup.getLayoutBounds().getMaxX();
        double dialsMaxY = dialsGroup.getLayoutBounds().getMaxY();

        double dialsLayoutWidth = dialsGroup.getLayoutBounds().getWidth();
        double dialsLayoutHeight = dialsGroup.getLayoutBounds().getHeight();

        dialsGroup.setLayoutX(dialsLayoutWidth - dialsMaxX);
        dialsGroup.setLayoutY(dialsLayoutHeight - dialsMaxY);

        Scale dialsScale = new Scale();
        dialsScale.setPivotX(dialsMinX);
        dialsScale.setPivotY(dialsMinY);

        dialsGroup.getTransforms().add(dialsScale);

        // Debug window
        debugTextArea.setMinWidth(600);
        debugTextArea.setMinHeight(400);
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
        debugWindow.setResizable(false);
//        debugWindow.show();

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
                event -> updateCurrentTime(suntime, sundial));

        Timeline timeline = new Timeline(keyframeClockTick);
        timeline.setCycleCount(Animation.INDEFINITE);


        // Events
        sundial.getControlThingyClose().setOnMouseClicked(event -> System.exit(0));
        sundial.getControlThingyMaximize().setOnMouseClicked(event -> toggleMaximizeWindow(primaryStage, dialsScale, event));
        sundial.getControlThingyMinimize().setOnMouseClicked(event -> minimizeWindow(primaryStage, timeline, event));

        sundial.getDialCircleCenterDot().setOnMousePressed(event -> recordNightCompressionPosition(suntime, sundial, event));
        sundial.getDialCircleCenterDot().setOnMouseReleased(event -> mouseButtonList.clear());
        sundial.getDialCircleCenterDot().setOnMouseDragged(event -> changeNightCompression(suntime, sundial, event));
        sundial.getDialCircleCenterDot().setOnScroll(event -> changeNightCompression(suntime, sundial, event));

        sundial.getTinyGlobeGroup().setOnMousePressed(event -> mouseButtonList.add(event.getButton()));
        sundial.getTinyGlobeGroup().setOnMouseClicked(event -> toggleGlobe(primaryStage, suntime, sundial, dialsGroup, dialsScale));

        sundial.getLongitudeGroup().setOnMousePressed(event -> recordGlobePosition(suntime, sundial, TYPE_LONGITUDE, event));
        sundial.getLongitudeGroup().setOnMouseReleased(event -> mouseButtonList.clear());
        sundial.getLongitudeGroup().setOnMouseDragged(event -> rotateGlobe(suntime, sundial, TYPE_LONGITUDE, event));
        sundial.getLongitudeGroup().setOnScroll(event -> rotateGlobe(suntime, sundial, TYPE_LONGITUDE, event));

        sundial.getLatitudeGroup().setOnMousePressed(event -> recordGlobePosition(suntime, sundial, TYPE_LATITUDE, event));
        sundial.getLatitudeGroup().setOnMouseReleased(event -> mouseButtonList.clear());
        sundial.getLatitudeGroup().setOnMouseDragged(event -> rotateGlobe(suntime, sundial, TYPE_LATITUDE, event));
        sundial.getLatitudeGroup().setOnScroll(event -> rotateGlobe(suntime, sundial, TYPE_LATITUDE, event));

        sundial.getControlThingyResize().setOnMousePressed(event -> recordWindowSize(primaryStage, dialsGroup, dialsScale, event));
        sundial.getControlThingyResize().setOnMouseReleased(event -> mouseButtonList.clear());
        sundial.getControlThingyResize().setOnMouseDragged(event -> resizeWindow(primaryStage, dialsGroup, dialsScale, event));

        sundial.getBackgroundGroup().setOnMousePressed(event -> recordWindowPosition(primaryStage, dialsGroup, dialsScale, event));
        sundial.getBackgroundGroup().setOnMouseReleased(event -> mouseButtonList.clear());
        sundial.getBackgroundGroup().setOnMouseDragged(event -> changeWindowPosition(primaryStage, event));

        sundial.getDialCircleFrame().setOnMousePressed(event -> recordWindowPosition(primaryStage, dialsGroup, dialsScale, event));
        sundial.getDialCircleFrame().setOnMouseReleased(event -> mouseButtonList.clear());
        sundial.getDialCircleFrame().setOnMouseDragged(event -> changeWindowPosition(primaryStage, event));

        sundial.getMatrixYear().setOnMousePressed(event -> recordCalendarPosition(suntime, sundial, event));
        sundial.getMatrixYear().setOnMouseReleased(event -> mouseButtonList.clear());
        sundial.getMatrixYear().setOnMouseDragged(event -> offsetTime(suntime, sundial, OFFSET_BY_YEAR, event));
        sundial.getMatrixYear().setOnScroll(event -> offsetTime(suntime, sundial, OFFSET_BY_YEAR, event));

        sundial.getMatrixMonth().setOnMousePressed(event -> recordCalendarPosition(suntime, sundial, event));
        sundial.getMatrixMonth().setOnMouseReleased(event -> mouseButtonList.clear());
        sundial.getMatrixMonth().setOnMouseDragged(event -> offsetTime(suntime, sundial, OFFSET_BY_MONTH, event));
        sundial.getMatrixMonth().setOnScroll(event -> offsetTime(suntime, sundial, OFFSET_BY_MONTH, event));

        sundial.getMatrixDay().setOnMousePressed(event -> recordCalendarPosition(suntime, sundial, event));
        sundial.getMatrixDay().setOnMouseReleased(event -> mouseButtonList.clear());
        sundial.getMatrixDay().setOnMouseDragged(event -> offsetTime(suntime, sundial, OFFSET_BY_DAY, event));
        sundial.getMatrixDay().setOnScroll(event -> offsetTime(suntime, sundial, OFFSET_BY_DAY, event));

        sundial.getMatrixHour().setOnMousePressed(event -> recordCalendarPosition(suntime, sundial, event));
        sundial.getMatrixHour().setOnMouseReleased(event -> mouseButtonList.clear());
        sundial.getMatrixHour().setOnMouseDragged(event -> offsetTime(suntime, sundial, OFFSET_BY_HOUR, event));
        sundial.getMatrixHour().setOnScroll(event -> offsetTime(suntime, sundial, OFFSET_BY_HOUR, event));

        sundial.getMatrixMinute().setOnMousePressed(event -> recordCalendarPosition(suntime, sundial, event));
        sundial.getMatrixMinute().setOnMouseReleased(event -> mouseButtonList.clear());
        sundial.getMatrixMinute().setOnMouseDragged(event -> offsetTime(suntime, sundial, OFFSET_BY_MINUTE, event));
        sundial.getMatrixMinute().setOnScroll(event -> offsetTime(suntime, sundial, OFFSET_BY_MINUTE, event));

        sundial.getMatrixWeek().setOnMousePressed(event -> recordCalendarPosition(suntime, sundial, event));
        sundial.getMatrixWeek().setOnMouseReleased(event -> mouseButtonList.clear());
        sundial.getMatrixWeek().setOnMouseDragged(event -> offsetTime(suntime, sundial, OFFSET_BY_WEEK, event));
        sundial.getMatrixWeek().setOnScroll(event -> offsetTime(suntime, sundial, OFFSET_BY_WEEK, event));

        sundial.getDialHighNoonGroup().setOnMouseClicked(event -> {
            if (sundial.animationOnEh) { sundial.animationOnEh = false; }
            else { sundial.animationOnEh = true; }
        });

        sundial.getHorizonGroup().setOnMouseClicked(event -> {
            if (statsWindow.isShowing()) { statsWindow.close(); }
            else { statsWindow.show(); }
        });

        primaryStage.setOnHidden(event -> timeline.pause());
        primaryStage.setOnShown(event -> timeline.play());

        // Showtime
        initCurrentTime(suntime, sundial);
        primaryStage.show();
        timeline.play();
        recordWindowPosition(primaryStage, dialsGroup, dialsScale, null);

    }

    // Methods
    private void resetTime(Suntime suntime, Sundial sundial) {
        timeOffset = 0;
        offsetLocalTime.set(
                currentLocalTime.get(Calendar.YEAR),
                currentLocalTime.get(Calendar.MONTH),
                currentLocalTime.get(Calendar.DAY_OF_MONTH),
                currentLocalTime.get(Calendar.HOUR_OF_DAY),
                currentLocalTime.get(Calendar.MINUTE),
                currentLocalTime.get(Calendar.SECOND)
        );
        sundial.setDialFrameWarning(false);
        initCurrentTime(suntime, sundial);
    }

    private void offsetTime(Suntime suntime, Sundial sundial, int offset, ScrollEvent event) {

        if (suntime == null || sundial == null || event == null) { return; }

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

        initCurrentTime(suntime, sundial);
    }

    private void offsetTime(Suntime suntime, Sundial sundial, int offsetType, MouseEvent event) {

        if (suntime == null || sundial == null || event == null) { return; }

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

        initCurrentTime(suntime, sundial);
    }

    private void changeNightCompression(Suntime suntime, Sundial sundial, ScrollEvent event) {

        if (suntime == null || sundial == null || event == null) { return; }

        if (!mouseButtonList.isEmpty()) { return; }

        if (event.getDeltaY() < 0) { sundial.increaseNightCompression(); }
        else if (event.getDeltaY() > 0) { sundial.decreaseNightCompression(); }
        else { return; }

    }

    private void changeNightCompression(Suntime suntime, Sundial sundial, MouseEvent event) {

        if (suntime == null || sundial == null || event == null) { return; }

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

    private void initCurrentTime(Suntime suntime, Sundial sundial) {
        updateCurrentTime(suntime, sundial, true);
    }

    private void updateCurrentTime(Suntime suntime, Sundial sundial) {
        updateCurrentTime(suntime, sundial, false);
    }

    private void updateCurrentTime(Suntime suntime, Sundial sundial, boolean initialize) {

        GregorianCalendar newLocalTime = new GregorianCalendar();

        long newTimeInSeconds = newLocalTime.getTimeInMillis() / 1000;
        long currentTimeInSeconds = currentLocalTime.getTimeInMillis() / 1000;

        if (newTimeInSeconds == currentTimeInSeconds && !initialize) { return; }

        // Update current and offset local time
        long offsetSeconds = (offsetLocalTime.getTimeInMillis() - currentLocalTime.getTimeInMillis()) / 1000;
        currentLocalTime = newLocalTime;
        offsetLocalTime.setTimeInMillis(currentLocalTime.getTimeInMillis() + offsetSeconds * 1000);

        // Store current Julian Day Number before updating current time
        long oldJulianDayNumber = Suntime.getJulianDayNumber(offsetLocalTime);

        // Update suntime and sundial objects
        suntime.setObserverTime(offsetLocalTime);
        suntime.setObserverPosition(longitude, latitude);

        long julianDayNumber = suntime.getJulianDayNumber();
        double julianDate = suntime.getJulianDate();
        double sunTime = suntime.getSunTime();

        GregorianCalendar sunTimeDate = Suntime.getCalendarDate(sunTime, offsetLocalTime.getTimeZone());

        sundial.setSunTime(sunTimeDate);
        sundial.setLocalTime(offsetLocalTime);

        sundial.setLocalTimeText(offsetLocalTime.getTime().toString());

        String startupInformation =
                "Day[9] date              : " + offsetLocalTime.getTime().toString()
            + "\nDay[9] day of the year   : " + offsetLocalTime.get(Calendar.DAY_OF_YEAR)
            + "\nDay[9] Julian Date       : " + julianDateFormat.format(julianDate) + " (UTC)"
            + "\nDay[9] Gregorian Date    : " + Suntime.getCalendarDate(julianDate, offsetLocalTime.getTimeZone()).getTime().toString()
            + "\nDay[9] Julian Day Number : " + julianDayNumber
            ;

        String sunTimeInformation =
                    "Sun Time   : " + sunTimeDate.getTime().toString();

        startupInfoText.setText(startupInformation);
        sunTimeInfoText.setText(sunTimeInformation);

        String yearString = ("0000" + offsetLocalTime.get(Calendar.YEAR));
        yearString = yearString.substring(yearString.length() - 4, yearString.length());
        String monthString = ("00" + (offsetLocalTime.get(Calendar.MONTH) + 1));
        monthString = monthString.substring(monthString.length() - 2, monthString.length());
        String dayString = ("00" + offsetLocalTime.get(Calendar.DAY_OF_MONTH));
        dayString = dayString.substring(dayString.length() - 2, dayString.length());
        String hourString = ("00" + offsetLocalTime.get(Calendar.HOUR_OF_DAY));
        hourString = hourString.substring(hourString.length() - 2, hourString.length());
        String minuteString = ("00" + offsetLocalTime.get(Calendar.MINUTE));
        minuteString = minuteString.substring(minuteString.length() - 2, minuteString.length());
        String secondString = ("00" + offsetLocalTime.get(Calendar.SECOND));
        secondString = secondString.substring(secondString.length() - 2, secondString.length());
        String weekString = ("00" + offsetLocalTime.get(Calendar.WEEK_OF_YEAR));
        weekString = weekString.substring(weekString.length() - 2, weekString.length());

        sundial.getMatrixHour().setString(hourString);
        sundial.getMatrixMinute().setString(minuteString);
        sundial.getMatrixSecond().setString(secondString);

        sundial.getMatrixDay().setString(dayString);
        sundial.getMatrixMonth().setString(monthString);
        sundial.getMatrixYear().setString(yearString);
        sundial.getMatrixWeek().setString(weekString);

        // Update daily data only if it's a new day, or initialization event
        if (julianDayNumber != oldJulianDayNumber || initialize) {

            double highNoonJulianDate = suntime.getHighnoonJulianDate();
            double sunriseJulianDate = suntime.getSunriseJulianDate();
            double sunsetJulianDate = suntime.getSunsetJulianDate();

            double dayLength = sunsetJulianDate - sunriseJulianDate;

            GregorianCalendar highNoonDate = Suntime.getCalendarDate(highNoonJulianDate, offsetLocalTime.getTimeZone());
            GregorianCalendar sunriseDate = Suntime.getCalendarDate(sunriseJulianDate, offsetLocalTime.getTimeZone());
            GregorianCalendar sunsetDate = Suntime.getCalendarDate(sunsetJulianDate, offsetLocalTime.getTimeZone());

            sundial.setHighNoon(highNoonDate);
            sundial.setHorizon(sunriseDate, sunsetDate);
            sundial.setCoordinates(longitude, latitude);

            String calculatedInformation =
                    "High Noon  : " + highNoonDate.getTime().toString()
                + "\nSunrise    : " + sunriseDate.getTime().toString()
                + "\nSunset     : " + sunsetDate.getTime().toString()
                + "\nDay Length : " + Suntime.printSecondsToTime(Suntime.convertFractionToSeconds(dayLength))
                ;

            calculatedInfoText.setText(calculatedInformation);

            // update stats
            if (statsWindow.isShowing()) {
                sunchart.setSpacetimePosition(longitude, latitude, offsetLocalTime.get(Calendar.YEAR));
            }

            // debug info
            if (debugWindow.isShowing()) {
                double dividend = sin(toRadians(-0.83d)) - sin(toRadians(latitude)) * sin(toRadians(suntime.getDeclinationOfTheSun()));
                double divisor = cos(toRadians(latitude)) * cos(toRadians(suntime.getDeclinationOfTheSun()));

                String debugText = ""
                        + "meanAnomaly = " + suntime.getMeanAnomaly() + "\n"
                        + "equationOfCenter = " + suntime.getEquationOfCenter() + "\n"
                        + "eclipticalLongitude = " + suntime.getEclipticalLongitude() + "\n"
                        + "rightAscension = " + suntime.getRightAscension() + "\n"
                        + "declinationOfTheSun = " + suntime.getDeclinationOfTheSun() + "\n"
                        + "siderealTime = " + suntime.getSiderealTime() + "\n"
                        + "hourAngle = " + suntime.getHourAngle() + "\n"
                        + "solarTransit = " + suntime.getSolarTransit() + "\n"
                        + "localHourAngle = " + suntime.getLocalHourAngle() + "\n"
                        + "localHourAngle dividend = " + dividend + "\n"
                        + "localHourAngle divisor = " + divisor + "\n"
                        + "longitude = " + longitude + "\n"
                        + "latitude = " + latitude + "\n"
                        ;
                debugTextArea.setText(debugText);
            }
        }
    }

    private void resetGlobePosition(Suntime suntime, Sundial sundial, int type) {

        if (type == TYPE_LONGITUDE) { longitude = DEFAULT_LONGITUDE; }
        else if (type == TYPE_LATITUDE) { latitude = DEFAULT_LATITUDE; }
        else {
            longitude = DEFAULT_LONGITUDE;
            latitude = DEFAULT_LATITUDE;
        }
        initCurrentTime(suntime, sundial);
        sundial.rotateGlobe(longitude, latitude);
    }

    private void recordGlobePosition(Suntime suntime, Sundial sundial, int type, MouseEvent event) {

        mouseButtonList.add(event.getButton());
        if (event.getButton().equals(MouseButton.MIDDLE)) {
            resetGlobePosition(suntime, sundial, type);
        }

        savedMouseX = event.getScreenX();
        savedMouseY = event.getScreenY();

        savedLongitude = longitude;
        savedLatitude = latitude;
    }

    private void rotateGlobe(Suntime suntime, Sundial sundial, MouseEvent event) {

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

        initCurrentTime(suntime, sundial);
        sundial.rotateGlobe(longitude, latitude);
    }

    private void rotateGlobe(Suntime suntime, Sundial sundial, int type, MouseEvent event) {

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

        if (type == TYPE_LONGITUDE) { longitude += round((delta / precision) * 100) / 100d; }
        else { latitude -= round((delta / precision) * 100) / 100d; }

        if (longitude < Suntime.MIN_LONGITUDE) { longitude = Suntime.MAX_LONGITUDE - (Suntime.MIN_LONGITUDE - longitude); }
        if (longitude > Suntime.MAX_LONGITUDE) { longitude = Suntime.MIN_LONGITUDE - (Suntime.MAX_LONGITUDE - longitude); }
        if (latitude < Suntime.MIN_LATITUDE) { latitude = Suntime.MIN_LATITUDE; }
        if (latitude > Suntime.MAX_LATITUDE) { latitude = Suntime.MAX_LATITUDE; }

        initCurrentTime(suntime, sundial);
        sundial.rotateGlobe(longitude, latitude);
    }

    private void rotateGlobe(Suntime suntime, Sundial sundial, int type, ScrollEvent event) {

        if (!mouseButtonList.isEmpty()) {
            if(mouseButtonList.get(mouseButtonList.size() - 1).equals(MouseButton.MIDDLE)) {
                return;
            }
        }

        double step = 0;

        if (event.getDeltaY() < 0) { step = -0.01; }
        if (event.getDeltaY() > 0) { step = 0.01; }

        if (type == TYPE_LONGITUDE) { longitude += step; }
        else { latitude += step; }

        if (longitude < Suntime.MIN_LONGITUDE) { longitude = Suntime.MAX_LONGITUDE - (Suntime.MIN_LONGITUDE - longitude); }
        if (longitude > Suntime.MAX_LONGITUDE) { longitude = Suntime.MIN_LONGITUDE - (Suntime.MAX_LONGITUDE - longitude); }
        if (latitude < Suntime.MIN_LATITUDE) { latitude = Suntime.MIN_LATITUDE; }
        if (latitude > Suntime.MAX_LATITUDE) { latitude = Suntime.MAX_LATITUDE; }

        initCurrentTime(suntime, sundial);
        sundial.rotateGlobe(longitude, latitude);
    }

    private void resetWindowSize(Stage stage, Group dialsGroup, Scale dialsScale) {

        dialsScale.setX(1.0d);
        dialsScale.setY(1.0d);

        stage.setWidth(dialsGroup.getLayoutBounds().getWidth());
        stage.setHeight(dialsGroup.getLayoutBounds().getHeight());

    }

    private void recordCalendarPosition(Suntime suntime, Sundial sundial, MouseEvent event) {

        MouseButton mouseButton = event.getButton();
        mouseButtonList.add(event.getButton());

        if (mouseButton.equals(MouseButton.MIDDLE)) {
            resetTime(suntime, sundial);
            return;
        }

        savedMouseX = event.getScreenX();
        savedMouseY = event.getScreenY();

        offsetX = 0;
        offsetY = 0;

    }

    private void recordNightCompressionPosition(Suntime suntime, Sundial sundial, MouseEvent event) {

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

    private void recordWindowPosition(Stage stage, Group dialsGroup, Scale dialsScale, MouseEvent event) {

        if (event != null) {
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

    private void recordWindowSize(Stage stage, Group dialsGroup, Scale dialsScale, MouseEvent event) {

        if (event != null) {
            MouseButton mouseButton = event.getButton();
            mouseButtonList.add(event.getButton());

            if (mouseButton.equals(MouseButton.MIDDLE)) {
                resetWindowSize(stage, dialsGroup, dialsScale);
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

    private void resizeWindow(Stage stage, Group dialsGroup, Scale dialsScale, MouseEvent event) {

        MouseButton mouseButton = mouseButtonList.get(mouseButtonList.size() - 1);
        if (mouseButton == null || mouseButton.equals(MouseButton.MIDDLE)) { return; }

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

        dialsScale.setX((windowSizeX) / Sundial.DIAL_WIDTH);
        dialsScale.setY((windowSizeY) / Sundial.DIAL_HEIGHT);

        stage.setWidth(windowSizeX);
        stage.setHeight(windowSizeY);
    }

    private void toggleMaximizeWindow(Stage stage, Scale dialsScale, MouseEvent event) {

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

        dialsScale.setX(stage.getWidth() / Sundial.DIAL_WIDTH);
        dialsScale.setY(stage.getHeight() / Sundial.DIAL_HEIGHT);
    }

    private void minimizeWindow(Stage stage, Timeline timeline, MouseEvent event) {
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

    private void toggleGlobe(Stage stage, Suntime suntime, Sundial sundial, Group dialsGroup, Scale dialsScale) {

        if (!mouseButtonList.isEmpty()) {

            MouseButton mouseButton = mouseButtonList.get(mouseButtonList.size() - 1);
            mouseButtonList.clear();

            if (mouseButton.equals(MouseButton.MIDDLE)) {
                resetGlobePosition(suntime, sundial, 0);
                return;
            }
        }

        sundial.toggleGlobeVisibility();

        if (sundial.globeVisibleEh) {
            sundial.getDialCircleFrame().setOnMouseEntered(event -> sundial.getDialCircleFrame().setCursor(Cursor.OPEN_HAND));
            sundial.getDialCircleFrame().setOnMousePressed(event -> recordGlobePosition(suntime, sundial, 0, event));
            sundial.getDialCircleFrame().setOnMouseReleased(event -> mouseButtonList.clear());
            sundial.getDialCircleFrame().setOnMouseDragged(event -> rotateGlobe(suntime, sundial, event));

        } else {
            sundial.getDialCircleFrame().setOnMouseEntered(event -> sundial.getDialCircleFrame().setCursor(Cursor.MOVE));
            sundial.getDialCircleFrame().setOnMousePressed(event -> recordWindowPosition(stage, dialsGroup, dialsScale, event));
            sundial.getDialCircleFrame().setOnMouseReleased(event -> mouseButtonList.clear());
            sundial.getDialCircleFrame().setOnMouseDragged(event -> changeWindowPosition(stage, event));

        }
    }
}
