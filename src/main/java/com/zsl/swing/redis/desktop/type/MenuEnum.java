package com.zsl.swing.redis.desktop.type;

import com.zsl.swing.redis.desktop.window.ZslRedisDesktopMainWindow;

import java.util.Arrays;

public enum MenuEnum {
    ADD("添加"),UPDATE("修改"),READ("查看"),DELETE("删除");

    private String text;

    MenuEnum(String text){
        this.text = text;
    }

    public String getText(){
        return this.text;
    }

    public static void process( String menuName){
        Arrays.stream(values()).filter(m -> m.getText().equals(menuName)).findFirst().ifPresent(m -> {
            ZslRedisDesktopMainWindow.getZslErrorLogPanel().log(m.getText() + "操作开始-------》");
            ZslRedisDesktopMainWindow.getZslConnectionPanel().doAction(m);
            ZslRedisDesktopMainWindow.getZslErrorLogPanel().log(m.getText() + "操作失败-----》");
        });
    }
}
