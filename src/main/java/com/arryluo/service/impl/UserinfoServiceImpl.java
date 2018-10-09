package com.arryluo.service.impl;

import com.arryluo.annotation.ArryAutowired;
import com.arryluo.annotation.ArryService;
import com.arryluo.mapper.VideoMapper;
import com.arryluo.service.UserinfoService;

import java.util.List;
import java.util.Map;

/**
 * Created by LUOZUBANG on 2018/10/7.
 */
@ArryService
public class UserinfoServiceImpl implements UserinfoService {
    @ArryAutowired("VideoMapper")
    private VideoMapper videoMapper;
    @Override
    public void show() {
        System.out.println("显示");
    }

    @Override
    public List<Map<String, Object>> all() {
        return videoMapper.all();
    }
}
