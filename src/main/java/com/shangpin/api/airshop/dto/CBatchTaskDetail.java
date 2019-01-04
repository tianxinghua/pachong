package com.shangpin.api.airshop.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CBatchTaskDetail {
	
	private String sopPurchaseOrderNo; //该商品所在采购单的采购单号
	private String productModel; //货号
	private String supplierSkuNo; //商品的供应商Sku编码
	private String brandName; //品牌
	private String skuPrice;
	private String qty; //数量
	private String orderNo; //尚品订单号
	private String logisticsOrderNo; //物流单号
	private String productName;
	private String status;
	private String requestTimes;
	private String path;
	private String supplierOrderNo;
	private String serviceUrl;
	
	private String importType;

}
