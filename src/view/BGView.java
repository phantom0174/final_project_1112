package view;

/*

遊戲中的 2D 背景，常駐存在，沒有任何動態功能，位於 View 的最底層。

*/


import base.Config;
import base.MaterialLoader;
import base.View;
import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class BGView implements View {
	private boolean isLoaded = false;
	
	public SubScene s;
	public Group root;

	public boolean isLoaded() {
		return isLoaded;
	}
	
	public void load() {
		MaterialLoader matLoader = new MaterialLoader("game");
		ImageView imageView = new ImageView(matLoader.get("space.jpeg"));
		imageView.setPreserveRatio(true);
		
		root = new Group();
		root.getChildren().add(imageView);
		root.setOpacity(Config.backgroundOpacity);
		
		s = new SubScene(root, Config.width, Config.height);
		s.setFill(Color.DARKBLUE);
		
		isLoaded = true;
	}

	@Override
	public void unload() {
		root.getChildren().clear();
		root = null;
		s = null;
		
		this.isLoaded = false;
	}

	@Override
	public SubScene getSubScene() {
		return s;
	}
}
