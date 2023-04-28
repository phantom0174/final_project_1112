package finalProject;


import javafx.application.Application;
import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {
	public int width = 1080, height = 720;
	
	@Override
	public void start(Stage stage) throws Exception {
		TestWorld testWorld = new TestWorld();
		
		Scene scene = new Scene(testWorld, width, height, true, SceneAntialiasing.BALANCED);
		scene.setFill(Color.LIGHTGRAY);
		
		// the camera
		FreeCamera cameraGroup = new FreeCamera(new PerspectiveCamera(true));
		cameraGroup.bindMovements(scene);
		scene.setCamera((Camera) cameraGroup.core);
		
		// the snake!
		Snake mySnake = new Snake(testWorld);
		mySnake.moveHead(new Point3D(0, -10, -200));
		for (int i = 0; i < 5; i++) mySnake.generateBody();
		mySnake.keyBinding(scene);
		
		
		stage.setTitle("Scene Testing");
		stage.setScene(scene);
		stage.show();
		
		testWorld.startAnimation();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
