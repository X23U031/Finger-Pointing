const cursor = document.getElementById("custom-cursor");
let lastTrailTime = 0;

const NAME_CHAR_LIMIT = 22;
const CONTENT_CHAR_LIMIT = 55;
const SCROLL_SPEED_PX_PER_SEC = 50;

document.addEventListener("mousemove", (e) => {
	// ... (カーソル操作のコードはそのまま残す) ...
	if (cursor) {
		cursor.style.left = e.clientX + "px";
		cursor.style.top = e.clientY + "px";
	}
	// ... (省略) ...
});

// カーソルエフェクト (流用)
document.querySelectorAll("a, button").forEach(el => {
	// ... (カーソル操作のコードはそのまま残す) ...
});

// クリック波紋 (流用)
document.addEventListener("click", (e) => {
	// ... (カーソル操作のコードはそのまま残す) ...
});

// スクロールアニメーション (流用)
// ★ この関数はJSPから呼び出すために残します ★
function setupScrollingAnimation(cell, charLimit) {
	// ... (関数の中身はそのまま残す) ...
	const wrapper = cell.querySelector('.scrolling-wrapper');
	// ... (省略) ...
}