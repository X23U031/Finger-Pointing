package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
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
		List<RankingEntry> spotlightRankingList = null; // ★ 追加

		try {
			con = dao.connect();
			normalRankingList = dao.getRankingList(con);
			timeAttackRankingList = dao.getTimeAttackRankingList(con);
			spotlightRankingList = dao.getSpotlightRankingList(con); // ★ 追加
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (con != null)
					dao.disConnect();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		request.setAttribute("normalRankingList", normalRankingList);
		request.setAttribute("timeAttackRankingList", timeAttackRankingList);
		request.setAttribute("spotlightRankingList", spotlightRankingList); // ★ 追加

		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/ranking.jsp");
		dispatcher.forward(request, response);
	}
}