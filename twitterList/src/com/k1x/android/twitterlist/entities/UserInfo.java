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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((created_at == null) ? 0 : created_at.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + favourites_count;
		result = prime * result + followers_count;
		result = prime * result + (following ? 1231 : 1237);
		result = prime * result + friends_count;
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime
				* result
				+ ((profile_image_url == null) ? 0 : profile_image_url
						.hashCode());
		result = prime * result
				+ ((screen_name == null) ? 0 : screen_name.hashCode());
		result = prime * result
				+ ((userBitmap == null) ? 0 : userBitmap.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserInfo other = (UserInfo) obj;
		if (created_at == null) {
			if (other.created_at != null)
				return false;
		} else if (!created_at.equals(other.created_at))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (favourites_count != other.favourites_count)
			return false;
		if (followers_count != other.followers_count)
			return false;
		if (following != other.following)
			return false;
		if (friends_count != other.friends_count)
			return false;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (profile_image_url == null) {
			if (other.profile_image_url != null)
				return false;
		} else if (!profile_image_url.equals(other.profile_image_url))
			return false;
		if (screen_name == null) {
			if (other.screen_name != null)
				return false;
		} else if (!screen_name.equals(other.screen_name))
			return false;
		if (userBitmap == null) {
			if (other.userBitmap != null)
				return false;
		} else if (!userBitmap.equals(other.userBitmap))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return String.format("id: %s\nname: %s\nprofile_image_url: %s", id, name,  profile_image_url);
	}
}
