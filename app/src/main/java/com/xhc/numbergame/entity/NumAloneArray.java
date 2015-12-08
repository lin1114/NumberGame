package com.xhc.numbergame.entity;

import java.util.ArrayList;

import com.xhc.numbergame.entity.NumAloneArray.NumNode;
import com.xhc.numbergame.tools.GameState;
import com.xhc.numbergame.tools.MyFile;

import android.util.Log;


/**
 * 此类是数独的后台二维数组 一定是同行数同列数 类似 4X4 9x9 25X25
 * 先将数据排好。然后再随机摸去几个数据供用户填写(提供在每个节点处写几个数据（在那个数据的时候不确定是哪个）) 数独中的每个节点是NumNode(提供
 * 原数（系统生成）,确定数,替代数) 最后比较usernum和sysnum便可以知道是否成功
 * 
 * @author Administrator
 */
public class NumAloneArray extends ArrayGame {

	/* 几阶数组 一般9x9 */
	int x;
	NumNode array[][];
	int level;
	/* 用来记录位置莫位置上测试过的数字，并且如果确定后就将数字放入其中进行记录 */
	ArrayList<recordPosition> arrayPosition = new ArrayList<recordPosition>();

	public NumAloneArray(int x, int level) {
		this.x = x;
		this.level = level;
		array = new NumNode[x][x];
		for (int i = 0; i < x; i++) {
			for (int j = 0; j < x; j++) {
				array[i][j] = new NumNode();
			}
		}
	}
	
	/*判断是否是赢了比赛*/
	public int isVictory(){
		for(int i = 0 ;i < array.length; i ++){
			for(int j = 0 ; j < array[i].length ; j ++){
				if(array[i][j].userNum == 0 && array[i][j].flag){
					return GameState.Nothing;
				}
				else{
					if(array[i][j].flag && isContradict2(i, j, array[i][j].userNum)){
						continue;
					}
					else if(array[i][j].flag && !isContradict2(i, j, array[i][j].userNum)){
						return GameState.Nothing;
					}
				}
			}
		}
		return GameState.Success;
	}
	
	/*开始游戏*/
	public void begin(){
		for(int i = 0 ;i < array.length ; i++){
			for(int j = 0 ;j < array[i].length ; j++){
				array[i][j].flag = false;
				array[i][j].systemNum = 0 ;
				array[i][j].userNum = 0 ;
				array[i][j].getBack().clear();
			}
		}
		setNumber();
		removeSomeNumber();
	}
 
	/*随机投放复合规则的九个数字*/
    private void randomSetNum(){
    	for(int i = 0 ; i < 9 ; i ++){
    		int x = getRandom()-1;
    		int y = getRandom()-1;
    		int tempNum = getRandom();
    		if(isContradict(x, y, tempNum)){
    			array[x][y].systemNum = tempNum;
    		}
    		else i--;
    	}
    	/*其他没有添加的数字 置为抹去*/
    	for(int i = 0 ;i < 9 ; i ++){
    		for(int j = 0 ; j < 9 ; j ++){
    			if(array[i][j].systemNum == 0)
    			array[i][j].flag = true;
    		}
    	}
    } 
    
   /**
    * 获取从数据库中的数字
    *  system,usernum,flag,1@3@#system,usernum,flag,1@3@#
	* 系统生成数system,用户数，标志，候选数#下个节点*/
    public void setArray(String content){
    	String[] tempNode = content.split("#");
    	for(int i = 0 ; i < array.length ; ++ i ){
    		for(int j = 0 ;j < array[i].length ;++ j){
    			
    			String[] temp = tempNode[i*array[i].length+j].split(",");
    			array[i][j].systemNum = Integer.parseInt(temp[0]);
    			array[i][j].userNum = Integer.parseInt(temp[1]);
    			array[i][j].flag = Boolean.parseBoolean(temp[2]);
    			if(temp.length >= 4){
    				String[] backNum = temp[3].split("@");
    				for(int back = 0 ; back < backNum.length ; ++ back){
    					array[i][j].getBack().add(Integer.parseInt(backNum[back]));
    				}
    			}
    		}
    	}
    }
    
    public void setArray(NumNode[][] node){
    	this.array = node;
    }
    
    
    /*构造一个数独终盘*/
    private void setNumber(){
    	randomSetNum();
    	answerShuDu();
    }
	public NumNode[][] getArray() {
		return array;
	}

