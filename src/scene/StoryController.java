package scene;

/*

遊戲開始前撥放背景故事的操控器。

*/


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

public class StoryController implements Initializable {

    @FXML
    public Label line1;
    public Label line2;
    public Label line3;
    public Label line4;
    public Label line5;
    public Label line6;
    public Label line7;
    public Label line8;
    public Label skipLabel;
    
    
    private Stage stage;
    private Scene scene;
    private int curLine = -1;
    private Label[] story;
    
    private Timeline endStory = new Timeline(30,
		new KeyFrame(Duration.ZERO, e -> {
			for (Label l: story) {
				fadeOut(l);
			}
			fadeOut(skipLabel);
		}),
		new KeyFrame(Duration.seconds(2.2), e -> {
			startGame();
		})
	);
    
    private Timeline playStory = new Timeline(30,
		new KeyFrame(Duration.ZERO, e -> {
			if (curLine < 7) curLine++;
		}),
		new KeyFrame(Duration.millis(100), e -> {
			fadeInLabel(curLine);
		}),
		new KeyFrame(Duration.seconds(1.2), e -> {
			if (curLine == 7) endStory.play();
		})
	);
    
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		story = new Label[] {
			line1, line2, line3, line4,
			line5, line6, line7, line8
		};
		
		for (Label line: story) {
			line.setOpacity(0);
		}
		skipLabel.setOpacity(0);
		
		playStory.setCycleCount(8);
	}
	
	private void fadeInLabel(int index) {
		if (index == 0) {
			fadeIn(skipLabel);
		}
		fadeIn(story[index]);
	}
	
	private void fadeIn(Node n) {
		Timeline anima = new Timeline(
			new KeyFrame(Duration.ZERO, new KeyValue(n.opacityProperty(), 0)),
			new KeyFrame(Duration.seconds(1), new KeyValue(n.opacityProperty(), 1))
		);
		anima.play();
	}
	
	private void fadeOut(Node n) {
		Timeline anima = new Timeline(
			new KeyFrame(Duration.ZERO, new KeyValue(n.opacityProperty(), 1)),
			new KeyFrame(Duration.seconds(2), new KeyValue(n.opacityProperty(), 0))
		);
		anima.play();
	}
	
	public void startStory(Stage stage, Scene scene) {
		this.stage = stage;
		this.scene = scene;
		this.stage.setScene(scene);
		this.stage.show();
		
		addSkipKey();
		
		playStory.play();
	}
	
	private void addSkipKey() {
		scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
			if (e.getCode() != KeyCode.SPACE) return;
			
			playStory.stop();
			startGame();
		});
	}
	
	private void startGame() {
		playStory.stop();
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/scene/menu.fxml"));
		Parent root = null;
		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scene menuScene = new Scene(root);
		MenuController control = loader.getController();
		control.enterMenu(stage, menuScene);
	}
}
