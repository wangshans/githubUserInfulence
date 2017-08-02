package entity;

import java.sql.Timestamp;


public class Project {
	private int id;
	private String url;
	private int ower_id;
	private String name;
	private String description;
	private String language;
	private String created_at;
	private int forked_from;
	private short deleted;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getOwer_id() {
		return ower_id;
	}
	public void setOwer_id(int owerId) {
		ower_id = owerId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getCreated_at() {
		return created_at;
	}
	public void setCreated_at(String createdAt) {
		created_at = createdAt;
	}
	public int getForked_from() {
		return forked_from;
	}
	public void setForked_from(int forkedFrom) {
		forked_from = forkedFrom;
	}
	public short getDeleted() {
		return deleted;
	}
	public void setDeleted(short deleted) {
		this.deleted = deleted;
	}
	public void printProject(){
		System.out.print(getId()+","+getUrl()+","+getOwer_id()+","+getName()+",");
		System.out.print(getDescription()+","+getLanguage()+","+getCreated_at()+",");
		System.out.println(getForked_from()+","+getDeleted());
	}
}
