package com.k1x.android.twitterlist.listviews;

import java.util.LinkedList;
import java.util.List;

import com.k1x.android.twitterlist.BaseActivity;
import com.k1x.android.twitterlist.R;
import com.k1x.android.twitterlist.TweetActivity;
import com.k1x.android.twitterlist.R.layout;
import com.k1x.android.twitterlist.TweetListActivity;
import com.k1x.android.twitterlist.UsersListActivity;
import com.k1x.android.twitterlist.entities.SlideMenuItem;
import com.k1x.android.twitterlist.entities.TweetData;
import com.k1x.android.twitterlist.entities.UserInfo;
import com.k1x.android.twitterlist.layouts.MenuListItem;
import com.k1x.android.twitterlist.layouts.TweetListItem;
import com.k1x.android.twitterlist.layouts.UserListItem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class UserListAdapter extends BaseAdapter {
	
    private LinkedList<UserInfo> list;
	private BaseActivity activity;

	public UserListAdapter(BaseActivity activity)
    {
		this.activity = activity;
    	list = new LinkedList<UserInfo>();      	
   	
    }
    
	public void add(UserInfo data)
    {
    	list.add(data);
    }
	
	public void addArray(UserInfo[] array)
	{
		for(UserInfo userInfo: array) {
			list.add(userInfo);
		}
	}
    
    public void clear()
    {
    	list.clear(); 	
    }
    
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int num) {
		return (list == null )? null :list.get(num);
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		UserListItem userCell;
		if(null == convertView)
		{
			userCell = (UserListItem)View.inflate(activity, R.layout.listview_userinfo, null);
		}
		else 
		{
			userCell = (UserListItem)convertView;
		}
		userCell.setUserCell(list.get(position));
		return userCell;
	}

    public BaseActivity getActivity() {
		return activity;
	}

	public void setActivity(BaseActivity activity) {
		this.activity = activity;
	}

	
}

