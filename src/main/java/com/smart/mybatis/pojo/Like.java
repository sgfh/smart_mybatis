package com.smart.mybatis.pojo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 条件like
 */
public class Like {
    /**
     * 属性
     */
    private String property;

    /**值*/
    private Object value;

    public Like(String property,Object value){
        this.property=property;
        this.value=value;
    }

    public static List<Like> callList(Like... likes){
        return new ArrayList<>(Arrays.asList(likes));
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
