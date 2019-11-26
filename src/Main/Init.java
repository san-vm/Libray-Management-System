package Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import Database.DBhandle;

public class Init extends Application {
	public static void main(String[] args) {
		System.out.println("LIBRARY MANAGEMENT SYSTEM\t-- SanVM & TejG\nLoading..");
		launch(args);
	}

	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/Login/Login.fxml"));
		stage.setMaximized(true);
		stage.setTitle("Library Management System");
		stage.getIcons().add(new Image("/res/logo.png"));
		DBhandle.setLayout(root, stage);
	}
}