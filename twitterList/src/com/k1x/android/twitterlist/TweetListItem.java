package com.k1x.android.twitterlist;

import com.k1x.android.twiterlist.R;
import com.k1x.android.twitterlist.jsonobj.TweetData;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TweetListItem extends LinearLayout {

	private TweetData tweetData;
	private TextView tweetText;
	private TextView tweetAuthor;
	private TextView tweetNick;

	public TweetListItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		tweetText = (TextView)findViewById(R.id.cell_tweet);
		tweetAuthor = (TextView)findViewById(R.id.cell_fullname);
		tweetNick = (TextView)findViewById(R.id.cell_nickname);
	}
	
	public void setTweet(TweetData tweet)
	{
		this.tweetData = tweet;
		tweetText.setText(tweet.getText());
		tweetAuthor.setText(tweet.getUser().getName());
		tweetNick.setText(tweet.getUser().getScreen_name());
	}

	public TweetData getTweetData() {
		return tweetData;
	}

	public void setTweetData(TweetData tweetData) {
		this.tweetData = tweetData;
	}

}
