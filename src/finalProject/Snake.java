package finalProject;


import java.util.ArrayList;

import base.AnimaNode;
import base.Config;
import base.Entity;
import base.Utils;
import camera.SnakeCamera;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;


/*

--- Controls ---

Snake will move with a certain rate all the time.

W/S keys will increase/decrease pitch angle to a certain amount.
A/D keys will decrease/increase yaw angle to a certain amount.

Pitch will slowly reset to 0 if W/S key is not pressed.
Similarly for Yaw (A/D keys)


*/

public class Snake implements AnimaNode {
	public Group fatherGroup;
	public SnakeCamera camera;
	
	public Entity head;
	public ArrayList<Entity> bodies = new ArrayList<>();
	
	private final double bodySize = 10;
	
	public Snake(Group fatherGroup, SnakeCamera mainCam) {
		this.fatherGroup = fatherGroup;
		this.camera = mainCam;
		
		initializeHead();
		startAnimation();
	}
	
	// --------------- logic and initialize ----------------
	
	private void initializeHead() {
		Sphere headCore = new Sphere(bodySize);
		PhongMaterial headMaterial = new PhongMaterial(Color.GREEN);
		headCore.setMaterial(headMaterial);
		
		this.head = new Entity(headCore);
		this.fatherGroup.getChildren().add(this.head.shell);	
	}
	
	public void generateBody() {
		Sphere bodyCore = new Sphere(bodySize);
		PhongMaterial headMaterial = new PhongMaterial(Color.GREENYELLOW);
		bodyCore.setMaterial(headMaterial);
		
		Entity body = new Entity(bodyCore);
		
		// set the position to the current position of the body in front of this body
		if (this.bodies.size() == 0) {
			body.setPos(head.getPos());
		} else {
			Point3D pos = this.bodies.get(this.bodies.size() - 1).getPos();
			body.setPos(pos);
		}
		
		this.bodies.add(body);
		
		this.fatherGroup.getChildren().add(body.shell);
	}
	
	// Need to call this function before moving the snake's head position!
	public void updateBodyPosition() {
		for (int i = this.bodies.size() - 1; i > 0; i--) {
			Entity curBody = this.bodies.get(i);
			Point3D nextPos = this.bodies.get(i - 1).getPos();
			Point3D curPos = curBody.getPos();
			
			if (nextPos.subtract(curPos).magnitude() < 2 * this.bodySize) continue;
			curBody.move(nextPos.subtract(curPos).normalize().multiply(moveSpeed));
		}
		
		if (this.bodies.size() == 0) return;
		 
		Entity curBody = this.bodies.get(0);
		Point3D headPos = this.head.getPos();
		Point3D curPos = curBody.getPos();
		
		if (headPos.subtract(curPos).magnitude() < 2 * this.bodySize) return;
		curBody.move(headPos.subtract(curPos).normalize().multiply(moveSpeed));
	}
	
	// --------------- controls and animations -------------------
	public double moveSpeed = Config.snakeSpeed;
	public short intensityDamping = 30;
	
	/*
	
	pitch & yaw intensity: short [0 ~ intensityDamping]
	used to calculate the pitch value & yaw value of the snake's next move.
	
	*/
	
	private short pitchIntensity = (short) (intensityDamping / 2),
			yawIntensity = (short) (intensityDamping / 2);
	
	
	/*
	
	--- Temporary variables ---
	
	These variables were created to save computing resources.
	They will be updated before moving the snake head.
	
	pitch: current pitch of snakeHead + f(pitch intensity)
	+ pitchCos, pitchSin
	
	yaw: f(yaw intensity)
	+ yawCos, yawSin
	
	*/
	
	double pitch, pitchCos, pitchSin,
		yaw, yawCos, yawSin;
	
	public void updateTempRot() {
		pitch = Math.toRadians(
			-45 * (2 * Utils.easeInOut((double) pitchIntensity / intensityDamping) - 1)
		);
		pitchCos = Math.cos(pitch);
		pitchSin = Math.sin(pitch);
		
		yaw = Math.toRadians(
			45 * (2 * Utils.easeInOut((double) yawIntensity / intensityDamping) - 1)
		);
		yawCos = Math.cos(yaw);
		yawSin = Math.sin(yaw);
	}
	
	
	// Calculate pitch & yaw intensity based on the current status of keyHolds.
	private boolean wHold = false,
			sHold = false,
			aHold = false,
			dHold = false;
	
	public void writeKeyHold(KeyCode c, boolean mode) {
		switch (c) {
			case W: this.wHold = mode; break;
			case S: this.sHold = mode; break;
			case A: this.aHold = mode; break;
			case D: this.dHold = mode; break;
			default: break;
		}
	}
	
	public void bindMovements(SubScene s) {
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
			else if (pitchIntensity > intensityDamping) pitchIntensity = intensityDamping;
		} else {
			pitchIntensity -= Utils.sign((short) (pitchIntensity - intensityDamping / 2));
		}
		
		boolean yawAct = aHold ^ dHold;
		if (yawAct) {
			yawIntensity += aHold ? 1 : 0;
			yawIntensity -= dHold ? 1 : 0;
			
			if (yawIntensity < 0) yawIntensity = 0;
			else if (yawIntensity > intensityDamping) yawIntensity = intensityDamping;
		} else {
			yawIntensity -= Utils.sign((short) (yawIntensity - intensityDamping / 2));
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
	
	AnimationTimer movementTimer = new AnimationTimer() {
		@Override
		public void handle(long now) {
			updateRotIntensity();
			updateTempRot();
			
			
			Point3D headRot = head.getRot();
//			if (now % 1000 == 0) System.out.println();
			
			Point3D pitchVetor = headRotMatrix(new Point3D(0, -pitchSin, pitchCos));
			Point3D yawVector = headRotMatrix(new Point3D(-yawSin, 0, yawCos));
			
			Point3D directionVector = pitchVetor.add(yawVector)
					.normalize()
					.multiply(moveSpeed);
			
			moveHead(directionVector);
			head.setRot(
				headRot.getX(),
				(yaw + headRot.getY()) % 360,
				0
			);
			
			// ----- camera position update --------
			Point3D rightVector = headRotMatrix(new Point3D(1, 0, 0));
			Point3D frontVector = headRotMatrix(new Point3D(0, 0, 1));
			Point3D camPosVecor = rightVector.crossProduct(frontVector)
					.normalize()
					.multiply(15 * bodySize)
					.subtract(frontVector.normalize().multiply(40 * bodySize));
			
			camera.setPos(head.getPos().add(camPosVecor));
			camera.setRot(-20, -headRot.getY(), 0);
		}
	};
	
	public void moveHead(Point3D pos_v) {
		this.updateBodyPosition();
		this.head.move(pos_v);
	}
	
	public void startAnimation() {
		movementTimer.start();
	}
	
	public void stopAnimation() {
		movementTimer.stop();
	}
}
