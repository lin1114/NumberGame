package com.xhc.numbergame.entity;

import com.xhc.numbergame.entity.NumAloneArray.NumNode;
import com.xhc.numbergame.tools.ClickFlag;
import com.xhc.numbergame.tools.Constant;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.Log;

/*
 * ��������������滭������
 * 
 * */
public class DrawNumAloneArray {
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

	/* ������¼��ǰ���ĸ������Ӧ����̨������ */
	private int clickX = -1, clickY = -1;

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
	 * �����������
	 * */
	NumAloneArray array;

	/**
	 * Ϊ����Ĺ��캯��
	 * */
	public DrawNumAloneArray(NumAloneArray array) {
		this.array = array;
		rectf = new RectF();
		this.array = array;
		int x = array.getArray().length;
		numSpace = (12 / x) * Constant.density;
		roundX = (35 / x) * Constant.density;
		if (roundX > 25)
			roundX = 25;
		roundY = roundX;

		mapLeft = numSpace;
		mapRight = Constant.width - numSpace;
		rectWidth = (mapRight - mapLeft) / x;
		mapTop = ((Constant.height - Constant.width) - 2*rectWidth - 2 * numSpace);
		mapBottom = Constant.height - 2*rectWidth;
		numSize = (int) (rectWidth / 3);
	}

	/* �滭���� */
	public void onDrawMap(Canvas canvas, Paint paint) {
		/*���Ƶİ�ť����*/
		drawBackground(canvas,paint,clickX,clickY);
		for (int i = 0; i < array.getArray().length; i++) {
			for (int j = 0; j < array.getArray().length; j++) {
				
				drawNumber(canvas, paint, j * rectWidth + numSpace + 1, i
						* rectWidth + numSpace + mapTop + 1,
						array.getArray()[i][j]);
			}
		}
		/*�滭�����水ť*/
		drawClickNum(canvas, paint);
		/*���ƺ�ѡ��*/
		drawCandidateNum(canvas,paint);
	}

	/* ��������������е����ֺ���ƺ���ı��� */
	private void drawBackground(Canvas canvas,Paint paint,int x ,int y ){
		if(x == clickX && y == clickY){
			rectf.set(y * rectWidth + 3 * numSpace + 1, x * rectWidth + 2
					* numSpace + mapTop + 1,
					(y + 1) * rectWidth - 2 * numSpace, (x + 1) * rectWidth
							+ mapTop - 2 * numSpace);
			paint.setColor(Color.argb(255, 150, 150, 150));
			canvas.drawRect(rectf, paint);
		}
	}

	/**
	 * �жϵ���¼�
	 * 1.����¼�     a.������� ѡ������ Ȼ���ٺ�ѡ������ʾ��ѡ���е����� b.�����ͨ���������ַ����ѡ����
	 *          c.�����ѡ���е����ֱ�ʾ�û��Ѿ�ȷ���˸����� 
	 * 2.�����¼�     a.�������� ��ʾ���û��Ѿ�ȷ���˵�����ȥ����ʾ��ѡ���е�����
	 *          b.������ͨ������ʾ�û��Ѿ�ȷ�������� c.������ѡ��������ʾɾȥ������
	 * */
	public void clickEvent(float x, float y, int clickFlag) {
		if(clickFlag == ClickFlag.click){
			/* �������� */
			if ((y >= mapTop -rectWidth ) && y <= (mapTop + 9 * rectWidth + numSpace)
					&& (x > numSpace && x < Constant.width - numSpace)) {
				clickX = (int) (Math.floor((y - mapTop- numSpace - 1)) / rectWidth);
				clickY = (int) (Math.floor((x - numSpace - 1)) / rectWidth);
			}
			
			/* ��ѡ���еİ�ť ���������� */
			else if (y >= mapBottom && y <= Constant.height - rectWidth) {
                int candidateX = (int) ((x - numSpace) / rectWidth) ;
                try{
                	/*�˴����ܳ��ֵ��쳣 clickx��clicky condidatex�����ܻ���������±�Խ��*/
                	array.getArray()[clickX][clickY].userNum =
                			(Integer) array.getArray()[clickX][clickY].getBack().get(candidateX);	
                }catch(Exception e){
                	array.getArray()[clickX][clickY].userNum = 0 ;
                }
			}
			else if(y >= Constant.height - rectWidth){
				int tempX = (int) ((x - numSpace) / rectWidth) ;
				/*����ʱ�������*/
				if(tempX >= 8){
					tempX = 8;
				}
				try{
	                	/*�˴����ܳ��ֵ��쳣 clickx��clicky condidatex�����ܻ���������±�Խ��*/
	                	array.getArray()[clickX][clickY].setBackNum(tempX+1) ;
	            }catch(Exception e){
	            }
			}
		}
		else if(clickFlag == ClickFlag.longClick){
			/* �������� */
			if ((y >= mapTop -rectWidth ) && y <= (mapTop + 9 * rectWidth + numSpace)
					&& (x > numSpace && x < Constant.width - numSpace)) {
				clickX = (int) (Math.floor((y - mapTop- numSpace - 1)) / rectWidth);
				clickY = (int) (Math.floor((x - numSpace - 1)) / rectWidth);
				array.getArray()[clickX][clickY].userNum = 0 ;
			}
			/* ��ѡ���еİ�ť������ȥ��������*/
			else if (y >= mapBottom && y <= Constant.height - rectWidth) {
                int candidateX = (int) ((x - numSpace) / rectWidth) ;
                try{
					array.getArray()[clickX][clickY].clearBackNum((Integer)(array
							.getArray()[clickX][clickY].getBack().get(
							candidateX )));
                }catch(Exception e){
                	array.getArray()[clickX][clickY].userNum = 0 ;
                }
			}
			else if(y >= Constant.height - rectWidth){
				int tempX = (int) ((x - numSpace) / rectWidth) ;
				/*����ʱ�������*/
				if(tempX >= 8){
					tempX = 8;
				}
				try{
					array.getArray()[clickX][clickY].userNum =  tempX + 1;
					
	            }catch(Exception e){
	            }
			}
		}
		
	}
	/*�滭��ѡ����*/
     private void drawCandidateNum(Canvas canvas,Paint paint){
    	 if(clickX == -1 && clickY == -1)return ;
    	 for(int i = 0 ; i < array.getArray()[clickX][clickY].getBack().size();i++){
			drawNormalNumber(canvas, paint, false,
					Integer.parseInt(array.getArray()[clickX][clickY].getBack().get(i)+""), 
					i * rectWidth + numSpace + 1, mapTop + 9 * rectWidth + numSpace + 1);
    	 }
     }
	/* ��������������֡����һ�й��û���� �����ڶ��е������Ǻ�ѡ�� */
	private void drawClickNum(Canvas canvas, Paint paint) {
		paint.setColor(Color.argb(255, 128, 52, 0));
		canvas.drawLine(0, mapTop + 10 * rectWidth + numSpace,
				Constant.width, mapTop + 10 * rectWidth + numSpace, paint);
			for (int j = 0; j < 9; j++) {
					drawNormalNumber(canvas, paint, true, j + 1, j * rectWidth
							+ numSpace + 1, mapTop + 10 * rectWidth + numSpace
							+ 1);
		}
	}

