package com.smart.mybatis.annotation;

import java.lang.annotation.*;

/**
 * @Auther: gfh
 * @Date: 2019/3/25 19:36
 * @Description:数据库对应表名称
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Table {
    String value();
    //分表个数，默认是1个不分表
    int tableCount() default 1;
    //分表规则
    String rule() default "";
    //是否要建表
    boolean isCreate() default true;
}
