package com.zsl.swing.redis.desktop.window.panel;

import com.zsl.swing.redis.desktop.common.IconPaths;
import com.zsl.swing.redis.desktop.model.NodeEntity;
import com.zsl.swing.redis.desktop.tree.ConnectionTree;
import com.zsl.swing.redis.desktop.type.MenuEnum;
import com.zsl.swing.redis.desktop.utils.FileUtils;
import com.zsl.swing.redis.desktop.utils.IconUtils;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.util.List;

public class ZslConnectionPanel extends JScrollPane {

    private JTree serverTree;

    public ZslConnectionPanel() {
        this.setVisible(true);
        this.initServerTree();
    }


    private void initServerTree() {
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(NodeEntity.createRootNode());
        List<NodeEntity> serverNodeEntityList = FileUtils.readConnections();

        serverNodeEntityList.stream().map(DefaultMutableTreeNode::new).forEach(node -> node.setParent(rootNode));

        serverTree = new JTree(rootNode);
        serverTree.setVisible(true);
        serverTree.setCellRenderer(new ConnectionTreeCellRender(30,20));

    }

    public void doAction(MenuEnum menu) {
    }

    private class ConnectionTreeCellRender extends DefaultTreeCellRenderer {
        private static final long serialVersionUID = 1L;

        private int iconWidth;
        private int iconHeight;

        public ConnectionTreeCellRender(int iconWidth,int iconHeight) {
            super();
            this.iconWidth = iconWidth;
            this.iconHeight = iconHeight;
        }

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
                                                      boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

            DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
            NodeEntity nodeEntity = (NodeEntity) node.getUserObject();
            setText(nodeEntity.getShowName());
            switch (nodeEntity.getNodeType()) {
                case ROOT:
                    setIcon(IconUtils.getScaleImageIcon(IconPaths.SET_ICON, iconWidth, iconHeight));
                    break;
                case CONNECTION:
                    setIcon(IconUtils.getScaleImageIcon(IconPaths.CONN_ICON, iconWidth, iconHeight));
                    break;
                case DB:
                    setIcon(IconUtils.getScaleImageIcon(IconPaths.DB_ICON, iconWidth, iconHeight));
                    break;
            }
            return this;
        }
    }
}
