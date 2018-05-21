package com.shangpin.ep.order.module.orderapiservice.impl;


import com.google.gson.Gson;
import com.shangpin.ep.order.common.HandleException;
import com.shangpin.ep.order.common.LogCommon;
import com.shangpin.ep.order.conf.supplier.SupplierProperties;
import com.shangpin.ep.order.enumeration.ErrorStatus;
import com.shangpin.ep.order.enumeration.LogLeve;
import com.shangpin.ep.order.enumeration.LogTypeStatus;
import com.shangpin.ep.order.enumeration.PushStatus;
import com.shangpin.ep.order.module.order.bean.OrderDTO;
import com.shangpin.ep.order.module.orderapiservice.IOrderService;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.redi.BillingInfo;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.redi.Product;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.redi.RediOrderDto;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.redi.ShippingInfo;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.spinnaker.ResponseObject;
import com.shangpin.ep.order.util.httpclient.HttpUtil45;
import com.shangpin.ep.order.util.httpclient.OutTimeConfig;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;


@Component("rediOrderService")
public class RediOrderService implements IOrderService {


	
	@Autowired
    LogCommon logCommon;    
    @Autowired
    SupplierProperties supplierProperties;
    @Autowired
    HandleException handleException;

	private static Logger logger = Logger.getLogger("info");
	private static Logger loggerError = Logger.getLogger("error");
	private static OutTimeConfig defaultConfig = new OutTimeConfig(1000 * 2, 1000 * 60*5, 1000 * 60*5);
    private  String orderUrl = null;

    
    @PostConstruct
    public void init(){
		orderUrl = supplierProperties.getRedi().getOrderUrl();

    }

	/**
	 * 锁库存
	 */

	@Override
	public void handleSupplierOrder(OrderDTO orderDTO) {
		orderDTO.setLockStockTime(new Date());
		orderDTO.setPushStatus(PushStatus.NO_LOCK_API);
		orderDTO.setLogContent("------锁库结束-------");
		logCommon.loggerOrder(orderDTO, LogTypeStatus.LOCK_LOG);
	}

	/**
	 * 推送订单
	 */
	@Override
	public void handleConfirmOrder(OrderDTO orderDTO) {
		createOrder(orderDTO);
	}

	@Override
	public void handleCancelOrder(OrderDTO deleteOrder) {
		deleteOrder.setPushStatus(PushStatus.LOCK_CANCELLED);
		deleteOrder.setCancelTime(new Date());
		deleteOrder.setLogContent("------取消锁库结束-------");
		logCommon.loggerOrder(deleteOrder, LogTypeStatus.LOCK_CANCELLED_LOG);
	}

	@Override
	public void handleRefundlOrder(OrderDTO deleteOrder) {

		deleteOrder.setCancelTime(new Date());
		deleteOrder.setPushStatus(PushStatus.NO_REFUNDED_API);
		logCommon.loggerOrder(deleteOrder, LogLeve.INFO);
	}
	 /**
	 * 给对方推送订单或者退单
	 * @param
	 * @return
	 * @throws Exception
	 */	
	private String pushOrder(String url, OrderDTO orderDTO)  throws Exception{
		Gson gson = new Gson();
		RediOrderDto rediOrderDto = new RediOrderDto();


		BillingInfo billing_address = new BillingInfo();
		billing_address.setName("John");
		billing_address.setSurname("Doe");
		billing_address.setCompany("");
		billing_address.setVatcode("");
		billing_address.setAddress("89 Xizhimen Outer St");
		billing_address.setCity("Pechino");
		billing_address.setState("Bei");
		billing_address.setCountry("CN");
		billing_address.setZip("100005");
		billing_address.setEmail("j.doe@fashion.us");
		billing_address.setPhone("555-333-112345");
		billing_address.setMobile("333-113-34565");
		rediOrderDto.setBillingInfo(billing_address);
		
		ShippingInfo shipping_address_ch = new ShippingInfo();
		shipping_address_ch.setName("约翰");
		shipping_address_ch.setSurname("母鹿");
		shipping_address_ch.setCompany("");
		shipping_address_ch.setVatcode("");
		shipping_address_ch.setAddress("西直门外89号");
		shipping_address_ch.setCity("Pechino");
		shipping_address_ch.setState("北");
		shipping_address_ch.setCountry("CN");
		shipping_address_ch.setZip("100005");
		shipping_address_ch.setEmail("j.doe@fashion.us");
		shipping_address_ch.setPhone("555-333-112345");
		shipping_address_ch.setMobile("333-113-34565");
		rediOrderDto.setShippingChInfo(shipping_address_ch);

		ShippingInfo shipping_address = new ShippingInfo();
		shipping_address.setName("John");
		shipping_address.setSurname("Doe");
		shipping_address.setCompany("");
		shipping_address.setVatcode("");
		shipping_address.setAddress("89 Xizhimen Outer St");
		shipping_address.setCity("Pechino");
		shipping_address.setState("Bei");
		shipping_address.setCountry("CN");
		shipping_address.setZip("100005");
		shipping_address.setEmail("j.doe@fashion.us");
		shipping_address.setPhone("555-333-112345");
		shipping_address.setMobile("333-113-34565");
		rediOrderDto.setShippingInfo(shipping_address);


		List<Product> items = new ArrayList<Product>();
		Product item = new Product();

		item.setQuantity(1);
		item.setItem_size_id(orderDTO.getSupplierSkuNo());
		items.add(item);
		rediOrderDto.setProducts(items);

		String jsonValue = gson.toJson(rediOrderDto);



		String result =  HttpUtil45.operateData("post", "json", url, new OutTimeConfig(1000*60*1,1000*60*3,1000*60*3), null, jsonValue, null, null,null);
		orderDTO.setLogContent("推送订单返回结果="+result+"推送的订单="+jsonValue);
		return result;
	}
	public String handleException(String url,OrderDTO orderDTO,Map<String, String> map,Throwable e){
		handleException.handleException(orderDTO, e); 
		return null;
	}
	private void createOrder( OrderDTO orderDTO) {

		// 获取订单信息
		String detail = orderDTO.getDetail();
		String[] details = detail.split(":");
		String skuId = details[0];
		String qty = details[1];
		String rtnData = null;
		try {


			 rtnData =pushOrder(orderUrl,orderDTO);

			 logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
			
			Gson gson = new Gson();
			ResponseObject responseObject = gson.fromJson(rtnData, ResponseObject.class);
			if(null==responseObject.getStatus()){
				orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
				orderDTO.setErrorType(ErrorStatus.OTHER_ERROR);							
				orderDTO.setDescription(orderDTO.getLogContent());
			}else if ("ko".equals(responseObject.getStatus().toLowerCase())) {
				if("0".equals(String.valueOf(responseObject.getId_b2b_order()))||"-1".equals(String.valueOf(responseObject.getId_b2b_order()))){   //无库存
					orderDTO.setPushStatus(PushStatus.NO_STOCK);
					orderDTO.setDescription(orderDTO.getLogContent());
				}else{
					orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
					orderDTO.setErrorType(ErrorStatus.OTHER_ERROR);							
					orderDTO.setDescription(orderDTO.getLogContent());
					orderDTO.setLogContent(orderDTO.getLogContent());
				}

			} else {
				orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED);
				orderDTO.setConfirmTime(new Date());
				orderDTO.setSupplierOrderNo(String.valueOf(responseObject.getId_b2b_order()));
			}
		} catch (Exception e) {
			orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
			orderDTO.setDescription(orderDTO.getLogContent());	
			orderDTO.setLogContent(e.getMessage());
			handleException.handleException(orderDTO,e);
		}
	}
	




}
