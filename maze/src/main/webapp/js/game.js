// --- 1. DOM要素の取得 ---
const canvas = document.getElementById('maze-canvas');
const ctx = canvas.getContext('2d');
const timeLabel = document.getElementById('time-label');
const scoreLabel = document.getElementById('score-label');

// --- 2. Javaのフィールド（変数）を移植 ---

const CELL_SIZE = 30;
const STROKE_WIDTH = 2;
const PLAYER_PIXEL_SPEED = 9.0;
const TIME_LIMIT_MS = 180 * 1000;
const INITIAL_MAZE_WIDTH = 20;
const INITIAL_MAZE_HEIGHT = 10;

let maze = [];
let height = 0, width = 0;
let gridHeight = 0, gridWidth = 0;

let logicalPlayerX = 1, logicalPlayerY = 1;
let currentPixelX = 0, currentPixelY = 0;
let targetPixelX = 0, targetPixelY = 0;
let currentDirection = null;
let nextDirection = null;
let isAutoMoving = false;
let inputLock = false;
let stopRequested = false;

let startTime = 0;
let remainingTime = TIME_LIMIT_MS;
let gameFinished = false;
let difficultyLevel = 0;
let score = 0;

let gameTimerInterval;
let animationTimerInterval;


function generateMaze(w, h) {
	width = (w % 2 === 0) ? w + 1 : w;
	height = (h % 2 === 0) ? h + 1 : h;
	maze = Array(height).fill(null).map(() => Array(width).fill(1));
	const stack = [];
	const directions = [[-1, 0], [1, 0], [0, -1], [0, 1]];
	const startX = 1, startY = 1;
	stack.push([startY, startX]);
	maze[startY][startX] = 0;
	while (stack.length > 0) {
		const [y, x] = stack[stack.length - 1];
		directions.sort(() => Math.random() - 0.5);
		let foundNext = false;
		for (const [dy, dx] of directions) {
			const ny = y + dy * 2;
			const nx = x + dx * 2;
			if (nx > 0 && nx < width - 1 && ny > 0 && ny < height - 1 && maze[ny][nx] === 1) {
				maze[y + dy][x + dx] = 0;
				maze[ny][nx] = 0;
				stack.push([ny, nx]);
				foundNext = true;
				break;
			}
		}
		if (!foundNext) {
			stack.pop();
		}
	}
	maze[0][1] = 0; // スタートマスの上の壁を開ける
	maze[height - 1][width - 2] = 0; // ゴールマスの下の壁を開ける
	gridHeight = (height - 1) / 2;
	gridWidth = (width - 1) / 2;
}
function getPixelCenter(logicalCoord) {
	return ((logicalCoord - 1) / 2) * CELL_SIZE + (CELL_SIZE / 2.0);
}

