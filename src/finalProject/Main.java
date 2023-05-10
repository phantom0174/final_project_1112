package finalProject;

/*

專案的 Entry Class。

*/


import scene.StoryController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class Main extends Application {
	@Override
	public void start(Stage stage) throws Exception {
		stage.setResizable(false);
		Image icon = new Image(getClass().getResourceAsStream("/resources/materials/icon.png"));
		stage.getIcons().add(icon);

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/scene/story.fxml"));
		Parent root = loader.load();
		Scene storyScene = new Scene(root);
		StoryController control = loader.getController();
		
		control.startStory(stage, storyScene);
	}
	
	public static void bindSkipkey(Scene s) {
		
	}

	public static void main(String[] args) {
		launch(args);
	}
}
