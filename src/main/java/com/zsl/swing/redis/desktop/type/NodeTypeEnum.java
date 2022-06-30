package com.zsl.swing.redis.desktop.type;

import com.zsl.swing.redis.desktop.common.Constants;
import com.zsl.swing.redis.desktop.common.IconPaths;
import com.zsl.swing.redis.desktop.utils.IconUtils;

import javax.swing.*;

public enum NodeTypeEnum {


    ROOT(0, IconUtils.getScaleImageIcon(IconPaths.SET_ICON, Constants.ICON_WIDTH, Constants.ICON_HEIGHT)),
    CONNECTION(1,IconUtils.getScaleImageIcon(IconPaths.CONN_ICON,  Constants.ICON_WIDTH, Constants.ICON_HEIGHT)),
    DB(2, IconUtils.getScaleImageIcon(IconPaths.DB_ICON, Constants.ICON_WIDTH, Constants.ICON_HEIGHT)),
    DB_KEY(3,IconUtils.getScaleImageIcon(IconPaths.DB_KEY_ICON,  Constants.ICON_WIDTH, Constants.ICON_HEIGHT)),
    DB_KEY_ROOT(4,IconUtils.getScaleImageIcon(IconPaths.DB_DIR_ICON, Constants.ICON_WIDTH, Constants.ICON_HEIGHT));

    private int type;

    private Icon icon;

    NodeTypeEnum(int type, Icon icon){
        this.type = type;
        this.icon = icon;
    }

    public int getType(){
        return this.type;
    }

    public Icon getIcon(){
        return this.icon;
    }

    public static boolean isRoot(NodeTypeEnum type){
        return type == ROOT;
    }

    public static boolean isServer(NodeTypeEnum type){
        return type == CONNECTION;
    }

    public boolean isDb(NodeTypeEnum type){
        return type == DB;
    }
}
