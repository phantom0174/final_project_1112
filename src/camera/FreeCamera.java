package camera;

/*

可在遊戲世界中自由移動的相機，在專案初期作為熟悉語法的練習。

底層邏輯使用 Entity 實作而成，簡化後成為 SnakeCamera。

*/


import base.AnimaNode;
import base.Entity;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point3D;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SubScene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


public class FreeCamera extends Entity implements AnimaNode {
	public FreeCamera(PerspectiveCamera c) {
		super(c);
		
		// initialize camera
		c.setNearClip(1);
		c.setFarClip(4000);
		startAnimation();
	}
	
	// for controls
	private double anchorX, anchorY;
	private boolean wHold = false,
			sHold = false,
			aHold = false,
			dHold = false,
			upHold = false,
			downHold = false;
	
	double cur_arg = Math.toRadians(-yRot.get()),
			cos = Math.cos(cur_arg),
			sin = Math.sin(cur_arg);
	
	public void writeKeyHold(KeyCode c, boolean mode) {
		switch (c) {
			case UP: this.wHold = mode; break;
			case DOWN: this.sHold = mode; break;
			case LEFT: this.aHold = mode; break;
			case RIGHT: this.dHold = mode; break;
			case SPACE: this.upHold = mode; break;
			case SHIFT: this.downHold = mode; break;
			default: break;
		}
	}
	
	// Monitor that whether the movement keys has been pressed,
	// then update the movements using AnimationTimer
	public void bindMovements(SubScene s) {
		s.setPickOnBounds(true);
		
		s.setOnMousePressed(event -> {
			anchorX = event.getSceneX();
			anchorY = event.getSceneY();
		});

        s.setOnMouseDragged(event -> {
        	double rotDx = (anchorY - event.getSceneY()) / 10;
            double rotDy = (anchorX - event.getSceneX()) / 10;
            
            xRot.set(xRot.get() + rotDx);
            yRot.set(yRot.get() - rotDy);
            anchorX = event.getSceneX();
			anchorY = event.getSceneY();
			
			cur_arg = Math.toRadians(-yRot.get());
			cos = Math.cos(cur_arg);
			sin = Math.sin(cur_arg);
        });
        
        s.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
        	writeKeyHold(event.getCode(), true);
    	});
        
        s.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
        	writeKeyHold(event.getCode(), false);
    	});
	}
	
	AnimationTimer movementTimer = new AnimationTimer() {
		@Override
		public void handle(long now) {
			if (wHold) move(new Point3D(-sin, 0, cos).multiply(3));
			if (sHold) move(new Point3D(sin, 0, -cos).multiply(3));
			if (aHold) move(new Point3D(-cos, 0, -sin).multiply(3));
			if (dHold) move(new Point3D(cos, 0, sin).multiply(3));
			if (upHold) move(new Point3D(0, -3, 0));
			if (downHold) move(new Point3D(0, 3, 0));
		}
	};

	public void startAnimation() {
		movementTimer.start();
	}

	public void stopAnimation() {
		movementTimer.stop();
	}
}
