package com.uwo.android.http.annotations;

import java.lang.annotation.*;

/**
 * 注解参数
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RequestParam {

    String name();

    String value() default "";



} 