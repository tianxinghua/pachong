package com.shangpin.ep.order.module.orderapiservice.impl;

import com.shangpin.ep.order.conf.supplier.SupplierProperties;
import com.shangpin.ep.order.module.order.bean.OrderDTO;
import com.shangpin.ep.order.util.httpclient.HttpUtil45;
import com.shangpin.ep.order.util.httpclient.OutTimeConfig;
import com.shangpin.ep.order.util.xml.ObjectXMLUtil;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lizhongren on 2017/2/25.
 */
@Component
public class ParisiOrderUtil  {

    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    private static OutTimeConfig defaultConfig = new OutTimeConfig(1000 * 2, 1000 * 60*5, 1000 * 60*5);
    private String hostUrl = null;
    private String createOrderUrl = null;
    private String cancelOrderUrl = null;
    private String confirmOrderUrl = null;
    private String deleteOrderUrl = null;
    private String stockUrl = null;
    private String strKey = null;
    @Autowired
    SupplierProperties supplierProperties;

    @PostConstruct
    public void init(){
        hostUrl = supplierProperties.getParisi().getHostUrl();
        stockUrl = supplierProperties.getParisi().getStockUrl();
        createOrderUrl = supplierProperties.getParisi().getSetOrderUrl();
        cancelOrderUrl = supplierProperties.getParisi().getCancelOrderUrl();
        confirmOrderUrl = supplierProperties.getParisi().getConfirmOrderUrl();
        deleteOrderUrl = supplierProperties.getParisi().getDeleteOrderUrl();
        strKey = supplierProperties.getParisi().getStrKey();
    }

    public boolean pushOrder(OrderDTO orderDTO,String sku, String  quantity){

            String request="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                    "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                    "  <soap:Body>\n" +
                    "    <PutOrderWithSku xmlns=\"http://tempuri.org/\">\n" +
                    "      <order_no>string</order_no>\n" +
                    "      <sku>"+sku + "</sku>\n" +
                    "      <quantity>"+quantity+"</quantity>\n" +
                    "      <strKey>"+strKey+"</strKey>\n" +
                    "    </PutOrderWithSku>\n" +
                    "  </soap:Body>\n" +
                    "</soap:Envelope>";
            String result = "";
            try {
                Map<String,String> headerMap = new HashMap<>();
                headerMap.put("SOAPAction",createOrderUrl);

                result = HttpUtil45.operateData("post","soap",hostUrl,
                        new OutTimeConfig(1000*60,1000*60*30,1000*60*30),null,request,headerMap,"","");
                orderDTO.setLogContent("推送订单返回结果="+result+"推送的订单="+request);
                return analyzeData(result);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;


    }

    private boolean analyzeData(String xmlContent){
        SAXReader reader = new SAXReader();
        InputStream in_withcode =null;
        try {
            in_withcode = new ByteArrayInputStream(xmlContent.getBytes("UTF-8"));
            Document document =reader.read(in_withcode);
            Element root = document.getRootElement();


            Element  productElement = root.element("Body").element("PutOrderWithSkuResponse ")
                    .element("PutOrderWithSkuResult").element("diffgram")
                    .element("DocumentElement").element("ns_product").element("Stock");
            String xml =  productElement.asXML();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;


    }


}
