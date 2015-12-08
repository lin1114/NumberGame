package com.xhc.numbergame.tools;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {

	private final static String DatabaseName = "NumberGame";
	private final static int DatabaseVersion = 1;
	/* 表名 */
	private final static String TableName = "NuberGame";
	/* 游戏名字的列名 */
	private final static String GameName = "GameName";
	/* 存数据的地方。数组等 */
	private final static String GameContent = "GameContent";
	/* 存游戏的难度 */
	private final static String Level = "level";
	/* 存游戏所花的时间 */
	private final static String Time = "Time";
    private Context context ;
	@SuppressLint("NewApi")
	public DbHelper(Context context) {
		super(context, DatabaseName, null, 1);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table " + TableName + " ( " + GameName + " text, "
				+ GameContent + " text, " + Level + " integer, " + Time
				+ " integer " + " ) ";
		db.execSQL(sql);
//		db.close();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	/* 插入数据 */
	public void insert(String gameName, String content, int level, int time) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "insert into " + TableName + " ( " + GameName + " , "
				+ GameContent + " , " + Level + " , " + Time +" ) "+ "  values ( '"
				+ gameName + "' , '" + content + "' , " + level + " , " + time
				+ ")";
		db.execSQL(sql);
	}


	/* 查询这个难度和这个游戏是否有存档 并且返回时间和内容的map */
	public Map select(String gameName, int level) {
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "select " + GameContent+" , "+Time  + " from "
				+ TableName +" where "+GameName+" = '"+gameName+"' and "+Level+" = "+level;
		
		Cursor cursor = db.rawQuery(sql, null);
		if(cursor.moveToFirst()){
           Map<Integer,String> map = new HashMap();
           map.put(Integer.parseInt(cursor.getString(1)), cursor.getString(0));
           return map;
		}
		
		return null;
	}

	/* 根据游戏难度 和游戏名字更新 */
	public void update(String gameName, int level, String content, int time) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "update " + TableName + " set " + GameContent + " = '"
				+ content + "'  ," + Time + " = " + time + " where " + GameName
				+ " = '" + gameName + "' and " + Level + " = " + level;
		db.execSQL(sql);
	}

	/* 先查询数据库中是否有了如果没有那么直接插入。如果有了就更新 */
	public void operation(String gameName, String content, int level, int time) {
		if (select(gameName, level) == null) {
			/* 没有存档 */
			insert(gameName, content, level, time);
		} else {
			/* 已有存档直接更新 */
			update(gameName, level, content, time);
		}
	}

}













