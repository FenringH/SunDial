import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sun.security.provider.Sun;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Sunface extends Application {

    private static Color Color_Of_Window = new Color(0.65, 0.85, 0.85, 1.0);
    private static Color Color_Of_Earth = new Color(0.85, 0.85, 0.65, 1.0);
    private static Color Color_Of_Darkness = new Color(0.00, 0.00, 0.00, 1.0);

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

        // Text
        Text textCurrentTime = new Text();
        textCurrentTime.setFont(Font_Of_Info);
        textCurrentTime.setX(50);
        textCurrentTime.setY(50);
        textCurrentTime.setText(
                    "Day[9] date              : " + currentLocalTime.getTime().toString()
                + "\nDay[9] day of the year   : " + currentLocalTime.get(Calendar.DAY_OF_YEAR)
                + "\nDay[9] Julian Date       : " + julianDate
                + "\nDay[9] Gregorian Date    : " + Suntime.getCalendarDate(julianDate, currentLocalTime.getTimeZone()).getTime().toString()
                + "\nDay[9] Julian Day Number : " + julianDayNumber
        );

        // Text 2
        Text textCalc = new Text();
        textCalc.setFont(Font_Of_Info);
        textCalc.setX(50);
        textCalc.setY(150);
        textCalc.setText(
                    "Calc Results: "
                + "\nSun Time   : " + sunTimeDate.getTime().toString()
                + "\nHigh Noon  : " + highNoonDate.getTime().toString()
                + "\nSunrise    : " + sunriseDate.getTime().toString()
                + "\nSunset     : " + sunsetDate.getTime().toString()
                + "\nDay Length : " + Suntime.printSecondsToTime(Suntime.convertFractionToSeconds(dayLength))
        );

        // Root node group
        Group rootNode = new Group();

        ObservableList rootNodeChildren = rootNode.getChildren();
//        rootNodeChildren.add(pathOfEarth);
        rootNodeChildren.add(textCurrentTime);
        rootNodeChildren.add(textCalc);

        // And scene...
        Scene testScene = new Scene(rootNode, 600,300);
        testScene.setFill(Color_Of_Window);

        primaryStage.setTitle("SunDial");
        primaryStage.setScene(testScene);

        primaryStage.show();
    }
}
