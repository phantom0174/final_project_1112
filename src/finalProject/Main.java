package finalProject;


import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
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
		
		
		// light lab
		PointLight pl = new PointLight();
		pl.setColor(Color.WHITE);
		pl.getTransforms().add(new Translate(0, 0, -80));
		pl.setRotationAxis(Rotate.X_AXIS);
		
		Sphere s = new Sphere(5);
		s.getTransforms().addAll(pl.getTransforms());
		s.setRotationAxis(Rotate.X_AXIS);
		
		
		PointLight pl2 = new PointLight();
		pl2.setColor(Color.WHITE);
		pl2.getTransforms().add(new Translate(80, 0, 0));
		pl2.setRotationAxis(Rotate.X_AXIS);
		
		Sphere s2 = new Sphere(5);
		s2.getTransforms().addAll(pl2.getTransforms());
		s2.setRotationAxis(Rotate.Z_AXIS);
		//
		group.getChildren().addAll(pl, s, pl2, s2);
		
		
		
		Scene scene = new Scene(group, width, height, true, SceneAntialiasing.BALANCED);
		scene.setFill(Color.LIGHTGRAY);
		
		FreeCamera cameraGroup = new FreeCamera();
		cameraGroup.bindMovements(scene);
		
		stage.setTitle("Camera Testing");
		stage.setScene(scene);
		scene.setCamera(cameraGroup.c);

		stage.show();
		
		
		AnimationTimer timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				pl.setRotate(pl.getRotate() + 1);
				s.setRotate(s.getRotate() + 1);
				
				pl2.setRotate(pl2.getRotate() + 1);
				s2.setRotate(s2.getRotate() + 1);
			}
		};
		timer.start();
	}

	public static void main(String[] args) {
		launch(args);
	}

	public Group addObjects() {
		Box woodBox = new Box(50, 50, 50);
		
		PhongMaterial pm = new PhongMaterial();
		pm.setDiffuseMap(new Image(getClass().getResourceAsStream("/materials/diffuse.jpg")));
		pm.setSpecularMap(new Image(getClass().getResourceAsStream("/materials/specular.jpg")));
		
		woodBox.setMaterial(pm);
		
		Box X = new Box(50, 50, 50);
		X.setTranslateX(200);
		setBoxMaterial(X, Color.RED);

		Box Z = new Box(50, 50, 50);
		Z.setTranslateZ(200);
		setBoxMaterial(Z, Color.GREEN);
		
		Box Y = new Box(50, 50, 50);
		Y.setTranslateY(-200);
		setBoxMaterial(Y, Color.BLUE);

		
		Box xaxis = new Box(10000, 3, 3);
		setBoxMaterial(xaxis, Color.RED);
		
		Box zaxis = new Box(3, 3, 10000);
		setBoxMaterial(zaxis, Color.GREEN);

		Box yaxis = new Box(3, 10000, 3);
		setBoxMaterial(yaxis, Color.BLUE);
		

		Group group = new Group();
		group.getChildren().addAll(X, Z, Y, xaxis, zaxis, yaxis, woodBox);

		return group;
	}
	
	public void setBoxMaterial(Box b, Color c) {
		PhongMaterial pm = new PhongMaterial(c);
		pm.setSpecularColor(c);
		b.setMaterial(pm);
	}
}
