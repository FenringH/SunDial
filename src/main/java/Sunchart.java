import javafx.scene.Group;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static java.lang.Math.*;

public class Sunchart {

    private final static int CHART_RESOLUTION = 1;
    private final static int DAYS_IN_YEAR = 365;

    private int dataSize = DAYS_IN_YEAR / CHART_RESOLUTION;

    private double longitude;
    private double latitude;
    private int year;

    private GregorianCalendar calendar;
    private Suntime suntime;

    private XYChart.Series sunriseSeries;
    private XYChart.Series sunsetSeries;
    private XYChart.Series daylengthSeries;

    private LineChart suntimeLineChart;

    private String chartTitle;

    StringConverterAxisX<Number> stringConverterAxisX;
    StringConverterAxisY<Number> stringConverterAxisY;


    Sunchart(double longitude, double latitude, int year) {

        this.longitude = longitude;
        this.latitude = latitude;
        this.year = year;

        calendar = new GregorianCalendar();
        calendar.set(Calendar.YEAR, this.year);

        suntime = new Suntime.Builder()
                .localTime(calendar)
                .observerLongitude(this.longitude)
                .observerLatitude(this.latitude)
                .build();

        sunriseSeries = new XYChart.Series();
        sunriseSeries.setName("Sunrise");

        sunsetSeries = new XYChart.Series();
        sunsetSeries.setName("Sunset");

        daylengthSeries = new XYChart.Series();
        daylengthSeries.setName("Day Length");

        for (int i = 0; i < dataSize; i++) {

            String dateNumber = 1 + i * CHART_RESOLUTION + "";

            sunriseSeries.getData().add(new XYChart.Data<>(dateNumber, 0));
            sunsetSeries.getData().add(new XYChart.Data<>(dateNumber, 24));
            daylengthSeries.getData().add(new XYChart.Data<>(dateNumber, 24));
        }

        chartTitle = formatTitle();

        stringConverterAxisX = new StringConverterAxisX<>(this.calendar);
        stringConverterAxisY = new StringConverterAxisY<>();

        NumberAxis suntimeAxisX = new NumberAxis("Date", 0, 365, 1);
        suntimeAxisX.setTickLabelFormatter(stringConverterAxisX);
        suntimeAxisX.setForceZeroInRange(true);

        NumberAxis suntimeAxisY = new NumberAxis("Time", 0, 24, 1);
        suntimeAxisY.setTickLabelFormatter(stringConverterAxisY);

        suntimeLineChart = new LineChart(suntimeAxisX, suntimeAxisY);
        suntimeLineChart.setTitle(chartTitle);
        suntimeLineChart.getData().addAll(sunriseSeries, sunsetSeries, daylengthSeries);
        suntimeLineChart.setAnimated(false);

        recalculateDataPoints();
    }

    private String formatTitle() {
        return "Suntime at "
                + Sundial.formatCoordinateToString(this.longitude, "E", "W")
                + ", " + Sundial.formatCoordinateToString(this.latitude, "N", "S")
                + " in " + calendar.get(Calendar.YEAR) + "."
        ;
    }

    private Group createDataNode(GregorianCalendar calendar, double time) {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder
                .append("Date : ")
                .append(calendar.get(Calendar.DAY_OF_MONTH))
                .append(".")
                .append(calendar.get(Calendar.MONTH) + 1)
                .append(".")
                .append(calendar.get(Calendar.YEAR))
                .append(".\n")
                .append("Time : ")
                .append((int) floor(time))
                .append("h ")
                .append((int) round(time % 60))
                .append("m")
        ;

        Rectangle rectangle = new Rectangle(8, 8);
        rectangle.setArcWidth(3);
        rectangle.setArcHeight(3);
        rectangle.setStroke(Color.GREEN);
        rectangle.setFill(Color.WHITE);

        Text text = new Text(stringBuilder.toString());
        rectangle.setOnMouseEntered(event -> text.setVisible(true));
        rectangle.setOnMouseExited(event -> text.setVisible(false));

        Group dataNodeGroup = new Group(rectangle, text);

        return dataNodeGroup;
    }

    private String getInfoString(GregorianCalendar calendar, double time) {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder
                .append("Date : ")
                .append(calendar.get(Calendar.DAY_OF_MONTH))
                .append(".")
                .append(calendar.get(Calendar.MONTH) + 1)
                .append(".")
                .append(calendar.get(Calendar.YEAR))
                .append(".\n")
                .append("Time : ")
                .append((int) floor(time))
                .append("h ")
                .append((int) round((time * 60) % 60))
                .append("m")
        ;

        return stringBuilder.toString();
    }

