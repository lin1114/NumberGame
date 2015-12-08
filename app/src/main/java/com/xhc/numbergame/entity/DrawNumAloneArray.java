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
 * 根据数独的数组绘画出界面
 * 
 * */
public class DrawNumAloneArray {
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

	/* 用来记录当前下哪个宫格对应到后台数组中 */
	private int clickX = -1, clickY = -1;

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
	 * 排序数组对象
	 * */
	NumAloneArray array;

	/**
	 * 为排序的构造函数
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

	/* 绘画汇总 */
	public void onDrawMap(Canvas canvas, Paint paint) {
		/*绘制的按钮背景*/
		drawBackground(canvas,paint,clickX,clickY);
		for (int i = 0; i < array.getArray().length; i++) {
			for (int j = 0; j < array.getArray().length; j++) {
				
				drawNumber(canvas, paint, j * rectWidth + numSpace + 1, i
						* rectWidth + numSpace + mapTop + 1,
						array.getArray()[i][j]);
			}
		}
		/*绘画最下面按钮*/
		drawClickNum(canvas, paint);
		/*绘制候选数*/
		drawCandidateNum(canvas,paint);
	}

	/* 当点击到了棋盘中的数字后绘制后面的背景 */
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
	 * 判断点击事件
	 * 1.点击事件     a.点击棋盘 选中棋盘 然后再候选区中显示候选区中的数字 b.点击普通按键将数字放入候选区中
	 *          c.点击候选区中的数字表示用户已经确认了该数字 
	 * 2.长按事件     a.长按棋盘 表示将用户已经确认了的数字去掉显示候选区中的数字
	 *          b.长按普通按键表示用户已经确认了数字 c.长按候选区按键表示删去该数字
	 * */
	public void clickEvent(float x, float y, int clickFlag) {
		if(clickFlag == ClickFlag.click){
			/* 在棋盘中 */
			if ((y >= mapTop -rectWidth ) && y <= (mapTop + 9 * rectWidth + numSpace)
					&& (x > numSpace && x < Constant.width - numSpace)) {
				clickX = (int) (Math.floor((y - mapTop- numSpace - 1)) / rectWidth);
				clickY = (int) (Math.floor((x - numSpace - 1)) / rectWidth);
			}
			
			/* 候选区中的按钮 单击后上屏 */
			else if (y >= mapBottom && y <= Constant.height - rectWidth) {
                int candidateX = (int) ((x - numSpace) / rectWidth) ;
                try{
                	/*此处可能出现的异常 clickx和clicky condidatex都可能会造成数组下标越界*/
                	array.getArray()[clickX][clickY].userNum =
                			(Integer) array.getArray()[clickX][clickY].getBack().get(candidateX);	
                }catch(Exception e){
                	array.getArray()[clickX][clickY].userNum = 0 ;
                }
			}
			else if(y >= Constant.height - rectWidth){
				int tempX = (int) ((x - numSpace) / rectWidth) ;
				/*先临时打个补丁*/
				if(tempX >= 8){
					tempX = 8;
				}
				try{
	                	/*此处可能出现的异常 clickx和clicky condidatex都可能会造成数组下标越界*/
	                	array.getArray()[clickX][clickY].setBackNum(tempX+1) ;
	            }catch(Exception e){
	            }
			}
		}
		else if(clickFlag == ClickFlag.longClick){
			/* 在棋盘中 */
			if ((y >= mapTop -rectWidth ) && y <= (mapTop + 9 * rectWidth + numSpace)
					&& (x > numSpace && x < Constant.width - numSpace)) {
				clickX = (int) (Math.floor((y - mapTop- numSpace - 1)) / rectWidth);
				clickY = (int) (Math.floor((x - numSpace - 1)) / rectWidth);
				array.getArray()[clickX][clickY].userNum = 0 ;
			}
			/* 候选区中的按钮长按后去除此数字*/
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
				/*先临时打个补丁*/
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
	/*绘画候选数字*/
     private void drawCandidateNum(Canvas canvas,Paint paint){
    	 if(clickX == -1 && clickY == -1)return ;
    	 for(int i = 0 ; i < array.getArray()[clickX][clickY].getBack().size();i++){
			drawNormalNumber(canvas, paint, false,
					Integer.parseInt(array.getArray()[clickX][clickY].getBack().get(i)+""), 
					i * rectWidth + numSpace + 1, mapTop + 9 * rectWidth + numSpace + 1);
    	 }
     }
	/* 画在最下面的数字。最后一行共用户点击 倒数第二行的数字是候选数 */
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
	 * 绘画数字 num哪个数字 positionx左上角的x坐标 positiony左上角的y坐标 计算出数字的高度和宽度 
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
	 * 绘画数字 num哪个数字 positionx左上角的x坐标 positiony左上角的y坐标 计算出数字的高度和宽度
	 */
	public void drawNumber(Canvas canvas, Paint paint, float positionX,
			float positionY, NumNode node) {

		float textHeight = 0, textWidth = 0;
		if (node.flag) {
			/* 被抹去了的数字 */
			if (node.userNum != 0) {
				/* 显示用户填写了的数字 */
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
				/* 用户填写了候选的数字在这个格子中可能会填写九个数字 */

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
					int x = i / 3;// 行
					int y = i % 3;// 列
					canvas.drawText(node.getBack().get(i) + "", positionX + x
							* littleNumWidth + textWidth, positionY + y
							* littleNumWidth + textHeight, paint);
				}
			} else {
				/* 用户还没有填写数字 */
			}
		}
		else {
			/* 没有抹去的数字。显示系统的数字 */
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
