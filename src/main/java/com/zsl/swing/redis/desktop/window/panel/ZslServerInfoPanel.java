package com.zsl.swing.redis.desktop.window.panel;

import com.zsl.swing.redis.desktop.common.ContextHolder;
import com.zsl.swing.redis.desktop.model.NodeEntity;
import com.zsl.swing.redis.desktop.type.NodeTypeEnum;
import com.zsl.swing.redis.desktop.utils.DialogUtils;
import com.zsl.swing.redis.desktop.utils.FileUtils;
import com.zsl.swing.redis.desktop.utils.StringUtils;
import com.zsl.swing.redis.desktop.utils.UniqueIdUtils;
import com.zsl.swing.redis.desktop.window.ZslRedisDesktopMainWindow;
import com.zsl.swing.redis.desktop.window.tree.ZslServerTree;

import javax.swing.*;
import java.awt.*;

/**
 * 
 * @author 张帅令
 * @description 连接信息面版
 *
 */
public class ZslServerInfoPanel extends JPanel{

	private static final long serialVersionUID = 1L;

	private JTextField hostTextField;

	private JTextField portTextField;

	private JPasswordField passwordField;

	private JTextField showNameField;

	private JTextField idField;

	private static final int DEFAULT_LEN = 30;

	public ZslServerInfoPanel() {
		this(null,true);
	}

	public ZslServerInfoPanel(NodeEntity entity, boolean editable) {
		JLabel showNameLabel = new JLabel("名称：",JLabel.RIGHT);
		showNameField = new JTextField(DEFAULT_LEN);
		
		JLabel hostLabel = new JLabel("主机：",JLabel.RIGHT);
		hostTextField = new JTextField(DEFAULT_LEN);
		
		JLabel portLabel = new JLabel("端口：",JLabel.RIGHT);
		portTextField = new JTextField(DEFAULT_LEN);
		portTextField.setText("6379");
		
		JLabel passwordLabel = new JLabel("密码：",JLabel.RIGHT);
		passwordField = new JPasswordField(DEFAULT_LEN);
		
		idField = new JTextField();
		idField.setVisible(false);
		
		this.setPanelValue(entity);
		this.setEditable(editable);
		this.setLayout(new GridBagLayout());
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.EAST;
		
		this.add(showNameLabel,constraints,0,0,2);
		this.add(showNameField,constraints,2,0,6);
		this.add(hostLabel,constraints,0,1,2);
		this.add(hostTextField,constraints,2,1,6);
		this.add(portLabel,constraints,0,2,2);
		this.add(portTextField,constraints,2,2,6);
		this.add(passwordLabel,constraints,0,3,2);
		this.add(passwordField,constraints,2,3,6);

		this.setVisible(true);
	}
	
	public void reset() {
		this.idField.setText(null);
		this.showNameField.setText(null);
		this.hostTextField.setText(null);
		this.portTextField.setText(null);
		this.passwordField.setText(null);
	}
	
	public void setPanelValue(NodeEntity entity) {
		if(entity != null) {
			this.idField.setText(entity.getUniqueId());
			this.hostTextField.setText(entity.getHost());
			this.portTextField.setText(String.valueOf(entity.getPort()));
			this.passwordField.setText(entity.getPassword());
			this.showNameField.setText(entity.getShowName());
		}
	}
	
	public void setEditable(boolean editable) {
		this.hostTextField.setEditable(editable);
		this.portTextField.setEditable(editable);
		this.passwordField.setEditable(editable);
		this.showNameField.setEditable(editable);
	}
	
	public String getHost() {
		return this.hostTextField.getText().trim();
	}
	
	public String getShowName() {
		return this.showNameField.getText().trim();
	}
	
	public String getPort() {
		return this.portTextField.getText().trim();
	}
	
	public String getPwd() {
		return new String(this.passwordField.getPassword());
	}
	
	public String getUniqueId() {
		return this.idField.getText();
	}
	
	public void add(Component s,GridBagConstraints constraints,int x,int y,int w) {
		constraints.gridheight = 1;
		constraints.gridwidth = w;
		constraints.gridx = x;
		constraints.gridy = y;
		this.add(s,constraints);
	}
	
	
	public boolean dataValidate() {
		if(StringUtils.hasEmpty(this.getShowName(),this.getHost(),this.getPort())) {
			ZslRedisDesktopMainWindow.getZslErrorLogPanel().log("名称、主机与端口均不能为空！");
			return false;
		}
		
		if(!StringUtils.isInt(this.getPort())) {
			ZslRedisDesktopMainWindow.getZslErrorLogPanel().log("端口号只能是数字！");
			return false;
		}
		
		return true;
	}
	
	public NodeEntity getEntity() {
		NodeEntity entity = new NodeEntity();
		
		String uniqueId = StringUtils.isEmpty(this.getUniqueId())?UniqueIdUtils.getUniqueId():this.getUniqueId();
		entity.setUniqueId(uniqueId);
		entity.setShowName(this.getShowName());
		entity.setHost(this.getHost());
		entity.setPort(Integer.parseInt(this.getPort()));
		entity.setPassword(this.getPwd());
		entity.setNodeType(NodeTypeEnum.CONNECTION);
		return entity;
	}
	
	
	

	public void saveToConnectionTree() {
		ZslServerTree.saveNode(this.getEntity());
	}
	
	

}
