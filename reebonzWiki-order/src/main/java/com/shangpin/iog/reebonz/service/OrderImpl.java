package com.shangpin.iog.reebonz.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.mail.MessagingException;

import com.google.gson.Gson;
import com.shangpin.iog.onsite.base.utils.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
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
import com.shangpin.iog.ice.dto.OrderStatus;
import com.shangpin.iog.reebonz.dto.Item;
import com.shangpin.iog.reebonz.dto.Items;
import com.shangpin.iog.reebonz.dto.Order;
import com.shangpin.iog.reebonz.dto.RequestObject;
import com.shangpin.iog.reebonz.dto.ResponseObject;
import com.shangpin.iog.service.EventProductService;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.SkuPriceService;
@Component
public class OrderImpl extends AbsOrderService {

	@Autowired
	SkuPriceService skuPriceService;
	@Autowired
	EventProductService eventProductService;
	private static Logger logger = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String supplierId = null;
	private static String supplierNo = null;
	private static String smtpHost = null;
	private static String from = null;
	private static String fromUserPassword = null;
	private static String to = null;
	private static String toReebonz = null;
	private static String subject = null;
	private static String messageText = null;
	private static String messageType = null;
	private static String time = null;
	private static String reebonzSendEmail;
	private static String eventUrl;
	private ReservationProStock stock = new ReservationProStock();
	static {
		if(null==bdl){
			bdl=ResourceBundle.getBundle("conf");
		}
		eventUrl = bdl.getString("eventUrl");
		supplierId = bdl.getString("supplierId");
		supplierNo = bdl.getString("supplierNo");
		smtpHost = bdl.getString("smtpHost");
		from = bdl.getString("from");

		fromUserPassword = bdl.getString("fromUserPassword");
		to = bdl.getString("to");
		toReebonz = bdl.getString("toReebonz"); 
		subject = bdl.getString("subject");
		messageText = bdl.getString("messageText");
		messageType = bdl.getString("messageType");
		time = bdl.getString("time");
		reebonzSendEmail = bdl.getString("reebonzSendEmail");

	}

	public void loopExecute() {
		if("1".equals(reebonzSendEmail)) SENDMAIL=true;
		this.checkoutOrderFromWMS(supplierNo, supplierId, true);
	}

	public void confirmOrder() {
		if("1".equals(reebonzSendEmail)) SENDMAIL=true;
		this.confirmOrder(supplierId);

	}
	/**
	 * 锁库存
	 */
	@Override
	public void handleSupplierOrder(OrderDTO orderDTO) {

		try{
			String order_id = orderDTO.getSpOrderId();
			String order_site = "shangpin";
			String data = getJsonData(orderDTO.getDetail(),null);
			logger.info("锁库存推送的数据：data："+data+",order_id:"+order_id);
			Map<String, String> map = stock.lockStock(order_id, order_site, data);
			if (map.get("1") != null) {
				orderDTO.setExcState("0");
				orderDTO.setSupplierOrderNo(map.get("1"));
				orderDTO.setStatus(OrderStatus.PLACED);

			} else{
				orderDTO.setExcDesc(map.get("0"));
				sendMail(orderDTO);
			}
		}catch (Exception e) {
			orderDTO.setExcDesc(e.getMessage());
			orderDTO.setExcState("1");
			e.printStackTrace();
		}
	}

