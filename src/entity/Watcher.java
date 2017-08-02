package entity;

import java.sql.Timestamp;

public class Watcher {
	private int repo_id;
	private int user_id;
	private String created_at;
	public int getRepo_id() {
		return repo_id;
	}
	public void setRepo_id(int repoId) {
		repo_id = repoId;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int userId) {
		user_id = userId;
	}
	public String getCreated_at() {
		return created_at;
	}
	public void setCreated_at(String createdAt) {
		created_at = createdAt;
	}
	public void printWatcher(){
		System.out.println(getRepo_id()+","+getUser_id()+","+getCreated_at());
	}
}