    private void recalculateDataPoints() {

        for (int i = 0; i < dataSize; i++) {

            int dayOfYear = 1 + i * CHART_RESOLUTION;

            calendar.set(Calendar.DAY_OF_YEAR, dayOfYear);

            stringConverterAxisX.setCalendar(calendar);

            suntime.setObserverTime(calendar);
            suntime.setObserverPosition(longitude, latitude);

            double sunrise = suntime.getSunriseJulianDate();
            double sunset = suntime.getSunsetJulianDate();
            double daylength = (sunset - sunrise) * 24;

            GregorianCalendar sunriseDate = Suntime.getCalendarDate(sunrise, calendar.getTimeZone());
            GregorianCalendar sunsetDate = Suntime.getCalendarDate(sunset, calendar.getTimeZone());

            double sunriseTime = sunriseDate.get(Calendar.HOUR_OF_DAY) + sunriseDate.get(Calendar.MINUTE) / 60d;
            double sunsetTime = sunsetDate.get(Calendar.HOUR_OF_DAY) + sunsetDate.get(Calendar.MINUTE) / 60d;

            if (daylength >= 24) {
                daylength = 24;
                sunriseTime = 0;
                sunsetTime = 0;
            }

            if (daylength <= 0) {
                daylength = 0;
                sunriseTime = 0;
                sunsetTime = 0;
            }

            XYChart.Data<Integer, Double> sunriseData = new XYChart.Data<>(dayOfYear, sunriseTime);
            XYChart.Data<Integer, Double> sunsetData = new XYChart.Data<>(dayOfYear, sunsetTime);
            XYChart.Data<Integer, Double> daylengthData = new XYChart.Data<>(dayOfYear, daylength);

            sunriseSeries.getData().set(i, sunriseData);
            sunsetSeries.getData().set(i, sunsetData);
            daylengthSeries.getData().set(i, daylengthData);

            Tooltip.install(sunriseData.getNode(), new Tooltip(getInfoString(calendar, sunriseTime)));
            Tooltip.install(sunsetData.getNode(), new Tooltip(getInfoString(calendar, sunsetTime)));
            Tooltip.install(daylengthData.getNode(), new Tooltip(getInfoString(calendar, daylength)));
        }

    }

    public void setSpacetimePosition(double longitude, double latitude, int year) {

        this.longitude = longitude;
        this.latitude = latitude;
        this.year = year;

        calendar.set(Calendar.YEAR, this.year);

        chartTitle = formatTitle();

        suntimeLineChart.setTitle(this.chartTitle);

        recalculateDataPoints();
    }

    public LineChart getChart() {
        return this.suntimeLineChart;
    }


    // Converters
    private class StringConverterAxisX<Number> extends StringConverter<Number> {

        GregorianCalendar calendar;

        public StringConverterAxisX() {
            this.calendar = new GregorianCalendar();
        }

        public StringConverterAxisX(GregorianCalendar calendar) {
            this();
            this.calendar.setTimeInMillis(calendar.getTimeInMillis());
        }

        @Override
        public String toString(Number object) {

            StringBuilder stringBuilder = new StringBuilder();

            int dayOfYear = (int) round((Double) object);
            calendar.set(Calendar.DAY_OF_YEAR, dayOfYear);

            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

/*
            if (dayOfMonth == 1) {
                stringBuilder
                        .append(dayOfMonth)
                        .append(".")
                        .append(calendar.get(Calendar.MONTH) + 1)
                        .append(".");
            }
*/

            if (dayOfMonth == 15) {
                switch (calendar.get(Calendar.MONTH)) {
                    case 0 : stringBuilder.append("January"); break;
                    case 1 : stringBuilder.append("February"); break;
                    case 2 : stringBuilder.append("March"); break;
                    case 3 : stringBuilder.append("April"); break;
                    case 4 : stringBuilder.append("May"); break;
                    case 5 : stringBuilder.append("June"); break;
                    case 6 : stringBuilder.append("July"); break;
                    case 7 : stringBuilder.append("August"); break;
                    case 8 : stringBuilder.append("September"); break;
                    case 9 : stringBuilder.append("October"); break;
                    case 10 : stringBuilder.append("November"); break;
                    case 11 : stringBuilder.append("December"); break;
                }
            }

            return stringBuilder.toString();
        }

        @Override
        public Number fromString(String string) {
            return null;
        }

        public void setCalendar(GregorianCalendar calendar) {
            this.calendar = calendar;
        }
    }

    private class StringConverterAxisY<Number> extends StringConverter<Number> {
        @Override
        public String toString(Number object) {
            StringBuilder stringBuilder = new StringBuilder();
            return stringBuilder.append((int) round((Double) object)).append("h 00m").toString();
        }

        @Override
        public Number fromString(String string) {
            return null;
        }
    };


}
