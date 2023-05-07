package view;

/*

遊戲結束介面的控制器。

*/


import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class GameoverController {
	@FXML
	public Label showScore;
	public Label deadReason;
	
	private static String scoreBase = "Your Score: ";
	
	public void setScore(int score) {
		this.showScore.setText(scoreBase + String.valueOf(score));
	}
	
	public void showDeadReason(String deadReason) {
		this.deadReason.setText(deadReason);
	}
}
