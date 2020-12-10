package com.zsl.swing.redis.desktop.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.tree.TreePath;

import com.zsl.swing.redis.desktop.common.Constants;
import com.zsl.swing.redis.desktop.common.ContextHolder;
import com.zsl.swing.redis.desktop.model.ConnectionEntity;
import com.zsl.swing.redis.desktop.model.DataBaseEntity;
import com.zsl.swing.redis.desktop.tree.ConnectionTreeNode;
import com.zsl.swing.redis.desktop.utils.DialogUtils;
import com.zsl.swing.redis.desktop.utils.RedisUtils;

/**
 * 
 * @author 张帅令
 * @description 连接菜单
 *
 */
public class ConnectServerAction implements ActionListener{
	
	private ConnectionTreeNode<ConnectionEntity> treeNode;
	
	public ConnectServerAction(ConnectionTreeNode<ConnectionEntity> treeNode) {
		this.treeNode = treeNode;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ConnectionEntity connectionEntity = treeNode.getUserObject();
		
		
		boolean testConn = RedisUtils.connect(connectionEntity.getUniqueId());
		if(!testConn) {
			DialogUtils.errorDialog("连接失败");
			return;
		}
		
		this.showDbList(connectionEntity);
	}

	
	
	private void showDbList(ConnectionEntity connectionEntity) {
		treeNode.removeAllChildren();

		int dbCount = RedisUtils.dbCount(connectionEntity.getUniqueId());
		for(int i = 0;i<dbCount;i++) {
			DataBaseEntity dataBaseEntity = new DataBaseEntity();
			dataBaseEntity.setDbIndex(i);
			dataBaseEntity.setShowName(String.valueOf(i));
			dataBaseEntity.setUniqueId(connectionEntity.getUniqueId());

			ConnectionTreeNode<DataBaseEntity> childNode = new ConnectionTreeNode<>(dataBaseEntity);
			treeNode.add(childNode);
		}

		TreePath nodePath = new TreePath(treeNode);
		ContextHolder.getTree().refreshTree(nodePath);
	}

}
