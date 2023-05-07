package view;

/*

遊戲中的 2D 介面操控器，詳情請見 GameEventView。

*/


import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.util.Duration;


public class GameEventController implements Initializable {
    @FXML
    public Label scoreLabel;
    public Label eventLabel;
    public Pane upperPane;
    public Pane lowerPane;
    
    Timeline appear, disappear, flashingBanner;
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		scoreLabel.setText("0");
    	eventLabel.setOpacity(0);
    	
    	upperPane.setOpacity(0);
    	lowerPane.setOpacity(0);
    	
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
    	
    	flashingBanner = new Timeline(30,
    		new KeyFrame(Duration.ZERO, new KeyValue(upperPane.opacityProperty(), 0)),
    		new KeyFrame(Duration.ZERO, new KeyValue(lowerPane.opacityProperty(), 0)),
    		new KeyFrame(Duration.millis(500), new KeyValue(upperPane.opacityProperty(), 0.9)),
    		new KeyFrame(Duration.millis(500), new KeyValue(lowerPane.opacityProperty(), 0.9)),
    		new KeyFrame(Duration.seconds(1), new KeyValue(upperPane.opacityProperty(), 0)),
    		new KeyFrame(Duration.seconds(1), new KeyValue(lowerPane.opacityProperty(), 0))
    	);
    	flashingBanner.setCycleCount(Timeline.INDEFINITE);
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
    		flashingBanner.stop();
    	}
    	
    	eventLabel.setText(s);
    	appear.play();
    	flashingBanner.play();
    	
    	showingEvent = true;
    }
    
    public void closeEvent() {
    	disappear.play();
    	flashingBanner.stop();
    	upperPane.setOpacity(0);
    	lowerPane.setOpacity(0);
    }
}
