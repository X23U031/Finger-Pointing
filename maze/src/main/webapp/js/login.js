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