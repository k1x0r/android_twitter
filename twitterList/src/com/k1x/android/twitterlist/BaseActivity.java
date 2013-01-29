package com.k1x.android.twitterlist;

import java.io.IOException;

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.k1x.android.twitterlist.httputil.HTTPUtil;
import com.k1x.android.twitterlist.jsonobj.UserInfo;
import com.k1x.android.twitterlist.twitter.DialogError;
import com.k1x.android.twitterlist.twitter.TwDialog;
import com.k1x.android.twitterlist.twitter.Twitter;
import com.k1x.android.twitterlist.twitter.TwitterError;
import com.k1x.android.twitterlist.twitterutil.Tweeter;

public abstract class BaseActivity extends Activity {

    public static final String TAG = "Trololo";   
    public static final String TWITTER_OAUTH_REQUEST_TOKEN_ENDPOINT = "http://twitter.com/oauth/request_token";
    public static final String TWITTER_OAUTH_ACCESS_TOKEN_ENDPOINT = "http://twitter.com/oauth/access_token";
    public static final String TWITTER_OAUTH_AUTHORIZE_ENDPOINT = "http://twitter.com/oauth/authorize";
    
	private Tweeter tweeter;
	private Button loginButton;
	private Button logoutButton;
	private TextView userNameTextField;
	private ImageView userAvatar;
	private TwitterListApplication app;
	private CommonsHttpOAuthProvider commonsHttpOAuthProvider;
	private CommonsHttpOAuthConsumer commonsHttpOAuthConsumer;
	private RelativeLayout userInfoLayout;
	private int resLayout;

	protected void onCreate(Bundle savedInstanceState, int resLayout) {
		super.onCreate(savedInstanceState);
		this.resLayout = resLayout;
		
        app = (TwitterListApplication) getApplication();
		setUpViews();
		setUpData();
		getUserInfo();
	}
	
	

	private void setUpViews() {
		setContentView(resLayout);
		
		userInfoLayout = (RelativeLayout) findViewById(R.id.tl_userloginDataLayout);

		loginButton = (Button) findViewById(R.id.tl_loginbutton);
		loginButton.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				authorizeUser();
			}
		});
		
		logoutButton = (Button) findViewById(R.id.tl_logoutbutton);
		logoutButton.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				logOut();
			}
		});
		
		userNameTextField = (TextView) findViewById(R.id.tl_username);
		userAvatar = (ImageView) findViewById(R.id.tl_useravatar);		
	}

	private void setUpData() {
		tweeter = new Tweeter(app.getAccessToken(), app.getSecretToken(), BaseActivity.this);
	}

    private void getUserInfo()
    {
		Thread T = new Thread(new Runnable() {
			private UserInfo uinfo;
			Bitmap userAvatarImage;
			@Override
			public void run() {
				uinfo = tweeter.getUserInfo();
				if(uinfo!=null)
				{ 
				try {
					int destSize = userInfoLayout.getHeight(); 
					Bitmap avatarFromTwitter =  HTTPUtil.getImage(uinfo.getProfile_image_url());			
					userAvatarImage = Bitmap.createScaledBitmap(avatarFromTwitter, destSize, destSize, true);
					
				} catch (IOException e) {
					e.printStackTrace();
				}
	            runOnUiThread(new Runnable() {
					@Override
					public void run() {
						userNameTextField.setText(uinfo.getName());
						userAvatar.setImageBitmap(userAvatarImage);
						setLoggedIn(true);
					}});
				} else 
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							setLoggedIn(false);
						}});
			}		
		});
		T.start();
    }
	
	private void setLoggedIn(boolean loggedIn)
	{
		if(loggedIn)
		{
			loginButton.setVisibility(View.GONE);
			logoutButton.setVisibility(View.VISIBLE);
			userNameTextField.setVisibility(View.VISIBLE);
			userAvatar.setVisibility(View.VISIBLE);
		}
		else
		{
			loginButton.setVisibility(View.VISIBLE);
			logoutButton.setVisibility(View.GONE);
			userNameTextField.setVisibility(View.GONE);
			userAvatar.setVisibility(View.GONE);
		}
	}
	
    private void authorizeUser() {
        commonsHttpOAuthProvider = new CommonsHttpOAuthProvider(TWITTER_OAUTH_REQUEST_TOKEN_ENDPOINT,
                TWITTER_OAUTH_ACCESS_TOKEN_ENDPOINT, TWITTER_OAUTH_AUTHORIZE_ENDPOINT);
        commonsHttpOAuthConsumer = new CommonsHttpOAuthConsumer(getString(R.string.twitter_oauth_consumer_key),
                getString(R.string.twitter_oauth_consumer_secret));
        commonsHttpOAuthProvider.setOAuth10a(true);
        TwDialog dialog = new TwDialog(this, commonsHttpOAuthProvider, commonsHttpOAuthConsumer,
                dialogListener, R.drawable.ic_launcher);
        dialog.show();		
	}
    
    private void logOut()
    {
        app.setAccessToken("0");
        app.setSecretToken("0");
        setLoggedIn(false);
    }
    
    private Twitter.DialogListener dialogListener = new Twitter.DialogListener() {
        public void onComplete(Bundle values) {
            String secretToken = values.getString("secret_token");
            Log.i(TAG,"secret_token=" + secretToken);
            String accessToken = values.getString("access_token");
            Log.i(TAG,"access_token=" + accessToken);
            app.setAccessToken(accessToken);
            app.setSecretToken(secretToken);
            
            runOnUiThread(new Runnable() {
				@Override
				public void run() {
		            setLoggedIn(true);				
				}});
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
    
	public TwitterListApplication getApp() {
		return app;
	}
}
