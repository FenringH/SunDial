import javafx.scene.Group;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Sunchart {

    private final static int CHART_RESOLUTION = 1;
    private final static int DAYS_IN_YEAR = 366;

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
            sunriseSeries.getData().add(new XYChart.Data<>(1 + i * CHART_RESOLUTION, 0));
            sunsetSeries.getData().add(new XYChart.Data<>(1 + i * CHART_RESOLUTION, 0));
            daylengthSeries.getData().add(new XYChart.Data<>(1 + i * CHART_RESOLUTION, 0));
        }

        chartTitle = "Suntime @ " + this.longitude + ", " + this.latitude + " in " + calendar.get(Calendar.YEAR) + ".";

        NumberAxis suntimeAxisX = new NumberAxis();
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

            calendar.set(Calendar.DAY_OF_YEAR, 1 + i * CHART_RESOLUTION);
            suntime.setObserverTime(calendar);
            suntime.setObserverPosition(longitude, latitude);

            double sunrise = suntime.getSunriseJulianDate() - suntime.getJulianDayNumber();
            double sunset = suntime.getSunsetJulianDate() - suntime.getJulianDayNumber();
            double daylength = sunset - sunrise;

            sunriseSeries.getData().set(i, new XYChart.Data<>(1 + i * CHART_RESOLUTION, sunrise));
            sunsetSeries.getData().set(i, new XYChart.Data<>(1 + i * CHART_RESOLUTION, sunset));
            daylengthSeries.getData().set(i, new XYChart.Data<>(1 + i * CHART_RESOLUTION, daylength));
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
