package dto;

public class Reward {
	int rewardId;
	String rewardName;
	String rewardContent;

	// ★★★ ここから追加 ★★★
	/**
	 * この実績をユーザーが解除済みかどうか
	 * (DBのカラムではなく、Servletで動的にセットするための変数)
	 */
	boolean unlocked = false;

	public boolean isUnlocked() {
		return unlocked;
	}

	public void setUnlocked(boolean unlocked) {
		this.unlocked = unlocked;
	}
	// ★★★ ここまで追加 ★★★

	// (↓ 既存のgetter/setterはそのまま)
	public int getRewardId() {
		return rewardId;
	}

	public void setRewardId(int rewardId) {
		this.rewardId = rewardId;
	}

	public String getRewardName() {
		return rewardName;
	}

	public void setRewardName(String rewardName) {
		this.rewardName = rewardName;
	}

	public String getRewardContent() {
		return rewardContent;
	}

	public void setRewardContent(String rewardContent) {
		this.rewardContent = rewardContent;
	}

}