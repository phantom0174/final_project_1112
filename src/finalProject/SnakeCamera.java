package finalProject;


import javafx.scene.PerspectiveCamera;


public class SnakeCamera extends Entity {
	public SnakeCamera(PerspectiveCamera c) {
		super(c);
		
		// initialize camera
		c.setNearClip(1);
		c.setFarClip(4000);
	}
}
