package scene;


import java.io.IOException;

import base.Config;
import base.GameStatus;
import base.SoundPlayer;
import base.ViewHandler;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import view.BGView;
import view.GameView;
import view.ScoreResult;

/*

Three steps to activate a view:
1. load scene entities
2. attach scene to root
3. switch to scene

*/

public class GameScene {
	public Scene s;
	public Group root;
	private ViewHandler view = new ViewHandler();
	
	private GameView gameView;
	
	// ---- sounds -----
	private SoundPlayer sound = new SoundPlayer();
	private String bgm = "bgm/my-lonely-journey.mp3";
	
	public GameScene() {
		root = new Group();
		s = new Scene(root, Config.width, Config.height);
		
		addViews();
		
		view.load("3d_game_view");
		view.attach("3d_game_view", root);
		
		view.load("2d_bg");
		view.attach("2d_bg", root);
		
		playSounds();
		view.switchToView("3d_game_view");
		checkAlive.start();
	}
	
	private void addViews() {
		gameView = new GameView();
		view.add("3d_game_view", gameView);
		view.add("2d_bg", new BGView());
	}
	
	private void playSounds() {
		sound.load(bgm);
		sound.play(bgm, true);
	}
	
	public boolean showScored = false;
	public AnimationTimer checkAlive = new AnimationTimer() {
		public void handle(long arg0) {
			if (gameView.gameStatus != GameStatus.DEAD) return;
			
			if (!showScored) {
				view.add("show_score", new ScoreResult(gameView.score));
				view.load("show_score");
				view.attach("show_score", root);
				view.switchToView("show_score");
				
				System.out.println("switched!");
				
				showScored = true;
			}
		}
	};
	
	public boolean checkReturnMenu() {
		if (gameView.gameStatus != GameStatus.DEAD) return false;
		if (!showScored) return false;
		return true;
	}
	
	public void closeScene() {
		gameView.frameGenerator.stop();
		view.unload("3d_game_view");
		view.unload("2d_bg");
		view.unload("show_score");
		sound.stop(bgm);
	}
}
