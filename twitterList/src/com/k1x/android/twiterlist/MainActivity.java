package com.k1x.android.twiterlist;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class MainActivity extends Activity {

    private Button tweetListButton;
	private EditText loginEditText;
	private EditText passwordEditText;
	private CheckBox saveDataCheckBox;
	private Button loginButton;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    private void setUpViews()
    {
        setContentView(R.layout.activity_main);
        tweetListButton = (Button)findViewById(R.id.tweet_list_button);
        
        tweetListButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, TweetListActivity.class);
		        startActivity(intent);
			}
		});
        
        loginEditText = (EditText) findViewById(R.id.editText_login);
        passwordEditText = (EditText) findViewById(R.id.editText_password);
        saveDataCheckBox = (CheckBox) findViewById(R.id.checkBox_saveData);
        loginButton = (Button) findViewById(R.id.button_login);
        
        loginButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				System.out.println(String.format("login: %s password: %s savedataCheckBox: %b", 
						loginEditText.getText().toString(), passwordEditText.getText().toString(), String.valueOf(saveDataCheckBox.isChecked())));
			}
		});
    }
}
