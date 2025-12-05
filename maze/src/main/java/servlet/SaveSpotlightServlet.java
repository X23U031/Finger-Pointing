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

@WebServlet("/SaveSpotlightServlet")
public class SaveSpotlightServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("SaveSpotlightServletが呼ばれました！");

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

		// ★順位の初期値は「1位」にしておく
		int rank = 1;

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
				score.setGameMode(3);

				scoreDao.insertScore(con, score);

				// ★★★ 修正箇所ここから ★★★
				// 不要な if (finalScore <= 0) { ... } を削除しました。

				List<RankingEntry> rankingList = scoreDao.getSpotlightRankingList(con);

				// 「今回のスコア」がランキングの何位に相当するか計算
				for (RankingEntry entry : rankingList) {
					// 自分より高いスコアの人がいたら、順位を1つ下げる
					if (entry.getScore() > finalScore) {
						rank++;
					}
				}
				// ★★★ 修正箇所ここまで ★★★

				// 実績解除
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

		// リザルト画面へのデータ渡し
		session.setAttribute("lastScore", finalScore);
		session.setAttribute("lastMode", "spotlight");
		session.setAttribute("gameMode", 3);

		// ★ result.jsp は "lastRank" を探しているのでこの名前で保存
		session.setAttribute("lastRank", rank);

		response.setStatus(HttpServletResponse.SC_OK);
	}
}