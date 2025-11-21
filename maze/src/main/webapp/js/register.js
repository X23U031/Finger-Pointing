// パスワード表示切り替え機能
document.querySelectorAll('.toggle-password').forEach(button => {
	button.addEventListener('click', () => {
		const targetId = button.dataset.target;
		const passwordInput = document.getElementById(targetId);

		if (passwordInput.type === 'password') {
			passwordInput.type = 'text';
			button.classList.add('is-visible');
		} else {
			passwordInput.type = 'password';
			button.classList.remove('is-visible');
		}
	});
});

// アカウント作成フォームの処理
const accountForm = document.querySelector('.account-form');

accountForm.addEventListener('submit', (event) => {
	// フォーム送信を「待機」させる
	event.preventDefault();

	const username = document.getElementById('username').value;
	const password = document.getElementById('password').value;
	const confirmPassword = document.getElementById('confirm-password').value;

	// ユーザー名、パスワードが未入力の場合
	if (!username || !password || !confirmPassword) {
		alert('すべての項目を入力してください');
		return; // 送信を中止
	}

	// パスワードと確認用パスワードが一致しない場合
	if (password !== confirmPassword) {
		alert('パスワードが一致しません');
		return; // 送信を中止
	}

	// バリデーションを通過した場合、フォームの送信を再開
	// (RegisterServlet にデータが送信されます)
	event.target.submit();
});