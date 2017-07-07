package com.shangpin.ep.order.module.orderapiservice.impl;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.shangpin.ep.order.common.HandleException;
import com.shangpin.ep.order.common.LogCommon;
import com.shangpin.ep.order.conf.supplier.SupplierProperties;
import com.shangpin.ep.order.enumeration.ErrorStatus;
import com.shangpin.ep.order.enumeration.LogTypeStatus;
import com.shangpin.ep.order.enumeration.PushStatus;
import com.shangpin.ep.order.module.order.bean.OrderDTO;
import com.shangpin.ep.order.module.order.service.impl.OpenApiService;
import com.shangpin.ep.order.module.order.service.impl.PriceService;
import com.shangpin.ep.order.module.orderapiservice.IOrderService;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.efashion.Item;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.efashion.RequestObject;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.efashion.Result;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.efashion.ReturnObject;

@Component
public class BaseBluOrderImpl  implements IOrderService {
	private static Logger loggerError = Logger.getLogger("error");
	private static Logger logger = Logger.getLogger("info");
	
	@Autowired
    LogCommon logCommon;    
    @Autowired
    SupplierProperties supplierProperties;
    @Autowired
    HandleException handleException;  
    @Autowired
    OpenApiService openApiService;

    @Autowired
	PriceService priceService;

    private  String cancelUrl;
    private  String placeUrl;
    private  String appKey;
    private  String appSe;
    
    @PostConstruct
    public void init(){
    	cancelUrl = supplierProperties.getBaseBluEdsConf().getCancelUrl();
    	placeUrl =  supplierProperties.getBaseBluEdsConf().getPlaceUrl();
    	appKey =  supplierProperties.getBaseBluEdsConf().getOpenApiKey();
    	appSe =  supplierProperties.getBaseBluEdsConf().getOpenApiSecret();
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
	@SuppressWarnings("static-access")
	@Override
	public void handleConfirmOrder(final OrderDTO orderDTO) {

		String json = null;
		try{
			json = getJsonData(orderDTO,false);
			String rtnData= null;
//			rtnData = efashionPushOrder(orderDTO,placeUrl,json);/api/v3.0/products/date.json?storeCode=YYW8M&datetime=2011-01-01
			rtnData = efashionPushOrder(orderDTO,"http://baseblu.edstema.it/api/v3.0/place/order.json?storeCode=YYW8M",json);
			orderDTO.setLogContent("confirm返回的结果=" + rtnData+",推送的参数="+json);
			logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
			
			String[] data = rtnData.split("\\|");
			String code = data[0];
			String res = data[1];
			if ("400".equals(code)) {
				Result result = new Gson().fromJson(res, Result.class);
				if ("400".equals(result.getReqCode())) {
					if ("StoreCode not valid".equals(result.getResult())) {
						orderDTO.setErrorType(ErrorStatus.API_ERROR);
						orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
					} else if ("Order number just placed".equals(result
							.getResult())) {
						orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
					} else if (result.getResult().startsWith("Quantity")) {
						orderDTO.setPushStatus(PushStatus.NO_STOCK);
					}  else if (result.getResult().startsWith("Method is temporarily unavailable")) {
						orderDTO.setErrorType(ErrorStatus.API_ERROR);
						orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
					}else {
						if (res.length() > 200) {
							res = res.substring(0, 200);
						}
						// 供应商返回信息有误，暂时设置成不处理
						orderDTO.setErrorType(ErrorStatus.API_ERROR);
						orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
					}
					orderDTO.setDescription(orderDTO.getLogContent());
				}
			} else if ("200".equals(code)) {
				ReturnObject obj = new Gson().fromJson(res, ReturnObject.class);
				if (obj != null) {
					Result r = obj.getResults();
					if (r != null) {
						if ("200".equals(r.getReqCode())) {
							orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED);
							orderDTO.setConfirmTime(new Date());
						} else{
							orderDTO.setErrorType(ErrorStatus.API_ERROR);
							orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
							orderDTO.setDescription(orderDTO.getLogContent());
						}
					}
				}
			} else {
				loggerError.info(res);
				if (res.length() > 200) {
					res = res.substring(0, 200);
				}
				orderDTO.setDescription(orderDTO.getLogContent());
				orderDTO.setErrorType(ErrorStatus.API_ERROR);
				orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
			}

		}catch(Exception e){
			orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
			handleException.handleException(orderDTO,e);
			orderDTO.setLogContent("推送订单异常="+e.getMessage()+",推送的参数="+json);
			orderDTO.setDescription(orderDTO.getLogContent());
			orderDTO.setErrorType(ErrorStatus.API_ERROR);
			logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
		}
		
	}

