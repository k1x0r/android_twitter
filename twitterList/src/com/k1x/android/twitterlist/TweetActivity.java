package com.k1x.android.twitterlist;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TweetActivity extends BaseActivity {

	private EditText tweetText;
	private Button tweetButton;
	private TextView tweetStatus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.activity_base_tweet);
		setUpViews();
	}

	private void setUpViews()
	{
		tweetText = (EditText) findViewById(R.id.t_tweetEditText);
		tweetStatus = (TextView) findViewById(R.id.t_statusTextView);
		tweetButton = (Button) findViewById(R.id.t_tweetButton);
		tweetButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Tweet();
			}
		});

	}

    private void Tweet() 
    {
		Thread T = new Thread(new Runnable() {
			private String result ;
						
			@Override
			public void run() {
				result = getTweeter().tweet(tweetText.getText().toString())
						.getCreatedAt();
				if (result != null) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							tweetStatus.setText(result);
						}
					});
				}
			}
			
		});
		T.start();
    }
}
