package com.smart.mybatis.pojo;

import java.io.Serializable;

/**
 * count计算字段
 */
public class CountField implements Serializable {
    public static final String DEFAULT = "*";
    //不计算重复的
    public static final String DISTINCT = "DISTINCT ";
    private String field;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public CountField() {
    }

    public CountField(String field) {
        this.field = field;
    }
}
