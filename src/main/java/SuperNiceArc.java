import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;

public class SuperNiceArc extends Group {

    public final static double START_REDUCTION_ANGLE = 15;
    public final static double END_REDUCTION_ANGLE = 5;

    public final static double START_CURVE_HEIGHT = 0.2;
    public final static double END_CURVE_HEIGHT = 0.1;

    public final static double CURVE_SHARPNESS = 0.5;

    private double centerX;
    private double centerY;
    private double radiusBig;
    private double radiusSmol;
    private double strokeWidth;
    private double endLineExtension;

    private double totalReductionAngle;
    private double startCurveRatio;
    private double endCurveRatio;

    private DoubleProperty startAngle;
    private DoubleProperty endAngle;

    private Line startLine;
    private Line endLine;
    private Arc mainArc;
    private Polygon startPoly;
    private Polygon endPoly;
    private CubicCurve startCurve;
    private CubicCurve endCurve;

    private Rotate startLineRotate;
    private Rotate endLineRotate;

    public SuperNiceArc(PleaseBuild builder) {

        super();

        this.centerX = builder.centerX;
        this.centerY = builder.centerY;
        this.radiusBig = builder.radiusBig;
        this.radiusSmol = builder.radiusSmol;
        this.strokeWidth = builder.strokeWidth;
        this.endLineExtension = builder.endLineExtension;

        double startPolyHeight = radiusSmol * (1 - START_CURVE_HEIGHT);
        double endPolyHeight = radiusSmol * (1 + END_CURVE_HEIGHT);

        totalReductionAngle = START_REDUCTION_ANGLE + END_REDUCTION_ANGLE;
        startCurveRatio = START_REDUCTION_ANGLE / totalReductionAngle;
        endCurveRatio = END_REDUCTION_ANGLE / totalReductionAngle;

        startAngle = new SimpleDoubleProperty(180);
        endAngle = new SimpleDoubleProperty(0);

        startLineRotate = new Rotate();
        startLineRotate.angleProperty().bind(startAngle);

        endLineRotate = new Rotate();
        endLineRotate.angleProperty().bind(endAngle);

        // helper bindings
        DoubleProperty arcStartPointX = new SimpleDoubleProperty();
        DoubleProperty arcStartPointY = new SimpleDoubleProperty();
        DoubleProperty arcStartTangentY = new SimpleDoubleProperty();

        arcStartPointX.bind(Bindings.createDoubleBinding(() ->
                    -radiusSmol * Math.sin(Math.toRadians(getStartReductionAngle())),
                startAngle, endAngle
        ));

        arcStartPointY.bind(Bindings.createDoubleBinding(() ->
                    radiusSmol * Math.cos(Math.toRadians(getStartReductionAngle())),
                startAngle, endAngle
        ));

        arcStartTangentY.bind(Bindings.createDoubleBinding(() ->
                    radiusSmol / Math.cos(Math.toRadians(getStartReductionAngle())),
                startAngle, endAngle
        ));

        DoubleProperty arcEndPoint1X = new SimpleDoubleProperty();
        DoubleProperty arcEndPoint1Y = new SimpleDoubleProperty();
        DoubleProperty arcEndPoint2X = new SimpleDoubleProperty();
        DoubleProperty arcEndPoint2Y = new SimpleDoubleProperty();
        DoubleProperty arcEndTangentQ = new SimpleDoubleProperty();
        DoubleProperty arcEndTangentL = new SimpleDoubleProperty();
        DoubleProperty arcEndTangentX = new SimpleDoubleProperty();
        DoubleProperty arcEndTangentY = new SimpleDoubleProperty();

        arcEndPoint1X.bind(Bindings.createDoubleBinding(() ->
                        radiusSmol * Math.sin(Math.toRadians(getLength() + getEndReductionAngle())),
                startAngle, endAngle
        ));

        arcEndPoint1Y.bind(Bindings.createDoubleBinding(() ->
                        radiusSmol * Math.cos(Math.toRadians(getLength() + getEndReductionAngle())),
                startAngle, endAngle
        ));

        arcEndPoint2X.bind(Bindings.createDoubleBinding(() ->
                        endPolyHeight  * Math.sin(Math.toRadians(getLength())),
                startAngle, endAngle
        ));

        arcEndPoint2Y.bind(Bindings.createDoubleBinding(() ->
                        endPolyHeight * Math.cos(Math.toRadians(getLength())),
                startAngle, endAngle
        ));

        arcEndTangentQ.bind(Bindings.createDoubleBinding(() ->
                        radiusSmol * Math.tan(Math.toRadians(getEndReductionAngle())),
                startAngle, endAngle
        ));

        arcEndTangentL.bind(Bindings.createDoubleBinding(() ->
                        radiusSmol / Math.cos(Math.toRadians(getEndReductionAngle())),
                startAngle, endAngle
        ));

        arcEndTangentX.bind(Bindings.createDoubleBinding(() ->
                        arcEndTangentL.get() * Math.cos(Math.toRadians(90 - getLength())),
//                        arcEndPoint1X.get() + arcEndTangentQ.get() * Math.cos(Math.toRadians(getLength() + 180)),
                startAngle, endAngle, arcEndTangentL
        ));

        arcEndTangentY.bind(Bindings.createDoubleBinding(() ->
                        arcEndTangentL.get() * Math.sin(Math.toRadians(90 - getLength())),
//                        arcEndPoint1Y.get() + arcEndTangentQ.get() * Math.sin(Math.toRadians(getLength() + 180)),
                startAngle, endAngle, arcEndTangentL
        ));

        // 1) start line
        startLine = new Line(0, 0, 0, -radiusSmol);
        startLine.getTransforms().add(startLineRotate);
        startLine.setStrokeLineCap(StrokeLineCap.ROUND);
        startLine.setStrokeWidth(strokeWidth);

        // 1b) start poly
        startPoly = new Polygon(
                -0.5 * strokeWidth, -startPolyHeight,
                -0.25, -Sunconfig.DOT_RADIUS_SMOL,
                0.25, -Sunconfig.DOT_RADIUS_SMOL,
                0.5 * strokeWidth, -startPolyHeight
        );
        startPoly.setStroke(Color.TRANSPARENT);
        startPoly.getTransforms().add(startLineRotate);

        // 2) start bezier curve
        startCurve = new CubicCurve(0, startPolyHeight, 0, radiusSmol, 0, radiusSmol, 0, startPolyHeight);
        startCurve.setFill(Color.TRANSPARENT);
        startCurve.setStrokeLineCap(StrokeLineCap.ROUND);

        startCurve.controlY1Property().bind(Bindings.createDoubleBinding(() ->
                    startPolyHeight + (arcStartTangentY.get() - startPolyHeight) * CURVE_SHARPNESS,
                arcStartTangentY
        ));

        startCurve.controlX2Property().bind(Bindings.createDoubleBinding(() ->
                    arcStartPointX.get() * (1 - CURVE_SHARPNESS),
                arcStartPointX
        ));

        startCurve.controlY2Property().bind(Bindings.createDoubleBinding(() ->
                    arcStartPointY.get() + (arcStartTangentY.get() - arcStartPointY.get()) * CURVE_SHARPNESS,
                arcStartPointY, arcStartTangentY
        ));

        startCurve.endXProperty().bind(arcStartPointX);
        startCurve.endYProperty().bind(arcStartPointY);
        startCurve.setStrokeWidth(strokeWidth);

        // 3) main arc
        mainArc = new Arc(0, 0, radiusSmol, radiusSmol, -90, -180);
        mainArc.setType(ArcType.OPEN);
        mainArc.setFill(Color.TRANSPARENT);
        mainArc.setStrokeLineCap(StrokeLineCap.ROUND);
        mainArc.setStrokeWidth(strokeWidth);

        mainArc.startAngleProperty().bind(Bindings.createDoubleBinding(() ->
                        90 - (startAngle.get() + getStartReductionAngle()),
                startAngle, endAngle
        ));

        mainArc.lengthProperty().bind(Bindings.createDoubleBinding(() -> {
                    double length = Math.abs(getLength());
                    return -(length - getStartReductionAngle() - getEndReductionAngle());
                },
                startAngle, endAngle
        ));

        // 4) end bezier curve
        endCurve = new CubicCurve(-10, -radiusSmol, 0, radiusSmol, 0, radiusSmol, 0, endPolyHeight);
        endCurve.setFill(Color.TRANSPARENT);
        endCurve.setStrokeLineCap(StrokeLineCap.ROUND);
        endCurve.setStrokeWidth(strokeWidth);

        endCurve.startXProperty().bind(arcEndPoint1X);
        endCurve.startYProperty().bind(arcEndPoint1Y);

        endCurve.controlX1Property().bind(Bindings.createDoubleBinding(() ->
                        arcEndPoint1X.get() + (arcEndTangentX.get() - arcEndPoint1X.get()) * CURVE_SHARPNESS,
                arcEndPoint1X, arcEndTangentX
        ));

        endCurve.controlY1Property().bind(Bindings.createDoubleBinding(() ->
                        arcEndPoint1Y.get() + (arcEndTangentY.get() - arcEndPoint1Y.get()) * CURVE_SHARPNESS,
                arcEndPoint1Y, arcEndTangentY
        ));

        endCurve.controlX2Property().bind(Bindings.createDoubleBinding(() ->
                        arcEndTangentX.get() + (arcEndPoint2X.get() - arcEndTangentX.get()) * (1 - CURVE_SHARPNESS),
                arcEndTangentX, arcEndPoint2X
        ));

        endCurve.controlY2Property().bind(Bindings.createDoubleBinding(() ->
                        arcEndTangentY.get() + (arcEndPoint2Y.get() - arcEndTangentY.get()) * (1 - CURVE_SHARPNESS),
                arcEndTangentY, arcEndPoint2Y
        ));

        endCurve.endXProperty().bind(arcEndPoint2X);
        endCurve.endYProperty().bind(arcEndPoint2Y);

        // 5) end line
        endLine = new Line(0, -endPolyHeight, 0, -radiusBig);
        endLine.getTransforms().add(endLineRotate);
        endLine.setStrokeLineCap(StrokeLineCap.ROUND);
        endLine.setStrokeWidth(strokeWidth);

        // 5b) end poly
        endPoly = new Polygon(
                -0.5 * strokeWidth, -endPolyHeight,
                -0.25, -radiusBig,
                0.25, -radiusBig,
                0.5 * strokeWidth, -endPolyHeight,
                0, -(endPolyHeight - endLineExtension)
        );
        endPoly.setStroke(Color.TRANSPARENT);
        endPoly.getTransforms().add(endLineRotate);

        super.getChildren().addAll(startPoly, startCurve, mainArc, endCurve, endPoly);
        super.setTranslateX(centerX);
        super.setTranslateY(centerY);
    }

