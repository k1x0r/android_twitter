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


    public TweetListAdapter(BaseActivity activity)
    {
		this.activity = (BaseActivity)activity;
    	list = new LinkedList<TweetData>();   	
    }
    
    public void add(TweetData data)
    {
    	list.add(data);
    	UserImageDownloader downloader = new UserImageDownloader(data.getUser(), this);
    	downloader.start();
    	if(data.getRetweeted_status()!=null) {
        	UserImageDownloader downloaderRT = new UserImageDownloader(data.getRetweeted_status().getUser(), this);
        	downloaderRT.start();
    	}
    		
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
	
	
}

