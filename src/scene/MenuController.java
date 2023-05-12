package scene;

/*

播完故事後進入的主選單，底下包括：

	1. 開始遊戲
	2. 遊戲設定
	3. 遊戲規則
	4. 銘謝列表
	5. 排行榜


此 class 會被重複利用（不會被解構）。無論進入到甚麼選單，

最後都會使用 enterMenuFromEndingGame() 或是 reenterMenu() 重新返回到此介面。

*/


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import base.Config;
import base.SoundPlayer;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;


public class MenuController implements Initializable {
	
	@FXML
	public Node title;
	public Node subtitle;
	public Node startBtn;
	public Node settingBtn;
	public Node controlBtn;
	public Node creditBtn;
	
	private SoundPlayer sound;
	private int playerHightestScore = 0;
	
	public Boolean easyRBSelect;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		sound = new SoundPlayer();
		startMenuSnakeAnimation();
		startTitleAnimation();
	}
	
	public void playMenuMusic() {
		sound.load("bgm/space-age");
		sound.play("bgm/space-age", true);
	}
	
	private Stage stage;
	private Scene scene;
	
	// ------------ entry point! --------------
	public void enterMenu(Stage stage, Scene scene) {
		this.stage = stage;
		this.scene = scene;
		addQuit();
		
		playMenuMusic();
		reenterMenu();
		stage.show();
	}
	
	public void enterMenuFromEndingGame() {
		playMenuMusic();
		reenterMenu();
		stage.show();
	}
	
	public void reenterMenu() {
		stage.setScene(scene);
		stage.setTitle("finalProject | Menu");
	}
	// ------------ entry point! --------------
	
	public void enterGame(ActionEvent e) {
		getStage(e);
		sound.stop("bgm/space-age");
		GameScene gameScene = new GameScene();
		
		gameScene.s.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
			if (event.getCode() != KeyCode.ESCAPE) return;
			if (((GameScene) gameScene).checkCanReturnMenu()) {
				// get new highscore
				int newScore = ((GameScene) gameScene).getPlayerScore();
				if (newScore > playerHightestScore) {
					playerHightestScore = newScore;
					Config.scoreUploadCoda = true;
				}
				
				((GameScene) gameScene).closeScene();
				
				enterMenuFromEndingGame();
			}
		});
		
		stage.setScene(gameScene.s);
		stage.setTitle("finalProject | Game");
		gameScene.startGame();
	}
	
	public void showSettings(ActionEvent e) throws IOException {
		Parent tempRoot = FXMLLoader.load(getClass().getResource("settings.fxml"));
		
		Scene settingScene = new Scene(tempRoot);
		addEscapeKey(settingScene);
		
		getStage(e);
		stage.setScene(settingScene);
		stage.setTitle("finalProject | Settings");
	}
	
	public void showControls(ActionEvent e) throws IOException {
		Parent tempRoot = FXMLLoader.load(getClass().getResource("controls.fxml"));
		
		Scene controlScene = new Scene(tempRoot);
		addEscapeKey(controlScene);
		
		getStage(e);
		stage.setScene(controlScene);
		stage.setTitle("finalProject | Controls");
	}
	
	public void showCredits(ActionEvent e) throws IOException {
		Parent tempRoot = FXMLLoader.load(getClass().getResource("credits.fxml"));
		
		Scene creditScene = new Scene(tempRoot);
		addEscapeKey(creditScene);
		
		getStage(e);
		stage.setScene(creditScene);
		stage.setTitle("finalProject | Credits");
	}
	
	public void showScoreBoard(ActionEvent e) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("scoreboard.fxml"));
		Parent tempRoot = loader.load();
		Scene scoreScene = new Scene(tempRoot);
		ScoreboardController control = loader.getController();
		control.showHighestScore(playerHightestScore);
		
		addEscapeKey(scoreScene);
		
		getStage(e);
		stage.setScene(scoreScene);
		stage.setTitle("finalProject | Scoreboard");
	}
	
	public void getStage(ActionEvent e) {
		stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
	}
	
	public void addEscapeKey(Scene s) {
		s.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
			if (event.getCode() != KeyCode.ESCAPE) return;
			reenterMenu();
		});
	}
	
	public void addQuit() {
		scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
			if (event.getCode() != KeyCode.ESCAPE) return;
			stage.close();
		});
	}
	
	// ------------------- snake animation ------------------
	@FXML
	public Group s1, s2;
	
	public void startMenuSnakeAnimation() {
		Snake2D snake1 = new Snake2D(s1, Color.GREEN, Color.GREENYELLOW, 5),
				snake2 = new Snake2D(s2, Color.RED, Color.PINK, 5);
		
		Timeline snakeAnima = new Timeline(60,
			new KeyFrame(Duration.millis(1000 / 60), e -> {
				snake1.updateFrame();
				snake2.updateFrame();
			})
		);
		snakeAnima.setCycleCount(Timeline.INDEFINITE);
		snakeAnima.play();
	}
	
	public void startTitleAnimation() {
		Timeline titleAnima = new Timeline(60,
			new KeyFrame(Duration.ZERO, new KeyValue(subtitle.scaleXProperty(), 1)),
			new KeyFrame(Duration.ZERO, new KeyValue(subtitle.scaleYProperty(), 1)),
			new KeyFrame(Duration.ZERO, new KeyValue(subtitle.opacityProperty(), 1)),
			
			new KeyFrame(Duration.seconds(0.5), new KeyValue(subtitle.scaleXProperty(), 1.5)),
			new KeyFrame(Duration.seconds(0.5), new KeyValue(subtitle.scaleYProperty(), 1.5)),
			new KeyFrame(Duration.seconds(0.5), new KeyValue(subtitle.opacityProperty(), 0.5)),
			
			new KeyFrame(Duration.seconds(1), new KeyValue(subtitle.scaleXProperty(), 1)),
			new KeyFrame(Duration.seconds(1), new KeyValue(subtitle.scaleYProperty(), 1)),
			new KeyFrame(Duration.seconds(1), new KeyValue(subtitle.opacityProperty(), 1))
		);
		titleAnima.setCycleCount(Timeline.INDEFINITE);
		titleAnima.play();
	}
}


