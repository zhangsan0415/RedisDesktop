package com.zsl.swing.redis.desktop.tree;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.zsl.swing.redis.desktop.common.ContextHolder;
import com.zsl.swing.redis.desktop.common.IconPaths;
import com.zsl.swing.redis.desktop.model.NodeEntity;
import com.zsl.swing.redis.desktop.model.DataBaseEntity;
import com.zsl.swing.redis.desktop.model.Entity;
import com.zsl.swing.redis.desktop.model.Entity.ConnectionNodeType;
import com.zsl.swing.redis.desktop.model.RootEntity;
import com.zsl.swing.redis.desktop.utils.CollectionUtils;
import com.zsl.swing.redis.desktop.utils.DialogUtils;
import com.zsl.swing.redis.desktop.utils.FileUtils;
import com.zsl.swing.redis.desktop.utils.IconUtils;
import com.zsl.swing.redis.desktop.utils.RedisUtils;

/**
 * 
 * @author 张帅令
 * @description 连接配置树
 *
 */
@Deprecated
public class ConnectionTree extends JTree{
	
	private static final long serialVersionUID = 1L;

	/**
	 * 根节点数据
	 */
	private static RootEntity rootEntity = new RootEntity();
	
	/**
	 * 根节点
	 */
	private static ConnectionTreeNode<RootEntity> rootNode = new ConnectionTreeNode<>(rootEntity);
	
	
	private ConnectionTree tree = this;
	
	public ConnectionTree() {
		super(rootNode);

		this.setRootVisible(true);

		this.initConnectionNodes();
		
		this.setCellRenderer(new ConnectionTreeCellRender(30,20));
		
		this.addMouseListener(new TreeMouseClickAction());
		
		this.addTreeSelectionListener(new TreeSelectionAction());
		
		this.setExpandedState(new TreePath(rootNode), true);
	}
	
	private void initConnectionNodes() {
		List<NodeEntity> connections = rootEntity.getConnections();
		
		if(!CollectionUtils.isEmpty(connections)) {
			RedisUtils.addConnections(connections);
			for(NodeEntity entity:connections) {
				rootEntity.addConnectionEntity(entity);
//				rootNode.add(new ConnectionTreeNode<>(entity));
			}
		}
	}
	
	public RootEntity getRootEntity() {
		return rootEntity;
	}
	
	public ConnectionTreeNode<RootEntity> getRootNode() {
		return rootNode;
	}
	
	public void saveConnectionsToFile() {
		FileUtils.writeConnections(rootEntity.getConnections());
	}
	
	@SuppressWarnings("rawtypes")
	public void saveConnectionNode(NodeEntity newEntity) {
		rootEntity.addConnectionEntity(newEntity);
		RedisUtils.addConnection(newEntity);

		Enumeration<?> children = rootNode.children();
		while (children.hasMoreElements()) {
			ConnectionTreeNode node = (ConnectionTreeNode) children.nextElement();
			NodeEntity connEntity = (NodeEntity) node.getUserObject();
			if(newEntity.getUniqueId().equals(connEntity.getUniqueId())) {
				node.setUserObject(newEntity);
				tree.refreshTree(new TreePath(rootNode));
				return;
			}
		}
		
		
//		rootNode.add(new ConnectionTreeNode<>(newEntity));
		
		tree.refreshTree(new TreePath(rootNode));
	}

	@SuppressWarnings("unchecked")
	public ConnectionTreeNode<Entity> getSelectionConnectionNode(){
		TreePath path = tree.getSelectionPath();
		if(!Objects.isNull(path)){
			ConnectionTreeNode<Entity> selectNode = (ConnectionTreeNode<Entity>) path.getLastPathComponent();
			Entity entity = selectNode.getUserObject();
			if(entity.isConnectionNode()){
//				return (ConnectionTreeNode<NodeEntity>) selectNode;
			}
		}
		return null;
	}
	
	public void removeNode(ConnectionTreeNode<Entity> treeNode) {
		DefaultTreeModel model = (DefaultTreeModel)this.getModel();
		model.removeNodeFromParent(treeNode);
		
		Entity entity = treeNode.getUserObject();
//		rootEntity.removeConnectionEntity(entity);
		
//		RedisUtils.removeConnection(entity.getUniqueId());
		
		ContextHolder.getKeyPanel().clearPanel();
		this.updateUI();
	}
	
