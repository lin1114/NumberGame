package com.xhc.numbergame.activity;

import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.xhc.numbergame.R;
import com.xhc.numbergame.bean.NumberGame;
import com.xhc.numbergame.entity.ArrayMap;
import com.xhc.numbergame.entity.NumAloneArray;
import com.xhc.numbergame.entity.NumAloneArray.NumNode;
import com.xhc.numbergame.tools.DbHelper;
import com.xhc.numbergame.tools.GameName;
import com.xhc.numbergame.tools.MyFile;
import com.xhc.numbergame.view.NumAloneSurfaceView;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class NumAloneActivity extends Activity {
	private NumAloneSurfaceView view;
	private NumAloneArray numAloneArray;
	private Thread loadData;
	private int level;
	private DbUtils dbUtils;
	private DbHelper dbHelper;
	private Gson gson;
	private NumberGame numberGame;

	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.loadsudoku);
		Intent intent = getIntent();
		level = Integer.parseInt(intent.getStringExtra("level"));
		dbHelper = new DbHelper(this);
		dbUtils = DbUtils.create(NumAloneActivity.this);
		gson = new Gson();
	}

	/**
	 * 数据结构使用的是gson 数据库部分使用的三方xutil
	 * 
	 * @param i
	 */
	@SuppressWarnings("unused")
	public void getArray(int i) {
		try {
			numberGame = dbUtils.findFirst(
					Selector.from(NumberGame.class).where("gameName", "=", GameName.Suduku).and("Level", "=", level));
			// Log.e("xhc", "--- id --- " + numberGame);

			if (numberGame != null) {

				ArrayMap.time = 0;
				ArrayMap.dataBaseTime = Integer.parseInt(numberGame.getTime());
				numAloneArray = new NumAloneArray(9, level);
				NumNode[][] tempNodeArray = gson.fromJson(numberGame.getGameContent(), NumNode[][].class);
				numAloneArray.setArray(tempNodeArray);
				view = new NumAloneSurfaceView(this, handler, numAloneArray);
				Message msg = new Message();
				msg.arg1 = 1;
				handler.sendMessage(msg);

			} else {
				ArrayMap.time = 0;
				ArrayMap.dataBaseTime = 0;
				numAloneArray = new NumAloneArray(9, level);
				view = new NumAloneSurfaceView(this, handler, numAloneArray);
				loadData = new LoadData();
				loadData.start();
			}

		} catch (Exception e) {
			Log.e("xhc", e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 自己的写的原生的数据库方式保存数组部分的数据结构是自己写的
	 * 
	 * system,usernum,flag,1@3@#system,usernum,flag,1@3@#
	 * 系统生成数system,用户数，标志，候选数#下个节点
	 */
	public void getArray() {
		Map<Integer, String> map;
		int time = 0;
		if ((map = dbHelper.select(GameName.Suduku, level)) != null) {
			Set<Integer> set = map.keySet();
			for (Integer a : set) {
				time = a;
			}
			ArrayMap.time = 0;
			ArrayMap.dataBaseTime = time;
			String content = map.get(time);
			numAloneArray = new NumAloneArray(9, level);
			numAloneArray.setArray(content);
			view = new NumAloneSurfaceView(this, handler, numAloneArray);
			Message msg = new Message();
			msg.arg1 = 1;
			handler.sendMessage(msg);
		} else {
			ArrayMap.time = 0;
			ArrayMap.dataBaseTime = 0;
			numAloneArray = new NumAloneArray(9, level);
			view = new NumAloneSurfaceView(this, handler, numAloneArray);
			loadData = new LoadData();
			loadData.start();
		}
	}

	public void onResume() {
		super.onResume();
		getArray(1);
	}

	
	public void onDestroy() {
		super.onDestroy();
	}

	private void save2Db() {
		try {
            if(numberGame == null){
            	numberGame = new NumberGame();
            }
			NumNode[][] node = numAloneArray.getArray();
			String str = gson.toJson(node);
			numberGame.setGameContent(str);
			numberGame.setLevel(level + "");
			numberGame.setGameName(GameName.Suduku);
			numberGame.setTime(ArrayMap.time + "");
			dbUtils.saveOrUpdate(numberGame);

			// MyFile.writeLog(MyFile.path,str );
			// Log.e("xhc", str);
		} catch (Exception e) {
			MyFile.writeException(MyFile.path, e.getMessage());
			e.printStackTrace();
		}
	}

	public void onStop() {
		super.onStop();
		save2Db();

		// NumNode[][] node = numAloneArray.getArray();
		// String str = gson.toJson(node);
		//
		// String content = "";
		// for (int i = 0; i < node.length; ++i) {
		// for (int j = 0; j < node[i].length; ++j) {
		// content += node[i][j].systemNum;
		// content += ",";
		// content += node[i][j].userNum;
		// content += ",";
		// content += node[i][j].flag;
		// content += ",";
		// for (int back = 0; back < node[i][j].getBack().size(); ++back) {
		// content += node[i][j].getBack().get(back);
		// content += "@";
		// }
		// content += "#";
		// }
		// }
		// dbHelper.operation(GameName.Suduku, content, level, ArrayMap.time);
		// finish();
	}

	class LoadData extends Thread {

		@Override
		public void run() {
			numAloneArray.begin();
			Message msg = new Message();
			msg.arg1 = 1;
			handler.sendMessage(msg);

		}
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.arg1 == 1) {
				setContentView(view);
			} else if (msg.arg1 == 2) {
				setContentView(R.layout.loadsudoku);
				new LoadData().start();

			}
		}
	};
}
