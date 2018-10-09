package com.arryluo.util;

/**
 * Created by LUOZUBANG on 2018/10/9.
 */

import com.alibaba.druid.util.StringUtils;

/**
 * 处理DispatcherServlet中的工具类
 */
public class DispatcherServletUtil {
    /**
     * 处理实现类的父级类的名称,默认是一个
     * @param tClass
     * @return
     */
    public static String  getFatherPath(Class<?extends Object>tClass){
        Class[] classes= tClass.getInterfaces();
        String result="";
        for (Class cla:
             classes) {
                result=cla.getSimpleName();
        }
            if(StringUtils.isEmpty(result)){
                result=tClass.getSimpleName();
            }
        return result;
    }

}
