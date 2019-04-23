import javafx.beans.binding.Bindings;
import javafx.event.Event;
import javafx.scene.Group;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.*;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.*;

import static java.lang.Math.*;

public class Sunyear {

    private final static int DAYS_IN_YEAR = 367;
    private final static int HOURS = 24;

    private final static double SPACING_X = 2.0d;
    private final static double SPACING_Y = 20.0d;

    private final static double AREA_WIDTH = DAYS_IN_YEAR * SPACING_X;
    private final static double AREA_HEIGHT = HOURS * SPACING_Y;

    private final static double MARGIN_X = 30.0d;
    private final static double MARGIN_Y = 20.0d;
    private final static double ARC_WIDTH = 30.0d;
    private final static double ARC_HEIGHT = 30.0d;

    private final static double INFO_MARGIN = 10.0d;

    private final static Color Color_Of_Sunrise_Line    = new Color(1.00, 0.50, 0.20, 1.00);
    private final static Color Color_Of_Sunset_Line     = new Color(0.20, 0.50, 1.00, 1.00);
    private final static Color Color_Of_Daylength_Line  = new Color(0.30, 0.70, 0.30, 1.00);

    private final static String SUNRISE_GLOW    = "-fx-effect: dropshadow(three-pass-box, rgba(255, 64, 0, 1.0),  8.0, 0.60, 0, 0);";
    private final static String SUNSET_GLOW     = "-fx-effect: dropshadow(three-pass-box, rgba(0, 64, 255, 1.0),  8.0, 0.60, 0, 0);";
    private final static String DAYLENGTH_GLOW  = "-fx-effect: dropshadow(three-pass-box, rgba(0, 192, 0, 1.0),  8.0, 0.60, 0, 0);";

    enum InfoType { SUNRISE, SUNSET, DAYLENGTH };

    private double defaultWidth;
    private double defaultHeight;

    private double longitude;
    private double latitude;
    private int year;
    private long timeZoneOffset;

    private double savedMouseX;
    private double savedMouseY;

    private Suntime suntime;

    private GregorianCalendar calendar;
    private GregorianCalendar localDate;

    private ArrayList<GregorianCalendar> sunriseDateList;
    private ArrayList<GregorianCalendar> sunsetDateList;

    private ArrayList<Double> sunriseList;
    private ArrayList<Double> sunsetList;
    private ArrayList<Double> daylengthList;

    private Polyline sunrisePolyline;
    private Polyline sunsetPolyline;
    private Polyline daylengthPolyline;

    private Group sunriseLineList;
    private Group sunsetLineList;

    private Text infoTextDate;
    private Text infoTextSunrise;
    private Text infoTextSunset;
    private Text infoTextDaylength;

    private Group mouseTrapInfoGroup;
    private Rectangle mouseTrapBar;
    private Line mouseTrapSunriseLine;
    private Line mouseTrapSunsetLine;
    private Line mouseTrapDaylengthLine;
    private Rectangle localDateBar;

    private Text chartTitleText;

    private Group dayMarkerLineGroup;
    private Group dayMarkerRectangleGroup;
    private Group dayMarkerTextGroup;
    private Group monthMarkerTextGroup;

    private ControlThingy controlThingyResize;
    private ControlThingy controlThingyClose;
    private ControlThingy controlThingyMinimize;
    private ControlThingy controlThingyMaximize;

    private Group chartFrame;
    private Group chart;

