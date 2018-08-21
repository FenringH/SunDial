import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Sunface extends Application {

    private static Color Color_Of_Window = new Color(0.65, 0.85, 0.85, 1.0);
    private static Color Color_Of_Earth = new Color(0.85, 0.85, 0.65, 1.0);
    private static Color Color_Of_Darkness = new Color(0.00, 0.00, 0.00, 1.0);

    private static Font Font_Of_Info = new Font(14);
    private static String Path_Of_Earth = "M 100 100 L 300 100 L 200 300 Z M 150 150 L 100 250 L 350 150 Z";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        GregorianCalendar currentCalendar = new GregorianCalendar();
        Date currentTime = new Date();

        // Earth map
        SVGPath pathOfEarth = new SVGPath();
        pathOfEarth.setContent(Path_Of_Earth);
        pathOfEarth.setStroke(Color_Of_Darkness);
        pathOfEarth.setFill(Color_Of_Earth);

        // Text
        Text textCurrentTime = new Text();
        textCurrentTime.setFont(Font_Of_Info);
        textCurrentTime.setX(50);
        textCurrentTime.setY(50);
        textCurrentTime.setText("Current Time: " + currentTime.toString() +
                "\nCurrent day of year: " + currentCalendar.get(Calendar.DAY_OF_YEAR));

        // Root node group
        Group rootNode = new Group();

        ObservableList rootNodeChildren = rootNode.getChildren();
        rootNodeChildren.add(pathOfEarth);
        rootNodeChildren.add(textCurrentTime);

        // And scene...
        Scene testScene = new Scene(rootNode, 600,300);
        testScene.setFill(Color_Of_Window);

        primaryStage.setTitle("SunDial");
        primaryStage.setScene(testScene);

        primaryStage.show();
    }
}
