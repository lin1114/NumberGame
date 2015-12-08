package com.xhc.numbergame.bean;
/**
 * 数字游戏用来保存到数据库中
 * @author TongMin
 *
 */
public class NumberGame {

	private int id ;
	
	//游戏名字
	private String gameName ;
	//存游戏内容的地方，数组等
	private String GameContent;
	//游戏难度
	private String Level;
	//游戏花的时间
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
