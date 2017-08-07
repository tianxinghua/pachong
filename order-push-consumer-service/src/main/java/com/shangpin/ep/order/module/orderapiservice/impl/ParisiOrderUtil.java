package com.shangpin.ep.order.module.orderapiservice.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.ep.order.conf.supplier.SupplierProperties;
import com.shangpin.ep.order.module.order.bean.OrderDTO;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.parisi.OrderDetail;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.parisi.OrderOfSupplier;
import com.shangpin.ep.order.util.httpclient.HttpUtil45;
import com.shangpin.ep.order.util.httpclient.OutTimeConfig;
import com.shangpin.ep.order.util.xml.ObjectXMLUtil;

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
    private String strKey = null;
    private String strPassword = null;
    @Autowired
    SupplierProperties supplierProperties;

    @PostConstruct
    public void init(){
//        hostUrl = supplierProperties.getParisi().getHostUrl();
//        createOrderUrl = supplierProperties.getParisi().getSetOrderUrl();
//        cancelOrderUrl = supplierProperties.getParisi().getCancelOrderUrl();
//        confirmOrderUrl = supplierProperties.getParisi().getConfirmOrderUrl();
//        deleteOrderUrl = supplierProperties.getParisi().getDeleteOrderUrl();
//        strKey = supplierProperties.getParisi().getStrKey();
//        strPassword = supplierProperties.getParisi().getStrPassword();
        hostUrl = "http://www.rpwebservice.it/wsnat13.asmx";
        createOrderUrl = "http://tempuri.org/PutOrderWithSku";
        cancelOrderUrl = "http://tempuri.org/CancelOrderWithSku";
        confirmOrderUrl = "http://tempuri.org/ConfirmOrderWithSku";
        deleteOrderUrl = "http://tempuri.org/DeleteOrder";
        strKey = "9317";
        strPassword = "FYI";
    }

    public OrderOfSupplier pushOrder(OrderDTO orderDTO ,String sku, String  quantity){
    	String order_no = orderDTO.getSpOrderId();
        String request="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
                "  <soap12:Body>\n" +
                "    <PutOrderWithSku xmlns=\"http://tempuri.org/\">\n" +
                "      <order_no>"+order_no+"</order_no>\n" +
                "      <sku>"+sku+"</sku>\n" +
                "      <quantity>"+quantity+"</quantity>\n" +
                "      <strKey>"+strKey+"</strKey>\n" +
                "      <strPassword>"+strPassword+"</strPassword>\n" +
                "    </PutOrderWithSku>\n" +
                "  </soap12:Body>\n" +
                "</soap12:Envelope>";
        String result = "";
        try {
            Map<String,String> headerMap = new HashMap<>();
            headerMap.put("SOAPAction",createOrderUrl);

            result = HttpUtil45.operateData("post","soap",hostUrl,
                    new OutTimeConfig(1000*60,1000*60*30,1000*60*30),null,request,headerMap,"","");
            return analyzePushData(result);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;


    }



    public OrderOfSupplier cancelOrder(OrderDTO orderDTO){
        String order_no = orderDTO.getSpOrderId();
        String request="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
                "  <soap12:Body>\n" +
                "    <DeleteOrder xmlns=\"http://tempuri.org/\">\n" +
                "      <order_no>"+order_no+"</order_no>\n" +
                "      <strKey>"+strKey+"</strKey>\n" +
                "      <strPassword>"+strPassword+"</strPassword>\n" +
                "    </DeleteOrder>\n" +
                "  </soap12:Body>\n" +
                "</soap12:Envelope>";
        String result = "";
        try {
            Map<String,String> headerMap = new HashMap<>();
            headerMap.put("SOAPAction",deleteOrderUrl);
            headerMap.put("SOAPAction","http://tempuri.org/DeleteOrder");
            result = HttpUtil45.operateData("post","soap","http://www.rpwebservice.it/wsnat13.asmx",
                    new OutTimeConfig(1000*60,1000*60*30,1000*60*30),null,request,headerMap,"","");
            return analyzeCancelData(result);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;


    }
    public static void main(String[] args){
    	ParisiOrderUtil util = new ParisiOrderUtil();
    	util.init();
    	String sku = "62680-47-S";
        String quantity = "1";
    	OrderDTO orderDTO = new OrderDTO();
    	orderDTO.setSpOrderId("0123456789");
    	orderDTO.setPurchaseNo("9876543210");
    	util.pushOrder(orderDTO,sku,quantity);
    	util.cancelOrder(orderDTO);
    	util.confirmOrder(orderDTO, sku, quantity);
    	util.refund(orderDTO, sku);
    }

    public OrderOfSupplier confirmOrder(OrderDTO orderDTO ,String sku, String  quantity){
    	String order_no = orderDTO.getSpOrderId();
    	String purchase_no = orderDTO.getPurchaseNo();
        String request="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
                "  <soap12:Body>\n" +
                "    <ConfirmOrderWithSku xmlns=\"http://tempuri.org/\">\n" +
                "      <order_no>"+order_no+"</order_no>\n" +
                "      <purchase_no>"+purchase_no+"</purchase_no>\n" +
                "      <sku>"+sku+"</sku>\n" +
                "      <quantity>"+quantity+"</quantity>\n" +
                "      <strKey>"+strKey+"</strKey>\n" +
                "      <strPassword>"+strPassword+"</strPassword>\n" +
                "    </ConfirmOrderWithSku>\n" +
                "  </soap12:Body>\n" +
                "</soap12:Envelope>";
        String result = "";
        try {
            Map<String,String> headerMap = new HashMap<>();
            headerMap.put("SOAPAction",confirmOrderUrl);

            result = HttpUtil45.operateData("post","soap",hostUrl,
                    new OutTimeConfig(1000*60,1000*60*30,1000*60*30),null,request,headerMap,"","");
            return analyzeConfirmData(result);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;


    }


    public OrderOfSupplier refund(OrderDTO orderDTO ,String sku){
    	String order_no = orderDTO.getSpOrderId();
    	String purchase_no = orderDTO.getPurchaseNo();
        String request="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
                "  <soap12:Body>\n" +
                "    <CancelOrderWithSku xmlns=\"http://tempuri.org/\">\n" +
                "      <order_no>"+order_no+"</order_no>\n" +
                "      <purchase_no>"+purchase_no+"</purchase_no>\n" +
                "      <sku>"+sku+"</sku>\n" +
                "      <strKey>"+strKey+"</strKey>\n" +
                "      <strPassword>"+strPassword+"</strPassword>\n" +
                "    </CancelOrderWithSku>\n" +
                "  </soap12:Body>\n" +
                "</soap12:Envelope>";
        String result = "";
        try {
            Map<String,String> headerMap = new HashMap<>();
            headerMap.put("SOAPAction",cancelOrderUrl);

            result = HttpUtil45.operateData("post","soap",hostUrl,
                    new OutTimeConfig(1000*60,1000*60*30,1000*60*30),null,request,headerMap,"","");
            return analyzeRefundData(result);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;


    }



    private OrderOfSupplier analyzePushData(String xmlContent){
        SAXReader reader = new SAXReader();
        InputStream in_withcode =null;
        OrderOfSupplier order = new OrderOfSupplier();
        OrderDetail detail = new OrderDetail();
        try {
            in_withcode = new ByteArrayInputStream(xmlContent.getBytes("UTF-8"));
            Document document =reader.read(in_withcode);
            Element root = document.getRootElement();

            Element  productElement = root.element("Body").element("PutOrderWithSkuResponse")
                    .element("PutOrderWithSkuResult").element("diffgram").element("DocumentElement");
            String xml =  productElement.asXML();
            if(xml.contains("Error")){
                Element  errorElement = productElement.element("Response").element("Error");
                String message = errorElement.getText();
                detail.setError(message);
                order.setOrderDetail(detail);
                logger.info("error message = " + message);
                return order;
            }else{
                order = ObjectXMLUtil.xml2Obj(OrderOfSupplier.class,xml);
                logger.info("order = " + order.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            detail.setError("调用供应商接口错误!");
            order.setOrderDetail(detail);
            return order;
        }
        return order;


    }

    private OrderOfSupplier analyzeConfirmData(String xmlContent){
        SAXReader reader = new SAXReader();
        InputStream in_withcode =null;
        OrderOfSupplier order = new OrderOfSupplier();
        OrderDetail detail = new OrderDetail();
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
                detail.setError(message);
                order.setOrderDetail(detail);
                logger.info("error message = " + message);
                return order;
            }else{
                order = ObjectXMLUtil.xml2Obj(OrderOfSupplier.class,xml);
                logger.info("order = " + order.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
            detail.setError("调用供应商接口错误!");
            order.setOrderDetail(detail);
            return order;
        }
        return order;


    }

    private static OrderOfSupplier analyzeCancelData(String xmlContent){
        SAXReader reader = new SAXReader();
        InputStream in_withcode =null;
        OrderOfSupplier order = new OrderOfSupplier();
        OrderDetail detail = new OrderDetail();
        try {
            in_withcode = new ByteArrayInputStream(xmlContent.getBytes("UTF-8"));
            Document document =reader.read(in_withcode);
            Element root = document.getRootElement();

            Element  productElement = root.element("Body").element("DeleteOrderResponse")
                    .element("DeleteOrderResult").element("diffgram").element("DocumentElement");
            String xml =  productElement.asXML();
            if(xml.contains("Error")){
                Element  errorElement = productElement.element("Response").element("Error");
                String message = errorElement.getText();
                detail.setError(message);
                order.setOrderDetail(detail);
                logger.info("error message = " + message);
                return order;
            }else{
                order = ObjectXMLUtil.xml2Obj(OrderOfSupplier.class,xml);
                logger.info("order = " + order.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            detail.setError("调用供应商接口错误!");
            order.setOrderDetail(detail);
            return order;
        }
        return order;
    }

    private OrderOfSupplier analyzeRefundData(String xmlContent){
        SAXReader reader = new SAXReader();
        InputStream in_withcode =null;
        OrderOfSupplier order = new OrderOfSupplier();
        OrderDetail detail = new OrderDetail();
        try {
            in_withcode = new ByteArrayInputStream(xmlContent.getBytes("UTF-8"));
            Document document =reader.read(in_withcode);
            Element root = document.getRootElement();

            Element  productElement = root.element("Body").element("CancelOrderWithSkuResponse")
                    .element("CancelOrderWithSkuResult").element("diffgram").element("DocumentElement");
            String xml =  productElement.asXML();
            if(xml.contains("Error")){
                Element  errorElement = productElement.element("Response").element("Error");
                String message = errorElement.getText();
                detail.setError(message);
                order.setOrderDetail(detail);
                logger.info("error message = " + message);
                return order;
            }else{
                order = ObjectXMLUtil.xml2Obj(OrderOfSupplier.class,xml);
                logger.info("order = " + order.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            detail.setError("调用供应商接口错误!");
            order.setOrderDetail(detail);
            return order;
        }
        return order;


    }


}
