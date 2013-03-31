package com.k1x.android.twitterlist;

import com.k1x.android.twitterlist.constants.Constants;
import com.k1x.android.twitterlist.entities.TweetData;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class FullTweetActivity extends BaseActivity {

	
	private ImageView userAvatar;
	private Bitmap userBitmap;
	private TextView userFullName;
	private TextView userScreenName;
	private TextView tweetText;
	private TextView tweetCreatedAt;
	private TextView tweetSource;
	private TweetData tweetData;
	private Button userProfileButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.activity_base_tweetinfo);
	}

	@Override
	protected void onCreate() {
		tweetData = (TweetData) getIntent().getParcelableExtra(Constants.TWEET_DATA);
		userBitmap = tweetData.getUser().getUserBitmap();
		setUpViews();
	}

	private void setUpViews()
	{
		userAvatar = (ImageView) findViewById(R.id.tweetI_tweet_icon);
		userFullName = (TextView) findViewById(R.id.tweetI_fullname);
		userScreenName = (TextView) findViewById(R.id.tweetI_nickname);
		tweetText = (TextView) findViewById(R.id.tweetI_tweet);
		tweetCreatedAt = (TextView) findViewById(R.id.tweetI_created);
		tweetSource =(TextView) findViewById(R.id.tweetI_source);
		userProfileButton = (Button) findViewById(R.id.tweetI_userProfileButton);
		
		if(tweetData!= null) {
			if(tweetData.getRetweeted_status()!= null) {
				tweetData = tweetData.getRetweeted_status();
			}
			userAvatar.setImageBitmap(userBitmap);
			userFullName.setText(tweetData.getUser().getName());
			userScreenName.setText(tweetData.getUser().getScreen_name());
			tweetText.setText(tweetData.getText());
			tweetCreatedAt.setText(tweetData.getCreatedAt());
			tweetSource.setText(tweetData.getSource());
			userProfileButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Intent I = new Intent(FullTweetActivity.this, UserProfileActivity.class);
					I.putExtra(Constants.USER_INFO, (Parcelable)tweetData.getUser());
					I.putExtra(Constants.USER_BITMAP, userBitmap);
					startActivity(I);					
				}
			});
			
		}
	}
}
