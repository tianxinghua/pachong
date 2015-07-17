package com.shangpin.iog.brunarosso;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.brunarosso.service.FetchProduct;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by sunny on 2015/7/10.
 */
public class Startup {
    private static ApplicationContext factory;
    private static void loadSpringContext()
    {

        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }

    public static void main(String[] args)
    {
        //加载spring

        loadSpringContext();
        //拉取数据
        FetchProduct fetchService = (FetchProduct)factory.getBean("brunarosso");
        System.out.println("-------brunarosso start---------");
        try {
            fetchService.fetchProductAndSave("");
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("成功插入数据库");
        System.out.println("-------brunarosso end---------");
    }
}
