package com.k1x.android.twitterlist.entities;


public class TweetData {

	public class User
	{

		private String name;
		private String screen_name;
		private String profile_image_url;
		
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
		public String getProfile_image_url() {
			return profile_image_url;
		}
		public void setProfile_image_url(String profile_image_url) {
			this.profile_image_url = profile_image_url;
		}
	}
	
	private User user;
	private TweetData retweeted_status;
	private String text;
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
    public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public TweetData getRetweeted_status() {
		return retweeted_status;
	}
	public void setRetweeted_status(TweetData retweeted_status) {
		this.retweeted_status = retweeted_status;
	}

	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{").append(user.getName()).append(" ").append(text).append("}");
		return sb.toString();
	}
	
}
