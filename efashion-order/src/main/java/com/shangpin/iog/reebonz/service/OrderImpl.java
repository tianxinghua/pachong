package com.shangpin.iog.reebonz.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Map.Entry;

import javax.mail.MessagingException;

import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.shangpin.framework.ServiceException;
import com.shangpin.framework.ServiceMessageException;
import com.shangpin.ice.ice.AbsOrderService;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.SendMail;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.EventProductDTO;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.dto.ReturnOrderDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.efashion.dto.Item;
import com.shangpin.iog.efashion.dto.RequestObject;
import com.shangpin.iog.efashion.dto.Result;
import com.shangpin.iog.efashion.dto.ReturnObject;
import com.shangpin.iog.ice.dto.OrderStatus;
import com.shangpin.iog.product.service.SkuPriceServiceImpl;
import com.shangpin.iog.service.EventProductService;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.SkuPriceService;
@Component
public class OrderImpl  extends AbsOrderService{

	private static Logger logger = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String supplierId = null;
	private static String supplierNo = null;
	private static String url = null;
	@Autowired
	SkuPriceService skuPriceService;
	static {
		if(null==bdl){
			bdl=ResourceBundle.getBundle("conf");
		}
		supplierId = bdl.getString("supplierId");
		supplierNo = bdl.getString("supplierNo");
		url = bdl.getString("url");
	}

	public void loopExecute() {
		this.checkoutOrderFromWMS(supplierNo, supplierId, true);
	}

	public void confirmOrder() {
		this.confirmOrder(supplierId);

	}
	/**
	 * 锁库存
	 */
	@Override
	public void handleSupplierOrder(OrderDTO orderDTO) {
		orderDTO.setStatus(OrderStatus.PLACED);
	}

	/**
	 * 推送订单
	 */
	@Override
	public void handleConfirmOrder(final OrderDTO orderDTO) {
		
		String orderId = orderDTO.getSpOrderId();
		
		
		if(orderId.startsWith("20160206")||orderId.startsWith("20160207")||orderId.startsWith("20160208")||orderId.startsWith("20160209")){
			orderDTO.setStatus(OrderStatus.CONFIRMED);
		}else{
			String rtnData = pushOrder(orderDTO);
			logger.info("rtnData===="+rtnData);
			System.out.println("rtnData===="+rtnData);
			String [] data = rtnData.split("\\|");
			String code = data[0];
			String res = data[1];
			if("400".equals(code)){
				Result result = new Gson().fromJson(res, Result.class);
				if("400".equals(result.getReqCode())){
					orderDTO.setExcDesc(result.getResult());
					orderDTO.setExcState("0");
					String reResult = setPurchaseOrderExc(orderDTO);
					if("-1".equals(reResult)){
						orderDTO.setStatus(OrderStatus.NOHANDLE);
					}else if("1".equals(reResult)){
						orderDTO.setStatus(OrderStatus.PURCHASE_EXP_SUCCESS);
					}else if("0".equals(reResult)){
						orderDTO.setStatus(OrderStatus.PURCHASE_EXP_ERROR);
					}
				}
			}else if("200".equals(code)){
				ReturnObject obj = new Gson().fromJson(res, ReturnObject.class);
				if(obj!=null){
					Result r = obj.getResults();
					if(r!=null){
						if("200".equals(r.getReqCode())){
							orderDTO.setExcState("0");
							orderDTO.setSupplierOrderNo(r.getDescription());
							orderDTO.setStatus(OrderStatus.CONFIRMED);
						}else if("400".equals(r.getReqCode())){
							
							orderDTO.setExcState("0");
							orderDTO.setExcDesc(r.getDescription());
							String reResult = setPurchaseOrderExc(orderDTO);
							if("-1".equals(reResult)){
								orderDTO.setStatus(OrderStatus.NOHANDLE);
							}else if("1".equals(reResult)){
								orderDTO.setStatus(OrderStatus.PURCHASE_EXP_SUCCESS);
							}else if("0".equals(reResult)){
								orderDTO.setStatus(OrderStatus.PURCHASE_EXP_ERROR);
							}
						}
					}
				}
			}else{
				orderDTO.setExcDesc(res);
				orderDTO.setExcState("0");
				String reResult = setPurchaseOrderExc(orderDTO);
				if("-1".equals(reResult)){
					orderDTO.setStatus(OrderStatus.NOHANDLE);
				}else if("1".equals(reResult)){
					orderDTO.setStatus(OrderStatus.PURCHASE_EXP_SUCCESS);
				}else if("0".equals(reResult)){
					orderDTO.setStatus(OrderStatus.PURCHASE_EXP_ERROR);
				}
			}
		}
	}

