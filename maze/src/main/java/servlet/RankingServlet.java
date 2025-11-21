package servlet;

import java.io.IOException;
import java.sql.Connection; // ★ Connectionをインポート
import java.sql.SQLException; // ★ SQLExceptionをインポート
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.ScoreDAO;
import dto.RankingEntry;

@WebServlet("/RankingServlet")
public class RankingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		ScoreDAO dao = new ScoreDAO();
		Connection con = null;
		List<RankingEntry> normalRankingList = null;
		List<RankingEntry> timeAttackRankingList = null;

		try {
			// ★ 1. 接続
			con = dao.connect();

			// ★ 2. 2つのDAOメソッドを、同じ接続(con)で呼び出す
			normalRankingList = dao.getRankingList(con);
			timeAttackRankingList = dao.getTimeAttackRankingList(con);

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			// ★ 3. 最後に切断
			try {
				if (con != null)
					dao.disConnect();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		request.setAttribute("normalRankingList", normalRankingList);
		request.setAttribute("timeAttackRankingList", timeAttackRankingList);

		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/ranking.jsp");
		dispatcher.forward(request, response);
	}

}