package finalProject;

/*

玩家操控的主角。

--- Controls ---

Snake will move with a certain rate all the time.

W/S keys will increase/decrease pitch angle to a certain amount.
A/D keys will decrease/increase yaw angle to a certain amount.

Pitch will slowly reset to 0 if W/S key is not pressed.
Similarly for Yaw (A/D keys)


*/


import java.util.ArrayList;


import base.Config;
import base.Entity;
import base.Utils;
import camera.SnakeCamera;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.PointLight;
import javafx.scene.SubScene;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;


public class Snake {
	public Group fatherGroup;
	public SnakeCamera camera;
	
	public Entity head;
	public ArrayList<Entity> bodies = new ArrayList<>();
	
	public final double bodySize = 5;
	
	public PointLight snakeLight = new PointLight(Color.WHITE);
	
	public Snake(Group fatherGroup, SnakeCamera mainCam) {
		this.fatherGroup = fatherGroup;
		this.camera = mainCam;
		
		moveSpeed.bind(Config.snakeSpeed);
		
		initializeHead();
		initializeLight();
		setInitialCameraPos();
	}
	
	// --------------- logic and initialize ----------------
	
	private void initializeHead() {
		Group headCore = new Group();
		Sphere headBall = new Sphere(bodySize);
		Box tenta1 = new Box(1, 5, 1),
			tenta2 = new Box(1, 5, 1);
		
		tenta1.getTransforms().addAll(
			new Rotate(-45, Rotate.Z_AXIS),
			new Translate(-5, -5, 0)
		);
		tenta2.getTransforms().addAll(
			new Rotate(45, Rotate.Z_AXIS),
			new Translate(5, -5, 0)
		);
		
		headCore.getChildren().addAll(headBall, tenta1, tenta2);
		
		PhongMaterial headMaterial = new PhongMaterial(Color.GREEN);
		headMaterial.setDiffuseMap(new Image(getClass().getResourceAsStream("/resources/materials/planet1.jpg")));
		headBall.setMaterial(headMaterial);
//		headCore.setMaterial(headMaterial);
		
		this.head = new Entity(headCore);
		this.fatherGroup.getChildren().add(this.head.shell);	
	}
	
	private void initializeLight() {
		double intensity = 1;
		snakeLight.setLightOn(true);
		
		snakeLight.setConstantAttenuation((1 / intensity));
		snakeLight.setLinearAttenuation((1 / intensity) / 1000);
		snakeLight.setQuadraticAttenuation((1 / intensity) / 20000);
		
		
		Point3D lightPosVecor = new Point3D(0, 0, 5 * bodySize)
				.add(new Point3D(0, 0, -100)); // head initial position
		
		snakeLight.setTranslateX(lightPosVecor.getX());
		snakeLight.setTranslateY(lightPosVecor.getY());
		snakeLight.setTranslateZ(lightPosVecor.getZ());
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
	
	public void alignBodies() {
		Point3D prevPos = head.getPos();
		
		for (Entity e: bodies) {
			e.setPos(prevPos.add(new Point3D(0, 0, -2 * bodySize)));
			prevPos = e.getPos();
		}
	}
	
	public void setInitialCameraPos() {
//		Point3D camPosVecor = new Point3D(0, -15 * bodySize, -40 * bodySize);
		
		Point3D camPosVecor = new Point3D(0, 0, 50);
		
		camera.setPos(head.getPos().add(camPosVecor));
//		camera.setRot(-20, 0, 0);
		camera.setRot(0, -180, 0);
	}
	
	// Need to call this function before moving the snake's head position!
	public void updateBodyPosition(boolean isDead) {
		for (int i = this.bodies.size() - 1; i > 0; i--) {
			Entity curBody = this.bodies.get(i);
			Point3D nextPos = this.bodies.get(i - 1).getPos();
			Point3D curPos = curBody.getPos();
			
			if (isDead) {
				curBody.move(curPos.subtract(deadPos).normalize().multiply(moveSpeed.get()));
				continue;
			}
			
			if (nextPos.subtract(curPos).magnitude() < 2 * this.bodySize) continue;
			curBody.move(nextPos.subtract(curPos).normalize().multiply(moveSpeed.get()));
		}
		
		if (this.bodies.size() == 0) return;
		
		Entity curBody = this.bodies.get(0);
		Point3D headPos = this.head.getPos();
		Point3D curPos = curBody.getPos();
		
		if (isDead) {
			curBody.move(curPos.subtract(deadPos).normalize().multiply(moveSpeed.get()));
			return;
		}
		
		if (headPos.subtract(curPos).magnitude() < 2 * this.bodySize) return;
		curBody.move(headPos.subtract(curPos).normalize().multiply(moveSpeed.get()));
	}
	
	// --------------- controls and animations -------------------
	public DoubleProperty moveSpeed = new SimpleDoubleProperty(1);
	public short intensityDamping = 30;
	
	/*
	
	pitch & yaw intensity: short [0 ~ intensityDamping]
	used to calculate the pitch value & yaw value of the snake's next move.
	
	*/
	
	private short pitchIntensity = (short) (intensityDamping / 2),
			yawIntensity = (short) (intensityDamping / 2);
	
	
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
	
	EventHandler<KeyEvent> keyHold = event -> {
    	writeKeyHold(event.getCode(), true);
	};
	
	EventHandler<KeyEvent> keyReleased = event -> {
    	writeKeyHold(event.getCode(), false);
	};
	
	public void bindMovements(SubScene s) {
		s.addEventFilter(KeyEvent.KEY_PRESSED, keyHold);
        s.addEventFilter(KeyEvent.KEY_RELEASED, keyReleased);
	}
	
	public void unbindMovements(SubScene s) {
		s.removeEventFilter(KeyEvent.KEY_PRESSED, keyHold);
        s.removeEventFilter(KeyEvent.KEY_RELEASED, keyReleased);
	}
	
	/*
	
	--- Temporary variables ---
	
	These variables were created to save computing resources.
	They will be updated before moving the snake head.
	
	pitch: current pitch of snakeHead + f(pitch intensity)
	+ pitchCos, pitchSin
	
	yaw: f(yaw intensity)
	+ yawCos, yawSin
	
	*/
	
	double absPitch, absPitchCos, absPitchSin,
		absYaw, absYawCos, absYawSin;
	
	public void updateTempRot() {
		absPitch = Math.toRadians(
			-45 * (2 * Utils.easeInOut((double) pitchIntensity / intensityDamping) - 1)
		);
		absPitchCos = Math.cos(absPitch);
		absPitchSin = Math.sin(absPitch);
		
		absYaw = Math.toRadians(
			(45 * moveSpeed.get() / 2) * (2 * Utils.easeInOut((double) yawIntensity / intensityDamping) - 1)
		);
		absYawCos = Math.cos(absYaw);
		absYawSin = Math.sin(absYaw);
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
			yawIntensity -= aHold ? 1 : 0;
			yawIntensity += dHold ? 1 : 0;
			
			if (yawIntensity < 0) yawIntensity = 0;
			else if (yawIntensity > intensityDamping) yawIntensity = intensityDamping;
		} else {
			yawIntensity -= Utils.sign((short) (yawIntensity - intensityDamping / 2));
		}
	}
	
