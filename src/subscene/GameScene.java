package subscene;

import base.AnimaNode;
import base.Config;
import camera.FreeCamera;
import camera.SnakeCamera;
import finalProject.Snake;
import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import world.World0;

public class GameScene extends SubScene {
	private static Group mainWorld = new World0();
	
	private FreeCamera fCam = new FreeCamera(new PerspectiveCamera(true));
	private SnakeCamera sCam = new SnakeCamera(new PerspectiveCamera(true));
	
	private Snake theSnake = new Snake(mainWorld, sCam);
	
	// to toggle between fCam and sCam
	private boolean camViewToggle = false;
	
	public GameScene() {
		super(mainWorld, Config.width, Config.height, true, SceneAntialiasing.BALANCED);
		bindMovements();
		
		fCam.bindMovements(this);
		setupSnake();
		startAnimation();
	}
	
	public void bindMovements() {
		this.setCamera((Camera) sCam.core);
		
		this.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
			if (e.getCode() == KeyCode.C) {
				if (camViewToggle) this.setCamera((Camera) fCam.core);
				else this.setCamera((Camera) sCam.core);
				
				camViewToggle = !camViewToggle;
			}
		});
	}
	
	public void setupSnake() {
		theSnake.bindMovements(this);
		theSnake.moveHead(new Point3D(0, -10, -200));
		for (int i = 0; i < 5; i++) theSnake.generateBody();
	}
	
	public void startAnimation() {
		((AnimaNode) mainWorld).startAnimation();
	}

	public void stopAnimation() {
		((AnimaNode) mainWorld).stopAnimation();
	}
}
