import javafx.scene.Group;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.*;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static java.lang.Math.abs;

public class Sunyear {

    private final static int DAYS_IN_YEAR = 367;
    private final static int HOURS = 24;

    private final static double SPACING_X = 2.0d;
    private final static double SPACING_Y = 20.0d;

    private final static double AREA_HEIGHT = HOURS * SPACING_Y;

    private final static double MARGIN_X = 30.0d;
    private final static double MARGIN_Y = 20.0d;
    private final static double ARC_WIDTH = 30.0d;
    private final static double ARC_HEIGHT = 30.0d;

    private final static LinearGradient SUNRISE_GRADIENT = new LinearGradient(
            0, 0,
            0, 1,
            true,
            CycleMethod.NO_CYCLE,
            new Stop(0.50, Color.SKYBLUE),
            new Stop(0.85, Color.DEEPSKYBLUE),
            new Stop(0.95,  new Color(1.00, 0.50, 0.00, 1.00)),
            new Stop(0.98, Color.RED),
            new Stop(1.00, Color.YELLOW)
    );

    private final static LinearGradient SUNSET_GRADIENT = new LinearGradient(
            0, 0,
            0, 1,
            true,
            CycleMethod.NO_CYCLE,
            new Stop(0.00, Color.YELLOW),
            new Stop(0.02, Color.RED),
            new Stop(0.05, new Color(1.00, 0.50, 0.00, 1.00)),
            new Stop(0.15, Color.DEEPSKYBLUE),
            new Stop(0.50, Color.SKYBLUE)
    );

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

    private ArrayList<Rectangle> sunriseBarList;
    private ArrayList<Rectangle> sunsetBarList;

    private Text chartTitleText;

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
//        daylengthLine.setStroke(new Color(0.30, 0.70, 0.30, 1.00));
        daylengthLine.setStroke(Color.WHITE);
        daylengthLine.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(  0, 192,  0, 1.0),  8.0, 0.60, 0, 0);");
        daylengthLine.setStrokeWidth(2.0);
        daylengthLine.setBlendMode(BlendMode.SCREEN);

        dayMarkerLineGroup = new Group();
        dayMarkerTextGroup = new Group();

        sunriseBarList = new ArrayList<>();
        sunsetBarList = new ArrayList<>();

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

            long timeZoneCorrection = timeZoneOffset - calendar.getTimeZone().getRawOffset();

            long sunriseInMillis = sunriseDate.getTimeInMillis() + timeZoneCorrection;
            long sunsetInMillis = sunsetDate.getTimeInMillis() + timeZoneCorrection;

            GregorianCalendar sunriseDateCorrected = new GregorianCalendar(calendar.getTimeZone());
            sunriseDateCorrected.setTimeInMillis(sunriseInMillis);
            GregorianCalendar sunsetDateCorrected = new GregorianCalendar(calendar.getTimeZone());
            sunsetDateCorrected.setTimeInMillis(sunsetInMillis);

            double sunriseTime = sunriseDateCorrected.get(Calendar.HOUR_OF_DAY) + sunriseDateCorrected.get(Calendar.MINUTE) / 60d;
            double sunsetTime = sunsetDateCorrected.get(Calendar.HOUR_OF_DAY) + sunsetDateCorrected.get(Calendar.MINUTE) / 60d;