//
// ★★★ draw() 関数 (変更なし) ★★★
//
function draw() {
	ctx.fillStyle = 'black';
	ctx.fillRect(0, 0, canvas.width, canvas.height);
	const offsetX = STROKE_WIDTH;
	const offsetY = STROKE_WIDTH;

	// --- 1. スタート地点「S」の描画 ---
	const startX = getPixelCenter(1) + offsetX; // 論理X=1の中心
	const startY = getPixelCenter(1) + offsetY; // 論理Y=1の中心
	ctx.fillStyle = 'green'; // S の色
	ctx.font = 'bold 20px Arial'; // S のフォント
	ctx.textAlign = 'center';
	ctx.textBaseline = 'middle';
	ctx.fillText('S', startX, startY);

	// --- 2. ゴール地点「G」の描画 ---
	const goalX = getPixelCenter(width - 2) + offsetX; // 論理X=最後(width-2)の中心
	const goalY = getPixelCenter(height - 2) + offsetY; // 論理Y=最後(height-2)の中心
	ctx.fillStyle = 'cyan'; // G の色
	ctx.font = 'bold 20px Arial'; // G のフォント
	ctx.textAlign = 'center';
	ctx.textBaseline = 'middle';
	ctx.fillText('G', goalX, goalY);

	// --- 3. 迷路の壁の描画 (変更なし) ---
	ctx.strokeStyle = 'white';
	ctx.lineWidth = STROKE_WIDTH;
	ctx.beginPath();
	for (let y = 0; y < height; y++) {
		for (let x = 0; x < width; x++) {
			if (maze[y][x] === 1) {
				const gridX = (x - 1) / 2;
				const gridY = (y - 1) / 2;
				if (y % 2 === 0 && x % 2 !== 0) { // 水平な壁
					const px = gridX * CELL_SIZE + offsetX;
					const py = y / 2 * CELL_SIZE + offsetY;
					ctx.moveTo(px, py);
					ctx.lineTo(px + CELL_SIZE, py);
				} else if (y % 2 != 0 && x % 2 == 0) { // 垂直な壁
					const px = x / 2 * CELL_SIZE + offsetX;
					const py = gridY * CELL_SIZE + offsetY;
					ctx.moveTo(px, py);
					ctx.lineTo(px, py + CELL_SIZE);
				}
			}
		}
	}
	ctx.stroke();

	// --- 4. プレイヤーの描画 (変更なし) ---
	ctx.fillStyle = 'red';
	ctx.beginPath();
	ctx.arc(
		currentPixelX + offsetX,
		currentPixelY + offsetY,
		CELL_SIZE / 3,
		0, Math.PI * 2
	);
	ctx.fill();
}
//
// ★★★ draw() 関数の修正ここまで ★★★
//

function updateCanvasDimensions() {
	const panelWidth = gridWidth * CELL_SIZE + (STROKE_WIDTH * 2);
	const panelHeight = gridHeight * CELL_SIZE + (STROKE_WIDTH * 2);
	canvas.width = panelWidth;
	canvas.height = panelHeight;
}
function resetPlayerState() {
	logicalPlayerX = 1;
	logicalPlayerY = 1;
	currentPixelX = getPixelCenter(logicalPlayerX);
	currentPixelY = getPixelCenter(logicalPlayerY);
	targetPixelX = currentPixelX;
	targetPixelY = currentPixelY;
	currentDirection = null;
	nextDirection = null;
	isAutoMoving = false;
	inputLock = false;
	stopRequested = false;
}

// タイムアップ時 (変更なし)
function onTimeUp() {
	gameFinished = true;
	isAutoMoving = false;
	inputLock = true;
	clearInterval(animationTimerInterval);
	clearInterval(gameTimerInterval);
	console.log("時間切れです。サーブレットにスコアを保存します。");

	// 1. フォームデータを作成
	const formData = new URLSearchParams();
	formData.append('score', score); // 'score'という名前で、スコア変数をセット

	// 2. fetch API を使って SaveScoreServlet を呼び出す
	//    (game.jspで定義した CONTEXT_PATH を使う)
	fetch(`${CONTEXT_PATH}/SaveScoreServlet`, {
		method: 'POST',
		headers: {
			'Content-Type': 'application/x-www-form-urlencoded',
		},
		body: formData
	})
		.catch(error => {
			// 送信に失敗しても、とりあえず結果画面には遷移する
			console.error('スコアの保存に失敗しました:', error);
		})
		.finally(() => {
			// 3. Servletの処理が終わったら（成功しても失敗しても）、結果画面に遷移する
			window.location.href = `${CONTEXT_PATH}/jsp/result.jsp`;
		});
}


