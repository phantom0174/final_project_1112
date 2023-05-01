package base;

import java.util.HashMap;

import javafx.scene.Group;
import javafx.scene.Node;

public class ViewHandler {
	private HashMap<String, View> viewPool = new HashMap<>();
	private HashMap<String, Node> attachedViews = new HashMap<>();
	
	public void add(String viewName, View view) {
		this.viewPool.put(viewName, view);
	}
	
	public void switchToView(String viewName) {
		View v = viewPool.get(viewName);
		if (!v.isLoaded()) v.load();
		
		v.getSubScene().toFront();
		v.getSubScene().requestFocus();
	}
	
	public void attach(String viewName, Group root) {
		if (attachedViews.get(viewName) == null) {
			Node scene = viewPool.get(viewName).getSubScene();
			
			root.getChildren().add(scene);
			attachedViews.put(viewName, scene);
		}
	}
	
	public void detach(String viewName, Group root) {
		Node scene = attachedViews.get(viewName);
		
		if (scene != null) {
			
			root.getChildren().remove(scene);
			attachedViews.remove(viewName);
		}
	}
	
	public void load(String viewName) {
		View v = viewPool.get(viewName);
		if (!v.isLoaded()) v.load();
		else System.out.printf("View %s is already loaded!\n", viewName);
	}

	public void unload(String viewName) {
		View v = viewPool.get(viewName);
		if (v.isLoaded()) v.unload();
		else System.out.printf("View %s is already unloaded!\n", viewName);
	}
}
