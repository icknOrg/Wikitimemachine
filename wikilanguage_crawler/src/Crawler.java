import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import util.DbConnector;

public class Crawler {

	public static void main(String[] args) {
		try {
			Connection c = DbConnector.getInstance().getDbConnection();
			Statement st = null;
			ResultSet rs = null;
			st = c.createStatement();
			rs = st.executeQuery("select * from " + "tblnarkosen");
			while (rs.next()) {
				System.out.println(rs.getString("Protokollnummer"));

			}
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
