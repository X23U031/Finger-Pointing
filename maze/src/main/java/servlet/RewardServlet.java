package servlet;

import java.io.IOException;
import java.sql.Connection; // ★ Connectionをインポート
import java.sql.SQLException; // ★ SQLExceptionをインポート
import java.util.List;
import java.util.Set;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.RewardDAO;
import dto.Reward;
import dto.User;

@WebServlet("/RewardServlet")
public class RewardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		RewardDAO dao = new RewardDAO();
		Connection con = null;
		List<Reward> allRewardsList = null;

		try {
			// ★ 1. 接続
			con = dao.connect();

			// 2. 「すべての実績」のリストを取得
			allRewardsList = dao.findAll(con);

			HttpSession session = request.getSession(false);
			User loginUser = null;
			if (session != null) {
				loginUser = (User) session.getAttribute("loginUser");
			}

			if (loginUser != null) {
				// 3a. 「解除済みID」のリストを取得
				Set<Integer> unlockedIds = dao.findUnlockedRewardIds(con, loginUser.getUserId());

				// 3b. フラグを立てる
				for (Reward reward : allRewardsList) {
					if (unlockedIds.contains(reward.getRewardId())) {
						reward.setUnlocked(true);
					}
				}
			}

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			// ★ 4. 切断
			try {
				if (con != null)
					dao.disConnect();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		// 5. フラグ設定済みの「すべての実績」リストをリクエストに詰める
		request.setAttribute("rewardList", allRewardsList);

		// 6. reward.jsp に処理をフォワード
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/reward.jsp");
		dispatcher.forward(request, response);
	}

}