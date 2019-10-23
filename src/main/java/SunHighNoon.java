import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;

import java.text.DecimalFormat;
import java.util.GregorianCalendar;

import static java.lang.Math.*;

public class SunHighNoon extends Group {

    private static final Color MARKER_COLOR = new Color(1.00, 1.00, 1.00, 1.00);
    private static final Color ARC_COLOR = new Color(1.00, 1.00, 1.00, 1.00);
    private static final Color TIME_COLOR = new Color(1.00, 1.00, 1.00, 1.00);
    private static final Color ANGLE_COLOR = new Color(1.00, 1.00, 1.00, 1.00);
    private static final Color DAYLENGTH_COLOR = new Color(1.00, 1.00, 1.00, 1.00);
    private static final Color SUNDOT_COLOR = new Color(1.00, 1.00, 1.00, 1.00);
    private static final Color HORIZONLINE_COLOR = new Color(1.00, 1.00, 1.00, 1.00);
    private static final Color SUNLINE_COLOR = new Color(1.00, 1.00, 1.00, 1.00);

    private static final DecimalFormat angleFormat = new DecimalFormat("#0.0");

    private static final long DAY_SECONDS = 24 * 60 * 60;

    private Arc arc;
    private Circle sunDot;
    private Line horizonLine;
    private Line sunLine;
    private Group markerGroup;

    private DotMatrix matrixTime;
    private DotMatrix matrixAngle;
    private DotMatrix matrixDayLength;

    private double centerX, centerY, radius, angle, marginX, marginY;

    private boolean detailsVisible;

    public SunHighNoon(double radius, Rotate rotate) {
        this(0, 0, radius, 1, rotate);
    }

    public SunHighNoon(double centerX, double centerY, double radius, double matrixScale, Rotate rotate) {

        super();

        detailsVisible = false;

        marginX = radius * 0.05;
        marginY = radius * 0.04;

        double matrixDayLengthScale = matrixScale * 1.00;
        double matrixTimeScale      = matrixScale * 0.90;
        double matrixAngleScale     = matrixScale * 0.70;

        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = radius;

        arc = new Arc(centerX, centerY, radius, radius, 0, 180);
        arc.setType(ArcType.OPEN);
        arc.setFill(Color.TRANSPARENT);
        arc.setStroke(ARC_COLOR);

        horizonLine = new Line(centerX - radius, centerY, centerX + radius, centerY);
        horizonLine.setStroke(HORIZONLINE_COLOR);

        sunLine = new Line(centerX, centerY, centerX, centerY - radius * 1.5);
        sunLine.setStroke(SUNLINE_COLOR);

        sunDot = new Circle(centerX, centerY, radius * 0.025);
        sunDot.setFill(SUNDOT_COLOR);
        sunDot.setStroke(Color.TRANSPARENT);

        matrixAngle = new DotMatrix("45.0^N", ANGLE_COLOR);
        matrixAngle.setScaleX(matrixAngleScale);
        matrixAngle.setScaleY(matrixAngleScale);
        matrixAngle.setTranslateX(centerX - matrixAngle.getLayoutBounds().getWidth() / 2);
//        matrixAngle.setTranslateY(matrixDayLength.getTranslateY() + matrixAngle.getLayoutBounds().getHeight() + marginY);
        matrixAngle.setTranslateY(centerY - radius * 1.06 + matrixAngle.getLayoutBounds().getHeight());

        matrixDayLength = new DotMatrix("00h00m00s", DAYLENGTH_COLOR);
        matrixDayLength.setScaleX(matrixDayLengthScale);
        matrixDayLength.setScaleY(matrixDayLengthScale);
        matrixDayLength.setTranslateX(centerX - matrixDayLength.getLayoutBounds().getWidth() / 2);
        matrixDayLength.setTranslateY(matrixAngle.getTranslateY() + matrixDayLength.getLayoutBounds().getHeight() + marginY * matrixAngleScale);
//        matrixDayLength.setTranslateY(centerY - radius * 0.95 + matrixDayLength.getLayoutBounds().getHeight());

        matrixTime = new DotMatrix("12:00:00", TIME_COLOR);
        matrixTime.setScaleX(matrixTimeScale);
        matrixTime.setScaleY(matrixTimeScale);
        matrixTime.setTranslateX(centerX - matrixTime.getLayoutBounds().getWidth() / 2);
        matrixTime.setTranslateY(matrixDayLength.getTranslateY() + matrixTime.getLayoutBounds().getHeight() + marginY * matrixDayLengthScale);
//        matrixTime.setTranslateY(centerY - radius * 1.02 + matrixTime.getLayoutBounds().getHeight());

        markerGroup = new Group();

        for (int i = 0; i <= 90; i += 5) {

            double dX = ((i % 10) == 0) ? (marginX * 0.75) : (marginX * 0.40);
            double dY = (i / 90d) * radius;

            if (i == 90) { dX = marginX * 1.5; }

            Line line = new Line(centerX - dX, centerY - dY, centerX + dX, centerY - dY);
            line.setStroke(MARKER_COLOR);

            markerGroup.getChildren().add(line);
        }

        matrixTime.setVisible(detailsVisible);
        matrixAngle.setVisible(detailsVisible);

        super.getChildren().addAll(/*sunLine, markerGroup, horizonLine, sunDot, arc,*/ matrixDayLength, matrixTime, matrixAngle);
//        super.getTransforms().add(rotate);
        super.setMouseTransparent(true);
    }

