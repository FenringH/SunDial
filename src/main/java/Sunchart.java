import javafx.scene.Group;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

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

        chartTitle = "Suntime @ " + this.longitude + ", " + this.latitude + " in " + calendar.get(Calendar.YEAR) + ".";

//        NumberAxis suntimeAxisX = new NumberAxis();
        CategoryAxis suntimeAxisX = new CategoryAxis();
        suntimeAxisX.setLabel("Day");
        NumberAxis suntimeAxisY = new NumberAxis();
        suntimeAxisY.setLabel("Time");

        suntimeLineChart = new LineChart(suntimeAxisX, suntimeAxisY);
        suntimeLineChart.setTitle(chartTitle);
        suntimeLineChart.getData().addAll(sunriseSeries, sunsetSeries, daylengthSeries);
        suntimeLineChart.setAnimated(false);

        recalculateDataPoints();
    }

    private void recalculateDataPoints() {

        for (int i = 0; i < dataSize; i++) {

            int dayNumber = 1 + i * CHART_RESOLUTION;

            calendar.set(Calendar.DAY_OF_YEAR, dayNumber);
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

            String dayString = (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);

            sunriseSeries.getData().set(i, new XYChart.Data<>(dayString, sunriseTime));
            sunsetSeries.getData().set(i, new XYChart.Data<>(dayString, sunsetTime));
            daylengthSeries.getData().set(i, new XYChart.Data<>(dayString, daylength));
        }

    }

    public void setSpacetimePosition(double longitude, double latitude, int year) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.year = year;

        this.chartTitle = "Suntime @ " + this.longitude + ", " + this.latitude + " in " + calendar.get(Calendar.YEAR) + ".";

        suntimeLineChart.setTitle(this.chartTitle);

        recalculateDataPoints();
    }

    public LineChart getChart() {
        return this.suntimeLineChart;
    }
}
