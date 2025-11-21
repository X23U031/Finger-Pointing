const cursor = document.getElementById("custom-cursor");
let lastTrailTime = 0;

// カーソル追従
document.addEventListener("mousemove", (e) => {
	// カーソルが存在するかチェックしてから動かす
	if (cursor) {
		cursor.style.left = e.clientX + "px";
		cursor.style.top = e.clientY + "px";
	}

	// 軌跡（トレイル）の処理
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

// ボタンのエフェクト
document.querySelectorAll("a, button").forEach(el => {
	if (!cursor) return;

	el.addEventListener("mouseenter", () => cursor.classList.add("active"));
	el.addEventListener("mouseleave", () => cursor.classList.remove("active"));

	// リンクのクリック処理
	if (el.tagName === 'A') {
		el.addEventListener("click", (event) => {
			event.preventDefault();
			const targetUrl = el.href;
			const ripple = document.createElement("div");
			ripple.className = "ripple";
			ripple.style.left = event.clientX + "px";
			ripple.style.top = event.clientY + "px";
			document.body.appendChild(ripple);
			setTimeout(() => ripple.remove(), 600);
			setTimeout(() => {
				if (targetUrl) window.location.href = targetUrl;
			}, 700);
		});
	}
});

// 何もないところをクリックした時の波紋
document.addEventListener("click", (e) => {
	if (e.target.closest("a, button")) return;
	const ripple = document.createElement("div");
	ripple.className = "ripple";
	ripple.style.left = e.clientX + "px";
	ripple.style.top = e.clientY + "px";
	document.body.appendChild(ripple);
	setTimeout(() => ripple.remove(), 600);
});