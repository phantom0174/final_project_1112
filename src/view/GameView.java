package view;

import java.util.ArrayList;
import java.util.Stack;

import base.AnimaNode;
import base.Config;
import base.GameStatus;
import base.Grid2D;
import base.SoundPlayer;
import base.View;
import camera.SnakeCamera;
import finalProject.Snake;
import world.GameWorld;

import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Sphere;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;


public class GameView implements View, AnimaNode {
	// ---- view ----
	private boolean loaded = false;
	public SubScene s;
	private GameWorld world;
	
	// ---- game entities ----
	private SnakeCamera snakeCamera;
	private Snake snake;
	
	// ---- game logic entities ----
	private Grid2D<Sphere> planetGrid = new Grid2D(200, 1500, 750);
	private ArrayList<Group> appleList = new ArrayList<>();
	private ArrayList<Group> propList = new ArrayList<>();
	
	// ---- game sound -----
	private SoundPlayer sound = new SoundPlayer();
	
	
	public GameStatus gameStatus = GameStatus.ALIVE;
	public DeadType deadReason;
	public int score = 0;
	
	// ----------------- View -----------------
	
	public boolean isLoaded() {
		return loaded;
	}
	
	public SubScene getSubScene() {
		return this.s;
	}

	public void load() {
		this.world = new GameWorld(planetGrid, appleList, propList);
		this.s = new SubScene(this.world, Config.width, Config.height, true, SceneAntialiasing.BALANCED);
			
		// cameras
		this.snakeCamera = new SnakeCamera(new PerspectiveCamera(true));
		this.s.setCamera((Camera) this.snakeCamera.core);

		// snake
		this.snake = new Snake(this.world, snakeCamera);
		this.setupSnake();
		
		// sound/sfx
		this.sound.loadAll(new String[] {
			"sfx/eating",
			"sfx/explosion",
			"sfx/boost",
			"sfx/alarm"
		});
		
		this.loaded = true;
		this.startAnimation();
		startGame();
	}
	
	public void unload() {
		// in reverse order of load()
		this.sound.unLoadAll();
		this.sound = null;
		
		this.snake = null;
		this.snakeCamera = null;
		this.s = null;
		
		this.world.getChildren().clear();
		this.world = null;
		
		this.loaded = false;
	}
	
	// ----------------- animations ------------
	
	public void startAnimation() {
		((AnimaNode) world).startAnimation();
	}

	public void stopAnimation() {
		((AnimaNode) world).stopAnimation();
	}
	
	// ----------------- logic -----------------
	
	private void setupSnake() {
		snake.bindMovements(s);
		snake.moveHead(new Point3D(0, -10, -200), false);
		for (int i = 0; i < 5; i++) snake.generateBody();
	}
	
	public void stopProcess() {
		this.frameGenerator.stop();
	}
	
	private Timeline frameGenerator,
			scoreAdder;
	private void startGame() {
		double fps = 60;
		Duration frameDuration = Duration.millis(1000.0 / fps);
		frameGenerator = new Timeline(fps, new KeyFrame(frameDuration, e -> executeFrame()));
		frameGenerator.setCycleCount(Timeline.INDEFINITE);
		frameGenerator.play();
		
		scoreAdder = new Timeline(1, new KeyFrame(Duration.seconds(1), e -> score++));
		scoreAdder.setCycleCount(Timeline.INDEFINITE);
		scoreAdder.play();
	}
	
	//
	boolean deadSnakeMovementUnbinded = false;
	private void executeFrame() {
		boolean isDead = (gameStatus == GameStatus.DEAD);

		snake.updateFrameMovement(isDead);
		if (!isDead) snakeCollideCheck();
		
		isDead = (gameStatus == GameStatus.DEAD);
		
		if (isDead && !deadSnakeMovementUnbinded) {
			snake.unbindMovements(this.s);
			deadSnakeMovementUnbinded = true;
		}

		if (!isDead) {
			snakeEatAppleCheck();
			snakeUsePropCheck();
			snakeBoarderCheck();
		}
	}
	
