package com.k1x.android.twitterlist.jsonobj;

public class UserInfo {
	
	private String id;
	private String name;
	private String profile_image_url;
	private String url;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProfile_image_url() {
		return profile_image_url;
	}
	public void setProfile_image_url(String profile_image_url) {
		this.profile_image_url = profile_image_url;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	@Override
	public String toString() {
		return String.format("id: %s\nname: %s\nprofile_image_url: %s\nurl: %s", id, name, url, profile_image_url);
	}

}
