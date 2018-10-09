package com.arryluo.c3p0;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by LUOZUBANG on 2018/10/8.
 */
public class SqlFactoryUtil {
    //读取mybatis的配置信息
    public static SqlSession getSqlSession() throws IOException {
        Reader reader = Resources.getResourceAsReader("mybatisconfig.xml");
        // 创建
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        // 解析资源
        SqlSessionFactory factory = builder.build(reader);
        // 2,用接口映射的形式进行查询，官方推荐
        // 打开session
        SqlSession session = factory.openSession();

        return session;
    }
}
