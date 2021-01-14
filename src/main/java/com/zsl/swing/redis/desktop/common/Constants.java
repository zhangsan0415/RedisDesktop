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
	public static final int FRAME_W = CommonUtils.maxWidth()*17/20;
	
	/**
	 * 默认桌面空口高
	 */
	public static final int FRAME_H = CommonUtils.maxHeight()*17/20;
	
	public static final int CONNECTION_W = FRAME_W/2;
	
	public static final int CONNECTION_H = FRAME_H/2;
	

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


}
