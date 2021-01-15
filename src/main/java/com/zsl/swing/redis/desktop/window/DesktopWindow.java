package com.zsl.swing.redis.desktop.window;

import com.zsl.swing.redis.desktop.action.ConnectServerAction;
import com.zsl.swing.redis.desktop.action.ConnectionDetailAction;
import com.zsl.swing.redis.desktop.action.ConsoleOpenAction;
import com.zsl.swing.redis.desktop.action.DeleteConnectionAction;
import com.zsl.swing.redis.desktop.common.Constants;
import com.zsl.swing.redis.desktop.common.ContextHolder;
import com.zsl.swing.redis.desktop.common.IconPaths;
import com.zsl.swing.redis.desktop.menu.BuildConnectionDialog;
import com.zsl.swing.redis.desktop.utils.ButtonUtils;
import com.zsl.swing.redis.desktop.utils.CommonUtils;
import com.zsl.swing.redis.desktop.utils.IconUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * 
 * @author 张帅令
 * @description  程序主窗口
 *
 */
public class DesktopWindow extends BaseWindow{

	private static final long serialVersionUID = 1L;
	
	private static final int DIVIDER_SIZE = 10;
	
	public DesktopWindow() {
		super("RedisDesktop", IconPaths.DESKTOP_ICON);
		
		setSize(Constants.FRAME_W, Constants.FRAME_H);
		
		int x = CommonUtils.maxWidth()/2 - Constants.FRAME_W/2;
		int y = CommonUtils.maxHeight()/2 - Constants.FRAME_H/2;
		setLocation(x,y);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//连接面版
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setContinuousLayout(true);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerSize(DIVIDER_SIZE);
		splitPane.setDividerLocation(Constants.FRAME_W/7);

		JScrollPane left = new JScrollPane(ContextHolder.getTree());
		JSplitPane right = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		
		splitPane.setLeftComponent(left);
		splitPane.setRightComponent(right);

//		this.setContentPane(splitPane);

		JToolBar toolBar = this.buildToolBar();
		this.getContentPane().add(toolBar,BorderLayout.NORTH);
		this.getContentPane().add(splitPane,BorderLayout.CENTER);

		this.buildRightPane(right);

		setVisible(true);
		
		addWindowListener(new WindowAdapter() {
			
		    @Override
			public void windowClosing(WindowEvent e) {
		    	ContextHolder.getTree().saveConnectionsToFile();
		    }
			
		});
		
	}

	private JToolBar buildToolBar(){
		JToolBar toolBar = new JToolBar();

		JButton create = ButtonUtils.createNewToolBar();
		create.addActionListener(event -> new BuildConnectionDialog());

		JButton delete = ButtonUtils.deleteToolBar();
		delete.addActionListener(new DeleteConnectionAction(this));

		JButton detail = ButtonUtils.detailToolBar();
		detail.addActionListener(new ConnectionDetailAction(this));

		JButton console = ButtonUtils.consoleToolBar();
		console.addActionListener(new ConsoleOpenAction(this));

		JButton connect = ButtonUtils.connectToolBar();
		connect.addActionListener(new ConnectServerAction(this));

		toolBar.add(create);
		toolBar.add(detail);
		toolBar.add(connect);
		toolBar.add(console);
		toolBar.add(delete);

		return toolBar;
	}

	private void buildRightPane(JSplitPane root) {
		root.setContinuousLayout(true);
		root.setOneTouchExpandable(true);
		root.setDividerSize(DIVIDER_SIZE);
		root.setDividerLocation(Constants.FRAME_H*5/8);
		
		root.setTopComponent(ContextHolder.getKeyPanel());
		
		JScrollPane logPane = new JScrollPane(ContextHolder.getLogArea());
		logPane.setBorder(BorderFactory.createTitledBorder("错误日志"));
		root.setBottomComponent(logPane);
	}
	

}
