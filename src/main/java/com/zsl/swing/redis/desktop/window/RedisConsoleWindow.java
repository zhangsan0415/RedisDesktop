package com.zsl.swing.redis.desktop.window;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.zsl.swing.redis.desktop.common.Constants;
import com.zsl.swing.redis.desktop.common.IconPaths;
import com.zsl.swing.redis.desktop.model.ConnectionEntity;
import com.zsl.swing.redis.desktop.utils.CommonUtils;
import com.zsl.swing.redis.desktop.utils.IconUtils;
import com.zsl.swing.redis.desktop.utils.JsonOutUtils;
import com.zsl.swing.redis.desktop.utils.RedisUtils;
import com.zsl.swing.redis.desktop.utils.StringUtils;

public class RedisConsoleWindow extends BaseWindow{

	private static final long serialVersionUID = 1L;

	private static final Image ICON_IMAGE = IconUtils.getScaleImage(IconPaths.REDIS_CONSOLE_ICON, 40, 40);
	
	private static final String CLEAR_COMMAND = "clear";
	
	private static String CONNECTION_PREFIX;
	
	private ConnectionEntity connectionEntity;
	
	private JTextArea console = new JTextArea();
	
	private RedisConsoleWindow consoleWindow = this;
	
	public RedisConsoleWindow(ConnectionEntity connEntity) {
		super(connEntity.getShowName());
		CONNECTION_PREFIX = new StringBuilder(connEntity.getShowName()).append("{").append(connEntity.getHost()).append("}").append(">").toString();

		this.setIconImage(ICON_IMAGE);
		this.connectionEntity = connEntity;
		this.setSize(Constants.FRAME_W, Constants.FRAME_H);
		
		int x = CommonUtils.maxWidth()/2 - Constants.FRAME_W/2;
		int y = CommonUtils.maxHeight()/2 - Constants.FRAME_H/2;
		
		this.setLocation(x, y);
		this.setContentPane(new JScrollPane(console));
		
		this.initConsole();
		this.setVisible(true);

		console.setBackground(Color.CYAN);
		
		Font font = new Font("宋体", Font.BOLD, 14);
		console.setFont(font);
		console.setCaretColor(Color.WHITE);
		
		console.setLineWrap(true);
		console.addKeyListener(new EnterKeyAction());
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				consoleWindow.dispose();
			}
		});
	}
	
	private void initConsole() {
		console.setText(null);
		console.append("Connecting to ");
		console.append(connectionEntity.getHost());
		console.append("\t......\n");
		
		RedisUtils.addConnection(connectionEntity);
		boolean connect = RedisUtils.connect(connectionEntity.getUniqueId());
		console.append(connect?"Connected!":"Connect Failed!\n");
		
		if(connect) {
			this.beginConnect();
		}
	}
	
	private void beginConnect() {
		console.setEditable(true);
		console.append("\n");
		console.append(CONNECTION_PREFIX);
		console.requestFocus();
	}
	
	private void nextBeginConnect() {
		console.setEditable(true);
		console.append(CONNECTION_PREFIX);
		console.requestFocus();
	}
	
	
	private class EnterKeyAction extends KeyAdapter{
		
		@Override
		public void keyTyped(KeyEvent e) {
			if(e.getKeyChar() == KeyEvent.VK_ENTER) {
				try {
					console.setEditable(false);
					
					String text = console.getText();
					String target = text.substring(text.lastIndexOf(CONNECTION_PREFIX) + CONNECTION_PREFIX.length()).trim();
					
					if(StringUtils.isEmpty(target)) {
						consoleWindow.nextBeginConnect();
						return;
					}
					
					if(CLEAR_COMMAND.equalsIgnoreCase(target)) {
						console.setText(null);
						consoleWindow.nextBeginConnect();
						return;
					}
					
					String result = RedisUtils.execute(target, connectionEntity.getUniqueId());
					
					console.append(JsonOutUtils.formatJson(result));
					consoleWindow.beginConnect();
				} catch (Exception e1) {
					console.append(e1.getMessage());
					consoleWindow.beginConnect();
				}
			}
		}
	}

}
