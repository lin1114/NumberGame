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
	/* ���� */
	private final static String TableName = "NuberGame";
	/* ��Ϸ���ֵ����� */
	private final static String GameName = "GameName";
	/* �����ݵĵط�������� */
	private final static String GameContent = "GameContent";
	/* ����Ϸ���Ѷ� */
	private final static String Level = "level";
	/* ����Ϸ������ʱ�� */
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

	/* �������� */
	public void insert(String gameName, String content, int level, int time) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "insert into " + TableName + " ( " + GameName + " , "
				+ GameContent + " , " + Level + " , " + Time +" ) "+ "  values ( '"
				+ gameName + "' , '" + content + "' , " + level + " , " + time
				+ ")";
		db.execSQL(sql);
	}


	/* ��ѯ����ѶȺ������Ϸ�Ƿ��д浵 ���ҷ���ʱ������ݵ�map */
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

	/* ������Ϸ�Ѷ� ����Ϸ���ָ��� */
	public void update(String gameName, int level, String content, int time) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "update " + TableName + " set " + GameContent + " = '"
				+ content + "'  ," + Time + " = " + time + " where " + GameName
				+ " = '" + gameName + "' and " + Level + " = " + level;
		db.execSQL(sql);
	}

	/* �Ȳ�ѯ���ݿ����Ƿ��������û����ôֱ�Ӳ��롣������˾͸��� */
	public void operation(String gameName, String content, int level, int time) {
		if (select(gameName, level) == null) {
			/* û�д浵 */
			insert(gameName, content, level, time);
		} else {
			/* ���д浵ֱ�Ӹ��� */
			update(gameName, level, content, time);
		}
	}

}













