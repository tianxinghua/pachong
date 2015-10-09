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
import com.shangpin.iog.service.ProductFetchService;

public class OrderImpl extends AbsOrderService {
	private static ResourceBundle bdl = null;
	private static String supplierId = null;
	private ReservationProStock stock = new ReservationProStock();
	static {
		 if(null==bdl){
			 bdl=ResourceBundle.getBundle("conf");
		 }
		 supplierId = bdl.getString("supplierId");
	}
	
	public void loopExecute() {
		this.checkoutOrderFromWMS(supplierId, "", false);
	}

	/**
	 * 锁库存
	 */
	@Override
	public void handleSupplierOrder(OrderDTO orderDTO) {

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
	}

	/**
	 * 推送订单
	 */
	@Override
	public void handleConfirmOrder(OrderDTO orderDTO) {

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
		}
	}

	/**
	 * 解除库存锁
	 */
	@Override
	public void handleCancelOrder(ReturnOrderDTO deleteOrder) {
		Map<String, String> map = null;
		map = stock.unlockStock(deleteOrder.getSupplierOrderNo(),
				deleteOrder.getSpOrderId(), deleteOrder.getSpOrderId(),
				"voided");// deducted" (for confirmation) "voided" (for
							// reversal)
		if (map.get("1") != null) {
			deleteOrder.setExcDesc(map.get("1"));
			deleteOrder.setExcState("1");
		} else {
			deleteOrder.setStatus(OrderStatus.CANCELLED);
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
		// TODO Auto-generated method stub

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
				obj.setEvent_id(skuIDs[1]);
				String code = skuIDs[2];
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
