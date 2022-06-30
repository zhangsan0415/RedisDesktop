package com.zsl.swing.redis.desktop.type;

public enum DbOptEnum {

    QUERY("精确查询"), LIKE("模糊查询"),DELETE_LIKE("模糊删除"),DELETE("删除"),VALUE_FORMAT_SHOW("格式化展示"),
    UPDATE("修改"),ADD("新增");

    private String text;

    DbOptEnum(String text){
        this.text = text;
    }

    public String getText(){
        return this.text;
    }
}
