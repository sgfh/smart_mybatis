package com.smart.mybatis.pojo;

import java.io.Serializable;

/**
 * 表属性
 */
public class TableField implements Serializable {
    /**
     * 行名称
     */
    private String columnName;

    /**
     * 类型
     */
    private String columnType;

    /**字段默认值*/
    private String columnDefault;

    /**是否可空--返回YES或者NO*/
    private String isNullable;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    @Override
    public String toString() {
        return "TableField{" +
                "columnName='" + columnName + '\'' +
                ", columnType='" + columnType + '\'' +
                '}';
    }

    public String getColumnDefault() {
        return columnDefault;
    }

    public void setColumnDefault(String columnDefault) {
        this.columnDefault = columnDefault;
    }

    public String getIsNullable() {
        return isNullable;
    }

    public void setIsNullable(String isNullable) {
        this.isNullable = isNullable;
    }
}
