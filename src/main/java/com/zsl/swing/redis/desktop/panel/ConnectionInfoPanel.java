package com.zsl.swing.redis.desktop.panel;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.zsl.swing.redis.desktop.common.ContextHolder;
import com.zsl.swing.redis.desktop.model.ConnectionEntity;
import com.zsl.swing.redis.desktop.utils.DialogUtils;
import com.zsl.swing.redis.desktop.utils.StringUtils;
import com.zsl.swing.redis.desktop.utils.UniqueIdUtils;

/**
 * 
 * @author 张帅令
 * @description 连接信息面版
 *
 */
public class ConnectionInfoPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	
	private JTextField hostTextField;
	
	private JTextField portTextField;
	
	private JPasswordField passwordField;
	
	private JTextField showNameField;
	
	private JTextField idField;
	
	private static final int DEFAULT_LEN = 30;
	
	public ConnectionInfoPanel() {
		this(null,true);
	}
	
	public ConnectionInfoPanel(ConnectionEntity entity,boolean editable) {
		JLabel showNameLabel = new JLabel("名称：",JLabel.RIGHT);
		showNameField = new JTextField(DEFAULT_LEN);
		
		JLabel hostLabel = new JLabel("主机：",JLabel.RIGHT);
		hostTextField = new JTextField(DEFAULT_LEN);
		
		JLabel portLabel = new JLabel("端口：",JLabel.RIGHT);
		portTextField = new JTextField(DEFAULT_LEN);
		
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
		this.add(showNameField,constraints,2,0,4);
		this.add(hostLabel,constraints,0,1,2);
		this.add(hostTextField,constraints,2,1,4);
		this.add(portLabel,constraints,0,2,2);
		this.add(portTextField,constraints,2,2,4);
		this.add(passwordLabel,constraints,0,3,2);
		this.add(passwordField,constraints,2,3,4);
	}
	
	public void reset() {
		this.idField.setText(null);
		this.showNameField.setText(null);
		this.hostTextField.setText(null);
		this.portTextField.setText(null);
		this.passwordField.setText(null);
	}
	
	public void setPanelValue(ConnectionEntity entity) {
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
	
	
	public String dataValidate() {
		if(StringUtils.hasEmpty(this.getShowName(),this.getHost(),this.getPort())) {
			return "名称、主机与端口均不能为空！";
		}
		
		if(!StringUtils.isInt(this.getPort())) {
			return "端口号只能是数字！";
		}
		
		return null;
	}
	
	public ConnectionEntity getEntity() {
		ConnectionEntity entity = new ConnectionEntity();
		
		String uniqueId = StringUtils.isEmpty(this.getUniqueId())?UniqueIdUtils.getUniqueId():this.getUniqueId();
		entity.setUniqueId(uniqueId);
		entity.setShowName(this.getShowName());
		entity.setHost(this.getHost());
		entity.setPort(Integer.parseInt(this.getPort()));
		entity.setPassword(this.getPwd());
		return entity;
	}
	
	
	
	public boolean validateAndSaveToConnectionTree() {
		String errorMsg = this.dataValidate();
		if(errorMsg != null) {
			DialogUtils.errorDialog(this, errorMsg);
			return false;
		}
		
		this.saveToConnectionTree();
		return true;
	}
	
	
	public void saveToConnectionTree() {
		ContextHolder.getTree().saveConnectionNode(this.getEntity());

	}
	
	

}
