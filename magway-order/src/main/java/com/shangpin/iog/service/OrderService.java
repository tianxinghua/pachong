package com.shangpin.iog.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsOrderService;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.CancelOrder;
import com.shangpin.iog.dto.CancelResult;
import com.shangpin.iog.dto.ConfirmResult;
import com.shangpin.iog.dto.CreateOrder;
import com.shangpin.iog.dto.Goods;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.dto.OrderResult;
import com.shangpin.iog.dto.ReturnOrderDTO;
import com.shangpin.iog.dto.Token;
import com.shangpin.iog.ice.dto.OrderStatus;

public class OrderService extends AbsOrderService{

	private static Logger logger = Logger.getLogger("info");
	private static Logger error = Logger.getLogger("error");
	private static OutTimeConfig outTimeConf = new OutTimeConfig(1000*60*5, 1000*60 * 5, 1000*60 * 5);
	private static Token token = null;
	
	private static ResourceBundle bdl=null;
    private static String supplierId = "";
    private static String supplierNo = "";
    
    private static String url_token = null;
    private static String url_createOrder = null;
    private static String url_getOrderConfirmState = null;
    private static String url_cancelOrders = null;
    private static String Authorization = null;
	private static String grant_type = null;
    
    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        supplierNo = bdl.getString("supplierNo");
        url_token = bdl.getString("url_token");
        url_createOrder = bdl.getString("url_createOrder");
        url_getOrderConfirmState = bdl.getString("url_getOrderConfirmState");
        url_cancelOrders = bdl.getString("url_cancelOrders");
        Authorization = bdl.getString("Authorization");
		grant_type = bdl.getString("grant_type");
        
