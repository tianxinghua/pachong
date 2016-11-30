package com.shangpin.message.order.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.message.conf.stream.source.order.message.SupplierOrderDetailSync;
import com.shangpin.message.conf.stream.source.order.message.SupplierOrderSync;
import com.shangpin.message.conf.stream.source.order.sender.OrderStreamSender;
import com.shangpin.message.order.dto.SupplierOrderDTO;
import com.shangpin.message.order.dto.SupplierOrderDetailDTO;

/**
 * <p>
 * Title:SupplierOrderService.java
 * </p>
 * <p>
 * Description: 供货商订单流数据业务逻辑处理
 * </p>
 * <p>
 * Company: www.shangpin.com
 * </p>
 * 
 * @author yanxiaobin
 * @date 2016年11月23日 下午4:06:52
 */
@Service
public class SupplierOrderService {
	
	@Autowired
	private OrderStreamSender orderStreamSender;
	
	/**
	 * 重采
	 */
	public static final String SYNC_TYPE_REPURCHASE_ORDER = "RePurchaseSupplierOrder";

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 根据调用方传递的订单流数据以供货商为维度进行拆单
	 * 
	 * @param dto 订单流数据
	 * @return 拆分之后的订单
	 */
	public List<SupplierOrderSync> splitOrder(SupplierOrderDTO dto) {
		String syncType = dto.getSyncType();
		if (StringUtils.isBlank(syncType)) {
			throw new RuntimeException("消息类型不可为空");
		} else if (syncType.equals(SYNC_TYPE_REPURCHASE_ORDER)) {// 重采类型拆分方式
			return doSplit(dto,true);
		} else {// 非重采类型拆分方式
			return doSplit(dto, false);
		}
	}
	/**
	 * 拆分订单
	 * @param dto 
	 * @param flag 是否是重采类型
	 * @return 拆分之后的订单
	 */
	private List<SupplierOrderSync> doSplit(SupplierOrderDTO dto, boolean flag) {
		List<SupplierOrderSync> supplierOrderSyncs = null;
		List<SupplierOrderDetailDTO> syncDetailDto = dto.getSyncDetailDto();
		if (CollectionUtils.isNotEmpty(syncDetailDto)) {
			supplierOrderSyncs = new ArrayList<>();
			Map<String , List<SupplierOrderDetailDTO>> supplierOrder = new HashMap<>();
			for (SupplierOrderDetailDTO supplierOrderDetailDTO : syncDetailDto) {//此循环以供应商为单位进行拆分订单
				String supplierNo = supplierOrderDetailDTO.getSupplierNo();
				if (supplierOrder.containsKey(supplierNo)) {
					supplierOrder.get(supplierNo).add(supplierOrderDetailDTO);
				} else {
					List<SupplierOrderDetailDTO> detailDTOs = new ArrayList<>();
					detailDTOs.add(supplierOrderDetailDTO);
					supplierOrder.put(supplierNo, detailDTOs);
				}
			}
			for (Entry<String, List<SupplierOrderDetailDTO>> entry : supplierOrder.entrySet()) {//此循环以供应商为单位一单一件的方式进行拆分
				int counter= 0;//计数器
				for (SupplierOrderDetailDTO detailDTO : entry.getValue()) {
					Integer quantity = detailDTO.getQuantity();
					if (quantity != null) {
						for (int i = 1; i <= quantity; i++) {
							String orderNo = null;
							if (flag) {//重采
								orderNo = detailDTO.getSupplierOrderNo();
							} else {//非重采
								if (entry.getValue().size() == 1 && quantity ==1) {//一个供货商只有一个子单并且只有一件商品的时候处理逻辑
									orderNo = dto.getOrderNo();//以主单号为准推送给供货商	
								} else {//一个供货商不是一个子单的时候或者数量不是一件的时候
									orderNo = new StringBuilder(dto.getOrderNo()).append(++counter).toString();//在主单号的基础上追加1，2，3.......
								}
							}
							List<SupplierOrderDetailSync> detailSyncs= new ArrayList<>();
							SupplierOrderDetailSync supplierOrderDetailSync = new SupplierOrderDetailSync(detailDTO.getSupplierNo(), detailDTO.getSkuNo(), 1, detailDTO.getSupplierOrderNo(), detailDTO.getPurchaseOrderNo(), detailDTO.getOriginalSupplierOrderNo(), orderNo);
									detailSyncs.add(supplierOrderDetailSync);
							SupplierOrderSync supplierOrderSync = new SupplierOrderSync(UUID.randomUUID().toString(), dto.getMessageId(), sdf.format(new Date()), dto.getOrderNo(), dto.getSyncType(), detailSyncs);
							supplierOrderSyncs.add(supplierOrderSync);
						}
					} else {
						throw new RuntimeException("订购数量为空");
					}	
				}
				counter = 0;//计数器清零
			}	
		} else {
			throw new RuntimeException("子单列表为空");
		}
		return supplierOrderSyncs;
	}
	/**
	 * 发送订单
	 * @param supplierOrders 订单流数据
	 * @return 是否发送成功
	 */
	public boolean sendOrder(List<SupplierOrderSync> supplierOrders) {
		for (SupplierOrderSync supplierOrderSync : supplierOrders) {
			orderStreamSender.orderStreamSend(supplierOrderSync);
		}
		return true;
	}

}
