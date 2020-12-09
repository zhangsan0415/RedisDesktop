package com.zsl.swing.redis.desktop;

import javax.swing.SwingUtilities;

import com.zsl.swing.redis.desktop.common.ContextHolder;
import com.zsl.swing.redis.desktop.utils.FontUtils;
import com.zsl.swing.redis.desktop.window.DesktopWindow;

import java.awt.*;

/**
 * 
 * @author 张帅令
 * @description  桌面版主程序
 *
 */
public class RedisDesktop {

	public static void main(String[] args) {
		FontUtils.setDefaultFont(new Font("宋体", Font.BOLD, 25));
		SwingUtilities.invokeLater(() -> ContextHolder.setMainWindow(new DesktopWindow()));
	}
}
