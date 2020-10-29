package com.zsl.swing.redis.desktop.utils;

import java.awt.Dimension;
import java.awt.Toolkit;

public class CommonUtils {
	
	private static Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
	
	public static int maxWidth() {
		return screen.width;
	}
	
	public static int maxHeight() {
		return screen.height;
	}
	
	public static void refresh() {
		screen = Toolkit.getDefaultToolkit().getScreenSize();
	}

	
}
