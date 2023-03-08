package com.ift.mybatis.annotation;

import java.lang.annotation.*;

/**
 * @author liufei
 */
@Documented
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
public @interface DataScope {
    /**
     * 模块代码
     * @return 模块代码
     */
    String value();
}
