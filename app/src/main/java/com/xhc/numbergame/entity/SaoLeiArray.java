package com.xhc.numbergame.entity;

import com.xhc.numbergame.tools.ClickFlag;

/**
 * 此类接收传入的x y表示几x几的矩阵 然后开始随机布雷然后初始化数组 然后用另个数组来表示用户的操作（插旗）
 * 
 * @author Administrator
 *
 */
public class SaoLeiArray extends ArrayGame {
	int testcount = 0;
	/**
	 * 数组的下标
	 */
	private int x, y;
	/**
	 * 本类操作的数组
	 */
	private Node[][] array;

	/**
	 * 表示需要放多少个雷
	 */
	private int bombCount;
	/**
	 * 用户标志了多少雷
	 * */
//	private int uerBomb;
	// /**
	// * 用户标记的雷数
	// * 可以用来判断游戏是否完成
	// */
	// // private int signBomb = 0 ;
	/* 点击到炸弹的标志表示游戏结束 */
	private boolean isLose = false;
    
	/**
	 * 
	 * @param x
	 * @param y
	 * @param count
	 */
	public SaoLeiArray(int x, int y, int count) {
		this.x = x;
		this.y = y;
		this.bombCount = count;
		array = new Node[x][y];
		for (int i = 0; i < x; i++) {
			for (int j = 0; j < y; j++) {
				array[i][j] = new Node();
			}
		}
	}
	/*开始游戏*/
	public void begin(){
		/* 随机放入雷 */
		setBomb();
		/* 根据雷数目确定数字 */
		setNumber();
		 
	}
	/*从数据库中读取的数据转换成数组*/
	public void setArray(String content){
		String[] node= content.split("#");
		for(int i = 0 ;i < x ; ++i){
			for(int j = 0 ; j < y ; ++j){
				
				String[] temp = node[i*y+j].split(",");
				array[i][j].clickFlag = Integer.parseInt(temp[0]);
				array[i][j].num = Integer.parseInt(temp[1]);
			}
		}
	}
    public int getX(){
    	return x;
    }
    public int getY(){
    	return y ;
    }
	/* 根据雷将周边九宫格的数字+1 */
	private void setNumber() {
		for (int i = 0; i < x; i++) {
			for (int j = 0; j < y; j++) {
				if (array[i][j].num == -1) {
					if ((i - 1) >= 0 && (j - 1) >= 0
							&& array[i - 1][j - 1].num >= 0) {
						array[i - 1][j - 1].num++;
					}
					if ((i - 1) >= 0 && array[i - 1][j].num >= 0) {
						array[i - 1][j].num++;
					}
					if ((i - 1) >= 0 && (j + 1) < array[i - 1].length
							&& array[i - 1][j + 1].num >= 0) {
						array[i - 1][j + 1].num++;
					}
					if ((j - 1) >= 0 && array[i][j - 1].num >= 0) {
						array[i][j - 1].num++;
					}
					if ((j + 1) < array[i].length && array[i][j + 1].num >= 0) {
						array[i][j + 1].num++;
					}
					if ((i + 1) < array.length && (j - 1) >= 0
							&& array[i + 1][j - 1].num >= 0) {
						array[i + 1][j - 1].num++;
					}
					if ((i + 1) < array.length && array[i + 1][j].num >= 0) {
						array[i + 1][j].num++;
					}
					if ((i + 1) < array.length && (j + 1) < array[i + 1].length
							&& array[i + 1][j + 1].num >= 0) {
						array[i + 1][j + 1].num++;
					}
				}
			}
		}
	}

	/* 随机放雷 */
	private void setBomb() {
//		uerBomb = 0 ;
		int positionX, positionY;
		for (int i = 0; i < bombCount;) {
			positionX = (int) (Math.random() * 100 % x);
			positionY = (int) (Math.random() * 100 % y);
			if (array[positionX][positionY].num == 0) {
				/* 是空白的可以填雷 */
				array[positionX][positionY].num = -1;
				i++;
			}
		}
	}

