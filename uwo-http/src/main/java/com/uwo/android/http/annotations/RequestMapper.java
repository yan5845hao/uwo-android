package com.uwo.android.http.annotations;

import com.uwo.android.http.core.HttpMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by yanhao on 16/10/16.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface RequestMapper {

    String value() default "";

    HttpMethod method() default HttpMethod.GET;

}
