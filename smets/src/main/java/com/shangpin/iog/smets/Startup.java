package com.shangpin.iog.smets;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.smets.util.MyCrawler;

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
        MyCrawler fetchService = (MyCrawler) factory.getBean("mycrawler");
        System.out.println("-------bernardelli start---------");
        try {
        	String[] feeds = new String[]{"www.farfetch.com/cn/shopping/women/smets/items.aspx?q=smets","http://www.farfetch.com/cn/shopping/men/smets/items.aspx?q=smets"};
            fetchService.crawling(feeds);
//        	fetchService.test("/cn/shopping/women/smets/bags-purses-1/items.aspx|包袋");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("成功插入数据库");

        System.out.println("-------bernardelli end---------");
    }
}
