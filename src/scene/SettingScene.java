package scene;

import java.io.IOException;

import base.Config;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class SettingScene {
	public Scene scene;
	public static Parent root;
	
	public SettingScene() {
		try {
			root = FXMLLoader.load(getClass().getResource("settings.fxml"));
			
			scene = new Scene(root, Config.width, Config.height);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
