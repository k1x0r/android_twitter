package com.k1x.android.twitterlist.listviews;

import java.util.LinkedList;

import com.k1x.android.twitterlist.BaseActivity;
import com.k1x.android.twitterlist.R;
import com.k1x.android.twitterlist.entities.UserInfo;
import com.k1x.android.twitterlist.httputil.UserImageDownloader;
import com.k1x.android.twitterlist.layouts.UserListItem;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class UserListAdapter extends BaseAdapter implements IPostDataChange {
	
    private LinkedList<UserInfo> list;


	private BaseActivity activity;

	public UserListAdapter(BaseActivity activity, LinkedList<UserInfo> list)
    {
		this.activity = activity;
		if(list!=null) {
			this.list = list;
		} else {
			this.list = new LinkedList<UserInfo>();      	
		}
    }
    
	public void add(UserInfo data)
    {
    	list.add(data);
    	UserImageDownloader downloader = new UserImageDownloader(activity, data, this);
    	downloader.start();
    }
	
	public void addArray(UserInfo[] array)
	{
		for(UserInfo userInfo: array) {
			add(userInfo);
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
    public void postNotifyDataSetChanged() {
    	activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				notifyDataSetChanged();
			}
		});
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

	public LinkedList<UserInfo> getList() {
		return list;
	}
}

