package com.zsl.swing.redis.desktop.window.menu;

import com.zsl.swing.redis.desktop.common.Constants;

import javax.swing.*;

public class ZslMenuBar extends JMenuBar {

    /**
     * 服务命令
     */
    private ZslServerMenu serverMenu = new ZslServerMenu();

    public ZslMenuBar(){
        super();
        this.add(serverMenu);
    }

}
