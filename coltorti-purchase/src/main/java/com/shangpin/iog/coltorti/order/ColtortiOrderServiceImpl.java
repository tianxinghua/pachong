package com.shangpin.iog.coltorti.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsOrderService;
import com.shangpin.iog.coltorti.dto.Customer;
import com.shangpin.iog.coltorti.dto.OrderJson;
import com.shangpin.iog.coltorti.dto.Product;
import com.shangpin.iog.coltorti.service.ColtortiTokenService;
import com.shangpin.iog.coltorti.service.ColtortiUtil;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.dto.ReturnOrderDTO;
import com.shangpin.iog.ice.dto.OrderStatus;
@Component
public class ColtortiOrderServiceImpl extends AbsOrderService{
	private static Logger logger = Logger.getLogger("info");
	@Override
	public void handleSupplierOrder(OrderDTO orderDTO) {
		 orderDTO.setExcState("0");
		 orderDTO.setSupplierOrderNo(orderDTO.getSpOrderId());
		 orderDTO.setStatus(OrderStatus.PLACED);
	}

	@Override
	public void handleConfirmOrder(OrderDTO orderDTO) {
		String operateData ="";
		try {
			Map<String,String> param=ColtortiUtil.getCommonParam(0,0);
			Gson gson = new Gson();
			OrderJson oj = new OrderJson();
			oj.setCustomer(new Customer("FilippoTroina", "FilippoTroina", "VIAG.LEOPARDI 27，22075 LURATE CACCIVIO (COMO)", "22075", "LURATE CACCIVIO", "COMO", "Italy"));
			
			oj.setOrder_id(orderDTO.getSpPurchaseNo());
			
			List<Product> products = new ArrayList<Product>();
			Map<String,String> param2=new HashMap<String, String>();
			String detail = orderDTO.getDetail();
			logger.info("detail=========="+detail);
			String skuId = detail.split(",")[0].split(":")[0].split("#")[0];
			String sizeNum = detail.split(",")[0].split(":")[0].split("#")[1];
			String num = detail.split(",")[0].split(":")[1];
			param2.put(sizeNum, num);
			products.add(new Product(skuId,"",param2));
			oj.setProducts(products );
			String json = gson.toJson(oj);
			logger.info("推送订单数据为："+json);
			logger.info("初始化token");
			Map<String,String> param1=new HashMap<String, String>();
			logger.info("token="+param.get("access_token"));
			param1.put("Authorization", "Bearer "+param.get("access_token"));
			String jsonValue = json;
			logger.info("开始推送订单");
			 operateData = HttpUtil45.operateData("post", "json", "https://api.orderlink.it/v1/orders",new OutTimeConfig(1000*60*3,1000*60*3,1000*60*3), null, jsonValue,param1,"SHANGPIN", "12345678");
			 logger.info("推送成功："+json);
			 orderDTO.setExcState("0");
			 orderDTO.setSupplierOrderNo(orderDTO.getSpPurchaseNo());
			 orderDTO.setStatus(OrderStatus.CONFIRMED);
		
		} catch (Exception e) {
			String message = e.getMessage();
			logger.info("订单失败"+e.getMessage());
			if (message.contains("状态码")) {
				String statusCode = message.split(":")[1];
				if (statusCode.equals("422")) {
					logger.info(e+operateData+"订单号："+orderDTO.getSpOrderId());
					orderDTO.setExcDesc("订单失败重复订单号");
					orderDTO.setExcTime(new Date());
					orderDTO.setExcState("1");
				}else{
					logger.info(e+operateData+"订单号："+orderDTO.getSpOrderId());
					orderDTO.setExcDesc("网络异常，订单失败");
					orderDTO.setExcTime(new Date());
					orderDTO.setExcState("1");
				}
			}
			
			if(e instanceof ServiceException){
				if(ColtortiUtil.isTokenExpire((ServiceException) e)){
					try {
						ColtortiTokenService.initToken();
					} catch (ServiceException e1) {
						logger.error("更新token错误",e1);
					}
				}
			}
		}
	}

	@Override
	public void handleCancelOrder(ReturnOrderDTO deleteOrder) {
		deleteOrder.setExcState("0");
		deleteOrder.setStatus(OrderStatus.CANCELLED);
	}

	@Override
	public void handleRefundlOrder(ReturnOrderDTO deleteOrder) {
		deleteOrder.setExcState("0");
		deleteOrder.setStatus(OrderStatus.REFUNDED);
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
	//采购异常处理
	private void handlePurchaseOrderExc(OrderDTO orderDTO) {
		String result = setPurchaseOrderExc(orderDTO);
		if("-1".equals(result)){
			orderDTO.setStatus(OrderStatus.NOHANDLE);
		}else if("1".equals(result)){
			orderDTO.setStatus(OrderStatus.PURCHASE_EXP_SUCCESS);
		}else if("0".equals(result)){
			orderDTO.setStatus(OrderStatus.PURCHASE_EXP_ERROR);
		}
		orderDTO.setExcState("0");
	}
	public static void main(String[] args) {
		try {
			Gson gson = new Gson();
			OrderJson oj = new OrderJson();
			oj.setCustomer(new Customer("FilippoTroina", "test", "VIAG.LEOPARDI 27，22075 LURATE CACCIVIO (COMO)", "22075", "LURATE CACCIVIO", "COMO", "Italy"));
			oj.setOrder_id("CGD2016012700569");
			List<Product> products = new ArrayList<Product>();
			Map<String,String> param2=new HashMap<String, String>();
			param2.put("8", "1");
			products.add(new Product("152328NCX000011-F0002","",param2));
			oj.setProducts(products );
			String json = gson.toJson(oj);
			System.out.println(json);
			Map<String,String> param=ColtortiUtil.getCommonParam(0,0);
			Map<String,String> param1=new HashMap<String, String>();
			param1.put("Authorization", "Bearer "+param.get("access_token"));
			
			
			String jsonValue = json;
			String operateData = HttpUtil45.operateData("post", "json", "https://api.orderlink.it/v1/orders",new OutTimeConfig(10000,10000,10000), null, jsonValue,param1,"SHANGPIN", "12345678");
			System.out.println("+++++++++++++++++++++"+operateData);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
}
