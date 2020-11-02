package com.zsl.swing.redis.desktop.common;

import com.zsl.swing.redis.desktop.utils.CommonUtils;

/**
 * 
 * @author 张帅令
 * @description 一些常量
 *
 */
public class Constants {

	public static final String WIDTH = "width";
	
	public static final String HEIGHT = "height";
	
	/**
	 * 默认DB数量
	 */
	public static final int DB_COUNT = 16;
	
	/**
	 * 默认桌面窗口宽
	 */
	public static final int FRAME_W = CommonUtils.maxWidth();
	
	/**
	 * 默认桌面空口高
	 */
	public static final int FRAME_H = CommonUtils.maxHeight()/2;
	
	public static final int CONNECTION_W = FRAME_W/2;
	
	public static final int CONNECTION_H = FRAME_H/2;
	
	/**
	 * 默认connection树面版的宽度
	 */
	public static final int TREE_PANEL_WIDTH = FRAME_W/5;
	
	
	public static final double DIVIDER_RADIO_MIN = 0.25d;
	
	public static final double DIVIDER_RADIO_MAX = 0.75d;
	
	public static final String REDIS_ALL_PATTERN = "*";
	
	
	public static final int OP_ALL = 1;
	
	public static final int OP_KEYS = 2;
	
	public static final int OP_VALUE = 3;
	
	/**
	 * Redis成功时返回的编码
	 */
	public static final String OK = "OK";

	/**
	 * 默认一次加载的REDIS KEY的数量
	 */
	public static final int DEFAULT_SCAN_COUNT = 50;


}
