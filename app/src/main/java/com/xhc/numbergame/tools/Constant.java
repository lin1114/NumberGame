package com.xhc.numbergame.tools;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class Constant {

	public static int width ;//720px
	public static int height ; //1280px
	public static float density ;//�ܶ�
	public static int densityDpi;//�ܶ�dpi
	
	public static int DatabaseVersion = 1;
 //�ı�ͼƬ�Ŀ�ȸ߶�
public static Bitmap resizeImage(Bitmap bitmap, int w, int h) {
		
        Bitmap BitmapOrg = bitmap;
        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        int newWidth = w;
        int newHeight = h;
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg,0, 0, width,height, matrix, true);
        return resizedBitmap;
    }
}
