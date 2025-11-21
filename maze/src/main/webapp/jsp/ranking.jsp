<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%-- ✅ JSTLのおまじない --%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%-- ✅ Javaのクラス(DTO)をJSPで使うためのおまじない --%>
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

			<%-- ★★★ 1. ノーマルモード・ランキング ★★★ --%>
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
					<c:forEach var="entry" items="${normalRankingList}"
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
					<c:if test="${empty normalRankingList}">
						<tr>
							<td colspan="3" style="text-align: center;">ランキングデータはまだありません。</td>
						</tr>
					</c:if>
				</tbody>
			</table>

			<%-- ★★★ 2. タイムアタック・ランキング ★★★ --%>
			<h2>タイムアタックモード (クリアタイム)</h2>
			<table>
				<thead>
					<tr>
						<th>順位</th>
						<th>名前</th>
						<th>スコア (M:SS.mmm)</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="entry" items="${timeAttackRankingList}"
						varStatus="status">
						<tr>
							<td>${status.count}</td>
							<td>
								<div class="scrolling-wrapper">
									<span class="scrolling-text">${entry.userName}</span>
								</div>
							</td>
							<td style="text-align: right;">
								<%-- タイムも右揃えに --%> <%-- 
                                  ta_result.jsp と同じロジックでタイムをフォーマット
                                --%> <% 
                                   // JSTLのループ変数 "entry" をJavaで取得
                                   RankingEntry entry = (RankingEntry) pageContext.getAttribute("entry");
                                   long timeInMs = (long) entry.getScore();
                                   
                                   long totalSeconds = timeInMs / 1000;
                                   long minutes = totalSeconds / 60;        // 分
                                   long seconds = totalSeconds % 60;        // 秒
                                   long millis = timeInMs % 1000;         // ミリ秒
                                   
                                   // 画面に出力
                                   out.print(String.format("%d : %02d . %03d", minutes, seconds, millis));
                                %>
							</td>
						</tr>
					</c:forEach>
					<c:if test="${empty timeAttackRankingList}">
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

	<%-- スクロールアニメーションを実行 (変更なし) --%>
	<script>
        document.addEventListener('DOMContentLoaded', () => {
            const cellsToAnimate = [];
            document.querySelectorAll('tbody td:nth-child(2)').forEach(cell => {
                cellsToAnimate.push({ cell: cell, limit: NAME_CHAR_LIMIT });
            });

            cellsToAnimate.forEach(item => setupScrollingAnimation(item.cell, item.limit));

            let resizeTimer;
            window.addEventListener('resize', () => {
                clearTimeout(resizeTimer);
                document.querySelectorAll('.scrolling-text').forEach(span => {
                    if (span.animationLoop) clearTimeout(span.animationLoop);
                });
                resizeTimer = setTimeout(() => {
                    cellsToAnimate.forEach(item => setupScrollingAnimation(item.cell, item.limit));
                }, 200);
            });
        });
    </script>

</body>
</html>