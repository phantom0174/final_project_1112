package scene;


import base.Config;

import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.layout.Pane;
import subscene.GameScene;

public class MainScene extends Scene {
	private static Pane root = new Pane();
	
	private SubScene gameScene = new GameScene();

	public MainScene() {
		super(root, Config.width, Config.height);
		
		
		root.getChildren().add(gameScene);
		
		switchToSubScene(gameScene);
	}
	
	private void switchToSubScene(SubScene s) {
		s.toFront();
		s.requestFocus();
	}
}
