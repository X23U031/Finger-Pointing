package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dto.Reward;

public class RewardDAO extends BaseDao {

	//庄司はあほ
	// ★ Connectionを受け取る形に修正
	public List<Reward> findAll(Connection con) {
		List<Reward> rewardList = new ArrayList<>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT * FROM reward ORDER BY reward_id ASC";
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				Reward reward = new Reward();
				reward.setRewardId(rs.getInt("reward_id"));
				reward.setRewardName(rs.getString("reward_name"));
				reward.setRewardContent(rs.getString("reward_content"));
				rewardList.add(reward);
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
		return rewardList;
	}

	// ★ Connectionを受け取る形に修正
	public Set<Integer> findUnlockedRewardIds(Connection con, int userId) {
		Set<Integer> unlockedIds = new HashSet<>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT reward_id FROM user_rewards WHERE user_id = ?";
			ps = con.prepareStatement(sql);
			ps.setInt(1, userId);
			rs = ps.executeQuery();
			while (rs.next()) {
				unlockedIds.add(rs.getInt("reward_id"));
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
		return unlockedIds;
	}

	/**
	 * 【メイン処理1】累計スコア実績 (ID 5, 6, 7, 19, 20, 21)
	 */
	public void checkAndGrantScoreAchievements(Connection con, int userId) {
		int totalScore = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT SUM(score) AS total_score FROM score WHERE user_id = ? AND game_mode = 1";
			ps = con.prepareStatement(sql);
			ps.setInt(1, userId);
			rs = ps.executeQuery();
			if (rs.next()) {
				totalScore = rs.getInt("total_score");
			}
			// 累計スコア
			if (totalScore >= 100)
				grantRewardIfNotOwned(con, userId, 7);
			if (totalScore >= 500)
				grantRewardIfNotOwned(con, userId, 6);
			if (totalScore >= 1000)
				grantRewardIfNotOwned(con, userId, 5);
			// ゾロ目
			if (totalScore >= 111)
				grantRewardIfNotOwned(con, userId, 20);
			if (totalScore >= 222)
				grantRewardIfNotOwned(con, userId, 21);
			if (totalScore >= 777)
				grantRewardIfNotOwned(con, userId, 19);

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
	}

	/**
	 * 【メイン処理2】順位実績 (ID 1, 2)
	 */
	public void checkAndGrantRankAchievements(Connection con, int userId, int rank) {
		if (rank <= 0 || rank > 10)
			return;
		try {
			if (rank <= 10)
				grantRewardIfNotOwned(con, userId, 2);
			if (rank == 1)
				grantRewardIfNotOwned(con, userId, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 【メイン処理3】プレイ回数実績 (ID 12, 22, 23) ★新設★
	 */
	public void checkAndGrantPlayCountAchievements(Connection con, int userId) {
		int playCount = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			// ノーマルモード(1)のプレイ回数をカウント
			String sql = "SELECT COUNT(*) AS cnt FROM score WHERE user_id = ? AND game_mode = 1";
			ps = con.prepareStatement(sql);
			ps.setInt(1, userId);
			rs = ps.executeQuery();
			if (rs.next()) {
				playCount = rs.getInt("cnt");
			}
			// プレイ回数チェック
			if (playCount >= 1)
				grantRewardIfNotOwned(con, userId, 12); // 初プレイ
			if (playCount >= 10)
				grantRewardIfNotOwned(con, userId, 22); // 10回
			if (playCount >= 100)
				grantRewardIfNotOwned(con, userId, 23); // 100回

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
	}

	/**
	 * 【メイン処理4】特殊スコア実績 (ID 9, 27) ★新設★
	 */
	public void checkAndGrantSpecialScoreAchievements(Connection con, int userId, int currentScore) {
		try {
			// ID 27: 沈黙（スコア0）
			if (currentScore == 0) {
				grantRewardIfNotOwned(con, userId, 27);
			}
			// ID 9: 素数
			if (isPrime(currentScore)) {
				grantRewardIfNotOwned(con, userId, 9);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 素数判定ヘルパーメソッド
	private boolean isPrime(int n) {
		if (n <= 1)
			return false;
		if (n == 2)
			return true;
		if (n % 2 == 0)
			return false;
		for (int i = 3; i <= Math.sqrt(n); i += 2) {
			if (n % i == 0)
				return false;
		}
		return true;
	}

	// ★ Connectionを受け取る形に修正
	private void grantRewardIfNotOwned(Connection con, int userId, int rewardId) {
		PreparedStatement checkPs = null;
		ResultSet checkRs = null;
		PreparedStatement insertPs = null;
		try {
			String checkSql = "SELECT COUNT(*) AS count FROM user_rewards WHERE user_id = ? AND reward_id = ?";
			checkPs = con.prepareStatement(checkSql);
			checkPs.setInt(1, userId);
			checkPs.setInt(2, rewardId);
			checkRs = checkPs.executeQuery();

			int count = 0;
			if (checkRs.next())
				count = checkRs.getInt("count");

			if (count == 0) {
				System.out.println("★ 実績解除！ UserID: " + userId + ", RewardID: " + rewardId);
				String insertSql = "INSERT INTO user_rewards (user_id, reward_id, achieved_at) VALUES (?, ?, NOW())";
				insertPs = con.prepareStatement(insertSql);
				insertPs.setInt(1, userId);
				insertPs.setInt(2, rewardId);
				insertPs.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (checkRs != null)
					checkRs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (checkPs != null)
					checkPs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (insertPs != null)
					insertPs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}