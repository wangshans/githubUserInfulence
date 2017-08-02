package entity;

import java.util.Calendar;

public class User {
	private int id;
	private String login;
	private String name;
	private String company;
	private String location;
	private String email;
	private String created_at;
	private String type;
	private boolean fake;
	private boolean deleted;
	
	private double pr;  //PR值
	
	public double getPr() {
		return pr;
	}
	public void setPr(double pr) {
		this.pr = pr;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
//	public Calendar getCreated_at() {
//		return created_at;
//	}
//	public void setCreated_at(Calendar createdAt) {
//		created_at = createdAt;
//	}
	
	public String getType() {
		return type;
	}
	public String getCreated_at() {
		return created_at;
	}
	public void setCreated_at(String createdAt) {
		created_at = createdAt;
	}
	public void setType(String type) {
		this.type = type;
	}
	public boolean isFake() {
		return fake;
	}
	public void setFake(boolean fake) {
		this.fake = fake;
	}
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
}
