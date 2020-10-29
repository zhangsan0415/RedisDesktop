package com.zsl.swing.redis.desktop.utils;

/**
 * 
 * @author 张帅令
 * @description 惟一标识 生成，后续优化
 *
 */
public class UniqueIdUtils {

	
	public static String getUniqueId() {
		return String.valueOf(System.currentTimeMillis());
	}
}
