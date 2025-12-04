const cursor = document.getElementById("custom-cursor");
let lastTrailTime = 0;

// カーソル追従＋光の尾
document.addEventListener("mousemove", (e) => {
	cursor.style.left = e.clientX + "px";
	cursor.style.top = e.clientY + "px";

	const now = Date.now();
	if (now - lastTrailTime > 5) {
		const trail = document.createElement("div");
		trail.className = "trail";
		trail.style.left = e.clientX + "px";
		trail.style.top = e.clientY + "px";
		document.body.appendChild(trail);
		setTimeout(() => trail.remove(), 1500);
		lastTrailTime = now;
	}
});

// ボタン上で●になる ＆ 画面遷移制御
document.querySelectorAll("a, button").forEach(el => {
	el.addEventListener("mouseenter", () => cursor.classList.add("active"));
	el.addEventListener("mouseleave", () => cursor.classList.remove("active"));

	// ボタンがクリックされたときの処理
	el.addEventListener("click", (event) => {
		// 1. デフォルトの画面遷移（<a>タグの動作）をキャンセル
		event.preventDefault();
		const targetUrl = el.href;

		// 2. 波紋エフェクトを即座に表示
		const ripple = document.createElement("div");
		ripple.className = "ripple";
		ripple.style.left = event.clientX + "px";
		ripple.style.top = event.clientY + "px";
		document.body.appendChild(ripple);
		setTimeout(() => ripple.remove(), 600);

		// 3. エフェクト完了を待ってから画面遷移
		// 700ms = 波紋アニメーション(600ms) + 余裕
		setTimeout(() => {
			if (targetUrl) {
				window.location.href = targetUrl;
			}
		}, 700);
	});
});

// 空白をクリックしたときの波紋処理（元の処理をボタンクリックと分離）
document.addEventListener("click", (e) => {
	// クリックされた要素が a または button でないことを確認
	if (e.target.closest("a, button")) {
		return;
	}

	const ripple = document.createElement("div");
	ripple.className = "ripple";
	ripple.style.left = e.clientX + "px";
	ripple.style.top = e.clientY + "px";
	document.body.appendChild(ripple);
	setTimeout(() => ripple.remove(), 600);
});
