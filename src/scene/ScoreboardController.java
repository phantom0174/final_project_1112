package scene;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import base.ScoreBoard;
import base.ScoreDatum;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Pair;


public class ScoreboardController implements Initializable {
	@FXML
	public TableView<ScoreDatum> scoreBoard;
	@FXML
	public TableColumn<ScoreDatum, String> name;
	@FXML
	public TableColumn<ScoreDatum, String> score;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		name.setCellValueFactory(
			new PropertyValueFactory<>("name")
		);
		score.setCellValueFactory(
			new PropertyValueFactory<>("score")
		);
		
		ObservableList<ScoreDatum> list = FXCollections.observableArrayList();
		scoreBoard.setItems(list);
		
		ScoreBoard scoreHandler = new ScoreBoard();
		
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
}
