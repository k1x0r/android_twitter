package com.k1x.android.twitterlist.entities;


import android.os.Parcel;
import android.os.Parcelable;

public class TweetData implements Parcelable {


	private UserInfo user;
	private TweetData retweeted_status;
	private String text;
	private String source;
    private String created_at;

    public TweetData(Parcel in) {
    	user = in.readParcelable(UserInfo.class.getClassLoader());
    	retweeted_status = in.readParcelable(TweetData.class.getClassLoader());
		text = in.readString();
		source = in.readString();
	    created_at = in.readString();	
	}
    
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
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(user, flags);
		dest.writeParcelable(retweeted_status, flags);
		dest.writeString(text);
		dest.writeString(source);
	    dest.writeString(created_at);		
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{").append(user.getName()).append(" ").append(text).append("}");
		return sb.toString();
	}
	
	public static final Parcelable.Creator<TweetData> CREATOR = new Parcelable.Creator<TweetData>() {  
	    
        public TweetData createFromParcel(Parcel in) {  
            return new TweetData(in);  
        }  
   
        public TweetData[] newArray(int size) {  
            return new TweetData[size];  
        }  
          
    }; 

	
}