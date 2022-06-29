package com.zsl.swing.redis.desktop.window.panel;

import com.zsl.swing.redis.desktop.model.NodeEntity;
import com.zsl.swing.redis.desktop.type.MenuEnum;
import com.zsl.swing.redis.desktop.utils.DialogUtils;
import com.zsl.swing.redis.desktop.window.ZslRedisDesktopMainWindow;
import com.zsl.swing.redis.desktop.window.dialog.ZslServerInfoDialog;
import com.zsl.swing.redis.desktop.window.tree.ZslServerTree;

import javax.swing.*;
import java.util.Objects;

public class ZslConnectionPanel extends JScrollPane {

    private ZslServerInfoDialog zslServerInfoDialog;

    public ZslConnectionPanel() {
        super(ZslServerTree.getTree());
    }


    public static void doActionForMenu(String menuName) {
        MenuEnum menu = MenuEnum.getMenuEnum(menuName);
        if(Objects.isNull(menu)){
            return;
        }

        if(MenuEnum.ADD == menu){
            ZslServerInfoDialog.getInstance();
            return;
        }

        NodeEntity entity = ZslServerTree.getSelectedEntity();
        if(Objects.isNull(entity)){
            DialogUtils.errorDialog(ZslRedisDesktopMainWindow.getMainWindow(),"请选择连接！");
            return;
        }

        if(MenuEnum.DELETE == menu){
            boolean flag = DialogUtils.confirmDialog(ZslRedisDesktopMainWindow.getMainWindow(), "确定要删除么？");
            if(flag){
                ZslServerTree.deleteNode(entity);
            }
        }else{
            ZslServerInfoDialog.getInstance(entity);
        }

    }


}
