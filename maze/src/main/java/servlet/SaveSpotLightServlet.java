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

@WebServlet("/SaveSpotLightServlet")
public class SaveSpotLightServlet extends HttpServlet {
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

		ScoreDAO scoreDao = new ScoreDAO();
		RewardDAO rewardDao = new RewardDAO();
		Connection con = null;

		try {
			con = scoreDao.connect();

			if (loginUser != null) {
				System.out.println("ログインユーザー(Spotlight): " + loginUser.getUserName() + " Score: " + finalScore);
				Score score = new Score();
				score.setUserId(loginUser.getUserId());
				score.setScore(finalScore);
				score.setGameMode(3); // ★ モード3

				scoreDao.insertScore(con, score);

				// 順位計算 (スコアが高いほうが偉い)
				List<RankingEntry> rankingList = scoreDao.getSpotlightRankingList(con);
				int calculatedRank = 1;
				for (RankingEntry entry : rankingList) {
					// ★ 自分よりスコアが高い人がいたら順位を下げる
					if (entry.getScore() > finalScore) {
						calculatedRank++;
					}
				}
				rank = calculatedRank;

				// 実績解除 (順位系のみ)
				rewardDao.checkAndGrantRankAchievements(con, loginUser.getUserId(), rank);

			} else {
				System.out.println("ゲストプレイヤー(Spotlight)のスコアは保存しません。");
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (con != null)
					scoreDao.disConnect();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		session.setAttribute("lastScore", finalScore);
		session.setAttribute("lastMode", "spotlight");
		session.setAttribute("lastRank", rank);
		response.setStatus(HttpServletResponse.SC_OK);
	}
}