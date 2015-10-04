package com.shangpin.iog.atelier;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import java.io.*;

/**
 * Created by Administrator on 2015/9/23.
 */
public class FetchProduct {
    public static String soapRequestData = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
            "  <soap:Body>\n" +
            "    <GetAllItemsMarketplace xmlns=\"http://tempuri.org/\" />\n" +
            "  </soap:Body>\n" +
            "</soap:Envelope>";



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
                "      <BARCODE>string</BARCODE>\n" +
                "      <QTY>9</QTY>\n" +
                "      <PRICE>11.11</PRICE>\n" +
                "    </NewOrder>\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope>";
        System.out.println("soapRequestData=="+soapRequestData);
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
            InputStream in=postMethod.getResponseBodyAsStream();
            byte[] ims=new byte[(int)postMethod.getResponseContentLength()];
            in.read(ims);
            String s = new String(ims);
            System.out.println("return data ======"+s);
            OutputStream out=new FileOutputStream(new File("E:\\atelier.xml"));
            out.write(ims);
            in.close();
            out.close();
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void getAllPricelistMarketplace(){
        String soapRequestData = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <soap:Body>\n" +
                "    <GetAllImageMarketplace xmlns=\"http://tempuri.org/\" />\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope>";
        HttpClient httpClient = new HttpClient();
        String uri="http://109.168.12.42/ws_sito_P15/ws_sito_p15.asmx?op=GetAllImageMarketplace";
        PostMethod postMethod = new PostMethod(uri);
        postMethod.setRequestHeader("SOAPAction", "http://tempuri.org/GetAllImageMarketplace");
        postMethod.setRequestHeader("Content-Type", "text/xml; charset=UTF-8");

        StringRequestEntity requestEntity=new StringRequestEntity(soapRequestData);
        postMethod.setRequestEntity(requestEntity);

        int returnCode=0;
        try {
            returnCode = httpClient.executeMethod(postMethod);
            System.out.println("returnCode=11="+returnCode);
            InputStream in=postMethod.getResponseBodyAsStream();
            byte[] ims=new byte[(int)postMethod.getResponseContentLength()];
            in.read(ims);
            String s = new String(ims);
            System.out.println("s======"+s);
            OutputStream out=new FileOutputStream(new File("E:\\atelier.xml"));
            out.write(ims);
            in.close();
            out.close();
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void test(){
        HttpClient httpClient = new HttpClient();
        //String uri="http://109.168.12.42/ws_sito_P15/ws_sito_p15.asmx?op=GetAllItems";
        String uri="http://109.168.12.42/ws_sito_P15/ws_sito_p15.asmx?op=GetAllItemsMarketplace";
        PostMethod postMethod = new PostMethod(uri);
        postMethod.setRequestHeader("SOAPAction", "http://tempuri.org/GetAllItemsMarketplace");
        postMethod.setRequestHeader("Content-Type", "text/xml; charset=UTF-8");

        StringRequestEntity requestEntity=new StringRequestEntity(soapRequestData);
        postMethod.setRequestEntity(requestEntity);

        int returnCode=0;
        try {
            returnCode = httpClient.executeMethod(postMethod);
            System.out.println("returnCode=="+returnCode);
            InputStream in=postMethod.getResponseBodyAsStream();
            byte[] ims=new byte[(int)postMethod.getResponseContentLength()];
            in.read(ims);
            String s = new String(ims);
            System.out.println("s======"+s);
            OutputStream out=new FileOutputStream(new File("E:\\atelier.xml"));
            out.write(ims);
            in.close();
            out.close();
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  void  test2() throws IOException {

        PostMethod postMethod = new PostMethod("http://109.168.12.42/ws_sito_P15/ws_sito_p15.asmx?op=GetAllItems");

        byte[] b = soapRequestData.getBytes("utf-8");
        InputStream is = new ByteArrayInputStream(b,0,b.length);
        RequestEntity re = new InputStreamRequestEntity(is,b.length,"application/soap+xml; charset=utf-8");
        postMethod.setRequestEntity(re);

        HttpClient httpClient = new HttpClient();
        int statusCode = httpClient.executeMethod(postMethod);
        System.out.println("statusCode=33===="+statusCode);
        soapRequestData =  postMethod.getResponseBodyAsString();

        System.out.println(soapRequestData);
    }

    public void  returnRe(){
        String str = "171003;AI15;Burberry;3201798;2310C;Donna;Autunno Inverno;Stivali;Impermeabili.;" +
                ";Beige;100%pvc;Fantasia;MADE IN ITALY;Stivali da pioggia protettivi con motivo House check e suola in gomma antiscivolo.&lt;BR/&gt;&lt;BR/&gt;&lt;UL&gt;&lt;BR/&gt;&lt;LI&gt;suola colorata a contrasto&lt;/LI&gt;&lt;/UL&gt;;Burberry: Stivali Impermeabili Beige Stampa Check;" +
                "195;195;;195;;NO;1;Chiari e Naturali;;0;Altezza;Tacco;Plateau;Polpaccio;Suola interna;;;;;" +
                "16/12/2014 00:00:00;;;0;;;;;Azienda inglese fondata nel 1856 da Thomas Burberry che, dopo aver fatto per vario tempo l’apprendista in un importante sartoria di allora, apre il suo primo negozio di stoffe a Basingstoke, nell’Hampshire in Inghilterra. L’azienda diventa molto popolare nel secondo dopoguerra associando indissolubilmente il suo marchio al famoso trench color beige con fodera tartan nera e rossa.Il capo gli       ";
        System.out.println(str.length());
        int s1 = str.split(";").length;
        System.out.println(s1);
        String s2 = str.replaceAll("&lt;","");
        System.out.println(s2.length());
        int s3 = s2.split(";").length;
        System.out.println(s3);
        System.out.println(s2);
    }
    public static void main(String[] args) throws IOException {
        //new FetchProduct().returnRe();
        System.out.println("------------------------");
        new FetchProduct().newOrder();
    }
}
