import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import sun.security.provider.Sun;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static java.lang.Math.abs;
import static java.lang.Math.floor;

public class Sunface extends Application {

    private static final double DEFAULT_FPS = 30.0;
    private static final int DEFAULT_SCALE = 50;

    private static final double MIN_WIDTH = 150;
    private static final double MIN_HEIGHT = 150;

    private static final int OFFSET_BY_YEAR = Calendar.YEAR;
    private static final int OFFSET_BY_MONTH = Calendar.MONTH;
    private static final int OFFSET_BY_DAY = Calendar.DAY_OF_MONTH;
    private static final int OFFSET_BY_HOUR = Calendar.HOUR_OF_DAY;
    private static final int OFFSET_BY_MINUTE = Calendar.MINUTE;
    private static final int OFFSET_BY_SECOND = Calendar.SECOND;
    private static final int OFFSET_BY_WEEK = Calendar.WEEK_OF_YEAR;

    public static final String BUTTON_SHADOW  = "-fx-effect: dropshadow(three-pass-box, rgba(  0,  0,  0, 1.0),  5.0, 0.50, 0, 0);";
    public static final String BUTTON_GLOW    = "-fx-effect: dropshadow(three-pass-box, rgba(255,128, 32, 0.5),  5.0, 0.50, 0, 0);";

    private static final Color Color_Of_Window = new Color(0.65, 0.85, 0.85, 1.00);
    private static final Font Font_Of_Info = new Font("Lucida Console", 14);
    private static final DecimalFormat julianDateFormat = new DecimalFormat("###,###,###.00000000");

    private int fpsSetting = (int) floor(1000 / DEFAULT_FPS);

    private GregorianCalendar currentLocalTime;
    private long timeOffset;

    private double deltaX;
    private double deltaY;
    private double mouseX;
    private double mouseY;
    private int dX;
    private int dY;

    private Text startupInfoText;
    private Text sunTimeInfoText;
    private Text calculatedInfoText;

    private Circle controlCircleClose;

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

        // Controls
        Rectangle controlBox = new Rectangle(60, 20);
        controlBox.setOpacity(0.1d);

        controlCircleClose = new Circle(controlBox.getWidth() - 10, 10,6);
        controlCircleClose.setFill(Color.RED);
//        controlCircleClose.setStyle(BUTTON_SHADOW);

        // Sun objects
        currentLocalTime = new GregorianCalendar();
        timeOffset = 0;

        Suntime suntime = new Suntime.Builder()
//                .julianDayNumber(2453097)
                .localTime(currentLocalTime)
                .observerLongitude(15.9816d)
                .observerLatitude(45.7827d)
                .build();

        Sundial sundial = new Sundial.Builder()
                .nightCompression(0)
                .build();

        initCurrentTime(suntime, sundial);

        // Scene
        Group rootGroup = new Group();

        Group dialsGroup = new Group();
        dialsGroup.getChildren().add(sundial.getDialsGroup());

        Group controlsGroup = new Group();
        controlsGroup.getChildren().addAll(controlBox, controlCircleClose);

        rootGroup.getChildren().addAll(dialsGroup, controlsGroup);

        double sizeX = rootGroup.getLayoutBounds().getWidth();
        double sizeY = rootGroup.getLayoutBounds().getHeight();

        Scene mainScene = new Scene(rootGroup, sizeX, sizeY);
        mainScene.setFill(Color.TRANSPARENT);

        double dialsMinX = dialsGroup.getLayoutBounds().getMinX();
        double dialsMinY = dialsGroup.getLayoutBounds().getMinY();

        double dialsMaxX = dialsGroup.getLayoutBounds().getMaxX();
        double dialsMaxY = dialsGroup.getLayoutBounds().getMaxY();

        double dialsLayoutWidth = dialsGroup.getLayoutBounds().getWidth();
        double dialsLayoutHeight = dialsGroup.getLayoutBounds().getHeight();

        dialsGroup.setLayoutX(dialsLayoutWidth - dialsMaxX);
        dialsGroup.setLayoutY(dialsLayoutHeight - dialsMaxY);

        controlsGroup.setLayoutX(sizeX - controlsGroup.getLayoutBounds().getWidth());
        controlsGroup.setLayoutY(0);

        Scale dialsScale = new Scale();
        dialsScale.setPivotX(dialsMinX);
        dialsScale.setPivotY(dialsMinY);
        dialsGroup.getTransforms().add(dialsScale);

        primaryStage.setTitle("SunDial");
        primaryStage.setScene(mainScene);

        // Events
        controlCircleClose.setOnMouseEntered(event -> controlCircleClose.setStyle(BUTTON_GLOW));
        controlCircleClose.setOnMouseExited(event -> controlCircleClose.setStyle(""));
        controlCircleClose.setOnMousePressed(event -> controlCircleClose.setFill(Color.ORANGE));
        controlCircleClose.setOnMouseReleased(event -> controlCircleClose.setFill(Color.RED));
        controlCircleClose.setOnMouseClicked(event -> System.exit(0));

