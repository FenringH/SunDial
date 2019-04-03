import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.scene.*;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;
import javafx.util.Duration;

import java.util.ArrayList;

import static java.lang.Math.*;

public class Suncreator {

    public enum TimelineDirection { IN, OUT };

    public enum ControlThingyType {
        HELP,
        RESIZE,
        CLOSE,
        MAXIMIZE,
        MINIMIZE,
        NIGTMODE,
        ALWAYSONTOP,
        GLOBEGRID,
        GLOBELINES
    };

    public static ControlThingy createControlThingy(ControlThingyType type, Text helpText) {

        ControlThingy controlThingy;

        switch (type) {
            case HELP:
                controlThingy = new ControlThingy.PleaseBuildControlThingy()
                    .positionPolar(Sunconfig.CENTER_X, Sunconfig.CENTER_Y, Sunconfig.CONTROL_HELP_OFFSET, Sunconfig.CONTROL_HELP_ANGLE)
                    .size(Sunconfig.CONTROL_HELP_RADIUS)
                    .colorStroke(Sunconfig.Color_Of_ResizeStroke, Color.WHITE)
                    .strokeWidth(Sunconfig.CONTROL_HELP_STROKE_WIDTH)
                    .colorFill(Sunconfig.Color_Of_ResizeFill)
                    .marker("?", Color.WHITE, Sunconfig.MATRIX_SHADOW)
                    .style(Sunconfig.CONTROL_RESIZE_SHADOW, Sunconfig.CONTROL_RESIZE_GLOW)
                    .cursor(Cursor.HAND)
                    .helpText(Sunconfig.HELPTEXT_DEFAULT, helpText)
                    .thankYou();
            break;
            case RESIZE:
                controlThingy = new ControlThingy.PleaseBuildControlThingy()
                    .type(ControlThingy.Type.TRIANGLE)
                    .positionCartesian(Sunconfig.CENTER_X + Sunconfig.CONTROL_RESIZE_OFFSET, Sunconfig.CENTER_Y + Sunconfig.CONTROL_RESIZE_OFFSET)
                    .size(Sunconfig.CONTROL_RESIZE_SIZE)
                    .colorStroke(Sunconfig.Color_Of_ResizeStroke, Color.WHITE)
                    .strokeWidth(Sunconfig.CONTROL_RESIZE_STROKE_WIDTH)
                    .colorFill(Sunconfig.Color_Of_ResizeFill)
                    .style(Sunconfig.CONTROL_RESIZE_SHADOW, Sunconfig.CONTROL_RESIZE_GLOW)
                    .cursor(Cursor.NW_RESIZE)
                    .helpText(Sunconfig.HELPTEXT_RESIZE, helpText)
                    .thankYou();
            break;
            case CLOSE:
                controlThingy = new ControlThingy.PleaseBuildControlThingy()
                    .positionPolar(Sunconfig.CENTER_X, Sunconfig.CENTER_Y, Sunconfig.CONTROL_CLOSE_OFFSET, Sunconfig.CONTROL_CLOSE_ANGLE)
                    .size(Sunconfig.CONTROL_CLOSE_RADIUS)
                    .colorStroke(Sunconfig.Color_Of_CloseStroke, Sunconfig.Color_Of_CloseStroke)
                    .strokeWidth(Sunconfig.CONTROL_CLOSE_STROKE_WIDTH)
                    .colorFill(Sunconfig.Color_Of_CloseFill)
                    .style(Sunconfig.CONTROL_CLOSE_SHADOW, Sunconfig.CONTROL_CLOSE_GLOW)
                    .cursor(Cursor.HAND)
                    .helpText(Sunconfig.HELPTEXT_CLOSE, helpText)
                    .thankYou();
            break;
            case MAXIMIZE:
                controlThingy = new ControlThingy.PleaseBuildControlThingy()
                    .positionPolar(Sunconfig.CENTER_X, Sunconfig.CENTER_Y, Sunconfig.CONTROL_MAXIMIZE_OFFSET, Sunconfig.CONTROL_MAXIMIZE_ANGLE)
                    .size(Sunconfig.CONTROL_MAXIMIZE_RADIUS)
                    .colorStroke(Sunconfig.Color_Of_MaximizeStroke, Color.WHITE)
                    .strokeWidth(Sunconfig.CONTROL_MAXIMIZE_STROKE_WIDTH)
                    .colorFill(Sunconfig.Color_Of_MaximizeFill)
                    .style(Sunconfig.CONTROL_MAXIMIZE_SHADOW, Sunconfig.CONTROL_MAXIMIZE_GLOW)
                    .cursor(Cursor.HAND)
                    .helpText(Sunconfig.HELPTEXT_MAXIMIZE, helpText)
                    .thankYou();
            break;
            case MINIMIZE:
                controlThingy = new ControlThingy.PleaseBuildControlThingy()
                    .positionPolar(Sunconfig.CENTER_X, Sunconfig.CENTER_Y, Sunconfig.CONTROL_MINIMIZE_OFFSET, Sunconfig.CONTROL_MINIMIZE_ANGLE)
                    .size(Sunconfig.CONTROL_MINIMIZE_RADIUS)
                    .colorStroke(Sunconfig.Color_Of_MinimizeStroke, Color.WHITE)
                    .strokeWidth(Sunconfig.CONTROL_MINIMIZE_STROKE_WIDTH)
                    .colorFill(Sunconfig.Color_Of_MinimizeFill)
                    .style(Sunconfig.CONTROL_MINIMIZE_SHADOW, Sunconfig.CONTROL_MINIMIZE_GLOW)
                    .cursor(Cursor.HAND)
                    .helpText(Sunconfig.HELPTEXT_MINIMIZE, helpText)
                    .thankYou();
            break;
            case NIGTMODE:
                controlThingy = new ControlThingy.PleaseBuildControlThingy()
                    .positionPolar(Sunconfig.CENTER_X, Sunconfig.CENTER_Y, Sunconfig.CONTROL_NIGHTMODE_OFFSET, Sunconfig.CONTROL_NIGHTMODE_ANGLE)
                    .size(Sunconfig.CONTROL_NIGHTMODE_RADIUS)
                    .colorStroke(Sunconfig.Color_Of_NightmodeStroke, Color.WHITE)
                    .strokeWidth(Sunconfig.CONTROL_NIGHTMODE_STROKE_WIDTH)
                    .colorFill(Sunconfig.Color_Of_NightmodeFill)
                    .style(Sunconfig.CONTROL_NIGHTMODE_SHADOW, Sunconfig.CONTROL_NIGHTMODE_GLOW)
                    .cursor(Cursor.HAND)
                    .helpText(Sunconfig.HELPTEXT_NIGHTMODE, helpText)
                    .thankYou();
            break;
            case ALWAYSONTOP:
                controlThingy = new ControlThingy.PleaseBuildControlThingy()
                    .positionPolar(Sunconfig.CENTER_X, Sunconfig.CENTER_Y, Sunconfig.CONTROL_ALWAYSONTOP_OFFSET, Sunconfig.CONTROL_ALWAYSONTOP_ANGLE)
                    .size(Sunconfig.CONTROL_ALWAYSONTOP_RADIUS)
                    .colorStroke(Sunconfig.Color_Of_AlwaysOnTopStroke, Color.WHITE)
                    .strokeWidth(Sunconfig.CONTROL_ALWAYSONTOP_STROKE_WIDTH)
                    .colorFill(Sunconfig.Color_Of_AlwaysOnTopFill)
                    .style(Sunconfig.CONTROL_ALWAYSONTOP_SHADOW, Sunconfig.CONTROL_ALWAYSONTOP_GLOW)
                    .cursor(Cursor.HAND)
                    .helpText(Sunconfig.HELPTEXT_ALWAYSONTOP, helpText)
                    .thankYou();
                controlThingy.toggle();
                break;
            case GLOBEGRID:
                controlThingy = new ControlThingy.PleaseBuildControlThingy()
                    .positionPolar(Sunconfig.CENTER_X, Sunconfig.CENTER_Y, Sunconfig.CONTROL_GLOBEGRID_OFFSET, 40)
                    .size(Sunconfig.CONTROL_GLOBEGRID_RADIUS)
                    .colorStroke(Sunconfig.Color_Of_GlobeGridStroke, Color.WHITE)
                    .strokeWidth(Sunconfig.CONTROL_GLOBEGRID_STROKE_WIDTH)
                    .colorFill(Sunconfig.Color_Of_GlobeGridFill)
//                    .marker("G", Color.WHITE, Sunconfig.MATRIX_SHADOW)
                    .style(Sunconfig.CONTROL_GLOBEGRID_SHADOW, Sunconfig.CONTROL_GLOBEGRID_GLOW)
                    .cursor(Cursor.HAND)
                    .helpText(Sunconfig.HELPTEXT_GLOBEGRID, helpText)
                    .thankYou();
                controlThingy.setVisible(false);
                break;
            case GLOBELINES:
                controlThingy = new ControlThingy.PleaseBuildControlThingy()
                        .positionPolar(Sunconfig.CENTER_X, Sunconfig.CENTER_Y, Sunconfig.CONTROL_GLOBEGRID_OFFSET, 50)
                        .size(Sunconfig.CONTROL_GLOBEGRID_RADIUS)
                        .colorStroke(Sunconfig.Color_Of_GlobeGridStroke, Color.WHITE)
                        .strokeWidth(Sunconfig.CONTROL_GLOBEGRID_STROKE_WIDTH)
                        .colorFill(Sunconfig.Color_Of_GlobeGridFill)
//                    .marker("G", Color.WHITE, Sunconfig.MATRIX_SHADOW)
                        .style(Sunconfig.CONTROL_GLOBEGRID_SHADOW, Sunconfig.CONTROL_GLOBEGRID_GLOW)
                        .cursor(Cursor.HAND)
                        .helpText(Sunconfig.HELPTEXT_GLOBELINES, helpText)
                        .thankYou();
                controlThingy.setVisible(false);
                break;
            default:
                controlThingy = new ControlThingy.PleaseBuildControlThingy()
                    .positionPolar(Sunconfig.CENTER_X, Sunconfig.CENTER_Y, Sunconfig.CONTROL_HELP_OFFSET, Sunconfig.CONTROL_HELP_ANGLE)
                    .size(Sunconfig.CONTROL_HELP_RADIUS)
                    .colorStroke(Sunconfig.Color_Of_ResizeStroke, Color.WHITE)
                    .strokeWidth(Sunconfig.CONTROL_HELP_STROKE_WIDTH)
                    .colorFill(Sunconfig.Color_Of_ResizeFill)
                    .marker("?", Color.WHITE, Sunconfig.MATRIX_SHADOW)
                    .style(Sunconfig.CONTROL_RESIZE_SHADOW, Sunconfig.CONTROL_RESIZE_GLOW)
                    .cursor(Cursor.HAND)
                    .helpText(Sunconfig.HELPTEXT_DEFAULT, helpText)
                    .thankYou();
        }

        return controlThingy;
    }

