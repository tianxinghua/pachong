package com.shangpin.ephub.data.mysql.sku.price.po;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class HubSupplierPriceChangeRecord implements Serializable {
    private Long supplierPriceChangeRecordId;

    private String supplierId;

    private String supplierNo;

    private String supplierSkuNo;

    private String supplierSpuNo;

    private String spSkuNo;

    private BigDecimal marketPrice;

    private BigDecimal supplyPrice;

    private String currency;

    private String supplierSeason;

    private String marketYear;

    private String marketSeason;

    /**
     * 1:　价格  ２：季节  ３　: 价格 +　季节
     */
    private Byte type;

    /**
     * 0:未处理 1、推送消息队列完成 2：消息推送失败 3：处理完成 4：处理失败
     */
    private Byte state;

    private String createUserName;

    private Date createTime;

    private Date updateTime;

    private String memo;

    private static final long serialVersionUID = 1L;

    public Long getSupplierPriceChangeRecordId() {
        return supplierPriceChangeRecordId;
    }

    public void setSupplierPriceChangeRecordId(Long supplierPriceChangeRecordId) {
        this.supplierPriceChangeRecordId = supplierPriceChangeRecordId;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId == null ? null : supplierId.trim();
    }

    public String getSupplierNo() {
        return supplierNo;
    }

    public void setSupplierNo(String supplierNo) {
        this.supplierNo = supplierNo == null ? null : supplierNo.trim();
    }

    public String getSupplierSkuNo() {
        return supplierSkuNo;
    }

    public void setSupplierSkuNo(String supplierSkuNo) {
        this.supplierSkuNo = supplierSkuNo == null ? null : supplierSkuNo.trim();
    }

    public String getSupplierSpuNo() {
        return supplierSpuNo;
    }

    public void setSupplierSpuNo(String supplierSpuNo) {
        this.supplierSpuNo = supplierSpuNo == null ? null : supplierSpuNo.trim();
    }

    public String getSpSkuNo() {
        return spSkuNo;
    }

    public void setSpSkuNo(String spSkuNo) {
        this.spSkuNo = spSkuNo == null ? null : spSkuNo.trim();
    }

    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    public BigDecimal getSupplyPrice() {
        return supplyPrice;
    }

    public void setSupplyPrice(BigDecimal supplyPrice) {
        this.supplyPrice = supplyPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency == null ? null : currency.trim();
    }

    public String getSupplierSeason() {
        return supplierSeason;
    }

    public void setSupplierSeason(String supplierSeason) {
        this.supplierSeason = supplierSeason == null ? null : supplierSeason.trim();
    }

    public String getMarketYear() {
        return marketYear;
    }

    public void setMarketYear(String marketYear) {
        this.marketYear = marketYear == null ? null : marketYear.trim();
    }

    public String getMarketSeason() {
        return marketSeason;
    }

    public void setMarketSeason(String marketSeason) {
        this.marketSeason = marketSeason == null ? null : marketSeason.trim();
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName == null ? null : createUserName.trim();
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

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", supplierPriceChangeRecordId=").append(supplierPriceChangeRecordId);
        sb.append(", supplierId=").append(supplierId);
        sb.append(", supplierNo=").append(supplierNo);
        sb.append(", supplierSkuNo=").append(supplierSkuNo);
        sb.append(", supplierSpuNo=").append(supplierSpuNo);
        sb.append(", spSkuNo=").append(spSkuNo);
        sb.append(", marketPrice=").append(marketPrice);
        sb.append(", supplyPrice=").append(supplyPrice);
        sb.append(", currency=").append(currency);
        sb.append(", supplierSeason=").append(supplierSeason);
        sb.append(", marketYear=").append(marketYear);
        sb.append(", marketSeason=").append(marketSeason);
        sb.append(", type=").append(type);
        sb.append(", state=").append(state);
        sb.append(", createUserName=").append(createUserName);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", memo=").append(memo);
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
        HubSupplierPriceChangeRecord other = (HubSupplierPriceChangeRecord) that;
        return (this.getSupplierPriceChangeRecordId() == null ? other.getSupplierPriceChangeRecordId() == null : this.getSupplierPriceChangeRecordId().equals(other.getSupplierPriceChangeRecordId()))
            && (this.getSupplierId() == null ? other.getSupplierId() == null : this.getSupplierId().equals(other.getSupplierId()))
            && (this.getSupplierNo() == null ? other.getSupplierNo() == null : this.getSupplierNo().equals(other.getSupplierNo()))
            && (this.getSupplierSkuNo() == null ? other.getSupplierSkuNo() == null : this.getSupplierSkuNo().equals(other.getSupplierSkuNo()))
            && (this.getSupplierSpuNo() == null ? other.getSupplierSpuNo() == null : this.getSupplierSpuNo().equals(other.getSupplierSpuNo()))
            && (this.getSpSkuNo() == null ? other.getSpSkuNo() == null : this.getSpSkuNo().equals(other.getSpSkuNo()))
            && (this.getMarketPrice() == null ? other.getMarketPrice() == null : this.getMarketPrice().equals(other.getMarketPrice()))
            && (this.getSupplyPrice() == null ? other.getSupplyPrice() == null : this.getSupplyPrice().equals(other.getSupplyPrice()))
            && (this.getCurrency() == null ? other.getCurrency() == null : this.getCurrency().equals(other.getCurrency()))
            && (this.getSupplierSeason() == null ? other.getSupplierSeason() == null : this.getSupplierSeason().equals(other.getSupplierSeason()))
            && (this.getMarketYear() == null ? other.getMarketYear() == null : this.getMarketYear().equals(other.getMarketYear()))
            && (this.getMarketSeason() == null ? other.getMarketSeason() == null : this.getMarketSeason().equals(other.getMarketSeason()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getState() == null ? other.getState() == null : this.getState().equals(other.getState()))
            && (this.getCreateUserName() == null ? other.getCreateUserName() == null : this.getCreateUserName().equals(other.getCreateUserName()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getMemo() == null ? other.getMemo() == null : this.getMemo().equals(other.getMemo()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getSupplierPriceChangeRecordId() == null) ? 0 : getSupplierPriceChangeRecordId().hashCode());
        result = prime * result + ((getSupplierId() == null) ? 0 : getSupplierId().hashCode());
        result = prime * result + ((getSupplierNo() == null) ? 0 : getSupplierNo().hashCode());
        result = prime * result + ((getSupplierSkuNo() == null) ? 0 : getSupplierSkuNo().hashCode());
        result = prime * result + ((getSupplierSpuNo() == null) ? 0 : getSupplierSpuNo().hashCode());
        result = prime * result + ((getSpSkuNo() == null) ? 0 : getSpSkuNo().hashCode());
        result = prime * result + ((getMarketPrice() == null) ? 0 : getMarketPrice().hashCode());
        result = prime * result + ((getSupplyPrice() == null) ? 0 : getSupplyPrice().hashCode());
        result = prime * result + ((getCurrency() == null) ? 0 : getCurrency().hashCode());
        result = prime * result + ((getSupplierSeason() == null) ? 0 : getSupplierSeason().hashCode());
        result = prime * result + ((getMarketYear() == null) ? 0 : getMarketYear().hashCode());
        result = prime * result + ((getMarketSeason() == null) ? 0 : getMarketSeason().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getState() == null) ? 0 : getState().hashCode());
        result = prime * result + ((getCreateUserName() == null) ? 0 : getCreateUserName().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getMemo() == null) ? 0 : getMemo().hashCode());
        return result;
    }
}