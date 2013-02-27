package com.k1x.android.twitterlist;

import java.io.IOException;

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.k1x.android.twitterlist.entities.UserInfo;
import com.k1x.android.twitterlist.httputil.HTTPUtil;
import com.k1x.android.twitterlist.layouts.MenuListItem;
import com.k1x.android.twitterlist.listviews.MenuListAdapter;
import com.k1x.android.twitterlist.twitter.DialogError;
import com.k1x.android.twitterlist.twitter.TwDialog;
import com.k1x.android.twitterlist.twitter.Twitter;
import com.k1x.android.twitterlist.twitter.TwitterError;
import com.k1x.android.twitterlist.twitterutil.TweeterAPI;
import com.slidingmenu.lib.SlidingMenu;

public abstract class BaseActivity extends Activity {

    public static final String TAG = "Trololo";   
    public static final String TWITTER_OAUTH_REQUEST_TOKEN_ENDPOINT = "https://api.twitter.com/oauth/request_token";
    public static final String TWITTER_OAUTH_ACCESS_TOKEN_ENDPOINT = "https://api.twitter.com/oauth/access_token";
    public static final String TWITTER_OAUTH_AUTHORIZE_ENDPOINT = "https://api.twitter.com/oauth/authorize";
    
	private TweeterAPI tweeter;

	private Button loginButton;
	private Button logoutButton;
	private TextView userNameTextField;
	private ImageView userAvatar;
	private TwitterListApplication app;
	private CommonsHttpOAuthProvider commonsHttpOAuthProvider;
	private CommonsHttpOAuthConsumer commonsHttpOAuthConsumer;
	private RelativeLayout userInfoLayout;
	private int resLayout;

	private ListView mList;
	private LayoutInflater mInflater;
	private LinearLayout activityContentLayout;
	private SlidingMenu menu;

	protected void onCreate(Bundle savedInstanceState, int resLayout) {
		super.onCreate(savedInstanceState);
		this.resLayout = resLayout;
		
        app = (TwitterListApplication) getApplication();
		setUpViews();
		setUpData();
		getUserInfo();
	}
	
	

	private void setUpViews() {
		setContentView(R.layout.activity_base_assembly);
		
		activityContentLayout = (LinearLayout) findViewById(R.id.activity_content);
        
		userInfoLayout = (RelativeLayout) findViewById(R.id.tl_userloginDataLayout);
		
        menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.slidebar);
		
		mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mInflater.inflate(resLayout, activityContentLayout);
		
        mList   = (ListView) findViewById(R.id.sidebar_list);
        mList.setAdapter(new MenuListAdapter(this));
        mList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				MenuListItem item = (MenuListItem) arg1;
				item.getItem().getAction().run();
				System.out.println(item.getItem().getName());
			}
		});

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
		tweeter = new TweeterAPI(app.getAccessToken(), app.getSecretToken(), BaseActivity.this);
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
						onGettingUserInfo(uinfo, userAvatarImage);
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
	
    protected void onGettingUserInfo(UserInfo userInfo, Bitmap bitmap) {}  	
    
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
	
	public SlidingMenu getMenu() {
		return menu;
	}
	
	public TweeterAPI getTweeter() {
		return tweeter;
	}

}
