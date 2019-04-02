import javafx.beans.binding.Bindings;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.MouseEvent;
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

    private double defaultWidth;
    private double defaultHeight;

    private double longitude;
    private double latitude;
    private int year;
    private long timeZoneOffset;

    private Suntime suntime;

    private GregorianCalendar calendar;

    private ArrayList<GregorianCalendar> sunriseDateList;
    private ArrayList<GregorianCalendar> sunsetDateList;

    private ArrayList<Double> sunriseList;
    private ArrayList<Double> sunsetList;
    private ArrayList<Double> daylengthList;

    private Polyline sunriseLine;
    private Polyline sunsetLine;
    private Polyline daylengthLine;

    private Text mouseTrapText;
    private Group mouseTrapInfoGroup;
    private Group mouseTrapBarGroup;

    private Text chartTitleText;

    private Group dayMarkerLineGroup;
    private Group dayMarkerTextGroup;

    private ControlThingy controlThingyResize;
    private ControlThingy controlThingyClose;
    private ControlThingy controlThingyMaximize;

    private Group chartFrame;
    private Group chart;

    public Sunyear(double longitude, double latitude, int year, long timeZoneOffset) {

        this.longitude = longitude;
        this.latitude = latitude;
        this.year = year;
        this.timeZoneOffset = timeZoneOffset;

        sunriseDateList = new ArrayList<>();
        sunsetDateList = new ArrayList<>();

        sunriseList = new ArrayList<>();
        sunsetList = new ArrayList<>();
        daylengthList = new ArrayList<>();

        sunriseLine = new Polyline();
//        sunriseLine.setStroke(new Color(1.00, 0.50, 0.20, 1.00));
        sunriseLine.setStroke(Color.WHITE);
        sunriseLine.setStrokeWidth(2.0);
        sunriseLine.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(255, 64, 0, 1.0),  8.0, 0.60, 0, 0);");
        sunriseLine.setBlendMode(BlendMode.SCREEN);

        sunsetLine = new Polyline();
//        sunsetLine.setStroke(new Color(0.20, 0.50, 1.00, 1.00));
        sunsetLine.setStroke(Color.WHITE);
        sunsetLine.setStrokeWidth(2.0);
        sunsetLine.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0, 64, 255, 1.0),  8.0, 0.60, 0, 0);");
        sunsetLine.setBlendMode(BlendMode.SCREEN);

        daylengthLine = new Polyline();
