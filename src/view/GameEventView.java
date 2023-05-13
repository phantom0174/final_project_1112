package view;

/*

遊戲中的 2D 介面，常駐存在。

GameScene 會依照 GameView 中的狀態進行事件呼叫。

位於 View 的最頂層。

*/


import java.io.IOException;

import base.Entity;
import base.View;
import finalProject.Snake;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point3D;
import javafx.scene.Parent;
import javafx.scene.SubScene;


public class GameEventView implements View {
	private boolean isloaded = false;
	
	private SubScene s;
	private Parent root = null;
	private GameEventController control = null;
	
	@Override
	public boolean isLoaded() {
		return isloaded;
	}

	@Override
	public void load() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("gameevent.fxml"));
		
		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		s = new SubScene(root, 1080, 600);
		control = loader.getController();
		
		isloaded = true;
	}

	@Override
	public void unload() {
		control = null;
		root = null;
		s = null;
		
		isloaded = false;
	}

	@Override
	public SubScene getSubScene() {
		return s;
	}
	
	public void showScore(int score) {
		control.showScore(score);
	}
	
	public void doubleScoreEffect(boolean on) {
		control.doubleScore(on);
	}
	
	public void outOfBoundaryOn() {
		control.showEvent("Don't get too FAR!");
	}
	
	public void outOfBoundaryOff() {
		control.closeEvent();
	}
	
	public void setupMiniMap(Snake s) {
		control.setupMap(s.head);
	}
}
