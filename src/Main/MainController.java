package Main;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.sql.Date;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.effect.InnerShadow;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import Database.DBhandle;
import Main.Books.Book;
import Main.Books.BorrowedBooks;

public class MainController {
	static int a = 0;
	@FXML
	private MenuBar topMenu;
	@FXML
	private MenuItem addusers;
	@FXML
	private TextField isbnTextField;
	@FXML
	private TextField titleTextField;
	@FXML
	private TextField authorTextField;
	@FXML
	private TextField publisherTextField;
	@FXML
	private TextField quantityTextField;
	@FXML
	private TextField searchTextField;
	@FXML
	private TextField borrowedSearch;
	@FXML
	private TableView<Book> libraryTable;
	@FXML
	private TableColumn<?, ?> snColumn;
	@FXML
	private TableColumn<?, ?> isbnColumn;
	@FXML
	private TableColumn<?, ?> titleColumn;
	@FXML
	private TableColumn<?, ?> authorColumn;
	@FXML
	private TableColumn<?, ?> publisherColumn;
	@FXML
	private TableColumn<?, ?> quantityColumn;
	@FXML
	private TableColumn<?, ?> issueColumn;
	@FXML
	private TableColumn<?, ?> deleteColumn;
	@FXML
	private TableView<BorrowedBooks> borrowerTable;
	@FXML
	private TableColumn<?, ?> bname;
	@FXML
	private TableColumn<?, ?> cid;
	@FXML
	private TableColumn<?, ?> isbn;
	@FXML
	private TableColumn<?, ?> bnumber;
	@FXML
	private TableColumn<?, ?> bbook;
	@FXML
	private TableColumn<?, ?> bdate;
	@FXML
	private TableColumn<?, ?> bdue;
	@FXML
	private TableColumn<?, ?> returned;
	@FXML
	private Tab user_tab;
	@FXML
	private ListView<String> duebooksListView;
	@FXML
	private ListView<String> membersListView;
	@FXML
	private ListView<String> UsersListView;

	ObservableList<String> dueDetails;
	ObservableList<String> memberDetails;
	ObservableList<String> userDetails;

	@FXML
	void initialize() throws SQLException {
		if (DBhandle.currentUser.equals("admin")) {
			user_tab.setDisable(false);
			addusers.setVisible(true);
		} else {
			user_tab.setDisable(true);
			addusers.setVisible(false);
		}
		new Thread(() -> {
			try {
				bookView();
				BorrowBookView();
				updateView(null);
			} catch (SQLException e) {
				return;
			}
		}).start();

		searchTextField.textProperty().addListener(t -> {
			try {
				bookView();
			} catch (SQLException e) {
				return;
			}
		});

		borrowedSearch.textProperty().addListener(t -> {
			try {
				BorrowBookView();
			} catch (SQLException e) {
				return;
			}
		});

		// Book View
		libraryTable.setEffect(new InnerShadow());
		snColumn.setCellValueFactory(new PropertyValueFactory<>("sn"));
		isbnColumn.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
		titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
		authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
		publisherColumn.setCellValueFactory(new PropertyValueFactory<>("publisher"));
		quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		issueColumn.setCellValueFactory(new PropertyValueFactory<>("issue"));
		deleteColumn.setCellValueFactory(new PropertyValueFactory<>("delete"));

		// Borrowed View
		borrowerTable.setEffect(new InnerShadow());
		cid.setCellValueFactory(new PropertyValueFactory<>("cid"));
		bname.setCellValueFactory(new PropertyValueFactory<>("name"));
		bnumber.setCellValueFactory(new PropertyValueFactory<>("ph"));
		isbn.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
		bbook.setCellValueFactory(new PropertyValueFactory<>("title"));
		bdate.setCellValueFactory(new PropertyValueFactory<>("borrowedDate"));
		bdue.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
		returned.setCellValueFactory(new PropertyValueFactory<>("returned"));
	}

