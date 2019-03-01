import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;

public class Globe extends Group {

    private static final int SPHERE_DIVISIONS = 256;
    private static final int NUMBER_OF_PARALLELS = 360 / 10;
    private static final int NUMBER_OF_MERIDIANS = 180 / 10;

    private static final double MAP_WIDTH  = 8192 / 2d;
    private static final double MAP_HEIGHT = 4092 / 2d;

    public static final Color Color_Of_Void         = new Color(0.00, 0.00, 0.00, 0.00);
    public static final Color Color_Of_Light        = new Color(1.00, 1.00, 1.00, 1.00);
    public static final Color Color_Of_Transparency = new Color(1.00, 1.00, 1.00, 0.50);

    private static final String DEFAULT_DIFFUSE_MAP = "maps/earth_diffuse_gall-peters_02.jpg";
    private static final Image DEFAULT_DIFFUSE_IMAGE = new Image(DEFAULT_DIFFUSE_MAP, 1003, 639, true, false);

    private double longitude = 0;
    private double latitude = 0;

    private Group globe;
    private Sphere sphere;
    private PhongMaterial globeMaterial;

    private Image diffuseMap;

    private Rotate rotateLongitude;
    private Rotate rotateLatitude;

    public Globe(double radius) {
        this(DEFAULT_DIFFUSE_IMAGE, radius);
    }

    public Globe(Image diffuseMap, double radius) {
        super();
        this.globe = new Group();
        this.diffuseMap = diffuseMap;
        getChildren().add(getGlobe(radius));
    }

    private Group getGlobe(double radius) {

        this.globe = new Group();

        this.rotateLongitude = new Rotate();
        this.rotateLatitude = new Rotate();

        this.rotateLongitude.setAxis(Rotate.Y_AXIS);
        this.rotateLatitude.setAxis(Rotate.X_AXIS);

        sphere = new Sphere(radius, SPHERE_DIVISIONS);
//        sphere.setDrawMode(DrawMode.LINE);
//        sphere.setCullFace(CullFace.NONE);

        globeMaterial = new PhongMaterial();
        globeMaterial.setDiffuseMap(diffuseMap);
//        globeMaterial.setDiffuseColor(Color_Of_Transparency);

        sphere.setMaterial(globeMaterial);
        sphere.getTransforms().add(this.rotateLongitude);

        Group sphereHolder = new Group();
        sphereHolder.getTransforms().add(this.rotateLatitude);
        sphereHolder.getChildren().add(sphere);

        AmbientLight ambientLight = new AmbientLight(Color_Of_Light);

        this.globe.getChildren().addAll(sphereHolder, ambientLight);

        return globe;
    }

    public void rotateGlobe(double longitude, double latitude) {

        this.longitude = longitude;
        this.latitude = latitude;

        this.rotateLongitude.setAngle(this.longitude);
        this.rotateLatitude.setAngle(this.latitude);
    }

    public void setDiffuseMap(Image map) {
        diffuseMap = map;
        globeMaterial.setDiffuseMap(diffuseMap);
    }
}
