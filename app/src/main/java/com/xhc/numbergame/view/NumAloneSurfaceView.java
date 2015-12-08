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
 * 数独界面 点击棋盘中的宫格定位到宫格然后操作 1.长按普通的按键表示用户已经确认当前的值 2.单击普通按键将会把数字放入候选区中
 * 3.单击候选区中的按键表示用户已经确认了当前的值 4.长按候选区的按键表示删除候选区中的那个数
 * 5.长按棋盘中的宫格表示将之前确认的数字删去显示候选区的数字
 * */
public class NumAloneSurfaceView extends SurfaceView implements
		SurfaceHolder.Callback {

	private SurfaceHolder holder;
	/* 加载数据结束后通知界面的handler */
	private Handler handler;
	private Paint paint;
	private ArrayMap map;
	private ChangeFace changeFace;
	/* 后台数据 */
	private DrawNumAloneArray drawNumAloneArray;
	/* 为后台数据绘制界面的类 */
	private NumAloneArray numAloneArray;
	/* 触摸屏后保存的坐标 */
	private float downX = 0, downY = 0, moveX = 0, moveY = 0, upX = 0, upY = 0;
	/* 为响应各种事件 */
	private long downTime, upTime;
	/* 点击标志 */
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
	 * 只用响应点击和长按
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
				/* 点击了重来按钮 */
				Message msg = new Message();
				msg.arg1 = 2;
				handler.sendMessage(msg);
			} else {
				if (isVictory == GameState.Nothing) {

					/* 长按 */
					if (upTime - downTime >= 200)
						clickFlag = ClickFlag.longClick;
					/* 点击 */
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

	/* 刷帧线程 */
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
