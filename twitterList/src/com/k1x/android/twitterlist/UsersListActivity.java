package com.k1x.android.twitterlist;

import com.k1x.android.twitterlist.constants.Constants;
import com.k1x.android.twitterlist.entities.UserInfo;
import com.k1x.android.twitterlist.entities.UserList;
import com.k1x.android.twitterlist.layouts.UserListItem;
import com.k1x.android.twitterlist.listviews.UserListAdapter;
import com.k1x.android.twitterlist.twitterutil.TweeterAPI;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class UsersListActivity extends BaseActivity {
	

	private static final String START_CURSOR_VALUE = "-1";
	private ListView userListView;
	private UserListAdapter userListAdapter;
	private int mode;
	private String userLogin;
	private boolean isLoading = false;
	private String cursor = START_CURSOR_VALUE;

	private UserInfo userInfo;
	private TwitterListApplication app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.activity_base_userlist);
        app = (TwitterListApplication) getApplication();
		mode =  getIntent().getIntExtra(Constants.KEY_MODE, TweeterAPI.FOLOWERS);
		System.out.println(userInfo);
		setUpViews();
	}

	private void setUpViews()
	{
		userListAdapter = new UserListAdapter(this, app.getUserList());
		userListView = (ListView) findViewById(R.id.userlist_listview);
		userListView.setAdapter(userListAdapter);
		userListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				UserListItem item = (UserListItem) arg1;
				Intent I = new Intent(UsersListActivity.this, UserProfileActivity.class);
				I.putExtra(Constants.USER_INFO, (Parcelable)item.getUserInfo());
				I.putExtra(Constants.USER_BITMAP, item.getUserInfo().getUserBitmap());
				startActivity(I);
				
			}
		});
		
        userListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				int loadedItems = firstVisibleItem + visibleItemCount;
				if((loadedItems == totalItemCount ) && !isLoading && !cursor.equals(START_CURSOR_VALUE)) {
					getList();
				}
			}
		});
		
	}

    @Override
	protected void onGettingUserInfo(UserInfo userInfo, Bitmap bitmap) {
    	userLogin = (String) getIntent().getStringExtra(Constants.KEY_USER_LOGIN);
		if(userLogin == null) {
			userLogin = userInfo.getScreen_name();
		}
		userListAdapter.clear();
    	getList();
	}

	private void getList() 
    {
		Thread T = new Thread(new Runnable() {
			private UserList result ;
						
			@Override
			public void run() {
				isLoading = true;
	            result = getTweeter().getFolowingsFolowers(mode, userLogin, cursor);
	            System.out.println(result);
	            
	            if(result.getUsers()!=null) {
	            for(UserInfo info: result.getUsers()) {
	            	System.out.println(info);
	            }
	            runOnUiThread(new Runnable() {
					@Override
					public void run() {
						userListAdapter.addArray(result.getUsers());
						userListAdapter.notifyDataSetChanged();
						app.setUserList(userListAdapter.getList());
					}});
	            }
				if (result.getNextCursorStr() != null) {
					cursor = result.getNextCursorStr();
				}
	            System.out.println("cursor = " + cursor);
	            isLoading = false;
			}		
		});
		T.start();
    }
}
