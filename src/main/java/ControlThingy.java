import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;

import static java.lang.Math.*;

public class ControlThingy extends Group {

    public enum Type { CIRCLE, TRIANGLE };

    private Type type;
    private double size;
    private double x, y;
    private double strokeWidth;
    private Color strokeColorOff, strokeColorOn;
    private Color colorFill;
    private String matrixString;
    private Color matrixColorOff;
    private Color matrixColorOn;
    private String matrixShadow;
    private double matrixScale;
    private String styleShadow, styleGlow;
    private Cursor cursor;
    private String helpTextString;
    private Text helpTextObject;
    private int cycles;

    private int cycle;
    private boolean onEh;

    private Circle circle;
    private Circle overlayCircle;
    private Polygon triangle;
    private Polygon overlayTriangle;
    private DotMatrix dotMatrix;

    public ControlThingy(PleaseBuildControlThingy builder) {

        super();

        this.type = builder.type;
        this.size = builder.size;
        this.x = builder.x;
        this.y = builder.y;
        this.strokeWidth = builder.strokeWidth;
        this.strokeColorOff = builder.strokeColorOff;
        this.strokeColorOn = builder.strokeColorOn;
        this.colorFill = builder.colorFill;
        this.matrixString = builder.matrixString;
        this.matrixColorOff = builder.matrixColorOff;
        this.matrixColorOn = builder.matrixColorOn;
        this.matrixShadow = builder.matrixShadow;
        this.matrixScale = builder.matrixScale;
        this.styleShadow = builder.styleShadow;
        this.styleGlow = builder.styleGlow;
        this.helpTextString = builder.helpTextString;
        this.helpTextObject = builder.helpTextObject;
        this.cursor = builder.cursor;
        this.cycles = builder.cycles;

        this.cycle = 0;
        this.onEh = false;


        if (type.equals(Type.CIRCLE)) {
            circle = createCircle();
            super.getChildren().add(circle);
        }

        if (type.equals(Type.TRIANGLE)) {
            triangle = createTriangle();
            super.getChildren().addAll(triangle);
        }

        if (!matrixString.isEmpty()) {
            dotMatrix = createDotMatrix();
            super.getChildren().add(dotMatrix);
        }

        if (type.equals(Type.CIRCLE)) {
            overlayCircle = createOverlayCircle();
            super.getChildren().add(overlayCircle);
        }

        if (type.equals(Type.TRIANGLE)) {
            overlayTriangle = createOverlayTriangle();
            super.getChildren().addAll(overlayTriangle);
        }

        super.setTranslateX(x);
        super.setTranslateY(y);

        super.setStyle(styleShadow);

        // Events
        super.setOnMouseEntered(event -> {
            helpTextObject.setText(helpTextString);
            super.setCursor(cursor);
            super.setStyle(styleGlow);
        });

        super.setOnMouseExited(event -> {
            helpTextObject.setText(Sunconfig.HELPTEXT_DEFAULT);
            super.setCursor(Cursor.DEFAULT);
            super.setStyle(styleShadow);
        });

    }

    private Circle createCircle() {
        Circle circle = new Circle(size);
        circle.setFill(colorFill);
        circle.setStroke(strokeColorOff);
        circle.setStrokeWidth(strokeWidth);
        return circle;
    }

    private Circle createOverlayCircle() {
        Circle circle = new Circle(size);
        circle.setFill(Color.BLACK);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(strokeWidth);
        circle.setBlendMode(BlendMode.BLUE);
        circle.setOpacity(1);
        circle.setMouseTransparent(true);
        circle.setVisible(false);
        return circle;
    }

    private Polygon createTriangle() {
        Polygon triangle = new Polygon(
                size, 0,
                size, size,
                0, size
        );
        triangle.setFill(colorFill);
        triangle.setStroke(strokeColorOff);
        triangle.setStrokeWidth(strokeWidth);
        return triangle;
    }

    private Polygon createOverlayTriangle() {
        Polygon triangle = new Polygon(
                size, 0,
                size, size,
                0, size
        );
        triangle.setFill(Color.BLACK);
        triangle.setStroke(Color.BLACK);
        triangle.setStrokeWidth(strokeWidth);
        triangle.setBlendMode(BlendMode.BLUE);
        triangle.setOpacity(1);
        triangle.setMouseTransparent(true);
        triangle.setVisible(false);
        return triangle;
    }

    private DotMatrix createDotMatrix() {
        DotMatrix dotMatrix = new DotMatrix(matrixString, matrixColorOff);
        dotMatrix.setScaleX(matrixScale);
        dotMatrix.setScaleY(matrixScale);
        dotMatrix.setTranslateX(-dotMatrix.getLayoutBounds().getWidth() / 2);
        dotMatrix.setTranslateY(-dotMatrix.getLayoutBounds().getHeight() / 2);
        dotMatrix.setStyle(matrixShadow);
        return dotMatrix;
    }

    // Builder
    public static class PleaseBuildControlThingy {