    private double getLength() {
        double start = startAngle.get();
        double end = endAngle.get();
        return (start < end) ? -(end - start) : -(end + start);
    }

    private double getStartReductionAngle() {
        double length = Math.abs(getLength());
        return (length < totalReductionAngle) ? length * startCurveRatio : START_REDUCTION_ANGLE;
    }

    private double getEndReductionAngle() {
        double length = Math.abs(getLength());
        return (length < totalReductionAngle) ? length * endCurveRatio : END_REDUCTION_ANGLE;
    }


    // public

    public void setStartAngle(double startAngle) {
        this.startAngle.set(startAngle);
    }

    public void setEndAngle(double endAngle) {
        this.endAngle.set(endAngle);
    }

    public void setStrokeColor(Color color) {
        startLine.setStroke(color);
        endLine.setStroke(color);
        mainArc.setStroke(color);
        startPoly.setFill(color);
        endPoly.setFill(color);
        startCurve.setStroke(color);
        endCurve.setStroke(color);
    }

    // Builder
    public static class PleaseBuild {

        private double centerX;
        private double centerY;
        private double radiusBig;
        private double radiusSmol;
        private double strokeWidth;
        private double endLineExtension;

        public PleaseBuild() {
            centerX = 0;
            centerY = 0;
            radiusBig = 10;
            radiusSmol = radiusBig / 2;
            strokeWidth = 1.0;
            endLineExtension = 0.0;
        }

        public PleaseBuild center(double centerX, double centerY) {
            this.centerX = centerX;
            this.centerY = centerY;
            return this;
        }

        public PleaseBuild size(double radiusSmol, double radiusBig) {
            this.radiusSmol = radiusSmol;
            this.radiusBig = radiusBig;
            return this;
        }

        public PleaseBuild strokeWidth(double strokeWidth) {
            this.strokeWidth = strokeWidth;
            return this;
        }

        public PleaseBuild endLineExtension(double endLineExtension) {
            this.endLineExtension = endLineExtension;
            return this;
        }

        public SuperNiceArc thankYou() {
            return new SuperNiceArc(this);
        }
    }
}
