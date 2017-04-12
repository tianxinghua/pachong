package com.shangpin.iog.itemInfo.stock.service;

import java.util.Collection;
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
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.common.utils.logger.LoggerUtil;
import com.shangpin.iog.itemInfo.stock.schedule.AppContext;
import com.shangpin.iog.itemInfo.stock.utils.SoapXmlUtil;

@Component("grabStockImp")
public class GrabStockImp extends AbsUpdateProductStock {

	private static Logger logger = Logger.getLogger("info");
//    private static Logger loggerError = Logger.getLogger("error");
	private static LoggerUtil logError = LoggerUtil.getLogger("error");
    private static ResourceBundle bdl=null;
//    private static String supplierId = "";
//    private static String soapRequestData = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
//								            "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
//								            "  <soap12:Body>\n" +
//								            "    <GetAllItems xmlns=\"http://tempuri.org/\" />\n" +
//								            "  </soap12:Body>\n" +
//								            "</soap12:Envelope>";
//    private static String serviceUrl = "http://service.alducadaosta.com/EcSrv/Services.asmx?op=GetItem4Platform";
//    private static String soapAction = "http://service.alducadaosta.com/EcSrv/GetItem4Platform";
    private static String soapRequestData ="<?xml version=\"1.0\" encoding=\"utf-8\"?>"+
											"<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">"+
											  "<soap12:Body>"+
											    "<GetSku4Platform xmlns=\"http://service.alducadaosta.com/EcSrv\">"+
											      "<Customer>shangpin</Customer>"+
											    "</GetSku4Platform>"+
											  "</soap12:Body>"+
											"</soap12:Envelope>";
    private static String serviceUrl ="http://service.alducadaosta.com/EcSrv/Services.asmx?op=GetSku4Platform";
    private static String soapAction ="http://service.alducadaosta.com/EcSrv/GetSku4Platform";
    private static String contentType = "text/xml; charset=utf-8";
    private static String localPathDefault = "";
    
    static {
        if(null==bdl)
         bdl=ResourceBundle.getBundle("conf");
//        supplierId = bdl.getString("supplierId");
        localPathDefault = bdl.getString("localPathDefualt");
    }

	@Override
	public Map<String, String> grabStock(Collection<String> skuNo)
			throws ServiceException {
		
		Map<String, String> skustock = new HashMap<>();
		Map<String,String> stockMap = new HashMap<>();
		String filePath = null;
		try{
			logger.info("开始下载文件，文件保存在============="+localPathDefault); 

			filePath = SoapXmlUtil.downloadSoapXmlAsFile(serviceUrl,soapAction,contentType,soapRequestData,localPathDefault);

		}catch(Exception ex){
			logger.info("第一次下载失败，正在尝试第二次================"+ex.toString()); 
			System.out.println("下载失败，正在重新下载...");
			try{

				filePath = SoapXmlUtil.downloadSoapXmlAsFile(serviceUrl,soapAction,contentType,soapRequestData,localPathDefault);

			}catch(Exception e){
				e.printStackTrace();
				logger.info("第二次拉取alduca daosta数据失败---" + e.getMessage());
				logError.error("拉取alduca daosta数据失败---" + e.getMessage());
				return skustock;
			}
		}
//		String filePath = "d:/soapml.xml";
		if(filePath != null){
			logger.info("拉取alduca daosta数据成功");
			//获取更新文件信息
			SOAPMessage soapMessage = SoapXmlUtil.formatSoapStringByFilePath(filePath);
			if(soapMessage != null){
				SOAPBody body = null;
				try {
					body = soapMessage.getSOAPBody();
					Iterator<SOAPElement> iterator = body.getChildElements();
					getSkuStockCollection(iterator,stockMap);
					
				} catch (SOAPException e) {
					e.printStackTrace();
					logger.error("解析出错："+e.getMessage(),e);
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
	
	/**
	 * 返回抓取的sku-stock map
	 * @param iterator
	 * @param stockMap
	 */
	public void getSkuStockCollection(Iterator<SOAPElement> iterator,Map<String,String> stockMap){
		
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
				getSkuStockCollection(element.getChildElements(),stockMap);
			}
		}
	}
	
	@SuppressWarnings("unused")
	private static ApplicationContext factory;
    private static void loadSpringContext()
    {

        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }
	
	public static void main(String[] args) throws Exception{
		new GrabStockImp().grabStock(null);
		//加载spring
        loadSpringContext();
        
	} 

}
