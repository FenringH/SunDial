
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Miro
 */
public class DotMatrix extends Group {

    final static private int MAX_CHARS = 20;
    final static private int MATRIX_COLUMNS = 5;
    final static private int MATRIX_ROWS = 7;
    final static private double DOT_SIZE = 0.85;

    final static private Color DEFAULT_DOT_PAINT = new Color(1, 1, 1, 1);
    final static private Color PAINT_TRANSPARENT = new Color(0, 0, 0, 0);

    final static private long bitMask = 0b10000_00000_00000_00000_00000_00000_00000L;

    final private static long MATRIX_0 =           0b01110_10001_10001_10101_10001_10001_01110L;
    final private static long MATRIX_1 =           0b00010_00010_00010_00010_01010_00110_00010L;
    final private static long MATRIX_2 =           0b11111_10000_01000_00110_00001_10001_01110L;
    final private static long MATRIX_3 =           0b01110_10001_00001_00110_00001_10001_01110L;
    final private static long MATRIX_4 =           0b00010_00010_11111_10010_01010_00110_00010L;
    final private static long MATRIX_5 =           0b01110_10001_00001_11110_10000_10000_11111L;
    final private static long MATRIX_6 =           0b01110_10001_10001_11110_10000_10001_01110L;
    final private static long MATRIX_7 =           0b10000_10000_01000_00100_00010_00001_11111L;
    final private static long MATRIX_8 =           0b01110_10001_10001_01110_10001_10001_01110L;
    final private static long MATRIX_9 =           0b01110_10001_00001_01111_10001_10001_01110L;
    final private static long MATRIX_h =           0b10001_10001_10001_11110_10000_10000_10000L;
    final private static long MATRIX_m =           0b10101_10101_10101_10101_11010_00000_00000L;
    final private static long MATRIX_s =           0b11110_00001_01110_10000_01111_00000_00000L;
    final private static long MATRIX_N =           0b10001_10001_10001_10011_10101_11001_10001L;
    final private static long MATRIX_E =           0b11111_10000_10000_11110_10000_10000_11111L;
    final private static long MATRIX_W =           0b01010_10101_10101_10101_10101_10001_10001L;
    final private static long MATRIX_S =           0b01110_10001_00001_01110_10000_10001_01110L;
    final private static long MATRIX_G =           0b01110_10001_10001_10111_10000_10001_01110L;
    final private static long MATRIX_M =           0b10001_10001_10001_10001_10101_11011_10001L;
    final private static long MATRIX_T =           0b00100_00100_00100_00100_00100_00100_11111L;
    final private static long MATRIX_SPACE =       0b00000_00000_00000_00000_00000_00000_00000L;
    final private static long MATRIX_DASH =        0b00000_00000_00000_11111_00000_00000_00000L;
    final private static long MATRIX_PLUS =        0b00000_00100_00100_11111_00100_00100_00000L;
    final private static long MATRIX_COLON =       0b00000_00100_00000_00000_00100_00000_00000L;
    final private static long MATRIX_DOT =         0b00100_00000_00000_00000_00000_00000_00000L;
    final private static long MATRIX_QUESTION =    0b00100_00000_00100_00010_00001_10001_01110L;
    final private static long MATRIX_UNKNOWN =     0b00100_01010_10101_01010_10101_01010_00100L;

    private Circle[][][] dots;
    private String string;
    private Group stringGroup;
    private Paint dotPaint;

    public DotMatrix(String string, Paint dotPaint) {

        super();

        stringGroup = new Group();

        if (string == null || string.isEmpty()) {
            this.string = "##";
        } else {
            this.string = string;
        }

        if (dotPaint == null) { this.dotPaint = DEFAULT_DOT_PAINT; }
        else { this.dotPaint = dotPaint; }

        getChildren().add(getStringGroup(this.string, this.dotPaint));
    }

