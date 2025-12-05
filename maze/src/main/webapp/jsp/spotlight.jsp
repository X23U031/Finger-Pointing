<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>フラッシュ迷路 (暗闇モード)</title>

<%-- ★変更点: spotlight.css を読み込む --%>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/spotlight.css">

</head>
<body>

	<%-- 上部パネル --%>
	<div id="top-panel">
		<span id="time-label">03 : 00</span>
	</div>

	<%-- 中央パネル --%>
	<div id="maze-container">
		<canvas id="maze-canvas" width="300" height="300"></canvas>
	</div>

	<%-- 下部パネル --%>
	<div id="bottom-panel">
		<span id="score-label">到達数 : 0</span>
	</div>

	<%-- コンテキストパス定義 --%>
	<script>
		const CONTEXT_PATH = "${pageContext.request.contextPath}";
	</script>

	<%-- JS読み込み（キャッシュ対策付き） --%>
	<script
		src="${pageContext.request.contextPath}/js/spotlight.js?v=<%= System.currentTimeMillis() %>"
		defer></script>

</body>