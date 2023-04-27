package finalProject;


import javafx.animation.AnimationTimer;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

// - Camera and its Group
// camera rotation belongs to the camera itself,
// and the group moves the camera,
// so that rotation and translation can be separated.
public class FreeCamera {
	public Camera c = new PerspectiveCamera(true);
	public Group g = new Group();
	
	// camera rotation
	public DoubleProperty xRot = new SimpleDoubleProperty(0),
			yRot = new SimpleDoubleProperty(0);
	
	// group position
	public DoubleProperty xPos = new SimpleDoubleProperty(0),
			yPos = new SimpleDoubleProperty(0),
			zPos = new SimpleDoubleProperty(0);
	
	public FreeCamera() {
		// initialize camera
		c.setTranslateX(0);
		c.setTranslateY(0);
		c.setTranslateZ(0);
		c.setNearClip(1);
		c.setFarClip(10000);
		
		this.g.getChildren().add(c);
		
		// binding camera rotation
		Rotate xRotate = new Rotate(xRot.get(), Rotate.X_AXIS),
				yRotate = new Rotate(yRot.get(), Rotate.Y_AXIS);
			
		xRotate.angleProperty().bind(xRot);
		yRotate.angleProperty().bind(yRot);
		
		c.getTransforms().addAll(yRotate, xRotate); // order matters, in order of matrix multi.
		
		// binding group position
        Translate trans = new Translate();
		trans.xProperty().bind(xPos);
		trans.yProperty().bind(yPos);
		trans.zProperty().bind(zPos);
		
		this.g.getTransforms().add(trans);
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
	
	// Monitor that whether the movement keys has been pressed,
	// then update the movements using AnimationTimer
	public void bindMovements(Scene s) {
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
        
        s.setOnKeyPressed(event -> {
        	switch (event.getCode()) {
				case W: this.wHold = true; break;
				case S: this.sHold = true; break;
				case A: this.aHold = true; break;
				case D: this.dHold = true; break;
				case SPACE: this.upHold = true; break;
				case SHIFT: this.downHold = true; break;
				default: break;
			}
    	});
        
        s.setOnKeyReleased(event -> {
			switch (event.getCode()) {
				case W: this.wHold = false; break;
				case S: this.sHold = false; break;
				case A: this.aHold = false; break;
				case D: this.dHold = false; break;
				case SPACE: this.upHold = false; break;
				case SHIFT: this.downHold = false; break;
				default: break;
    		}
        });
        
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
		movementTimer.start();
	}
	
	public void move(Point3D pos_v) {
		xPos.set(xPos.get() + pos_v.getX());
		yPos.set(yPos.get() + pos_v.getY());
		zPos.set(zPos.get() + pos_v.getZ());
	}
	
	public void moveTo(Point3D pos_v) {
		xPos.set(pos_v.getX());
		yPos.set(pos_v.getY());
		zPos.set(pos_v.getZ());		
	}
	
	public Point3D getPos() {
		double x = this.xPos.get(),
			y = this.yPos.get(),
			z = this.zPos.get();
		
		return new Point3D(x, y, z);
	}
}
