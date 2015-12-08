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
 * 此类用来到后期调试代码使用
 * 程序出现的异常。和调试时写入的信息
 * 1.当在异常中使用的时候需要在文件中出现异常的时间和出现异常时在代码中的位置
 * 2.在调试时出现用户填写的信息外，还需要在文件中写入出现的时间和出现在代码中的位置
 * 此类为静态类
 */
    public static String path ;
    public static String name = "WrongLog.txt";
    public static Context context;
    /*可以创建多层目录但是不能创建文件*/
	private static void createDirectory(){
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			File sdCardDir = Environment.getExternalStorageDirectory();
			File file = new File(sdCardDir,"NumberGame");
			path = file.toString();
			if(!file.exists()){
				try {
					/*只能创建目录*/
					file.mkdirs();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else{
			Toast.makeText(context, "sdcard有误", 1000).show();
		}
	}
	/*创建文件*/
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
	/*只需要给出文件路径 文件名同一为WrongLog.txt*/
	public static void createLogFile( ){
		createDirectory();
		createFile( );
	}
	/*往文件中写入调试信息*/
	public static void writeLog(String where,String content){
		String detail = "*******************************Debug*****************************\r\nTIME：";
		detail += new Date() +" \r\n POSITION:";
		detail += where+" \r\n CONTENT:";
		detail += content +"\r\n";
		File file = new File(path+"/"+name);
		FileOutputStream fileOutputStream = null;
		
		try {
			fileOutputStream = new FileOutputStream(file,true);
			byte[] buffer = detail.getBytes("GBK");
			/*是重这个数组中的第几个位置开始写*/
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
	
	/*当用户捕捉的异常写在这个方法中*/
	public static void writeException(String where,String content){
		String detail = "*******************************EXCEPTION******************************\r\n时间：";
		detail += new Date() +" \r\n POSITION:";
		detail += where+" \r\n EXCEPTION:";
		detail += content +"\r\n";
		File file = new File(path+"/"+name);
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(file,true);
			byte[] buffer = detail.getBytes();
			/*是重这个数组中的第几个位置开始写*/
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
