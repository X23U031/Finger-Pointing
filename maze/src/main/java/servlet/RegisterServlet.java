package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.UserDAO;
import dto.User;
import util.PasswordUtil;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		String userName = request.getParameter("username");
		String rawPassword = request.getParameter("password");

		String hashedPassword = PasswordUtil.hash(rawPassword);

		User user = new User();
		user.setUserName(userName);
		user.setPasswordHash(hashedPassword);

		UserDAO dao = new UserDAO();
		Connection con = null;

		try {
			// ★ 1. 接続
			con = dao.connect();

			// ★ 2. DAOに con を渡す
			dao.insert(con, user);

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

		response.sendRedirect("jsp/login.jsp");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("jsp/register.jsp").forward(request, response);
	}

}