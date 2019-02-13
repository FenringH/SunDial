import javafx.geometry.Point3D;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;

public class Globe extends Group {

    private static final int SPHERE_DIVISIONS = 360 / 10;
    private static final int NUMBER_OF_PARALLELS = 360 / 10;
    private static final int NUMBER_OF_MERIDIANS = 180 / 10;

    private static final double MAP_WIDTH  = 8192 / 2d;
    private static final double MAP_HEIGHT = 4092 / 2d;

    public static final Color Color_Of_Void         = new Color(0.00, 0.00, 0.00, 0.00);
    public static final Color Color_Of_Light        = new Color(1.00, 1.00, 1.00, 1.00);
    public static final Color Color_Of_Transparency = new Color(1.00, 1.00, 1.00, 0.50);

    private static final String DIFFUSE_MAP = "maps/patterson-political-v3-big.png";

    private double longitude = 0;
    private double latitude = 0;

    private Group globe;

    private Rotate rotateLongitude;
    private Rotate rotateLatitude;

    public Globe(double radius) {

        super();

        this.globe = new Group();

        getChildren().add(getGlobe(radius));
    }

    private Group getGlobe(double radius) {

        this.globe = new Group();

        this.rotateLongitude = new Rotate();
        this.rotateLatitude = new Rotate();

        this.rotateLongitude.setAxis(Rotate.Y_AXIS);
        this.rotateLatitude.setAxis(Rotate.X_AXIS);

        Sphere sphere = new Sphere(radius, SPHERE_DIVISIONS);
//        sphere.setDrawMode(DrawMode.LINE);
//        sphere.setCullFace(CullFace.BACK);

        PhongMaterial earthMaterial = new PhongMaterial();
        Image earthDiffuseMap = new Image(DIFFUSE_MAP, 1003, 617, true, false);
        earthMaterial.setDiffuseMap(earthDiffuseMap);
//        earthMaterial.setDiffuseColor(Color_Of_Transparency);
        sphere.setMaterial(earthMaterial);
        sphere.getTransforms().add(this.rotateLongitude);

        Group sphereHolder = new Group();
        sphereHolder.getTransforms().add(this.rotateLatitude);
        sphereHolder.getChildren().add(sphere);

        AmbientLight ambientLight = new AmbientLight(Color_Of_Light);

        this.globe.getChildren().addAll(sphereHolder, ambientLight);

/*
        for (int i = 0; i < NUMBER_OF_PARALLELS; i++) {
            Circle parallel = new Circle(0, 0, radius);
            Rotate parallelRotate = new Rotate();
            parallelRotate.setPivotX(0);
            parallelRotate.setPivotY(0);
            parallelRotate.setAxis(Rotate.Y_AXIS);
            parallel.getTransforms().add(parallelRotate);
            parallelRotate.setAngle(i * 360 / NUMBER_OF_PARALLELS);
            parallel.setFill(Color_Of_Void);
            parallel.setStroke(Color_Of_Light);
            this.globe.getChildren().add(parallel);
        }
*/


        return globe;
    }

    public void rotateGlobe(double longitude, double latitude) {

        this.longitude = longitude;
        this.latitude = latitude;

        this.rotateLongitude.setAngle(this.longitude);
        this.rotateLatitude.setAngle(this.latitude);
    }
}
