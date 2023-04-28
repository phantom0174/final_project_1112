package finalProject;

import javafx.animation.AnimationTimer;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.PointLight;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class TestWorld extends Group implements AnimaNode {
	TestWorld() {
		super();

		setupObjects();
		setupLights();
	}
	
	AnimationTimer spinningPointLight;

	public void setupObjects() {
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

		this.getChildren().addAll(X, Z, Y, xaxis, zaxis, yaxis, woodBox);
	}

	public void setupLights() {
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

		AmbientLight amb = new AmbientLight(Color.WHITE);

		this.getChildren().addAll(pl, s, pl2, s2, amb);
		
		spinningPointLight = new AnimationTimer() {
			@Override
			public void handle(long now) {
				pl.setRotate(pl.getRotate() + 1);
				s.setRotate(s.getRotate() + 1);
				
				pl2.setRotate(pl2.getRotate() + 1);
				s2.setRotate(s2.getRotate() + 1);
			}
		};
	}

	public void setBoxMaterial(Box b, Color c) {
		PhongMaterial pm = new PhongMaterial(c);
		pm.setSpecularColor(c);
		b.setMaterial(pm);
	}

	public void startAnimation() {
		spinningPointLight.start();
	}

	public void stopAnimation() {
		spinningPointLight.start();
	}
}