        sundial.getDialMarginFillBox().setOnMouseEntered(event -> sundial.getDialMarginFillBox().setFill(Sundial.Color_Of_SunTime));
        sundial.getDialMarginFillBox().setOnMouseExited(event -> sundial.getDialMarginFillBox().setFill(Sundial.Color_Of_DaySky));
        sundial.getDialMarginFillBox().setOnMousePressed(event -> recordWindowPosition(primaryStage, event));
        sundial.getDialMarginFillBox().setOnMouseDragged(event -> changeWindowSize(primaryStage, dialsGroup, controlsGroup, dialsScale, event));

        sundial.getDialCircleFrame().setOnMouseEntered(event -> mainScene.setCursor(Cursor.OPEN_HAND));
        sundial.getDialCircleFrame().setOnMouseExited(event -> mainScene.setCursor(Cursor.DEFAULT));
        sundial.getDialCircleFrame().setOnMousePressed(event -> recordWindowPosition(primaryStage, event));
        sundial.getDialCircleFrame().setOnMouseDragged(event -> changeWindowPosition(primaryStage, event));

        sundial.getMatrixYear().setOnScroll(event -> offsetTime(suntime, sundial, OFFSET_BY_YEAR, event));
        sundial.getMatrixYear().setOnMouseClicked(event -> resetTime(suntime, sundial));

        sundial.getMatrixMonth().setOnScroll(event -> offsetTime(suntime, sundial, OFFSET_BY_MONTH, event));
        sundial.getMatrixMonth().setOnMouseClicked(event -> resetTime(suntime, sundial));

        sundial.getMatrixDay().setOnScroll(event -> offsetTime(suntime, sundial, OFFSET_BY_DAY, event));
        sundial.getMatrixDay().setOnMouseClicked(event -> resetTime(suntime, sundial));

        sundial.getMatrixHour().setOnScroll(event -> offsetTime(suntime, sundial, OFFSET_BY_HOUR, event));
        sundial.getMatrixHour().setOnMouseClicked(event -> resetTime(suntime, sundial));

        sundial.getMatrixMinute().setOnScroll(event -> offsetTime(suntime, sundial, OFFSET_BY_MINUTE, event));
        sundial.getMatrixMinute().setOnMouseClicked(event -> resetTime(suntime, sundial));

        sundial.getMatrixSecond().setOnScroll(event -> offsetTime(suntime, sundial, OFFSET_BY_SECOND, event));
        sundial.getMatrixSecond().setOnMouseClicked(event -> resetTime(suntime, sundial));

        sundial.getMatrixWeek().setOnScroll(event -> offsetTime(suntime, sundial, OFFSET_BY_WEEK, event));
        sundial.getMatrixWeek().setOnMouseClicked(event -> resetTime(suntime, sundial));

        // Playtime
        KeyFrame keyframeClockTick = new KeyFrame(
                Duration.millis(fpsSetting),
                event -> updateCurrentTime(suntime, sundial));

