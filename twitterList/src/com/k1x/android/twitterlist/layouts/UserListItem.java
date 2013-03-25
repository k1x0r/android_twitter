package com.k1x.android.twitterlist.layouts;


import com.k1x.android.twitterlist.R;
import com.k1x.android.twitterlist.entities.UserInfo;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class UserListItem extends RelativeLayout {

	private UserInfo userInfo;
	private TextView userScreenName;
	private TextView userName;
	private ImageView userImage;
	private ImageView userFolowingIcon;
	private ImageView userBlockedIcon;

	public UserListItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		userName = (TextView)findViewById(R.id.userinfo_fullname);
		userScreenName = (TextView)findViewById(R.id.userinfo_screenname);
		userImage = (ImageView)findViewById(R.id.userinfo_user_icon);
		userFolowingIcon = (ImageView)findViewById(R.id.userinfo_folowing);
		userBlockedIcon = (ImageView)findViewById(R.id.userinfo_blocked);
	}
	
	public void setUserCell(UserInfo userInfo)
	{
		this.userInfo = userInfo;
		userName.setText(userInfo.getName());
		userScreenName.setText(userInfo.getScreen_name());
		if (userInfo.getUserBitmap() != null) {
			userImage.setImageBitmap(userInfo.getUserBitmap());
		}
		if(userInfo.isFollowing()) {
			userFolowingIcon.setVisibility(VISIBLE);
		} else {
			userFolowingIcon.setVisibility(GONE);
		}
		if(userInfo.isBlocked()) {
			userBlockedIcon.setVisibility(VISIBLE);
		} else {
			userBlockedIcon.setVisibility(GONE);
		}
	}
	
	public UserInfo getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	

}
