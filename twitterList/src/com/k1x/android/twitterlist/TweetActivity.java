package com.k1x.android.twitterlist;

import java.net.UnknownHostException;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class TweetActivity extends BaseActivity {

	private EditText tweetText;
	private Button tweetButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.activity_base_tweet);
		setUpViews();
	}

	private void setUpViews()
	{
		tweetText = (EditText) findViewById(R.id.t_tweetEditText);
		tweetButton = (Button) findViewById(R.id.t_tweetButton);
		tweetButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new TweetTask().execute(tweetText.getText().toString());
			}
		});

	}
	
	private class TweetTask extends AsyncTask<String, Void, Boolean> {
		
		
		@Override
		protected Boolean doInBackground(String... params) {
			try {
				String result = getTweeter().tweet(params[0]).getText();
				if (result != null) {
					return true;
				} else {
					return false;
				}
			} catch (UnknownHostException e) {
				showToastMessage(R.string.unknown_host);
				return false;
			} catch (Exception e) {
				showToastMessage(e.getMessage());
				return false;
			}
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			if(result) {
				Intent I = new Intent(TweetActivity.this, TweetsTimelineActivity.class);
				startActivity(I);
				finish();
			}
		}

	}



}
