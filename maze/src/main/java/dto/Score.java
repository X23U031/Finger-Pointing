package dto;

import java.sql.Timestamp;

public class Score {
	int scoreId;
	int userId;
	int score;
	Timestamp playedAt;
	int ctScore;
	int gameMode;

	public int getGameMode() {
		return gameMode;
	}

	public void setGameMode(int gameMode) {
		this.gameMode = gameMode;
	}

	public int getScoreId() {
		return scoreId;
	}

	public void setScoreId(int scoreId) {
		this.scoreId = scoreId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public Timestamp getPlayedAt() {
		return playedAt;
	}

	public void setPlayedAt(Timestamp playedAt) {
		this.playedAt = playedAt;
	}

	public int getCtScore() {
		return ctScore;
	}

	public void setCtScore(int ctScore) {
		this.ctScore = ctScore;
	}
}