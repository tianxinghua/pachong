package com.shangpin.iog.itemInfo.stock.service;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.itemInfo.utils.SoapXmlUtil;

@Component("amandaStock")
public class GrabStockImp extends AbsUpdateProductStock {

	private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    private static Logger logMongo = Logger.getLogger("mongodb");
    private static ResourceBundle bdl=null;
    private static String supplierId = "201509301515";
    private static String soapRequestData = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
								            "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
								            "  <soap12:Body>\n" +
								            "    <GetAllItems xmlns=\"http://tempuri.org/\" />\n" +
								            "  </soap12:Body>\n" +
								            "</soap12:Envelope>";
    private static String serviceUrl = "http://service.alducadaosta.com/EcSrv/Services.asmx?op=GetItem4Platform";
    private static String soapAction = "http://service.alducadaosta.com/EcSrv/GetItem4Platform";
    private static String contentType = "text/xml; charset=utf-8";
    private static String localPath = "D:\\soapxml.xml";
    
    static {
        if(null==bdl)
         bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
    }

	@Override
	public Map<String, String> grabStock(Collection<String> skuNo)
			throws ServiceException {
		
		Map<String, String> skustock = new HashMap<>();
		Map<String,String> stockMap = new HashMap<>();
		Map<String,String> mongMap = new HashMap<>();
		mongMap.put("supplierId",supplierId);
        mongMap.put("supplierName","alduca daosta");
        mongMap.put("result","") ;
        try {
            logMongo.info(mongMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
		String filePath = null;
		try{
			
			filePath = SoapXmlUtil.downloadSoapXmlAsFile(serviceUrl,soapAction,contentType,soapRequestData,localPath);
			
		}catch(Exception ex){
			ex.printStackTrace();
			System.out.println("下载失败，正在重新下载...");
			try{
				
				filePath = SoapXmlUtil.downloadSoapXmlAsFile(serviceUrl,soapAction,contentType,soapRequestData,localPath);
				
			}catch(Exception e){
				e.printStackTrace();
				loggerError.error("拉取alduca daosta数据失败---" + e.getMessage());
			}
		}	
		if(filePath != null){
			logger.info("拉取alduca daosta数据成功");
			//获取更新文件信息
			SOAPMessage soapMessage = SoapXmlUtil.formatSoapStringByFilePath(filePath);
			if(soapMessage != null){
				SOAPBody body;
				try {
					body = soapMessage.getSOAPBody();
					Iterator<SOAPElement> iterator = body.getChildElements();
					while(iterator.hasNext()){
						SOAPElement element = iterator.next();
						if(element.getNodeName().equals("SkuStok") && element.getChildElements().hasNext()){
							Iterator<SOAPElement> iteratorSku = element.getChildElements();
							String skuId = "";
							String stock = "";
							while(iteratorSku.hasNext()){
								SOAPElement elementSku = iteratorSku.next();
								String nodeName = elementSku.getNodeName();
								if(nodeName.equals("sku_id")){  //必填
									if(elementSku.getValue() != null){
										skuId = elementSku.getValue();
									}else{
										break;
									}
								}else if(nodeName.equals("stock")){  //必填
									if(elementSku.getValue() != null){
										stock = elementSku.getValue();
									}else{
										break;
									}
								}
							}
							if(StringUtils.isNotBlank(skuId) && StringUtils.isNotBlank(stock)){
								stockMap.put(skuId, stock);
							}
							
						}else if(element.getChildElements().hasNext()){
							continue;
						}
					}
				} catch (SOAPException e) {
					e.printStackTrace();
				}
				
			}
			
			//更新库存
			for (String skuno : skuNo) {

	            if(stockMap.containsKey(skuno)){
	                skustock.put(skuno, stockMap.get(skuno));
	            } else{
	                skustock.put(skuno, "0");
	            }
	        }
			logger.info("alduca daosta更新数据库成功！");
		}
		
		return skustock;
	}
	
	public static void main(String[] args) throws Exception{
		
		AbsUpdateProductStock grabStockImp = new GrabStockImp();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        logger.info("alduca daosta更新数据库开始");
        grabStockImp.updateProductStock(supplierId,"2015-01-01 00:00",format.format(new Date()));
        logger.info("alduca daosta更新数据库结束");
        System.exit(0);
	} 

}
