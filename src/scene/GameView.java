package scene;

import base.AnimaNode;
import base.Config;
import base.SoundPlayer;
import base.View;
import camera.FreeCamera;
import camera.SnakeCamera;
import finalProject.Snake;
import world.World0;

import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.event.EventHandler;


public class GameView implements View, AnimaNode {
	// ---- view ----
	private boolean loaded = false;
	public SubScene s;
	private World0 root;

	// ---- sounds -----
	private SoundPlayer sound = new SoundPlayer();
	private String bgm = "bgm/my-lonely-journey.mp3";

	// ---- game entities ----
	private boolean camViewToggle = false;
	private FreeCamera freeCamera;
	private SnakeCamera snakeCamera;

	private Snake snake;

	// ---- camera toggle ----
	private EventHandler<KeyEvent> toggleCamera = e -> {
		if (e.getCode() != KeyCode.C) return;

		sound.play("sfx/click.mp3");

		if (!camViewToggle) s.setCamera((Camera) freeCamera.core);
		else s.setCamera((Camera) snakeCamera.core);

		camViewToggle = !camViewToggle;
	};
	
	// ----------------- View -----------------
	
	public boolean isLoaded() {
		return loaded;
	}
	
	public SubScene getSubScene() {
		return this.s;
	}

	public void load() {
		this.root = new World0();
		this.s = new SubScene(this.root, Config.width, Config.height, true, SceneAntialiasing.BALANCED);

		// cameras
		this.freeCamera = new FreeCamera(new PerspectiveCamera(true));
		this.snakeCamera = new SnakeCamera(new PerspectiveCamera(true));

		// snake
		this.snake = new Snake(this.root, snakeCamera);
		this.setupSnake();

		// sounds
		this.sound.load(bgm);
		this.sound.load("sfx/click.mp3");

		this.bindMovements();
		this.startAnimation();
		
		this.loaded = true;
	}
	
	public void unload() {
		// in reverse order of load()
		
		this.unbindMovements();
		this.sound.stop(bgm);
		this.sound.unLoadAll();
		this.snake = null;
		this.snakeCamera = null;
		this.freeCamera = null;
		this.s = null;
		
		this.root.getChildren().clear();
		this.root = null;
		
		this.loaded = false;
	}

	public void bindMovements() {
		freeCamera.bindMovements(this.s);
		s.setCamera((Camera) snakeCamera.core);
		s.addEventFilter(KeyEvent.KEY_PRESSED, toggleCamera);
	}

	public void unbindMovements() {
		// todo: freeCamera.unbindMovements();
		
		s.setCamera(null);
		s.removeEventFilter(KeyEvent.KEY_PRESSED, toggleCamera);
	}
	
	// ----------------- logic -----------------
	
	public void setupSnake() {
		snake.bindMovements(s);
		snake.moveHead(new Point3D(0, -10, -200));
		for (int i = 0; i < 5; i++) snake.generateBody();
	}

	public void startAnimation() {
		sound.play(bgm);
		((AnimaNode) root).startAnimation();
	}

	public void stopAnimation() {
		((AnimaNode) root).stopAnimation();
	}
}
