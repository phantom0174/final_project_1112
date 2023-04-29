package finalProject;


import scene.MainScene;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
	@Override
	public void start(Stage stage) throws Exception {
		Scene mainScene = new MainScene();
		
		stage.setTitle("Scene Testing");
		stage.setScene(mainScene);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