	/**
	 * 解除库存锁
	 */
	@Override
	public void handleCancelOrder(OrderDTO deleteOrder) {
		deleteOrder.setCancelTime(new Date()); 
		deleteOrder.setPushStatus(PushStatus.NO_LOCK_CANCELLED_API); 
		deleteOrder.setLogContent("------取消锁库结束-------");
		logCommon.loggerOrder(deleteOrder, LogTypeStatus.LOCK_CANCELLED_LOG);
	}

	/**
	 * 退款
	 */
	@Override
	public void handleRefundlOrder(OrderDTO orderDTO) {
		try{
			String json = getJsonData(orderDTO,true);
			String rtnData= null;
			rtnData = efashionPushOrder(orderDTO,cancelUrl,json);
			orderDTO.setLogContent("refund返回的结果=" + rtnData+",推送的订单参数="+json);
			logCommon.loggerOrder(orderDTO, LogTypeStatus.REFUNDED_LOG);
			String[] data = rtnData.split("\\|");
			String code = data[0];
			String res = data[1];
			orderDTO.setRefundTime(new Date());
			orderDTO.setLogContent(rtnData);
			if ("400".equals(code)) {
				Result result = new Gson().fromJson(res, Result.class);
				if ("400".equals(result.getReqCode())) {
					logger.info(result.getResult());
					orderDTO.setPushStatus(PushStatus.REFUNDED_ERROR);
					orderDTO.setErrorType(ErrorStatus.API_ERROR);
				}
			} else if ("200".equals(code)) {
				ReturnObject obj = new Gson().fromJson(res, ReturnObject.class);
				if (obj != null) {
					Result r = obj.getResults();
					if (r != null) {
						if ("200".equals(r.getReqCode())) {
								orderDTO.setPushStatus(PushStatus.REFUNDED);
						} else if ("400".equals(r.getReqCode())) {
							orderDTO.setPushStatus(PushStatus.REFUNDED_ERROR);
							orderDTO.setErrorType(ErrorStatus.API_ERROR);
						}
					}
				}
			} else {
				loggerError.info(res);
				if (res.length() > 200) {
					res = res.substring(0, 200);
				}
				orderDTO.setErrorType(ErrorStatus.API_ERROR);
				orderDTO.setPushStatus(PushStatus.REFUNDED_ERROR);
			}
		}catch(Exception e){
			orderDTO.setPushStatus(PushStatus.REFUNDED_ERROR);
			orderDTO.setErrorType(ErrorStatus.NETWORK_ERROR);
			handleException.handleException(orderDTO,e);
			orderDTO.setLogContent("退款订单异常========= "+e.getMessage());
			logCommon.loggerOrder(orderDTO, LogTypeStatus.REFUNDED_LOG);
		}
	}

