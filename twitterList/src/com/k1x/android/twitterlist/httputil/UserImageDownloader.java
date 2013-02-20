package com.k1x.android.twitterlist.httputil;

import java.io.IOException;

import android.graphics.Bitmap;

import com.k1x.android.twitterlist.constants.Constants;
import com.k1x.android.twitterlist.entities.TweetData;
import com.k1x.android.twitterlist.entities.UserInfo;
import com.k1x.android.twitterlist.listviews.IPostDataChange;
import com.k1x.android.twitterlist.listviews.TweetListAdapter;

public class UserImageDownloader extends Thread {

	private UserInfo userData;
	private IPostDataChange adapter;

	public UserImageDownloader (UserInfo userData, IPostDataChange adapter) {
		this.userData = userData;
		this.adapter = adapter;
	}
	
	
	@Override
	public void run() {
		try {
			int preferredSize = Constants.PREFFED_USER_AVATAR_SIZE;
			Bitmap receivedBitmap = HTTPUtil.getImage(userData.getProfile_image_url());
			userData.setUserBitmap(Bitmap.createScaledBitmap(receivedBitmap, preferredSize, preferredSize, true));
			adapter.postNotifyDataSetChanged();
		} catch (IOException e) {
			e.printStackTrace();
		}		
};
}
