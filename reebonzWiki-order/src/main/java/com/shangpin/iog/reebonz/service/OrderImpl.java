package com.shangpin.iog.reebonz.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsOrderService;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.dto.ReturnOrderDTO;
import com.shangpin.iog.ice.dto.OrderStatus;
import com.shangpin.iog.reebonz.dto.Order;
import com.shangpin.iog.reebonz.dto.RequestObject;
import com.shangpin.iog.service.EventProductService;
import com.shangpin.iog.service.ProductFetchService;

@Component
public class OrderImpl extends AbsOrderService {
	@Autowired
	EventProductService eventProductService;
	private static ResourceBundle bdl = null;
	private static String supplierId = null;
	private static String supplierNo = null;
	private ReservationProStock stock = new ReservationProStock();
	static {
		 if(null==bdl){
			 bdl=ResourceBundle.getBundle("conf");
		 }
		 supplierId = bdl.getString("supplierId");
		 supplierNo = bdl.getString("supplierNo");
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
			orderDTO.setExcState("0");
			String order_id = orderDTO.getSpOrderId();
			String order_site = "shangpin";
			String data = getJsonData(orderDTO.getDetail());

			Map<String, String> map = stock.lockStock(order_id, order_site, data);
			if (map.get("1") != null) {
				orderDTO.setExcDesc(map.get("1"));
				orderDTO.setExcState("1");
			} else {
				orderDTO.setSupplierOrderNo(map.get("0"));
				orderDTO.setStatus(OrderStatus.PLACED);
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
	public void handleConfirmOrder(OrderDTO orderDTO) {
		
		try{
			String data = getJsonData(orderDTO.getDetail());
			Map<String, String> map = null;
			map = stock.pushOrder(orderDTO.getSupplierOrderNo(),
					orderDTO.getSpOrderId(), orderDTO.getSpPurchaseNo(), data);
			// 1：代表发生了异常
			if (map.get("1") != null) {
				orderDTO.setExcDesc(map.get("1"));
				orderDTO.setExcState("1");
			} else {
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
				deleteOrder.setExcDesc(map.get("1"));
				deleteOrder.setExcState("1");
			} else {
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
	private String getJsonData(String detail) {

		List<RequestObject> list = null;
		if (detail != null) {
			list = new ArrayList<RequestObject>();
			String[] details = detail.split(",");
			for (int i = 0; i < details.length; i++) {
				// detail[i]数据格式==>skuId:数量
				String num = details[i].split(":")[1];
				String skuNo = details[i].split(":")[0];
				// skuNo数据格式：skuId|eventId|option_code
				String skuIDs[] = skuNo.split("\\|");

				RequestObject obj = new RequestObject();
				obj.setSku(skuIDs[0]);
				try {
					String eventId = eventProductService.selectEventIdBySku(skuIDs[0], supplierId);
					obj.setEvent_id(eventId);
				} catch (ServiceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				obj.setEvent_id(skuIDs[1]);
				String code = skuIDs[1];
				if ("A".equals(code)) {
					obj.setOption_code("");
				} else {
					obj.setOption_code(code);
				}
				obj.setQty(num);
				list.add(obj);
			}
		}
		JSONArray array = JSONArray.fromObject(list);
		return array.toString();
	}




}
