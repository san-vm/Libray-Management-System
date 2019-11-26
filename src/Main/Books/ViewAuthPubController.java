package Main.Books;

import java.sql.ResultSet;
import java.sql.SQLException;

import Database.DBhandle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.scene.control.ListView;

public class ViewAuthPubController {
	@FXML
	private ListView<String> authView;
	@FXML
	private ListView<String> pubView;

	@FXML
	void initialize() throws SQLException {
		ObservableList<String> author = FXCollections.observableArrayList("NAME\t|\tEMAIL\t|\tPHONE");
		ObservableList<String> publisher = FXCollections.observableArrayList("NAME\t|\tEMAIL\t|\tPHONE");
		authView.setItems(author);
		pubView.setItems(publisher);
		ResultSet rs = DBhandle.exqS("SELECT * FROM AUTH");
		String temp;
		while (rs.next()) {
			temp = rs.getString(1);
			temp = temp + "\t  " + rs.getString(2);
			temp = temp + "\t  " + rs.getLong(3);
			author.add(temp);
		}

		rs = DBhandle.exqS("SELECT * FROM PUBLISHER");
		while (rs.next()) {
			temp = rs.getString(1);
			temp = temp + "\t  " + rs.getString(2);
			temp = temp + "\t  " + rs.getLong(3);
			publisher.add(temp);
		}
	}
}