	public void refreshTree(TreePath path) {
		tree.expandPath(path);
		tree.updateUI();
	}
	
	public DataBaseEntity getSelectedDbNode() {
		TreePath selectionPath = this.getSelectionPath();
		if(selectionPath == null) {
			return null;
		}
		
		@SuppressWarnings("rawtypes")
		ConnectionTreeNode<Entity> selectedNode = (ConnectionTreeNode)selectionPath.getLastPathComponent();
		
		Entity entity = selectedNode.getUserObject();
		return entity.isDbNode()?(DataBaseEntity)entity:null;
	}

	public void connect(Component parent){
		TreePath path = this.getSelectionPath();

		ConnectionTreeNode<Entity> selectNode = this.getSelectionConnectionNode();
		if(Objects.isNull(selectNode)){
			DialogUtils.errorDialog(parent,"请先选择连接！");
			return;
		}

		Entity connectionEntity = selectNode.getUserObject();
//		boolean testConn = RedisUtils.connect(connectionEntity.getUniqueId());
//		if(!testConn) {
//			DialogUtils.errorDialog("连接失败");
//			return;
//		}

		selectNode.removeAllChildren();
//		int dbCount = RedisUtils.dbCount(connectionEntity.getUniqueId());
//		for(int i = 0;i<dbCount;i++) {
//			DataBaseEntity dataBaseEntity = new DataBaseEntity();
//			dataBaseEntity.setDbIndex(i);
//			dataBaseEntity.setShowName(String.valueOf(i));
//			dataBaseEntity.setUniqueId(connectionEntity.getUniqueId());
//
//			ConnectionTreeNode<DataBaseEntity> childNode = new ConnectionTreeNode<>(dataBaseEntity);
//			selectNode.add(childNode);
//		}

		this.expandPath(path);
		this.updateUI();
	}
	
	
	private class ConnectionTreeCellRender extends DefaultTreeCellRenderer{
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
			super.getTreeCellRendererComponent(ConnectionTree.this, value, selected, expanded, leaf, row, hasFocus);
		
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
			Entity obj = (Entity) node.getUserObject();
			setText(obj.getShowName());
			ConnectionNodeType nodeType = (ConnectionNodeType)obj.nodeType();
			switch (nodeType) {
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
	
	private class TreeMouseClickAction extends MouseAdapter{
		
		@Override
		public void mousePressed(MouseEvent e) {
			TreePath path = tree.getPathForLocation(e.getX(), e.getY());
			if(path == null) {
				return;
			}
			
			@SuppressWarnings("rawtypes")
			ConnectionTreeNode<Entity> node = (ConnectionTreeNode<Entity>)path.getLastPathComponent();
			Entity entity = node.getUserObject();

			if(!entity.isRoot()) {
				tree.setSelectionPath(path);
			}
			
			ConnectionNodeType nodeType = (ConnectionNodeType)entity.nodeType();
			switch (nodeType) {
			case ROOT:
			case CONNECTION:
				break;
			case DB:
				DataBaseEntity dbEntity = (DataBaseEntity)entity;
				RedisUtils.selectDb(dbEntity.getUniqueId(), dbEntity.getDbIndex());
				break;
			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			if(e.getClickCount() == 2){
				TreePath path = tree.getPathForLocation(e.getX(), e.getY());
				if(path == null) {
					return;
				}

				@SuppressWarnings("rawtypes")
				ConnectionTreeNode<Entity> node = (ConnectionTreeNode)path.getLastPathComponent();
				Entity entity = node.getUserObject();

				if(entity.isConnectionNode() && !node.children().hasMoreElements()){
					tree.connect(ContextHolder.getMainWindow());
				}
			}
		}

	}
	
	private class TreeSelectionAction implements TreeSelectionListener{

		@Override
		public void valueChanged(TreeSelectionEvent e) {
			ContextHolder.getKeyPanel().clearPanel();
		}
	}

	
	

}
