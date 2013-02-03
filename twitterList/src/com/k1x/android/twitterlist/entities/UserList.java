package com.k1x.android.twitterlist.entities;

public class UserList {
	
	private long previous_cursor;
	private long next_cursor;
	private UserInfo[] users;

	public long getPrevious_cursor() {
		return previous_cursor;
	}
	public void setPrevious_cursor(int previous_cursor) {
		this.previous_cursor = previous_cursor;
	}
	public long getNext_cursor() {
		return next_cursor;
	}
	public void setNext_cursor(int next_cursor) {
		this.next_cursor = next_cursor;
	}
	public UserInfo[] getUsers() {
		return users;
	}
	public void setUsers(UserInfo[] users) {
		this.users = users;
	}

}
