package dto;

import java.sql.Timestamp;

public class UserReward {
	int achievementId;
	int userId;
	int rewardId;
	Timestamp achievedAt;

	public int getAchievementId() {
		return achievementId;
	}

	public void setAchievementId(int achievementId) {
		this.achievementId = achievementId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getRewardId() {
		return rewardId;
	}

	public void setRewardId(int rewardId) {
		this.rewardId = rewardId;
	}

	public Timestamp getAchievedAt() {
		return achievedAt;
	}

	public void setAchievedAt(Timestamp achievedAt) {
		this.achievedAt = achievedAt;
	}
}