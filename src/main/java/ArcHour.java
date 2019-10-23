import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.scene.Group;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.transform.Rotate;

public class ArcHour extends Group {

    private double DEFAULT_WIDTH = 5.0d;
    private double DEFAULT_SHADOW_OPACITY = 0.5d;
    private double DEFAULT_LINE_LENGTH = 10.0d;
    private double DEFAULT_MATRIX_SCALE = 0.5;
    private double DEFAULT_MATRIX_OPACITY = 0.75;

    private double centerX;
    private double centerY;
    private double radius;

    private Rotate rotate;

    private Arc arcMinuteProgress;
    private Arc arcMinuteShadow;
    private Arc arcSecondProgress;
    private Arc arcSecondShadow;

    private Group minutesMarkerMatrixGroup;
    private Group secondsMarkerMatrixGroup;

    private Line lineStart;
    private Line lineEnd;

    public ArcHour(double centerX, double centerY, double radius, Rotate rotate) {

        super();

        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = radius;
        this.rotate = rotate;

        createArcHour();

        Group subGroup = new Group(
                minutesMarkerMatrixGroup, secondsMarkerMatrixGroup,
                lineStart, lineEnd, arcMinuteShadow, arcSecondShadow, arcMinuteProgress, arcSecondProgress
        );
        subGroup.setTranslateX(centerX);
        subGroup.setTranslateY(centerY);

        super.getChildren().add(subGroup);
        super.getTransforms().add(this.rotate);
    }

    private void createArcHour() {

        double secondsOffsetAngle = 90d / 75d;

        double segmentLength = (radius * Math.PI) / 59d;
        double dashLengthFull = segmentLength * (1d/20d);
        double dashLengthEmpty = segmentLength - dashLengthFull;

        double arcMinuteRadius = radius;
        double arcSecondRadius = radius + DEFAULT_WIDTH * 0.5;

        arcMinuteProgress = new Arc(0, 0, arcMinuteRadius, arcMinuteRadius, 0, -90);
        arcMinuteProgress.setType(ArcType.OPEN);
        arcMinuteProgress.setStrokeLineCap(StrokeLineCap.ROUND);
        arcMinuteProgress.setStrokeWidth(DEFAULT_WIDTH);
        arcMinuteProgress.setStroke(Color.WHITE);
        arcMinuteProgress.setFill(Color.TRANSPARENT);

        arcMinuteShadow = new Arc(0, 0, arcMinuteRadius, arcMinuteRadius, 0, -180);
        arcMinuteShadow.setType(ArcType.OPEN);
        arcMinuteShadow.setStrokeLineCap(StrokeLineCap.ROUND);
        arcMinuteShadow.setStrokeWidth(DEFAULT_WIDTH);
        arcMinuteShadow.setStroke(Color.BLACK);
        arcMinuteShadow.setFill(Color.TRANSPARENT);
        arcMinuteShadow.setBlendMode(BlendMode.OVERLAY);
        arcMinuteShadow.setOpacity(DEFAULT_SHADOW_OPACITY);

        arcSecondProgress = new Arc(0, 0, arcSecondRadius, arcSecondRadius, 180 - secondsOffsetAngle, -90);
        arcSecondProgress.setType(ArcType.OPEN);
        arcSecondProgress.setStrokeLineCap(StrokeLineCap.ROUND);
        arcSecondProgress.setStrokeWidth(DEFAULT_WIDTH);
        arcSecondProgress.getStrokeDashArray().addAll(dashLengthFull, dashLengthEmpty);
        arcSecondProgress.setStrokeDashOffset(-dashLengthEmpty * 0.5);
        arcSecondProgress.setStroke(Color.WHITE);
        arcSecondProgress.setFill(Color.TRANSPARENT);

        arcSecondShadow = new Arc(0, 0, arcSecondRadius, arcSecondRadius, 180 - secondsOffsetAngle, -180 + secondsOffsetAngle * 2);
        arcSecondShadow.setType(ArcType.OPEN);
        arcSecondShadow.setStrokeLineCap(StrokeLineCap.ROUND);
        arcSecondShadow.setStrokeWidth(DEFAULT_WIDTH);
        arcSecondShadow.getStrokeDashArray().addAll(dashLengthFull, dashLengthEmpty);
        arcSecondShadow.setStrokeDashOffset(-dashLengthEmpty * 0.5);
        arcSecondShadow.setStroke(Color.BLACK);
        arcSecondShadow.setFill(Color.TRANSPARENT);
        arcSecondShadow.setBlendMode(BlendMode.OVERLAY);
        arcSecondShadow.setOpacity(DEFAULT_SHADOW_OPACITY);

        lineStart = new Line(radius, 0, radius + DEFAULT_LINE_LENGTH, 0);
        lineStart.setStrokeWidth(DEFAULT_WIDTH);
        lineStart.setStroke(Color.WHITE);
        lineStart.setStrokeLineCap(StrokeLineCap.ROUND);
//        lineStart.setBlendMode(BlendMode.OVERLAY);
//        lineStart.setOpacity(DEFAULT_SHADOW_OPACITY);

        lineEnd = new Line(-radius, 0, -radius + DEFAULT_LINE_LENGTH, 0);
        lineEnd.setStrokeWidth(DEFAULT_WIDTH);
        lineEnd.setStroke(Color.BLACK);
        lineEnd.setStrokeLineCap(StrokeLineCap.ROUND);
        lineEnd.setBlendMode(BlendMode.OVERLAY);
        lineEnd.setOpacity(DEFAULT_SHADOW_OPACITY);

        minutesMarkerMatrixGroup = new Group();
        secondsMarkerMatrixGroup = new Group();

        double matrixRadius = radius - 8;

        for (int i = 1; i < 60; i++) {

            if (i % 10 == 0) {

                double x = matrixRadius * Math.cos(Math.toRadians(i * 3));
                double y = matrixRadius * Math.sin(Math.toRadians(i * 3));

                DotMatrix dotMatrix = new DotMatrix(i + "", Color.WHITE);
                dotMatrix.setStyle(Sunconfig.LOCALMINUTE_GLOW);
                dotMatrix.setOpacity(DEFAULT_MATRIX_OPACITY);
                dotMatrix.setScaleX(DEFAULT_MATRIX_SCALE);
                dotMatrix.setScaleY(DEFAULT_MATRIX_SCALE);
                dotMatrix.setTranslateX(x - dotMatrix.getLayoutBounds().getWidth() / 2);
                dotMatrix.setTranslateY(y - dotMatrix.getLayoutBounds().getHeight() / 2);
                dotMatrix.rotateProperty().bind(Bindings.createDoubleBinding(() ->
                        -rotate.angleProperty().get(), rotate.angleProperty()
                ));

                minutesMarkerMatrixGroup.getChildren().add(dotMatrix);
            }

            if (i % 5 == 0) {
                double x = matrixRadius * Math.cos(Math.toRadians(i * 3 + 180));
                double y = matrixRadius * Math.sin(Math.toRadians(i * 3 + 180));

                DotMatrix dotMatrix = new DotMatrix(i + "", Color.WHITE);
                dotMatrix.setStyle(Sunconfig.LOCALSECOND_GLOW);
                dotMatrix.setOpacity(DEFAULT_MATRIX_OPACITY);
                dotMatrix.setScaleX(DEFAULT_MATRIX_SCALE);
                dotMatrix.setScaleY(DEFAULT_MATRIX_SCALE);
                dotMatrix.setTranslateX(x - dotMatrix.getLayoutBounds().getWidth() / 2);
                dotMatrix.setTranslateY(y - dotMatrix.getLayoutBounds().getHeight() / 2);
                dotMatrix.rotateProperty().bind(Bindings.createDoubleBinding(() ->
                        -rotate.angleProperty().get(), rotate.angleProperty()
                ));

                secondsMarkerMatrixGroup.getChildren().add(dotMatrix);
            }
        }
    }

