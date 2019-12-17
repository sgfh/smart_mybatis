package com.smart.mybatis.annotation;

import java.lang.annotation.*;

/**
 * @Auther: gfh
 * @Date: 2019/3/25 19:36
 * @Description:数据库对应列名称
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Column {
    String value();
    /**字段描述*/
    String columnDefinition();
    /**字段注释*/
    String columnDesc() default "";
    boolean unique() default false;
    boolean isNull() default false;
    boolean index() default false;
}