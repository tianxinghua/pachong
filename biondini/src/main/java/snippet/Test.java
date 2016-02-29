package snippet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.rmi.Remote;
import java.util.Iterator;

import javax.xml.namespace.QName;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;


public class Test {
public static void main(String[] args) {
	getAllAvailabilityMarketplaceBySoap();
}
	public static void getAllAvailabilityMarketplaceBySoap(){
        System.out.println("getAllAvailabilityMarketplace--------------------------------");
//        String soapRequestData = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
//                "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
//                "  <soap12:Body>\n" +
//                "    <GetAllAvailabilityMarketplace xmlns=\"http://tempuri.org/\" />\n" +
//                "  </soap12:Body>\n" +
//                "</soap12:Envelope>";
        
        
        String js = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" 
        + "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" 
        	+"xmlns:xsi=\"http://www.w3.org/1999/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/1999/XMLSchema\">"+
        		"<soap:Body>"
        		+ "<LecturePLA xmlns=\"http://80.12.82.220:8080/LCVMAGWS_WEB\"><bCData xsd:type=\"xsd:string\"></bCData>"
        	+"<sUser xsd:type=\"xsd:string\">BION456</sUser><sMdp xsd:type=\"xsd:string\">INI123 </sMdp></LecturePLA></soap:Body>"
        + "</soap:Envelope>";
        HttpClient httpClient = new HttpClient();
        String uri="http://80.12.82.220:8080/lcvmagws/LecturePLA.htm";
        PostMethod postMethod = new PostMethod(uri);
        postMethod.setRequestHeader("Content-Type", "application/soap+xml; charset=utf-8");
/*
 * <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" xmlns:xsi="http://www.w3.org/1999/XMLSchema-instance" 
 * xmlns:xsd="http://www.w3.org/1999/XMLSchema">
 * <soap:Body><EcritureVenteSKU xmlns="urn:LcvMagWS">
 * <sVente xsd:type="xsd:string"></sVente><sEncaissement xsd:type="xsd:string"></sEncaissement><
 * sTVA xsd:type="xsd:string"></sTVA><sMagasin xsd:type="xsd:string"></sMagasin><sDate xsd:type="xsd:string">
 * </sDate><scodeCli xsd:type="xsd:string"></scodeCli><sUser xsd:type="xsd:string"></sUser><sMdp xsd:type="xsd:string">
 * </sMdp></EcritureVenteSKU></soap:Body></soap:Envelope>
 * 
 * 
 * 
 * */
        //System.out.println("soapRequestData=="+soapRequestData);
        StringRequestEntity requestEntity=new StringRequestEntity(js);
        postMethod.setRequestEntity(requestEntity);

        int returnCode=0;
        try {
            returnCode = httpClient.executeMethod(postMethod);
            System.out.println("returnCode=="+returnCode);
            BufferedOutputStream out=new BufferedOutputStream(new FileOutputStream(new File("C://kkk.xml")));
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

}
