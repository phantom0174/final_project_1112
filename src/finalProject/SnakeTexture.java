package finalProject;

import base.Config;import base.MaterialLoader;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;

public class SnakeTexture {
	private static MaterialLoader matLoader = new MaterialLoader("skin");
	
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
			headMat.setDiffuseColor(Color.LIGHTBLUE);
			headMat.setDiffuseMap(matLoader.get("phh.png"));
			bodyMat.setDiffuseColor(Color.LIGHTBLUE);
			bodyMat.setDiffuseMap(matLoader.get("ph.png"));
			break;
		}
		case EASTEREGG2: {
			headMat.setDiffuseColor(Color.WHITE);
			headMat.setDiffuseMap(matLoader.get("boo.png"));
			bodyMat.setDiffuseColor(Color.WHITE);
			bodyMat.setDiffuseMap(matLoader.get("bo.png"));
			break;
		}
		case EASTEREGG3: {
			headMat.setDiffuseColor(Color.WHITE);
			headMat.setDiffuseMap(matLoader.get("pf.png"));
			bodyMat.setDiffuseColor(Color.WHITE);
			bodyMat.setDiffuseMap(matLoader.get("pff.png"));
			break;
		}
		}
	}
}
