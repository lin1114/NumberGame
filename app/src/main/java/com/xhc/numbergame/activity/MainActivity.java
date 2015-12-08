package com.xhc.numbergame.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.xhc.numbergame.R;
import com.xhc.numbergame.tools.Constant;
import com.xhc.numbergame.tools.MyFile;
import com.xhc.numbergame.view.Welcome;

public class MainActivity extends Activity {
	LinearLayout numberGame;
	int where = 1;
	Welcome welcome;
	loading loadView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		Constant.height = dm.heightPixels;
		Constant.width = dm.widthPixels;
		Constant.density = dm.density;
		Constant.densityDpi = dm.densityDpi;
		MyFile.context = this;
		MyFile.createLogFile();
		welcome = new Welcome(this, handler);
		setContentView(welcome);
		loadView = new loading();
		loadView.start();
	}

	class loading extends Thread {
		boolean flag = true;

		@Override
		public void run() {
			while (where < 7 && flag) {
				try {
					Thread.sleep(2000 / 7);
					welcome.postInvalidate();
					where++;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (flag) {
				Message msg = new Message();
				msg.arg1 = 1;
				handler.sendMessage(msg);
			}

		}
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.arg1 == 1) {
				Intent intent = new Intent(MainActivity.this,
						ChoiseActivity.class);
				startActivity(intent);
				finish();
			}

		}
	};

	public void onDestroy() {
		super.onDestroy();
		/* Í£Ö¹Ïß³Ì */
		loadView.flag = false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == event.KEYCODE_BACK) {
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
}











