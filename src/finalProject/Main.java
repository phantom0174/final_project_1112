package finalProject;


import scene.MenuController;
import base.ScoreBoard;
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
		MenuController control = loader.getController();
		stage.setResizable(false);
		control.enterMenu(stage, menuScene);
		
		ScoreBoard sb = new ScoreBoard();
		boolean success = sb.uploadScore("poij", "123123");
		System.out.println(success);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
