<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>ゲーム終了 (タイムアタック)</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/ta_result.css">
</head>
<body>
	<div class="game-over-container">
		<h1>ゲーム終了</h1>

		<p class="score">
			クリアタイム: <span id="score-display"> <%
 long timeInMs = 0;
 Object scoreObj = session.getAttribute("lastScore");
 if (scoreObj instanceof Integer) {
 	timeInMs = ((Integer) scoreObj).longValue();
 } else if (scoreObj instanceof Long) {
 	timeInMs = (Long) scoreObj;
 }

 if (timeInMs <= 0) {
 	out.print("タイムアップ");
 } else {
 	long totalSeconds = timeInMs / 1000;
 	long minutes = totalSeconds / 60;
 	long seconds = totalSeconds % 60;
 	long millis = timeInMs % 1000;
 	out.print(String.format("%d : %02d . %03d", minutes, seconds, millis));
 }
 %>
			</span>
		</p>

		<p class="rank">
			順位: <span id="rank-display"> <c:choose>
					<c:when
						test="${not empty sessionScope.lastRank && sessionScope.lastRank > 0}">
                        ${sessionScope.lastRank} 位
                    </c:when>
					<c:otherwise>ランク外</c:otherwise>
				</c:choose>
			</span>
		</p>

		<div class="button-group">
			<a href="${pageContext.request.contextPath}/jsp/index.jsp"
				class="button" id="title-button">タイトル</a> <a
				href="${pageContext.request.contextPath}/jsp/ta_game.jsp"
				class="button" id="retry-button">リトライ</a>
		</div>
	</div>

	<div id="custom-cursor"></div>
	<script src="${pageContext.request.contextPath}/js/ta_result.js"></script>
</body>
</html>