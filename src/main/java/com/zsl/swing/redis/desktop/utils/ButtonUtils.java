package com.zsl.swing.redis.desktop.utils;

import com.zsl.swing.redis.desktop.common.IconPaths;

import javax.swing.*;

public class ButtonUtils {

    public static JButton createButton(String text,String alt,String iconPath){
        ImageIcon icon = IconUtils.getScaleImageIcon(iconPath, 20, 20);
        JButton button = new JButton(text,icon);
        button.setToolTipText(alt);
        return button;
    }

    public static JButton createNewToolBar(){
        return createButton(null,"新建连接",IconPaths.CREATE_NEW_ICON);
    }

    public static JButton deleteToolBar(){
        return createButton(null,"删除连接",IconPaths.DELETE_ICON);
    }

    public static JButton detailToolBar(){
        return createButton(null,"查看连接",IconPaths.DETAIL_ICON);
    }

    public static JButton consoleToolBar(){
        return createButton(null,"打开控制台",IconPaths.CONSOLE_ICON);
    }

    public static JButton connectToolBar() {
        return createButton(null,"连接",IconPaths.DO_CONNECT_ICON);
    }
}
