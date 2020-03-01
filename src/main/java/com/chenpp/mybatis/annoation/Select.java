package com.chenpp.mybatis.annoation;

import java.lang.annotation.*;

/**
 * 注解方法，配置SQL语句 查询
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Select {
    String value();
}
