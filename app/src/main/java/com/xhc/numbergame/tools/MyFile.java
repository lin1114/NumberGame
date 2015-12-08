package com.xhc.numbergame.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;


public class MyFile{
/**
 * �������������ڵ��Դ���ʹ��
 * ������ֵ��쳣���͵���ʱд�����Ϣ
 * 1.�����쳣��ʹ�õ�ʱ����Ҫ���ļ��г����쳣��ʱ��ͳ����쳣ʱ�ڴ����е�λ��
 * 2.�ڵ���ʱ�����û���д����Ϣ�⣬����Ҫ���ļ���д����ֵ�ʱ��ͳ����ڴ����е�λ��
 * ����Ϊ��̬��
 */
    public static String path ;
    public static String name = "WrongLog.txt";
    public static Context context;
    /*���Դ������Ŀ¼���ǲ��ܴ����ļ�*/
	private static void createDirectory(){
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			File sdCardDir = Environment.getExternalStorageDirectory();
			File file = new File(sdCardDir,"NumberGame");
			path = file.toString();
			if(!file.exists()){
				try {
					/*ֻ�ܴ���Ŀ¼*/
					file.mkdirs();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else{
			Toast.makeText(context, "sdcard����", 1000).show();
		}
	}
	/*�����ļ�*/
	private static void createFile( ){
		File file = new File(path+"/"+name);
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	} 
	/*ֻ��Ҫ�����ļ�·�� �ļ���ͬһΪWrongLog.txt*/
	public static void createLogFile( ){
		createDirectory();
		createFile( );
	}
	/*���ļ���д�������Ϣ*/
	public static void writeLog(String where,String content){
		String detail = "*******************************Debug*****************************\r\nTIME��";
		detail += new Date() +" \r\n POSITION:";
		detail += where+" \r\n CONTENT:";
		detail += content +"\r\n";
		File file = new File(path+"/"+name);
		FileOutputStream fileOutputStream = null;
		
		try {
			fileOutputStream = new FileOutputStream(file,true);
			byte[] buffer = detail.getBytes("GBK");
			/*������������еĵڼ���λ�ÿ�ʼд*/
			fileOutputStream.write(buffer, 0, buffer.length);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if( fileOutputStream != null){
				try{
					fileOutputStream.close();
				}catch(Exception e){}
				
			}
	    }
	}
	
	/*���û���׽���쳣д�����������*/
	public static void writeException(String where,String content){
		String detail = "*******************************EXCEPTION******************************\r\nʱ�䣺";
		detail += new Date() +" \r\n POSITION:";
		detail += where+" \r\n EXCEPTION:";
		detail += content +"\r\n";
		File file = new File(path+"/"+name);
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(file,true);
			byte[] buffer = detail.getBytes();
			/*������������еĵڼ���λ�ÿ�ʼд*/
			fileOutputStream.write(buffer, 0, buffer.length);
		} catch (Exception e) {
		}finally{
			if( fileOutputStream != null){
				try{
					fileOutputStream.close();
				}catch(Exception e){}
				
			}
	    }
	}
}
