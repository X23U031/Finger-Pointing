package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.RewardDAO;
import dao.ScoreDAO;
import dto.RankingEntry;
import dto.Score;
import dto.User;

@WebServlet("/SaveScoreServlet")
public class SaveScoreServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int finalScore = 0;
		try {
			finalScore = Integer.parseInt(request.getParameter("score"));
		} catch (NumberFormatException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid score format.");
			return;
		}

		HttpSession session = request.getSession();
		User loginUser = (User) session.getAttribute("loginUser");

		int rank = 0;

		Connection con = null;
		ScoreDAO scoreDao = new ScoreDAO();
		RewardDAO rewardDao = new RewardDAO();

		try {
			// ★ 1. 接続開始 (ここから切断まで1つの接続を使う)
			con = scoreDao.connect();

			if (loginUser != null) {
				System.out.println("ログインユーザー: " + loginUser.getUserName() + " Score: " + finalScore);

				Score score = new Score();
				score.setUserId(loginUser.getUserId());
				score.setScore(finalScore);
				score.setGameMode(1);

				scoreDao.insertScore(con, score);

				// 順位計算
				List<RankingEntry> rankingList = scoreDao.getRankingList(con);
				for (int i = 0; i < rankingList.size(); i++) {
					if (rankingList.get(i).getUserId() == loginUser.getUserId()) {
						rank = i + 1;
						break;
					}
				}

				// ★★★ 実績解除ロジック (0点でも実行されるようにifを外しました) ★★★

				// 1. 累計スコア実績
				rewardDao.checkAndGrantScoreAchievements(con, loginUser.getUserId());

				// 2. 順位実績
				rewardDao.checkAndGrantRankAchievements(con, loginUser.getUserId(), rank);

				// 3. プレイ回数実績 (★追加)
				rewardDao.checkAndGrantPlayCountAchievements(con, loginUser.getUserId());

				// 4. 特殊スコア実績 (★追加)
				rewardDao.checkAndGrantSpecialScoreAchievements(con, loginUser.getUserId(), finalScore);

			} else {
				System.out.println("ゲストプレイヤーのスコアは保存しません。");
			}

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			// ★ 2. 最後に切断
			try {
				if (con != null)
					scoreDao.disConnect();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		session.setAttribute("lastScore", finalScore);
		session.setAttribute("lastMode", "normal");
		session.setAttribute("lastRank", rank);

		response.setStatus(HttpServletResponse.SC_OK);
	}
}