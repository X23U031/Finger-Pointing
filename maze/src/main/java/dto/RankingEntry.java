package dto;

// Score.java や User.java とは別の、ランキング表示専用のDTO
public class RankingEntry {

	private String userName; // userテーブルのuser_name
	private int score; // scoreテーブルのscore
	private int userId; // userテーブルのuser_id

	// --- 以下、getter/setter ---

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public void setUser_id(int int1) {
		// TODO 自動生成されたメソッド・スタブ
		
	}
}