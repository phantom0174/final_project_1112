package finalProject;


import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.input.KeyEvent;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

// Camera and its Group
// camera rotation belongs to the camera itself
// and the group moves the camera
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
	
	public void bindMovements(Stage s) {
		s.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
			double cur_arg = Math.toRadians(-yRot.get()),
					cos = Math.cos(cur_arg),
					sin = Math.sin(cur_arg);
			
			switch (event.getCode()) {
			case W: {
				this.move(new Point3D(-sin, 0, cos).multiply(5));
				break;
			}
			case S: {
				this.move(new Point3D(sin, 0, -cos).multiply(5));
				break;
			}
			case A: {
				this.move(new Point3D(-cos, 0, -sin).multiply(5));
				break;
			}
			case D: {
				this.move(new Point3D(cos, 0, sin).multiply(5));
				break;
			}
			case UP: {
				xRot.set(xRot.get() + 2);
				break;
			}
			case DOWN: {
				xRot.set(xRot.get() - 2);
				break;
			}
			case LEFT: {
				yRot.set(yRot.get() - 2);
				break;
			}
			case RIGHT: {
				yRot.set(yRot.get() + 2);
				break;
			}
			default:
				break;
			}
		});
	}
	
	public void move(Point3D pos_v) {
		xPos.set(xPos.get() + pos_v.getX());
		yPos.set(yPos.get() + pos_v.getY());
		zPos.set(zPos.get() + pos_v.getZ());
	}
	
	public Point3D getPos() {
		double x = this.xPos.get(),
			y = this.yPos.get(),
			z = this.zPos.get();
		
		return new Point3D(x, y, z);
	}
}
