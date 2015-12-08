package com.xhc.numbergame.activity;

import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;

import com.xhc.numbergame.R;
import com.xhc.numbergame.entity.ArrayMap;
import com.xhc.numbergame.entity.Num;
import com.xhc.numbergame.entity.SortArrayGame;
import com.xhc.numbergame.tools.DbHelper;
import com.xhc.numbergame.tools.GameName;
import com.xhc.numbergame.view.SortSurfaceView;

public class SortActivity extends Activity {
	/**
	 * numCount 是从用户手中传过来的
	 */
	private Num number;
	private SortArrayGame sortArray;
	private DbHelper dbHelper;
	int numCount = 6;
	int level;
	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			if (msg.arg1 == 1) {
			}
		}
	};

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.loading);
		ActivityCollection.push(this);
		Intent intent = getIntent();
		level = Integer.parseInt(intent.getStringExtra("level"));
		dbHelper = new DbHelper(this);
		if (level == 1) {
			numCount = 4;
		}
		if (level == 2) {
			numCount = 5;
		}
		if (level == 3) {
			numCount = 6;
		}
		
	}

	public void getArray() {
		/* 如果这个游戏并且这个难度有存档那么直接获取 */
		Map<Integer, String> map;
		int time = 0;
		if ((map = dbHelper.select(GameName.sortGame, level)) != null) {
			Set<Integer> set = map.keySet();
			for (Integer a : set) {
				time = a;
			}
			ArrayMap.time = 0 ;
			ArrayMap.dataBaseTime = time;
			String content = map.get(time);
			sortArray = new SortArrayGame(numCount, level, handler);
			sortArray.setArray(content);
			number = new Num(sortArray);
		} else {
			ArrayMap.time = 0 ;
			ArrayMap.dataBaseTime = 0;
			sortArray = new SortArrayGame(numCount, level, handler);
			sortArray.begin();
			number = new Num(sortArray);
		}
	}

	@Override
	public void onStop(){
		super.onStop();
		  /*原始数组*/
				int[][] array = sortArray.getOriginalArray();
		        /*打乱后的数组*/
				int[][] confuseArray = sortArray.getArray();
		        String content ="";
		        for(int i = 0 ;i < array.length ; ++ i){
		        	for(int j = 0 ; j < array[i].length ;++ j){
		        		content += array[i][j];
		        		content += ",";
		        	}
		        }
		        content += "#";
		        for(int i = 0 ;i < array.length ; ++ i){
		        	for(int j = 0 ; j < array[i].length ; ++ j){
		        		content += confuseArray[i][j];
		        		content += ",";
		        	}
		        }
		        
		        
		        dbHelper.operation(GameName.sortGame,content , level, ArrayMap.time);
		        finish();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		getArray();
		SortSurfaceView sortFaceView = new SortSurfaceView(SortActivity.this,
				numCount, number, sortArray);
		setContentView(sortFaceView);
	}

	/**
	 * 原始数组#打乱数组
	 * 数组中 用，
	 * 先存正常的数组
	 * 再存混乱的数组
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}

