        private Type type;
        private double size;
        private double x, y;
        private double strokeWidth;
        private String matrixString;
        private Color matrixColorOff;
        private Color matrixColorOn;
        private String matrixShadow;
        private double matrixScale;
        private String styleShadow, styleGlow;
        private Color strokeColorOff, strokeColorOn;
        private Color colorFill;
        private Cursor cursor;
        private String helpTextString;
        private Text helpTextObject;
        private int cycles;

        public PleaseBuildControlThingy() {

            // defaults
            this.type = Type.CIRCLE;
            this.size = Sunconfig.CONTROL_THINGY_RADIUS;
            this.x = 0; this.y = 0;
            this.strokeWidth = Sunconfig.CONTROL_THINGY_STROKE_WIDTH;
            this.matrixString = "";
            this.matrixColorOff = Color.WHITE;
            this.matrixColorOn = Color.WHITE;
            this.matrixShadow = Sunconfig.MATRIX_SHADOW;
            this.matrixScale = Sunconfig.MATRIX_CONTROLTHINGY_SCALE;
            this.styleShadow = Sunconfig.CONTROL_THINGY_SHADOW;
            this.styleGlow = Sunconfig.CONTROL_THINGY_GLOW;
            this.strokeColorOff = Sunconfig.Color_Of_ThingyStroke;
            this.strokeColorOn = Sunconfig.Color_Of_ThingyStroke;
            this.cursor = Cursor.DEFAULT;
            this.helpTextString = "No function";
            this.cycles = 0;

            this.colorFill = new Color(
                    this.strokeColorOff.getRed() * 0.5,
                    this.strokeColorOff.getGreen() * 0.5,
                    this.strokeColorOff.getBlue() * 0.5,
                    Sunconfig.CONTROL_THINGY_FILL_OPACITY
            );
        }

        public PleaseBuildControlThingy type(Type type) {
            this.type = type;
            return this;
        }

        public PleaseBuildControlThingy size(double size) {
            this.size = size;
            return this;
        }

        public PleaseBuildControlThingy positionCartesian(double x, double y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public PleaseBuildControlThingy positionPolar(double centerX, double centerY, double r, double alpha) {
            this.x = centerX + r * cos(toRadians(alpha));
            this.y = centerY - r * sin(toRadians(alpha));
            return this;
        }

        public PleaseBuildControlThingy marker(String matrixString, Color matrixColor, String matrixShadow) {
            this.matrixString = matrixString;
            this.matrixColorOff = matrixColor;
            this.matrixColorOn = matrixColor;
            this.matrixShadow = matrixShadow;
            this.matrixScale = Sunconfig.MATRIX_CONTROLTHINGY_SCALE;
            return this;
        }

        public PleaseBuildControlThingy markerColorOn(Color color) {
            this.matrixColorOn = color;
            return this;
        }

        public PleaseBuildControlThingy markerScale(double scale) {
            this.matrixScale = scale;
            return this;
        }

        public PleaseBuildControlThingy style(String styleShadow, String styleGlow) {
            this.styleShadow = styleShadow;
            this.styleGlow = styleGlow;
            return this;
        }

        public PleaseBuildControlThingy colorStroke(Color colorOff, Color colorOn) {
            this.strokeColorOff = colorOff;
            this.strokeColorOn = colorOn;
            return this;
        }

        public PleaseBuildControlThingy strokeWidth(double strokeWidth) {
            this.strokeWidth = strokeWidth;
            return this;
        }

        public PleaseBuildControlThingy colorFill(Color colorFill) {
            this.colorFill = colorFill;
            return this;
        }

        public PleaseBuildControlThingy cursor(Cursor cursor) {
            this.cursor = cursor;
            return this;
        }

        public PleaseBuildControlThingy cycles(int cycles) {
            this.cycles = cycles;
            return this;
        }

        public PleaseBuildControlThingy helpText(String helpTextString, Text helpTextObject) {
            this.helpTextString = helpTextString;
            this.helpTextObject = helpTextObject;
            return this;
        }

        public ControlThingy thankYou() {
            return new ControlThingy(this);
        }
    }

    // Methods
    public void toggleState() {
        onEh = !onEh;
        if (circle != null) { circle.setStroke(onEh ? strokeColorOn : strokeColorOff); }
        if (triangle != null) { triangle.setStroke(onEh ? strokeColorOn : strokeColorOff); }
        if (dotMatrix != null) { dotMatrix.setFill(onEh ? matrixColorOn : matrixColorOff); }
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;

        super.setTranslateX(this.x);
        super.setTranslateY(this.y);
    }

    public void toggleNightMode() {

        if (type.equals(Type.CIRCLE)) {
            if (overlayCircle.isVisible()) {
                overlayCircle.setVisible(false);
            } else {
                overlayCircle.setVisible(true);
            }
        }

        if (type.equals(Type.TRIANGLE)) {
            if (overlayTriangle.isVisible()) {
                overlayTriangle.setVisible(false);
            } else {
                overlayTriangle.setVisible(true);
            }
        }
    }

    // Getterers
    public boolean getState() {
        return onEh;
    }
}
