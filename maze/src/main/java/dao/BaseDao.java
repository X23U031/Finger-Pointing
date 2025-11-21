package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//データベース接続用
public class BaseDao {

	private static final String URL = "jdbc:mysql://localhost:3306/maze";
	private static final String USER = "root";
	private static final String PASSWORD = "";

	// コネクション
	protected Connection con = null;

	// DB接続する
	public Connection connect() throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		con = DriverManager.getConnection(URL, USER, PASSWORD);
		return con;
	}

	// DB切断する
	public void disConnect() throws SQLException {
		if (con != null) {
			con.close();
		}
	}
}