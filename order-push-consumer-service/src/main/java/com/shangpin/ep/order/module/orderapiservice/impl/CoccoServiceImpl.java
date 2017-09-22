package com.shangpin.ep.order.module.orderapiservice.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.shangpin.ep.order.common.HandleException;
import com.shangpin.ep.order.common.LogCommon;
import com.shangpin.ep.order.conf.mail.message.ShangpinMail;
import com.shangpin.ep.order.conf.mail.sender.ShangpinMailSender;
import com.shangpin.ep.order.enumeration.ErrorStatus;
import com.shangpin.ep.order.enumeration.LogTypeStatus;
import com.shangpin.ep.order.enumeration.PushStatus;
import com.shangpin.ep.order.exception.ServiceException;
import com.shangpin.ep.order.module.order.bean.OrderDTO;
import com.shangpin.ep.order.module.orderapiservice.IOrderService;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.coccolebimbi.Order;
import com.shangpin.ep.order.module.orderapiservice.impl.dto.coccolebimbi.Result;
import com.shangpin.ep.order.util.httpclient.HttpUtil45;
import com.shangpin.ep.order.util.httpclient.OutTimeConfig;

import lombok.extern.slf4j.Slf4j;
/**
 * <p>Title: CoccoServiceImpl</p>
 * <p>Description: 供应商订单以发送邮件的方式发送给对方</p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年6月22日 上午10:15:54
 *
 */
@Component("coccolebimbiOrderImpl")
@Slf4j
public class CoccoServiceImpl implements IOrderService{

	private static String FORMAT = "yyyy/MM/dd HH:mm:ss";
	private static String placeUrl = "http://coccole.teknosis.link:444/ordiniweb";
	private static String confirmUrl = "http://coccole.teknosis.link:444/detordiniweb";
	private static Gson gson = new Gson();
	@Autowired
    LogCommon logCommon;  
	 @Autowired
	    HandleException handleException;  
	@Autowired
	private ShangpinMailSender shangpinMailSender;

