package com.zsl.swing.redis.desktop.window.menu;

import com.zsl.swing.redis.desktop.type.MenuEnum;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ZslServerMenu extends JMenu implements ActionListener {

    private static final String MENU_NAME = "服务（server）";


    public ZslServerMenu(){
        super(MENU_NAME);
        add(this.buildMenuItem(MenuEnum.ADD.getText()));
        add(this.buildMenuItem(MenuEnum.UPDATE.getText()));
        add(this.buildMenuItem(MenuEnum.READ.getText()));
        add(this.buildMenuItem(MenuEnum.DELETE.getText()));

    }

    private JMenuItem buildMenuItem(String itemName){
        JMenuItem menuItem = new JMenuItem(itemName);
        menuItem.addActionListener(this);
        return menuItem;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        MenuEnum.process(actionCommand);
    }
}
