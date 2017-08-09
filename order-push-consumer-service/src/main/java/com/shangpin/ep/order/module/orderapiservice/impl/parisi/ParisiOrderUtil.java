package com.shangpin.ep.order.module.orderapiservice.impl.parisi;

import com.shangpin.ep.order.conf.supplier.SupplierProperties;
import com.shangpin.ep.order.module.order.bean.OrderDTO;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.parisi.OrderOfSupplier;
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

    public boolean pushOrder(String sku, String  quantity){

        String request="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
                "  <soap12:Body>\n" +
                "    <PutOrderWithSku xmlns=\"http://tempuri.org/\">\n" +
                "      <order_no>201703010001</order_no>\n" +
                "      <sku>18753-4-OS</sku>\n" +
                "      <quantity>1</quantity>\n" +
                "      <strKey>YX123HF</strKey>\n" +
                "    </PutOrderWithSku>\n" +
                "  </soap12:Body>\n" +
                "</soap12:Envelope>";
        String result = "";
        try {
            Map<String,String> headerMap = new HashMap<>();
            headerMap.put("SOAPAction","http://tempuri.org/PutOrderWithSku");

            result = HttpUtil45.operateData("post","soap",hostUrl,
                    new OutTimeConfig(1000*60,1000*60*30,1000*60*30),null,request,headerMap,"","");
            return analyzePushData(result);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;


    }



    public boolean cancelOrder(String sku, String  quantity){

        String request="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
                "  <soap12:Body>\n" +
                "    <DeleteOrder xmlns=\"http://tempuri.org/\">\n" +
                "      <order_no>201703010001</order_no>\n" +
                "      <strKey>YX123HF</strKey>\n" +
                "    </DeleteOrder>\n" +
                "  </soap12:Body>\n" +
                "</soap12:Envelope>";
        String result = "";
        try {
            Map<String,String> headerMap = new HashMap<>();
            headerMap.put("SOAPAction","http://tempuri.org/DeleteOrder");

            result = HttpUtil45.operateData("post","soap",hostUrl,
                    new OutTimeConfig(1000*60,1000*60*30,1000*60*30),null,request,headerMap,"","");
            return analyzeCancelData(result);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;


    }

    public boolean confirmOrder(String sku, String  quantity){

        String request="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
                "  <soap12:Body>\n" +
                "    <ConfirmOrderWithSku xmlns=\"http://tempuri.org/\">\n" +
                "      <order_no>201703010002</order_no>\n" +
                "      <purchase_no>CGDF201703010002</purchase_no>\n" +
                "      <sku>18753-4-OS</sku>\n" +
                "      <quantity>1</quantity>\n" +
                "      <strKey>YX123HF</strKey>\n" +
                "    </ConfirmOrderWithSku>\n" +
                "  </soap12:Body>\n" +
                "</soap12:Envelope>";
        String result = "";
        try {
            Map<String,String> headerMap = new HashMap<>();
            headerMap.put("SOAPAction","http://tempuri.org/ConfirmOrderWithSku");

            result = HttpUtil45.operateData("post","soap",hostUrl,
                    new OutTimeConfig(1000*60,1000*60*30,1000*60*30),null,request,headerMap,"","");
            return analyzeConfirmData(result);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;


    }


    public boolean refund(String sku, String  quantity){

        String request="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
                "  <soap12:Body>\n" +
                "    <CancelOrderWithSku xmlns=\"http://tempuri.org/\">\n" +
                "      <order_no>201703010003</order_no>\n" +
                "      <purchase_no>CGDF201703010003</purchase_no>\n" +
                "      <sku>18753-4-OS</sku>\n" +
                "      <strKey>YX123HF</strKey>\n" +
                "    </CancelOrderWithSku>\n" +
                "  </soap12:Body>\n" +
                "</soap12:Envelope>";
        String result = "";
        try {
            Map<String,String> headerMap = new HashMap<>();
            headerMap.put("SOAPAction","http://tempuri.org/CancelOrderWithSku");

            result = HttpUtil45.operateData("post","soap",hostUrl,
                    new OutTimeConfig(1000*60,1000*60*30,1000*60*30),null,request,headerMap,"","");
            return analyzeRefundData(result);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;


    }



    private boolean analyzePushData(String xmlContent){
        SAXReader reader = new SAXReader();
        InputStream in_withcode =null;
        try {
            in_withcode = new ByteArrayInputStream(xmlContent.getBytes("UTF-8"));
            Document document =reader.read(in_withcode);
            Element root = document.getRootElement();

            Element  productElement = root.element("Body").element("PutOrderWithSkuResponse")
                    .element("PutOrderWithSkuResult").element("diffgram").element("DocumentElement");
            String xml =  productElement.asXML();
            OrderOfSupplier order = ObjectXMLUtil.xml2Obj(OrderOfSupplier.class,xml);
            System.out.println("order = " + order.toString());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;


    }

    private boolean analyzeConfirmData(String xmlContent){
        SAXReader reader = new SAXReader();
        InputStream in_withcode =null;
        try {
            in_withcode = new ByteArrayInputStream(xmlContent.getBytes("UTF-8"));
            Document document =reader.read(in_withcode);
            Element root = document.getRootElement();

            Element  productElement = root.element("Body").element("ConfirmOrderWithSkuResponse")
                    .element("ConfirmOrderWithSkuResult").element("diffgram").element("DocumentElement");
            String xml =  productElement.asXML();
            if(xml.contains("Error")){
                Element  errorElement = productElement.element("Response").element("Error");
                String message = errorElement.getText();
                System.out.println("error message = " + message);
            }else{
                OrderOfSupplier order = ObjectXMLUtil.xml2Obj(OrderOfSupplier.class,xml);
                System.out.println("order = " + order.toString());
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;


    }

    private boolean analyzeCancelData(String xmlContent){
        SAXReader reader = new SAXReader();
        InputStream in_withcode =null;
        try {
            in_withcode = new ByteArrayInputStream(xmlContent.getBytes("UTF-8"));
            Document document =reader.read(in_withcode);
            Element root = document.getRootElement();

            Element  productElement = root.element("Body").element("DeleteOrderResponse")
                    .element("DeleteOrderResult").element("diffgram").element("DocumentElement");
            String xml =  productElement.asXML();
            OrderOfSupplier order = ObjectXMLUtil.xml2Obj(OrderOfSupplier.class,xml);
            System.out.println("order = " + order.toString());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;


    }

    private boolean analyzeRefundData(String xmlContent){
        SAXReader reader = new SAXReader();
        InputStream in_withcode =null;
        try {
            in_withcode = new ByteArrayInputStream(xmlContent.getBytes("UTF-8"));
            Document document =reader.read(in_withcode);
            Element root = document.getRootElement();

            Element  productElement = root.element("Body").element("CancelOrderWithSkuResponse")
                    .element("CancelOrderWithSkuResult").element("diffgram").element("DocumentElement");
            String xml =  productElement.asXML();
            OrderOfSupplier order = ObjectXMLUtil.xml2Obj(OrderOfSupplier.class,xml);
            System.out.println("order = " + order.toString());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;


    }


}