    public void setParameters(GregorianCalendar noonTime, double angle, long dayLength) {

        this.angle = (abs(angle) >= 90) ? 90 : abs(angle);

        String poleSuffix = (angle > 0) ? "S" : "N";

        String angleString = angleFormat.format(90 - this.angle).replace(",", ".");
        angleString = "00" + angleString + "^" + poleSuffix;
        angleString = angleString.substring(angleString.length() - 6);

        long clampedDayLength = (dayLength >= DAY_SECONDS) ? DAY_SECONDS : dayLength;

        matrixAngle.setString(angleString);
        matrixTime.setString(Sunutil.getShortTimeString(noonTime));
        matrixDayLength.setString(Sunutil.getShortTimeLengthString(clampedDayLength));

/*
        double dY = ((90 - this.angle) / 90d) * radius;

        sunDot.setCenterY(centerY - dY);
        matrixAngle.setTranslateY(centerY - dY * 0.75 - radius * 0.25 - matrixAngle.getLayoutBounds().getHeight() / 2);

        Rotate rotate = (Rotate) super.getTransforms().get(0);
        if (rotate.getAngle() > 90 && rotate.getAngle() < 270) {
            matrixTime.setRotate(180);
            matrixAngle.setRotate(180);
            matrixDayLength.setRotate(180);
        } else {
            matrixTime.setRotate(0);
            matrixAngle.setRotate(0);
            matrixDayLength.setRotate(0);
        }


        double arcLength = (clampedDayLength / (double) DAY_SECONDS) * 360;
        double arcStart = 90 - arcLength / 2;

        arc.setStartAngle(arcStart);
        arc.setLength(arcLength);

        arc.setRadiusY(dY);

        double hY = (dY * -cos(toRadians(arcLength / 2)));

        arc.setCenterY(Sunconfig.CENTER_Y - hY);
        arc.setRadiusY(dY - hY);

        horizonLine.setTranslateY(dY * (-cos(toRadians(arcLength / 2))));
*/

    }


    public void setArcLook(Color color, double width, String style, BlendMode blendMode) {
        arc.setStroke(color);
        arc.setStrokeWidth(width);
        arc.setStyle(style);
        arc.setBlendMode(blendMode);
    }

    public void setHorizonLook(Color color, double width, String style) {
        horizonLine.setStroke(color);
        horizonLine.setStrokeWidth(width);
        horizonLine.setStyle(style);
    }

    public void setSunLineLook(Color color, double width, String style, BlendMode blendMode) {
        sunLine.setStroke(color);
        sunLine.setStrokeWidth(width);
        sunLine.setStyle(style);
        sunLine.setBlendMode(blendMode);
    }

    public void setSunDotLook(Color color, String style, BlendMode blendMode) {
        sunDot.setFill(color);
        sunDot.setStyle(style);
        sunDot.setBlendMode(blendMode);
    }

    public void setMarkerLook(Color color, double width, String style) {

        for (Node node : markerGroup.getChildren()) {
            Line line = (Line) node;
            line.setStroke(color);
            line.setStrokeWidth(width);
        }

        markerGroup.setStyle(style);
    }

    public void setMatrixTimeLook(Color color, String style) {
        matrixTime.setFill(color);
        matrixTime.setStyle(style);
    }

    public void setMatrixAngleLook(Color color, String style) {
        matrixAngle.setFill(color);
        matrixAngle.setStyle(style);
    }

    public void setMatrixDayLengthLook(Color color, String style) {
        matrixDayLength.setFill(color);
        matrixDayLength.setStyle(style);
    }

    public void toggleDetails() {
        detailsVisible = !detailsVisible;
        matrixTime.setVisible(detailsVisible);
        matrixAngle.setVisible(detailsVisible);
    }

}
