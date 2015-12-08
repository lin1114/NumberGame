package com.xhc.numbergame.entity;

import java.util.Calendar;
import java.util.Date;

import com.xhc.numbergame.tools.Constant;
import com.xhc.numbergame.tools.DbHelper;
import com.xhc.numbergame.tools.GameState;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

/**
 * 此类用来绘画各种棋盘
 * 
 * @author Administrator
 * 
 */
public class ArrayMap {
	RectF rectf;
	/**
	 * mapLeft mapTop mapRight mapBottom 是整个棋盘的布局
	 * 
	 * 
	 */
	private float mapLeft;
	private float mapTop;
	private float mapRight;
	private float mapBottom;
	/**
	 * x 是多少行。y是多少列
	 */
	private int x, y;
	/**
	 * 其中每个数字模块的宽度(正方形)
	 */
	private float rectWidth;
	/**
	 * 模块与模块之间的间隙
	 */
	private float numSpace;
	/**
	 * 矩形边角的圆弧度
	 */
	private float roundX;
	private float roundY;
	/* 游戏的开始时间 */
	private long startTime;
	/* 游戏的间隔事件 end-start */
	public static int time;
	/* 从数据库中获取的时间 */
	public static int dataBaseTime;
	/**
	 * 重来按钮的宽度和左 上 右 下的坐标
	 */
	float restRectWidth, left, right, top, bottom;

	public ArrayMap(int x) {
		rectf = new RectF();
		startTime = Calendar.getInstance().getTimeInMillis();
		this.x = x;
		numSpace = (12 / x) * Constant.density;
		roundX = (35 / x) * Constant.density;
		if (roundX > 25)
			roundX = 25;
		roundY = roundX;
		mapLeft = numSpace;
		mapRight = Constant.width - numSpace;
		rectWidth = (mapRight - mapLeft) / x;
		mapTop = ((Constant.height - Constant.width) - rectWidth - 2 * numSpace);
		mapBottom = Constant.height;

		/* 重来按钮的位置初始化 */
		restRectWidth = (40 * Constant.density + 20);
		left = Constant.width - restRectWidth - 3 * numSpace;
		top = numSpace * 3;
		right = Constant.width - 2 * numSpace;
		bottom = (float) (restRectWidth * 2 / 3.0) + numSpace;
	}

	/* 初始化地图属性 和重来按钮的属性 */
	public ArrayMap(int x, int y) {
		this.x = x;
		this.y = y;

		rectf = new RectF();
		startTime = Calendar.getInstance().getTimeInMillis();
		numSpace = (12 / y) * Constant.density;
		roundX = (35 / y) * Constant.density;
		if (roundX > 25)
			roundX = 25;
		roundY = roundX;
		mapLeft = numSpace;
		mapRight = Constant.width - numSpace;
		rectWidth = (mapRight - mapLeft) / y;
		mapTop = (Constant.height - (x * rectWidth) - 2 * numSpace);
		mapBottom = Constant.height;

		/* 重来按钮的位置初始化 */
		restRectWidth = (40 * Constant.density + 20);
		left = Constant.width - restRectWidth - Constant.density * 2;
		top = Constant.density * 2;
		right = Constant.width - Constant.density * 2;
		bottom = (float) (restRectWidth * 2 / 3.0);

	}

	/* 排序矩阵 并且绘制两个按钮 重来 原始图形 */
	public void drawSortMap(Canvas canvas, Paint paint, int success) {

		rectf.set(mapLeft, mapTop, mapRight, mapBottom);

		paint.setColor(Color.argb(255, 176, 160, 148));
		for (int i = 0; i < x; i++) {
			for (int j = 0; j < x; j++) {
				rectf.set((j * rectWidth + numSpace),
						(i * rectWidth + numSpace) + mapTop, (j + 1)
								* rectWidth, (i + 1) * rectWidth + mapTop);
				canvas.drawRoundRect(rectf, roundX, roundY, paint);
			}
		}

		canvas.drawRoundRect(new RectF(numSpace, (x * rectWidth + numSpace)
				+ mapTop, rectWidth, (x + 1) * rectWidth + mapTop), 20, 20,
				paint);
		drawSortMapForButton(success, canvas, paint);
	}

	/* 原始图形按钮的左上角的横坐标 */
	private float getOrginButtonLeft() {
		return left - 2 * restRectWidth - numSpace;
	}

	/* 原始图形按钮右上角的横坐标 */
	private float getOrginButtonRight() {
		return left - 2 * numSpace;
	}

