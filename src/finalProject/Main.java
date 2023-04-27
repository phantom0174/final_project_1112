package finalProject;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.stage.Stage;

public class Main extends Application {
	public int width = 1080, height = 720;
	
	public DoubleProperty xRot = new SimpleDoubleProperty(0),
			yRot = new SimpleDoubleProperty(0);
	
	public DoubleProperty xPos = new SimpleDoubleProperty(0),
		yPos = new SimpleDoubleProperty(-10),
		zPos = new SimpleDoubleProperty(-200);
	
	@Override
	public void start(Stage stage) throws Exception {
		Group group = this.addObjects();
		Scene scene = new Scene(group, width, height, true, SceneAntialiasing.BALANCED);
		scene.setFill(Color.WHITE);
		
		FreeCamera cameraGroup = new FreeCamera();
		stage.setTitle("Camera Testing");
		stage.setScene(scene);
		cameraGroup.bindMovements(stage);
		scene.setCamera(cameraGroup.c);

		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

	public Group addObjects() {
		Box X = new Box(50, 50, 50);
		X.setTranslateX(200);
		X.setMaterial(new PhongMaterial(Color.RED));

		Box Z = new Box(50, 50, 50);
		Z.setTranslateZ(200);
		Z.setMaterial(new PhongMaterial(Color.GREEN));
		
		Box Y = new Box(50, 50, 50);
		Y.setTranslateY(-200);
		Y.setMaterial(new PhongMaterial(Color.BLUE));

		
		Box xaxis = new Box(10000, 3, 3);
		xaxis.setMaterial(new PhongMaterial(Color.RED));
		
		Box zaxis = new Box(3, 3, 10000);
		zaxis.setMaterial(new PhongMaterial(Color.GREEN));

		Box yaxis = new Box(3, 10000, 3);
		yaxis.setMaterial(new PhongMaterial(Color.BLUE));
		

		Group group = new Group();
		group.getChildren().addAll(X, Z, Y, xaxis, zaxis, yaxis);

		return group;
	}
}
