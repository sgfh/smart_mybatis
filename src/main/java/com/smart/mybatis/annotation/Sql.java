package com.smart.mybatis.annotation;

import java.lang.annotation.*;

/**
 * @Auther: gfh
 * @Date: 2019/3/25 19:36
 * @Description:动态sql
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Sql {
}