	// if snake has a collision with planet object
	private void snakeCollideCheck() {
		Point3D headPos = snake.head.getPos();
		
		for (ArrayList<Sphere> list: planetGrid.query(headPos)) {
			for (Sphere planet: list) {
				double x = planet.getTranslateX(),
						y = planet.getTranslateY(),
						z = planet.getTranslateZ();
				
				double dist = headPos.subtract(new Point3D(x, y, z)).magnitude();
				
				if (dist > planet.getRadius() + snake.bodySize) continue;
				
				this.snake.deadPos = new Point3D(x, y, z);
				gameStatus = GameStatus.DEAD;
				deadReason = DeadType.COLLISION;
				this.scoreAdder.stop();
				sound.play("sfx/explosion");
				return;
			}
		}
	}
	
	// if snake has eaten a apple
	private void snakeEatAppleCheck() {
		Stack<Integer> needRmvInd = new Stack<>();
		
		Point3D headPos = snake.head.getPos();
		int pos = 0;
		for (Group a : appleList) {
			double x = a.getTranslateX(),
					y = a.getTranslateY(),
					z = a.getTranslateZ();
			
			double dist = headPos.subtract(new Point3D(x, y, z)).magnitude();
			
			// 10 is the apple sphere size
			if (dist > snake.bodySize + 10) {
				pos++;
				continue;
			}
			
			this.world.getChildren().remove(a);
			needRmvInd.push(pos);
			this.score += 10;
			this.snake.generateBody();
			
			sound.play("sfx/eating");
			
			pos++;
		}
		
		while (needRmvInd.size() != 0) {
			int rmvPos = needRmvInd.pop();
			appleList.remove(rmvPos);
		}
	}
	
	private boolean playingBoostSFX = false;
	Timeline tempBoost = new Timeline(10,
		new KeyFrame(Duration.ZERO, e -> {
			Config.snakeSpeed.set(4);
			playingBoostSFX = true;
		}),
		new KeyFrame(Duration.millis(2700), e -> {
			playingBoostSFX = false;
		}),
		new KeyFrame(Duration.seconds(5), e -> {
			Config.snakeSpeed.set(2);
		})
	);
	private void snakeUsePropCheck() {
		Stack<Integer> needRmvInd = new Stack<>();
		
		Point3D headPos = snake.head.getPos();
		int pos = 0;
		for (Group pr : propList) {
			double x = pr.getTranslateX(),
					y = pr.getTranslateY(),
					z = pr.getTranslateZ();
			
			// distance in Norm(max)
			double dx = Math.abs(headPos.getX() - x),
					dy = Math.abs(headPos.getY() - y),
					dz = Math.abs(headPos.getZ() - z);
			
			double dist = Math.max(Math.max(dx, dy), dz);
			
			// 20 is the prop size in Norm(max) coordinate
			if (dist > 20) {
				pos++;
				continue;
			}
			
			this.world.getChildren().remove(pr);
			needRmvInd.push(pos);
			
			if (playingBoostSFX) {
				sound.stop("sfx/boost");
				tempBoost.stop();
			}
			sound.play("sfx/boost");
			tempBoost.play();
			
			pos++;
		}
		
		while (needRmvInd.size() != 0) {
			int rmvPos = needRmvInd.pop();
			propList.remove(rmvPos);
		}
	}
	
	private boolean playingBoarderSFX = false;
	Timeline tooFar = new Timeline(10,
		new KeyFrame(Duration.millis(900), e -> {
			playingBoarderSFX = false;
		})
	);
	private void snakeBoarderCheck() {
		Point3D headPos = snake.head.getPos();
		double dist = headPos.magnitude();
		
		if (dist >= 1300) {
			gameStatus = GameStatus.DEAD;
			deadReason = DeadType.OUTOFBOUNDARY;
			this.scoreAdder.stop();
		} else if (dist > 800) {
			world.ambLight.setLightOn(true);
			
			Color lightColor = Color.rgb(
				Math.min((int) (255 * (dist - 800) / 200) + 20, 255),
				0, 0
			);
			world.ambLight.setColor(lightColor);
			
			if (playingBoarderSFX) return;
			playingBoarderSFX = true;
			tooFar.play();
			sound.play("sfx/alarm");
		} else if (dist <= 800) {
			world.ambLight.setLightOn(false);
		}
	}
}
