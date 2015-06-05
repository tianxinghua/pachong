package com.shangpin.iog.webcontainer.front.conf.interceptor;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.apennine.utils.ApennineHttpUtil;
import com.shangpin.iog.webcontainer.front.conf.AppContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by sunny on 2015/6/5.
 */
public class ApennineTest {
    /*private static ApplicationContext factory;
    private static void loadSpringContext()
    {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }
    public static void main(String[] args)  {
        System.out.println("test start");
        loadSpringContext();
        ApennineHttpUtil httpService = new ApennineHttpUtil();
        try {
            httpService.insertApennineProducts("http://112.74.74.98:8082/api/GetProductDetails?userName=spin&userPwd=spin112233");
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        System.out.println("test end");
    }*/
}
