package com.shangpin.iog.channeladvisor.service.axis;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;

import com.channeladvisor.api.webservices.AdminServiceStub;
import org.apache.axis2.databinding.types.soapencoding.QName;
import org.apache.axis2.rpc.client.RPCServiceClient;
//import com.channeladvisor.api.webservices.LcvMagWSStub;
//import com.channeladvisor.api.webservices.LcvMagWSStub.TLectureDesModelesAvecPrix;
//import com.channeladvisor.api.webservices.LcvMagWSStub.TLectureDesTables;
//import com.channeladvisor.api.webservices.LcvMagWSStub.TLectureDesTablesResponse;

/**
 * Created by lizhongren on 2016/2/29.
 */
public class Test {
    public static void main(String[] args) throws InstantiationException, IllegalAccessException, RemoteException{
//        try {
            
        	
            
//            com.channeladvisor.api.webservices.LcvMagWSStub stub = new com.channeladvisor.api.webservices.LcvMagWSStub(); //the default implementation should point to the right endpoint
//            ServiceClient client =null;
//            //1、根据服务地址，创建一个发送消息的客户端。
//                client = stub._getServiceClient();
//                client.getOptions().setSoapVersionURI("http://www.w3.org/2003/05/soap-envelope");
//            com.channeladvisor.api.webservices.LcvMagWSStub.LectureDesTables LectureDesTables =
//                (com.channeladvisor.api.webservices.LcvMagWSStub.LectureDesTables) com.channeladvisor.api.webservices.LcvMagWSStub.LectureDesTables.class.newInstance();
//            // TODO : Fill in the lectureDesModelesAvecPrix56 here
//            TLectureDesTables tl = new TLectureDesTables();
//            tl.setSMdp("INI123");
//            tl.setSUser("BION456");
//            tl.setBCData("false");
//            LectureDesTables.setLectureDesTables(tl);
//            
//            com.channeladvisor.api.webservices.LcvMagWSStub.LectureDesTablesResponse respo = stub.lectureDesTables(LectureDesTables);
//            
//            TLectureDesTablesResponse ll = respo.getLectureDesTablesResponse();
            
//            System.out.println(ll.getLectureDesTablesResult());
            
            
//        } catch (AxisFault axisFault) {
//            axisFault.printStackTrace();
//        }


//        //  使用RPC方式调用WebService
//        RPCServiceClient serviceClient = new RPCServiceClient();
//        Options options = serviceClient.getOptions();
//        //  指定调用WebService的URL
//        EndpointReference targetEPR = new EndpointReference(
//                "http://studio69.atelier98.net/api_studio69/api_studio69.asmx");
//        options.setTo(targetEPR);
//        //  指定sayHelloToPerson方法的参数值
//        Object[] opAddEntryArgs = new Object[] {"美女"};
//        //  指定sayHelloToPerson方法返回值的数据类型的Class对象
//        Class[] classes = new Class[] {String.class};
//        //  指定要调用的sayHelloToPerson方法及WSDL文件的命名空间
//        QName opAddEntry = new QName("http://ws.apache.org/axis2", "sayHelloToPerson");
//        //  调用sayHelloToPerson方法并输出该方法的返回值
//        System.out.println(serviceClient.invokeBlocking(opAddEntry, opAddEntryArgs, classes)[0]);


    }
}
