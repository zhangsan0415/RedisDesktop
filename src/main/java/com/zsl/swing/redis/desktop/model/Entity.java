package com.zsl.swing.redis.desktop.model;

/**
 * 
 * @author 张帅令
 * @description  树节点对应的实体基类
 *
 */
public interface Entity {

	NodeType nodeType();
	
	String getShowName();
	
	default int getTreeRow() {
		return nodeType().getRow();
	}
	
	default boolean isRoot() {
		return nodeType() == ConnectionNodeType.ROOT || nodeType() == KeyNodeType.QUERY_KEY_DIR;
	}
	
	default boolean isDbNode() {
		return nodeType() == ConnectionNodeType.DB;
	}

	default boolean isConnectionNode(){ return nodeType() == ConnectionNodeType.CONNECTION;}
	
	public static interface NodeType {
		int getRow();
	}
	
	public static enum ConnectionNodeType implements NodeType{
		ROOT("root",0),CONNECTION("connection",1),DB("db",2);
		
		private String code;
		
		private int row;
		
		private ConnectionNodeType(String code,int row) {
			this.code = code;
			this.row = row;
		}
		
		public String getCode() {
			return this.code;
		}
		
		@Override
		public int getRow() {
			return this.row;
		}
	}
	
	public static enum KeyNodeType implements NodeType{
		QUERY_KEY_DIR("query_key_dir",0),DB_KEY("db_key",1);
		
		private String code;
		
		private int row;
		
		private KeyNodeType(String code,int row) {
			this.code = code;
			this.row = row;
		}
		
		public String getCode() {
			return this.code;
		}
		
		@Override
		public int getRow() {
			return this.row;
		}
	}
}