    public static Group createMasterGlobe(
            DoubleProperty longitude,
            DoubleProperty latitude,
            DoubleProperty phase,
            DoubleProperty tilt,
            DoubleProperty lightScale,
            BooleanProperty gridVisibleEh,
            BooleanProperty linesVisibleEh
    ) {

        Globe dayGlobe = new Globe(Sunconfig.GLOBE_DAY_IMAGE, Sunconfig.CENTER_X - Sunconfig.MARGIN_X, Sunconfig.GLOBE_ROTATE_DURATION);
        dayGlobe.setLayoutX(Sunconfig.CENTER_X);
        dayGlobe.setLayoutY(Sunconfig.CENTER_Y);
        dayGlobe.setNightLightColor(Color.RED);
        dayGlobe.longitudeProperty().bind(longitude);
        dayGlobe.latitudeProperty().bind(latitude);
        dayGlobe.phaseProperty().bind(phase);
        dayGlobe.tiltProperty().bind(tilt);
        dayGlobe.lightScaleProperty().bind(lightScale);

        Globe nightGlobe = new Globe(Sunconfig.GLOBE_NIGHT_IMAGE, Sunconfig.CENTER_X - Sunconfig.MARGIN_X, Sunconfig.GLOBE_ROTATE_DURATION);
        nightGlobe.setLayoutX(Sunconfig.CENTER_X);
        nightGlobe.setLayoutY(Sunconfig.CENTER_Y);
        nightGlobe.setAmbientLightColor(Color.WHITE);
        nightGlobe.longitudeProperty().bind(longitude);
        nightGlobe.latitudeProperty().bind(latitude);
        nightGlobe.phaseProperty().bind(phase);
        nightGlobe.tiltProperty().bind(tilt);

        GlobeGrid globeGrid = new GlobeGrid(Sunconfig.CENTER_X - Sunconfig.MARGIN_X, Sunconfig.GLOBEGRID_LINE_WIDTH, Color.WHITE, Sunconfig.GLOBE_ROTATE_DURATION);
        globeGrid.setLayoutX(Sunconfig.CENTER_X);
        globeGrid.setLayoutY(Sunconfig.CENTER_Y);
        globeGrid.longitudeProperty().bind(longitude);
        globeGrid.latitudeProperty().bind(latitude);
        globeGrid.visibleProperty().bind(gridVisibleEh);

        GlobeLines globeLines = new GlobeLines(Sunconfig.CENTER_X - Sunconfig.MARGIN_X, 2, Color.WHITE, Sunconfig.GLOBE_ROTATE_DURATION);
        globeLines.setLayoutX(Sunconfig.CENTER_X);
        globeLines.setLayoutY(Sunconfig.CENTER_Y);
        globeLines.longitudeProperty().bind(longitude);
        globeLines.latitudeProperty().bind(latitude);
        globeLines.visibleProperty().bind(linesVisibleEh);

        Ring dayTerminatorLine = new Ring(Sunconfig.CENTER_X - Sunconfig.MARGIN_X, Sunconfig.DAY_TERMINATOR_WIDTH, Sunconfig.Color_Of_TerminatorLine, Sunconfig.GLOBE_ROTATE_DURATION);
        dayTerminatorLine.setLayoutX(Sunconfig.CENTER_X);
        dayTerminatorLine.setLayoutY(Sunconfig.CENTER_Y);
        dayTerminatorLine.longitudeProperty().bind(longitude);
        dayTerminatorLine.latitudeProperty().bind(latitude);
        dayTerminatorLine.phaseProperty().bind(phase);
        dayTerminatorLine.tiltProperty().bind(tilt);

        Ring dayTerminatorGlow = new Ring(Sunconfig.CENTER_X - Sunconfig.MARGIN_X, Sunconfig.DAY_TERMINATOR_GLOW_WIDTH, Sunconfig.Color_Of_TerminatorGlow, Sunconfig.GLOBE_ROTATE_DURATION);
        dayTerminatorGlow.setLayoutX(Sunconfig.CENTER_X);
        dayTerminatorGlow.setLayoutY(Sunconfig.CENTER_Y);
        dayTerminatorGlow.longitudeProperty().bind(longitude);
        dayTerminatorGlow.latitudeProperty().bind(latitude);
        dayTerminatorGlow.phaseProperty().bind(phase);
        dayTerminatorGlow.tiltProperty().bind(tilt);


        SubScene dayGlobeScene = new SubScene(dayGlobe, Sundial.DEFAULT_WIDTH, Sundial.DEFAULT_HEIGHT, true, SceneAntialiasing.BALANCED);
        dayGlobeScene.setBlendMode(BlendMode.ADD);

        SubScene nightGlobeScene = new SubScene(nightGlobe, Sundial.DEFAULT_WIDTH, Sundial.DEFAULT_HEIGHT, true, SceneAntialiasing.BALANCED);
        nightGlobeScene.setBlendMode(BlendMode.SCREEN);

        SubScene globeGridScene = new SubScene(globeGrid, Sundial.DEFAULT_WIDTH, Sundial.DEFAULT_HEIGHT, true, SceneAntialiasing.BALANCED);
        globeGridScene.setBlendMode(BlendMode.SCREEN);
        globeGridScene.setEffect(new GaussianBlur(1));
        globeGridScene.setOpacity(Sunconfig.DAY_TERMINATOR_OPACITY);

        SubScene globeLinesScene = new SubScene(globeLines, Sundial.DEFAULT_WIDTH, Sundial.DEFAULT_HEIGHT, true, SceneAntialiasing.BALANCED);
        globeLinesScene.setBlendMode(BlendMode.SCREEN);
        globeLinesScene.setEffect(new GaussianBlur(1));
        globeLinesScene.setOpacity(Sunconfig.DAY_GRIDLINE_OPACITY);

        SubScene dayTerminatorLineScene = new SubScene(dayTerminatorLine, Sundial.DEFAULT_WIDTH, Sundial.DEFAULT_HEIGHT, true, SceneAntialiasing.BALANCED);
        dayTerminatorLineScene.setBlendMode(BlendMode.SCREEN);
        dayTerminatorLineScene.setEffect(new GaussianBlur(Sunconfig.GLOBEGRID_LINE_WIDTH));
        dayTerminatorLineScene.setOpacity(Sunconfig.DAY_TERMINATOR_OPACITY);

        SubScene dayTerminatorGlowScene = new SubScene(dayTerminatorGlow, Sundial.DEFAULT_WIDTH, Sundial.DEFAULT_HEIGHT, true, SceneAntialiasing.BALANCED);
        dayTerminatorGlowScene.setBlendMode(BlendMode.SCREEN);
        dayTerminatorGlowScene.setEffect(new GaussianBlur(Sunconfig.DAY_TERMINATOR_GLOW_WIDTH));
        dayTerminatorGlowScene.setOpacity(Sunconfig.DAY_TERMINATOR_OPACITY);

        Circle globeAtmosphere = new Circle(Sunconfig.CENTER_X, Sunconfig.CENTER_Y, Sunconfig.CENTER_X - Sunconfig.MARGIN_X + 2);
        globeAtmosphere.setFill(Sunconfig.GLOBE_ATMOSPHERE);
        globeAtmosphere.setStroke(Sunconfig.Color_Of_Void);
        globeAtmosphere.setMouseTransparent(true);

        return new Group(dayGlobeScene, nightGlobeScene, globeGridScene, globeLinesScene, dayTerminatorGlowScene, dayTerminatorLineScene, globeAtmosphere);
    }

    public static Group createTinyGlobe(DoubleProperty longitude, DoubleProperty latitude, DoubleProperty phase, DoubleProperty tilt) {

        Globe tinyGlobe = new Globe(Sunconfig.GLOBE_DAY_IMAGE, Sunconfig.TINYGLOBE_RADIUS, Sunconfig.GLOBE_ROTATE_DURATION);
        tinyGlobe.setDayLightColor(Sunconfig.Color_Of_TinyDay);
        tinyGlobe.setNightLightColor(Sunconfig.Color_Of_TinyNight);
        tinyGlobe.setAmbientLightColor(Sunconfig.Color_Of_TinyAmbient);
        tinyGlobe.setLayoutX(Sunconfig.CENTER_X);
        tinyGlobe.setLayoutY(Sunconfig.CENTER_Y + Sunconfig.TINYGLOBE_OFFSET);
        tinyGlobe.longitudeProperty().bind(longitude);
        tinyGlobe.latitudeProperty().bind(latitude);
        tinyGlobe.phaseProperty().bind(phase);
        tinyGlobe.tiltProperty().bind(tilt);

        SubScene tinyGlobeScene = new SubScene(tinyGlobe, Sundial.DEFAULT_WIDTH, Sundial.DEFAULT_HEIGHT, true, SceneAntialiasing.BALANCED);

        Ring tinyDayTerminatorLine = new Ring(Sunconfig.TINYGLOBE_RADIUS, Sunconfig.TINYGLOBE_TERMINATOR_WIDTH, Sunconfig.Color_Of_TerminatorLine, Sunconfig.GLOBE_ROTATE_DURATION);
        tinyDayTerminatorLine.setTranslateX(Sunconfig.CENTER_X);
        tinyDayTerminatorLine.setTranslateY(Sunconfig.CENTER_Y + Sunconfig.TINYGLOBE_OFFSET);
        tinyDayTerminatorLine.longitudeProperty().bind(longitude);
        tinyDayTerminatorLine.latitudeProperty().bind(latitude);
        tinyDayTerminatorLine.phaseProperty().bind(phase);
        tinyDayTerminatorLine.tiltProperty().bind(tilt);

        SubScene tinyDayTerminatorLineScene = new SubScene(tinyDayTerminatorLine, Sundial.DEFAULT_WIDTH, Sundial.DEFAULT_HEIGHT, true, SceneAntialiasing.BALANCED);
        tinyDayTerminatorLineScene.setBlendMode(BlendMode.ADD);
        tinyDayTerminatorLineScene.setEffect(new GaussianBlur(Sunconfig.DAY_TERMINATOR_WIDTH));
        tinyDayTerminatorLineScene.setOpacity(Sunconfig.DAY_TERMINATOR_OPACITY);

        Circle tinyGlobeDot = new Circle(Sunconfig.TINYGLOBE_DOT_RADIUS);
        tinyGlobeDot.setFill(Sunconfig.Color_Of_TinyFrame);
        tinyGlobeDot.setStroke(Sunconfig.Color_Of_Void);
        tinyGlobeDot.setTranslateX(Sunconfig.CENTER_X);
        tinyGlobeDot.setTranslateY(Sunconfig.CENTER_Y + Sunconfig.TINYGLOBE_OFFSET);

        Group tinyGlobeGroup = new Group(tinyGlobeScene, tinyDayTerminatorLineScene, tinyGlobeDot);

        return tinyGlobeGroup;
    }

