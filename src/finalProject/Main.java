package finalProject;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.stage.Stage;

public class Main extends Application {

	public int width = 1080, height = 720;

	public double ang_roll = 0, ang_flip = 0;

	public double rad = Math.PI * 2;
	public double ang_t = Math.PI / 180;
	
	public double anchorX, anchorY;

	@Override
	public void start(Stage stage) throws Exception {
		Group group = this.addObjects();
		Scene scene = new Scene(group, width, height, true, SceneAntialiasing.BALANCED);
		scene.setFill(Color.WHITE);
		
		
		MyCamera camera = new MyCamera();
		scene.setCamera(camera.c);

		stage.setTitle("Testing...");
		stage.setScene(scene);
		
		
		scene.setOnMousePressed(event -> {
			anchorX = event.getSceneX();
			anchorY = event.getSceneY();
		});
		
        scene.setOnMouseDragged(event -> {
        	double rotDx = (anchorY - event.getSceneY()) / 10;
            double rotDy = (anchorX - event.getSceneX()) / 10;
            camera.xRot.set(camera.xRot.get() + rotDx);
            camera.yRot.set(camera.yRot.get() + rotDy);
            anchorX = event.getSceneX();
			anchorY = event.getSceneY();
        });

		stage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
			Double ang = camera.getRawRot().getY() * ang_t;
			switch (event.getCode()) {
			case W: {
				camera.move(new Point3D(-Math.sin(ang), 0, Math.cos(ang)));
				break;
			}
			case S: {
				camera.move(new Point3D(Math.sin(ang), 0, -Math.cos(ang)));
				break;
			}
			case A: {
				camera.move(new Point3D(-Math.cos(ang), 0, -Math.sin(ang)));
				break;
			}
			case D: {
				camera.move(new Point3D(Math.cos(ang), 0, Math.sin(ang)));
				break;
			}
//			case LEFT: {
//				camera.yRot.set(camera.yRot.get() + 2);
//				break;
//			}
//			case RIGHT: {
//				camera.yRot.set(camera.yRot.get() - 2);
//				break;
//			}
//			case UP: {
//				camera.xRot.set(camera.xRot.get() + 2);
//				break;
//			}
//			case DOWN: {
//				camera.xRot.set(camera.xRot.get() - 2);
//				break;
//			}
			case SPACE: {
				camera.move(camera.getPos().multiply(-1));
				break;
			}
			default:
				break;
			}
		});

		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	public Group addObjects() {
		Sphere s1 = new Sphere(50);
		s1.setMaterial(new PhongMaterial(Color.AQUA));

		Box b1 = new Box(50, 50, 50);
		b1.setTranslateX(-200);
		b1.setMaterial(new PhongMaterial(Color.BLACK));
		
		Box b2 = new Box(50, 50, 50);
		b2.setTranslateX(200);
		b2.setMaterial(new PhongMaterial(Color.RED));
		
		Box b3 = new Box(50, 50, 50);
		b3.setTranslateY(200);
		b3.setMaterial(new PhongMaterial(Color.GREEN));
		
		Box b4 = new Box(50, 50, 50);
		b4.setTranslateZ(200);
		b4.setMaterial(new PhongMaterial(Color.BLUE));
		
		Box b5 = new Box(50, 50, 50);
		b5.setTranslateZ(-200);
		b5.setMaterial(new PhongMaterial(Color.YELLOW));
		
		Box xaxis = new Box(10000, 3, 3);
		xaxis.setMaterial(new PhongMaterial(Color.RED));
		Box yaxis = new Box(3, 10000, 3);
		yaxis.setMaterial(new PhongMaterial(Color.BLUE));
		Box zaxis = new Box(3, 3, 10000);
		zaxis.setMaterial(new PhongMaterial(Color.GREEN));
		

		Group group = new Group();
		group.getChildren().addAll(b1, b2, b3, b4, b5, xaxis, yaxis, zaxis);
		
		return group;
	}
}
