package view;

import java.util.ArrayList;
import java.util.List;

import base.AnimaNode;
import base.Config;
import base.GameStatus;
import base.SoundPlayer;
import base.View;
import camera.FreeCamera;
import camera.SnakeCamera;
import finalProject.Snake;
import world.World0;

import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Sphere;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
	
	// ---- game logic entities ----
	private ArrayList<Sphere> planetList = new ArrayList<>();
	private GameStatus gameStatus = GameStatus.ALIVE;
	
	// ----------------- View -----------------
	
	public boolean isLoaded() {
		return loaded;
	}
	
	public SubScene getSubScene() {
		return this.s;
	}

	public void load() {
		this.root = new World0(planetList);
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
		startGame();
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
		snake.moveHead(new Point3D(0, -10, -200), false);
		for (int i = 0; i < 5; i++) snake.generateBody();
	}
	
	public Timeline frameGenerator;
	public void startGame() {
		double fps = 60;
		var frameDuration = Duration.millis(1000.0 / fps);
		frameGenerator = new Timeline(fps, new KeyFrame(frameDuration, e -> executeFrame()));
		frameGenerator.setCycleCount(Timeline.INDEFINITE);
		frameGenerator.play();
	}
	
	public void executeFrame() {
		boolean isDead = (gameStatus == GameStatus.DEAD);
		
		snake.updateFrameMovement(isDead);
		Point3D headPos = snake.head.getPos();
		
		// if snake has a collision with planet object
		for (Sphere p : planetList) {
			double x = p.translateXProperty().get(),
					y = p.translateYProperty().get(),
					z = p.translateZProperty().get();
			
			double dist = headPos.subtract(new Point3D(x, y, z)).magnitude();
			
			if (dist < p.getRadius() + snake.bodySize) {
				gameStatus = GameStatus.DEAD;
				break;
			}
		}
		
		// recheck isDead?
		if (gameStatus == GameStatus.DEAD) {
			snake.unbindMovements(this.s);
		}
	}

	public void startAnimation() {
		sound.play(bgm);
		((AnimaNode) root).startAnimation();
	}

	public void stopAnimation() {
		((AnimaNode) root).stopAnimation();
	}
}