/*
            System.out.println(
                    dayOfYear + ". "
                            + " | timeZoneOffset = " + (timeZoneOffset / (60 * 60 * 1000d))
                            + " | raw offset = " + (- calendar.getTimeZone().getRawOffset() / (60 * 60 * 1000d))
                            + " | sunrise hour = " + sunriseDate.get(Calendar.HOUR_OF_DAY)
                            + " | sunset hour = " + sunsetDate.get(Calendar.HOUR_OF_DAY)
                            + " | sunrise = " + sunriseTime
                            + " | sunset = " + sunsetTime
            );
*/

            sunriseList.add(sunriseTime);
            sunsetList.add(sunsetTime);
            daylengthList.add(daylength);
        }
    }

    private void refreshLines() {

//        sunriseLine.getPoints().clear();
//        sunsetLine.getPoints().clear();
        daylengthLine.getPoints().clear();

        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.set(Calendar.YEAR, year);

        int month = 0;

        for (int i = 0; i < DAYS_IN_YEAR; i++) {

            double sunrise = sunriseList.get(i);
            double sunset = sunsetList.get(i);
            double daylength = daylengthList.get(i);

            if (daylength < 0) { daylength = 0; }
            if (daylength > 24) { daylength = 24; }

            double x = i * SPACING_X;

            double sunriseY = AREA_HEIGHT - sunrise * SPACING_Y;
            double sunsetY = AREA_HEIGHT - sunset * SPACING_Y;

            double daylengthSizeY = daylength * SPACING_Y;
            double daylengthY = AREA_HEIGHT - daylengthSizeY;

//            sunriseLine.getPoints().addAll(x, sunriseY);
//            sunsetLine.getPoints().addAll(x, sunsetY);
            daylengthLine.getPoints().addAll(x, daylengthY);

            Rectangle sunriseBar = sunriseBarList.get(i);
            Rectangle sunsetBar = sunsetBarList.get(i);

            sunriseBar.setHeight(daylengthSizeY / 2 + 1);
            sunriseBar.setTranslateY(sunriseY - daylengthSizeY / 2 - 1);

            sunsetBar.setHeight(daylengthSizeY / 2 + 1);
            sunsetBar.setTranslateY(sunsetY + 1);

            int dayOfYear = i + 1;
            gregorianCalendar.set(Calendar.DAY_OF_YEAR, dayOfYear);

            int dayOfMonth = gregorianCalendar.get(Calendar.DAY_OF_MONTH);

            if (dayOfMonth == 1) {

                if (month > dayMarkerLineGroup.getChildren().size() - 1) { continue; }

                Line line = (Line) dayMarkerLineGroup.getChildren().get(month);
                Text text = (Text) dayMarkerTextGroup.getChildren().get(month);

                line.setStartX(x);
                line.setEndX(x);

                text.setX(x);

                month++;
            }
        }
    }

    private Group createChartGroup() {

        Group chartArea = new Group();
        Group chartGrid = new Group();
        Group chartAxisX = new Group();
        Group chartAxisY = new Group();
        Group chartTitle = new Group();
        Group chartFrame = new Group();

        double areaWidth = DAYS_IN_YEAR * SPACING_X;
        double areaHeight = HOURS * SPACING_Y;
        int month;
        GregorianCalendar gregorianCalendar = new GregorianCalendar();

        Font fontAxis = new Font(12);
        Font fontTitle = new Font(24);

        // AREA
        Rectangle rectangleArea = new Rectangle(areaWidth, areaHeight);
        rectangleArea.setStroke(Sunconfig.Color_Of_Void);
        rectangleArea.setFill(new Color(0.20, 0.10, 0.50, 1.00));
        rectangleArea.setOpacity(0.8);

        for (int i = 0; i < DAYS_IN_YEAR; i++) {

            int dayOfYear = i + 1;
            double x = i * SPACING_X;

            Rectangle rectangleSunrise = new Rectangle(SPACING_X * 2, 10);
            rectangleSunrise.setTranslateX(x - SPACING_X / 2);
            rectangleSunrise.setFill(SUNRISE_GRADIENT);
            rectangleSunrise.setStroke(Color.TRANSPARENT);

            Rectangle rectangleSunset = new Rectangle(SPACING_X * 2, 10);
            rectangleSunset.setTranslateX(x - SPACING_X / 2);
            rectangleSunset.setFill(SUNSET_GRADIENT);
            rectangleSunset.setStroke(Color.TRANSPARENT);

            sunriseBarList.add(rectangleSunrise);
            sunsetBarList.add(rectangleSunset);
        }

        Group barsGroup = new Group();
        barsGroup.getChildren().addAll(sunriseBarList);
        barsGroup.getChildren().addAll(sunsetBarList);
        barsGroup.setOpacity(1);

        chartArea.getChildren().addAll(rectangleArea, barsGroup, /*sunriseLine, sunsetLine, */daylengthLine);

        // GRID
        for (int i = 0; i <= HOURS; i++) {
            double y = i * SPACING_Y;
            Line line = new Line(-5, y, areaWidth, y);
            line.setStroke(Color.WHITE);

            chartGrid.getChildren().add(line);
        }

        chartGrid.setOpacity(0.65);
        chartGrid.setBlendMode(BlendMode.SCREEN);

        gregorianCalendar.set(Calendar.YEAR, year);
        month = 0;
        for (int i = 0; i < DAYS_IN_YEAR; i++) {

            int dayOfYear = i + 1;
            gregorianCalendar.set(Calendar.DAY_OF_YEAR, dayOfYear);

            int dayOfMonth = gregorianCalendar.get(Calendar.DAY_OF_MONTH);

            if (dayOfMonth == 1) {

                if (month > 12) { continue; }

                double x = i * SPACING_X;
                Line line = new Line(x, 0, x, areaHeight + 5);
                line.setStroke(Color.GRAY);

                month++;

                dayMarkerLineGroup.getChildren().add(line);
            }
        }

        chartGrid.getChildren().add(dayMarkerLineGroup);

        // AXIS Y
        double maxWidth = 0;

        Group axisYTextGroup = new Group();
        for (int i = 0; i <= HOURS; i++) {

            double y = i * SPACING_Y;

            String hourMarkerString = (HOURS - i) + ":00";

            Text text = new Text(0, y, hourMarkerString);
            text.setFont(fontAxis);
            text.setFill(Color.WHITE);

            double width = text.getLayoutBounds().getWidth();
            text.setTranslateX(-width);

            if (width > maxWidth) { maxWidth = width; }

            axisYTextGroup.getChildren().add(text);
        }

        Rectangle axisYRectangle = new Rectangle(maxWidth, axisYTextGroup.getLayoutBounds().getHeight());
        axisYRectangle.setFill(Color.TRANSPARENT);
        axisYRectangle.setStroke(Color.TRANSPARENT);

        axisYTextGroup.setTranslateX(maxWidth);

        chartAxisY.getChildren().addAll(axisYRectangle, axisYTextGroup);

        // AXIS X
        gregorianCalendar.set(Calendar.YEAR, year);
        month = 0;

        for (int i = 0; i < DAYS_IN_YEAR; i++) {

            int dayOfYear = i + 1;
            gregorianCalendar.set(Calendar.DAY_OF_YEAR, dayOfYear);

            int dayOfMonth = gregorianCalendar.get(Calendar.DAY_OF_MONTH);

            if (dayOfMonth == 1) {

                if (month > 12) { continue; }

                double x = i * SPACING_X;
                String dayMarkerString = dayOfMonth + "." + (gregorianCalendar.get(Calendar.MONTH) + 1) + ".";

                Text text = new Text(x, 0, dayMarkerString);
                text.setFont(fontAxis);
                text.setFill(Color.WHITE);

                month++;

                dayMarkerTextGroup.getChildren().add(text);
            }
        }

        dayMarkerTextGroup.setTranslateX(-fontAxis.getSize());

        chartAxisX.getChildren().add(dayMarkerTextGroup);

        // TITLE
        String titleString = formatTitle();

        chartTitleText = new Text(titleString);
        chartTitleText.setFont(fontTitle);
        chartTitleText.setFill(Color.WHITE);

        double titleWidth = chartTitleText.getLayoutBounds().getWidth();
        double titleHeight = chartTitleText.getLayoutBounds().getHeight();

        Rectangle titleRectangle = new Rectangle(titleWidth, titleHeight);
        titleRectangle.setFill(Color.TRANSPARENT);
        titleRectangle.setStroke(Color.TRANSPARENT);

        chartTitle.getChildren().addAll(titleRectangle, chartTitleText);

        // ALL GROUPS sans FRAME
        double chartTitleHeight = chartTitle.getLayoutBounds().getHeight() / 2;
        double chartAxisYWidth = chartAxisY.getLayoutBounds().getWidth();
        double chartAreaHeight = chartArea.getLayoutBounds().getHeight();

        chartTitle.setTranslateX(chartAxisYWidth + areaWidth / 2 - chartTitle.getLayoutBounds().getWidth() / 2);
        chartTitle.setTranslateY(MARGIN_Y * 0.75);

        chartArea.setTranslateX(chartAxisYWidth);
        chartArea.setTranslateY(chartTitleHeight);

        chartGrid.setTranslateX(chartAxisYWidth);
        chartGrid.setTranslateY(chartTitleHeight);

        chartAxisY.setTranslateX(-10);
        chartAxisY.setTranslateY(chartTitleHeight + fontAxis.getSize() * 0.35);

        chartAxisX.setTranslateX(chartAxisYWidth + fontAxis.getSize() * 0.5);
        chartAxisX.setTranslateY(chartAreaHeight + chartTitleHeight + fontAxis.getSize() * 1.5);

        Group chartContents = new Group(chartTitle, chartAxisX, chartAxisY, chartArea, chartGrid);

        // RESIZE
        Text helpText = new Text("Resize");
        ControlThingy resizeControlThingy = Suncreator.createControlThingy(Suncreator.ControlThingyType.RESIZE, helpText);

        // FRAME
        double contentWidth = chartContents.getLayoutBounds().getWidth() + MARGIN_X * 2;
        double contentHeight = chartContents.getLayoutBounds().getHeight() + MARGIN_Y * 2;

        Rectangle rectangleFrame = new Rectangle(contentWidth, contentHeight);
        rectangleFrame.setArcWidth(ARC_WIDTH);
        rectangleFrame.setArcHeight(ARC_HEIGHT);
        rectangleFrame.setStroke(Sunconfig.Color_Of_Void);
        rectangleFrame.setFill(Color.BLACK);
        rectangleFrame.setOpacity(0.7);

        resizeControlThingy.setPosition(contentWidth - resizeControlThingy.getLayoutBounds().getWidth(),
                contentHeight -resizeControlThingy.getLayoutBounds().getHeight());

        chartFrame.getChildren().addAll(rectangleFrame, resizeControlThingy);

        chartContents.setTranslateX(MARGIN_X);
        chartContents.setTranslateY(MARGIN_Y);

        return new Group(chartFrame, chartContents);
    }

    private String formatTitle() {
        return "Sun Chart"
                + " for year " + calendar.get(Calendar.YEAR)
                + " at "
                + Sunutil.formatCoordinateToString(this.longitude, "E", "W")
                + " / "
                + Sunutil.formatCoordinateToString(this.latitude, "N", "S")
                ;
    }


    public void setSpaceTime(double longitude, double latitude, int year, long timeZoneOffset) {

        this.longitude = longitude;
        this.latitude = latitude;
        this.year = year;
        this.timeZoneOffset = timeZoneOffset;

        calendar.set(Calendar.YEAR, year);

        chartTitleText.setText(formatTitle());

        recalculateDataPoints();
        refreshLines();
    }

    public Group getChart() {
        return chart;
    }

    public int getYear() {
        return year;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public long getTimeZoneOffset() {
        return timeZoneOffset;
    }
}