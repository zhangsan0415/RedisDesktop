package com.zsl.swing.redis.desktop.action;

import com.zsl.swing.redis.desktop.common.ContextHolder;
import com.zsl.swing.redis.desktop.model.ConnectionEntity;
import com.zsl.swing.redis.desktop.model.Entity;
import com.zsl.swing.redis.desktop.tree.ConnectionTree;
import com.zsl.swing.redis.desktop.tree.ConnectionTreeNode;
import com.zsl.swing.redis.desktop.utils.DialogUtils;

import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class DeleteConnectionAction implements ActionListener {

    private Component parent;

    public DeleteConnectionAction(Component parent){
        this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ConnectionTreeNode<ConnectionEntity> selectedNode = ContextHolder.getTree().getSelectionConnectionNode();
        if(Objects.isNull(selectedNode)){
            DialogUtils.errorDialog(parent,"请选择要删除的连接！");
            return;
        }

        boolean flag = DialogUtils.confirmDialog(parent,"确定要删除么？");
        if(flag){
            ContextHolder.getTree().removeNode(selectedNode);
        }
    }
}
