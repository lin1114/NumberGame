package com.xhc.numbergame.entity;

import com.xhc.numbergame.tools.MyFile;
import com.xhc.numbergame.tools.NumDirection;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * 为排序数组使用
 * 需要先生成一个“图形”数组
 * 然后将这个数组打乱
 * 注意：数据可能是刚产生的。也有可能是从数据库中读取出来的
 * @author Administrator
 *
 */
public class SortArrayGame extends ArrayGame{
	/**
	 * 数组中空白地方的下标x,y
	 */
   private int blankX,blankY;
   private int level ;
   /**
    * array 是最原始的数组，正确的数组
    * confusearray是用来显示在界面上的数组打乱后的
    */
   private int[][] array,confuseArray ;
   private Handler handler;
   /*判断是不是点击了重来*/
   private boolean resteFlag = false; 
   private int x ;
   /**
    * 构造函数中先判断数据库中是否有存档没有再自己新建一个
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
   /*复制数组用来后面校正时使用*/
   private void copyArray(){
	   for(int i = 0 ;i < array.length ; i++){
		   for(int j = 0 ; j < array[i].length ; j ++){
			   confuseArray[i][j] = array[i][j];
		   }
	   }
   }
   /*判断两个数组是否相同 */
   public boolean isSameArray(){
	   if(confuseArray[confuseArray.length - 1][0] != -1){return false;}//最下面的不是-1 就不是原数组
	   for(int i = 0 ;i < array.length ; i++){
		   for(int j = 0 ; j < array[i].length ; j ++){
			  if( confuseArray[i][j] == array[i][j]);
			  else return false;
		   }
	   }
	   return true;
   }
    /*
    * 产生一个普通的数组
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
    * 将从数据库中获取的数据填入两个数组中。一个array和confusearray
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
    * 产生一个蛇形数组
    * 偶数行向右
    * 奇数行向左
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
    * 产生一个环形数组
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
				if (flag == 1) {// 横向向右走
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
				if (flag == 2) {// 竖直向下走
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
				if (flag == 3) {// 横向向左走
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
				if (flag == 4) {// 竖直向上走
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
   /*获取原始数组*/
   public int[][] getOriginalArray(){
	   return array;
   }
   
   
   /**
    * 通过方向直接将数组中的数字进行转换
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
   /*将confuse数组打乱 不同level获取不同类型的数组*/
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
    * 随机打乱数组
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
		  
		  
   /*重来因为是重来所以原型数组是相同的不用再copy一次 */
   public void reste(){
	   resteFlag = true;
	   getConfuseArray();
   }
   /*获取打乱后的数组*/
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











