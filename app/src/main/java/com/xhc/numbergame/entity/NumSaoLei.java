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
 * ����ɨ�׵Ľ��� ��ȫ�Ǳ����ʱ����ɫһ�� ����
 * 
 * @author Administrator
 *
 */
public class NumSaoLei {
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

	private SaoLeiArray saoLeiArray;

	/**
	 * ���ݳ�ʼ��
	 * 
	 * @param saoLeiArray
	 *            saoLeiArray.getSaoLeiNodeArray()[0].length �����е�һ�еĳ���
	 *            �������������ݶ��Ǹ�����������
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

	/* ͨ�������ϵ�����x y ������������ϵ�����x y ע��������x y ��������x y������ */
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
		/* ��С���� */
		rectf.set(positionX, positionY, positionX + rectWidth - numSpace,
				positionY + rectWidth - numSpace);
		canvas.drawRoundRect(rectf, roundX, roundY, paint);
		/* �������ֻ����ʺ� ���� ���� ����ը�� */
		paint.setColor(Color.argb(255, 0, 0, 0));
		paint.setTextSize(numSize);
		paint.setTypeface(Typeface.DEFAULT_BOLD);
		/* ����ͼ�θ߶� */
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
			/* ��ȷ����ʲô */
			float textWidthQuestion = paint.measureText("��");
			canvas.drawText("��", positionX + (rectWidth - numSpace) / 2
					- textWidthQuestion / 2, positionY + (rectWidth - numSpace)
					/ 2 + textHeight / 2, paint);
			break;
		case 3:
			/* ������� */
			float textWidthBomb = paint.measureText("��");
			canvas.drawText("��", positionX + (rectWidth - numSpace) / 2
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









