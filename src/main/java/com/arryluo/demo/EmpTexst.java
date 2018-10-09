package com.arryluo.demo;


import com.arryluo.mapper.VideoMapper;
import com.arryluo.service.impl.UserinfoServiceImpl;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;

/**
 * Created by LUOZUBANG on 2018/10/8.
 */
public class EmpTexst {
    public static void main(String[] args) {


        try {
            t4();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void t4() throws IOException, ClassNotFoundException {

            Reader reader = Resources.getResourceAsReader("mybatisconfig.xml");
            // 创建
            SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
            // 解析资源
            SqlSessionFactory factory = builder.build(reader);
     		// 2,用接口映射的形式进行查询，官方推荐
            // 打开session
            SqlSession session = factory.openSession();
         // 1传统写法，不推荐
		/*
			 * List<Emp> list =
			 * * session.selectList("com.shandian.mapper.empmapper.queryall"); for
			 * (Emp emp : list) { System.out.println(emp); }
			 *
*/

             Class   cla = Class.forName("com.arryluo.mapper.VideoMapper");

                Object videoMapper= session.getMapper(cla);
              VideoMapper videoMapperobj= (VideoMapper) videoMapper;
        System.out.println(videoMapperobj.all());


          /*  Class cla=   VideoMapper.class;
            Class<?>interfaces[]= cla.getInterfaces();
            for (Class<?> inte : interfaces) {//打印
                //System.out.println("Dog实现接口："+inte);
                *//*CB videoMapper= session.getMapper(CB.class);
                System.out.println(videoMapper.all());*//*
            }*/
// 2,用接口映射的形式进行查询，官方推荐


    }

}
