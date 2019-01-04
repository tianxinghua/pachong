package com.shangpin.iog.parisi.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.ObjectXMLUtil;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.parisi.dto.Products;
import org.apache.log4j.Logger;
import org.dom4j.*;
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
    private static String strKey="",    hostUrl="",    productSoapUrl="";
    static {
        if (null == bdl)
            bdl = ResourceBundle.getBundle("conf");
        strKey = bdl.getString("strKey");
        hostUrl = bdl.getString("hostUrl");
        productSoapUrl = bdl.getString("productSoapUrl");
    }

    public String getProduct(){
            String request="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                    "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
                    "  <soap12:Body>\n" +
                    "    <GetAllProduct xmlns=\"http://tempuri.org/\">\n" +
                    "      <strKey>"+strKey+"</strKey>\n" +
                    "    </GetAllProduct>\n" +
                    "  </soap12:Body>\n" +
                    "</soap12:Envelope>";
        String result = "";
		try {
			Map<String,String> headerMap = new HashMap<>();
			headerMap.put("SOAPAction",productSoapUrl);
			result = HttpUtil45.operateData("post","soap",hostUrl,
                    new OutTimeConfig(1000*60,1000*60*30,1000*60*30),null,request,headerMap,"","");



		} catch (ServiceException e) {
			e.printStackTrace();
		}
        return result;
    }
    
    public   Products getList(String str) throws DocumentException {

        List<Element> allList = new ArrayList<Element>();
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


        Element  productElement = root.element("Body").element("GetAllProductResponse")
                .element("GetAllProductResult").element("diffgram").element("DocumentElement");
        String xml =  productElement.asXML();
        System.out.println(xml);
        Products products = null;
        try {
            products =  ObjectXMLUtil.xml2Obj(Products.class,xml);
//            System.out.println(products.toString());
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return products;
    }
    
    public static void main(String[] args){
        ProductFetchUtil util = new ProductFetchUtil();
//        String result = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><soap:Body><GetAllProductResponse xmlns=\"http://tempuri.org/\"><GetAllProductResult><xs:schema id=\"NewDataSet\" xmlns=\"\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:msdata=\"urn:schemas-microsoft-com:xml-msdata\"><xs:element name=\"NewDataSet\" msdata:IsDataSet=\"true\" msdata:MainDataTable=\"ns_product\" msdata:UseCurrentLocale=\"true\"><xs:complexType><xs:choice minOccurs=\"0\" maxOccurs=\"unbounded\"><xs:element name=\"ns_product\"><xs:complexType><xs:sequence><xs:element name=\"idProduct\" type=\"xs:int\" /><xs:element name=\"product\" type=\"xs:int\" minOccurs=\"0\" /><xs:element name=\"sku\" minOccurs=\"0\"><xs:simpleType><xs:restriction base=\"xs:string\"><xs:maxLength value=\"100\" /></xs:restriction></xs:simpleType></xs:element><xs:element name=\"gender\" minOccurs=\"0\"><xs:simpleType><xs:restriction base=\"xs:string\"><xs:maxLength value=\"45\" /></xs:restriction></xs:simpleType></xs:element><xs:element name=\"idGender\" type=\"xs:int\" minOccurs=\"0\" /><xs:element name=\"category\" minOccurs=\"0\"><xs:simpleType><xs:restriction base=\"xs:string\"><xs:maxLength value=\"100\" /></xs:restriction></xs:simpleType></xs:element><xs:element name=\"idCategory\" type=\"xs:int\" minOccurs=\"0\" /><xs:element name=\"season\" minOccurs=\"0\"><xs:simpleType><xs:restriction base=\"xs:string\"><xs:maxLength value=\"45\" /></xs:restriction></xs:simpleType></xs:element><xs:element name=\"idSeason\" type=\"xs:int\" minOccurs=\"0\" /><xs:element name=\"brand\" minOccurs=\"0\"><xs:simpleType><xs:restriction base=\"xs:string\"><xs:maxLength value=\"255\" /></xs:restriction></xs:simpleType></xs:element><xs:element name=\"idBrand\" type=\"xs:int\" minOccurs=\"0\" /><xs:element name=\"composition\" minOccurs=\"0\"><xs:simpleType><xs:restriction base=\"xs:string\"><xs:maxLength value=\"255\" /></xs:restriction></xs:simpleType></xs:element><xs:element name=\"idComposition\" type=\"xs:int\" minOccurs=\"0\" /><xs:element name=\"country\" minOccurs=\"0\"><xs:simpleType><xs:restriction base=\"xs:string\"><xs:maxLength value=\"255\" /></xs:restriction></xs:simpleType></xs:element><xs:element name=\"idCountry\" type=\"xs:int\" minOccurs=\"0\" /><xs:element name=\"name\" minOccurs=\"0\"><xs:simpleType><xs:restriction base=\"xs:string\"><xs:maxLength value=\"255\" /></xs:restriction></xs:simpleType></xs:element><xs:element name=\"price\" type=\"xs:float\" minOccurs=\"0\" /><xs:element name=\"supplierPrice\" type=\"xs:float\" minOccurs=\"0\" /><xs:element name=\"stock\" type=\"xs:int\" minOccurs=\"0\" /><xs:element name=\"barcode\" minOccurs=\"0\"><xs:simpleType><xs:restriction base=\"xs:string\"><xs:maxLength value=\"45\" /></xs:restriction></xs:simpleType></xs:element><xs:element name=\"productCode\" minOccurs=\"0\"><xs:simpleType><xs:restriction base=\"xs:string\"><xs:maxLength value=\"255\" /></xs:restriction></xs:simpleType></xs:element><xs:element name=\"color\" minOccurs=\"0\"><xs:simpleType><xs:restriction base=\"xs:string\"><xs:maxLength value=\"255\" /></xs:restriction></xs:simpleType></xs:element><xs:element name=\"idColor\" type=\"xs:int\" minOccurs=\"0\" /><xs:element name=\"currency\" minOccurs=\"0\"><xs:simpleType><xs:restriction base=\"xs:string\"><xs:maxLength value=\"45\" /></xs:restriction></xs:simpleType></xs:element><xs:element name=\"size\" minOccurs=\"0\"><xs:simpleType><xs:restriction base=\"xs:string\"><xs:maxLength value=\"45\" /></xs:restriction></xs:simpleType></xs:element><xs:element name=\"idSize\" type=\"xs:int\" minOccurs=\"0\" /><xs:element name=\"specification\" minOccurs=\"0\"><xs:simpleType><xs:restriction base=\"xs:string\"><xs:maxLength value=\"1000\" /></xs:restriction></xs:simpleType></xs:element><xs:element name=\"images\" minOccurs=\"0\"><xs:simpleType><xs:restriction base=\"xs:string\"><xs:maxLength value=\"1000\" /></xs:restriction></xs:simpleType></xs:element></xs:sequence></xs:complexType></xs:element></xs:choice></xs:complexType><xs:unique name=\"Constraint1\" msdata:PrimaryKey=\"true\"><xs:selector xpath=\".//ns_product\" /><xs:field xpath=\"idProduct\" /></xs:unique></xs:element></xs:schema><diffgr:diffgram xmlns:msdata=\"urn:schemas-microsoft-com:xml-msdata\" xmlns:diffgr=\"urn:schemas-microsoft-com:xml-diffgram-v1\">" +
//                "<DocumentElement xmlns=\"\">" +
//                "<ns_product diffgr:id=\"ns_product1\" msdata:rowOrder=\"0\">" +
//                "<idProduct>1</idProduct><product>18753</product><sku>18753-4-OS</sku><gender>Woman</gender>" +
//                "<idGender>6</idGender><category>Sciarpa</category><idCategory>55</idCategory>" +
//                "<season>Carry Over </season><idSeason>15</idSeason><brand>Fendi</brand><idBrand>75</idBrand>" +
//                "<composition />" +
//                "<idComposition>0</idComposition><country /><idCountry>0</idCountry><name>Scialle zucchino diagonale </name>" +
//                "<price>350</price><supplierPrice>350</supplierPrice><stock>1</stock><barcode>0187530004712</barcode>" +
//                "<productCode>FXT924-MEA</productCode><color>Rosso</color><idColor>4</idColor>" +
//                "<currency>EUR</currency><size>OS</size><idSize>71</idSize><specification />" +
//                "<images>http://www.poggiodelmonaco.it/test/img/p/1/1.jpg||http://www.poggiodelmonaco.it/test/img/p/2/2.jpg||http://www.poggiodelmonaco.it/test/img/p/3/3.jpg</images></ns_product></DocumentElement></diffgr:diffgram></GetAllProductResult></GetAllProductResponse></soap:Body></soap:Envelope>";//util.getProduct();


        try {
//        	String result  = util.getProduct();
//          util.getList(result);
        	
        	String request="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                    "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
                    "  <soap12:Body>\n" +
                    "    <GetImagesWithSku xmlns=\"http://tempuri.org/\">\n" +
                    "      <sku>58174-667-50</sku>\n" +
                    "      <strKey>"+strKey+"</strKey>\n" +
                    "    </GetImagesWithSku>\n" +
                    "  </soap12:Body>\n" +
                    "</soap12:Envelope>";
        String result = "";
    	try {
    		Map<String,String> headerMap = new HashMap<>();
    		headerMap.put("SOAPAction","http://tempuri.org/GetImagesWithSku");
    		result = HttpUtil45.operateData("post","soap",hostUrl,
                    new OutTimeConfig(1000*60,1000*60*30,1000*60*30),null,request,headerMap,"","");
    		
    		SAXReader reader = new SAXReader();
            InputStream in_withcode =null;
            try {
                in_withcode = new ByteArrayInputStream(result.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Document document =reader.read(in_withcode);
            Element root = document.getRootElement();


            String  images = root.element("Body").element("GetImagesWithSkuResponse")
                    .element("GetImagesWithSkuResult").element("diffgram").element("DocumentElement").element("Dati").element("images").getText();
            System.out.println(images);

    	} catch (ServiceException e) {
    		e.printStackTrace();
    	}
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
}
