package view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;

/*

遊戲結束介面的控制器。

*/


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class GameoverController implements Initializable {
	@FXML
	public Label showScore;
	public Label deadReason;
	public Pane coverPane;
	
	private static String scoreBase = "Your Score: ";

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		coverPane.setOpacity(0);
	}
	
	public void setScore(int score) {
		this.showScore.setText(scoreBase + String.valueOf(score));
	}
	
	public void showMessage(String msg) {
		this.deadReason.setText(msg);
	}
	
	public void showCoverPane() {
		Timeline anima = new Timeline(30,
			new KeyFrame(Duration.ZERO, new KeyValue(coverPane.opacityProperty(), 0)),
			new KeyFrame(Duration.seconds(3), new KeyValue(coverPane.opacityProperty(), 1))
		);
		anima.play();
	}
}
