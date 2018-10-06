package com.arryluo.annotation;

import java.lang.annotation.*;

/**
 * Created by LUOZUBANG on 2018/10/5.
 */
@Target({ElementType.TYPE,ElementType.METHOD})//既可以作用于类中，又可以作用于方法中
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ArryRequestMapping {
    //这个注解类主要是作用域url方面的请求
    String value() default "";
}
