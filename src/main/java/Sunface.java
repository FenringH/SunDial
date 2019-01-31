import com.sun.javafx.collections.ImmutableObservableList;
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
import sun.security.provider.Sun;

import java.awt.*;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static java.lang.Math.floor;

public class Sunface extends Application {

    private static Color Color_Of_Window    = new Color(0.65, 0.85, 0.85, 1.0);
    private static Color Color_Of_Earth     = new Color(0.85, 0.85, 0.65, 1.0);
    private static Color Color_Of_Darkness  = new Color(0.00, 0.00, 0.00, 1.0);
    private static Color Color_Of_TextBack  = new Color(0.90, 0.90, 0.50, 1.0);
    private static Color Color_Of_Void      = new Color(0.00, 0.00, 0.00, 0.0);

    private static Color Color_Of_DaySky    = new Color(0.35, 0.75, 1.00, 1.0);
    private static Color Color_Of_NightSky  = new Color(0.30, 0.20, 1.00, 1.0);
    private static Color Color_Of_Midnight  = new Color(0.00, 0.00, 0.00, 0.5);

    private static Color Color_Of_SunTime   = new Color(1.00, 0.50, 0.00, 1.0);
    private static Color Color_Of_HighNoon  = new Color(1.00, 1.00, 0.00, 1.0);
    private static Color Color_Of_SunRise   = new Color(1.00, 0.00, 0.00, 1.0);
    private static Color Color_Of_SunSet    = new Color(0.65, 0.00, 0.65, 1.0);
    private static Color Color_Of_LocalTime = new Color(1.00, 1.00, 1.00, 1.0);

    private static Font Font_Of_Info = new Font("Lucida Console", 14);
    private static String Path_Of_Earth = "M 100 100 L 300 100 L 200 300 Z M 150 150 L 100 250 L 350 150 Z";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        GregorianCalendar currentLocalTime = new GregorianCalendar();

        Suntime suntime = new Suntime.Builder()
//                .julianDayNumber(2453097)
                .localTime(currentLocalTime)
                .observerLongitude(15.9816d)
                .observerLatitude(45.7827d)
                .build();

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

        // Earth map
        SVGPath pathOfEarth = new SVGPath();
        pathOfEarth.setContent(Path_Of_Earth);
        pathOfEarth.setStroke(Color_Of_Darkness);
        pathOfEarth.setFill(Color_Of_Earth);

        // Data
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

        double timeZoneOffset = currentLocalTime.getTimeZone().getRawOffset() / (60 * 60 * 1000);

        double sunTimeDialAngle = (sunTime - floor(sunTime) + timeZoneOffset / 24d) * 360d;
        double highNoonDialAngle = (highNoonJulianDate - floor(highNoonJulianDate) + timeZoneOffset / 24d) * 360d;
        double sunriseDialAngle = (sunriseJulianDate - floor(sunriseJulianDate) + timeZoneOffset / 24d) * 360d;
        double sunsetDialAngle = (sunsetJulianDate - floor(sunsetJulianDate) + timeZoneOffset / 24d) * 360d;
        double localTimeDialAngle = (julianDate - floor(julianDate) + timeZoneOffset / 24d) * 360d;

        /*** FX ***/

        // Info text
        Text startupInfoText = new Text();
        startupInfoText.setText(startupInformation);
        startupInfoText.setFont(Font_Of_Info);
        startupInfoText.setLayoutX(5d);
        startupInfoText.setLayoutY(20d);

        Text calculatedInfoText = new Text();
        calculatedInfoText.setText(calculatedInformation);
        calculatedInfoText.setFont(Font_Of_Info);
        calculatedInfoText.setLayoutX(5d);
        calculatedInfoText.setLayoutY(525d);

        // Dials in a box
        Group dialsGroup = new Group();

        Rectangle dialBox = new Rectangle(200, 200);
        dialBox.setOpacity(0.1d);

        Arc dialArcNight = new Arc(100, 100, 100, 100, 90 - sunsetDialAngle, sunsetDialAngle - sunriseDialAngle);
        dialArcNight.setType(ArcType.ROUND);
        dialArcNight.setStroke(Color_Of_Void);
        dialArcNight.setFill(Color_Of_NightSky);

        Arc dialArcMidnight = new Arc(100, 100, 100, 100, 0, -180);
        dialArcMidnight.setType(ArcType.ROUND);
        dialArcMidnight.setStroke(Color_Of_Void);
        dialArcMidnight.setFill(Color_Of_Midnight);