	/* 判断是不是点击原始按钮作用用来查看原始按钮 */
	public boolean isClickOrginButton(float x, float y) {
		if (x >= getOrginButtonLeft() && x <= getOrginButtonRight() && y >= top
				&& y <= bottom) {
			return true;
		}
		return false;
	}

	/* 为排序数组界面绘制map 查看原始图形按钮 */
	public void drawSortMapForButton(int success, Canvas canvas, Paint paint) {
		drawMap(success, canvas, paint);
		drawLineRect(canvas, paint, getOrginButtonLeft(), top,
				getOrginButtonRight(), bottom);
		paint.setTextSize(Constant.density * 18);
		float textWidth = paint.measureText("原始图形");
		FontMetrics fm = paint.getFontMetrics();
		float textHeight = (float) (Math.ceil(fm.descent - fm.ascent) - Constant.density * 8);
		canvas.drawText("原始图形", getOrginButtonLeft()
				+ (getOrginButtonRight() - getOrginButtonLeft()) / 2
				- textWidth / 2, top + (bottom - top) / 2 + textHeight / 2,
				paint);
	}

	/* 绘制通知界面 */
	public void drawInfo(String info, Canvas canvas, Paint paint) {
		paint.setColor(Color.argb(255, 204, 72, 64));
		paint.setTextSize(Constant.density * 8 + 30);

		FontMetrics fm = paint.getFontMetrics();
		float textHeight = (float) (Math.ceil(fm.descent - fm.ascent) - Constant.density * 8);
		canvas.drawText(info, numSpace * 5, mapTop - textHeight - textHeight
				/ 2, paint);
	}

	/*
	 * 若游戏成功便将时间停止 因为有三种情况在这里面
	 */
	private int getTime(int success) {
		long end = Calendar.getInstance().getTimeInMillis();
		if (success == GameState.Nothing) {
			time = ((int) ((end - startTime) / 1000) + dataBaseTime);
			
		} else if (success == GameState.Success) {
			
		} else if (success == GameState.DbSuccess) {
			time = dataBaseTime;
		}
		return time;
	}

	/* 绘制重来按钮 和时间 */
	public void drawMap(int success, Canvas canvas, Paint paint) {
		paint.setColor(Color.argb(255, 204, 72, 64));
		paint.setTextSize(Constant.density * 8 + 30);
		FontMetrics fm = paint.getFontMetrics();
		float textHeight = (float) (Math.ceil(fm.descent - fm.ascent) - Constant.density * 8);
		if (success == GameState.Success) {
			canvas.drawText("恭喜！用时：" + getTime(GameState.Success) + "秒",
					numSpace * 5, mapTop - textHeight - textHeight / 2, paint);
		} else if(success == GameState.Nothing){
			canvas.drawText("时间：" + getTime(GameState.Nothing) + "秒",
					numSpace * 5, mapTop - textHeight, paint);
		}
		else{
			canvas.drawText("上次时间：" + getTime(GameState.DbSuccess) + "秒",
					numSpace * 5, mapTop - textHeight, paint);
		}
		drawReset(canvas, paint);
	}

	/* 为数独绘制重来按钮 和时间 */
	public void drawNumAloneMap(int success, Canvas canvas, Paint paint) {
		paint.setColor(Color.argb(255, 204, 72, 64));
		paint.setTextSize(Constant.density * 8 + 30);
		FontMetrics fm = paint.getFontMetrics();
		float textHeight = (float) (Math.ceil(fm.descent - fm.ascent) - Constant.density * 8);
		if (success == GameState.Success) {
			canvas.drawText("恭喜！用时：" + getTime(GameState.Success) + "秒",
					numSpace * 3, mapTop / 2, paint);
		} 
		else if(success == GameState.DbSuccess){
			canvas.drawText("上次时间：" + getTime(GameState.DbSuccess) + "秒", numSpace,
					textHeight, paint);
		}
		else {
			canvas.drawText("时间：" + getTime(GameState.Nothing) + "秒", numSpace,
					textHeight, paint);
		}
		drawReset(canvas, paint);
	}

	/* 绘画重来的按钮 */
	public void drawReset(Canvas canvas, Paint paint) {

		paint.setColor(Color.argb(255, 113, 150, 217));
		drawLineRect(canvas, paint, left, top, right, bottom);
		paint.setTextSize(Constant.density * 18);
		float textWidth = paint.measureText("重来");
		FontMetrics fm = paint.getFontMetrics();
		float textHeight = (float) (Math.ceil(fm.descent - fm.ascent) - Constant.density * 8);
		canvas.drawText("重来", left + (right - left) / 2 - textWidth / 2, top
				+ (bottom - top) / 2 + textHeight / 2, paint);
	}

