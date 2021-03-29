package com.zsl.swing.redis.desktop.panel;

import com.alibaba.fastjson.JSON;
import com.zsl.swing.redis.desktop.common.Constants;
import com.zsl.swing.redis.desktop.common.ContextHolder;
import com.zsl.swing.redis.desktop.model.DataBaseEntity;
import com.zsl.swing.redis.desktop.tree.KeyTree;
import com.zsl.swing.redis.desktop.utils.*;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import static com.zsl.swing.redis.desktop.common.Constants.*;


/**
 * 
 * @author 张帅令
 * @description  日志信息上面面版
 *
 */
public class KeyPanel extends JPanel implements ActionListener{

	private static final long serialVersionUID = 1L;
	
	private static KeyTree keyTree = new KeyTree();
	
	private static JTextArea valueArea = new JTextArea();
	
	private static JTextField queryField = new JTextField(40);

	private static JComboBox<String> actionBox = new JComboBox<>();
	
	public KeyPanel() {
		actionBox.addItem(QUERY);
		actionBox.addItem(QUERY_MATCH);
		actionBox.addItem(DELETE);
		actionBox.addItem(DELETE_MATCH);

		this.setLayout(new BorderLayout());
		
		this.add(initNorthPanel(),BorderLayout.NORTH);
		
		this.add(this.initCenterPanel(),BorderLayout.CENTER);
	}
	
	private JSplitPane initCenterPanel() {
		valueArea.setEditable(false);
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

		actionBox.addActionListener(this);

		p.add(label);
		p.add(queryField);
		p.add(actionBox);

		JButton b1 = new JButton(QUERY_ALL);
		JButton b2 = new JButton(FLUSH_DB);

		b1.addActionListener(this);
		b2.addActionListener(this);

		p.add(b1);
		p.add(b2);

		p.setBorder(BorderFactory.createTitledBorder("操作"));
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
		if(QUERY_ALL.equals(command)){
			this.clearPanel();
			keyTree.setOpType(Constants.OP_ALL);
			this.showAllKeys(dbEntity,Constants.REDIS_ALL_PATTERN);
			return;
		}

		if(FLUSH_DB.equals(command)){
			if(DialogUtils.warnDialog(this, "确定要清空所有数据么？")) {
				this.clearPanel();
				RedisUtils.flushDb(dbEntity.getUniqueId(),dbEntity.getDbIndex());
			}
			return;
		}

		String selected = (String) actionBox.getSelectedItem();
		String text = queryField.getText();
		if(StringUtils.isEmpty(text)) {
			return;
		}

		text = text.trim();
		switch (selected){
			case QUERY:
				keyTree.setOpType(Constants.OP_VALUE);
				this.showKeyValue(dbEntity, text);
				break;
			case QUERY_MATCH:
				this.clearResult();
				keyTree.setOpType(Constants.OP_KEYS);
				this.showAllKeys(dbEntity, text);
				break;
			case DELETE:
				if(DialogUtils.warnDialog(this, "确定要删除么？")){
					keyTree.setOpType(Constants.OP_DEL);
					this.delKey(dbEntity,text);
				}
				break;
			case DELETE_MATCH:
				if(DialogUtils.warnDialog(this, "确定要删除么？")){
					keyTree.setOpType(Constants.OP_DEL);
					this.delKeyMatch(dbEntity,text);
				}
				break;
			default:
				break;

		}
	}

	private void delKeyMatch(DataBaseEntity dbEntity,String text){
		String uniqueId = dbEntity.getUniqueId();
		int dbIndex = dbEntity.getDbIndex();
		String[] keys = RedisUtils.keysMatch(uniqueId, dbIndex, text);
		if(!CollectionUtils.isEmpty(keys)){
			RedisUtils.delKeys(uniqueId,dbIndex,keys);
			valueArea.setText(null);
			for(String key:keys){
				keyTree.removeNodeByShowName(key);
			}
			DialogUtils.msgDialog(this, "删除成功！");
		}
	}
	
	private void delKey(DataBaseEntity dbEntity, String query) {
		boolean del = RedisUtils.del(dbEntity.getUniqueId(), dbEntity.getDbIndex(), query);
		if(del) {
			valueArea.setText(null);
			keyTree.removeNodeByShowName(query);
			DialogUtils.msgDialog(this, "删除成功！");
		}else {
			DialogUtils.errorDialog(this,"删除失败！");
		}
	}

	private void showAllKeys(DataBaseEntity dbEntity,String query) {
		ScanResult<String> keyResult = RedisUtils.scanDb(dbEntity.getUniqueId(), dbEntity.getDbIndex(), query,null);
		
		keyTree.addNodes(keyResult.getResult(), keyResult.getCursor());
	}
	
	private void showKeyValue(DataBaseEntity dbEntity,String query) {
		this.clearResult();
		String result = RedisUtils.get(dbEntity.getUniqueId(), dbEntity.getDbIndex(), query);
		
		if(!StringUtils.isEmpty(result)) {
			List<String> nodeList = new ArrayList<>(1);
			nodeList.add(query);
			keyTree.addNodes(nodeList, ScanParams.SCAN_POINTER_START);
			KeyPanel.setValueText(result);
		}
	}

}
