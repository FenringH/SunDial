import com.sun.javafx.collections.ImmutableObservableList;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.control.TextArea;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;
import sun.security.provider.Sun;

import java.awt.*;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static java.lang.Math.floor;

public class Sunface extends Application {

    private final double DEFAULT_FPS = 30.0;
    private int fpsSetting = (int) floor(1000 / DEFAULT_FPS);

    private static Color Color_Of_Window    = new Color(0.65, 0.85, 0.85, 1.00);
    private static Font Font_Of_Info        = new Font("Lucida Console", 14);

    private GregorianCalendar currentLocalTime;

    private Text startupInfoText = new Text();
    private Text calculatedInfoText = new Text();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        currentLocalTime = new GregorianCalendar();

        startupInfoText.setFont(Font_Of_Info);
        startupInfoText.setLayoutX(5d);
        startupInfoText.setLayoutY(20d);

        calculatedInfoText.setFont(Font_Of_Info);
        calculatedInfoText.setLayoutX(5d);
        calculatedInfoText.setLayoutY(525d);

        Suntime suntime = new Suntime.Builder()
//                .julianDayNumber(2453097)
                .localTime(currentLocalTime)
                .observerLongitude(15.9816d)
                .observerLatitude(45.7827d)
                .build();

        Sundial sundial = new Sundial.Builder()
                .build();

        updateCurrentTime(suntime, sundial);

        Group rootNode = new Group();
        Group dialsGroup = sundial.getDialsGroup();
        rootNode.getChildren().addAll(startupInfoText, calculatedInfoText, dialsGroup);

        Scene mainScene = new Scene(rootNode, 600,600);
        mainScene.setFill(Color_Of_Window);

        primaryStage.setTitle("SunDial");
        primaryStage.setScene(mainScene);
        primaryStage.show();

        dialsGroup.setLayoutX(mainScene.getWidth() / 2 - dialsGroup.getLayoutBounds().getWidth() / 2);
        dialsGroup.setLayoutY(mainScene.getHeight() / 2 - dialsGroup.getLayoutBounds().getHeight() / 2);

        // Playtime
        KeyFrame keyframeClockTick = new KeyFrame(
                Duration.millis(fpsSetting),
                event -> updateCurrentTime(suntime, sundial));

        Timeline timeline = new Timeline(keyframeClockTick);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

    }

    // Event handlers
    public void updateCurrentTime(Suntime suntime, Sundial sundial) {

        GregorianCalendar newLocalTime = new GregorianCalendar();
        if (currentLocalTime.equals(newLocalTime)) { return; }

        currentLocalTime = newLocalTime;

        // Update suntime
        suntime.setObserverTime(currentLocalTime);

        // update FX
        long julianDayNumber = suntime.getJulianDayNumber();
        double julianDate = suntime.getJulianDate();
        double sunTime = suntime.getSunTime();
        double sunriseJulianDate = suntime.getSunriseJulianDate();
        double sunsetJulianDate = suntime.getSunsetJulianDate();
        double highNoonJulianDate = suntime.getHighnoonJulianDate();

        double dayLength = sunsetJulianDate - sunriseJulianDate;

        GregorianCalendar sunriseDate = Suntime.getCalendarDate(sunriseJulianDate, currentLocalTime.getTimeZone());
        GregorianCalendar sunsetDate = Suntime.getCalendarDate(sunsetJulianDate, currentLocalTime.getTimeZone());
        GregorianCalendar highNoonDate = Suntime.getCalendarDate(highNoonJulianDate, currentLocalTime.getTimeZone());
        GregorianCalendar sunTimeDate = Suntime.getCalendarDate(sunTime, currentLocalTime.getTimeZone());

        String startupInformation =
                "Day[9] date              : " + currentLocalTime.getTime().toString()
            + "\nDay[9] day of the year   : " + currentLocalTime.get(Calendar.DAY_OF_YEAR)
            + "\nDay[9] Julian Date       : " + julianDate + " (UTC)"
            + "\nDay[9] Gregorian Date    : " + Suntime.getCalendarDate(julianDate, currentLocalTime.getTimeZone()).getTime().toString()
            + "\nDay[9] Julian Day Number : " + julianDayNumber
            ;

        String calculatedInformation =
                "Sun Time   : " + sunTimeDate.getTime().toString()
            + "\nHigh Noon  : " + highNoonDate.getTime().toString()
            + "\nSunrise    : " + sunriseDate.getTime().toString()
            + "\nSunset     : " + sunsetDate.getTime().toString()
            + "\nDay Length : " + Suntime.printSecondsToTime(Suntime.convertFractionToSeconds(dayLength))
            ;

        double timeZoneOffset = currentLocalTime.getTimeZone().getRawOffset() / (60d * 60d * 1000d);

        double sunTimeDialAngle = (sunTime - floor(sunTime) + timeZoneOffset / 24d) * 360d;
        double highNoonDialAngle = (highNoonJulianDate - floor(highNoonJulianDate) + timeZoneOffset / 24d) * 360d;
        double sunriseDialAngle = (sunriseJulianDate - floor(sunriseJulianDate) + timeZoneOffset / 24d) * 360d;
        double sunsetDialAngle = (sunsetJulianDate - floor(sunsetJulianDate) + timeZoneOffset / 24d) * 360d;
        double localTimeDialAngle = (julianDate - floor(julianDate) + timeZoneOffset / 24d) * 360d;

        startupInfoText.setText(startupInformation);
        calculatedInfoText.setText(calculatedInformation);

        sundial.setSunTimeDialAngle(sunTimeDialAngle);
        sundial.setHighNoonDialAngle(highNoonDialAngle);
        sundial.setSunriseSunsetDialAngle(sunriseDialAngle, sunsetDialAngle);
        sundial.setLocalTimeDialAngle(localTimeDialAngle);
        sundial.setLocalTimeText(currentLocalTime.getTime().toString());

    }
}
