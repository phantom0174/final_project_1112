package finalProject;


import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.PerspectiveCamera;
import javafx.scene.transform.Rotate;

public class MyCamera {
	public Camera c = new PerspectiveCamera(true);
	
	// rotation
	public DoubleProperty xRot = new SimpleDoubleProperty(180),
			yRot = new SimpleDoubleProperty(-180),
			zRot = new SimpleDoubleProperty(180);
	
	// position
	public DoubleProperty xPos = new SimpleDoubleProperty(0),
			yPos = new SimpleDoubleProperty(10),
			zPos = new SimpleDoubleProperty(-200);
	
	public MyCamera() {
		this.c.translateXProperty().bind(xPos);
		this.c.translateYProperty().bind(yPos);
		this.c.translateZProperty().bind(zPos);
		
		this.c.setNearClip(1);
		this.c.setFarClip(10000);
		
		Rotate xRotate = new Rotate(xRot.get(), Rotate.X_AXIS),
			yRotate = new Rotate(yRot.get(), Rotate.Y_AXIS),
			zRotate = new Rotate(zRot.get(), Rotate.Z_AXIS);
		
		// useless things
//		xRotate.pivotXProperty().bind(xPos);
//		xRotate.pivotYProperty().bind(yPos);
//		xRotate.pivotZProperty().bind(zPos);
//		
//		yRotate.pivotXProperty().bind(xPos);
//		yRotate.pivotYProperty().bind(yPos);
//		yRotate.pivotZProperty().bind(zPos);
//		
//		zRotate.pivotXProperty().bind(xPos);
//		zRotate.pivotYProperty().bind(yPos);
//		zRotate.pivotZProperty().bind(zPos);
		
		xRotate.angleProperty().bind(xRot);
		yRotate.angleProperty().bind(yRot);
		zRotate.angleProperty().bind(zRot);
		
		this.c.getTransforms().addAll(xRotate, yRotate, zRotate);
	}
	
	public void move(Point3D pos_v) {
		xPos.set(xPos.get() + pos_v.getX());
		yPos.set(yPos.get() + pos_v.getY());
		zPos.set(zPos.get() + pos_v.getZ());
		
		System.out.println(
			"Pos: " + xPos.get() + ", " + yPos.get() + ", " + zPos.get()
		);
		
		System.out.println(
			"Ang: " + xRot.get() + ", " + yRot.get() + ", " + zRot.get()
		);
	}
	
	public Point3D getPos() {
		double x = this.xPos.get(),
			y = this.yPos.get(),
			z = this.zPos.get();
		
		return new Point3D(x, y, z);
	}
	
	public Point3D getRawRot() {
		return new Point3D(
			xRot.get() - 180,
			yRot.get() + 180,
			zRot.get() - 180
		);
	}
}
