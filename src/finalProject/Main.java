package finalProject;

/*

專案的 Entry Class。

*/


import scene.StoryController;
import base.MaterialLoader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
	@Override
	public void start(Stage stage) throws Exception {
		MaterialLoader matLoader = new MaterialLoader();
		
		stage.setResizable(false);
		stage.getIcons().add(matLoader.get("icon.png"));
		
		// to menu entry
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/scene/story.fxml"));
		Parent root = loader.load();
		Scene storyScene = new Scene(root);
		StoryController control = loader.getController();
		
		control.startStory(stage, storyScene);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
