<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>フラッシュ迷路 - タイトル画面</title>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/style.css">

<style>
/* =====================================================================
   ▼ 隠しボタン用スタイル（test-visible）
   ===================================================================== */
/* index.jsp の <style> の中 */

.hidden-secret-btn {
    /* 位置やサイズはそのままでOK */
    position: absolute;
    width: 30px;
    height: 30px;
    
    /* ▼▼▼ ここを変える！ ▼▼▼ */
    background: transparent; /* 背景色をなしに */
    border: none;
    cursor: pointer; /* マウスを乗せた時の指マークは残す？（消すなら default にしてね） */
    z-index: 9999;
    transform: translate(-50%, -50%);
    
    /* ★ これが魔法の呪文！完全に見えなくするわ ★ */
    opacity: 0; 
}
</style>

</head>
<body>

	<div class="container">

		<%-- ログイン／ログアウトの表示分岐 (左上) --%>
		<c:if test="${empty sessionScope.loginUser}">
			<a href="${pageContext.request.contextPath}/jsp/login.jsp"
				class="btn login-btn">ログイン</a>
		</c:if>

		<c:if test="${not empty sessionScope.loginUser}">
			<div class="user-info">ようこそ、${sessionScope.loginUser.userName}
				さん</div>
			<a href="${pageContext.request.contextPath}/LogoutServlet"
				class="btn logout-btn logout-btn">ログアウト</a>
		</c:if>


		<a href="${pageContext.request.contextPath}/jsp/manual.jsp"
			class="btn manual-btn">遊び方</a>

		<main class="main-content">

			<%-- ▼▼▼ 1. 画像とボタンを包む「箱（wrapper）」を作る ▼▼▼ --%>
			<div class="logo-wrapper" style="position: relative; display: inline-block;">

                <img src="${pageContext.request.contextPath}/images/finger-pointing.png" 
                     alt="ロゴ" class="logo">

                <%-- 
                   ▼▼▼ 文字「TEST」を消して、空っぽにするの ▼▼▼ 
                --%>
                <button id="secretModeBtn" class="hidden-secret-bttesn"
                    style="top: 40%; left: 51%;">
                    <%-- ここは空でOK --%>
                </button>

            </div>
			<%-- ▲▲▲ 箱はここまで ▲▲▲ --%>

			<h1 class="title">フラッシュ迷路</h1>
		</main>

		<nav class="menu-buttons">
			<a href="${pageContext.request.contextPath}/jsp/mode_select.jsp"
				class="btn start-btn">スタート</a> <a
				href="${pageContext.request.contextPath}/RankingServlet"
				class="btn ranking-btn">ランキング</a> <a
				href="${pageContext.request.contextPath}/RewardServlet"
				class="btn reward-btn">実績</a>
		</nav>
	</div>

	<div id="custom-cursor"></div>

	<script src="${pageContext.request.contextPath}/js/main.js"></script>

	<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

	<script src="${pageContext.request.contextPath}/js/hidden_mode.js"></script>

</body>
</html>