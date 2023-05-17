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
import base.MaterialLoader;
import base.ModelLoader;
import base.Utils;
import javafx.animation.AnimationTimer;
import javafx.geometry.Point3D;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;


public class GameWorld extends Group implements AnimaNode {
	public Grid2D<Sphere> planetGrid;
	public ArrayList<Group> appleList;
	public ArrayList<Group> propList;
	
	public MaterialLoader matLoader = new MaterialLoader("game");

	// --- ambient light --- (used for indicating boarder)
	public AmbientLight ambLight = new AmbientLight();

	public GameWorld(Grid2D<Sphere> p, ArrayList<Group> a, ArrayList<Group> pr) {
		super();

		this.planetGrid = p;
		this.appleList = a;
		this.propList = pr;

		setupLights();
		setupObjects();
	}
	
	// ----------------- animations --------------
	public ArrayList<AnimationTimer> spinningApple = new ArrayList<>();
	public ArrayList<AnimationTimer> spinningProps = new ArrayList<>();

	public void setupObjects() {
		int planetNum, radius = 50;
		if (Config.difficulty == GameDiff.EASY) {
			planetNum = 60;
			Config.scoreMultiplier = 1;
		} else if (Config.difficulty == GameDiff.MEDIUM) {
			planetNum = 90;
			radius *= 1.2;
			Config.scoreMultiplier = 1.4;
		} else if (Config.difficulty == GameDiff.HARD) {
			planetNum = 120;
			radius *= 1.4;
			Config.scoreMultiplier = 1.8;
		} else {
			planetNum = 150;
			radius *= 1.6;
			Config.scoreMultiplier = 2.2;
		}
		
//		this.getChildren().add(createTransientPlanet());
		
		// 隨機生成星球
		for (int i = 0; i < planetNum; i++) {
			Sphere planet = createPlanet(radius);
			planetGrid.insert(planet);
			this.getChildren().add(planet);
		}

		// 隨機生成蘋果
		setAppleModel();
		for (int i = 0; i < 10; i++) {
			Group apple = createApple();
			appleList.add(apple);
			this.getChildren().add(apple);
		}

		// 隨機生成道具
		for (int i = 0; i < 30; i++) {
			Group props = createProps();
			propList.add(props);
			this.getChildren().add(props);
		}
	}

	public void setupLights() {
		ambLight.setLightOn(false);
		this.getChildren().add(ambLight);
	}

	public void startAnimation() {
		for (AnimationTimer a: spinningProps) {
			a.start();
		}
		for (AnimationTimer a: spinningApple) {
			a.start();
		}
	}

	public void stopAnimation() {
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
			double x = Utils.rand(-500, 500),
					y = Utils.rand(-500, 500),
					z = Utils.rand(-500, 500),
					r = Utils.rand(radius / 2, radius);
			
			if (Math.abs(x) < 100)
				x += 100 * Utils.sign((short) x);
			if (Math.abs(y) < 100)
				y += 100 * Utils.sign((short) y);
			if (Math.abs(z) < 100)
				z += 100 * Utils.sign((short) z);
			
			
			if (collisionWithPlanet(new Point3D(x, y, z), r)) continue;
			createSuccess = true;
			
			double red = Utils.rand(0.5, 0.9),
					green = Utils.rand(0.5, 0.9),
					blue = Utils.rand(0.5, 0.9);
			
			
			PhongMaterial m = new PhongMaterial();
			m.setDiffuseColor(Color.color(red, green, blue));
			
			// random index: picture 3 ~ 8 -> rand(3, 9)
			int diffIndex = (int) Utils.rand(3, 9);
			m.setDiffuseMap(matLoader.get("planet" + diffIndex + ".jpg"));
			
			Sphere s = new Sphere(r);
			s.setMaterial(m);

			// 因後續獲取座標時會有問題，故不使用 .getTransforms.addAll(...) 
			s.setTranslateX(x);
			s.setTranslateY(y);
			s.setTranslateZ(z);

			return s;
		}
		return null;
	}
	
	public Sphere createTransientPlanet() {
		Sphere transientPlanet = new Sphere(100);
		
		PhongMaterial pm = new PhongMaterial();
		pm.setDiffuseMap(matLoader.get("planet6.jpg"));
		
		transientPlanet.setMaterial(pm);
		return transientPlanet;
	}
	
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
			double x = Utils.rand(-500, 500),
					y = Utils.rand(-500, 500),
					z = Utils.rand(-500, 500);
			
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
		    
		    double spinningSpeed = Utils.rand(2, 3);
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
	
	public Group createProps() {
		Group g = new Group();
		
		PhongMaterial propMaterial = new PhongMaterial();
		propMaterial.setDiffuseColor(Color.WHITE);
		propMaterial.setDiffuseMap(matLoader.get("lucky_box.png"));
		propMaterial.setSpecularColor(Color.YELLOW);
		
		boolean createSuccess = false;
		while(!createSuccess) {
			double x = Utils.rand(-500, 500),
					y = Utils.rand(-500, 500),
					z = Utils.rand(-500, 500);
			
			
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
			double yRot = Utils.rand(0, 2 * Math.PI);
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
	
	@Override
	protected void finalize() {
	    System.out.println("Game World recycled!");
	}
}
