package com.zsl.swing.redis.desktop.type;

import java.util.Arrays;
import java.util.Optional;

public enum RedisTypeEnum {
    NONE("none"),STRING("string"),LIST("list"),SET("set"),ZSET("zset"),HASH("hash");

    private String type;

    RedisTypeEnum(String type){
        this.type = type;
    }

    public String getType(){
        return this.type;
    }

    public static boolean isRedisType(String type){
        Optional<RedisTypeEnum> any = Arrays.stream(values()).filter(e -> e.getType().equals(type)).findAny();

        return any.isPresent() && any.get() != NONE;
    }
}
