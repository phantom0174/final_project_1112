package scene;

/*

排行榜選單的操控器，功能包含：

	- 顯示玩家最高分
	- 讓玩家上傳分數
	- 顯示所有已上傳的分數
	
分數 API 使用 base/ScoreAPI 進行連接。

*/


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import base.Config;
import base.ScoreAPI;
import base.ScoreDatum;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Pair;


public class ScoreboardController implements Initializable {
	@FXML
	public TableView<ScoreDatum> scoreBoard;
	public TableColumn<ScoreDatum, String> name, score;
	public TextField nameField;
	public Button uploadButton;
	public Label scoreLabel;
	
	
	public static String scoreLabelDefault = "Your Highest Score:\n";
	public ScoreAPI scoreHandler = new ScoreAPI();
	public String highestScore;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		name.setCellValueFactory(
			new PropertyValueFactory<>("name")
		);
		score.setCellValueFactory(
			new PropertyValueFactory<>("score")
		);
		
		refreshScoreBoard();
	}
	
	public void showHighestScore(int score) {
		highestScore = String.valueOf(score);
		
		scoreLabel.setText(scoreLabelDefault + highestScore);
	}
	
	public void refreshScoreBoard() {
		ObservableList<ScoreDatum> list = FXCollections.observableArrayList();
		scoreBoard.setItems(list);
		
		try {
			ArrayList<Pair<String, String>> datums = scoreHandler.getScores();
			
			if (datums == null) {
				list.add(new ScoreDatum("error", "try again"));
			} else {
				for (Pair<String, String> p: datums) {
					list.add(new ScoreDatum(p.getKey(), p.getValue()));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void uploadScore(ActionEvent e) throws IOException {
		if (!Config.scoreUploadCoda) {
			showAlert(
				"Uplaod Score Error | Final Project",
				"You can only upload once per highscore!"
			);
			return;
		}
		
		String name = nameField.getText().strip();
		if (name.equals("")) {
			showAlert(
				"Uplaod Score Error | Final Project",
				"Please enter your name to upload a score!"
			);
			return;
		}
		
		if (!name.matches("^[A-Za-z][A-Za-z0-9]*$") || !NameChecker.check(name)) {
			showAlert(
				"Uplaod Score Error | Final Project",
				"Invalid name!"
			);
			return;
		}
		
		if (highestScore.equals("0")) {
			showAlert(
				"Uplaod Score Error | Final Project",
				"Try harder!"
			);
			return;
		}
		
		boolean uploadStatus = scoreHandler.uploadScore(name, highestScore);
		if (!uploadStatus) {
			showAlert(
				"Uplaod Score Error | Final Project",
				"Failed to upload score..."
			);
			return;
		}
		
		refreshScoreBoard();
		Config.scoreUploadCoda = false;
		
		showAlert(
			"Uplaod Score Success | Final Project",
			"Your score has been uploaded!"
		);
	}
	
	public void showAlert(String title, String headerText) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.showAndWait();
	}
}


class NameChecker {
	public static String[] badPharses = {
		"2g1c", "acrotomophilia", "anal", "anilingus", "anus", "apeshit", "arsehole", "ass", "asshole", "assmunch", "autoerotic", "babeland", "bangbros", "bangbus", "bareback", "barenaked", "bastard", "bastardo", "bastinado", "bbw", "bdsm", "beaner", "beaners", "beastiality", "bestiality", "bimbos", "birdlock", "bitch", "bitches", "blowjob", "blumpkin", "bollocks", "bondage", "boner", "boob", "boobs", "bukkake", "bulldyke", "bullshit", "bunghole", "busty", "butt", "buttcheeks", "butthole", "camgirl", "camslut", "camwhore", "carpetmuncher", "cialis", "circlejerk", "clit", "clitoris", "clusterfuck", "cock", "cocks", "coprolagnia", "coprophilia", "cornhole", "coon", "coons", "creampie", "cum", "cumming", "cumshot", "cumshots", "cunnilingus", "cunt", "darkie", "daterape", "deepthroat", "dendrophilia", "dick", "dildo", "dingleberry", "dingleberries", "doggiestyle", "doggystyle", "dolcett", "domination", "dominatrix", "dommes", "dvda", "ecchi", 
		"ejaculation", "erotic", "erotism", "escort", "eunuch", "fag", "faggot", "fecal", "felch", "fellatio", "feltch", "femdom", "figging", "fingerbang", "fingering", "fisting", "footjob", "frotting", "fuck", "fuckin", "fucking", "fucktards", "fudgepacker", "futanari", "gangbang", "genitals", "goatcx", "goatse", "gokkun", "goodpoop", "goregasm", "grope", "g-spot", "guro", "handjob", "hardcore", "hentai", "homoerotic", "honkey", "hooker", "horny", "humping", "incest", "intercourse", "jailbait", "jigaboo", "jiggaboo", "jiggerboo", "jizz", "juggs", "kike", "kinbaku", "kinkster", "kinky", "knobbing", "livesex", "lolita", "lovemaking", "masturbate", "masturbating", "masturbation", "milf", "mong", "motherfucker", "muffdiving", "nambla", "nawashi", "negro", "neonazi", "nigga", "nigger", "nimphomania", "nipple", 
		"nipples", "nsfw", "nude", "nudity", "nutten", "nympho", "nymphomania", "octopussy", "omorashi", "orgasm", "orgy", "paedophile", "paki", "panties", "panty", "pedobear", "pedophile", "pegging", "penis", "pikey", "pissing", "pisspig", "playboy", "ponyplay", "poof", "poon", "poontang", "punany", "poopchute", "porn", "porno", "pornography", "pthc", "pubes", "pussy", "queaf", "queef", "quim", "raghead", "rape", "raping", "rapist", "rectum", "rimjob", "rimming", "sadism", "santorum", "scat", "schlong", "scissoring", "semen", "sex", "sexcam", "sexo", "sexy", "sexual", "sexually", "sexuality", "shemale", "shibari", "shit", "shitblimp", "shitty", "shota", "shrimping", "skeet", "slanteye", "slut", "s&m", "smut", "snatch", "snowballing", "sodomize", "sodomy", "spastic", "spic", "splooge", "spooge", "spunk", "strapon", "strappado", "suck", "sucks", "swastika", "swinger", "threesome", "throating", "thumbzilla", "tit", "tits", "titties", "titty", "topless", "tosser", "towelhead", "tranny", "tribadism", "tubgirl", "tushy", "twat", "twink", "twinkie", "undressing", "upskirt", "urophilia", "vagina", "viagra", "vibrator", "vorarephilia", "voyeur", "voyeurweb", "voyuer", "vulva", "wank", "wetback", "whore", "worldsex", "xx", "xxx", "yaoi", "yiffy", "zoophilia"
	};
	
	public static boolean check(String s) {
		s = s.toLowerCase();
		for (String p: badPharses) {
			if (s.contains(p.toLowerCase())) return false;
		}
		return true;
	}
}
