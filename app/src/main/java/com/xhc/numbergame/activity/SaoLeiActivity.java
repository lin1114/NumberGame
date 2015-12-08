package com.xhc.numbergame.activity;

import java.util.Map;
import java.util.Set;

import com.lidroid.xutils.DbUtils;
import com.xhc.numbergame.entity.ArrayMap;
import com.xhc.numbergame.entity.SaoLeiArray;
import com.xhc.numbergame.entity.SaoLeiArray.Node;
import com.xhc.numbergame.tools.DbHelper;
import com.xhc.numbergame.tools.GameName;
import com.xhc.numbergame.tools.MyFile;
import com.xhc.numbergame.view.SaoLeiSurfaceView;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class SaoLeiActivity extends Activity {

	private int x,y,bombNum;
	private	SaoLeiArray saoleiArray;
	private DbHelper dbHelper ;
	private int level;
	private DbUtils dbUtils ;
	@Override
	public void onCreate(Bundle bundle){
		super.onCreate(bundle);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN); 
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		Intent intent = getIntent();
		level = Integer.parseInt(intent.getStringExtra("level"));
		if(level == 1){x = 11; y = 11; bombNum = 13;}
		else if(level == 2){x = 12; y = 11; bombNum = 16;}
		else{x = 13; y = 11; bombNum = 27;}
		dbHelper  = new DbHelper(this);
		dbUtils = DbUtils.create(SaoLeiActivity.this);
		
	}
	
	private void getArray(){
		
		
	}
	
	private void getSaoLeiArray(){
		/*如果这个游戏并且这个难度有存档那么直接获取*/
		Map<Integer,String> map;
		int time = 0;
		if((map = dbHelper.select("扫雷", level)) != null){
			Set<Integer> set = map.keySet();
			for(Integer a : set){
				time = a;
			}
			ArrayMap.time = 0 ;
			ArrayMap.dataBaseTime = time;
			String content = map.get(time);
			saoleiArray = new SaoLeiArray(x,y,bombNum);
			saoleiArray.setArray(content);
			
		}
		else{
			ArrayMap.time = 0 ;
			ArrayMap.dataBaseTime = 0;
			saoleiArray = new SaoLeiArray(x,y,bombNum);
			saoleiArray.begin();
		}
	}
	
	public void onResume(){
		super.onResume();
		getSaoLeiArray();
		SaoLeiSurfaceView saoLei  = new SaoLeiSurfaceView(this,saoleiArray);
		setContentView(saoLei);
		
	}
	
	public void onDestroy(){
		super.onDestroy();
	}
	/**
	 * 退出后保存信息
	 * gamename = 扫雷
	 * content = e.g clickflag^num#clickflag^num#
	 * 
	 * 
	 * */
	public void onStop(){
		super.onStop();
		 Node node[][] = saoleiArray.getSaoLeiNodeArray();
		   String content  = "";
		   for(int i = 0 ;i < node.length ;i ++){
			   for(int j = 0 ;j < node[i].length ; j ++){
				   content += node[i][j].clickFlag;
				   content += ",";
				   content += node[i][j].num;
				   content += "#";
			   }
		   }
		   dbHelper.operation(GameName.SaoLei,content , level, ArrayMap.time);
	    finish();
	}
}