	/**
	 * �滭���� num�ĸ����� positionx���Ͻǵ�x���� positiony���Ͻǵ�y���� ��������ֵĸ߶ȺͿ�� 
	 */
	public void drawNormalNumber(Canvas canvas, Paint paint, boolean flag,
			int num, float positionX, float positionY) {
		if (flag) {
			paint.setColor(Color.argb(255, 187, 173, 160));
		} else {
			paint.setColor(Color.argb(255, 241, 168, 168));
		}
		rectf.set(positionX, positionY, positionX + rectWidth - numSpace,
				positionY + rectWidth - numSpace);
		canvas.drawRoundRect(rectf, 0, 0, paint);
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
	 * �滭���� num�ĸ����� positionx���Ͻǵ�x���� positiony���Ͻǵ�y���� ��������ֵĸ߶ȺͿ��
	 */
	public void drawNumber(Canvas canvas, Paint paint, float positionX,
			float positionY, NumNode node) {

		float textHeight = 0, textWidth = 0;
		if (node.flag) {
			/* ��Ĩȥ�˵����� */
			if (node.userNum != 0) {
				/* ��ʾ�û���д�˵����� */
				paint.setColor(Color.argb(255, 95, 95, 95));
				paint.setTextSize(numSize);
				paint.setTypeface(Typeface.DEFAULT_BOLD);
				textWidth = paint.measureText(node.userNum + "");
				FontMetrics fm = paint.getFontMetrics();
				textHeight = (float) (Math.ceil(fm.descent - fm.ascent) - Constant.density * 8);
				if (textHeight <= 0)
					textHeight = (float) (Math.ceil(fm.descent - fm.ascent));

				canvas.drawText(node.userNum + "", positionX
						+ (rectWidth - numSpace) / 2 - textWidth / 2, positionY
						+ (rectWidth - numSpace) / 2 + textHeight / 2, paint);

			} else if (node.getBack().size() != 0) {
				/* �û���д�˺�ѡ����������������п��ܻ���д�Ÿ����� */

				paint.setColor(Color.argb(255, 0, 0, 0));
				paint.setTextSize(numSize*2/3);
				paint.setTypeface(Typeface.DEFAULT_BOLD);
				textWidth = paint.measureText(node.userNum + "");
				FontMetrics fm = paint.getFontMetrics();
				textHeight = (float) (Math.ceil(fm.descent - fm.ascent) - Constant.density * 2.5);
				if (textHeight <= 0)
					textHeight = (float) (Math.ceil(fm.descent - fm.ascent));
				float littleNumWidth = rectWidth / 3;
				for (int i = 0; i < node.getBack().size(); i++) {
					int x = i / 3;// ��
					int y = i % 3;// ��
					canvas.drawText(node.getBack().get(i) + "", positionX + x
							* littleNumWidth + textWidth, positionY + y
							* littleNumWidth + textHeight, paint);
				}
			} else {
				/* �û���û����д���� */
			}
		}
		else {
			/* û��Ĩȥ�����֡���ʾϵͳ������ */
			paint.setColor(Color.argb(255, 0, 0, 0));
			paint.setTextSize(numSize);
			paint.setTypeface(Typeface.DEFAULT_BOLD);
			textWidth = paint.measureText(node.systemNum + "");
			FontMetrics fm = paint.getFontMetrics();
			textHeight = (float) (Math.ceil(fm.descent - fm.ascent) - Constant.density * 8);
			if (textHeight <= 0)
				textHeight = (float) (Math.ceil(fm.descent - fm.ascent));

			canvas.drawText(node.systemNum + "", positionX
					+ (rectWidth - numSpace) / 2 - textWidth / 2, positionY
					+ (rectWidth - numSpace) / 2 + textHeight / 2, paint);

		}

	}
}
