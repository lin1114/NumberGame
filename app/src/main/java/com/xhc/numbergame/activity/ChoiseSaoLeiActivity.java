package com.xhc.numbergame.activity;

import com.xhc.numbergame.R;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

public class ChoiseSaoLeiActivity extends Activity{
	private float downX = 0,downY = 0,upX = 0 ,upY = 0 ;
	@Override
	public void onCreate(Bundle bundle){
		super.onCreate(bundle);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN); 
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.saoleichoise);
		ActivityCollection.push(this);
	}


	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
        switch(event.getAction()){
        case MotionEvent.ACTION_DOWN:
        	downX = event.getX();
        	downY = event.getY();
        	break;
        case MotionEvent.ACTION_UP:
        	upX = event.getX();
        	upY = event.getY();
        	if(Math.abs(upX - downX) > Math.abs(upY - downY)){
        		if(upX - downX > 0){
        			/*����*/
        			 
        		}
        		else{
        			/*����*/
        			Intent intent = new Intent(ChoiseSaoLeiActivity.this,ChoiseShudukuActivity.class);
        			startActivity(intent);
        		}
        	}
        	break;
        }
		return false;
	}
	 
}
