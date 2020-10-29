package com.zsl.swing.redis.desktop.window;

import javax.swing.JFrame;

import com.zsl.swing.redis.desktop.utils.IconUtils;

/**
 * 
 * @author 张帅令
 * @description 本程序操作所有窗口基类
 *
 */
public class BaseWindow extends JFrame{

	private static final long serialVersionUID = 1L;
	
	
	private final static int ICON_WIDTH = 40;
	
	private final static int ICON_HEIGHT = 40;
	
	public BaseWindow(String title,String iconPath) {
		super(title);
		setIconImage(IconUtils.getScaleImage(iconPath,ICON_WIDTH , ICON_HEIGHT));
	}
	
	public BaseWindow(String title) {
		super(title);
	}


}
