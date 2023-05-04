package scene;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import base.ScoreBoard;
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
	@FXML
	public TableColumn<ScoreDatum, String> name;
	@FXML
	public TableColumn<ScoreDatum, String> score;
	@FXML
	public TextField nameField;
	@FXML
	public Button uploadButton;
	@FXML
	public Label scoreLabel; 
	
	public static String scoreLabelDefault = "You Highest Score:\n";
	public ScoreBoard scoreHandler = new ScoreBoard();
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
		String name = nameField.getText();
		if (name == null) {
			showAlert(
				"Uplaod Score Error | Final Project",
				"Please enter your name to upload a score!"
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
		
		showAlert(
			"Uplaod Score Success | Final Project",
			"Your score has been uploaded!"
		);
		refreshScoreBoard();
	}
	
	public void showAlert(String title, String headerText) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.showAndWait();
	}
}
