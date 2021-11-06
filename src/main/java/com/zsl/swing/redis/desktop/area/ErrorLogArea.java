package com.zsl.swing.redis.desktop.area;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;

import com.zsl.swing.redis.desktop.utils.StringUtils;

public class ErrorLogArea extends JTextArea implements ActionListener {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String B1_STR = "清空日志";

    private MouseListener mouseListener = new MouseAdapter() {

        @Override
        public void mouseClicked(MouseEvent e) {
            JPopupMenu popupMenu = new JPopupMenu();
            JMenuItem menuItem = new JMenuItem(B1_STR);
            menuItem.addActionListener(ErrorLogArea.this);
            popupMenu.add(menuItem);

            String errorLog = ErrorLogArea.this.getText();
            if(StringUtils.isEmpty(errorLog)){
                menuItem.setEnabled(false);
            }

            popupMenu.show(ErrorLogArea.this,e.getX(),e.getY());
        }
    };

    public ErrorLogArea(){
        super();
        this.setEditable(false);
        this.addMouseListener(mouseListener);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if(B1_STR.equals(command)){
            this.setText(null);
        }
    }
}
