package com.smart.mybatis.annotation;

import java.lang.annotation.*;

/**
 * @Auther: gfh
 * @Date: 2019/3/25 19:36
 * @Description:数据库对应主键id
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Id {
    String value();
    String columnDefinition();
}
