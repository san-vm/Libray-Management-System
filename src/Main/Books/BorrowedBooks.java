package Main.Books;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import Database.DBhandle;

public class BorrowedBooks {
	int cid;
	String name;
	long ph, ISBN;
	String title;
	String borrowedDate;
	String dueDate;
	Button returned;
	boolean stat;

	public BorrowedBooks(ResultSet rs, boolean returnStat) throws SQLException {
		ISBN = rs.getLong(1);
		cid = rs.getInt(2);
		borrowedDate = rs.getDate(3).toString();
		dueDate = rs.getDate(4).toString();
		ResultSet memRs = DBhandle.exqS("SELECT NAME, PH FROM MEMBER WHERE CID = " + cid);
		memRs.next();
		name = memRs.getString(1);
		ph = memRs.getLong(2);
		ResultSet bkRs = DBhandle.exqS("SELECT TITLE FROM BOOK WHERE ISBN = " + ISBN);
		bkRs.next();
		title = bkRs.getString(1);
		stat = returnStat;
	}

	public Button getReturned() {
		returned = new Button("RETURN");
		returned.setStyle("-fx-background-color: #FA8072");
		if (stat) {
			returned.setDisable(true);
			return returned;
		}
		returned.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("/res/logo.png"));
				alert.setHeaderText(null);
				alert.setTitle("Return Book");
				alert.setContentText("Return Book?\nBook: " + title + "\nISBN: " + ISBN);
				Optional<ButtonType> result = alert.showAndWait();
				try {
					if (result.get() == ButtonType.OK) {
						DBhandle.stmt.execute("UPDATE BORROWED SET RETURNED = TRUE WHERE BID = " + ISBN + " AND CID = " + cid);
						returned.setDisable(true);
					}
				} catch (SQLException e) {
				}
			}
		});
		return returned;
	}

	public int getCid() {
		return cid;
	}

	public String getName() {
		return name;
	}

	public long getPh() {
		return ph;
	}

	public long getISBN() {
		return ISBN;
	}

	public String getTitle() {
		return title;
	}

	public String getBorrowedDate() {
		return borrowedDate;
	}

	public String getDueDate() {
		return dueDate;
	}
}