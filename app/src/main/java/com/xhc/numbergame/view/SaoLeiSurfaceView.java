package com.xhc.numbergame.view;

import java.util.Calendar;

import com.xhc.numbergame.entity.ArrayMap;
import com.xhc.numbergame.entity.NumSaoLei;
import com.xhc.numbergame.entity.SaoLeiArray;
import com.xhc.numbergame.tools.ClickFlag;
import com.xhc.numbergame.tools.GameState;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class SaoLeiSurfaceView extends SurfaceView implements
		SurfaceHolder.Callback {

	private SurfaceHolder holder;
	private NumSaoLei numSaoLei;
	private ArrayMap map;
	private SaoLeiArray saoLeiArray;
	private Paint paint;
	private ChangeFace changeFace;
	private float downX = 0, downY = 0, moveX = 0, moveY = 0, upX = 0, upY = 0;
	/* 扫雷的数组是x行y列 多少个炸弹 */
	// private int x, y, bomb;
	/* 如果用户在1s内点击一次是单击，两次是双击 在1s内一直按着是长按 */
	private long downTime, upTime;
	private Handler handler;
	/* 记录在250ms中点击的次数如果大于2 为双击事件 1为单击事件 */
	private int clickFlag = ClickFlag.click;
	/* 判断游戏是否成功 判断是否失败 */
	private int isVictory = GameState.Nothing;
	private boolean isLose = false;

	public SaoLeiSurfaceView(Context context, SaoLeiArray saoLeiArray) {
		super(context);
		this.setFocusableInTouchMode(true);
		holder = this.getHolder();
		holder.addCallback(this);
		this.saoLeiArray = saoLeiArray;

		// MyFile.writeLog("SaoleiSurfaceView constrctory",
		// "i'm in SaoLeisurfaceview constrctory");
		// Log.e("SaoleiSurfaceView constrctory",
		// "i'm in SaoLeisurfaceview constrctory");

	}

	@Override
	public void onDraw(Canvas canvas) {
		canvas.drawARGB(255, 187, 173, 160);
		if (!isLose) {
			numSaoLei.drawArray(canvas, paint);
			map.drawMap(isVictory, canvas, paint);
		} else {
			numSaoLei.drawArray(canvas, paint);
			map.drawInfo("游戏失败", canvas, paint);
			map.drawReset(canvas, paint);
		}

	}

	/* 触屏事件 模拟单击双击长按事件 每次的触摸屏幕之后将会延迟250ms的时间判断是什么事件 如果是双击则只执行一次触摸事件 */
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
			long tempUpTime;
			/* 双击 */
			if (((tempUpTime = Calendar.getInstance().getTimeInMillis()) - upTime) <= 220) {
				upTime = tempUpTime;

				clickFlag = ClickFlag.doubleClick;
			}
			/* 长按 */
			else if (tempUpTime - downTime >= 250) {
				upTime = tempUpTime;
				clickFlag = ClickFlag.longClick;

			} else {
				clickFlag = ClickFlag.click;
				upTime = tempUpTime;
			}
			/* 为了避免响应两次双击事件 */
			if (clickFlag != ClickFlag.doubleClick) {
				handler.postDelayed(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (map.isClickRest(upX, upY)) {
							/* 重新开始 */
							saoLeiArray.resteArray();
							isVictory = GameState.Nothing;
							isLose = false;
						} else {
							/* 屏蔽键盘 */
							if (GameState.Success == isVictory
									|| GameState.DbSuccess == isVictory
									|| isLose)
								return;
							numSaoLei.click(upX, upY, clickFlag);
							if (saoLeiArray.isVictory()) {
								isVictory = GameState.Success;
							}
							isLose = saoLeiArray.isLose();
						}
					}

				}, 220);
			}
			break;

		}
		return true;
	}

	/* 刷帧的线程 */
	class ChangeFace extends Thread {
		boolean flag = true;
		private Canvas lockCanvas;

		@SuppressLint("WrongCall")
		@Override
		public void run() {
			while (flag) {
				synchronized (this) {
					try {

						Thread.sleep(5);

						try {
							lockCanvas = holder.lockCanvas();
						} catch (Exception e) {
							lockCanvas = null;
						}finally{
							if (lockCanvas != null) {
								onDraw(lockCanvas);
								holder.unlockCanvasAndPost(lockCanvas);
							}
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			if (lockCanvas != null) {
				lockCanvas = null;
			}
		}

		public void stop_() {
			flag = false;
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {

		handler = new Handler();

		// MyFile.writeLog("SaoleiSurfaceView create",
		// "i'm in SaoLeisurfaceview create");
		// Log.e("SaoleiSurfaceView create", "i'm in SaoLeisurfaceview create");
		/* 异步加载 */
		handler.post(new Runnable() {
			@Override
			public void run() {
				paint = new Paint();
				numSaoLei = new NumSaoLei(saoLeiArray);
				paint.setAntiAlias(true);
				map = new ArrayMap(saoLeiArray.getX(), saoLeiArray.getY());
				changeFace = new ChangeFace();
				changeFace.start();
				if (saoLeiArray.isVictory()) {
					isVictory = GameState.DbSuccess;
				}
				isLose = saoLeiArray.isLose();
			}
		});

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		// MyFile.writeLog("SaoleiSurfaceView destroy",
		// "i'm in SaoLeisurfaceview destroy");
		// Log.e("SaoleiSurfaceView destroy",
		// "i'm in SaoLeisurfaceview destroy");
		changeFace.flag = false;
		// changeFace.stop_();
		// String temp = "";
		// for(int i = 0 ; i < saoLeiArray.getSaoLeiNodeArray().length ; i++){
		// temp += "\r\n";
		// for(int j = 0 ; j < saoLeiArray.getSaoLeiNodeArray()[i].length ;
		// j++){
		// temp += saoLeiArray.getSaoLeiNodeArray()[i][j].num+" ";
		// }
		//
		// }
		//
		// MyFile.writeLog("saolei destroy ", temp);
	}

}
