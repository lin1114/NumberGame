package com.xhc.numbergame.entity;

import com.xhc.numbergame.tools.ClickFlag;

/**
 * ������մ����x y��ʾ��x���ľ��� Ȼ��ʼ�������Ȼ���ʼ������ Ȼ���������������ʾ�û��Ĳ��������죩
 * 
 * @author Administrator
 *
 */
public class SaoLeiArray extends ArrayGame {
	int testcount = 0;
	/**
	 * ������±�
	 */
	private int x, y;
	/**
	 * �������������
	 */
	private Node[][] array;

	/**
	 * ��ʾ��Ҫ�Ŷ��ٸ���
	 */
	private int bombCount;
	/**
	 * �û���־�˶�����
	 * */
//	private int uerBomb;
	// /**
	// * �û���ǵ�����
	// * ���������ж���Ϸ�Ƿ����
	// */
	// // private int signBomb = 0 ;
	/* �����ը���ı�־��ʾ��Ϸ���� */
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
	/*��ʼ��Ϸ*/
	public void begin(){
		/* ��������� */
		setBomb();
		/* ��������Ŀȷ������ */
		setNumber();
		 
	}
	/*�����ݿ��ж�ȡ������ת��������*/
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
	/* �����׽��ܱ߾Ź��������+1 */
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

	/* ������� */
	private void setBomb() {
//		uerBomb = 0 ;
		int positionX, positionY;
		for (int i = 0; i < bombCount;) {
			positionX = (int) (Math.random() * 100 % x);
			positionY = (int) (Math.random() * 100 % y);
			if (array[positionX][positionY].num == 0) {
				/* �ǿհ׵Ŀ������� */
				array[positionX][positionY].num = -1;
				i++;
			}
		}
	}

	/**
	 * �ж���Ϸ�Ƿ�ɹ�
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
				/*�����ǵĲ����ף�����ʤ�������׵���û���Ҳ����ʤ��*/
				if ((array[i][j].num == -1 && array[i][j].clickFlag != 3)
						|| (array[i][j].clickFlag == 3 && array[i][j].num != -1))
					return false;
			}
		}

		return true;
	}

	/* �ж��Ƿ������� */
	public boolean isLose() {
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[i].length; j++) {
				/*�����ǵĲ����ף�����ʤ�������׵���û���Ҳ����ʤ��*/
				if ((array[i][j].num == -1 && array[i][j].clickFlag == 1))
					return true;
			}
		}
		return isLose;
	}

	/* �ж��û��ĵ���¼� */
	public void clickArray(int x, int y, int flag) {
		if (flag == ClickFlag.click) {
			/* �������ը�� */
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
			/* ������˿հ׵ĵط������пհ׵ĵط��ݹ����ֱ����������Ϊֹ���һ�û�з��� */
			if (array[x][y].num == 0 && array[x][y].clickFlag != 1) {
				findNumber(x, y);
			}
			/* ����������ֲ��һ�û�з��� */
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

	/* ���û�������հ׵ĵط������пհ׵ĵط��ݹ���� ���ŵĵط����Եݹ���� ���ӵĵط����ܵݹ���� */
	private void findNumber(int x, int y) {
		testcount++;

		/* ���˱߽� */
		if (x < 0 || x > array.length - 1 || y < 0 || y > array[x].length - 1) {

			return;
		}
		if (array[x][y].clickFlag == 1 || array[x][y].clickFlag == 3) {
			return;
		}
		/* �������� ����������ը�� ����û�б����ӱ�� */
		if (array[x][y].num != 0) {
			array[x][y].clickFlag = 1;
			return;
		}
		/* ����0 ����û�б����ӱ�� */
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
    /*���¿�ʼ*/
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
		 * 1 �ǵ��� ������ֱ�ӷ��� Ҳ�����Ƶ����� 2��˫�� ˫���ǣ� ��ȷ���������׻������� 3�ǳ��� ������ʾȷ����������(1s)����
		 * 0��ʾ�û�û�н����κβ�������ʾ�����ƺ���
		 */
		public int clickFlag = 0;
		/**
		 * -1���� 0�ǿհ� �������־��ǾŹ������м�����
		 */
		public int num = 0;
	}
}