    private Group getCharGroup(int charIndex, char c, Paint dotPaint) {

        Group charGroup = new Group();

        Rectangle backPlate = new Rectangle(MATRIX_COLUMNS * 2, MATRIX_ROWS * 2);
        backPlate.setFill(PAINT_TRANSPARENT);
        charGroup.getChildren().add(backPlate);

        for (int dy = 0; dy < MATRIX_ROWS; dy++) {
            for (int dx = 0; dx < MATRIX_COLUMNS; dx++) {

                double positionX = (dx * 2.0) + 1.0;
                double positionY = ((MATRIX_COLUMNS - dy) * 2.0) + 3.0;

                Circle dot = new Circle(positionX, positionY, DOT_SIZE, dotPaint);
                charGroup.getChildren().add(dot);

                dots[charIndex][dy][dx] = dot;
            }
        }

        return charGroup;
    }

    private Group getStringGroup(String string, Paint dotPaint) {

        int stringSize = string.length();
        if (stringSize > MAX_CHARS) { string = string.substring(0, MAX_CHARS - 1); }

        dots = new Circle[stringSize][MATRIX_ROWS][MATRIX_COLUMNS];

        for (int index = 0; index < stringSize; index++) {
            char regularChar = string.charAt(index);
            Group digitChar = this.getCharGroup(index, regularChar, dotPaint);
            double shiftX = (MATRIX_COLUMNS + 1) * 2 * index;
            digitChar.setLayoutX(shiftX);
            stringGroup.getChildren().add(digitChar);
        }

        setString(string);

        Rectangle backPlate = new Rectangle(stringGroup.getLayoutBounds().getWidth(), stringGroup.getLayoutBounds().getHeight());
        backPlate.setFill(PAINT_TRANSPARENT);
        stringGroup.getChildren().add(backPlate);

        return stringGroup;
    }

    // methods
    public void setString(String s) {

        if (s == null) { return; }

        if (s.length() > this.string.length()) { s = s.substring(0, this.string.length() - 1); }

        int stringSize = s.length();
        for (int index = 0; index < stringSize; index++) {

            char regularChar = s.charAt(index);

            long charCode;

            switch(regularChar) {
                case '0': charCode = MATRIX_0; break;
                case '1': charCode = MATRIX_1; break;
                case '2': charCode = MATRIX_2; break;
                case '3': charCode = MATRIX_3; break;
                case '4': charCode = MATRIX_4; break;
                case '5': charCode = MATRIX_5; break;
                case '6': charCode = MATRIX_6; break;
                case '7': charCode = MATRIX_7; break;
                case '8': charCode = MATRIX_8; break;
                case '9': charCode = MATRIX_9; break;
                case 'h': charCode = MATRIX_h; break;
                case 'm': charCode = MATRIX_m; break;
                case 's': charCode = MATRIX_s; break;
                case 'N': charCode = MATRIX_N; break;
                case 'E': charCode = MATRIX_E; break;
                case 'W': charCode = MATRIX_W; break;
                case 'S': charCode = MATRIX_S; break;
                case 'G': charCode = MATRIX_G; break;
                case 'M': charCode = MATRIX_M; break;
                case 'T': charCode = MATRIX_T; break;
                case ' ': charCode = MATRIX_SPACE; break;
                case '-': charCode = MATRIX_DASH; break;
                case '+': charCode = MATRIX_PLUS; break;
                case ':': charCode = MATRIX_COLON; break;
                case '.': charCode = MATRIX_DOT; break;
                case '?': charCode = MATRIX_QUESTION; break;
                default : charCode = MATRIX_UNKNOWN; break;
            }

            for (int dy = 0; dy < MATRIX_ROWS; dy++) {
                for (int dx = 0; dx < MATRIX_COLUMNS; dx++) {

                    Circle dot = dots[index][dy][dx];

                    if ((charCode & bitMask) != 0) {
                        dot.setVisible(true);
                    } else {
                        dot.setVisible(false);
                    }

                    charCode <<= 1;
                }
            }
        }
    }

    public void setStroke(Paint dotPaint) {
        this.dotPaint = dotPaint;
    }
}
