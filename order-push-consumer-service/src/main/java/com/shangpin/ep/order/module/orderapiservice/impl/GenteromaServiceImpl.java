package com.shangpin.ep.order.module.orderapiservice.impl;

import com.shangpin.ep.order.common.HandleException;
import com.shangpin.ep.order.common.LogCommon;
import com.shangpin.ep.order.conf.supplier.SupplierProperties;
import com.shangpin.ep.order.enumeration.ErrorStatus;
import com.shangpin.ep.order.enumeration.LogTypeStatus;
import com.shangpin.ep.order.enumeration.PushStatus;
import com.shangpin.ep.order.module.order.bean.OrderDTO;
import com.shangpin.ep.order.module.orderapiservice.IOrderService;
import com.shangpin.ep.order.module.sku.bean.HubSkuCriteria;
import com.shangpin.ep.order.module.sku.mapper.HubSkuMapper;
import com.shangpin.ep.order.util.httpclient.HttpUtil45;
import com.shangpin.ep.order.util.httpclient.OutTimeConfig;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component("genteromaServiceImpl")
public class GenteromaServiceImpl implements IOrderService {
	
	@Autowired
    LogCommon logCommon;    
    @Autowired
    SupplierProperties supplierProperties;
    @Autowired
    HandleException handleException;
    @Autowired
    HubSkuMapper skuDAO;
    
    /**
     * 给对方推送数据
     * @param url
     * @param param
     * @param outTimeConf
     * @param userName
     * @param password
     * @return
     * @throws Exception
     */
    public String genteromaPost(String url, Map<String,String> param, OutTimeConfig outTimeConf, String userName, String password,OrderDTO order) throws Exception{
    	return HttpUtil45.postAuth(url, param, outTimeConf, userName, password);
    }
    
    public String handleException(String url, Map<String,String> param, OutTimeConfig outTimeConf, String userName, String password,OrderDTO order,Throwable e){
    	handleException.handleException(order, e); 
		return null;
    }
	
    /**
     * 根据供应商门户编号和供应商skuid查找尺码
     * @param supplierId
     * @param supplierSkuId
     * @return
     */
    public String getProductSize(String supplierId,String supplierSkuId){
    	try {
    		HubSkuCriteria skuCriteria  = new HubSkuCriteria();
        	skuCriteria.createCriteria().andSupplierIdEqualTo(supplierId).andSkuIdEqualTo(supplierSkuId);
        	skuCriteria.setFields("PRODUCT_SIZE");
        	return skuDAO.selectByExample(skuCriteria).get(0).getProductSize();
		} catch (Exception e) {			
			return "";
		}
    	
    }
    
