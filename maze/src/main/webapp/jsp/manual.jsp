<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>遊び方 - フラッシュ迷路</title>

<%-- ✅ CSSを外部ファイルから読み込み --%>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/manual.css">
</head>

<body>

	<div class="manual-container">
		<h1>遊び方</h1>
		<ol>
			<li>ゲーム開始と同時に制限時間がスタートします。</li>
			<li>ポインターを操作してゴールを目指してください。</li>
			<li>制限時間内に多くの迷路をクリアし、高スコアを目指しましょう。</li>
		</ol>

		<h2>操作方法</h2>
		<ul class="controls-list">
			<li><strong>W</strong>: 上に進む</li>
			<li><strong>A</strong>: 左に進む</li>
			<li><strong>S</strong>: 下に進む</li>
			<li><strong>D</strong>: 右に進む</li>
			<li><strong>Space</strong>: ポインターを停止</li>
		</ul>

	</div>

	<%-- ✅ [修正] リンクを index.jsp への絶対パスに変更 --%>
	<a href="${pageContext.request.contextPath}/jsp/index.jsp"
		class="back-button btn">戻る</a>

	<div id="custom-cursor"></div>

	<%-- ✅ JavaScriptを外部ファイルから読み込み --%>
	<script src="${pageContext.request.contextPath}/js/manual.js"></script>

</body>

</html>