    public static Circle createTinyGlobeFrame() {

        Circle tinyGlobeFrame = new Circle(Sunconfig.TINYGLOBE_RADIUS);
        tinyGlobeFrame.setFill(Sunconfig.Color_Of_Void);
        tinyGlobeFrame.setStroke(Sunconfig.Color_Of_TinyFrame);
        tinyGlobeFrame.setStrokeWidth(Sunconfig.TINYGLOBE_FRAME_STROKE_WIDTH);
        tinyGlobeFrame.setStyle(Sunconfig.MATRIX_SHADOW);
        tinyGlobeFrame.setTranslateX(Sunconfig.CENTER_X);
        tinyGlobeFrame.setTranslateY(Sunconfig.CENTER_Y + Sunconfig.TINYGLOBE_OFFSET);

        return tinyGlobeFrame;
    }

    public static Timeline createTinyGlobeTimeline(TimelineDirection timelineDirection, Group tinyGlobe, Scale tinyGlobeScale) {

        double slideX, slideY, scale, opacity;

        if (timelineDirection.equals(TimelineDirection.IN)) {
            slideX = 0;
            slideY = 0;
            opacity = Sunconfig.TINYGLOBE_DEFAULT_OPACITY;
            scale = 1;
        } else {
            slideX = Sunconfig.CENTER_X * cos(toRadians(135)) - Sunconfig.TINYGLOBE_SLIDE;
            slideY = Sunconfig.CENTER_Y * sin(toRadians(135)) - Sunconfig.TINYGLOBE_OFFSET + Sunconfig.TINYGLOBE_SLIDE;
            opacity = Sunconfig.TINYGLOBE_OFFSET_OPACITY;
            scale = Sunconfig.TINYGLOBE_DOWNSCALE;
        }

        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        timeline.setRate(1);
        timeline.setAutoReverse(false);

        KeyValue keyValueScaleX = new KeyValue(tinyGlobeScale.xProperty(), scale, Interpolator.EASE_BOTH);
        KeyFrame keyFrameScaleX = new KeyFrame(Duration.millis(Sunconfig.TINY_GLOBE_DURATION), keyValueScaleX);
        KeyValue keyValueScaleY = new KeyValue(tinyGlobeScale.yProperty(), scale, Interpolator.EASE_BOTH);
        KeyFrame keyFrameScaleY = new KeyFrame(Duration.millis(Sunconfig.TINY_GLOBE_DURATION), keyValueScaleY);

        KeyValue keyValueSlideX = new KeyValue(tinyGlobe.translateXProperty(), slideX, Interpolator.EASE_BOTH);
        KeyFrame keyFrameSlideX = new KeyFrame(Duration.millis(Sunconfig.TINY_GLOBE_DURATION), keyValueSlideX);
        KeyValue keyValueSlideY = new KeyValue(tinyGlobe.translateYProperty(), slideY, Interpolator.EASE_OUT);
        KeyFrame keyFrameSlideY = new KeyFrame(Duration.millis(Sunconfig.TINY_GLOBE_DURATION), keyValueSlideY);

        KeyValue keyValueOpacity = new KeyValue(tinyGlobe.opacityProperty(), opacity, Interpolator.EASE_OUT);
        KeyFrame keyFrameOpacity = new KeyFrame(Duration.millis(Sunconfig.TINY_GLOBE_DURATION), keyValueOpacity);

        timeline.getKeyFrames().addAll(keyFrameScaleX, keyFrameScaleY, keyFrameSlideX, keyFrameSlideY, keyFrameOpacity);

        return timeline;
    }

    public static Circle createDialMarginCircle() {
        Circle dialMarginCircle = new Circle(Sunconfig.CENTER_X, Sunconfig.CENTER_Y, Sundial.DEFAULT_WIDTH / 2);
        dialMarginCircle.setFill(Sunconfig.Color_Of_Margin);
        dialMarginCircle.setStroke(Sunconfig.Color_Of_Void);
        dialMarginCircle.setOpacity(Sunconfig.MARGIN_CIRCLE_OPACITY);
        return dialMarginCircle;
    }

    public static SubScene createBackgroundSubScene(Circle dialMarginCircle) {

        Rectangle dialMarginFillBox = new Rectangle(Sundial.DEFAULT_WIDTH, Sundial.DEFAULT_HEIGHT);
        dialMarginFillBox.setTranslateX(0);
        dialMarginFillBox.setTranslateY(0);
        dialMarginFillBox.setFill(Sunconfig.Color_Of_DaySky);
        dialMarginFillBox.setStroke(Sunconfig.Color_Of_Void);
        dialMarginFillBox.setOpacity(0);

        return new SubScene(new Group(dialMarginFillBox, dialMarginCircle), Sundial.DEFAULT_WIDTH, Sundial.DEFAULT_HEIGHT, true, SceneAntialiasing.DISABLED);
    }

    public static Arc createDialArcNight() {
        Arc dialArcNight = new Arc(Sunconfig.CENTER_X, Sunconfig.CENTER_Y, Sundial.DEFAULT_WIDTH / 2 - Sunconfig.MARGIN_X, Sundial.DEFAULT_HEIGHT / 2 - Sunconfig.MARGIN_Y, 90, 0);
        dialArcNight.setType(ArcType.ROUND);
        dialArcNight.setStroke(Sunconfig.Color_Of_Void);
        dialArcNight.setFill(Sunconfig.Color_Of_NightSky);
        return dialArcNight;
    }

    public static Arc createDialArcMidnight() {
        Arc dialArcMidnight = new Arc(Sunconfig.CENTER_X, Sunconfig.CENTER_Y, Sundial.DEFAULT_WIDTH / 2 - Sunconfig.MARGIN_X, Sundial.DEFAULT_HEIGHT / 2 - Sunconfig.MARGIN_Y, 0, -180);
        dialArcMidnight.setType(ArcType.ROUND);
        dialArcMidnight.setStroke(Sunconfig.Color_Of_Void);
        dialArcMidnight.setFill(Sunconfig.Color_Of_Midnight);
        return dialArcMidnight;
    }

    public static Circle createDialCircleBackground() {
        Circle dialCircleBackground = new Circle(Sunconfig.CENTER_X, Sunconfig.CENTER_Y, Sundial.DEFAULT_WIDTH / 2 - Sunconfig.MARGIN_X);
        dialCircleBackground.setFill(Sunconfig.Color_Of_DaySky);
        dialCircleBackground.setStroke(Sunconfig.Color_Of_Void);
        dialCircleBackground.setStyle(Sunconfig.MATRIX_SHADOW);
        return dialCircleBackground;
    }

    public static Circle createDialCircleFrame() {
        Circle dialCircleFrame = new Circle(Sunconfig.CENTER_X, Sunconfig.CENTER_Y, Sundial.DEFAULT_WIDTH / 2 - Sunconfig.MARGIN_X);
        dialCircleFrame.setFill(Sunconfig.FRAME_DIAL_NOMINAL);
        dialCircleFrame.setStroke(Sunconfig.Color_Of_Void);
        dialCircleFrame.setStrokeWidth(Sunconfig.MARKER_FRAME_STROKE_WIDTH);
        return dialCircleFrame;
    }

