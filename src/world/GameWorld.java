package world;

import java.util.ArrayList;

import base.AnimaNode;
import base.GameStatus;
import base.Grid2D;
import base.Utils;
import finalProject.Snake;
import javafx.animation.AnimationTimer;
import javafx.geometry.Point3D;
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
	public Grid2D<Sphere> planetGrid;
	public ArrayList<Group> appleList;
	public ArrayList<Group> propList;

	// --- ambient light --- (used for indicating boarder)
	public AmbientLight ambLight = new AmbientLight();

	public GameWorld(Grid2D<Sphere> p, ArrayList<Group> a, ArrayList<Group> pr) {
		super();

		this.planetGrid = p;
		this.appleList = a;
		this.propList = pr;

		setupObjects();
		setupLights();
	}

	public AnimationTimer spinningPointLight;

	public void setupObjects() {
//		隨機生成星球
		for (int i = 0; i < 100; i++) {
			Sphere planet = createPlanet();
			planetGrid.insert(planet);
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
		for (int i = 0; i < 30; i++) {
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

	public Sphere createPlanet() {
		double x = (double) (Math.random() - 0.5) * 1000;
		double y = (double) (Math.random() - 0.5) * 1000;
		double z = (double) (Math.random() - 0.5) * 1000;
		double r = (double) (Math.random() + 1) / 2 * 30;
		double red = (double) Math.random() / 2 + 0.5;
		double green = (double) Math.random() / 2 + 0.5;
		double blue = (double) Math.random() / 2 + 0.5;
		double p = (double) Math.random();

		if (Math.abs(x) < 100)
			x += 100 * Utils.sign((short) x);
		if (Math.abs(y) < 100)
			y += 100 * Utils.sign((short) y);
		if (Math.abs(z) < 100)
			z += 100 * Utils.sign((short) z);

		Sphere s = new Sphere(r);
		PhongMaterial m = new PhongMaterial();
		m.setDiffuseColor(Color.color(red, green, blue));
		if (p > 0.5) {
			m.setDiffuseMap(new Image(getClass().getResourceAsStream("/resources/materials/planet1.jpg")));
		} else if (p <= 0.5) {
			m.setDiffuseMap(new Image(getClass().getResourceAsStream("/resources/materials/planet2.jpg")));
		}

		s.setMaterial(m);
		
		// 因後續獲取座標時會有問題，故不使用 .getTransforms.addAll(...) 
		s.setTranslateX(x);
		s.setTranslateY(y);
		s.setTranslateZ(z);

		return s;
	}

	public Group createApple() {
		Group apple = new Group();

		boolean createSuccess = false;
		while (!createSuccess) {
			double x = (Math.random() - 0.5) * 1000;
			double y = (Math.random() - 0.5) * 1000;
			double z = (Math.random() - 0.5) * 1000;
			
			
			if (coveredByPlanet(new Point3D(x, y, z), 20)) continue;
			createSuccess = true;
			
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
		}

		return apple;
	}

	public Group createProps() {
		Group g = new Group();
		
		PhongMaterial propMaterial = new PhongMaterial();
		propMaterial.setDiffuseMap(new Image(getClass().getResourceAsStream("/resources/materials/lucky_box.png")));
		propMaterial.setSpecularColor(Color.WHITE);
		propMaterial.setSpecularPower(50);
		
		boolean createSuccess = false;
		while(!createSuccess) {
			double x = (double) (Math.random() - 0.5) * 1000;
			double y = (double) (Math.random() - 0.5) * 1000;
			double z = (double) (Math.random() - 0.5) * 1000;
			
			
			if (coveredByPlanet(new Point3D(x, y, z), 40)) continue;
			createSuccess = true;
			
			Box prop = new Box(20, 20, 20);
			prop.setMaterial(propMaterial);
			
			double yRot = Math.random() * 2 * Math.PI;
			prop.getTransforms().add(new Rotate(yRot, Rotate.Y_AXIS));
			
			g.getChildren().add(prop);
			
//			for (int i = 0; i < 30; i++) {
//				Sphere s = new Sphere(0.5);
//				PhongMaterial ps = new PhongMaterial(Color.YELLOW);
//				s.setMaterial(ps);
//				double rx = (double) (Math.random() - 0.5) * 15;
//				double ry = (double) (Math.random() - 0.5) * 15;
//				double rz = (double) (Math.random() - 0.5) * 15;
//				
//				s.getTransforms().add(new Translate(rx, ry, rz));
//				g.getChildren().add(s);
//			}
			
			g.setTranslateX(x);
			g.setTranslateY(y);
			g.setTranslateZ(z);
		}

		return g;
	}
	
	public boolean coveredByPlanet(Point3D p, double radius) {
		for (ArrayList<Sphere> list: planetGrid.query(p)) {
			for (Sphere planet: list) {
				double x = planet.getTranslateX(),
						y = planet.getTranslateY(),
						z = planet.getTranslateZ();
				
				double dist = p.subtract(new Point3D(x, y, z)).magnitude();
				
				if (dist > radius) continue;
				return true;
			}
		}
		return false;
	}
}
