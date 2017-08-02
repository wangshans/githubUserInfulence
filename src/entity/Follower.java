package entity;

public class Follower {
	public int user_id;
	public int follower_id;
	public String crated_at;
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int userId) {
		user_id = userId;
	}
	public int getFollower_id() {
		return follower_id;
	}
	public void setFollower_id(int followerId) {
		follower_id = followerId;
	}
	public String getCrated_at() {
		return crated_at;
	}
	public void setCrated_at(String cratedAt) {
		crated_at = cratedAt;
	}
	
}
