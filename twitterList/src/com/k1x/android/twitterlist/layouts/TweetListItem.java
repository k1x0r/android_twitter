package com.k1x.android.twitterlist.layouts;

import java.io.IOException;

import com.k1x.android.twitterlist.R;
import com.k1x.android.twitterlist.TweetListActivity;
import com.k1x.android.twitterlist.httputil.HTTPUtil;
import com.k1x.android.twitterlist.jsonobj.TweetData;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TweetListItem extends LinearLayout {

	private Activity activity;
	private TweetData tweetData;
	private TextView tweetText;
	private TextView tweetAuthor;
	private TextView tweetNick;
	private ImageView tweetIcon;
	private Context context;
	private Bitmap bitmap;
	String url;
	
	public TweetListItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		tweetText = (TextView)findViewById(R.id.cell_tweet);
		tweetAuthor = (TextView)findViewById(R.id.cell_fullname);
		tweetNick = (TextView)findViewById(R.id.cell_nickname);
		tweetIcon = (ImageView)findViewById(R.id.cell_tweet_icon);
	}
	
	public void setTweet(TweetData tweet)
	{
		this.tweetData = tweet;
		if(tweet.getRetweeted_status()!=null)
			url = tweet.getRetweeted_status().getUser().getProfile_image_url();
		else 
			url = tweet.getUser().getProfile_image_url();
		
		tweetText.setText(tweet.getText());
		tweetAuthor.setText(tweet.getUser().getName());
		tweetNick.setText(tweet.getUser().getScreen_name());
		
		activity = (TweetListActivity) context;
		
		Thread T = new Thread(setTweetImage);
		T.start();

	}
	
	Runnable setTweetImage = new Runnable() {
		@Override
		public void run() {
			try {
				int preferredSize = 64;
				Bitmap receivedBitmap = HTTPUtil.getImage(url);
				bitmap = Bitmap.createScaledBitmap(receivedBitmap, preferredSize, preferredSize, true);
			} catch (IOException e) {
				e.printStackTrace();
			}		
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					tweetIcon.setImageBitmap(bitmap);
				}});
		}};
	
	public TweetData getTweetData() {
		return tweetData;
	}

	public void setTweetData(TweetData tweetData) {
		this.tweetData = tweetData;
	}

}
