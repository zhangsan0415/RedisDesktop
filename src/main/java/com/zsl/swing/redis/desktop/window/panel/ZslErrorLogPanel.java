package com.zsl.swing.redis.desktop.window.panel;

import com.zsl.swing.redis.desktop.utils.StringUtils;
import com.zsl.swing.redis.desktop.window.ZslRedisDesktopMainWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 日志面版
 */
public class ZslErrorLogPanel extends JScrollPane  implements ActionListener {

    /**
     * 日志记录文本域
     */
    private static JTextArea logArea = new JTextArea();

    private static final String B1_STR = "清空日志";

    private static final String B2_STR = "复制";

    private static final String LOG_FORMAT = "ZslRedisDesktop:%s:>%s";

    private static final String ERROR_LEVEL = "ERROR";
    private static final String INFO__LEVEL = "INFO ";

    public ZslErrorLogPanel(){
        super(logArea);
        logArea.setEditable(false);
        logArea.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                JMenuItem clearItem = new JMenuItem(B1_STR);
                clearItem.addActionListener(ZslErrorLogPanel.this);
                JMenuItem copyItem = new JMenuItem(B2_STR);
                copyItem.addActionListener(ZslErrorLogPanel.this);

                JPopupMenu popupMenu = new JPopupMenu();
                popupMenu.add(clearItem);
                popupMenu.add(copyItem);

                String errorLog = logArea.getText();
                if(StringUtils.isEmpty(errorLog)){
                    clearItem.setEnabled(false);
                    copyItem.setEnabled(false);
                }

                popupMenu.show(logArea,e.getX(),e.getY());
            }
        });
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if(B1_STR.equals(command)){
            logArea.setText(null);
        }else if(B2_STR.equals(command)){
            logArea.copy();
        }
    }

    public void logError(Throwable e) {
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));

        logArea.append(String.format(LOG_FORMAT,ERROR_LEVEL, stringWriter));
        logArea.append("\n");
    }

    public void logError(String msg){
        logArea.append(String.format(LOG_FORMAT,ERROR_LEVEL, msg));
        logArea.append("\n");
    }
    public void log(String msg){
        logArea.append(String.format(LOG_FORMAT,INFO__LEVEL, msg));
        logArea.append("\n");
    }

}
