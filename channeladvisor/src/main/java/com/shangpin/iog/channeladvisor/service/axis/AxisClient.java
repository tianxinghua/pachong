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
            clientStub = new AdminServiceStub(serviceAddress);
        }
        catch (AxisFault e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        //2、创建一个发送消息的请求消息体。
        AdminServiceStub.Ping ping = new

        //3、根据请求消息体，发送消息并获取响应。
        try
        {
            response = clientStub.ping();
        }
        catch (RemoteException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //4、如果获取的响应不为空，获取响应的字符串内容。
        if(response != null)
        {
            result = response.get_return();
        }

    }
}
