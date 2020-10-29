package com.zsl.swing.redis.desktop.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;

import com.zsl.swing.redis.desktop.menu.ConnectionMenu;
import com.zsl.swing.redis.desktop.model.ConnectionEntity;
import com.zsl.swing.redis.desktop.panel.ConnectionInfoPanel;
import com.zsl.swing.redis.desktop.window.ConnectionInfoDialog;

/**
 * 
 * @author 张帅令
 * @description 展示连接配置信息
 *
 */
public class ShowConnectionInfoAction implements ActionListener{
	private static final String B1_STR = "编辑";
	private static final String B2_STR = "确定";

	private ConnectionInfoPanel connectionInfoPanel;
	
	private DefaultMutableTreeNode treeNode;

	public ShowConnectionInfoAction(DefaultMutableTreeNode treeNode) {
		this.treeNode = treeNode;
		connectionInfoPanel = new ConnectionInfoPanel();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if(ConnectionMenu.M1_STR.equals(command)) {
			this.showConnectionInfo();
		}
	}

	private void showConnectionInfo() {
		ConnectionEntity entity = (ConnectionEntity)treeNode.getUserObject();
		
		connectionInfoPanel.setPanelValue(entity);
		connectionInfoPanel.setEditable(false);
		
		JPanel btnPanel =  new JPanel();
		ConnectionInfoDialog dialog = new ConnectionInfoDialog(connectionInfoPanel, btnPanel, "连接信息");

		this.initBtnPanel(btnPanel);
		
		dialog.showDialog();
	}

	private JPanel initBtnPanel(JPanel panel) {
		JButton b = new JButton(B1_STR);
		b.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String command = e.getActionCommand();
				if(B1_STR.equals(command)) {
					JButton editBtn = (JButton)e.getSource();
					editBtn.setText(B2_STR);
					connectionInfoPanel.setEditable(true);
					connectionInfoPanel.repaint();
					editBtn.repaint();
				}else if(B2_STR.equals(command)) {
					boolean flag = connectionInfoPanel.validateAndSaveToConnectionTree();
					if(flag) {
						JButton confirmBtn = (JButton)e.getSource();
						
						confirmBtn.setText(B1_STR);
						confirmBtn.repaint();
						
						connectionInfoPanel.setEditable(false);
						connectionInfoPanel.repaint();
					}
				}
			}
		});
		
		panel.add(b);
		return panel;
	}

}
