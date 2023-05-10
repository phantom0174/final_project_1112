package world;


/*

遊戲主世界的自動化生成，包括：

	1. 實體生成
	2. 光照生成
	3. 互動物生成

被動被 GameView 操控。

*/


import java.util.ArrayList;

import base.AnimaNode;
import base.Config;
import base.GameDiff;
import base.Grid2D;
import base.ModelLoader;
import base.Utils;
import javafx.animation.AnimationTimer;
import javafx.geometry.Point3D;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.Node;
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
	
	// ----------------- animations --------------
	public AnimationTimer spinningPointLight;
	public ArrayList<AnimationTimer> spinningApple = new ArrayList<>();
	public ArrayList<AnimationTimer> spinningProps = new ArrayList<>();

	public void setupObjects() {
		
//		隨機生成星球
		int planetNum, radius = 50;
		if (Config.difficulty == GameDiff.EASY) {
			planetNum = 50;
		} else if (Config.difficulty == GameDiff.MEDIUM) {
			planetNum = 100;
			radius *= 1.2;
		} else if (Config.difficulty == GameDiff.HARD) {
			planetNum = 150;
			radius *= 1.4;
		} else {
			planetNum = 200;
			radius *= 1.6;
		}
		
		// 隨機生成星球
		for (int i = 0; i < planetNum; i++) {
			Sphere planet = createPlanet(radius);
			planetGrid.insert(planet);
			this.getChildren().add(planet);
		}

		// 隨機生成蘋果
		
		setAppleModel();
		for (int i = 0; i < 12; i++) {
			Group apple = createApple();
			appleList.add(apple);
			this.getChildren().add(apple);
		}
	
//		隨機生成道具

		// 隨機生成道具
		for (int i = 0; i < 30; i++) {
			Group props = createProps();
			propList.add(props);
			this.getChildren().add(props);
		}
		
		// 隨機生成星星（點照明）
//		setStarModel();
//		for (int i = 0; i < 3; i++) {
//			this.getChildren().add(createStars());
//		}
	}

	public void setupLights() {
		this.getChildren().add(ambLight);
		
//		PointLight pl = new PointLight();
//		pl.setColor(Color.WHITE);
//		pl.getTransforms().add(new Translate(-10000, 0, 0));
//		pl.setLinearAttenuation(1 / 1000);
//		this.getChildren().add(pl);
//		
		
		/*
		PointLight pl = new PointLight();
		pl.setColor(Color.WHITE);
		pl.getTransforms().add(new Translate(0, 0, -400));
		pl.setRotationAxis(Rotate.X_AXIS);

		Sphere s = new Sphere(5);
		s.getTransforms().addAll(pl.getTransforms());
		s.setRotationAxis(Rotate.X_AXIS);

		PointLight pl2 = new PointLight();
		pl2.setColor(Color.WHITE);
		pl2.getTransforms().add(new Translate(400, 0, 0));
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
		*/
	}

	public void setBoxMaterial(Box b, Color c) {
		PhongMaterial pm = new PhongMaterial(c);
		pm.setSpecularColor(c);
		b.setMaterial(pm);
	}

	public void startAnimation() {
//		spinningPointLight.start();
		for (AnimationTimer a: spinningProps) {
			a.start();
		}
		for (AnimationTimer a: spinningApple) {
			a.start();
		}
	}

	public void stopAnimation() {
//		spinningPointLight.start();
		for (AnimationTimer a: spinningProps) {
			a.stop();
		}
		for (AnimationTimer a: spinningApple) {
			a.stop();
		}
	}

	public Sphere createPlanet(int radius) {
		boolean createSuccess = false;
		while (!createSuccess) {
			double x = (double) (Math.random() - 0.5) * 1000;
			double y = (double) (Math.random() - 0.5) * 1000;
			double z = (double) (Math.random() - 0.5) * 1000;
			double r = (double) (Math.random() + 1) / 2 * radius;
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
			
			
			if (collisionWithPlanet(new Point3D(x, y, z), r)) continue;
			createSuccess = true;
			

			Sphere s = new Sphere(r);
			PhongMaterial m = new PhongMaterial();
			m.setDiffuseColor(Color.color(red, green, blue));
//			m.setSpecularPower(4);
			
//			m.setSpecularColor(Color.GRAY);
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
		return null;
	}
	
	PhongMaterial ps = new PhongMaterial();
    
	/*
	
	大小對應：
		radius: 10 -> setScale(0.27)
		
	中心點調整：
		yTrans += 5
	
	*/
	
	private ModelLoader appleModel = new ModelLoader("apple", "obj");
	
	private void setAppleModel() {
		appleModel.setDiff("diffuse.png");
		appleModel.setSpec("specular.png");
		appleModel.material.setSpecularPower(128);
	}
	
	public Group createApple() {
		boolean createSuccess = false;
		while (!createSuccess) {
			double x = (Math.random() - 0.5) * 1000;
			double y = (Math.random() - 0.5) * 1000;
			double z = (Math.random() - 0.5) * 1000;
			
			Point3D pos = new Point3D(x, y, z);
			if (
				collisionWithPlanet(pos, 20)
				|| collisionWithGroup(appleList, pos, 20 + 10) // 10 is the apple size
			) continue;
			createSuccess = true;
		    
			Group appleCore = appleModel.getMesh();
		    
		    appleCore.setScaleX(0.27);
		    appleCore.setScaleY(0.27);
		    appleCore.setScaleZ(0.27);
		    
		    appleCore.setTranslateY(5);
		    
		    
		    // random rotation
		    Point3D randAxis = new Point3D(Math.random(), Math.random(), Math.random());
		    appleCore.setRotationAxis(randAxis);
		    
		    double spinningSpeed = Math.random() * 2 + 1;
		    spinningApple.add(new AnimationTimer() {
				@Override
				public void handle(long arg0) {
					appleCore.setRotate(appleCore.getRotate() + spinningSpeed);
				}
			});
		    
		    Group appleShell = new Group();
			
		    appleShell.getChildren().addAll(appleCore);
		    appleShell.setTranslateX(x);
		    appleShell.setTranslateY(y);
		    appleShell.setTranslateZ(z);
		    
		    return appleShell;
		}
		return null;
	}
	
	
	private ModelLoader starModel = new ModelLoader("star", "stl");
	
	private void setStarModel() {
		starModel.material.setDiffuseColor(Color.WHITE);
		starModel.material.setSpecularColor(Color.WHITE);
	}
	
	public Group createStars() {
		double x = (Math.random() - 0.5) * 2000;
		double y = (Math.random() - 0.5) * 2000;
		double z = (Math.random() - 0.5) * 2000;
		
		PointLight starLight = new PointLight(Color.WHITE);
		
		double intensity = 5;
		
		starLight.setConstantAttenuation(1 / intensity);
		starLight.setQuadraticAttenuation((1 / intensity) / 10000);
		
	    starLight.getTransforms().add(new Translate(x, y, z));
	    
		Group starCore = starModel.getMesh();
		
		starCore.setScaleX(0.2);
		starCore.setScaleY(0.2);
		starCore.setScaleZ(0.2);
		
		Point3D randAxis = new Point3D(Math.random(), Math.random(), Math.random());
		starCore.setRotationAxis(randAxis);
		starCore.setRotate(Math.random() * 360);
		
		starCore.getTransforms().addAll(starLight.getTransforms());
	    
		// packaging
		Group starEntity = new Group();
		starEntity.getChildren().addAll(starLight, starCore);
		return starEntity;
	}

	public Group createProps() {
		Group g = new Group();
		
		PhongMaterial propMaterial = new PhongMaterial();
		propMaterial.setDiffuseColor(Color.WHITE);
		propMaterial.setDiffuseMap(new Image(getClass().getResourceAsStream("/resources/materials/lucky_box.png")));
		propMaterial.setSpecularColor(Color.YELLOW);
//		propMaterial.setSpecularPower(256);
		
		
//		propMaterial.setSelfIlluminationMap(new Image(getClass().getResourceAsStream("/resources/materials/lucky_box_lumi.png")));
//		propMaterial.setSpecularColor(Color.WHITE);
//		propMaterial.setSpecularPower(100);
		
		boolean createSuccess = false;
		while(!createSuccess) {
			double x = (double) (Math.random() - 0.5) * 1000;
			double y = (double) (Math.random() - 0.5) * 1000;
			double z = (double) (Math.random() - 0.5) * 1000;
			
			
			Point3D pos = new Point3D(x, y, z);
			if (
				collisionWithPlanet(pos, 40)
				|| collisionWithGroup(appleList, pos, 50)
				|| collisionWithGroup(propList, pos, 200)
			) continue;
			createSuccess = true;
			
			Box prop = new Box(20, 20, 20);
			prop.setMaterial(propMaterial);
			
			// initial random rotation
			double yRot = Math.random() * 2 * Math.PI;
			prop.setRotationAxis(Rotate.Y_AXIS);
			prop.getTransforms().add(new Rotate(yRot, Rotate.Y_AXIS));
			
			// rotate animation
			spinningProps.add(new AnimationTimer() {
				@Override
				public void handle(long arg0) {
					prop.setRotate(prop.getRotate() + 0.3);
				}
			});
			
			g.getChildren().add(prop);
			
			g.setTranslateX(x);
			g.setTranslateY(y);
			g.setTranslateZ(z);
		}

		return g;
	}
	
	public boolean collisionWithPlanet(Point3D p, double radius) {
		for (ArrayList<Sphere> list: planetGrid.query(p)) {
			for (Sphere planet: list) {
				double x = planet.getTranslateX(),
						y = planet.getTranslateY(),
						z = planet.getTranslateZ();
				
				double dist = p.subtract(new Point3D(x, y, z)).magnitude();
				
				if (dist - radius - planet.getRadius() < 0) return true;
			}
		}
		return false;
	}
	
	public boolean collisionWithGroup(ArrayList<Group> list, Point3D p, double radius) {
		for (Group g: list) {
			double x = g.getTranslateX(),
					y = g.getTranslateY(),
					z = g.getTranslateZ();
			
			double dist = p.subtract(new Point3D(x, y, z)).magnitude();
			
			// 10 is the apple radius
			if (dist < radius) return true;
		}
		return false;
	}
}
