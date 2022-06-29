package com.zsl.swing.redis.desktop.window.dialog;

import com.zsl.swing.redis.desktop.common.Constants;
import com.zsl.swing.redis.desktop.common.ContextHolder;
import com.zsl.swing.redis.desktop.common.IconPaths;
import com.zsl.swing.redis.desktop.menu.BuildConnectionDialog;
import com.zsl.swing.redis.desktop.model.NodeEntity;
import com.zsl.swing.redis.desktop.panel.ConnectionInfoPanel;
import com.zsl.swing.redis.desktop.utils.*;
import com.zsl.swing.redis.desktop.window.ConnectionInfoDialog;
import com.zsl.swing.redis.desktop.window.ZslRedisDesktopMainWindow;
import com.zsl.swing.redis.desktop.window.panel.ZslConnectionPanel;
import com.zsl.swing.redis.desktop.window.panel.ZslServerInfoPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 连接信息对话框
 * @author 张帅令
 * @description
 *
 */
public class ZslServerInfoDialog extends JDialog{

	private static final long serialVersionUID = 1L;
	
	private static final Image CONN_ICON_IMAGE = IconUtils.getScaleImage(IconPaths.SET_ICON, 30, 30);
	
	private ZslServerInfoPanel zslServerInfoPanel;
	
	private JPanel btnPanel;

	private static final String B1_STR = "确定";
	private static final String B2_STR = "测试连接";

	private static final String TITLE = "连接信息";

	public ZslServerInfoDialog(NodeEntity entity) {
		super(ZslRedisDesktopMainWindow.getMainWindow());

		this.zslServerInfoPanel = new ZslServerInfoPanel(entity,true);
		this.btnPanel = this.buildBtnPanel();
		this.initDialog();
	}

	public static ZslServerInfoDialog getInstance(){
		return new ZslServerInfoDialog(null);
	}

	public static ZslServerInfoDialog getInstance(NodeEntity entity){
		return new ZslServerInfoDialog(entity);
	}
	private void initDialog() {
		JPanel content = new JPanel(new BorderLayout());
		content.add(this.zslServerInfoPanel,BorderLayout.CENTER);
		content.add(this.btnPanel,BorderLayout.SOUTH);

		this.setTitle(TITLE);
		this.setIconImage(CONN_ICON_IMAGE);
		this.setContentPane(content);
		this.setSize(Constants.CONNECTION_W, Constants.CONNECTION_H);
		
		int x = CommonUtils.maxWidth()/2 - Constants.CONNECTION_W/2;
		int y = CommonUtils.maxHeight()/2 - Constants.CONNECTION_H/2;
		this.setLocation(x, y);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}
	
	private JPanel buildBtnPanel() {
		JPanel btnPanel = new JPanel();

		JButton b1 = new JButton(B2_STR);
		JButton b2 = new JButton(B1_STR);


		b1.addActionListener(new BtnAction(this, this.zslServerInfoPanel));
		b2.addActionListener(new BtnAction(this, this.zslServerInfoPanel));

		btnPanel.add(b1);
		btnPanel.add(b2);

		return btnPanel;
	}


	private class BtnAction implements ActionListener {

		private JDialog dialog;

		private ZslServerInfoPanel zslServerInfoPanel;

		public BtnAction(JDialog dialog,ZslServerInfoPanel connectionInfoPanel) {
			this.dialog = dialog;
			this.zslServerInfoPanel = connectionInfoPanel;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			boolean validate = this.zslServerInfoPanel.dataValidate();
			if(!validate){
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
			NodeEntity entity = zslServerInfoPanel.getEntity();
			boolean testConn = RedisUtils.testConn(entity);
			String msg = testConn?"连接成功":"连接失败";
			ZslRedisDesktopMainWindow.getZslErrorLogPanel().log(msg);
			DialogUtils.msgDialog(dialog,msg);
		}

		private void addConn() {
			zslServerInfoPanel.saveToConnectionTree();
			this.dialog.dispose();
		}
	}

}
