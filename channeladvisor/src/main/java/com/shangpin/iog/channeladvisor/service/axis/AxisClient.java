package com.shangpin.iog.channeladvisor.service.axis;

import com.channeladvisor.api.webservices.AdminServiceStub;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.rpc.client.RPCServiceClient;

import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPHeaderElement;
import java.math.BigInteger;
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
        AdminServiceStub.RequestAccessResponse requestAccessResponse = null;
        AdminServiceStub.GetAuthorizationListResponse auListResponse =null;
        ServiceClient client =null;
        //1、根据服务地址，创建一个发送消息的客户端。
        try
        {
            clientStub = new AdminServiceStub();
            client = clientStub._getServiceClient();
            client.getOptions().setSoapVersionURI("http://schemas.xmlsoap.org/soap/envelope/");



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
            credentials.setPassword("ChannelAdvisor15");
            apiCredentialsE.setAPICredentials(credentials);
            AdminServiceStub.RequestAccess requestAccess  = new    AdminServiceStub.RequestAccess();
            requestAccess.setLocalID(12018111);

//            requestAccessResponse =clientStub.requestAccess(requestAccess, apiCredentialsE);

            AdminServiceStub.GetAuthorizationList getAuthorizationList;
            getAuthorizationList = new AdminServiceStub.GetAuthorizationList();
            getAuthorizationList.setLocalID(new BigInteger("12018111"));
             auListResponse =  clientStub.getAuthorizationList(getAuthorizationList, apiCredentialsE);

//            response = clientStub.ping(ping,apiCredentialsE);
//            AdminServiceStub.GetAuthorizationList getAuthorizationList = new   AdminServiceStub.GetAuthorizationList();
//            AdminServiceStub.GetAuthorizationListResponse authorizationListResponse = clientStub.getAuthorizationList(getAuthorizationList, apiCredentialsE);
        }
        catch (RemoteException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //4、如果获取的响应不为空，获取响应的字符串内容。
        if(auListResponse != null)
        {
            AdminServiceStub.APIResultOfArrayOfAuthorizationResponse apiArrayAuthorizationResonse= auListResponse.getGetAuthorizationListResult();
            AdminServiceStub.ArrayOfAuthorizationResponse arrayOfAuthorizationResponse  =  apiArrayAuthorizationResonse.getResultData();
            for (AdminServiceStub.AuthorizationResponse authorizationResponse : arrayOfAuthorizationResponse.getAuthorizationResponse()) {
                    System.out.println(" authorizationResponse.getAccountID() =" + authorizationResponse.getAccountID() + " authorizationResponse.getAccountName() " +
                    authorizationResponse.getAccountName() + " authorizationResponse.getAccountType() " + authorizationResponse.getAccountType() +
                            " authorizationResponse.getResourceName() = " + authorizationResponse.getResourceName()
                    + " authorizationResponse.getStatus() = " + authorizationResponse.getStatus() );
            }
            ;
        }

    }


    public static OMElement createHeaderOMElement(){
        OMFactory factory = OMAbstractFactory.getOMFactory();
        OMNamespace SecurityElementNamespace = factory.createOMNamespace("http://api.channeladvisor.com/webservices/","requestAccess");
        OMElement authenticationOM = factory.createOMElement("APICredentials",
                SecurityElementNamespace);
        OMElement usernameOM = factory.createOMElement("DeveloperKey",
                SecurityElementNamespace);
        OMElement passwordOM = factory.createOMElement("Password",
                SecurityElementNamespace);
        usernameOM.setText("537c99a8-e3d6-4788-9296-029420540832");
        passwordOM.setText("ChannelAdvisor15");  //      L1zhongren!
        authenticationOM.addChild(usernameOM);
        authenticationOM.addChild(passwordOM);
        return authenticationOM;
    }

    public static void main(String[] args){

        String url = "https://api.channeladvisor.com/ChannelAdvisorAPI/v7/AdminService.asmx?WSDL";

        Options options = new Options();
        // 指定调用WebService的URL
        EndpointReference targetEPR = new EndpointReference(url);
        options.setTo(targetEPR);
        options.setAction("web:Ping");

        ServiceClient client = null;
        try {
            client = new ServiceClient();
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        }
        client.setOptions(options);


        // 向Soap Header中添加校验信息
        client.addHeader(createHeaderOMElement());

        OMFactory fac = OMAbstractFactory.getOMFactory();
        String tns = "http://api.channeladvisor.com/webservices/";
        // 命名空间，有时命名空间不增加没事，不过最好加上，因为有时有事，你懂的
        OMNamespace omNs = fac.createOMNamespace(tns, "");

        OMElement method = fac.createOMElement("Ping", omNs);
//        OMElement symbol = fac.createOMElement("symbol", omNs);
//        symbol.addChild(fac.createOMText(symbol, "Axis2 Echo String "));
//        method.addChild(symbol);
        method.build();

        OMElement result = null;
        try {
            result = client.sendReceive(method);
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        }

        System.out.println(result);




    }
}
