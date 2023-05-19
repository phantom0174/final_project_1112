package finalProject;

import base.Config;import base.MaterialLoader;
import base.SnakeTextureType;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;

public class SnakeTexture {
	private static MaterialLoader matLoader = new MaterialLoader();
	
	public PhongMaterial headMat = new PhongMaterial();
	public PhongMaterial bodyMat = new PhongMaterial();
	
	public SnakeTexture() {
		switch (Config.snakeTexture) {
		case RED: {
			headMat.setDiffuseColor(Color.RED);
			bodyMat.setDiffuseColor(Color.PINK);
			break;
		}
		case GREEN: {
			headMat.setDiffuseColor(Color.GREEN);
			bodyMat.setDiffuseColor(Color.GREENYELLOW);
			break;
		}
		case BLUE: {
			headMat.setDiffuseColor(Color.DARKBLUE);
			bodyMat.setDiffuseColor(Color.LIGHTBLUE);
			break;
		}
		case EASTEREGG1: {
			headMat.setDiffuseColor(Color.WHITE);
			headMat.setDiffuseMap(matLoader.get("shiftPH0.jpg"));
			bodyMat.setDiffuseColor(Color.WHITE);
			break;
		}
		}
	}
}