        Circle dialFrameCircle = new Circle(100, 100, 100);
        dialFrameCircle.setFill(Color_Of_DaySky);
        dialFrameCircle.setStroke(Color_Of_Darkness);

        Circle dialDotCircle = new Circle(100, 100, 5);
        dialDotCircle.setFill(Color_Of_Darkness);
        dialDotCircle.setStroke(Color_Of_Void);

        Line sunTimeDial = new Line(100, 50, 100, 0);
        Rotate sunTimeDialRotate = new Rotate();
        sunTimeDialRotate.setPivotX(100);
        sunTimeDialRotate.setPivotY(100);
        sunTimeDialRotate.setAngle(sunTimeDialAngle);
        sunTimeDial.getTransforms().add(sunTimeDialRotate);
        sunTimeDial.setStroke(Color_Of_SunTime);

        Line highNoonDial = new Line(100, 30, 100, 0);
        Rotate highNoonRotate = new Rotate();
        highNoonRotate.setPivotX(100);
        highNoonRotate.setPivotY(100);
        highNoonRotate.setAngle(highNoonDialAngle);
        highNoonDial.getTransforms().add(highNoonRotate);
        highNoonDial.setStroke(Color_Of_HighNoon);

        Line sunriseDial = new Line(100, 100, 100, 0);
        Rotate sunriseRotate = new Rotate();
        sunriseRotate.setPivotX(100);
        sunriseRotate.setPivotY(100);
        sunriseRotate.setAngle(sunriseDialAngle);
        sunriseDial.getTransforms().add(sunriseRotate);
        sunriseDial.setStroke(Color_Of_SunRise);

        Line sunsetDial = new Line(100, 100, 100, 0);
        Rotate sunsetRotate = new Rotate();
        sunsetRotate.setPivotX(100);
        sunsetRotate.setPivotY(100);
        sunsetRotate.setAngle(sunsetDialAngle);
        sunsetDial.getTransforms().add(sunsetRotate);
        sunsetDial.setStroke(Color_Of_SunRise);

        Line localTimeDial = new Line(100, 110, 100, 0);
        Rotate localTimeRotate = new Rotate();
        localTimeRotate.setPivotX(100);
        localTimeRotate.setPivotY(100);
        localTimeRotate.setAngle(localTimeDialAngle);
        localTimeDial.getTransforms().add(localTimeRotate);
        localTimeDial.setStroke(Color_Of_LocalTime);

        dialsGroup.getChildren().add(dialBox);
        dialsGroup.getChildren().add(dialFrameCircle);
        dialsGroup.getChildren().add(dialArcNight);
        dialsGroup.getChildren().add(dialArcMidnight);

        for(int i = 0; i < 96; i++) {

            double lineLength = 5d;
            if (i % 2 == 0) { lineLength = 7.5d; }
            if (i % 4 == 0) { lineLength = 10d; }
            if (i % 24 == 0) { lineLength = 90d; }
            if (i % 48 == 0) { lineLength = 20d; }

            Line hourMarkerLine = new Line(100, lineLength, 100, 0);
            Rotate hourMarkerRotate = new Rotate();
            hourMarkerRotate.setPivotX(100);
            hourMarkerRotate.setPivotY(100);
            hourMarkerRotate.setAngle(i * 360d / 96d);
            hourMarkerLine.getTransforms().add(hourMarkerRotate);
            hourMarkerLine.setStroke(Color_Of_Darkness);
            hourMarkerLine.setStrokeWidth(0.5d);

            dialsGroup.getChildren().add(hourMarkerLine);
        }

        dialsGroup.getChildren().addAll(sunTimeDial, highNoonDial, sunriseDial, sunsetDial, localTimeDial);
        dialsGroup.getChildren().add(dialDotCircle);

        dialsGroup.setScaleX(2.0d);
        dialsGroup.setScaleY(2.0d);

        // Root node group
        Group rootNode = new Group();
        rootNode.getChildren().addAll(startupInfoText, calculatedInfoText, dialsGroup);

        // And scene...
        Scene testScene = new Scene(rootNode, 600,600);
        testScene.setFill(Color_Of_Window);

        primaryStage.setTitle("SunDial");
        primaryStage.setScene(testScene);
        primaryStage.show();

        dialsGroup.setLayoutX(testScene.getWidth() / 2 - dialBox.getWidth() / 2);
        dialsGroup.setLayoutY(testScene.getHeight() / 2 - dialBox.getHeight() / 2);
    }
}
