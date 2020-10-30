package com.zsl.swing.redis.desktop.panel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.alibaba.fastjson.JSON;
import com.zsl.swing.redis.desktop.common.Constants;
import com.zsl.swing.redis.desktop.common.ContextHolder;
import com.zsl.swing.redis.desktop.model.DataBaseEntity;
import com.zsl.swing.redis.desktop.tree.KeyTree;
import com.zsl.swing.redis.desktop.utils.DialogUtils;
import com.zsl.swing.redis.desktop.utils.JsonOutUtils;
import com.zsl.swing.redis.desktop.utils.RedisUtils;
import com.zsl.swing.redis.desktop.utils.StringUtils;

import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

/**
 * 
 * @author 张帅令
 * @description  日志信息上面面版
 *
 */
public class KeyPanel extends JPanel implements ActionListener{

	private static final long serialVersionUID = 1L;
	
	private static final String B1_STR = "查询{value}";
	private static final String B2_STR = "查询{keys}";
	private static final String B3_STR = "展示所有{keys}";
	private static final String B4_STR = "清空数据库";
	
	
	private static KeyTree keyTree = new KeyTree();
	
	private static JTextArea valueArea = new JTextArea();
	
	private static JTextField queryField = new JTextField(40);
	
	public KeyPanel() {
		this.setLayout(new BorderLayout());
		
		this.add(initNorthPanel(),BorderLayout.NORTH);
		
		this.add(this.initCenterPanel(),BorderLayout.CENTER);
		
		/*
		 * this.add(new JScrollPane(keyTree),BorderLayout.WEST);
		 * 
		 * this.add(new JScrollPane(valueArea),BorderLayout.CENTER);
		 */
	}
	
	private JSplitPane initCenterPanel() {
		JSplitPane target = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		target.setContinuousLayout(true);
		target.setOneTouchExpandable(false);
		target.setDividerSize(3);
		target.setDividerLocation(Constants.FRAME_W/5);
		
		target.setLeftComponent(new JScrollPane(keyTree));
		target.setRightComponent(new JScrollPane(valueArea));
		return target;
	}
	
	public void clearPanel() {
		queryField.setText(null);
		this.clearResult();
	}
	
	public void clearResult() {
		keyTree.clearNodes();
		valueArea.setText(null);
	}
	
	private JPanel initNorthPanel() {
		JPanel p = new JPanel();
		JLabel label = new JLabel("key:");
		
		JButton b1 = new JButton(B1_STR);
		JButton b2 = new JButton(B2_STR);
		JButton b3 = new JButton(B3_STR);
		JButton b4 = new JButton(B4_STR);
		
		b1.addActionListener(this);
		b2.addActionListener(this);
		b3.addActionListener(this);
		b4.addActionListener(this);
		
		p.add(label);
		p.add(queryField);
		p.add(b1);
		p.add(b2);
		p.add(b3);
		p.add(b4);
		return p;
	}
	
	public static void setValueText(String text) {
		if(!StringUtils.isEmpty(text)) {
			String target;
			try {
				target = JsonOutUtils.formatJson(JSON.parse(text).toString());
			}catch (@SuppressWarnings("unused") Exception e) {
				target = JsonOutUtils.formatJson(text);
			}
			valueArea.setText(target);
		}
	}
	
	public static String getQueryParam() {
		return queryField.getText();
	}
	
	public static void setQueryFieldText(String text) {
		queryField.setText(text);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		DataBaseEntity dbEntity = ContextHolder.getTree().getSelectedDbNode();
		if(dbEntity == null) {
			DialogUtils.msgDialog(this, "请先选择数据库！");
			return;
		}
		
		String command = e.getActionCommand();
		if(B3_STR.equals(command)) {
			this.clearPanel();
			keyTree.setOpType(Constants.OP_ALL);
			this.showAllKeys(dbEntity,Constants.REDIS_ALL_PATTERN);
		}else if(B4_STR.equals(command)){
			if(DialogUtils.warnDialog(this, "确定要清空所有数据么？")) {
				this.clearPanel();
				RedisUtils.flushDb(dbEntity.getUniqueId(),dbEntity.getDbIndex());
			}
		}else {
			String text = queryField.getText();
			if(StringUtils.isEmpty(text)) {
				return;
			}
			
			if(B1_STR.equals(command)) {
				keyTree.setOpType(Constants.OP_VALUE);
				this.showKeyValue(dbEntity, text);
			}else if(B2_STR.equals(command)) {
				this.clearResult();
				keyTree.setOpType(Constants.OP_KEYS);
				this.showAllKeys(dbEntity, text);
			}
		}
	}
	
	private void showAllKeys(DataBaseEntity dbEntity,String query) {
		ScanResult<String> keyResult = RedisUtils.scanDb(dbEntity.getUniqueId(), dbEntity.getDbIndex(), query,null);
		
		keyTree.addNodes(keyResult.getResult(), keyResult.getCursor());
	}
	
	private void showKeyValue(DataBaseEntity dbEntity,String text) {
		this.clearResult();
		String result = RedisUtils.get(dbEntity.getUniqueId(), dbEntity.getDbIndex(), text);
		
		if(!StringUtils.isEmpty(result)) {
			List<String> nodeList = new ArrayList<>(1);
			nodeList.add(text);
			keyTree.addNodes(nodeList, ScanParams.SCAN_POINTER_START);
			KeyPanel.setValueText(result);
		}
	}

}
