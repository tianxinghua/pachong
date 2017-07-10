package com.shangpin.iog.parisi.service;


import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.ObjectXMLUtil;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.parisi.dto.Products;
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
import java.util.*;

/**
 * Created by lizhongren on 2017/2/22.
 */
@Component
public class ProductFetchUtil {
    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    private static ResourceBundle bdl = null;
    private static String strKey="",    hostUrl="",    stockSoapUrl="";
    static {
        if (null == bdl)
            bdl = ResourceBundle.getBundle("conf");
        strKey = bdl.getString("strKey");
        hostUrl = bdl.getString("hostUrl");
        stockSoapUrl = bdl.getString("stockSoapUrl");
    }

    public String getProductStock(String skuNo){
        String request="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
                "  <soap12:Body>\n" +
                "    <GetStockWithSku xmlns=\"http://tempuri.org/\">\n" +
                "      <sku>"+skuNo+"</sku>\n" +
                "      <strKey>"+strKey+"</strKey>\n" +
                "    </GetStockWithSku>\n" +
                "  </soap12:Body>\n" +
                "</soap12:Envelope>";
        String result = "";
        try {
            Map<String,String> headerMap = new HashMap<>();
            headerMap.put("SOAPAction",stockSoapUrl);
            result = HttpUtil45.operateData("post","soap",hostUrl,
                    new OutTimeConfig(1000*60,1000*60*30,1000*60*30),null,request,headerMap,"","");



        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return result;
    }




    public   String  convertStock(String str) throws Exception {


        SAXReader reader = new SAXReader();
        InputStream in_withcode =null;
        try {
            in_withcode = new ByteArrayInputStream(str.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Document document =reader.read(in_withcode);
        Element root = document.getRootElement();


        Element  productElement = root.element("Body").element("GetStockWithSkuResponse")
                .element("GetStockWithSkuResult").element("diffgram")
                .element("DocumentElement").element("ns_product").element("Stock");

       return  productElement.getStringValue();



    }

    public static void main(String[] args){
        ProductFetchUtil util = new ProductFetchUtil();



        String result ="<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Body><GetStockWithSkuResponse xmlns=\"http://tempuri.org/\"><GetStockWithSkuResult><xs:schema id=\"NewDataSet\" xmlns=\"\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:msdata=\"urn:schemas-microsoft-com:xml-msdata\"><xs:element name=\"NewDataSet\" msdata:IsDataSet=\"true\" msdata:MainDataTable=\"ns_product\" msdata:UseCurrentLocale=\"true\"><xs:complexType><xs:choice minOccurs=\"0\" maxOccurs=\"unbounded\"><xs:element name=\"ns_product\"><xs:complexType><xs:sequence><xs:element name=\"Stock\" type=\"xs:int\" minOccurs=\"0\" /></xs:sequence></xs:complexType></xs:element></xs:choice></xs:complexType></xs:element></xs:schema><diffgr:diffgram xmlns:msdata=\"urn:schemas-microsoft-com:xml-msdata\" xmlns:diffgr=\"urn:schemas-microsoft-com:xml-diffgram-v1\"><DocumentElement xmlns=\"\"><ns_product diffgr:id=\"ns_product1\" msdata:rowOrder=\"0\"><Stock>1</Stock></ns_product></DocumentElement></diffgr:diffgram></GetStockWithSkuResult></GetStockWithSkuResponse></soap:Body></soap:Envelope>";
                //util.getStock("18753-4-OS");
        try {
            System.out.println(util.convertStock(result));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
