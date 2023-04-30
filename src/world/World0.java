package world;

import base.AnimaNode;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.util.Duration;

public class World0 extends Group implements AnimaNode {
	public World0() {
		super();

		setupObjects();
		setupLights();
	}

	AnimationTimer spinningPointLight;
	Timeline ambLightAni;

	public void setupObjects() {
		Box woodBox = new Box(50, 50, 50);

		PhongMaterial pm = new PhongMaterial();
		pm.setDiffuseMap(new Image(getClass().getResourceAsStream("/resources/materials/diffuse.jpg")));
		pm.setSpecularMap(new Image(getClass().getResourceAsStream("/resources/materials/specular.jpg")));

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
		
		for (int d = 1; d <= 3; d++) {
			for (int i = -5; i <= 5; i++) {
				int perm1Pos = i * 200;
				for (int j = -5; j <= 5; j++) {
					int perm2Pos = j * 200;
					
					Box grid = new Box(
						(d == 1) ? 2200 : 0.2,
						(d == 2) ? 2200 : 0.2,
						(d == 3) ? 2200 : 0.2
					);
					
					if (d == 1) {
						grid.getTransforms().add(new Translate(0, perm1Pos, perm2Pos));
					} else if (d == 2) {
						grid.getTransforms().add(new Translate(perm1Pos, 0, perm2Pos));
					} else {
						grid.getTransforms().add(new Translate(perm1Pos, perm2Pos, 0));
					}
					
					setBoxMaterial(grid, Color.LIGHTGRAY);
					
					this.getChildren().add(grid);
				}
			}
		}
		
//		隨機生成星球
		for (int i = 0; i < 100; i++) {
			this.getChildren().add(createPlanet());
		}

//		隨機生成蘋果
		for (int i = 0; i < 200; i++) {
			this.getChildren().add(createApple());
		}	
//		
//		隨機生成道具
		for(int i=0 ; i<30 ; i++) {
			this.getChildren().add(createProps());
		}
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

//		AmbientLight amb = new AmbientLight(Color.WHITE);

		this.getChildren().addAll(pl, s, pl2, s2);

		spinningPointLight = new AnimationTimer() {
			@Override
			public void handle(long now) {
				pl.setRotate(pl.getRotate() + 1);
				s.setRotate(s.getRotate() + 1);

				pl2.setRotate(pl2.getRotate() + 1);
				s2.setRotate(s2.getRotate() + 1);
			}
		};

//		ambLightAni = new Timeline(new KeyFrame(Duration.ZERO, e -> {
//			amb.setLightOn(false);
//		}), new KeyFrame(Duration.seconds(3), e -> {
//			amb.setLightOn(true);
//		}), new KeyFrame(Duration.seconds(8), e -> {
//			amb.setLightOn(false);
//		}));
//		ambLightAni.setCycleCount(Timeline.INDEFINITE);
	}

	public void setBoxMaterial(Box b, Color c) {
		PhongMaterial pm = new PhongMaterial(c);
		pm.setSpecularColor(c);
		b.setMaterial(pm);
	}

	public void startAnimation() {
		spinningPointLight.start();
//		ambLightAni.play();
	}

	public void stopAnimation() {
		spinningPointLight.start();
//		ambLightAni.stop();
	}

	public Sphere createPlanet() {
		double x = (double) (Math.random() - 0.5) * 1000;
		double y = (double) (Math.random() - 0.5) * 1000;
		double z = (double) (Math.random() - 0.5) * 1000;
		double r = (double) (Math.random() + 1)/2 * 30;
		double red = (double) Math.random() / 2 + 0.5;
		double green = (double) Math.random() / 2 + 0.5;
		double blue = (double) Math.random() / 2 + 0.5;
		double p = (double) Math.random();

		Sphere s = new Sphere(r);
		PhongMaterial m = new PhongMaterial();
		m.setDiffuseColor(Color.color(red, green, blue));
		if (p > 0.5) {
			m.setDiffuseMap(new Image(getClass().getResourceAsStream("/resources/materials/planet1.jpg")));
		} else if (p <= 0.5) {
			m.setDiffuseMap(new Image(getClass().getResourceAsStream("/resources/materials/planet2.jpg")));
		}

		s.setMaterial(m);
		s.getTransforms().add(new Translate(x, y, z));

		return s;
	}

	public Group createApple() {
		Group apple = new Group();

		double x = (Math.random() - 0.5) * 1000;
		double y = (Math.random() - 0.5) * 1000;
		double z = (Math.random() - 0.5) * 1000;

		Sphere s = new Sphere(3);
		PhongMaterial ps = new PhongMaterial(Color.RED);
		s.setMaterial(ps);
		s.getTransforms().add(new Translate(x, y, z));

		Box b = new Box(1, 2.5, 1);
		PhongMaterial pb = new PhongMaterial(Color.GREEN);
		b.setMaterial(pb);
		b.getTransforms().add(new Translate(x, y - 3, z));

		apple.getChildren().addAll(s, b);
		return apple;
	}
	
	public Group createProps(){
		Group g = new Group();
		double x = (double)(Math.random() - 0.5) * 1000;
		double y = (double)(Math.random() - 0.5) * 1000;
		double z = (double)(Math.random() - 0.5) * 1000;
		
		for(int i=0 ; i<30 ; i++) {
			Sphere s = new Sphere(0.5);
			PhongMaterial ps = new PhongMaterial(Color.YELLOW);
			s.setMaterial(ps);
			double rx = (double)(Math.random() - 0.5) * 15;
			double ry = (double)(Math.random() - 0.5) * 15;
			double rz = (double)(Math.random() - 0.5) * 15;
			s.getTransforms().add(new Translate(x + rx, y + ry, z + rz));
			g.getChildren().add(s);
		}
		return g;
	}
}
