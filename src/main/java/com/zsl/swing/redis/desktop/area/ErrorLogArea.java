package com.zsl.swing.redis.desktop.area;

import com.zsl.swing.redis.desktop.utils.StringUtils;

import javax.swing.*;
import java.awt.event.*;

public class ErrorLogArea extends JTextArea implements ActionListener {

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
