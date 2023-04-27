package finalProject;


import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

import javax.management.modelmbean.ModelMBean;

import javafx.animation.AnimationTimer;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;


/*

--- Controls ---

Snake will move with a certain rate all the time.

W/S keys will increase/decrease pitch angle to a certain amount.
A/D keys will decrease/increase yaw angle to a certain amount.

Pitch will slowly reset to 0 if W/S key is not pressed.
Similarly for Yaw (A/D keys)


*/

public class Snake {
	public Group fatherGroup;
	
	public Entity head;
	public ArrayList<Entity> bodies = new ArrayList<>();
	
	// the index of the body and its succs that is static in movement
	public int staticGap = 0;
	
	private final double bodySize = 10;
	
	Snake(Group fatherGroup) {
		this.fatherGroup = fatherGroup;
		initializeHead();
		updateTempArg();
		startAnimation();
	}
	
	private void initializeHead() {
		Sphere headCore = new Sphere(bodySize);
		PhongMaterial headMaterial = new PhongMaterial(Color.GREEN);
		headCore.setMaterial(headMaterial);
		
		this.head = new Entity(headCore);
		this.fatherGroup.getChildren().add(this.head.shell);
		
		
		this.head.setPos(0, 0, -500);
		for (int i = 0; i < 5; i++) generateBody();
	}
	
	public void generateBody() {
		Sphere bodyCore = new Sphere(bodySize);
		PhongMaterial headMaterial = new PhongMaterial(Color.GREENYELLOW);
		bodyCore.setMaterial(headMaterial);
		
		Entity body = new Entity(bodyCore);
		
		// set the position to the current position of the body in front of this body
		if (this.bodies.size() == 0) {
			Point3D pos = this.head.getPos();
			body.setPos(pos.getX(), pos.getY(), pos.getZ());
		} else {
			Point3D pos = this.bodies.get(this.bodies.size() - 1).getPos();
			body.setPos(pos.getX(), pos.getY(), pos.getZ());
		}
		
		this.bodies.add(body);
		
		this.fatherGroup.getChildren().add(body.shell);
	}
	
	public void updateStaticGap() {
		if (this.staticGap >= this.bodies.size()) return;
		
		Entity curBody;
		Point3D nextPos;
		if (this.staticGap == 0) {
			nextPos = this.head.getPos();
		} else {
			nextPos = this.bodies.get(staticGap - 1).getPos();
		}
		
		curBody = this.bodies.get(staticGap);
		Point3D curPos = curBody.getPos();
		if (nextPos.subtract(curPos).magnitude() < 2 * this.bodySize) return;
		
		staticGap += 1;
	}
	
	// need to call this function before moving the snake's head position
	public void updateBodyPosition() {
		this.updateStaticGap();
		if (this.staticGap == 0) return;
		
		for (int i = staticGap - 1; i > 0; i--) {
			Entity curBody = this.bodies.get(i);
			Point3D nextPos = this.bodies.get(i - 1).getPos();
			Point3D curPos = curBody.getPos();
			
			curBody.move(nextPos.subtract(curPos).normalize());
		}
		 
		Entity curBody = this.bodies.get(0);
		Point3D headPos = this.head.getPos();
		Point3D curPos = curBody.getPos();
		curBody.move(headPos.subtract(curPos).normalize());
	}
	
	// controls
	double cur_arg, cos, sin;
	
	public void updateTempArg() {
		cur_arg = Math.toRadians(-head.getRot().getY());
		cos = Math.cos(cur_arg);
		sin = Math.sin(cur_arg);
	}
	
	private short wHold = 0,
			sHold = 0,
			aHold = 0,
			dHold = 0,
			upHold = 0,
			downHold = 0;
	
	public short accumu(short v, boolean status) {
		if (status) return (short) Math.min(v + 1, 10);
		else return (short) Math.max(v - 1, 0);
	}
	
	public void keyPressMode(KeyCode c, boolean mode) {
		switch (c) {
			case W: this.wHold = accumu(this.wHold, mode); break;
			case S: this.sHold = accumu(this.sHold, mode); break;
			case A: this.aHold = accumu(this.aHold, mode); break;
			case D: this.dHold = accumu(this.dHold, mode); break;
			case SPACE: this.upHold = accumu(this.upHold, mode); break;
			case SHIFT: this.downHold = accumu(this.downHold, mode); break;
			default: break;
		}
	}
	
	public void keyBinding(Scene s) {
        s.setOnKeyPressed(event -> {
        	keyPressMode(event.getCode(), true);
    	});
        
        s.setOnKeyReleased(event -> {
        	keyPressMode(event.getCode(), false);
        });
	}
	
	public void startAnimation() {
		AnimationTimer movementTimer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				if (now % 1000 == 0) System.out.println(staticGap);
				moveHead(new Point3D(-sin, 0, cos));
			}
		};
		movementTimer.start();
	}
	
	public void moveHead(Point3D pos_v) {
		this.updateBodyPosition();
		this.head.move(pos_v);
	}
}
