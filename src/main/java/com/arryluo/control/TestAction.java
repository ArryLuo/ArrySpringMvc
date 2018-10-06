package com.arryluo.control;

import com.arryluo.annotation.ArryController;
import com.arryluo.annotation.ArryRequestMapping;
import com.arryluo.annotation.ArryResponseBody;


/**
 * Created by LUOZUBANG on 2018/10/5.
 */
@ArryController
@ArryRequestMapping("/demo")
public class TestAction {
    @ArryRequestMapping("/test")
    @ArryResponseBody()
    public Object show(String name){
        //进行打印
        return name;
    }

}
