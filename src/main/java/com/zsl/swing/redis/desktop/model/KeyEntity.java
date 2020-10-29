package com.zsl.swing.redis.desktop.model;

/**
 * 
 * @author 张帅令
 * @description DB key对应的节点实体
 *
 */
public class KeyEntity implements Entity{
	

	private String showName;
	
	private String nextCursor;
	
	@Override
	public KeyNodeType nodeType() {
		return KeyNodeType.DB_KEY;
	}

	@Override
	public String getShowName() {
		return showName;
	}

	public void setShowName(String value) {
		this.showName = value;
	}

	public String getNextCursor() {
		return nextCursor;
	}

	public void setNextCursor(String nextCursor) {
		this.nextCursor = nextCursor;
	}

}