    public Sunyear(double longitude, double latitude, GregorianCalendar date, long timeZoneOffset) {

        this.longitude = longitude;
        this.latitude = latitude;
        this.year = date.get(Calendar.YEAR);
        this.timeZoneOffset = timeZoneOffset;

        sunriseDateList = new ArrayList<>();
        sunsetDateList = new ArrayList<>();

        sunriseList = new ArrayList<>();
        sunsetList = new ArrayList<>();
        daylengthList = new ArrayList<>();

        calendar = new GregorianCalendar();
        calendar.set(Calendar.YEAR, this.year);

        localDate = new GregorianCalendar(date.getTimeZone());
        localDate.setTimeInMillis(date.getTimeInMillis());

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

            GregorianCalendar dayStart = new GregorianCalendar(calendar.getTimeZone());
            dayStart.set(
                    gregorianCalendar.get(Calendar.YEAR),
                    gregorianCalendar.get(Calendar.MONTH),
                    gregorianCalendar.get(Calendar.DAY_OF_MONTH),
                    0, 0, 0
            );

            GregorianCalendar dayEnd = new GregorianCalendar(calendar.getTimeZone());
            dayEnd.set(
                    gregorianCalendar.get(Calendar.YEAR),
                    gregorianCalendar.get(Calendar.MONTH),
                    gregorianCalendar.get(Calendar.DAY_OF_MONTH),
                    23, 59, 59
            );

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

            // Constrain values to fit in 24 hour window
            double sunriseTime =
                    (
                            abs(sunriseDateCorrected.get(Calendar.DAY_OF_YEAR) - dayStart.get(Calendar.DAY_OF_YEAR)) * 24
                                    + sunriseDateCorrected.get(Calendar.HOUR_OF_DAY)
                                    + sunriseDateCorrected.get(Calendar.MINUTE) / 60d
                                    + sunriseDateCorrected.get(Calendar.SECOND) / (60d * 60d)
                    ) % 24;

            double sunsetTime =
                    (
                            abs(sunsetDateCorrected.get(Calendar.DAY_OF_YEAR) - dayStart.get(Calendar.DAY_OF_YEAR)) * 24
                                    + sunsetDateCorrected.get(Calendar.HOUR_OF_DAY)
                                    + sunsetDateCorrected.get(Calendar.MINUTE) / 60d
                                    + sunsetDateCorrected.get(Calendar.SECOND) / (60d * 60d)
                    ) % 24;


            if (daylength <= 0) {
                daylength = 0;
                sunriseTime = 0;
                sunsetTime = 0;
                sunriseDateCorrected = dayStart;
                sunsetDateCorrected = dayStart;
            }

            if (daylength >= 24) {
                daylength = 24;
                sunriseTime = 0;
                sunsetTime = 24;
                sunriseDateCorrected = dayStart;
                sunsetDateCorrected = dayStart;
            }


            // Store data for textual display
            sunriseDateList.add(sunriseDateCorrected);
            sunsetDateList.add(sunsetDateCorrected);

            // Store data for lines
            sunriseList.add(sunriseTime);
            sunsetList.add(sunsetTime);
            daylengthList.add(daylength);
        }
    }

    private void refreshLocalDateBar() {
        if (localDate.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)) {
            int localDateDayOfYear = localDate.get(Calendar.DAY_OF_YEAR) - 1;
            localDateBar.setTranslateX(localDateDayOfYear * SPACING_X);
            localDateBar.setVisible(true);
        } else {
            localDateBar.setVisible(false);
        }
    }

    private void refreshLines() {

        sunrisePolyline.getPoints().clear();
        sunsetPolyline.getPoints().clear();
        daylengthPolyline.getPoints().clear();

        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.set(Calendar.YEAR, year);

        refreshLocalDateBar();

        int month = 0;

        for (int i = 0; i < DAYS_IN_YEAR; i++) {

            double sunrise = sunriseList.get(i);
            double sunset = sunsetList.get(i);
            double daylength = daylengthList.get(i);

            double x = i * SPACING_X;

            double sunriseY = AREA_HEIGHT - sunrise * SPACING_Y;
            double sunsetY = AREA_HEIGHT - sunset * SPACING_Y;
            double daylengthY = AREA_HEIGHT - daylength * SPACING_Y;

            sunrisePolyline.getPoints().addAll(x, sunriseY);
            sunsetPolyline.getPoints().addAll(x, sunsetY);
            daylengthPolyline.getPoints().addAll(x, daylengthY);

/*
            Line sunriseLine = (Line) sunriseLineList.getChildren().get(i);
            Line sunsetLine = (Line) sunsetLineList.getChildren().get(i);

            sunriseLine.setTranslateX(x);
            sunriseLine.setTranslateY(sunriseY);

            sunsetLine.setTranslateX(x);
            sunsetLine.setTranslateY(sunsetY);
*/

            // adjust chart markers for leap years
            int dayOfYear = i + 1;
            gregorianCalendar.set(Calendar.DAY_OF_YEAR, dayOfYear);

            int dayOfMonth = gregorianCalendar.get(Calendar.DAY_OF_MONTH);

            if (dayOfMonth == 1) {

                GregorianCalendar endMonthCalendar  = new GregorianCalendar(gregorianCalendar.getTimeZone());
                endMonthCalendar.set(Calendar.YEAR, gregorianCalendar.get(Calendar.YEAR));
                endMonthCalendar.set(Calendar.MONTH, month + 1);
                endMonthCalendar.set(Calendar.DAY_OF_MONTH, 1);
                endMonthCalendar.setTimeInMillis(endMonthCalendar.getTimeInMillis() - 36 * 60 * 60 * 1000);

                double endX = endMonthCalendar.get(Calendar.DAY_OF_YEAR) * SPACING_X;
                double width = endX - x;

                if (month < dayMarkerLineGroup.getChildren().size()) {
                    dayMarkerLineGroup.getChildren().get(month).setTranslateX(x);
                }

                if (month < dayMarkerRectangleGroup.getChildren().size()) {
                    Rectangle rectangle = (Rectangle) dayMarkerRectangleGroup.getChildren().get(month);
                    rectangle.setTranslateX(x);
                    rectangle.setWidth(width + SPACING_X);
                }

                if (month < dayMarkerTextGroup.getChildren().size()) {
                    dayMarkerTextGroup.getChildren().get(month).setTranslateX(x);
                }

                if (month < monthMarkerTextGroup.getChildren().size()) {
                    monthMarkerTextGroup.getChildren().get(month).setTranslateX(x);
                }

                month++;
            }
        }

    }

    private void showMouseTrapInfo(MouseEvent event) {
        updateMouseTrapInfo(event);
        mouseTrapInfoGroup.setVisible(true);
        mouseTrapBar.setVisible(true);
        mouseTrapSunriseLine.setVisible(true);
        mouseTrapSunsetLine.setVisible(true);
        mouseTrapDaylengthLine.setVisible(true);
    }

    private void changeInfoText(int index) {

        int dayOfYear = index + 1;

        GregorianCalendar gregorianCalendar = new GregorianCalendar(calendar.getTimeZone());
        gregorianCalendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        gregorianCalendar.set(Calendar.DAY_OF_YEAR, dayOfYear);

        String dateString       = "Date       : " + Sunutil.getShortDateString(gregorianCalendar);

        GregorianCalendar sunriseDate = sunriseDateList.get(index);
        String sunriseString    = "Sunrise    : " + Sunutil.getShortTimeString(sunriseDate);

        GregorianCalendar sunsetDate = sunsetDateList.get(index);
        String sunsetString     = "Sunset     : " + Sunutil.getShortTimeString(sunsetDate);

        double daylength = daylengthList.get(index);
        String daylengthString  = "Day length : " + Sunutil.getShortTimeLengthString(daylength * 60 * 60);

        infoTextDate.setText(dateString);
        infoTextSunrise.setText(sunriseString);
        infoTextSunset.setText(sunsetString);
        infoTextDaylength.setText(daylengthString);
    }

    public void updateMouseTrapInfo(Event event) {

        double mouseX, mouseY;

        if (event instanceof MouseEvent) {

            mouseX = ((MouseEvent) event).getX();
            mouseY = ((MouseEvent) event).getY();

            savedMouseX = mouseX;
            savedMouseY = mouseY;

        } else if (event instanceof ScrollEvent) {

            double offsetFactor = (((ScrollEvent) event).getDeltaY() < 0) ? -1 : 1;

            savedMouseX += offsetFactor * SPACING_X;

            mouseX = savedMouseX;
            mouseY = savedMouseY;

        } else if (event instanceof KeyEvent) {

            int offsetFactor = 0;

            KeyCode keyCode = ((KeyEvent) event).getCode();

            if (keyCode.equals(KeyCode.LEFT)) { offsetFactor = -1; }
            if (keyCode.equals(KeyCode.RIGHT)) { offsetFactor = 1; }

            savedMouseX += offsetFactor * SPACING_X;

            mouseX = savedMouseX;
            mouseY = savedMouseY;

        } else {
            return;
        }

        int index = (int) floor(mouseX / SPACING_X);

        if (index < 0 || index > daylengthList.size() - 1) { return; }

        changeInfoText(index);

        double infoWidth = mouseTrapInfoGroup.getLayoutBounds().getWidth();
        double infoHeight = mouseTrapInfoGroup.getLayoutBounds().getHeight();

        double offsetX = infoWidth + 10;
        double offsetY = infoHeight + 10;

        double positionX = mouseX + 10;
        double positionY = mouseY + 10;

        if (positionX + infoWidth > AREA_WIDTH) {
            positionX = mouseX - offsetX;
        }

        if (positionY + infoHeight > AREA_HEIGHT) {
            positionY = mouseY - offsetY;
        }

        double sunrise = sunriseList.get(index);
        double sunset = sunsetList.get(index);
        double daylength = daylengthList.get(index);

        double endX = index * SPACING_X;
        double sunriseY = AREA_HEIGHT - (sunrise * SPACING_Y);
        double sunsetY = AREA_HEIGHT - (sunset * SPACING_Y);
        double daylengthY = AREA_HEIGHT - (daylength * SPACING_Y);

        mouseTrapInfoGroup.setTranslateX(positionX);
        mouseTrapInfoGroup.setTranslateY(positionY);

        mouseTrapBar.setTranslateX(endX);

        mouseTrapSunriseLine.setEndX(endX);
        mouseTrapSunriseLine.setTranslateY(sunriseY);

        mouseTrapSunsetLine.setEndX(endX);
        mouseTrapSunsetLine.setTranslateY(sunsetY);

        mouseTrapDaylengthLine.setEndX(endX);
        mouseTrapDaylengthLine.setTranslateY(daylengthY);
    }

    private void hideMouseTrapInfo() {
        mouseTrapInfoGroup.setVisible(false);
        mouseTrapBar.setVisible(false);
        mouseTrapSunriseLine.setVisible(false);
        mouseTrapSunsetLine.setVisible(false);
        mouseTrapDaylengthLine.setVisible(false);
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

        sunriseLineList = new Group();
        sunsetLineList = new Group();

        chartFrame = new Group();
        dayMarkerLineGroup = new Group();
        dayMarkerRectangleGroup = new Group();
        dayMarkerTextGroup = new Group();
        monthMarkerTextGroup = new Group();


        int month;
        GregorianCalendar gregorianCalendar = new GregorianCalendar();

        Font fontAxis = new Font(12);
        Font fontTitle = new Font(24);
        Font fontInfo = Sunconfig.FONT_CHART_INFO;

        // MOUSE TRAP INFO
        infoTextDate = new Text("Spice must flow.");
        infoTextDate.setFont(fontInfo);
        infoTextDate.setFill(Color.WHITE);
        infoTextDate.setStroke(Color.WHITE);
        infoTextDate.setX(INFO_MARGIN);
        infoTextDate.setY(fontInfo.getSize() * 0.5 + INFO_MARGIN);

        infoTextSunrise = new Text("Spice must flow.");
        infoTextSunrise.setFont(fontInfo);
        infoTextSunrise.setFill(Color.WHITE);
        infoTextSunrise.setStroke(Color.WHITE);
        infoTextSunrise.setStyle(SUNRISE_GLOW);
        infoTextSunrise.setX(INFO_MARGIN);
        infoTextSunrise.setY(fontInfo.getSize() * 4 + INFO_MARGIN);
        infoTextSunrise.setBlendMode(BlendMode.SCREEN);
        infoTextSunrise.setOpacity(0.8);

        infoTextSunset = new Text("Spice must flow.");
        infoTextSunset.setFont(fontInfo);
        infoTextSunset.setFill(Color.WHITE);
        infoTextSunset.setStroke(Color.WHITE);
        infoTextSunset.setStyle(SUNSET_GLOW);
        infoTextSunset.setX(INFO_MARGIN);
        infoTextSunset.setY(fontInfo.getSize() * 2 + INFO_MARGIN);
        infoTextSunset.setBlendMode(BlendMode.SCREEN);
        infoTextSunset.setOpacity(0.8);

        infoTextDaylength = new Text("Spice must flow.");
        infoTextDaylength.setFont(fontInfo);
        infoTextDaylength.setFill(Color.WHITE);
        infoTextDaylength.setStroke(Color.WHITE);
        infoTextDaylength.setStyle(DAYLENGTH_GLOW);
        infoTextDaylength.setX(INFO_MARGIN);
        infoTextDaylength.setY(fontInfo.getSize() * 3 + INFO_MARGIN);
        infoTextDaylength.setBlendMode(BlendMode.SCREEN);
        infoTextDaylength.setOpacity(0.8);

        Group infoTextGroup = new Group(infoTextDate, infoTextSunrise, infoTextSunset, infoTextDaylength);

        Rectangle infoRectangle = new Rectangle(10, 10);
        infoRectangle.setFill(Color.BLACK);
        infoRectangle.setStroke(Color.WHITE);
        infoRectangle.setStrokeWidth(2.0);
        infoRectangle.setArcWidth(20);
        infoRectangle.setArcHeight(20);
        infoRectangle.setOpacity(0.50);

        infoRectangle.widthProperty().bind(Bindings.createDoubleBinding(() ->
                        (infoTextGroup.layoutBoundsProperty().get().getWidth()/* + fontInfo.getSize()*/ + INFO_MARGIN / 2),
                infoTextGroup.layoutBoundsProperty()
        ));

        infoRectangle.heightProperty().bind(Bindings.createDoubleBinding(() ->
                        (infoTextGroup.layoutBoundsProperty().get().getHeight()/* + fontInfo.getSize()*/ + INFO_MARGIN / 2),
                infoTextGroup.layoutBoundsProperty()
        ));

        mouseTrapInfoGroup = new Group(infoRectangle, infoTextGroup);
        mouseTrapInfoGroup.setVisible(false);

        // AREA
        Rectangle areaArea = new Rectangle(AREA_WIDTH, AREA_HEIGHT);
        areaArea.setStroke(Color.TRANSPARENT);
        areaArea.setFill(Color.TRANSPARENT);

        sunrisePolyline = new Polyline();
        sunrisePolyline.setStroke(Color.WHITE);
        sunrisePolyline.setStrokeWidth(2.0);
        sunrisePolyline.setStyle(SUNRISE_GLOW);
        sunrisePolyline.setBlendMode(BlendMode.SCREEN);
        sunrisePolyline.setMouseTransparent(true);

/*
        for (int i = 0; i < DAYS_IN_YEAR; i++) {
            Line sunriseLine = new Line(0, 0, SPACING_X, 0);
            sunriseLine.setStroke(Color.WHITE);
            sunriseLine.setStrokeWidth(2.0);
            sunriseLine.setStyle(SUNRISE_GLOW);
            sunriseLine.setBlendMode(BlendMode.SCREEN);
            sunriseLine.setMouseTransparent(true);
            sunriseLineList.getChildren().add(sunriseLine);

            Line sunsetLine = new Line(0, 0, SPACING_X, 0);
            sunsetLine.setStroke(Color.WHITE);
            sunsetLine.setStrokeWidth(2.0);
            sunsetLine.setStyle(SUNSET_GLOW);
            sunsetLine.setBlendMode(BlendMode.SCREEN);
            sunsetLine.setMouseTransparent(true);
            sunsetLineList.getChildren().add(sunsetLine);
        }
*/

        sunsetPolyline = new Polyline();
        sunsetPolyline.setStroke(Color.WHITE);
        sunsetPolyline.setStrokeWidth(2.0);
        sunsetPolyline.setStyle(SUNSET_GLOW);
        sunsetPolyline.setBlendMode(BlendMode.SCREEN);
        sunsetPolyline.setMouseTransparent(true);

        daylengthPolyline = new Polyline();
        daylengthPolyline.setStroke(Color.WHITE);
        daylengthPolyline.setStrokeWidth(2.0);
        daylengthPolyline.setStyle(DAYLENGTH_GLOW);
        daylengthPolyline.setBlendMode(BlendMode.SCREEN);
        daylengthPolyline.setMouseTransparent(true);

        localDateBar = new Rectangle(SPACING_X, AREA_HEIGHT);
        localDateBar.setTranslateX(0);
        localDateBar.setFill(Color.RED);
        localDateBar.setStroke(Color.TRANSPARENT);
        localDateBar.setOpacity(0.80);
        localDateBar.setVisible(true);
        localDateBar.setMouseTransparent(true);

        mouseTrapBar = new Rectangle(SPACING_X, AREA_HEIGHT);
        mouseTrapBar.setTranslateX(0);
        mouseTrapBar.setFill(Color.WHITE);
        mouseTrapBar.setStroke(Color.TRANSPARENT);
        mouseTrapBar.setOpacity(0.80);
        mouseTrapBar.setBlendMode(BlendMode.DIFFERENCE);
        mouseTrapBar.setVisible(false);
        mouseTrapBar.setMouseTransparent(true);

        mouseTrapSunriseLine = new Line(-5, 0, AREA_WIDTH, 0);
        mouseTrapSunriseLine.setStroke(Color_Of_Sunrise_Line);
        mouseTrapSunriseLine.setStrokeWidth(1.5);
        mouseTrapSunriseLine.setOpacity(1);
        mouseTrapSunriseLine.setVisible(false);
        mouseTrapSunriseLine.setMouseTransparent(true);

        mouseTrapSunsetLine = new Line(-5, 0, AREA_WIDTH, 0);
        mouseTrapSunsetLine.setStroke(Color_Of_Sunset_Line);
        mouseTrapSunsetLine.setStrokeWidth(1.5);
        mouseTrapSunsetLine.setOpacity(1);
        mouseTrapSunsetLine.setVisible(false);
        mouseTrapSunsetLine.setMouseTransparent(true);

        mouseTrapDaylengthLine = new Line(-5, 0, AREA_WIDTH, 0);
        mouseTrapDaylengthLine.setStroke(Color_Of_Daylength_Line);
        mouseTrapDaylengthLine.setStrokeWidth(1.5);
        mouseTrapDaylengthLine.setOpacity(1);
        mouseTrapDaylengthLine.setVisible(false);
        mouseTrapDaylengthLine.setMouseTransparent(true);

        chartArea.getChildren().addAll(
                areaArea,
                localDateBar,
                sunrisePolyline,
                sunsetPolyline,
//                sunriseLineList,
//                sunsetLineList,
                daylengthPolyline,
                mouseTrapSunriseLine,
                mouseTrapSunsetLine,
                mouseTrapDaylengthLine,
                mouseTrapBar,
                mouseTrapInfoGroup
        );

        // GRID
        for (int i = 0; i <= HOURS; i++) {
            double y = i * SPACING_Y;
            Line line = new Line(-5, y, AREA_WIDTH, y);
            line.setStroke(Color.WHITE);

            chartGrid.getChildren().add(line);
        }

        gregorianCalendar.set(Calendar.YEAR, year);
        month = 0;

        for (int i = 0; i < DAYS_IN_YEAR; i++) {

            int dayOfYear = i + 1;
            gregorianCalendar.set(Calendar.DAY_OF_YEAR, dayOfYear);

            int dayOfMonth = gregorianCalendar.get(Calendar.DAY_OF_MONTH);

            if (dayOfMonth == 1) {

                if (month < 13) {
                    Line line = new Line(0, 0, 0, AREA_HEIGHT + fontAxis.getSize() + 5);
                    line.setStroke(Color.WHITE);

                    dayMarkerLineGroup.getChildren().add(line);
                }

                if (month < 12) {

                    Rectangle rectangle = new Rectangle(30 * SPACING_X, AREA_HEIGHT);
                    rectangle.setStroke(Color.TRANSPARENT);
                    rectangle.setFill((month % 2 == 0) ? Color.ORANGE : Color.SKYBLUE);
                    rectangle.setOpacity(0.5);

                    dayMarkerRectangleGroup.getChildren().add(rectangle);
                }

                month++;
            }
        }

        chartGrid.getChildren().addAll(dayMarkerRectangleGroup, dayMarkerLineGroup);
        chartGrid.setOpacity(0.35);
        chartGrid.setMouseTransparent(true);

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
            int calendarMonth = gregorianCalendar.get(Calendar.MONTH) + 1;

            if (month > 12) { continue; }

            if (dayOfMonth == 1) {

                String dayMarkerString = dayOfMonth + "." + calendarMonth + ".";

                Text textDate = new Text(0, fontAxis.getSize() + 5, dayMarkerString);
                textDate.setFont(fontAxis);
                textDate.setFill(Color.WHITE);

                textDate.setX(-textDate.getLayoutBounds().getWidth() / 2);

                dayMarkerTextGroup.getChildren().add(textDate);

                month++;
            }

            if (dayOfMonth == 15) {

                String monthString;

                switch (calendarMonth) {
                    case 1: monthString = "January"; break;
                    case 2: monthString = "February"; break;
                    case 3: monthString = "March"; break;
                    case 4: monthString = "April"; break;
                    case 5: monthString = "May"; break;
                    case 6: monthString = "June"; break;
                    case 7: monthString = "July"; break;
                    case 8: monthString = "August"; break;
                    case 9: monthString = "September"; break;
                    case 10: monthString = "October"; break;
                    case 11: monthString = "November"; break;
                    case 12: monthString = "December"; break;
                    default: monthString = "Bakerary";
                }

                Text textMonth = new Text(0, 0, monthString);
                textMonth.setFont(fontAxis);
                textMonth.setFill(Color.WHITE);

                textMonth.setX(((30 * SPACING_X) - textMonth.getLayoutBounds().getWidth()) / 2);

                monthMarkerTextGroup.getChildren().add(textMonth);
            }
        }

        chartAxisX.getChildren().addAll(dayMarkerTextGroup, monthMarkerTextGroup);
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

        // MOUSE TRAP
        Rectangle areaBackground = new Rectangle(AREA_WIDTH, AREA_HEIGHT);
        areaBackground.setStroke(Color.TRANSPARENT);
        areaBackground.setFill(Color.BLACK);
        areaBackground.setOpacity(0.8);
        areaBackground.setMouseTransparent(true);

        Rectangle areaForeground = new Rectangle(AREA_WIDTH, AREA_HEIGHT);
        areaForeground.setStroke(Color.TRANSPARENT);
        areaForeground.setFill(Color.TRANSPARENT);

        areaForeground.setOnMouseEntered(event -> { showMouseTrapInfo(event); });
        areaForeground.setOnMouseExited(event -> { hideMouseTrapInfo(); });
        areaForeground.setOnMouseMoved(event -> updateMouseTrapInfo(event));
        areaForeground.setOnScroll(event -> updateMouseTrapInfo(event));

        // ALL GROUPS sans FRAME
        double chartTitleHeight = chartTitle.getLayoutBounds().getHeight() / 2;
        double chartAxisYWidth = chartAxisY.getLayoutBounds().getWidth();
        double chartAreaHeight = chartArea.getLayoutBounds().getHeight();

        chartTitle.setTranslateX(chartAxisYWidth + AREA_WIDTH / 2 - chartTitle.getLayoutBounds().getWidth() / 2);
        chartTitle.setTranslateY(MARGIN_Y * 0.75);

        chartArea.setTranslateX(chartAxisYWidth);
        chartArea.setTranslateY(chartTitleHeight);

        areaBackground.setTranslateX(chartAxisYWidth);
        areaBackground.setTranslateY(chartTitleHeight);

        areaForeground.setTranslateX(chartAxisYWidth);
        areaForeground.setTranslateY(chartTitleHeight);

        chartGrid.setTranslateX(chartAxisYWidth);
        chartGrid.setTranslateY(chartTitleHeight);

        chartAxisY.setTranslateX(-10);
        chartAxisY.setTranslateY(chartTitleHeight + fontAxis.getSize() * 0.35);

        chartAxisX.setTranslateX(chartAxisYWidth);
        chartAxisX.setTranslateY(chartAreaHeight + chartTitleHeight + fontAxis.getSize() * 1.5);

        Group chartContents = new Group(areaBackground, chartTitle, chartAxisX, chartAxisY, chartGrid, chartArea, areaForeground);
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
        rectangleFrame.setOpacity(0.8);

        chartFrame.getChildren().addAll(rectangleFrame);

        // CONTROL RESIZE
        Text helpText = new Text("Resize");
        controlThingyResize = Suncreator.createControlThingy(Suncreator.ControlThingyType.RESIZE, helpText);
        controlThingyResize.setPosition(
                contentWidth - controlThingyResize.getLayoutBounds().getWidth() - 5,
                contentHeight - controlThingyResize.getLayoutBounds().getHeight() - 5
        );

        // CONTROL MAXIMIZE
        controlThingyMaximize = Suncreator.createControlThingy(Suncreator.ControlThingyType.MAXIMIZE, helpText);
        controlThingyMaximize.setPosition(
                contentWidth - controlThingyMaximize.getLayoutBounds().getWidth() * 4.5,
                controlThingyMaximize.getLayoutBounds().getHeight() - 5
        );

        // CONTROL MINIMIZE
        controlThingyMinimize = Suncreator.createControlThingy(Suncreator.ControlThingyType.MINIMIZE, helpText);
        controlThingyMinimize.setPosition(
                contentWidth - controlThingyMinimize.getLayoutBounds().getWidth() * 3,
                controlThingyMinimize.getLayoutBounds().getHeight() - 5
        );

        // CONTROL CLOSE
        controlThingyClose = Suncreator.createControlThingy(Suncreator.ControlThingyType.CLOSE, helpText);
        controlThingyClose.setPosition(
                contentWidth - controlThingyClose.getLayoutBounds().getWidth() * 1.5,
                controlThingyClose.getLayoutBounds().getHeight() - 5
        );

        // CHART GROUP
        return new Group(chartFrame, chartContents, controlThingyResize, controlThingyMaximize, controlThingyMinimize, controlThingyClose);
    }


    // ***************************************
    // *** PUBLIC ***

    public void setSpaceTime(double longitude, double latitude, GregorianCalendar date, long timeZoneOffset) {

        this.longitude = longitude;
        this.latitude = latitude;
        this.year = date.get(Calendar.YEAR);
        this.timeZoneOffset = timeZoneOffset;

        calendar.set(Calendar.YEAR, year);

        localDate.setTimeZone(date.getTimeZone());
        localDate.setTimeInMillis(date.getTimeInMillis());

        chartTitleText.setText(formatTitle());

        recalculateDataPoints();
        refreshLines();
    }

    public void setTimeZone(TimeZone timeZone) {
        calendar.setTimeZone(timeZone);
        calendar.get(Calendar.HOUR_OF_DAY);
    }

    public void setLocalDate(GregorianCalendar localDate) {
        this.localDate.setTimeZone(localDate.getTimeZone());
        this.localDate.setTimeInMillis(localDate.getTimeInMillis());
        refreshLocalDateBar();
    }

    public GregorianCalendar getLocalDate() {
        return localDate;
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

    public ControlThingy getControlThingyMinimize() {
        return controlThingyMinimize;
    }

    public double getDefaultWidth() {
        return defaultWidth;
    }

    public double getDefaultHeight() {
        return defaultHeight;
    }

    public TimeZone getTimeZone() {
        return calendar.getTimeZone();
    }
}