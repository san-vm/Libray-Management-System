package Main;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;
import javafx.event.ActionEvent;
import Database.DBhandle;

public class AddMemController {
	@FXML
	private Text loginid;
	@FXML
	private TextField name;
	@FXML
	private Text nameerr;
	@FXML
	private TextField email;
	@FXML
	private TextField phno;
	@FXML
	private Button submit;
	int logno;

	@FXML
	void initialize() throws SQLException {
		ResultSet rs = DBhandle.exqS("SELECT COUNT(*) FROM MEMBER");
		rs.next();
		logno = rs.getInt(1) + 1;
		if (logno > 99)
			loginid.setText("" + logno);
		else if (logno > 9)
			loginid.setText("0" + logno);
		else
			loginid.setText("00" + logno);
		name.clear();
		email.clear();
		phno.clear();
	}

	@FXML
	public void submit(ActionEvent event) throws SQLException {
		nameerr.setVisible(false);
		if (name.getText().isEmpty()) {
			nameerr.setVisible(true);
			return;
		}
		PreparedStatement ps = DBhandle.exqP("INSERT INTO MEMBER VALUES(?, ?, ?, ?)");
		ps.setInt(1, logno);
		ps.setString(2, name.getText());
		ps.setString(3, email.getText());
		try {
			ps.setInt(4, Integer.parseInt(phno.getText()));
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Enter Valid Phone Number!", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		ps.execute();
		JOptionPane.showMessageDialog(null, "Member " + name.getText() + " Added Successfully!");
		initialize();
	}
}