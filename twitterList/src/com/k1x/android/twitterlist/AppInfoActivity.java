package com.k1x.android.twitterlist;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AppInfoActivity extends BaseActivity {

	
	private Button playStoreButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setRequiresInternet(false);
		super.onCreate(savedInstanceState, R.layout.activity_base_about);
	}

	@Override
	protected void onCreate() {
		setUpViews();
	}
	
	private void setUpViews() {
		playStoreButton = (Button) findViewById(R.id.button_playstore);
		playStoreButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse("market://details?id=com.k1x.android.twitterlist"));
				startActivity(intent);				
			}
		});
	}
}
