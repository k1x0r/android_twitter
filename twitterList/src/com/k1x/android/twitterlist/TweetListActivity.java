package com.k1x.android.twitterlist;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.k1x.android.twiterlist.R;
import com.k1x.android.twitterlist.httputil.HTTPUtil;
import com.k1x.android.twitterlist.jsonobj.TweetData;
import com.k1x.android.twitterlist.jsonobj.UserInfo;
import com.k1x.android.twitterlist.twitterutil.Tweeter;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TweetListActivity extends ListActivity {

	private InputStream is;
	private ArrayList<TweetData> tweetData;
	private TweetListAdapter listAdapter;
	private ListView listView;
	private Tweeter tweeter;
	private TwitterListApplication app;
	private Button loginButton;
	private Button logoutButton;
	private TextView userNameTextField;
	private ImageView userAvatar;
	private RelativeLayout userInfoLayout;
	private EditText searchTweetsEditText;
	private Button searchTweetsButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        app = (TwitterListApplication) getApplication();
		setUpViews();
		setUpData();
		getUserInfo();
		loadTweetsAction("and1_john");

	}
	
	private void setUpViews() {
		setContentView(R.layout.activity_tweetlist);
		Button closeButton = (Button)findViewById(R.id.close_button);
		closeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
	    listAdapter = new TweetListAdapter(this);  
		listView = (ListView) findViewById(android.R.id.list);
		listView.setAdapter(listAdapter);
		
		userInfoLayout = (RelativeLayout) findViewById(R.id.tl_userloginDataLayout);
		loginButton = (Button) findViewById(R.id.tl_loginbutton);
		logoutButton = (Button) findViewById(R.id.tl_logoutbutton);
		userNameTextField = (TextView) findViewById(R.id.tl_username);
		userAvatar = (ImageView) findViewById(R.id.tl_useravatar);

		searchTweetsEditText = (EditText) findViewById(R.id.tl_searchText);
		searchTweetsButton = (Button) findViewById(R.id.tl_searchButton);
		searchTweetsButton.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				loadTweetsAction(searchTweetsEditText.getText().toString());
			}


		});
		
		
		
		
	}
	
	private void loadTweetsAction(final String name) {
		Thread T = new Thread(new Runnable() {
			@Override
			public void run() {
				listAdapter.clear();
				loadTweets(name);
//				listAdapter.notifyDataSetChanged();
				listView.postInvalidate();
			}});
		T.start();		
	}
	
	private void setUpData() {
		tweeter = new Tweeter(app.getAccessToken(), app.getSecretToken(), TweetListActivity.this);
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
					}});
				}
			}
			
		});
		T.start();
    }
	
	private void loadTweets(String username)
	{
		try {
		    HttpParams params = new BasicHttpParams();
		    HttpConnectionParams.setSoTimeout(params, 0);
		    HttpClient httpClient = new DefaultHttpClient(params);

		    HttpGet httpget = new HttpGet(
		    		String.format("https://api.twitter.com/1/statuses/user_timeline.json?include_entities=true&include_rts=true&screen_name=%s&count=10", 
		    		username));
		    HttpEntity entity = httpClient.execute(httpget).getEntity();
		    if (entity != null) {
		    	
		    	is = entity.getContent();
		    	InputStreamReader reader = new InputStreamReader(is);
		        
		        Gson gson = new GsonBuilder().create();
		        Type collectionType = new TypeToken<ArrayList<TweetData>>(){}.getType();
		        tweetData = gson.fromJson(reader, collectionType);
		        	
		        runOnUiThread(new Runnable() {

					@Override
					public void run() {
		  		        for(TweetData data: tweetData)
				        	listAdapter.add(data);
		  		        
		  		        	listAdapter.notifyDataSetChanged();
					}});

		        System.out.println(tweetData);
		        
		        
		        httpClient.getConnectionManager().shutdown();

		    }
		}
		catch (JsonSyntaxException e)
		{
	        runOnUiThread(new Runnable() {       
			@Override
			public void run() {
				Toast toast = Toast.makeText(getApplicationContext(), 
						   "Page does not exist!", Toast.LENGTH_SHORT);			
				toast.show();  
	        }});        
		}
		catch (Exception e) {
		    e.printStackTrace();
		}
	}
	


}
