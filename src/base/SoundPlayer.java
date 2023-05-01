package base;

import java.io.File;
import java.util.HashMap;

import javafx.animation.Timeline;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundPlayer {
	public HashMap<String, Media> soundList = new HashMap<>();
	public HashMap<String, MediaPlayer> playerList = new HashMap<>();
	
	public String concatPath(String s) {
		return "src/resources/" + s + ".mp3";
	}
	
	public void load(String soundPath) {
		/*
		 * path format: "category/file_name" (without .mp3)
		 */
		
		File soundFile = new File(concatPath(soundPath));
		
		Media sound = new Media(soundFile.toURI().toString());
		soundList.put(soundPath, sound);
	}
	
	public void loadAll(String[] soundPaths) {
		for (String path: soundPaths) {
			File soundFile = new File(concatPath(path));
			
			Media sound = new Media(soundFile.toURI().toString());
			soundList.put(path, sound);
		}
	}
	
	public void unLoad(String soundPath) {
		soundList.remove(soundPath);
		playerList.remove(soundPath);
	}
	
	public void unLoadAll() {
		soundList.clear();
		playerList.clear();
	}
	
	public void play(String soundPath) {
		play(soundPath, false);
	}
	
	public void play(String soundPath, boolean loop) {
		MediaPlayer player = new MediaPlayer(soundList.get(soundPath));		
		player.volumeProperty().bind(Config.volume);
		if (loop) player.setCycleCount(Timeline.INDEFINITE);
		player.play();
		
		playerList.put(soundPath, player);
		System.out.println("saved: " + soundPath);
		System.out.println(playerList.size());
	}
	
	public void stop(String soundPath) {
		System.out.println("received: " + soundPath);
		MediaPlayer player = playerList.get(soundPath);
		System.out.println(playerList.size());
		player.stop();
	}
	
	@Override
	protected void finalize() {
	    System.out.println("sound-p del!!!!");
	}
}
