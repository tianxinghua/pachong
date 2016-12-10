package com.shangpin.ep.order.module.orderapiservice.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.shangpin.ep.order.enumeration.ErrorStatus;
import com.shangpin.ep.order.enumeration.LogTypeStatus;
import com.shangpin.ep.order.enumeration.PushStatus;
import com.shangpin.ep.order.module.order.bean.OrderDTO;
import com.shangpin.ep.order.util.httpclient.HttpUtil45;
import com.shangpin.ep.order.util.httpclient.OutTimeConfig;
import org.springframework.stereotype.Component;

@Component("testLeamServiceImpl")
public class TestLeamServiceImpl extends LeamServiceImpl {

	@Override
	public void handleConfirmOrder(OrderDTO orderDTO) {
		try {

			orderDTO.setConfirmTime(new Date());
			orderDTO.setPushStatus(PushStatus.NO_STOCK);

		} catch (Exception e) {
			orderDTO.setPushStatus(PushStatus.ORDER_CONFIRMED_ERROR);
			handleException.handleException(orderDTO,e);
			orderDTO.setLogContent("推送订单异常========= "+e.getMessage());
			logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
		}
	}

	/**
	 * 根据spu查询供货商库存
	 * @param item_id
	 * @return
	 */
	private String getItemStockBySizeMarketPlace(String item_id,OrderDTO orderDTO) throws Exception {
		Map<String,String> param = new HashMap<String,String>();
		param.put("ITEM_ID", item_id);
		orderDTO.setLogContent("查询库存参数============" + param.toString());
		logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
		String returnData = leamPost(supplierProperties.getLeam().getUrl()+supplierProperties.getLeam().getGetItemStockInterface(), param, new OutTimeConfig(1000*60*10,1000*60*10,1000*60*10),supplierProperties.getLeam().getUser(),supplierProperties.getLeam().getPassword(),orderDTO);
		orderDTO.setLogContent("查询库存返回结果======="+returnData);
		logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
		return returnData;
	}

	/**
	 * 下订单新接口
	 * @param id_order_mrkp 订单号
	 * @param barcode Barcode
	 * @param qty 库存
	 * @return
	 */
	private String newOrderMarketPlace(long id_order_mrkp, String barcode, int qty,OrderDTO orderDTO) throws Exception {
		Map<String,String> param = new HashMap<String,String>();
		param.put("ID_ORDER_MRKP", String.valueOf(id_order_mrkp));
		param.put("BARCODE", barcode);
		param.put("QTY", String.valueOf(qty));
		orderDTO.setLogContent("下单参数============"+param.toString());
		logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
		String returnData = leamPost(supplierProperties.getLeam().getUrl()+supplierProperties.getLeam().getCreateOrderInterface(), param, new OutTimeConfig(1000*60*10,1000*60*10,1000*60*10),supplierProperties.getLeam().getUser(),supplierProperties.getLeam().getPassword(),orderDTO);
		orderDTO.setLogContent("下订单返回结果======="+returnData);
		logCommon.loggerOrder(orderDTO, LogTypeStatus.CONFIRM_LOG);
		return returnData;
	}

}
