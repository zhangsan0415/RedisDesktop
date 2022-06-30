package com.zsl.swing.redis.desktop.window.tree;

import com.zsl.swing.redis.desktop.model.NodeEntity;
import com.zsl.swing.redis.desktop.type.MenuEnum;
import com.zsl.swing.redis.desktop.type.NodeTypeEnum;
import com.zsl.swing.redis.desktop.utils.DialogUtils;
import com.zsl.swing.redis.desktop.utils.FileUtils;
import com.zsl.swing.redis.desktop.utils.RedisUtils;
import com.zsl.swing.redis.desktop.window.ZslRedisDesktopMainWindow;
import com.zsl.swing.redis.desktop.window.panel.ZslConnectionPanel;
import com.zsl.swing.redis.desktop.window.panel.ZslDbOptPanel;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Objects;

public class ZslServerTree{

    private static JTree me = buildTree();

    /**
     * 根节点
     */
    private ZslServerTree(){
    }

    public static JTree getTree(){
        return me;
    }

    private static JTree buildTree(){
        DefaultMutableTreeNode rootNode = NodeEntity.getServerTreeRootNode();
        JTree tree = new JTree(rootNode);
        List<NodeEntity> serverNodeEntityList = FileUtils.readConnections();

        serverNodeEntityList.stream().map(DefaultMutableTreeNode::new).forEach(node -> rootNode.add(node));

        tree.setCellRenderer(new ConnectionTreeCellRender());
        tree.addMouseListener(new TreeMouseClickAction());
        tree.expandPath(new TreePath(rootNode));
        tree.setVisible(true);
        return tree;
    }

    public static NodeEntity getSelectedEntity(){
        TreePath path = me.getSelectionPath();
        if(Objects.isNull(path)){
            return null;
        }
        DefaultMutableTreeNode selectNode = (DefaultMutableTreeNode) path.getLastPathComponent();
        return (NodeEntity)selectNode.getUserObject();

    }

    private static void refreshTree(){
        List<NodeEntity> nodeEntities = FileUtils.readConnections();

        DefaultMutableTreeNode rootNode = NodeEntity.getServerTreeRootNode();
        rootNode.removeAllChildren();

        nodeEntities.stream().map(DefaultMutableTreeNode::new).forEach(node -> rootNode.add(node));


        me.clearSelection();
        me.expandPath(new TreePath(rootNode));
        me.updateUI();
    }

    public static void saveNode(NodeEntity entity) {
        boolean result = FileUtils.saveConnection(entity);
        if(result){
            refreshTree();
            DialogUtils.msgDialog(ZslRedisDesktopMainWindow.getMainWindow(),"保存成功！");
        } else{
            ZslRedisDesktopMainWindow.getZslErrorLogPanel().logError("保存连接失败");
            DialogUtils.errorDialog(ZslRedisDesktopMainWindow.getMainWindow(),"保存连接失败！");
        }
    }

    public static void deleteNode(NodeEntity entity) {
        boolean result = FileUtils.deleteConnection(entity);
        if(result){
            refreshTree();
            RedisUtils.removeConnection(entity.getUniqueId());
            DialogUtils.msgDialog(ZslRedisDesktopMainWindow.getMainWindow(),"删除成功！");
        }else{
            ZslRedisDesktopMainWindow.getZslErrorLogPanel().logError("删除连接失败");
            DialogUtils.errorDialog("删除连接失败！");
        }

    }


    private static class ConnectionTreeCellRender extends DefaultTreeCellRenderer {
        private static final long serialVersionUID = 1L;


        public ConnectionTreeCellRender() {
            super();
        }

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
                                                      boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

            DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
            NodeEntity nodeEntity = (NodeEntity) node.getUserObject();
            setText(nodeEntity.getShowName());
            setIcon(nodeEntity.getNodeType().getIcon());
            return this;
        }
    }

    private static class TreeMouseClickAction extends MouseAdapter implements ActionListener {

        private static final String TAB_TITLE_FORMAT = "%s[%s]";
        @Override
        public void mousePressed(MouseEvent e) {
            TreePath path = me.getPathForLocation(e.getX(), e.getY());
            if(path == null) {
                return;
            }

            DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
            NodeEntity entity = (NodeEntity)node.getUserObject();

            if(!entity.isRoot()) {
                me.setSelectionPath(path);
            }

            if(entity.isDb()){
                RedisUtils.selectDb(entity.getUniqueId(), entity.getDbIndex());
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            TreePath path = me.getPathForLocation(e.getX(), e.getY());
            if(path == null) {
                return;
            }

            DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
            NodeEntity entity = (NodeEntity)node.getUserObject();

            int type = e.getButton();
            if(type == MouseEvent.BUTTON3){
                //右键点击
                this.doWhenRightClick(entity,e);
            }else if(type == MouseEvent.BUTTON1){
                //左键点击
                this.doWhenLeftClick(node,entity,e);

            }
        }

        private void doWhenRightClick(NodeEntity entity, MouseEvent e){
            if(entity.isServer()){
                JMenuItem c = new JMenuItem(MenuEnum.ADD.getText());
                JMenuItem u = new JMenuItem(MenuEnum.UPDATE.getText());
                JMenuItem r = new JMenuItem(MenuEnum.READ.getText());
                JMenuItem d = new JMenuItem(MenuEnum.DELETE.getText());

                c.addActionListener(this);
                u.addActionListener(this);
                r.addActionListener(this);
                d.addActionListener(this);

                JPopupMenu popupMenu = new JPopupMenu();
                popupMenu.add(c);
                popupMenu.add(u);
                popupMenu.add(r);
                popupMenu.add(d);

                popupMenu.show(me,e.getX(),e.getY());
            }else if(entity.isDb()){
                JMenuItem flushMenuItem = new JMenuItem("FlushDB");
                flushMenuItem.addActionListener(event -> {
                    boolean result = DialogUtils.confirmDialog(ZslRedisDesktopMainWindow.getMainWindow(), "确定要清空数据？");
                    if(result){
                        RedisUtils.flushDb(entity.getUniqueId(),entity.getDbIndex());
                        DialogUtils.msgDialog(ZslRedisDesktopMainWindow.getMainWindow(), "清空数据成功！");
                    }
                });

                JMenuItem createOptWindowItem = new JMenuItem("新建操作窗口");
                createOptWindowItem.addActionListener(event -> {
                    String serverName = entity.getParent().getShowName();
                    String tabTitle = String.format(TAB_TITLE_FORMAT, serverName, entity.getDbIndex());
                    ZslRedisDesktopMainWindow.getZslShowPanel().addTab(tabTitle, NodeTypeEnum.DB.getIcon(), ZslDbOptPanel.getInstance(entity));
                });

                JPopupMenu popupMenu = new JPopupMenu();
                popupMenu.add(createOptWindowItem);
                popupMenu.add(flushMenuItem);

                popupMenu.show(me,e.getX(),e.getY());
            }

        }
        private void doWhenLeftClick(DefaultMutableTreeNode node, NodeEntity entity, MouseEvent e){
            int count = e.getClickCount();
            if(count == 2){
                //双击连接，展示数据库，
                if(entity.isServer() && !node.children().hasMoreElements()){
                    ZslServerTree.createDbNodeList(node);
                }

                //如果双击数据库,scan方式展示所有的的key
                if(entity.isDb()){
                    //TODO
                }
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            ZslConnectionPanel.doActionForMenu(e.getActionCommand());
        }
    }

    private static void createDbNodeList(DefaultMutableTreeNode node) {
        NodeEntity entity = (NodeEntity)node.getUserObject();

        boolean result = RedisUtils.testConn(entity);
        if(!result){
            DialogUtils.errorDialog(ZslRedisDesktopMainWindow.getMainWindow(),"连接失败！");
            return;
        }

        int dbCount = RedisUtils.dbCount(entity.getUniqueId());

        for(int i=0; i< dbCount; i++){
            NodeEntity dbEntity = new NodeEntity();
            dbEntity.setDbIndex(i);
            dbEntity.setNodeType(NodeTypeEnum.DB);
            dbEntity.setUniqueId(entity.getUniqueId());
            dbEntity.setShowName(String.valueOf(i));

            dbEntity.setParent(entity);
            node.add(new DefaultMutableTreeNode(dbEntity));
        }

        me.expandPath(new TreePath(node));
        me.updateUI();
    }
}
