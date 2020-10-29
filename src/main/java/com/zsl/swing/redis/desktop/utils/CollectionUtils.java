package com.zsl.swing.redis.desktop.utils;

import java.util.Collection;

public class CollectionUtils {

	private CollectionUtils() {
	}
	
	public static boolean isEmpty(Collection<?> c) {
		return c == null || c.isEmpty();
	}

}