	private String getJsonData(OrderDTO orderDTO, boolean flag) throws Exception{

		Object array = null;
//		try {
			RequestObject obj = new RequestObject();
			obj.setOrder_number(orderDTO.getSpOrderId());
			obj.setItems_count("1");
			SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");
			obj.setDate(time.format(orderDTO.getCreateTime()));
			Item item = new Item();

			String detail = orderDTO.getDetail();
			String skuNo = null;
			String num = null;
			if (detail != null) {
				skuNo = detail.split(":")[0];
				num = detail.split(":")[1];
			}
			item.setProduct(skuNo.split("-")[0]);
			item.setQuantity(num);
			String size = skuNo.split("-")[1];
			if ("A".equals(size)) {
				size = null;
			}
			item.setSize(size);
//			item.setPurchase_price(orderDTO.getPurchasePriceDetail());
			if(flag){
				item.setPurchase_price("1");
			}else{
//				try{
//					BigDecimal priceInt = priceService.getPurchasePrice(orderDTO.getSupplierId(),"",orderDTO.getSpSkuNo());
					BigDecimal priceInt = new BigDecimal("10");
					orderDTO.setLogContent("【geb在推送订单时获取采购价："+priceInt.toString()+"】"); 
					logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
					String price = priceInt.divide(new BigDecimal(1.05), 2)
							.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
					orderDTO.setPurchasePriceDetail(price);
					item.setPurchase_price(price);
//				}catch(Exception e){
//					Log.info(orderDTO.getPurchaseNo()+"geb获取采购价失败");
//					item.setPurchase_price("1");
//				}
			}
			Item[] i = { item };
			obj.setItems(i);
			
			array = JSONObject.toJSON(obj);
//		} catch (Exception ex) {
//
//		}
		if (array != null) {
			return array.toString();
		} else {
			return null;
		}

	}
  /**
	 * 给对方推送订单或者退单
	 * @param orderDTO
	 * @return
	 * @throws Exception
	 */
//    @HystrixCommand(fallbackMethod = "handleException")
	private String efashionPushOrder(OrderDTO orderDTO, String url, String json)  throws Exception{
		
		HttpResponse response = null;
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		CloseableHttpClient httpClient = httpClientBuilder.build();
		HttpPost httpPost = new HttpPost(url);
		StringEntity entity = new StringEntity(json, "utf-8");
		entity.setContentEncoding("UTF-8");
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		Map map = new HashMap();
		map.put("order", json);
		Iterable<? extends NameValuePair> nvs = map2NameValuePair(map);
		httpPost.setEntity(new UrlEncodedFormEntity(nvs, Charset
				.forName("UTF-8")));
		String str = null;
		response = httpClient.execute(httpPost);
		str = EntityUtils.toString(response.getEntity());
		return response.getStatusLine().getStatusCode() + "|" + str;
	}
	
	public String handleException(OrderDTO orderDTO, String url, String json,Throwable e){		
		handleException.handleException(orderDTO, e); 
		return null;
	}
	private static Iterable<? extends NameValuePair> map2NameValuePair(
			Map<String, String> param) {
		Iterator<Entry<String, String>> kvs = param.entrySet().iterator();
		List<NameValuePair> nvs = new ArrayList<NameValuePair>(param.size());
		while (kvs.hasNext()) {
			Entry<String, String> kv = kvs.next();
			NameValuePair nv = new BasicNameValuePair(kv.getKey(),
					kv.getValue());
			nvs.add(nv);
		}
		return nvs;
	}
	public static void main(String[] args) {
		BaseBluOrderImpl ompl = new BaseBluOrderImpl();
//		ReturnOrderDTO orderDTO = new ReturnOrderDTO();
		String d = "5908a9bafd7955c0bff4afdd-XXL:1";
//		orderDTO.setDetail(d);
//		orderDTO.setSpOrderId("201609134249189");
//		orderDTO.setCreateTime(new Date());
		
		OrderDTO orderDTO1 = new OrderDTO();
		orderDTO1.setDetail(d);
		orderDTO1.setSpOrderId("201705175613397");
		orderDTO1.setCreateTime(new Date());
		orderDTO1.setPurchasePriceDetail("110.45");
		
//		ompl.handleRefundlOrder(orderDTO);//(orderDTO);
		ompl.handleConfirmOrder(orderDTO1);//(orderDTO);
	}

}
