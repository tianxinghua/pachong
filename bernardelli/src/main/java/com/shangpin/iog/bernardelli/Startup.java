package com.shangpin.iog.bernardelli;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.bernardelli.service.FetchProduct;

/**
 * Created by houkun on 2016.01.25.
 */
public class Startup {
    private static ApplicationContext factory;
    private static void loadSpringContext()
    {

        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }

    public static void main(String[] args) {

        //加载spring

        loadSpringContext();
        //拉取数据
        FetchProduct fetchService = (FetchProduct) factory.getBean("bernardelli");
        System.out.println("-------bernardelli start---------");
        try {
            fetchService.fetchProductAndSave();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("成功插入数据库");

        System.out.println("-------bernardelli end---------");
    }
}
