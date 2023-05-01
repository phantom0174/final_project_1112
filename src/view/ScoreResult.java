package view;

import base.Config;
import base.View;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class ScoreResult implements View {
	public boolean isloaded = false;
	
	public SubScene s;
	public StackPane root;
	
	public int score;
	
	public ScoreResult(int score) {
		this.score = score;
	}

	@Override
	public boolean isLoaded() {
		return isloaded;
	}

	@Override
	public void load() {
		Text scoreText = new Text("Your score: " + score + "\n\n" + "Press ESC to go to Menu");
		scoreText.setTextAlignment(TextAlignment.CENTER);
		scoreText.setFont(new Font(30));
		
		root = new StackPane();
		root.getChildren().addAll(scoreText);
		StackPane.setAlignment(scoreText, Pos.CENTER);
		
		s = new SubScene(root, Config.width, Config.height);
		s.setFill(Color.BLACK);
		s.setOpacity(0.8);
		
		isloaded = true;
	}

	@Override
	public void unload() {
		root.getChildren().clear();
	}

	@Override
	public SubScene getSubScene() {
		return s;
	}

}
