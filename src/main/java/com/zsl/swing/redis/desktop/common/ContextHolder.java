package com.zsl.swing.redis.desktop.common;

import com.zsl.swing.redis.desktop.area.ErrorLogArea;
import com.zsl.swing.redis.desktop.panel.KeyPanel;
import com.zsl.swing.redis.desktop.tree.ConnectionTree;

import javax.swing.*;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 
 * @author 张帅令
 * @description  上下文环境的一些共享对象
 *
 */
public class ContextHolder {
	
	private static final int MIN_LOG_LEN = 60;
	
	/**
	 * 日志区域
	 */
	private static JTextArea logArea = new ErrorLogArea();
	
	/**
	 * 连接树
	 */
	private static ConnectionTree tree = new ConnectionTree();
	
	private static JFrame mainWindow;
	
	
	/**
	 * 日志上方区域
	 */
	private static KeyPanel keyPanel = new KeyPanel();
	
	public static ConnectionTree getTree() {
		return tree;
	}
	
	public static JTextArea getLogArea() {
		return logArea;
	}
	
	public static KeyPanel getKeyPanel() {
		return keyPanel;
	}
	
	
	public static void setKeyPanel(KeyPanel params) {
		keyPanel = params;
	}
	
	public static void logError(Throwable e) {
		StringWriter stringWriter = new StringWriter();
		e.printStackTrace(new PrintWriter(stringWriter));
		logArea.append("ERROR:\n");
		logArea.append(stringWriter.toString());
		logArea.append("\n");
	}
	
	public static void logMsgs(String ...msgs) {
		logArea.append("MESSAGE:\n");
		logArea.append(buildLogMsg(msgs));
		logArea.append("\n");
	}
	
	private static String buildLogMsg(String ...msgs) {
		StringBuilder sb = new StringBuilder();
		for(String msg:msgs) {
			sb.append(msg);
		}
		
		int len = sb.length();
		if(len < MIN_LOG_LEN) {
			int s = MIN_LOG_LEN - len;
			
			int t = s/2;
			int d = s%2;
			for(int i=0;i< t;i++) {
				sb.append("=>");
			}
			
			if(d != 0) {
				sb.append(">");
			}
		}
		
		return sb.toString();
	}

	public static JFrame getMainWindow() {
		return mainWindow;
	}

	public static void setMainWindow(JFrame mainWindow) {
		ContextHolder.mainWindow = mainWindow;
	}
}
