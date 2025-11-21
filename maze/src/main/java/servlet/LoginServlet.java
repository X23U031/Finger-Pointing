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

import dao.UserDAO;
import dto.User;
import util.PasswordUtil;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		String userName = request.getParameter("username");
		String rawPassword = request.getParameter("password");

		String hashedPassword = PasswordUtil.hash(rawPassword);

		UserDAO dao = new UserDAO();
		Connection con = null;
		User loginUser = null;

		try {
			// ★ 1. 接続
			con = dao.connect();

			// ★ 2. DAOに con を渡す
			loginUser = dao.findByUserNameAndPassword(con, userName, hashedPassword);

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			// ★ 3. 切断
			try {
				if (con != null)
					dao.disConnect();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		// 4. ログイン判定
		if (loginUser != null) {
			HttpSession session = request.getSession();
			session.setAttribute("loginUser", loginUser);
			response.sendRedirect("jsp/index.jsp");

		} else {
			request.setAttribute("errorMessage", "ユーザー名またはパスワードが間違っています。");
			request.getRequestDispatcher("jsp/login.jsp").forward(request, response);
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("jsp/index.jsp").forward(request, response);
	}

}