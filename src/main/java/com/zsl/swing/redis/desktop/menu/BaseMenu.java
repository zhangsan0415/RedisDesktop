package com.zsl.swing.redis.desktop.menu;

import javax.swing.JPopupMenu;

import com.zsl.swing.redis.desktop.model.Entity;
import com.zsl.swing.redis.desktop.tree.ConnectionTreeNode;

/**
 * 
 * @author 张帅令
 * @description 菜单基类
 *
 */
public class BaseMenu<T extends Entity> extends JPopupMenu{

	private static final long serialVersionUID = 1L;
	
	/**
	 * 菜单所绑定的树接点
	 */
	protected ConnectionTreeNode<T>  treeNode;
	
	public BaseMenu(ConnectionTreeNode<T> treeNode) {
		super();
		this.treeNode = treeNode;
	}

}