function startNewLevel() {
	gameFinished = true;
	isAutoMoving = false;
	score++;
	scoreLabel.textContent = "到達数 : " + score;
	if (score % 3 === 0) {
		difficultyLevel = score / 3;
	}
	const newWidth = INITIAL_MAZE_WIDTH + (difficultyLevel * 4);
	const newHeight = INITIAL_MAZE_HEIGHT + (difficultyLevel * 2);
	generateMaze(newWidth, newHeight);
	updateCanvasDimensions();
	resetPlayerState();
	gameFinished = false;
}
function updateGameTimer() {
	if (gameFinished) return;
	const currentElapsed = Date.now() - startTime;
	remainingTime = TIME_LIMIT_MS - currentElapsed;
	if (remainingTime < 0) remainingTime = 0;
	const minutes = Math.floor((remainingTime / 1000) / 60);
	const seconds = Math.floor((remainingTime / 1000) % 60);
	timeLabel.textContent = `${String(minutes).padStart(2, '0')} : ${String(seconds).padStart(2, '0')}`;
	if (remainingTime <= 0) {
		timeLabel.style.color = 'red';
		timeLabel.textContent = "TIME UP";
		onTimeUp();
	}
}
function gameLoop() {
	if (gameFinished || !isAutoMoving) {
		draw();
		return;
	}
	const atTargetX = Math.abs(currentPixelX - targetPixelX) < 1.0;
	const atTargetY = Math.abs(currentPixelY - targetPixelY) < 1.0;
	if (atTargetX && atTargetY) {
		inputLock = false;
		currentPixelX = targetPixelX;
		currentPixelY = targetPixelY;
		logicalPlayerX = Math.round(((currentPixelX - (CELL_SIZE / 2.0)) / CELL_SIZE) * 2) + 1;
		logicalPlayerY = Math.round(((currentPixelY - (CELL_SIZE / 2.0)) / CELL_SIZE) * 2) + 1;

		// ★ ゴール判定 (変更なし)
		if (logicalPlayerX == (width - 2) && logicalPlayerY == (height - 2)) {
			gameFinished = true;
			isAutoMoving = false;

			timeLabel.style.color = 'cyan';
			startNewLevel();
			return;
		}

		if (stopRequested) {
			isAutoMoving = false;
			currentDirection = null;
			stopRequested = false;
			return;
		}
		if (nextDirection && canMoveInDirection(logicalPlayerY, logicalPlayerX, nextDirection)) {
			currentDirection = nextDirection;
			nextDirection = null;
		}
		else if (!canMoveInDirection(logicalPlayerY, logicalPlayerX, currentDirection)) {
			isAutoMoving = false;
			currentDirection = null;
			return;
		}
		if (isAutoMoving) {
			setNextTargetCell();
			inputLock = true;
		}
	} else {
		switch (currentDirection) {
			case 'w': currentPixelY = Math.max(targetPixelY, currentPixelY - PLAYER_PIXEL_SPEED); break;
			case 's': currentPixelY = Math.min(targetPixelY, currentPixelY + PLAYER_PIXEL_SPEED); break;
			case 'a': currentPixelX = Math.max(targetPixelX, currentPixelX - PLAYER_PIXEL_SPEED); break;
			case 'd': currentPixelX = Math.min(targetPixelX, currentPixelX + PLAYER_PIXEL_SPEED); break;
		}
	}
	draw();
}
function setNextTargetCell() {
	targetPixelX = getPixelCenter(logicalPlayerX);
	targetPixelY = getPixelCenter(logicalPlayerY);
	switch (currentDirection) {
		case 'w': targetPixelY -= CELL_SIZE; break;
		case 's': targetPixelY += CELL_SIZE; break;
		case 'a': targetPixelX -= CELL_SIZE; break;
		case 'd': targetPixelX += CELL_SIZE; break;
	}
}
function isMovable(wallY, wallX) {
	if (wallX < 0 || wallX >= width || wallY < 0 || wallY >= height) {
		return false;
	}
	return maze[wallY][wallX] === 0;
}

