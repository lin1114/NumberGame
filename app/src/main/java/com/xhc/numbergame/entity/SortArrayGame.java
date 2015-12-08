package com.xhc.numbergame.entity;

import com.xhc.numbergame.tools.MyFile;
import com.xhc.numbergame.tools.NumDirection;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Ϊ��������ʹ��
 * ��Ҫ������һ����ͼ�Ρ�����
 * Ȼ������������
 * ע�⣺���ݿ����Ǹղ����ġ�Ҳ�п����Ǵ����ݿ��ж�ȡ������
 * @author Administrator
 *
 */
public class SortArrayGame extends ArrayGame{
	/**
	 * �����пհ׵ط����±�x,y
	 */
   private int blankX,blankY;
   private int level ;
   /**
    * array ����ԭʼ�����飬��ȷ������
    * confusearray��������ʾ�ڽ����ϵ�������Һ��
    */
   private int[][] array,confuseArray ;
   private Handler handler;
   /*�ж��ǲ��ǵ��������*/
   private boolean resteFlag = false; 
   private int x ;
   /**
    * ���캯�������ж����ݿ����Ƿ��д浵û�����Լ��½�һ��
    * @param x
    * @param level
    * @param handler
    */
   public SortArrayGame(int x,int level,Handler handler){
	   this.x = x ;
	   this.level = level;
	   this.handler = handler;
	   array = new int[x+1][];
	   confuseArray = new int[x+1][];
	   for(int i = 0 ; i < x ; i ++){
		   array[i] = new int[x];
		   confuseArray[i] = new int[x];
	   }
	   array[x] = new int[1];
	   confuseArray[x] = new int[1];
   }
   public void begin(){
	   getConfuseArray();
   }
   /*����������������У��ʱʹ��*/
   private void copyArray(){
	   for(int i = 0 ;i < array.length ; i++){
		   for(int j = 0 ; j < array[i].length ; j ++){
			   confuseArray[i][j] = array[i][j];
		   }
	   }
   }
   /*�ж����������Ƿ���ͬ */
   public boolean isSameArray(){
	   if(confuseArray[confuseArray.length - 1][0] != -1){return false;}//������Ĳ���-1 �Ͳ���ԭ����
	   for(int i = 0 ;i < array.length ; i++){
		   for(int j = 0 ; j < array[i].length ; j ++){
			  if( confuseArray[i][j] == array[i][j]);
			  else return false;
		   }
	   }
	   return true;
   }
    /*
    * ����һ����ͨ������
    */
   private void produceNormalArray(){
	   for(int i = 0,count = 1 ; i < array.length ; i++){
		   for(int j = 0 ; j < array[i].length ; j++,count ++){
			   if(i == array.length - 1 && j == array[i].length - 1 ){ 
				   array[i][j] = -1;
				   blankX = i ;
				   blankY = j ;
				   break;
			   }
			   array[i][j] = count;
		   }
	   }
   }
   
   /*
    * �������ݿ��л�ȡ�������������������С�һ��array��confusearray
    * */
   public void setArray(String content){
	   String[] arrayConllection = content.split("#");
	   String[] originalArray = arrayConllection[0].split(",");
	   String[] newConfuseArray = arrayConllection[1].split(",");
	   for(int i = 0 ;i < x ;++ i){
		   for(int j = 0 ; j < x ; ++ j){
			   array[i][j] = Integer.parseInt(originalArray[i*x+j]);
		       confuseArray[i][j] = Integer.parseInt(newConfuseArray[i*x+j]);
		       if(Integer.parseInt(newConfuseArray[i*x+j]) == -1){
		    	   blankX = i ;
		    	   blankY = j ;
		       }
		   }
	   }
	   array[x][0] = Integer.parseInt(originalArray[originalArray.length - 1]);
	   confuseArray[x][0] = Integer.parseInt(newConfuseArray[newConfuseArray.length - 1]);
	   
//	   String temp = "",temp2 = "";
//	   for(int i = 0 ; i < x ; ++ i){
//		   for(int j = 0 ; j < x ; ++ j){
//			  temp +=   array[i][j]+" " ;
//			  temp2 += confuseArray[i][j]+" ";
//		   }
//		   temp += "\r\n";
//		   temp2 += "\r\n";
//	   }
//	   temp += array[x][0]+"";
//	   temp2 +=  confuseArray[x][0] +"";
//	   MyFile.writeLog("sortarraygame", temp);
//	   MyFile.writeLog("sortarraygame2", temp2);
   }
 
