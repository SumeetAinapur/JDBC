package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Gives us a connection to the MySQL database
public class DBConnection {
	private static final String URL = "jdbc:mysql://localhost:3306/leave_db";
	private static final String USER = "root";
	private static final String PASSWORD = "3432";

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL, USER, PASSWORD);
	}
}
