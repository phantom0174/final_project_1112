package camera;


import base.Entity;

import javafx.scene.PerspectiveCamera;


public class SnakeCamera extends Entity {
	public SnakeCamera(PerspectiveCamera c) {
		super(c);
		
		// initialize camera
		c.setNearClip(1);
		c.setFarClip(20000);
	}
}
