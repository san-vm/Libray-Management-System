package Main.Books;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;
import Database.DBhandle;

public class AddAuthPubController {
	@FXML
	private TextField Aname;
	@FXML
	private Text Anameerr;
	@FXML
	private TextField Aemail;
	@FXML
	private TextField Aphno;
	@FXML
	private TextField Pname;
	@FXML
	private Text Pnameerr;
	@FXML
	private TextField Pemail;
	@FXML
	private TextField Pphno;

	@FXML
	public void AddAuthor(ActionEvent event) throws SQLException {
		if (Aname.getText().isEmpty()) {
			Anameerr.setVisible(true);
			return;
		}
		Anameerr.setVisible(false);
		if (DBhandle.exqS("SELECT * FROM AUTH WHERE NAME = '" + Aname.getText().toUpperCase() + "'").next()) {
			JOptionPane.showMessageDialog(null, "Author Name Already Exists!", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		PreparedStatement ps = DBhandle.exqP("INSERT INTO AUTH VALUES(?,?,?)");
		ps.setString(1, Aname.getText().toUpperCase());
		ps.setString(2, Aemail.getText().toUpperCase());
		try {
			ps.setLong(3, Long.parseLong(Aphno.getText()));
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Enter Valid Phone Number!", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		Anameerr.setVisible(false);
		ps.execute();
		JOptionPane.showMessageDialog(null, "Author " + Aname.getText() + " Added Successfully!");
		Aname.clear();
		Aemail.clear();
		Aphno.clear();
	}

	@FXML
	public void AddPub(ActionEvent event) throws SQLException {
		if (Pname.getText().isEmpty()) {
			Pnameerr.setVisible(true);
			return;
		}
		Pnameerr.setVisible(false);
		if (DBhandle.exqS("SELECT * FROM PUBLISHER WHERE NAME = '" + Aname.getText().toUpperCase() + "'").next()) {
			JOptionPane.showMessageDialog(null, "Publisher Name Already Exists!", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		PreparedStatement ps = DBhandle.exqP("INSERT INTO PUBLISHER VALUES(?,?,?)");
		ps.setString(1, Pname.getText().toUpperCase());
		ps.setString(2, Pemail.getText().toUpperCase());
		try {
			ps.setLong(3, Long.parseLong(Pphno.getText()));
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Enter Valid Phone Number!", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		Pnameerr.setVisible(false);
		ps.execute();
		JOptionPane.showMessageDialog(null, "Publisher " + Pname.getText() + " Added Successfully!");
		Pname.clear();
		Pemail.clear();
		Pphno.clear();
	}
}