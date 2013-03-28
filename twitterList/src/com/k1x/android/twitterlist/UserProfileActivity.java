package com.k1x.android.twitterlist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.k1x.android.twitterlist.constants.Constants;
import com.k1x.android.twitterlist.entities.UserInfo;

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
	private Button userFolowButton;
	private Button userBlockButton;
	private ImageView userFolowingIcon;
	private ImageView userBlockedIcon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.activity_base_userprofile);
	}
	
	
	
	@Override
	protected void onCreate() {
		userInfo = (UserInfo) getIntent().getParcelableExtra(Constants.USER_INFO);
		userInfoSet = userInfo!=null;
		setUpViews();
	}



	@Override
	protected void onGettingUserInfo(UserInfo userInfo) {
		if(!userInfoSet) {
			this.userInfo = userInfo;
		} 
		fillForm();
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
		userFolowButton = (Button) findViewById(R.id.folow_button);
		userBlockButton = (Button) findViewById(R.id.block_button);
		userFolowingIcon = (ImageView)findViewById(R.id.userinfo_folowing);
		userBlockedIcon = (ImageView)findViewById(R.id.userinfo_blocked);
		
		userFolowersButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent I = new Intent(UserProfileActivity.this, UsersListActivity.class);
				I.putExtra(Constants.KEY_MODE, Constants.MODE_FOLOWERS);
				I.putExtra(Constants.KEY_USER_LOGIN, userInfo.getScreen_name());
				startActivity(I);
			}
		});
		
		userFolowingsButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent I = new Intent(UserProfileActivity.this, UsersListActivity.class);
				I.putExtra(Constants.KEY_MODE, Constants.MODE_FOLOWINGS);
				I.putExtra(Constants.KEY_USER_LOGIN, userInfo.getScreen_name());
				startActivity(I);
			}
		});
		
		userTweetButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent I = new Intent(UserProfileActivity.this, UserHomeTimelineActivity.class);
				I.putExtra(Constants.KEY_USER_LOGIN, userInfo.getScreen_name());
				startActivity(I);				
			}
		});
		
		if(userInfoSet) {
			fillForm();
		}
	}



	private void fillForm() {
		userName.setText(userInfo.getName());
		userAvatar.setImageBitmap(userInfo.getUserBitmap());
		userScreenName.setText(userInfo.getScreen_name());
		userCreatedAt.setText(userInfo.getCreated_at());
		userDesctiption.setText(userInfo.getDescription());
		userFolowersButton.setText(getString(R.string.folowers) + " " + userInfo.getFollowers_count());
		userFolowingsButton.setText(getString(R.string.folowings) + " " + userInfo.getFriends_count());
		userFolowButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						getApp().getAPI().folowUser(userInfo.getScreen_name(), !userInfo.isBlocked());					
					}
				}).start();
			}
		});
		
		userBlockButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					getApp().getAPI().blockUser(userInfo.getScreen_name(), !userInfo.isFollowing());
				}
			}).start();
			}
		});
		
		if(userInfo.isFollowing()) {
			userFolowingIcon.setVisibility(View.VISIBLE);
		} else {
			userFolowingIcon.setVisibility(View.GONE);
		}
		if(userInfo.isBlocked()) {
			userBlockedIcon.setVisibility(View.VISIBLE);
		} else {
			userBlockedIcon.setVisibility(View.GONE);
		}
	}


	
	
}
