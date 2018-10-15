package com.shangpin.iog.mcw.dto;

public class SkuDTO {

    //{"product_sku":"3376856_40","qty":"0.0000"}
    /**
     * 供应商原始 skuNo
     */
    private String product_sku;
    /**
     * 库存
     */
    private String qty;
	/**
	 * @return the product_sku
	 */
	public String getProduct_sku() {
		return product_sku;
	}
	/**
	 * @param product_sku the product_sku to set
	 */
	public void setProduct_sku(String product_sku) {
		this.product_sku = product_sku;
	}
	/**
	 * @return the qty
	 */
	public String getQty() {
		return qty;
	}
	/**
	 * @param qty the qty to set
	 */
	public void setQty(String qty) {
		this.qty = qty;
	}
}
