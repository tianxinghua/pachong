package com.shangpin.iog.tony.purchase.dto;

public class ObjectOrder {
	
	private String merchantId;
    private String token;
    private String [] shopOrderIds;
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String[] getShopOrderIds() {
		return shopOrderIds;
	}
	public void setShopOrderIds(String [] shopOrderIds) {
		this.shopOrderIds = shopOrderIds;
	}

}
