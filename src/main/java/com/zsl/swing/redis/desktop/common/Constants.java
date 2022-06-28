package com.zsl.swing.redis.desktop.common;

import com.zsl.swing.redis.desktop.utils.CommonUtils;

/**
 * 
 * @author 张帅令
 * @description 一些常量
 *
 */
public class Constants {

	/**
	 * 默认DB数量
	 */
	public static final int DB_COUNT = 16;

	public static final int MAX_DB_COUNT = 32;
	
	/**
	 * 默认桌面窗口宽
	 */
	public static final int MAIN_WINDOW_WIDTH = CommonUtils.maxWidth()*18/20;
	
	/**
	 * 默认桌面空口高
	 */
	public static final int MAIN_WINDOW_HEIGHT = CommonUtils.maxHeight()*18/20;

	/**
	 * 主窗口分隔条大小
	 */
	public static final int MAIN_WINDOW_DIVIDER_SIZE = 10;
	
	public static final int CONNECTION_W = MAIN_WINDOW_WIDTH /2;
	
	public static final int CONNECTION_H = MAIN_WINDOW_HEIGHT /2;
	

	public static final String REDIS_ALL_PATTERN = "*";
	
	
	public static final int OP_ALL = 1;
	
	public static final int OP_KEYS = 2;
	
	public static final int OP_VALUE = 3;
	
	public static final int OP_DEL = 4;
	
	/**
	 * Redis成功时返回的编码
	 */
	public static final String OK = "OK";

	/**
	 * 默认一次加载的REDIS KEY的数量
	 */
	public static final int DEFAULT_SCAN_COUNT = 50;

	public static final int FONT_SIZE_8 = 25;

	public static final int FONT_SIZE_GT_8 = 12;


	/**
	 * 面版操作
	 */
	public static final String QUERY = "Query";
	public static final String QUERY_MATCH = "QueryMatch";
	public static final String QUERY_ALL = "QueryAll";
	public static final String FLUSH_DB = "FlushDB";
	public static final String DELETE = "Delete";
	public static final String DELETE_MATCH = "DeleteMatch";

}
