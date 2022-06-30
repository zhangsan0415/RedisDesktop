package com.zsl.swing.redis.desktop.model;

import com.zsl.swing.redis.desktop.type.RedisTypeEnum;
import com.zsl.swing.redis.desktop.utils.StringUtils;

public class KeyValueEntity {

    private String key;

    private String value;

    private String type;

    private long expire;

    private double score;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isOk(){
        return RedisTypeEnum.isRedisType(this.type) && !StringUtils.isEmpty(this.key) && !StringUtils.isEmpty(this.value);
    }
}
