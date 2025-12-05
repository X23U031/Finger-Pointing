// localStorageの読み込み処理は削除しました

const cursor = document.getElementById("custom-cursor");
let lastTrailTime = 0;

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

document.querySelectorAll("a, button").forEach(el => {
	el.addEventListener("mouseenter", () => cursor.classList.add("active"));
	el.addEventListener("mouseleave", () => cursor.classList.remove("active"));
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
});

document.addEventListener("click", (e) => {
	if (e.target.closest("a, button")) return;
	const ripple = document.createElement("div");
	ripple.className = "ripple";
	ripple.style.left = e.clientX + "px";
	ripple.style.top = e.clientY + "px";
	document.body.appendChild(ripple);
	setTimeout(() => ripple.remove(), 600);
});