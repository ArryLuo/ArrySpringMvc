package com.arryluo.service.impl;

import com.arryluo.annotation.ArryService;
import com.arryluo.service.UserinfoService;

/**
 * Created by LUOZUBANG on 2018/10/7.
 */
@ArryService
public class UserinfoServiceImpl implements UserinfoService {
    @Override
    public void show() {
        System.out.println("显示");
    }
}
