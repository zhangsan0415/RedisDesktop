package com.zsl.swing.redis.desktop.tree;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

import com.zsl.swing.redis.desktop.common.Constants;
import com.zsl.swing.redis.desktop.common.ContextHolder;
import com.zsl.swing.redis.desktop.common.IconPaths;
import com.zsl.swing.redis.desktop.model.DataBaseEntity;
import com.zsl.swing.redis.desktop.model.Entity;
import com.zsl.swing.redis.desktop.model.Entity.KeyNodeType;
import com.zsl.swing.redis.desktop.model.KeyEntity;
import com.zsl.swing.redis.desktop.model.RootKeyEntity;
import com.zsl.swing.redis.desktop.panel.KeyPanel;
import com.zsl.swing.redis.desktop.utils.CollectionUtils;
import com.zsl.swing.redis.desktop.utils.DialogUtils;
import com.zsl.swing.redis.desktop.utils.IconUtils;
import com.zsl.swing.redis.desktop.utils.RedisUtils;

import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

public class KeyTree extends JTree{

	private static final long serialVersionUID = 1L;
	
	private static final String MORE_NODE_TEXT = "查看更多";

	private static RootKeyEntity rootKeyEntity = new RootKeyEntity("keys");
	
	private static KeyTreeNode<RootKeyEntity> rootNode = new KeyTreeNode<>(rootKeyEntity);
	
	/**
	 * 1表示展示所有，2表示查询keys，3表示查询值 
	 */
	private int opType = Constants.OP_ALL;
	
	private KeyTree tree = this;
	
	public KeyTree() {
		super(rootNode);
		this.setRootVisible(true);
		this.setCellRenderer(new KeyTreeCellRender(20,15));
		this.addMouseListener(new KeyTreeMouseClickAction());
	}
	
	public void addNodes(List<String> keys,String nextCursor) {
		rootNode.removeAllChildren();
		this.appendNodes(keys, nextCursor);
	}
	
	public void appendNodes(List<String> keys,String nextCursor) {
		if(!CollectionUtils.isEmpty(keys)) {
			for(String key:keys) {
				KeyEntity keyEntity = new KeyEntity();
				keyEntity.setShowName(key);
				
				rootNode.add(new KeyTreeNode<>(keyEntity));
			}
			
			this.appendMoreNode(nextCursor);
		}
		this.expandPath(new TreePath(rootNode));
		this.updateUI();
	}
	
	private void appendMoreNode(String nextCursor) {
		if(!ScanParams.SCAN_POINTER_START.equals(nextCursor)) {
			KeyEntity keyEntity = new KeyEntity();
			keyEntity.setShowName(MORE_NODE_TEXT);
			keyEntity.setNextCursor(nextCursor);
			rootNode.add(new KeyTreeNode<>(keyEntity));
		}
	}
	
	public void removeNodeByShowName(String showName) {
		@SuppressWarnings("rawtypes")
		Enumeration children = rootNode.children();
		while(children.hasMoreElements()) {
			@SuppressWarnings("unchecked")
			KeyTreeNode<Entity> nextElement = (KeyTreeNode<Entity>)children.nextElement();
			
			if(nextElement.getUserObject().getShowName().equals(showName)) {
				nextElement.removeFromParent();
			}
		}
		
		this.updateUI();
	}
	
	public void clearNodes() {
		rootNode.removeAllChildren();
		this.updateUI();
	}
	
	
	
	public void setOpType(int opType) {
		this.opType = opType;
	}



	private class KeyTreeCellRender extends DefaultTreeCellRenderer{

		private static final long serialVersionUID = 1L;
		
		private int iconWidth;
		
		private int iconHeight;
		
		public KeyTreeCellRender(int width,int height) {
			this.iconHeight = height;
			this.iconWidth = width;
			
		}
		
		@Override
		public Component getTreeCellRendererComponent(JTree tree,Object value,boolean sel,boolean expanded,boolean leaf,int row,boolean hasFocus) {
			super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
			
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
			
			Entity entity = (Entity)node.getUserObject();
			this.setText(entity.getShowName());
			
			KeyNodeType nodeType = (KeyNodeType)entity.nodeType();
			switch (nodeType) {
			case QUERY_KEY_DIR:
				this.setIcon(IconUtils.getScaleImageIcon(IconPaths.DB_DIR_ICON, iconWidth, iconHeight));
				break;
			case DB_KEY:
				this.setIcon(IconUtils.getScaleImageIcon(IconPaths.DB_KEY_ICON, iconWidth, iconHeight));
				break;
			}
			return this;
		}
		
	}
	
	private class KeyTreeMouseClickAction extends MouseAdapter{
		@Override
		public void mousePressed(MouseEvent event) {
			TreePath treePath = tree.getPathForLocation(event.getX(), event.getY());
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
			if(showName.equals(MORE_NODE_TEXT)) {
				String key = Constants.OP_KEYS == tree.opType?KeyPanel.getQueryParam():Constants.OP_ALL == tree.opType?Constants.REDIS_ALL_PATTERN:null;
				ScanResult<String> scanDb = RedisUtils.scanDb(selectedDbNode.getUniqueId(), selectedDbNode.getDbIndex(), key, keyEntity.getNextCursor());
				
				keyNode.removeFromParent();
				tree.appendNodes(scanDb.getResult(), scanDb.getCursor());
			}else {
				String result = RedisUtils.get(selectedDbNode.getUniqueId(), selectedDbNode.getDbIndex(), showName);
				KeyPanel.setValueText(result);
				KeyPanel.setQueryFieldText(keyEntity.getShowName());
			}
			
		}
	}
}
