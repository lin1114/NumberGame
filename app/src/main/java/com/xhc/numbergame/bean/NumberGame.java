package com.xhc.numbergame.bean;
/**
 * ������Ϸ�������浽���ݿ���
 * @author TongMin
 *
 */
public class NumberGame {

	private int id ;
	
	//��Ϸ����
	private String gameName ;
	//����Ϸ���ݵĵط��������
	private String GameContent;
	//��Ϸ�Ѷ�
	private String Level;
	//��Ϸ����ʱ��
	private String time;
	 
	public String getGameName() {
		return gameName;
	}
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	public String getGameContent() {
		return GameContent;
	}
	public void setGameContent(String gameContent) {
		GameContent = gameContent;
	}
	public String getLevel() {
		return Level;
	}
	public void setLevel(String level) {
		Level = level;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "NumberGame [id=" + id + ", gameName=" + gameName + ", GameContent=" + GameContent + ", Level=" + Level
				+ ", time=" + time + "]";
	}
	
	
	
}