	/**
	 * 
	 * 1等难度: 随机去除40或者45个空格
	 * 2等难度：随机去除46—50个空格
	 * 3等难度:偶数行不去i，8奇数行不去除(间隔) i、0这个数 去除51-55个空格
	 * 4等难度：蛇形去除   55-60个空格
	 * 5等难度:从左至右从上至下 60-62个空格
	 * 
	 * */
	private void removeSomeNumber() {
		if (level == 1) {
			for (int i = 0; i < 47; i++) {
				int x = getRandom8();
				int y = getRandom8();
				if(array[x][y].flag){
                 /*避免挖到同一个洞*/
					i--;
					continue;
				}
				/*尝试挖洞*/
				array[x][y].flag = true;
				if (!checkSole(x, y)) {
					/*挖洞不满足唯一性 填上*/
					i--;
					array[x][y].flag = false;	
				}  
			}
		} else if (level == 2) {
			for (int i = 0; i < 55; i++) {
				int x = getRandom8();
				int y = getRandom8();
				if(array[x][y].flag){
                 /*避免挖到同一个洞*/
					i--;
					continue;
				}
				/*尝试挖洞*/
				array[x][y].flag = true;
				if (!checkSole(x, y)) {
					/*挖洞不满足唯一性 填上*/
					Log.e("挖洞中.....","i = "+i);
					i--;
					array[x][y].flag = false;	
				}  
			}
		}
		/* 间隔方式 */
		else if (level == 3) {
			for(int i = 0 ;i < array.length ; i ++){
				if( i % 2 == 0){
					/*偶数行 i 8这个洞不能挖*/
					for(int j = 0 ;j < 6 ; j ++){
						int tempY = getRandom8();
						if(tempY == 8 || array[i][tempY].flag){j--;continue;}
						array[i][tempY].flag = true;
						if (!checkSole(i, tempY)) {
							/*挖洞不满足唯一性 填上*/
							Log.e("挖洞中.....","i = "+i);
							j--;
							array[i][tempY].flag = false;	
						}  
					}
				}
				else{
					for(int j = 0 ;j < 6 ; j ++){
						int tempY = getRandom8();
						if(tempY == 0|| array[i][tempY].flag){j--;continue;}
						array[i][tempY].flag = true;
						if (!checkSole(i, tempY)) {
							/*挖洞不满足唯一性 填上*/
							Log.e("挖洞中.....","i = "+i);
							j--;
							array[i][tempY].flag = false;	
						}  
					}
				}
			}
			/*剩余4个随机分配*/
			for(int i = 0 ;i < 4 ; i ++){
				int tempX = getRandom8();
				int tempY = getRandom8();
				if(array[tempX][tempY].flag){i--;continue;}
				else{
					array[tempX][tempY].flag = true;
					if (!checkSole(i, tempY)) {
						/*挖洞不满足唯一性 填上*/
						i--;
						array[tempX][tempY].flag = false;	
					}  
				}
			}
		}
		/* 蛇形 去除60个每行至少6个剩余6个随机去除*/
		else if (level == 4) {
			
            for(int i = 0 ;i < array.length ; i++){
            	for(int j = 0 ; j < 6 ;j ++){
            		int tempY = getRandom8();
            		if(array[i][tempY].flag){j--;continue;}
            		array[i][tempY].flag = true;
					if (!checkSole(i, tempY)) {
						/*挖洞不满足唯一性 填上*/
						j--;
						array[i][tempY].flag = false;	
					}  
            		
            	}
            	
            }
            for(int j  = 0 ;j < 6 ; j++){
        		int tempY = getRandom8();
        		int tempX = getRandom8();
        		if(array[tempX][tempY].flag){j--;continue;}
        		array[tempX][tempY].flag = true;
				if (!checkSole(tempX, tempY)) {
					/*挖洞不满足唯一性 填上*/
					j--;
					array[tempX][tempY].flag = false;	
				}  
        	}
		}
		/* 从左到右从上到下。 */
		else if (level == 5) {

            for(int i = 0 ;i < array.length ; i++){
            	for(int j = 0 ; j < 6 ;j ++){
            		int tempY = getRandom8();
            		if(array[i][tempY].flag){j--;continue;}
            		array[i][tempY].flag = true;
					if (!checkSole(i, tempY)) {
						/*挖洞不满足唯一性 填上*/
						j--;
						array[i][tempY].flag = false;	
					}  
            		
            	}
            	
            }
            for(int i = 0 ;i < 8 ; i++){
        		int tempY = getRandom8();
        		int tempX = getRandom8();
        		if(array[tempX][tempY].flag){i--;continue;}
        		array[tempX][tempY].flag = true;
				if (!checkSole(i, tempY)) {
					/*挖洞不满足唯一性 填上*/
					i--;
					array[tempX][tempY].flag = false;	
				}  
        	}
		}else {
			throw new RuntimeException("难度有问题!");
		}
	}
	/**
	 *验证唯一性
	 *在当前生成的数组终盘中按照难度挖去一些数字 这时需要验证唯一性
	 *  1.在挖去当前的数字中调用数独求解器，如果能求出与之前终盘中的数字不一样那么唯一性不满足
	 *  2.在代码中的体现 a.每个节点中flag==true 表示被挖去 
	 *              b.然后验证唯一性中填入的数字临时放入uerNum中和其他没有挖去的数字进行比较
	 *              c.如果验证完毕后userNum放入的数字满足当前数独的规则。
	 *                但是却和systemNum中的数字不同那么当前挖去的空格不满足唯一性需要填上
	 *              d.每个空格需要除sysNum中之外的1-9的数字都进行尝试
	 *  x是需要验证位置是横坐标 y是需要验证位置的纵坐标 num是验证除num之外的所有数
	 * */
	private boolean checkSole(int x, int y) {
		arrayPosition.clear();
		return answerShuDu2(x,y);
	}
	/**
	 * 验证数独中的唯一性所编写的数独求解器(因为自己设计的数据结构不太好导致重写)
	 *   1.当前数字可以满足规则后需要填写入userNum中
	 *   2.不满足的话需要回溯，如果一直不满足。则会一直回溯导致数组越界这是就可以判断此洞是不可以挖去的
	 *   x 传入待挖是数字的横坐标 
	 *   y 纵坐标
	 *   num 是待挖的数字(systemNum)
	 *   返回true表示这个值可以挖去
	 *   返回false表示有其他解不能挖去
	 *   
	 * */
	private boolean answerShuDu2(int x , int y ) {
		try{
			for (int i = 0; i < array.length; ++i) {
				for (int j = 0; j < array[i].length; ++j) {
					/* 当前节点是被抹去了的 */
					if (array[i][j].flag) {
						/* 获取一个1-9的随机数字 但是num这个值获取不到*/
						int tempNum ;
						/*在测试节点不能测试系统生成的数字*/
						if(i == x && j == y)tempNum = getRandomNum(array[i][j].systemNum);
						else tempNum = getRandom();
						/* 如果这个数字不重复那么就可以往“栈”中添加数据成功 成功后变判断这个数在这个位置是否满足规则 */
						if (setArrayInfo2(i, j, tempNum) && isContradict2(i, j, tempNum)) {
							/* 将位置上的数字记录下来 */
							array[i][j].userNum = tempNum;
						}
						
						else{
							i = arrayPosition.get(arrayPosition.size() - 1).x ;
							j = arrayPosition.get(arrayPosition.size() - 1).y ;
							--j;
						}
	
					}
				}
		}
		}catch(ArrayIndexOutOfBoundsException e){
			/*表示没有解那么就满足唯一性 这个标志就是回溯到了界外*/
			clearUserNum();
			return true;
		}
		clearUserNum();
		return false;
	}
	/*将所有的userNum中的数字清零*/
	private void clearUserNum(){
		for(int i = 0 ; i < array.length; ++ i){
			for(int j = 0 ; j < array[i].length ; ++ j){
				array[i][j].userNum = 0 ;
			}
		}
		
	}
	/**
	 * 在验证唯一性的时候需要调用这个函数来验证是否满足数独规则
	 *    因为在验证唯一性的时候数字是填入userNum中不能直接使用isContradict（）函数需要重写编写
	 *    notice:需要验证x，y中的数字num需要先验证后填入位置，不能填入后再验证
	 * */
	private boolean isContradict2(int x , int y , int num){
		/* 同行比较 */
		for (int j = 0; j < array.length; j++) {
			if(j == y){continue;}
			if ((array[x][j].systemNum == num && !array[x][j].flag) || array[x][j].userNum == num) {
				return false;
			}
		}
		/* 同列比较 */
		for (int i = 0; i < array.length; i++) {
			if(i == x ){continue;}
			if ((array[i][y].systemNum == num && !array[i][y].flag)|| array[i][y].userNum == num) {
				return false;
			}
		}
		/* 九宫格比较 先知道在哪个宫格中,不用知道在宫格中的哪个位置。直接在那个宫格中从头开始遍历进行比较 */
		int sqrtLength = (int) Math.sqrt(array.length);
		/* 知道在哪个九宫格中 */
		int whereX = x / sqrtLength;
		int whereY = y / sqrtLength;
		/* 九宫格做成一个二维方式比较 直接定位到九宫格中 */
		for (int i = whereX * sqrtLength; i < whereX * sqrtLength + sqrtLength; i++) {
			for (int j = whereY * sqrtLength; j < whereY * sqrtLength
					+ sqrtLength; j++) {
				if (x == i && y == j)
					continue;// 是同一个数了
				else {
					if ((array[i][j].systemNum == num && !array[i][j].flag)|| array[i][j].userNum == num)
						return false;
				}
			}
		}
		return true;
	}


