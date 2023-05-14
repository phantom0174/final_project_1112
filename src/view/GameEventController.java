package view;

/*

遊戲中的 2D 介面操控器，詳情請見 GameEventView。

*/


import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import base.Entity;
import base.MiniMap;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.Circle;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.HLineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Polygon;
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
    
    @FXML
    public AnchorPane mapPane;
    public MiniMap miniMap = null;
    
    public void setupMap(Entity snakeHead, ArrayList<Group> appleList) {
    	miniMap = new MiniMap(mapPane, snakeHead, appleList);
    }
}
