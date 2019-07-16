import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;

public class SuperNiceArc extends Group {

    private final static double START_REDUCTION_ANGLE = 20;
    private final static double END_REDUCTION_ANGLE = 10;

    private final static double START_CURVE_HEIGHT = 0.2;
    private final static double END_CURVE_HEIGHT = 0.1;

    private final static double CURVE_SHARPNESS = 0.5;

    private double centerX;
    private double centerY;
    private double radiusBig;
    private double radiusSmol;

    private DoubleProperty startAngle;
    private DoubleProperty endAngle;

    private Line startLine;
    private Line endLine;
    private Arc mainArc;
    private Polygon startPoly;
    private CubicCurve startCurve;

    private Rotate startLineRotate;
    private Rotate endLineRotate;

    public SuperNiceArc(PleaseBuild builder) {

        super();

        this.centerX = builder.centerX;
        this.centerY = builder.centerY;
        this.radiusBig = builder.radiusBig;
        this.radiusSmol = builder.radiusSmol;

        double startPolyHeight = radiusSmol * (1 - START_CURVE_HEIGHT);
        double endPolyHeight = radiusSmol * (1 + END_CURVE_HEIGHT);

        startAngle = new SimpleDoubleProperty(180);
        endAngle = new SimpleDoubleProperty(0);

        startLineRotate = new Rotate();
        startLineRotate.angleProperty().bind(startAngle);

        endLineRotate = new Rotate();
        endLineRotate.angleProperty().bind(endAngle);

        DoubleProperty arcStartX = new SimpleDoubleProperty();
        DoubleProperty arcStartY = new SimpleDoubleProperty();
        DoubleProperty arcTangentY = new SimpleDoubleProperty();

        arcStartX.bind(Bindings.createDoubleBinding(() ->
                    -radiusSmol * Math.sin(Math.toRadians(getStartReductionAngle())),
                startAngle, endAngle
        ));

        arcStartY.bind(Bindings.createDoubleBinding(() ->
                    radiusSmol - radiusSmol * (1 - Math.cos(Math.toRadians(getStartReductionAngle()))),
                startAngle, endAngle
        ));

        arcTangentY.bind(Bindings.createDoubleBinding(() ->
                    radiusSmol / Math.cos(Math.toRadians(getStartReductionAngle())),
                startAngle, endAngle
        ));

        // 1) start line
        startLine = new Line(0, 0, 0, -radiusSmol);
        startLine.getTransforms().add(startLineRotate);
        startLine.setStrokeLineCap(StrokeLineCap.ROUND);

        // 1b) start poly
        startPoly = new Polygon(
                0, 0,
                -0.5, -startPolyHeight,
                0.5, -startPolyHeight
        );
        startPoly.setStroke(Color.TRANSPARENT);
        startPoly.getTransforms().add(startLineRotate);

        // 2) start bezier curve
        startCurve = new CubicCurve(0, startPolyHeight, 0, radiusSmol, 0, radiusSmol, 0, startPolyHeight);
        startCurve.setFill(Color.TRANSPARENT);
        startCurve.setStrokeLineCap(StrokeLineCap.ROUND);

        startCurve.controlY1Property().bind(Bindings.createDoubleBinding(() ->
                    startPolyHeight + (arcTangentY.get() - startPolyHeight) * CURVE_SHARPNESS,
                arcTangentY
        ));

        startCurve.controlX2Property().bind(Bindings.createDoubleBinding(() ->
                    arcStartX.get() * (1 - CURVE_SHARPNESS),
                arcStartX
        ));

        startCurve.controlY2Property().bind(Bindings.createDoubleBinding(() ->
                    arcStartY.get() + (arcTangentY.get() - arcStartY.get()) * CURVE_SHARPNESS,
                arcStartY, arcTangentY
        ));

        startCurve.endXProperty().bind(arcStartX);
        startCurve.endYProperty().bind(arcStartY);

        // 3) main arc
        mainArc = new Arc(0, 0, radiusSmol, radiusSmol, -90, -180);
        mainArc.setType(ArcType.OPEN);
        mainArc.setFill(Color.TRANSPARENT);
        mainArc.setStrokeLineCap(StrokeLineCap.ROUND);

        mainArc.startAngleProperty().bind(Bindings.createDoubleBinding(() ->
                        90 - (startAngle.get() + getStartReductionAngle() + END_REDUCTION_ANGLE),
                startAngle, endAngle
        ));

        mainArc.lengthProperty().bind(Bindings.createDoubleBinding(() -> {
                    double length = Math.abs(getLength(startAngle.get(), endAngle.get())) - END_REDUCTION_ANGLE;
                    return -(length - getStartReductionAngle());
                },
                startAngle, endAngle
        ));

        // 4) end bezier curve

        // 5) end line
        endLine = new Line(0, -endPolyHeight, 0, -radiusBig);
        endLine.getTransforms().add(endLineRotate);
        endLine.setStrokeLineCap(StrokeLineCap.ROUND);

        super.getChildren().addAll(startPoly, startCurve, endLine, mainArc);
        super.setTranslateX(centerX);
        super.setTranslateY(centerY);
    }

    private double getLength(double start, double end) {
        return (start < end) ? -(end - start) : -(end + start);
    }

    private double getStartReductionAngle() {
        double length = Math.abs(getLength(startAngle.get(), endAngle.get()));
        return (length < START_REDUCTION_ANGLE + END_REDUCTION_ANGLE) ? length : START_REDUCTION_ANGLE + END_REDUCTION_ANGLE;
    }

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
        startCurve.setStroke(color);
    }

    // Builder
    public static class PleaseBuild {

        private double centerX;
        private double centerY;
        private double radiusBig;
        private double radiusSmol;

        public PleaseBuild() {
            centerX = 0;
            centerY = 0;
            radiusBig = 10;
            radiusSmol = radiusBig / 2;
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

        public SuperNiceArc thankYou() {
            return new SuperNiceArc(this);
        }
    }
}
