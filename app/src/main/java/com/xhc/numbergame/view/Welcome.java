package com.xhc.numbergame.view;

import com.xhc.numbergame.tools.Constant;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

public class Welcome extends View {
	private Context context;
	public int where = 0;
	private Handler handler;
	public Welcome(Context context, Handler handler) {
		super(context);
		this.context = context;
//		new loading().start();
		this.handler = handler;
	}

	 
	/**
	 * 赤色 【RGB】255, 0, 0搜索 【CMYK】 0, 100, 100, 0 橙色 【RGB】 255, 165, 0 【CMYK】0,
	 * 35, 100, 0 黄色 【RGB】255, 255, 0 【CMYK】0, 0, 100, 0 绿色 【RGB】0, 255, 0
	 * 【CMYK】100, 0, 100, 0 青色 【RGB】0, 127, 255 【CMYK】100, 50, 0, 0 蓝色 【RGB】0,
	 * 0, 255 【CMYK】100, 100, 0, 0 紫色 【RGB】139, 0, 255 【CMYK】45, 100, 0, 0
	 */
	@Override
	public void onDraw(Canvas canvas) {
		where++;
		Paint paint = new Paint();
		Rect rect = new Rect();
		paint.setAntiAlias(true);
		/* 绘画进度条 */
		paint.setColor(Color.argb(255, 230, 230, 230));
		rect.set(0, Constant.height * 5 / 8, Constant.width,
				Constant.height * 6 / 8);
		canvas.drawRect(rect, paint);
		/* 绘画名字 */
		
		paint.setTextSize(Constant.height * 1 / 16);
		float textWidth = paint.measureText("数字游戏");
		FontMetrics fm = paint.getFontMetrics();
		float textHeight = (float) (Math.ceil(fm.descent - fm.ascent) );
		paint.setColor(Color.argb(255, 151, 173, 226));
		paint.setStrokeWidth(0); 
		canvas.drawText("数字游戏", Constant.width/2-textWidth/2, (Constant.height * 3 / 8) - textHeight/2, paint);

		paint.setColor(loadingColor(where));
		canvas.drawRect(loadingRect(where), paint);
		
	}
     
//	class loading extends Thread {
//
//		@Override
//		public void run() {
//			while (where < 7) {
//				try {
//					Thread.sleep(2000 / 7);
//					Welcome.this.postInvalidate();
//					where++;
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//			Message msg = new Message();
//			msg.arg1 = 1;
//			handler.sendMessage(msg);
//		}
//	}

	private Rect loadingRect(int where) {
		Rect rect = new Rect();
		switch (where) {
		case 1:
			rect.set(0, Constant.height * 5 / 8, 
					Constant.width * 1 / 7,Constant.height * 6 / 8);
			break;
		case 2:
			rect.set(Constant.width * 1 / 7, Constant.height * 5 / 8,
					Constant.width * 2 / 7, Constant.height * 6 / 8);
			break;
		case 3:
			rect.set(Constant.width * 2 / 7, Constant.height * 5 / 8,
					Constant.width * 3 / 7, Constant.height * 6 / 8);
			break;
		case 4:
			rect.set(Constant.width * 3 / 7, Constant.height * 5 / 8,
					Constant.width * 4 / 7, Constant.height * 6 / 8);
			break;
		case 5:
			rect.set(Constant.width * 4 / 7, Constant.height * 5 / 8,
					Constant.width * 5 / 7, Constant.height * 6 / 8);
			break;
		case 6:
			rect.set(Constant.width * 5 / 7, Constant.height * 5 / 8,
					Constant.width * 6 / 7, Constant.height * 6 / 8);
			break;
		case 7:
			rect.set(Constant.width * 6 / 7, Constant.height * 5 / 8,
					Constant.width, Constant.height * 6 / 8);
			break;

		}
		return rect;
	}

	private int loadingColor(int where) {
		switch (where) {
		case 1:
			return Color.argb(255, 255, 156, 156);
		case 2:
			return Color.argb(255, 255, 222, 158);
		case 3:
			return Color.argb(255, 255, 255, 162);
		case 4:
			return Color.argb(255, 158, 255, 158);
		case 5:
			return Color.argb(255, 158, 207, 255);
		case 6:
			return Color.argb(255, 156, 156, 255);
		case 7:
			return Color.argb(255, 214, 162, 255);
		}
		return 0;
	}
}