	@SuppressWarnings("static-access")
	@Override
	public void handleSupplierOrder(OrderDTO orderDTO) {
		
		orderDTO.setLockStockTime(new Date());
		orderDTO.setPushStatus(PushStatus.NO_LOCK_API);
		orderDTO.setLogContent("------锁库结束-------");
		logCommon.loggerOrder(orderDTO, LogTypeStatus.LOCK_LOG);		
	}

	
	@SuppressWarnings("static-access")
	@Override
	public void handleConfirmOrder(OrderDTO orderDTO) {
		try {
			Order order = new Order();
			order.setNORDINE("Shangpin-"+orderDTO.getPurchaseNo());
			order.setNWEB(orderDTO.getPurchaseNo());
			order.setDATA(new SimpleDateFormat(FORMAT).format(new Date()));
			
			String json = HttpUtil45.operateData("post","json",placeUrl,new OutTimeConfig(),null,gson.toJson(order),null, null, null);
			orderDTO.setLogContent("confirm返回的结果=" + json+",推送的参数="+gson.toJson(order));
			Result result = gson.fromJson(json, Result.class);
			if(result!=null&&(result.getRESULT().equals("INSERT OK")||result.getRESULT().equals("EDIT OK"))){
				String supplierSkuNo = orderDTO.getSupplierSkuNo();
				if(supplierSkuNo.contains("½")){
					supplierSkuNo = supplierSkuNo.replaceAll("½","*");
				}
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("NORDINE","Shangpin-"+orderDTO.getPurchaseNo());
				jsonObj.put("CODICE_ART",supplierSkuNo);
				jsonObj.put("PREZZO","1");
				jsonObj.put("QNT_ORD","1");
				json = HttpUtil45.operateData("post","json",confirmUrl,new OutTimeConfig(),null,gson.toJson(jsonObj),null, null, null);
				orderDTO.setLogContent("confirm返回的结果=" + json+",推送的参数="+gson.toJson(jsonObj));
				result = gson.fromJson(json, Result.class);
				if(result!=null&&(result.getRESULT().equals("INSERT OK")||result.getRESULT().equals("EDIT OK"))){
					orderDTO.setConfirmTime(new Date());
					orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED);
					orderDTO.setLogContent("------推送结束-------");
					logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
					return;
				}
			}
			orderDTO.setErrorType(ErrorStatus.API_ERROR);
			orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
		} catch (Exception e) {
			orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
			handleException.handleException(orderDTO,e);
			orderDTO.setLogContent("推送订单异常="+e.getMessage());
			orderDTO.setDescription(orderDTO.getLogContent());
			orderDTO.setErrorType(ErrorStatus.API_ERROR);
			logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
		}
	}

	@SuppressWarnings("static-access")
	public void handleRefundlOrder(OrderDTO deleteOrder) {
		
		try{
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("NORDINE","Shangpin-"+deleteOrder.getPurchaseNo());
			jsonObj.put("CODICE_ART",deleteOrder.getSupplierSkuNo());
			jsonObj.put("PREZZO","1");
			jsonObj.put("QNT_ORD","1");
			jsonObj.put("STATO","A");
			String json = HttpUtil45.operateData("post","json",confirmUrl,new OutTimeConfig(),null,gson.toJson(jsonObj),null, null, null);
			deleteOrder.setLogContent("退款返回的结果=" + json+",推送的参数="+gson.toJson(jsonObj));
			Result result = gson.fromJson(json, Result.class);
			if(result!=null&&result.getRESULT().equals("DELETE OK")){
				deleteOrder.setRefundTime(new Date());
				deleteOrder.setPushStatus(PushStatus.REFUNDED);
				deleteOrder.setLogContent("------退款结束-------");
				logCommon.loggerOrder(deleteOrder, LogTypeStatus.REFUNDED_LOG);
				return;
			}
			deleteOrder.setPushStatus(PushStatus.REFUNDED_ERROR);
			deleteOrder.setErrorType(ErrorStatus.NETWORK_ERROR);
		}catch(Exception e){
			deleteOrder.setPushStatus(PushStatus.REFUNDED_ERROR);
			deleteOrder.setErrorType(ErrorStatus.NETWORK_ERROR);
			handleException.handleException(deleteOrder,e);
			deleteOrder.setLogContent("退款订单异常========= "+e.getMessage());
			logCommon.loggerOrder(deleteOrder, LogTypeStatus.REFUNDED_LOG);
		}
	}
	/**
	 * 发送邮件
	 * @param subject 邮件主题
	 * @param text 邮件内容
	 * @throws Exception
	 */
	private void sendMail(String cgd,String sku,String price) throws Exception {
		
		String messageText =
 				"KEY: "+sku+"<br>"+
 				"QUANTITY: 1 <br>"+
 				"CUSTOMER NAME: "+"<br>"+
 				"CUSTOMER SHIPPING ADDRESS: "+"<br>"+
 				"CUSTOMER BILLING ADDRESS: "+"<br>"+
 				"TOTAL PAYED FOR THE ORDER: "+price+"<br>"+
 				"Shangpin OrderNo: "+cgd+"<br>";
   	
		ShangpinMail shangpinMail = new ShangpinMail();
		shangpinMail.setFrom("chengxu@shangpin.com");
		shangpinMail.setSubject("shangpin-order-"+cgd);
		shangpinMail.setText(messageText);
		shangpinMail.setTo("zhaogenchun@shangpin.com");
		List<String> addTo = new ArrayList<>();
		addTo.add("cesare.m@coccolebimbi.com");
		addTo.add("sabino.m@coccolebimbi.com");
		addTo.add("gio.p@coccolebimbi.com");
		addTo.add("winnie.liu@shangpin.com");
		shangpinMail.setAddTo(addTo);
		shangpinMailSender.sendShangpinMail(shangpinMail);
	}

	@SuppressWarnings("static-access")
	@Override
	public void handleCancelOrder(OrderDTO deleteOrder) {
		deleteOrder.setCancelTime(new Date());
		deleteOrder.setPushStatus(PushStatus.NO_LOCK_CANCELLED_API);
		deleteOrder.setLogContent("------取消结束-------");
		logCommon.loggerOrder(deleteOrder, LogTypeStatus.LOCK_LOG);		
	}

}
