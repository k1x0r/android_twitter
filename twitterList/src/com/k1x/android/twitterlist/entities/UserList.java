package com.k1x.android.twitterlist.entities;

import java.util.Arrays;

public class UserList {
	
	private long previous_cursor;
	private String previous_cursor_str;

	private long next_cursor;
	private String next_cursor_str;


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
	
	public String getPreviousCursorStr() {
		return previous_cursor_str;
	}
	public void setPreviousCursorStr(String previous_cursor_str) {
		this.previous_cursor_str = previous_cursor_str;
	}
	public String getNextCursorStr() {
		return next_cursor_str;
	}
	@Override
	public String toString() {
		return "UserList [previous_cursor=" + previous_cursor
				+ ", previous_cursor_str=" + previous_cursor_str
				+ ", next_cursor=" + next_cursor + ", next_cursor_str="
				+ next_cursor_str + ", users=" + Arrays.toString(users) + "]";
	}
	public void setNextCursorStr(String next_cursor_str) {
		this.next_cursor_str = next_cursor_str;
	}

}