class Snake2D {
	public ArrayList<Circle> HB = new ArrayList<>(); // head and bodies
	public ArrayList<Point2D> HBV = new ArrayList<>(); // head and bodies velocities
	
	public double speed = Math.random() * 2.5 + 2.5;
	
	public double minX = 0, maxX = 1080, minY = -20, maxY = 580;
	
	public Snake2D(Group g, Color headC, Color bodyC, int len) {
		Point2D defaultSpeed = new Point2D(Math.random(), Math.random());
		
		Circle head = new Circle();
		head.setFill(headC);
		HB.add(head);
		
		head.setRadius(10);
		moveHeadTo(Math.random() * 540 + 270, Math.random() * 300 + 150);
		
		for (int i = 1; i <= len; i++) {
			Circle body = new Circle();
			body.setRadius(10);
			body.setFill(bodyC);
			
			Point2D unitDr = defaultSpeed.normalize().multiply(-1).multiply(20 * i);
			
			body.setTranslateX(head.getTranslateX() + unitDr.getX());
			body.setTranslateY(head.getTranslateY() + unitDr.getY());
			
			HB.add(body);
		}
		
		for (int i = 0; i < len + 1; i++) HBV.add(defaultSpeed);
		
		for (Circle c: HB) {
			g.getChildren().add(c);
		}
	}
	
	public void moveHeadTo(Point2D p) {
		HB.get(0).setTranslateX(p.getX());
		HB.get(0).setTranslateY(p.getY());
	}
	
	public void moveHeadTo(double x, double y) {
		moveHeadTo(new Point2D(x, y));
	}
	
	public void updateFrame() {

		for (int i = 0; i < HB.size(); i++) {
			Circle curBody = HB.get(i);
			Point2D unitDr = HBV.get(i).normalize().multiply(speed);
			
			double nextX = curBody.getTranslateX() + unitDr.getX(),
					nextY = curBody.getTranslateY() + unitDr.getY();
			
			
			if (nextX > maxX) {
				nextX = 2 * maxX - nextX;
				unitDr = new Point2D(-unitDr.getX(), unitDr.getY());
			} else if (nextX < minX) {
				nextX = 2 * minX - nextX;
				unitDr = new Point2D(-unitDr.getX(), unitDr.getY());
			}
			
			if (nextY > maxY) {
				nextY = 2 * maxY - nextY;
				unitDr = new Point2D(unitDr.getX(), -unitDr.getY());
			} else if (nextY < minY) {
				nextY = 2 * minY - nextY;
				unitDr = new Point2D(unitDr.getX(), -unitDr.getY());
			}
			
			HBV.set(i, unitDr);
			
			curBody.setTranslateX(nextX);
			curBody.setTranslateY(nextY);
		}
	}
}
