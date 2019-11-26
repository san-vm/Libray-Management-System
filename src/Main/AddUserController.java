package Main;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.Random;
import javax.swing.JOptionPane;
import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;
import Database.DBhandle;

public class AddUserController {
	@FXML
	private TextField loginid;
	@FXML
	private TextField name;
	@FXML
	private TextField email;
	@FXML
	private TextField phno;
	@FXML
	private TextArea otp;
	@FXML
	private Text otpname;
	@FXML
	private Button genotp;
	@FXML
	private Text login_exists;
	@FXML
	private Button submit;
	@FXML
	private Text otperr;
	@FXML
	private Text nameerr;
	@FXML
	private Text login_nullerr;
	@FXML
	private Text pherr;

	@FXML
	public void genotp(ActionEvent event) {
		Random rn = new Random();
		int n = rn.nextInt(9999);
		while (n < 1001)
			n = rn.nextInt(9999);
		otp.setText("" + n);
	}

	@FXML
	public void submit(ActionEvent event) throws SQLException {
		login_exists.setVisible(false);
		login_nullerr.setVisible(false);
		nameerr.setVisible(false);
		pherr.setVisible(false);
		otperr.setVisible(false);

		if (check_fields())
			return;
		String loginq = "INSERT INTO LOGINS VALUES('" + loginid.getText() + "','" + otp.getText() + "')";
		DBhandle.stmt.execute(loginq);
		loginq = "INSERT INTO REG_USER VALUES(?, ?, ?, ?)";
		PreparedStatement ps = DBhandle.exqP(loginq);
		ps.setString(1, loginid.getText());
		ps.setString(2, name.getText());
		ps.setString(3, email.getText());
		ps.setLong(4, Long.parseLong(phno.getText()));
		ps.execute();
		JOptionPane.showMessageDialog(null, "User " + name.getText() + " Added Successfully!");
		loginid.clear();
		name.clear();
		email.clear();
		phno.clear();
		otp.clear();
	}

	public boolean check_fields() throws SQLException {
		String loginq = "SELECT * FROM LOGINS WHERE USERNAME = '" + loginid.getText().trim() + "'";
		boolean err = false;
		if (loginid.getText().trim().isEmpty()) {
			login_nullerr.setVisible(true);
			err = true;
		} else if (DBhandle.exqS(loginq).next()) {
			login_exists.setVisible(true);
			err = true;
		}
		if (name.getText().trim().isEmpty()) {
			nameerr.setVisible(true);
			err = true;
		}
		if (otp.getText().trim().isEmpty()) {
			otperr.setVisible(true);
			err = true;
		}
		try {
			Long.parseLong(phno.getText());
		} catch (NumberFormatException e) {
			pherr.setVisible(true);
			return true;
		}
		return err;
	}
}