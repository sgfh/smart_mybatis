package com.smart.mybatis.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * 条件like
 */
public class Like {
    /**
     * 属性
     */
    private String property;

    /**
     * 值
     */
    private Object value;

    public Like(String property, Object value) {
        this.property = property;
        if (value == null)
            this.value = "";
        else
            this.value = "'" + value + "'";
    }

    public static List<Like> callList(Like... likes) {
        List<Like> list = new ArrayList<>();
        for (Like like : likes) {
            if (!"".equals(like.getValue()))
                list.add(like);
        }
        return list;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }


    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}