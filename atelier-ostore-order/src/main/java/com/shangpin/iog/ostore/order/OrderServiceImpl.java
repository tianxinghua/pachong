package com.shangpin.iog.ostore.order;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsOrderService;
import com.shangpin.iog.common.utils.SendMail;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.dto.ProductDTO;
import com.shangpin.iog.dto.ReturnOrderDTO;
import com.shangpin.iog.ice.dto.OrderStatus;
import com.shangpin.iog.service.ProductSearchService;

@Component("atelierOServiceImpl") 
public class OrderServiceImpl extends AbsOrderService{
	private static Logger logger = Logger.getLogger("info");
	private static Logger errorLog = Logger.getLogger("error");
	private static ResourceBundle bdl = null;
	private static String supplierId = null;
	private static String supplierNo ;
	private static String url = null;
	private static String user = null;
	private static String password = null;
	private static String createOrder_interface = null;
	private static String setStatus_interface = null;
	private static String getStatus_interface = null;
	private static String getItemStock_interface = null;
	
	private static String smtpHost = null;
	private static String from = null;
	private static String fromUserPassword = null;
	private static String to = null;
	private static String messageType = null;
	
	static {
		if(null==bdl){
			bdl=ResourceBundle.getBundle("conf");
		}
		supplierId = bdl.getString("supplierId");
		supplierNo = bdl.getString("supplierNo");
		url = bdl.getString("url");
		user = bdl.getString("user");
		password = bdl.getString("password");
		createOrder_interface = bdl.getString("createOrder_interface");
		setStatus_interface = bdl.getString("setStatus_interface");
		getStatus_interface = bdl.getString("getStatus_interface");
		getItemStock_interface = bdl.getString("getItemStock_interface");
		
		fromUserPassword = bdl.getString("fromUserPassword");
		from = bdl.getString("from");
		to = bdl.getString("to");
		messageType = bdl.getString("messageType");
		smtpHost = bdl.getString("smtpHost");
	}
	
	@Autowired
    ProductSearchService productSearchService;
	
	/**
	 * 下订单
	 */
	public void loopExecute() {
		try {
			this.checkoutOrderFromWMS(supplierNo, supplierId, true);
		} catch (Exception e) {
			errorLog.error(e); 
		}		
	}

	/**
	 * 确认支付
	 */
	public void confirmOrder() {
		try {
			this.confirmOrder(supplierId);
		} catch (Exception e) {
			errorLog.error(e); 
		}
		
	}
	
	@Override
	public void handleSupplierOrder(OrderDTO orderDTO) {
		
		orderDTO.setExcState("0");
		orderDTO.setStatus(OrderStatus.PLACED);		
	}

