import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static java.lang.Math.floor;

public class Sunface extends Application {

    private final double DEFAULT_FPS = 30.0;

    private final int OFFSET_BY_YEAR = Calendar.YEAR;
    private final int OFFSET_BY_MONTH = Calendar.MONTH;
    private final int OFFSET_BY_DAY = Calendar.DAY_OF_MONTH;
    private final int OFFSET_BY_HOUR = Calendar.HOUR_OF_DAY;
    private final int OFFSET_BY_MINUTE = Calendar.MINUTE;
    private final int OFFSET_BY_SECOND = Calendar.SECOND;
    private final int OFFSET_BY_WEEK = Calendar.WEEK_OF_YEAR;

    private final Color Color_Of_Window = new Color(0.65, 0.85, 0.85, 1.00);
    private final Font Font_Of_Info = new Font("Lucida Console", 14);
    private final DecimalFormat julianDateFormat = new DecimalFormat("###,###,###.00000000");

    private int fpsSetting = (int) floor(1000 / DEFAULT_FPS);

    private GregorianCalendar currentLocalTime;
    private long timeOffset;

    private Text startupInfoText;
    private Text sunTimeInfoText;
    private Text calculatedInfoText;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

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

        // Initialize objects
        currentLocalTime = new GregorianCalendar();
        timeOffset = 0;

        Suntime suntime = new Suntime.Builder()
//                .julianDayNumber(2453097)
                .localTime(currentLocalTime)
                .observerLongitude(15.9816d)
                .observerLatitude(45.7827d)
                .build();

        Sundial sundial = new Sundial.Builder()
                .build();

        initCurrentTime(suntime, sundial);

        // FX Scene
        Group rootNode = new Group();
        Group dialsGroup = sundial.getDialsGroup();
        rootNode.getChildren().addAll(startupInfoText, sunTimeInfoText, calculatedInfoText, dialsGroup);

        Scene mainScene = new Scene(rootNode, 600,600);
        mainScene.setFill(Color_Of_Window);

        primaryStage.setTitle("SunDial");
        primaryStage.setScene(mainScene);
        primaryStage.show();

        dialsGroup.setLayoutX(mainScene.getWidth() / 2 - dialsGroup.getLayoutBounds().getWidth() / 2);
        dialsGroup.setLayoutY(mainScene.getHeight() / 2 - dialsGroup.getLayoutBounds().getHeight() / 2);

        dialsGroup.setOnMouseClicked(event -> resetTime(suntime, sundial));

        sundial.getMatrixYear().setOnScroll(event -> offsetTime(suntime, sundial, OFFSET_BY_YEAR, event));
        sundial.getMatrixYear().setOnMouseEntered(event -> sundial.setMatrixYearGlow(true));
        sundial.getMatrixYear().setOnMouseExited(event -> sundial.setMatrixYearGlow(false));

        sundial.getMatrixMonth().setOnScroll(event -> offsetTime(suntime, sundial, OFFSET_BY_MONTH, event));
        sundial.getMatrixMonth().setOnMouseEntered(event -> sundial.setMatrixMonthGlow(true));
        sundial.getMatrixMonth().setOnMouseExited(event -> sundial.setMatrixMonthGlow(false));

        sundial.getMatrixDay().setOnScroll(event -> offsetTime(suntime, sundial, OFFSET_BY_DAY, event));
        sundial.getMatrixDay().setOnMouseEntered(event -> sundial.setMatrixDayGlow(true));
        sundial.getMatrixDay().setOnMouseExited(event -> sundial.setMatrixDayGlow(false));

        sundial.getMatrixHour().setOnScroll(event -> offsetTime(suntime, sundial, OFFSET_BY_HOUR, event));
        sundial.getMatrixHour().setOnMouseEntered(event -> sundial.setMatrixHourGlow(true));
        sundial.getMatrixHour().setOnMouseExited(event -> sundial.setMatrixHourGlow(false));

        sundial.getMatrixMinute().setOnScroll(event -> offsetTime(suntime, sundial, OFFSET_BY_MINUTE, event));
        sundial.getMatrixMinute().setOnMouseEntered(event -> sundial.setMatrixMinuteGlow(true));
        sundial.getMatrixMinute().setOnMouseExited(event -> sundial.setMatrixMinuteGlow(false));

        sundial.getMatrixSecond().setOnScroll(event -> offsetTime(suntime, sundial, OFFSET_BY_SECOND, event));
        sundial.getMatrixSecond().setOnMouseEntered(event -> sundial.setMatrixSecondGlow(true));
        sundial.getMatrixSecond().setOnMouseExited(event -> sundial.setMatrixSecondGlow(false));

        sundial.getMatrixWeek().setOnScroll(event -> offsetTime(suntime, sundial, OFFSET_BY_WEEK, event));
        sundial.getMatrixWeek().setOnMouseEntered(event -> sundial.setMatrixWeekGlow(true));
        sundial.getMatrixWeek().setOnMouseExited(event -> sundial.setMatrixWeekGlow(false));

        // Playtime
        KeyFrame keyframeClockTick = new KeyFrame(
                Duration.millis(fpsSetting),
                event -> updateCurrentTime(suntime, sundial));

        Timeline timeline = new Timeline(keyframeClockTick);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

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

        timeOffset += currentLocalTime.getTimeInMillis() - offsetCalendar.getTimeInMillis();
        timeOffset += offsetWeek * (7 * 24 * 60 * 60 * 1000);

        if (timeOffset != 0) { sundial.setDialFrameWarning(true); }

        initCurrentTime(suntime, sundial);
    }

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

        double localTimeDialAngle = getAngle(offsetLocalTime);
        double sunTimeDialAngle = getAngle(sunTimeDate);

        sundial.setSunTimeDialAngle(sunTimeDialAngle);
        sundial.setLocalTimeDialAngle(localTimeDialAngle);

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

            double highNoonDialAngle = getAngle(highNoonDate);
            double sunriseDialAngle = getAngle(sunriseDate);
            double sunsetDialAngle = getAngle(sunsetDate);

            sundial.setHighNoonDialAngle(highNoonDialAngle);
            sundial.setHorizonDialAngle(sunriseDialAngle, sunsetDialAngle);

            String calculatedInformation =
                    "High Noon  : " + highNoonDate.getTime().toString()
                + "\nSunrise    : " + sunriseDate.getTime().toString()
                + "\nSunset     : " + sunsetDate.getTime().toString()
                + "\nDay Length : " + Suntime.printSecondsToTime(Suntime.convertFractionToSeconds(dayLength))
                ;

            calculatedInfoText.setText(calculatedInformation);
        }
    }

    // Utility
    private double getAngle(GregorianCalendar calendar) {

        if(calendar == null) { return 0; }

        double hour = (double) calendar.get(Calendar.HOUR_OF_DAY);
        double minute = (double) calendar.get(Calendar.MINUTE);
        double second = (double) calendar.get(Calendar.SECOND);

        return (hour / 24d + minute / (24d * 60d) + second / (24d * 60d * 60d)) * 360d - 180d;
    }
}
