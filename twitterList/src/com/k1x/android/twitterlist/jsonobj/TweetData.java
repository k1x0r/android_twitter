package com.k1x.android.twitterlist.jsonobj;

import com.google.gson.annotations.SerializedName;

public class TweetData {

	public class User
	{

		private String name;
		private String screen_name;
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getScreen_name() {
			return screen_name;
		}
		public void setScreen_name(String screen_name) {
			this.screen_name = screen_name;
		}
		
		@Override
		public String toString() {
			return name;
		}
	}
	
	private User user;
	
    public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

	@SerializedName("text")
	private String text;
    @SerializedName("source")
	private String source;
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}

	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{").append(user.getName()).append(" ").append(text).append("}");
		return sb.toString();
	}
	
	
	
}
