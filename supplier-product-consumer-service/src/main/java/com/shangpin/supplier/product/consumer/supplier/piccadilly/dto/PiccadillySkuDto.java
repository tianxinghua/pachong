package com.shangpin.supplier.product.consumer.supplier.piccadilly.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PiccadillySkuDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8288453007083329957L;
	
	private String id;//必填
    private String supplierId;//供货商ID  必填
    private String skuId;//必填
    private String spuId;//必填
    private String productName;
    private String marketPrice;//市场价  三个价格必须有一个 需要和BD沟通 那个价格是算尚品的供货价的价格
    private String salePrice;//销售价格
    private String supplierPrice;//供货商价格
    private String barcode;//条形码
    private String productCode;//货号
    private String color;// 颜色 必填
    private String productDescription;//描述
    private String saleCurrency;//币种
    private String productSize;//尺码   必填
    private String stock;//库存  必填   如果库存等于0的 不存
    private String memo;  //备注
    private String newMarketPrice; //新的市场价
    private String newSalePrice;
    private String newSupplierPrice;
    private String eventStartDate;
    private String eventEndDate;
    private String measurement;
    private String spSkuId;
    private String spStatus;
    private String spProductCode;

}
