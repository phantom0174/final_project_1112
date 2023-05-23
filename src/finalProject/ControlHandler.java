package finalProject;


import base.Utils;
import javafx.event.EventHandler;
import javafx.scene.SubScene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class ControlHandler {
	public short intensityDamping = 30;
	
	/*
	
	pitch & yaw intensity: short [-intensityDamping ~ intensityDamping]
	used to calculate the pitch value & yaw value of the snake's next move.
	
	*/
	
	public short pitchIntensity = 0,
			yawIntensity = 0;
	
	
	// Calculate pitch & yaw intensity based on the current status of keyHolds.
	public boolean wHold = false,
			sHold = false,
			aHold = false,
			dHold = false,
			spaceHold = false;
	
	private void writeKeyHold(KeyCode c, boolean mode) {
		switch (c) {
			case W: this.wHold = mode; break;
			case S: this.sHold = mode; break;
			case A: this.aHold = mode; break;
			case D: this.dHold = mode; break;
			case SPACE: this.spaceHold = mode; break;
			default: break;
		}
	}
	
	private EventHandler<KeyEvent> keyHold = event -> {
    	writeKeyHold(event.getCode(), true);
	};
	
	private EventHandler<KeyEvent> keyReleased = event -> {
    	writeKeyHold(event.getCode(), false);
	};
	
	public void bindControls(SubScene s) {
		s.addEventFilter(KeyEvent.KEY_PRESSED, keyHold);
        s.addEventFilter(KeyEvent.KEY_RELEASED, keyReleased);
	}
	
	public void unbindControls(SubScene s) {
		s.removeEventFilter(KeyEvent.KEY_PRESSED, keyHold);
        s.removeEventFilter(KeyEvent.KEY_RELEASED, keyReleased);
	}
	
	public void updateRotIntensity() {
		boolean pitchAct = wHold ^ sHold;
		
		if (pitchAct) {
			pitchIntensity += wHold ? 1 : 0;
			pitchIntensity -= sHold ? 1 : 0;

			if (pitchIntensity < -intensityDamping)
				pitchIntensity = (short) -intensityDamping;
			else if (pitchIntensity > intensityDamping)
				pitchIntensity = intensityDamping;
		} else {
			pitchIntensity -= Utils.sign(pitchIntensity);
		}
		
		boolean yawAct = aHold ^ dHold;
		if (yawAct) {
			yawIntensity -= aHold ? 1 : 0;
			yawIntensity += dHold ? 1 : 0;
			
			if (yawIntensity < -intensityDamping)
				yawIntensity = (short) -intensityDamping;
			else if (yawIntensity > intensityDamping)
				yawIntensity = intensityDamping;
		} else {
			yawIntensity -= Utils.sign(yawIntensity);
		}
	}
}
