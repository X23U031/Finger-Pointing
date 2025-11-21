package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dto.User;

public class UserDAO extends BaseDao {

	public User findByUserNameAndPassword(Connection con, String userName, String hashedPassword) {
		User user = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			String sql = "SELECT * FROM user WHERE user_name = ? AND password_hash = ?";
			ps = con.prepareStatement(sql);
			ps.setString(1, userName);
			ps.setString(2, hashedPassword);
			rs = ps.executeQuery();

			if (rs.next()) {
				user = new User();
				user.setUserId(rs.getInt("user_id"));
				user.setUserName(rs.getString("user_name"));
				user.setCreatedAt(rs.getTimestamp("created_at"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (ps != null)
					ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return user;
	}

	public void insert(Connection con, User user) {
		PreparedStatement ps = null;
		try {

			String sql = "INSERT INTO user (user_name, password_hash, created_at) VALUES (?, ?, NOW())";

			ps = con.prepareStatement(sql);
			ps.setString(1, user.getUserName());
			ps.setString(2, user.getPasswordHash());

			ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (ps != null)
					ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}