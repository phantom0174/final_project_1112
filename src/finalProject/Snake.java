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
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.PointLight;
import javafx.scene.SubScene;
import javafx.scene.paint.Color;
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
	
	public Entity snakeLight;
	
	public SnakeTexture texture = new SnakeTexture();
	
	public Snake(Group fatherGroup, SnakeCamera mainCam) {
		this.fatherGroup = fatherGroup;
		this.camera = mainCam;
		
		moveSpeed.bind(Config.snakeSpeed);
		
		initializeHead();
		initializeLight();
	}
	
	// --------------- logic and initialize ----------------
	
	private void initializeHead() {
		Group headCore = new Group();
		Sphere headBall = new Sphere(bodySize);
		Box tenta1 = new Box(1, 5, 1),
			tenta2 = new Box(1, 5, 1);
		
		tenta1.getTransforms().addAll(
			new Rotate(-10, Rotate.Z_AXIS),
			new Translate(-1, -5, 0)
		);
		tenta2.getTransforms().addAll(
			new Rotate(10, Rotate.Z_AXIS),
			new Translate(1, -5, 0)
		);
		tenta1.setMaterial(texture.bodyMat);
		tenta2.setMaterial(texture.bodyMat);
		
		headBall.setMaterial(texture.headMat);
		
		headCore.getChildren().addAll(headBall, tenta1, tenta2);
		
		this.head = new Entity(headCore);
		this.bodies.add(this.head);
		this.fatherGroup.getChildren().add(this.head.shell);
	}
	
	private void initializeLight() {
		double intensity = 0.8;
		
		PointLight paraLight = new PointLight(Color.WHITE),
				charaLight = new PointLight(Color.WHITE),
				frontLight = new PointLight(Color.WHITE);
		
		paraLight.setConstantAttenuation(1 / intensity);
		paraLight.setLinearAttenuation((1 / intensity) / 1000);
		
		charaLight.setConstantAttenuation(1 / intensity);
		charaLight.setLinearAttenuation((1 / intensity) / 1000);
		
		frontLight.setConstantAttenuation(1 / intensity);
		frontLight.setLinearAttenuation((1 / intensity) / 1000);
		
		paraLight.setTranslateY(-8 * bodySize);
		paraLight.setTranslateZ(-4 * bodySize);
		
		charaLight.setTranslateZ(4 * bodySize);
		
		Group snakeLightCore = new Group();
		snakeLightCore.getChildren().addAll(paraLight, charaLight, frontLight);
		
		Point3D lightPosVecor = new Point3D(0, 0, 5 * bodySize)
				.add(new Point3D(0, 0, -100)); // head initial position
		
		snakeLight = new Entity(snakeLightCore);
		snakeLight.yRot.bind(head.yRot);
		snakeLight.setPos(lightPosVecor);
		
		fatherGroup.getChildren().add(snakeLight.shell);
	}
	
	public void generateBody() {
		Sphere bodyCore = new Sphere(bodySize);
		bodyCore.setMaterial(texture.bodyMat);
		
		Entity body = new Entity(bodyCore);
		
		// set the position to the current position of the body in front of this body
		Point3D pos = bodies.get(bodies.size() - 1).getPos();
		body.setPos(pos);
		
		bodies.add(body);
		fatherGroup.getChildren().add(body.shell);
	}
	
	public void alignBodies() {
		Point3D prevPos = head.getPos();
		
		for (int i = 1; i < bodies.size(); i++) {
			Entity e = bodies.get(i);
			e.setPos(prevPos.add(new Point3D(0, 0, -2 * bodySize)));
			prevPos = e.getPos();
		}
	}
	
	public void setInitialCameraPos() {
		Point3D camPosVecor = new Point3D(0, -12, 110)
				.add(head.getPos());
		
		camera.setPos(camPosVecor);
		camera.setRot(-8, 180, 0);
	}
	
	// Need to call this function before moving the snake's head position!
	public void updateBodyPosition(boolean isDead) {
		Point3D direcVec;
		for (int i = bodies.size() - 1; i > 0; i--) {
			Entity curBody = bodies.get(i);
			
			Point3D nextPos = bodies.get(i - 1).getPos(),
					curPos = curBody.getPos();
			
			Point3D diffVec = nextPos.subtract(curPos);
			
			if (!isDead) direcVec = diffVec
					.normalize()
					.multiply(moveSpeed.get());
			else {
				direcVec = curPos
					.subtract(deadPos)
					.normalize()
					.multiply(moveSpeed.get());
				curBody.move(direcVec);
				continue;
			}
			
			double diffBodyCount = diffVec.magnitude() / bodySize;
			if (diffBodyCount < 2) {
				if (diffBodyCount < 1.8)
					direcVec = direcVec.multiply(0.7);
				else
					direcVec = direcVec.multiply(0.95);
			}
			
			curBody.move(direcVec);
		}
	}
	
	// --------------- controls and animations -------------------
	public DoubleProperty moveSpeed = new SimpleDoubleProperty(1);
	private ControlHandler keyControl = new ControlHandler();
	
	public void bindMovements(SubScene s) {
		keyControl.bindControls(s);
	}
	
	public void unbindMovements(SubScene s) {
		keyControl.unbindControls(s);
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
	
	private double absPitch, absPitchCos, absPitchSin,
		absYaw, absYawCos, absYawSin;
	
	private void updateTempRot() {
		keyControl.updateRotIntensity();
		
		absPitch = Math.toRadians(
			-90 * Utils.easeInOut((double) keyControl.pitchIntensity / keyControl.intensityDamping)
		);
		absPitchCos = Math.cos(absPitch);
		absPitchSin = Math.sin(absPitch);
		
		absYaw = Math.toRadians(
			(45 * moveSpeed.get())
			* Utils.easeInOut((double) keyControl.yawIntensity / keyControl.intensityDamping)
		);
		absYawCos = Math.cos(absYaw);
		absYawSin = Math.sin(absYaw);
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
		
		相機視角在蟲蟲的後正方。
		蟲蟲探照燈在前上方。
		
		*/
		// ----- camera position update --------
		if (!keyControl.spaceHold && !isDead) updateCameraToBack();
		else updateCameraToFront();
		
		// ----- snakeLight position update -----
		Point3D frontVector = relativeRot(new Point3D(0, 0, 5));
		Point3D lightPosVecor = frontVector
				.add(head.getPos());
		
		snakeLight.setPos(lightPosVecor);
	}
	
	public void moveHead(Point3D pos_v, boolean isDead) {
		this.updateBodyPosition(isDead);
		
		if (!isDead) this.head.move(pos_v);
		else this.head.move(
			head.getPos().subtract(deadPos).normalize().multiply(moveSpeed.get())
		);
	}
	
	public void updateCameraToFront() {
		Point3D camPosVecor = relativeRot(new Point3D(0, 30, 170))
				.add(head.getPos());
		
		camera.setPos(camPosVecor);
		camera.setRot(-10, head.getRot().getY() + 180, 0);
	}
	
	public void updateCameraToBack() {
		Point3D frontVector = relativeRot(new Point3D(0, 0, 1));
		
		Point3D camPosVecor = new Point3D(0, -13 * bodySize, 0)
				.subtract(frontVector.multiply(35 * bodySize))
				.add(head.getPos());
		
		camera.setPos(camPosVecor);
		camera.setRot(-18, head.getRot().getY(), 0);
	}
}
