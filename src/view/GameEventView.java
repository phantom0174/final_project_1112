package view;

/*

遊戲中的 2D 介面，常駐存在。

GameScene 會依照 GameView 中的狀態進行事件呼叫。

位於 View 的最頂層。

*/


import java.io.IOException;
import java.util.ArrayList;

import base.View;
import finalProject.Snake;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
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
		control.mapPane.getChildren().clear();
		control.miniMap.unload();
		control.miniMap = null;
		control.mapPane = null;
		
		control = null;
		root = null;
		s = null;
		
		isloaded = false;
	}

	@Override
	public SubScene getSubScene() {
		return s;
	}
	
	public void showTime(int time) {
		control.showTime(time);
	}
	
	public void outOfBoundaryOn(OutOfBoundaryType type) {
		String warningMsg = "COME BACK!";
		if (type == OutOfBoundaryType.TOOFAR)
			warningMsg = "Don't go too FAR!";
		else if (type == OutOfBoundaryType.TOOHIGH)
			warningMsg = "Don't go too HIGH!";
		else if (type == OutOfBoundaryType.TOOLOW)
			warningMsg = "Don't go too LOW!";
		
		control.showEvent(warningMsg);
	}
	
	public void outOfBoundaryOff() {
		control.closeEvent();
	}
	
	public void setupMiniMap(Snake s, ArrayList<Group> appleList) {
		control.setupMap(s.head, appleList);
	}
	
	public void updateAppleMap() {
		control.showAppleCount();
		control.miniMap.refreshAppleMap();
	}
}