        token = getToken();
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
		try{
			//参数
			String sku_stock = orderDTO.getDetail().split(",")[0];
			String sku = sku_stock.split(":")[0];
			String stock = sku_stock.split(":")[1];
			Goods goods = new Goods();
			goods.setID(sku.split("-")[0]);
			goods.setSize(sku.split("-")[1]); 
			goods.setCount(stock);
			List<Goods> goodslist = new ArrayList<Goods>();
			goodslist.add(goods);
			CreateOrder createOrder = new CreateOrder();
			createOrder.setGoods(goodslist);
			createOrder.setName("FilippoTroina");
			createOrder.setMobile("00393454377342");
			createOrder.setAddress("VIAG.LEOPARDI 27，22075 LURATE CACCIVIO (COMO)");
			createOrder.setIDCardNumber("641233187008230523");
			createOrder.setIDCardPathOne("ShangPin.com");
			createOrder.setIDCardPathTwo("Shangpin.com"); 
			String createOrderStr = new Gson().toJson(createOrder);
			logger.info("createOrderStr======"+createOrderStr);
			
			if(null != token && StringUtils.isNotBlank(token.getAccess_token())){
				Map<String,String> headMap = new HashMap<String,String>();
				headMap.put("Authorization", token.getToken_type()+" "+token.getAccess_token());
//				headMap.put("Authorization", "bearer hhetEMVrAc16iAO-5_l3WUndW818aUlNybHSPuloE-Y_P4ou_q3fPiEbbxxvsPNGiwGZ0WrnoAPdt3t-fww29jnHpBCDfJV40K868UQeZsfl9hfR_YPVklKPGf6QKXi7ZdKgOp0vOemwv_VQhbQ7RNiG9Hyw7RFMrutSNxcdlSlK3VkwOFt_H3xCz8LLIEYcOvTv17DmaDJL5eBUH4G10Qe-wdTgc3uH0o3iOBm8He8ahPFUnidagRsp-NXC25tQKRaN94-oof6H6urnOu-fOtuGF29JjPUnt0ywACswrA99qTrrL7OXE_DZ6W5VH9e2oKmZmLnfhx7VDNlE1EQu8cQKBq-zDiQMqzG-9t_5EoN1avOsanZB3-VaNtMOGOSiL5RwuyZHKT1hp3iXaWklabPbr65vtzlGI0_tmw");
				//
				try{
					createOrder(orderDTO,createOrderStr,headMap);				
				}catch(Exception e){
					
					if(e.getMessage().equals("状态码:401")){//token 过期
						token = getToken();
						headMap.put("Authorization", token.getToken_type()+" "+token.getAccess_token());					
						try {
							createOrder(orderDTO,createOrderStr,headMap);
						} catch (ServiceException e1) {	
							orderDTO.setExcDesc(e1.getMessage());
							orderDTO.setExcState("1");
							orderDTO.setExcTime(new Date());
							e1.printStackTrace();
							error.error(e1);
						}					
					}else{
						orderDTO.setExcDesc(e.getMessage());
						orderDTO.setExcState("1");
						orderDTO.setExcTime(new Date());
						error.error(e);
					}
					
				}
				
			}else{
				orderDTO.setExcDesc("获取token失败");
				orderDTO.setExcState("1");
				orderDTO.setExcTime(new Date());
				error.error("获取token失败，请检查具体原因!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			}
		}catch(Exception ex){
			orderDTO.setExcDesc(ex.getMessage());
			orderDTO.setExcState("1");
			orderDTO.setExcTime(new Date());
		}
		
	}
	
	/**
	 * 创建订单
	 * @param orderDTO
	 * @param createOrderStr 下订单所需参数json格式
	 * @param headMap
	 */
	public void createOrder(OrderDTO orderDTO,String createOrderStr,Map<String,String> headMap) throws ServiceException{
		
		String result = HttpUtil45.operateData("post", "json", url_createOrder, outTimeConf, null, createOrderStr, headMap, "", "");
		logger.info("创建订单result====="+result);
		OrderResult orderResult = new Gson().fromJson(result, OrderResult.class);
		String status = orderResult.getStatus();
		if(status.equals("0")){//库存不足
			orderDTO.setExcDesc(orderResult.getMessage());
			orderDTO.setExcState("1");
			orderDTO.setExcTime(new Date());
			//采购异常处理
			doOrderExc(orderDTO);
		}else if(status.equals("1")){//下单成功
			orderDTO.setSupplierOrderNo(orderResult.getData().getOrderCode());
			orderDTO.setStatus(OrderStatus.PLACED);
			orderDTO.setExcState("0");
		}else{//其他都失败
			orderDTO.setExcDesc(orderResult.getMessage());
			orderDTO.setExcState("1");
			orderDTO.setExcTime(new Date());
		}
	}
	
	/**
	 * 采购异常处理
	 * @param orderDTO
	 */
	public void doOrderExc(OrderDTO orderDTO){
		String reResult = setPurchaseOrderExc(orderDTO);
		if("-1".equals(reResult)){
			orderDTO.setStatus(OrderStatus.NOHANDLE);
		}else if("1".equals(reResult)){
			orderDTO.setStatus(OrderStatus.PURCHASE_EXP_SUCCESS);
		}else if("0".equals(reResult)){
			orderDTO.setStatus(OrderStatus.PURCHASE_EXP_ERROR);
		}
		orderDTO.setExcState("0");
	}

	@Override
	public void handleConfirmOrder(OrderDTO orderDTO) {
		try{
			Map<String,String> headMap = new HashMap<String,String>();
			headMap.put("Authorization", token.getToken_type()+" "+token.getAccess_token());		
			try{
				confirmOrder(orderDTO,headMap);
			}catch(Exception ex){
				if(ex.getMessage().equals("状态码:401")){
					token = getToken();
					headMap.put("Authorization", token.getToken_type()+" "+token.getAccess_token());
					try{
						confirmOrder(orderDTO,headMap);
					}catch(Exception e){
						error.error(e);
						orderDTO.setExcDesc(e.getMessage());
						orderDTO.setExcState("1");
						orderDTO.setExcTime(new Date());
					}				
				}else{
					error.error(ex);
					orderDTO.setExcDesc(ex.getMessage());
					orderDTO.setExcState("1");
					orderDTO.setExcTime(new Date());
				}
			}
		}catch(Exception ex){
			error.error(ex);
			orderDTO.setExcDesc(ex.getMessage());
			orderDTO.setExcState("1");
			orderDTO.setExcTime(new Date());
		}
				
	}
	
	public void confirmOrder(OrderDTO orderDTO,Map<String,String> headMap) throws Exception{
		String uri = url_getOrderConfirmState+orderDTO.getSupplierOrderNo();
		logger.info("confirm uri====="+uri);
		String confirmResult = HttpUtil45.get(uri, outTimeConf, null, headMap, "", "");
		logger.info("confirm result====="+confirmResult);
		ConfirmResult confirm = new Gson().fromJson(confirmResult, ConfirmResult.class);
		String confirmStr = confirm.getData().getConfirmedType();
		if(StringUtils.isBlank(confirmStr) || confirmStr.equals("待确认")){//可以认为待确认
			
		}else if(confirmStr.equals("已部分确认")){
			orderDTO.setExcDesc(confirmStr);
			orderDTO.setExcState("1");
			orderDTO.setExcTime(new Date());
			//采购异常
			doOrderExc(orderDTO);
			
		}else if(confirmStr.equals("已全部确认")){
			orderDTO.setStatus(OrderStatus.CONFIRMED);
			orderDTO.setExcState("0");
			
		}else if(confirmStr.equals("已被取消")){
			orderDTO.setExcDesc(confirmStr);
			orderDTO.setExcState("1");
			orderDTO.setExcTime(new Date());
			//采购异常
			doOrderExc(orderDTO);
		}
		
		
	}

	@Override
	public void handleCancelOrder(ReturnOrderDTO deleteOrder) {
		try{
			token = getToken();
			Map<String,String> headMap = new HashMap<String,String>();
			headMap.put("Authorization", token.getToken_type()+" "+token.getAccess_token());
			CancelOrder cancelOrder = new CancelOrder();
			cancelOrder.setOrderCode(deleteOrder.getSupplierOrderNo());
			cancelOrder.setReason("no reason");
			String canStr = new Gson().toJson(cancelOrder);
			logger.info("取消订单的参数====="+canStr);
			String result = HttpUtil45.operateData("post", "json", url_cancelOrders, outTimeConf, null, canStr, headMap, "", "");
			logger.info("取消订单返回结果====="+result);
			System.out.println(result); 
			CancelResult cancelResult = new Gson().fromJson(result, CancelResult.class);
			if(cancelResult.getStatus().equals("1")){
				deleteOrder.setExcState("0");
				deleteOrder.setStatus(OrderStatus.CANCELLED);
			}else{
				deleteOrder.setExcDesc(cancelResult.getMessage());
				deleteOrder.setExcState("1");
				deleteOrder.setExcTime(new Date());
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
			deleteOrder.setExcDesc(ex.getMessage());
			deleteOrder.setExcState("1");
			deleteOrder.setExcTime(new Date());
		}
		
	}

	@Override
	public void handleRefundlOrder(ReturnOrderDTO deleteOrder) {
		try{
			token = getToken();
			Map<String,String> headMap = new HashMap<String,String>();
			headMap.put("Authorization", token.getToken_type()+" "+token.getAccess_token());
			CancelOrder cancelOrder = new CancelOrder();
			cancelOrder.setOrderCode(deleteOrder.getSupplierOrderNo());
			cancelOrder.setReason("no reason");
			String canStr = new Gson().toJson(cancelOrder);
			logger.info("取消订单的参数====="+canStr);
			String result = HttpUtil45.operateData("post", "json", url_cancelOrders, outTimeConf, null, canStr, headMap, "", "");
			logger.info("取消订单返回结果====="+result);
			System.out.println(result); 
			CancelResult cancelResult = new Gson().fromJson(result, CancelResult.class);
			if(cancelResult.getStatus().equals("1")){
				deleteOrder.setExcState("0");
				deleteOrder.setStatus(OrderStatus.CANCELLED);
			}else{
				deleteOrder.setExcDesc(cancelResult.getMessage());
				deleteOrder.setExcState("1");
				deleteOrder.setExcTime(new Date());
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
			deleteOrder.setExcDesc(ex.getMessage());
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
	
	public static Token getToken(){
		Token token = null;
		try{
			
			Map<String,String> param = new HashMap<String,String>();
			param.put("grant_type", grant_type);
			Map<String,String> headerMap = new HashMap<String,String>();
			headerMap.put("Authorization", Authorization);
			String result = HttpUtil45.post(url_token, param, headerMap, outTimeConf);
			logger.info("Token===="+result);
//			System.out.println(result);
			Gson gson = new Gson();
			token = gson.fromJson(result, Token.class);
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return token;
		
	}
	
//	public static void main(String[] args){
//		OrderDTO orderDTO = new OrderDTO();
//		orderDTO.setDetail("12367-UNI:1,");
//		orderDTO.setSupplierOrderNo("1603000267"); 
//		
//		ReturnOrderDTO deleteOrder = new ReturnOrderDTO();
//		deleteOrder.setSupplierOrderNo("1603000267");
//		
//		OrderService order = new OrderService();
////		order.handleSupplierOrder(orderDTO); 
//		order.handleConfirmOrder(orderDTO); 
////		order.handleCancelOrder(deleteOrder); 
//	}

}
