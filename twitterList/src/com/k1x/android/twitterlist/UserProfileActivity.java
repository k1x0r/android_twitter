package com.k1x.android.twitterlist;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.k1x.android.twitterlist.entities.UserInfo;
import com.k1x.android.twitterlist.listviews.UserListAdapter;
import com.k1x.android.twitterlist.twitterutil.Tweeter;

public class UserProfileActivity extends BaseActivity {

	
	private ImageView userAvatar;
	private TextView userName;
	private TextView userScreenName;
	private TextView userDesctiption;
	private TextView userCreatedAt;
	private Button userFolowingsButton;
	private Button userFolowersButton;
	private Button userTweetButton;
	private Button userPinToSlideBarButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.activity_base_userprofile);
		setUpViews();
//		mode = getIntent().getIntExtra("mode", Tweeter.FOLOWERS);
	}

	private void setUpViews()
	{
		userAvatar = (ImageView) findViewById(R.id.userProfile_userAvatar);
		userName = (TextView) findViewById(R.id.userProfile_userName);
		userScreenName = (TextView) findViewById(R.id.userProfile_screenName);
		userDesctiption = (TextView) findViewById(R.id.userProfile_description);
		userCreatedAt = (TextView) findViewById(R.id.userProfile_createdAt);
		userFolowingsButton = (Button) findViewById(R.id.userProfile_folowingsBtn);
		userFolowersButton = (Button) findViewById(R.id.userProfile_folowersBtn);
		userTweetButton = (Button) findViewById(R.id.userProfile_tweetsBtn);
		userPinToSlideBarButton = (Button) findViewById(R.id.userProfile_pinToSlideMenuBtn);
	}

	@Override
	protected void onGettingUserInfo(UserInfo userInfo, Bitmap userImage) {
		userName.setText(userInfo.getName());
		userAvatar.setImageBitmap(userImage);
		userScreenName.setText(userInfo.getScreen_name());
		userCreatedAt.setText(userInfo.getCreated_at());
		userDesctiption.setText(userInfo.getDescription());
		userFolowersButton.setText("Folowers "+ userInfo.getFollowers_count());
		userFolowingsButton.setText("Folowings " + userInfo.getFriends_count());
	}
	
	
}
