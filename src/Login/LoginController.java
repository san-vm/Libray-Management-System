package Login;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import javafx.scene.control.PasswordField;
import java.io.IOException;
import java.sql.SQLException;
import Database.DBhandle;
import Main.Util;

public class LoginController {
	@FXML
	private TextField username;
	@FXML
	private PasswordField password;
	@FXML
	private Text errormsg;
	@FXML
	private ImageView loadImg;
	@FXML
	private Text loadText;

	@FXML
	void initialize() {
		new Thread(() -> {
			DBhandle.getHandle();
			loadImg.setVisible(false);
			loadText.setVisible(false);
		}).start();
	}

	@FXML
	public void initLogin(ActionEvent event) throws IOException, SQLException {
		String user = username.getText(), pass = password.getText();
		if (DBhandle.validate(user, pass).next()) {
			System.out.println("Login Successful @" + user);
			Parent root = FXMLLoader.load(getClass().getResource("/Main/Main.fxml"));
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setMaximized(true);
			DBhandle.setLayout(root, stage);
			return;
		}
		errormsg.setVisible(true);
	}

	@FXML
	void aboutus(ActionEvent event) {
		Util.aboutus(event);
	}

	@FXML
	void aboutApp(ActionEvent event) {
		Util.aboutApp(event);
	}

	@FXML
	void quit(ActionEvent event) {
		System.out.println("Exit");
		System.exit(0);
	}
}