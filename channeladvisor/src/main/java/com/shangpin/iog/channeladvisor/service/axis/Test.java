package com.shangpin.iog.channeladvisor.service.axis;

import com.channeladvisor.api.webservices.LcvMagWSStub;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.ServiceClient;

/**
 * Created by lizhongren on 2016/2/29.
 */
public class Test {
    public static void main(String[] args){
        try {
            LcvMagWSStub stub = new  LcvMagWSStub();
            ServiceClient client =null;
            client = stub._getServiceClient();

        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        }


    }
}
