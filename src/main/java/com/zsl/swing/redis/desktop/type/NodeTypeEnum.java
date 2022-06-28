package com.zsl.swing.redis.desktop.type;

public enum NodeTypeEnum {
    ROOT(0),CONNECTION(1),DB(2);

    private int type;

    NodeTypeEnum(int type){
        this.type = type;
    }

    public int getType(){
        return this.type;
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
