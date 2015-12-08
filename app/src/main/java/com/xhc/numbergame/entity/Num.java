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
 * ����ֻΪ��������ʹ��
 * ����Ĺ����Ǹ��������ϵ����ֻ滭����ͼ�� ����ģ�����ֻ�����Ч��
 * @author Administrator
 */
public class Num {
	/**
	 * Ϊ�˱����˷Ѷѿռ�ȫ����һ������
	 */
	RectF rectf;
	/**
	 * mapLeft mapTop mapRight mapBottom ���������̵Ĳ���
	 */
	private float mapLeft;
	private float mapTop;
	private float mapRight;
	private float mapBottom;
	/**
	 * ��¼����������С���ֿ��ߵĲ���
	 */
	private int stepCount;
	/**
	 * x �Ƕ����С�y�Ƕ�����
	 */
	private int x, y;
	/**
	 * ����ÿ������ģ��Ŀ��(������)
	 */
	private float rectWidth;
	/**
	 * ģ����ģ��֮��ļ�϶
	 */
	private float numSpace;
	/**
	 * ���α߽ǵ�Բ����
	 */
	private float roundX;
	private float roundY;
	/**
	 * ÿ�����ֵĴ�С 1 2 3 4 and so on
	 */
	private int numSize;
	/**
	 * ��������ʾ�ĺ�̨����
	 */
	int[][] array;
	/**
	 * �����������
	 * */
	SortArrayGame sortArrayGame;

	/**
	 * Ϊ����Ĺ��캯��
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
				}// �ո�
				drawNumber(canvas, paint, sortArrayGame.getOriginalArray()[i][j],
						j * rectWidth + numSpace, i * rectWidth + numSpace
								+ mapTop);
			}
		}
	}
	
	/**
	 * ���������˵Ķ�λ����
	 */
	public void drawArray(Canvas canvas, Paint paint) {
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[i].length; j++) {
				if (array[i][j] == -1) {
					continue;
				}// �ո�

				drawNumber(canvas, paint, array[i][j],
						j * rectWidth + numSpace, i * rectWidth + numSpace
								+ mapTop);
			}
		}
	}

	/**
	 * �滭���� num�ĸ����� positionx���Ͻǵ�x���� positiony���Ͻǵ�y���� ��������ֵĸ߶ȺͿ��
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
	 * ��ȡÿ������ı���ɫ ��5����ɫ 2 3 5 7 �ı����ֱ�ͬ����ɫ 1�Ǹ���������ɫ
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
	 * ģ������ֻ�����Ч��
	 * 
	 * @param direction
	 *            ��Ҫ�ƶ��ķ���
	 * @param blankPositionX
	 *            �հ״�������ĺ����±�
	 * @param blankPositionY
	 *            �հ״�������������±�
	 *����false��ʾ�ƶ����    
	 */
	public void drawSortArray(int direction, Canvas canvas, Paint paint) {

			drawArray(canvas,paint); 
	}

	/* ����С������ƶ�Ч�� ����false��ʾ�ƶ���� moveNumX ..y ��ʾ��Ҫ�ƶ����±� */
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














