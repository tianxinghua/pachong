package com.shangpin.iog.parisi.service;


import com.google.gson.Gson;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.ObjectXMLUtil;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.parisi.dto.Products;

import eu.monnalisa.pf.MonnalisaWSProxy;

import org.apache.log4j.Logger;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.util.*;

/**
 * Created by lizhongren on 2017/2/22.
 */
@Component
public class ProductFetchUtil {
//    private static Logger logger = Logger.getLogger("info");
//    private static Logger loggerError = Logger.getLogger("error");
//    private static ResourceBundle bdl = null;
//    private static String strKey="",    hostUrl="",    stockSoapUrl="";
//    static {
//        if (null == bdl)
//            bdl = ResourceBundle.getBundle("conf");
//        strKey = bdl.getString("strKey");
//        hostUrl = bdl.getString("hostUrl");
//        stockSoapUrl = bdl.getString("stockSoapUrl");
//    }
//
//    public String getProductStock(String skuNo){
//        String request="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
//                "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
//                "  <soap12:Body>\n" +
//                "    <GetStockWithSku xmlns=\"http://tempuri.org/\">\n" +
//                "      <sku>"+skuNo+"</sku>\n" +
//                "      <strKey>"+strKey+"</strKey>\n" +
//                "    </GetStockWithSku>\n" +
//                "  </soap12:Body>\n" +
//                "</soap12:Envelope>";
//        String result = "";
//        try {
//            Map<String,String> headerMap = new HashMap<>();
//            headerMap.put("SOAPAction",stockSoapUrl);
//            result = HttpUtil45.operateData("post","soap",hostUrl,
//                    new OutTimeConfig(1000*60,1000*60*30,1000*60*30),null,request,headerMap,"","");
//
//        } catch (ServiceException e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
//
//
//
//
//    public   String  convertStock(String str) throws Exception {
//
//
//        SAXReader reader = new SAXReader();
//        InputStream in_withcode =null;
//        try {
//            in_withcode = new ByteArrayInputStream(str.getBytes("UTF-8"));
//        } catch (UnsupportedEncodingException e) {
//        	loggerError.error(e.getMessage(),e);
//            e.printStackTrace();
//        }
//        Document document =reader.read(in_withcode);
//        Element root = document.getRootElement();
//
//
//        Element  productElement = root.element("Body").element("GetStockWithSkuResponse")
//                .element("GetStockWithSkuResult").element("diffgram")
//                .element("DocumentElement").element("Dati").element("Stock");
//
//       return  productElement.getStringValue();
//
//
//
//    }

    public static void main(String[] args){
    	MonnalisaWSProxy proxy = new MonnalisaWSProxy();
    	try {
    		eu.monnalisa.pf.GenericResult result= proxy.getDisponibilitaMagazzini("72", "110411", "0625", "6667", "12", null, null, "ecommerce", null);
		    System.out.println(result.getQuantity());
    	} catch (RemoteException e) {
			e.printStackTrace();
		}

    }
}