//        daylengthLine.setStroke(new Color(0.30, 0.70, 0.30, 1.00));
        daylengthLine.setStroke(Color.WHITE);
        daylengthLine.setStrokeWidth(2.0);
        daylengthLine.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0, 192, 0, 1.0),  8.0, 0.60, 0, 0);");
        daylengthLine.setBlendMode(BlendMode.SCREEN);

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

        defaultWidth = chart.getLayoutBounds().getWidth();
        defaultHeight = chart.getLayoutBounds().getHeight();
    }

    private void recalculateDataPoints() {

        sunriseDateList.clear();
        sunsetDateList.clear();

        sunriseList.clear();
        sunsetList.clear();
        daylengthList.clear();

        GregorianCalendar gregorianCalendar = new GregorianCalendar(calendar.getTimeZone());
        gregorianCalendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));

        for (int i = 0; i < DAYS_IN_YEAR; i++) {

            int dayOfYear = i + 1;

            gregorianCalendar.set(Calendar.DAY_OF_YEAR, dayOfYear);

            suntime.setObserverTime(gregorianCalendar);
            suntime.setObserverPosition(longitude, latitude);

            double sunrise = suntime.getSunriseJulianDate();
            double sunset = suntime.getSunsetJulianDate();
            double daylength = (sunset - sunrise) * 24;

            GregorianCalendar sunriseDate = Suntime.getCalendarDate(sunrise, gregorianCalendar.getTimeZone());
            GregorianCalendar sunsetDate = Suntime.getCalendarDate(sunset, gregorianCalendar.getTimeZone());

            long timeZoneCorrection = timeZoneOffset - gregorianCalendar.getTimeZone().getRawOffset();

            long sunriseInMillis = sunriseDate.getTimeInMillis() + timeZoneCorrection;
            long sunsetInMillis = sunsetDate.getTimeInMillis() + timeZoneCorrection;

            GregorianCalendar sunriseDateCorrected = new GregorianCalendar(gregorianCalendar.getTimeZone());
            sunriseDateCorrected.setTimeInMillis(sunriseInMillis);

            GregorianCalendar sunsetDateCorrected = new GregorianCalendar(gregorianCalendar.getTimeZone());
            sunsetDateCorrected.setTimeInMillis(sunsetInMillis);

            double sunriseTime = sunriseDateCorrected.get(Calendar.HOUR_OF_DAY) + sunriseDateCorrected.get(Calendar.MINUTE) / 60d;
            double sunsetTime = sunsetDateCorrected.get(Calendar.HOUR_OF_DAY) + sunsetDateCorrected.get(Calendar.MINUTE) / 60d;

            sunriseDateList.add(sunriseDateCorrected);
            sunsetDateList.add(sunsetDateCorrected);

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

            if (daylength <= 0) {
                daylength = 0;
                sunrise = 0;
                sunset = 0;
            }

            if (daylength >= 24) {
                daylength = 24;
                sunrise = 0;
                sunset = 24;
            }

            double x = i * SPACING_X;

            double sunriseY = AREA_HEIGHT - sunrise * SPACING_Y;
            double sunsetY = AREA_HEIGHT - sunset * SPACING_Y;
            double daylengthY = AREA_HEIGHT - daylength * SPACING_Y;

            sunriseLine.getPoints().addAll(x, sunriseY);
            sunsetLine.getPoints().addAll(x, sunsetY);
            daylengthLine.getPoints().addAll(x, daylengthY);

            // adjust chart markers for leap years
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

    private void showMouseTrapInfo(MouseEvent event) {

        int i = 0;
        int index = 0;

        for (Node bar : mouseTrapBarGroup.getChildren()) {
            if (!((Rectangle) bar).getFill().equals(Color.TRANSPARENT)) {
                index = i;
            }
            i++;
        }

        int dayOfYear = index + 1;

        GregorianCalendar gregorianCalendar = new GregorianCalendar(calendar.getTimeZone());
        gregorianCalendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        gregorianCalendar.set(Calendar.DAY_OF_YEAR, dayOfYear);

        double daylength = daylengthList.get(index);

        GregorianCalendar sunriseDate = sunriseDateList.get(index);
        String sunriseString = Sunutil.getShortTimeString(sunriseDate);

        GregorianCalendar sunsetDate = sunsetDateList.get(index);
        String sunsetString = Sunutil.getShortTimeString(sunsetDate);

        String daylengthString = Sunutil.getShortTimeLengthString(daylength * 60 * 60);

        if (daylength <= 0) {
            daylengthString = "00h00m00s";
            sunriseString = "00:00:00";
            sunsetString = "00:00:00";
        }

        if (daylength >= 24) {
            daylengthString = "24h00m00s";
            sunriseString = "00:00:00";
            sunsetString = "24:00:00";
        }

        String info = ""
                + "Date: " + gregorianCalendar.get(Calendar.DAY_OF_MONTH)
                + "." + (gregorianCalendar.get(Calendar.MONTH) + 1)
                + "." + (gregorianCalendar.get(Calendar.YEAR))
                + "\nSunrise: " + sunriseString
                + "\nSunset: " + sunsetString
                + "\nDay Length: " + daylengthString
                ;

        mouseTrapText.setText(info);

        moveMouseTrapInfo(event);
        mouseTrapInfoGroup.setVisible(true);
    }

    private void moveMouseTrapInfo(MouseEvent event) {

        int i = 0;
        int index = 0;

        for (Node bar : mouseTrapBarGroup.getChildren()) {
            if (!((Rectangle) bar).getFill().equals(Color.TRANSPARENT)) {
                index = i;
            }
            i++;
        }

        double barX = mouseTrapBarGroup.getChildren().get(index).getLocalToSceneTransform().getTx();
        double sceneScale = mouseTrapBarGroup.getChildren().get(index).getLocalToSceneTransform().getMxx();

        double mouseX = event.getX() + (barX / sceneScale);
        double mouseY = event.getY();

        double positionX = mouseX + 10;
        double positionY = mouseY + 10;

        double infoWidth = mouseTrapInfoGroup.getLayoutBounds().getWidth();
        double infoHeight = mouseTrapInfoGroup.getLayoutBounds().getHeight();

        if (positionX + infoWidth > chart.getLayoutBounds().getWidth()) {
            positionX = mouseX - (infoWidth + 10);
        }

        if (positionY + infoHeight > chart.getLayoutBounds().getHeight()) {
            positionY = mouseY - (infoHeight + 10);
        }

        mouseTrapInfoGroup.setLayoutX(positionX);
        mouseTrapInfoGroup.setLayoutY(positionY);
    }

    private void hideMouseTrapInfo() {
        mouseTrapInfoGroup.setVisible(false);
    }

    private String formatTitle() {
        return "Sun Chart"
                + " for year " + calendar.get(Calendar.YEAR)
                + " at "
                + Sunutil.formatCoordinateToString(this.longitude, "E", "W").trim()
                + " / "
                + Sunutil.formatCoordinateToString(this.latitude, "N", "S").trim()
                ;
    }


    // ***************************************
    // *** CREATOR ***

    private Group createChartGroup() {

        Group chartArea = new Group();
        Group chartGrid = new Group();
        Group chartAxisX = new Group();
        Group chartAxisY = new Group();
        Group chartTitle = new Group();
        chartFrame = new Group();

        double areaWidth = DAYS_IN_YEAR * SPACING_X;
        double areaHeight = HOURS * SPACING_Y;
        int month;
        GregorianCalendar gregorianCalendar = new GregorianCalendar();

        Font fontAxis = new Font(12);
        Font fontTitle = new Font(24);
        Font fontInfo = new Font(14);

        // MOUSE TRAP INFO
        mouseTrapText = new Text("Spice must flow.");
        mouseTrapText.setFont(fontInfo);
        mouseTrapText.setFill(Color.WHITE);
        mouseTrapText.setStroke(Color.WHITE);
        mouseTrapText.setX(fontInfo.getSize());
        mouseTrapText.setY(fontInfo.getSize() + 10);

        Rectangle infoRectangle = new Rectangle(10, 10);
        infoRectangle.setFill(Color.BLACK);
        infoRectangle.setStroke(Color.WHITE);
        infoRectangle.setStrokeWidth(2.0);
        infoRectangle.setArcWidth(20);
        infoRectangle.setArcHeight(20);
        infoRectangle.setOpacity(0.50);

        infoRectangle.widthProperty().bind(Bindings.createDoubleBinding(() ->
                        (mouseTrapText.layoutBoundsProperty().get().getWidth() + fontInfo.getSize() + 10),
                mouseTrapText.layoutBoundsProperty()
        ));

        infoRectangle.heightProperty().bind(Bindings.createDoubleBinding(() ->
                        (mouseTrapText.layoutBoundsProperty().get().getHeight() + fontInfo.getSize() + 10),
                mouseTrapText.layoutBoundsProperty()
        ));

        mouseTrapInfoGroup = new Group(infoRectangle, mouseTrapText);
        mouseTrapInfoGroup.setVisible(false);

        // AREA
        Rectangle rectangleArea = new Rectangle(areaWidth, areaHeight);
        rectangleArea.setStroke(Sunconfig.Color_Of_Void);
        rectangleArea.setFill(Color.BLACK);
        rectangleArea.setOpacity(0.8);

        mouseTrapBarGroup = new Group();

        for (int i = 0; i < DAYS_IN_YEAR; i++) {

            double x = i * SPACING_X;

            Rectangle rectangleMouseTrap = new Rectangle(SPACING_X, areaHeight);
            rectangleMouseTrap.setTranslateX(x);
            rectangleMouseTrap.setFill(Color.TRANSPARENT);
            rectangleMouseTrap.setStroke(Color.TRANSPARENT);
            rectangleMouseTrap.setOpacity(0.60);
            rectangleMouseTrap.setBlendMode(BlendMode.SCREEN);

            rectangleMouseTrap.setOnMouseEntered(event -> { rectangleMouseTrap.setFill(Color.WHITE); showMouseTrapInfo(event); });
            rectangleMouseTrap.setOnMouseMoved(event -> moveMouseTrapInfo(event));
            rectangleMouseTrap.setOnMouseExited(event -> { rectangleMouseTrap.setFill(Color.TRANSPARENT); hideMouseTrapInfo(); });

            mouseTrapBarGroup.getChildren().add(rectangleMouseTrap);
        }

        sunriseLine.setMouseTransparent(true);
        sunsetLine.setMouseTransparent(true);
        daylengthLine.setMouseTransparent(true);

        chartArea.getChildren().addAll(rectangleArea, mouseTrapBarGroup , sunriseLine, sunsetLine, daylengthLine);

        // GRID
        for (int i = 0; i <= HOURS; i++) {
            double y = i * SPACING_Y;
            Line line = new Line(-5, y, areaWidth, y);
            line.setStroke(Color.WHITE);

            chartGrid.getChildren().add(line);
        }

        chartGrid.setOpacity(0.65);
        chartGrid.setBlendMode(BlendMode.SCREEN);
        chartGrid.setMouseTransparent(true);

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
        chartAxisY.setMouseTransparent(true);

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
        chartAxisX.setMouseTransparent(true);

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
        chartTitle.setMouseTransparent(true);

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
        chartContents.setTranslateX(MARGIN_X);
        chartContents.setTranslateY(MARGIN_Y);

        // FRAME
        double contentWidth = chartContents.getLayoutBounds().getWidth() + MARGIN_X * 2;
        double contentHeight = chartContents.getLayoutBounds().getHeight() + MARGIN_Y * 2;

        Rectangle rectangleFrame = new Rectangle(contentWidth, contentHeight);
        rectangleFrame.setArcWidth(ARC_WIDTH);
        rectangleFrame.setArcHeight(ARC_HEIGHT);
        rectangleFrame.setStroke(Sunconfig.Color_Of_Void);
        rectangleFrame.setFill(Color.BLACK);
        rectangleFrame.setOpacity(0.7);

        chartFrame.getChildren().addAll(rectangleFrame);

        // CONTROL RESIZE
        Text helpText = new Text("Resize");
        controlThingyResize = Suncreator.createControlThingy(Suncreator.ControlThingyType.RESIZE, helpText);
        controlThingyResize.setPosition(
                contentWidth - controlThingyResize.getLayoutBounds().getWidth() - 5,
                contentHeight - controlThingyResize.getLayoutBounds().getHeight() - 5
        );

        // CONTROL CLOSE
        controlThingyClose = Suncreator.createControlThingy(Suncreator.ControlThingyType.CLOSE, helpText);
        controlThingyClose.setPosition(
                contentWidth - controlThingyClose.getLayoutBounds().getWidth(),
                controlThingyClose.getLayoutBounds().getHeight() - 5
        );

        // CONTROL MAXIMIZE
        controlThingyMaximize = Suncreator.createControlThingy(Suncreator.ControlThingyType.MAXIMIZE, helpText);
        controlThingyMaximize.setPosition(
                contentWidth - controlThingyMaximize.getLayoutBounds().getWidth() * 2.5,
                controlThingyMaximize.getLayoutBounds().getHeight() - 5
        );

        // CHART GROUP
        return new Group(chartFrame, chartContents, mouseTrapInfoGroup, controlThingyResize, controlThingyClose, controlThingyMaximize);
    }


    // ***************************************
    // *** PUBLIC ***

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

    public Group getChartFrame() {
        return chartFrame;
    }

    public ControlThingy getControlThingyResize() {
        return controlThingyResize;
    }

    public ControlThingy getControlThingyClose() {
        return controlThingyClose;
    }

    public ControlThingy getControlThingyMaximize() {
        return controlThingyMaximize;
    }

    public double getDefaultWidth() {
        return defaultWidth;
    }

    public double getDefaultHeight() {
        return defaultHeight;
    }
}