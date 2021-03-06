package com.shangpin.iog.ostore.common;

import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by monkey on 2015/11/26.
 */
public class WS_Sito_P15 {

    private static Logger logger = Logger.getLogger("info");
    private static ResourceBundle bdl=null;
    private static String itemsFile;
    private static String availabilityFile;
    private static String imageFile;
    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("conf");
    }
    /**
     * fetch product from atelier to local
     * */
    public void fetchProduct(){
        //get all items market place
        new WS_Sito_P15().getAllItemsMarketplace();
        //get all availability market place
        new WS_Sito_P15().getAllAvailabilityMarketplaceBySoap();
        //get all image market place
        new WS_Sito_P15().getAllImageMarketplaceBySoap();
    }
    /**
     * get items string from local
     * */
    public String getAllItemsStr(){
        return parseFile2Str(itemsFile);
    }
    /**
     * get items string from local
     * */
    public String getAllAvailabilityStr(){
        return parseFile2Str(availabilityFile);
    }
    /**
     * get items string from local
     * */
    public String getAllImageStr(){
        return parseFile2Str(imageFile);
    }
    /**
     * get all items market place
     * */
    public void getAllAvailabilityMarketplace(){
        System.out.println("getAllAvailabilityMarketplace---------------------------------");
        String items = "";
        try{
            items = HttpUtil45.post("http://109.168.12.42/ws_sito_P15/ws_sito_p15.asmx/GetAllAvailabilityMarketplace",
                    new OutTimeConfig(1000*60*10,1000*60*10,1000*60*10));
            // System.out.println(items);
        }catch (Exception e){
            System.out.println("拉取信息失败getAllAvailabilityMarketplace===========================");
            e.printStackTrace();
        } finally {
            HttpUtil45.closePool();
        }
        try {
            File newTextFile = new File(availabilityFile);
            if (!newTextFile.getParentFile().exists()) {
				newTextFile.getParentFile().mkdirs();
			}
            if (!newTextFile.exists())
                newTextFile.createNewFile();
            FileWriter fw;
            fw = new FileWriter(newTextFile);
            fw.write(items);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        /**
         * get all items market place
         * */
        public void getAllAvailabilityMarketplaceBySoap(){
        System.out.println("getAllAvailabilityMarketplace--------------------------------");
        String soapRequestData = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
                "  <soap12:Body>\n" +
                "    <GetAllAvailabilityMarketplace xmlns=\"http://tempuri.org/\" />\n" +
                "  </soap12:Body>\n" +
                "</soap12:Envelope>";
        HttpClient httpClient = new HttpClient();
        String uri="http://109.168.12.42/ws_sito_P15/ws_sito_p15.asmx?op=GetAllAvailabilityMarketplace";
        PostMethod postMethod = new PostMethod(uri);
        postMethod.setRequestHeader("Content-Type", "application/soap+xml; charset=utf-8");

        //System.out.println("soapRequestData=="+soapRequestData);
        StringRequestEntity requestEntity=new StringRequestEntity(soapRequestData);
        postMethod.setRequestEntity(requestEntity);

        int returnCode=0;
        try {
            returnCode = httpClient.executeMethod(postMethod);
            System.out.println("returnCode=="+returnCode);
            BufferedOutputStream out=new BufferedOutputStream(new FileOutputStream(new File(availabilityFile)));
            BufferedInputStream in=new BufferedInputStream(postMethod.getResponseBodyAsStream());
            int length = 0;
            byte[] b = new byte[10240];
            while((length = in.read(b,0,10240)) != -1)
            {
                out.write(b, 0, length);
            }
            in.close();
            out.flush();
            out.close();
        } catch (HttpException e) {
            System.out.println("getAllAvailabilityMarketplace 失败HttpException===========================");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("getAllAvailabilityMarketplace 失败IOException===========================");
            e.printStackTrace();
        } finally {
        }
        //return MyStringUtil.parseFile2Str(file);
    }

    /**
     * get all items market place
     * */
    public  void getAllItemsMarketplace(){
        System.out.println("getAllItemsMarketplace---------------------------------");
        String items = "";
        try{
            items = HttpUtil45.post("http://79.61.138.184/ws_sito/ws_sito_p15.asmx/GetAllItemsMarketplace",
                    new OutTimeConfig(1000*60*10,1000*60*10,1000*60*10));
           // System.out.println(items);
        }catch (Exception e){
            System.out.println("拉取信息失败GetAllItemsMarketplace===========================");
            e.printStackTrace();
        } finally {
            HttpUtil45.closePool();
        }
        try {
            File newTextFile = new File(itemsFile);
            if (!newTextFile.getParentFile().exists()) {
				newTextFile.getParentFile().mkdirs();
			}
            if (!newTextFile.exists()){
            	newTextFile.createNewFile();
            }
            FileWriter fw;
            fw = new FileWriter(newTextFile);
            fw.write(items.replaceAll("&lt;", "").replaceAll("&gt;", "").replaceAll("&amp;","").substring(77));
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("===========================");
        System.out.println(items.indexOf("171003"));
        //return items.replaceAll("&lt;","").replaceAll("&gt;", "").replaceAll("&amp;","").substring(77);
    }

    /**
     * get all images
     * */
    public void getAllImageMarketplace(){
        System.out.println("getAllImageMarketplace---------------------------------------");
        String allImages = "";
        try{
            allImages = HttpUtil45.post("http://109.168.12.42/ws_sito_P15/ws_sito_p15.asmx/GetAllImageMarketplace",
                    new OutTimeConfig(1000 * 60 * 10, 1000 * 60 * 10, 1000 * 60 * 10));
        }catch (Exception e){
            System.out.println("getAllImageMarketplace-failed--------------------------------------");
        }finally {
            HttpUtil45.closePool();
        }
        try {
            File newTextFile = new File(imageFile);
            if (!newTextFile.getParentFile().exists()) {
				newTextFile.getParentFile().mkdirs();
			}
            if (!newTextFile.exists())
                newTextFile.createNewFile();
            FileWriter fw;
            fw = new FileWriter(newTextFile);
            fw.write(allImages);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(allImages);
        //return allImages;
    }
    /**
     * get all images
     * */
    public void getUpdateItemsMarketplace(){
        System.out.println("GetUpdateItemsMarketplace---------------------------------------");
        String allImages = "";
        try{
            allImages = HttpUtil45.post("http://109.168.12.42/ws_sito_P15/ws_sito_p15.asmx/GetUpdateItemsMarketplace",
                    new OutTimeConfig(1000 * 60 * 10, 1000 * 60 * 10, 1000 * 60 * 10));
        }catch (Exception e){
            System.out.println("GetUpdateItemsMarketplace-failed--------------------------------------");
        }finally {
            HttpUtil45.closePool();
        }
        try {
            File newTextFile = new File("E:/aa.xml");
            if (!newTextFile.getParentFile().exists()) {
				newTextFile.getParentFile().mkdirs();
			}
            if (!newTextFile.exists())
                newTextFile.createNewFile();
            FileWriter fw;
            fw = new FileWriter(newTextFile);
            fw.write(allImages);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(allImages);
        //return allImages;
    }
    /**
     * get all images
     * */
    public void getAllImageMarketplaceBySoap(){
        System.out.println("getAllImageMarketplaceBySoap--------------------------------");
        String soapRequestData = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
                "  <soap12:Body>\n" +
                "    <GetAllImageMarketplace xmlns=\"http://tempuri.org/\" />\n" +
                "  </soap12:Body>\n" +
                "</soap12:Envelope>";
        HttpClient httpClient = new HttpClient();
        String uri="http://109.168.12.42/ws_sito_P15/ws_sito_p15.asmx?op=GetAllImageMarketplace";
        PostMethod postMethod = new PostMethod(uri);
        postMethod.setRequestHeader("Content-Type", "application/soap+xml; charset=utf-8");

        //System.out.println("soapRequestData=="+soapRequestData);
        StringRequestEntity requestEntity=new StringRequestEntity(soapRequestData);
        postMethod.setRequestEntity(requestEntity);

        int returnCode=0;
        try {
            returnCode = httpClient.executeMethod(postMethod);
            System.out.println("returnCode=="+returnCode);
            BufferedOutputStream out=new BufferedOutputStream(new FileOutputStream(new File(imageFile)));
            BufferedInputStream in=new BufferedInputStream(postMethod.getResponseBodyAsStream());
            int length = 0;
            byte[] b = new byte[10240];
            while((length = in.read(b,0,10240)) != -1)
            {
                out.write(b, 0, length);
            }
            in.close();
            out.flush();
            out.close();
        } catch (HttpException e) {
            System.out.println("getAllImageMarketplaceBySoap 失败HttpException===========================");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("getAllImageMarketplaceBySoap 失败IOException===========================");
            e.printStackTrace();
        } finally {
        }
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
                "      <BARCODE>2004962616130</BARCODE>\n" +
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
            String rtnData = postMethod.getResponseBodyAsString();
            System.out.println("======"+rtnData);
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
     * Order Amendment
     * */
    public void orderAmendment(){
        String soapRequestData = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <soap:Body>\n" +
                "    <OrderAmendment xmlns=\"http://tempuri.org/\">\n" +
                "      <ID_ORDER_WEB>123</ID_ORDER_WEB>\n" +
                "      <ID_CLIENTE_WEB>456</ID_CLIENTE_WEB>\n" +
                "      <BARCODE>2004962616130</BARCODE>\n" +
                "      <QTY>0</QTY>\n" +
                "    </OrderAmendment>\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope>";
        System.out.println("soapRequestData=="+soapRequestData);
        String file = "E:\\OrderAmendment.xml";
        HttpClient httpClient = new HttpClient();
        String uri="http://109.168.12.42/ws_sito_P15/ws_sito_p15.asmx?op=OrderAmendment";
        PostMethod postMethod = new PostMethod(uri);
        postMethod.setRequestHeader("SOAPAction", "http://tempuri.org/OrderAmendment");
        postMethod.setRequestHeader("Content-Type", "text/xml; charset=UTF-8");

        StringRequestEntity requestEntity=new StringRequestEntity(soapRequestData);
        postMethod.setRequestEntity(requestEntity);

        int returnCode=0;
        try {
            returnCode = httpClient.executeMethod(postMethod);
            System.out.println("returnCode=="+returnCode);
            BufferedOutputStream out=new BufferedOutputStream(new FileOutputStream(new File(file)));
            String rtnData = postMethod.getResponseBodyAsString();
            System.out.println("======"+rtnData);
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
     * Get Status Order
     * */
    public void getStatusOrder(){
        String soapRequestData = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <soap:Body>\n" +
                "    <GetStatusOrder xmlns=\"http://tempuri.org/\">\n" +
                "      <CODICE>123</CODICE>\n" +
                "      <ID_CLIENTE>4567</ID_CLIENTE>\n" +
                "    </GetStatusOrder>\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope>";
        System.out.println("soapRequestData=="+soapRequestData);
        String file = "E:\\GetStatusOrder.xml";
        HttpClient httpClient = new HttpClient();
        String uri="http://109.168.12.42/ws_sito_P15/ws_sito_p15.asmx?op=GetStatusOrder";
        PostMethod postMethod = new PostMethod(uri);
        postMethod.setRequestHeader("SOAPAction", "http://tempuri.org/GetStatusOrder");
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
     * Set Status Order
     * */
    public void setStatusOrder(){
        String soapRequestData = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <soap:Body>\n" +
                "    <SetStatusOrder xmlns=\"http://tempuri.org/\">\n" +
                "      <CODICE>123</CODICE>\n" +
                "      <ID_CLIENTE>4567</ID_CLIENTE>\n" +
                "      <ID_STATUS>3</ID_STATUS>\n" +
                "    </SetStatusOrder>\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope>";
        System.out.println("soapRequestData=="+soapRequestData);
        String file = "E:\\SetStatusOrder.xml";
        HttpClient httpClient = new HttpClient();
        String uri="http://109.168.12.42/ws_sito_P15/ws_sito_p15.asmx?op=SetStatusOrder";
        PostMethod postMethod = new PostMethod(uri);
        postMethod.setRequestHeader("SOAPAction", "http://tempuri.org/SetStatusOrder");
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
     *parse file to string
     */
    public  String parseFile2Str(String file) {
    	String str = "";
        StringBuffer sb = new StringBuffer();
        try {
            // 创建文件输入流对象
//            FileInputStream is = new FileInputStream(file);
            BufferedReader br = new  BufferedReader(new InputStreamReader(new FileInputStream(file)));
            
            // 设定读取的字节数
//            int n = 2048;
//            byte buffer[] = new byte[n];
            // 读取输入流
//            while ((is.read(buffer, 0, n) != -1) && (n > 0)) {
//                sb.append(new String(buffer,"utf-8"));
//            }
            while ((str = br.readLine())!=null) {
            	sb.append(str).append("\n");
			}
            // 关闭输入流
//            is.close();
            br.close();
        }  catch (Exception e) {
            System.out.println("parseFile 2Str获取文件字符串失败");
        }
        //System.out.println("output io string==="+sb.toString().length());
        return sb.toString();
    }
    /**
     * test
     * */
    public static void main(String[] args) throws IOException {
    	Map<String, String> param = new HashMap<String,String>();
    	
//    	param.put("ID_ORDER_WEB", "201511301023");
//    	param.put("ID_CLIENTE_WEB", "1");
//    	param.put("DESTINATIONROW1", "");
//		param.put("DESTINATIONROW2", "");
//		param.put("DESTINATIONROW3", "");
//    	param.put("BARCODE", "2115383168402");
//    	param.put("QTY", "1");
//    	param.put("price", "1");
//        String skuData = HttpUtil45.post("http://79.61.138.184/ws_sito/ws_sito_p15.asmx/NewOrder",
//				param,new OutTimeConfig(1000*60*10,1000*60*10,1000*60*10));
    	//http://2.118.242.149/ws_sito/ws_sito_p15.asmx/  http://185.5.180.90/ws_sito/ws_sito_p15.asmx
    	
//    	
//        String priceData = HttpUtil45.post("http://2.118.242.149/ws_sito/ws_sito_p15.asmx/GetAllAvailabilityMarketplace",
//        		param,new OutTimeConfig(1000*60*30,1000*60*30,1000*60*30));
        
        System.out.println("开始获取数据");
//        String post = HttpUtil45.post("http://79.62.242.6:8088/ws_sito/ws_sito_p15.asmx/GetAllItemsMarketplace", new OutTimeConfig(1000*60*120,1000*60*120,1000*60*120));
        String post = HttpUtil45.postAuth("http://95.227.147.192/WS_SITO/WS_SITO_P15.ASMX/GetAllAvailabilityMarketplace", null, new OutTimeConfig(1000*60*120,1000*60*120,1000*60*120), "shangpin", "Daniello0203");
        //        param.put("CODICE", "1066239979");
//        param.put("ID_CLIENTE", "1066239979");
//        param.put("ID_STATUS", "1");
//		String post = HttpUtil45.post("http://79.61.138.184/ws_sito/ws_sito_p15.asmx/SetStatusOrder", param, new OutTimeConfig(1000*60*10,1000*60*10,1000*60*10));
//		System.out.println(priceData);
		System.out.println("开始保存数据");
		File file = new File("E://anielloSPU.txt");
		if (!file.exists()) {
			try {
				file.getParentFile().mkdirs();
				file.createNewFile();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		FileWriter fwriter = null;
		try {
			fwriter = new FileWriter("E://anielloSPU.txt");
			fwriter.write(post);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				fwriter.flush();
				fwriter.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
        
        
    }
}












