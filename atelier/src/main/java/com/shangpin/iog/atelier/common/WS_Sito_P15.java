package com.shangpin.iog.atelier.common;

import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import java.io.*;

/**
 * Created by wangyuzhi on 2015/9/30.
 */
public class WS_Sito_P15 {

    /**
     * get all items market place
     * */
    public String getAllAvailabilityMarketplace(){
        String soapRequestData = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
                "  <soap12:Body>\n" +
                "    <GetAllAvailabilityMarketplace xmlns=\"http://tempuri.org/\" />\n" +
                "  </soap12:Body>\n" +
                "</soap12:Envelope>";
        String file = "E:\\AllAvailabilityMarketplace.xml";
        HttpClient httpClient = new HttpClient();
        String uri="http://109.168.12.42/ws_sito_P15/ws_sito_p15.asmx?op=GetAllAvailabilityMarketplace";
        PostMethod postMethod = new PostMethod(uri);
        postMethod.setRequestHeader("Content-Type", "application/soap+xml; charset=utf-8");

        System.out.println("soapRequestData=="+soapRequestData);
        StringRequestEntity requestEntity=new StringRequestEntity(soapRequestData);
        postMethod.setRequestEntity(requestEntity);

        int returnCode=0;
        try {
            returnCode = httpClient.executeMethod(postMethod);
            System.out.println("returnCode=="+returnCode);
            BufferedOutputStream out=new BufferedOutputStream(new FileOutputStream(new File(file)));
            BufferedInputStream in=new BufferedInputStream(postMethod.getResponseBodyAsStream());
            int length = 0;
            byte[] b = new byte[10240];
            System.out.println("44444");
            while((length = in.read(b,0,10240)) != -1)
            {
                out.write(b, 0, length);
            }
            in.close();
            out.flush();
            out.close();
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return MyStringUtil.parseFile2Str(file);
    }

    /**
     * get all items market place
     * */
    public  String getAllItemsMarketplace(){
        String items = HttpUtil45.post("http://109.168.12.42/ws_sito_P15/ws_sito_p15.asmx/GetAllItemsMarketplace",
                new OutTimeConfig(1000*60*10,1000*60*10,1000*60*10));
        System.out.println(items);
        return items.replaceAll("&lt;","").replaceAll("&gt;", "").replaceAll("&amp;","");
    }

    /**
     * get all images
     * */
    public String getAllImageMarketplace(){
        String allImages = HttpUtil45.post("http://109.168.12.42/ws_sito_P15/ws_sito_p15.asmx/GetAllImageMarketplace",
                new OutTimeConfig(1000 * 60 * 10, 1000 * 60 * 10, 1000 * 60 * 10));
        HttpUtil45.closePool();
        System.out.println(allImages);
        return allImages;
    }
    /**
     * new order
     * */
    public void newOrder(){
        String soapRequestData = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <soap:Body>\n" +
                "    <NewOrder xmlns=\"http://tempuri.org/\">\n" +
                "      <ID_ORDER_WEB>123</ID_ORDER_WEB>\n" +
                "      <ID_CLIENTE_WEB>456</ID_CLIENTE_WEB>\n" +
                "      <DESTINATIONROW1>address</DESTINATIONROW1>\n" +
                "      <DESTINATIONROW2>address</DESTINATIONROW2>\n" +
                "      <DESTINATIONROW3>address</DESTINATIONROW3>\n" +
                "      <BARCODE>789</BARCODE>\n" +
                "      <QTY>9</QTY>\n" +
                "      <PRICE>11.11</PRICE>\n" +
                "    </NewOrder>\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope>";
        System.out.println("soapRequestData=="+soapRequestData);
        String file = "E:\\newOrder.xml";
        HttpClient httpClient = new HttpClient();
        String uri="http://109.168.12.42/ws_sito_P15/ws_sito_p15.asmx?op=NewOrder";
        PostMethod postMethod = new PostMethod(uri);
        postMethod.setRequestHeader("SOAPAction", "http://tempuri.org/NewOrder");
        postMethod.setRequestHeader("Content-Type", "text/xml; charset=UTF-8");

        StringRequestEntity requestEntity=new StringRequestEntity(soapRequestData);
        postMethod.setRequestEntity(requestEntity);

        int returnCode=0;
        try {
            returnCode = httpClient.executeMethod(postMethod);
            System.out.println("returnCode=="+returnCode);
            BufferedOutputStream out=new BufferedOutputStream(new FileOutputStream(new File(file)));
            BufferedInputStream in=new BufferedInputStream(postMethod.getResponseBodyAsStream());
            int length = 0;
            byte[] b = new byte[10240];
            System.out.println("44444");
            while((length = in.read(b,0,10240)) != -1)
            {
                out.write(b, 0, length);
            }
            in.close();
            out.flush();
            out.close();
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * test
     * */
    public static void main(String[] args) throws IOException {
        //new WS_Sito_P15().getAllAvailabilityMarketplace();
        new WS_Sito_P15().getAllImageMarketplace();
       //new WS_Sito_P15().getAllItemsMarketplace();
    }
}
