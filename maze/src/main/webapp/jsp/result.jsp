<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>ゲーム終了</title>

<%-- CSS読み込み --%>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/result.css">
</head>
<body>
	<div class="game-over-container">
		<h1>ゲーム終了</h1>

		<p class="score">
			到達記録: <span id="score-display"> 
				<%-- スコア表示：score または lastScore を探す --%>
				<c:choose>
					<c:when test="${not empty sessionScope.score}">
                        <c:out value="${sessionScope.score}" />
                    </c:when>
					<c:when test="${not empty sessionScope.lastScore}">
                        <c:out value="${sessionScope.lastScore}" />
                    </c:when>
					<c:otherwise>
                        0
                    </c:otherwise>
				</c:choose>
			</span>
		</p>

		<p class="rank">
			順位: <span id="rank-display">
				<c:choose>
					<c:when test="${not empty sessionScope.rank}">
						<c:out value="${sessionScope.rank}" />位
					</c:when>
					<c:otherwise>未計測</c:otherwise>
				</c:choose>
			</span>
		</p>

		<div class="button-group">
			<a href="${pageContext.request.contextPath}/jsp/index.jsp"
				class="button" id="title-button">タイトル</a>

			<%-- ★リトライボタンの分岐 --%>
			<c:choose>
				
				<%-- gameMode == 3 : 暗闇モード --%>
				<c:when test="${sessionScope.gameMode == 3}">
					<a href="${pageContext.request.contextPath}/jsp/spotlight.jsp"
						class="button" id="retry-button">リトライ</a>
				</c:when>
				
				<%-- gameMode == 2 : タイムアタック (ta_game.jsp) --%>
				<c:when test="${sessionScope.gameMode == 2}">
					<a href="${pageContext.request.contextPath}/jsp/ta_game.jsp"
						class="button" id="retry-button">リトライ</a>
				</c:when>
				
				<%-- それ以外 : ノーマルモード --%>
				<c:otherwise>
					<a href="${pageContext.request.contextPath}/jsp/game.jsp"
						class="button" id="retry-button">リトライ</a>
				</c:otherwise>
			</c:choose>
		</div>
	</div>

	<div id="custom-cursor"></div>

	<script src="${pageContext.request.contextPath}/js/result.js"></script>
</body>
</html>