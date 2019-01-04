package com.shangpin.iog.channeladvisor.service.axis;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axiom.soap.SOAPHeaderBlock;
import org.apache.axis2.client.ServiceClient;


/**
 * Created by lizhongren on 2015/8/11.
 */
public class DealHeader extends AttachHeader {
    /**
     * 打包消息头。将设置到AttachHeader中的各字段的内容，打包到消息中发送出去。
     * @param serviceClient
     */
    public void packSoapHeader(ServiceClient serviceClient)
    {
        //获取创建工厂。
        OMFactory oMFactory = OMAbstractFactory.getOMFactory();
        SOAPFactory sOAPFactory = OMAbstractFactory.getSOAP11Factory();

        //利用工厂，创建命名空间和消息头。
        OMNamespace oMNamespace = oMFactory.createOMNamespace(NAMESPACE, NODEFLAG);
        SOAPHeaderBlock soapHeader =
                sOAPFactory.createSOAPHeaderBlock(HEADFLAG, oMNamespace);

        //消息头中的时间错节点。
        String timeStamp = (getTimeStamp() == null) ? "" : getTimeStamp();
        SOAPHeaderBlock timeBlock =
                sOAPFactory.createSOAPHeaderBlock(TIMESTAMP, oMNamespace);
        timeBlock.addChild(sOAPFactory.createOMText(timeStamp));

        //消息头中的业务表示节点。
        String serviceId = (getServiceId() == null) ? "" : getServiceId();
        SOAPHeaderBlock serviceIdBlock =
                sOAPFactory.createSOAPHeaderBlock(SERVICEID, oMNamespace);
        serviceIdBlock.addChild(sOAPFactory.createOMText(serviceId));

        //消息头中的业务校验密码节点。
        String servPassWord = (getServPassWord() == null) ? "" : getServPassWord();
        SOAPHeaderBlock servPassWordBlock =
                sOAPFactory.createSOAPHeaderBlock(SERVPASSWORD, oMNamespace);
        servPassWordBlock.addChild(sOAPFactory.createOMText(servPassWord));

        //将各个节点加入到消息头中。
        soapHeader.addChild(serviceIdBlock);
        soapHeader.addChild(servPassWordBlock);
        soapHeader.addChild(timeBlock);

        //将消息头加入到当前消息中。
        serviceClient.addHeader(soapHeader);
    }


}