	/**
	 * 推送订单
	 */
	@Override
	public void handleConfirmOrder(final OrderDTO orderDTO) {

		try{
			String data = getJsonData(orderDTO.getDetail(),orderDTO.getPurchasePriceDetail());
			Map<String, String> map = null;
			logger.info("推送订单的数据：data："+data+",SupplierOrderNo:"+orderDTO.getSupplierOrderNo()+",OrderId:"+orderDTO.getSpOrderId()+",SpPurchaseNo:"+ orderDTO.getSpPurchaseNo());
			map = stock.pushOrder(orderDTO.getSupplierOrderNo(),
					orderDTO.getSpOrderId(), orderDTO.getSpPurchaseNo(), data);
			// 1：代表发生了异常
			if (map.get("1") != null) {
				//超过一天 不需要在做处理 订单状态改为其它状体
				orderDTO.setExcState("0");
				orderDTO.setExcDesc(map.get("1"));
				//-1:不做处理  1：成功  0：失败
				String result = setPurchaseOrderExc(orderDTO);
				if("-1".equals(result)){
					orderDTO.setStatus(OrderStatus.NOHANDLE);
				}else if("1".equals(result)){
					orderDTO.setStatus(OrderStatus.PURCHASE_EXP_SUCCESS);
				}else if("0".equals(result)){
					orderDTO.setStatus(OrderStatus.PURCHASE_EXP_ERROR);
				}
				
				Thread t = new Thread(	 new Runnable() {
					@Override
					public void run() {
						try {
							SendMail.sendMessage(smtpHost, from, fromUserPassword, to, subject,"推送reebonz订单"+orderDTO.getSpOrderId()+"出现错误,已置为不做处理，原因："+orderDTO.getExcDesc(), messageType);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				t.start();
			}else if(map.get("-1") != null){
				sendMail(orderDTO);
				orderDTO.setExcDesc(map.get("-1"));
			} else {
				orderDTO.setExcState("0");
				orderDTO.setStatus(OrderStatus.CONFIRMED);
				orderDTO.setSupplierOrderNo(map.get("return_orderID"));
			}
		}catch(Exception e){
			orderDTO.setExcDesc(e.getMessage());
			orderDTO.setExcState("1");
			e.printStackTrace();
		}
	}

	/**
	 * 解除库存锁
	 */
	@Override
	public void handleCancelOrder(final ReturnOrderDTO deleteOrder) {
		try{
			if(deleteOrder.getSupplierOrderNo()==null){ 
				deleteOrder.setExcState("0");
				//超过一天 不需要在做处理 订单状态改为其它状体
				deleteOrder.setStatus(OrderStatus.CANCELLED);
				
			}else{
				Map<String, String> map = null;
				map = stock.unlockStock(deleteOrder.getSupplierOrderNo(),
						deleteOrder.getSpOrderId(), deleteOrder.getSpOrderId(),
						"voided");// deducted" (for confirmation) "voided" (for reversal)
				if (map.get("1") != null) {
					new Runnable() {
						@Override
						public void run() {
							try {
								SendMail.sendMessage(smtpHost, from, fromUserPassword, to, subject,"reebonz订单:"+deleteOrder.getSpOrderId()+"取消订单解除库存出现错误,已置为不做处理，原因：Invalid Reservation ID", messageType);
							} catch (Exception e) {      }
						}
					};
					deleteOrder.setStatus(OrderStatus.CANCELLED);
					deleteOrder.setExcState("0");
					deleteOrder.setExcDesc(map.get("1"));

				} else {
					deleteOrder.setExcState("0");
					deleteOrder.setStatus(OrderStatus.CANCELLED);
				}
			}
			
		}catch(Exception e){
			deleteOrder.setExcDesc(e.getMessage());
			deleteOrder.setExcState("1");
			e.printStackTrace();
		}
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
	private String getJsonData(String detail,String purchasePrice) {
		
		JSONArray array = null;
		try{
			List<RequestObject> list = null;
			if (detail != null) {
				list = new ArrayList<RequestObject>();
				String[] details = detail.split(",");
				logger.info("detail数据格式:"+detail);
				for (int i = 0; i < details.length; i++) {
					// detail[i]数据格式==>skuId:数量
					String num = details[i].split(":")[1];

					String skuNo = details[i].split(":")[0];
					// skuNo数据格式：skuId|option_code
					String skuIDs[] = skuNo.split("\\|");

					RequestObject obj = new RequestObject();
					obj.setSku(skuIDs[0]);
					try {
						if(purchasePrice!=null){
							String markPrice = null;
							try {
								Map tempmap = skuPriceService.getNewSkuPriceBySku(supplierId, skuNo);
								Map map =(Map) tempmap.get(supplierId);
								markPrice =(String) map.get(skuNo);
						        if(!"-1".equals(markPrice)){
						        	String price = markPrice.split("\\|")[1];
						        	if(!"-1".equals(price)){
						        		obj.setPayment_price(price);
						        	}else{
						        		obj.setPayment_price(purchasePrice);
						        	}
						        	
						        }else{
						        	obj.setPayment_price(purchasePrice);
						        }
							} catch (ServiceException e) {
								obj.setPayment_price(purchasePrice);
								System.out.println("sku"+skuNo+"没有供货价");
					        	logger.info("异常错误："+e.getMessage());
							}
						}
						String eventId = eventProductService.selectEventIdBySku(skuIDs[0], supplierId);
						System.out.println("获取的活动Id："+eventId);
						logger.info("eventId"+eventId);
						obj.setEvent_id(eventId);
					} catch (ServiceException e) {
						e.printStackTrace();
					}
					String code = skuIDs[1];
					if ("A".equals(code)) {
						obj.setOption_code("");
					} else {
						obj.setOption_code(code);
					}
					obj.setQty(num);
					logger.info(" 推送参数 ：" + obj.toString());
					list.add(obj);
				}
			}
			array = JSONArray.fromObject(list);
		}catch(Exception ex){
			
		}
		if(array!=null){
			return array.toString();
		}else{
			return null;
		}
		
	}

	private void sendMail(final OrderDTO orderDTO) {
		
		try{
			long tim = Long.parseLong(time);
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
				Thread t = new Thread(	 new Runnable() {
					@Override
					public void run() {
						try {
							System.out.println("email");

							SendMail.sendMessage(smtpHost, from, fromUserPassword, to, subject,"reebonz订单"+orderDTO.getSpOrderId()+"出现错误,已置为不做处理，原因："+orderDTO.getExcDesc(), messageType);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				t.start();

			}else{
				orderDTO.setExcState("1");
			}
		}catch(Exception x){
			logger.info(" 发邮件失败 ：" + x.getMessage());
			System.out.println(" 发邮件失败 ：" + x.getMessage());
		}
		
		
	}

	@Override
	public void handleRefundlOrder(ReturnOrderDTO deleteOrder) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleEmail(OrderDTO orderDTO) {
		
		try {
			if(StringUtils.isNotBlank(orderDTO.getSupplierOrderNo())&&orderDTO.getSupplierOrderNo().trim().startsWith("1")){

				SendMail.sendGroupMail(smtpHost, from, fromUserPassword, toReebonz, subject,"reebonz order has been cancelled : spPurchaseNo="+orderDTO.getSpPurchaseNo() +" , Reservation ID:"+orderDTO.getSupplierOrderNo(), messageType);
			}
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
	}

	public void updateEvent(){
   	 //拉取活动，更新活动结束时间
       String json = null;
		try {
			json = HttpUtil45
					.get(eventUrl,
							new OutTimeConfig(1000 * 60*5, 1000 * 60*5, 1000 * 60*5),
							null);
		} catch (Exception e) {
		}
		logger.info("抓取的活动列表："+json);
		System.out.println("抓取的活动列表："+json);
		List<Item> list = getProductList(json);
		for(Item item:list){
			String eventId = item.getEvent_id();
			String eventStart = item.getEvent_start_date();
			String eventEnd = item.getEvent_end_date();
			EventProductDTO dto = new EventProductDTO();
			dto.setEventId(eventId);
			dto.setStartDate(eventStart);
			dto.setEndDate(eventEnd);
			dto.setSupplierId(supplierId);
			try {
				eventProductService.updateEventDate(dto);
			} catch (ServiceMessageException e) {
			}
		}
   }
   private static List<Item> getProductList(String json) {
		
		if("{\"error\":\"发生异常错误\"}".equals(json)){
			logger.info("网络连接："+json);
			return null;
		}else{
			ResponseObject obj = new Gson().fromJson(json, ResponseObject.class);
			if(obj!=null){
				if("1".equals(obj.getReturn_code())){
					Object o = obj.getResponse();
					JSONObject jsonObject = JSONObject.fromObject(o); 
					Items list = new Gson().fromJson(jsonObject.toString(), Items.class);
					return list.getDocs();
				}else{
					logger.info("发生异常："+obj.getError_msg());
					return null;
				}
			}else{
				return null;
			}
			
		}
   }


}
