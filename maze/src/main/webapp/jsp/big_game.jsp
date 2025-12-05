<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>フラッシュ迷路 (タイムアタック)</title>

<%-- game.css をそのまま流用します --%>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/game.css">
</head>
<body>
	<a href="${pageContext.request.contextPath}/jsp/index.jsp"
		class="back-btn">タイトルへ</a>
	<div id="top-panel">
		<span id="time-label">05 : 00</span>
		<%-- タイムアタック用に時間を変更 --%>
	</div>

	<div id="maze-container">
		<canvas id="maze-canvas" width="300" height="300"></canvas>
	</div>

	<div id="bottom-panel">
		<%-- タイムアタックではスコアは使わないが、JSが参照するので残す --%>
		<span id="score-label">到達数 : 0</span>
	</div>

	<%-- game.js にプロジェクトのパスを教えるための変数 --%>
	<script>
		const CONTEXT_PATH = "${pageContext.request.contextPath}";
	</script>

	<%-- ★ 読み込むJSファイルを big_game.js に変更 ★ --%>
	<script src="${pageContext.request.contextPath}/js/big_game.js" defer></script>
</body>
</html>