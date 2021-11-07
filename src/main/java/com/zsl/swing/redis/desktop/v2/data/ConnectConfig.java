package com.zsl.swing.redis.desktop.v2.data;

public class ConnectConfig {

	/**
	 * 惟一标识
	 */
	private String id;
	
	/**
	 * 连接名称
	 */
	private String name;
	
	/**
	 * 连接主机
	 */
	private String host;
	
	/**
	 * 端口
	 */
	private int port = 6379;
	
	/**
	 * 密码
	 */
	private String pwd;
	
	/**
	 * 连接数据库
	 */
	private int dbIndex = 0;
	
	public String getShowName() {
		return this.name + "[db-" + (this.dbIndex < 10 ?  "0"+this.dbIndex : this.dbIndex) + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public int getDbIndex() {
		return dbIndex;
	}

	public void setDbIndex(int dbIndex) {
		this.dbIndex = dbIndex;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
}
