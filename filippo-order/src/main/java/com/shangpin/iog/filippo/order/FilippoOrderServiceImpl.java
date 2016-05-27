package com.shangpin.iog.filippo.order;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.security.auth.callback.ConfirmationCallback;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsOrderService;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.SendMail;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.dto.ReturnOrderDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.TokenDTO;
import com.shangpin.iog.ice.dto.OrderStatus;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.SkuPriceService;
import com.shangpin.iog.service.TokenService;
@Component
public class FilippoOrderServiceImpl extends AbsOrderService{
	
	private static Logger logger = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String supplierId = null;
	private static String orderurl = null;
	private static String flag = null;
	private static OutTimeConfig outTimeConf = null;
	static {
		if(null==bdl){
			bdl=ResourceBundle.getBundle("conf");
		}
		supplierId = bdl.getString("supplierId");
		orderurl = bdl.getString("orderurl");
		flag = bdl.getString("flag");
		outTimeConf = new OutTimeConfig(1000*60*2, 1000*60*2, 1000*60*2);
	}
	@Autowired
	ProductFetchService pfs;
	@Override
	public void handleSupplierOrder(OrderDTO orderDTO) {
		String operationO = operationO(orderDTO);
		if (operationO.contains("#a")) {
			//数据推送成功，filippo系统返回信息
			logger.info("下订单"+orderDTO.getSpOrderId()+"返回数据为"+operationO);
			String[] split = operationO.split("\\|");
			if (split[1].equals("ACK")) {
				orderDTO.setExcState("0");
				logger.info(orderDTO.getSpOrderId()+"设置供应商订单号为"+split[3]);
				orderDTO.setSupplierOrderNo(split[3]);
				orderDTO.setStatus(OrderStatus.PLACED);
			}else{
				orderDTO.setExcDesc("订单失败"+operationO);
				orderDTO.setExcTime(new Date());
			}
		}else{
			//数据推送失败
			logger.info("网络原因推送订单失败op=o,orderNo:"+orderDTO.getSpOrderId());
		}
	}

	@Override
	public void handleConfirmOrder(OrderDTO orderDTO) {
		String operationS = operationS(orderDTO);
		if (operationS.contains("#a")) {
			if (operationS.contains("NACK")) {
				//确认订单失败
				orderDTO.setExcDesc("支付订单失败"+operationS);
				orderDTO.setExcTime(new Date());
				if (flag.equals("520")) {
					orderDTO.setExcState(OrderStatus.SHOULD_PURCHASE_EXP);
					orderDTO.setExcState("1");
					orderDTO.setExcDesc("520采购异常");
				}else{
					handlePurchaseOrderExc(orderDTO);
				}
			}else{
				//支付成功
				 orderDTO.setExcState("0");
				 orderDTO.setStatus(OrderStatus.CONFIRMED);
			}
		}else{
			//数据推送失败
			logger.info("网络原因推送订单失败op=s,orderNo:"+orderDTO.getSpOrderId());
			orderDTO.setExcDesc("orderNo"+orderDTO.getSupplierOrderNo()+"网络原因付款失败"+operationS);
			orderDTO.setExcTime(new Date());
			orderDTO.setExcState("1");
		}
	
	}

