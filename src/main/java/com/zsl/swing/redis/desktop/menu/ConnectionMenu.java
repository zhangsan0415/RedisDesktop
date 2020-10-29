package com.zsl.swing.redis.desktop.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import com.zsl.swing.redis.desktop.action.ConnectServerAction;
import com.zsl.swing.redis.desktop.action.ShowConnectionInfoAction;
import com.zsl.swing.redis.desktop.common.ContextHolder;
import com.zsl.swing.redis.desktop.model.ConnectionEntity;
import com.zsl.swing.redis.desktop.tree.ConnectionTreeNode;

/**
 * 
 * @author 张帅令
 * @description 连接节点所对应的菜单
 *
 */
public class ConnectionMenu extends BaseMenu<ConnectionEntity> implements ActionListener{

	private static final long serialVersionUID = 1L;
	
	public static final String M1_STR = "连接信息";
	public static final String M2_STR = "连接";
	public static final String M3_STR = "删除";
	
	private JMenuItem m1 = new JMenuItem(M1_STR);
	private JMenuItem m2 = new JMenuItem(M2_STR);
	private JMenuItem m3 = new JMenuItem(M3_STR);
	
	public ConnectionMenu(ConnectionTreeNode<ConnectionEntity> treeNode) {
		super(treeNode);
		add(m1);
		add(m2);
		add(m3);
		
		m1.addActionListener(new ShowConnectionInfoAction(treeNode));
		m2.addActionListener(new ConnectServerAction(treeNode));
		m3.addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if(M3_STR.equals(command)) {
			ContextHolder.getTree().removeNode(treeNode);
		}
	}
}
