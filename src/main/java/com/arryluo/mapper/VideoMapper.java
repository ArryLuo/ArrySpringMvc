package com.arryluo.mapper;

import org.apache.ibatis.annotations.Select;

import java.util.Map;
import java.util.List;

/**
 * Created by LUOZUBANG on 2017/8/26.
 */
public interface VideoMapper {
    @Select("SELECT * FROM vediomanager")
    List<Map<String,Object>>all();
}
