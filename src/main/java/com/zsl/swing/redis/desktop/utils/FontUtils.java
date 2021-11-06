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

    private static final String JAVA_VERSION = System.getProperty("java.version");

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

    public static Font defaultFont(int fontSize){
        return new Font("宋体",Font.BOLD,fontSize);
    }

    public static int fontSizeOverJdk(){
        return JAVA_VERSION.contains("1.8")? FontForJDK8.fontSize():FontForJDK8.fontSize();
    }

    private static class FontForJDK8{
        private static int WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;

        public static int fontSize(){
            if(WIDTH <= 1366){
                return 12;
            }else if(WIDTH <=1440){
                return 13;
            }else if(WIDTH <= 1600){
                return 14;
            }else if(WIDTH <= 1920){
                return 15;
            }
            return 16;
        }
    }


}
