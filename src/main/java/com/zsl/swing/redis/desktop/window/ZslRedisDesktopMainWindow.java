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
import com.zsl.swing.redis.desktop.window.menu.ZslMenuBar;
import com.zsl.swing.redis.desktop.window.panel.ZslConnectionPanel;
import com.zsl.swing.redis.desktop.window.panel.ZslErrorLogPanel;
import com.zsl.swing.redis.desktop.window.panel.ZslShowPanel;

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
public class ZslRedisDesktopMainWindow extends BaseWindow{

	private static final long serialVersionUID = 1L;

	/**
	 * 菜单栏
	 */
	private static ZslMenuBar zslMenuBar = new ZslMenuBar();

	/**
	 * 连接面版
	 */
	private static ZslConnectionPanel zslConnectionPanel = new ZslConnectionPanel();

	/**
	 * 错误日志面版
	 */
	private static ZslErrorLogPanel zslErrorLogPanel = new ZslErrorLogPanel();

	/**
	 * 数据展示面版
	 */
	private static ZslShowPanel zslShowPanel = new ZslShowPanel();



	public ZslRedisDesktopMainWindow() {
		super("RedisDesktop", IconPaths.DESKTOP_ICON);

		//设置窗口大小
		setSize(Constants.MAIN_WINDOW_WIDTH, Constants.MAIN_WINDOW_HEIGHT);

		//计算窗口位置
		int x = CommonUtils.maxWidth()/2 - Constants.MAIN_WINDOW_WIDTH /2;
		int y = CommonUtils.maxHeight()/2 - Constants.MAIN_WINDOW_HEIGHT /2;
		setLocation(x,y);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//设置菜单
		this.setJMenuBar(zslMenuBar);


		//连接面版
		JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		mainSplitPane.setContinuousLayout(true);
		mainSplitPane.setOneTouchExpandable(true);
		mainSplitPane.setDividerSize(Constants.MAIN_WINDOW_DIVIDER_SIZE);
		mainSplitPane.setDividerLocation(Constants.MAIN_WINDOW_WIDTH /7);


		mainSplitPane.setLeftComponent(zslConnectionPanel);
		JSplitPane right = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		right.setContinuousLayout(true);
		right.setOneTouchExpandable(true);
		right.setDividerSize(Constants.MAIN_WINDOW_DIVIDER_SIZE);
		right.setDividerLocation(Constants.MAIN_WINDOW_HEIGHT *6/8);

		right.setTopComponent(zslShowPanel);

//		JScrollPane logPane = new JScrollPane(ContextHolder.getLogArea());
//		logPane.setBorder(BorderFactory.createTitledBorder("错误日志"));
		right.setBottomComponent(zslErrorLogPanel);

		mainSplitPane.setRightComponent(right);

//		this.setContentPane(splitPane);

//		JToolBar toolBar = this.buildToolBar();
//		this.getContentPane().add(toolBar,BorderLayout.NORTH);
//		this.getContentPane().add(mainSplitPane,BorderLayout.CENTER);

//		this.buildRightPane(right);

		setVisible(true);
		
		addWindowListener(new WindowAdapter() {
			
		    @Override
			public void windowClosing(WindowEvent e) {
		    	ContextHolder.getTree().saveConnectionsToFile();
		    }
			
		});
		
	}

	public static ZslMenuBar getZslMenuBar(){
		return zslMenuBar;
	}

	public static ZslConnectionPanel getZslConnectionPanel(){
		return zslConnectionPanel;
	}

	public static ZslErrorLogPanel getZslErrorLogPanel(){
		return zslErrorLogPanel;
	}

	public static ZslShowPanel getZslShowPanel(){
		return zslShowPanel;
	}
//	private JToolBar buildToolBar(){
//		JToolBar toolBar = new JToolBar();
//
//		JButton create = ButtonUtils.createNewToolBar();
//		create.addActionListener(event -> new BuildConnectionDialog());
//
//		JButton delete = ButtonUtils.deleteToolBar();
//		delete.addActionListener(new DeleteConnectionAction(this));
//
//		JButton detail = ButtonUtils.detailToolBar();
//		detail.addActionListener(new ConnectionDetailAction(this));
//
//		JButton console = ButtonUtils.consoleToolBar();
//		console.addActionListener(new ConsoleOpenAction(this));
//
//		JButton connect = ButtonUtils.connectToolBar();
//		connect.addActionListener(new ConnectServerAction(this));
//
//		toolBar.add(create);
//		toolBar.add(detail);
//		toolBar.add(connect);
//		toolBar.add(console);
//		toolBar.add(delete);
//
//		return toolBar;
//	}
//
//	private void buildRightPane(JSplitPane root) {
//
//	}
	

}
