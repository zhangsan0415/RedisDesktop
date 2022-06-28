package com.zsl.swing.redis.desktop.tree;

import javax.swing.tree.DefaultMutableTreeNode;

import com.zsl.swing.redis.desktop.model.Entity;

public class ConnectionTreeNode<T> extends DefaultMutableTreeNode{

	private static final long serialVersionUID = 1L;
	
	public ConnectionTreeNode(T entity) {
		super(entity);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public T getUserObject() {
		return (T)super.getUserObject();
	}
}
