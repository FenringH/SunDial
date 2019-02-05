import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import static java.lang.Math.floor;

public class Sunface extends Application {

    private final double DEFAULT_FPS = 30.0;
    private final int OFFSET_BY_DAY = 24;
    private final int OFFSET_BY_HOUR = 1;

    private int fpsSetting = (int) floor(1000 / DEFAULT_FPS);

    private static Color Color_Of_Window = new Color(0.65, 0.85, 0.85, 1.00);
    private static Font Font_Of_Info = new Font("Lucida Console", 14);
    private static DecimalFormat julianDateFormat = new DecimalFormat("###,###,###.00000000");

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

        dialsGroup.setOnScroll(event -> offsetTimeByModifier(suntime, sundial, event));
        dialsGroup.setOnMouseClicked(event -> resetTime(suntime, sundial));

        // Playtime
        KeyFrame keyframeClockTick = new KeyFrame(
                Duration.millis(fpsSetting),
                event -> updateCurrentTime(suntime, sundial));

        Timeline timeline = new Timeline(keyframeClockTick);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

    }

    // Methods
    public void resetTime(Suntime suntime, Sundial sundial) {
        timeOffset = 0;
        initCurrentTime(suntime, sundial);
    }

    public void offsetTimeByModifier(Suntime suntime, Sundial sundial, ScrollEvent event) {

        int offsetFactor = 0;

        if (event.getDeltaY() < 0) { offsetFactor = -1; }
        else if (event.getDeltaY() > 0) { offsetFactor = 1; }

        long offsetModifier = OFFSET_BY_HOUR;

        if (event.isControlDown()) { offsetModifier = OFFSET_BY_DAY; }

        timeOffset += offsetFactor * offsetModifier * (60 * 60 * 1000);
        initCurrentTime(suntime, sundial);
    }

    public void initCurrentTime(Suntime suntime, Sundial sundial) {
        updateCurrentTime(suntime, sundial, true);
    }

    public void updateCurrentTime(Suntime suntime, Sundial sundial) {
        updateCurrentTime(suntime, sundial, false);
    }

    public void updateCurrentTime(Suntime suntime, Sundial sundial, boolean initialize) {

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

        double timeZoneOffset = offsetLocalTime.getTimeZone().getRawOffset() / (60d * 60d * 1000d);
        double sunTimeDialAngle = (sunTime - floor(sunTime) + timeZoneOffset / 24d) * 360d;
        double localTimeDialAngle = (julianDate - floor(julianDate) + timeZoneOffset / 24d) * 360d;

        sundial.setSunTimeDialAngle(sunTimeDialAngle);
        sundial.setLocalTimeDialAngle(localTimeDialAngle);
        sundial.setLocalTimeText(offsetLocalTime.getTime().toString());

        GregorianCalendar sunTimeDate = Suntime.getCalendarDate(sunTime, offsetLocalTime.getTimeZone());

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

        // Update daily data only if it's a new day, or 1st time initialization
        if (julianDayNumber != oldJulianDayNumber || initialize) {

            double sunriseJulianDate = suntime.getSunriseJulianDate();
            double sunsetJulianDate = suntime.getSunsetJulianDate();
            double highNoonJulianDate = suntime.getHighnoonJulianDate();
            double dayLength = sunsetJulianDate - sunriseJulianDate;

            double highNoonDialAngle = (highNoonJulianDate - floor(highNoonJulianDate) + timeZoneOffset / 24d) * 360d;
            double sunriseDialAngle = (sunriseJulianDate - floor(sunriseJulianDate) + timeZoneOffset / 24d) * 360d;
            double sunsetDialAngle = (sunsetJulianDate - floor(sunsetJulianDate) + timeZoneOffset / 24d) * 360d;

            sundial.setHighNoonDialAngle(highNoonDialAngle);
            sundial.setSunriseSunsetDialAngle(sunriseDialAngle, sunsetDialAngle);

            GregorianCalendar sunriseDate = Suntime.getCalendarDate(sunriseJulianDate, offsetLocalTime.getTimeZone());
            GregorianCalendar sunsetDate = Suntime.getCalendarDate(sunsetJulianDate, offsetLocalTime.getTimeZone());
            GregorianCalendar highNoonDate = Suntime.getCalendarDate(highNoonJulianDate, offsetLocalTime.getTimeZone());

            String calculatedInformation =
                    "High Noon  : " + highNoonDate.getTime().toString()
                + "\nSunrise    : " + sunriseDate.getTime().toString()
                + "\nSunset     : " + sunsetDate.getTime().toString()
                + "\nDay Length : " + Suntime.printSecondsToTime(Suntime.convertFractionToSeconds(dayLength))
                ;

            calculatedInfoText.setText(calculatedInformation);
        }
    }
}