	public Point3D relativeRot(Point3D v) {
		/*
			M = (x, z, y)
			[ Cy -Sy  0 ]
			[ Sy  Cy  0 ]
			[ 0   0  -1 ]
			
			return (M)v
		*/
		
		// rotation matrix is in the opposite direction!
		double yaw = -Math.toRadians(head.getRot().getY()),
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
	
	public Point3D deadPos = new Point3D(0, 0, 0);
	public void updateFrameMovement(boolean isDead) {
		updateRotIntensity();
		updateTempRot();
		
		
		Point3D headRot = head.getRot();
		
		Point3D pitchVetor = relativeRot(new Point3D(0, -absPitchSin, absPitchCos));
		
		// rotation matrix is in the opposite direction!
		Point3D yawVector = relativeRot(new Point3D(absYawSin, 0, absYawCos));
		
		Point3D directionVector = pitchVetor.add(yawVector)
				.normalize()
				.multiply(moveSpeed.get());
		
		moveHead(directionVector, isDead);
		head.setRot(
			headRot.getX(),
			(absYaw + headRot.getY()) % 360,
			0
		);
		
		/*
		
		相機視角在蛇的後正方。
		
		垂直法向量由 i 與 k 向量進行 headRotation 修正之後進行外積而得。
		
		*/
		// ----- camera position update --------
		Point3D rightVector = relativeRot(new Point3D(1, 0, 0));
		Point3D frontVector = relativeRot(new Point3D(0, 0, 1));
		Point3D camPosVecor = rightVector.crossProduct(frontVector)
				.normalize()
				.multiply(15 * bodySize)
				.subtract(frontVector.normalize().multiply(40 * bodySize))
				.add(head.getPos());
		
		camera.setPos(camPosVecor);
		camera.setRot(-20, headRot.getY(), 0);
		
		
		// ----- snakeLight position update -----
		Point3D lightPosVecor = new Point3D(0, -5 * bodySize, 0).add(frontVector.multiply(5))
				.add(head.getPos());
		
		snakeLight.setTranslateX(lightPosVecor.getX());
		snakeLight.setTranslateY(lightPosVecor.getY());
		snakeLight.setTranslateZ(lightPosVecor.getZ());
	}
	
	public void moveHead(Point3D pos_v, boolean isDead) {
		this.updateBodyPosition(isDead);
		
		if (!isDead) this.head.move(pos_v);
		else this.head.move(
			head.getPos().subtract(deadPos).normalize().multiply(moveSpeed.get())
		);
	}
}
