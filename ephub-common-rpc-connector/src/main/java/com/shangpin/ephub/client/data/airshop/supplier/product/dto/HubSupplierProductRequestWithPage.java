package com.shangpin.ephub.client.data.airshop.supplier.product.dto;
import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * Created by loyalty on 15/9/15.
 * 产品信息
 */
@Setter
@Getter
@ToString
public class HubSupplierProductRequestWithPage implements Serializable{
	
	private static final long serialVersionUID = -3125067250395773889L;
	private String Status;
	private String barcode;
	private String brandName;
	private String SpStatus;
	private String Memo;
	private String productCode;
	private String productName;
	private String skuId;
	private String spSkuId;
	private String color;//颜色 
	private String size;//尺码 
	private String categoryName;
	private String sizeClass;
	private String startDate;
	private String endDate;
	private Integer pageIndex;
	private Integer pageSize;
	private String supplierId;
	private Long supplierSpuId;
	private Long supplierSkuId;
}