        Timeline timeline = new Timeline(keyframeClockTick);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        primaryStage.setMinWidth(MIN_WIDTH);
        primaryStage.setMinHeight(MIN_HEIGHT);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.show();

    }

    // Methods
    private void resetTime(Suntime suntime, Sundial sundial) {
        timeOffset = 0;
        sundial.setDialFrameWarning(false);
        initCurrentTime(suntime, sundial);
    }

    private void offsetTime(Suntime suntime, Sundial sundial, int offset, ScrollEvent event) {

        if (suntime == null || sundial == null || event == null) { return; }

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

        GregorianCalendar offsetCalendar = new GregorianCalendar();
        offsetCalendar.set(
                currentLocalTime.get(Calendar.YEAR) + offsetYear,
                currentLocalTime.get(Calendar.MONTH) + offsetMonth,
                currentLocalTime.get(Calendar.DAY_OF_MONTH) + offsetDay,
                currentLocalTime.get(Calendar.HOUR_OF_DAY) + offsetHour,
                currentLocalTime.get(Calendar.MINUTE) + offsetMinute,
                currentLocalTime.get(Calendar.SECOND) + offsetSecond
        );

        timeOffset += (currentLocalTime.getTimeInMillis() - offsetCalendar.getTimeInMillis());
        timeOffset += (offsetWeek * (7 * 24 * 60 * 60 * 1000));

        if (timeOffset != 0) { sundial.setDialFrameWarning(true); }
        else { sundial.setDialFrameWarning(false); }

        initCurrentTime(suntime, sundial);
    }

    // Methods
    private void initCurrentTime(Suntime suntime, Sundial sundial) {
        updateCurrentTime(suntime, sundial, true);
    }

    private void updateCurrentTime(Suntime suntime, Sundial sundial) {
        updateCurrentTime(suntime, sundial, false);
    }

    private void updateCurrentTime(Suntime suntime, Sundial sundial, boolean initialize) {

        if (suntime == null || sundial == null) { return; }

        GregorianCalendar newLocalTime = new GregorianCalendar();

        // Don't do anything if time hasn't changed
        if (currentLocalTime.equals(newLocalTime)) { return; }

        // Update current local time
        currentLocalTime = newLocalTime;

        // Create offset local time
        GregorianCalendar offsetLocalTime = new GregorianCalendar();
        offsetLocalTime.setTimeInMillis(currentLocalTime.getTimeInMillis() + timeOffset);

        // Store current Julian Day Number before updating current time
        long oldJulianDayNumber = Suntime.getJulianDayNumber(offsetLocalTime);

        // Update suntime and sundial objects
        suntime.setObserverTime(offsetLocalTime);

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

        // Update daily data only if it's a new day, or 1st time initialization
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

            String calculatedInformation =
                    "High Noon  : " + highNoonDate.getTime().toString()
                + "\nSunrise    : " + sunriseDate.getTime().toString()
                + "\nSunset     : " + sunsetDate.getTime().toString()
                + "\nDay Length : " + Suntime.printSecondsToTime(Suntime.convertFractionToSeconds(dayLength))
                ;

            calculatedInfoText.setText(calculatedInformation);
        }
    }

    public void recordWindowPosition(Stage stage, MouseEvent event) {

        double winPositionX = stage.getX();
        double winPositionY = stage.getY();

        double winSizeX = stage.getWidth();
        double winSizeY = stage.getHeight();

        mouseX = event.getScreenX();
        mouseY = event.getScreenY();

        deltaX = stage.getX() - mouseX;
        deltaY = stage.getY() - mouseY;

        if (winPositionX + winSizeX / 2 - mouseX > 0 && winPositionY + winSizeY / 2 - mouseY > 0) { dX = -1; dY = -1; }
        if (winPositionX + winSizeX / 2 - mouseX < 0 && winPositionY + winSizeY / 2 - mouseY > 0) { dX = 1; dY = -1; }
        if (winPositionX + winSizeX / 2 - mouseX > 0 && winPositionY + winSizeY / 2 - mouseY < 0) { dX = -1; dY = 1; }
        if (winPositionX + winSizeX / 2 - mouseX < 0 && winPositionY + winSizeY / 2 - mouseY < 0) { dX = 1; dY = 1; }

    }

    public void changeWindowSize(Stage stage, Group dialsGroup, Group controlsGroup, Scale dialsScale, MouseEvent event) {

        double winPositionX = stage.getX();
        double winPositionY = stage.getY();

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

        double mouseDeltaX = mouseScreenX - mouseX;
        double mouseDeltaY = mouseScreenY - mouseY;

        recordWindowPosition(stage, event);

        if (event.isShiftDown()) {

            if (abs(mouseDeltaX) > abs(mouseDeltaY)) {
                winSizeX += dX * mouseDeltaX;
                winSizeY = winSizeX;
            } else {
                winSizeY += dY * mouseDeltaY;
                winSizeX = winSizeY;
            }

        } else {
            winSizeX += dX * mouseDeltaX;
            winSizeY += dY * mouseDeltaY;
        }

        if (winSizeX <= MIN_WIDTH) { winSizeX = MIN_WIDTH; }
        if (winSizeY <= MIN_HEIGHT) { winSizeY = MIN_HEIGHT; }

        int movedX = dX < 0 ? dX * -1 : 0;
        int movedY = dY < 0 ? dY * -1 : 0;

        double winMoveX = winPositionX + movedX * mouseDeltaX;
        double winMoveY = winPositionY + movedY * mouseDeltaY;

        double dialsLayoutWidth = dialsGroup.getLayoutBounds().getWidth();
        double dialsLayoutHeight = dialsGroup.getLayoutBounds().getHeight();

        double scaleX = winSizeX / dialsLayoutWidth;
        double scaleY = winSizeY / dialsLayoutHeight;

        if (winPositionX + winMoveX <= currentScreenMinX) { winMoveX = winPositionX; }
        if (winPositionX + winSizeX >= currentScreenMaxX) { winSizeX = stage.getWidth(); }
        if (winPositionY + winMoveY <= currentScreenMinY) { winMoveY = winPositionY; }
        if (winPositionY + winSizeY >= currentScreenMaxY) { winSizeY = stage.getHeight(); }

        stage.setWidth(winSizeX);
        stage.setHeight(winSizeY);

        stage.setX(winMoveX);
        stage.setY(winMoveY);

        dialsScale.setX(scaleX);
        dialsScale.setY(scaleY);

        controlsGroup.setLayoutX(winSizeX - controlsGroup.getLayoutBounds().getWidth());

    }

    public void changeWindowPosition(Stage stage, MouseEvent event) {

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

}
