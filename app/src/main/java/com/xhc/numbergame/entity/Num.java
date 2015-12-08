package com.xhc.numbergame.entity;

import com.xhc.numbergame.tools.Constant;
import com.xhc.numbergame.tools.NumDirection;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.RectF;
import android.graphics.Typeface;

/**
 * 此类只为排序数组使用
 * 本类的功能是根据数组上的数字绘画数字图形 并且模拟数字滑动的效果
 * @author Administrator
 */
public class Num {
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
	 * 记录排序数组中小数字块走的步数
	 */
	private int stepCount;
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
	/**
	 * 每个数字的大小 1 2 3 4 and so on
	 */
	private int numSize;
	/**
	 * 界面上显示的后台数组
	 */
	int[][] array;
	/**
	 * 排序数组对象
	 * */
	SortArrayGame sortArrayGame;

	/**
	 * 为排序的构造函数
	 * */
	public Num(SortArrayGame sortArrayGame) {
		this.sortArrayGame = sortArrayGame;
		rectf = new RectF();
		this.array = sortArrayGame.getArray();
		this.x = array.length - 1;
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
		numSize = (int) (rectWidth / 3);
	}

	public Num(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void drawOriginalArray(Canvas canvas, Paint paint){
		for (int i = 0; i < sortArrayGame.getOriginalArray().length; i++) {
			for (int j = 0; j < sortArrayGame.getOriginalArray()[i].length; j++) {
				if (sortArrayGame.getOriginalArray()[i][j] == -1) {
					continue;
				}// 空格
				drawNumber(canvas, paint, sortArrayGame.getOriginalArray()[i][j],
						j * rectWidth + numSpace, i * rectWidth + numSpace
								+ mapTop);
			}
		}
	}
	
	/**
	 * 画出打乱了的二位数组
	 */
	public void drawArray(Canvas canvas, Paint paint) {
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[i].length; j++) {
				if (array[i][j] == -1) {
					continue;
				}// 空格

				drawNumber(canvas, paint, array[i][j],
						j * rectWidth + numSpace, i * rectWidth + numSpace
								+ mapTop);
			}
		}
	}

	/**
	 * 绘画数字 num哪个数字 positionx左上角的x坐标 positiony左上角的y坐标 计算出数字的高度和宽度
	 */
	public void drawNumber(Canvas canvas, Paint paint, int num,
			float positionX, float positionY) {
		paint.setColor(getColor(num));
		rectf.set(positionX, positionY, positionX + rectWidth - numSpace,
				positionY + rectWidth - numSpace);
		canvas.drawRoundRect(rectf, roundX, roundY, paint);
		paint.setColor(Color.argb(255, 0, 0, 0));
		paint.setTextSize(numSize);
		paint.setTypeface(Typeface.DEFAULT_BOLD);
		float textWidth = paint.measureText(num + "");
		FontMetrics fm = paint.getFontMetrics();
		float textHeight = (float) (Math.ceil(fm.descent - fm.ascent) - Constant.density * 8);
		if (textHeight <= 0)
			textHeight = (float) (Math.ceil(fm.descent - fm.ascent));
		canvas.drawText(num + "", positionX + (rectWidth - numSpace) / 2
				- textWidth / 2, positionY + (rectWidth - numSpace) / 2
				+ textHeight / 2, paint);
	}

	/**
	 * 获取每个字体的背景色 共5个颜色 2 3 5 7 的倍数分别不同的颜色 1是个单独的颜色
	 */
	private int getColor(int num) {
		if (num == 1) {
			return Color.argb(255, 216, 216, 216);
		} else if (num % 7 == 0) {
			return Color.argb(255, 245, 149, 99);
		} else if (num % 5 == 0) {
			return Color.argb(255, 237, 204, 97);
		} else if (num % 3 == 0) {
			return Color.argb(255, 246, 94, 59);
		} else if (num % 2 == 0) {
			return Color.argb(255, 245, 149, 99);
		} else {
			return Color.argb(255, 237, 204, 97);
		}
	}

	/**
	 * 模拟出数字滑动的效果
	 * 
	 * @param direction
	 *            需要移动的方向
	 * @param blankPositionX
	 *            空白处的数组的横向下标
	 * @param blankPositionY
	 *            空白处的数组的纵向下标
	 *返回false表示移动完毕    
	 */
	public void drawSortArray(int direction, Canvas canvas, Paint paint) {

			drawArray(canvas,paint); 
	}

	/* 画出小方块的移动效果 返回false表示移动完毕 moveNumX ..y 表示需要移动的下标 */
	private boolean drawSortArrayMoveNumber(int direction, Canvas canvas,
			Paint paint) {

		if (stepCount * Constant.density >= rectWidth + numSpace)
			return false;
		int moveNumX = sortArrayGame.getBlankX(), moveNumY = sortArrayGame
				.getBlankY();

		switch (direction) {
		case NumDirection.left:
			moveNumY++;
			break;
		case NumDirection.right:
			moveNumY--;
			break;
		case NumDirection.downward:
			moveNumX--;
			break;
		case NumDirection.upward:
			moveNumX++;
			break;
		}
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[i].length; j++) {
				if ((i == sortArrayGame.getBlankX() && j == sortArrayGame
						.getBlankY()) || (i == moveNumX && j == moveNumY)) {
					continue;
				}
				drawNumber(canvas, paint, array[i][j],
						j * rectWidth + numSpace, i * rectWidth + numSpace
								+ mapTop);
			}
		}

		switch (direction) {
		case NumDirection.left:
			drawNumber(canvas, paint, array[moveNumX][moveNumY], moveNumX
					* rectWidth + numSpace - stepCount * Constant.density,
					moveNumY * rectWidth + numSpace + mapTop);
			break;
		case NumDirection.right:
			drawNumber(canvas, paint, array[moveNumX][moveNumY], moveNumX
					* rectWidth + numSpace + stepCount * Constant.density,
					moveNumY * rectWidth + numSpace + mapTop);
			break;
		case NumDirection.downward:
			drawNumber(canvas, paint, array[moveNumX][moveNumY], moveNumX
					* rectWidth + numSpace, moveNumY * rectWidth + numSpace
					+ mapTop + stepCount*Constant.density);
			break;
		case NumDirection.upward:
			drawNumber(canvas, paint, array[moveNumX][moveNumY], moveNumX
					* rectWidth + numSpace, moveNumY * rectWidth + numSpace
					+ mapTop - stepCount*Constant.density);
			break;
		}
		stepCount++;
		return true;
	}
}














