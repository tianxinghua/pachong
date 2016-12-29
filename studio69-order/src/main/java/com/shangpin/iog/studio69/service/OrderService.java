package com.shangpin.iog.studio69.service;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.xml.bind.JAXBException;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.transport.http.HttpTransportProperties;
import org.apache.axis2.transport.http.impl.httpclient4.HttpTransportPropertiesImpl.Authenticator;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsOrderService;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.ObjectXMLUtil;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.dto.ReturnOrderDTO;
import com.shangpin.iog.ice.dto.OrderStatus;
import com.shangpin.iog.studio69.dto.Response;
import com.shangpin.iog.studio69.util.API_STUDIO69Stub;
import com.shangpin.iog.studio69.util.API_STUDIO69Stub.CancelOrder;
import com.shangpin.iog.studio69.util.API_STUDIO69Stub.CancelOrderResponse;
import com.shangpin.iog.studio69.util.API_STUDIO69Stub.CreateNewOrder;
import com.shangpin.iog.studio69.util.API_STUDIO69Stub.CreateNewOrderResponse;
import com.shangpin.iog.studio69.util.API_STUDIO69Stub.CreateNewOrderResult_type0;
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
			orderDTO.setExcState("0");
			orderDTO.setStatus(OrderStatus.PLACED); 
			
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
			CreateNewOrder createNewOrder = new CreateNewOrder();
			String size = orderDTO.getDetail().split(",")[0].split(":")[0].split("-")[1];
			String buyerInfo = "<buyerInfo>"
					+ "<Name>Genertec Italia S.r.l.</Name>"
					+ "<Address>Via Leopardi 27</Address>"					
					+ "<zipcode>22075</zipcode>"
					+ "<Corriere>Fedex 0123456789</Corriere>"
					+ "<Notes>"+orderDTO.getSpPurchaseNo()+"</Notes>"
					+ "</buyerInfo>";
			String goodsList = "<GoodsList>"
					+ "<Good>"
					+ "<ID>"+orderDTO.getDetail().split(",")[0].split(":")[0].split("-")[0]+"</ID>"
					+ "<Size>"+size+"</Size>" 
					+ "<Qty>"+orderDTO.getDetail().split(",")[0].split(":")[1]+"</Qty>"
					+ "<Price>"+orderDTO.getPurchasePriceDetail()+"</Price>"
					+ "</Good>"
					+ "</GoodsList>";			
			if(size.contains("+")){
				createNewOrder.setBuyerInfo(buyerInfo);
				createNewOrder.setGoodsList(goodsList);
				createNewOrder.setOrderID(orderDTO.getSpPurchaseNo());
				System.out.println("goodsList================="+orderDTO.getSpPurchaseNo()+":"+goodsList);
				logger.info("goodsList================="+orderDTO.getSpPurchaseNo()+":"+goodsList); 
				Response response = createOrder(orderDTO, createNewOrder);
				//如果尺码为+时下单失败，那么有有可能是½类型的尺码。
				if("Failed".equals(response.getResult()) && "Goods Stock doesn't enough".equals(response.getMessage())){
					size = size.replaceAll("\\+", "½");
					String newGoodsList = "<GoodsList>"
							+ "<Good>"
							+ "<ID>"+orderDTO.getDetail().split(",")[0].split(":")[0].split("-")[0]+"</ID>"
							+ "<Size>"+size+"</Size>" 
							+ "<Qty>"+orderDTO.getDetail().split(",")[0].split(":")[1]+"</Qty>"
							+ "<Price>"+orderDTO.getPurchasePriceDetail()+"</Price>"
							+ "</Good>"
							+ "</GoodsList>";	
					logger.info("含有+号的sku第一次下单失败，第二次下单参数======"+newGoodsList); 
					createNewOrder.setGoodsList(newGoodsList);
					createOrder(orderDTO, createNewOrder);
				}
			}else{				
				createNewOrder.setBuyerInfo(buyerInfo);
				createNewOrder.setGoodsList(goodsList);
				createNewOrder.setOrderID(orderDTO.getSpPurchaseNo());
				System.out.println("goodsList================="+orderDTO.getSpPurchaseNo()+":"+goodsList);
				logger.info("goodsList================="+orderDTO.getSpPurchaseNo()+":"+goodsList); 
				createOrder(orderDTO, createNewOrder);
			}
			
			
		} catch (Exception e) {
			errorLog.error(e);
			orderDTO.setExcDesc(e.getMessage());
			orderDTO.setExcState("1");
			orderDTO.setExcTime(new Date());
		}
				
	}

	private Response createOrder(OrderDTO orderDTO, CreateNewOrder createNewOrder)
			throws AxisFault, RemoteException, JAXBException {
		API_STUDIO69Stub stub = new API_STUDIO69Stub();	
		CreateNewOrderResponse  cresponse = stub.createNewOrder(createNewOrder);
		OMElement   result = cresponse.getCreateNewOrderResult().getExtraElement();
		System.out.println(result.toString());
		logger.info("下单返回结果============="+result.toString());
		Response response = ObjectXMLUtil.xml2Obj(Response.class, result.toString());
		if("Success".equals(response.getResult())){
			orderDTO.setExcState("0");
			orderDTO.setStatus(OrderStatus.CONFIRMED);
			orderDTO.setExcDesc(" "); 
		}else if("Failed".equals(response.getResult()) && "Goods Stock doesn't enough".equals(response.getMessage())){//库存不足
			orderDTO.setExcState("0");
			orderDTO.setStatus(OrderStatus.SHOULD_PURCHASE_EXP);
			orderDTO.setExcDesc(response.getMessage()); 
		}else{
			orderDTO.setExcState("1");
			orderDTO.setExcDesc(response.getMessage());
			orderDTO.setExcTime(new Date()); 
		}
		return response;
	}

	@Override
	public void handleCancelOrder(ReturnOrderDTO deleteOrder) {
		
		try {
			// TODO 退单逻辑
			deleteOrder.setExcState("0"); 
			deleteOrder.setStatus(OrderStatus.CANCELLED); 
			
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
			API_STUDIO69Stub stub = new API_STUDIO69Stub();	
			CancelOrder cancelOrder = new CancelOrder();
			cancelOrder.setOrderID(deleteOrder.getSpPurchaseNo());
			logger.info("退款的订单是==========="+deleteOrder.getSpPurchaseNo()); 
			CancelOrderResponse resonse = stub.cancelOrder(cancelOrder);
			OMElement result = resonse.getCancelOrderResult().getExtraElement();
			System.out.println(result.toString());
			logger.info("取消返回结果==========="+result.toString()); 
			Response response = ObjectXMLUtil.xml2Obj(Response.class, result.toString());
			if("Success".equals(response.getResult())){
				deleteOrder.setExcState("0");
				deleteOrder.setStatus(OrderStatus.REFUNDED);
			}else{
				deleteOrder.setExcState("1");
				deleteOrder.setExcDesc(response.getMessage());
				deleteOrder.setExcTime(new Date()); 
			}
			
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
		
		try {
			//下单=================================
			OrderService order = new OrderService();
			OrderDTO orderDTO = new OrderDTO();
			orderDTO.setDetail("35298-38:1,");
			orderDTO.setSpPurchaseNo("CGDF2016121383941");
			orderDTO.setPurchasePriceDetail("169.98");
			order.handleConfirmOrder(orderDTO); 
			
			
			//取消=================================
//			ReturnOrderDTO deleteOrder = new ReturnOrderDTO();
//			deleteOrder.setSpPurchaseNo("CGD2016103106874");//CGD2016091400169
//			order.handleRefundlOrder(deleteOrder); 
			
			
//			API_STUDIO69Stub stub = new API_STUDIO69Stub();	
//			CancelOrder cancelOrder = new CancelOrder();
//			cancelOrder.setOrderID("CGD2016091200298");
//			CancelOrderResponse resonse = stub.cancelOrder(cancelOrder);
//			OMElement result = resonse.getCancelOrderResult().getExtraElement();
//			System.out.println(result.toString());
//			logger.info("取消返回结果==========="+result.toString()); 
//			String result = "<Response><Result>Failed</Result><Message>order id not exist</Message></Response>";
//			Response response = ObjectXMLUtil.xml2Obj(Response.class, result.toString());
//			
//			System.out.println(response.getResult());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
