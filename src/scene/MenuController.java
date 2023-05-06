package scene;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import base.Config;
import base.SoundPlayer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class MenuController implements Initializable {
	private SoundPlayer sound;
	
	@FXML
	public Node title;
	public Node startBtn;
	public Node settingBtn;
	public Node controlBtn;
	public Node creditBtn;
	
	int playerHightestScore = 0;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		sound = new SoundPlayer();
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
			if (((GameScene) gameScene).checkReturnMenu()) {
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
}
