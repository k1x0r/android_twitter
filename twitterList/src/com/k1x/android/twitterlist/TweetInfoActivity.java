package com.k1x.android.twitterlist;

import com.k1x.android.twitterlist.entities.TweetData;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class TweetInfoActivity extends BaseActivity {

	
	private ImageView userAvatar;
	private TextView userFullName;
	private TextView userScreenName;
	private TextView tweetText;
	private TextView tweetCreatedAt;
	private TextView tweetSource;
	private TweetData tweetData;
	private Bitmap tweetBitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.activity_base_tweetinfo);
		tweetData = (TweetData) getIntent().getSerializableExtra(TweetListActivity.TWEET_DATA);
		tweetBitmap = (Bitmap) getIntent().getParcelableExtra(TweetListActivity.TWEET_BITMAP);
		setUpViews();

		System.out.println("tweetData: " + tweetData + "\n TweetBitmap: " + tweetBitmap);
	}

	private void setUpViews()
	{
		userAvatar = (ImageView) findViewById(R.id.tweetI_tweet_icon);
		userFullName = (TextView) findViewById(R.id.tweetI_fullname);
		userScreenName = (TextView) findViewById(R.id.tweetI_nickname);
		tweetText = (TextView) findViewById(R.id.tweetI_tweet);
		tweetCreatedAt = (TextView) findViewById(R.id.tweetI_created);
		tweetSource =(TextView) findViewById(R.id.tweetI_source);
		
		if(tweetData!= null) {
			userAvatar.setImageBitmap(tweetBitmap);
			userFullName.setText(tweetData.getUser().getName());
			userScreenName.setText(tweetData.getUser().getScreen_name());
			tweetText.setText(tweetData.getText());
			tweetCreatedAt.setText(tweetData.getCreatedAt());
			tweetSource.setText(tweetData.getSource());
		}
	}
}
