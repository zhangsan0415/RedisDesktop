package com.zsl.swing.redis.desktop.window;

import com.zsl.swing.redis.desktop.common.Constants;
import com.zsl.swing.redis.desktop.common.IconPaths;
import com.zsl.swing.redis.desktop.model.ConnectionEntity;
import com.zsl.swing.redis.desktop.utils.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.ConcurrentHashMap;

public class RedisConsoleWindow extends BaseWindow{

	private static final long serialVersionUID = 1L;

	private static final Image ICON_IMAGE = IconUtils.getScaleImage(IconPaths.REDIS_CONSOLE_ICON, 40, 40);
	
	private static final String CLEAR_COMMAND = "clear";

	private static final ConcurrentHashMap<Thread,String> map = new ConcurrentHashMap<>(32);
	
	private static String PREFIX_PATTERN = "%s{%s}[%s]>";
	
	private DocumentAction documentAction;

	private EnterKeyAction enterKeyAction;
	
	private ConnectionEntity connectionEntity;
	
	private JTextArea console = new JTextArea();
	
	private String tempText;
	
	public RedisConsoleWindow(ConnectionEntity connEntity) {
		super(connEntity.getShowName());
		this.connectionEntity = connEntity;

		String defaultPrefix = String.format(PREFIX_PATTERN, connEntity.getShowName(),connEntity.getHost(),"0");
		map.put(Thread.currentThread(),defaultPrefix);

		this.documentAction = new DocumentAction(this,this.console);
		this.enterKeyAction = new EnterKeyAction(this,this.console,this.documentAction,connEntity);

		this.setIconImage(ICON_IMAGE);
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
		console.getDocument().addDocumentListener(documentAction);
		console.addKeyListener(this.enterKeyAction);

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				RedisUtils.dbNumDefault();
				RedisConsoleWindow.this.dispose();
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
		console.append(map.get(Thread.currentThread()));
		console.requestFocus();
		
		this.tempText = console.getText();
		
	}
	
	private void nextBeginConnect() {
		console.setEditable(true);
		console.append(map.get(Thread.currentThread()));
		console.requestFocus();
		this.tempText = console.getText();
	}
	
	
	private class EnterKeyAction extends KeyAdapter{

		private RedisConsoleWindow redisConsoleWindow;

		private JTextArea redisConsole;

		private DocumentAction textDocumentAction;

		private ConnectionEntity entity;

		public EnterKeyAction(RedisConsoleWindow redisConsoleWindow,JTextArea redisConsole,DocumentAction textDocumentAction,ConnectionEntity entity){
			this.redisConsole = redisConsole;
			this.redisConsoleWindow = redisConsoleWindow;
			this.textDocumentAction = textDocumentAction;
			this.entity = entity;
		}
		
		@Override
		public void keyTyped(KeyEvent e) {
			if(e.getKeyChar() == KeyEvent.VK_ENTER) {
				try {
					this.redisConsole.setEditable(false);
					
					String text = this.redisConsole.getText();
					String target = text.substring(text.lastIndexOf(map.get(Thread.currentThread())) + map.get(Thread.currentThread()).length()).trim();
					
					if(StringUtils.isEmpty(target)) {
						this.redisConsoleWindow.nextBeginConnect();
						return;
					}
					
					if(CLEAR_COMMAND.equalsIgnoreCase(target)) {
						this.redisConsole.getDocument().removeDocumentListener(this.textDocumentAction);
						this.redisConsole.setText(null);
						this.redisConsoleWindow.nextBeginConnect();
						this.redisConsole.getDocument().addDocumentListener(this.textDocumentAction);
						return;
					}
					
					String result = RedisUtils.execute(target, this.entity.getUniqueId());

					this.redisConsole.append(JsonOutUtils.formatJson(result));
					this.redisConsoleWindow.beginConnect();
				} catch (Exception e1) {
					this.redisConsole.append(e1.getMessage());
					this.redisConsoleWindow.beginConnect();
				}
			}
		}
	}
	
	public static void setConnectPrefix(String showName, String host, String dbNum) {
		map.put(Thread.currentThread(),String.format(PREFIX_PATTERN, showName,host,dbNum));
	}
	
	private void clearConsole() {
		console.getDocument().removeDocumentListener(this.documentAction);
		console.setText(null);
		console.getDocument().addDocumentListener(this.documentAction);
	}
	
	private class DocumentAction implements DocumentListener{

		private RedisConsoleWindow consoleWindow;

		private JTextArea console;

		public DocumentAction(RedisConsoleWindow consoleWindow,JTextArea console){
			this.consoleWindow = consoleWindow;
			this.console = console;
		}
		
		@Override
		public void insertUpdate(DocumentEvent e) {
			this.removeUpdate(e);
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			String newText = this.console.getText();
			if(!newText.startsWith(this.consoleWindow.tempText)) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				
				new Thread(() -> {
					clearConsole();
					this.console.append(this.consoleWindow.tempText);
					this.console.requestFocus();
				}).start();
			}
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			
		}
		
	}

}