    public static Group createCetusMarkerGroup(
            Rotate centerRotate,
            ArrayList<Double> cetusMarkerAngleList,
            ArrayList<Rotate> cetusMarkerRotateList,
            ArrayList<Line> cetusMarkerLineList,
            ArrayList<Arc> cetusMarkerArcList,
            ArrayList<DotMatrix> cetusTimeMatrixList,
            ArrayList<Timeline> cetusMarkerHoverTransitionList) {

        Group cetusMarkerGroup = new Group();
        Group cetusHorizonGroup = new Group();
        Group cetusArcGroup = new Group();

        for (int i = 0; i <= Cetustime.CYCLES_PER_48h; i++) {

            double startAngle = (i * Cetustime.CYCLE_LENGTH * 360d) / (24d * 60 * 60 * 1000);
            double endAngle = ((i * Cetustime.CYCLE_LENGTH + Cetustime.NIGHT_LENGTH) * 360d) / (24d * 60 * 60 * 1000);

            Line markerLineStart = new Line(Sunconfig.CENTER_X, Sunconfig.CETUS_MARKER_LENGTH + Sunconfig.MARGIN_Y, Sunconfig.CENTER_X, Sunconfig.MARGIN_Y);
            markerLineStart.setStroke(Sunconfig.Color_Of_CetusMarker);
            markerLineStart.setStrokeWidth(Sunconfig.CETUS_MARKER_WIDTH);
            markerLineStart.setStyle(Sunconfig.CETUS_MARKER_SHADOW);
            markerLineStart.setMouseTransparent(true);

            Rotate markerLineStartRotate = centerRotate.clone();
            markerLineStartRotate.setAngle(startAngle);

            DotMatrix matrixStart = new DotMatrix("00:00", Sunconfig.Color_Of_CetusMarker);
            matrixStart.setScaleX(Sunconfig.CETUS_HORIZON_SCALE);
            matrixStart.setScaleY(Sunconfig.CETUS_HORIZON_SCALE);
            matrixStart.setLayoutX(Sunconfig.CENTER_X - matrixStart.getLayoutBounds().getWidth() / 2 - matrixStart.getLayoutBounds().getHeight() / 2);
            matrixStart.setLayoutY(Sunconfig.CETUS_HORIZON_OFFSET);
            matrixStart.setRotate(90d);
            matrixStart.setStyle(Sunconfig.CETUS_MARKER_SHADOW);
            matrixStart.setMouseTransparent(true);
            matrixStart.setOpacity(0);

            Group startHorizonGroup = new Group();
            startHorizonGroup.getChildren().addAll(markerLineStart, matrixStart);
            startHorizonGroup.getTransforms().add(markerLineStartRotate);

            Line markerLineEnd = new Line(Sunconfig.CENTER_X, Sunconfig.CETUS_MARKER_LENGTH + Sunconfig.MARGIN_Y, Sunconfig.CENTER_X, Sunconfig.MARGIN_Y);
            markerLineEnd.setStroke(Sunconfig.Color_Of_CetusMarker);
            markerLineEnd.setStrokeWidth(Sunconfig.CETUS_MARKER_WIDTH);
            markerLineEnd.setStyle(Sunconfig.CETUS_MARKER_SHADOW);
            markerLineEnd.setMouseTransparent(true);

            DotMatrix matrixEnd = new DotMatrix("00:00", Sunconfig.Color_Of_CetusMarker);
            matrixEnd.setScaleX(Sunconfig.CETUS_HORIZON_SCALE);
            matrixEnd.setScaleY(Sunconfig.CETUS_HORIZON_SCALE);
            matrixEnd.setTranslateX(Sunconfig.CENTER_X - matrixEnd.getLayoutBounds().getWidth() / 2 + matrixEnd.getLayoutBounds().getHeight() / 2);
            matrixEnd.setTranslateY(Sunconfig.CETUS_HORIZON_OFFSET);
            matrixEnd.setRotate(90d);
            matrixEnd.setStyle(Sunconfig.CETUS_MARKER_SHADOW);
            matrixEnd.setMouseTransparent(true);
            matrixEnd.setOpacity(0);

            Rotate markerLineEndRotate = centerRotate.clone();
            markerLineEndRotate.setAngle(endAngle);

            Group endHorizonGroup = new Group();
            endHorizonGroup.getChildren().addAll(markerLineEnd, matrixEnd);
            endHorizonGroup.getTransforms().add(markerLineEndRotate);

            Arc nightArc = new Arc(Sunconfig.CENTER_X, Sunconfig.CENTER_Y, Sunconfig.CENTER_X - Sunconfig.MARGIN_X, Sunconfig.CENTER_Y - Sunconfig.MARGIN_Y, 90 - startAngle, startAngle - endAngle);
            nightArc.setType(ArcType.ROUND);
            nightArc.setStroke(Sunconfig.Color_Of_Void);
            nightArc.setFill(Sunconfig.CETUS_ARC_GRADIENT);
            nightArc.setOpacity(Sunconfig.CETUS_ARC_OPACITY);
//            nightArc.setBlendMode(BlendMode.MULTIPLY);

            cetusArcGroup.getChildren().addAll(nightArc);
            cetusHorizonGroup.getChildren().addAll(startHorizonGroup, endHorizonGroup);

            Timeline cetusMarkerTransitionOn = new Timeline();
            cetusMarkerTransitionOn.setCycleCount(1);
            cetusMarkerTransitionOn.setRate(1);
            cetusMarkerTransitionOn.setAutoReverse(false);

            KeyValue keyValueStartOpacityOn = new KeyValue(matrixStart.opacityProperty(), 1.0, Interpolator.EASE_BOTH);
            KeyFrame keyFrameStartOpacityOn = new KeyFrame(Duration.millis(Sunconfig.CETUS_MARKER_DURATION), keyValueStartOpacityOn);
            KeyValue keyValueEndOpacityOn = new KeyValue(matrixEnd.opacityProperty(), 1.0, Interpolator.EASE_BOTH);
            KeyFrame keyFrameEndOpacityOn = new KeyFrame(Duration.millis(Sunconfig.CETUS_MARKER_DURATION), keyValueEndOpacityOn);
            KeyValue keyValueLineStartOn = new KeyValue(markerLineStart.startYProperty(), Sunconfig.CETUS_MARKER_LENGTH * 2, Interpolator.EASE_BOTH);
            KeyFrame keyFrameLineStartOn = new KeyFrame(Duration.millis(Sunconfig.CETUS_MARKER_DURATION), keyValueLineStartOn);
            KeyValue keyValueLineEndOn = new KeyValue(markerLineEnd.startYProperty(), Sunconfig.CETUS_MARKER_LENGTH * 2, Interpolator.EASE_BOTH);
            KeyFrame keyFrameLineEndOn = new KeyFrame(Duration.millis(Sunconfig.CETUS_MARKER_DURATION), keyValueLineEndOn);

            cetusMarkerTransitionOn.getKeyFrames().addAll(keyFrameStartOpacityOn, keyFrameEndOpacityOn, keyFrameLineStartOn, keyFrameLineEndOn);

            Timeline cetusMarkerTransitionOff = new Timeline();
            cetusMarkerTransitionOff.setCycleCount(1);
            cetusMarkerTransitionOff.setRate(1);
            cetusMarkerTransitionOff.setAutoReverse(false);

            KeyValue keyValueStartOpacityOff = new KeyValue(matrixStart.opacityProperty(), 0.0, Interpolator.EASE_BOTH);
            KeyFrame keyFrameStartOpacityOff = new KeyFrame(Duration.millis(Sunconfig.CETUS_MARKER_DURATION), keyValueStartOpacityOff);
            KeyValue keyValueEndOpacityOff = new KeyValue(matrixEnd.opacityProperty(), 0.0, Interpolator.EASE_BOTH);
            KeyFrame keyFrameEndOpacityOff = new KeyFrame(Duration.millis(Sunconfig.CETUS_MARKER_DURATION), keyValueEndOpacityOff);
            KeyValue keyValueLineStartOff = new KeyValue(markerLineStart.startYProperty(), Sunconfig.CETUS_MARKER_LENGTH + Sunconfig.MARGIN_Y, Interpolator.EASE_BOTH);
            KeyFrame keyFrameLineStartOff = new KeyFrame(Duration.millis(Sunconfig.CETUS_MARKER_DURATION), keyValueLineStartOff);
            KeyValue keyValueLineEndOff = new KeyValue(markerLineEnd.startYProperty(), Sunconfig.CETUS_MARKER_LENGTH + Sunconfig.MARGIN_Y, Interpolator.EASE_BOTH);
            KeyFrame keyFrameLineEndOff = new KeyFrame(Duration.millis(Sunconfig.CETUS_MARKER_DURATION), keyValueLineEndOff);

            cetusMarkerTransitionOff.getKeyFrames().addAll(keyFrameStartOpacityOff, keyFrameEndOpacityOff, keyFrameLineStartOff, keyFrameLineEndOff);

            nightArc.setOnMouseEntered(event -> cetusMarkerTransitionOn.play());
            nightArc.setOnMouseExited(event -> cetusMarkerTransitionOff.play());

            // these are sent back
            cetusMarkerAngleList.add(startAngle);
            cetusMarkerAngleList.add(endAngle);

            cetusMarkerRotateList.add(markerLineStartRotate);
            cetusMarkerRotateList.add(markerLineEndRotate);

            cetusMarkerLineList.add(markerLineStart);
            cetusMarkerLineList.add(markerLineEnd);

            cetusMarkerArcList.add(nightArc);

            cetusTimeMatrixList.add(matrixStart);
            cetusTimeMatrixList.add(matrixEnd);

            cetusMarkerHoverTransitionList.add(cetusMarkerTransitionOn);
            cetusMarkerHoverTransitionList.add(cetusMarkerTransitionOff);

        }

        cetusArcGroup.setBlendMode(BlendMode.MULTIPLY);

        cetusMarkerGroup.getChildren().addAll(cetusArcGroup, cetusHorizonGroup);

        return cetusMarkerGroup;
    }

    public static DotMatrix createCetusTimer() {
        DotMatrix cetusTimer = new DotMatrix("0h00m00s", Sunconfig.Color_Of_CetusNight);
        cetusTimer.setScaleX(Sunconfig.CETUS_TIMER_SCALE);
        cetusTimer.setScaleY(Sunconfig.CETUS_TIMER_SCALE);
        cetusTimer.setLayoutX(Sunconfig.CENTER_X - cetusTimer.getLayoutBounds().getWidth() / 2);
        cetusTimer.setLayoutY(Sunconfig.CETUS_TIMER_OFFSET);
        cetusTimer.setStyle(Sunconfig.CETUS_MATRIX_SHADOW_NIGHT);
        cetusTimer.setVisible(false);
        return cetusTimer;
    }

    public static Group createDialMinuteMarkers() {

        Group dialMinuteMarkers = new Group();
        for(int i = 0; i < 60; i++) {

            Group markerGroup = new Group();

            double opacity = Sunconfig.MARKER_MINUTE_OPACITY;

            Rectangle markerMinute = new Rectangle(Sunconfig.LOCALMINUTE_WIDTH, Sunconfig.LOCALMINUTE_HEIGHT);
            markerMinute.setArcWidth(Sunconfig.LOCALMINUTE_ROUND);
            markerMinute.setArcHeight(Sunconfig.LOCALMINUTE_ROUND);
            markerMinute.setTranslateX(Sunconfig.CENTER_X - Sunconfig.LOCALMINUTE_WIDTH / 2);
            markerMinute.setTranslateY(Sunconfig.LOCALMINUTE_OFFSET);
            markerMinute.setFill(Sunconfig.MINUTE_MARKER_GRADIENT);
            markerMinute.setOpacity(opacity);

            Polygon markerMinutePoly = new Polygon(
                    -1, Sunconfig.LOCALMINUTE_POLY_HEIGHT,
                    -Sunconfig.LOCALMINUTE_POLY_WIDTH, Sunconfig.LOCALMINUTE_POLY_WIDTH / 2,
                    -Sunconfig.LOCALMINUTE_POLY_WIDTH / 2, 0,
                    +Sunconfig.LOCALMINUTE_POLY_WIDTH / 2, 0,
                    +Sunconfig.LOCALMINUTE_POLY_WIDTH, Sunconfig.LOCALMINUTE_POLY_WIDTH / 2,
                    1, Sunconfig.LOCALMINUTE_POLY_HEIGHT
            );
            markerMinutePoly.setTranslateX(Sunconfig.CENTER_X);
            markerMinutePoly.setTranslateY(Sunconfig.LOCALMINUTE_OFFSET);
            markerMinutePoly.setFill(Sunconfig.MINUTE_POLY_GRADIENT);
            markerMinutePoly.setOpacity(opacity);

            Circle markerMinuteCircle = new Circle();
            markerMinuteCircle.setRadius(((i % 5) == 0) ? Sunconfig.LOCALSECOND_RADIUS_BIG : Sunconfig.LOCALSECOND_RADIUS_SMOL);
            markerMinuteCircle.setTranslateX(Sunconfig.CENTER_X);
            markerMinuteCircle.setTranslateY(Sunconfig.LOCALMINUTE_CIRCLE_OFFSET);
            markerMinuteCircle.setFill(Sunconfig.MINUTE_CIRCLE_GRADIENT);
            markerMinuteCircle.setOpacity(((i % 5) == 0) ? opacity * 2 : opacity);

            markerGroup.getChildren().add(markerMinuteCircle);

/*
            if (i % 5 == 0) {
                DotMatrix markerMatrix = new DotMatrix("" + i, Sunconfig.Color_Of_Darkness);
                markerMatrix.setTranslateX(Sunconfig.CENTER_X - markerMatrix.getLayoutBounds().getWidth() / 2);
                markerMatrix.setTranslateY(Sunconfig.LOCALMINUTE_OFFSET + Sunconfig.LOCALMINUTE_POLY_HEIGHT + Sunconfig.LOCALMINUTE_POLY_WIDTH);
                markerMatrix.setOpacity(Sunconfig.MATRIX_MINUTE_OPACITY);

                double rotationAdjust = i * -6;
                markerMatrix.setRotate(rotationAdjust);
                markerMatrix.setScaleX(Sunconfig.MATRIX_MINUTE_SCALE);
                markerMatrix.setScaleY(Sunconfig.MATRIX_MINUTE_SCALE);
                markerGroup.getChildren().add(markerMatrix);
            }
*/

            Rotate markerHourRotate = new Rotate();
            markerHourRotate.setPivotX(Sunconfig.CENTER_X);
            markerHourRotate.setPivotY(Sunconfig.CENTER_Y);
            markerHourRotate.setAngle(i * 360d / 60d);

            markerGroup.getTransforms().add(markerHourRotate);

            dialMinuteMarkers.getChildren().add(markerGroup);
        }

        dialMinuteMarkers.setBlendMode(BlendMode.COLOR_BURN);
        dialMinuteMarkers.setMouseTransparent(true);

        return dialMinuteMarkers;
    }

