package com.shangpin.iog.studio69.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.axis2.context.ConfigurationContext;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsOrderService;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.dto.ReturnOrderDTO;
import com.shangpin.iog.ice.dto.OrderStatus;
import com.shangpin.iog.studio69.util.API_STUDIO69Stub;
import com.shangpin.iog.studio69.util.API_STUDIO69Stub.CreateNewOrder;
import com.shangpin.iog.studio69.util.API_STUDIO69Stub.CreateNewOrderResponse;
import com.shangpin.iog.studio69.util.SoapXmlUtil;

/**
 * Created by lubaijiang
 */

@Component
public class OrderService extends AbsOrderService{

	private static Logger logger = Logger.getLogger("info");
	private static Logger errorLog = Logger.getLogger("error");
	private static OutTimeConfig outTimeConf = new OutTimeConfig(1000*60*5, 1000*60 * 5, 1000*60 * 5);
	
	private static ResourceBundle bdl=null;
    private static String supplierId = "";
    private static String supplierNo = "";
    
    private static String url = null;
    private static String username = null;
    private static String password = null;
       
    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        supplierNo = bdl.getString("supplierNo");
        
        url = bdl.getString("url");
        username = bdl.getString("username");
        password = bdl.getString("password");
    }
	
	/**
	 * 下订单
	 */
	public void loopExecute() {
		this.checkoutOrderFromWMS(supplierNo, supplierId, true);
	}

	/**
	 * 确认支付
	 */
	public void confirmOrder() {
		this.confirmOrder(supplierId);
	}
	
	@Override
	public void handleSupplierOrder(OrderDTO orderDTO) {
			
		try {
			// TODO 锁库存逻辑
			
			
		} catch (Exception e) {
			errorLog.error(e);
			orderDTO.setExcDesc(e.getMessage());
			orderDTO.setExcState("1");
			orderDTO.setExcTime(new Date());
		}
		
	}
	
	@Override
	public void handleConfirmOrder(OrderDTO orderDTO) {
		
		try {
			// TODO 支付逻辑
			
//			Map<String, String> map = new HashMap<String, String>();
//			map.put("SOAPAction", "http://tempuri.org/CreateNewOrder");
//			map.put("Content-Type", "text/xml; charset=UTF-8");
//			map.put("Content-Type", "application/soap+xml; charset=utf-8");
//			System.out.println("=================tables fetch begin====================================");
//			try {
//				json = HttpUtil45
//						.operateData(
//								"post",
//								"soap",
//								"http://studio69.atelier98.net/api_studio69/api_studio69.asmx?op=CreateNewOrder",
//								new OutTimeConfig(1000 * 60 * 10, 1000 * 60 * 10,
//										1000 * 60 * 10), map, xx, username, password);
//				System.out.println("tables ："+json.length());
//				System.out.println("=================tables fetch end====================================");
//				logger.info("tables ："+json.length());
//				logger.info("=================tables fetch end====================================");
//			} catch (ServiceException e) {
//				e.printStackTrace();
//			}
//			String sopAction = "http://tempuri.org/CreateNewOrder";
//			String contentType = "text/xml; charset=UTF-8";
//			String soapRequestData = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"+
//					"<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n"+
//					"  <soap:Body>\n"+
//					"    <CreateNewOrder xmlns=\"http://tempuri.org/\">\n"+
//					"      <OrderID>201607204043660</OrderID>\n"+
//					"      <BuyerInfo>"
//					+ "<Name>test</Name>"
//					+ "<Address>shangpin</Address>"					
//					+ "<zipcode>100000</zipcode>"
//					+ "<Corriere></Corriere>"
//					+ "<Notes></Notes>"
//					+ "</BuyerInfo>\n"+
//					"<GoodsList>"
//					+ "<Good>"
//					+ "<ID>65128</ID>"
//					+ "<Size>UNI</Size>"
//					+ "<Qty>1</Qty>"
//					+ "<Price>1265</Price>"
//					+ "</Good>"
//					+ "</GoodsList>\n"+
//					"</CreateNewOrder>\n"+
//					"</soap:Body>\n"+
//					"</soap:Envelope>";
//			String url = "http://studio69.atelier98.net/api_studio69/api_studio69.asmx?op=CreateNewOrder";
//			String sss = SoapXmlUtil.getSoapXml(url, sopAction, contentType, soapRequestData, username, password);
//			System.out.println(sss); 
		
			String buyerInfo = "<BuyerInfo>"
					+ "<Name>test</Name>"
					+ "<Address>shangpin</Address>"
					+ "<Corriere></Corriere>"
					+ "<zipcode>100000</zipcode>"
					+ "<Notes></Notes>"
					+ "</BuyerInfo>";
			String goodsList = "<GoodsList>"
					+ "<Good>"
					+ "<ID>65128</ID>"
					+ "<Size>UNI</Size>"
					+ "<Qty>1</Qty>"
					+ "<Price>1265</Price>"
					+ "</Good>"
					+ "</GoodsList>";
			
//			CreateNewOrder createNewOrder82 =  new CreateNewOrder();
//			createNewOrder82.setOrderID("201607204043660");
//			createNewOrder82.setBuyerInfo(buyerInfo);
//			createNewOrder82.setGoodsList(goodsList);
//			
//			ConfigurationContext configurationContext =  new ConfigurationContext(null);
//			API_STUDIO69Stub api = new API_STUDIO69Stub();
//			
//			CreateNewOrderResponse response = api.createNewOrder(createNewOrder82);
//			
//			System.out.println(response); 
//			logger.info(response);
			
			Map<String,String> param = new HashMap<String,String>();
			param.put("orderID", "201607204043660");
			param.put("buyerInfo", buyerInfo);
			param.put("goodsList", goodsList);
			String data = HttpUtil45.postAuth(url+"CreateNewOrder", param, outTimeConf, username, password);
			logger.info(data); 
			System.out.println(data); 
			
		} catch (Exception e) {
			errorLog.error(e);
			orderDTO.setExcDesc(e.getMessage());
			orderDTO.setExcState("1");
			orderDTO.setExcTime(new Date());
		}
				
	}

	@Override
	public void handleCancelOrder(ReturnOrderDTO deleteOrder) {
		
		try {
			// TODO 退单逻辑
			
			
		} catch (Exception e) {
			errorLog.error(e);
			deleteOrder.setExcDesc(e.getMessage());
			deleteOrder.setExcState("1");
			deleteOrder.setExcTime(new Date());
		}
				
	}

	@Override
	public void handleRefundlOrder(ReturnOrderDTO deleteOrder) {
		
		try {
			// TODO 退款逻辑
			
			
		} catch (Exception e) {
			errorLog.error(e);
			deleteOrder.setExcDesc(e.getMessage());
			deleteOrder.setExcState("1");
			deleteOrder.setExcTime(new Date());
		}
		
	}

	@Override
	public void handleEmail(OrderDTO orderDTO) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getSupplierSkuId(Map<String, String> skuMap)
			throws ServiceException {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) {
		OrderService o =  new OrderService();
		o.handleConfirmOrder(null);
	}

}
