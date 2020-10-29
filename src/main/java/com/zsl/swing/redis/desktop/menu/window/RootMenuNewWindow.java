package com.zsl.swing.redis.desktop.menu.window;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.zsl.swing.redis.desktop.common.Constants;
import com.zsl.swing.redis.desktop.common.IconPaths;
import com.zsl.swing.redis.desktop.model.ConnectionEntity;
import com.zsl.swing.redis.desktop.panel.ConnectionInfoPanel;
import com.zsl.swing.redis.desktop.utils.CommonUtils;
import com.zsl.swing.redis.desktop.utils.DialogUtils;
import com.zsl.swing.redis.desktop.utils.RedisUtils;
import com.zsl.swing.redis.desktop.window.BaseWindow;

/**
 * 
 * @author 张帅令
 * @description  新建连接界面
 *
 */
@Deprecated
public class RootMenuNewWindow implements ActionListener{

	private ConnectionInfoPanel connectionInfoPanel;
	
	private JFrame frame;
	
	public RootMenuNewWindow() {
		this.connectionInfoPanel = new ConnectionInfoPanel();
	}
	
	public void initWindow() {
		JPanel buttonPanel = this.buttonPanel();
		
		JPanel contentPanel = new JPanel(new BorderLayout());
		contentPanel.add(connectionInfoPanel,BorderLayout.CENTER);
		contentPanel.add(buttonPanel,BorderLayout.SOUTH);
		
		frame = new BaseWindow("新建连接",IconPaths.SET_ICON);
		frame.setContentPane(contentPanel);
		frame.setSize(Constants.CONNECTION_W, Constants.CONNECTION_H);
		
		int maxWidth = CommonUtils.maxWidth();
		int maxHeight = CommonUtils.maxHeight();
		int x = maxWidth/2 - Constants.CONNECTION_W/2;
		int y = maxHeight/2 - Constants.CONNECTION_H/2;
		frame.setLocation(x, y);
		
		frame.setVisible(true);
		
		frame.setResizable(false);
		
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				frame.dispose();
			}
		});
	}
	
	private JPanel buttonPanel() {
		JButton b1 = new JButton("测试连接");
		JButton b2 = new JButton("确定");

		
		b1.addActionListener(this);
		b2.addActionListener(this);
		
		JPanel panel = new JPanel(new GridLayout(1,2));
		panel.add(b1);
		panel.add(b2);
		
		return panel;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String errMsg = connectionInfoPanel.dataValidate();
		if(errMsg != null) {
			DialogUtils.errorDialog(frame, errMsg);
			return;
		}
		
		String command = e.getActionCommand();
		if(command.equals("确定")) {
			this.addConn();
		}else if(command.equals("测试连接")) {
			this.testConn();
		}
	}
	
	private void testConn() {
		ConnectionEntity entity = connectionInfoPanel.getEntity();
		boolean testConn = RedisUtils.testConn(entity);
		String msg = testConn?"连接成功":"连接失败";
		DialogUtils.msgDialog(frame,msg);
	}
	
	private void addConn() {
		connectionInfoPanel.saveToConnectionTree();
		frame.dispose();
	}


}
