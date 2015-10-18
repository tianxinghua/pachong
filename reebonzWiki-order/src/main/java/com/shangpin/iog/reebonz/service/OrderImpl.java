package com.shangpin.iog.reebonz.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsOrderService;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.SendMail;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.dto.ReturnOrderDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.ice.dto.OrderStatus;
import com.shangpin.iog.reebonz.dto.Order;
import com.shangpin.iog.reebonz.dto.RequestObject;
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
	private static String subject = null;
	private static String messageText = null;
	private static String messageType = null;
	private static String time = null;
	private ReservationProStock stock = new ReservationProStock();
	static {
		if(null==bdl){
			bdl=ResourceBundle.getBundle("conf");
		}
		supplierId = bdl.getString("supplierId");
		supplierNo = bdl.getString("supplierNo");
		smtpHost = bdl.getString("smtpHost");
		from = bdl.getString("from");

		fromUserPassword = bdl.getString("fromUserPassword");
		to = bdl.getString("to");
		subject = bdl.getString("subject");
		messageText = bdl.getString("messageText");
		messageType = bdl.getString("messageType");
		time = bdl.getString("time");
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

		try{
			String order_id = orderDTO.getSpOrderId();
			String order_site = "shangpin";
			String data = getJsonData(orderDTO.getDetail(),null);
			logger.info("锁库存推送的数据：data："+data+",order_id:"+order_id);
			Map<String, String> map = stock.lockStock(order_id, order_site, data);
			if (map.get("1") != null) {
				orderDTO.setExcState("0");
				orderDTO.setSupplierOrderNo(map.get("0"));
				orderDTO.setStatus(OrderStatus.PLACED);

			} else{
				sendMail(orderDTO);
				orderDTO.setExcDesc(map.get("-1"));
			}

//			else if(map.get("0")!=null){
//				orderDTO.setExcState("0");
//				orderDTO.setSupplierOrderNo(map.get("0"));
//				orderDTO.setStatus(OrderStatus.NOHANDLE);
//
//			}
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
	public void handleConfirmOrder(OrderDTO orderDTO) {

		try{
			String data = getJsonData(orderDTO.getDetail(),orderDTO.getPurchasePriceDetail());

			Map<String, String> map = null;
			logger.info("推送订单的数据：data："+data+",SupplierOrderNo:"+orderDTO.getSupplierOrderNo()+",OrderId:"+orderDTO.getSpOrderId()+",SpPurchaseNo:"+ orderDTO.getSpPurchaseNo());
			map = stock.pushOrder(orderDTO.getSupplierOrderNo(),
					orderDTO.getSpOrderId(), orderDTO.getSpPurchaseNo(), data);
			// 1：代表发生了异常
			if (map.get("1") != null) {
				sendMail(orderDTO);
				orderDTO.setExcDesc(map.get("1"));
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
	public void handleCancelOrder(ReturnOrderDTO deleteOrder) {
		try{
			Map<String, String> map = null;
			map = stock.unlockStock(deleteOrder.getSupplierOrderNo(),
					deleteOrder.getSpOrderId(), deleteOrder.getSpOrderId(),
					"voided");// deducted" (for confirmation) "voided" (for reversal)
			if (map.get("1") != null) {
				if(DateTimeUtil.getTimeDifference(deleteOrder.getCreateTime(),new Date())/(Integer.parseInt(time))>0){
					deleteOrder.setExcState("0");
					//超过一天 不需要在做处理 订单状态改为其它状体
					deleteOrder.setStatus(OrderStatus.NOHANDLE);
					new Runnable() {
						@Override
						public void run() {
							try {
								SendMail.sendMessage(smtpHost, from, fromUserPassword, to, subject,messageText, messageType);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					};
				}else{
					deleteOrder.setExcState("1");
				}
				deleteOrder.setExcDesc(map.get("1"));

			} else {
				deleteOrder.setExcState("0");
				deleteOrder.setStatus(OrderStatus.CANCELLED);
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
						obj.setPayment_price(purchasePrice);
//						SkuDTO sku = skuPriceService.findSupplierPrice(supplierId, skuNo);
//						if(sku!=null){
//							String supplierPrice = sku.getSupplierPrice();
//							System.out.println("获取的进货价："+supplierPrice);
//							
//						}
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
		JSONArray array = JSONArray.fromObject(list);
		return array.toString();
	}

	private void sendMail(final OrderDTO orderDTO) {
		long tim = Long.parseLong(time);
		if(DateTimeUtil.getTimeDifference(orderDTO.getCreateTime(),new Date())/(tim)>1){
			//超过一天 不需要在做处理 订单状态改为其它状体
			orderDTO.setExcState("0");
			orderDTO.setStatus(OrderStatus.NOHANDLE);
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
	}



}
