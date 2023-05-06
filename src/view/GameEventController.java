package view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class GameEventController implements Initializable {
    @FXML
    public Label scoreLabel;
    @FXML
    public Label eventLabel;
    
    Timeline appear, disappear;
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		scoreLabel.setText("0");
    	eventLabel.setOpacity(0);
    	
    	setupAnimation();
	}
    
    public void setupAnimation() {
    	appear = new Timeline(30,
			new KeyFrame(Duration.ZERO, new KeyValue(eventLabel.opacityProperty(), 0)),
			new KeyFrame(Duration.millis(500), new KeyValue(eventLabel.opacityProperty(), 1))
		);
    	
    	disappear = new Timeline(30,
			new KeyFrame(Duration.ZERO, new KeyValue(eventLabel.opacityProperty(), 1)),
			new KeyFrame(Duration.millis(500), new KeyValue(eventLabel.opacityProperty(), 0)),
			new KeyFrame(Duration.millis(500), e -> { showingEvent = false; })
		);
    }
    
    public void showScore(int score) {
    	scoreLabel.setText(String.valueOf(score));
    }
    
    public void doubleScore(boolean on) {
    	if (on) {
    		scoreLabel.setStyle("-fx-text-fill: gold");
    	} else {
    		scoreLabel.setStyle("-fx-text-fill: white");
    	}
    }
    
    private boolean showingEvent = false;
    public void showEvent(String s) {
    	if (showingEvent) {
    		appear.stop();
    		disappear.stop();
    	}
    	
    	eventLabel.setText(s);
    	appear.play();
    	
    	showingEvent = true;
    }
    
    public void closeEvent() {
    	disappear.play();
    }
}
