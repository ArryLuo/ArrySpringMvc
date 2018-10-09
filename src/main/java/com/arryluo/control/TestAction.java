package com.arryluo.control;

import com.arryluo.annotation.ArryAutowired;
import com.arryluo.annotation.ArryController;
import com.arryluo.annotation.ArryRequestMapping;
import com.arryluo.annotation.ArryResponseBody;
import com.arryluo.mapper.VideoMapper;
import com.arryluo.service.UserinfoService;


/**
 * Created by LUOZUBANG on 2018/10/5.
 */
@ArryController
@ArryRequestMapping("/demo")
public class TestAction {
    @ArryAutowired("UserinfoServiceImpl")
    private UserinfoService userinfoImplmm;
    @ArryRequestMapping("/test")
    @ArryResponseBody()
    public Object show(String name){
        //进行打印
        System.out.println(userinfoImplmm.all());
        return name;
    }

}
