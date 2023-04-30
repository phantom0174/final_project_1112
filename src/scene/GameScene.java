package scene;


import base.Config;
import base.ViewHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import view.BGView;
import view.GameView;

/*

Three steps to activate a view:
1. load scene entities
2. attach scene to root
3. switch to scene

*/

public class GameScene extends Scene {
	private static Group root = new Group();
	private ViewHandler view = new ViewHandler();
	
	public GameScene() {
		super(root, Config.width, Config.height);
		
		addViews();
		
		view.load("3d_game_view");
		view.attach("3d_game_view", root);
		
		view.load("2d_bg");
		view.attach("2d_bg", root);
		
		view.switchToView("3d_game_view");
	}
	
	private void addViews() {
		view.add("3d_game_view", new GameView());
		view.add("2d_bg", new BGView());
	}
}
