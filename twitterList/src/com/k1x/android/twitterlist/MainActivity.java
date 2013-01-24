package com.k1x.android.twitterlist;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;


import com.k1x.android.twiterlist.R;
import com.k1x.android.twitterlist.twitter.DialogError;
import com.k1x.android.twitterlist.twitter.TwDialog;
import com.k1x.android.twitterlist.twitter.Twitter;
import com.k1x.android.twitterlist.twitter.TwitterError;

import oauth.signpost.OAuth;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private Button tweetListButton;
	private Button loginButton;
   
    public static final String TAG = "Trololo";
    private static final String APP_SHARED_PREFS = "sharedpreferences"; //  Name of the file -.xml

    public static final String TWITTER_OAUTH_REQUEST_TOKEN_ENDPOINT = "http://twitter.com/oauth/request_token";
    public static final String TWITTER_OAUTH_ACCESS_TOKEN_ENDPOINT = "http://twitter.com/oauth/access_token";
    public static final String TWITTER_OAUTH_AUTHORIZE_ENDPOINT = "http://twitter.com/oauth/authorize";
    private CommonsHttpOAuthProvider commonsHttpOAuthProvider;
    private CommonsHttpOAuthConsumer commonsHttpOAuthConsumer;
	private TwitterListApplication app;
	private EditText tweetEditText;
	private Button tweetButton;
	
 //   public final static String consumerKey = "rLgj2ZPStx0cFoFv5vM3A";
 //   public final static String consumerSecret = "1SZJt4hzWqggIzG6CD7is3wHE5vmrrxg2ijDJMpeQ40";
    

	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (TwitterListApplication) getApplication();
        setUpViews();

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
				System.out.println("ACCESS_TOKEN " + app.getAccessToken());
				System.out.println("SECRET_TOKEN " + app.getSecretToken());
				
				Thread T = new Thread(new Runnable() {
					@Override
					public void run() {
			            new Tweeter(app.getAccessToken(), app.getSecretToken()).tweet(
			                    "Tweet from sample Android OAuth app.");					
					}
				});
				T.start();
			}
		});

    }
    
    public static final Pattern ID_PATTERN = Pattern.compile(".*?\"id_str\":\"(\\d*)\".*");
    public static final Pattern SCREEN_NAME_PATTERN = Pattern.compile(".*?\"screen_name\":\"([^\"]*).*");

    public class Tweeter {
        protected CommonsHttpOAuthConsumer oAuthConsumer;

        public Tweeter(String accessToken, String secretToken) {
            oAuthConsumer = new CommonsHttpOAuthConsumer(getString(R.string.twitter_oauth_consumer_key),
                    getString(R.string.twitter_oauth_consumer_secret));
            oAuthConsumer.setTokenWithSecret(accessToken, secretToken);
        }

        public boolean tweet(String message) {
            if (message == null && message.length() > 140) {
                throw new IllegalArgumentException("message cannot be null and must be less than 140 chars");
            }
            // create a request that requires authentication

            try {
                HttpClient httpClient = new DefaultHttpClient();
                Uri.Builder builder = new Uri.Builder();
                builder.appendPath("statuses").appendPath("update.json")
                        .appendQueryParameter("status", message);
                Uri man = builder.build();
                HttpPost post = new HttpPost("https://api.twitter.com/1.1" + man.toString());
                System.out.println(man.toString());
                
                oAuthConsumer.sign(post);
                HttpResponse resp = httpClient.execute(post);
                String jsonResponseStr = convertStreamToString(resp.getEntity().getContent());
                Log.i(TAG,"response: " + jsonResponseStr);
                String id = getFirstMatch(ID_PATTERN,jsonResponseStr);
                Log.i(TAG,"id: " + id);
                String screenName = getFirstMatch(SCREEN_NAME_PATTERN,jsonResponseStr);
                Log.i(TAG,"screen name: " + screenName);

                final String url = MessageFormat.format("https://twitter.com/#!/{0}/status/{1}",screenName,id);
                Log.i(TAG,"url: " + url);

                Runnable runnable = new Runnable() {
                    public void run() {
                         ((TextView)MainActivity.this.findViewById(R.id.status_view)).setText("Tweeted: " + url);
                    }
                };
                
                MainActivity.this.runOnUiThread(runnable);

                return resp.getStatusLine().getStatusCode() == 200;
                
            } catch (Exception e) {
                Log.e(TAG,"trying to tweet: " + message, e);
                return false;
            }

        }
    }
    
    private Twitter.DialogListener dialogListener = new Twitter.DialogListener() {
        public void onComplete(Bundle values) {
            String secretToken = values.getString("secret_token");
            Log.i(TAG,"secret_token=" + secretToken);
            String accessToken = values.getString("access_token");
            Log.i(TAG,"access_token=" + accessToken);
            app.setAccessToken(accessToken);
            app.setSecretToken(secretToken);

        }

        public void onTwitterError(TwitterError e) { Log.e(TAG,"onTwitterError called for TwitterDialog",
                new Exception(e)); }

        public void onError(DialogError e) { 
        	Log.e(TAG,"onError called for TwitterDialog", new Exception(e)); 
        	}

        public void onCancel() { 
        	Log.e(TAG,"onCancel"); 
        	}


    };
    
    public static String convertStreamToString(java.io.InputStream is) {
        try {
            return new java.util.Scanner(is).useDelimiter("\\A").next();
        } catch (java.util.NoSuchElementException e) {
            return "";
        }
    }
    
    public static String getFirstMatch(Pattern pattern, String str){
        Matcher matcher = pattern.matcher(str);
        if(matcher.matches()){
            return matcher.group(1);
        }
        return null;
    }
}
