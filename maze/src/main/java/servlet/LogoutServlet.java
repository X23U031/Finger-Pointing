package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

//ログアウト

@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// セッションを取得（なければ作らない）
		HttpSession session = request.getSession(false);

		if (session != null) {
			// セッションが存在すれば、それを無効にする
			session.invalidate();
		}

		// タイトル画面にリダイレクト
		response.sendRedirect(request.getContextPath() + "/jsp/index.jsp");
	}

}