package com.shangpin.ep.order.module.orderapiservice.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.shangpin.ep.order.common.HandleException;
import com.shangpin.ep.order.common.LogCommon;
import com.shangpin.ep.order.conf.supplier.SupplierProperties;
import com.shangpin.ep.order.enumeration.LogTypeStatus;
import com.shangpin.ep.order.enumeration.PushStatus;
import com.shangpin.ep.order.exception.ServiceException;
import com.shangpin.ep.order.module.order.bean.OrderDTO;
import com.shangpin.ep.order.module.orderapiservice.IOrderService;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.coltorti.ColtortiTokenService;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.coltorti.ColtortiUtil;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.coltorti.Customer;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.coltorti.OrderJson;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.coltorti.Product;
import com.shangpin.ep.order.util.httpclient.HttpUtil45;
import com.shangpin.ep.order.util.httpclient.OutTimeConfig;

@Component("coltortiOrderServiceImpl")
public class ColtortiOrderServiceImpl implements IOrderService {

	//	private static Logger logger = Logger.getLogger("info");
	@Autowired
	LogCommon logCommon;
	@Autowired
	SupplierProperties supplierProperties;
	@Autowired
	HandleException handleException;

	/**
	 * 推送数据方法
	 * @param operatorType
	 * @param transParaType
	 * @param url
	 * @param outTimeConf
	 * @param param
	 * @param jsonValue
	 * @param headerMap
	 * @param username
	 * @param password
	 * @return
	 * @throws ServiceException
	 */
	public String coltortiPushOrder(String operatorType, String transParaType , String url, OutTimeConfig outTimeConf, Map<String,String> param, String jsonValue , Map<String,String> headerMap, String username, String password,OrderDTO orderDTO) throws ServiceException{
		return HttpUtil45.operateData(operatorType, transParaType, url, outTimeConf, param, jsonValue, headerMap, username, password);
	}
	public String handleException(String operatorType, String transParaType , String url, OutTimeConfig outTimeConf, Map<String,String> param, String jsonValue , Map<String,String> headerMap, String username, String password,OrderDTO orderDTO,Throwable e){
		handleException.handleException(orderDTO, e);
		return null;
	}

	@Override
	public void handleSupplierOrder(OrderDTO orderDTO) {
		orderDTO.setSupplierOrderNo(orderDTO.getSpOrderId());
		orderDTO.setPushStatus(PushStatus.NO_LOCK_API);
		orderDTO.setLockStockTime(new Date());
	}

	@Override
	public void handleConfirmOrder(OrderDTO orderDTO) {
		String operateData ="";
		String json = "";//推送的数据
		try {
			Map<String,String> param=ColtortiUtil.getCommonParam(0,0);
			Gson gson = new Gson();
			OrderJson oj = new OrderJson();
			oj.setCustomer(new Customer("FilippoTroina", "FilippoTroina", "VIAG.LEOPARDI 27，22075 LURATE CACCIVIO (COMO)", "22075", "LURATE CACCIVIO", "COMO", "Italy"));

			oj.setOrder_id(orderDTO.getPurchaseNo());

			List<Product> products = new ArrayList<Product>();
			Map<String,String> param2=new HashMap<String, String>();
			String detail = orderDTO.getDetail();
			orderDTO.setLogContent("detail=========="+detail);
			logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
			String skuId = detail.split(",")[0].split(":")[0].split("#")[0];
			String sizeNum = detail.split(",")[0].split(":")[0].split("#")[1];
			String num = detail.split(",")[0].split(":")[1];
			param2.put(sizeNum, num);
			products.add(new Product(skuId,"",param2));
			oj.setProducts(products );
			json = gson.toJson(oj);
			orderDTO.setLogContent("推送订单数据为："+json);
			logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
			orderDTO.setLogContent("初始化token");
			logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
			Map<String,String> param1=new HashMap<String, String>();
			orderDTO.setLogContent("token="+param.get("access_token"));
			logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
			param1.put("Authorization", "Bearer "+param.get("access_token"));
			String jsonValue = json;
			orderDTO.setLogContent("开始推送订单");
			logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
			operateData = coltortiPushOrder("post", "json", "https://api.orderlink.it/v1/orders",new OutTimeConfig(1000*60*3,1000*60*3,1000*60*3), null, jsonValue,param1,"SHANGPIN", "12345678",orderDTO);
			orderDTO.setLogContent("推送成功："+json+" 推送订单数据为："+json);
			logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
			orderDTO.setSupplierOrderNo(orderDTO.getPurchaseNo());
			orderDTO.setConfirmTime(new Date());
			orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED);

		} catch (Exception e) {
			orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
			handleException.handleException(orderDTO,e);
			String message = e.getMessage();
			orderDTO.setLogContent("订单失败"+e.getMessage()+" 推送订单数据为："+json);
			logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
			if (message.contains("状态码")) {
				String statusCode = message.split(":")[1];
				if (statusCode.equals("422")) {
					orderDTO.setLogContent(e+operateData+"订单号："+orderDTO.getSpOrderId());
					logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
				}else{
					orderDTO.setLogContent(e+operateData+"订单号："+orderDTO.getSpOrderId());
					logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
				}
			}

			if(e instanceof ServiceException){
				if(ColtortiUtil.isTokenExpire((ServiceException) e)){
					try {
						ColtortiTokenService.initToken();
					} catch (ServiceException e1) {
						orderDTO.setLogContent("更新token错误"+e1);
						logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
					}
				}
			}
		}
	}

	@Override
	public void handleCancelOrder(OrderDTO deleteOrder) {
		deleteOrder.setPushStatus(PushStatus.LOCK_CANCELLED);
		deleteOrder.setCancelTime(new Date());
	}

	@Override
	public void handleRefundlOrder(OrderDTO deleteOrder) {
		deleteOrder.setPushStatus(PushStatus.NO_REFUNDED_API);
		deleteOrder.setRefundTime(new Date());
	}

	//	@Override
//	public void handleEmail(OrderDTO orderDTO) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void getSupplierSkuId(Map<String, String> skuMap)
//			throws ServiceException {
//		// TODO Auto-generated method stub
//		
//	}
//	//采购异常处理
//	private void handlePurchaseOrderExc(OrderDTO orderDTO) {
//		String result = setPurchaseOrderExc(orderDTO);
//		if("-1".equals(result)){
//			orderDTO.setStatus(OrderStatus.NOHANDLE);
//		}else if("1".equals(result)){
//			orderDTO.setStatus(OrderStatus.PURCHASE_EXP_SUCCESS);
//		}else if("0".equals(result)){
//			orderDTO.setStatus(OrderStatus.PURCHASE_EXP_ERROR);
//		}
//		orderDTO.setExcState("0");
//	}
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
