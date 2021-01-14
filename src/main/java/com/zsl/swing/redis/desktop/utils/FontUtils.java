package com.zsl.swing.redis.desktop.utils;

import com.zsl.swing.redis.desktop.common.Constants;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.util.Enumeration;

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
        return JAVA_VERSION.contains("1.8")? Constants.FONT_SIZE_8:Constants.FONT_SIZE_GT_8;
    }

}
