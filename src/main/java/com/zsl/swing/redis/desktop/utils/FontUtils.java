package com.zsl.swing.redis.desktop.utils;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.util.Enumeration;

/**
 * 统一字体工具
 */
public class FontUtils {

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
}