	/* 模拟空心矩形 */
	private void drawLineRect(Canvas canvas, Paint paint, float left,
			float top, float right, float bottom) {
		paint.setStrokeWidth(Constant.density + 5);
		canvas.drawLine(left, top, right, top, paint);
		canvas.drawLine(right, top, right, bottom, paint);
		canvas.drawLine(right, bottom, left, bottom, paint);
		canvas.drawLine(left, bottom, left, top, paint);
	}

	/* 数独中的几根线 */
	public void drawLineMap(Canvas canvas, Paint paint) {

		/* 设置线宽 */
		paint.setStrokeWidth(numSpace);
		/* 竖细线 */
		paint.setColor(Color.argb(255, 138, 138, 138));
		canvas.drawLine(mapLeft + rectWidth, mapTop - rectWidth, mapLeft
				+ rectWidth, mapBottom - 2 * rectWidth - 2 * numSpace, paint);
		canvas.drawLine(mapLeft + 2 * rectWidth, mapTop - rectWidth, mapLeft
				+ 2 * rectWidth, mapBottom - 2 * rectWidth - 2 * numSpace,
				paint);
		canvas.drawLine(mapLeft + 4 * rectWidth, mapTop - rectWidth, mapLeft
				+ 4 * rectWidth, mapBottom - 2 * rectWidth - 2 * numSpace,
				paint);
		canvas.drawLine(mapLeft + 5 * rectWidth, mapTop - rectWidth, mapLeft
				+ 5 * rectWidth, mapBottom - 2 * rectWidth - 2 * numSpace,
				paint);
		canvas.drawLine(mapLeft + 7 * rectWidth, mapTop - rectWidth, mapLeft
				+ 7 * rectWidth, mapBottom - 2 * rectWidth - 2 * numSpace,
				paint);
		canvas.drawLine(mapLeft + 8 * rectWidth, mapTop - rectWidth, mapLeft
				+ 8 * rectWidth, mapBottom - 2 * rectWidth - 2 * numSpace,
				paint);
		/* 横细线 */
		canvas.drawLine(mapLeft, mapTop, mapRight, mapTop, paint);
		canvas.drawLine(mapLeft, mapTop + rectWidth, mapRight, mapTop
				+ rectWidth, paint);
		canvas.drawLine(mapLeft, mapTop + 3 * rectWidth, mapRight, mapTop + 3
				* rectWidth, paint);
		canvas.drawLine(mapLeft, mapTop + 4 * rectWidth, mapRight, mapTop + 4
				* rectWidth, paint);
		canvas.drawLine(mapLeft, mapTop + 6 * rectWidth, mapRight, mapTop + 6
				* rectWidth, paint);
		canvas.drawLine(mapLeft, mapTop + 7 * rectWidth, mapRight, mapTop + 7
				* rectWidth, paint);

		/* 竖粗线 */
		paint.setColor(Color.argb(255, 36, 72, 214));
		canvas.drawLine(mapLeft, mapTop - rectWidth, mapLeft, mapBottom - 2
				* rectWidth - 2 * numSpace, paint);

		canvas.drawLine(mapLeft + rectWidth * 3, mapTop - rectWidth, mapLeft
				+ rectWidth * 3, mapBottom - 2 * rectWidth - 2 * numSpace,
				paint);

		canvas.drawLine(mapLeft + rectWidth * 6, mapTop - rectWidth, mapLeft
				+ rectWidth * 6, mapBottom - 2 * rectWidth - 2 * numSpace,
				paint);

		canvas.drawLine(mapLeft + rectWidth * 9, mapTop - rectWidth, mapLeft
				+ rectWidth * 9, mapBottom - 2 * rectWidth - 2 * numSpace,
				paint);
		/* 横粗线 */
		canvas.drawLine(mapLeft, mapTop - rectWidth, mapRight, mapTop
				- rectWidth, paint);
		canvas.drawLine(mapLeft, mapTop + rectWidth * 2, mapRight, mapTop
				+ rectWidth * 2, paint);
		canvas.drawLine(mapLeft, mapTop + rectWidth * 5, mapRight, mapTop
				+ rectWidth * 5, paint);
		canvas.drawLine(mapLeft, mapTop + rectWidth * 8, mapRight, mapTop
				+ rectWidth * 8, paint);

	}

	/**
	 * 判断是否点击了重来按钮
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isClickRest(float x, float y) {
		if (x >= left && x <= right && y >= top && y <= bottom) {
			startTime = Calendar.getInstance().getTimeInMillis();
			dataBaseTime = 0;
			return true;
		}
		return false;

	}
}
