package com.zsl.swing.redis.desktop.model;

/**
 * 
 * @author 张帅令
 * @description DB节点所对应的实体
 *
 */
@Deprecated
public class DataBaseEntity implements Entity{

	private int dbIndex;
	
	private String showName;
	
	private String uniqueId;
	
	public int getDbIndex() {
		return dbIndex;
	}



	public void setDbIndex(int dbIndex) {
		this.dbIndex = dbIndex;
	}



	@Override
	public String getShowName() {
		return showName;
	}



	public void setShowName(String showName) {
		this.showName = showName;
	}


	@Override
	public ConnectionNodeType nodeType() {
		return ConnectionNodeType.DB;
	}



	public String getUniqueId() {
		return uniqueId;
	}



	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}
}