	@Override
	public void handleCancelOrder(ReturnOrderDTO deleteOrder) {
		String operationC = operationC(deleteOrder);
		if (operationC.contains("#a")) {
			if (operationC.contains("NACK")) {
				//取消订单失败
				logger.info("取消订单失败");
				deleteOrder.setExcDesc("取消订单失败"+operationC);
				deleteOrder.setStatus(OrderStatus.CANCELLED);
				deleteOrder.setExcTime(new Date());
				deleteOrder.setExcState("0");
			}else{
				//取消订单成功
				deleteOrder.setExcState("0");
				deleteOrder.setStatus(OrderStatus.CANCELLED);
			}
		}else{
			logger.info("请求取消订单失败,orderNo:"+deleteOrder.getSpOrderId()+operationC);
			deleteOrder.setExcDesc("取消订单失败,orderNo:"+deleteOrder.getSpOrderId());
			deleteOrder.setExcState("1");
			deleteOrder.setExcTime(new Date());
		}
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
	
	private void sendMail(OrderDTO orderDTO) {
		logger.info("处理采购异常 orderNo:"+orderDTO.getSupplierOrderNo());
		try{
			long tim = 60l;
			//判断有异常的订单如果处理超过两小时，依然没有解决，则把状态置为不处理，并发邮件
			if(DateTimeUtil.getTimeDifference(orderDTO.getCreateTime(),new Date())/(tim*1000*60)>0){ 
				
				String result = setPurchaseOrderExc(orderDTO);
				if("-1".equals(result)){
					orderDTO.setStatus(OrderStatus.NOHANDLE);
				}else if("1".equals(result)){
					orderDTO.setStatus(OrderStatus.PURCHASE_EXP_SUCCESS);
				}else if("0".equals(result)){
					orderDTO.setStatus(OrderStatus.PURCHASE_EXP_ERROR);
				}else{
					orderDTO.setStatus(OrderStatus.NOHANDLE);
				}
				//超过一天 不需要在做处理 订单状态改为其它状体
				orderDTO.setExcState("0");
			}else{
				orderDTO.setExcState("1");
			}
		}catch(Exception x){
			logger.info("订单超时" + x.getMessage());
		}
		
	}
	private String operationO(OrderDTO orderDTO){
		
		Map<String, String> param = new HashMap<String, String>();
		String orderdetail = orderDTO.getDetail().split(",")[0];
		String skuid = orderdetail.split(":")[0];
		String qty = orderdetail.split(":")[1];
		SkuDTO skuDTO = pfs.findSKUBySupplierIdAndSkuId(supplierId, skuid);
		
		param.put("op", "o");param.put("qty", qty);
		param.put("o", "shangG");param.put("p", "aW5102cn6");
		param.put("w", "ha");param.put("q", "ordreq");
		param.put("v", skuid.split("-")[0]);param.put("tg", skuDTO.getProductName());
		param.put("cf", orderDTO.getSpOrderId().replace("|", "_"));

		logger.info("op=o请求参数为v="+skuid.split("-")[0]+"tg="+skuDTO.getProductName());
		
		String result = HttpUtil45.get(orderurl, outTimeConf, param);
		return result;
	}
	
	private String operationS(OrderDTO orderDTO){
		Map<String, String> param = new HashMap<String, String>();
		
		param.put("op", "s");
		param.put("nohtml", "csv");
		param.put("o", "shangG");param.put("p", "aW5102cn6");
		param.put("w", "ha");param.put("q", "ordreq");
		param.put("poc", orderDTO.getSupplierOrderNo());
		
		logger.info("op=s请求参数为poc="+orderDTO.getSupplierOrderNo());
		String result = HttpUtil45.get(orderurl, outTimeConf, param);
		return result;
	}
	private String operationC(ReturnOrderDTO deleteOrder){
		Map<String, String> param = new HashMap<String, String>();
		
		param.put("op", "c");
		param.put("nohtml", "csv");
		param.put("o", "shangG");param.put("p", "aW5102cn6");
		param.put("w", "ha");param.put("q", "ordreq");
		param.put("poc", deleteOrder.getSupplierOrderNo());
		
		logger.info("op=c请求参数为poc="+deleteOrder.getSupplierOrderNo());
		String result = HttpUtil45.get(orderurl, outTimeConf, param);
		return result;
	}
	
	
	public static void main(String[] args) {
		
		String s = "a|s";
		System.out.println(s.replace("|", "_"));
//		OrderDTO orderDTO = new OrderDTO();
//		orderDTO.setDetail("2161905-4148973:1,");
//		Map<String, String> param = new HashMap<String, String>();
//		String orderdetail = orderDTO.getDetail().split(",")[0];
//		String skuid = orderdetail.split(":")[0];
//		
//		String qty = orderdetail.split(":")[1];
//		param.put("op", "o");param.put("qty", qty);
//		param.put("o", "shangG");param.put("p", "aW5102cn6");
//		param.put("w", "ha");param.put("q", "ordreq");
//		param.put("v", skuid.split("-")[0]);param.put("tg", skuid.split("-")[1]);
//		param.put("cf", "test");
//		param.put("nohtml", "csv");
//		logger.info("op=o请求参数为v="+skuid.split("-")[0]+"tg="+skuid.split("-")[1]);
//		String result = HttpUtil45.get(orderurl, outTimeConf, param);
//		System.out.println(result);
//		String[] split = result.split("\\|");
//		for (String string : split) {
//			System.out.println(string);
//			
//		}
		
		
		OrderDTO orderDTO = new OrderDTO();
		orderDTO.setSupplierOrderNo("20160506-1E6E64");
		Map<String, String> param = new HashMap<String, String>();
		
		param.put("op", "c");	param.put("nohtml", "csv");
		param.put("o", "shangG");param.put("p", "aW5102cn6");
		param.put("w", "ha");param.put("q", "ordreq");
		param.put("poc", orderDTO.getSupplierOrderNo());
		param.put("cf", "test");
		
		logger.info("op=s请求参数为poc="+orderDTO.getSupplierOrderNo());
		String result = HttpUtil45.get(orderurl, outTimeConf, param);
		System.out.println(result);
		String[] split = result.split("\\|");
		for (String string : split) {
			System.out.println(string);
		}
		
		
	}
}
