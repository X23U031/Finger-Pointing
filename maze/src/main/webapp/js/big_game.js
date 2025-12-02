// --- 1. DOM要素の取得 ---
const canvas = document.getElementById('maze-canvas');
const ctx = canvas.getContext('2d');
const timeLabel = document.getElementById('time-label');
const scoreLabel = document.getElementById('score-label');

// --- 2. タイムアタックモード用の設定 ---

const CELL_SIZE = 30;
const STROKE_WIDTH = 2;
const PLAYER_PIXEL_SPEED = 9.0;
const TIME_LIMIT_MS = 1200 * 1000;
const INITIAL_MAZE_WIDTH = 100;   // 大きい迷路
const INITIAL_MAZE_HEIGHT = 38;  // 大きい迷路

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
let elapsedTime = 0; // カウントアップ用の経過時間
let gameFinished = false;

let score = 0; // タイムアタックでは使わないが、他関数が参照するため残す
let difficultyLevel = 0;

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
	maze[0][1] = 0;
	maze[height - 1][width - 2] = 0;
	gridHeight = (height - 1) / 2;
	gridWidth = (width - 1) / 2;
}
function getPixelCenter(logicalCoord) {
	return ((logicalCoord - 1) / 2) * CELL_SIZE + (CELL_SIZE / 2.0);
}

function draw() {
	ctx.fillStyle = 'black';
	ctx.fillRect(0, 0, canvas.width, canvas.height);
	const offsetX = STROKE_WIDTH;
	const offsetY = STROKE_WIDTH;

	const startX = getPixelCenter(1) + offsetX;
	const startY = getPixelCenter(1) + offsetY;
	ctx.fillStyle = 'green';
	ctx.font = 'bold 20px Arial';
	ctx.textAlign = 'center';
	ctx.textBaseline = 'middle';
	ctx.fillText('S', startX, startY);

	const goalX = getPixelCenter(width - 2) + offsetX;
	const goalY = getPixelCenter(height - 2) + offsetY;
	ctx.fillStyle = 'cyan';
	ctx.font = 'bold 20px Arial';
	ctx.textAlign = 'center';
	ctx.textBaseline = 'middle';
	ctx.fillText('G', goalX, goalY);

	ctx.strokeStyle = 'white';
	ctx.lineWidth = STROKE_WIDTH;
	ctx.beginPath();
	for (let y = 0; y < height; y++) {
		for (let x = 0; x < width; x++) {
			if (maze[y][x] === 1) {
				const gridX = (x - 1) / 2;
				const gridY = (y - 1) / 2;
				if (y % 2 === 0 && x % 2 !== 0) {
					const px = gridX * CELL_SIZE + offsetX;
					const py = y / 2 * CELL_SIZE + offsetY;
					ctx.moveTo(px, py);
					ctx.lineTo(px + CELL_SIZE, py);
				} else if (y % 2 != 0 && x % 2 == 0) {
					const px = x / 2 * CELL_SIZE + offsetX;
					const py = gridY * CELL_SIZE + offsetY;
					ctx.moveTo(px, py);
					ctx.lineTo(px, py + CELL_SIZE);
				}
			}
		}
	}
	ctx.stroke();
	
	ctx.save();            
	ctx.globalAlpha = 0.5; 

	ctx.fillStyle = 'red';
	ctx.beginPath();
	ctx.arc(
		currentPixelX + offsetX,
		currentPixelY + offsetY,
		CELL_SIZE * 3,
		0, Math.PI * 2
	);
	ctx.fill();
	ctx.restore();
}

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

// タイムアップ時
function onTimeUp() {
	gameFinished = true;
	isAutoMoving = false;
	inputLock = true;
	clearInterval(animationTimerInterval);
	clearInterval(gameTimerInterval);
	console.log("時間切れです。サーブレットにスコアを保存します。");

	const formData = new URLSearchParams();
	formData.append('score', 0); // タイムアップはスコア0
	formData.append('mode', 'time_attack');

	fetch(`${CONTEXT_PATH}/SaveTimeAttackServlet`, {
		method: 'POST',
		headers: {
			'Content-Type': 'application/x-www-form-urlencoded',
		},
		body: formData
	})
		.catch(error => {
			console.error('スコアの保存に失敗しました:', error);
		})
		.finally(() => {
			// ★★★ 修正 ★★★
			window.location.href = `${CONTEXT_PATH}/jsp/big_result.jsp`; // bigTA結果画面へ
		});
}

