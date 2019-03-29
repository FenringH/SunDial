import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class Sunyear {

    private final static int DAYS_IN_YEAR = 367;
    private final static int HOURS = 24;

    private final static double SPACING_X = 2.0d;
    private final static double SPACING_Y = 20.0d;

    private final static double MARGIN_X = 30.0d;
    private final static double MARGIN_Y = 30.0d;
    private final static double ARC_WIDTH = 30.0d;
    private final static double ARC_HEIGHT = 30.0d;

    private double longitude;
    private double latitude;
    private int year;
    private long timeZoneOffset;

    private Suntime suntime;

    private GregorianCalendar calendar;

    private ArrayList<Double> sunriseList;
    private ArrayList<Double> sunsetList;
    private ArrayList<Double> daylengthList;

    private Polyline sunriseLine;
    private Polyline sunsetLine;
    private Polyline daylengthLine;

    private double chartLeft;
    private double chartBottom;

    private Group dayMarkerLineGroup;
    private Group dayMarkerTextGroup;

    private Group chart;

    public Sunyear(double longitude, double latitude, int year, long timeZoneOffset) {

        this.longitude = longitude;
        this.latitude = latitude;
        this.year = year;
        this.timeZoneOffset = timeZoneOffset;

        sunriseList = new ArrayList<>();
        sunsetList = new ArrayList<>();
        daylengthList = new ArrayList<>();

        sunriseLine = new Polyline();
        sunriseLine.setStroke(new Color(1.00, 0.50, 0.20, 1.00));
        sunriseLine.setStrokeWidth(2.0);

        sunsetLine = new Polyline();
        sunsetLine.setStroke(new Color(0.20, 0.50, 1.00, 1.00));
        sunsetLine.setStrokeWidth(2.0);

        daylengthLine = new Polyline();
        daylengthLine.setStroke(new Color(0.30, 0.70, 0.30, 1.00));
        daylengthLine.setStrokeWidth(2.0);

        dayMarkerLineGroup = new Group();
        dayMarkerTextGroup = new Group();

        calendar = new GregorianCalendar();
        calendar.set(Calendar.YEAR, this.year);

        suntime = new Suntime.PleaseBuildSuntime()
                .localTime(calendar)
                .observerLongitude(this.longitude)
                .observerLatitude(this.latitude)
                .thankYou();

        chart = createChartGroup();
    }

    private void recalculateDataPoints() {

        sunriseList.clear();
        sunsetList.clear();
        daylengthList.clear();

        for (int i = 0; i < DAYS_IN_YEAR; i++) {

            int dayOfYear = i + 1;

            calendar.set(Calendar.DAY_OF_YEAR, dayOfYear);

            suntime.setObserverTime(calendar);
            suntime.setObserverPosition(longitude, latitude);

            double sunrise = suntime.getSunriseJulianDate();
            double sunset = suntime.getSunsetJulianDate();
            double daylength = (sunset - sunrise) * 24;

            GregorianCalendar sunriseDate = Suntime.getCalendarDate(sunrise, calendar.getTimeZone());
            GregorianCalendar sunsetDate = Suntime.getCalendarDate(sunset, calendar.getTimeZone());

            double sunriseTime = sunriseDate.get(Calendar.HOUR_OF_DAY)
                    + sunriseDate.get(Calendar.MINUTE) / 60d
                    + timeZoneOffset / (60 * 60 * 1000d)
                    - calendar.getTimeZone().getRawOffset() / (60 * 60 * 1000d)
                    ;

            double sunsetTime = sunsetDate.get(Calendar.HOUR_OF_DAY)
                    + sunsetDate.get(Calendar.MINUTE) / 60d
                    + timeZoneOffset / (60 * 60 * 1000d)
                    - calendar.getTimeZone().getRawOffset() / (60 * 60 * 1000d)
                    ;

            sunriseList.add(sunriseTime);
            sunsetList.add(sunsetTime);
            daylengthList.add(daylength);
        }
    }

    private void refreshLines() {

        sunriseLine.getPoints().clear();
        sunsetLine.getPoints().clear();
        daylengthLine.getPoints().clear();

        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.set(Calendar.YEAR, year);

        int month = 0;

        for (int i = 0; i < DAYS_IN_YEAR; i++) {

            double sunrise = sunriseList.get(i);
            double sunset = sunsetList.get(i);
            double daylength = daylengthList.get(i);

            if (sunrise < 0) { sunrise = 0; }
            if (sunrise > 24) { sunrise = 24; }

            if (sunset < 0) { sunset = 0; }
            if (sunset > 24) { sunset = 24; }

            if (daylength < 0) { daylength = 0; }
            if (daylength > 24) { daylength = 24; }

            double x = chartLeft + (i * SPACING_X);

            double sunriseY = chartBottom - (sunrise * SPACING_Y);
            double sunsetY = chartBottom - (sunset * SPACING_Y);
            double daylengthY = chartBottom - (daylength * SPACING_Y);

            sunriseLine.getPoints().addAll(x, sunriseY);
            sunsetLine.getPoints().addAll(x, sunsetY);
            daylengthLine.getPoints().addAll(x, daylengthY);


            int dayOfYear = i + 1;
            gregorianCalendar.set(Calendar.DAY_OF_YEAR, dayOfYear);

            int dayOfMonth = gregorianCalendar.get(Calendar.DAY_OF_MONTH);

            if (dayOfMonth == 1) {

                if (month > dayMarkerLineGroup.getChildren().size() - 1) { continue; }

                Line line = (Line) dayMarkerLineGroup.getChildren().get(month);
                Text text = (Text) dayMarkerTextGroup.getChildren().get(month);

                line.setStartX(x - chartLeft);
                line.setEndX(x - chartLeft);

                text.setX(x - chartLeft);

                month++;
            }
        }
    }

    private Group createChartGroup() {

        double hourLineLength = DAYS_IN_YEAR * SPACING_X;

        Group hourLineGroup = new Group();
        Group hourTextGroup = new Group();

        double maxTextWidth = 0;
        double maxY = 0;

        Font font = new Font(12);

        for (int i = 0; i <= HOURS; i++) {

            double y = i * SPACING_Y;
            if (y > maxY) { maxY = y; }

            String hourMarkerString = (HOURS - i) + ":00";

            Line line = new Line(-5, y, hourLineLength, y);
            line.setStroke(Color.GRAY);

            Text text = new Text(-5, y, hourMarkerString);
            text.setFont(font);
            text.setFill(Color.WHITE);

            double textWidth = text.getLayoutBounds().getWidth();
            if (textWidth > maxTextWidth) { maxTextWidth = textWidth; }

            text.setTranslateX(-textWidth);
            text.setTranslateY(font.getSize() * 0.35);

            hourLineGroup.getChildren().add(line);
            hourTextGroup.getChildren().add(text);
        }

        hourTextGroup.setTranslateX(maxTextWidth - 5);
        hourLineGroup.setTranslateX(maxTextWidth);


        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.set(Calendar.YEAR, year);

        double dayLineHeight = HOURS * SPACING_Y;

        int month = 0;

        for (int i = 0; i < DAYS_IN_YEAR; i++) {

            int dayOfYear = i + 1;
            gregorianCalendar.set(Calendar.DAY_OF_YEAR, dayOfYear);

            int dayOfMonth = gregorianCalendar.get(Calendar.DAY_OF_MONTH);

            if (dayOfMonth == 1) {

                if (month > 12) { continue; }

                double x = i * SPACING_X;
                String dayMarkerString = dayOfMonth + "." + (gregorianCalendar.get(Calendar.MONTH) + 1) + ".";

                Line line = new Line(x, 0, x, dayLineHeight + 5);
                line.setStroke(Color.GRAY);

                Text text = new Text(x, dayLineHeight, dayMarkerString);
                text.setFont(font);
                text.setFill(Color.WHITE);

                text.setTranslateX(-text.getLayoutBounds().getWidth() / 2);
                text.setTranslateY(font.getSize() + 5);

                dayMarkerLineGroup.getChildren().add(line);
                dayMarkerTextGroup.getChildren().add(text);

                month++;
            }

        }

        dayMarkerLineGroup.setTranslateX(maxTextWidth);
        dayMarkerTextGroup.setTranslateX(maxTextWidth);

        Group markersGroup = new Group(hourLineGroup, hourTextGroup, dayMarkerLineGroup, dayMarkerTextGroup);

        double width = markersGroup.getLayoutBounds().getWidth() + MARGIN_X * 2;
        double height = markersGroup.getLayoutBounds().getHeight() + MARGIN_Y * 2;

        Rectangle rectangle = new Rectangle(width, height);
        rectangle.setArcWidth(ARC_WIDTH);
        rectangle.setArcHeight(ARC_HEIGHT);
        rectangle.setStroke(Sunconfig.Color_Of_Void);
        rectangle.setFill(Color.BLACK);
        rectangle.setOpacity(0.8);

        markersGroup.setTranslateX(MARGIN_X);
        markersGroup.setTranslateY(MARGIN_Y);

        sunriseLine.setTranslateX(MARGIN_X);
        sunriseLine.setTranslateY(MARGIN_Y);

        sunsetLine.setTranslateX(MARGIN_X);
        sunsetLine.setTranslateY(MARGIN_Y);

        daylengthLine.setTranslateX(MARGIN_X);
        daylengthLine.setTranslateY(MARGIN_Y);

        chartLeft = maxTextWidth;
        chartBottom = maxY;

        return new Group(rectangle, markersGroup, sunriseLine, sunsetLine, daylengthLine);
    }

    public void setSpaceTime(double longitude, double latitude, int year, long timeZoneOffset) {

        this.longitude = longitude;
        this.latitude = latitude;
        this.year = year;
        this.timeZoneOffset = timeZoneOffset;

        calendar.set(Calendar.YEAR, year);

        recalculateDataPoints();
        refreshLines();
    }

    public Group getChart() {
        return chart;
    }

    public int getYear() {
        return year;
    }
}