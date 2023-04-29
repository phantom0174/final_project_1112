package scene;


import java.util.HashMap;

import base.Config;
import base.View;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;

/*

Three steps to activate a view:
1. load scene entities
2. attach scene to root
3. switch to scene

*/

public class MainScene extends Scene {
	private static Group root = new Group();
	
	private HashMap<String, View> viewPool = new HashMap<>();
	private HashMap<String, Node> attachedViews = new HashMap<>();
	
	public MainScene() {
		super(root, Config.width, Config.height);	
		
		addViews();
		
		load("3d_game_view");
		attach("3d_game_view");
		switchToView("3d_game_view");
	}
	
	private void addViews() {
		viewPool.put("3d_game_view", (View) new GameView());
	}
	
	private void switchToView(String viewName) {
		View v = viewPool.get(viewName);
		if (!v.isLoaded()) v.load();
		
		v.getSubScene().toFront();
		v.getSubScene().requestFocus();
	}
	
	public void attach(String viewName) {
		if (attachedViews.get(viewName) == null) {
			Node scene = viewPool.get(viewName).getSubScene();
			
			root.getChildren().add(scene);
			attachedViews.put(viewName, scene);
		}
	}
	
	public void detach(String viewName) {
		Node scene = attachedViews.get(viewName);
		
		if (scene != null) {
			
			root.getChildren().remove(scene);
			attachedViews.remove(viewName);
		}
	}
	
	public void load(String viewName) {
		View v = viewPool.get(viewName);
		if (!v.isLoaded()) v.load();
		else System.out.printf("View %s is already loaded!", viewName);
	}

	public void unload(String viewName) {
		View v = viewPool.get(viewName);
		if (v.isLoaded()) v.unload();
		else System.out.printf("View %s is already unloaded!", viewName);
	}
}