// ゴール時の処理
function onGoal() {
	gameFinished = true;
	isAutoMoving = false;
	inputLock = true;
	clearInterval(animationTimerInterval);
	clearInterval(gameTimerInterval); // タイマーを止める
	console.log("ゴール！サーブレットにタイムを保存します。");

	let timeTaken = elapsedTime;

	scoreLabel.textContent = "クリア！";
	timeLabel.style.color = 'cyan';

	const formData = new URLSearchParams();
	formData.append('score', timeTaken); // スコアとして「経過時間」を送信
	formData.append('mode', 'time_attack');

	fetch(`${CONTEXT_PATH}/SaveTimeAttackServlet`, {
		method: 'POST',
		headers: {
			'Content-Type': 'application/x-www-form-urlencoded',
		},
		body: formData
	})
		.catch(error => {
			console.error('スコアの保存に失敗しました:', error);
		})
		.finally(() => {
			// ★★★ 修正 ★★★
			window.location.href = `${CONTEXT_PATH}/jsp/big_result.jsp`; // bigTA結果画面へ
		});
}


// カウントアップタイマー
function updateGameTimer() {
	if (gameFinished) return;

	elapsedTime = Date.now() - startTime;

	const minutes = Math.floor((elapsedTime / 1000) / 60);
	const seconds = Math.floor((elapsedTime / 1000) % 60);

	timeLabel.textContent = `${String(minutes).padStart(2, '0')} : ${String(seconds).padStart(2, '0')}`;

	if (elapsedTime >= TIME_LIMIT_MS) {
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

		if (logicalPlayerX == (width - 2) && logicalPlayerY == (height - 2)) {
			onGoal();
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
function canMoveInDirection(playerY, playerX, direction) {
	let wallY = playerY, wallX = playerX;
	switch (direction) {
		case 'w': wallY -= 1; break;
		case 's': wallY += 1; break;
		case 'a': wallX -= 1; break;
		case 'd': wallX += 1; break;
		default: return false;
	}

	if (playerY == (height - 2) && playerX == (width - 2) && direction == 's') {
		return isMovable(wallY, wallX);
	}
	if (playerY === 1 && playerX === 1 && direction === 'w') {
		return false;
	}

	if (wallY <= 0 || wallY >= height - 1 || wallX <= 0 || wallX >= width - 1) {
		return false;
	}

	return isMovable(wallY, wallX);
}


document.addEventListener('keydown', (e) => {
	if (gameFinished) return;

	if (e.key === 'Escape') {
		clearInterval(animationTimerInterval);
		clearInterval(gameTimerInterval);
		gameFinished = true;
		window.location.href = `${CONTEXT_PATH}/jsp/index.jsp`;
		return;
	}

	let key = e.key;
	if (key === 'ArrowUp') {
		key = 'w';
	} else if (key === 'ArrowDown') {
		key = 's';
	} else if (key === 'ArrowLeft') {
		key = 'a';
	} else if (key === 'ArrowRight') {
		key = 'd';
	} else {
		key = key.toLowerCase();
	}

	if (key === ' ') {
		if (isAutoMoving) {
			stopRequested = true;
			inputLock = true;
		}
		return;
	}
	if (inputLock) return;

	const isMoveKey = (key === 'w' || key === 'a' || key === 's' || key === 'd');
	if (!isMoveKey) return;

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
	scoreLabel.textContent = "（タイムアタック中）";
	timeLabel.textContent = "00 : 00";
	timeLabel.style.color = 'white';

	elapsedTime = 0;

	generateMaze(INITIAL_MAZE_WIDTH, INITIAL_MAZE_HEIGHT);
	updateCanvasDimensions();
	resetPlayerState();
	startTime = Date.now();
	gameTimerInterval = setInterval(updateGameTimer, 100);
	animationTimerInterval = setInterval(gameLoop, 5);
}
init();