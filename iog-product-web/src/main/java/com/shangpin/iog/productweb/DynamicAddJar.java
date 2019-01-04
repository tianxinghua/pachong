package com.shangpin.iog.productweb;

import com.shangpin.iog.app.AppContext;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;

/**
 * Created by lizhongren on 2016/3/29.
 */
public class DynamicAddJar {

    private static ApplicationContext context;
    private static void loadSpringContext()
    {
        context = new AnnotationConfigApplicationContext(AppContext.class);
    }


    public void addJar(){
        //加载jar
        JarClassLoader loader = new JarClassLoader();

//设置当前线程的classLoader
        Thread.currentThread().setContextClassLoader(loader);

//注册bean
        loadSpringContext();
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner((DefaultListableBeanFactory)
                context.getAutowireCapableBeanFactory());//使用Spring的注解自动扫描
//扫描jar中的包路径，使用通配符，另外在导出jar时必须选择add directory entries（即把目录也加入到jar中）
//否则spring扫描时将无法找到class
        scanner.scan("com.test.moduleA.*");

//手动拿jar包里的bean的实例时因为存在ClassLoader的隔离虽然在开始设置了ContextClassLoader
//但是Spring默认getBean的时候并没有每次都去拿最新的ContextClassLoader使用，所以需要手动设置Bean的ClassLoader
//因为是手动设置的这里存在线程安全的问题...不知道有没有其他方法.
        DefaultListableBeanFactory factory = (DefaultListableBeanFactory)context.getAutowireCapableBeanFactory();
        factory.setBeanClassLoader(loader);

        System.out.println(context.getBean("questionDaoImpl"));
    }
}
