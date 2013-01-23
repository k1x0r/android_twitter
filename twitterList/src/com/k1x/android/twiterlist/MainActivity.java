package com.k1x.android.twiterlist;

import oauth.signpost.OAuth;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

    private Button tweetListButton;
	private EditText loginEditText;
	private EditText passwordEditText;
	private CheckBox saveDataCheckBox;
	private Button loginButton;

   private CommonsHttpOAuthConsumer httpOauthConsumer;
    private OAuthProvider httpOauthprovider;
    public final static String consumerKey = "rLgj2ZPStx0cFoFv5vM3A";
    public final static String consumerSecret = "1SZJt4hzWqggIzG6CD7is3wHE5vmrrxg2ijDJMpeQ40";
    private final String CALLBACKURL = "app://twitter";
//	private static final Uri CALLBACK_URI = Uri.parse("bloa-app://twitter");

	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpViews();
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
        
        loginEditText = (EditText) findViewById(R.id.editText_login);
        passwordEditText = (EditText) findViewById(R.id.editText_password);
        saveDataCheckBox = (CheckBox) findViewById(R.id.checkBox_saveData);
        loginButton = (Button) findViewById(R.id.button_login);
        
        loginButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				System.out.println(String.format("login: %s password: %s savedataCheckBox: %b", 
						loginEditText.getText().toString(), passwordEditText.getText().toString(), String.valueOf(saveDataCheckBox.isChecked())));
				startOAuth();
			}
		});
    }
    
    private void startOAuth()
    {
    	try {
    	    httpOauthConsumer = new CommonsHttpOAuthConsumer(consumerKey, consumerSecret);
    	    httpOauthprovider = new DefaultOAuthProvider("http://twitter.com/oauth/request_token",
    	                                            "http://twitter.com/oauth/access_token",
    	                                            "http://twitter.com/oauth/authorize");
    		httpOauthprovider.setOAuth10a(true);

    	    String authUrl = httpOauthprovider.retrieveRequestToken(httpOauthConsumer, CALLBACKURL);
    	    // Open the browser
    	    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl)));
    	} catch (Exception e) {
    		e.printStackTrace();
    	    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
    	}
    }
    
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Uri uri = intent.getData();

        //Check if you got NewIntent event due to Twitter Call back only

        if (uri != null && uri.toString().startsWith(CALLBACKURL)) {

            String veriﬁer = uri.getQueryParameter(oauth.signpost.OAuth.OAUTH_VERIFIER);

            try {
                // this will populate token and token_secret in consumer

                httpOauthprovider.retrieveAccessToken(httpOauthConsumer, veriﬁer);
                String userKey = httpOauthConsumer.getToken();
                String userSecret = httpOauthConsumer.getTokenSecret();

                // Save user_key and user_secret in user preferences and return

                SharedPreferences settings = getBaseContext().getSharedPreferences("your_app_prefs", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("user_key", userKey);
                editor.putString("user_secret", userSecret);
                editor.commit();

            } catch(Exception e){

            }
        } else {
            // Do something if the callback comes from elsewhere
        }

    }
}
