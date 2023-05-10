package finalProject;

import base.Config;
import base.SnakeTextureType;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;


class HeadMaterial {
	public PhongMaterial mat = new PhongMaterial();
}

class BodyMaterial {
	public PhongMaterial mat = new PhongMaterial();
}

public class SnakeTexture {
	public HeadMaterial headMat = new HeadMaterial();
	public BodyMaterial bodyMat = new BodyMaterial();
	
	public SnakeTexture() {
		if (Config.snakeTexture == SnakeTextureType.RED) {
			headMat.mat.setDiffuseColor(Color.RED);
			bodyMat.mat.setDiffuseColor(Color.PINK);
		} else if (Config.snakeTexture == SnakeTextureType.GREEN) {
			headMat.mat.setDiffuseColor(Color.GREEN);
			bodyMat.mat.setDiffuseColor(Color.GREENYELLOW);
		} else if (Config.snakeTexture == SnakeTextureType.BLUE) {
			headMat.mat.setDiffuseColor(Color.DARKBLUE);
			bodyMat.mat.setDiffuseColor(Color.LIGHTBLUE);
		}
	}
}
