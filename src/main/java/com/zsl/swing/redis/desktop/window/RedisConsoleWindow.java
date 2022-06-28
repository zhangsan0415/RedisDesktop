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

	private static final String CLEAR_COMMAND = "clear";

	private static final ConcurrentHashMap<Thread,Integer> dbIndexMap = new ConcurrentHashMap<>(32);

	private static final String PREFIX_PATTERN = "%s{%s}[%s]>";

	private final JTextArea console = new JTextArea();

	private final DocumentAction documentAction = new DocumentAction();

	private final EnterKeyAction enterKeyAction = new EnterKeyAction();
	
	private final ConnectionEntity connectionEntity;

	private String tempText;

	private String prefix;

	public RedisConsoleWindow(ConnectionEntity connEntity) {
		super(connEntity.getShowName(),IconPaths.REDIS_CONSOLE_ICON);
		this.connectionEntity = connEntity;

		this.setSize(Constants.MAIN_WINDOW_WIDTH, Constants.MAIN_WINDOW_HEIGHT);
		
		int x = CommonUtils.maxWidth()/2 - Constants.MAIN_WINDOW_WIDTH /2;
		int y = CommonUtils.maxHeight()/2 - Constants.MAIN_WINDOW_HEIGHT /2;
		
		this.setLocation(x, y);
		this.setContentPane(new JScrollPane(console));

		this.initConsole();
		this.setVisible(true);

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				RedisConsoleWindow.this.dispose();
			}
		});
	}
	
	private void initConsole() {
		console.setBackground(Color.CYAN);
		console.setCaretColor(Color.WHITE);
		console.setFont(FontUtils.defaultFont());
		console.setLineWrap(true);
		console.setText(null);
		console.append("Connecting to ");
		console.append(connectionEntity.getHost());
		console.append("\t......\n");
		
		RedisUtils.addConnection(connectionEntity);
		boolean connect = RedisUtils.connect(connectionEntity.getUniqueId());
		console.append(connect?"Connected!":"Connect Failed!\n");

		dbIndexMap.put(Thread.currentThread(),0);
		if(connect) {
			this.buildPrefix();
			this.beginConnect();
			console.getDocument().addDocumentListener(this.documentAction);
			console.addKeyListener(this.enterKeyAction);
		}

	}

	private void buildPrefix(){
		this.prefix = String.format(PREFIX_PATTERN, connectionEntity.getShowName(),connectionEntity.getHost(),dbIndexMap.get(Thread.currentThread()));
	}
	
	private void beginConnect() {
		this.buildPrefix();
		console.setEditable(true);
		console.append("\n");
		console.append(this.prefix);
		console.requestFocus();
		
		this.tempText = console.getText();
		
	}
	
	private void nextBeginConnect() {
		console.setEditable(true);
		console.append(this.prefix);
		console.requestFocus();
		this.tempText = console.getText();
	}
	
	
	private class EnterKeyAction extends KeyAdapter{

		@Override
		public void keyTyped(KeyEvent e) {
			if(e.getKeyChar() == KeyEvent.VK_ENTER) {
				try {
					RedisConsoleWindow.this.console.setEditable(false);
					
					String text = RedisConsoleWindow.this.console.getText();
					String prefix = RedisConsoleWindow.this.prefix;
					String target = text.substring(text.lastIndexOf(prefix) + prefix.length()).trim();
					
					if(StringUtils.isEmpty(target)) {
						RedisConsoleWindow.this.nextBeginConnect();
						return;
					}
					
					if(CLEAR_COMMAND.equalsIgnoreCase(target)) {
						RedisConsoleWindow.this.console.getDocument().removeDocumentListener(RedisConsoleWindow.this.documentAction);
						RedisConsoleWindow.this.console.setText(null);
						RedisConsoleWindow.this.nextBeginConnect();
						RedisConsoleWindow.this.console.getDocument().addDocumentListener(RedisConsoleWindow.this.documentAction);
						return;
					}
					
					String result = RedisUtils.execute(target, RedisConsoleWindow.this.connectionEntity.getUniqueId(),dbIndexMap.get(Thread.currentThread()));

					RedisConsoleWindow.this.console.append(JsonOutUtils.formatJson(result));
					RedisConsoleWindow.this.beginConnect();
				} catch (Exception e1) {
					RedisConsoleWindow.this.console.append(e1.getMessage());
					RedisConsoleWindow.this.beginConnect();
				}
			}
		}
	}

	public static void putDbIndex(Integer dbIndex){
		dbIndexMap.put(Thread.currentThread(),dbIndex);
	}

	public void clearConsole() {
		console.getDocument().removeDocumentListener(this.documentAction);
		console.setText(null);
		console.getDocument().addDocumentListener(this.documentAction);
	}
	
	private class DocumentAction implements DocumentListener{

		@Override
		public void insertUpdate(DocumentEvent e) {
			this.removeUpdate(e);
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			String newText = RedisConsoleWindow.this.console.getText();
			if(!newText.startsWith(RedisConsoleWindow.this.tempText)) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException ignored) {
				}
				
				new Thread(() -> {
					clearConsole();
					RedisConsoleWindow.this.console.append(RedisConsoleWindow.this.tempText);
					RedisConsoleWindow.this.console.requestFocus();
				}).start();
			}
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			
		}
		
	}

}
