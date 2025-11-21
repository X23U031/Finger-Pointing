<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>ゲーム終了</title>

<%-- ✅ CSSを外部ファイルから読み込み --%>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/result.css">
</head>
<body>
	<div class="game-over-container">
		<h1>ゲーム終了</h1>

		<%-- 
          ✅ [修正]
          SaveScoreServletでセッションに保存した "lastScore" を表示 
          c:choose を使って、もしセッションにスコアがなければ "0" を表示
        --%>
		<p class="score">
			到達記録: <span id="score-display"> <c:choose>
					<c:when test="${not empty sessionScope.lastScore}">
                        ${sessionScope.lastScore}
                    </c:when>
					<c:otherwise>
                        0
                    </c:otherwise>
				</c:choose>
			</span>
		</p>

		<p class="rank">
			順位: <span id="rank-display"> <c:choose>
					<%-- 順位が 0 より大きい (ランクインした) 場合 --%>
					<c:when
						test="${not empty sessionScope.lastRank && sessionScope.lastRank > 0}">
                        ${sessionScope.lastRank} 位
                    </c:when>
					<%-- ゲストプレイ、またはランク外の場合 --%>
					<c:otherwise>
                        ランク外
                    </c:otherwise>
				</c:choose>
			</span>
		</p>

		<div class="button-group">
			<%-- ✅ リンクを絶対パスに修正 --%>
			<a href="${pageContext.request.contextPath}/jsp/index.jsp"
				class="button" id="title-button">タイトル</a>
			<%-- ✅ リンクを絶対パスに修正 --%>
			<a href="${pageContext.request.contextPath}/jsp/game.jsp"
				class="button" id="retry-button">リトライ</a>
		</div>
	</div>

	<div id="custom-cursor"></div>

	<%-- ✅ JavaScriptを外部ファイルから読み込み --%>
	<script src="${pageContext.request.contextPath}/js/result.js"></script>
</body>
</html>