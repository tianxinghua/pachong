package com.shangpin.iog.channeladvisor.service.axis;

import com.channeladvisor.api.webservices.AdminServiceStub;
import org.apache.axis2.AxisFault;

import java.rmi.RemoteException;

/**
 * Created by lizhongren on 2015/8/11.
 */
public class AxisClient {

    /**
     * 声明客户端对象。
     */
    private AdminServiceStub clientStub ;

    /**
     * 服务地址。
     */
    private String serviceAddress;


    public AdminServiceStub getClientStub() {
        return clientStub;
    }

    public void setClientStub(AdminServiceStub clientStub) {
        this.clientStub = clientStub;
    }

    public String getServiceAddress() {
        return serviceAddress;
    }

    public void setServiceAddress(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }


    public void ping()
    {
        String result = null;
        AdminServiceStub.PingResponse response = null;

        //1、根据服务地址，创建一个发送消息的客户端。
        try
        {
            clientStub = new AdminServiceStub();
        }
        catch (AxisFault e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        //2、创建一个发送消息的请求消息体。


        //3、根据请求消息体，发送消息并获取响应。
        try
        {
            AdminServiceStub.Ping ping =new AdminServiceStub.Ping();
            AdminServiceStub.APICredentialsE apiCredentialsE = new AdminServiceStub.APICredentialsE();
            AdminServiceStub.APICredentials credentials = new  AdminServiceStub.APICredentials();
            credentials.setDeveloperKey("537c99a8-e3d6-4788-9296-029420540832");
            credentials.setPassword("L1zhongren!");
            apiCredentialsE.setAPICredentials(credentials);
            response = clientStub.ping(ping,apiCredentialsE);
//            AdminServiceStub.GetAuthorizationList getAuthorizationList = new   AdminServiceStub.GetAuthorizationList();
//            AdminServiceStub.GetAuthorizationListResponse authorizationListResponse = clientStub.getAuthorizationList(getAuthorizationList, apiCredentialsE);
        }
        catch (RemoteException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //4、如果获取的响应不为空，获取响应的字符串内容。
        if(response != null)
        {
            AdminServiceStub.APIResultOfString apiResultOfString= response.getPingResult();
        }

    }
}