    public static void createDialHourMarkers(
            double nightCompression,
            Rotate centerRotate,
            Group dialHourMatrixMarkerGroup,
            ArrayList<DotMatrix> hourMarkerMatrixList,
            ArrayList<Line> dialHourLineMarkerList,
            ArrayList<Rotate> dialMarkerRotateList
            ) {

        for(int i = 0; i < Sunconfig.MAX_MARKER; i++) {

            double lineLength = Sunconfig.MARKER_HOUR_LENGTH * 0.50d;
            double strokeWidth = Sunconfig.MARKER_HOUR_STROKE_WIDTH;
            double opacity = 0.35d;

            if (i % 2 == 0) { lineLength = Sunconfig.MARKER_HOUR_LENGTH * (0.75d); opacity = 0.5d;}
            if (i % 4 == 0) { lineLength = Sunconfig.MARKER_HOUR_LENGTH; opacity = 1.0d; }

            Rotate markerRotate = centerRotate.clone();
            markerRotate.setAngle(Sunutil.getNightCompressionAngle(i * 360d / 96d, nightCompression));

            Line markerLine = new Line(Sunconfig.CENTER_X, lineLength  + Sunconfig.MARGIN_Y, Sunconfig.CENTER_X, Sunconfig.MARGIN_Y + 1);
            markerLine.setStroke(Sunconfig.Color_Of_Darkness);
            markerLine.setStrokeWidth(strokeWidth);
            markerLine.setOpacity(opacity);
            markerLine.getTransforms().add(markerRotate);
            markerLine.setMouseTransparent(true);

            if (i % 4 == 0) {

                Group matrixMarkerGroup = new Group();

                Line matrixMarkerLine = new Line(Sunconfig.CENTER_X, lineLength  + Sunconfig.MARGIN_Y, Sunconfig.CENTER_X, Sunconfig.MARGIN_Y + 1);
                matrixMarkerLine.setStroke(Sunconfig.Color_Of_Darkness);
                matrixMarkerLine.setVisible(false);

                DotMatrix markerMatrix = new DotMatrix("" + ((12 + i / 4) % 24), Sunconfig.Color_Of_LocalTime);
                markerMatrix.setTranslateX(Sunconfig.CENTER_X - markerMatrix.getLayoutBounds().getWidth() / 2);
                markerMatrix.setTranslateY(Sunconfig.MATRIX_MARKER_OFFSET);
                markerMatrix.setStyle(Sunconfig.MATRIX_SHADOW);

                double rotationAdjust = i * -3.75d;
                markerMatrix.setRotate(rotationAdjust);
                markerMatrix.setScaleX(Sunconfig.MATRIX_HOUR_SCALE);
                markerMatrix.setScaleY(Sunconfig.MATRIX_HOUR_SCALE);

                matrixMarkerGroup.getChildren().addAll(matrixMarkerLine, markerMatrix);
                matrixMarkerGroup.getTransforms().add(markerRotate);

                dialHourMatrixMarkerGroup.getChildren().add(matrixMarkerGroup);
                hourMarkerMatrixList.add(markerMatrix);
            }

            dialHourLineMarkerList.add(markerLine);
            dialMarkerRotateList.add(markerRotate);
        }
    }

    public static Circle createDialCircleCenterPoint() {
        Circle dialCircleCenterPoint = new Circle(Sunconfig.CENTER_X, Sunconfig.CENTER_Y, 1);
        dialCircleCenterPoint.setFill(Sunconfig.Color_Of_LocalTime);
        dialCircleCenterPoint.setStroke(Sunconfig.Color_Of_Void);
        return dialCircleCenterPoint;
    }

    public static Circle createControlNightCompression() {
        Circle controlNightCompression = new Circle(Sunconfig.CENTER_X, Sunconfig.CENTER_Y, Sunconfig.DOT_RADIUS);
        controlNightCompression.setFill(Sunconfig.Color_Of_LocalTime);
        controlNightCompression.setStroke(Sunconfig.Color_Of_Void);
        controlNightCompression.setStyle(Sunconfig.MATRIX_SHADOW2);
        return controlNightCompression;
    }

    public static Group creatDialHighNoonGroup(Rotate highNoonDialRotate) {

        Group dialHighNoonGroup = new Group();

        Polygon dialHighNoonPoly = new Polygon(
                Sunconfig.CENTER_X - Sunconfig.HIGHNOON_STROKE_WIDTH, Sunconfig.MARGIN_Y + Sunconfig.MARKER_HOUR_LENGTH,
                Sunconfig.CENTER_X - Sunconfig.HIGHNOON_DIAL_WIDTH / 2, Sunconfig.MARGIN_Y,
                Sunconfig.CENTER_X - Sunconfig.HIGHNOON_STROKE_WIDTH / 2, Sunconfig.MARGIN_Y / 2,
                Sunconfig.CENTER_X - Sunconfig.HIGHNOON_STROKE_WIDTH / 2, Sunconfig.MARGIN_Y + Sunconfig.MARKER_HOUR_LENGTH,
                Sunconfig.CENTER_X + Sunconfig.HIGHNOON_STROKE_WIDTH / 2, Sunconfig.MARGIN_Y + Sunconfig.MARKER_HOUR_LENGTH,
                Sunconfig.CENTER_X + Sunconfig.HIGHNOON_STROKE_WIDTH / 2, Sunconfig.MARGIN_Y / 2,
                Sunconfig.CENTER_X + Sunconfig.HIGHNOON_DIAL_WIDTH / 2, Sunconfig.MARGIN_Y,
                Sunconfig.CENTER_X + Sunconfig.HIGHNOON_STROKE_WIDTH, Sunconfig.MARGIN_Y + Sunconfig.MARKER_HOUR_LENGTH
        );
        dialHighNoonPoly.setFill(Sunconfig.Color_Of_HighNoon);
        dialHighNoonPoly.setStroke(Sunconfig.Color_Of_Void);

        Polygon dialHighNoonPolyBackground = new Polygon(
                Sunconfig.CENTER_X - Sunconfig.HIGHNOON_STROKE_WIDTH, Sunconfig.MARGIN_Y + Sunconfig.MARKER_HOUR_LENGTH,
                Sunconfig.CENTER_X - Sunconfig.HIGHNOON_DIAL_WIDTH / 2, Sunconfig.MARGIN_Y,
                Sunconfig.CENTER_X - Sunconfig.HIGHNOON_STROKE_WIDTH / 2, Sunconfig.MARGIN_Y / 2,
                Sunconfig.CENTER_X + Sunconfig.HIGHNOON_STROKE_WIDTH / 2, Sunconfig.MARGIN_Y / 2,
                Sunconfig.CENTER_X + Sunconfig.HIGHNOON_DIAL_WIDTH / 2, Sunconfig.MARGIN_Y,
                Sunconfig.CENTER_X + Sunconfig.HIGHNOON_STROKE_WIDTH, Sunconfig.MARGIN_Y + Sunconfig.MARKER_HOUR_LENGTH
        );
        dialHighNoonPolyBackground.setFill(Sunconfig.Color_Of_Void);
        dialHighNoonPolyBackground.setStroke(Sunconfig.Color_Of_Void);

        dialHighNoonGroup.getChildren().addAll(dialHighNoonPolyBackground, dialHighNoonPoly);
        dialHighNoonGroup.getTransforms().add(highNoonDialRotate);
        dialHighNoonGroup.setStyle(Sunconfig.MATRIX_GLOW);
        dialHighNoonGroup.setBlendMode(BlendMode.SCREEN);

        return dialHighNoonGroup;
    }

    public static Group createDialLocalHourGroup(Rotate dialRotateLocalHour) {

        Polygon dialLocalHourPoly = new Polygon(

                // outside
                - Sunconfig.LOCALTIME_HOUR_STROKE_WIDTH * 2, Sunconfig.LOCALTIME_DIAL_LENGTH,
                - Sunconfig.LOCALTIME_HOUR_WIDTH / 2, Sunconfig.LOCALTIME_DIAL_LENGTH * 0.75,
                - Sunconfig.LOCALTIME_HOUR_STROKE_WIDTH, Sunconfig.MARGIN_Y * 1.5,
                0, Sunconfig.MARGIN_Y/* + Sunconfig.LOCALTIME_HOUR_STROKE_WIDTH * 2*/,
                + Sunconfig.LOCALTIME_HOUR_STROKE_WIDTH, Sunconfig.MARGIN_Y * 1.5,
                + Sunconfig.LOCALTIME_HOUR_WIDTH / 2, Sunconfig.LOCALTIME_DIAL_LENGTH * 0.75,
                + Sunconfig.LOCALTIME_HOUR_STROKE_WIDTH * 2, Sunconfig.LOCALTIME_DIAL_LENGTH,

                // inside
                0, Sunconfig.LOCALTIME_DIAL_LENGTH * 0.40
        );
        dialLocalHourPoly.setTranslateX(Sunconfig.CENTER_X);
        dialLocalHourPoly.setFill(new Color(1, 1, 1, 0.1));
        dialLocalHourPoly.setStroke(Color.WHITE);
        dialLocalHourPoly.setStrokeWidth(Sunconfig.LOCALTIME_HOUR_STROKE_WIDTH);
        dialLocalHourPoly.setOpacity(1);

        Group dialLocalHourGroup = new Group(dialLocalHourPoly);
        dialLocalHourGroup.getTransforms().add(dialRotateLocalHour);
        dialLocalHourGroup.setStyle(Sunconfig.LOCALHOUR_DIAL_GLOW);
        dialLocalHourGroup.setBlendMode(BlendMode.SCREEN);
        dialLocalHourGroup.setMouseTransparent(true);

        return dialLocalHourGroup;
    }

    public static Group createDialLocalMinuteGroup(Rotate dialRotateLocalMinute) {

        Line localMinuteLine = new Line(
                0, Sunconfig.LOCALTIME_DIAL_LENGTH,
                0, Sunconfig.LOCALMINUTE_DIAL_OFFSET
        );
        localMinuteLine.setTranslateX(Sunconfig.CENTER_X);
        localMinuteLine.setStroke(Color.WHITE);
        localMinuteLine.setStrokeWidth(Sunconfig.LOCALTIME_MINUTE_STROKE_WIDTH);
        localMinuteLine.setOpacity(0.6);

/*
        Polygon dialLocalMinutePoly = new Polygon(
                - Sunconfig.LOCALTIME_MINUTE_STROKE_WIDTH, Sunconfig.LOCALTIME_DIAL_LENGTH,
                - Sunconfig.LOCALTIME_MINUTE_STROKE_WIDTH * 1.25, Sunconfig.LOCALTIME_DIAL_LENGTH,
                - Sunconfig.LOCALTIME_MINUTE_WIDTH / 2, Sunconfig.LOCALTIME_DIAL_LENGTH * 0.85,
                0, Sunconfig.LOCALMINUTE_DIAL_OFFSET,
                + Sunconfig.LOCALTIME_MINUTE_WIDTH / 2, Sunconfig.LOCALTIME_DIAL_LENGTH * 0.85,
                + Sunconfig.LOCALTIME_MINUTE_STROKE_WIDTH * 1.25, Sunconfig.LOCALTIME_DIAL_LENGTH,
                + Sunconfig.LOCALTIME_MINUTE_STROKE_WIDTH, Sunconfig.LOCALTIME_DIAL_LENGTH
        );
        dialLocalMinutePoly.setTranslateX(Sunconfig.CENTER_X);
        dialLocalMinutePoly.setFill(Sunconfig.Color_Of_Void);
        dialLocalMinutePoly.setStroke(Color.WHITE);
        dialLocalMinutePoly.setStrokeWidth(Sunconfig.LOCALTIME_MINUTE_STROKE_WIDTH);
        dialLocalMinutePoly.setOpacity(1);
*/

        Group dialLocalMinuteGroup = new Group(localMinuteLine);
        dialLocalMinuteGroup.getTransforms().add(dialRotateLocalMinute);
        dialLocalMinuteGroup.setStyle(Sunconfig.LOCALMINUTE_DIAL_GLOW);
        dialLocalMinuteGroup.setMouseTransparent(true);

        return dialLocalMinuteGroup;
    }

