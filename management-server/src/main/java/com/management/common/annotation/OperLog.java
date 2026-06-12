package com.management.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Mark a controller method for automatic operation logging */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OperLog {

    /** Module name, e.g. "用户管理" */
    String module() default "";

    /** Operation type, e.g. "新增", "修改", "删除" */
    String operation() default "";
}
