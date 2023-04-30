package scene;

import java.net.URL;
import java.util.ResourceBundle;

import base.SoundPlayer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneSwitcher implements Initializable {
	private Stage stage;
	private SoundPlayer sound = new SoundPlayer();
	
	@FXML
	public Node title;
	public Node startBtn;
	public Node settingBtn;
	public Node controlBtn;
	public Node creditBtn;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		sound.load("bgm/space-age.mp3");
		sound.play("bgm/space-age.mp3", true);
	}
	
	public void enterGame(ActionEvent e) {
		sound.stop("bgm/space-age.mp3");
		getStage(e);
		Scene gameScene = new GameScene();
		stage.setScene(gameScene);
	}
	
	public void showSettings(ActionEvent e) {
		getStage(e);
		SettingScene settingScene = new SettingScene();
		stage.setScene(settingScene.scene);
	}
	
	public void getStage(ActionEvent e) {
		stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
	}
}
