package com.shangpin.api.airshop.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

/**采购单实体类
 * @author wanghua
 *
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeliveryOrdersContent implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//发货单号
	private String sopDeliveryOrderNo;
	//物流公司
	private String logisticsName;
	//物流单号
	private String logisticsOrderNo;
	//发货时间
	private String dateDeliver;
	//收货时间
	private String dateRecieved;
	//预计到达时间，状态值描述：1=1-2天，2=3-4天，3=5-6天，4=1周，5=1-2周
	private String estimateArrivedTime;
	//发货单状态；	格式：List<int>；	状态值描述：1=待发货，2=待收货， 4=已完成
	private String deliveryStatus;	
	//发货联系人
	private String deliveryContacts;
	//发货人联系方式
	private String deliveryContactsPhone;	
	//发货地址
	private String deliveryAddress;
	//发货描述
	private String deliveryMemo;
	//该发货单发往仓库的仓库编号
	private String warehouseNo;
	//该发货单发往仓库的仓库名称
	private String warehouseName;
	//数量
	private String deliveryCount;


}
