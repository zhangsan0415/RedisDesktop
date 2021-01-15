package com.zsl.swing.redis.desktop.action;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

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

	private Component parent;

	public ConnectServerAction(Component c){
		this.parent = c;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		ContextHolder.getTree().connect(parent);
	}
}
