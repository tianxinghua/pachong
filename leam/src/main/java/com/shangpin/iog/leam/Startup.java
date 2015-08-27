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
        String tokenUrl = "http://188.226.153.91/modules/api/v2/getToken/";
        String url="http://188.226.153.91/modules/api/v2/stock/";
        String stockIdUrl="http://188.226.153.91/modules/api/v2/stock/id/";
        String user="shamping";
        String password="PA#=k2xU^ddUc6Jm";
        Map<String,String> param = new HashMap<>();
        OutTimeConfig outTimeConf = new OutTimeConfig(1000*60*10,1000*60*10,1000*60*10);
        param.put("user",user);
        param.put("password",password);//exp836shang*/
        //String result= HttpUtil45.get(tokenUrl, outTimeConf, param, "", "");
        //String token = HttpUtil45.post(tokenUrl,param,outTimeConf);
       // param.put("t","e1c3b10ca17299cfbdc8ed3ad2ea7bbd6781fe0d");
        String result=HttpUtil45.post(url+"?t=e1c3b10ca17299cfbdc8ed3ad2ea7bbd6781fe0d", param, outTimeConf);//e1c3b10ca17299cfbdc8ed3ad2ea7bbd6781fe0d
        //String result= HttpUtil45.get(url+"?t=e1c3b10ca17299cfbdc8ed3ad2ea7bbd6781fe0d"/*+"&limit=1"*/,outTimeConf,null);
        //System.out.println("token:"+token);
        System.out.println("result: "+result);
    }
}
