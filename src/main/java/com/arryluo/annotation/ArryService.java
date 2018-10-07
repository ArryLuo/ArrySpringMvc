package com.arryluo.annotation;

import java.lang.annotation.*;

/**
 * Created by LUOZUBANG on 2018/10/7.
 //service端实现依赖注入
 */
@Target(ElementType.TYPE)// 指定直接可作用在接口、类、枚举、注解上
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ArryService {
    /**
     * 给service起别名
     * @return
     */
    String value() default "";
}
