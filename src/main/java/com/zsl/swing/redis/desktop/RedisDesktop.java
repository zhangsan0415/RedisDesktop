package com.zsl.swing.redis.desktop;

import javax.swing.SwingUtilities;

import com.zsl.swing.redis.desktop.common.ContextHolder;
import com.zsl.swing.redis.desktop.window.DesktopWindow;

/**
 * 
 * @author 张帅令
 * @description  桌面版主程序
 *
 */
public class RedisDesktop {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> ContextHolder.setMainWindow(new DesktopWindow()));
	}
}
