package com.k1x.android.twitterlist.entities;

import com.google.gson.annotations.SerializedName;

public class SearchData {

	@SerializedName("statuses")
	private TweetData[] tweets;
	
	@SerializedName("search_metadata")
	private SearchMetadata searchData;
	
	public TweetData[] getTweets() {
		return tweets;
	}

	public void setTweets(TweetData[] tweets) {
		this.tweets = tweets;
	}

	public SearchMetadata getSearchData() {
		return searchData;
	}

	public void setSearchData(SearchMetadata searchData) {
		this.searchData = searchData;
	}

	public class SearchMetadata {
		@SerializedName("max_id")
		private long maxId;
		
		@SerializedName("since_id")
		private long sinceId;
		
		private int count;
		
		public long getMaxId() {
			return maxId;
		}
		public void setMaxId(long maxId) {
			this.maxId = maxId;
		}
		public long getSinceId() {
			return sinceId;
		}
		public void setSinceId(long sinceId) {
			this.sinceId = sinceId;
		}
		public int getCount() {
			return count;
		}
		public void setCount(int count) {
			this.count = count;
		}
	}
	
}
