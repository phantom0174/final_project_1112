package finalProject;


import scene.MenuScene;
import scene.SceneSwitcher;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Scene Testing");
		
		MenuScene menu = new MenuScene();
		stage.setScene(menu.scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
