package com.shangpin.api.airshop.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class PurchaseOrderDetailForGather  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -445907772164101264L;
	private String sopPurchaseOrderNo;
	private String sopPurchaseOrderDetailNo;

	private String warehouseNo;
	private String warehouseName;
	private String warehouseAddress;
	private String warehousePost;
	private String warehouseContactPerson;
	private String warehouseContactMobile;
	private String createTime; //采购单生成时间
	private List<PurOrderSkuDetail> skuList;
	private String logisticsName;
	private String logisticsOrderNo;
	

}
