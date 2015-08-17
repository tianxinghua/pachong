package com.shangpin.iog.leam;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sunny on 2015/8/17.
 */
public class Startup {
    private static ApplicationContext factory;
    private static void loadSpringContext()
    {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }

    public static void main(String[] args)
    {
        String stockUrl ="http://188.226.153.91/modules/api/v2/stock/id/W01590158/?t=e7962b1b6f8a02";
        String tokenUrl = "http://188.226.153.91/modules/api/v2/getToken/";
        Map<String,String> param = new HashMap<>();
        OutTimeConfig outTimeConf = new OutTimeConfig();
        param.put("user","shangpin");
        param.put("password","exp836shang");
        //String result= HttpUtil45.get(tokenUrl, outTimeConf, param, "", "");
        String result=HttpUtil45.post(tokenUrl, param, outTimeConf);
        System.out.println("token: "+result);
    }
}
