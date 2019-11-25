package com.smart.mybatis.pojo;


public class UpdateColumn {
    /**
     * 字段名称
     */
    private String field;

    /**
     * 字段值
     */
    private Object value;

    @Override
    public String toString() {
        return "UpdateColumn{" +
                "field='" + field + '\'' +
                ", value=" + value +
                '}';
    }

    public UpdateColumn() {
    }

    public UpdateColumn(String field, Object value) {
        this.field = field;
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
