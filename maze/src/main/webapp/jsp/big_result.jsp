<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>ゲーム終了 (タイムアタック)</title>

<%-- ✅ タイムアタック用のCSS (result.cssのコピー) を読み込み --%>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/ta_result.css">
</head>
<body>
	<div class="game-over-container">
		<h1>ゲーム終了</h1>

		<%-- 
          ★★★ ここから修正 (タイムアタック専用表示) ★★★
        --%>
		<p class="score">
			クリアタイム: <span id="score-display"> <%-- 
                  JSPのJavaコード(スクリプトレット)を使って、
                  セッションから取得したミリ秒(lastScore)を M:SS.mmm 形式に変換
                --%> <%
 // 1. セッションから "lastScore" を取得 (デフォルトは 0L)
 long timeInMs = 0;
 Object scoreObj = session.getAttribute("lastScore");
 if (scoreObj instanceof Integer) {
 	timeInMs = ((Integer) scoreObj).longValue();
 } else if (scoreObj instanceof Long) {
 	timeInMs = (Long) scoreObj;
 }

 // 2. タイムアップ(0)か、クリア(0より大きい)かで表示を分岐
 if (timeInMs <= 0) {
 	// タイムアップの場合
 	out.print("タイムアップ");

 } else {
 	// クリアした場合
 	long totalSeconds = timeInMs / 1000;
 	long minutes = totalSeconds / 60; // 分
 	long seconds = totalSeconds % 60; // 秒
 	long millis = timeInMs % 1000; // ミリ秒

 	// 3. 画面に "M : SS . mmm" の形式で出力
 	out.print(String.format("%d : %02d . %03d", minutes, seconds, millis));
 }
 %>
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
			<%-- ✅ 「リトライ」は ta_game.jsp を指すように修正 --%>
			<a href="${pageContext.request.contextPath}/jsp/ta_game.jsp"
				class="button" id="retry-button">リトライ</a>
		</div>
	</div>

	<div id="custom-cursor"></div>

	<%-- ✅ タイムアタック用のJS (result.jsのコピー) を読み込み --%>
	<script src="${pageContext.request.contextPath}/js/ta_result.js"></script>
</body>
</html>