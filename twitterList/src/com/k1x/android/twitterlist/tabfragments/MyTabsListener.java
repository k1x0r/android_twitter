package com.k1x.android.twitterlist.tabfragments;

import com.k1x.android.twitterlist.BaseActivity;
import com.k1x.android.twitterlist.R;

import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.widget.Toast;
import android.app.ActionBar;

public class MyTabsListener implements ActionBar.TabListener {
	public Fragment fragment;
	private BaseActivity activity;

	public MyTabsListener(Fragment fragment, BaseActivity activity) {
		this.activity = activity;
		this.fragment = fragment;
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		Toast.makeText(activity, "Reselected!", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
	//	ft.replace(R.id.fragment_container, fragment);
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		ft.remove(fragment);
	}

}