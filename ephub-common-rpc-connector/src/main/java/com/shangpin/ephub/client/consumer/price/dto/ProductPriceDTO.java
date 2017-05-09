package com.shangpin.ephub.client.consumer.price.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by lizhongren on 2016/9/19.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProductPriceDTO {
	Long supplierPriceChangeRecordId;//主键
    String sopUserNo; //门户编号
    String supplierNo;//供货商编号
    String skuNo;     //尚品的SKU编号
    String supplierSkuNo;//供货商skuNo
    String marketPrice;//市场价
    String purchasePrice;//采购价
    String createUserName;
    String marketYear;
    String marketSeason;
    String currency;
    String memo;
    @JsonProperty("SupplierPriceChangeRecordId")
    public Long getSupplierPriceChangeRecordId(){
    	return this.supplierPriceChangeRecordId;
    }
    @JsonProperty("SupplierPriceChangeRecordId")
    public void setSupplierPriceChangeRecordId(Long supplierPriceChangeRecordId){
    	this.supplierPriceChangeRecordId = supplierPriceChangeRecordId;
    }

    @JsonProperty("SopUserNo")
    public String getSopUserNo() {
        return sopUserNo;
    }
    @JsonProperty("SopUserNo")
    public void setSopUserNo(String sopUserNo) {
        this.sopUserNo = sopUserNo;
    }


    @JsonProperty("SupplierNo")
    public String getSupplierNo() {
        return supplierNo;
    }
    @JsonProperty("SupplierNo")
    public void setSupplierNo(String supplierNo) {
        this.supplierNo = supplierNo;
    }

    @JsonProperty("SkuNo")
    public String getSkuNo() {
        return skuNo;
    }

    @JsonProperty("SkuNo")
    public void setSkuNo(String skuNo) {
        this.skuNo = skuNo;
    }

    @JsonProperty("SupplierSkuNo")
    public String getSupplierSkuNo() {
        return supplierSkuNo;
    }
    @JsonProperty("SupplierSkuNo")
    public void setSupplierSkuNo(String supplierSkuNo) {
        this.supplierSkuNo = supplierSkuNo;
    }

    @JsonProperty("MarketPrice")
    public String getMarketPrice() {
        return marketPrice;
    }
    @JsonProperty("MarketPrice")
    public void setMarketPrice(String marketPrice) {
        this.marketPrice = marketPrice;
    }

    @JsonProperty("PurchasePrice")
    public String getPurchasePrice() {
        return purchasePrice;
    }

    @JsonProperty("PurchasePrice")
    public void setPurchasePrice(String purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    @JsonProperty("CreateUserName")
    public String getCreateUserName() {
        return createUserName;
    }
    @JsonProperty("CreateUserName")
    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }
    @JsonProperty("Memo")
    public String getMemo() {
        return memo;
    }
    @JsonProperty("Memo")
    public void setMemo(String memo) {
        this.memo = memo;
    }

    @JsonProperty("MarketYear")
    public String getMarketYear() {
        return marketYear;
    }

    @JsonProperty("MarketYear")
    public void setMarketYear(String marketYear) {
        this.marketYear = marketYear;
    }

    @JsonProperty("MarketSeason")
    public String getMarketSeason() {
        return marketSeason;
    }

    @JsonProperty("MarketSeason")
    public void setMarketSeason(String marketSeason) {
        this.marketSeason = marketSeason;
    }

    @JsonProperty("Currency")
    public String getCurrency() {
        return currency;
    }

    @JsonProperty("Currency")
    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
