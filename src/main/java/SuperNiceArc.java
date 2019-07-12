import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;

public class SuperNiceArc extends Group {

    private final static double MAX_REDUCTION_ANGLE = 10;
    private final static double SUB_ARC_WIDTH = 0.1;
    private final static double SUB_ARC_HEIGHT = 0.2;
    private final static double PIXEL_HEIGHT_CORRECTION = 0.125;

    private double centerX;
    private double centerY;
    private double radiusBig;
    private double radiusSmol;

    private DoubleProperty startAngle;
    private DoubleProperty endAngle;

    private Line startLine;
    private Line endLine;
    private Arc mainArc;
    private Arc subArc;
    private Polygon startPoly;

    private Rotate startLineRotate;
    private Rotate endLineRotate;

    public SuperNiceArc(PleaseBuild builder) {

        super();

        this.centerX = builder.centerX;
        this.centerY = builder.centerY;
        this.radiusBig = builder.radiusBig;
        this.radiusSmol = builder.radiusSmol;

        double subArcWidth = radiusSmol * Math.sin(Math.toRadians(MAX_REDUCTION_ANGLE));
        double subArcHeight = radiusSmol * SUB_ARC_HEIGHT;
        double startPolyHeight = radiusSmol * (1 - SUB_ARC_HEIGHT);

        startAngle = new SimpleDoubleProperty(180);
        endAngle = new SimpleDoubleProperty(0);

//        startAngle.addListener((observable, oldValue, newValue) -> {});
//        endAngle.addListener((observable, oldValue, newValue) -> {});

        startLineRotate = new Rotate();
        startLineRotate.angleProperty().bind(startAngle);

        endLineRotate = new Rotate();
        endLineRotate.angleProperty().bind(endAngle);

        startLine = new Line(0, 0, 0, -radiusSmol);
        startLine.getTransforms().add(startLineRotate);
        startLine.setStrokeLineCap(StrokeLineCap.ROUND);

        endLine = new Line(0, -radiusSmol, 0, -radiusBig);
        endLine.getTransforms().add(endLineRotate);
        endLine.setStrokeLineCap(StrokeLineCap.ROUND);

        mainArc = new Arc(0, 0, radiusSmol, radiusSmol, -90, -180);
        mainArc.setType(ArcType.OPEN);
        mainArc.setFill(Color.TRANSPARENT);
        mainArc.setStrokeLineCap(StrokeLineCap.ROUND);

        mainArc.startAngleProperty().bind(Bindings.createDoubleBinding(() -> {
                    double length = Math.abs(getLength(startAngle.get(), endAngle.get()));
                    return (90 - (startAngle.get() + getReductionAngle(length)));
                },
                startAngle, endAngle
        ));

        mainArc.lengthProperty().bind(Bindings.createDoubleBinding(() -> {
                    double length = Math.abs(getLength(startAngle.get(), endAngle.get()));
                    return -(length - getReductionAngle(length));
                },
                startAngle, endAngle
        ));

        subArc = new Arc(subArcWidth, -startPolyHeight, subArcWidth, subArcHeight, 90, 90);
        subArc.setType(ArcType.OPEN);
        subArc.setFill(Color.TRANSPARENT);
        subArc.getTransforms().add(startLineRotate);
        subArc.setStrokeLineCap(StrokeLineCap.ROUND);

        subArc.radiusXProperty().bind(Bindings.createDoubleBinding(() -> {
                    double length = Math.abs(getLength(startAngle.get(), endAngle.get()));
                    return radiusSmol * Math.sin(Math.toRadians(getReductionAngle(length)));
                },
                startAngle, endAngle
        ));

        subArc.centerXProperty().bind(Bindings.createDoubleBinding(() -> {
                    double length = Math.abs(getLength(startAngle.get(), endAngle.get()));
                    return radiusSmol * Math.sin(Math.toRadians(getReductionAngle(length)));
                },
                startAngle, endAngle
        ));

        subArc.radiusYProperty().bind(Bindings.createDoubleBinding(() -> {
                    double length = Math.abs(getLength(startAngle.get(), endAngle.get()));
                    return subArcHeight
                            - radiusSmol * (1 - Math.cos(Math.toRadians(getReductionAngle(length))))
                            - PIXEL_HEIGHT_CORRECTION
                            ;
                },
                startAngle, endAngle
        ));

        startPoly = new Polygon(
                0, 0,
                -0.5, -startPolyHeight,
                0.5, -startPolyHeight
        );
        startPoly.setStroke(Color.TRANSPARENT);
        startPoly.getTransforms().add(startLineRotate);

        super.getChildren().addAll(startPoly, subArc, endLine, mainArc);
        super.setTranslateX(centerX);
        super.setTranslateY(centerY);
    }

    private double getLength(double start, double end) {
        return (start < end) ? -(end - start) : -(end + start);
    }

    private double getReduction(double radius, double length) {
        return (length < MAX_REDUCTION_ANGLE) ? (radius * length / MAX_REDUCTION_ANGLE) : radius;
    }

    private double getReductionAngle(double length) {
        return (length < MAX_REDUCTION_ANGLE) ? length : MAX_REDUCTION_ANGLE;
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
        subArc.setStroke(color);
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
