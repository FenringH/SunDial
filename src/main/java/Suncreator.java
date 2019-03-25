import javafx.beans.property.DoubleProperty;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import static java.lang.Math.*;
import static java.lang.Math.toRadians;

public class Suncreator {

    public enum ControlThingyType {
        HELP,
        RESIZE,
        CLOSE,
        MAXIMIZE,
        MINIMIZE,
        NIGTMODE,
        ALWAYSONTOP
    };

    public static ControlThingy createControlThingy(ControlThingyType type, Text helpText) {

        ControlThingy controlThingy;

        switch (type) {
            case HELP: controlThingy = new ControlThingy.PleaseBuildControlThingy()
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
            case RESIZE: controlThingy = new ControlThingy.PleaseBuildControlThingy()
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
            case CLOSE: controlThingy = new ControlThingy.PleaseBuildControlThingy()
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
            case MAXIMIZE: controlThingy = new ControlThingy.PleaseBuildControlThingy()
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
            case MINIMIZE: controlThingy = new ControlThingy.PleaseBuildControlThingy()
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
            case NIGTMODE: controlThingy = new ControlThingy.PleaseBuildControlThingy()
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
            case ALWAYSONTOP: controlThingy = new ControlThingy.PleaseBuildControlThingy()
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
            default: controlThingy = new ControlThingy.PleaseBuildControlThingy()
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

    public static Group createMasterGlobe(DoubleProperty longitude, DoubleProperty latitude, DoubleProperty phase, DoubleProperty tilt) {

        Globe dayGlobe = new Globe(Sunconfig.GLOBE_DAY_IMAGE, Sunconfig.CENTER_X - Sunconfig.MARGIN_X, Sunconfig.GLOBE_ROTATE_DURATION);
        dayGlobe.setLayoutX(Sunconfig.CENTER_X);
        dayGlobe.setLayoutY(Sunconfig.CENTER_Y);
        dayGlobe.setNightLightColor(Color.RED);
        dayGlobe.longitudeProperty().bind(longitude);
        dayGlobe.latitudeProperty().bind(latitude);
        dayGlobe.phaseProperty().bind(phase);
        dayGlobe.tiltProperty().bind(tilt);

        Globe nightGlobe = new Globe(Sunconfig.GLOBE_NIGHT_IMAGE, Sunconfig.CENTER_X - Sunconfig.MARGIN_X, Sunconfig.GLOBE_ROTATE_DURATION);
        nightGlobe.setLayoutX(Sunconfig.CENTER_X);
        nightGlobe.setLayoutY(Sunconfig.CENTER_Y);
        nightGlobe.setAmbientLightColor(Color.WHITE);
        nightGlobe.longitudeProperty().bind(longitude);
        nightGlobe.latitudeProperty().bind(latitude);
        nightGlobe.phaseProperty().bind(phase);
        nightGlobe.tiltProperty().bind(tilt);

        Grid dayGrid = new Grid(Sunconfig.CENTER_X - Sunconfig.MARGIN_X, 1, Color.WHITE, Sunconfig.GLOBE_ROTATE_DURATION);
        dayGrid.setLayoutX(Sunconfig.CENTER_X);
        dayGrid.setLayoutY(Sunconfig.CENTER_Y);
        dayGrid.longitudeProperty().bind(longitude);
        dayGrid.latitudeProperty().bind(latitude);

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

        Group dayGlobeGroup = new Group();
        dayGlobeGroup.getChildren().add(dayGlobe);
        SubScene dayGlobeScene = new SubScene(dayGlobeGroup, Sunconfig.DIAL_WIDTH, Sunconfig.DIAL_HEIGHT, true, SceneAntialiasing.BALANCED);
        dayGlobeScene.setBlendMode(BlendMode.ADD);

        Group nightGlobeGroup = new Group();
        nightGlobeGroup.getChildren().add(nightGlobe);
        SubScene nightGlobeScene = new SubScene(nightGlobeGroup, Sunconfig.DIAL_WIDTH, Sunconfig.DIAL_HEIGHT, true, SceneAntialiasing.BALANCED);
        nightGlobeScene.setBlendMode(BlendMode.SCREEN);

        Group dayGridGroup = new Group();
        dayGridGroup.getChildren().add(dayGrid);
        SubScene dayGridScene = new SubScene(dayGridGroup, Sunconfig.DIAL_WIDTH, Sunconfig.DIAL_HEIGHT, true, SceneAntialiasing.BALANCED);
        dayGridScene.setBlendMode(BlendMode.SCREEN);
        dayGridScene.setEffect(new GaussianBlur(1));
        dayGridScene.setOpacity(Sunconfig.DAY_TERMINATOR_OPACITY);

        Group dayTerminatorLineGroup = new Group();
        dayTerminatorLineGroup.getChildren().add(dayTerminatorLine);
        SubScene dayTerminatorLineScene = new SubScene(dayTerminatorLineGroup, Sunconfig.DIAL_WIDTH, Sunconfig.DIAL_HEIGHT, true, SceneAntialiasing.BALANCED);
        dayTerminatorLineScene.setBlendMode(BlendMode.SCREEN);
        dayTerminatorLineScene.setEffect(new GaussianBlur(Sunconfig.DAY_TERMINATOR_WIDTH));
        dayTerminatorLineScene.setOpacity(Sunconfig.DAY_TERMINATOR_OPACITY);

        Group dayTerminatorGlowGroup = new Group();
        dayTerminatorGlowGroup.getChildren().add(dayTerminatorGlow);
        SubScene dayTerminatorGlowScene = new SubScene(dayTerminatorGlowGroup, Sunconfig.DIAL_WIDTH, Sunconfig.DIAL_HEIGHT, true, SceneAntialiasing.BALANCED);
        dayTerminatorGlowScene.setBlendMode(BlendMode.SCREEN);
        dayTerminatorGlowScene.setEffect(new GaussianBlur(Sunconfig.DAY_TERMINATOR_GLOW_WIDTH));
        dayTerminatorGlowScene.setOpacity(Sunconfig.DAY_TERMINATOR_OPACITY);

        Circle globeAtmosphere = new Circle(Sunconfig.CENTER_X, Sunconfig.CENTER_Y, Sunconfig.CENTER_X - Sunconfig.MARGIN_X + 2);
        globeAtmosphere.setFill(Sunconfig.GLOBE_ATMOSPHERE);
        globeAtmosphere.setStroke(Sunconfig.Color_Of_Void);
        globeAtmosphere.setMouseTransparent(true);

        return new Group(dayGlobeScene, nightGlobeScene, dayTerminatorGlowScene, dayTerminatorLineScene, globeAtmosphere);
    }

    public static Group createTinyGlobe(DoubleProperty longitude, DoubleProperty latitude, DoubleProperty phase, DoubleProperty tilt) {

        Circle tinyGlobeFrame = new Circle(Sunconfig.TINYGLOBE_RADIUS);
        tinyGlobeFrame.setFill(Sunconfig.Color_Of_Void);
        tinyGlobeFrame.setStroke(Sunconfig.Color_Of_TinyFrame);
        tinyGlobeFrame.setStrokeWidth(Sunconfig.TINYGLOBE_FRAME_STROKE_WIDTH);
        tinyGlobeFrame.setStyle(Sunconfig.MATRIX_SHADOW);
        tinyGlobeFrame.setTranslateX(Sunconfig.TINYGLOBE_RADIUS);
        tinyGlobeFrame.setTranslateY(Sunconfig.TINYGLOBE_RADIUS);

        Circle tinyGlobeDot = new Circle(1.0d);
        tinyGlobeDot.setFill(Sunconfig.Color_Of_TinyFrame);
        tinyGlobeDot.setStroke(Sunconfig.Color_Of_Void);
        tinyGlobeDot.setTranslateX(Sunconfig.TINYGLOBE_RADIUS);
        tinyGlobeDot.setTranslateY(Sunconfig.TINYGLOBE_RADIUS);

        Globe tinyGlobe = new Globe(Sunconfig.GLOBE_DAY_IMAGE, Sunconfig.TINYGLOBE_RADIUS, Sunconfig.GLOBE_ROTATE_DURATION);
        tinyGlobe.setDayLightColor(Sunconfig.Color_Of_TinyDay);
        tinyGlobe.setNightLightColor(Sunconfig.Color_Of_TinyNight);
        tinyGlobe.setAmbientLightColor(Sunconfig.Color_Of_TinyAmbient);
        tinyGlobe.setLayoutX(Sunconfig.TINYGLOBE_RADIUS);
        tinyGlobe.setLayoutY(Sunconfig.TINYGLOBE_RADIUS);
        tinyGlobe.longitudeProperty().bind(longitude);
        tinyGlobe.latitudeProperty().bind(latitude);
        tinyGlobe.phaseProperty().bind(phase);
        tinyGlobe.tiltProperty().bind(tilt);

        Group tinyGlobeSceneGroup = new Group();
        tinyGlobeSceneGroup.getChildren().addAll(tinyGlobe);
        SubScene tinyGlobeScene = new SubScene(tinyGlobeSceneGroup, tinyGlobeFrame.getLayoutBounds().getWidth(), tinyGlobeFrame.getLayoutBounds().getHeight(), true, SceneAntialiasing.BALANCED);

        Ring tinyDayTerminatorLine = new Ring(Sunconfig.TINYGLOBE_RADIUS, 1, Sunconfig.Color_Of_TerminatorLine, Sunconfig.GLOBE_ROTATE_DURATION);
        tinyDayTerminatorLine.setTranslateX(Sunconfig.TINYGLOBE_RADIUS);
        tinyDayTerminatorLine.setTranslateY(Sunconfig.TINYGLOBE_RADIUS);
        tinyDayTerminatorLine.longitudeProperty().bind(longitude);
        tinyDayTerminatorLine.latitudeProperty().bind(latitude);
        tinyDayTerminatorLine.phaseProperty().bind(phase);
        tinyDayTerminatorLine.tiltProperty().bind(tilt);

        Group tinyDayTerminatorLineGroup = new Group();
        tinyDayTerminatorLineGroup.getChildren().add(tinyDayTerminatorLine);
        SubScene tinyDayTerminatorLineScene = new SubScene(tinyDayTerminatorLineGroup, tinyGlobeFrame.getLayoutBounds().getWidth(), tinyGlobeFrame.getLayoutBounds().getHeight(), true, SceneAntialiasing.BALANCED);
        tinyDayTerminatorLineScene.setBlendMode(BlendMode.SCREEN);
        tinyDayTerminatorLineScene.setEffect(new GaussianBlur(Sunconfig.DAY_TERMINATOR_WIDTH));
        tinyDayTerminatorLineScene.setOpacity(Sunconfig.DAY_TERMINATOR_OPACITY);

        Group tinyGlobeGroup = new Group(tinyGlobeScene, tinyDayTerminatorLineScene, tinyGlobeDot, tinyGlobeFrame);

        return tinyGlobeGroup;
    }
}
