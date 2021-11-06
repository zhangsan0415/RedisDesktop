package com.zsl.swing.redis.desktop;

import java.awt.Font;

import javax.swing.SwingUtilities;

import com.zsl.swing.redis.desktop.common.ContextHolder;
import com.zsl.swing.redis.desktop.utils.FontUtils;
import com.zsl.swing.redis.desktop.window.DesktopWindow;

/**
 * 
 * @author 张帅令
 * @description  桌面版主程序
 *
 */
public class RedisDesktop {

	public static void main(String[] args) {
		System.out.println(System.getProperty("java.version"));

		Font font = FontUtils.defaultFont(FontUtils.fontSizeOverJdk());

		System.out.println(font);
		FontUtils.setDefaultFont(font);
		SwingUtilities.invokeLater(() -> ContextHolder.setMainWindow(new DesktopWindow()));
	}
}
