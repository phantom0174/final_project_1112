package scene;

/*

設定選單的操控器，功能包含：

	- 調整遊戲音量大小

*/


import java.net.URL;
import java.util.ResourceBundle;

import base.Config;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;

public class SettingMenuController implements Initializable {
	@FXML
	public Slider volumeSlider;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		volumeSlider.setValue(Config.volume.get() * 100);
		volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				double newVolume = volumeSlider.getValue();
				Config.volume.setValue(newVolume / 100);
			}
		});
	}
}
