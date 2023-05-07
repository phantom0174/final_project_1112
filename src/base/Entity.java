package base;

/*

An "Entity" is an encapsulation of a node(core) by a group(shell).

Reason:
	The rotation and translation actions performed on a node need to be separated,
	otherwise the movement will be corrupted.
	
	To separated those two transformation, we let the core itself
	focus on rotation, and its shell focus on translation.
	The core will moves with the shell.

*/


import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;


public class Entity {
	public DoubleProperty x = new SimpleDoubleProperty(0),
			y = new SimpleDoubleProperty(0),
			z = new SimpleDoubleProperty(0);
	
	public DoubleProperty xRot = new SimpleDoubleProperty(0),
			yRot = new SimpleDoubleProperty(0),
			zRot = new SimpleDoubleProperty(0);
	
	public Node core;
	public Group shell = new Group();
	
	public Entity(Node core) {
		this.core = core;

		bindPosition();
		bindRotation();

		this.shell.getChildren().add(this.core);
	}

	public void setRot(double xRot, double yRot, double zRot) {
		this.xRot.set(xRot);
		this.yRot.set(yRot);
		this.zRot.set(zRot);
	}
	
	public void setRot(Point3D p) {
		this.xRot.set(p.getX());
		this.yRot.set(p.getY());
		this.zRot.set(p.getZ());
	}
	
	public void setPos(double x, double y, double z) {
		this.x.set(x);
		this.y.set(y);
		this.z.set(z);
	}
	
	public void setPos(Point3D p) {
		this.x.set(p.getX());
		this.y.set(p.getY());
		this.z.set(p.getZ());
	}
	
	public Point3D getPos() {
		return new Point3D(this.x.get(), this.y.get(), this.z.get());
	}
	
	public Point3D getRot() {
		return new Point3D(
			this.xRot.get(), this.yRot.get(), this.zRot.get()
		);
	}
	
	private void bindPosition() {
		Translate pos = new Translate();
		pos.xProperty().bind(this.x);
		pos.yProperty().bind(this.y);
		pos.zProperty().bind(this.z);
		
		shell.getTransforms().add(pos);
	}
	
	private void bindRotation() {
		// order matters
		// z -> x -> y
		Rotate xR = new Rotate(0, Rotate.X_AXIS),
				yR = new Rotate(0, Rotate.Y_AXIS),
				zR = new Rotate(0, Rotate.Z_AXIS);
		
		xR.angleProperty().bind(xRot);
		yR.angleProperty().bind(yRot);
		zR.angleProperty().bind(zRot);
		
		this.core.getTransforms().addAll(yR, xR, zR);
	}
	
	public void move(Point3D pos_v) {
		x.set(x.get() + pos_v.getX());
		y.set(y.get() + pos_v.getY());
		z.set(z.get() + pos_v.getZ());
	}
}
