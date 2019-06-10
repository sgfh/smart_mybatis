package com.smart.mybatis.pojo;

import java.io.Serializable;

public class Compare implements Serializable {
    /**
     * 比较类型
     */
    private String type;

    /**
     * 被比较的属性
     */
    private String property;

    /**
     * 比较的运算符号
     */
    private String symbol;

    /**
     * 传入的动态值
     */
    private Object value;

    public Compare(CompareType type, String property, Symbol symbol, Object value) {
        this.type = type.toString();
        this.value = value;
        this.symbol = symbol.getLabel();
        if (type == CompareType.date) {
            this.property = "TO_DAYS(" + property + ")";
            this.value = "TO_DAYS('" + value + "')";
        } else if (type == CompareType.time) {
            this.property = property;
            this.value = "'" + value + "'";
        } else if (type == CompareType.string) {
            this.property = property;
            this.value = "'" + value + "'";
        } else
            this.property = property;
    }

    public static void main(String args[]) {
        //  new Compare("", "", Symbol.ge.toString(), new Date());
    }

    /**
     * 运算符号
     */
    public enum Symbol {

        ge(">"), greaterThanOrEqualTo(">="), le("<"), lessThanOrEqualTo("<="), eq("="), ne("!=");

        private String label;

        Symbol(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }
    }

    /**
     * 比较类型
     */
    public enum CompareType {

        date("日期比较"), time("时间比较"), number("数字比较"), string("文字比较");

        private String label;

        CompareType(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Compare() {

    }


}
