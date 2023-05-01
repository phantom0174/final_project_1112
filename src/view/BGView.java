package view;

import base.Config;
import base.View;
import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.image.Image;
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
		Image image = new Image(getClass().getResourceAsStream("/resources/materials/galaxy.jpg"));
		ImageView imageView = new ImageView(image);
		imageView.setPreserveRatio(true);
		
		root = new Group();
		root.getChildren().add(imageView);
		root.setOpacity(Config.backgroundOpacity);
		
		s = new SubScene(root, Config.width, Config.height);
		s.setFill(Color.BLACK);
		
		isLoaded = true;
	}

	@Override
	public void unload() {
		root.getChildren().clear();
	}

	@Override
	public SubScene getSubScene() {
		return s;
	}
}
