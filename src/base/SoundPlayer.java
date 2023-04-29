package base;

import java.io.File;
import java.util.HashMap;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundPlayer {
	public HashMap<String, Media> soundList = new HashMap<>();
	public HashMap<String, MediaPlayer> playerList = new HashMap<>();
	
	public String concatPath(String s) {
		return "src/resources/" + s;
	}
	
	public void load(String soundPath) {
		/*
		 * path format: "category/name.mp3"
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
		player.setVolume(Config.volume);
		this.playerList.put(soundPath, player);
		if (loop) player.setOnEndOfMedia(() -> {
			player.play();
		});
		player.play();
	}
	
	public void stop(String soundPath) {
		MediaPlayer player = playerList.get(soundPath);
		player.stop();
	}
}
