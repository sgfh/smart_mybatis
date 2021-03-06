package com.smart.mybatis.annotation;

import java.lang.annotation.*;

/**
 * @Auther: gfh
 * @Date: 2019/3/25 19:36
 * @Description:一对多
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OneToOne {
    String value();
    /**防止自己调用自己，此时table名称会出现错乱，这里加一个参数*/
    String tableAlias() default "";
}
