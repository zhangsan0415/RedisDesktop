package com.zsl.swing.redis.desktop.window;

import java.awt.BorderLayout;
import java.awt.Image;

import javax.swing.JDialog;
import javax.swing.JPanel;

import com.zsl.swing.redis.desktop.common.Constants;
import com.zsl.swing.redis.desktop.common.ContextHolder;
import com.zsl.swing.redis.desktop.common.IconPaths;
import com.zsl.swing.redis.desktop.panel.ConnectionInfoPanel;
import com.zsl.swing.redis.desktop.utils.CommonUtils;
import com.zsl.swing.redis.desktop.utils.IconUtils;

/**
 * 连接信息对话框
 * @author 张帅令
 * @description
 *
 */
public class ConnectionInfoDialog extends JDialog{

	private static final long serialVersionUID = 1L;
	
	private static final Image CONN_ICON_IMAGE = IconUtils.getScaleImage(IconPaths.SET_ICON, 30, 30);
	
	private ConnectionInfoPanel connectionInfoPanel;
	
	private JPanel btnPanel;

	public ConnectionInfoDialog(ConnectionInfoPanel connectionInfoPanel, JPanel btnPanel,String title) {
		super(ContextHolder.getMainWindow());
		this.setTitle(title);
		this.setIconImage(CONN_ICON_IMAGE);
		this.connectionInfoPanel = connectionInfoPanel;
		this.btnPanel = btnPanel;
		this.initDialog();
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}

	private void initDialog() {
		JPanel content = new JPanel(new BorderLayout());
		content.add(this.connectionInfoPanel,BorderLayout.CENTER);
		content.add(this.btnPanel,BorderLayout.SOUTH);
		this.setContentPane(content);
		this.setSize(Constants.CONNECTION_W, Constants.CONNECTION_H);
		
		int x = CommonUtils.maxWidth()/2 - Constants.CONNECTION_W/2;
		int y = CommonUtils.maxHeight()/2 - Constants.CONNECTION_H/2;
		this.setLocation(x, y);
	}
	
	public void showDialog() {
		this.setVisible(true);
	}

}
