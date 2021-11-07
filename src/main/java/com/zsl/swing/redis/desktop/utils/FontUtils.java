package com.zsl.swing.redis.desktop.utils;

import java.awt.Font;
import java.awt.Toolkit;
import java.util.Enumeration;

import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

/**
 * 统一字体工具
 */
public class FontUtils {
	
	public static void setDefaultFont() {
		setDefaultFont(defaultFont());
	}

    public static void setDefaultFont(Font font){

        FontUIResource resource = new FontUIResource(font);
        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while(keys.hasMoreElements()){
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if(value instanceof FontUIResource){
                UIManager.put(key,resource);
            }
        }
    }

    public static Font defaultFont() {
    	int width = Toolkit.getDefaultToolkit().getScreenSize().width, fontSize;
    	switch (width) {
		case 1366:
			fontSize = 12;
			break;
		case 1440:
			fontSize = 13;
			break;
		case 1600:
			fontSize = 14;
			break;
		case 1920:
			fontSize = 15;
			break;
		default:
			fontSize = 16;
			break;
		}
    	
    	return new Font("宋体",Font.BOLD,fontSize);
    }



}
