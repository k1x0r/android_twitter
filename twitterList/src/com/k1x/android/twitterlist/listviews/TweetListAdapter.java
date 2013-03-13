package com.k1x.android.twitterlist.listviews;

import java.util.LinkedList;

import com.k1x.android.twitterlist.BaseActivity;
import com.k1x.android.twitterlist.R;
import com.k1x.android.twitterlist.entities.TweetData;
import com.k1x.android.twitterlist.httputil.UserImageDownloader;
import com.k1x.android.twitterlist.layouts.TweetListItem;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class TweetListAdapter extends BaseAdapter implements IPostDataChange {
	
    private LinkedList<TweetData> list;


	private BaseActivity activity;

    public TweetListAdapter(BaseActivity activity, LinkedList<TweetData> list)
    {
		this.activity = (BaseActivity)activity;
		if(list!=null) {
			System.out.println("List!=null");
			this.list = list;
		} else {
	    	this.list = new LinkedList<TweetData>();   	
		}
    }
    
    public void add(TweetData data)
    {
    	if(data.getRetweeted_status()!=null) {
    		data = data.getRetweeted_status();
    	}
    	
    	list.add(data);
    	UserImageDownloader downloader = new UserImageDownloader(data.getUser(), this);
    	downloader.start();
    }
        
    public TweetData getLast()
    {
    	return list.getLast(); 	
    }
    
    public void clear()
    {
    	list.clear(); 	
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
		TweetListItem tweetItem;
		if(null == convertView)
		{
			tweetItem = (TweetListItem)View.inflate(activity, R.layout.listview_tweetrow, null);
		}
		else 
		{
			tweetItem = (TweetListItem)convertView;
		}
		tweetItem.setTweet(list.get(position));
		return tweetItem;
	}

	public LinkedList<TweetData> getList() {
		return list;
	}
	
}

