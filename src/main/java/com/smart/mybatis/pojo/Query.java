package com.smart.mybatis.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**查询条件，主要用于副表条件限定*/
public class Query implements Serializable {
    /**
     * 属性
     */
    private String property;

    /**值*/
    private Object value;

    public Query(String property,Object value){
        this.property=property;
        this.value=value;
    }

    public static List<Query> callList(Query... likes){
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
