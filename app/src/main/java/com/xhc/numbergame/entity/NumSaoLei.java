package com.xhc.numbergame.entity;

import com.xhc.numbergame.tools.Constant;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.Paint.FontMetrics;
import android.util.Log;

/**
 * 绘制扫雷的界面 当全是背面的时候颜色一样 根据
 * 
 * @author Administrator
 *
 */
public class NumSaoLei {
	/**
	 * 为了避免浪费堆空间全局用一个矩形
	 */
	RectF rectf;
	/**
	 * mapLeft mapTop mapRight mapBottom 是整个棋盘的布局
	 */
	private float mapLeft;
	private float mapTop;
	private float mapRight;
	private float mapBottom;

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
	/**
	 * 每个数字的大小 1 2 3 4 and so on
	 */
	private int numSize;

	private SaoLeiArray saoLeiArray;

	/**
	 * 数据初始化
	 * 
	 * @param saoLeiArray
	 *            saoLeiArray.getSaoLeiNodeArray()[0].length 数组中第一行的长度
	 *            后续的其他数据都是根据这个计算的
	 */
	public NumSaoLei(SaoLeiArray saoLeiArray) {
		this.saoLeiArray = saoLeiArray;
		rectf = new RectF();
		numSpace = (12 / (saoLeiArray.getSaoLeiNodeArray()[0].length))
				* Constant.density;
		roundX = (35 / (saoLeiArray.getSaoLeiNodeArray()[0].length))
				* Constant.density;
		if (roundX > 25)
			roundX = 25;
		roundY = roundX;
		mapLeft = numSpace;
		mapRight = Constant.width - numSpace;
		rectWidth = (mapRight - mapLeft)
				/ (saoLeiArray.getSaoLeiNodeArray()[0].length);
		mapTop = (Constant.height
				- (saoLeiArray.getSaoLeiNodeArray().length * rectWidth) - 2 * numSpace);
		mapBottom = Constant.height;
		numSize = (int) (rectWidth / 3);
	}

	/* 通过像素上的坐标x y 可以算出数组上的坐标x y 注意坐标上x y 和数组上x y的区别 */
	public void click(float x, float y, int clickFlag) {
		int arrayX, arrayY;
		arrayX = (int) Math.floor((y - mapTop - numSpace) / rectWidth);
		arrayY = (int) Math.floor((x - numSpace) / rectWidth);
		if (arrayX < 0 || arrayX >= saoLeiArray.getSaoLeiNodeArray().length
				|| arrayY < 0
				|| arrayY > saoLeiArray.getSaoLeiNodeArray()[arrayX].length) {
             return ;
		}
		else
		saoLeiArray.clickArray(arrayX, arrayY, clickFlag);
	}

	public void drawArray(Canvas canvas, Paint paint) {

		for (int i = 0; i < saoLeiArray.getSaoLeiNodeArray().length; i++) {
			for (int j = 0; j < saoLeiArray.getSaoLeiNodeArray()[i].length; j++) {
				drawNumber(canvas, paint,
						saoLeiArray.getSaoLeiNodeArray()[i][j].num,
						saoLeiArray.getSaoLeiNodeArray()[i][j].clickFlag, j
								* rectWidth + numSpace, i * rectWidth
								+ numSpace + mapTop);
			}
		}
	}

	public void drawNumber(Canvas canvas, Paint paint, int num, int flag,
			float positionX, float positionY) {
		paint.setColor(getColor(flag));
		/* 画小方块 */
		rectf.set(positionX, positionY, positionX + rectWidth - numSpace,
				positionY + rectWidth - numSpace);
		canvas.drawRoundRect(rectf, roundX, roundY, paint);
		/* 绘制数字或者问号 或者 旗子 或者炸弹 */
		paint.setColor(Color.argb(255, 0, 0, 0));
		paint.setTextSize(numSize);
		paint.setTypeface(Typeface.DEFAULT_BOLD);
		/* 计算图形高度 */
		FontMetrics fm = paint.getFontMetrics();
		float textHeight = (float) (Math.ceil(fm.descent - fm.ascent) - Constant.density * 8);
		if (textHeight <= 0)
			textHeight = (float) (Math.ceil(fm.descent - fm.ascent));
		switch (flag) {
		case 0:

			break;
		case 1:

			String temp = null;

			if (num == -1)
				temp = "*";
			else if(num == 0)temp="";
			else
				temp = num + "";

			float textWidth = paint.measureText(num + "");

			canvas.drawText(temp + "", positionX + (rectWidth - numSpace) / 2
					- textWidth / 2, positionY + (rectWidth - numSpace) / 2
					+ textHeight / 2, paint);
			break;
		case 2:
			/* 不确定是什么 */
			float textWidthQuestion = paint.measureText("？");
			canvas.drawText("？", positionX + (rectWidth - numSpace) / 2
					- textWidthQuestion / 2, positionY + (rectWidth - numSpace)
					/ 2 + textHeight / 2, paint);
			break;
		case 3:
			/* 插的旗子 */
			float textWidthBomb = paint.measureText("ㄗ");
			canvas.drawText("ㄗ", positionX + (rectWidth - numSpace) / 2
					- textWidthBomb / 2, positionY + (rectWidth - numSpace) / 2
					+ textHeight / 2, paint);
			break;
		}

	}

	public int getColor(int flag) {
		int color = 0;
		switch (flag) {
		case 0:
			color = Color.argb(255, 63, 62, 59);
			break;
		case 1:
			color = Color.argb(255, 197, 196, 195);
			break;
		case 2:
			color = Color.argb(255, 90, 149, 207);
			break;
		case 3:
			color = Color.argb(255, 200, 66, 66);
			break;

		}
		return color;
	}
}









