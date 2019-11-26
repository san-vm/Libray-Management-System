package Database;

import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class DBhandle {
	private static String DB_URL = "jdbc:derby:database;create=true;dataEncryption=true;bootPassword=";
	private static DBhandle handle = null;
	public static Statement stmt;
	private static PreparedStatement ps;
	private static ResultSet rs;
	private static Connection conn;
	public static String currentUser = "null";

	private DBhandle() {
		try {
			getConn();
		} catch (Exception e) {
			System.exit(1);
		}
	}

	public static DBhandle getHandle() {
		if (handle == null)
			handle = new DBhandle();
		return handle;
	}

	private static void getConn() throws Exception {
		Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
		conn = DriverManager.getConnection(DB_URL + "qwertyuiop"); // CHANGE DATABASE PASSWORD HERE
		stmt = conn.createStatement();
		System.out.println("Searching Database");
		DatabaseMetaData dbm = conn.getMetaData();
		rs = dbm.getTables(null, null, "BORROWED", null);
		if (rs.next()) {
			System.out.println("DataBase Exists");
			return;
		}
		createDB();
	}

	private static void createDB() throws Exception {
		System.out.println(" -Not Found\nCreating Database");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(DBhandle.class.getClass().getResourceAsStream("/Database/tables.xml"));
		NodeList nList = doc.getElementsByTagName("table-entry");
		String query;
		for (int i = 0; i < nList.getLength(); i++) {
			Node nNode = nList.item(i);
			Element entry = (Element) nNode;
			String tableName = entry.getAttribute("name");
			String coldata = entry.getAttribute("col-data");
			query = "CREATE TABLE " + tableName + "(" + coldata + ")";
			stmt.execute(query);
			System.out.print("-");
		}
		stmt.execute("INSERT INTO CONFIG VALUES(15)");
		stmt.execute("INSERT INTO LOGINS VALUES('admin', 'admin')");

		System.out.println("OK");
	}

	public static ResultSet validate(String user, String passwd) throws SQLException {
		String credentials = "SELECT * FROM LOGINS WHERE USERNAME = ? AND PASSWORD = ?";
		ps = conn.prepareStatement(credentials);
		ps.setString(1, user);
		ps.setString(2, passwd);
		rs = ps.executeQuery();
		currentUser = user;
		return rs;
	}

	public static void setLayout(Parent root, Stage stage) {
		stage.setScene(new Scene(root));
		stage.show();
	}

	public static ResultSet exqS(String query) throws SQLException {
		return conn.createStatement().executeQuery(query);
	}

	public static PreparedStatement exqP(String query) throws SQLException {
		return conn.prepareStatement(query);
	}
}