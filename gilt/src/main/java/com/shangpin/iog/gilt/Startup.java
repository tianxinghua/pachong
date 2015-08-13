package com.shangpin.iog.gilt;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.gilt.service.FetchProduct;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by sunny on 2015/8/5.
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
        FetchProduct fetchProductService =(FetchProduct)factory.getBean("gilt");
        System.out.println("-------gilt start---------");
        try {
            fetchProductService.fetchProductAndSave("https://api-sandbox.gilt.com/global/skus");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("成功插入数据库");
        System.out.println("-------gilt end---------");

    }

}