	/**
	 * 数独求解器 根据已知部分数独格子的数独题进行求解 1.先随机抽取一些数字放入格子中如果满足要求继续下步骤
	 * 2.如果1-9都不满足那么回溯到上一步再重新取值（并且不会取同一个值）回溯有多种情况 3.如此往复知道穷举出合适的值便可以
	 * 
	 * */
	private void answerShuDu() {

		for (int i = 0; i < array.length; ++i) {
			for (int j = 0; j < array[i].length; ++j) {
				/* 当前节点是被抹去了的 */
				if (array[i][j].flag) {
					/* 获取一个1-9的随机数字 */
					int tempNum = getRandom();
					/* 如果这个数字不重复那么就可以往“栈”中添加数据成功 成功后变判断这个数在这个位置是否满足规则 */
					if (setArrayInfo(i, j, tempNum) && isContradict(i, j, tempNum)) {
						/* 将位置上的数字记录下来 */
						array[i][j].systemNum = tempNum;
						/*将当前数字设置为正常数*/
						array[i][j].flag = false;
					}
					else{
						i = arrayPosition.get(arrayPosition.size() - 1).x ;
						j = arrayPosition.get(arrayPosition.size() - 1).y ;
						j--;
					}

				}
			}
		}
	}

	
	
	/*为了验证唯一性编写代码 抛出了异常表示已经回溯到底了 便是没有解 所以可以挖洞*/
	private boolean setArrayInfo2(int x, int y , int num) throws ArrayIndexOutOfBoundsException{
		for(int i = 0 ; i < arrayPosition.size() ; i++){
		     if(arrayPosition.get(i).x == x && arrayPosition.get(i).y ==  y){
		    	 /*如果有相同位置的数据那么直接往其中添加数据 添加成功返回true*/
		    	 if(arrayPosition.get(i).setEightNum(num) == 1){
		    		 return true;
		         }
		    	 else if (arrayPosition.get(i).setEightNum(num) == -1) {
		    		 /*这个节点添加的数字已经超过8个了说明这个节点已经尝试完毕需要回溯了*/
		    		int tempX = arrayPosition.get(arrayPosition.size()-1).x;
		    		int tempY = arrayPosition.get(arrayPosition.size()-1).y;
		    		array[tempX][tempY].flag = true;
					arrayPosition.remove(arrayPosition.size() - 1);
					tempX = arrayPosition.get(arrayPosition.size()-1).x;
		    		tempY = arrayPosition.get(arrayPosition.size()-1).y;
		    		array[tempX][tempY].flag = true;
					return false;
				 }
		    	 return false;
		     }
		}
		/*如果没有这个位置的信息那么新建一个*/
		recordPosition tempRecord = new recordPosition();
		tempRecord.x = x ;
		tempRecord.y = y ;
		if(tempRecord.setNum(num) == 1){
			arrayPosition.add(tempRecord);
			return true;
		}
		return false;
	}
	/* 往arrayPosition中添加数据但是要保证添加的数据不重复 位置不重复。在那个位置中的数据不重复添加成功true不成功false */
	private boolean setArrayInfo(int x, int y , int num){
		for(int i = 0 ; i < arrayPosition.size() ; i++){
		     if(arrayPosition.get(i).x == x && arrayPosition.get(i).y ==  y){
		    	 /*如果有相同位置的数据那么直接往其中添加数据 添加成功返回true*/
		    	 if(arrayPosition.get(i).setNum(num) == 1){
		    		 return true;
		         }
		    	 else if (arrayPosition.get(i).setNum(num) == -1) {
		    		 /*这个节点添加的数字已经超过9个了说明这个节点已经尝试完毕需要回溯了*/
		    		int tempX = arrayPosition.get(arrayPosition.size()-1).x;
		    		int tempY = arrayPosition.get(arrayPosition.size()-1).y;
		    		array[tempX][tempY].flag = true;
					arrayPosition.remove(arrayPosition.size() - 1);
					tempX = arrayPosition.get(arrayPosition.size()-1).x;
		    		tempY = arrayPosition.get(arrayPosition.size()-1).y;
		    		array[tempX][tempY].flag = true;
					return false;
				 }
		    	 return false;
		     }
		}
		/*如果没有这个位置的信息那么新建一个*/
		recordPosition tempRecord = new recordPosition();
		tempRecord.x = x ;
		tempRecord.y = y ;
		if(tempRecord.setNum(num) == 1){
			arrayPosition.add(tempRecord);
			return true;
		}
		return false;
	}

