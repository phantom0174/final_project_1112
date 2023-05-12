package base;

import javafx.scene.image.Image;

public class MaterialLoader {
	public String fixedPrefix = "";
	
	public MaterialLoader() {
		
	}
	
	public MaterialLoader(String fixedPrefix) {
		this.fixedPrefix = fixedPrefix + "/";
	}
	
	public Image get(String path) {
		return new Image(getClass().getResourceAsStream("/resources/materials/" + fixedPrefix + path));
	}
}