	private String pushOrder(OrderDTO orderDTO){
		
		String json = getJsonData(orderDTO);
		logger.info("推送的数据："+json);
		HttpResponse response = null;
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		CloseableHttpClient httpClient = httpClientBuilder.build();
		HttpPost httpPost = new HttpPost(url);
		StringEntity entity = new StringEntity(json,"utf-8");
		entity.setContentEncoding("UTF-8");    
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        Map map = new HashMap();
        map.put("order",json);
        Iterable<? extends NameValuePair> nvs = map2NameValuePair(map);
        httpPost.setEntity(new UrlEncodedFormEntity(nvs, Charset
				.forName("UTF-8")));
        try {
			response = httpClient.execute(httpPost);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        String str = null;
        try {
			str = EntityUtils.toString(response.getEntity());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return response.getStatusLine().getStatusCode()+"|"+str;
	}
	
	private static Iterable<? extends NameValuePair> map2NameValuePair(
			Map<String, String> param) {
		Iterator<Entry<String, String>> kvs=param.entrySet().iterator();
		List<NameValuePair> nvs = new ArrayList<NameValuePair>(param.size());
		while(kvs.hasNext()){
			Entry<String, String> kv = kvs.next();
			NameValuePair nv = new BasicNameValuePair(kv.getKey(), kv.getValue());
			nvs.add(nv);
		}
		return nvs;
	}
	/**
	 * 解除库存锁
	 */
	@Override
	public void handleCancelOrder(final ReturnOrderDTO deleteOrder) {
		deleteOrder.setExcState("0");
		//超过一天 不需要在做处理 订单状态改为其它状体
		deleteOrder.setStatus(OrderStatus.CANCELLED);
	}

	/**
	 * 获取真正的供货商编号
	 *
	 * @param skuMap
	 *            key skuNo ,value supplerSkuNo
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public void getSupplierSkuId(Map<String, String> skuMap)
			throws ServiceException {

	}

	/*
	 * detail数据格式： skuId:数量,skuId:数量
	 */

	@Override
	public void handleRefundlOrder(ReturnOrderDTO deleteOrder) {
		// TODO Auto-generated method stub
		deleteOrder.setStatus(OrderStatus.REFUNDED);
	}

	@Override
	public void handleEmail(OrderDTO orderDTO) {
		
		
	} 
	private  String getJsonData(OrderDTO orderDTO) {
		
		JSONObject array = null;    
		try{
			RequestObject obj = new RequestObject();
			obj.setOrder_number(orderDTO.getSpOrderId());                                                                     
			obj.setItems_count("1");
			SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd"); 
			obj.setDate(time.format(orderDTO.getCreateTime()));
			Item item = new Item();
		    
			String detail = orderDTO.getDetail();
			String skuNo = null;
			String num = null;
			if(detail!=null){
				skuNo = detail.split(":")[0];
				num = detail.split(":")[1];
			}
			//item.getSku_id()+"|"+item.getProduct_reference()+"|"+item.getColor_reference()+"|"+size
			item.setSku_id(skuNo.split("\\|")[0]);
			item.setColor_reference(skuNo.split("\\|")[2]);
			item.setProduct_reference(skuNo.split("\\|")[1]);
			item.setQuantity(num);
			String size = skuNo.split("\\|")[3];
			if("A".equals(size)){
				size = null;
			}
			item.setSize(size);
			
	    	
	    	BigDecimal priceInt = new BigDecimal(orderDTO.getPurchasePriceDetail());
			String price = priceInt.divide(new BigDecimal(1.05),2).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
			
			item.setPurchase_price(price);
			
			Item [] i = {item};
			obj.setItems(i);
			array = JSONObject.fromObject(obj);	
		}catch(Exception ex){
			
		}
		if(array!=null){
			return array.toString();
		}else{
			return null;
		}
		
	}
//	public static void main(String[] args) {
//		OrderImpl ompl = new OrderImpl();
//		OrderDTO orderDTO = new OrderDTO();
//		String d= "1201|09029100|1555|40:1";
//		orderDTO.setDetail(d);
//		orderDTO.setSpOrderId("2016021012");
//		orderDTO.setSpPurchaseDetailNo("123");
//		ompl.handleConfirmOrder(orderDTO);
//	}
}
