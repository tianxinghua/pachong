package com.shangpin.iog.channeladvisor.service;

import com.channeladvisor.api.webservices.*;
import org.apache.cxf.frontend.ClientProxyFactoryBean;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

import javax.xml.namespace.QName;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lizhongren on 2015/8/10.
 */
public class Test {


    public final static QName SERVICE = new QName("http://api.channeladvisor.com/webservices/", "AdminService");
    public static void main(String[] args) {

        SoapHeaderUtil ash = new SoapHeaderUtil();
        List list = new ArrayList();
        // 添加soap header 信息
        list.add(ash);

        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
//        ClientProxyFactoryBean factory = new ClientProxyFactoryBean();
        factory.setOutInterceptors(list);
        factory.setServiceClass(AdminServiceSoap.class);

        factory.setAddress("https://api.channeladvisor.com/ChannelAdvisorAPI/v7/AdminService.asmx");


        AdminServiceSoap service = (AdminServiceSoap) factory.create();

        APIResultOfString resultOfString = service.ping();
        System.out.println(" " + resultOfString.getResultData());

//        try {
//            AdminService service = new AdminService();
//
//            ObjectFactory objectFactory = new ObjectFactory();
//            APICredentials credentials = new APICredentials();
//            credentials.setDeveloperKey("537c99a8-e3d6-4788-9296-029420540832");
//            credentials.setPassword("");
//            objectFactory.createAPICredentials(credentials);
//
//            AdminServiceSoap adminServiceSoap =   service.getAdminServiceSoap();
//
//
//            APIResultOfString apiResultOfString =  adminServiceSoap.ping();
//            System.out.println(" " + apiResultOfString.getResultData());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }
}
