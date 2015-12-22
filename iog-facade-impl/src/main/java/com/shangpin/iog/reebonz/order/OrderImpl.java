package com.shangpin.iog.reebonz.order;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.mail.MessagingException;

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
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.SendMail;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.EventProductDTO;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.dto.ReturnOrderDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.facade.dubbo.dto.RequestObject;
import com.shangpin.iog.service.EventProductService;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.SkuPriceService;
@Component
public class OrderImpl{

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
		supplierId = bdl.getString("supplierId");
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

	/**
	 * 锁库存
	 */
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
				orderDTO.setStatus("placed");
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
//						if(purchasePrice!=null){
//						    obj.setPayment_price(purchasePrice);
//						}
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
						System.out.println("获取的活动Id："+eventId+",推送的价格："+obj.getPayment_price());
						logger.info("eventId"+eventId+"，推送的价格："+obj.getPayment_price());
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
				orderDTO.setStatus("nohandle");
				//超过一天 不需要在做处理 订单状态改为其它状体
				orderDTO.setExcState("0");
				Thread t = new Thread(	 new Runnable() {
					@Override
					public void run() {
						try {
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
}