//
// ★★★ ここから canMoveInDirection() 関数を修正 ★★★
//
function canMoveInDirection(playerY, playerX, direction) {
	let wallY = playerY, wallX = playerX;
	switch (direction) {
		case 'w': wallY -= 1; break;
		case 's': wallY += 1; break;
		case 'a': wallX -= 1; break;
		case 'd': wallX += 1; break;
		default: return false;
	}

	// ★ ゴール判定 (変更なし)
	if (playerY == (height - 2) && playerX == (width - 2) && direction == 's') {
		// ゴールマス(height-2, width-2) から下 (height-1, width-2) へは行ける
		return isMovable(wallY, wallX);
	}

	// ★★★ [修正] スタート地点(1, 1)から上('w')には行けないようにする ★★★
	if (playerY === 1 && playerX === 1 && direction === 'w') {
		// スタートマス(1, 1) から上 (0, 1) へは行けない (場外)
		return false; // ← isMovable(wallY, wallX) から false に変更
	}
	// ★★★ 修正ここまで ★★★


	// 壁の外に出ようとしていないかチェック (変更なし)
	if (wallY <= 0 || wallY >= height - 1 || wallX <= 0 || wallX >= width - 1) {
		return false;
	}

	return isMovable(wallY, wallX);
}


//
// ★★★ keydown イベントリスナー (変更なし) ★★★
//
document.addEventListener('keydown', (e) => {
	if (gameFinished) return;

	// ESCキーの処理
	if (e.key === 'Escape') {
		// すべてのタイマーを停止
		clearInterval(animationTimerInterval);
		clearInterval(gameTimerInterval);
		gameFinished = true; // ゲームを停止状態にする

		// (game.jspで定義した CONTEXT_PATH を使ってタイトルに戻る)
		window.location.href = `${CONTEXT_PATH}/jsp/index.jsp`;
		return; // 他のキー処理をしない
	}

	// 1. Get the raw key. Don't convert to lowercase yet.
	let key = e.key;

	// 2. Map arrow keys to WASD characters
	if (key === 'ArrowUp') {
		key = 'w';
	} else if (key === 'ArrowDown') {
		key = 's';
	} else if (key === 'ArrowLeft') {
		key = 'a';
	} else if (key === 'ArrowRight') {
		key = 'd';
	} else {
		// If not an arrow key, convert to lowercase for WASD
		key = key.toLowerCase();
	}

	// 3. Continue with existing logic
	if (key === ' ') {
		if (isAutoMoving) {
			stopRequested = true;
			inputLock = true;
		}
		return;
	}
	if (inputLock) return;

	// 4. Check if the (now mapped) key is a move key
	const isMoveKey = (key === 'w' || key === 'a' || key === 's' || key === 'd');
	if (!isMoveKey) return;

	// 5. Rest of logic is unchanged
	if (!isAutoMoving) {
		if (canMoveInDirection(logicalPlayerY, logicalPlayerX, key)) {
			currentDirection = key;
			isAutoMoving = true;
			setNextTargetCell();
			inputLock = true;
		}
	} else {
		if ((key === 'w' && currentDirection === 's') ||
			(key === 's' && currentDirection === 'w') ||
			(key === 'a' && currentDirection === 'd') ||
			(key === 'd' && currentDirection === 'a')) {
			currentDirection = key;
			nextDirection = null;
			targetPixelX = currentPixelX + (currentPixelX - targetPixelX);
			targetPixelY = currentPixelY + (currentPixelY - targetPixelY);
			inputLock = true;
		}
		else {
			nextDirection = key;
			inputLock = true;
		}
	}
});


function init() {
	scoreLabel.textContent = "到達数 : 0";
	timeLabel.textContent = "01: 30"; // ← 90秒
	timeLabel.style.color = 'white';

	// ★ 制限時間を TIME_LIMIT_MS (180秒) に合わせる
	remainingTime = TIME_LIMIT_MS;
	const minutes = Math.floor((remainingTime / 1000) / 60);
	const seconds = Math.floor((remainingTime / 1000) % 60);
	timeLabel.textContent = `${String(minutes).padStart(2, '0')} : ${String(seconds).padStart(2, '0')}`;


	generateMaze(INITIAL_MAZE_WIDTH, INITIAL_MAZE_HEIGHT);
	updateCanvasDimensions();
	resetPlayerState();
	startTime = Date.now();
	gameTimerInterval = setInterval(updateGameTimer, 100);
	animationTimerInterval = setInterval(gameLoop, 5);
}
init();