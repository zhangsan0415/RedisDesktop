package com.zsl.swing.redis.desktop.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import com.zsl.swing.redis.desktop.model.ConnectionEntity;
import com.zsl.swing.redis.desktop.model.RootEntity;
import com.zsl.swing.redis.desktop.panel.ConnectionInfoPanel;
import com.zsl.swing.redis.desktop.tree.ConnectionTreeNode;
import com.zsl.swing.redis.desktop.utils.DialogUtils;
import com.zsl.swing.redis.desktop.utils.RedisUtils;
import com.zsl.swing.redis.desktop.window.ConnectionInfoDialog;

/**
 * 
 * @author 张帅令
 * @description connections节点，也即根节点对应的菜单
 *
 */
public class RootMenu extends BaseMenu<RootEntity> implements ActionListener{

	private static final long serialVersionUID = 1L;
	
	private static final String B1_STR = "确定";
	private static final String B2_STR = "测试连接";
	

	private JMenuItem newItem = new JMenuItem("新建");
	
	public RootMenu(ConnectionTreeNode<RootEntity> treeNode) {
		super(treeNode);
		
		add(newItem);
		
		newItem.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		ConnectionInfoPanel connectionInfoPanel = new ConnectionInfoPanel();
		JPanel btnPanel = new JPanel();
		
		ConnectionInfoDialog dialog = new ConnectionInfoDialog(connectionInfoPanel, btnPanel, "新建连接");
		
		this.buildBtnPanel(connectionInfoPanel, dialog, btnPanel);
		dialog.showDialog();
		
//		final RootMenuNewWindow newConnWindow = new RootMenuNewWindow();
//		newConnWindow.initWindow();
	}
	
	private JPanel buildBtnPanel(ConnectionInfoPanel connectionInfoPanel,ConnectionInfoDialog dialog,JPanel btnPanel) {
		JButton b1 = new JButton(B2_STR);
		JButton b2 = new JButton(B1_STR);

		
		b1.addActionListener(new BtnAction(dialog, connectionInfoPanel));
		b2.addActionListener(new BtnAction(dialog, connectionInfoPanel));
		
		btnPanel.add(b1);
		btnPanel.add(b2);
		
		return btnPanel;
	}
	
	
	private class BtnAction implements ActionListener{
		
		private JDialog dialog;
		
		private ConnectionInfoPanel connectionInfoPanel;
		
		public BtnAction(JDialog dialog,ConnectionInfoPanel connectionInfoPanel) {
			this.dialog = dialog;
			this.connectionInfoPanel = connectionInfoPanel;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			String errMsg = this.connectionInfoPanel.dataValidate();
			if(errMsg != null) {
				DialogUtils.errorDialog(this.dialog, errMsg);
				return;
			}
			
			String command = e.getActionCommand();
			if(command.equals(B1_STR)) {
				this.addConn();
			}else if(command.equals(B2_STR)) {
				this.testConn();
			}
		}
		
		private void testConn() {
			ConnectionEntity entity = connectionInfoPanel.getEntity();
			boolean testConn = RedisUtils.testConn(entity);
			String msg = testConn?"连接成功":"连接失败";
			DialogUtils.msgDialog(dialog,msg);
		}
		
		private void addConn() {
			connectionInfoPanel.saveToConnectionTree();
			this.dialog.dispose();
		}
	}

}
