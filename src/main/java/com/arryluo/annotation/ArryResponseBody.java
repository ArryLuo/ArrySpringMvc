package com.arryluo.annotation;

import java.lang.annotation.*;

/**
 * Created by LUOZUBANG on 2018/10/5.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ArryResponseBody {
    //这个主要是作用于方法上，区分返回值和界面跳转

}
