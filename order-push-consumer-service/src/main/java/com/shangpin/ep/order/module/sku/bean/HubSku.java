package com.shangpin.ep.order.module.sku.bean;

import java.io.Serializable;
import java.util.Date;
/**
 * <p>Title:HubSku.java </p>
 * <p>Description: HUB订单系统sku实体</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月18日 下午2:36:28
 */
public class HubSku implements Serializable {
    private String id;

    private String supplierId;

    private String spuId;

    private String skuId;

    private String productName;

    private String marketPrice;

    private String salePrice;

    private String supplierPrice;

    private String barcode;

    /**
     * 货号
     */
    private String productCode;

    private String color;

    private String productDescription;

    private String saleCurrency;

    private String productSize;

    private String stock;

    private String memo;

    private Date createTime;

    private Date lastTime;

    private String newMarketPrice;

    private String newSalePrice;

    private String newSupplierPrice;

    private Date updateTime;

    private String eventStartDate;

    private String eventEndDate;

    private String measurement;

    private String spSkuId;

    private String status;

    private String spStatus;

    private String spProductCode;

    private String picUrlDefault;

    private String isExclude;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId == null ? null : supplierId.trim();
    }

    public String getSpuId() {
        return spuId;
    }

    public void setSpuId(String spuId) {
        this.spuId = spuId == null ? null : spuId.trim();
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId == null ? null : skuId.trim();
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName == null ? null : productName.trim();
    }

    public String getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(String marketPrice) {
        this.marketPrice = marketPrice == null ? null : marketPrice.trim();
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice == null ? null : salePrice.trim();
    }

    public String getSupplierPrice() {
        return supplierPrice;
    }

    public void setSupplierPrice(String supplierPrice) {
        this.supplierPrice = supplierPrice == null ? null : supplierPrice.trim();
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode == null ? null : barcode.trim();
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode == null ? null : productCode.trim();
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color == null ? null : color.trim();
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription == null ? null : productDescription.trim();
    }

    public String getSaleCurrency() {
        return saleCurrency;
    }

    public void setSaleCurrency(String saleCurrency) {
        this.saleCurrency = saleCurrency == null ? null : saleCurrency.trim();
    }

    public String getProductSize() {
        return productSize;
    }

    public void setProductSize(String productSize) {
        this.productSize = productSize == null ? null : productSize.trim();
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock == null ? null : stock.trim();
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastTime() {
        return lastTime;
    }

    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }

    public String getNewMarketPrice() {
        return newMarketPrice;
    }

    public void setNewMarketPrice(String newMarketPrice) {
        this.newMarketPrice = newMarketPrice == null ? null : newMarketPrice.trim();
    }

    public String getNewSalePrice() {
        return newSalePrice;
    }

    public void setNewSalePrice(String newSalePrice) {
        this.newSalePrice = newSalePrice == null ? null : newSalePrice.trim();
    }

    public String getNewSupplierPrice() {
        return newSupplierPrice;
    }

    public void setNewSupplierPrice(String newSupplierPrice) {
        this.newSupplierPrice = newSupplierPrice == null ? null : newSupplierPrice.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getEventStartDate() {
        return eventStartDate;
    }

    public void setEventStartDate(String eventStartDate) {
        this.eventStartDate = eventStartDate == null ? null : eventStartDate.trim();
    }

    public String getEventEndDate() {
        return eventEndDate;
    }

    public void setEventEndDate(String eventEndDate) {
        this.eventEndDate = eventEndDate == null ? null : eventEndDate.trim();
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement == null ? null : measurement.trim();
    }

    public String getSpSkuId() {
        return spSkuId;
    }

    public void setSpSkuId(String spSkuId) {
        this.spSkuId = spSkuId == null ? null : spSkuId.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getSpStatus() {
        return spStatus;
    }

    public void setSpStatus(String spStatus) {
        this.spStatus = spStatus == null ? null : spStatus.trim();
    }

    public String getSpProductCode() {
        return spProductCode;
    }

    public void setSpProductCode(String spProductCode) {
        this.spProductCode = spProductCode == null ? null : spProductCode.trim();
    }

    public String getPicUrlDefault() {
        return picUrlDefault;
    }

    public void setPicUrlDefault(String picUrlDefault) {
        this.picUrlDefault = picUrlDefault == null ? null : picUrlDefault.trim();
    }

    public String getIsExclude() {
        return isExclude;
    }

    public void setIsExclude(String isExclude) {
        this.isExclude = isExclude == null ? null : isExclude.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", supplierId=").append(supplierId);
        sb.append(", spuId=").append(spuId);
        sb.append(", skuId=").append(skuId);
        sb.append(", productName=").append(productName);
        sb.append(", marketPrice=").append(marketPrice);
        sb.append(", salePrice=").append(salePrice);
        sb.append(", supplierPrice=").append(supplierPrice);
        sb.append(", barcode=").append(barcode);
        sb.append(", productCode=").append(productCode);
        sb.append(", color=").append(color);
        sb.append(", productDescription=").append(productDescription);
        sb.append(", saleCurrency=").append(saleCurrency);
        sb.append(", productSize=").append(productSize);
        sb.append(", stock=").append(stock);
        sb.append(", memo=").append(memo);
        sb.append(", createTime=").append(createTime);
        sb.append(", lastTime=").append(lastTime);
        sb.append(", newMarketPrice=").append(newMarketPrice);
        sb.append(", newSalePrice=").append(newSalePrice);
        sb.append(", newSupplierPrice=").append(newSupplierPrice);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", eventStartDate=").append(eventStartDate);
        sb.append(", eventEndDate=").append(eventEndDate);
        sb.append(", measurement=").append(measurement);
        sb.append(", spSkuId=").append(spSkuId);
        sb.append(", status=").append(status);
        sb.append(", spStatus=").append(spStatus);
        sb.append(", spProductCode=").append(spProductCode);
        sb.append(", picUrlDefault=").append(picUrlDefault);
        sb.append(", isExclude=").append(isExclude);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        HubSku other = (HubSku) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getSupplierId() == null ? other.getSupplierId() == null : this.getSupplierId().equals(other.getSupplierId()))
            && (this.getSpuId() == null ? other.getSpuId() == null : this.getSpuId().equals(other.getSpuId()))
            && (this.getSkuId() == null ? other.getSkuId() == null : this.getSkuId().equals(other.getSkuId()))
            && (this.getProductName() == null ? other.getProductName() == null : this.getProductName().equals(other.getProductName()))
            && (this.getMarketPrice() == null ? other.getMarketPrice() == null : this.getMarketPrice().equals(other.getMarketPrice()))
            && (this.getSalePrice() == null ? other.getSalePrice() == null : this.getSalePrice().equals(other.getSalePrice()))
            && (this.getSupplierPrice() == null ? other.getSupplierPrice() == null : this.getSupplierPrice().equals(other.getSupplierPrice()))
            && (this.getBarcode() == null ? other.getBarcode() == null : this.getBarcode().equals(other.getBarcode()))
            && (this.getProductCode() == null ? other.getProductCode() == null : this.getProductCode().equals(other.getProductCode()))
            && (this.getColor() == null ? other.getColor() == null : this.getColor().equals(other.getColor()))
            && (this.getProductDescription() == null ? other.getProductDescription() == null : this.getProductDescription().equals(other.getProductDescription()))
            && (this.getSaleCurrency() == null ? other.getSaleCurrency() == null : this.getSaleCurrency().equals(other.getSaleCurrency()))
            && (this.getProductSize() == null ? other.getProductSize() == null : this.getProductSize().equals(other.getProductSize()))
            && (this.getStock() == null ? other.getStock() == null : this.getStock().equals(other.getStock()))
            && (this.getMemo() == null ? other.getMemo() == null : this.getMemo().equals(other.getMemo()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getLastTime() == null ? other.getLastTime() == null : this.getLastTime().equals(other.getLastTime()))
            && (this.getNewMarketPrice() == null ? other.getNewMarketPrice() == null : this.getNewMarketPrice().equals(other.getNewMarketPrice()))
            && (this.getNewSalePrice() == null ? other.getNewSalePrice() == null : this.getNewSalePrice().equals(other.getNewSalePrice()))
            && (this.getNewSupplierPrice() == null ? other.getNewSupplierPrice() == null : this.getNewSupplierPrice().equals(other.getNewSupplierPrice()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getEventStartDate() == null ? other.getEventStartDate() == null : this.getEventStartDate().equals(other.getEventStartDate()))
            && (this.getEventEndDate() == null ? other.getEventEndDate() == null : this.getEventEndDate().equals(other.getEventEndDate()))
            && (this.getMeasurement() == null ? other.getMeasurement() == null : this.getMeasurement().equals(other.getMeasurement()))
            && (this.getSpSkuId() == null ? other.getSpSkuId() == null : this.getSpSkuId().equals(other.getSpSkuId()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getSpStatus() == null ? other.getSpStatus() == null : this.getSpStatus().equals(other.getSpStatus()))
            && (this.getSpProductCode() == null ? other.getSpProductCode() == null : this.getSpProductCode().equals(other.getSpProductCode()))
            && (this.getPicUrlDefault() == null ? other.getPicUrlDefault() == null : this.getPicUrlDefault().equals(other.getPicUrlDefault()))
            && (this.getIsExclude() == null ? other.getIsExclude() == null : this.getIsExclude().equals(other.getIsExclude()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getSupplierId() == null) ? 0 : getSupplierId().hashCode());
        result = prime * result + ((getSpuId() == null) ? 0 : getSpuId().hashCode());
        result = prime * result + ((getSkuId() == null) ? 0 : getSkuId().hashCode());
        result = prime * result + ((getProductName() == null) ? 0 : getProductName().hashCode());
        result = prime * result + ((getMarketPrice() == null) ? 0 : getMarketPrice().hashCode());
        result = prime * result + ((getSalePrice() == null) ? 0 : getSalePrice().hashCode());
        result = prime * result + ((getSupplierPrice() == null) ? 0 : getSupplierPrice().hashCode());
        result = prime * result + ((getBarcode() == null) ? 0 : getBarcode().hashCode());
        result = prime * result + ((getProductCode() == null) ? 0 : getProductCode().hashCode());
        result = prime * result + ((getColor() == null) ? 0 : getColor().hashCode());
        result = prime * result + ((getProductDescription() == null) ? 0 : getProductDescription().hashCode());
        result = prime * result + ((getSaleCurrency() == null) ? 0 : getSaleCurrency().hashCode());
        result = prime * result + ((getProductSize() == null) ? 0 : getProductSize().hashCode());
        result = prime * result + ((getStock() == null) ? 0 : getStock().hashCode());
        result = prime * result + ((getMemo() == null) ? 0 : getMemo().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getLastTime() == null) ? 0 : getLastTime().hashCode());
        result = prime * result + ((getNewMarketPrice() == null) ? 0 : getNewMarketPrice().hashCode());
        result = prime * result + ((getNewSalePrice() == null) ? 0 : getNewSalePrice().hashCode());
        result = prime * result + ((getNewSupplierPrice() == null) ? 0 : getNewSupplierPrice().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getEventStartDate() == null) ? 0 : getEventStartDate().hashCode());
        result = prime * result + ((getEventEndDate() == null) ? 0 : getEventEndDate().hashCode());
        result = prime * result + ((getMeasurement() == null) ? 0 : getMeasurement().hashCode());
        result = prime * result + ((getSpSkuId() == null) ? 0 : getSpSkuId().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getSpStatus() == null) ? 0 : getSpStatus().hashCode());
        result = prime * result + ((getSpProductCode() == null) ? 0 : getSpProductCode().hashCode());
        result = prime * result + ((getPicUrlDefault() == null) ? 0 : getPicUrlDefault().hashCode());
        result = prime * result + ((getIsExclude() == null) ? 0 : getIsExclude().hashCode());
        return result;
    }
}