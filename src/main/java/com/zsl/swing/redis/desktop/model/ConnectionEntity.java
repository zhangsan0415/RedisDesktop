package com.zsl.swing.redis.desktop.model;

/**
 * 
 * @author 张帅令
 * @description 连接节点对应的实体
 *
 */
public class ConnectionEntity implements Entity{
	
	private String host;
	
	private int port;
	
	private String password;
	
	private String showName;
	
	private String uniqueId;
	
	public ConnectionEntity() {
	}
	
	
	public ConnectionEntity(String showName) {
		this.showName = showName;
	}

	@Override
	public ConnectionNodeType nodeType() {
		return ConnectionNodeType.CONNECTION;
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

	@Override
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
