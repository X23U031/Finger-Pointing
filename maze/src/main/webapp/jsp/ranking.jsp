<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ page import="dto.RankingEntry"%>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>ランキング</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/ranking.css">
</head>
<body>
	<div class="reward-container">
		<h1>ランキング</h1>

		<div class="reward-table-wrapper">

			<%-- 1. ノーマルモード --%>
			<h2>ノーマルモード (スコアアタック)</h2>
			<table>
				<thead>
					<tr>
						<th>順位</th>
						<th>名前</th>
						<th>スコア</th>
					</tr>
				</thead>
				<tbody>
					<%-- JSTLを使ってリストを表示 --%>
					<c:forEach var="entry" items="${normalRankingList}"
						varStatus="status">
						<tr>
							<td>${status.count}</td>
							<td><div class="scrolling-wrapper">
									<span class="scrolling-text">${entry.userName}</span>
								</div></td>
							<td>${entry.score}</td>
						</tr>
					</c:forEach>
					<c:if test="${empty normalRankingList}">
						<tr>
							<td colspan="3" style="text-align: center;">データなし</td>
						</tr>
					</c:if>
				</tbody>
			</table>

			<%-- 2. タイムアタックモード --%>
			<h2>タイムアタックモード (クリアタイム)</h2>
			<table>
				<thead>
					<tr>
						<th>順位</th>
						<th>名前</th>
						<th>タイム</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="entry" items="${timeAttackRankingList}"
						varStatus="status">
						<tr>
							<td>${status.count}</td>
							<td><div class="scrolling-wrapper">
									<span class="scrolling-text">${entry.userName}</span>
								</div></td>
							<td style="text-align: right;">
								<%
								RankingEntry entry = (RankingEntry) pageContext.getAttribute("entry");
								long timeInMs = (long) entry.getScore();
								long min = (timeInMs / 1000) / 60;
								long sec = (timeInMs / 1000) % 60;
								long ms = timeInMs % 1000;
								out.print(String.format("%d : %02d . %03d", min, sec, ms));
								%>
							</td>
						</tr>
					</c:forEach>
					<c:if test="${empty timeAttackRankingList}">
						<tr>
							<td colspan="3" style="text-align: center;">データなし</td>
						</tr>
					</c:if>
				</tbody>
			</table>

			<%-- ★★★ 3. スポットライトモード・ランキング (★追加) ★★★ --%>
			<h2>スポットライトモード (到達数)</h2>
			<table>
				<thead>
					<tr>
						<th>順位</th>
						<th>名前</th>
						<th>到達エリア数</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="entry" items="${spotlightRankingList}"
						varStatus="status">
						<tr>
							<td>${status.count}</td>
							<td>
								<div class="scrolling-wrapper">
									<span class="scrolling-text">${entry.userName}</span>
								</div>
							</td>
							<td>${entry.score}</td>
						</tr>
					</c:forEach>
					<c:if test="${empty spotlightRankingList}">
						<tr>
							<td colspan="3" style="text-align: center;">ランキングデータはまだありません。</td>
						</tr>
					</c:if>
				</tbody>
			</table>

		</div>
	</div>

	<a href="${pageContext.request.contextPath}/jsp/index.jsp"
		class="back-button btn">戻る</a>
	<div id="custom-cursor"></div>
	<script src="${pageContext.request.contextPath}/js/ranking.js"></script>

	<script>
        document.addEventListener('DOMContentLoaded', () => {
            const cellsToAnimate = [];
            document.querySelectorAll('tbody td:nth-child(2)').forEach(cell => {
                cellsToAnimate.push({ cell: cell, limit: 22 });
            });
            cellsToAnimate.forEach(item => setupScrollingAnimation(item.cell, item.limit));
        });
    </script>
</body>
</html>
