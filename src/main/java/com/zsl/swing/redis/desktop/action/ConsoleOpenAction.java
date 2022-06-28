package com.zsl.swing.redis.desktop.action;

import com.zsl.swing.redis.desktop.common.ContextHolder;
import com.zsl.swing.redis.desktop.model.Entity;
import com.zsl.swing.redis.desktop.model.NodeEntity;
import com.zsl.swing.redis.desktop.tree.ConnectionTreeNode;
import com.zsl.swing.redis.desktop.utils.DialogUtils;
import com.zsl.swing.redis.desktop.window.RedisConsoleWindow;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class ConsoleOpenAction  implements ActionListener {

    private Component parent;

    public ConsoleOpenAction(Component c){
        this.parent = c;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        ConnectionTreeNode<Entity> selectedNode = ContextHolder.getTree().getSelectionConnectionNode();

        if(Objects.isNull(selectedNode)){
            DialogUtils.errorDialog(parent,"请先选择连接！");
            return;
        }

//        new RedisConsoleWindow(selectedNode.getUserObject());
    }
}
