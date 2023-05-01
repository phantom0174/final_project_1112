package world;

import java.util.ArrayList;

import base.AnimaNode;
import base.Utils;
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


public class GameWorld extends Group implements AnimaNode {
	public ArrayList<Sphere> planetList;
	public ArrayList<Group> appleList;
	public ArrayList<Group> propList;
	
	// --- ambient light --- (used for indicating boarder)
	AmbientLight ambLight = new AmbientLight();
	
	public GameWorld(ArrayList<Sphere> p, ArrayList<Group> a, ArrayList<Group> pr) {
		super();
		
		this.planetList = p;
		this.appleList = a;
		this.propList = pr;

		setupObjects();
		setupLights();
	}

	AnimationTimer spinningPointLight;

	public void setupObjects() {
		/*
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
		*/
		
//		隨機生成星球
		for (int i = 0; i < 100; i++) {
			Sphere planet = createPlanet();
			planetList.add(planet);
			this.getChildren().add(planet);
		}

//		隨機生成蘋果
		for (int i = 0; i < 200; i++) {
			Group apple = createApple();
			appleList.add(apple);
			this.getChildren().add(apple);
		}	
//		
//		隨機生成道具
		for(int i=0 ; i<30 ; i++) {
			Group props = createProps();
			propList.add(props);
			this.getChildren().add(props);
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
		
		ambLight.setLightOn(false);
		this.getChildren().addAll(pl, s, pl2, s2, ambLight);

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
		
		if (Math.abs(x) < 100) x += 100 * Utils.sign((short) x);
		if (Math.abs(y) < 100) y += 100 * Utils.sign((short) y);
		if (Math.abs(z) < 100) z += 100 * Utils.sign((short) z);

		Sphere s = new Sphere(r);
		PhongMaterial m = new PhongMaterial();
		m.setDiffuseColor(Color.color(red, green, blue));
		if (p > 0.5) {
			m.setDiffuseMap(new Image(getClass().getResourceAsStream("/resources/materials/planet1.jpg")));
		} else if (p <= 0.5) {
			m.setDiffuseMap(new Image(getClass().getResourceAsStream("/resources/materials/planet2.jpg")));
		}

		s.setMaterial(m);
		s.setTranslateX(x);
		s.setTranslateY(y);
		s.setTranslateZ(z);
//		s.getTransforms().add(new Translate(x, y, z));
		
		return s;
	}

	public Group createApple() {
		Group apple = new Group();

		double x = (Math.random() - 0.5) * 1000;
		double y = (Math.random() - 0.5) * 1000;
		double z = (Math.random() - 0.5) * 1000;
		
		Sphere s = new Sphere(10);
		PhongMaterial ps = new PhongMaterial(Color.RED);
		s.setMaterial(ps);

		Box b = new Box(4, 10, 4);
		PhongMaterial pb = new PhongMaterial(Color.GREEN);
		b.setMaterial(pb);
		b.setTranslateY(-12);
		
		apple.getChildren().addAll(s, b);
		apple.setTranslateX(x);
		apple.setTranslateY(y);
		apple.setTranslateZ(z);
		
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
			
			s.getTransforms().add(new Translate(rx, ry, rz));
			g.getChildren().add(s);
		}
		
		g.setTranslateX(x);
		g.setTranslateY(y);
		g.setTranslateZ(z);
		
		return g;
	}
}
