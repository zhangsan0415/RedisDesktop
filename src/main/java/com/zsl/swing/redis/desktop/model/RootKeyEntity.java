package com.zsl.swing.redis.desktop.model;

import java.util.List;

/**
 * 
 * @author 张帅令
 * @description DB节点下目录节点对应的实体
 *
 */
@Deprecated
public class RootKeyEntity implements Entity{
	
	private String showName;
	
	private List<RootKeyEntity> childrenDir;
	
	private RootKeyEntity parent;
	
	private List<KeyEntity> keyList;
	
	public RootKeyEntity(String showName) {
		this.showName = showName;
	}

	@Override
	public String getShowName() {
		return showName;
	}



	public void setShowName(String directoryName) {
		this.showName = directoryName;
	}



	public List<RootKeyEntity> getChildrenDir() {
		return childrenDir;
	}



	public void setChildrenDir(List<RootKeyEntity> childrenDir) {
		this.childrenDir = childrenDir;
	}



	public RootKeyEntity getParent() {
		return parent;
	}



	public void setParent(RootKeyEntity parent) {
		this.parent = parent;
	}



	public List<KeyEntity> getKeyList() {
		return keyList;
	}



	public void setKeyList(List<KeyEntity> keyList) {
		this.keyList = keyList;
	}



	@Override
	public KeyNodeType nodeType() {
		return KeyNodeType.QUERY_KEY_DIR;
	}

}
