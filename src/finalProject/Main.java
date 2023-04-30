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
//		stage.setTitle("Scene Testing");
//		
//		MenuScene menu = new MenuScene();
//		stage.setScene(menu.scene);
//		stage.show();
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/scene/menu.fxml"));
		Parent root = loader.load();
		Scene menuScene = new Scene(root);
		MenuController control = loader.getController();
		control.enterMenu(stage, menuScene);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
