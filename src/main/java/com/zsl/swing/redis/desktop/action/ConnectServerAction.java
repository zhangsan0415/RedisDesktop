package com.zsl.swing.redis.desktop.action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.zsl.swing.redis.desktop.common.ContextHolder;

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
