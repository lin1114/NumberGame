package com.xhc.numbergame.view;

import com.xhc.numbergame.entity.ArrayMap;
import com.xhc.numbergame.entity.Num;
import com.xhc.numbergame.entity.SortArrayGame;
import com.xhc.numbergame.tools.Constant;
import com.xhc.numbergame.tools.GameState;
import com.xhc.numbergame.tools.NumDirection;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * numCount 传入的是几阶的数组
 * 
 * @author Administrator
 */
public class SortSurfaceView extends SurfaceView implements
		SurfaceHolder.Callback {
	private SurfaceHolder holder;
	/* 几阶的数组 */
	private int numCount;
	private ArrayMap map;
	private SortArrayGame sortArray;
	private Paint paint = new Paint();
	private Num number;
	private ChangeFace changeFace;
	/* 触屏的坐标 */
	private float downX = 0, downY = 0, moveX = 0, moveY = 0, upX = 0, upY = 0;
	/* 判断方块的移动方向 */
	private int direction = NumDirection.dontMove;
	/* 判断游戏是否成功 */
	private int success = GameState.Nothing;
	/* 绘制原始数组让用户知道需要排序成什么样 */
	private boolean drawOrgin = true;

	public SortSurfaceView(Context context, int numCount, Num number,
			SortArrayGame sortArray) {
		super(context);
		this.setFocusableInTouchMode(true);
		holder = this.getHolder();
		holder.addCallback(this);
		/* 数据初始化 实例化对象 */
		this.numCount = numCount;
		this.number = number;
		this.sortArray = sortArray;
		paint.setAntiAlias(true);

	}

	/* 刷帧的线程 */
	class ChangeFace extends Thread {
		private Canvas lockCanvas;
		private boolean flag = true;

		@SuppressLint("WrongCall")
		@Override
		public void run() {
			while (flag) {
				synchronized (this) {

					try {
						Thread.sleep(10);
						try {
							lockCanvas = holder.lockCanvas();

						} catch (Exception e) {
							lockCanvas = null;
						} finally {
							if (lockCanvas != null) {
								onDraw(lockCanvas);
								holder.unlockCanvasAndPost(lockCanvas);
							}
						}

					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			if (lockCanvas != null) {
				lockCanvas = null;
			}
		}

		/* 停止线程 */
		public void stop_() {
			this.flag = false;
		}
	}

	/* 触屏事件 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downX = event.getX();
			downY = event.getY();

			break;
		case MotionEvent.ACTION_MOVE:
			moveX = event.getX();
			moveY = event.getY();
			break;

		case MotionEvent.ACTION_UP:
			drawOrgin = false;
			upX = event.getX();
			upY = event.getY();
			/* 横向 */
			if (Math.abs(upX - downX) > Math.abs(upY - downY)) {
				if ((upX - downX) > Constant.density * 5)
					direction = NumDirection.right;// 右向移动
				else if ((upX - downX) < -Constant.density * 5)
					direction = NumDirection.left;// 不然就是左向
			}
			/* 纵向 */
			else if (Math.abs(upX - downX) < Math.abs(upY - downY)) {
				if ((upY - downY) > Constant.density * 5)
					direction = NumDirection.downward;
				else if ((upY - downY) < -Constant.density * 5)
					direction = NumDirection.upward;
			}
			/* 点击 */
			else {
				direction = NumDirection.dontMove;
				if (map.isClickRest(upX, upY)) {
					success = GameState.Nothing;
					sortArray.reste();
				}
				if (map.isClickOrginButton(upX, upY)) {
					drawOrgin = true;
				}
			}
			/* 先将后台的数组更改 如果已经成功了。则不会响应事件 */
			if (success == GameState.Nothing)
				sortArray.moveArray(direction);
			if (sortArray.isSameArray() && success != GameState.DbSuccess) {

				success = GameState.Success;
			}
			direction = NumDirection.dontMove;// 恢复
			break;
		}
		return true;
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawARGB(255, 187, 173, 160);
		paint.setARGB(255, 255, 255, 255);

		if (!drawOrgin) {
			map.drawSortMap(canvas, paint, success);
			number.drawSortArray(direction, canvas, paint);
		} else {
			map.drawInfo("记住图形，点击继续", canvas, paint);
			number.drawOriginalArray(canvas, paint);
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		map = new ArrayMap(numCount);
		if (sortArray.isSameArray()) {
			success = GameState.DbSuccess;
		}
		changeFace = new ChangeFace();
		changeFace.start();

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		/* 当这个view销毁的时候停止线程 */

		// String temp = "";
		// for (int i = 0; i < sortArray.getArray().length; ++i) {
		// temp += "\r\n";
		// for (int j = 0; j < sortArray.getArray()[i].length; ++j) {
		// temp += sortArray.getArray()[i][j];
		// }
		//
		// }
		changeFace.stop_();
	}

}
