package com.xhc.numbergame.entity;

import java.util.ArrayList;

import com.xhc.numbergame.entity.NumAloneArray.NumNode;
import com.xhc.numbergame.tools.GameState;
import com.xhc.numbergame.tools.MyFile;

import android.util.Log;


/**
 * �����������ĺ�̨��ά���� һ����ͬ����ͬ���� ���� 4X4 9x9 25X25
 * �Ƚ������źá�Ȼ���������ȥ�������ݹ��û���д(�ṩ��ÿ���ڵ㴦д�������ݣ����Ǹ����ݵ�ʱ��ȷ�����ĸ���) �����е�ÿ���ڵ���NumNode(�ṩ
 * ԭ����ϵͳ���ɣ�,ȷ����,�����) ���Ƚ�usernum��sysnum�����֪���Ƿ�ɹ�
 * 
 * @author Administrator
 */
public class NumAloneArray extends ArrayGame {

	/* �������� һ��9x9 */
	int x;
	NumNode array[][];
	int level;
	/* ������¼λ��Īλ���ϲ��Թ������֣��������ȷ����ͽ����ַ������н��м�¼ */
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
	
	/*�ж��Ƿ���Ӯ�˱���*/
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
	
	/*��ʼ��Ϸ*/
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
 
	/*���Ͷ�Ÿ��Ϲ���ľŸ�����*/
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
    	/*����û����ӵ����� ��ΪĨȥ*/
    	for(int i = 0 ;i < 9 ; i ++){
    		for(int j = 0 ; j < 9 ; j ++){
    			if(array[i][j].systemNum == 0)
    			array[i][j].flag = true;
    		}
    	}
    } 
    
   /**
    * ��ȡ�����ݿ��е�����
    *  system,usernum,flag,1@3@#system,usernum,flag,1@3@#
	* ϵͳ������system,�û�������־����ѡ��#�¸��ڵ�*/
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
    
    
    /*����һ����������*/
    private void setNumber(){
    	randomSetNum();
    	answerShuDu();
    }
	public NumNode[][] getArray() {
		return array;
	}

	/**
	 * 
	 * 1���Ѷ�: ���ȥ��40����45���ո�
	 * 2���Ѷȣ����ȥ��46��50���ո�
	 * 3���Ѷ�:ż���в�ȥi��8�����в�ȥ��(���) i��0����� ȥ��51-55���ո�
	 * 4���Ѷȣ�����ȥ��   55-60���ո�
	 * 5���Ѷ�:�������Ҵ������� 60-62���ո�
	 * 
	 * */
	private void removeSomeNumber() {
		if (level == 1) {
			for (int i = 0; i < 47; i++) {
				int x = getRandom8();
				int y = getRandom8();
				if(array[x][y].flag){
                 /*�����ڵ�ͬһ����*/
					i--;
					continue;
				}
				/*�����ڶ�*/
				array[x][y].flag = true;
				if (!checkSole(x, y)) {
					/*�ڶ�������Ψһ�� ����*/
					i--;
					array[x][y].flag = false;	
				}  
			}
		} else if (level == 2) {
			for (int i = 0; i < 55; i++) {
				int x = getRandom8();
				int y = getRandom8();
				if(array[x][y].flag){
                 /*�����ڵ�ͬһ����*/
					i--;
					continue;
				}
				/*�����ڶ�*/
				array[x][y].flag = true;
				if (!checkSole(x, y)) {
					/*�ڶ�������Ψһ�� ����*/
					Log.e("�ڶ���.....","i = "+i);
					i--;
					array[x][y].flag = false;	
				}  
			}
		}
		/* �����ʽ */
		else if (level == 3) {
			for(int i = 0 ;i < array.length ; i ++){
				if( i % 2 == 0){
					/*ż���� i 8�����������*/
					for(int j = 0 ;j < 6 ; j ++){
						int tempY = getRandom8();
						if(tempY == 8 || array[i][tempY].flag){j--;continue;}
						array[i][tempY].flag = true;
						if (!checkSole(i, tempY)) {
							/*�ڶ�������Ψһ�� ����*/
							Log.e("�ڶ���.....","i = "+i);
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
							/*�ڶ�������Ψһ�� ����*/
							Log.e("�ڶ���.....","i = "+i);
							j--;
							array[i][tempY].flag = false;	
						}  
					}
				}
			}
			/*ʣ��4���������*/
			for(int i = 0 ;i < 4 ; i ++){
				int tempX = getRandom8();
				int tempY = getRandom8();
				if(array[tempX][tempY].flag){i--;continue;}
				else{
					array[tempX][tempY].flag = true;
					if (!checkSole(i, tempY)) {
						/*�ڶ�������Ψһ�� ����*/
						i--;
						array[tempX][tempY].flag = false;	
					}  
				}
			}
		}
		/* ���� ȥ��60��ÿ������6��ʣ��6�����ȥ��*/
		else if (level == 4) {
			
            for(int i = 0 ;i < array.length ; i++){
            	for(int j = 0 ; j < 6 ;j ++){
            		int tempY = getRandom8();
            		if(array[i][tempY].flag){j--;continue;}
            		array[i][tempY].flag = true;
					if (!checkSole(i, tempY)) {
						/*�ڶ�������Ψһ�� ����*/
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
					/*�ڶ�������Ψһ�� ����*/
					j--;
					array[tempX][tempY].flag = false;	
				}  
        	}
		}
		/* �����Ҵ��ϵ��¡� */
		else if (level == 5) {

            for(int i = 0 ;i < array.length ; i++){
            	for(int j = 0 ; j < 6 ;j ++){
            		int tempY = getRandom8();
            		if(array[i][tempY].flag){j--;continue;}
            		array[i][tempY].flag = true;
					if (!checkSole(i, tempY)) {
						/*�ڶ�������Ψһ�� ����*/
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
					/*�ڶ�������Ψһ�� ����*/
					i--;
					array[tempX][tempY].flag = false;	
				}  
        	}
		}else {
			throw new RuntimeException("�Ѷ�������!");
		}
	}
	/**
	 *��֤Ψһ��
	 *�ڵ�ǰ���ɵ����������а����Ѷ���ȥһЩ���� ��ʱ��Ҫ��֤Ψһ��
	 *  1.����ȥ��ǰ�������е������������������������֮ǰ�����е����ֲ�һ����ôΨһ�Բ�����
	 *  2.�ڴ����е����� a.ÿ���ڵ���flag==true ��ʾ����ȥ 
	 *              b.Ȼ����֤Ψһ���������������ʱ����uerNum�к�����û����ȥ�����ֽ��бȽ�
	 *              c.�����֤��Ϻ�userNum������������㵱ǰ�����Ĺ���
	 *                ����ȴ��systemNum�е����ֲ�ͬ��ô��ǰ��ȥ�Ŀո�����Ψһ����Ҫ����
	 *              d.ÿ���ո���Ҫ��sysNum��֮���1-9�����ֶ����г���
	 *  x����Ҫ��֤λ���Ǻ����� y����Ҫ��֤λ�õ������� num����֤��num֮���������
	 * */
	private boolean checkSole(int x, int y) {
		arrayPosition.clear();
		return answerShuDu2(x,y);
	}
	/**
	 * ��֤�����е�Ψһ������д�����������(��Ϊ�Լ���Ƶ����ݽṹ��̫�õ�����д)
	 *   1.��ǰ���ֿ�������������Ҫ��д��userNum��
	 *   2.������Ļ���Ҫ���ݣ����һֱ�����㡣���һֱ���ݵ�������Խ�����ǾͿ����жϴ˶��ǲ�������ȥ��
	 *   x ������������ֵĺ����� 
	 *   y ������
	 *   num �Ǵ��ڵ�����(systemNum)
	 *   ����true��ʾ���ֵ������ȥ
	 *   ����false��ʾ�������ⲻ����ȥ
	 *   
	 * */
	private boolean answerShuDu2(int x , int y ) {
		try{
			for (int i = 0; i < array.length; ++i) {
				for (int j = 0; j < array[i].length; ++j) {
					/* ��ǰ�ڵ��Ǳ�Ĩȥ�˵� */
					if (array[i][j].flag) {
						/* ��ȡһ��1-9��������� ����num���ֵ��ȡ����*/
						int tempNum ;
						/*�ڲ��Խڵ㲻�ܲ���ϵͳ���ɵ�����*/
						if(i == x && j == y)tempNum = getRandomNum(array[i][j].systemNum);
						else tempNum = getRandom();
						/* ���������ֲ��ظ���ô�Ϳ�������ջ����������ݳɹ� �ɹ�����ж�����������λ���Ƿ�������� */
						if (setArrayInfo2(i, j, tempNum) && isContradict2(i, j, tempNum)) {
							/* ��λ���ϵ����ּ�¼���� */
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
			/*��ʾû�н���ô������Ψһ�� �����־���ǻ��ݵ��˽���*/
			clearUserNum();
			return true;
		}
		clearUserNum();
		return false;
	}
	/*�����е�userNum�е���������*/
	private void clearUserNum(){
		for(int i = 0 ; i < array.length; ++ i){
			for(int j = 0 ; j < array[i].length ; ++ j){
				array[i][j].userNum = 0 ;
			}
		}
		
	}
	/**
	 * ����֤Ψһ�Ե�ʱ����Ҫ���������������֤�Ƿ�������������
	 *    ��Ϊ����֤Ψһ�Ե�ʱ������������userNum�в���ֱ��ʹ��isContradict����������Ҫ��д��д
	 *    notice:��Ҫ��֤x��y�е�����num��Ҫ����֤������λ�ã��������������֤
	 * */
	private boolean isContradict2(int x , int y , int num){
		/* ͬ�бȽ� */
		for (int j = 0; j < array.length; j++) {
			if(j == y){continue;}
			if ((array[x][j].systemNum == num && !array[x][j].flag) || array[x][j].userNum == num) {
				return false;
			}
		}
		/* ͬ�бȽ� */
		for (int i = 0; i < array.length; i++) {
			if(i == x ){continue;}
			if ((array[i][y].systemNum == num && !array[i][y].flag)|| array[i][y].userNum == num) {
				return false;
			}
		}
		/* �Ź���Ƚ� ��֪�����ĸ�������,����֪���ڹ����е��ĸ�λ�á�ֱ�����Ǹ������д�ͷ��ʼ�������бȽ� */
		int sqrtLength = (int) Math.sqrt(array.length);
		/* ֪�����ĸ��Ź����� */
		int whereX = x / sqrtLength;
		int whereY = y / sqrtLength;
		/* �Ź�������һ����ά��ʽ�Ƚ� ֱ�Ӷ�λ���Ź����� */
		for (int i = whereX * sqrtLength; i < whereX * sqrtLength + sqrtLength; i++) {
			for (int j = whereY * sqrtLength; j < whereY * sqrtLength
					+ sqrtLength; j++) {
				if (x == i && y == j)
					continue;// ��ͬһ������
				else {
					if ((array[i][j].systemNum == num && !array[i][j].flag)|| array[i][j].userNum == num)
						return false;
				}
			}
		}
		return true;
	}


	/**
	 * ��������� ������֪�����������ӵ������������� 1.�������ȡһЩ���ַ���������������Ҫ������²���
	 * 2.���1-9����������ô���ݵ���һ��������ȡֵ�����Ҳ���ȡͬһ��ֵ�������ж������ 3.�������֪����ٳ����ʵ�ֵ�����
	 * 
	 * */
	private void answerShuDu() {

		for (int i = 0; i < array.length; ++i) {
			for (int j = 0; j < array[i].length; ++j) {
				/* ��ǰ�ڵ��Ǳ�Ĩȥ�˵� */
				if (array[i][j].flag) {
					/* ��ȡһ��1-9��������� */
					int tempNum = getRandom();
					/* ���������ֲ��ظ���ô�Ϳ�������ջ����������ݳɹ� �ɹ�����ж�����������λ���Ƿ�������� */
					if (setArrayInfo(i, j, tempNum) && isContradict(i, j, tempNum)) {
						/* ��λ���ϵ����ּ�¼���� */
						array[i][j].systemNum = tempNum;
						/*����ǰ��������Ϊ������*/
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

	
	
	/*Ϊ����֤Ψһ�Ա�д���� �׳����쳣��ʾ�Ѿ����ݵ����� ����û�н� ���Կ����ڶ�*/
	private boolean setArrayInfo2(int x, int y , int num) throws ArrayIndexOutOfBoundsException{
		for(int i = 0 ; i < arrayPosition.size() ; i++){
		     if(arrayPosition.get(i).x == x && arrayPosition.get(i).y ==  y){
		    	 /*�������ͬλ�õ�������ôֱ��������������� ��ӳɹ�����true*/
		    	 if(arrayPosition.get(i).setEightNum(num) == 1){
		    		 return true;
		         }
		    	 else if (arrayPosition.get(i).setEightNum(num) == -1) {
		    		 /*����ڵ���ӵ������Ѿ�����8����˵������ڵ��Ѿ����������Ҫ������*/
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
		/*���û�����λ�õ���Ϣ��ô�½�һ��*/
		recordPosition tempRecord = new recordPosition();
		tempRecord.x = x ;
		tempRecord.y = y ;
		if(tempRecord.setNum(num) == 1){
			arrayPosition.add(tempRecord);
			return true;
		}
		return false;
	}
	/* ��arrayPosition��������ݵ���Ҫ��֤��ӵ����ݲ��ظ� λ�ò��ظ������Ǹ�λ���е����ݲ��ظ���ӳɹ�true���ɹ�false */
	private boolean setArrayInfo(int x, int y , int num){
		for(int i = 0 ; i < arrayPosition.size() ; i++){
		     if(arrayPosition.get(i).x == x && arrayPosition.get(i).y ==  y){
		    	 /*�������ͬλ�õ�������ôֱ��������������� ��ӳɹ�����true*/
		    	 if(arrayPosition.get(i).setNum(num) == 1){
		    		 return true;
		         }
		    	 else if (arrayPosition.get(i).setNum(num) == -1) {
		    		 /*����ڵ���ӵ������Ѿ�����9����˵������ڵ��Ѿ����������Ҫ������*/
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
		/*���û�����λ�õ���Ϣ��ô�½�һ��*/
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
	 * ������¼ÿ��λ�������Թ�������������Ե������Ѿ���9����Ȼ�����������������ô�Ϳ��Ի����� x:������¼λ��x���� y:������¼λ�õ�y����
	 * resultNum��������¼λ���Ͽ�����д������
	 * 
	 * */
	private class recordPosition {
		int x = -1, y = -1;
		ArrayList<Integer> recordNum = new ArrayList<Integer>();

		/* �ж������Ƿ����� 9 */
		public boolean isFull() {
			if (recordNum.size() >= 9)
				return true;
			return false;
		}

		/* ����ǰ�ڵ���������� ����0���ʧ�ܣ�����1��ӳɹ�������-1��Ҫɾ�����һ���ڵ� */
		public int setNum(int num) {
			if (recordNum.size() >= 9) {
				/* �ڴ˴���Ҫɾ����ǰ�ڵ� */
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
		/* ����ǰ�ڵ���������� ����0���ʧ�ܣ�����1��ӳɹ�������-1��Ҫɾ�����һ���ڵ�ֻ��������8�����ظ������� */
		public int setEightNum(int num) {
			if (recordNum.size() >= 8){
				/* �ڴ˴���Ҫɾ����ǰ�ڵ� */
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



	/* �ж�������Ƿ񸴺����λ�� ���ݴ�ϵͳ������ ������ʼ�����������õ� ���������ϵͳ�������бȽ� false�����ʡ�true���� */
	private boolean isContradict(int x, int y, int num) {
		/* ͬ�бȽ� */
		for (int j = 0; j < array.length; j++) {
			if (array[x][j].systemNum == num && !array[x][j].flag) {
				return false;
			}
		}
		/* ͬ�бȽ� */
		for (int i = 0; i < array.length; i++) {
			if (array[i][y].systemNum == num && !array[i][y].flag) {
				return false;
			}
		}
		/* �Ź���Ƚ� ��֪�����ĸ�������,����֪���ڹ����е��ĸ�λ�á�ֱ�����Ǹ������д�ͷ��ʼ�������бȽ� */
		int sqrtLength = (int) Math.sqrt(array.length);
		/* ֪�����ĸ��Ź����� */
		int whereX = x / sqrtLength;
		int whereY = y / sqrtLength;
		/* �Ź�������һ����ά��ʽ�Ƚ� ֱ�Ӷ�λ���Ź����� */
		for (int i = whereX * sqrtLength; i < whereX * sqrtLength + sqrtLength; i++) {
			for (int j = whereY * sqrtLength; j < whereY * sqrtLength
					+ sqrtLength; j++) {
				if (x == i && y == j)
					continue;// ��ͬһ������
				else {
					if (array[i][j].systemNum == num && !array[i][j].flag)
						return false;
				}
			}
		}
		return true;
	}

	/* ��ȡ1-9������� */
	private int getRandom() {
		int temp = 0;
		while (temp == 0) {
			temp = (int) (Math.random() * 100 % 10);
		}
		return temp;
	}
	/*�����ȡ��num֮�������*/
	private int getRandomNum(int num) {
		int temp = 0;
		while (temp == 0 || temp == num) {
			temp = (int) (Math.random() * 100 % 10);
		}
		return temp;
	}

	/* ��ȡ0-8������� */
	private int getRandom8() {
		return (int) (Math.random() * 100 % 9);
	}
	/**
	 * 
	 * @author Administrator back ���û���ȷ�������ݷŽ�ȥ (��ѡ����) userNum���û�ȷ��������
	 *         systemNum��ϵͳ���ɵ����� flag �Ƿ���Ĩȥ������ true��Ĩȥ�ġ�false��û��Ĩȥ��
	 * 
	 */
	public class NumNode {
		private ArrayList<Integer> back = new ArrayList<Integer>();
		public int systemNum = 0;
		public int userNum = 0;
		public boolean flag = false;
		
		/* ��Ӻ�ѡ���������ѡ���Ķ�����9����ô�Ͳ��ܲ��������� */
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
		/*ȥ�������е�ĳ������*/
		void clearBackNum(Integer num){
			back.remove(num);
		}
		
		public ArrayList getBack() {
			return back;
		}
	}
}