	@Override
	public void handleConfirmOrder(OrderDTO orderDTO) {
		
		try {
			String spOrderId = orderDTO.getSpOrderId();
			if(spOrderId.contains("-")){
				spOrderId = spOrderId.substring(0, spOrderId.indexOf("-")); 
			}
			long id_order_mrkp = Long.valueOf(spOrderId);
			String skuId = orderDTO.getDetail().split(",")[0].split(":")[0];
			String item_id = skuId.split("-")[0];
			String barcode = skuId.split("-")[1];			
			int qty = Integer.valueOf(orderDTO.getDetail().split(",")[0].split(":")[1]);
			//先通过查询库存接口查询库存,如果库存大于0则下单,否则采购异常
			ProductDTO product = productSearchService.findProductForOrder(supplierId,skuId);
			if(null != product && StringUtils.isNotBlank(product.getSize())){
				String size = product.getSize().replaceAll("\\+", "½");
				//查询对方库存接口
				String stockData = getItemStockBySizeMarketPlace(item_id);
				if(!HttpUtil45.errorResult.equals(stockData)){
					int stock = 0;
					String prex = "<string xmlns=\"http://tempuri.org/\">";
					String end = "</string>";
					String stocks = stockData.substring(stockData.indexOf(prex)+prex.length(), stockData.indexOf(end));
					for(String size_stock : stocks.split("\\|")){
						if(StringUtils.isNotBlank(size_stock)){
							if(size.equals(size_stock.split(";")[0])){
								stock = Integer.parseInt(size_stock.split(";")[1]);
								logger.info("查询到的供货商的库存为============"+stock); 
								break;
							}
						}
					}
					//如果库存大于0,则下单
					if(stock > 0){
						//下单
						String returnData = newOrderMarketPlace(id_order_mrkp,barcode,qty);
						
						if (returnData.contains("OK")) {
							 orderDTO.setExcState("0");				 
							 orderDTO.setStatus(OrderStatus.CONFIRMED);
						} else {
							//推送订单失败
							orderDTO.setExcState("1");
							orderDTO.setExcDesc(returnData);
							orderDTO.setExcTime(new Date()); 				
						}
					}else{
						orderDTO.setExcState("1");
						orderDTO.setExcDesc("库存不足!");
						orderDTO.setExcTime(new Date()); 
						orderDTO.setStatus(OrderStatus.NOHANDLE); 
						sendMail(item_id+" 该产品库存不足!");
					}
				}else{
					orderDTO.setExcState("1");
					orderDTO.setExcDesc("查询对方库存接口失败,"+stockData);
					orderDTO.setExcTime(new Date()); 
					sendMail("查询对方库存接口失败,"+stockData);
				}
			}else{
				orderDTO.setStatus(OrderStatus.NOHANDLE); 
				orderDTO.setExcState("1");
				orderDTO.setExcDesc("查询数据库失败,未找到该商品 "+skuId);
				logger.info("查询数据库失败,未找到该商品=========== "+skuId);
				orderDTO.setExcTime(new Date()); 
				sendMail("查询数据库失败,未找到该商品=========== "+skuId);
			}
			
		} catch (Exception e) {
			orderDTO.setExcDesc(e.getMessage());
			orderDTO.setExcState("1");
			orderDTO.setExcTime(new Date()); 
			errorLog.error(e.toString()); 
			e.printStackTrace();
		}
		
	}

	@Override
	public void handleCancelOrder(ReturnOrderDTO deleteOrder) {
		
		deleteOrder.setExcState("0");
		deleteOrder.setStatus(OrderStatus.CANCELLED);
//		try {			
//			String returnData = setStatusOrderMarketplace(deleteOrder.getSpOrderId(),"CANCELED");
//			if(returnData.contains("OK")){
//				deleteOrder.setExcState("0");
//				deleteOrder.setStatus(OrderStatus.CANCELLED);
//			}else{
//				deleteOrder.setExcState("1");
//				deleteOrder.setExcDesc(returnData);
//				deleteOrder.setExcTime(new Date()); 
//			}			
//			
//		} catch (Exception e) {
//			errorLog.error(e); 
//			deleteOrder.setExcState("1");
//			deleteOrder.setExcDesc(e.getMessage());
//			deleteOrder.setExcTime(new Date()); 
//		}
	}

	@Override
	public void handleRefundlOrder(ReturnOrderDTO deleteOrder) {
		try {
			String spOrderId = deleteOrder.getSpOrderId();
			if(spOrderId.contains("-")){
				spOrderId = spOrderId.substring(0, spOrderId.indexOf("-")); 
			}
			String returnData = setStatusOrderMarketplace(spOrderId,"CANCELED");
			if(returnData.contains("OK")){
				deleteOrder.setExcState("0");
				deleteOrder.setStatus(OrderStatus.REFUNDED);
			}else{
				deleteOrder.setExcState("1");
				deleteOrder.setExcDesc(returnData);
				deleteOrder.setExcTime(new Date()); 
			}				
		} catch (Exception e) {
			errorLog.error(e); 
			deleteOrder.setExcState("1");
			deleteOrder.setExcDesc(e.getMessage());
			deleteOrder.setExcTime(new Date()); 
		}
		
	}

	@Override
	public void handleEmail(OrderDTO orderDTO) {
		
	}

