package com.k1x.android.twitterlist;

import java.io.IOException;
import java.net.UnknownHostException;

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;

import com.k1x.android.twitterlist.entities.SlideMenuItem;
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


	public static final String TAG = "My Twitter";   
    public static final String TWITTER_OAUTH_REQUEST_TOKEN_ENDPOINT = "https://api.twitter.com/oauth/request_token";
    public static final String TWITTER_OAUTH_ACCESS_TOKEN_ENDPOINT = "https://api.twitter.com/oauth/access_token";
    public static final String TWITTER_OAUTH_AUTHORIZE_ENDPOINT = "https://api.twitter.com/oauth/authorize";
    
	private TweeterAPI tweeter;


	private TwitterListApplication app;
	private CommonsHttpOAuthProvider commonsHttpOAuthProvider;
	private CommonsHttpOAuthConsumer commonsHttpOAuthConsumer;
	private int resLayout;

	private ListView menuListView;
	private LayoutInflater mInflater;
	private LinearLayout activityContentLayout;
	private SlidingMenu menu;
	private MenuListAdapter menuAdapter;
	private SlideMenuItem loginItem;
	private boolean loggedIn;
	private TextView errorMessageView;
	private TwDialog dialog;

	protected void onCreate(Bundle savedInstanceState, int resLayout) {
		super.onCreate(savedInstanceState);
		this.resLayout = resLayout;
        app = (TwitterListApplication) getApplication();
		setUpViews();
		getAPI();
		onCreate();
		
		if(app.getUserProfile()!=null && !app.getAccessToken().equals("0")) {
			setUserUIControlsLoggedIn(app.getUserProfile());
			onGettingUserInfo(app.getUserProfile());
		} else if(!app.getAccessToken().equals("0")) {
			getUserInfo();
		} else {
			showErrorMessege();
		}
	}

	protected void onGettingUserInfo(UserInfo userInfo) {}  
    protected void onCreate() {}
    protected void onLogout() {}
	protected void onShowErrorMessege() {}

	
	protected void showErrorMessege() {
		errorMessageView.setVisibility(View.VISIBLE);
		activityContentLayout.setVisibility(View.GONE);
		onShowErrorMessege();
	}
	
	protected void hideErrorMessege() {
		errorMessageView.setVisibility(View.GONE);
		activityContentLayout.setVisibility(View.VISIBLE);
	}
	
	private void setUpViews() {
	
		setContentView(R.layout.activity_base_assembly);
		activityContentLayout = (LinearLayout) findViewById(R.id.activity_content);
		errorMessageView = (TextView) findViewById(R.id.error_not_logged_in);
		
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
		
		menuAdapter = new MenuListAdapter(this);
        menuListView   = (ListView) findViewById(R.id.sidebar_list);
        menuListView.setAdapter(menuAdapter);
        menuListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				MenuListItem item = (MenuListItem) arg1;
				item.getItem().getAction().run();
			}
		});
        
        menuListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View view,
					int arg2, long arg3) {
				MenuListItem item = (MenuListItem) view;
				OnLongClickListener longClickAction = item.getItem().getLongClickAction();
				if(longClickAction != null) {
					item.getItem().getLongClickAction().onLongClick(view);
				}
				return true;
			}
		});

        
        loginItem = menuAdapter.getLoginItem();
        loginItem.setAction(new Runnable() {
			@Override
			public void run() {
				if (loggedIn) {
					showUserProfile();
				} else {
					authorizeUser();
				}
			}
		});
        
        loginItem.setLongClickAction(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View view) {
				if (loggedIn) {
					PopupMenu popupMenu = new PopupMenu(BaseActivity.this, view);
					popupMenu.inflate(R.menu.profile_popup);
					popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
								@Override
								public boolean onMenuItemClick(MenuItem item) {
									return onProfilePopupItemClick(item);
								}
							});
					popupMenu.show();
					return true;
				} else {
					return false;
				}
			}
		});
        

	}

	public boolean onProfilePopupItemClick(MenuItem item) {
		switch(item.getItemId()) {
			case R.id.popup_view_profile:
				showUserProfile();
				break;
			case R.id.popup_log_out:
				logOut();
				break;
		}
		return true;
	}
	
	private void getAPI() {
		tweeter = app.getAPI();
	}

    protected void getUserInfo()
    {
		Thread T = new Thread(new Runnable() {
			
			private UserInfo uinfo;
			Bitmap userAvatarImage;
			
			@Override
			public void run() {
				try {
					execute();
				} catch (final Exception e) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							showErrorMessege();
							Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
						}});
				}
			}
			
			private void execute() throws Exception {
				uinfo = tweeter.getUserInfo();
				if(uinfo!=null)
				{ 
				try {
					int destSize = 72; 
					Bitmap avatarFromTwitter =  HTTPUtil.getImage(uinfo.getProfile_image_url());			
					userAvatarImage = Bitmap.createScaledBitmap(avatarFromTwitter, destSize, destSize, true);
				} catch (IOException e) {
					e.printStackTrace();
				}
	            runOnUiThread(new Runnable() {
					@Override
					public void run() {		
						uinfo.setUserBitmap(userAvatarImage);
						app.setUserProfile(uinfo);
						setUserUIControlsLoggedIn(uinfo);
						onGettingUserInfo(uinfo);
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
	
	private void setUserUIControlsLoggedIn(UserInfo uInfo) {
		String userName = uInfo.getName();
		Bitmap userAvatarImage = uInfo.getUserBitmap();
		setLoggedIn(true);
		BitmapDrawable userAvatarDrawable = new BitmapDrawable(getResources(), userAvatarImage);


		loginItem.setText(userName);
		loginItem.setImage(userAvatarDrawable);
		menuAdapter.notifyDataSetChanged();
		
		hideErrorMessege();
	}
   
	private void setLoggedIn(boolean loggedIn)
	{
		this.loggedIn = loggedIn;
		if (loggedIn) {

		} else {		
			loginItem.setText(getResources().getString(R.string.log_in));
			loginItem.setImage(getResources().getDrawable(R.drawable.login));
			menuAdapter.notifyDataSetChanged();
		}
	}
	
    private void authorizeUser() {
        commonsHttpOAuthProvider = new CommonsHttpOAuthProvider(TWITTER_OAUTH_REQUEST_TOKEN_ENDPOINT,
                TWITTER_OAUTH_ACCESS_TOKEN_ENDPOINT, TWITTER_OAUTH_AUTHORIZE_ENDPOINT);
        commonsHttpOAuthConsumer = new CommonsHttpOAuthConsumer(getString(R.string.twitter_oauth_consumer_key),
                getString(R.string.twitter_oauth_consumer_secret));
        commonsHttpOAuthProvider.setOAuth10a(true);
        dialog = new TwDialog(this, commonsHttpOAuthProvider, commonsHttpOAuthConsumer,
                dialogListener, R.drawable.login);
        dialog.show();		
	}
    
    private void logOut()
    {
        app.setAccessToken("0");
        app.setSecretToken("0");
        app.setUserProfile(null);
        setLoggedIn(false);
        onLogout();
    }
    
    private void showUserProfile() {
		menu.toggle();
		Intent I = new Intent(this, UserProfileActivity.class);
		startActivity(I);	
		finish();
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
            getAPI();
            getUserInfo();
        }

        public void onTwitterError(TwitterError e) { 
        	Log.e(TAG,"onTwitterError called for TwitterDialog", new Exception(e));
        	}

        public void onError(final DialogError e) { 
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					showErrorMessege();
					Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
					dialog.hide();
				}});
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
