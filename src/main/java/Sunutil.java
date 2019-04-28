import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static java.lang.Math.*;

public class Sunutil {

    public static double getNightCompressionAngle(double angle, double nightCompression) {

        double newAngle = angle;

        if (angle > 0 && angle <= 90) { newAngle = angle + angle * nightCompression / 90; }
        if (angle > 90 && angle <= 180) { newAngle = angle + (180 - angle) * nightCompression / 90; }
        if (angle > 180 && angle <= 270) { newAngle = angle - (angle - 180) * nightCompression / 90; }
        if (angle > 270 && angle <= 360) { newAngle = angle - (360 - angle) * nightCompression / 90; }

        return newAngle;
    }

    public static Timeline createTimelineForLED(Node node, double endValue, int duration) {

        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        timeline.setRate(1);
        timeline.setAutoReverse(false);
        KeyValue keyValue = new KeyValue(node.opacityProperty(), endValue, Interpolator.EASE_IN);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(duration), keyValue);
        timeline.getKeyFrames().add(keyFrame);

        return timeline;
    }

    public static double getCleanAngle(GregorianCalendar calendar) {

        if(calendar == null) { return 0; }

        double hour = (double) calendar.get(Calendar.HOUR_OF_DAY);
        double minute = (double) calendar.get(Calendar.MINUTE);
        double second = (double) calendar.get(Calendar.SECOND);

        double angle = (hour / 24d + minute / (24d * 60d) + second / (24d * 60d * 60d)) * 360d + 180d;

        return angle;
    }

    public static String getShortTimeString(GregorianCalendar calendar) {

        String hourString = ("00" + calendar.get(Calendar.HOUR_OF_DAY));
        hourString = hourString.substring(hourString.length() - 2);

        String minuteString = ("00" + calendar.get(Calendar.MINUTE));
        minuteString = minuteString.substring(minuteString.length() - 2);

        String secondString = ("00" + calendar.get(Calendar.SECOND));
        secondString = secondString.substring(secondString.length() - 2);

        return hourString + ":" + minuteString + ":" + secondString;
    }

    public static String getShorterTimeString(GregorianCalendar calendar) {

        int minutes = calendar.get(Calendar.MINUTE) + round(calendar.get(Calendar.SECOND) / 60f);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);

        if (minutes >= 60) {

            hours++;
            minutes = 0;

            if (hours >= 24) {
                hours = 0;
            }
        }

        String hourString = ("00" + hours);
        hourString = hourString.substring(hourString.length() - 2);

        String minuteString = ("00" + minutes);
        minuteString = minuteString.substring(minuteString.length() - 2);

        return hourString + ":" + minuteString;
    }

    public static String getShortTimeLengthString(double inputSeconds) {

        double precisionHours = inputSeconds / (60 * 60);
        int hours = (int) floor(precisionHours);

        double precisionMins = (precisionHours - hours) * 60;
        int minutes = (int) floor(precisionMins);

        double precisionSecs = (precisionMins - minutes) * 60;
        int seconds = (int) floor(precisionSecs);

        String hoursString = ("00" + hours);
        hoursString = hoursString.substring(hoursString.length() - 2);

        String minutesString = ("00" + minutes);
        minutesString = minutesString.substring(minutesString.length() - 2);

        String secondsString = ("00" + seconds);
        secondsString = secondsString.substring(secondsString.length() - 2);

        return hoursString + "h" + minutesString + "m" + secondsString + "s";
    }

    public static String getShortDateString(GregorianCalendar calendar) {

        String dayString = "00" + calendar.get(Calendar.DAY_OF_MONTH);
        String monthString = "00" + (calendar.get(Calendar.MONTH) + 1);
        String yearString = "0000" + calendar.get(Calendar.YEAR);

        dayString = dayString.substring(dayString.length() - 2);
        monthString = monthString.substring(monthString.length() - 2);
        yearString = yearString.substring(yearString.length() - 4);

        return dayString + "." + monthString + "." + yearString + ".";
    }

    public static String getShorterTimeLengthString(double inputSeconds) {

        double precisionHours = inputSeconds / (60 * 60);
        int hours = (int) floor(precisionHours);

        double precisionMins = (precisionHours - hours) * 60;
        int minutes = (int) round(precisionMins);

        String hoursString = ("00" + hours);
        hoursString = hoursString.substring(hoursString.length() - 2);

        String minutesString = ("00" + minutes);
        minutesString = minutesString.substring(minutesString.length() - 2);

        return hoursString + "h" + minutesString + "m";
    }

    public static String formatCoordinateToString(double coordinate, String suffixPositive, String suffixNegative) {

        DecimalFormat coordinateFormat = new DecimalFormat("#0.00");

        String coordinateString = coordinateFormat.format(coordinate);

        String whole = "   " + coordinateString.replace("-", "").split("[.,]")[0];
        whole = whole.substring(whole.length() - 3);

        String decimal = coordinateString.split("[.,]")[1].substring(0, 2);

        String result = whole + "." + decimal;

        if (coordinate < 0) {
            result += suffixNegative;
        } else {
            result += suffixPositive;
        }

        return result;
    }

    public static double getRemainder(double a, double b) {
        double division = a / b;
        return (division - floor(division)) * b;
    }

    public static WritableImage convertSphericalToCylindricalMapping(Image input) {

        if (input == null) { return null; }

        double H = input.getHeight();
        double L = input.getHeight();

        int width = (int) floor(input.getWidth());
        int height = (int) floor(H);

        WritableImage output = new WritableImage(width, height);

        for (int x = 0; x < width; x++) {

            for (int y = 0; y < height; y++) {

                double yS = (L/PI)*asin(2*(y/H) - 1) + L/2;
                output.getPixelWriter().setColor(x, y, input.getPixelReader().getColor(x, (int) floor(yS)));
            }

        }

        return output;
    }

    public static Color averageColor(ArrayList<Color> colorList) {

        int N = colorList.size();
        double r = 0;
        double g = 0;
        double b = 0;
        double a = 0;

        for (int i = 0; i < N; i++) {
            r += colorList.get(i).getRed();
            g += colorList.get(i).getGreen();
            b += colorList.get(i).getBlue();
            a += colorList.get(i).getOpacity();
        }

        return new Color(r/N, g/N, b/N, a/N);
    }

    public static long rotateTimeZoneOffset(long timeZoneOffset) {

        long result = timeZoneOffset;

        if (result > (12 * 60 * 60 * 1000)) {
            result = -11 * 60 * 60 * 1000;
        }

        if (result <= (-12 * 60 * 60 * 1000)) {
            result =  12 * 60 * 60 * 1000;
        }

        return result;
    }
}
