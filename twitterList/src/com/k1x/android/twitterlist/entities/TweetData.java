package com.k1x.android.twitterlist.entities;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class TweetData implements Serializable {

	private static final long serialVersionUID = 6129274701693473510L;

	private UserInfo user;
	private TweetData retweeted_status;
	private String text;
	private String source;
    private String created_at;

    public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
    public UserInfo getUser() {
		return user;
	}
	public void setUser(UserInfo user) {
		this.user = user;
	}
	public TweetData getRetweeted_status() {
		return retweeted_status;
	}
	public void setRetweeted_status(TweetData retweeted_status) {
		this.retweeted_status = retweeted_status;
	}
	public String getCreatedAt() {
		return created_at;
	}
	public void setCreatedAt(String created_at) {
		this.created_at = created_at;
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
