package com.k1x.android.twitterlist.entities;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class UserInfo implements Parcelable {


	private static final long serialVersionUID = -4960712174937206448L;
	
	private boolean following;
	private int id;
	private int friends_count;
	private int favourites_count;
	private int followers_count;
	private String name;
	private String profile_image_url;
	private String screen_name;
	private String description;
	private String created_at;
	
	@Expose
	private Bitmap userBitmap;
	

	public UserInfo(Parcel in) {
		following = in.readInt() == 1;
		id = in.readInt();
		friends_count = in.readInt();
		favourites_count = in.readInt();
		followers_count = in.readInt();
		name = in.readString();
		profile_image_url = in.readString();
		screen_name = in.readString();
		description = in.readString();
		created_at = in.readString();	
		userBitmap = in.readParcelable(Bitmap.class.getClassLoader());
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
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

	public String getScreen_name() {
		return screen_name;
	}
	public void setScreen_name(String screen_name) {
		this.screen_name = screen_name;
	}
	
	public String getCreated_at() {
		return created_at;
	}
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isFollowing() {
		return following;
	}
	public void setFollowing(boolean following) {
		this.following = following;
	}
	public int getFavourites_count() {
		return favourites_count;
	}
	public void setFavourites_count(int favourites_count) {
		this.favourites_count = favourites_count;
	}
	public int getFollowers_count() {
		return followers_count;
	}
	public void setFollowers_count(int followers_count) {
		this.followers_count = followers_count;
	}
	public int getFriends_count() {
		return friends_count;
	}
	public void setFriends_count(int friends_count) {
		this.friends_count = friends_count;
	}
	
	public Bitmap getUserBitmap() {
		return userBitmap;
	}
	public void setUserBitmap(Bitmap userBitmap) {
		this.userBitmap = userBitmap;
	}

	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(isFollowing() ? 1 : 0);
		out.writeInt(id);
		out.writeInt(friends_count);
		out.writeInt(favourites_count);
		out.writeInt(followers_count);
		out.writeString(name);
		out.writeString(profile_image_url);
		out.writeString(screen_name);
		out.writeString(description);
		out.writeString(created_at);		
		out.writeParcelable(userBitmap, flags);
	}
	public static final Parcelable.Creator<UserInfo> CREATOR = new Parcelable.Creator<UserInfo>() {  
	    
        public UserInfo createFromParcel(Parcel in) {  
            return new UserInfo(in);  
        }  
   
        public UserInfo[] newArray(int size) {  
            return new UserInfo[size];  
        }  
          
    }; 
    

	@Override
	public String toString() {
		return String.format("id: %s\nname: %s\nprofile_image_url: %s", id, name,  profile_image_url);
	}
}
