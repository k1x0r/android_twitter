package com.k1x.android.twitterlist;

import com.k1x.android.twitterlist.R;
import com.k1x.android.twitterlist.twitter.DialogError;
import com.k1x.android.twitterlist.twitter.TwDialog;
import com.k1x.android.twitterlist.twitter.Twitter;
import com.k1x.android.twitterlist.twitter.TwitterError;
import com.k1x.android.twitterlist.twitterutil.Tweeter;


import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

    private Button tweetListButton;
	private Button loginButton;
   
    public static final String TAG = "Trololo";

    public static final String TWITTER_OAUTH_REQUEST_TOKEN_ENDPOINT = "http://twitter.com/oauth/request_token";
    public static final String TWITTER_OAUTH_ACCESS_TOKEN_ENDPOINT = "http://twitter.com/oauth/access_token";
    public static final String TWITTER_OAUTH_AUTHORIZE_ENDPOINT = "http://twitter.com/oauth/authorize";
    private CommonsHttpOAuthProvider commonsHttpOAuthProvider;
    private CommonsHttpOAuthConsumer commonsHttpOAuthConsumer;
	private TwitterListApplication app;
	private EditText tweetEditText;
	private Button tweetButton;
	private TextView statusTextView;
	private Tweeter tweeter;
	
 //   public final static String consumerKey = "rLgj2ZPStx0cFoFv5vM3A";
 //   public final static String consumerSecret = "1SZJt4hzWqggIzG6CD7is3wHE5vmrrxg2ijDJMpeQ40";
    

	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (TwitterListApplication) getApplication();
        setUpViews();
        setUpData();
		if(!app.getAccessToken().equals("0"))
			getUserInfo();

    }



	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    

    
    private void setUpViews()
    {
        setContentView(R.layout.activity_main);
        tweetListButton = (Button)findViewById(R.id.tweet_list_button);
        
        tweetListButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, TweetListActivity.class);
		        startActivity(intent);
			}
		});
             
        loginButton = (Button) findViewById(R.id.button_login);       
        loginButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setUpOAuth();
			}
		});
        
        tweetEditText = (EditText) findViewById(R.id.tweetEditText);
        tweetButton = (Button) findViewById(R.id.tweet_button);
        tweetButton.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View arg0) {
				Tweet();
			}
		});
        statusTextView = (TextView) findViewById(R.id.status_view);
    }
	private void setUpData() {
		tweeter = new Tweeter(app.getAccessToken(), app.getSecretToken(), MainActivity.this);
	}

    private void getUserInfo()
    {
		Thread T = new Thread(new Runnable() {
			private String text;
			@Override
			public void run() {
				text = tweeter.getUserInfo().toString();
				if(text!=null)
	            runOnUiThread(new Runnable() {
					@Override
					public void run() {
						statusTextView.setText(text);
					}});
			}
			
		});
		T.start();
    }
	
    private void Tweet() 
    {
		Thread T = new Thread(new Runnable() {
			private String result ;
						
			@Override
			public void run() {
	            result = tweeter.tweet(tweetEditText.getText().toString());
	            runOnUiThread(new Runnable() {
					@Override
					public void run() {
						statusTextView.setText(result);
					}});
			}
			
		});
		T.start();
    }
    
    private void setUpOAuth() {
        commonsHttpOAuthProvider = new CommonsHttpOAuthProvider(TWITTER_OAUTH_REQUEST_TOKEN_ENDPOINT,
                TWITTER_OAUTH_ACCESS_TOKEN_ENDPOINT, TWITTER_OAUTH_AUTHORIZE_ENDPOINT);
        commonsHttpOAuthConsumer = new CommonsHttpOAuthConsumer(getString(R.string.twitter_oauth_consumer_key),
                getString(R.string.twitter_oauth_consumer_secret));
        commonsHttpOAuthProvider.setOAuth10a(true);
        TwDialog dialog = new TwDialog(this, commonsHttpOAuthProvider, commonsHttpOAuthConsumer,
                dialogListener, R.drawable.ic_launcher);
        dialog.show();		
	}
    
    private Twitter.DialogListener dialogListener = new Twitter.DialogListener() {
        public void onComplete(Bundle values) {
            String secretToken = values.getString("secret_token");
            Log.i(TAG,"secret_token=" + secretToken);
            String accessToken = values.getString("access_token");
            Log.i(TAG,"access_token=" + accessToken);
            app.setAccessToken(accessToken);
            app.setSecretToken(secretToken);
            setUpData();

        }

        public void onTwitterError(TwitterError e) { 
        	Log.e(TAG,"onTwitterError called for TwitterDialog", new Exception(e));
        	}

        public void onError(DialogError e) { 
        	Log.e(TAG,"onError called for TwitterDialog", new Exception(e)); 
        	}

        public void onCancel() { 
        	Log.e(TAG,"onCancel"); 
        	}


    };
    

}
