package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.RewardDAO;
import dao.ScoreDAO;
import dto.Score;
import dto.User;

// ★ JSからのリクエストURLに合わせるため、マッピングは小文字の "l"
@WebServlet("/SaveSpotlightServlet")
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

		// ★★★ 修正点：エラー回避のため、ここを数値の 3 にする！ ★★★
		session.setAttribute("score", finalScore);
		session.setAttribute("gameMode", 3);

		Connection con = null;
		ScoreDAO scoreDao = new ScoreDAO();
		RewardDAO rewardDao = new RewardDAO();

		try {
			con = scoreDao.connect();

			if (loginUser != null) {
				System.out.println(
						"ログインユーザー(Spotlight): " + loginUser.getUserName() + " のスコア " + finalScore + " を保存します。");

				Score score = new Score();
				score.setUserId(loginUser.getUserId());
				score.setScore(finalScore);
				score.setGameMode(3);

				scoreDao.insertScore(con, score);

				if (finalScore > 0) {
					rewardDao.checkAndGrantScoreAchievements(con, loginUser.getUserId());
				}

			} else {
				System.out.println("ゲストプレイヤー(Spotlight)のスコア " + finalScore + " はDBに保存しません。");
			}

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error.");
		} finally {
			try {
				if (con != null)
					scoreDao.disConnect();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		response.sendRedirect(request.getContextPath() + "/jsp/result.jsp");
	}
}