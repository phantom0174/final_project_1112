package view;

import java.io.IOException;

import base.View;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.SubScene;

public class GameResult implements View {
	private boolean isloaded = false;
	
	private SubScene s;
	private Parent root = null;
	
	private int finalScore;
	private DeadType deadType;
	
	public GameResult(int score, DeadType deadType) {
		this.finalScore = score;
		this.deadType = deadType;
	}

	@Override
	public boolean isLoaded() {
		return isloaded;
	}

	@Override
	public void load() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("gameover.fxml"));
		
		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		s = new SubScene(root, 1080, 600);
		GameoverController control = loader.getController();
		control.setScore(finalScore);
		control.showDeadReason(DeadReasonChooser.choose(deadType));
		
		isloaded = true;
	}

	@Override
	public void unload() {
		this.isloaded = false;
	}

	@Override
	public SubScene getSubScene() {
		return s;
	}
}


class DeadReasonChooser {
	private static String[] collisionReasons = {
		"You eat the planet instead of the apples.",
		"You exploded.",
		"You were floating with your eyes closed.",
	};
	
	private static String[] outofBoarderReasons = {
		"You were killed by cosmic radiation.",
		"You overheated.",
		"The air was too thin for you to breathe.",
		"You become a piece of space junk."
	};
	
	public static String choose(DeadType dt) {
		if (dt == DeadType.COLLISION) {
			int randInd = (int) (Math.random() * collisionReasons.length);
			return collisionReasons[randInd];
		} else {
			int randInd = (int) (Math.random() * outofBoarderReasons.length);
			return outofBoarderReasons[randInd];
		}
	}
}