	/**
	 * 用来记录每个位置所尝试过的数。如果尝试的数字已经有9个了然后还往其中添加数字那么就可以回溯了 x:用来记录位置x坐标 y:用来记录位置的y坐标
	 * resultNum：用来记录位置上可以填写的数字
	 * 
	 * */
	private class recordPosition {
		int x = -1, y = -1;
		ArrayList<Integer> recordNum = new ArrayList<Integer>();

		/* 判断数组是否满了 9 */
		public boolean isFull() {
			if (recordNum.size() >= 9)
				return true;
			return false;
		}

		/* 往当前节点中添加数字 返回0添加失败，返回1添加成功，返回-1需要删除最后一个节点 */
		public int setNum(int num) {
			if (recordNum.size() >= 9) {
				/* 在此处需要删除当前节点 */
				return -1;
			}
			for (int i = 0; i < recordNum.size(); i++) {
				if (recordNum.get(i) == num) {
					return 0;
				}
			}
			recordNum.add(num);
			return 1;
		}
		/* 往当前节点中添加数字 返回0添加失败，返回1添加成功，返回-1需要删除最后一个节点只能最多添加8个不重复的数据 */
		public int setEightNum(int num) {
			if (recordNum.size() >= 8){
				/* 在此处需要删除当前节点 */
				return -1;
			}
			for (int i = 0; i < recordNum.size(); i++) {
				if (recordNum.get(i) == num) {
					return 0;
				}
			}
			recordNum.add(num);
			return 1;
		}
	}



