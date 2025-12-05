<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>フラッシュ迷路</title>

<%-- ✅ CSSを外部ファイルから読み込み --%>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/game.css">
</head>
<body>
	<a href="${pageContext.request.contextPath}/jsp/index.jsp"
		class="back-btn">タイトルへ</a>
	<div id="top-panel">
		<%-- ✅ IDを "time-label" に修正 (game.jsに合わせる) --%>
		<span id="time-label">01 : 30</span>
	</div>

	<div id="maze-container">
		<canvas id="maze-canvas" width="300" height="300"></canvas>
	</div>

	<div id="bottom-panel">
		<%-- ✅ IDを "score-label" に修正 (game.jsに合わせる) --%>
		<span id="score-label">到達数 : 0</span>
	</div>

	<%-- ✅ game.js にプロジェクトのパスを教えるための変数 --%>
	<script>
		const CONTEXT_PATH = "${pageContext.request.contextPath}";
	</script>

	<%-- ✅ JavaScriptを外部ファイルから読み込み --%>
	<script src="${pageContext.request.contextPath}/js/game.js" defer></script>
</body>
</html>