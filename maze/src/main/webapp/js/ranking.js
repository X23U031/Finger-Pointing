const cursor = document.getElementById("custom-cursor");
let lastTrailTime = 0;
const NAME_CHAR_LIMIT = 22;
const SCROLL_SPEED_PX_PER_SEC = 50;

document.addEventListener("mousemove", (e) => {
	if (cursor) {
		cursor.style.left = e.clientX + "px";
		cursor.style.top = e.clientY + "px";
	}
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
	if (!cursor) return;
	el.addEventListener("mouseenter", () => cursor.classList.add("active"));
	el.addEventListener("mouseleave", () => cursor.classList.remove("active"));
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

document.addEventListener("click", (e) => {
	if (e.target.closest("a, button, input")) return;
	const ripple = document.createElement("div");
	ripple.className = "ripple";
	ripple.style.left = e.clientX + "px";
	ripple.style.top = e.clientY + "px";
	document.body.appendChild(ripple);
	setTimeout(() => ripple.remove(), 600);
});

function setupScrollingAnimation(cell, charLimit) {
	const wrapper = cell.querySelector('.scrolling-wrapper');
	const textSpan = wrapper.querySelector('.scrolling-text');
	if (!wrapper || !textSpan) return;
	const text = textSpan.textContent || textSpan.innerText;
	if (text.length <= charLimit) {
		textSpan.style.transition = 'none';
		textSpan.style.transform = 'translateX(0)';
		if (textSpan.animationLoop) {
			clearTimeout(textSpan.animationLoop);
			delete textSpan.animationLoop;
		}
		return;
	}
	const cellWidth = wrapper.clientWidth;
	textSpan.style.transition = 'none';
	textSpan.style.transform = 'translateX(0)';
	const textWidth = textSpan.scrollWidth;
	if (textWidth > cellWidth + 5) {
		const scrollDistance = cellWidth - textWidth;
		const moveTime = (textWidth - cellWidth) / SCROLL_SPEED_PX_PER_SEC;
		if (textSpan.animationLoop) clearTimeout(textSpan.animationLoop);
		const startAnimationLoop = () => {
			textSpan.style.transition = 'none';
			textSpan.style.transform = 'translateX(0)';
			textSpan.animationLoop = setTimeout(() => {
				textSpan.style.transition = `transform ${moveTime}s linear`;
				textSpan.style.transform = `translateX(${scrollDistance}px)`;
				textSpan.animationLoop = setTimeout(() => {
					textSpan.style.transition = 'none';
					textSpan.animationLoop = setTimeout(() => {
						startAnimationLoop();
					}, 1000);
				}, moveTime * 1000);
			}, 1000);
		};
		startAnimationLoop();
	} else {
		textSpan.style.transition = 'none';
		textSpan.style.transform = 'translateX(0)';
		if (textSpan.animationLoop) {
			clearTimeout(textSpan.animationLoop);
			delete textSpan.animationLoop;
		}
	}
}
// ★ fetchRanking 関数と DOMContentLoaded の呼び出しは削除しました