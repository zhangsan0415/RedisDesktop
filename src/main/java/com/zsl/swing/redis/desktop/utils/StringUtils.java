package com.zsl.swing.redis.desktop.utils;

import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author 张帅令
 * @description  字符串工具类
 *
 */
public class StringUtils {

	private StringUtils() {
	}
	
	public static boolean isEmpty(String str) {
		return str == null || str.trim().length() == 0;
	}
	
	public static boolean hasEmpty(String ...strArr) {
		if(strArr == null) {
			return true;
		}else {
			Optional<String> optional = Arrays.stream(strArr).filter(obj -> isEmpty(obj)).findFirst();
			return optional.isPresent();
		}
	}
	
	public static boolean isInt(String str) {
		Pattern p = Pattern.compile("[0-9]*");
		Matcher matcher = p.matcher(str);
		return matcher.matches();
	}
}
