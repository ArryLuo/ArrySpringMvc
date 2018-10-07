package com.arryluo.annotation;

import java.lang.annotation.*;

/**
 * Created by LUOZUBANG on 2018/10/7.
 */
@Target(value = {ElementType.ANNOTATION_TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ArryAutowired {
    String value() default "";
}
