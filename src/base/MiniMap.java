package base;

import java.util.ArrayList;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.Group;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.Circle;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.HLineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;

public class MiniMap {
	public double mapRatio = 0.11;
	
	public Circle mapCircle = new Circle(100, 100, 80);
	public Polygon chara = new Polygon();
    public Group staticMap = new Group();
    
    public Pane applePane = new Pane();
    public Group appleMap = new Group();
    
    public Entity snakeHead = null;
    
    DoubleBinding x, y;
    
    // ------- copies --------
    ArrayList<Group> appleList = new ArrayList<>();
    
    public MiniMap(AnchorPane mapPane, Entity snakeHead, ArrayList<Group> appleList) {
    	this.snakeHead = snakeHead;
    	this.appleList = appleList;
    	
    	x = snakeHead.x.multiply(-mapRatio).add(100);
    	y = snakeHead.z.multiply(mapRatio).add(100);
    	
    	mapPane.setClip(mapCircle);
    	
    	setupStaticMap();
    	setupAppleMap();
    	
    	setupChara();
    	setupBoarder();
    	
    	mapPane.getChildren().addAll(
    		staticMap, applePane, setupBubble(), chara
    	);
    }
    
    private void setupStaticMap() {
    	staticMap.translateXProperty().bind(x);
    	staticMap.translateYProperty().bind(y);
    }
    
    private void setupAppleMap() {
    	appleMap.translateXProperty().bind(x);
    	appleMap.translateYProperty().bind(y);
    	
    	applePane.setPrefWidth(200);
    	applePane.setPrefHeight(200);
    	applePane.setClip(new Circle(100, 100, 40));
    	applePane.getChildren().add(appleMap);
    	
    	refreshAppleMap();
    }
    
    public void refreshAppleMap() {
    	appleMap.getChildren().clear();
    	
    	System.out.println("map apple count: " + appleList.size());
    	
    	for (Group a: appleList) {
    		double x = a.getTranslateX(),
    				y = a.getTranslateZ() * -1;
    		
    		Circle appleIcon = new Circle(x * mapRatio, y * mapRatio, 2);
    		appleIcon.setFill(Color.RED);
    		appleIcon.setStroke(Color.TRANSPARENT);
    		
    		appleMap.getChildren().add(appleIcon);
    	}
    }
    
    private void setupChara() {
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
    }
    
    private void setupBoarder() {
    	Circle safeBoarder = new Circle(0, 0, 800 * mapRatio);
    	safeBoarder.setFill(Color.rgb(200, 200, 200, 0.5));
    	safeBoarder.setStrokeWidth(2);
    	safeBoarder.setStroke(Color.LIGHTGREEN);
    	
    	Path upperDeadBoarder = drawSemiRing(
    		0, 0, 1300 * mapRatio, 800 * mapRatio, Color.DARKRED, Color.TRANSPARENT
    	);
    	Path lowerDeadBoarder = drawSemiRing(
    		0, 0, 1300 * mapRatio, 800 * mapRatio, Color.DARKRED, Color.TRANSPARENT
    	);
    	lowerDeadBoarder.setRotate(180);
    	lowerDeadBoarder.setTranslateY(1300 * mapRatio);
    	
    	upperDeadBoarder.setOpacity(0.3);
    	lowerDeadBoarder.setOpacity(0.3);
    	
    	Group boarders = new Group();
    	boarders.getChildren().addAll(safeBoarder, upperDeadBoarder, lowerDeadBoarder);
    	staticMap.getChildren().add(boarders);
    }
    
    private Circle setupBubble() {
    	Circle bubble = new Circle();
    	bubble.setFill(Color.TRANSPARENT);
    	bubble.setStroke(Color.WHITE);
    	bubble.setStrokeWidth(2);
    	bubble.setTranslateX(100);
    	bubble.setTranslateY(100);
    	
    	Timeline anima = new Timeline(60,
    		new KeyFrame(Duration.ZERO, new KeyValue(bubble.radiusProperty(), 0, Interpolator.EASE_OUT)),
    		new KeyFrame(Duration.ZERO, new KeyValue(bubble.opacityProperty(), 1, Interpolator.EASE_OUT)),
    		
    		new KeyFrame(Duration.seconds(1), new KeyValue(bubble.radiusProperty(), 20)),
    		new KeyFrame(Duration.seconds(1), new KeyValue(bubble.opacityProperty(), 0.8)),
    		
    		new KeyFrame(Duration.seconds(2), new KeyValue(bubble.radiusProperty(), 40)),
    		new KeyFrame(Duration.seconds(2), new KeyValue(bubble.opacityProperty(), 0))
    	);
    	anima.setCycleCount(Timeline.INDEFINITE);
    	anima.play();
    	
    	return bubble;
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
