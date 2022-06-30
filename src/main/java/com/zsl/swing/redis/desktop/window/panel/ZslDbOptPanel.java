package com.zsl.swing.redis.desktop.window.panel;

import com.zsl.swing.redis.desktop.model.NodeEntity;
import com.zsl.swing.redis.desktop.type.DbOptEnum;

import javax.swing.*;
import java.awt.*;

/**
 * 操作面版
 */
public class ZslDbOptPanel extends JSplitPane {

    /**
     * 数据库连接信息
     */
    private NodeEntity entity;

    private ZslKeyQueryPanel zslKeyQueryPanel;

    private ZslValueShowPanel rightPanel;

    public static ZslDbOptPanel getInstance(NodeEntity entity){
        ZslDbOptPanel instance = new ZslDbOptPanel(entity);
        instance.setDividerLocation(0.4);
        return instance;
    }

    private ZslDbOptPanel(NodeEntity entity){
        super();
        this.entity = entity;
        this.rightPanel = new ZslValueShowPanel(this.entity);
        this.zslKeyQueryPanel = new ZslKeyQueryPanel(this.rightPanel,this.entity);

        this.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        this.setContinuousLayout(true);
        this.setOneTouchExpandable(true);
        this.setLeftComponent(this.zslKeyQueryPanel);
        this.setRightComponent(this.rightPanel);
        this.setDividerSize(10);
        this.setVisible(true);

    }



    public void add(Component f,JPanel t, GridBagConstraints constraints, int x, int y, int w,int h) {
        constraints.gridheight = h;
        constraints.gridwidth = w;
        constraints.gridx = x;
        constraints.gridy = y;
        t.add(f,constraints);
    }
}
