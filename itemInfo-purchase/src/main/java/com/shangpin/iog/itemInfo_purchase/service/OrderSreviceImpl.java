package com.shangpin.iog.itemInfo_purchase.service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPMessage;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsOrderService;
import com.shangpin.ice.ice.OrderService;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.dto.ReturnOrderDTO;
import com.shangpin.iog.ice.dto.OrderStatus;
import com.shangpin.iog.itemInfo.utils.SoapXmlUtil;

/**
 * 下订单类，并且完成在线验证
 * @author sunny
 *
 */
@Component("amandaOrder")
public class OrderSreviceImpl extends AbsOrderService {
	
	@Autowired
    com.shangpin.iog.service.OrderService productOrderService;
	
	private static String serviceUrl = "http://service.alducadaosta.com/EcSrv/Services.asmx?op=InsertOrder";
	private static String sopAction = "http://service.alducadaosta.com/EcSrv/InsertOrder";
	private static String contentType = "text/xml; charset=utf-8"; //或者    application/soap+xml; charset=utf-8
	private static String soapRequestData = "";
	
	private static ResourceBundle bdl=null;
    private static String supplierId;
    private static String key ;
    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        key = bdl.getString("key");
    }
    
    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    private static Logger logMongo = Logger.getLogger("mongodb");
	
    public void start(){
    	this.checkoutOrderFromSOP(supplierId, "", false);
    }
	
	@Override
	public void handleSupplierOrder(OrderDTO spOrder) {
		String order = spOrder.getSpOrderId();
		try{
			
			String[] sku_stocks = spOrder.getDetail().split(",");
			for(String sku_stock : sku_stocks){
				String[] tem = sku_stock.split(":");
				soapRequestData =  "<?xml version=\"1.0\" encoding=\"utf-8\"?>"+
									"<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">"+
									"  <soap12:Body>"+
									"    <InsertOrder xmlns=\"http://service.alducadaosta.com/EcSrv\">"+
									"      <Identifier>Mosu</Identifier>"+
									"      <Order>"+order+"</Order>"+     //"+order+"
									"      <SkuID>"+tem[0].trim()+"</SkuID>"+
									"      <Quantity>"+tem[1].trim()+"</Quantity>"+  //
									"    </InsertOrder>"+
									"  </soap12:Body>"+
									"</soap12:Envelope>";
				System.out.println(soapRequestData);
				try{
					
					String str = SoapXmlUtil.getSoapXml(serviceUrl, sopAction, contentType, soapRequestData);
					System.out.println("str=====" +str);
					if(str != null){
						/*SOAPMessage soapMessage = SoapXmlUtil.formatSoapString(str);
						if(soapMessage != null){
							NodeList nodeList = soapMessage.getSOAPBody().getChildNodes();
							for(int i=0;i<nodeList.getLength();i++){
								Node element = nodeList.item(i);
								if(element.getNodeName().equals("InsertOrderResult")){
									String retMessage = element.getNodeValue();
									if(retMessage.equals("OK")){//下单成功
										spOrder.setStatus(OrderStatus.PAYED);
										System.out.println("下单成功*********");
									}else if(retMessage.equals("Fail, No Stock!")){//下单失败，无库存
										logger.info("下单失败：该商品没有库存");
									}else if(retMessage.equals("OK, but only: 1")){//下单失败，库存不够
										logger.info("下单失败：库存只有 1 件 ");
									}else{
										logger.info("下单失败："+retMessage);
									}
									break;
								}
							}
						}*/
						
						String startStr = "<InsertOrderResult>";
						String endStr = "</InsertOrderResult>";
						String retMessage = str.substring(str.indexOf(startStr)+startStr.length(), str.indexOf(endStr));
						if(retMessage.equals("OK")){//下单成功
							spOrder.setStatus(OrderStatus.PAYED);
							System.out.println("下单成功*********");
						}else if(retMessage.equals("Fail, No Stock!")){//下单失败，无库存
							logger.info("下单失败：该商品没有库存");
						}else if(retMessage.equals("OK, but only: 1")){//下单失败，库存不够
							logger.info("下单失败：库存只有 1 件 ");
						}else{
							logger.info("下单失败："+retMessage);
						}
					}else{
						spOrder.setExcState("1");
						spOrder.setExcDesc("采购单："+order+" 在请求验证时发生错误，未返回响应信息");
						loggerError.error("采购单："+order+" 在请求验证时发生错误，未返回响应信息");
					}
					
				}catch(Exception e){
					//下单失败
					spOrder.setExcState("1");
					spOrder.setExcDesc("未知错误...\n"+e.getMessage());
		            loggerError.error("采购单："+order+" 下单返回转化失败");
		            e.printStackTrace();
				}
			}
			
		}catch(Exception ex){
			//下单失败
			spOrder.setExcState("1");
			spOrder.setExcDesc("未知错误...\n"+ex.getMessage());
            loggerError.error("采购单："+order+" 下单返回转化失败");
            ex.printStackTrace();
		}
	}
	

	/**
	 * 在线推送订单，已支付
	 */
	@Override
	public void handleConfirmOrder(OrderDTO spOrder) {
		
		
	}

	@Override
	public void handleCancelOrder(ReturnOrderDTO deleteOrder) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getSupplierSkuId(Map<String, String> skuMap)
			throws ServiceException {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args){
		OrderSreviceImpl o = new OrderSreviceImpl();
		OrderDTO spOrder = new OrderDTO();
		spOrder.setSpOrderId("Mosu Test");
		spOrder.setDetail("910042603901:1");
		o.handleSupplierOrder(spOrder);
	}
}
