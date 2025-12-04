package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dto.RankingEntry;
import dto.Score;

public class ScoreDAO extends BaseDao {

	public void insertScore(Connection con, Score score) {
		PreparedStatement ps = null;
		try {
			String sql = "INSERT INTO score (user_id, score, game_mode, played_at) VALUES (?, ?, ?, NOW())";
			ps = con.prepareStatement(sql);
			ps.setInt(1, score.getUserId());
			ps.setInt(2, score.getScore());
			ps.setInt(3, score.getGameMode());
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

	// モード1: ノーマル (スコア高い順)
	public List<RankingEntry> getRankingList(Connection con) {
		List<RankingEntry> rankingList = new ArrayList<>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT u.user_id, u.user_name, MAX(s.score) AS max_score " +
					"FROM score s JOIN user u ON s.user_id = u.user_id " +
					"WHERE s.game_mode = 1 GROUP BY u.user_id, u.user_name " +
					"ORDER BY max_score DESC LIMIT 100";
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				RankingEntry entry = new RankingEntry();
				entry.setUserId(rs.getInt("user_id"));
				entry.setUserName(rs.getString("user_name"));
				entry.setScore(rs.getInt("max_score"));
				rankingList.add(entry);
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
		return rankingList;
	}

	// モード2: タイムアタック (タイム速い順)
	public List<RankingEntry> getTimeAttackRankingList(Connection con) {
		List<RankingEntry> rankingList = new ArrayList<>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT u.user_id, u.user_name, MIN(s.score) AS best_time " +
					"FROM score s JOIN user u ON s.user_id = u.user_id " +
					"WHERE s.game_mode = 2 AND s.score > 0 GROUP BY u.user_id, u.user_name " +
					"ORDER BY best_time ASC LIMIT 100";
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				RankingEntry entry = new RankingEntry();
				entry.setUserId(rs.getInt("user_id"));
				entry.setUserName(rs.getString("user_name"));
				entry.setScore(rs.getInt("best_time"));
				rankingList.add(entry);
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
		return rankingList;
	}

	// ★★★ モード3: スポットライト (スコア高い順に修正) ★★★
	public List<RankingEntry> getSpotlightRankingList(Connection con) {
		List<RankingEntry> rankingList = new ArrayList<>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			// ★ MAX(s.score) と DESC に変更
			String sql = "SELECT u.user_id, u.user_name, MAX(s.score) AS max_score " +
					"FROM score s JOIN user u ON s.user_id = u.user_id " +
					"WHERE s.game_mode = 3 GROUP BY u.user_id, u.user_name " +
					"ORDER BY max_score DESC LIMIT 100";
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				RankingEntry entry = new RankingEntry();
				entry.setUserId(rs.getInt("user_id"));
				entry.setUserName(rs.getString("user_name"));
				entry.setScore(rs.getInt("max_score")); // ★ max_score
				rankingList.add(entry);
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
		return rankingList;
	}
}