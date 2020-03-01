package com.chenpp.mybatis.plugin;

import java.lang.annotation.*;

/**
 * 2020/2/29
 * created by chenpp
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Intercepts {
    Signature[] value();
}

