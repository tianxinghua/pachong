package com.shangpin.iog.channeladvisor.service.cxf;

import org.apache.cxf.binding.soap.SoapHeader;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.headers.Header;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.helpers.DOMUtils;
import org.apache.cxf.phase.Phase;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


import javax.xml.namespace.QName;
import java.util.List;

/**
 * Created by lizhongren on 2015/8/10.
 */
public class SoapHeaderUtil extends AbstractSoapInterceptor {

    public static final String xml_namespaceUR_att = "http://api.channeladvisor.com/webservices/";
    public static final String xml_header_el = "soapenv:Header";
    public static final String xml_authentication_el = "web:APICredentials";
    public static final String xml_DeveloperKey_el = "web:DeveloperKey";
    public static final String xml_Password_el = "web:Password";

    public SoapHeaderUtil() {
        super(Phase.WRITE);
    }

    public SoapHeaderUtil(String i, String p) {
        super(i, p);
    }

    @Override
    public void handleMessage(SoapMessage message) throws Fault {

        QName APICredentialsName = new QName("APICredentials");




        Document doc = (Document) DOMUtils.createDocument();
        Element root = doc.createElement(xml_header_el);


        Element eUserId = doc.createElement(xml_DeveloperKey_el);
        eUserId.setTextContent("537c99a8-e3d6-4788-9296-029420540832");
        Element ePwd = doc.createElement(xml_Password_el);
        ePwd.setTextContent("");
//        Element child = doc.createElementNS(xml_namespaceUR_att,xml_authentication_el);
        Element child = doc.createElement(xml_authentication_el);
        child.appendChild(eUserId);
        child.appendChild(ePwd);
        root.appendChild(child);
//        XMLUtils.printDOM(root);// 只是打印xml内容到控制台,可删除
        root.toString();






        SoapHeader head = new SoapHeader(APICredentialsName, root);

        List<Header> headers = message.getHeaders();
        headers.add(head);

    }
}
