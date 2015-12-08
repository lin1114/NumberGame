package com.xhc.numbergame.view;

import java.util.Calendar;

import com.xhc.numbergame.entity.ArrayMap;
import com.xhc.numbergame.entity.DrawNumAloneArray;
import com.xhc.numbergame.entity.NumAloneArray;
import com.xhc.numbergame.tools.ClickFlag;
import com.xhc.numbergame.tools.GameState;
import com.xhc.numbergame.tools.MyFile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * �������� ��������еĹ���λ������Ȼ����� 1.������ͨ�İ�����ʾ�û��Ѿ�ȷ�ϵ�ǰ��ֵ 2.������ͨ������������ַ����ѡ����
 * 3.������ѡ���еİ�����ʾ�û��Ѿ�ȷ���˵�ǰ��ֵ 4.������ѡ���İ�����ʾɾ����ѡ���е��Ǹ���
 * 5.���������еĹ����ʾ��֮ǰȷ�ϵ�����ɾȥ��ʾ��ѡ��������
 * */
public class NumAloneSurfaceView extends SurfaceView implements
		SurfaceHolder.Callback {

	private SurfaceHolder holder;
	/* �������ݽ�����֪ͨ�����handler */
	private Handler handler;
	private Paint paint;
	private ArrayMap map;
	private ChangeFace changeFace;
	/* ��̨���� */
	private DrawNumAloneArray drawNumAloneArray;
	/* Ϊ��̨���ݻ��ƽ������ */
	private NumAloneArray numAloneArray;
	/* �������󱣴������ */
	private float downX = 0, downY = 0, moveX = 0, moveY = 0, upX = 0, upY = 0;
	/* Ϊ��Ӧ�����¼� */
	private long downTime, upTime;
	/* �����־ */
	private int clickFlag = ClickFlag.click;
	private int isVictory = GameState.Nothing;

	public NumAloneSurfaceView(Context context, Handler handler,
			NumAloneArray numAloneArray) {
		super(context);
		this.setFocusableInTouchMode(true);
		holder = this.getHolder();
		holder.addCallback(this);

		this.handler = handler;
		map = new ArrayMap(9);
		paint = new Paint();
		paint.setAntiAlias(true);
		this.numAloneArray = numAloneArray;
		drawNumAloneArray = new DrawNumAloneArray(numAloneArray);

	}

	@Override
	public void onDraw(Canvas canvas) {
		canvas.drawARGB(255, 187, 173, 160);
		map.drawNumAloneMap(isVictory, canvas, paint);
		map.drawLineMap(canvas, paint);
		drawNumAloneArray.onDrawMap(canvas, paint);
	}

	/**
	 * ֻ����Ӧ����ͳ���
	 * */
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downX = event.getX();
			downY = event.getY();
			downTime = Calendar.getInstance().getTimeInMillis();
			break;
		case MotionEvent.ACTION_UP:
			upX = event.getX();
			upY = event.getY();
			upTime = Calendar.getInstance().getTimeInMillis();
			if (map.isClickRest(upX, upY)) {
				/* �����������ť */
				Message msg = new Message();
				msg.arg1 = 2;
				handler.sendMessage(msg);
			} else {
				if (isVictory == GameState.Nothing) {

					/* ���� */
					if (upTime - downTime >= 200)
						clickFlag = ClickFlag.longClick;
					/* ��� */
					else
						clickFlag = ClickFlag.click;
					drawNumAloneArray.clickEvent(upX, upY, clickFlag);
					isVictory = numAloneArray.isVictory();
				}
			}
			break;
		}
		return true;
	}

	/* ˢ֡�߳� */
	class ChangeFace extends Thread {
		boolean flag = true;
		Canvas canvas;

		@SuppressLint("WrongCall")
		@Override
		public void run() {
			while (flag) {
				synchronized (this) {

					try {
						canvas = holder.lockCanvas();
						Thread.sleep(5);
					} catch (Exception e) {
						canvas = null;
					} finally {
						if (canvas != null) {
							onDraw(canvas);
							holder.unlockCanvasAndPost(canvas);
						}
					}
				}
			}
			if (canvas != null) {
				canvas = null;
			}
		}

		public void stop_() {
			this.flag = false;
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		isVictory = numAloneArray.isVictory();
		if (isVictory == GameState.Success) {
			isVictory = GameState.DbSuccess;
		}
		changeFace = new ChangeFace();
		changeFace.start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		changeFace.flag = false;
		// changeFace.stop_();
		// String temp = "";
		// for (int i = 0; i < numAloneArray.getArray().length; i++) {
		// temp += "\r\n";
		// for (int j = 0; j < numAloneArray.getArray()[i].length; j++) {
		// temp += numAloneArray.getArray()[i][j].systemNum + " ";
		// }
		//
		// }
		// MyFile.writeLog("sudoku", temp);
	}

}
