package com.zsl.swing.redis.desktop.utils;

import java.awt.Component;

import javax.swing.JOptionPane;

/**
 * 
 * @author 张帅令
 * @description 对话框工具类
 *
 */
public class DialogUtils {

	private DialogUtils() {}
	
	public static void errorDialog(Component c,String msg) {
		JOptionPane.showMessageDialog(c, msg,"错误",JOptionPane.ERROR_MESSAGE);
	}
	
	public static void errorDialog(String msg) {
		JOptionPane.showMessageDialog(null,msg,"错误",JOptionPane.ERROR_MESSAGE);
	}

	public static void msgDialog(Component c,String object) {
		JOptionPane.showMessageDialog(c, object, "消息", JOptionPane.INFORMATION_MESSAGE);
	}

	public static boolean warnDialog(Component c,String msg) {
		return JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(c, msg,"警告",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
	}
}
