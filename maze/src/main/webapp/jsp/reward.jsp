<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ page import="dto.Reward"%>
<%-- ★ JavaのReward DTOをインポート --%>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>実績一覧</title>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/reward.css">
</head>
<body>
	<div class="reward-container">
		<h1>実績一覧</h1>
		<div class="reward-table-wrapper">
			<table>
				<thead>
					<tr>
						<th>実績名</th>
						<th>内容</th>
					</tr>
				</thead>

				<tbody>
					<%-- 
                      Servletから渡された "rewardList" をループ
                    --%>
					<c:forEach var="reward" items="${rewardList}">

						<%-- 
                          [修正]
                          reward.unlocked (boolean) が true なら空のclassを、
                          false なら "locked-achievement" class を <tr> に適用する
                        --%>
						<tr class="${reward.unlocked ? '' : 'locked-achievement'}">

							<%-- 実績名セル --%>
							<td>
								<div class="scrolling-wrapper">
									<span class="scrolling-text">${reward.rewardName}</span>
								</div>
							</td>

							<%-- 内容セル --%>
							<td>
								<div class="scrolling-wrapper">
									<span class="scrolling-text">${reward.rewardContent}</span>
								</div>
							</td>
						</tr>
					</c:forEach>

					<c:if test="${empty rewardList}">
						<tr>
							<td colspan="2" style="text-align: center;">実績はまだありません。</td>
						</tr>
					</c:if>
				</tbody>

			</table>
		</div>
	</div>

	<a href="${pageContext.request.contextPath}/jsp/index.jsp"
		class="back-button btn">戻る</a>
	<div id="custom-cursor"></div>
	<script src="${pageContext.request.contextPath}/js/reward.js"></script>

	<script>
        document.addEventListener('DOMContentLoaded', () => {
            const cellsToAnimate = [];
            document.querySelectorAll('tbody td:first-child').forEach(cell => {
                cellsToAnimate.push({ cell: cell, limit: NAME_CHAR_LIMIT });
            });
            document.querySelectorAll('tbody td:nth-child(2)').forEach(cell => {
                cellsToAnimate.push({ cell: cell, limit: CONTENT_CHAR_LIMIT });
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