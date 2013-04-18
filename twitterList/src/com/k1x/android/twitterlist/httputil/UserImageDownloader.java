package com.k1x.android.twitterlist.httputil;

import java.io.IOException;

import android.graphics.Bitmap;

import com.k1x.android.twitterlist.BaseActivity;
import com.k1x.android.twitterlist.constants.Constants;
import com.k1x.android.twitterlist.entities.UserInfo;
import com.k1x.android.twitterlist.listviews.IPostDataChange;

public class UserImageDownloader extends Thread {

	private UserInfo userData;
	private IPostDataChange adapter;
	private BaseActivity activity;

	public UserImageDownloader (BaseActivity activity, UserInfo userData, IPostDataChange adapter) {
		this.activity = activity;
		this.userData = userData;
		this.adapter = adapter;
	}
	
	
	@Override
	public void run() {
		try {
			int preferredSize = (int) activity.pxFromDp(Constants.PREFFED_USER_AVATAR_SIZE);
			Bitmap receivedBitmap = HTTPUtil.getImage(userData.getProfile_image_url());
			userData.setUserBitmap(Bitmap.createScaledBitmap(receivedBitmap, preferredSize, preferredSize, true));
			adapter.postNotifyDataSetChanged();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	};
	

}