	@Override
	public void handleSupplierOrder(OrderDTO orderDTO) {
		orderDTO.setLockStockTime(new Date());
		orderDTO.setPushStatus(PushStatus.NO_LOCK_API);
		orderDTO.setLogContent("------锁库结束-------");
		logCommon.loggerOrder(orderDTO, LogTypeStatus.LOCK_LOG);
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
			String productSize = getProductSize(orderDTO.getSupplierId(),skuId);
			if(StringUtils.isNotBlank(productSize)){
				String size = productSize.replaceAll("\\+", "½");				
				//查询对方库存接口				
				String stockData = getItemStockBySizeMarketPlace(item_id,orderDTO);
				
				if(!HttpUtil45.errorResult.equals(stockData)){
					int stock = 0;
					String prex = "<string xmlns=\"http://tempuri.org/\">";
					String end = "</string>";
					String stocks = stockData.substring(stockData.indexOf(prex)+prex.length(), stockData.indexOf(end));
					for(String size_stock : stocks.split("\\|")){
						if(StringUtils.isNotBlank(size_stock)){
							if(size.equals(size_stock.split(";")[0])){
								stock = Integer.parseInt(size_stock.split(";")[1]);
								orderDTO.setLogContent("查询到的供货商的库存为============"+stock);
								logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
								break;
							}
						}
					}
					//如果库存大于0,则下单
					if(stock > 0){
						//下单
						String returnData = newOrderMarketPlace(id_order_mrkp,barcode,qty,orderDTO);
						
						if (returnData.contains("OK")) {
							orderDTO.setConfirmTime(new Date()); 
							orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED); 
						} else {
							//推送订单失败
							orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
							orderDTO.setErrorType(ErrorStatus.OTHER_ERROR);							
							orderDTO.setDescription("下单失败：" + returnData);
						}
					}else{
						orderDTO.setConfirmTime(new Date()); 
						orderDTO.setPushStatus(PushStatus.NO_STOCK);
//						sendMail(item_id+" 该产品库存不足!采购单号是："+orderDTO.getSpPurchaseNo());
					}
				}else{
					orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
					orderDTO.setErrorType(ErrorStatus.OTHER_ERROR);	
					orderDTO.setDescription("查询对方库存接口失败,对方返回的信息是："+stockData);
//					sendMail("订单 "+orderDTO.getSpPurchaseNo()+" spuid等于 "+item_id+" 查询对方库存接口 GetItemStockBySizeMarketPlace 失败,对方返回的信息是："+stockData+",请与供应商联系。2分钟后会再推一次。 ");
				}
			}else{
				orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
				orderDTO.setErrorType(ErrorStatus.OTHER_ERROR);	
				orderDTO.setDescription("查询数据库失败,未找到该商品 "+skuId);
				orderDTO.setLogContent("查询数据库失败,未找到该商品=========== "+skuId);
				logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
//				sendMail("订单 "+orderDTO.getSpPurchaseNo()+" 查询数据库失败,未找到该商品=========== "+skuId);
			}
			
		} catch (Exception e) {
			orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
			handleException.handleException(orderDTO,e);
			orderDTO.setLogContent("推送订单异常========= "+e.getMessage());
			logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
		}
		
	}

	@Override
	public void handleCancelOrder(OrderDTO deleteOrder) {
		deleteOrder.setCancelTime(new Date()); 
		deleteOrder.setPushStatus(PushStatus.NO_LOCK_CANCELLED_API); 
	}

	@Override
	public void handleRefundlOrder(OrderDTO deleteOrder) {
		try {
			String spOrderId = deleteOrder.getSpOrderId();
			if(spOrderId.contains("-")){
				spOrderId = spOrderId.substring(0, spOrderId.indexOf("-")); 
			}
			String returnData = setStatusOrderMarketplace(spOrderId,"CANCELED",deleteOrder);
			if(returnData.contains("OK")){
				deleteOrder.setRefundTime(new Date());
				deleteOrder.setPushStatus(PushStatus.REFUNDED);
			}else{
				deleteOrder.setPushStatus(PushStatus.REFUNDED_ERROR);
				deleteOrder.setErrorType(ErrorStatus.OTHER_ERROR);
				deleteOrder.setDescription(returnData);
			}				
		} catch (Exception e) {
			deleteOrder.setPushStatus(PushStatus.REFUNDED_ERROR);
			handleException.handleException(deleteOrder, e); 
			deleteOrder.setLogContent("退款发生异常============"+e.getMessage());
			logCommon.loggerOrder(deleteOrder, LogTypeStatus.REFUNDED_LOG);			
		}
		
	}

	/**
	 * 根据spu查询供货商库存
	 * @param item_id
	 * @return
	 */
	private String getItemStockBySizeMarketPlace(String item_id,OrderDTO orderDTO) throws Exception {
		Map<String,String> param = new HashMap<String,String>();
		param.put("ITEM_ID", item_id);	
		orderDTO.setLogContent("查询库存参数============" + param.toString()); 
		logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
		String returnData = genteromaPost(supplierProperties.getGenteroma().getUrl()+supplierProperties.getGenteroma().getGetItemStockInterface(), param, new OutTimeConfig(1000*60*10,1000*60*10,1000*60*10),supplierProperties.getGenteroma().getUser(),supplierProperties.getGenteroma().getPassword(),orderDTO);
		orderDTO.setLogContent("查询库存返回结果======="+returnData);
		logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
		return returnData;
	}
	
	/**
	 * 下订单新接口
	 * @param id_order_mrkp 订单号
	 * @param barcode Barcode
	 * @param qty 库存
	 * @return
	 */
	private String newOrderMarketPlace(long id_order_mrkp, String barcode, int qty,OrderDTO orderDTO) throws Exception {
		Map<String,String> param = new HashMap<String,String>();
		param.put("ID_ORDER_MRKP", String.valueOf(id_order_mrkp));
		param.put("BARCODE", barcode);
		param.put("QTY", String.valueOf(qty));
		orderDTO.setLogContent("下单参数============"+param.toString());
		logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
		String returnData = genteromaPost(supplierProperties.getGenteroma().getUrl()+supplierProperties.getGenteroma().getCreateOrderInterface(), param, new OutTimeConfig(1000*60*10,1000*60*10,1000*60*10),supplierProperties.getGenteroma().getUser(),supplierProperties.getGenteroma().getPassword(),orderDTO);
		orderDTO.setLogContent("下订单返回结果======="+returnData+" 下单参数============"+param.toString());
		logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
		return returnData;
	}
	
	/**
	 * 设置订单状态 
	 * @param code 订单号
	 * @param status 可取值为:NEW, PROCESSING, SHIPPED, CANCELED
	 */
	private String setStatusOrderMarketplace(String code, String status,OrderDTO orderDTO) throws Exception {
		Map<String,String> param = new HashMap<String,String>();
		param.put("CODE", code);
		param.put("STATUS", status);//NEW PROCESSING SHIPPED CANCELED (for delete ORDER)
		orderDTO.setLogContent("设置订单参数======="+param.toString());
		logCommon.loggerOrder(orderDTO, LogTypeStatus.REFUNDED_LOG);
		String returnData = genteromaPost(supplierProperties.getGenteroma().getUrl()+supplierProperties.getGenteroma().getSetStatusInterface(), param, new OutTimeConfig(1000*60*10,1000*60*10,1000*60*10),supplierProperties.getGenteroma().getUser(),supplierProperties.getGenteroma().getPassword(),orderDTO);
		orderDTO.setLogContent("设置订单状态返回结果======="+returnData);
		logCommon.loggerOrder(orderDTO, LogTypeStatus.REFUNDED_LOG);
		return returnData;
	}
	
	/**
	 * 获取订单状态
	 * @param code 订单编号
	 * @return
	 */
