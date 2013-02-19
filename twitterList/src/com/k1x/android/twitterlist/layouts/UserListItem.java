package com.k1x.android.twitterlist.layouts;

import java.io.IOException;

import com.k1x.android.twitterlist.BaseActivity;
import com.k1x.android.twitterlist.R;

import com.k1x.android.twitterlist.constants.Constants;

import com.k1x.android.twitterlist.entities.UserInfo;
import com.k1x.android.twitterlist.httputil.HTTPUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class UserListItem extends LinearLayout {

	private UserInfo userInfo;
	private TextView userScreenName;
	private TextView userName;
	private ImageView userImage;
	private BaseActivity baseActivity;

	public UserListItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.baseActivity = (BaseActivity) context;
	}
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		userName = (TextView)findViewById(R.id.userinfo_fullname);
		userScreenName = (TextView)findViewById(R.id.userinfo_screenname);
		userImage = (ImageView)findViewById(R.id.userinfo_user_icon);
	}
	
	public void setUserCell(UserInfo userInfo)
	{
		this.userInfo = userInfo;
		userName.setText(userInfo.getName());
		if(userInfo.getScreen_name()!=null)
		userScreenName.setText(userInfo.getScreen_name());
		new Thread(setUserAvatar).start();
	}
	
	public UserInfo getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	
	Runnable setUserAvatar = new Runnable() {

		@Override
		public void run() {
			try {
				int preferredSize = Constants.PREFFED_USER_AVATAR_SIZE;
				String url = userInfo.getProfile_image_url();
				Bitmap receivedBitmap = HTTPUtil.getImage(url);
				userInfo.setUserBitmap(Bitmap.createScaledBitmap(receivedBitmap, preferredSize, preferredSize, true));
			} catch (IOException e) {
				e.printStackTrace();
			}		
			if(userInfo.getUserBitmap()!=null) {
			baseActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					userImage.setImageBitmap(userInfo.getUserBitmap());
				}});
			}}};
	
}
