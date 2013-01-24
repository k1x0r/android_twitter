package com.k1x.android.twitterlist;

import android.app.Application;
import android.util.Log;

public class TwitterListApplication extends Application {
	
    private String secretToken;
    private String accessToken;
    
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	public String getSecretToken() {
		return secretToken;
	}

	public void setSecretToken(String secretToken) {
		this.secretToken = secretToken;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
}