//	private String getStatusOrderMarketplace(String code) throws Exception {
//		Map<String,String> param = new HashMap<String,String>();
//		param.put("CODE", code);
//		logger.info("查询的订单号为======"+code);
//		String returnData = HttpUtil45.postAuth(url+getStatus_interface, param, new OutTimeConfig(1000*60*10,1000*60*10,1000*60*10),user,password);
//		logger.info("查询返回结果======="+returnData);
//		return returnData;
//	}
	
//	public static void main(String[] args) {
//		try {
//			
//		
//		OrderServiceImpl orderService = new OrderServiceImpl();
//		OrderDTO orderDTO = new OrderDTO();
////    	orderDTO.setSpPurchaseNo("CGD2016072500097");
//		orderDTO.setSpOrderId("201609264074169");
//    	orderDTO.setDetail("6137974-2015343123475:1,");
//    	orderDTO.setPurchasePriceDetail("685.24");
//    	orderService.handleConfirmOrder(orderDTO);
//		//201607284050007L, "2111344053718"
//		//201607284050011L, "2016398420885"
////		try {
////			orderService.newOrderMarketPlace(201607284050015L, "2109590957966", 1);
////		} catch (Exception e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
////		orderService.getStatusOrderMarketplace("201607284050011");
////		orderService.setStatusOrderMarketplace("201607284050015", "CANCELED"); 
////		orderService.getStatusOrderMarketplace("201607284050011");
//		
////		try {
////			String result = orderService.getItemStockBySizeMarketPlace("1841085");
////			System.out.println(result);
////		} catch (Exception e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	
}
