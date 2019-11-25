package com.smart.mybatis.pojo;

import java.util.List;

/**
 * 更新
 */
public class Update {
    /**
     * 更新字段
     */
    private List<UpdateColumn> updateColumnList;

    /**
     * 限制条件
     */
    private List<UpdateColumn> limitColumnList;

    public List<UpdateColumn> getUpdateColumnList() {
        return updateColumnList;
    }

    public void setUpdateColumnList(List<UpdateColumn> updateColumnList) {
        this.updateColumnList = updateColumnList;
    }

    public List<UpdateColumn> getLimitColumnList() {
        return limitColumnList;
    }

    public void setLimitColumnList(List<UpdateColumn> limitColumnList) {
        this.limitColumnList = limitColumnList;
    }
}
