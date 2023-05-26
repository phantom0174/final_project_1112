package view;

/*

遊戲的核心 View，功能包括：

	- 遊戲世界/人物的導入
	- 每一幀的狀態更新與監控
	- 控制 GameWorld 進行依照角色行動所做出的遊戲世界更新

被 GameScene 監聽，會用 eventPipeline 發送要在 2D 遊戲介面上渲染的事件。

*/


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
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Sphere;
import javafx.util.Duration;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;


public class GameView implements View, AnimaNode {
	// ---- view ----
	private boolean loaded = false;
	public SubScene s;
	private GameWorld world;
	
	// ---- game entities ----
	private SnakeCamera snakeCamera;
	public Snake snake;
	
	// ---- game logic entities ----
	public Grid2D<Sphere> planetGrid = new Grid2D<Sphere>(150, 1500);
	public ArrayList<Group> appleList = new ArrayList<>();
	public ArrayList<Group> propList = new ArrayList<>();
	
	// ---- game sound -----
	private SoundPlayer sound = new SoundPlayer();
	
	
	// -------- Bubbling up to GameScene -------- start
	
	// ---- game status ----
	public GameStatus gameStatus = GameStatus.ALIVE;
	public DeadType deadReason;
	public double time = 300;
	
	// ---- event pipeline ---
	public ArrayList<String> eventPipeline = new ArrayList<>();
	/*
	因為 score 顯示、effect 跳出通知都需要從 GameScene 那邊去顯示；
	但如果一直更新又很耗資源；
	所以 GameScene 會持續監聽這個 pipeline，處理從這邊發出的需求。
	
	需求一覽：
		- updateTime
			更新剩餘時間
		
		- outOfBoundaryOn
			要超出邊界時常駐顯示
			
		- outOfBoundaryOff
			回到安全區域內時執行
	*/
	
	// -------- Bubbling up to GameScene -------- end
	
	
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
			"sfx/alarm",
			"sfx/extra_time"
		});
		
		this.loaded = true;
		this.startAnimation();
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
		
		planetGrid.deleteAll();
		appleList.clear();
		propList.clear();
		eventPipeline.clear();
		
		planetGrid = null;
		appleList = null;
		propList = null;
		eventPipeline = null;
		
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
	
	private void addTime(int time) {
		this.time += time;
		eventPipeline.add("updateTime");
	}
	
	private void setupSnake() {
		snake.bindMovements(s);
		snake.moveHead(new Point3D(0, 0, -100), false);
		for (int i = 0; i < 5; i++) snake.generateBody();
		snake.alignBodies();
		snake.setInitialCameraPos();
	}
	
	public void stopProcess() {
		this.frameGenerator.stop();
		this.timeDownCounter.stop();
		
		if (boostEffect.getStatus() == Animation.Status.RUNNING) {
			boostEffect.stop();
			Config.snakeSpeed.set(Config.snakeSpeed.get() / 2);
		}
	}
	
	private Timeline frameGenerator,
			timeDownCounter;
	public void startGame() {
		double fps = 60;
		Duration frameDuration = Duration.millis(1000.0 / fps);
		frameGenerator = new Timeline(fps, new KeyFrame(frameDuration, e -> executeFrame()));
		frameGenerator.setCycleCount(Timeline.INDEFINITE);
		frameGenerator.play();
		
		timeDownCounter = new Timeline(10,
			new KeyFrame(Duration.seconds(1), e -> {
				addTime(-1);
			}
		));
		timeDownCounter.setCycleCount(Timeline.INDEFINITE);
		timeDownCounter.play();
	}
	
	public void endGame() {
		frameGenerator.stop();
		timeDownCounter.stop();
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
	
	// -------------- Game Event Checking ----------------
	
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
				this.timeDownCounter.stop();
				this.time = 0;
				sound.play("sfx/explosion");
				return;
			}
		}
	}
	
	// if snake has eaten a apple
	private void snakeEatAppleCheck() {
		if (appleList.size() == 0) {
			snake.unbindMovements(this.s);
			gameStatus = GameStatus.WIN;
			this.endGame();
			return;
		}
		
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
			this.snake.generateBody();
			
			sound.play("sfx/eating");
			eventPipeline.add("updateAppleMap");
			
			pos++;
		}
		
		while (needRmvInd.size() != 0) {
			int rmvPos = needRmvInd.pop();
			appleList.remove(rmvPos);
		}
	}
	
	/*
	random effects:
		- double speed in 8 seconds
		
	音樂播放模式：
		如果在吃完一次道具之後，又再吃到道具並獲取到同樣的效果；
		則直接暫停上輪尚未播完的音樂，並重置效果時間。
		
	效果 timeline 統一以 0.1 秒為單位。
	*/
	
	private boolean boostSFXOn = false;
	private boolean boostOn = false;
	Timeline boostEffect = new Timeline(10,
		new KeyFrame(Duration.ZERO, e -> {
			if (boostSFXOn) sound.stop("sfx/boost");
			sound.play("sfx/boost");
			if (!boostOn) Config.snakeSpeed.set(Config.snakeSpeed.get() * 1.5);
			boostOn = true;
		}),
		new KeyFrame(Duration.millis(2700), e -> {
			boostSFXOn = false;
		}),
		new KeyFrame(Duration.seconds(8), e -> {
			boostOn = false;
			Config.snakeSpeed.set(Config.snakeSpeed.get() / 1.5);
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
			
			if (Math.random() < 0.5) {
				if (boostOn) boostEffect.stop();
				boostEffect.play();				
			} else {
				sound.play("sfx/extra_time");
				addTime(10);
			}
			
			pos++;
		}
		
		while (needRmvInd.size() != 0) {
			int rmvPos = needRmvInd.pop();
			propList.remove(rmvPos);
		}
	}
	
	public OutOfBoundaryType outOfBoundType = null;
	private boolean boarderEffectToggled = false;
	private boolean playingBoarderSFX = false;
	Timeline tooFar = new Timeline(10,
		new KeyFrame(Duration.millis(900), e -> {
			playingBoarderSFX = false;
		})
	);
	private void snakeBoarderCheck() {
		Point3D headPos = snake.head.getPos();
		
		double norm_2 = new Point2D(headPos.getX(), headPos.getZ()).magnitude(),
				norm_max = Math.abs(headPos.getY());
		
		double dist = Math.max(norm_2, norm_max);
		
		OutOfBoundaryType newType = null;
		if (norm_2 > 800)
			newType = OutOfBoundaryType.TOOFAR;
		else if (headPos.getY() < -800)
			newType = OutOfBoundaryType.TOOHIGH;
		else if (headPos.getY() > 800)
			newType = OutOfBoundaryType.TOOLOW;
		
		if (newType != null && newType != outOfBoundType) {
			eventPipeline.add("updateOutOfBoundaryMessage");
			outOfBoundType = newType;
		}
		
		if (dist >= 1300) {
			gameStatus = GameStatus.DEAD;
			deadReason = DeadType.OUTOFBOUNDARY;
			this.timeDownCounter.stop();
			this.time = 0;
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
			
			if (!boarderEffectToggled) {
				eventPipeline.add("outOfBoundaryOn");
				boarderEffectToggled = true;
			}
		} else if (dist <= 800) {
			world.ambLight.setLightOn(false);
			
			if (boarderEffectToggled) {
				eventPipeline.add("outOfBoundaryOff");
				boarderEffectToggled = false;
			}
		}
	}
	
	@Override
	protected void finalize() {
	    System.out.println("GameView recycled!");
	}
}