	@Override
	public void getSupplierSkuId(Map<String, String> skuMap)
			throws ServiceException {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 根据spu查询供货商库存
	 * @param item_id
	 * @return
	 */
	private String getItemStockBySizeMarketPlace(String item_id) throws Exception{
		Map<String,String> param = new HashMap<String,String>();
		param.put("ITEM_ID", item_id);
		logger.info("查询库存参数============"+param.toString());
		String returnData = HttpUtil45.postAuth(url+getItemStock_interface, param, new OutTimeConfig(1000*60*10,1000*60*10,1000*60*10),user,password);
		logger.info("查询库存返回结果======="+returnData); 
		return returnData;
	}
	
	/**
	 * 下订单新接口
	 * @param id_order_mrkp 订单号
	 * @param barcode Barcode
	 * @param qty 库存
	 * @return
	 */
	private String newOrderMarketPlace(long id_order_mrkp,String barcode,int qty) throws Exception{
		Map<String,String> param = new HashMap<String,String>();
		param.put("ID_ORDER_MRKP", String.valueOf(id_order_mrkp));
		param.put("BARCODE", barcode);
		param.put("QTY", String.valueOf(qty));
		logger.info("下单参数============"+param.toString());
		String returnData = HttpUtil45.postAuth(url+createOrder_interface, param, new OutTimeConfig(1000*60*10,1000*60*10,1000*60*10),user,password);
		logger.info("下订单返回结果======="+returnData);
		return returnData;
	}
	
	/**
	 * 设置订单状态 
	 * @param code 订单号
	 * @param status 可取值为:NEW, PROCESSING, SHIPPED, CANCELED
	 */
	private String setStatusOrderMarketplace(String code,String status) throws Exception{
		Map<String,String> param = new HashMap<String,String>();
		param.put("CODE", code);
		param.put("STATUS", status);//NEW PROCESSING SHIPPED CANCELED (for delete ORDER)
		logger.info("设置订单参数======="+param.toString());
		String returnData = HttpUtil45.postAuth(url+setStatus_interface, param, new OutTimeConfig(1000*60*10,1000*60*10,1000*60*10),user,password);
		logger.info("设置订单状态返回结果======="+returnData); 
		return returnData;
	}
	
	/**
	 * 获取订单状态
	 * @param code 订单编号
	 * @return
	 */
	private String getStatusOrderMarketplace(String code) throws Exception{
		Map<String,String> param = new HashMap<String,String>();
		param.put("CODE", code);
		logger.info("查询的订单号为======"+code);
		String returnData = HttpUtil45.postAuth(url+getStatus_interface, param, new OutTimeConfig(1000*60*10,1000*60*10,1000*60*10),user,password);
		logger.info("查询返回结果======="+returnData);
		return returnData;
	}
	
	/**
	 * 发送邮件
	 * @param messageText 邮件正文
	 */
	private void sendMail(String messageText){
		try {
			
			SendMail.sendGroupMail(smtpHost, from, fromUserPassword, to, "!!! O订单推送异常", messageText , messageType);
			
		} catch (Exception e) {
			errorLog.error(e.toString()); 
		}
	}
	
	public static void main(String[] args) {
		OrderServiceImpl orderService = new OrderServiceImpl();
		OrderDTO orderDTO = new OrderDTO();
//    	orderDTO.setSpPurchaseNo("CGD2016072500097");
		orderDTO.setSpOrderId("201607254074169");
    	orderDTO.setDetail("6794202-2107151150283:1,");
//    	orderDTO.setPurchasePriceDetail("170.41");
    	orderService.handleConfirmOrder(orderDTO);
		//201607284050007L, "2111344053718"
		//201607284050011L, "2016398420885"
//		orderService.newOrderMarketPlace(201607284050015L, "2109449363719", 1);
//		orderService.getStatusOrderMarketplace("201607284050011");
//		orderService.setStatusOrderMarketplace("201607284050015", "CANCELED"); 
//		orderService.getStatusOrderMarketplace("201607284050011");
		
//		orderService.getItemStockBySizeMarketPlace("384938");
	}
	
	
}
