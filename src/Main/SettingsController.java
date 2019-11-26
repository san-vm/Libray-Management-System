package Main;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import Database.DBhandle;

public class SettingsController {
	@FXML
	private Text Uname;
	@FXML
	private PasswordField oldPassword;
	@FXML
	private Text passIncorrect;
	@FXML
	private Text passNull;
	@FXML
	private PasswordField newPassword;
	@FXML
	private PasswordField confirmPassword;
	@FXML
	private Text passwdSuccessText;
	@FXML
	private TextField dueno;
	@FXML
	private Button set;

	@FXML
	void initialize() throws SQLException {
		Uname.setText(DBhandle.currentUser);
		if (DBhandle.currentUser.equals("admin")) {
			dueno.setDisable(false);
			set.setDisable(false);
		} else {
			dueno.setDisable(true);
			set.setDisable(true);
		}
		ResultSet rs = DBhandle.exqS("SELECT * FROM CONFIG");
		rs.next();
		dueno.setText("" + rs.getInt(1));
	}

	@FXML
	public void updatePasswd(ActionEvent event) throws SQLException {
		passNull.setVisible(false);
		passIncorrect.setVisible(false);
		passwdSuccessText.setVisible(false);
		if (oldPassword.getText().isEmpty()) {
			passNull.setVisible(true);
			return;
		}
		if (DBhandle.validate(DBhandle.currentUser, oldPassword.getText()).next())
			if (!confirmPassword.getText().isEmpty() && !newPassword.getText().isEmpty() && confirmPassword.getText().equals(newPassword.getText())) {
				PreparedStatement ps = DBhandle.exqP("UPDATE LOGINS SET PASSWORD = ? WHERE USERNAME = ?");
				ps.setString(1, newPassword.getText());
				ps.setString(2, DBhandle.currentUser);
				ps.execute();
			} else {
				JOptionPane.showMessageDialog(null, "New Password Invalid / Incorrect", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
		else {
			passNull.setVisible(false);
			passIncorrect.setVisible(true);
			return;
		}
		passwdSuccessText.setVisible(true);
		oldPassword.clear();
		newPassword.clear();
		confirmPassword.clear();
	}

	@FXML
	public void updateDueno(ActionEvent event) throws SQLException {
		try {
			Integer.parseInt(dueno.getText());
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Enter Valid Number!");
			return;
		}
		DBhandle.stmt.execute("UPDATE CONFIG SET DUEDAYS = " + dueno.getText());
	}
}