    public static Group createDialLocalSecondGroup(Rotate dialRotateLocalSecond) {

        Line localSecondLine = new Line(
                0, Sunconfig.LOCALTIME_DIAL_LENGTH,
                0, Sunconfig.LOCALMINUTE_DIAL_OFFSET
        );
        localSecondLine.setTranslateX(Sunconfig.CENTER_X);
        localSecondLine.setStroke(Color.WHITE);
        localSecondLine.setStrokeWidth(Sunconfig.LOCALTIME_MINUTE_STROKE_WIDTH);
        localSecondLine.setOpacity(0.6);

        Group dialLocalSecondGroup = new Group(localSecondLine);
        dialLocalSecondGroup.getTransforms().add(dialRotateLocalSecond);
        dialLocalSecondGroup.setStyle(Sunconfig.LOCALSECOND_DIAL_GLOW);
        dialLocalSecondGroup.setMouseTransparent(true);

        return dialLocalSecondGroup;
    }

    public static void createLEDs(
            ArrayList<Node> dialLocalSecondLedList,
            ArrayList<Boolean> dialLocalSecondOn,
            ArrayList<Node> dialLocalMinuteLedList,
            ArrayList<Boolean> dialLocalMinuteOn,
            ArrayList<Timeline> dialLocalSecondLedTransitionList,
            ArrayList<Timeline> dialLocalMinuteLedTransitionList
    ) {

        for (int i = 0; i < 60; i++) {

/*
            Rectangle localSecond = new Rectangle(Sunconfig.LOCALSECOND_WIDTH, Sunconfig.LOCALSECOND_HEIGHT);
            localSecond.setArcWidth(Sunconfig.LOCALSECOND_ROUND);
            localSecond.setArcHeight(Sunconfig.LOCALSECOND_ROUND);
            localSecond.setTranslateX(Sunconfig.CENTER_X - Sunconfig.LOCALSECOND_WIDTH / 2);
            localSecond.setTranslateY(Sunconfig.LOCALSECOND_OFFSET);
            localSecond.setFill(Sunconfig.Color_Of_Seconds);
            localSecond.setStroke(Sunconfig.Color_Of_Void);
            localSecond.setStyle(Sunconfig.LOCALSECOND_GLOW);
            localSecond.setMouseTransparent(true);

            Polygon localSecondPoly = new Polygon(
                    -1, Sunconfig.LOCALMINUTE_POLY_HEIGHT,
                    -Sunconfig.LOCALMINUTE_POLY_WIDTH, Sunconfig.LOCALMINUTE_POLY_WIDTH / 2,
                    -Sunconfig.LOCALMINUTE_POLY_WIDTH / 2, 0,
                    +Sunconfig.LOCALMINUTE_POLY_WIDTH / 2, 0,
                    +Sunconfig.LOCALMINUTE_POLY_WIDTH, Sunconfig.LOCALMINUTE_POLY_WIDTH / 2,
                    1, Sunconfig.LOCALMINUTE_POLY_HEIGHT
            );
            localSecondPoly.setTranslateX(Sunconfig.CENTER_X);
            localSecondPoly.setTranslateY(Sunconfig.LOCALMINUTE_OFFSET);
            localSecondPoly.setFill(Sunconfig.Color_Of_Seconds);
            localSecondPoly.setStroke(Sunconfig.Color_Of_Void);
            localSecondPoly.setStyle(Sunconfig.LOCALSECOND_GLOW);
            localSecondPoly.setMouseTransparent(true);
*/

            Circle localSecondCircle = new Circle();
            localSecondCircle.setRadius(((i % 5) == 0) ? Sunconfig.LOCALSECOND_RADIUS_BIG : Sunconfig.LOCALSECOND_RADIUS_SMOL);
            localSecondCircle.setTranslateX(Sunconfig.CENTER_X);
            localSecondCircle.setTranslateY(Sunconfig.LOCALMINUTE_CIRCLE_OFFSET);
            localSecondCircle.setFill(Sunconfig.Color_Of_Seconds);
            localSecondCircle.setStroke(Sunconfig.Color_Of_Void);
            localSecondCircle.setStyle(Sunconfig.LOCALSECOND_GLOW);
            localSecondCircle.setMouseTransparent(true);

            Rotate localSecondRotate = new Rotate();
            localSecondRotate.setPivotX(Sunconfig.CENTER_X);
            localSecondRotate.setPivotY(Sunconfig.CENTER_Y);
            localSecondRotate.setAngle(i * 6);

            Group secondGroup = new Group(localSecondCircle);
            secondGroup.setOpacity(0.0);
            secondGroup.getTransforms().add(localSecondRotate);
            secondGroup.setBlendMode(BlendMode.SCREEN);

/*
            Rectangle localMinute = new Rectangle(Sunconfig.LOCALMINUTE_WIDTH, Sunconfig.LOCALMINUTE_HEIGHT);
            localMinute.setArcWidth(Sunconfig.LOCALMINUTE_ROUND);
            localMinute.setArcHeight(Sunconfig.LOCALMINUTE_ROUND);
            localMinute.setTranslateX(Sunconfig.CENTER_X - Sunconfig.LOCALMINUTE_WIDTH / 2);
            localMinute.setTranslateY(Sunconfig.LOCALMINUTE_OFFSET);
            localMinute.setFill(Sunconfig.Color_Of_Minutes);
            localMinute.setStroke(Sunconfig.Color_Of_Void);
            localMinute.setStyle(Sunconfig.LOCALMINUTE_GLOW);
            localMinute.setMouseTransparent(true);

            Polygon localMinutePoly = new Polygon(
                    -1, Sunconfig.LOCALMINUTE_POLY_HEIGHT,
                    -Sunconfig.LOCALMINUTE_POLY_WIDTH, Sunconfig.LOCALMINUTE_POLY_WIDTH / 2,
                    -Sunconfig.LOCALMINUTE_POLY_WIDTH / 2, 0,
                    +Sunconfig.LOCALMINUTE_POLY_WIDTH / 2, 0,
                    +Sunconfig.LOCALMINUTE_POLY_WIDTH, Sunconfig.LOCALMINUTE_POLY_WIDTH / 2,
                    1, Sunconfig.LOCALMINUTE_POLY_HEIGHT
            );
            localMinutePoly.setTranslateX(Sunconfig.CENTER_X);
            localMinutePoly.setTranslateY(Sunconfig.LOCALMINUTE_OFFSET);
            localMinutePoly.setFill(Sunconfig.Color_Of_Minutes);
            localMinutePoly.setStroke(Sunconfig.Color_Of_Void);
            localMinutePoly.setStyle(Sunconfig.LOCALMINUTE_GLOW);
            localMinutePoly.setMouseTransparent(true);
*/

            Circle localMinuteCircle = new Circle();
            localMinuteCircle.setRadius(((i % 5) == 0) ? Sunconfig.LOCALSECOND_RADIUS_BIG : Sunconfig.LOCALSECOND_RADIUS_SMOL);
            localMinuteCircle.setTranslateX(Sunconfig.CENTER_X);
            localMinuteCircle.setTranslateY(Sunconfig.LOCALMINUTE_CIRCLE_OFFSET);
            localMinuteCircle.setFill(Sunconfig.Color_Of_Void);
            localMinuteCircle.setStroke(Sunconfig.Color_Of_Minutes);
            localMinuteCircle.setStyle(Sunconfig.LOCALMINUTE_GLOW);
            localMinuteCircle.setMouseTransparent(true);

            Rotate localMinuteRotate = new Rotate();
            localMinuteRotate.setPivotX(Sunconfig.CENTER_X);
            localMinuteRotate.setPivotY(Sunconfig.CENTER_Y);
            localMinuteRotate.setAngle(i * 6);

            Group minuteLedGroup = new Group(localMinuteCircle);
            minuteLedGroup.setOpacity(0.0);
            minuteLedGroup.getTransforms().add(localMinuteRotate);
//            minuteLedGroup.setBlendMode(BlendMode.SCREEN);

            dialLocalSecondLedList.add(secondGroup);
            dialLocalMinuteLedList.add(minuteLedGroup);

            dialLocalSecondOn.add(false);
            dialLocalMinuteOn.add(false);

            dialLocalSecondLedTransitionList.add(Sunutil.createTimelineForLED(secondGroup, Sunconfig.LED_OPACITY_DURATION));
            dialLocalMinuteLedTransitionList.add(Sunutil.createTimelineForLED(minuteLedGroup, Sunconfig.LED_OPACITY_DURATION));
        }
    }

    public static DotMatrix createMatrixSunrise() {
        DotMatrix matrixSunrise = new DotMatrix("00:00", Sunconfig.Color_Of_Horizon);
        matrixSunrise.setTranslateX(Sunconfig.CENTER_X - matrixSunrise.getLayoutBounds().getWidth() / 2 + matrixSunrise.getLayoutBounds().getHeight() / 2 + Sunconfig.MATRIX_HORIZON_SLIDE);
        matrixSunrise.setTranslateY(Sunconfig.MATRIX_HORIZON_OFFSET);
        matrixSunrise.setRotate(90d);
        matrixSunrise.setScaleX(Sunconfig.MATRIX_HORIZON_SCALE);
        matrixSunrise.setScaleY(Sunconfig.MATRIX_HORIZON_SCALE);
        matrixSunrise.setStyle(Sunconfig.MATRIX_GLOW2);
        return matrixSunrise;
    }

    public static DotMatrix createMatrixSunset() {
        DotMatrix matrixSunset = new DotMatrix("00:00", Sunconfig.Color_Of_Horizon);
        matrixSunset.setTranslateX(Sunconfig.CENTER_X - matrixSunset.getLayoutBounds().getWidth() / 2 - matrixSunset.getLayoutBounds().getHeight() / 2 - Sunconfig.MATRIX_HORIZON_SLIDE);
        matrixSunset.setTranslateY(Sunconfig.MATRIX_HORIZON_OFFSET);
        matrixSunset.setRotate(-90d);
        matrixSunset.setScaleX(Sunconfig.MATRIX_HORIZON_SCALE);
        matrixSunset.setScaleY(Sunconfig.MATRIX_HORIZON_SCALE);
        matrixSunset.setStyle(Sunconfig.MATRIX_GLOW2);
        return matrixSunset;
    }

