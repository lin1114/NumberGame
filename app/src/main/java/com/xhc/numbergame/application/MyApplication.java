package com.xhc.numbergame.application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.lang.Thread.UncaughtExceptionHandler;

import com.xhc.numbergame.tools.Utils;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

public class MyApplication extends Application {

	static String error = "";
	/**
	 * ������־
	 */
	private static String ERROR_LOG_PATH = "/com/NumberGame/classroom/error/";
	private static String error_log_name = "numbergame-error.log";
	private static String error_log_path = android.os.Environment.getExternalStorageDirectory() + "" + ERROR_LOG_PATH;

	@Override
	public void onCreate() {
		super.onCreate();

		/*
		 * ���ʹ�����־
		 */
		CrashHandler ch = CrashHandler.getInstance();
		ch.init(getApplicationContext());
		try {
			Utils.MakeDir(error_log_path);
			File file = new File(error_log_path + error_log_name);
			if (!file.exists()) {
				file.createNewFile();
			}
			ch.postReport(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static class CrashHandler implements UncaughtExceptionHandler {

		/**
		 * �Ƿ�����־���,��Debug״̬�¿���, ��Release״̬�¹ر�����ʾ��������
		 */
		public static final boolean DEBUG = true;

		/** ϵͳĬ�ϵ�UncaughtException������ */
		private UncaughtExceptionHandler mDefaultHandler;

		/** CrashHandlerʵ�� */
		public static CrashHandler INSTANCE;
		/** �����Context���� */
		private Context mContext;

		/** ��ֻ֤��һ��CrashHandlerʵ�� */

		private CrashHandler() {
		}

		private static Handler handler;

		/** ��ȡCrashHandlerʵ�� ,����ģʽ */
		public static CrashHandler getInstance() {
			handler = new Handler();
			if (INSTANCE == null) {
				INSTANCE = new CrashHandler();
			}
			return INSTANCE;
		}

		/**
		 * ��ʼ��,ע��Context����, ��ȡϵͳĬ�ϵ�UncaughtException������,
		 * ���ø�CrashHandlerΪ�����Ĭ�ϴ�����
		 * 
		 * @param ctx
		 */

		public void init(Context ctx) {
			mContext = ctx;
			mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
			Thread.setDefaultUncaughtExceptionHandler(this);
		}

		/**
		 * ��UncaughtException����ʱ��ת��ú���������
		 */
		@Override
		public void uncaughtException(Thread thread, Throwable ex) {
			if (!handleException(ex) && mDefaultHandler != null) {
				// ����û�û�д�������ϵͳĬ�ϵ��쳣������������
				mDefaultHandler.uncaughtException(thread, ex);
			} else {
				// ����Լ��������쳣���򲻻ᵯ������Ի�������Ҫ�ֶ��˳�app
			}
		}

		// TODO ʹ��HTTP Post ���ʹ��󱨸浽������ ���ﲻ������ϸ����
		public void postReport(final File file) throws Exception {

			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = "";
			while ((line = br.readLine()) != null) {
				error += line;
			}
			br.close();
			// new Thread() {
			// public void run() {
			// try {
			// String mtype = android.os.Build.MODEL;
			// String mbrand = android.os.Build.BRAND;
			//// MailTool.sendMail(mail_error_log_name + mbrand + "-"
			//// + mtype + "-" + System.currentTimeMillis(),
			//// error);
			// file.delete();
			// } catch (MessagingException e) {
			// e.printStackTrace();
			// }
			// };
			// }.start();
		}

		/**
		 * �Զ��������,�ռ�������Ϣ ���ʹ��󱨸�Ȳ������ڴ����. �����߿��Ը����Լ���������Զ����쳣�����߼�
		 * 
		 * @return true��������쳣�������������쳣��
		 *         false����������쳣(���Խ���log��Ϣ�洢����)Ȼ�󽻸��ϲ�(����͵���ϵͳ���쳣����)ȥ����
		 *         ����˵����true���ᵯ���Ǹ�������ʾ��false�ͻᵯ��
		 */
		private static boolean handleException(final Throwable ex) {
			if (ex == null) {
				return false;
			}

			final String msg = ex.getLocalizedMessage();
			final StackTraceElement[] stack = ex.getStackTrace();
			final String message = ex.getMessage();

			new Thread() {
				public void run() {
					Looper.prepare();
					File file = new File(error_log_path + error_log_name);
					try {
						if (!file.exists()) {
							file.createNewFile();
						}
						FileOutputStream fos = new FileOutputStream(file, true);
						fos.write(new String("=================Start:Error Info=================\n").getBytes());
						fos.write(new String("Time:" + Utils.getCurTime() + "\n").getBytes());

						String mtype = android.os.Build.MODEL;
						String mbrand = android.os.Build.BRAND;
						String versionSdk = android.os.Build.VERSION.SDK;

						fos.write(new String("Mobile Brand:" + mbrand + "\n").getBytes());
						fos.write(new String("Mobile Mode:" + mtype + "\n").getBytes());
						fos.write(new String("SDK:" + versionSdk + "\n\n").getBytes());

						fos.write(message.getBytes());
						for (int i = 0; i < stack.length; i++) {
							fos.write(stack[i].toString().getBytes());
						}

						fos.write(new String("\n=================End:Error Info=================\n").getBytes());

						fos.write(new String("\n\n\n\n").getBytes());

						fos.flush();
						fos.close();
					} catch (Exception e) {
					}
					Looper.loop();
				};
			}.start();
			return false;
		}
	}
}
