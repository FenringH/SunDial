import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;

public class SuperNiceArc extends Group {

    private double centerX;
    private double centerY;
    private double radiusBig;
    private double radiusSmol;

    private DoubleProperty startAngle;
    private DoubleProperty endAngle;

    private Line startLine;
    private Line endLine;
    private Arc mainArc;

    private Rotate startLineRotate;
    private Rotate endLineRotate;

    public SuperNiceArc(PleaseBuild builder) {

        super();

        this.centerX = builder.centerX;
        this.centerY = builder.centerY;
        this.radiusBig = builder.radiusBig;
        this.radiusSmol = builder.radiusSmol;

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

        endLine = new Line(0, -radiusSmol, 0, -radiusBig);
        endLine.getTransforms().add(endLineRotate);

        mainArc = new Arc(0, 0, radiusSmol, radiusSmol, -90, -180);
        mainArc.setType(ArcType.OPEN);
        mainArc.setFill(Color.TRANSPARENT);

        mainArc.startAngleProperty().bind(Bindings.createDoubleBinding(() ->
                (90 - startAngle.get()),
                startAngle
        ));

        mainArc.lengthProperty().bind(Bindings.createDoubleBinding(() -> {
                    double start = startAngle.get();
                    double end = endAngle.get();
                    return (start < end) ? -(end - start) : -(end + start);
                },
                startAngle, endAngle
        ));

        super.getChildren().addAll(startLine, endLine, mainArc);
        super.setTranslateX(centerX);
        super.setTranslateY(centerY);
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
