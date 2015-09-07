package com.shangpin.iog.channeladvisor.service;

import com.channeladvisor.api.webservices.*;
import com.shangpin.iog.channeladvisor.service.axis.AxisClient;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import org.apache.cxf.frontend.ClientProxyFactoryBean;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

import javax.xml.namespace.QName;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lizhongren on 2015/8/10.
 */
public class Test {


    public final static QName SERVICE = new QName("http://api.channeladvisor.com/webservices/", "AdminService");
    public static void main(String[] args) {

//

//        AxisClient client = new AxisClient();
//        client.ping();

        Map<String,String> map = new HashMap<>();
        map.put("client_id","qwmmx12wu7ug39a97uter3dz29jbij3j");
        map.put("grant_type","soap");
        map.put("scope","orders inventory");
        map.put("developer_key","537c99a8-e3d6-4788-9296-029420540832");
        map.put("password","ChannelAdvisor15");
        map.put("account_id","12018111");

        String s =  (new sun.misc.BASE64Encoder()).encode( "qwmmx12wu7ug39a97uter3dz29jbij3j-:TqMSdN6-LkCFA0n7g7DWuQ".getBytes() );

        System.out.println("s = " + s);
        String kk = HttpUtil45.postAuth("https://api.channeladvisor.com/oauth2/token", map, new OutTimeConfig(),
                s, "");

//        String  kk = (new sun.misc.BASE64Encoder()).encode( "12345:abcde".getBytes());
                System.out.println("kk = "  + kk);



    }
}
