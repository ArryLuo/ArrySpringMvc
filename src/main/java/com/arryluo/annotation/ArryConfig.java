package com.arryluo.annotation;

import java.lang.annotation.*;

/**
 * Created by LUOZUBANG on 2018/10/8.
 * 专门加载配置文件的
 */
@Target(ElementType.TYPE)//Target注解,TYPE:用于描述类、接口(包括注解类型) 或enum声明
@Retention(RetentionPolicy.RUNTIME)//RetentionPolicy.RUNTIME —— 这种类型的Annotations将被JVM保留,所以他们能在运行时被JVM或其他使用反射机制的代码所读取和使用.
@Documented//@Documented,则它会被 javadoc 之类的工具处理, 所以注解类型信息也会被包括在生成的文档中
public @interface ArryConfig {

}
