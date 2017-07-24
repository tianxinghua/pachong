package com.shangpin.supplier.product.consumer.supplier.coccolebimbi.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Sku {

	private String skuId;
	private String barCode;
	private String size;
	private String qty;
	
	 private String marketPrice;//市场价  三个价格必须有一个 需要和BD沟通 那个价格是算尚品的供货价的价格
	    private String salePrice;//销售价格
	    private String supplierPrice;//供货商价格
	    private String saleCurrency;//币种
}
