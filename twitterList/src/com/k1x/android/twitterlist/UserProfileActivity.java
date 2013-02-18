package com.k1x.android.twitterlist;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.k1x.android.twitterlist.entities.TweetData;
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
	private boolean userInfoSet;
	private UserInfo userInfo;
	private Bitmap userBitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.activity_base_userprofile);
		userInfo = (UserInfo) getIntent().getSerializableExtra(UsersListActivity.USER_INFO);
		userBitmap = (Bitmap) getIntent().getParcelableExtra(UsersListActivity.USER_BITMAP);
		userInfoSet = userInfo!=null;
		setUpViews();
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
		
		userFolowersButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent I = new Intent(UserProfileActivity.this, UsersListActivity.class);
				I.putExtra(UsersListActivity.KEY_MODE, UsersListActivity.MODE_FOLOWERS);
				I.putExtra(UsersListActivity.KEY_USER_INFO, userInfo);
				startActivity(I);
			}
		});
		
		userFolowingsButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent I = new Intent(UserProfileActivity.this, UsersListActivity.class);
				I.putExtra(UsersListActivity.KEY_MODE, UsersListActivity.MODE_FOLOWINGS);
				I.putExtra(UsersListActivity.KEY_USER_INFO, userInfo);
				startActivity(I);
			}
		});
		
		userTweetButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		if(userInfoSet) {
			userName.setText(userInfo.getName());
			userAvatar.setImageBitmap(userBitmap);
			userScreenName.setText(userInfo.getScreen_name());
			userCreatedAt.setText(userInfo.getCreated_at());
			userDesctiption.setText(userInfo.getDescription());
			userFolowersButton.setText("Folowers "+ userInfo.getFollowers_count());
			userFolowingsButton.setText("Folowings " + userInfo.getFriends_count());
		}
	}

	@Override
	protected void onGettingUserInfo(UserInfo userInfo, Bitmap userImage) {
		if(!userInfoSet) {
			this.userInfo = userInfo;
			userName.setText(userInfo.getName());
			userAvatar.setImageBitmap(userImage);
			userScreenName.setText(userInfo.getScreen_name());
			userCreatedAt.setText(userInfo.getCreated_at());
			userDesctiption.setText(userInfo.getDescription());
			userFolowersButton.setText("Folowers "+ userInfo.getFollowers_count());
			userFolowingsButton.setText("Folowings " + userInfo.getFriends_count());
		}
	}
	
	
}
