import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Calendar;
import java.util.Date;
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

        GregorianCalendar currentCalendar = new GregorianCalendar();

        GregorianCalendar dayNine = new GregorianCalendar();
        double dayNineDayNumber = Suntime.getJulianDayNumber(dayNine);

        Suntime suntime = new Suntime.Builder()
//                .julianDayNumber(2453097.3d)
//                .observerLongitude(5.0d)
//                .observerLatitude(52.0d)
                .julianDayNumber(dayNineDayNumber)
                .observerLongitude(15.9816d)
                .observerLatitude(45.7827d)
                .build();

        double sunriseJulianDate = suntime.getSunriseJulianDate();
        double sunsetJulianDate = suntime.getSunsetJulianDate();
        double highnoonJulianDate = suntime.getHighnoonJulianDate();

        double dayLength = sunsetJulianDate - sunriseJulianDate;

        GregorianCalendar sunriseDate = Suntime.getCalendarDate(sunriseJulianDate, currentCalendar.getTimeZone());
        GregorianCalendar sunsetDate = Suntime.getCalendarDate(sunsetJulianDate, currentCalendar.getTimeZone());
        GregorianCalendar highnoonDate = Suntime.getCalendarDate(highnoonJulianDate, currentCalendar.getTimeZone());

        GregorianCalendar fakeNoon = new GregorianCalendar();
        fakeNoon.set(
            currentCalendar.get(Calendar.YEAR),
            currentCalendar.get(Calendar.MONTH),
            currentCalendar.get(Calendar.DAY_OF_MONTH),
            12, 0, 0
        );

        long timeOffset = fakeNoon.getTimeInMillis() - highnoonDate.getTimeInMillis();

        GregorianCalendar sunTime = new GregorianCalendar();
        sunTime.setTimeInMillis(currentCalendar.getTimeInMillis() + timeOffset);

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
                  "Day[9] date              : " + dayNine.getTime().toString() +
                "\nDay[9] day of the year   : " + dayNine.get(Calendar.DAY_OF_YEAR) +
                "\nDay[9] Julian Day Number : " + dayNineDayNumber);

        // Text 2
        Text textCalc = new Text();
        textCalc.setFont(Font_Of_Info);
        textCalc.setX(50);
        textCalc.setY(150);
        textCalc.setText(
                  "Calc Results: "
                + "\nSun Time   : " + sunTime.getTime().toString()
                + "\nHigh Noon  : " + highnoonDate.getTime().toString()
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
