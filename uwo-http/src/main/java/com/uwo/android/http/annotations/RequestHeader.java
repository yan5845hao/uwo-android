package com.uwo.android.http.annotations;

import java.lang.annotation.*;

/**
 * 注解头部信息
 *
 * yanhao
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RequestHeader {

    String name();

    String value() default "";

}