	@FXML
	void addMem(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("AddMem.fxml"));
		Stage stage = new Stage();
		stage.setTitle("Add Member");
		stage.getIcons().add(new Image("/res/about.png"));
		DBhandle.setLayout(root, stage);
	}

	@FXML
	void setting(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("Settings.fxml"));
		Stage stage = new Stage();
		stage.setTitle("Settings");
		stage.getIcons().add(new Image("/res/logo.png"));
		DBhandle.setLayout(root, stage);
	}

	@FXML
	void addusers(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("AddUser.fxml"));
		Stage stage = new Stage();
		stage.setTitle("Add User");
		stage.getIcons().add(new Image("/res/logo.png"));
		DBhandle.setLayout(root, stage);
	}

	@FXML
	void addBook(ActionEvent event) throws SQLException {
		if (isbnTextField.getText().isEmpty() || titleTextField.getText().isEmpty()
				|| authorTextField.getText().isEmpty() || publisherTextField.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Enter All Fields!");
			return;
		}
		PreparedStatement ps = DBhandle.exqP("INSERT INTO BOOK VALUES(?,?,?,?,?)");
		ps.setString(2, titleTextField.getText().toUpperCase());
		ps.setString(3, authorTextField.getText().toUpperCase());
		ps.setString(4, publisherTextField.getText().toUpperCase());
		try {
			ps.setInt(5, Integer.parseInt(quantityTextField.getText()));
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Enter Valid Quantity!", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		try {
			ps.setLong(1, Long.parseLong(isbnTextField.getText()));
			ps.execute();
			bookViewAll(null);
			JOptionPane.showMessageDialog(null, "Book Added Successfully\nName: " + titleTextField.getText());
			isbnTextField.clear();
			titleTextField.clear();
			authorTextField.clear();
			publisherTextField.clear();
			quantityTextField.clear();
		} catch (NumberFormatException | SQLException e) {
			JOptionPane.showMessageDialog(null, "Check Fields!", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	@FXML
	void addAuthPub(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/Main/Books/AddAuthPub.fxml"));
		Stage stage = new Stage();
		stage.getIcons().add(new Image("/res/about.png"));
		DBhandle.setLayout(root, stage);
	}

	@FXML
	void viewAuthPub(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/Main/Books/ViewAuthPub.fxml"));
		Stage stage = new Stage();
		stage.setTitle("View Authors & Publishers");
		stage.getIcons().add(new Image("/res/about.png"));
		DBhandle.setLayout(root, stage);
	}

	@FXML
	void logout(ActionEvent event) throws IOException {
		System.out.println("Logging Out");
		Parent root = FXMLLoader.load(getClass().getResource("/Login/Login.fxml"));
		Stage stage = new Stage();
		stage.setMaximized(true);
		stage.setTitle("Library Management System");
		stage.getIcons().add(new Image("/res/logo.png"));
		DBhandle.setLayout(root, stage);
		((Stage) topMenu.getScene().getWindow()).close();
	}

	@FXML
	void quit(ActionEvent event) {
		System.exit(0);
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
	void bookView() throws SQLException {
		ObservableList<Book> book = FXCollections.observableArrayList();
		libraryTable.setItems(book);
		ResultSet rs;
		int sn = 0;
		if (searchTextField.getText().isEmpty()) {
			rs = DBhandle.exqS("SELECT * FROM BOOK");
			while (rs.next()) {
				Book row = new Book(rs, ++sn);
				book.add(row);
			}
			return;
		}

		PreparedStatement ps = DBhandle.exqP(
				"SELECT * FROM BOOK WHERE CAST(ISBN AS CHAR(16)) LIKE ? OR TITLE LIKE ? OR AUTHOR LIKE ? OR PUBLISHER LIKE ?");
		ps.setString(1, "%" + searchTextField.getText() + "%");
		ps.setString(2, "%" + searchTextField.getText().toUpperCase() + "%");
		ps.setString(3, "%" + searchTextField.getText().toUpperCase() + "%");
		ps.setString(4, "%" + searchTextField.getText().toUpperCase() + "%");
		rs = ps.executeQuery();
		while (rs.next()) {
			Book row = new Book(rs, ++sn);
			book.add(row);
		}
	}

	@FXML
	public void bookViewAll(ActionEvent event) throws SQLException {
		searchTextField.clear();
		bookView();
	}

	@FXML
	void BorrowBookViewAll(ActionEvent event) throws SQLException {
		borrowedSearch.clear();
		BorrowBookView();
	}

	@FXML
	void BorrowBookView() throws SQLException {
		ObservableList<BorrowedBooks> book = FXCollections.observableArrayList();
		borrowerTable.setItems(book);
		ResultSet rs;
		if (borrowedSearch.getText().isEmpty()) {
			rs = DBhandle.exqS("SELECT * FROM BORROWED WHERE RETURNED = FALSE");
			while (rs.next()) {
				BorrowedBooks row = new BorrowedBooks(rs, false);
				book.add(row);
			}
			rs = DBhandle.exqS("SELECT * FROM BORROWED WHERE RETURNED = TRUE");
			while (rs.next()) {
				BorrowedBooks row = new BorrowedBooks(rs, true);
				book.add(row);
			}
			return;
		}

		PreparedStatement ps = DBhandle.exqP("SELECT BID, BR.CID, SINCE, UNTIL FROM BORROWED BR, MEMBER, BOOK WHERE (CAST(BID AS CHAR(16)) LIKE ? "
						+ "OR CAST(BR.CID AS CHAR(10)) LIKE ? OR TITLE LIKE ? OR AUTHOR LIKE ? OR PUBLISHER LIKE ? OR NAME LIKE ?) AND BID = ISBN AND RETURNED = FALSE");
		ps.setString(1, "%" + borrowedSearch.getText() + "%");
		ps.setString(2, "%" + borrowedSearch.getText() + "%");
		ps.setString(3, "%" + borrowedSearch.getText().toUpperCase() + "%");
		ps.setString(4, "%" + borrowedSearch.getText().toUpperCase() + "%");
		ps.setString(5, "%" + borrowedSearch.getText().toUpperCase() + "%");
		ps.setString(6, "%" + borrowedSearch.getText().toUpperCase() + "%");
		rs = ps.executeQuery();
		while (rs.next()) {
			BorrowedBooks row = new BorrowedBooks(rs, false);
			book.add(row);
		}
	}

	@FXML
	void updateView(MouseEvent event) throws SQLException {
		dueDetails = FXCollections.observableArrayList(
				"ISBN \t|\tTITLE\t|\tAUTHOR  \t|\tSINCE\t|\tDUE\t|\tBorrower NAME\t|\t ID\t|\tEMAIL\t|\tPHONE");
		memberDetails = FXCollections.observableArrayList("ID\t|\tNAME\t|\tEMAIL\t|\tPHONE");
		userDetails = FXCollections.observableArrayList("NAME\t|\tEMAIL\t|\tPHONE");

		duebooksListView.setItems(dueDetails);
		membersListView.setItems(memberDetails);
		UsersListView.setItems(userDetails);

		dueDetails.add("");
		memberDetails.add("");
		userDetails.add("");

		ResultSet rs = DBhandle.exqS("SELECT * FROM MEMBER");
		while (rs.next()) {
			String details = rs.getInt(1) + "\t|\t" + rs.getString(2) + "\t|\t" + rs.getString(3) + "\t|\t"
					+ rs.getLong(4);
			memberDetails.add(details);
		}

		rs = DBhandle.exqS("SELECT * FROM REG_USER");
		while (rs.next()) {
			String details = rs.getString(2) + "\t|\t" + rs.getString(3) + "\t|\t" + rs.getLong(4);
			userDetails.add(details);
		}

		PreparedStatement ps = DBhandle.exqP("SELECT * FROM BORROWED WHERE RETURNED = FALSE AND UNTIL < ?");
		ps.setDate(1, Date.valueOf(LocalDate.now()));
		rs = ps.executeQuery();
		String details;
		long bid;
		int cid;
		while (rs.next()) {
			bid = rs.getLong(1);
			cid = rs.getInt(2);
			String since = rs.getDate(3).toString();
			String due = rs.getDate(4).toString();
			ResultSet bkRs = DBhandle.exqS("SELECT * FROM BOOK WHERE ISBN = " + bid);
			ResultSet memberRs = DBhandle.exqS("SELECT * FROM MEMBER WHERE CID = " + cid);
			bkRs.next();
			memberRs.next();
			details = bid + " \t|\t" + bkRs.getString("TITLE") + "\t|\t " + bkRs.getString("AUTHOR") + "\t|\t " + since
					+ "\t|\t" + due + "\t|\t" + memberRs.getString("NAME") + "\t|\t" + memberRs.getInt(1) + "\t|\t"
					+ memberRs.getString("EMAIL") + "\t|\t" + memberRs.getLong("PH");
			dueDetails.add(details);
		}
	}
}