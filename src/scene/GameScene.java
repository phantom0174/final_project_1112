package scene;


import base.Config;
import base.GameStatus;
import base.SoundPlayer;
import base.ViewHandler;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import view.BGView;
import view.GameEventView;
import view.GameView;
import view.GameResult;

/*

Three steps to activate a view:
1. load scene entities
2. attach scene to root
3. switch to scene

*/

public class GameScene {
	public Scene s;
	public Group root = new Group();
	private ViewHandler view = new ViewHandler();
	
	private GameView gameView;
	private GameEventView gameEventView;
	
	// ---- sounds -----
	private SoundPlayer sound = new SoundPlayer();
	private String bgm = "bgm/my-lonely-journey";
	
	public GameScene() {
		s = new Scene(root, Config.width, Config.height);
		
		addViews();
		playSounds();
		
		view.switchToView("3d_game_view");
		checkAlive.start();
		handleEventPipeline.start();
	}
	
	private void addViews() {
		gameView = new GameView();
		gameEventView = new GameEventView();
		view.add("3d_game_view", gameView);
		view.add("2d_bg", new BGView());
		view.add("event_board", gameEventView);
		
		view.load("2d_bg");
		view.attach("2d_bg", root);
		
		view.load("3d_game_view");
		view.attach("3d_game_view", root);
		
		view.load("event_board");
		view.attach("event_board", root);
	}
	
	private void playSounds() {
		sound.load(bgm);
		sound.play(bgm, true);
	}
	
	public boolean showScored = false;
	public AnimationTimer checkAlive = new AnimationTimer() {
		public void handle(long now) {
			if (gameView.gameStatus != GameStatus.DEAD) return;
			
			if (showScored) return;
			
			GameResult sR = new GameResult(
				(int) gameView.score,
				gameView.deadReason
			);
			
			view.detach("event_board", root);
			
			view.add("show_score", sR);
			view.load("show_score");
			view.attach("show_score", root);
			view.switchToView("show_score");
			
			showScored = true;
		}
	};
	
	public AnimationTimer handleEventPipeline = new AnimationTimer() {
		@Override
		public void handle(long now) {
			if (gameView.gameStatus == GameStatus.DEAD) return;
			if (gameView.eventPipeline.size() == 0) return;
			
			for (String event: gameView.eventPipeline) {
				switch(event) {
				case "updateScore":
					gameEventView.showScore((int) gameView.score); break;
				case "doubleScoreEffectOn":
					gameEventView.doubleScoreEffect(true); break;
				case "doubleScoreEffectOff":
					gameEventView.doubleScoreEffect(false); break;
				case "outOfBoundaryOn":
					gameEventView.outOfBoundaryOn(); break;
				case "outOfBoundaryOff":
					gameEventView.outOfBoundaryOff(); break;
				}
			}
			
			gameView.eventPipeline.clear();
		}
	};

	public boolean checkCanReturnMenu() {
		if (gameView.gameStatus != GameStatus.DEAD) return false;
		if (!showScored) return false;
		return true;
	}
	
	public void closeScene() {
		gameView.stopProcess();
		view.unload("3d_game_view");
		view.unload("2d_bg");
		view.unload("show_score");
		sound.stop(bgm);
	}
	
	public int getPlayerScore() {
		return (int) gameView.score;
	}
}
