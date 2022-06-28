package com.zsl.swing.redis.desktop.model;

import com.zsl.swing.redis.desktop.type.NodeTypeEnum;

import java.util.List;

/**
 * 
 * @author 张帅令
 * @description 连接节点对应的实体
 *
 */
public class NodeEntity {
	
	private String host;
	
	private int port;
	
	private String password;
	
	private String showName;

	private String uniqueId;

	private NodeTypeEnum nodeType;

	private String nextCursor;

	private List<NodeEntity> sonList;

	public static NodeEntity createRootNode(){
		NodeEntity node = new NodeEntity();
		node.setShowName("Servers");
		node.setNodeType(NodeTypeEnum.ROOT);
		return node;
	}

	public List<NodeEntity> getSonList() {
		return sonList;
	}

	public void setSonList(List<NodeEntity> sonList) {
		this.sonList = sonList;
	}

	public String getNextCursor() {
		return nextCursor;
	}

	public void setNextCursor(String nextCursor) {
		this.nextCursor = nextCursor;
	}

	public NodeTypeEnum getNodeType() {
		return nodeType;
	}

	public void setNodeType(NodeTypeEnum nodeType) {
		this.nodeType = nodeType;
	}

	public NodeEntity() {
	}
	
	
	public NodeEntity(String showName) {
		this.showName = showName;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}


	public String getUniqueId() {
		return uniqueId;
	}


	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}
}
