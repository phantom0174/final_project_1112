package view;

/*

遊戲中的 2D 介面操控器，詳情請見 GameEventView。

*/


import java.net.URL;
import java.util.ResourceBundle;

import base.Entity;
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
    
    public Circle mapCircle;
    public Group staticMap;
    public Polygon chara;
    
    public void setupMap(Entity snakeHead) {
    	mapCircle = new Circle(100, 100, 80);
    	mapPane.setClip(mapCircle);
    	
    	chara = new Polygon();
    	chara.getPoints().addAll(new Double[] {
    		0d, 3d,
    		-6d, 6d,
    		0d, -6d,
    		6d, 6d
    	});
    	chara.setFill(Color.LIGHTGREEN);
    	chara.setStroke(Color.TRANSPARENT);
    	chara.setStrokeWidth(0);
    	chara.rotateProperty().bind(snakeHead.yRot);
    	chara.setTranslateX(100);
    	chara.setTranslateY(100);
    	
    	double ratio = 0.1;
    	
    	Circle boarder = new Circle(0, 0, 800 * ratio);
    	boarder.setFill(Color.rgb(200, 200, 200, 0.5));
    	boarder.setStrokeWidth(2);
    	boarder.setStroke(Color.LIGHTGREEN);
    	
    	Path upperDeadBoarder = drawSemiRing(
    		0, 0, 1300 * ratio, 800 * ratio, Color.DARKRED, Color.TRANSPARENT
    	);
    	Path lowerDeadBoarder = drawSemiRing(
    		0, 0, 1300 * ratio, 800 * ratio, Color.DARKRED, Color.TRANSPARENT
    	);
    	lowerDeadBoarder.setRotate(180);
    	lowerDeadBoarder.setTranslateY(1300 * ratio);
    	
    	upperDeadBoarder.setOpacity(0.3);
    	lowerDeadBoarder.setOpacity(0.3);
    	
    	staticMap = new Group();
    	staticMap.getChildren().addAll(boarder, upperDeadBoarder, lowerDeadBoarder);
    	staticMap.translateXProperty().bind(snakeHead.x.multiply(-1 * ratio).add(100));
    	staticMap.translateYProperty().bind(snakeHead.z.multiply(ratio).add(mapCircle.getTranslateY()).add(100));
    	
    	mapPane.getChildren().addAll(staticMap, chara);
    }
    
    // source: https://stackoverflow.com/questions/11719005/draw-a-semi-ring-javafx
    private Path drawSemiRing(double centerX, double centerY, double radius, double innerRadius, Color bgColor, Color strkColor) {
        Path path = new Path();
        path.setFill(bgColor);
        path.setStroke(strkColor);
        path.setFillRule(FillRule.EVEN_ODD);

        MoveTo moveTo = new MoveTo();
        moveTo.setX(centerX + innerRadius);
        moveTo.setY(centerY);

        ArcTo arcToInner = new ArcTo();
        arcToInner.setX(centerX - innerRadius);
        arcToInner.setY(centerY);
        arcToInner.setRadiusX(innerRadius);
        arcToInner.setRadiusY(innerRadius);

        MoveTo moveTo2 = new MoveTo();
        moveTo2.setX(centerX + innerRadius);
        moveTo2.setY(centerY);

        HLineTo hLineToRightLeg = new HLineTo();
        hLineToRightLeg.setX(centerX + radius);

        ArcTo arcTo = new ArcTo();
        arcTo.setX(centerX - radius);
        arcTo.setY(centerY);
        arcTo.setRadiusX(radius);
        arcTo.setRadiusY(radius);

        HLineTo hLineToLeftLeg = new HLineTo();
        hLineToLeftLeg.setX(centerX - innerRadius);

        path.getElements().addAll(moveTo, arcToInner, moveTo2, hLineToRightLeg, arcTo, hLineToLeftLeg);
        return path;
    }
}
