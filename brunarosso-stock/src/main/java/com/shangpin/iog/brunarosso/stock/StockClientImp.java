/**
 * Created by huxia on 2015/6/10.
 */
package com.shangpin.iog.brunarosso.stock;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.brunarosso.utils.XmlReader;
import com.shangpin.iog.common.utils.ftp.ReconciliationFtpUtil;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;

import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.log4j.Logger;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jdom2.input.SAXBuilder;


import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;


import java.util.*;

public class StockClientImp extends AbsUpdateProductStock{
    private static Logger logger = Logger.getLogger("info");
    public static void grabStockDynamic() throws ServiceException {
        //Map<String, Integer> skustock = new HashMap<>(skuNo.size());
        String wsdlUrl="http://85.159.181.250/ws_sito/ws_sito.asmx";//DisponibilitaVarianteTaglia
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        org.apache.cxf.endpoint.Client client = dcf.createClient(wsdlUrl+"?wsdl");
        try {
            Object[]obj=client.invoke("DisponibilitaVarianteTaglia", "6759079","");
            System.out.println(obj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
       // return skustock;
    }
    public Map<String,Integer>getStockStatic(Collection<String> skuNo)throws ServiceException{
        Map<String, Integer> skustock = new HashMap<>(skuNo.size());
        JaxWsProxyFactoryBean svr = new JaxWsProxyFactoryBean();
        String realUrl="http://85.159.181.250/ws_sito/ws_sito.asmx";
       /* svr.setServiceClass(HelloWorld.class);*/
        svr.setAddress(realUrl);
        return skustock;
    }
    public static String getStock(String id,String size) throws DocumentException {
        String url="http://85.159.181.250/ws_sito/ws_sito.asmx/DisponibilitaVarianteTaglia";
        Map<String,String> param = new HashMap<>();
        param.put("ID_ARTICOLO",id);
        param.put("TAGLIA",size);
        OutTimeConfig outTimeConf = new OutTimeConfig();
        String result = HttpUtil45.post(url,param,outTimeConf);
        Document doc = DocumentHelper.parseText(result);
        Element element=doc.getRootElement();
        return element.getText();
    }
    public static void main(String[] args) throws Exception {
        getStock("-1058993","35");
    }

    @Override
    public Map<String, Integer> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
        /*ReconciliationFtpUtil.download("","","E:\\brunarosso",false);
        Map<String, Integer> skuStock= new HashMap<>(skuNo.size());
        Map<String,List<String>> map = XmlReader.getSizeByPath("");
        for (Iterator<String> it=skuNo.iterator();it.hasNext();){
            String[] skuAndSize=it.next().split(",");
            String skuId = skuAndSize[0];
        }*/

        return null;
    }
    public Collection<String> grabProduct(String supplier,String start,String end,Map<String,String> stocks) throws Exception{

        //Map<String,List<String>>map=getSizeByPath("");
        return null;
    }
    public static Map<String,Integer> getSizeByPath(String url){
        Map<String,Integer>map = new HashMap();
            //File file=list.get(i);
            try {
                System.out.println("正在读取的尺寸文件: " + url);
                getMap("E:\\brunarosso" + url, map);
            } catch (Exception e) {
                e.printStackTrace();
            }
        return map;
    }
    public static Map<String,Integer> getMap(String url, Map<String, Integer> map){
        List<org.jdom2.Element> allChildren = new ArrayList();
        try {
            SAXBuilder builder = new SAXBuilder();
            org.jdom2.Document doc = builder.build(new File(url));//"E:/MailDoc/firma/Disponibilita.xml"
            org.jdom2.Element foo =doc.getRootElement();
            allChildren = foo.getChildren();
            for (org.jdom2.Element element:allChildren){
               /* if (map.containsKey(element.getChildText("ID_ARTICOLO"))){
                   // map.get(element.getChildText("ID_ARTICOLO")).add(element.getChildText("MM_TAGLIA"));
                }else{
                    List<String> valueList = new ArrayList();
                    valueList.add(element.getChildText("MM_TAGLIA"));
                   // map.put(element.getChildText("ID_ARTICOLO"),valueList);
                }*/
                map.put(element.getChildText("ID_ARTICOLO")+","+element.getChildText("MM_TAGLIA"),Integer.parseInt(element.getChildText("MM_TAGLIA")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
