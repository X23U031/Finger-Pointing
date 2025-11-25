/* ============================================================
   裏モード解禁 ロジック（index.jsp から完全分離）
   ============================================================ */

// ▼ 遷移先（自由に変更可）
const NEXT_PAGE_URL = "../jsp/mode_secret.jsp";

// DOM が読み込まれたら隠しボタンにイベントを付与
window.addEventListener("DOMContentLoaded", () => {

	const btn = document.getElementById("secretModeBtn");

	// カーソル要素を取得
	const cursor = document.getElementById("custom-cursor");

	// ★ 設定：元のサイズと、1回で大きくなる量
	const baseCursorSize = 20;   // 元の大きさ (20px)
	const growthPerClick = 15;   // ★ 1回で 15px ずつ大きくするわ（かなり目立つはずよ）

	if (!btn) return;

	let clickCount = 0;
	const requiredClicks = 10;

	// ボタン押下イベント
	btn.addEventListener("click", () => {

		clickCount++;

		// ★ 計算したサイズ
		const newSize = baseCursorSize + (clickCount * growthPerClick);

		// コンソールに、今の状況を表示させるわ（F12キーのConsoleで確認できるのよ）
		console.log(`★クリック${clickCount}回目: サイズを ${newSize}px にしようとしています`);

		// カーソルのサイズ変更処理
		if (cursor) {
			if (clickCount <= requiredClicks) {
				cursor.style.width = newSize + "px";
				cursor.style.height = newSize + "px";
			}
		} else {
			console.error("★エラー: 'custom-cursor' というIDの要素が見つかりません！");
		}

		// 解禁処理
		if (clickCount >= requiredClicks) {
			console.log("★ 解禁条件達成！");
			unlockSecretMode();

			// カウンターリセット
			clickCount = 0;

			// カーソルのサイズを元に戻す
			if (cursor) {
				console.log("★ カーソルサイズをリセットします");
				cursor.style.width = baseCursorSize + "px";
				cursor.style.height = baseCursorSize + "px";
			}
		}
	});

});


/**
 * ▼ SweetAlert2 による解禁アラート
 * ※ OK で裏モード画面へ遷移
 */
function unlockSecretMode() {

	Swal.fire({
		title: 'モード解禁',
		text: '新しいモードが解禁されました。',
		icon: 'success',

		background: '#000',
		color: '#fff',

		customClass: {
			popup: 'unlock-popup',
			title: 'unlock-title',
			htmlContainer: 'unlock-text',
			confirmButton: 'unlock-button',
			cancelButton: 'unlock-cancel-button'
		},

		showCancelButton: true,
		confirmButtonText: 'OK',
		cancelButtonText: 'キャンセル',

		reverseButtons: true,  // 左：キャンセル / 右：OK にしたい場合
		allowOutsideClick: false
	}).then((result) => {

		if (result.isConfirmed) {
			// OK → 次画面へ遷移
			window.location.href = NEXT_PAGE_URL;

		} else if (result.isDismissed) {
			// キャンセル → 何もせず元の画面に留まる
			console.log("操作キャンセル");
		}

	});
}
