package com.shangpin.ep.order.module.order.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by lizhongren on 2017/5/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PriceQueryDTO {
    @JsonIgnore
    String orderTime;
    @JsonIgnore
    String skuNo;
    @JsonIgnore
    String supplierId;

    @JsonProperty("OrderTime")
    public String getOrderTime() {
        return orderTime;
    }
    @JsonProperty("OrderTime")
    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }
    @JsonProperty("SkuNo")
    public String getSkuNo() {
        return skuNo;
    }
    @JsonProperty("SkuNo")
    public void setSkuNo(String skuNo) {
        this.skuNo = skuNo;
    }
    @JsonProperty("SupplierId")
    public String getSupplierId() {
        return supplierId;
    }
    @JsonProperty("SupplierId")
    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }
}
