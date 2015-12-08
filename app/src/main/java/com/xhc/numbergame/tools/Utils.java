package com.xhc.numbergame.tools;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.xhc.numbergame.R;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Utils {
	 public static final String NORMAL="normal";
	 public static final String THUMB="thumb";

	/**
	 * �?查是否有网络
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetWorkAvailable(Context context) {
		if (context == null) {
			return false;
		}
		ConnectivityManager mgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (mgr != null) {
			NetworkInfo info = mgr.getActiveNetworkInfo();
			if (info != null && info.getState() == NetworkInfo.State.CONNECTED) {
				return true;
			}
		}
		return false;
	}

	public static String getStringDate(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		return format.format(date);
	}

	public static boolean isEmail(String email, Context context) {
		Pattern pattern = Pattern
				.compile("\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
		Matcher matcher = pattern.matcher(email); // 验证用户名是否匹配邮�?
		Log.v("email", "-----------email:"+email);
		if (!matcher.matches() || TextUtils.isEmpty(email)) {
			Toast.makeText(context, "�����ʽ����", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
	
	public static void setListViewHeightBasedOnChildren(ListView listView) {
	    if(listView == null) return;
	    ListAdapter listAdapter = listView.getAdapter();
	    if (listAdapter == null) {
	        // pre-condition
	        return;
	    }
	    int totalHeight = 0;
	    for (int i = 0; i < listAdapter.getCount(); i++) {
	        View listItem = listAdapter.getView(i, null, listView);
	        listItem.measure(0, 0);
	        totalHeight += listItem.getMeasuredHeight();
	    }
	    ViewGroup.LayoutParams params = listView.getLayoutParams();
	    params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() -1));
	    listView.setLayoutParams(params);
	}

	/**
	 * 得到当前时间
	 */
	public static String getCurTime()
	{
		Calendar c = Calendar.getInstance();
		String curTime = c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH) + " " + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND);
		return formatTime(curTime);
	}
	/**
	 * 创建文件�?
	 * 
	 * @param dirName
	 */
	public static void MakeDir(String dirName)
	{
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		{
			File destDir = new File(dirName);
			if (!destDir.exists())
			{
				destDir.mkdirs();
			}
		}
	}
	public static String formatTime(String time)
	{
		String returnTime = "";
		String[] first = time.split(" ");
		String[] hour = first[1].split(":");
		String[] years = first[0].split("-");

		for (String s : years)
		{
			if (Integer.parseInt(s) < 10)
			{
				if (s.equals("00"))
				{
					returnTime += s + "-";
				}
				else if (Integer.parseInt(s) == 0)
				{
					returnTime += "0" + s + "-";
				}
				else if (s.contains("0"))
				{
					returnTime += s + "-";
				}
				else
				{
					returnTime += "0" + s + "-";
				}
			}
			else
			{
				returnTime += s + "-";
			}
		}
		returnTime = returnTime.substring(0, returnTime.length() - 1);
		returnTime = returnTime + " ";
		for (String s : hour)
		{
			if (Integer.parseInt(s) < 10)
			{
				if (s.equals("00"))
				{
					returnTime += s + ":";
				}
				else if (Integer.parseInt(s) == 0)
				{
					returnTime += "0" + s + ":";
				}
				else if (s.contains("0"))
				{
					returnTime += s + ":";
				}
				else
				{
					returnTime += "0" + s + ":";
				}
			}
			else
			{
				returnTime += s + ":";
			}
		}
		returnTime = returnTime.substring(0, returnTime.length() - 1);
		return returnTime;
	}
	
}
