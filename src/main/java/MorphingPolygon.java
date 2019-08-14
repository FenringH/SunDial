import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collection;

public class MorphingPolygon extends Polygon {

    private DoubleProperty morphPosition;

    private ArrayList<Double> startPointList;
    private ArrayList<Double> endPointList;
    private double startOpacity;
    private double endOpacity;

    private Timeline timelineOut;
    private Timeline timelineIn;

    public MorphingPolygon(
            Collection<Double> startPoints
            , Collection<Double> endPoints
            , long duration
            , Interpolator interpolator)
    {

        super();

        this.startOpacity = 1.0;
        this.endOpacity = 1.0;

        this.startPointList = new ArrayList<>(startPoints);
        this.endPointList = new ArrayList<>(endPoints);

        morphPosition = new SimpleDoubleProperty(0f);
        morphPosition.addListener((observable, oldValue, newValue) -> updatePoints());


        timelineOut = new Timeline();
        timelineOut.setCycleCount(1);
        timelineOut.setRate(1);
        timelineOut.setAutoReverse(false);

        KeyValue keyValueMorphOut = new KeyValue(morphPosition, 1, interpolator);
        KeyFrame keyFrameMorphOut = new KeyFrame(Duration.millis(duration), keyValueMorphOut);

        timelineOut.getKeyFrames().add(keyFrameMorphOut);


        timelineIn = new Timeline();
        timelineIn.setCycleCount(1);
        timelineIn.setRate(1);
        timelineIn.setAutoReverse(false);

        KeyValue keyValueMorphIn = new KeyValue(morphPosition, 0, interpolator);
        KeyFrame keyFrameMorphIn = new KeyFrame(Duration.millis(duration), keyValueMorphIn);

        timelineIn.getKeyFrames().add(keyFrameMorphIn);


        super.getPoints().addAll(this.startPointList);
    }

    public MorphingPolygon(
            Collection<Double> startPoints
            , Collection<Double> endPoints
            , double startOpacity
            , double endOpacity
            , long duration
            , Interpolator interpolator)
    {
        this(startPoints, endPoints, duration, interpolator);

        this.startOpacity = startOpacity;
        this.endOpacity = endOpacity;

        this.setOpacity(startOpacity);
    }

    private void updatePoints() {

        int size = startPointList.size() <= endPointList.size() ? startPointList.size() : endPointList.size();

        double change = morphPosition.get();

        for (int i = 0; i < size; i++) {
            double interimPoint = startPointList.get(i) * (1 - change) + endPointList.get(i) * change;
            super.getPoints().set(i, interimPoint);
        }

        if (startOpacity != endOpacity) {
            double interimOpacity = startOpacity * (1 - change) + endOpacity * change;
            super.setOpacity(interimOpacity);
        }
    }


    public void setRate(double value) {
        timelineOut.setRate(value);
        timelineIn.setRate(value);
    }

    public void playOut() {
        timelineOut.play();
    }

    public void stopOut() {
        timelineOut.stop();
    }

    public void playIn() {
        timelineIn.play();
    }

    public void stopIn() {
        timelineIn.stop();
    }

    public void stop() {
        timelineIn.stop();
        timelineOut.stop();
    }


}
