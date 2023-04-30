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
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		System.out.println("cont ini!!");
		updateUIPosition();
		sound = new SoundPlayer();
	}
	
	public void updateUIPosition() {
		// still some problems
		
//		Node[] uis = { title, startBtn, settingBtn, controlBtn, creditBtn };
//		for (Node n: uis) {
//			n.setLayoutX(Config.width / 2);
//		}
	}
	
	public void playMenuMusic() {
		sound.load("bgm/space-age.mp3");
		sound.play("bgm/space-age.mp3", true);
	}
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	// entry point!
	public void enterMenu(Stage stage, Scene scene, Parent root) {
		this.stage = stage;
		this.scene = scene;
		playMenuMusic();
		reenterMenu();
		stage.show();
	}
	
	public void reenterMenu() {
		stage.setTitle("Menu");
		stage.setScene(scene);
	}
	
	public void enterGame(ActionEvent e) {
		getStage(e);
		sound.stop("bgm/space-age.mp3");
		Scene gameScene = new GameScene();
		stage.setScene(gameScene);
	}
	
	public void showSettings(ActionEvent e) throws IOException {
		Parent tempRoot = FXMLLoader.load(getClass().getResource("settings.fxml"));
		
		Scene settingScene = new Scene(tempRoot);
		addEscapeKey(settingScene);
		
		getStage(e);
		stage.setScene(settingScene);
	}
	
	public void showControls(ActionEvent e) throws IOException {
		Parent tempRoot = FXMLLoader.load(getClass().getResource("controls.fxml"));
		
		Scene controlScene = new Scene(tempRoot);
		addEscapeKey(controlScene);
		
		getStage(e);
		stage.setScene(controlScene);
	}
	
	public void showCredits(ActionEvent e) throws IOException {
		Parent tempRoot = FXMLLoader.load(getClass().getResource("credits.fxml"));
		
		Scene creditScene = new Scene(tempRoot);
		addEscapeKey(creditScene);
		
		getStage(e);
		stage.setScene(creditScene);
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
}