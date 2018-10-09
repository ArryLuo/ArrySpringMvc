package com.arryluo.servlet;

import com.arryluo.annotation.ArryAutowired;
import com.arryluo.annotation.ArryController;
import com.arryluo.annotation.ArryRequestMapping;
import com.arryluo.annotation.ArryService;

import com.arryluo.c3p0.SqlFactoryUtil;

import com.arryluo.util.DispatcherServletUtil;
import com.google.gson.Gson;
import org.apache.ibatis.session.SqlSession;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
/**
 * Created by LUOZUBANG on 2018/10/5.
 */

/**
 *对所有的请求进行一个控制
 */
public class ArryDispatcherServlet extends HttpServlet {
    //进行初始化httpServlet的生命周期
    //实现控制器的第一步是要做的就是加载配置文件
    private Properties properties = new Properties();
    //装扫描后的类
    private List<String>classNames=new ArrayList<>();
    //进行对实例化放入到ioc的容器中
    private Map<String ,Object>ioc=new HashMap<String,Object>();
    //
    private Map<String, Method> handlerMapping = new HashMap<>();

    private Map<String, Object> controllerMap = new HashMap<>();
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try {
        //1.加载配置文件
        doLoadConfig(config);
        //2，扫描配置下的包
        String packageName= properties.getProperty("scanPackage");

            doScanning(packageName);

        //3，得到扫描后的文件之后，进行实例化扫描后的文件
        doInstance();//拿到实例
        //4，进行依赖注入
            doIOC();
        //5，进行HandlerMapping,将控制层中URL,和method进行拼接
        doHandlerMapping();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void doIOC() throws Exception {
        if(ioc.isEmpty()){
            throw  new Exception("没有可以依赖注入的");
        }
        for (Map.Entry<String, Object> entry:ioc.entrySet()) {
            Field[] fields = entry.getValue().getClass().getDeclaredFields();
            // 遍历bean对象的字段
            for (Field field : fields) {
                   if(field.isAnnotationPresent(ArryAutowired.class)){
                       field.setAccessible(true);
                       //注释掉了由之前的在注解上传入的值进行获取，改成了直接通过反射获取父级的名称
                       //String className=field.getType().getName();
                      // System.out.println(className);
                     /*  String name=field.getType().getSimpleName();
                       System.out.println(name);*/
                     // String name= field.getAnnotation(ArryAutowired.class).value();
                       // 注入实例,
                     String name=  DispatcherServletUtil.getFatherPath(field.getType());
                     // Object obj= ioc.get(toLowerFirstWord(name));
                       System.out.println(entry.getValue().toString());
                      Object iocobj= ioc.get(toLowerFirstWord(name));
                      if(iocobj!=null){
                          field.set(entry.getValue(),iocobj);
                      }else{
                          //这个就是加载mybatis的文件

                      }


                }

            }
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       // super.doPost(req, resp);
       try {
           doDispatch(req, resp);
       }catch (Exception e){
           resp.getWriter().write("500!! Server Exception");
       }

    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if(handlerMapping.isEmpty()){
            return ;
        }
        String url = req.getRequestURI();
        String contextPath = req.getContextPath();
        //拼接url并把多个/替换成一个
        url = url.replace(contextPath, "").replaceAll("/+", "/");
        if (!this.handlerMapping.containsKey(url)) {
            resp.getWriter().write("404 NOT FOUND!");
            return;
        }
       Method method=  this.handlerMapping.get(url);
        //获取方法的参数列表
       Class<?extends Object>[]parameterTypes=  method.getParameterTypes();
       //获取请求参数
       Map<String,String[]>parameterMap=req.getParameterMap();
        //保存参数值
        Object[] paramValues = new Object[parameterTypes.length];
        Map<String,Object>objectMap=new HashMap<>();//这个专门存储基本类型的
        int count=0;//这个是记录有多少除基本数据类型之外的数据集,防止下面计算出现越界
        //方法的参数列表
        for (int i = 0; i < parameterTypes.length; i++) {
            //根据参数名称，做某些处理
            String requestParam = parameterTypes[i].getSimpleName();
            if (requestParam.equals("HttpServletRequest")) {
                //参数类型已明确，这边强转类型
                paramValues[i] = req;
                count++;
                continue;
            }
            if (requestParam.equals("HttpServletResponse")) {
                paramValues[i] = resp;
                count++;
                continue;
            }
            objectMap.put(i-count+"",requestParam);

        }
        Set<String> set = parameterMap.keySet();
        Iterator<String> it = set.iterator();
        String[]data=new String[set.size()];
        int py=0;
        while (it.hasNext()) {
            String[] str = parameterMap.get(it.next());
            data[py]=str[0];
            py++;
        }
        for (int j = 0; j <data.length ; j++) {
            Object oj= objectMap.get(j+"");
            if (oj.equals("Integer")) {
                paramValues[j+count] = Integer.parseInt(data[j]);
            } else {
                paramValues[j+count] = data[j];

            }
        }
        //利用反射机制来调用
        try {
            Object object=   method.invoke(this.controllerMap.get(url), paramValues);//obj是method所对应的实例 在ioc容器中
            resp.setContentType("text/html;charset=utf-8");

            resp. setCharacterEncoding("UTF-8");
            if(object==null){
                resp.getWriter().write( "doTest method success! param:参数为空!");
                return;
            }
            try {
                //引入json，实现对象json化
                Gson gson=new Gson();
               String json= gson.toJson(object);
                resp.getWriter().write( json);//进行打印到控制台中
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doHandlerMapping() {
        //判断ioc容器中是否有值
        try {
            if (ioc.isEmpty()) {
                return;
            }
            for (Map.Entry<String, Object> entry : ioc.entrySet()) {
                Class<? extends Object> cla = entry.getValue().getClass();
                //判断类是否加了ArryController这个注解,加了才算是控制层
                if (!cla.isAnnotationPresent(ArryController.class)) {
                    continue;
                }
                //进行拼接url
                String baseUrl = "";
                //取出父级的url
                if (cla.isAnnotationPresent(ArryRequestMapping.class)) {
                    ArryRequestMapping arryRequestMapping = cla.getAnnotation(ArryRequestMapping.class);
                    //取出注解的值
                    baseUrl = arryRequestMapping.value();
                }
                //从而在取出该类下所有的方法的路径
                Method[] methods = cla.getMethods();
                for (Method method : methods) {
                    //判断该方法是要进行请求的方法
                    if (!method.isAnnotationPresent(ArryRequestMapping.class)) {
                        continue;
                    }
                    //取出第二级路径
                    ArryRequestMapping arryRequestMapping = method.getAnnotation(ArryRequestMapping.class);

                /* //在方法上有了这个，还要判断是否是跳转界面的还是返回值的一种处理
                boolean isOutWrite=false;
                if(method.isAnnotationPresent(ArryResponseBody.class)){
                 //有的话就进行返回值
                    isOutWrite=true;
                }
                if(isOutWrite){
                    //
                }*/
                    String url = arryRequestMapping.value();
                    url = (baseUrl + "/" + url).replaceAll("/+", "/");
                    handlerMapping.put(url, method);//装载方法
                    controllerMap.put(url, entry.getValue());//再维护一个只存储controller实例的map

                }
            }
        }
       catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doInstance() throws IOException {
        //判断是否组装的存在数据
        if(classNames.isEmpty()){
            return;
        }
        //初始化mybatis的信息
       SqlSession sqlSession= SqlFactoryUtil.getSqlSession();
        //开始实例化
        for(String className:classNames){
            //进行反射实例化
            try {
               Class<?extends Object> cla= Class.forName(className);
                //获取父级名称
                String pathName= DispatcherServletUtil.getFatherPath(cla);
                String mybatisPack= properties.getProperty("scanmybatisPackage");
               //判断下哪些可以被实例化
                if(cla.isAnnotationPresent(ArryController.class)){
                    Object object= cla.newInstance();//获取的实例化对象
                    //将实例化的对象放入到ioc容器中
                    ioc.put(toLowerFirstWord(pathName),object);
                }else if(cla.isAnnotationPresent(ArryService.class)){
                    //userinfoImpl
                    Object object= cla.newInstance();//获取的实例化对象
                    ioc.put(toLowerFirstWord(pathName),object);
                }else if(cla.getName().indexOf(mybatisPack)!=-1){
                    //这个是获取mybatis的实例的
                    Object videoMapper= sqlSession.getMapper(cla);//拿到mybatis的实例
                    ioc.put(toLowerFirstWord(pathName),videoMapper);

                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
        //进行sql提交
       sqlSession.commit();

    }



    public static String toLowerFirstWord(String simpleName) {
        char[] charArray = simpleName.toCharArray();
        charArray[0] += 32;
        return String.valueOf(charArray);
    }
    private void doScanning(String packageName) {
        //取出配置文件中要扫描的包

       //拿到这个包
       //URL url= this.getClass().getClassLoader().getResource("/"+packageName);
        URL url = this.getClass().getClassLoader().getResource("/" + packageName.replaceAll("\\.", "/"));

        //通过url找到对应的文件
        File dirfiles=new File(url.getFile());
        for (File file: dirfiles.listFiles()) {
            //首先判断该配置是指定的是包，还是指定的是具体的文件
            if(file.isDirectory()){
                //是就要进入递归找出文件
                doScanning(packageName+"."+file.getName());
            }else{
                //就将扫描到的文件装到一个集合中
                String className = packageName + "." + file.getName().replace(".class", "");
                classNames.add(className);//将符合的文件装入进来
            }
        }

    }

    private void doLoadConfig(ServletConfig config) {
        String location=config.getInitParameter("contextConfigLocation");
        //把web.xml中的contextConfigLocation对应value值的文件加载到留里面
        InputStream inputStream= this.getClass().getClassLoader().getResourceAsStream(location);
        //用Properties文件加载文件里的内容
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(inputStream!=null){
                try {
                    //关闭流
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
