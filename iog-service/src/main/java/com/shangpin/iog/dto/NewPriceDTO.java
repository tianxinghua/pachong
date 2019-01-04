package com.shangpin.iog.dto;

import java.io.Serializable;

public class NewPriceDTO implements Serializable{
	private static final long serialVersionUID = -8960086791613738637L;
	private String supplierId;//供货商ID  必填
    private String skuId;//必填
    private String marketPrice;//市场价  三个价格必须有一个 需要和BD沟通 那个价格是算尚品的供货价的价格
    private String supplierPrice;//供货商价格
    private String salePrice;
    private String newMarketPrice;//市场价  三个价格必须有一个 需要和BD沟通 那个价格是算尚品的供货价的价格
    private String newSupplierPrice;//供货商价格
    private String newSalePrice;
	public String getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}
	public String getSkuId() {
		return skuId;
	}
	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}
	public String getMarketPrice() {
		return marketPrice;
	}
	public void setMarketPrice(String marketPrice) {
		this.marketPrice = marketPrice;
	}
	public String getSupplierPrice() {
		return supplierPrice;
	}
	public void setSupplierPrice(String supplierPrice) {
		this.supplierPrice = supplierPrice;
	}
	public String getNewMarketPrice() {
		return newMarketPrice;
	}
	public void setNewMarketPrice(String newMarketPrice) {
		this.newMarketPrice = newMarketPrice;
	}
	public String getNewSupplierPrice() {
		return newSupplierPrice;
	}
	public void setNewSupplierPrice(String newSupplierPrice) {
		this.newSupplierPrice = newSupplierPrice;
	}
	public String getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(String salePrice) {
		this.salePrice = salePrice;
	}
	public String getNewSalePrice() {
		return newSalePrice;
	}
	public void setNewSalePrice(String newSalePricel) {
		this.newSalePrice = newSalePricel;
	}
    
}
