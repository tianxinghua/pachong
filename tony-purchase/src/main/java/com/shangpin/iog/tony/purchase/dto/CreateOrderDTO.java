package com.shangpin.iog.tony.purchase.dto;

import java.util.List;

/**
 * Created by Administrator on 2015/9/28.
 */
public class CreateOrderDTO {

    private String merchantId;
    private String token;
    private String shopOrderId;
    private String status;
    private String statusDate;
    private String orderDate;
    private ItemDTO[] items;

    public ItemDTO[] getItems() {
        return items;
    }

    public void setItems(ItemDTO[] items) {
        this.items = items;
    }

    private double orderTotalPrice;
    private ShippingInfoDTO shippingInfo;
    private BillingInfoDTO billingInfo;

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

    public String getShopOrderId() {
        return shopOrderId;
    }

    public void setShopOrderId(String shopOrderId) {
        this.shopOrderId = shopOrderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(String statusDate) {
        this.statusDate = statusDate;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public double getOrderTotalPrice() {
        return orderTotalPrice;
    }

    public void setOrderTotalPrice(double orderTotalPrice) {
        this.orderTotalPrice = orderTotalPrice;
    }
    public void setOrderTotalPrice(String orderTotalPrice) {
        if(orderTotalPrice == null || "".equals(orderTotalPrice.trim()))
            this.orderTotalPrice = 0;
        this.orderTotalPrice = Double.parseDouble(orderTotalPrice);
    }

    public ShippingInfoDTO getShippingInfo() {
        return shippingInfo;
    }

    public void setShippingInfo(ShippingInfoDTO shippingInfo) {
        this.shippingInfo = shippingInfo;
    }

    public BillingInfoDTO getBillingInfo() {
        return billingInfo;
    }

    public void setBillingInfo(BillingInfoDTO billingInfo) {
        this.billingInfo = billingInfo;
    }
}
