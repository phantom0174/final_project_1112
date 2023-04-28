package finalProject;


import java.nio.channels.NonWritableChannelException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

import javax.management.modelmbean.ModelMBean;

import javafx.animation.AnimationTimer;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventType;
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
		startAnimation();
	}
	
	// --------------- logic and initialize ----------------
	
	private void initializeHead() {
		Sphere headCore = new Sphere(bodySize);
		PhongMaterial headMaterial = new PhongMaterial(Color.GREEN);
		headCore.setMaterial(headMaterial);
		
		this.head = new Entity(headCore);
		this.head.setRot(0, 0, 0);
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
			
			curBody.move(nextPos.subtract(curPos).normalize().multiply(moveSpeed));
		}
		 
		Entity curBody = this.bodies.get(0);
		Point3D headPos = this.head.getPos();
		Point3D curPos = curBody.getPos();
		curBody.move(headPos.subtract(curPos).normalize().multiply(moveSpeed));
	}
	
	// --------------- controls and animations -------------------
	public double moveSpeed = 0.5;
	
	/*
	
	pitch & yaw intensity: short [0 ~ 10]
	used to calculate the pitch value & yaw value of the snake's next move.
	
	*/
	
	private short pitchIntensity = 5,
			yawIntensity = 5;
	
	
	/*
	
	--- Temp variables ---
	
	These variables were created to save computing resources.
	They will be updated before moving the snake head.
	
	pitch: current pitch of snakeHead + f(pitch intensity)
	+ pitchCos, pitchSin
	
	yaw: current yaw of snakeHead + f(yaw intensity)
	+ yawCos, yawSin
	
	*/
	
	double pitch, pitchCos, pitchSin,
		yaw, yawCos, yawSin;
	
	public void updateTempRot() {
		pitch = Math.toRadians(-(45 * moveSpeed) * (2 * Utils.easeInOut((double) pitchIntensity / 10) - 1));
		pitchCos = Math.cos(pitch);
		pitchSin = Math.sin(pitch);
		
		yaw = Math.toRadians((45 * moveSpeed) * (2 * Utils.easeInOut((double) yawIntensity / 10) - 1));
		yawCos = Math.cos(yaw);
		yawSin = Math.sin(yaw);
	}
	
	/*
	
	Calculate pitch & yaw intensity based on the current status of keyHolds.
	
	*/
	private boolean wHold = false,
			sHold = false,
			aHold = false,
			dHold = false;
	
	public void writeKeyHold(KeyCode c, boolean mode) {
		switch (c) {
			case UP: this.wHold = mode; break;
			case DOWN: this.sHold = mode; break;
			case LEFT: this.aHold = mode; break;
			case RIGHT: this.dHold = mode; break;
			default: break;
		}
	}
	
	public void keyBinding(Scene s) {
		s.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
        	writeKeyHold(event.getCode(), true);
    	});
        
        s.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
        	writeKeyHold(event.getCode(), false);
    	});
	}
	
	public void updateRotIntensity() {
		boolean pitchAct = wHold ^ sHold;
		
		if (pitchAct) {
			pitchIntensity += wHold ? 1 : 0;
			pitchIntensity -= sHold ? 1 : 0;

			if (pitchIntensity < 0) pitchIntensity = 0;
			else if (pitchIntensity > 10) pitchIntensity = 10;
		} else {
			pitchIntensity -= Utils.sign((short) (pitchIntensity - 5));
		}
		
		boolean yawAct = aHold ^ dHold;
		if (yawAct) {
			yawIntensity += aHold ? 1 : 0;
			yawIntensity -= dHold ? 1 : 0;
			
			if (yawIntensity < 0) yawIntensity = 0;
			else if (yawIntensity > 10) yawIntensity = 10;
		} else {
			yawIntensity -= Utils.sign((short) (yawIntensity - 5));
		}
	}
	
	public Point3D headRotMatrix(Point3D v) {
		/*
		
		M = (x, z, y)
		[ Cy -Sy  0 ]
		[ Sy  Cy  0 ]
		[ 0   0  -1 ]
		
		return (M)v
		
		*/
		
		double yaw = Math.toRadians(head.getRot().getY()),
			Cy = Math.cos(yaw),
			Sy = Math.sin(yaw);
		
		double x = v.getX(),
			z = v.getZ(),
			y = v.getY();
		
		return new Point3D(
			x*Cy - z*Sy,
			-y,
			x*Sy + z*Cy
		);
	}
	
	public void startAnimation() {
		AnimationTimer movementTimer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				updateRotIntensity();
				updateTempRot();
				
				
				Point3D headRot = head.getRot();
				if (now % 1000 == 0) System.out.println(headRot.getY());
				
				Point3D pitchVetor = headRotMatrix(new Point3D(0, -pitchSin, pitchCos));
				Point3D yawVector = headRotMatrix(new Point3D(-yawSin, 0, yawCos));
				
				Point3D directionVector = pitchVetor.add(yawVector).normalize().multiply(moveSpeed);
				
				moveHead(directionVector);
				head.setRot(
					pitch + headRot.getX(),
					yaw + headRot.getY(),
					0
				);
			}
		};
		movementTimer.start();
	}
	
	public void moveHead(Point3D pos_v) {
		this.updateBodyPosition();
		this.head.move(pos_v);
	}
}
