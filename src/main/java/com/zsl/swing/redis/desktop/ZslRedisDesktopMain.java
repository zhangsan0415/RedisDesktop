package com.zsl.swing.redis.desktop;

import javax.swing.SwingUtilities;

import com.zsl.swing.redis.desktop.common.ContextHolder;
import com.zsl.swing.redis.desktop.utils.FontUtils;
import com.zsl.swing.redis.desktop.window.DesktopWindow;
import com.zsl.swing.redis.desktop.window.ZslRedisDesktopMainWindow;

/**
 * 
 * @author 张帅令
 * @description  桌面版主程序
 *
 */
public class ZslRedisDesktopMain {

	public static void main(String[] args) {
		System.out.println(System.getProperty("java.version"));
		FontUtils.setDefaultFont();
//		SwingUtilities.invokeLater(() -> ContextHolder.setMainWindow(new DesktopWindow()));
		SwingUtilities.invokeLater(() -> new ZslRedisDesktopMainWindow());
	}
}
