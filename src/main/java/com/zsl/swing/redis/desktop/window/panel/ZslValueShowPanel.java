package com.zsl.swing.redis.desktop.window.panel;

import com.zsl.swing.redis.desktop.model.NodeEntity;
import com.zsl.swing.redis.desktop.type.DbOptEnum;

import javax.swing.*;
import java.awt.*;

public class ZslValueShowPanel extends JPanel {

    private NodeEntity entity;

    private JLabel vLabel = new JLabel("值：");
    private JTextField vField = new JTextField(300);

    private JTextArea formatField = new JTextArea();

    private JButton addBtn = new JButton(DbOptEnum.ADD.getText());
    private JButton deleteBtn = new JButton(DbOptEnum.DELETE.getText());
    private JButton updateBtn = new JButton(DbOptEnum.UPDATE.getText());

    public ZslValueShowPanel(NodeEntity entity) {
        super();
        this.entity = entity;

        this.setLayout(new BorderLayout());
    }
}
