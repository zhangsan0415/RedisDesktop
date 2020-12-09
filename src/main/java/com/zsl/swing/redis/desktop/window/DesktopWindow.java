package com.zsl.swing.redis.desktop.window;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import com.zsl.swing.redis.desktop.common.Constants;
import com.zsl.swing.redis.desktop.common.ContextHolder;
import com.zsl.swing.redis.desktop.utils.CommonUtils;
import com.zsl.swing.redis.desktop.utils.RedisUtils;

/**
 * 
 * @author 张帅令
 * @description  程序主窗口
 *
 */
public class DesktopWindow extends BaseWindow{

	private static final long serialVersionUID = 1L;
	
	private static final int DIVIDER_SIZE = 20;
	
	public DesktopWindow() {
		super("RedisDesktop","/image/desktop.png");
		
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
		splitPane.setDividerLocation(Constants.FRAME_W/8);
		
		JScrollPane left = new JScrollPane(ContextHolder.getTree());
		JSplitPane right = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		
		splitPane.setLeftComponent(left);
		splitPane.setRightComponent(right);
		
		setContentPane(splitPane);
		
		this.buildRightPane(right);

		setVisible(true);
		
		addWindowListener(new WindowAdapter() {
			
		    @Override
			public void windowClosing(WindowEvent e) {
		    	ContextHolder.getTree().saveConnectionsToFile();
		    }
			
		});
		
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
