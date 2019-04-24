package com.smart.mybatis.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GroupBy implements Serializable {

    /**排序属性*/
    private String property;


    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public GroupBy(String property){
        this.property=property;
    }

    public static List<GroupBy> callList(GroupBy... groupBys){
        return new ArrayList<>(Arrays.asList(groupBys));
    }
}
