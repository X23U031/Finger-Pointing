<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>アカウント作成</title>

<%-- ✅ CSSはOK: 絶対パス --%>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/register.css">
</head>
<body>

	<header>
		<%-- ✅ [修正] リンクを絶対パスに変更 --%>
		<a href="${pageContext.request.contextPath}/jsp/index.jsp"
			class="back-to-title">タイトルに戻る</a>
		<%-- ✅ [修正] リンクを絶対パスに変更 --%>
		<a href="${pageContext.request.contextPath}/jsp/login.jsp"
			class="go-to-login">ログイン画面へ</a>
	</header>

	<div class="form-container">
		<h1>アカウント作成</h1>

		<%-- 
          ✅ Servletへの送信パスはOK: 絶対パス
        --%>
		<form class="account-form"
			action="${pageContext.request.contextPath}/RegisterServlet"
			method="POST">
			<div class="form-group">
				<label for="username">ユーザー名</label> <input type="text" id="username"
					name="username">
			</div>

			<div class="form-group">
				<label for="password">パスワード</label>
				<div class="password-wrapper">
					<input type="password" id="password" name="password">
					<button type="button" class="toggle-password"
						data-target="password">
						<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"
							fill="none" stroke="currentColor" stroke-width="2"
							stroke-linecap="round" stroke-linejoin="round">
                            <g class="icon-eye">
                                <path
								d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path>
                                <circle cx="12" cy="12" r="3"></circle>
                            </g>
                            <g class="icon-eye-off">
                                <path
								d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19m-6.72-1.07a3 3 0 1 1-4.24-4.24"></path>
                                <line x1="1" y1="1" x2="23" y2="23"></line>
                            </g>
                        </svg>
					</button>
				</div>
			</div>

			<div class="form-group">
				<label for="confirm-password" class="confirm-label">パスワード<br>（確認用）
				</label>
				<div class="password-wrapper">
					<input type="password" id="confirm-password"
						name="confirm-password">
					<button type="button" class="toggle-password"
						data-target="confirm-password">
						<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"
							fill="none" stroke="currentColor" stroke-width="2"
							stroke-linecap="round" stroke-linejoin="round">
                            <g class="icon-eye">
                                <path
								d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path>
                                <circle cx="12" cy="12" r="3"></circle>
                            </g>
                            <g class="icon-eye-off">
                                <path
								d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19m-6.72-1.07a3 3 0 1 1-4.24-4.24"></path>
                                <line x1="1" y1="1" x2="23" y2="23"></line>
                            </g>
                        </svg>
					</button>
				</div>
			</div>

			<div class="button-group">
				<button type="submit" class="btn-create-account">アカウント作成</button>
			</div>
		</form>
	</div>

	<%-- ✅ JavaScriptはOK: 絶対パス --%>
	<script src="${pageContext.request.contextPath}/js/register.js"></script>

</body>
</html>