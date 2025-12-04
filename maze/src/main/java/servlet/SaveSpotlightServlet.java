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

		// ★修正1: デフォルトを1位にしておく
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

				// 現在のランキング(全員のMAXスコア)を取得
				List<RankingEntry> rankingList = scoreDao.getSpotlightRankingList(con);

				// ★★★ 修正2: 「今回のスコア」の順位を計算するロジック ★★★
				// IDを探すのではなく、「自分より上のスコアが何人いるか」を数える
				for (RankingEntry entry : rankingList) {
					// もしランキング上の誰かのスコアが、今回のスコアより高ければ
					if (entry.getScore() > finalScore) {
						rank++; // 順位を1つ下げる
					}
				}

				// 実績解除 (※ここは今回の順位で判定して良いか、最高順位かによりますが、一旦今のrankで渡します)
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
		session.setAttribute("gameMode", 3);

		// ★修正3: JSPが読み取るキー名は "rank"
		session.setAttribute("rank", rank);

		response.setStatus(HttpServletResponse.SC_OK);
	}
}