    public static Group createSunriseGroup(Rotate sunriseDialRotate, DotMatrix matrixSunrise) {

        Group sunriseGroup = new Group();

        Line sunriseDial = new Line(Sunconfig.CENTER_X, Sunconfig.SUNRISE_DIAL_LENGTH, Sunconfig.CENTER_X, Sunconfig.MARGIN_Y);
        sunriseDial.setStroke(Sunconfig.Color_Of_Horizon);
        sunriseDial.setStrokeWidth(Sunconfig.SUNRISE_STROKE_WIDTH);
        sunriseDial.setStyle(Sunconfig.HORIZON_GLOW);

        sunriseGroup.getChildren().addAll(sunriseDial, matrixSunrise);
        sunriseGroup.getTransforms().add(sunriseDialRotate);

        return sunriseGroup;
    }

    public static Group createSunsetGroup(Rotate sunsetDialRotate, DotMatrix matrixSunset) {

        Group sunsetGroup = new Group();

        Line sunsetDial = new Line(Sunconfig.CENTER_X, Sunconfig.SUNSET_DIAL_LENGTH, Sunconfig.CENTER_X, Sunconfig.MARGIN_Y);
        sunsetDial.setStroke(Sunconfig.Color_Of_Horizon);
        sunsetDial.setStrokeWidth(Sunconfig.SUNSET_STROKE_WIDTH);
        sunsetDial.setStyle(Sunconfig.HORIZON_GLOW);

        sunsetGroup.getChildren().addAll(sunsetDial, matrixSunset);
        sunsetGroup.getTransforms().add(sunsetDialRotate);

        return sunsetGroup;
    }

    public static Group createMatrixDate(DotMatrix matrixDay, DotMatrix matrixMonth, DotMatrix matrixYear, DotMatrix matrixWeek) {

        matrixDay.setStyle(Sunconfig.MATRIX_SHADOW);
        matrixMonth.setStyle(Sunconfig.MATRIX_SHADOW);
        matrixYear.setStyle(Sunconfig.MATRIX_SHADOW);
        matrixWeek.setStyle(Sunconfig.MATRIX_SHADOW);

        DotMatrix matrixSeparatorDayToMonth = new DotMatrix(".", Sunconfig.Color_Of_LocalTime);
        matrixSeparatorDayToMonth.setTranslateX(matrixDay.getLayoutBounds().getWidth() + Sunconfig.MATRIX_SEPARATOR_OFFSET);
        matrixSeparatorDayToMonth.setStyle(Sunconfig.MATRIX_SHADOW);

        matrixMonth.setTranslateX(matrixSeparatorDayToMonth.getLayoutBounds().getWidth() + matrixSeparatorDayToMonth.getTranslateX() + Sunconfig.MATRIX_SEPARATOR_OFFSET);

        DotMatrix matrixSeparatorMonthToYear = new DotMatrix(".", Sunconfig.Color_Of_LocalTime);
        matrixSeparatorMonthToYear.setTranslateX(matrixMonth.getLayoutBounds().getWidth() + matrixMonth.getTranslateX() + Sunconfig.MATRIX_SEPARATOR_OFFSET);
        matrixSeparatorMonthToYear.setStyle(Sunconfig.MATRIX_SHADOW);

        matrixYear.setTranslateX(matrixSeparatorMonthToYear.getLayoutBounds().getWidth() + matrixSeparatorMonthToYear.getTranslateX() + Sunconfig.MATRIX_SEPARATOR_OFFSET);

        matrixWeek.setScaleX(Sunconfig.MATRIX_WEEK_SCALE);
        matrixWeek.setScaleY(Sunconfig.MATRIX_WEEK_SCALE);
        matrixWeek.setLayoutX(Sunconfig.CENTER_X - matrixWeek.getLayoutBounds().getWidth() / 2);
        matrixWeek.setLayoutY(Sunconfig.CENTER_Y - matrixWeek.getLayoutBounds().getHeight() / 2 + Sunconfig.MATRIX_WEEK_OFFSET);

        Group matrixDate = new Group();
        matrixDate.getChildren().addAll(matrixDay, matrixSeparatorDayToMonth, matrixMonth, matrixSeparatorMonthToYear, matrixYear);
        matrixDate.setScaleX(Sunconfig.MATRIX_DATE_SCALE);
        matrixDate.setScaleY(Sunconfig.MATRIX_DATE_SCALE);
        matrixDate.setLayoutX(Sunconfig.CENTER_X - matrixDate.getLayoutBounds().getWidth() / 2);
        matrixDate.setLayoutY(Sunconfig.CENTER_Y - matrixDate.getLayoutBounds().getHeight() / 2 + Sunconfig.MATRIX_DATE_OFFSET);

        return matrixDate;
    }

    public static Group createMatrixTime(DotMatrix matrixHour, DotMatrix matrixMinute, DotMatrix matrixSecond) {

        matrixHour.setStyle(Sunconfig.MATRIX_SHADOW);
        matrixMinute.setStyle(Sunconfig.MATRIX_SHADOW);
        matrixSecond.setStyle(Sunconfig.MATRIX_SHADOW);

        DotMatrix matrixSeparatorHourToMinute = new DotMatrix(":", Sunconfig.Color_Of_LocalTime);
        matrixSeparatorHourToMinute.setTranslateX(matrixHour.getLayoutBounds().getWidth() + Sunconfig.MATRIX_SEPARATOR_OFFSET);
        matrixSeparatorHourToMinute.setStyle(Sunconfig.MATRIX_SHADOW);

        matrixMinute.setTranslateX(matrixSeparatorHourToMinute.getLayoutBounds().getWidth() + matrixSeparatorHourToMinute.getTranslateX()/* + Sunconfig.MATRIX_SEPARATOR_OFFSET*/);

        DotMatrix matrixSeparatorMinuteToSecond = new DotMatrix(":", Sunconfig.Color_Of_LocalTime);
        matrixSeparatorMinuteToSecond.setTranslateX(matrixMinute.getLayoutBounds().getWidth() + matrixMinute.getTranslateX() + Sunconfig.MATRIX_SEPARATOR_OFFSET);
        matrixSeparatorMinuteToSecond.setStyle(Sunconfig.MATRIX_SHADOW);

        matrixSecond.setTranslateX(matrixSeparatorMinuteToSecond.getLayoutBounds().getWidth() + matrixSeparatorMinuteToSecond.getTranslateX() + Sunconfig.MATRIX_SEPARATOR_OFFSET);

        Group matrixTime = new Group();
        matrixTime.getChildren().addAll(matrixHour, matrixMinute);
        matrixTime.setScaleX(Sunconfig.MATRIX_TIME_SCALE);
        matrixTime.setScaleY(Sunconfig.MATRIX_TIME_SCALE);
        matrixTime.setLayoutX(Sunconfig.CENTER_X - matrixTime.getLayoutBounds().getWidth() / 2);
        matrixTime.setLayoutY(Sunconfig.CENTER_Y - matrixTime.getLayoutBounds().getHeight() / 2 + Sunconfig.MATRIX_TIME_OFFSET);

        return matrixTime;
    }

    public static Arc createDialArcDayLength() {
        Arc dialArcDayLength = new Arc(Sunconfig.CENTER_X, Sunconfig.CENTER_Y, Sunconfig.DAYLENGTH_ARC_RADIUS, Sunconfig.DAYLENGTH_ARC_RADIUS, 90, 360);
        dialArcDayLength.setType(ArcType.OPEN);
        dialArcDayLength.setStroke(Sunconfig.Color_Of_LocalTime);
        dialArcDayLength.setStrokeWidth(Sunconfig.DAYLENGTH_STROKE_WIDTH);
        dialArcDayLength.setFill(Sunconfig.Color_Of_Void);
        dialArcDayLength.setOpacity(Sunconfig.DAYLENGTH_ARC_OPACITY);
        dialArcDayLength.setMouseTransparent(true);
        return dialArcDayLength;
    }

    public static DotMatrix createMatrixDayLength() {
        DotMatrix matrixDayLength = new DotMatrix("00h00m00s", Sunconfig.Color_Of_LocalTime);
        matrixDayLength.setScaleX(Sunconfig.MATRIX_DAYLENGTH_SCALE);
        matrixDayLength.setScaleY(Sunconfig.MATRIX_DAYLENGTH_SCALE);
        matrixDayLength.setLayoutX(Sunconfig.CENTER_X - matrixDayLength.getLayoutBounds().getWidth() / 2);
        matrixDayLength.setLayoutY(Sunconfig.CENTER_Y + matrixDayLength.getLayoutBounds().getHeight() - Sunconfig.DAYLENGTH_ARC_RADIUS * 0.95);
        matrixDayLength.setStyle(Sunconfig.LOCALTIME_SHADOW);
        return matrixDayLength;
    }

    public static DotMatrix createMatrixLongitude() {

        DotMatrix matrixLongitude = new DotMatrix("000.00E", Sunconfig.Color_Of_LocalTime);
        matrixLongitude.setScaleX(Sunconfig.MATRIX_LONGITUDE_SCALE);
        matrixLongitude.setScaleY(Sunconfig.MATRIX_LONGITUDE_SCALE);
        matrixLongitude.setLayoutX(Sunconfig.CENTER_X + Sunconfig.MATRIX_LONGITUDE_SLIDE - matrixLongitude.getLayoutBounds().getWidth() / 2);
        matrixLongitude.setLayoutY(Sunconfig.CENTER_Y + matrixLongitude.getLayoutBounds().getHeight() + Sunconfig.MATRIX_LONGITUDE_OFFSET);
        matrixLongitude.setStyle(Sunconfig.MATRIX_SHADOW);

        Rectangle longitudeBackdrop = new Rectangle(
                matrixLongitude.getLayoutBounds().getMinX(),
                matrixLongitude.getLayoutBounds().getMinY(),
                matrixLongitude.getLayoutBounds().getWidth(),
                matrixLongitude.getLayoutBounds().getHeight());
        longitudeBackdrop.setOpacity(0);

        matrixLongitude.getChildren().add(longitudeBackdrop);
        matrixLongitude.setVisible(false);

        return matrixLongitude;
    }

    public static DotMatrix createMatrixLatitude() {

        DotMatrix matrixLatitude = new DotMatrix("000.00N", Sunconfig.Color_Of_LocalTime);
        matrixLatitude.setScaleX(Sunconfig.MATRIX_LATITUDE_SCALE);
        matrixLatitude.setScaleY(Sunconfig.MATRIX_LATITUDE_SCALE);
        matrixLatitude.setLayoutX(Sunconfig.CENTER_X + Sunconfig.MATRIX_LATITUDE_SLIDE - matrixLatitude.getLayoutBounds().getWidth() / 2);
        matrixLatitude.setLayoutY(Sunconfig.CENTER_Y + matrixLatitude.getLayoutBounds().getHeight() + Sunconfig.MATRIX_LATITUDE_OFFSET);
        matrixLatitude.setStyle(Sunconfig.MATRIX_SHADOW);

        Rectangle latitudeBackdrop = new Rectangle(
                matrixLatitude.getLayoutBounds().getMinX(),
                matrixLatitude.getLayoutBounds().getMinY(),
                matrixLatitude.getLayoutBounds().getWidth(),
                matrixLatitude.getLayoutBounds().getHeight());
        latitudeBackdrop.setOpacity(0);

        matrixLatitude.getChildren().add(latitudeBackdrop);
        matrixLatitude.setVisible(false);

        return matrixLatitude;
    }

