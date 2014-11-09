package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnector {

	private static DbConnector instance;
	private String db = "eardata";
	private String port = "3307";
	private String host = "localhost";
	private String dbUsr = "root";
	private String dbPwd = "derpate";

	private DbConnector() {
	}

	public static synchronized DbConnector getInstance() {
		if (DbConnector.instance == null) {
			DbConnector.instance = new DbConnector();
		}
		return DbConnector.instance;
	}

	public Connection getDbConnection() throws SQLException,
			ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		return DriverManager.getConnection("jdbc:mysql://" + host + ":" + port
				+ "/" + db, dbUsr, dbPwd);
	}

}