   /*
    * ����һ����������
    * ż��������
    * ����������
    * */
   private void produceSnakeArray(){
	   for(int i = 0,count = 1  ;i < array.length ; ++ i ){
		   for(int j = 0 ; j < array[i].length ; ++ j,++ count){
			   if(i == array.length - 1 && j == array[i].length - 1 ){ 
				   array[i][j] = -1;
				   blankX = i ;
				   blankY = j ;
				   break;
			   }
			   else if(i % 2 == 0){array[i][j] = count;}
			   else if(i % 2 != 0){array[i][array[i].length - 1 -j] = count ;}
		   }
	   }
   }
   /**
    * ����һ����������
    */
	private void produceHuanArray() {
		int count = array.length - 1;
		int I = 0, J = 0;
		blankX = array.length - 1;
		blankY = 0;
		array[blankX][blankY] = -1;
		int flag = 1;// 1 i j++ 2 i++ j 3 i j-- 4 i-- j
		try {
			for (int i = 1; i <= count * count; i++) {
				if (flag == 1) {// ����������
					if (J >= count) {
						J = count - 1;
						I++;
						flag = 2;
					} else if (array[I][J] != 0) {
						J--;
						I++;
						flag = 2;
					} else {
						array[I][J] = i;
						J++;
					}
				}
				if (flag == 2) {// ��ֱ������
					if (I >= count) {
						I = count - 1;
						J--;
						flag = 3;
					} else if (array[I][J] != 0) {
						I--;
						J--;
						flag = 3;
					} else {
						array[I][J] = i;
						I++;
					}
				}
				if (flag == 3) {// ����������
					if (J < 0) {
						J = 0;
						I--;
						flag = 4;
					} else if (array[I][J] != 0) {
						J++;
						I--;
						flag = 4;
					} else {
						array[I][J] = i;
						J--;
					}
				}
				if (flag == 4) {// ��ֱ������
					if (I < 0) {
						I = 0;
						flag = 1;
						J++;
					} else if (array[I][J] != 0) {
						I++;
						J++;
						flag = 1;
						i--;
					} else {
						array[I][J] = i;
						I--;
					}
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
   /*��ȡԭʼ����*/
   public int[][] getOriginalArray(){
	   return array;
   }
   
   
   /**
    * ͨ������ֱ�ӽ������е����ֽ���ת��
    * @param direction
    */
   public void moveArray(int direction){
	   switch(direction){
	     case NumDirection.downward:
	    	 /*i++*/
	    	 if(blankX <= 0 )break;
	    	 confuseArray[blankX][blankY] = confuseArray[blankX - 1 ][blankY];
             blankX --;
             confuseArray[blankX][blankY] = -1 ;
	    	 break;
	     case NumDirection.upward:
	    	 /*i--*/
	    	 if((blankY == 0&&blankX >= confuseArray.length-1) || (blankY > 0&&blankX >= confuseArray.length-2))
	    		 break;
	    	 confuseArray[blankX][blankY] = confuseArray[blankX +1 ][blankY];
             blankX++;
             confuseArray[blankX][blankY] = -1 ;
 	    	 break;
	     case NumDirection.left:
	    	 /*j--*/
	    	 
	    	 if(blankY >= confuseArray[blankX].length - 1)break;
	    	 confuseArray[blankX][blankY] = confuseArray[blankX][blankY + 1];
	    	 blankY++;
	    	 confuseArray[blankX][blankY] = -1 ;
	    	 break;
	     case NumDirection.right:
	    	 /*j++*/
	    	 if(blankY <= 0 )break;
	    	 confuseArray[blankX][blankY] = confuseArray[blankX][blankY - 1];
	    	 blankY--;
	    	 confuseArray[blankX][blankY] = -1 ;
	    	 break;
	   }
 
   }
   /*��confuse������� ��ͬlevel��ȡ��ͬ���͵�����*/
   public void getConfuseArray(){
	   switch(level){
	   case 1 :
		   produceNormalArray();
		   break;
	   case 2:
		   produceSnakeArray();
		   break;
	   case 3:
		   produceHuanArray();
		   break;
	   }
	   
	   copyArray();
	   confuseNum();
   }
   /*
    * �����������
    * */
   private void confuseNum(){
	    int direction;
		   for(int i = 0 ; i < level * 700 ;i++){
			   moveArray((int)(Math.random() * 100 % 5 ));
		   }
		   if(!resteFlag){ 
			   Message msg = new Message();
			   msg.arg1 = 1;
			   handler.sendMessage(msg);
		   }
   }
		  
		  
   /*������Ϊ����������ԭ����������ͬ�Ĳ�����copyһ�� */
   public void reste(){
	   resteFlag = true;
	   getConfuseArray();
   }
   /*��ȡ���Һ������*/
   public int[][] getArray(){
	   return confuseArray;
   }
   public int getBlankX() {
		return blankX;
	}
	public int getBlankY() {
		return blankY;
	}
}











