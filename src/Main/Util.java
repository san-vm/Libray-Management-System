package Main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Util {
	@FXML
	public static void aboutus(ActionEvent event) {
		Alert alert = new Alert(AlertType.INFORMATION);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("/res/about.png"));
		alert.setHeaderText(null);
		alert.setTitle("About Us");
		alert.setContentText("Developers:\nSanjay VM\nTejas G\nRNSIT Bangalore");
		alert.showAndWait();
	}

	@FXML
	public static void aboutApp(ActionEvent event) {
		String git = "github.com/san-vm/Libray-Management-System";
		Alert alert = new Alert(AlertType.INFORMATION);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("/res/logo.png"));
		alert.setHeaderText(null);
		alert.setTitle("About App");
		alert.setContentText("\t\tLIBRARY MANAGEMENT SYSTEM\n\nMini Project V\nGIT:  " + git + "\nDevelopers:\nSanjay VM\nTejas G");
		alert.showAndWait();
	}
}