	/**
	 * 判断游戏是否成功
	 * 
	 * @return
	 */
	public boolean isVictory() {
		// if(signBomb != bombCount){
		// return false;
		// }
//		if(uerBomb != bombCount){
//			return false;
//		}
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[i].length; j++) {
				/*如果标记的不是雷，则不是胜利，是雷但是没标记也不是胜利*/
				if ((array[i][j].num == -1 && array[i][j].clickFlag != 3)
						|| (array[i][j].clickFlag == 3 && array[i][j].num != -1))
					return false;
			}
		}

		return true;
	}

	/* 判断是否点击到雷 */
	public boolean isLose() {
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[i].length; j++) {
				/*如果标记的不是雷，则不是胜利，是雷但是没标记也不是胜利*/
				if ((array[i][j].num == -1 && array[i][j].clickFlag == 1))
					return true;
			}
		}
		return isLose;
	}

	/* 判断用户的点击事件 */
	public void clickArray(int x, int y, int flag) {
		if (flag == ClickFlag.click) {
			/* 点击到了炸弹 */
			if (array[x][y].num == -1) {
				for (int i = 0; i < array.length; i++) {
					for (int j = 0; j < array[i].length; j++) {
						if (array[i][j].num == -1) {
							array[i][j].clickFlag = 1;
						}
					}
				}
				isLose = true;
				return;
			}
			/* 点击到了空白的地方则将所有空白的地方递归出来直到遇到数字为止并且还没有翻开 */
			if (array[x][y].num == 0 && array[x][y].clickFlag != 1) {
				findNumber(x, y);
			}
			/* 点击到了数字并且还没有翻开 */
			if (array[x][y].num > 0 && array[x][y].clickFlag != 1) {
				array[x][y].clickFlag = 1;
			}
		} else if (flag == ClickFlag.longClick && array[x][y].clickFlag != 1) {
//			uerBomb++;
			array[x][y].clickFlag = 3;
		} else if (flag == ClickFlag.doubleClick && array[x][y].clickFlag != 1) {
			array[x][y].clickFlag = 2;
		}
	}

	/* 当用户点击到空白的地方则将所有空白的地方递归出来 ？号的地方可以递归出来 旗子的地方不能递归出来 */
	private void findNumber(int x, int y) {
		testcount++;

		/* 到了边界 */
		if (x < 0 || x > array.length - 1 || y < 0 || y > array[x].length - 1) {

			return;
		}
		if (array[x][y].clickFlag == 1 || array[x][y].clickFlag == 3) {
			return;
		}
		/* 遇到数字 不可能遇到炸弹 并且没有被旗子标记 */
		if (array[x][y].num != 0) {
			array[x][y].clickFlag = 1;
			return;
		}
		/* 遇到0 并且没有被旗子标记 */
		if (array[x][y].num == 0) {
			array[x][y].clickFlag = 1;
		}

		findNumber(x - 1, y);
		findNumber(x - 1, y - 1);
		findNumber(x - 1, y + 1);
		
		findNumber(x, y - 1);
		findNumber(x, y + 1);
		findNumber(x + 1, y);
		findNumber(x + 1, y-1);
		findNumber(x + 1, y+1);

		return;
	}
    /*重新开始*/
	public void resteArray() {
		for (int i = 0; i < x; i++) {
			for (int j = 0; j < y; j++) {
				array[i][j].num = 0;
				array[i][j].clickFlag = 0;
			}
		}
		setBomb();
		setNumber();
		isLose = false;

	}

	public Node[][] getSaoLeiNodeArray() {
		return array;
	}

	public class Node {
		/**
		 * 1 是单击 单击是直接翻牌 也就是牌的正面 2是双击 双击是？ 不确定下面是雷还是数字 3是长按 长按表示确定下面是雷(1s)旗子
		 * 0表示用户没有进行任何操作则显示的是牌后面
		 */
		public int clickFlag = 0;
		/**
		 * -1是雷 0是空白 其他数字就是九宫格中有几个雷
		 */
		public int num = 0;
	}
}





