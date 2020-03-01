package com.chenpp.mybatis.annoation;

import java.lang.annotation.*;

/**
 * 用于注解接口，用于在注解方式上映射返回的实体类
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Entity {
    Class<?> value();
}
