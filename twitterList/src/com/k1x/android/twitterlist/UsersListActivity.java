package com.k1x.android.twitterlist;

import com.k1x.android.twitterlist.constants.Constants;
import com.k1x.android.twitterlist.entities.UserInfo;
import com.k1x.android.twitterlist.entities.UserList;
import com.k1x.android.twitterlist.layouts.UserListItem;
import com.k1x.android.twitterlist.listviews.UserListAdapter;
import com.k1x.android.twitterlist.twitterutil.Tweeter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class UsersListActivity extends BaseActivity {
	

	
	private ListView userListView;
	private UserListAdapter userListAdapter;
	private int mode;
	private String userLogin;

	private UserInfo userInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.activity_base_userlist);
		userLogin = (String) getIntent().getStringExtra(Constants.KEY_USER_LOGIN);
		mode =  getIntent().getIntExtra(Constants.KEY_MODE, 0);
		System.out.println(userInfo + "");
		setUpViews();
		mode = getIntent().getIntExtra("mode", Tweeter.FOLOWERS);
	}

	private void setUpViews()
	{
		userListAdapter = new UserListAdapter(this);
		userListView = (ListView) findViewById(R.id.userlist_listview);
		userListView.setAdapter(userListAdapter);
		userListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				UserListItem item = (UserListItem) arg1;
				Intent I = new Intent(UsersListActivity.this, UserProfileActivity.class);
				I.putExtra(Constants.USER_INFO, item.getUserInfo());
				I.putExtra(Constants.USER_BITMAP, item.getUserInfo().getUserBitmap());
				startActivity(I);
				
			}
		});
		getList();
	}

    private void getList() 
    {
		Thread T = new Thread(new Runnable() {
			private UserList result ;
						
			@Override
			public void run() {
	            result = getTweeter().getFolowingsFolowers(mode, userLogin);
	            if(result.getUsers()!=null) {
	            for(UserInfo info: result.getUsers()) {
	            	System.out.println(info);
	            }
	            runOnUiThread(new Runnable() {
					@Override
					public void run() {
						userListAdapter.addArray(result.getUsers());
						userListAdapter.notifyDataSetChanged();
					}});
	            }
			}		
		});
		T.start();
    }
}
