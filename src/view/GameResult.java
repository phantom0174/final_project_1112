package view;

/*

遊戲結束介面，遊戲結束時才會啟用並浮到 View 的最上層。功能包括：

	1. 顯示玩家死亡標語 / 通關標語
	2. 顯示玩家此次遊玩分數
	3. 顯示回到 Menu 的提示詞

*/


import java.io.IOException;

import base.GameStatus;
import base.SoundPlayer;
import base.View;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.SubScene;

public class GameResult implements View {
	private boolean isloaded = false;
	
	private SubScene s;
	private Parent root = null;
	
	private int finalScore;
	private GameStatus gameStatus;
	private DeadType deadType;
	
	private SoundPlayer sound = new SoundPlayer();
	
	public GameResult(int score, GameStatus gameStatus, DeadType deadType) {
		this.gameStatus = gameStatus;
		this.finalScore = score;
		this.deadType = deadType;
	}

	@Override
	public boolean isLoaded() {
		return isloaded;
	}

	@Override
	public void load() {
		sound.load("sfx/game_win");
		
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("gameover.fxml"));
		
		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		s = new SubScene(root, 1080, 600);
		GameoverController control = loader.getController();
		control.setScore(finalScore);
		
		String message = "...";
		if (gameStatus == GameStatus.WIN) {
			message = WinMessage.choose();
			control.showCoverPane();
			sound.play("sfx/game_win");
		} else {
			message = DeadMessage.choose(deadType);
		}
		control.showMessage(message);
		
		isloaded = true;
	}

	@Override
	public void unload() {
		sound.unLoadAll();
		sound = null;
		
		root = null;
		s = null;
		
		this.isloaded = false;
	}

	@Override
	public SubScene getSubScene() {
		return s;
	}
}

/*

依照死亡方式隨機挑選要顯示在遊戲結果畫面最上方的死亡訊息
> 參考自 Minecraft 機制

*/

class DeadMessage {
	private static String[] collisionTitles = {
		"You eat the planet instead of the apples.",
		"You exploded.",
		"You were floating with your eyes closed.",
	};
	
	private static String[] outofBoarderTitles = {
		"You were killed by cosmic radiation.",
		"You overheated.",
		"The air was too thin for you to breathe.",
		"You become a piece of space junk."
	};
	
	public static String choose(DeadType dt) {
		if (dt == DeadType.COLLISION) {
			int randInd = (int) (Math.random() * collisionTitles.length);
			return collisionTitles[randInd];
		} else if (dt == DeadType.OUTOFBOUNDARY) {
			int randInd = (int) (Math.random() * outofBoarderTitles.length);
			return outofBoarderTitles[randInd];
		}
		return "You were dead.";
	}
}

class WinMessage {
	private static String[] winTitles = {
		"Congratulations!",
		"You are eligible to marry professor SHI!"
	};
	
	public static String choose() {
		int randInd = (int) (Math.random() * winTitles.length);
		return winTitles[randInd];
	}
}