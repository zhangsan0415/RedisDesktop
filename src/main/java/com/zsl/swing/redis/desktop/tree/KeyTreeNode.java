package com.zsl.swing.redis.desktop.tree;

import javax.swing.tree.DefaultMutableTreeNode;

import com.zsl.swing.redis.desktop.model.Entity;

public class KeyTreeNode<T extends Entity> extends DefaultMutableTreeNode{

	private static final long serialVersionUID = 1L;
	
	public KeyTreeNode(T userObjecT) {
		super(userObjecT);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T getUserObject() {
		return (T)super.getUserObject();
	}

}
