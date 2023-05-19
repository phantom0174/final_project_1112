package scene;

/*

設定選單的操控器，功能包含：

	- 調整遊戲音量大小

*/

import java.net.URL;
import java.util.ResourceBundle;

import base.Config;
import base.GameDiff;
import base.SnakeTextureType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;


public class SettingMenuController implements Initializable {
	@FXML
	public Slider volumeSlider;
	public RadioButton easyRB, mediumRB, hardRB, hellRB;
	public ChoiceBox<String> skinChoice;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initiateVolume();
		initiateDiffi();
		initiateSkin();
	}
	
	private void initiateVolume() {
		volumeSlider.setValue(Config.volume.get() * 100);
		volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				double newVolume = volumeSlider.getValue();
				Config.volume.setValue(newVolume / 100);
			}
		});
	}
	
	private void initiateDiffi() {
		switch (Config.difficulty) {
		case EASY:
			easyRB.setSelected(true); break;
		case MEDIUM:
			mediumRB.setSelected(true); break;
		case HARD:
			hardRB.setSelected(true); break;
		case HELL:
			hellRB.setSelected(true); break;
		}
	}
	
	private static String[] skinChoices = {
		"Red", "Green", "Blue"	
	};
	private void initiateSkin() {
		skinChoice.getItems().addAll(skinChoices);
		skinChoice.setOnAction(this::changeSkin);

		switch (Config.snakeTexture) {
		case RED:
			skinChoice.setValue("Red"); break;
		case GREEN:
			skinChoice.setValue("Green"); break;
		case BLUE:
			skinChoice.setValue("Blue"); break;
		}
	}
	
	// ----------- difficulty --------------
	public void setEasy(ActionEvent e) {
		Config.difficulty = GameDiff.EASY;
	}
	
	public void setMedium(ActionEvent e) {
		Config.difficulty = GameDiff.MEDIUM;
	}
	
	public void setHard(ActionEvent e) {
		Config.difficulty = GameDiff.HARD;
	}
	
	public void setHell(ActionEvent e) {
		Config.difficulty = GameDiff.HELL;
	}
	
	// ----------- skin -------------
	public void changeSkin(ActionEvent e) {
		String newSkin = skinChoice.getValue();
		
		switch (newSkin) {
		case "Red":
			Config.snakeTexture = SnakeTextureType.RED; break;
		case "Green":
			Config.snakeTexture = SnakeTextureType.GREEN; break;
		case "Blue":
			Config.snakeTexture = SnakeTextureType.BLUE; break;
		}
	}
}