    public void setWidth(double width) {
        setSecondWidth(width);
        setMinuteWidth(width);
        setLineWidth(width);
    }

    public void setSecondWidth(double width) {
        arcSecondProgress.setStrokeWidth(width);
        arcSecondShadow.setStrokeWidth(width);
    }

    public void setMinuteWidth(double width) {
        arcMinuteProgress.setStrokeWidth(width);
        arcMinuteShadow.setStrokeWidth(width);
    }

    public void setLineWidth(double width) {
        lineStart.setStrokeWidth(width);
        lineEnd.setStrokeWidth(width);
    }

    public void setSecondStroke(Paint paint) {
        arcSecondProgress.setStroke(paint);
    }

    public void setMinuteStroke(Paint paint) {
        arcMinuteProgress.setStroke(paint);
    }

    public void setSecondStyle(String style) {
        arcSecondProgress.setStyle(style);
    }

    public void setMinuteStyle(String style) {
        arcMinuteProgress.setStyle(style);
    }

    public void setLineStartStroke(Paint paint) {
        lineStart.setStroke(paint);
    }

    public void setLineStartStyle(String style) {
        lineStart.setStyle(style);
    }

    public void setShadowOpacity(double opacity) {
        arcSecondShadow.setOpacity(opacity);
        arcMinuteShadow.setOpacity(opacity);
        lineEnd.setOpacity(opacity);
    }

    public void setProgressOpacity(double opacity) {
        arcSecondProgress.setOpacity(opacity);
        arcMinuteProgress.setOpacity(opacity);
        lineStart.setOpacity(opacity);
    }

    public void setTime(int minutes, int seconds) {
        arcSecondProgress.setLength(-(seconds / 60d) * 180);
        arcMinuteProgress.setLength(-(minutes / 60d) * 180 - (seconds / 3600d) * 180);
    }

    public void setMatrixOpacityBinding(DoubleProperty opacityProperty) {
        minutesMarkerMatrixGroup.opacityProperty().bind(opacityProperty);
        secondsMarkerMatrixGroup.opacityProperty().bind(opacityProperty);
    }
}
