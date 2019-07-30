import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.collections.ObservableList;
import javafx.scene.*;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collection;

import static java.lang.Math.*;

public class Suncreator {

    public enum TimelineDirection { IN, OUT };

    public enum ControlThingyType {
        HELP,
        RESIZE,
        CLOSE,
        MAXIMIZE,
        MINIMIZE,
        NIGHTMODE,
        ALWAYSONTOP,
        GLOBEGRID,
        GLOBELINES,
        DST,
        ANIMATION,
        CHART,
        CETUS,
        ORBVALLIS
    };

    public static ControlThingy createControlThingy(ControlThingyType type, Text helpText) {

        ControlThingy controlThingy;

        switch (type) {
            case HELP:
                controlThingy = new ControlThingy.PleaseBuildControlThingy()
                        .positionPolar(Sunconfig.CENTER_X, Sunconfig.CENTER_Y, Sunconfig.CONTROL_HELP_OFFSET, Sunconfig.CONTROL_HELP_ANGLE)
                        .size(Sunconfig.CONTROL_HELP_RADIUS)
                        .colorStroke(Sunconfig.Color_Of_HelpStroke_Off, Color.WHITE)
                        .strokeWidth(Sunconfig.CONTROL_HELP_STROKE_WIDTH)
                        .colorFill(Sunconfig.Color_Of_ResizeFill)
                        .marker("?", Color.WHITE, Sunconfig.MATRIX_SHADOW)
                        .style(Sunconfig.CONTROL_RESIZE_SHADOW, Sunconfig.CONTROL_RESIZE_GLOW)
                        .cursor(Cursor.HAND)
                        .helpText(Sunconfig.HELPTEXT_HELP, helpText)
                        .thankYou();
                break;
            case CETUS:
                controlThingy = new ControlThingy.PleaseBuildControlThingy()
                        .positionPolar(Sunconfig.CENTER_X, Sunconfig.CENTER_Y, Sunconfig.CONTROL_CHART_OFFSET, Sunconfig.CONTROL_CETUS_ANGLE)
                        .size(Sunconfig.CONTROL_CHART_RADIUS)
                        .colorStroke(Sunconfig.Color_Of_ResizeStroke, Color.WHITE)
                        .strokeWidth(Sunconfig.CONTROL_CHART_STROKE_WIDTH)
                        .colorFill(Sunconfig.Color_Of_ResizeFill)
                        .image(Sunconfig.LOGO_OSTRON, 1.25, 0, -1, Sunconfig.CONTROL_RESIZE_SHADOW)
                        .style(Sunconfig.CONTROL_RESIZE_SHADOW, Sunconfig.CONTROL_RESIZE_GLOW)
                        .cursor(Cursor.HAND)
                        .helpText(Sunconfig.HELPTEXT_CETUS, helpText)
                        .thankYou();
                break;
            case ORBVALLIS:
                controlThingy = new ControlThingy.PleaseBuildControlThingy()
                        .positionPolar(Sunconfig.CENTER_X, Sunconfig.CENTER_Y, Sunconfig.CONTROL_CHART_OFFSET, Sunconfig.CONTROL_ORBVALLIS_ANGLE)
                        .size(Sunconfig.CONTROL_CHART_RADIUS)
                        .colorStroke(Sunconfig.Color_Of_ResizeStroke, Color.WHITE)
                        .strokeWidth(Sunconfig.CONTROL_CHART_STROKE_WIDTH)
                        .colorFill(Sunconfig.Color_Of_ResizeFill)
                        .image(Sunconfig.LOGO_SOLARIS_UNITED, 1.0, 0, 0, Sunconfig.CONTROL_RESIZE_SHADOW)
                        .style(Sunconfig.CONTROL_RESIZE_SHADOW, Sunconfig.CONTROL_RESIZE_GLOW)
                        .cursor(Cursor.HAND)
                        .helpText(Sunconfig.HELPTEXT_ORBVALLIS, helpText)
                        .thankYou();
                break;
            case CHART:
                controlThingy = new ControlThingy.PleaseBuildControlThingy()
                        .positionPolar(Sunconfig.CENTER_X, Sunconfig.CENTER_Y, Sunconfig.CONTROL_CHART_OFFSET, Sunconfig.CONTROL_CHART_ANGLE)
                        .size(Sunconfig.CONTROL_CHART_RADIUS)
                        .colorStroke(Sunconfig.Color_Of_ChartStroke_Off, Color.WHITE)
                        .strokeWidth(Sunconfig.CONTROL_CHART_STROKE_WIDTH)
                        .colorFill(Sunconfig.Color_Of_ResizeFill)
                        .marker("#", Color.WHITE, Sunconfig.MATRIX_SHADOW)
                        .markerScale(0.75)
                        .style(Sunconfig.CONTROL_RESIZE_SHADOW, Sunconfig.CONTROL_RESIZE_GLOW)
                        .cursor(Cursor.HAND)
                        .helpText(Sunconfig.HELPTEXT_CHART, helpText)
                        .thankYou();
                break;
            case ANIMATION:
                controlThingy = new ControlThingy.PleaseBuildControlThingy()
                        .positionPolar(Sunconfig.CENTER_X, Sunconfig.CENTER_Y, Sunconfig.CONTROL_ANIMATION_OFFSET, Sunconfig.CONTROL_ANIMATION_ANGLE)
                        .size(Sunconfig.CONTROL_ANIMATION_RADIUS)
                        .colorStroke(Sunconfig.Color_Of_ResizeStroke, Color.WHITE)
                        .strokeWidth(Sunconfig.CONTROL_ANIMATION_STROKE_WIDTH)
                        .colorFill(Sunconfig.Color_Of_ResizeFill)
                        .marker("A", Color.WHITE, Sunconfig.MATRIX_SHADOW)
                        .markerScale(0.75)
                        .style(Sunconfig.CONTROL_RESIZE_SHADOW, Sunconfig.CONTROL_RESIZE_GLOW)
                        .cursor(Cursor.HAND)
                        .helpText(Sunconfig.HELPTEXT_ANIMATION, helpText)
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
            case NIGHTMODE:
                controlThingy = new ControlThingy.PleaseBuildControlThingy()
                        .positionPolar(Sunconfig.CENTER_X, Sunconfig.CENTER_Y, Sunconfig.CONTROL_NIGHTMODE_OFFSET, Sunconfig.CONTROL_NIGHTMODE_ANGLE)
                        .size(Sunconfig.CONTROL_NIGHTMODE_RADIUS)
                        .colorStroke(Sunconfig.Color_Of_ResizeStroke, Color.WHITE)
                        .strokeWidth(Sunconfig.CONTROL_NIGHTMODE_STROKE_WIDTH)
                        .colorFill(Sunconfig.Color_Of_ResizeFill)
                        .marker("N", Color.WHITE, Sunconfig.MATRIX_SHADOW)
                        .markerScale(0.75)
                        .style(Sunconfig.CONTROL_RESIZE_SHADOW, Sunconfig.CONTROL_RESIZE_GLOW)
                        .cursor(Cursor.HAND)
                        .helpText(Sunconfig.HELPTEXT_NIGHTMODE, helpText)
                        .thankYou();
                break;
            case ALWAYSONTOP:
                controlThingy = new ControlThingy.PleaseBuildControlThingy()
                        .positionPolar(Sunconfig.CENTER_X, Sunconfig.CENTER_Y, Sunconfig.CONTROL_ALWAYSONTOP_OFFSET, Sunconfig.CONTROL_ALWAYSONTOP_ANGLE)
                        .size(Sunconfig.CONTROL_ALWAYSONTOP_RADIUS)
                        .colorStroke(Sunconfig.Color_Of_ResizeStroke, Color.WHITE)
                        .strokeWidth(Sunconfig.CONTROL_ALWAYSONTOP_STROKE_WIDTH)
                        .colorFill(Sunconfig.Color_Of_ResizeFill)
                        .marker("T", Color.WHITE, Sunconfig.MATRIX_SHADOW)
                        .markerScale(0.75)
                        .style(Sunconfig.CONTROL_RESIZE_SHADOW, Sunconfig.CONTROL_RESIZE_GLOW)
                        .cursor(Cursor.HAND)
                        .helpText(Sunconfig.HELPTEXT_ALWAYSONTOP, helpText)
                        .thankYou();
                break;
            case GLOBEGRID:
                controlThingy = new ControlThingy.PleaseBuildControlThingy()
                        .positionPolar(Sunconfig.CENTER_X, Sunconfig.CENTER_Y, Sunconfig.CONTROL_GLOBEGRID_OFFSET, Sunconfig.CONTROL_GLOBE_GRID_ANGLE)
                        .size(Sunconfig.CONTROL_GLOBEGRID_RADIUS)
                        .colorStroke(Sunconfig.Color_Of_ResizeStroke, Color.WHITE)
                        .strokeWidth(Sunconfig.CONTROL_GLOBEGRID_STROKE_WIDTH)
                        .colorFill(Sunconfig.Color_Of_ResizeFill)
                        .marker("G", Color.WHITE, Sunconfig.MATRIX_SHADOW)
                        .markerScale(0.85)
//                        .markerColorOn(Color.WHITE)
                        .style(Sunconfig.CONTROL_RESIZE_SHADOW, Sunconfig.CONTROL_RESIZE_GLOW)
                        .cursor(Cursor.HAND)
                        .helpText(Sunconfig.HELPTEXT_GLOBEGRID, helpText)
                        .thankYou();
                controlThingy.setVisible(false);
                break;
            case GLOBELINES:
                controlThingy = new ControlThingy.PleaseBuildControlThingy()
                        .positionPolar(Sunconfig.CENTER_X, Sunconfig.CENTER_Y, Sunconfig.CONTROL_GLOBEGRID_OFFSET, Sunconfig.CONTROL_GLOBE_LINES_ANGLE)
                        .size(Sunconfig.CONTROL_GLOBEGRID_RADIUS)
                        .colorStroke(Sunconfig.Color_Of_ResizeStroke, Color.WHITE)
                        .strokeWidth(Sunconfig.CONTROL_GLOBEGRID_STROKE_WIDTH)
                        .colorFill(Sunconfig.Color_Of_ResizeFill)
                        .marker("L", Color.WHITE, Sunconfig.MATRIX_SHADOW)
                        .markerScale(0.85)
//                        .markerColorOn(Color.WHITE)
                        .style(Sunconfig.CONTROL_RESIZE_SHADOW, Sunconfig.CONTROL_RESIZE_GLOW)
                        .cursor(Cursor.HAND)
                        .helpText(Sunconfig.HELPTEXT_GLOBELINES, helpText)
                        .thankYou();
                controlThingy.setVisible(false);
                break;
            case DST:
                controlThingy = new ControlThingy.PleaseBuildControlThingy()
                        .positionCartesian(Sunconfig.CONTROL_DST_OFFSET_X, Sunconfig.CONTROL_DST_OFFSET_Y)
                        .size(Sunconfig.CONTROL_DST_RADIUS)
                        .colorStroke(Color.TRANSPARENT, Color.TRANSPARENT)
                        .strokeWidth(Sunconfig.CONTROL_DST_STROKE_WIDTH)
                        .colorFill(Color.TRANSPARENT)
                        .marker("DST", Sunconfig.Color_Of_ResizeStroke, Sunconfig.MATRIX_SHADOW3)
                        .markerColorOn(Color.WHITE)
                        .markerScale(Sunconfig.CONTROL_DST_MATRIX_SCALE)
                        .style(Sunconfig.CONTROL_RESIZE_SHADOW, Sunconfig.CONTROL_RESIZE_GLOW)
                        .cursor(Cursor.HAND)
                        .helpText(Sunconfig.HELPTEXT_DST, helpText)
                        .thankYou();
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

    public static Timeline createOuterControlsGroupTimeline(Group group) {

        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        timeline.setRate(1);
        timeline.setAutoReverse(false);

        KeyValue keyValueStartOpacitySleep = new KeyValue(group.opacityProperty(), 1.0, Interpolator.EASE_BOTH);
        KeyFrame keyFrameStartOpacitySleep = new KeyFrame(Duration.millis(Sunconfig.OUTER_CONTROLS_HIDE_DURATION / 2), keyValueStartOpacitySleep);
        KeyValue keyValueStartOpacityZero = new KeyValue(group.opacityProperty(), 0.0, Interpolator.EASE_BOTH);
        KeyFrame keyFrameStartOpacityZero = new KeyFrame(Duration.millis(Sunconfig.OUTER_CONTROLS_HIDE_DURATION), keyValueStartOpacityZero);

        timeline.getKeyFrames().addAll(keyFrameStartOpacitySleep, keyFrameStartOpacityZero);

        timeline.setOnFinished(event -> group.setVisible(false));

        return timeline;
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

        // 3D objects
        Globe dayGlobe = new Globe(Sunconfig.GLOBE_DAY_IMAGE, Sunconfig.CENTER_X - Sunconfig.MARGIN_X, Sunconfig.GLOBE_ROTATE_DURATION);
        dayGlobe.setLayoutX(Sunconfig.CENTER_X);
        dayGlobe.setLayoutY(Sunconfig.CENTER_Y);
        dayGlobe.setDayLightColor(Sunconfig.Color_Of_DayDay);
        dayGlobe.setDayReverseLightColor(Sunconfig.Color_Of_DayReverse);
        dayGlobe.setNightLightColor(Color.BLACK);
        dayGlobe.setAmbientLightColor(Sunconfig.Color_Of_DayAmbient);
        dayGlobe.setSpecularColor(Sunconfig.Color_Of_DaySpecular);
        dayGlobe.setSpecularPower(6);
        dayGlobe.setReverseSpecularPower(12);
        dayGlobe.setSpecularMap(Sunconfig.GLOBE_SPECULAR_IMAGE);
        dayGlobe.longitudeProperty().bind(longitude);
        dayGlobe.latitudeProperty().bind(latitude);
        dayGlobe.phaseProperty().bind(phase);
        dayGlobe.tiltProperty().bind(tilt);
        dayGlobe.lightScaleProperty().bind(lightScale);

        Globe nightGlobe = new Globe(Sunconfig.GLOBE_NIGHT_IMAGE, Sunconfig.CENTER_X - Sunconfig.MARGIN_X, Sunconfig.GLOBE_ROTATE_DURATION);
        nightGlobe.setLayoutX(Sunconfig.CENTER_X);
        nightGlobe.setLayoutY(Sunconfig.CENTER_Y);
        nightGlobe.setDayLightColor(Color.BLACK);
        nightGlobe.setNightLightColor(Sunconfig.Color_Of_NightNight);
        nightGlobe.setNightReverseLightColor(Sunconfig.Color_Of_NightReverse);
        nightGlobe.setAmbientLightColor(Sunconfig.Color_Of_NightAmbient);
        nightGlobe.setSpecularColor(Sunconfig.Color_Of_NightSpecular);
        nightGlobe.setSpecularPower(1.85);
        nightGlobe.setReverseSpecularPower(6);
        nightGlobe.setSpecularMap(Sunconfig.GLOBE_SPECULAR_IMAGE);
        nightGlobe.longitudeProperty().bind(longitude);
        nightGlobe.latitudeProperty().bind(latitude);
        nightGlobe.phaseProperty().bind(phase);
        nightGlobe.tiltProperty().bind(tilt);
        nightGlobe.lightScaleProperty().bind(lightScale);

/*
        Globe edgesGlobe = new Globe(Sunconfig.GLOBE_EDGE_IMAGE, Sunconfig.CENTER_X - Sunconfig.MARGIN_X, Sunconfig.GLOBE_ROTATE_DURATION);
        edgesGlobe.setLayoutX(Sunconfig.CENTER_X);
        edgesGlobe.setLayoutY(Sunconfig.CENTER_Y);
        edgesGlobe.setDayLightColor(Color.BLACK);
        edgesGlobe.setNightLightColor(Color.BLACK);
        edgesGlobe.setAmbientLightColor(Color.SKYBLUE);
        edgesGlobe.longitudeProperty().bind(longitude);
        edgesGlobe.latitudeProperty().bind(latitude);
        edgesGlobe.phaseProperty().bind(phase);
        edgesGlobe.tiltProperty().bind(tilt);
*/

        GlobeGrid globeGrid = new GlobeGrid(Sunconfig.CENTER_X - Sunconfig.MARGIN_X, Sunconfig.GLOBEGRID_LINE_WIDTH, Color.WHITE, Sunconfig.GLOBE_ROTATE_DURATION);
        globeGrid.setLayoutX(Sunconfig.CENTER_X);
        globeGrid.setLayoutY(Sunconfig.CENTER_Y);
        globeGrid.longitudeProperty().bind(longitude);
        globeGrid.latitudeProperty().bind(latitude);
        globeGrid.visibleProperty().bind(gridVisibleEh);

        GlobeLines globeLines = new GlobeLines(Sunconfig.CENTER_X - Sunconfig.MARGIN_X, 1, Color.WHITE, Sunconfig.GLOBE_ROTATE_DURATION);
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


        // Cameras (workaround for specular issues with ParallelCamera)
        PerspectiveCamera dayPerspectiveCamera = new PerspectiveCamera(false);
        dayPerspectiveCamera.setFieldOfView(0.1);

        PerspectiveCamera nightPerspectiveCamera = new PerspectiveCamera(false);
        nightPerspectiveCamera.setFieldOfView(0.1);

        PerspectiveCamera gridPerspectiveCamera = new PerspectiveCamera(false);
        gridPerspectiveCamera.setFieldOfView(0.1);

        PerspectiveCamera linesPerspectiveCamera = new PerspectiveCamera(false);
        linesPerspectiveCamera.setFieldOfView(0.1);

        PerspectiveCamera terminatorLinePerspectiveCamera = new PerspectiveCamera(false);
        terminatorLinePerspectiveCamera.setFieldOfView(0.1);

        PerspectiveCamera terminatorGlowPerspectiveCamera = new PerspectiveCamera(false);
        terminatorGlowPerspectiveCamera.setFieldOfView(0.1);


        // Scenes (to flatten 3D and enable layer compositing)
        SubScene dayGlobeScene = new SubScene(dayGlobe, Sundial.DEFAULT_WIDTH, Sundial.DEFAULT_HEIGHT, true, SceneAntialiasing.BALANCED);
        dayGlobeScene.setCamera(dayPerspectiveCamera);

        SubScene nightGlobeScene = new SubScene(nightGlobe, Sundial.DEFAULT_WIDTH, Sundial.DEFAULT_HEIGHT, true, SceneAntialiasing.BALANCED);
        nightGlobeScene.setBlendMode(BlendMode.LIGHTEN);
        nightGlobeScene.setCamera(nightPerspectiveCamera);

//        SubScene edgesGlobeScene = new SubScene(edgesGlobe, Sundial.DEFAULT_WIDTH, Sundial.DEFAULT_HEIGHT, true, SceneAntialiasing.BALANCED);
//        edgesGlobeScene.setBlendMode(BlendMode.SCREEN);

        SubScene globeGridScene = new SubScene(globeGrid, Sundial.DEFAULT_WIDTH, Sundial.DEFAULT_HEIGHT, true, SceneAntialiasing.BALANCED);
        globeGridScene.setBlendMode(BlendMode.SCREEN);
        globeGridScene.setEffect(new GaussianBlur(1));
        globeGridScene.setOpacity(Sunconfig.DAY_GRIDLINE_OPACITY);
//        globeGridScene.setCamera(gridPerspectiveCamera);

        SubScene globeLinesScene = new SubScene(globeLines, Sundial.DEFAULT_WIDTH, Sundial.DEFAULT_HEIGHT, true, SceneAntialiasing.BALANCED);
        globeLinesScene.setBlendMode(BlendMode.SCREEN);
        globeLinesScene.setEffect(new GaussianBlur(1));
        globeLinesScene.setOpacity(1);
        globeLinesScene.setCamera(linesPerspectiveCamera);

        SubScene dayTerminatorLineScene = new SubScene(dayTerminatorLine, Sundial.DEFAULT_WIDTH, Sundial.DEFAULT_HEIGHT, true, SceneAntialiasing.BALANCED);
        dayTerminatorLineScene.setBlendMode(BlendMode.SCREEN);
        dayTerminatorLineScene.setEffect(new GaussianBlur(Sunconfig.GLOBEGRID_LINE_WIDTH));
        dayTerminatorLineScene.setOpacity(Sunconfig.DAY_TERMINATOR_LINE_OPACITY);
        dayTerminatorLineScene.setCamera(terminatorLinePerspectiveCamera);

        SubScene dayTerminatorGlowScene = new SubScene(dayTerminatorGlow, Sundial.DEFAULT_WIDTH, Sundial.DEFAULT_HEIGHT, true, SceneAntialiasing.BALANCED);
        dayTerminatorGlowScene.setBlendMode(BlendMode.SCREEN);
        dayTerminatorGlowScene.setEffect(new GaussianBlur(Sunconfig.DAY_TERMINATOR_GLOW_WIDTH));
        dayTerminatorGlowScene.setOpacity(Sunconfig.DAY_TERMINATOR_GLOW_OPACITY);
        dayTerminatorGlowScene.setCamera(terminatorGlowPerspectiveCamera);


        // Atmosphere effect
        double atmosphereRadius = Sunconfig.CENTER_X - Sunconfig.MARGIN_X + 5;

        Circle globeAtmosphere = new Circle(Sunconfig.CENTER_X, Sunconfig.CENTER_Y, atmosphereRadius);
        globeAtmosphere.setStroke(Sunconfig.Color_Of_Void);
        globeAtmosphere.setMouseTransparent(true);
//        globeAtmosphere.setBlendMode(BlendMode.SCREEN);


        // Bindings for color changes of atmosphere and terminator line while changing globe rotation
        dayTerminatorLine.getRingMaterial().diffuseColorProperty().bind(Bindings.createObjectBinding(() -> {

            double dayLightSceneZ = dayGlobe.getDayLight().localToSceneTransformProperty().get().getTz()
                    / lightScale.get();

            double ratio = abs(dayLightSceneZ / dayGlobe.getLightDistance());
            if (ratio > 1) { ratio = 1; }

            double changeFactor = pow(ratio, 3);

            Color sideColor;

            if (dayLightSceneZ > 0) {
                sideColor = Sunconfig.Color_Of_AtmosphereNight;
            } else {
                sideColor = Sunconfig.Color_Of_TerminatorLine;
            }

            double r = Sunconfig.Color_Of_TerminatorLine.getRed() * (1 - changeFactor) + sideColor.getRed() * changeFactor;
            double g = Sunconfig.Color_Of_TerminatorLine.getGreen() * (1 - changeFactor) + sideColor.getGreen() * changeFactor;
            double b = Sunconfig.Color_Of_TerminatorLine.getBlue() * (1 - changeFactor) + sideColor.getBlue() * changeFactor;

            return new Color(r, g, b, 1);

        }, dayGlobe.getDayLight().localToSceneTransformProperty()));

        dayTerminatorGlow.getRingMaterial().diffuseColorProperty().bind(Bindings.createObjectBinding(() -> {

            double dayLightSceneZ = dayGlobe.getDayLight().localToSceneTransformProperty().get().getTz()
                    / lightScale.get();

            double ratio = abs(dayLightSceneZ / dayGlobe.getLightDistance());
            if (ratio > 1) { ratio = 1; }

            double changeFactor = pow(ratio, 3);

            Color sideColor;

            if (dayLightSceneZ > 0) {
                sideColor = Sunconfig.Color_Of_AtmosphereNight;
            } else {
                sideColor = Sunconfig.Color_Of_TerminatorLine;
            }

            double r = Sunconfig.Color_Of_TerminatorLine.getRed() * (1 - changeFactor) + sideColor.getRed() * changeFactor;
            double g = Sunconfig.Color_Of_TerminatorLine.getGreen() * (1 - changeFactor) + sideColor.getGreen() * changeFactor;
            double b = Sunconfig.Color_Of_TerminatorLine.getBlue() * (1 - changeFactor) + sideColor.getBlue() * changeFactor;

            return new Color(r, g, b, 1);

        }, dayGlobe.getDayLight().localToSceneTransformProperty()));

        globeAtmosphere.fillProperty().bind(Bindings.createObjectBinding(() -> {

            double dayLightSceneZ = dayGlobe.getDayLight().localToSceneTransformProperty().get().getTz()
                    / lightScale.get();

            double ratio = abs(dayLightSceneZ / dayGlobe.getLightDistance());
            if (ratio > 1) { ratio = 1; }

            double changeFactor = pow(ratio, 3);

            Color sideColor;

            if (dayLightSceneZ > 0) {
                sideColor = Sunconfig.Color_Of_AtmosphereNight;
            } else {
                sideColor = Sunconfig.Color_Of_AtmosphereDay;
            }

            double r = Sunconfig.Color_Of_AtmosphereMid.getRed() * (1 - changeFactor) + sideColor.getRed() * changeFactor;
            double g = Sunconfig.Color_Of_AtmosphereMid.getGreen() * (1 - changeFactor) + sideColor.getGreen() * changeFactor;
            double b = Sunconfig.Color_Of_AtmosphereMid.getBlue() * (1 - changeFactor) + sideColor.getBlue() * changeFactor;

            return new RadialGradient(
                    0, 0,
                    Sunconfig.CENTER_X, Sunconfig.CENTER_Y,
                    atmosphereRadius,
                    false,
                    CycleMethod.NO_CYCLE,
                    new Stop(0.700, new Color(
                            Sunconfig.Color_Of_AtmosphereDay.getRed(),
                            Sunconfig.Color_Of_AtmosphereDay.getGreen(),
                            Sunconfig.Color_Of_AtmosphereDay.getBlue(),
                            0.00
                    )),
                    new Stop(0.850, new Color(
                            Sunconfig.Color_Of_AtmosphereDay.getRed(),
                            Sunconfig.Color_Of_AtmosphereDay.getGreen(),
                            Sunconfig.Color_Of_AtmosphereDay.getBlue(),
                            0.15
                    )),
                    new Stop(0.960, new Color(r, g, b, 0.35)),
                    new Stop(0.971, new Color(r, g, b, 1.00)),
                    new Stop(0.975, new Color(0.85, 0.77, 0.85, 1.00)),
                    new Stop(0.979, new Color(r, g, b, 1.00)),
                    new Stop(0.985, new Color(r, g, b, 0.65)),
                    new Stop(1.000, new Color(r, g, b, 0.00))
            );

        }, dayGlobe.getDayLight().localToSceneTransformProperty()));


        // Return final composite
        return new Group(
                dayGlobeScene
                , nightGlobeScene
//                , edgesGlobeScene
                , globeGridScene
                , globeLinesScene
                , dayTerminatorGlowScene
                , dayTerminatorLineScene
                , globeAtmosphere
        );
    }

    public static Group createTinyGlobe(DoubleProperty longitude, DoubleProperty latitude, DoubleProperty phase, DoubleProperty tilt) {

        Globe tinyGlobe = new Globe(Sunconfig.GLOBE_DAY_IMAGE, Sunconfig.TINYGLOBE_RADIUS, Sunconfig.GLOBE_ROTATE_DURATION);
        tinyGlobe.setDayLightColor(Sunconfig.Color_Of_DayDay);
        tinyGlobe.setDayReverseLightColor(Sunconfig.Color_Of_DayReverse);
        tinyGlobe.setNightLightColor(Color.BLACK);
        tinyGlobe.setAmbientLightColor(Sunconfig.Color_Of_DayAmbient);
        tinyGlobe.setSpecularColor(Sunconfig.Color_Of_TinySpecular);
        tinyGlobe.setSpecularPower(6);
        tinyGlobe.setReverseSpecularPower(12);
        tinyGlobe.setSpecularMap(Sunconfig.GLOBE_SPECULAR_IMAGE);
        tinyGlobe.setLayoutX(Sunconfig.CENTER_X);
        tinyGlobe.setLayoutY(Sunconfig.CENTER_Y + Sunconfig.TINYGLOBE_OFFSET);
        tinyGlobe.longitudeProperty().bind(longitude);
        tinyGlobe.latitudeProperty().bind(latitude);
        tinyGlobe.phaseProperty().bind(phase);
        tinyGlobe.tiltProperty().bind(tilt);

        SubScene tinyGlobeScene = new SubScene(tinyGlobe, Sundial.DEFAULT_WIDTH, Sundial.DEFAULT_HEIGHT, true, SceneAntialiasing.BALANCED);

        Globe tinyGlobeNight = new Globe(Sunconfig.GLOBE_DAY_IMAGE, Sunconfig.TINYGLOBE_RADIUS, Sunconfig.GLOBE_ROTATE_DURATION);
        tinyGlobeNight.setDayLightColor(Color.BLACK);
        tinyGlobeNight.setAmbientLightColor(Sunconfig.Color_Of_TinyAmbient);
        tinyGlobeNight.setNightLightColor(Color.RED);
        tinyGlobeNight.setLayoutX(Sunconfig.CENTER_X);
        tinyGlobeNight.setLayoutY(Sunconfig.CENTER_Y + Sunconfig.TINYGLOBE_OFFSET);
        tinyGlobeNight.longitudeProperty().bind(longitude);
        tinyGlobeNight.latitudeProperty().bind(latitude);
        tinyGlobeNight.phaseProperty().bind(phase);
        tinyGlobeNight.tiltProperty().bind(tilt);

        SubScene tinyGlobeNightScene = new SubScene(tinyGlobeNight, Sundial.DEFAULT_WIDTH, Sundial.DEFAULT_HEIGHT, true, SceneAntialiasing.BALANCED);
        tinyGlobeNightScene.setBlendMode(BlendMode.LIGHTEN);

/*
        Globe tinyGlobeEdges = new Globe(Sunconfig.GLOBE_EDGE_IMAGE, Sunconfig.TINYGLOBE_RADIUS, Sunconfig.GLOBE_ROTATE_DURATION);
        tinyGlobeEdges.setDayLightColor(Color.BLACK);
        tinyGlobeEdges.setNightLightColor(Color.BLACK);
        tinyGlobeEdges.setAmbientLightColor(Color.WHITE);
        tinyGlobeEdges.setLayoutX(Sunconfig.CENTER_X);
        tinyGlobeEdges.setLayoutY(Sunconfig.CENTER_Y + Sunconfig.TINYGLOBE_OFFSET);
        tinyGlobeEdges.longitudeProperty().bind(longitude);
        tinyGlobeEdges.latitudeProperty().bind(latitude);
        tinyGlobeEdges.phaseProperty().bind(phase);
        tinyGlobeEdges.tiltProperty().bind(tilt);

        SubScene tinyGlobeEdgesScene = new SubScene(tinyGlobeEdges, Sundial.DEFAULT_WIDTH, Sundial.DEFAULT_HEIGHT, true, SceneAntialiasing.BALANCED);
        tinyGlobeEdgesScene.setBlendMode(BlendMode.ADD);
*/

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
        tinyDayTerminatorLineScene.setOpacity(Sunconfig.DAY_TERMINATOR_LINE_OPACITY);

        Circle tinyGlobeDot = new Circle(Sunconfig.TINYGLOBE_DOT_RADIUS);
        tinyGlobeDot.setFill(Sunconfig.Color_Of_TinyFrame);
        tinyGlobeDot.setStroke(Sunconfig.Color_Of_Void);
        tinyGlobeDot.setTranslateX(Sunconfig.CENTER_X);
        tinyGlobeDot.setTranslateY(Sunconfig.CENTER_Y + Sunconfig.TINYGLOBE_OFFSET);

        Group tinyGlobeGroup = new Group(tinyGlobeScene, tinyGlobeNightScene, /*tinyGlobeEdgesScene,*/ tinyDayTerminatorLineScene, tinyGlobeDot);

        return tinyGlobeGroup;
    }

    public static Group createTinyGlobeFrame() {

        Circle circle = new Circle(Sunconfig.TINYGLOBE_RADIUS);
        circle.setFill(Sunconfig.Color_Of_Void);
        circle.setStroke(Sunconfig.Color_Of_TinyFrame);
        circle.setStrokeWidth(Sunconfig.TINYGLOBE_FRAME_STROKE_WIDTH);

        Group group = new Group(circle);
        group.setTranslateX(Sunconfig.CENTER_X);
        group.setTranslateY(Sunconfig.CENTER_Y + Sunconfig.TINYGLOBE_OFFSET);
        group.setStyle(Sunconfig.MATRIX_SHADOW);

        return group;
    }

    public static Timeline createGlobeTimeline(TimelineDirection timelineDirection, Group group) {

        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        timeline.setRate(1);
        timeline.setAutoReverse(false);

        double opacity;

        if (timelineDirection.equals(TimelineDirection.IN)) {
            opacity = 0;
            timeline.setOnFinished(event -> group.setVisible(false));
        } else {
            opacity = 1;
            timeline.setOnFinished(event -> group.setVisible(true));
        }

        KeyValue keyValueOpacity = new KeyValue(group.opacityProperty(), opacity, Interpolator.EASE_OUT);
        KeyFrame keyFrameOpacity = new KeyFrame(Duration.millis(Sunconfig.TINY_GLOBE_DURATION), keyValueOpacity);

        timeline.getKeyFrames().addAll(keyFrameOpacity);

        return timeline;
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

    public static Timeline createHorizonTimeline(TimelineDirection timelineDirection, Group group) {

        double startY;

        Line sunriseLine = (Line) ((Group)group.getChildren().get(0)).getChildren().get(0);
        Line sunsetLine = (Line) ((Group)group.getChildren().get(1)).getChildren().get(0);

        if (timelineDirection.equals(TimelineDirection.IN)) {
            startY = Sunconfig.SUNRISE_DIAL_LENGTH;
        } else {
            startY = Sunconfig.SUNRISE_DIAL_SHORT_LENGTH;
        }

        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        timeline.setRate(1);
        timeline.setAutoReverse(false);

        KeyValue keyValueSunriseStartY = new KeyValue(sunriseLine.startYProperty(), startY, Interpolator.EASE_BOTH);
        KeyFrame keyFrameSunriseStartY = new KeyFrame(Duration.millis(Sunconfig.TIMEANDDATE_DURATION), keyValueSunriseStartY);

        KeyValue keyValueSunsetStartY = new KeyValue(sunsetLine.startYProperty(), startY, Interpolator.EASE_BOTH);
        KeyFrame keyFrameSunsetStartY = new KeyFrame(Duration.millis(Sunconfig.TIMEANDDATE_DURATION), keyValueSunsetStartY);


        timeline.getKeyFrames().addAll(keyFrameSunriseStartY, keyFrameSunsetStartY);

        return timeline;
    }

    public static Timeline createTimeAndDateTimeline(TimelineDirection timelineDirection, Group group) {

        double slideX, slideY, scale;

        if (timelineDirection.equals(TimelineDirection.IN)) {
            slideX = 0;
            slideY = 0;
            scale = 1;
        } else {
            slideX = 0;
            slideY = Sunconfig.MATRIX_TIMEDATE_OFFSET;
            scale = Sunconfig.MATRIX_TIMEDATE_DOWNSCALE;
        }

        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        timeline.setRate(1);
        timeline.setAutoReverse(false);

        KeyValue keyValueSlideX = new KeyValue(group.translateXProperty(), slideX, Interpolator.EASE_BOTH);
        KeyFrame keyFrameSlideX = new KeyFrame(Duration.millis(Sunconfig.TIMEANDDATE_DURATION), keyValueSlideX);

        KeyValue keyValueSlideY = new KeyValue(group.translateYProperty(), slideY, Interpolator.EASE_BOTH);
        KeyFrame keyFrameSlideY = new KeyFrame(Duration.millis(Sunconfig.TIMEANDDATE_DURATION), keyValueSlideY);

        KeyValue keyValueScaleX = new KeyValue(group.scaleXProperty(), scale, Interpolator.EASE_BOTH);
        KeyFrame keyFrameScaleX = new KeyFrame(Duration.millis(Sunconfig.TIMEANDDATE_DURATION), keyValueScaleX);

        KeyValue keyValueScaleY = new KeyValue(group.scaleYProperty(), scale, Interpolator.EASE_BOTH);
        KeyFrame keyFrameScaleY = new KeyFrame(Duration.millis(Sunconfig.TIMEANDDATE_DURATION), keyValueScaleY);

        timeline.getKeyFrames().addAll(keyFrameSlideX, keyFrameSlideY, keyFrameScaleX, keyFrameScaleY);

        return timeline;
    }

    public static Timeline createCoordinatesTimeline(TimelineDirection timelineDirection, Group group) {

        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        timeline.setRate(1);
        timeline.setAutoReverse(false);

        double slideX, slideY, opacity;
        Interpolator interpolator;

        if (timelineDirection.equals(TimelineDirection.IN)) {
            slideX = 0;
            slideY = 150;
            opacity = 0;
            interpolator = Interpolator.EASE_OUT;
            timeline.setOnFinished(event -> group.setVisible(false));
        } else {
            slideX = 0;
            slideY = 0;
            opacity = 1;
            interpolator = Interpolator.EASE_IN;
            timeline.setOnFinished(event -> group.setVisible(true));
        }

        KeyValue keyValueSlideX = new KeyValue(group.translateXProperty(), slideX, interpolator);
        KeyFrame keyFrameSlideX = new KeyFrame(Duration.millis(Sunconfig.TIMEANDDATE_DURATION), keyValueSlideX);

        KeyValue keyValueSlideY = new KeyValue(group.translateYProperty(), slideY, interpolator);
        KeyFrame keyFrameSlideY = new KeyFrame(Duration.millis(Sunconfig.TIMEANDDATE_DURATION), keyValueSlideY);

        KeyValue keyValueOpacity = new KeyValue(group.opacityProperty(), opacity, interpolator);
        KeyFrame keyFrameOpacity = new KeyFrame(Duration.millis(Sunconfig.TIMEANDDATE_DURATION), keyValueOpacity);

        timeline.getKeyFrames().addAll(keyFrameOpacity, keyFrameSlideX, keyFrameSlideY);

        return timeline;
    }

    public static Timeline createDialLocalHourSuperNiceArcTimeline(TimelineDirection timelineDirection, Group group) {

        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        timeline.setRate(1);
        timeline.setAutoReverse(false);

        double opacity;

        if (timelineDirection.equals(TimelineDirection.IN)) {
            opacity = Sunconfig.DIAL_LOCAL_ARC_NORMAL_OPACITY;
        } else {
            opacity = Sunconfig.DIAL_LOCAL_ARC_DOWN_OPACITY;
        }

        KeyValue keyValueOpacity = new KeyValue(group.opacityProperty(), opacity, Interpolator.EASE_BOTH);
        KeyFrame keyFrameOpacity = new KeyFrame(Duration.millis(Sunconfig.TIMEANDDATE_DURATION), keyValueOpacity);

        timeline.getKeyFrames().addAll(keyFrameOpacity);

        return timeline;

    }

    public static Timeline createHighNoonTimeline(TimelineDirection timelineDirection, Group group, Scale scaleTransform) {

        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        timeline.setRate(1);
        timeline.setAutoReverse(false);

        double scale, opacity;

        if (timelineDirection.equals(TimelineDirection.IN)) {
            scale = 1;
            opacity = Sunconfig.HIGHNOON_NORMAL_OPACITY;
        } else {
            scale = Sunconfig.HIGHNOON_DOWN_SCALE;
            opacity = Sunconfig.HIGHNOON_DOWN_OPACITY;
        }

        KeyValue keyValueOpacity = new KeyValue(group.opacityProperty(), opacity, Interpolator.EASE_BOTH);
        KeyFrame keyFrameOpacity = new KeyFrame(Duration.millis(Sunconfig.TIMEANDDATE_DURATION), keyValueOpacity);

        KeyValue keyValueScaleX = new KeyValue(scaleTransform.xProperty(), scale, Interpolator.EASE_BOTH);
        KeyFrame keyFrameScaleX = new KeyFrame(Duration.millis(Sunconfig.TIMEANDDATE_DURATION), keyValueScaleX);

        KeyValue keyValueScaleY = new KeyValue(scaleTransform.yProperty(), scale, Interpolator.EASE_BOTH);
        KeyFrame keyFrameScaleY = new KeyFrame(Duration.millis(Sunconfig.TIMEANDDATE_DURATION), keyValueScaleY);

        timeline.getKeyFrames().addAll(keyFrameOpacity, keyFrameScaleX, keyFrameScaleY);

        return timeline;
    }

    public static Circle createDialMarginCircle() {
        Circle dialMarginCircle = new Circle(Sunconfig.CENTER_X, Sunconfig.CENTER_Y, Sundial.DEFAULT_WIDTH / 2);
        dialMarginCircle.setFill(Sunconfig.MARGIN_CIRCLE_FILL);
        dialMarginCircle.setStroke(Sunconfig.Color_Of_Void);
        return dialMarginCircle;
    }

    public static Circle createDialMarginCircleRing() {
        Circle dialMarginCircleRing = new Circle(Sunconfig.CENTER_X, Sunconfig.CENTER_Y, Sundial.DEFAULT_WIDTH / 2);
        dialMarginCircleRing.setFill(Sunconfig.Color_Of_Void);
        dialMarginCircleRing.setStroke(Sunconfig.Color_Of_ChartStroke_On);
        dialMarginCircleRing.setStrokeWidth(1.50);
        dialMarginCircleRing.setStyle(Sunconfig.HELP_MARKER_GLOW);
        dialMarginCircleRing.setVisible(false);
        dialMarginCircleRing.setMouseTransparent(true);
        return dialMarginCircleRing;
    }

    public static SubScene createBackgroundSubScene(Group group) {

        Rectangle dialMarginFillBox = new Rectangle(Sundial.DEFAULT_WIDTH, Sundial.DEFAULT_HEIGHT);
        dialMarginFillBox.setTranslateX(0);
        dialMarginFillBox.setTranslateY(0);
        dialMarginFillBox.setFill(Sunconfig.Color_Of_DaySky);
        dialMarginFillBox.setStroke(Sunconfig.Color_Of_Void);
        dialMarginFillBox.setOpacity(0);

        return new SubScene(new Group(dialMarginFillBox, group), Sundial.DEFAULT_WIDTH, Sundial.DEFAULT_HEIGHT, true, SceneAntialiasing.DISABLED);
    }

    public static Arc createDialArcNight() {
        Arc dialArcNight = new Arc(Sunconfig.CENTER_X, Sunconfig.CENTER_Y, Sundial.DEFAULT_WIDTH / 2 - Sunconfig.MARGIN_X, Sundial.DEFAULT_HEIGHT / 2 - Sunconfig.MARGIN_Y, 90, 0);
        dialArcNight.setType(ArcType.ROUND);
        dialArcNight.setStroke(Sunconfig.Color_Of_Void);
        dialArcNight.setFill(Sunconfig.Color_Of_NightSky);
        return dialArcNight;
    }

    public static Arc createDialArcHalfNight() {
        Arc dialArcMidnight = new Arc(Sunconfig.CENTER_X, Sunconfig.CENTER_Y, Sundial.DEFAULT_WIDTH / 2 - Sunconfig.MARGIN_X, Sundial.DEFAULT_HEIGHT / 2 - Sunconfig.MARGIN_Y, 0, -180);
        dialArcMidnight.setType(ArcType.ROUND);
        dialArcMidnight.setStroke(Sunconfig.Color_Of_Void);
        dialArcMidnight.setFill(Sunconfig.Color_Of_HalfNight);
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

    public static Group createKriegsrahmenZeitMarkerGroup(
            KriegsrahmenZeit.Location location,
            Rotate centerRotate,
            ArrayList<Double> markerAngleList,
            ArrayList<Rotate> markerRotateList,
            ArrayList<Line> markerLineList,
            ArrayList<Arc> markerArcList,
            ArrayList<DotMatrix> matrixList,
            ArrayList<Timeline> markerHoverTransitionList) {

        Group markerGroup = new Group();
        Group horizonGroup = new Group();
        Group arcGroup = new Group();

        long mainPhaseLength = location.getMainPhaseLength();
        long lastPhaseLength = location.getLastPhaseLength();
        long cycleLength = location.getCycleLength();
        long cyclesPerTwoDays = (long) ceil(48d * 60 * 60 * 1000 / cycleLength);

        double markerWidth, markerLength;
        Color markerColor;
        String markerShadow;
        double matrixScaleX, matrixScaleY;
        double horizonOffset;
        Paint arcFill;
        double arcFillStop1, arcFillStop2;
        Color arcColor;
        double arcOpacity;
        double animationDuration;
        boolean arcMouseTransparentEh;
        BlendMode blendMode;

        switch (location) {
            case CETUS:
                markerWidth = Sunconfig.CETUS_MARKER_WIDTH;
                markerLength = Sunconfig.CETUS_MARKER_LENGTH;
                markerColor = Sunconfig.Color_Of_CetusMarker;
                markerShadow = Sunconfig.CETUS_MARKER_SHADOW;
                matrixScaleX = Sunconfig.CETUS_HORIZON_SCALE;
                matrixScaleY = Sunconfig.CETUS_HORIZON_SCALE;
                horizonOffset = Sunconfig.CETUS_HORIZON_OFFSET;
                arcFill = Sunconfig.CETUS_ARC_GRADIENT;
                arcFillStop1 = Sunconfig.CETUS_ARC_GRADIENT_STOP1;
                arcFillStop2 = Sunconfig.CETUS_ARC_GRADIENT_STOP2;
                arcColor = Sunconfig.Color_Of_CetusArc;
                arcOpacity = Sunconfig.CETUS_ARC_OPACITY;
                arcMouseTransparentEh = false;
                animationDuration = Sunconfig.CETUS_MARKER_DURATION;
                blendMode = BlendMode.MULTIPLY;
                break;
            case ORB_VALLIS:
                markerWidth = Sunconfig.ORBVALLIS_MARKER_WIDTH;
                markerLength = Sunconfig.ORBVALLIS_MARKER_LENGTH;
                markerColor = Color.TRANSPARENT/*Sunconfig.Color_Of_OrbVallisMarker*/;
                markerShadow = ""/*Sunconfig.ORBVALLIS_MARKER_SHADOW*/;
                matrixScaleX = Sunconfig.ORBVALLIS_HORIZON_SCALE;
                matrixScaleY = Sunconfig.ORBVALLIS_HORIZON_SCALE;
                horizonOffset = Sunconfig.ORBVALLIS_HORIZON_OFFSET;
                arcFill = Sunconfig.ORBVALLIS_ARC_GRADIENT;
                arcFillStop1 = Sunconfig.ORBVALLIS_ARC_GRADIENT_STOP1;
                arcFillStop2 = Sunconfig.ORBVALLIS_ARC_GRADIENT_STOP2;
                arcColor = Sunconfig.Color_Of_OrbVallisArc;
                arcOpacity = Sunconfig.ORBVALLIS_ARC_OPACITY;
                arcMouseTransparentEh = true;
                animationDuration = Sunconfig.ORBVALLIS_MARKER_DURATION;
                blendMode = BlendMode.SRC_OVER;
                break;
            default: return markerGroup;
        }

        for (int i = 0; i <= cyclesPerTwoDays; i++) {

            double startAngle = ((i * cycleLength) * 360d) / (24d * 60 * 60 * 1000);
            double endAngle = ((i * cycleLength + mainPhaseLength) * 360d) / (24d * 60 * 60 * 1000);

            Line markerLineStart = new Line(Sunconfig.CENTER_X, markerLength + Sunconfig.MARGIN_Y, Sunconfig.CENTER_X, Sunconfig.MARGIN_Y);
            markerLineStart.setStroke(markerColor);
            markerLineStart.setStrokeWidth(markerWidth);
            markerLineStart.setStyle(markerShadow);
            markerLineStart.setMouseTransparent(true);

            Rotate markerLineStartRotate = centerRotate.clone();
            markerLineStartRotate.setAngle(startAngle);

            DotMatrix matrixStart = new DotMatrix("00:00", markerColor);
            matrixStart.setScaleX(matrixScaleX);
            matrixStart.setScaleY(matrixScaleY);
            matrixStart.setLayoutX(Sunconfig.CENTER_X - matrixStart.getLayoutBounds().getWidth() / 2 - matrixStart.getLayoutBounds().getHeight() / 2);
            matrixStart.setLayoutY(horizonOffset);
            matrixStart.setRotate(90d);
            matrixStart.setStyle(markerShadow);
            matrixStart.setMouseTransparent(true);
            matrixStart.setOpacity(0);

            Group startHorizonGroup = new Group();
            startHorizonGroup.getChildren().addAll(markerLineStart, matrixStart);
            startHorizonGroup.getTransforms().add(markerLineStartRotate);

            Line markerLineEnd = new Line(Sunconfig.CENTER_X, markerLength + Sunconfig.MARGIN_Y, Sunconfig.CENTER_X, Sunconfig.MARGIN_Y);
            markerLineEnd.setStroke(markerColor);
            markerLineEnd.setStrokeWidth(markerWidth);
            markerLineEnd.setStyle(markerShadow);
            markerLineEnd.setMouseTransparent(true);

            DotMatrix matrixEnd = new DotMatrix("00:00", markerColor);
            matrixEnd.setScaleX(matrixScaleX);
            matrixEnd.setScaleY(matrixScaleY);
            matrixEnd.setTranslateX(Sunconfig.CENTER_X - matrixEnd.getLayoutBounds().getWidth() / 2 + matrixEnd.getLayoutBounds().getHeight() / 2);
            matrixEnd.setTranslateY(horizonOffset);
            matrixEnd.setRotate(90d);
            matrixEnd.setStyle(markerShadow);
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
            nightArc.setFill(arcFill);
            nightArc.setOpacity(arcOpacity);
            nightArc.setMouseTransparent(arcMouseTransparentEh);

            arcGroup.getChildren().addAll(nightArc);
            horizonGroup.getChildren().addAll(startHorizonGroup, endHorizonGroup);

            // Animations
            Timeline markerTransitionOn = new Timeline();
            markerTransitionOn.setCycleCount(1);
            markerTransitionOn.setRate(1);
            markerTransitionOn.setAutoReverse(false);

            KeyValue keyValueStartOpacityOn = new KeyValue(matrixStart.opacityProperty(), 1.0, Interpolator.EASE_BOTH);
            KeyFrame keyFrameStartOpacityOn = new KeyFrame(Duration.millis(animationDuration), keyValueStartOpacityOn);
            KeyValue keyValueEndOpacityOn = new KeyValue(matrixEnd.opacityProperty(), 1.0, Interpolator.EASE_BOTH);
            KeyFrame keyFrameEndOpacityOn = new KeyFrame(Duration.millis(animationDuration), keyValueEndOpacityOn);
            KeyValue keyValueLineStartOn = new KeyValue(markerLineStart.startYProperty(), markerLength * 3, Interpolator.EASE_BOTH);
            KeyFrame keyFrameLineStartOn = new KeyFrame(Duration.millis(animationDuration), keyValueLineStartOn);
            KeyValue keyValueLineEndOn = new KeyValue(markerLineEnd.startYProperty(), markerLength * 3, Interpolator.EASE_BOTH);
            KeyFrame keyFrameLineEndOn = new KeyFrame(Duration.millis(animationDuration), keyValueLineEndOn);

            markerTransitionOn.getKeyFrames().addAll(keyFrameStartOpacityOn, keyFrameEndOpacityOn, keyFrameLineStartOn, keyFrameLineEndOn);

            Timeline markerTransitionOff = new Timeline();
            markerTransitionOff.setCycleCount(1);
            markerTransitionOff.setRate(1);
            markerTransitionOff.setAutoReverse(false);

            KeyValue keyValueStartOpacityOff = new KeyValue(matrixStart.opacityProperty(), 0.0, Interpolator.EASE_BOTH);
            KeyFrame keyFrameStartOpacityOff = new KeyFrame(Duration.millis(animationDuration), keyValueStartOpacityOff);
            KeyValue keyValueEndOpacityOff = new KeyValue(matrixEnd.opacityProperty(), 0.0, Interpolator.EASE_BOTH);
            KeyFrame keyFrameEndOpacityOff = new KeyFrame(Duration.millis(animationDuration), keyValueEndOpacityOff);
            KeyValue keyValueLineStartOff = new KeyValue(markerLineStart.startYProperty(), markerLength + Sunconfig.MARGIN_Y, Interpolator.EASE_BOTH);
            KeyFrame keyFrameLineStartOff = new KeyFrame(Duration.millis(animationDuration), keyValueLineStartOff);
            KeyValue keyValueLineEndOff = new KeyValue(markerLineEnd.startYProperty(), markerLength + Sunconfig.MARGIN_Y, Interpolator.EASE_BOTH);
            KeyFrame keyFrameLineEndOff = new KeyFrame(Duration.millis(animationDuration), keyValueLineEndOff);

            markerTransitionOff.getKeyFrames().addAll(keyFrameStartOpacityOff, keyFrameEndOpacityOff, keyFrameLineStartOff, keyFrameLineEndOff);

            nightArc.fillProperty().bind(Bindings.createObjectBinding(() -> {
                double stop1 = arcFillStop1 - (0.40 * matrixStart.opacityProperty().get());
                double stop2 = arcFillStop2 - (0.15 * matrixStart.opacityProperty().get());
                return new RadialGradient(
                        0, 0,
                        Sunconfig.CENTER_X, Sunconfig.CENTER_Y, Sunconfig.CENTER_Y - Sunconfig.MARGIN_Y,
                        false,
                        CycleMethod.NO_CYCLE,
                        new Stop(stop1, Sunconfig.Color_Of_Void),
                        new Stop(stop2, arcColor)
                );
            }, matrixStart.opacityProperty()));

            // Events
            nightArc.setOnMouseEntered(event -> {
                markerTransitionOff.stop();
                markerTransitionOn.play();
            });
            nightArc.setOnMouseExited(event -> {
                markerTransitionOn.stop();
                markerTransitionOff.play();
            });

            // these are sent back
            markerAngleList.add(startAngle);
            markerAngleList.add(endAngle);

            markerRotateList.add(markerLineStartRotate);
            markerRotateList.add(markerLineEndRotate);

            markerArcList.add(nightArc);

            markerLineList.add(markerLineStart);
            markerLineList.add(markerLineEnd);

            matrixList.add(matrixStart);
            matrixList.add(matrixEnd);

            markerHoverTransitionList.add(markerTransitionOn);
            markerHoverTransitionList.add(markerTransitionOff);

        }

        arcGroup.setBlendMode(blendMode);

        markerGroup.getChildren().addAll(arcGroup, horizonGroup);

        return markerGroup;
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

    public static DotMatrix createOrbVallisTimer() {
        DotMatrix cetusTimer = new DotMatrix("0h00m00s", Sunconfig.Color_Of_OrbVallisWarm);
        cetusTimer.setScaleX(Sunconfig.ORBVALLIS_TIMER_SCALE);
        cetusTimer.setScaleY(Sunconfig.ORBVALLIS_TIMER_SCALE);
        cetusTimer.setLayoutX(Sunconfig.CENTER_X - cetusTimer.getLayoutBounds().getWidth() / 2);
        cetusTimer.setLayoutY(Sunconfig.ORBVALLIS_TIMER_OFFSET);
        cetusTimer.setStyle(Sunconfig.ORBVALLIS_MATRIX_SHADOW_WARM);
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
            markerMinuteCircle.setOpacity(((i % 5) == 0) ? opacity * 1.5 : opacity);

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
            Group dialHourLineMarkerGroupA,
            Group dialHourLineMarkerGroupB,
            ArrayList<Rotate> dialMarkerRotateList
            ) {

        for(int i = 0; i < Sunconfig.MAX_MARKER; i++) {

            double strokeWidth = Sunconfig.MARKER_HOUR_STROKE_WIDTH * 1.00;
            double lineLength = Sunconfig.MARKER_HOUR_LENGTH * 0.50d;
            double dotRadiusA = Sunconfig.MARKER_HOUR_STROKE_WIDTH * 1.25;
            boolean dotVisibleA = false;
            double lineOpacityA = 0.50d;
            double lineOpacityB = 0.50d;
            Color lineColorA = Color.BLACK/*Sunconfig.Color_Of_HourMarkerQwrt*/;
            Color lineColorB = Color.BLACK/*Sunconfig.Color_Of_HourMarkerQwrt*/;
            String style = "";

            if (i % 2 == 0) {
                lineLength = Sunconfig.MARKER_HOUR_LENGTH * 0.75d;
                dotRadiusA = Sunconfig.MARKER_HOUR_STROKE_WIDTH * 1.50;
                lineOpacityA = 0.65d;
                lineColorA = Color.BLACK/*Sunconfig.Color_Of_HourMarkerHalf*/;
                strokeWidth = Sunconfig.MARKER_HOUR_STROKE_WIDTH * 1.00;
            }

            if (i % 4 == 0) {
                lineLength = Sunconfig.MARKER_HOUR_LENGTH;
                dotRadiusA = Sunconfig.MARKER_HOUR_STROKE_WIDTH * 2.00;
                dotVisibleA = true;
                lineOpacityA = 0.85d;
                lineColorA = Color.BLACK/*Sunconfig.Color_Of_HourMarkerFull*/;
                strokeWidth = Sunconfig.MARKER_HOUR_STROKE_WIDTH * 1.00;
//                style = Sunconfig.HOUR_MARKER_SHADOW;
            }

            Rotate markerRotate = centerRotate.clone();
            markerRotate.setAngle(Sunutil.getNightCompressionAngle(i * 360d / 96d, nightCompression));

            Polygon markerPoly = new Polygon(
                    0, lineLength,
                    -Sunconfig.MARKER_HOUR_STROKE_WIDTH, 0,
                    0, -lineLength / 4,
                    Sunconfig.MARKER_HOUR_STROKE_WIDTH, 0
            );
            markerPoly.setTranslateX(Sunconfig.CENTER_X);
            markerPoly.setTranslateY(Sunconfig.MARGIN_Y);
            markerPoly.setStroke(Sunconfig.Color_Of_HourMarkerStroke);
            markerPoly.setStrokeWidth(strokeWidth);
            markerPoly.setFill(lineColorA);
            markerPoly.setOpacity(lineOpacityA / 2);
            markerPoly.setMouseTransparent(true);

            Line markerLineA = new Line(0, 0, 0, -lineLength);
            markerLineA.setTranslateX(Sunconfig.CENTER_X);
            markerLineA.setTranslateY(Sunconfig.MARGIN_Y);
            markerLineA.setStroke(Sunconfig.Color_Of_DaySky);
            markerLineA.setStrokeWidth(strokeWidth);
            markerLineA.setOpacity(lineOpacityA * 0.35);
            markerLineA.setVisible(dotVisibleA);
            markerLineA.setMouseTransparent(true);

            Circle markerDotA = new Circle();
            markerDotA.setRadius(dotRadiusA);
            markerDotA.setTranslateX(Sunconfig.CENTER_X);
            markerDotA.setTranslateY(Sunconfig.MARGIN_Y);
            markerDotA.setStroke(Color.TRANSPARENT);
            markerDotA.setFill(Sunconfig.Color_Of_DaySky);
            markerDotA.setVisible(dotVisibleA);
            markerDotA.setMouseTransparent(true);

            Line markerLineB = new Line(0, 0, 0, -lineLength);
            markerLineB.setTranslateX(Sunconfig.CENTER_X);
            markerLineB.setTranslateY(Sunconfig.MARGIN_Y);
            markerLineB.setStroke(lineColorB);
            markerLineB.setStrokeWidth(strokeWidth);
            markerLineB.setOpacity(lineOpacityB);
            markerLineB.setStyle(style);
            markerLineB.setMouseTransparent(true);

            Group markerGroupA = new Group(markerDotA);
            markerGroupA.getTransforms().add(markerRotate);

            Group markerGroupB = new Group(markerLineB);
            markerGroupB.getTransforms().add(markerRotate);

            if (i % 4 == 0) {

                Group matrixMarkerGroup = new Group();

                Line matrixMarkerLine = new Line(Sunconfig.CENTER_X, lineLength + Sunconfig.MARGIN_Y, Sunconfig.CENTER_X, Sunconfig.MARGIN_Y + 1);
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
                markerMatrix.setOpacity(Sunconfig.LOCAL_HOUR_MARKER_OPACITY);

                if (i % 24 != 0) {
                    markerMatrix.setScaleX(Sunconfig.MATRIX_HOUR_OFF_SCALE);
                    markerMatrix.setScaleY(Sunconfig.MATRIX_HOUR_OFF_SCALE);
                    markerMatrix.setFill(Color.BLACK);
                    markerMatrix.setOpacity(Sunconfig.LOCAL_HOUR_MARKER_OFF_OPACITY);
//                    markerMatrix.setBlendMode(BlendMode.MULTIPLY);
                }

                matrixMarkerGroup.getChildren().addAll(matrixMarkerLine, markerMatrix);
                matrixMarkerGroup.getTransforms().add(markerRotate);

                dialHourMatrixMarkerGroup.getChildren().add(matrixMarkerGroup);
                hourMarkerMatrixList.add(markerMatrix);
            }

            dialHourLineMarkerGroupA.getChildren().addAll(markerGroupA);
            dialHourLineMarkerGroupB.getChildren().addAll(markerGroupB);

            dialMarkerRotateList.add(markerRotate);
        }

    }

    public static Group createDialCircleCenterPoint() {

        Circle dialCircleCenterPoint = new Circle(Sunconfig.CENTER_X, Sunconfig.CENTER_Y, 1);
        dialCircleCenterPoint.setFill(Sunconfig.Color_Of_LocalTime);
        dialCircleCenterPoint.setStroke(Sunconfig.Color_Of_Void);

        Circle dialCircleCenterFrame = new Circle(Sunconfig.CENTER_X, Sunconfig.CENTER_Y, 12);
        dialCircleCenterFrame.setFill(Sunconfig.Color_Of_Void);
        dialCircleCenterFrame.setStroke(Sunconfig.Color_Of_LocalTime);

        Group group = new Group(dialCircleCenterFrame, dialCircleCenterPoint);
        group.setMouseTransparent(true);

        return group;
    }

    public static Group createControlNightCompression() {

        Circle circle = new Circle(Sunconfig.DOT_RADIUS_SMOL, Sunconfig.DOT_RADIUS_SMOL, Sunconfig.DOT_RADIUS_SMOL);
        circle.setFill(Sunconfig.Color_Of_LocalTime);
        circle.setStroke(Sunconfig.Color_Of_Void);

        Group group = new Group(circle);
        group.setLayoutX(Sunconfig.CENTER_X - Sunconfig.DOT_RADIUS_SMOL);
        group.setLayoutY(Sunconfig.CENTER_Y - Sunconfig.DOT_RADIUS_SMOL);
        group.setStyle(Sunconfig.MATRIX_SHADOW2);

        return group;
    }

    public static Group createDialHighNoonGroup(Rotate highNoonDialRotate) {

        Group dialHighNoonGroup = new Group();

        Polygon dialHighNoonPolyLong = new Polygon(
                0, Sunconfig.HIGHNOON_DIAL_LENGTH,
                - Sunconfig.HIGHNOON_DIAL_WIDTH * 0.5, Sunconfig.HIGHNOON_DIAL_LENGTH * 0.25,
                0, Sunconfig.MARGIN_Y,
                Sunconfig.HIGHNOON_DIAL_WIDTH * 0.5, Sunconfig.HIGHNOON_DIAL_LENGTH * 0.25
        );

        Polygon dialHighNoonPolyShort = new Polygon(
                0, Sunconfig.HIGHNOON_DIAL_SHORT_LENGTH,
                - Sunconfig.HIGHNOON_DIAL_WIDTH * 0.85, Sunconfig.HIGHNOON_DIAL_SHORT_LENGTH * 0.4,
                0, Sunconfig.MARGIN_Y,
                Sunconfig.HIGHNOON_DIAL_WIDTH * 0.85, Sunconfig.HIGHNOON_DIAL_SHORT_LENGTH * 0.4
        );

        MorphingPolygon morphingPolygon = new MorphingPolygon(
                dialHighNoonPolyLong.getPoints(),
                dialHighNoonPolyShort.getPoints(),
                Sunconfig.TIMEANDDATE_DURATION,
                Interpolator.EASE_BOTH
        );
        morphingPolygon.setTranslateX(Sunconfig.CENTER_X);
        morphingPolygon.setFill(Sunconfig.Color_Of_HighNoon);
        morphingPolygon.setStroke(Color.TRANSPARENT);
        morphingPolygon.setStrokeWidth(Sunconfig.HIGHNOON_STROKE_WIDTH);
        morphingPolygon.setStrokeLineCap(StrokeLineCap.ROUND);

        Polygon dialHighNoonPolyBack = new Polygon(
                0, Sunconfig.HIGHNOON_DIAL_LENGTH,
                - Sunconfig.HIGHNOON_DIAL_WIDTH * 4, Sunconfig.HIGHNOON_DIAL_LENGTH * 0.25,
                0, Sunconfig.MARGIN_Y,
                Sunconfig.HIGHNOON_DIAL_WIDTH * 4, Sunconfig.HIGHNOON_DIAL_LENGTH * 0.25
        );
        dialHighNoonPolyBack.setTranslateX(Sunconfig.CENTER_X);
        dialHighNoonPolyBack.setFill(Color.TRANSPARENT);
        dialHighNoonPolyBack.setStroke(Color.TRANSPARENT);

        dialHighNoonGroup.getChildren().addAll(dialHighNoonPolyBack, morphingPolygon);
        dialHighNoonGroup.getTransforms().add(highNoonDialRotate);
        dialHighNoonGroup.setStyle(Sunconfig.LOCALNOON_DIAL_GLOW);
        dialHighNoonGroup.setOpacity(Sunconfig.DIAL_HIGH_NOON_OPACITY);

        return dialHighNoonGroup;
    }

    public static Arc createDialLocalHourArc() {
        Arc arc = new Arc(0, 0,
                Sunconfig.CENTER_X,
                Sunconfig.CENTER_Y,
                0, 15);
        arc.setType(ArcType.ROUND);
        arc.setTranslateX(Sunconfig.CENTER_X);
        arc.setTranslateY(Sunconfig.CENTER_Y);
        arc.setFill(Sunconfig.LOCAL_HOUR_ARC_FILL);
        arc.setStroke(Color.TRANSPARENT);
//        arc.setBlendMode(BlendMode.OVERLAY);
        return arc;
    }

    public static SuperNiceArc createDialLocalHourSuperNiceArc() {

        SuperNiceArc superNiceArc = new SuperNiceArc.PleaseBuild()
                .center(Sunconfig.CENTER_X, Sunconfig.CENTER_Y)
                .size(Sunconfig.SUPER_NICE_ARC_RADIUS_SMOL, Sunconfig.CENTER_Y - 1)
                .strokeWidth(Sunconfig.SUPER_NICE_ARC_STROKE_WIDTH)
                .thankYou();

        superNiceArc.setStrokeColor(Color.WHITE);
        superNiceArc.setStyle(Sunconfig.LOCALHOUR_DIAL_GLOW);
        superNiceArc.setBlendMode(BlendMode.SCREEN);
        superNiceArc.setMouseTransparent(true);

        return superNiceArc;
    }

    public static Group createDialLocalHourGroup(Rotate dialRotateLocalHour) {

        double C = Sunconfig.CENTER_Y - Sunconfig.SUPER_NICE_ARC_RADIUS_SMOL * 1.1;
        double D = C * 0.75;

        Polygon dialLocalHourPolyShortestSimple = new Polygon(
                0, C, // 1
                - Sunconfig.LOCALTIME_HOUR_SHORTEST_WIDTH / 2, D, // 2
                0, 3, // 3
                + Sunconfig.LOCALTIME_HOUR_SHORTEST_WIDTH / 2, D // 4
        );

        Polygon dialLocalHourPolyShorterSimple = new Polygon(
                0, (Sunconfig.CENTER_Y - Sunconfig.SUPER_NICE_ARC_RADIUS_SMOL) * 1.33, // 1
                - Sunconfig.LOCALTIME_HOUR_SHORTER_WIDTH / 2, (Sunconfig.CENTER_Y - Sunconfig.SUPER_NICE_ARC_RADIUS_SMOL), // 2
                0, 3, // 3
                + Sunconfig.LOCALTIME_HOUR_SHORTER_WIDTH / 2, (Sunconfig.CENTER_Y - Sunconfig.SUPER_NICE_ARC_RADIUS_SMOL) // 4
        );

        Polygon dialLocalHourPolyShortSimple = new Polygon(
                0, Sunconfig.LOCALTIME_DIAL_SHORT_LENGTH, // 1
                - Sunconfig.LOCALTIME_HOUR_SHORT_WIDTH / 2, Sunconfig.LOCALTIME_DIAL_SHORT_LENGTH * 0.75, // 2
                0, 3, // 3
                + Sunconfig.LOCALTIME_HOUR_SHORT_WIDTH / 2, Sunconfig.LOCALTIME_DIAL_SHORT_LENGTH * 0.75 // 4
        );

        Polygon dialLocalHourPolyLongComplex = new Polygon(
                0, Sunconfig.LOCALTIME_DIAL_LENGTH, // 1
                - Sunconfig.LOCALTIME_HOUR_WIDTH / 2, Sunconfig.LOCALTIME_DIAL_LENGTH * 0.75, // 2
                - Sunconfig.LOCALTIME_HOUR_STROKE_WIDTH, Sunconfig.MARGIN_Y * 1.5, // 3
                0, Sunconfig.MARGIN_Y, // 4
                + Sunconfig.LOCALTIME_HOUR_STROKE_WIDTH, Sunconfig.MARGIN_Y * 1.5, // 5
                + Sunconfig.LOCALTIME_HOUR_WIDTH / 2, Sunconfig.LOCALTIME_DIAL_LENGTH  * 0.75, // 6
                0, Sunconfig.LOCALTIME_DIAL_LENGTH, // 7
                + Sunconfig.LOCALTIME_HOUR_STROKE_WIDTH, Sunconfig.LOCALTIME_DIAL_LENGTH  * 0.75, // 8
                0, Sunconfig.LOCALTIME_DIAL_LENGTH  * 0.50, // 9
                - Sunconfig.LOCALTIME_HOUR_STROKE_WIDTH, Sunconfig.LOCALTIME_DIAL_LENGTH  * 0.75 // 10
        );

        Polygon dialLocalHourPolyShortComplex = new Polygon(
                0, Sunconfig.LOCALTIME_DIAL_SHORT_LENGTH, // 1
                - Sunconfig.LOCALTIME_HOUR_SHORT_WIDTH / 2, Sunconfig.LOCALTIME_DIAL_SHORT_LENGTH * 0.75, // 2
                - Sunconfig.LOCALTIME_HOUR_STROKE_WIDTH, Sunconfig.MARGIN_Y * 1.5, // 3
                0, Sunconfig.MARGIN_Y, // 4
                + Sunconfig.LOCALTIME_HOUR_STROKE_WIDTH, Sunconfig.MARGIN_Y * 1.5, // 5
                + Sunconfig.LOCALTIME_HOUR_SHORT_WIDTH / 2, Sunconfig.LOCALTIME_DIAL_SHORT_LENGTH * 0.75, // 6
                0, Sunconfig.LOCALTIME_DIAL_SHORT_LENGTH, // 7
                0, Sunconfig.LOCALTIME_DIAL_SHORT_LENGTH, // 8
                0, Sunconfig.LOCALTIME_DIAL_SHORT_LENGTH, // 9
                0, Sunconfig.LOCALTIME_DIAL_SHORT_LENGTH // 10
        );

        MorphingPolygon morphingPolygon = new MorphingPolygon(
                dialLocalHourPolyShortestSimple.getPoints(),
                dialLocalHourPolyShortSimple.getPoints(),
                0.0, 1.0,
                Sunconfig.TIMEANDDATE_DURATION,
                Interpolator.EASE_BOTH
        );
        morphingPolygon.setTranslateX(Sunconfig.CENTER_X);
        morphingPolygon.setFill(new Color(1, 1, 1, 0.1));
        morphingPolygon.setStroke(Color.WHITE);
        morphingPolygon.setStrokeWidth(Sunconfig.LOCALTIME_HOUR_STROKE_WIDTH);
//        morphingPolygon.setOpacity(1);

        Group dialLocalHourGroup = new Group(morphingPolygon);
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
            Group dialLocalSecondLedList,
            ArrayList<Boolean> dialLocalSecondOn,
            Group dialLocalMinuteLedList,
            ArrayList<Boolean> dialLocalMinuteOn,
            ArrayList<Timeline> dialLocalSecondLedOffList,
            ArrayList<Timeline> dialLocalMinuteLedOffList,
            ArrayList<Timeline> dialLocalMinuteLedDimList
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
            localMinuteCircle.setFill(Sunconfig.Color_Of_Minutes);
            localMinuteCircle.setStroke(Sunconfig.Color_Of_Void);
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

            dialLocalSecondLedList.getChildren().add(secondGroup);
            dialLocalMinuteLedList.getChildren().add(minuteLedGroup);

            dialLocalSecondOn.add(false);
            dialLocalMinuteOn.add(false);

            dialLocalSecondLedOffList.add(Sunutil.createTimelineForLED(secondGroup, Sunconfig.SECOND_LED_OFF_OPACITY, Sunconfig.LED_OPACITY_DURATION));
            dialLocalMinuteLedOffList.add(Sunutil.createTimelineForLED(minuteLedGroup, Sunconfig.MINUTE_LED_OFF_OPACITY, Sunconfig.LED_OPACITY_DURATION));
            dialLocalMinuteLedDimList.add(Sunutil.createTimelineForLED(minuteLedGroup, Sunconfig.MINUTE_LED_DIM_OPACITY, Sunconfig.LED_OPACITY_DURATION));
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

        Line sunriseLine = new Line(Sunconfig.CENTER_X, Sunconfig.SUNRISE_DIAL_LENGTH, Sunconfig.CENTER_X, Sunconfig.MARGIN_Y);
        sunriseLine.setStroke(Sunconfig.Color_Of_Horizon);
        sunriseLine.setStrokeWidth(Sunconfig.SUNRISE_STROKE_WIDTH);
        sunriseLine.setStyle(Sunconfig.HORIZON_GLOW);

        sunriseGroup.getChildren().addAll(sunriseLine, matrixSunrise);
        sunriseGroup.getTransforms().add(sunriseDialRotate);

        return sunriseGroup;
    }

    public static Group createSunsetGroup(Rotate sunsetDialRotate, DotMatrix matrixSunset) {

        Group sunsetGroup = new Group();

        Line sunsetLine = new Line(Sunconfig.CENTER_X, Sunconfig.SUNRISE_DIAL_LENGTH, Sunconfig.CENTER_X, Sunconfig.MARGIN_Y);
        sunsetLine.setStroke(Sunconfig.Color_Of_Horizon);
        sunsetLine.setStrokeWidth(Sunconfig.SUNSET_STROKE_WIDTH);
        sunsetLine.setStyle(Sunconfig.HORIZON_GLOW);

        sunsetGroup.getChildren().addAll(sunsetLine, matrixSunset);
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
        dialArcDayLength.setStyle(Sunconfig.MATRIX_SHADOW);
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
        matrixDayLength.setOpacity(0.85);
        matrixDayLength.setMouseTransparent(true);
        return matrixDayLength;
    }

    public static DotMatrix createMatrixLongitude() {

        DotMatrix matrixLongitude = new DotMatrix("000.00E", Sunconfig.Color_Of_LocalTime);
        matrixLongitude.setScaleX(Sunconfig.MATRIX_LONGITUDE_SCALE);
        matrixLongitude.setScaleY(Sunconfig.MATRIX_LONGITUDE_SCALE);
        matrixLongitude.setLayoutX(Sunconfig.CENTER_X + Sunconfig.MATRIX_LONGITUDE_SLIDE - matrixLongitude.getLayoutBounds().getWidth() / 2);
        matrixLongitude.setLayoutY(Sunconfig.MATRIX_LONGITUDE_OFFSET);
        matrixLongitude.setStyle(Sunconfig.MATRIX_SHADOW);

        Rectangle longitudeBackdrop = new Rectangle(
                matrixLongitude.getLayoutBounds().getMinX(),
                matrixLongitude.getLayoutBounds().getMinY(),
                matrixLongitude.getLayoutBounds().getWidth(),
                matrixLongitude.getLayoutBounds().getHeight());
        longitudeBackdrop.setOpacity(0);

        matrixLongitude.getChildren().add(longitudeBackdrop);

        return matrixLongitude;
    }

    public static DotMatrix createMatrixLatitude() {

        DotMatrix matrixLatitude = new DotMatrix("000.00N", Sunconfig.Color_Of_LocalTime);
        matrixLatitude.setScaleX(Sunconfig.MATRIX_LATITUDE_SCALE);
        matrixLatitude.setScaleY(Sunconfig.MATRIX_LATITUDE_SCALE);
        matrixLatitude.setLayoutX(Sunconfig.CENTER_X + Sunconfig.MATRIX_LATITUDE_SLIDE - matrixLatitude.getLayoutBounds().getWidth() / 2);
        matrixLatitude.setLayoutY(Sunconfig.MATRIX_LATITUDE_OFFSET);
        matrixLatitude.setStyle(Sunconfig.MATRIX_SHADOW);

        Rectangle latitudeBackdrop = new Rectangle(
                matrixLatitude.getLayoutBounds().getMinX(),
                matrixLatitude.getLayoutBounds().getMinY(),
                matrixLatitude.getLayoutBounds().getWidth(),
                matrixLatitude.getLayoutBounds().getHeight());
        latitudeBackdrop.setOpacity(0);

        matrixLatitude.getChildren().add(latitudeBackdrop);

        return matrixLatitude;
    }

    public static DotMatrix createMatrixHighNoon() {
        DotMatrix matrixHighNoon = new DotMatrix("00:00:00", Sunconfig.Color_Of_HighNoon);
        matrixHighNoon.setScaleX(Sunconfig.MATRIX_HIGHNOON_SCALE);
        matrixHighNoon.setScaleY(Sunconfig.MATRIX_HIGHNOON_SCALE);
        matrixHighNoon.setLayoutX(Sunconfig.CENTER_X - matrixHighNoon.getLayoutBounds().getWidth() / 2);
        matrixHighNoon.setLayoutY(Sunconfig.MARGIN_Y + matrixHighNoon.getLayoutBounds().getHeight() / 2);
        matrixHighNoon.setStyle(Sunconfig.MATRIX_GLOW);
        matrixHighNoon.setMouseTransparent(true);
        matrixHighNoon.setVisible(false);
        return matrixHighNoon;
    }

    public static DotMatrix createMatrixTimeZone() {
        DotMatrix matrixTimeZone = new DotMatrix("UTC+00", Sunconfig.Color_Of_LocalTime);
        matrixTimeZone.setScaleX(Sunconfig.MATRIX_TIMEZONE_SCALE);
        matrixTimeZone.setScaleY(Sunconfig.MATRIX_TIMEZONE_SCALE);
        matrixTimeZone.setLayoutX(Sunconfig.CENTER_X - matrixTimeZone.getLayoutBounds().getWidth() / 2);
        matrixTimeZone.setLayoutY(Sunconfig.MATRIX_TIMEZONE_OFFSET);
        matrixTimeZone.setStyle(Sunconfig.MATRIX_SHADOW);
        matrixTimeZone.setOpacity(Sunconfig.MATRIX_TIMEZONE_DEFAULT_OPACITY);
//        matrixTimeZone.setVisible(false);
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
        helpText.setFill(Color.WHITE);
        helpText.setText(Sunconfig.HELPTEXT_DEFAULT);
        helpText.setTranslateX(5);
        helpText.setTranslateY(15);

        Rectangle helpTextRectangle = new Rectangle(0, 0, 20, 20);
        helpTextRectangle.setArcWidth(10);
        helpTextRectangle.setArcHeight(10);
        helpTextRectangle.setStroke(Color.WHITE);
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
        infoText.setStroke(Color.YELLOW);
        infoText.setFill(Color.YELLOW);
        infoText.setText(Sunconfig.HELPTEXT_DEFAULT);
        infoText.setTranslateX(5);
        infoText.setTranslateY(15);

        Rectangle infoTextRectangle = new Rectangle(0, 0, 20, 20);
        infoTextRectangle.setArcWidth(10);
        infoTextRectangle.setArcHeight(10);
        infoTextRectangle.setStroke(Color.YELLOW);
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

    public static Group createHelpOverlay(ArrayList<Group> helpMarkers) {

        Group helpOverlay = new Group();

        Group backdropGroup = new Group();

        Rectangle backdropFrame = new Rectangle(Sundial.DEFAULT_WIDTH, Sundial.DEFAULT_HEIGHT);
        backdropFrame.setArcWidth(Sunconfig.HELP_OVERLAY_ROUND);
        backdropFrame.setArcHeight(Sunconfig.HELP_OVERLAY_ROUND);
        backdropFrame.setFill(Color.BLACK);
        backdropFrame.setStroke(Sunconfig.Color_Of_Void);
        backdropFrame.setMouseTransparent(true);

        backdropGroup.getChildren().add(backdropFrame);

        for (Group markerGroup : helpMarkers) {

            Rectangle markerRectangle = (Rectangle) markerGroup.getChildren().get(0);

            Rectangle backdropCutout = new Rectangle();
            backdropCutout.setArcWidth(markerRectangle.getArcWidth());
            backdropCutout.setArcHeight(markerRectangle.getArcHeight());
            backdropCutout.setFill(Color.WHITE);
            backdropCutout.setStroke(Color.TRANSPARENT);
            backdropCutout.setMouseTransparent(true);

            Collection<Transform> markerTransforms = markerGroup.getTransforms();
            if (!markerTransforms.isEmpty()) {

                backdropCutout.setX(markerRectangle.getX());
                backdropCutout.setY(markerRectangle.getY());
                backdropCutout.setWidth(markerRectangle.getWidth());
                backdropCutout.setHeight(markerRectangle.getHeight());

                backdropCutout.getTransforms().addAll(markerTransforms);

            } else {

                backdropCutout.xProperty().bind(markerRectangle.xProperty());
                backdropCutout.yProperty().bind(markerRectangle.yProperty());
                backdropCutout.widthProperty().bind(markerRectangle.widthProperty());
                backdropCutout.heightProperty().bind(markerRectangle.heightProperty());
            }

            backdropCutout.visibleProperty().bind((markerRectangle.visibleProperty()));

            backdropGroup.getChildren().add(backdropCutout);
        }

        backdropGroup.setBlendMode(BlendMode.MULTIPLY);
        backdropGroup.setOpacity(Sunconfig.HELP_OVERLAY_OPACITY);
        backdropGroup.setMouseTransparent(true);


        // Value Added Services
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

        // Full help overlay with cutouts and markers
        helpOverlay.getChildren().addAll(backdropGroup, helpWindowMarker, helpGlobeMarker);
        helpOverlay.getChildren().addAll(helpMarkers);
        helpOverlay.setVisible(false);
        helpOverlay.setMouseTransparent(true);

        return helpOverlay;
    }

    public static Group createMiroTextGroup(Text text) {

        text.setText(Sunconfig.MIRO_TEXT);
        text.setFont(Sunconfig.FONT_MINI);
        text.setFill(Color.WHITE);
        text.setStyle(Sunconfig.HELP_MARKER_GLOW);

        text.setX(Sunconfig.CENTER_X - text.getLayoutBounds().getWidth() / 2);
        text.setY(Sunconfig.CENTER_Y * 2 - Sunconfig.MARGIN_Y - Sunconfig.MARKER_HOUR_LENGTH);

        Rectangle rectangle = new Rectangle();
        rectangle.setX(text.getX());
        rectangle.setY(text.getY() - text.getLayoutBounds().getHeight());
        rectangle.setWidth(text.getLayoutBounds().getWidth());
        rectangle.setHeight(text.getLayoutBounds().getHeight());
        rectangle.setFill(Color.TRANSPARENT);
        rectangle.setStroke(Color.TRANSPARENT);

        return new Group(text, rectangle);
    }

    public static Group createHelpMarker(Node node, BooleanProperty visibilityProperty, DoubleProperty opacityProperty) {

        ObservableList<Transform> transformList = node.getTransforms();

        Rectangle rectangle = new Rectangle();
        rectangle.setArcWidth(Sunconfig.HELP_MARKER_ROUND);
        rectangle.setArcHeight(Sunconfig.HELP_MARKER_ROUND);
        rectangle.setFill(Sunconfig.Color_Of_Void);
        rectangle.setStroke(Color.WHITE);
        rectangle.setStyle(Sunconfig.HELP_MARKER_GLOW);

        Circle circle = new Circle(Sunconfig.HELP_MARKER_RADIUS);
        circle.setFill(Sunconfig.Color_Of_Void);
        circle.setStroke(Color.WHITE);
        circle.setStyle(Sunconfig.HELP_MARKER_GLOW);

        Group group = new Group();

        if (!transformList.isEmpty()) {

            double x = node.getLocalToSceneTransform().getMxx() * node.getLayoutBounds().getMinX()
                    + node.getLocalToSceneTransform().getTx();

            double y = node.getLocalToSceneTransform().getMyy() * node.getLayoutBounds().getMinY()
                    + node.getLocalToSceneTransform().getTy();

            double width = node.getLocalToSceneTransform().getMxx() * node.getLayoutBounds().getWidth();
            double height = node.getLocalToSceneTransform().getMyy() * node.getLayoutBounds().getHeight();

            rectangle.setX(x);
            rectangle.setY(y);
            rectangle.setWidth(width);
            rectangle.setHeight(height);

            circle.setCenterX(x + width / 2);
            circle.setCenterY(y + width / 2);

            group.getTransforms().addAll(transformList);

        } else {

            rectangle.xProperty().bind(Bindings.createDoubleBinding(() -> {
                return node.localToSceneTransformProperty().get().getMxx() * node.layoutBoundsProperty().get().getMinX()
                        + node.localToSceneTransformProperty().get().getTx()
                        - Sunconfig.HELP_MARKER_MARGIN / 2;
            }, node.layoutBoundsProperty(), node.localToSceneTransformProperty()));

            rectangle.yProperty().bind(Bindings.createDoubleBinding(() -> {
                return node.localToSceneTransformProperty().get().getMyy() * node.layoutBoundsProperty().get().getMinY()
                        + node.localToSceneTransformProperty().get().getTy()
                        - Sunconfig.HELP_MARKER_MARGIN / 2;
            }, node.layoutBoundsProperty(), node.localToSceneTransformProperty()));

            rectangle.widthProperty().bind(Bindings.createDoubleBinding(() -> {
                return node.localToSceneTransformProperty().get().getMxx() * node.layoutBoundsProperty().get().getWidth()
                        + Sunconfig.HELP_MARKER_MARGIN;
            }, node.localToSceneTransformProperty(), node.layoutBoundsProperty()));

            rectangle.heightProperty().bind(Bindings.createDoubleBinding(() -> {
                return node.localToSceneTransformProperty().get().getMyy() * node.layoutBoundsProperty().get().getHeight()
                        + Sunconfig.HELP_MARKER_MARGIN;
            }, node.localToSceneTransformProperty(), node.layoutBoundsProperty()));


            circle.centerXProperty().bind(Bindings.createDoubleBinding(() -> {
                return node.localToSceneTransformProperty().get().getMxx() * node.layoutBoundsProperty().get().getMinX()
                        + node.localToSceneTransformProperty().get().getTx()
                        + node.localToSceneTransformProperty().get().getMxx() * node.layoutBoundsProperty().get().getWidth() / 2;
            }, node.layoutBoundsProperty(), node.localToSceneTransformProperty()));

            circle.centerYProperty().bind(Bindings.createDoubleBinding(() -> {
                return node.localToSceneTransformProperty().get().getMyy() * node.layoutBoundsProperty().get().getMinY()
                        + node.localToSceneTransformProperty().get().getTy()
                        + node.localToSceneTransformProperty().get().getMyy() * node.layoutBoundsProperty().get().getHeight() / 2;
            }, node.layoutBoundsProperty(), node.localToSceneTransformProperty()));

        }

        if (visibilityProperty != null) {
            rectangle.visibleProperty().bind(visibilityProperty);
            circle.visibleProperty().bind(visibilityProperty);
        } else {
            rectangle.visibleProperty().bind(node.visibleProperty());
            circle.visibleProperty().bind(node.visibleProperty());
        }

        if (visibilityProperty != null) {
            rectangle.opacityProperty().bind(opacityProperty);
            circle.opacityProperty().bind(opacityProperty);
        } else {
            rectangle.opacityProperty().bind(node.opacityProperty());
            circle.opacityProperty().bind(node.opacityProperty());
        }

        group.getChildren().addAll(rectangle/*, circle*/);

        return group;
    }

}
