package scene;

import java.io.IOException;

import base.Config;
import base.SoundPlayer;
import base.View;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class MenuScene {
	public Scene scene;
	public static Parent root;
	
	public MenuScene() {
		try {
			root = FXMLLoader.load(getClass().getResource("menu.fxml"));
			
			scene = new Scene(root, Config.width, Config.height);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
