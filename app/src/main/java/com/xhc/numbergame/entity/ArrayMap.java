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
 * ���������滭��������
 * 
 * @author Administrator
 * 
 */
public class ArrayMap {
	RectF rectf;
	/**
	 * mapLeft mapTop mapRight mapBottom ���������̵Ĳ���
	 * 
	 * 
	 */
	private float mapLeft;
	private float mapTop;
	private float mapRight;
	private float mapBottom;
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
	/* ��Ϸ�Ŀ�ʼʱ�� */
	private long startTime;
	/* ��Ϸ�ļ���¼� end-start */
	public static int time;
	/* �����ݿ��л�ȡ��ʱ�� */
	public static int dataBaseTime;
	/**
	 * ������ť�Ŀ�Ⱥ��� �� �� �µ�����
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

		/* ������ť��λ�ó�ʼ�� */
		restRectWidth = (40 * Constant.density + 20);
		left = Constant.width - restRectWidth - 3 * numSpace;
		top = numSpace * 3;
		right = Constant.width - 2 * numSpace;
		bottom = (float) (restRectWidth * 2 / 3.0) + numSpace;
	}

	/* ��ʼ����ͼ���� ��������ť������ */
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

		/* ������ť��λ�ó�ʼ�� */
		restRectWidth = (40 * Constant.density + 20);
		left = Constant.width - restRectWidth - Constant.density * 2;
		top = Constant.density * 2;
		right = Constant.width - Constant.density * 2;
		bottom = (float) (restRectWidth * 2 / 3.0);

	}

	/* ������� ���һ���������ť ���� ԭʼͼ�� */
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

	/* ԭʼͼ�ΰ�ť�����Ͻǵĺ����� */
	private float getOrginButtonLeft() {
		return left - 2 * restRectWidth - numSpace;
	}

	/* ԭʼͼ�ΰ�ť���Ͻǵĺ����� */
	private float getOrginButtonRight() {
		return left - 2 * numSpace;
	}

	/* �ж��ǲ��ǵ��ԭʼ��ť���������鿴ԭʼ��ť */
	public boolean isClickOrginButton(float x, float y) {
		if (x >= getOrginButtonLeft() && x <= getOrginButtonRight() && y >= top
				&& y <= bottom) {
			return true;
		}
		return false;
	}

	/* Ϊ��������������map �鿴ԭʼͼ�ΰ�ť */
	public void drawSortMapForButton(int success, Canvas canvas, Paint paint) {
		drawMap(success, canvas, paint);
		drawLineRect(canvas, paint, getOrginButtonLeft(), top,
				getOrginButtonRight(), bottom);
		paint.setTextSize(Constant.density * 18);
		float textWidth = paint.measureText("ԭʼͼ��");
		FontMetrics fm = paint.getFontMetrics();
		float textHeight = (float) (Math.ceil(fm.descent - fm.ascent) - Constant.density * 8);
		canvas.drawText("ԭʼͼ��", getOrginButtonLeft()
				+ (getOrginButtonRight() - getOrginButtonLeft()) / 2
				- textWidth / 2, top + (bottom - top) / 2 + textHeight / 2,
				paint);
	}

	/* ����֪ͨ���� */
	public void drawInfo(String info, Canvas canvas, Paint paint) {
		paint.setColor(Color.argb(255, 204, 72, 64));
		paint.setTextSize(Constant.density * 8 + 30);

		FontMetrics fm = paint.getFontMetrics();
		float textHeight = (float) (Math.ceil(fm.descent - fm.ascent) - Constant.density * 8);
		canvas.drawText(info, numSpace * 5, mapTop - textHeight - textHeight
				/ 2, paint);
	}

	/*
	 * ����Ϸ�ɹ��㽫ʱ��ֹͣ ��Ϊ�����������������
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

	/* ����������ť ��ʱ�� */
	public void drawMap(int success, Canvas canvas, Paint paint) {
		paint.setColor(Color.argb(255, 204, 72, 64));
		paint.setTextSize(Constant.density * 8 + 30);
		FontMetrics fm = paint.getFontMetrics();
		float textHeight = (float) (Math.ceil(fm.descent - fm.ascent) - Constant.density * 8);
		if (success == GameState.Success) {
			canvas.drawText("��ϲ����ʱ��" + getTime(GameState.Success) + "��",
					numSpace * 5, mapTop - textHeight - textHeight / 2, paint);
		} else if(success == GameState.Nothing){
			canvas.drawText("ʱ�䣺" + getTime(GameState.Nothing) + "��",
					numSpace * 5, mapTop - textHeight, paint);
		}
		else{
			canvas.drawText("�ϴ�ʱ�䣺" + getTime(GameState.DbSuccess) + "��",
					numSpace * 5, mapTop - textHeight, paint);
		}
		drawReset(canvas, paint);
	}

	/* Ϊ��������������ť ��ʱ�� */
	public void drawNumAloneMap(int success, Canvas canvas, Paint paint) {
		paint.setColor(Color.argb(255, 204, 72, 64));
		paint.setTextSize(Constant.density * 8 + 30);
		FontMetrics fm = paint.getFontMetrics();
		float textHeight = (float) (Math.ceil(fm.descent - fm.ascent) - Constant.density * 8);
		if (success == GameState.Success) {
			canvas.drawText("��ϲ����ʱ��" + getTime(GameState.Success) + "��",
					numSpace * 3, mapTop / 2, paint);
		} 
		else if(success == GameState.DbSuccess){
			canvas.drawText("�ϴ�ʱ�䣺" + getTime(GameState.DbSuccess) + "��", numSpace,
					textHeight, paint);
		}
		else {
			canvas.drawText("ʱ�䣺" + getTime(GameState.Nothing) + "��", numSpace,
					textHeight, paint);
		}
		drawReset(canvas, paint);
	}

	/* �滭�����İ�ť */
	public void drawReset(Canvas canvas, Paint paint) {

		paint.setColor(Color.argb(255, 113, 150, 217));
		drawLineRect(canvas, paint, left, top, right, bottom);
		paint.setTextSize(Constant.density * 18);
		float textWidth = paint.measureText("����");
		FontMetrics fm = paint.getFontMetrics();
		float textHeight = (float) (Math.ceil(fm.descent - fm.ascent) - Constant.density * 8);
		canvas.drawText("����", left + (right - left) / 2 - textWidth / 2, top
				+ (bottom - top) / 2 + textHeight / 2, paint);
	}

	/* ģ����ľ��� */
	private void drawLineRect(Canvas canvas, Paint paint, float left,
			float top, float right, float bottom) {
		paint.setStrokeWidth(Constant.density + 5);
		canvas.drawLine(left, top, right, top, paint);
		canvas.drawLine(right, top, right, bottom, paint);
		canvas.drawLine(right, bottom, left, bottom, paint);
		canvas.drawLine(left, bottom, left, top, paint);
	}

	/* �����еļ����� */
	public void drawLineMap(Canvas canvas, Paint paint) {

		/* �����߿� */
		paint.setStrokeWidth(numSpace);
		/* ��ϸ�� */
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
		/* ��ϸ�� */
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

		/* ������ */
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
		/* ����� */
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
	 * �ж��Ƿ�����������ť
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
