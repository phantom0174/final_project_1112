package view;

import java.io.IOException;

import base.View;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.SubScene;

public class GameEventView implements View {
	private boolean isloaded = false;
	
	private SubScene s;
	private Parent root = null;
	private GameEventController control = null;
	
	@Override
	public boolean isLoaded() {
		return isloaded;
	}

	@Override
	public void load() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("gameevent.fxml"));
		
		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		s = new SubScene(root, 1080, 600);
		control = loader.getController();
		
		isloaded = true;
	}

	@Override
	public void unload() {
		isloaded = false;
	}

	@Override
	public SubScene getSubScene() {
		return s;
	}
	
	public void showScore(int score) {
		control.showScore(score);
	}
	
	public void doubleScoreEffect(boolean on) {
		control.doubleScore(on);
	}
	
	public void outOfBoundaryOn() {
		control.showEvent("Don't get too FAR!");
	}
	
	public void outOfBoundaryOff() {
		control.closeEvent();
	}
}
