package Main.Books;

import java.awt.HeadlessException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;
import javax.swing.JOptionPane;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import Database.DBhandle;

public class Book {
	int sn;
	long ISBN;
	String title;
	String author;
	String publisher;
	int quantity;
	Button issue;
	Button delete;

	public Book(ResultSet rs, int sn) throws SQLException {
		this.sn = sn;
		ISBN = rs.getLong(1);
		title = rs.getString(2);
		author = rs.getString(3);
		publisher = rs.getString(4);
		quantity = rs.getInt(5);
	}

	public Button getIssue() {
		issue = new Button("ISSUE");
		issue.setStyle("-fx-background-color: #6FF3B5");
		issue.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				TextInputDialog dialog = new TextInputDialog();
				Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("/res/logo.png"));
				dialog.setTitle("Issue Book");
				dialog.setHeaderText("Book: " + title + "\nISBN: " + ISBN);
				dialog.setContentText("Enter Card No:");

				Optional<String> result = dialog.showAndWait();
				String ID;
				try {
					ID = result.get();
					LocalDate startDate = LocalDate.now();
					PreparedStatement ps = DBhandle.exqP("INSERT INTO BORROWED VALUES(?, ?, ?, ?, false)");
					ps.setLong(1, ISBN);
					ps.setInt(2, Integer.parseInt(ID));
					ps.setDate(3, java.sql.Date.valueOf(startDate));
					ResultSet rs = DBhandle.exqS("SELECT * FROM CONFIG");
					rs.next();
					ps.setDate(4, java.sql.Date.valueOf(startDate.plusDays(rs.getInt(1))));
					ps.execute();
					DBhandle.stmt.execute("UPDATE BOOK SET QUANTITY = " + (quantity - 1) + " WHERE ISBN = " + ISBN);
				} catch (NoSuchElementException | HeadlessException e) {
					return;
				} catch (NumberFormatException | SQLException e) {
					JOptionPane.showMessageDialog(null, "Invalid Card Number!", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				JOptionPane.showMessageDialog(null, "Book: " + title + "\nIssued To:\nCard No: " + ID, "Book ISSUED",
						JOptionPane.PLAIN_MESSAGE);
			}
		});
		return issue;
	}

	public Button getDelete() {
		delete = new Button("DELETE");
		delete.setStyle("-fx-background-color: #FA8072");
		delete.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("/res/logo.png"));
				alert.setHeaderText(null);
				alert.setTitle("Delete Book");
				alert.setContentText("Are you sure to Delete?\nBook: " + title + "\nISBN: " + ISBN);
				Optional<ButtonType> result = alert.showAndWait();
				try {
					if (result.get() == ButtonType.OK) {
						DBhandle.stmt.execute("DELETE FROM BOOK WHERE ISBN = " + ISBN);
						delete.setDisable(true);
						issue.setDisable(true);
					}
				} catch (SQLException e) {
				}
			}
		});
		return delete;
	}

	public int getSn() {
		return sn;
	}

	public String getTitle() {
		return title;
	}

	public long getISBN() {
		return ISBN;
	}

	public String getAuthor() {
		return author;
	}

	public String getPublisher() {
		return publisher;
	}

	public int getQuantity() {
		return quantity;
	}
}