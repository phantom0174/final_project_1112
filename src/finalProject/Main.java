package finalProject;


import scene.MenuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/scene/menu.fxml"));
		Parent root = loader.load();
		Scene menuScene = new Scene(root);
		menuScene.getStylesheets().add("/css/menu_scene.css");
		MenuController control = loader.getController();
		stage.setResizable(false);
		control.enterMenu(stage, menuScene);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
