package com.zsl.swing.redis.desktop.window.panel;

import com.zsl.swing.redis.desktop.common.ContextHolder;
import com.zsl.swing.redis.desktop.model.DataBaseEntity;
import com.zsl.swing.redis.desktop.model.Entity;
import com.zsl.swing.redis.desktop.model.KeyEntity;
import com.zsl.swing.redis.desktop.model.NodeEntity;
import com.zsl.swing.redis.desktop.tree.KeyTreeNode;
import com.zsl.swing.redis.desktop.type.DbOptEnum;
import com.zsl.swing.redis.desktop.type.NodeTypeEnum;
import com.zsl.swing.redis.desktop.utils.DialogUtils;
import com.zsl.swing.redis.desktop.window.node.ZslTreeNode;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

public class ZslKeyQueryPanel extends JPanel {

    private JPanel rightPanel;

    private NodeEntity entity;

    private JTextField queryTextField = new JTextField(30);

    private JComboBox<String> actionBox = this.buildComboBox();

    private JButton runBtn = new JButton("执行(run)");

    private JTree keyTree;

    private static ZslTreeNode<String> rootNode = null;

    public ZslKeyQueryPanel(JPanel rightPanel, NodeEntity entity) {
        this.rightPanel = rightPanel;
        this.entity = entity;

        this.setLayout(new BorderLayout());

        JPanel northPanel = new JPanel();
        northPanel.add(this.queryTextField);
        northPanel.add(this.actionBox);
        northPanel.add(this.runBtn);

        this.initTree();

        this.add(northPanel,BorderLayout.NORTH);
        this.add(new JScrollPane(this.keyTree), BorderLayout.CENTER);
        
        this.runBtn.addActionListener(event -> this.runBtnAction());
    }

    private void runBtnAction() {
    }

    private void initTree() {
        this.keyTree = new JTree(this.getRootNode());
        this.keyTree.setCellRenderer(new KeyTreeCellRender());
        this.keyTree.addMouseListener(new KeyTreeMouseClickAction());

        this.keyTree.setVisible(true);
    }

    private synchronized ZslTreeNode<String> getRootNode(){
        if(Objects.isNull(rootNode)){
            rootNode = new ZslTreeNode<>("keys");
            rootNode.setRoot(true);
        }

        return rootNode;
    }

    public void add(Component f,JPanel t, GridBagConstraints constraints, int x, int y, int w,int h) {
        constraints.gridheight = h;
        constraints.gridwidth = w;
        constraints.gridx = x;
        constraints.gridy = y;
        t.add(f,constraints);
    }

    private JComboBox<String> buildComboBox(){
        JComboBox<String> actionBox = new JComboBox<>();
        actionBox.addItem(DbOptEnum.LIKE.getText());
        actionBox.addItem(DbOptEnum.QUERY.getText());
        actionBox.addItem(DbOptEnum.DELETE_LIKE.getText());
        return actionBox;
    }

    private class KeyTreeCellRender extends DefaultTreeCellRenderer {

        private static final long serialVersionUID = 1L;

        @Override
        public Component getTreeCellRendererComponent(JTree tree,Object value,boolean sel,boolean expanded,boolean leaf,int row,boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

            ZslTreeNode<String> node = (ZslTreeNode<String>)value;

            this.setText(node.getEntity());
            if(node.isRoot()){
                this.setIcon(NodeTypeEnum.DB_KEY_ROOT.getIcon());
            }else{
                this.setIcon(NodeTypeEnum.DB_KEY.getIcon());
            }


            return this;
        }

    }

    private class KeyTreeMouseClickAction extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent event) {
            TreePath treePath = keyTree.getPathForLocation(event.getX(), event.getY());
            if(treePath == null) {
                return ;
            }

            @SuppressWarnings("rawtypes")
            KeyTreeNode keyNode = (KeyTreeNode)treePath.getLastPathComponent();

            Entity entity = keyNode.getUserObject();
            if(entity.isRoot()) {
                return;
            }

            DataBaseEntity selectedDbNode = ContextHolder.getTree().getSelectedDbNode();
            if(selectedDbNode == null) {
                DialogUtils.msgDialog(ContextHolder.getKeyPanel(), "请选择数据库！");
                return;
            }

            KeyEntity keyEntity = (KeyEntity)entity;
            String showName = keyEntity.getShowName();
//            if(showName.equals(MORE_NODE_TEXT)) {
//                ScanResult<String> scanDb = RedisUtils.scanDb(selectedDbNode.getUniqueId(), selectedDbNode.getDbIndex(), key, keyEntity.getNextCursor());
//
//                keyNode.removeFromParent();
//                keyTree.appendNodes(scanDb.getResult(), scanDb.getCursor());
//            }else {
//                String result = RedisUtils.get(selectedDbNode.getUniqueId(), selectedDbNode.getDbIndex(), showName);
//                KeyPanel.setValueText(result);
//                KeyPanel.setQueryFieldText(keyEntity.getShowName());
//            }

        }
    }
}
