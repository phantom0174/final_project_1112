package finalProject;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MoveAction;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;


public class Entity {
	// pos & rot
	public DoubleProperty x = new SimpleDoubleProperty(0),
			y = new SimpleDoubleProperty(0),
			z = new SimpleDoubleProperty(0);
	
	public Rotate xRot = new Rotate(0, Rotate.X_AXIS),
			yRot = new Rotate(0, Rotate.Y_AXIS),
			zRot = new Rotate(0, Rotate.Z_AXIS);
	
	public Sphere core;
	public Group shell = new Group();
	
	public Entity() {
		bindPosition();
	}
	
	public Entity(Sphere core) {
		bindPosition();

		this.core = core;
		this.shell.getChildren().add(core);
	}

	public void setRot(double xRot, double yRot, double zRot) {
		this.xRot.setAngle(xRot);
		this.yRot.setAngle(yRot);
		this.zRot.setAngle(zRot);
	}
	
	public void setPos(double x, double y, double z) {
		this.x.set(x);
		this.y.set(y);
		this.z.set(z);
	}
	
	public Point3D getPos() {
		return new Point3D(this.x.get(), this.y.get(), this.z.get());
	}
	
	public Point3D getRot() {
		return new Point3D(
			this.xRot.getAngle(), this.yRot.getAngle(), this.zRot.getAngle()
		);
	}
	
	private void bindPosition() {
		Translate pos = new Translate();
		pos.xProperty().bind(this.x);
		pos.yProperty().bind(this.y);
		pos.zProperty().bind(this.z);
		
		shell.getTransforms().add(pos);
	}
	
	public void move(Point3D pos_v) {
		x.set(x.get() + pos_v.getX());
		y.set(y.get() + pos_v.getY());
		z.set(z.get() + pos_v.getZ());
	}
}
