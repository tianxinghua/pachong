package com.shangpin.iog.atelier;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ProductFetchService;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;

/**
 * Created by wangyuzhi on 2015/9/30
 */
@Component("atelier")
public class FetchProduct {
    public static String soapRequestData = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
            "  <soap:Body>\n" +
            "    <GetAllItemsMarketplace xmlns=\"http://tempuri.org/\" />\n" +
            "  </soap:Body>\n" +
            "</soap:Envelope>";


    final Logger logger = Logger.getLogger("info");
    final String supplierId = "2015093001426";
    @Autowired
    ProductFetchService productFetchService;
    /**
     * 主处理
     */
    public void fetchProductAndSave() {
        //fetch product
        String items = getItemsData();
        //save into DB
        messMappingAndSave(items.split("\\n"));

    }
    /**
     * get items data
     * */
    private String getItemsData(){
        String items = HttpUtil45.post("http://109.168.12.42/ws_sito_P15/ws_sito_p15.asmx/GetAllItemsMarketplace",
                new OutTimeConfig(1000*60*1,1000*60*1,1000*60*1));
        return items.replaceAll("&lt;","").replaceAll("&gt;", "").replaceAll("&amp;","");
    }
    /**
     *
     * **/
    private void messMappingAndSave(String[] items) {
        for (String item : items) {
            String[] fields = item.split(";");
            System.out.println();
            for (int i = 0; i < fields.length; i++) {
                System.out.print("; fields[" + i + "]=" + fields[i]);
            }
        }

        items = new String[0];
        for (String item : items) {
            String[] fields = item.split(";");
            SpuDTO spu = new SpuDTO();
            try {
                spu.setId(UUIDGenerator.getUUID());
                spu.setSupplierId(supplierId);
                spu.setSpuId(fields[0]);
                spu.setBrandName(fields[2]);
                spu.setCategoryName(fields[13]);
                spu.setSpuName(fields[0]);
                spu.setSeasonId(fields[6]);
                spu.setMaterial(fields[11]);
                spu.setCategoryGender(fields[5]);
                spu.setProductOrigin(fields[13]);
                productFetchService.saveSPU(spu);
            } catch (ServiceException e) {
                e.printStackTrace();
            }

            SkuDTO sku = new SkuDTO();
            try {
                sku.setId(UUIDGenerator.getUUID());
                sku.setSupplierId(supplierId);

                sku.setSpuId(fields[0]);
                sku.setSkuId(fields[0]);
                sku.setProductSize(fields[0]);
                sku.setMarketPrice(fields[0]);
                sku.setSalePrice(fields[0]);
                sku.setSupplierPrice(fields[0]);
                sku.setColor(fields[10]);
                sku.setProductDescription(fields[0]);
                sku.setStock(fields[0]);
                sku.setBarcode(fields[0]);
                sku.setProductCode(fields[0]);
                sku.setProductName(fields[15]);
                productFetchService.saveSKU(sku);

                if (StringUtils.isNotBlank(fields[0])) {
                    String[] picArray = fields[0].split("\\|");

//                            List<String> picUrlList = Arrays.asList(picArray);
                    for (String picUrl : picArray) {
                        ProductPictureDTO dto = new ProductPictureDTO();
                        dto.setPicUrl(picUrl);
                        dto.setSupplierId(supplierId);
                        dto.setId(UUIDGenerator.getUUID());
                        dto.setSkuId(fields[0]);
                        try {
                            productFetchService.savePictureForMongo(dto);
                        } catch (ServiceException e) {
                            e.printStackTrace();
                        }
                    }
                }

            } catch (ServiceException e) {
                try {
                    if (e.getMessage().equals("数据插入失败键重复")) {
                        //更新价格和库存
                        productFetchService.updatePriceAndStock(sku);
                    } else {
                        e.printStackTrace();
                    }

                } catch (ServiceException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

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
                "    <GetAllAvailabilityMarketplace xmlns=\"http://tempuri.org/\" />\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope>";
        HttpClient httpClient = new HttpClient();
        String uri="http://109.168.12.42/ws_sito_P15/ws_sito_p15.asmx?op=GetAllAvailabilityMarketplace";
        PostMethod postMethod = new PostMethod(uri);
        postMethod.setRequestHeader("SOAPAction", "http://tempuri.org/GetAllAvailabilityMarketplace");
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
            OutputStream out=new FileOutputStream(new File("E:\\atelier1.xml"));
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

        System.out.println("soapRequestData=="+soapRequestData);
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
            System.out.println("returnItems=="+s);
            OutputStream out=new FileOutputStream(new File("E:\\atelier.xml"));
            out.write(ims);
            in.close();
            out.flush();
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
    public String readAtelier() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader("E:/atelier.xml"));
        StringBuilder stringBuilder = new StringBuilder();
        String content;
        int i = 0;
        while((content = bufferedReader.readLine() )!=null){
            System.out.println("======================"+i++);
            System.out.println(content);
            stringBuilder.append(content);
        }
        return stringBuilder.toString();
    }
    public void testPost(){
        //new FetchProduct().returnRe();
        System.out.println("-----------------------------------------------------------------------");
        //new FetchProduct().test();
        // new FetchProduct().getAllPricelistMarketplace();
/*        String s = new FetchProduct().readAtelier();
        System.out.println(s);
        System.out.println(s.split("\\n").length);*/
        String kk = HttpUtil45.post("http://109.168.12.42/ws_sito_P15/ws_sito_p15.asmx/GetUpdateItemsMarketplace",
                new OutTimeConfig(1000*60*1,1000*60*1,1000*60*1));
        System.out.println("return kk = " + kk);
        kk = kk.replaceAll("&lt;","");
        kk = kk.replaceAll("&gt;","");
        kk = kk.replaceAll("&amp;","");
        String[] arr = kk.split("\\n");
        System.out.println(arr.length);
        for(String str:arr){
            System.out.print(str.split(";").length + " ");
            if (str.split(";").length == 50){
                System.out.println(str);
            }
        }
        System.out.println();
        System.out.println("-------------------------------sex--------------------------------");
        for(String str:arr){
            if (str.split(";").length>=48){
                System.out.print(str.split(";")[5]+" ");
            }
        }
        System.out.println();
        System.out.println("--------------------------------price-------------------------------");
        for(String str:arr){
            if (str.split(";").length>=48){
                System.out.print(str.split(";")[16]+" ");
            }
        }
        System.out.println();
        System.out.println("--------------------------------date-------------------------------");
        for(String str:arr){
            if (str.split(";").length>=48){
                System.out.print(str.split(";")[35]+" ");
            }
        }
    }

    /**
     * test
     * */
    public static void main(String[] args) throws IOException {
        new FetchProduct().fetchProductAndSave();
        //new FetchProduct().testPost();
    }
}
