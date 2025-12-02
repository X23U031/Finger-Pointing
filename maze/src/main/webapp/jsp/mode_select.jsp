<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>モード選択 - フラッシュ迷路</title>

<%-- index.jsp と同じスタイルを流用します --%>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/style.css">

<%-- ★★★ ここから追加 (このページ専用のスタイル) ★★★ --%>
<style>
/* .btn スタイル (width: 200px) よりも幅を広くする */
.mode-select-btn {
	width: 320px;
	height: auto; /* 高さを自動に */
	padding: 15px; /* 上下の余白 */
	line-height: 1.4; /* 行間 */
	/* 中身を中央揃えのままにする */
	display: flex;
	flex-direction: column;
	justify-content: center;
	align-items: center;
}

/* モード名 (大) */
.mode-title {
	font-size: 1.4rem; /* 1.2rem より少し大きく */
	font-weight: bold;
}

/* 説明文 (小) */
.mode-subtitle {
	font-size: 0.9rem; /* 1.2rem より小さく */
	color: #ccc; /* 少し薄い色に */
}
</style>
<%-- ★★★ ここまで追加 ★★★ --%>

</head>
<body>

	<div class="container">

		<%-- 戻るボタン --%>
		<a href="${pageContext.request.contextPath}/jsp/index.jsp" class="btn"
			style="position: absolute; top: 30px; left: 30px; width: auto; padding: 10px 20px; height: auto; font-size: 1rem;">戻る</a>


		<main class="main-content">
			<h1 class="title" style="font-size: 4rem; margin-bottom: 50px;">モード選択</h1>
		</main>

		<nav class="menu-buttons">
			
			<%--  ノーマルモード --%>
			<a href="${pageContext.request.contextPath}/jsp/game.jsp" class="btn mode-select-btn">
				<span class="mode-title">ノーマルモード</span>
				<span class="mode-subtitle">(3分間スコアアタック)</span>
			</a>

			<%--  タイムアタックモード --%>
			<a href="${pageContext.request.contextPath}/jsp/ta_game.jsp" class="btn mode-select-btn">
				<span class="mode-title">タイムアタックモード</span>
				<span class="mode-subtitle">(巨大迷路クリアタイム)</span>
			</a>

			<%--  暗闇モード --%>
			<a href="${pageContext.request.contextPath}/jsp/spotlight.jsp" class="btn mode-select-btn">
				<span class="mode-title">暗闇モード</span>
				<span class="mode-subtitle">(周囲が真っ暗)</span>
			</a>

		</nav>
	</div>

	<div id="custom-cursor"></div>

	<script src="${pageContext.request.contextPath}/js/main.js"></script>

</body>
</html>