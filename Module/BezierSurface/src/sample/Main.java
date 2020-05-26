package sample;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Camera;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main extends Application {
    private static final int WIDTH = 1280;
    private static final int HEIGHT = 960;

    private static final double CAMERA_SPEED = 200;
    private static final double ROTATION_ANGLE = 5;
    private static double anchorX;
    private static double anchorY;
    private static double anchorAngleX = 0;
    private static double anchorAngleY = 0;
    private static DoubleProperty angleX = new SimpleDoubleProperty(0);
    private static DoubleProperty angleY = new SimpleDoubleProperty(0);

    @Override
    public void start(Stage primaryStage) {
        Point.setWindowHeight(HEIGHT);
        List<Point> points = points();

        PointLight pointLight = new PointLight();
        pointLight.setColor(Color.WHITE);

        RotationGroup root = new RotationGroup();
        root.getChildren().add(pointLight);
        root.getChildren().addAll(SurfaceInterpolator.buildBezierSurface(points2x4()));
        root.getChildren().addAll(points);

        Camera camera = new PerspectiveCamera();
        camera.setTranslateX(-600);
        camera.setTranslateZ(-700);

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        scene.setFill(Color.SILVER);
        scene.setCamera(camera);

        primaryStage.setTitle("Bezier Surface");
        primaryStage.setScene(scene);
        initKeyboardControl(primaryStage);
        initMouseControl(primaryStage);
        primaryStage.show();
    }

    private void initMouseControl(Stage stage) {
        Scene scene = stage.getScene();
        Camera camera = scene.getCamera();
        RotationGroup root = (RotationGroup) scene.getRoot();

        Rotate xRotate = new Rotate(0, Rotate.X_AXIS);
        Rotate yRotate = new Rotate(0, Rotate.Y_AXIS);

        root.getTransforms().addAll(xRotate, yRotate);
        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);

        scene.setOnMousePressed(event -> {
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
            anchorAngleX = angleX.get();
            anchorAngleY = angleY.get();
        });

        scene.setOnMouseDragged(event -> {
//            angleX.set(anchorAngleX - anchorY - event.getSceneY());
            angleY.set(anchorAngleY + anchorX - event.getSceneX());
        });

        scene.setOnScroll(event -> {
            if (event.getDeltaY() > 0) {
                camera.setTranslateZ(camera.getTranslateZ() + CAMERA_SPEED);
            } else {
                camera.setTranslateZ(camera.getTranslateZ() - CAMERA_SPEED);
            }
        });
    }

    private void initKeyboardControl(Stage stage) {
        Camera camera = stage.getScene().getCamera();
        RotationGroup root = (RotationGroup) stage.getScene().getRoot();

        stage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case W:
                    root.setTranslateY(root.getTranslateY() + CAMERA_SPEED);
                    break;
                case S:
                    root.setTranslateY(root.getTranslateY() - CAMERA_SPEED);
                    break;
                case D:
                    root.setTranslateX(root.getTranslateX() - CAMERA_SPEED);
                    break;
                case A:
                    root.setTranslateX(root.getTranslateX() + CAMERA_SPEED);
                    break;
                case UP:
                    root.rotateByX(-ROTATION_ANGLE);
                    break;
                case DOWN:
                    root.rotateByX(ROTATION_ANGLE);
                    break;
                case RIGHT:
                    root.rotateByY(ROTATION_ANGLE);
                    break;
                case LEFT:
                    root.rotateByY(-ROTATION_ANGLE);
                    break;
            }
        });
    }

    private List<Point> points() {
        return new ArrayList<>(Arrays.asList(
                new Point(100, 100, 200, Color.RED),
                new Point(200, 300, 200, Color.RED),
                new Point(300, 200, 100, Color.RED),
                new Point(400, 400, 100, Color.RED),
                new Point(500, 100, 100, Color.RED),
                new Point(600, 400, 200, Color.RED),
                new Point(700, 300, 200, Color.RED),
                new Point(900, 100, 0, Color.RED)
        ));
    }

    private List<List<Point>> points2x4() {
        List<List<Point>> points = new ArrayList<>();

        points.add(new ArrayList<>(Arrays.asList(
                new Point(100, 100, 200, Color.RED),
                new Point(200, 300, 200, Color.RED),
                new Point(300, 200, 100, Color.RED),
                new Point(400, 400, 100, Color.RED)
        )));
        points.add(new ArrayList<>(Arrays.asList(
                new Point(500, 100, 100, Color.RED),
                new Point(600, 400, 200, Color.RED),
                new Point(700, 300, 200, Color.RED),
                new Point(900, 100, 0, Color.RED)
        )));

        return points;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
