package com.xhc.numbergame.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.xhc.numbergame.R;

public class HelpActivity extends Activity {

	@Override
	public void onCreate(Bundle bundle){
		super.onCreate(bundle);
		ActivityCollection.push(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN); 
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		Intent intent = getIntent();
		int flag = Integer.parseInt(intent.getStringExtra("who"));
		if(flag == 1){setContentView(R.layout.helpshudu);}
		if(flag == 2){setContentView(R.layout.saoleihelp);}
		if(flag == 3){setContentView(R.layout.sorthelp);}
		
	}
}