    public static DotMatrix createMatrixHighNoon() {
        DotMatrix matrixHighNoon = new DotMatrix("00:00:00", Sunconfig.Color_Of_HighNoon);
        matrixHighNoon.setScaleX(Sunconfig.MATRIX_HIGHNOON_SCALE);
        matrixHighNoon.setScaleY(Sunconfig.MATRIX_HIGHNOON_SCALE);
        matrixHighNoon.setLayoutX(Sunconfig.CENTER_X - matrixHighNoon.getLayoutBounds().getWidth() / 2);
        matrixHighNoon.setLayoutY(Sunconfig.CENTER_Y - matrixHighNoon.getLayoutBounds().getHeight() * 1.5d - Sunconfig.DAYLENGTH_ARC_RADIUS * 1.1);
        matrixHighNoon.setStyle(Sunconfig.MATRIX_GLOW);
        matrixHighNoon.setMouseTransparent(true);
        matrixHighNoon.setVisible(false);
        return matrixHighNoon;
    }

    public static DotMatrix createMatrixTimeZone() {
        DotMatrix matrixTimeZone = new DotMatrix("GMT+00", Sunconfig.Color_Of_LocalTime);
        matrixTimeZone.setScaleX(Sunconfig.MATRIX_TIMEZONE_SCALE);
        matrixTimeZone.setScaleY(Sunconfig.MATRIX_TIMEZONE_SCALE);
        matrixTimeZone.setLayoutX(Sunconfig.CENTER_X - matrixTimeZone.getLayoutBounds().getWidth() / 2);
        matrixTimeZone.setLayoutY(Sunconfig.MATRIX_TIMEZONE_OFFSET);
        matrixTimeZone.setStyle(Sunconfig.MATRIX_SHADOW);
        matrixTimeZone.setVisible(false);
        return matrixTimeZone;
    }

    public static Group createNightModeOverlay() {

        Group nightModeOverlay = new Group();

        Rectangle nightModeRectangle = new Rectangle(Sundial.DEFAULT_WIDTH, Sundial.DEFAULT_HEIGHT);
        nightModeRectangle.setArcWidth(Sunconfig.NIGHTMODE_RECTANGLE_ROUND);
        nightModeRectangle.setArcHeight(Sunconfig.NIGHTMODE_RECTANGLE_ROUND);
        nightModeRectangle.setFill(Color.BLACK);
        nightModeRectangle.setStroke(Sunconfig.Color_Of_Void);
        nightModeRectangle.setBlendMode(BlendMode.BLUE);
        nightModeRectangle.setOpacity(Sunconfig.NIGHTMODE_RECTANGLE_OPACITY);
        nightModeRectangle.setMouseTransparent(true);

        Circle nightModeCircle = new Circle(Sunconfig.CENTER_X, Sunconfig.CENTER_Y, Sunconfig.CENTER_X);
        nightModeCircle.setFill(Color.BLACK);
        nightModeCircle.setStroke(Sunconfig.Color_Of_Void);
        nightModeCircle.setBlendMode(BlendMode.BLUE);
        nightModeCircle.setOpacity(Sunconfig.NIGHTMODE_RECTANGLE_OPACITY);
        nightModeCircle.setMouseTransparent(true);

        nightModeOverlay.getChildren().addAll(nightModeCircle);
        nightModeOverlay.setVisible(false);

        return nightModeOverlay;
    }

    public static Group createHelpTextGroup(Text helpText) {

        helpText.setFont(new Font(12));
        helpText.setStroke(Color.WHITE);
        helpText.setFill(Sunconfig.Color_Of_Void);
        helpText.setText(Sunconfig.HELPTEXT_DEFAULT);
        helpText.setTranslateX(5);
        helpText.setTranslateY(15);

        Rectangle helpTextRectangle = new Rectangle(0, 0, 20, 20);
        helpTextRectangle.setArcWidth(10);
        helpTextRectangle.setArcHeight(10);
        helpTextRectangle.setStroke(Sunconfig.Color_Of_Void);
        helpTextRectangle.setFill(Color.BLACK);
        helpTextRectangle.setOpacity(0.50);

        helpTextRectangle.widthProperty().bind(Bindings.createDoubleBinding(() -> {
            double size = helpText.layoutBoundsProperty().get().getWidth();
            return size + 10;
        }, helpText.layoutBoundsProperty()));

        helpTextRectangle.heightProperty().bind(Bindings.createDoubleBinding(() -> {
            double size = helpText.layoutBoundsProperty().get().getHeight();
            return size + 5;
        }, helpText.layoutBoundsProperty()));

        Group helpTextGroup = new Group(helpTextRectangle, helpText);
        helpTextGroup.setMouseTransparent(true);
        helpTextGroup.setVisible(false);

        return helpTextGroup;
    }

    public static Group createInfoTextGroup(Text infoText) {

        infoText.setFont(new Font(12));
        infoText.setStroke(Color.WHITE);
        infoText.setFill(Sunconfig.Color_Of_Void);
        infoText.setText(Sunconfig.HELPTEXT_DEFAULT);
        infoText.setTranslateX(5);
        infoText.setTranslateY(15);

        Rectangle infoTextRectangle = new Rectangle(0, 0, 20, 20);
        infoTextRectangle.setArcWidth(10);
        infoTextRectangle.setArcHeight(10);
        infoTextRectangle.setStroke(Sunconfig.Color_Of_Void);
        infoTextRectangle.setFill(Color.BLACK);
        infoTextRectangle.setOpacity(0.50);

        infoTextRectangle.widthProperty().bind(Bindings.createDoubleBinding(() -> {
            double size = infoText.layoutBoundsProperty().get().getWidth();
            return size + 10;
        }, infoText.layoutBoundsProperty()));

        infoTextRectangle.heightProperty().bind(Bindings.createDoubleBinding(() -> {
            double size = infoText.layoutBoundsProperty().get().getHeight();
            return size + 5;
        }, infoText.layoutBoundsProperty()));

        Group infoTextGroup = new Group(infoTextRectangle, infoText);
        infoTextGroup.setMouseTransparent(true);
        infoTextGroup.setVisible(false);

        return infoTextGroup;
    }

    public static Group createHelpOverlay(ArrayList<Group> helpMarkers, Group globeMasterGroup) {

        Group helpOverlay = new Group();

        Rectangle helpBackdrop = new Rectangle(Sundial.DEFAULT_WIDTH, Sundial.DEFAULT_HEIGHT);
        helpBackdrop.setArcWidth(Sunconfig.HELP_OVERLAY_ROUND);
        helpBackdrop.setArcHeight(Sunconfig.HELP_OVERLAY_ROUND);
        helpBackdrop.setFill(Color.BLACK);
        helpBackdrop.setStroke(Sunconfig.Color_Of_Void);
        helpBackdrop.setBlendMode(BlendMode.MULTIPLY);
        helpBackdrop.setOpacity(Sunconfig.HELP_OVERLAY_OPACITY);
        helpBackdrop.setMouseTransparent(true);

        Circle helpWindowMarker = new Circle(Sunconfig.CENTER_X, Sunconfig.CENTER_Y, Sundial.DEFAULT_WIDTH / 2 - 1);
        helpWindowMarker.setFill(Sunconfig.Color_Of_Void);
        helpWindowMarker.setStroke(Color.WHITE);
        helpWindowMarker.setStyle(Sunconfig.HELP_MARKER_GLOW);
        helpWindowMarker.setMouseTransparent(true);

        Circle helpGlobeMarker = new Circle(Sunconfig.CENTER_X, Sunconfig.CENTER_Y, Sundial.DEFAULT_WIDTH / 2 - Sunconfig.MARGIN_X);
        helpGlobeMarker.setFill(Sunconfig.Color_Of_Void);
        helpGlobeMarker.setStroke(Color.WHITE);
        helpGlobeMarker.setStyle(Sunconfig.HELP_MARKER_GLOW);
        helpGlobeMarker.setMouseTransparent(true);
        helpGlobeMarker.visibleProperty().bind(globeMasterGroup.visibleProperty());

        helpOverlay.getChildren().addAll(helpBackdrop, helpWindowMarker, helpGlobeMarker);
        helpOverlay.getChildren().addAll(helpMarkers);
        helpOverlay.setVisible(false);

        return helpOverlay;
    }

    public static Group createHelpMarker(double centerX, double centerY, Node node) {

        double sizeX = node.getLocalToSceneTransform().getMxx() * node.getLayoutBounds().getWidth() + Sunconfig.HELP_MARKER_MARGIN;
        double sizeY = node.getLocalToSceneTransform().getMyy() * node.getLayoutBounds().getHeight() + Sunconfig.HELP_MARKER_MARGIN;

        Rectangle rectangle = new Rectangle(sizeX, sizeY);
        rectangle.setArcWidth(Sunconfig.HELP_MARKER_ROUND);
        rectangle.setArcHeight(Sunconfig.HELP_MARKER_ROUND);
        rectangle.setFill(Sunconfig.Color_Of_Void);
        rectangle.setStroke(Color.WHITE);
        rectangle.setStyle(Sunconfig.HELP_MARKER_GLOW);
        rectangle.setTranslateX(-rectangle.getWidth() / 2);
        rectangle.setTranslateY(-rectangle.getHeight() / 2);

        Circle circle = new Circle(Sunconfig.HELP_MARKER_RADIUS);
        circle.setFill(Sunconfig.Color_Of_Void);
        circle.setStroke(Color.WHITE);
        circle.setStyle(Sunconfig.HELP_MARKER_GLOW);

        Group marker = new Group(rectangle, circle);
        marker.setMouseTransparent(true);

        marker.setTranslateX(centerX);
        marker.setTranslateY(centerY);

        marker.visibleProperty().bind(node.visibleProperty());

        return marker;
    }

    public static Group createHelpMarkerGroup(double offsetX, double offsetY, Node node) {

        Group markerGroup = new Group(createHelpMarker(offsetX, offsetY, node));

        markerGroup.translateXProperty().bind(Bindings.createDoubleBinding(() -> {
            double t = node.localToSceneTransformProperty().get().getTx();
            return t;
        }, node.localToSceneTransformProperty(), node.layoutBoundsProperty()));

        markerGroup.translateYProperty().bind(Bindings.createDoubleBinding(() -> {
            double t = node.localToSceneTransformProperty().get().getTy();
            return t;
        }, node.localToSceneTransformProperty(), node.layoutBoundsProperty()));

        return markerGroup;
    }

    public static Group createHelpMarkerGroup(double offsetX, double offsetY, Node node, Transform transform) {

        Group markerGroup = new Group(createHelpMarker(offsetX, offsetY, node));

        if (transform != null) {
            markerGroup.getTransforms().add(transform);
        }
        return markerGroup;
    }
}