	/* 判断这个数是否复合这个位置 数据从系统中来的 用来初始化数独数组用的 填入的数和系统的数进行比较 false不合适。true合适 */
	private boolean isContradict(int x, int y, int num) {
		/* 同行比较 */
		for (int j = 0; j < array.length; j++) {
			if (array[x][j].systemNum == num && !array[x][j].flag) {
				return false;
			}
		}
		/* 同列比较 */
		for (int i = 0; i < array.length; i++) {
			if (array[i][y].systemNum == num && !array[i][y].flag) {
				return false;
			}
		}
		/* 九宫格比较 先知道在哪个宫格中,不用知道在宫格中的哪个位置。直接在那个宫格中从头开始遍历进行比较 */
		int sqrtLength = (int) Math.sqrt(array.length);
		/* 知道在哪个九宫格中 */
		int whereX = x / sqrtLength;
		int whereY = y / sqrtLength;
		/* 九宫格做成一个二维方式比较 直接定位到九宫格中 */
		for (int i = whereX * sqrtLength; i < whereX * sqrtLength + sqrtLength; i++) {
			for (int j = whereY * sqrtLength; j < whereY * sqrtLength
					+ sqrtLength; j++) {
				if (x == i && y == j)
					continue;// 是同一个数了
				else {
					if (array[i][j].systemNum == num && !array[i][j].flag)
						return false;
				}
			}
		}
		return true;
	}

	/* 获取1-9的随机数 */
	private int getRandom() {
		int temp = 0;
		while (temp == 0) {
			temp = (int) (Math.random() * 100 % 10);
		}
		return temp;
	}
	/*随机获取除num之外的数据*/
	private int getRandomNum(int num) {
		int temp = 0;
		while (temp == 0 || temp == num) {
			temp = (int) (Math.random() * 100 % 10);
		}
		return temp;
	}

	/* 获取0-8的随机数 */
	private int getRandom8() {
		return (int) (Math.random() * 100 % 9);
	}
	/**
	 * 
	 * @author Administrator back 供用户不确定的数据放进去 (候选数字) userNum是用户确定的数据
	 *         systemNum是系统生成的数据 flag 是否是抹去的数字 true是抹去的。false是没有抹去的
	 * 
	 */
	public class NumNode {
		private ArrayList<Integer> back = new ArrayList<Integer>();
		public int systemNum = 0;
		public int userNum = 0;
		public boolean flag = false;
		
		/* 添加候选数，如果候选数的多于了9个那么就不能插入数字了 */
		boolean setBackNum(int num) {
			for (int i = 0; i < back.size(); i++) {
				if (num == back.get(i))
					return false;
			}
			if ((int) back.size() > 9) {
				return false;
			}
			back.add(num);
			return true;
		}
		/*去除队列中的某个数字*/
		void clearBackNum(Integer num){
			back.remove(num);
		}
		
		public ArrayList getBack() {
			return back;
		}
	}
}






