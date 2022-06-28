package com.zsl.swing.redis.desktop.action;

import com.zsl.swing.redis.desktop.common.ContextHolder;
import com.zsl.swing.redis.desktop.model.Entity;
import com.zsl.swing.redis.desktop.model.NodeEntity;
import com.zsl.swing.redis.desktop.tree.ConnectionTreeNode;
import com.zsl.swing.redis.desktop.utils.DialogUtils;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class ConnectionDetailAction implements ActionListener {

    private Component parent;

    public ConnectionDetailAction(Component c){
        this.parent = c;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        ConnectionTreeNode<Entity> selectedNode = ContextHolder.getTree().getSelectionConnectionNode();

        if(Objects.isNull(selectedNode)){
            DialogUtils.errorDialog(parent,"请选择要查看的连接！");
            return;
        }

        new ShowConnectionInfoAction(selectedNode);
    }
}
