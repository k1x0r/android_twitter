package com.k1x.android.twitterlist;

import java.util.LinkedList;

import com.k1x.android.twitterlist.entities.TweetData;
import com.k1x.android.twitterlist.entities.UserInfo;
import com.k1x.android.twitterlist.twitterutil.TweeterAPI;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class TwitterListApplication extends Application {
	
    private static final String SECRET_TOKEN_KEY = "secretToken";
    private static final String ACCESS_TOKEN_KEY = "accessToken";

    private static Context context;

    private String secretToken;
    private String accessToken;

    private LinkedList<UserInfo> userList = null;
    private LinkedList<TweetData> tweetList = null;

    private SharedPreferences prefs;
	private Editor prefsEditor;
	private UserInfo userProfile;
	private TweeterAPI twitterAPI;
    
	private boolean started = false;

	@Override
	public void onCreate() {
		super.onCreate();
        context=getApplicationContext();
		loadPrefs();
	}

	private void loadPrefs()
	{
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefsEditor = prefs.edit();
		secretToken = prefs.getString(SECRET_TOKEN_KEY, "0");
		accessToken = prefs.getString(ACCESS_TOKEN_KEY, "0");
		System.out.println(String.format("Access Token: %s\nSecret Token: %s\n",
				accessToken, secretToken));
	}
	
	public String getSecretToken() {
		return secretToken;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
		prefsEditor.putString(ACCESS_TOKEN_KEY, accessToken);
		prefsEditor.commit();
	}

	public void setSecretToken(String secretToken) {
		this.secretToken = secretToken;
		prefsEditor.putString(SECRET_TOKEN_KEY, secretToken);
		prefsEditor.commit();
	}

	public LinkedList<UserInfo> getUserList() {
		return userList;
	}

	public void setUserList(LinkedList<UserInfo> userList) {
		this.userList = userList;
	}

	public LinkedList<TweetData> getTweetList() {
		return tweetList;
	}

	public void setDataList(LinkedList<TweetData> dataList) {
		this.tweetList = dataList;
	}
	
    public static Context getTwitterAppContext(){
        return context;
    } 
    
	public UserInfo getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(UserInfo userProfile) {
		this.userProfile = userProfile;
	}
	
	public boolean isStarted() {
		return started;
	}

	public void setStarted(boolean started) {
		this.started = started;
	}
	
	public boolean isSupportsHoneyComb() {
		return android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB;
	}
	
	public TweeterAPI getAPI() {
		if(twitterAPI!=null) {
			return twitterAPI;
		} else {
			return new TweeterAPI(getAccessToken(), getSecretToken(), getApplicationContext());

		}
	}




}
