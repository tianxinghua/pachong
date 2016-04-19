package com.shangpin.iog.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by loyalty on 15/6/3.
 * SPU价格对象
 */
/*@Getter
@Setter*/
public class SkuPriceDTO {
    private String supplierId;
    private String skuId;

    private String marketPrice;//市场价
    private String supplierPrice;//供货商价格

    private Date createTime=new Date();//创建时间

    private Date updateTime;

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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
