package com.shangpin.pending.product.consumer.conf.clients.mysql.sku.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class HubSkuPending implements Serializable {
    /**
     * 主键
     */
    private Long skuPendingId;

    /**
     * spu_pending_id
     */
    private Long spuPendingId;

    /**
     * 供货商id
     */
    private String supplierId;

    /**
     * 供应商SkuNo
     */
    private String supplierSkuNo;

    /**
     * 供应商Sku名称
     */
    private String skuName;

    /**
     * 市场价
     */
    private BigDecimal marketPrice;

    /**
     * 市场价币种
     */
    private String marketPriceCurrencyorg;

    /**
     * 售价
     */
    private BigDecimal salesPrice;

    /**
     * 售价币种
     */
    private String salesPriceCurrency;

    /**
     * 供价
     */
    private BigDecimal supplyPrice;

    /**
     * 供价币种
     */
    private String supplyPriceCurrency;

    /**
     * 供应商条码
     */
    private String supplierBarcode;

    /**
     * 尺码
     */
    private String hubSkuSize;

    /**
     * 库存
     */
    private Integer stock;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 最后拉取时间
     */
    private Date lastPullTime;

    /**
     * 尚品Sku编号
     */
    private String spSkuNo;

    /**
     * 尺寸
     */
    private String measurement;

    /**
     * 0：待处理   1：成功  2：已发送mq   3：已发送openapi   4:推送mq失败 5:推送openapi失败      6：openapi更新失败 
     */
    private Byte priceState;

    /**
     * sku信息状态
     */
    private Byte skuState;

    /**
     * hubskuno
     */
    private String hubSkuNo;

    /**
     * 备注
     */
    private String memo;

    /**
     * 数据状态：1未删除 0已删除
     */
    private Byte dataState;

    /**
     * 版本字段
     */
    private Long version;

    /**
     * 信息来源
     */
    private Byte infoFrom;

    /**
     * 尺码处理状态
     */
    private Byte spSkuSizeState;

    private static final long serialVersionUID = 1L;

    public Long getSkuPendingId() {
        return skuPendingId;
    }

    public void setSkuPendingId(Long skuPendingId) {
        this.skuPendingId = skuPendingId;
    }

    public Long getSpuPendingId() {
        return spuPendingId;
    }

    public void setSpuPendingId(Long spuPendingId) {
        this.spuPendingId = spuPendingId;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId == null ? null : supplierId.trim();
    }

    public String getSupplierSkuNo() {
        return supplierSkuNo;
    }

    public void setSupplierSkuNo(String supplierSkuNo) {
        this.supplierSkuNo = supplierSkuNo == null ? null : supplierSkuNo.trim();
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName == null ? null : skuName.trim();
    }

    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    public String getMarketPriceCurrencyorg() {
        return marketPriceCurrencyorg;
    }

    public void setMarketPriceCurrencyorg(String marketPriceCurrencyorg) {
        this.marketPriceCurrencyorg = marketPriceCurrencyorg == null ? null : marketPriceCurrencyorg.trim();
    }

    public BigDecimal getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(BigDecimal salesPrice) {
        this.salesPrice = salesPrice;
    }

    public String getSalesPriceCurrency() {
        return salesPriceCurrency;
    }

    public void setSalesPriceCurrency(String salesPriceCurrency) {
        this.salesPriceCurrency = salesPriceCurrency == null ? null : salesPriceCurrency.trim();
    }

    public BigDecimal getSupplyPrice() {
        return supplyPrice;
    }

    public void setSupplyPrice(BigDecimal supplyPrice) {
        this.supplyPrice = supplyPrice;
    }

    public String getSupplyPriceCurrency() {
        return supplyPriceCurrency;
    }

    public void setSupplyPriceCurrency(String supplyPriceCurrency) {
        this.supplyPriceCurrency = supplyPriceCurrency == null ? null : supplyPriceCurrency.trim();
    }

    public String getSupplierBarcode() {
        return supplierBarcode;
    }

    public void setSupplierBarcode(String supplierBarcode) {
        this.supplierBarcode = supplierBarcode == null ? null : supplierBarcode.trim();
    }

    public String getHubSkuSize() {
        return hubSkuSize;
    }

    public void setHubSkuSize(String hubSkuSize) {
        this.hubSkuSize = hubSkuSize == null ? null : hubSkuSize.trim();
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
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

    public Date getLastPullTime() {
        return lastPullTime;
    }

    public void setLastPullTime(Date lastPullTime) {
        this.lastPullTime = lastPullTime;
    }

    public String getSpSkuNo() {
        return spSkuNo;
    }

    public void setSpSkuNo(String spSkuNo) {
        this.spSkuNo = spSkuNo == null ? null : spSkuNo.trim();
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement == null ? null : measurement.trim();
    }

    public Byte getPriceState() {
        return priceState;
    }

    public void setPriceState(Byte priceState) {
        this.priceState = priceState;
    }

    public Byte getSkuState() {
        return skuState;
    }

    public void setSkuState(Byte skuState) {
        this.skuState = skuState;
    }

    public String getHubSkuNo() {
        return hubSkuNo;
    }

    public void setHubSkuNo(String hubSkuNo) {
        this.hubSkuNo = hubSkuNo == null ? null : hubSkuNo.trim();
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    public Byte getDataState() {
        return dataState;
    }

    public void setDataState(Byte dataState) {
        this.dataState = dataState;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Byte getInfoFrom() {
        return infoFrom;
    }

    public void setInfoFrom(Byte infoFrom) {
        this.infoFrom = infoFrom;
    }

    public Byte getSpSkuSizeState() {
        return spSkuSizeState;
    }

    public void setSpSkuSizeState(Byte spSkuSizeState) {
        this.spSkuSizeState = spSkuSizeState;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", skuPendingId=").append(skuPendingId);
        sb.append(", spuPendingId=").append(spuPendingId);
        sb.append(", supplierId=").append(supplierId);
        sb.append(", supplierSkuNo=").append(supplierSkuNo);
        sb.append(", skuName=").append(skuName);
        sb.append(", marketPrice=").append(marketPrice);
        sb.append(", marketPriceCurrencyorg=").append(marketPriceCurrencyorg);
        sb.append(", salesPrice=").append(salesPrice);
        sb.append(", salesPriceCurrency=").append(salesPriceCurrency);
        sb.append(", supplyPrice=").append(supplyPrice);
        sb.append(", supplyPriceCurrency=").append(supplyPriceCurrency);
        sb.append(", supplierBarcode=").append(supplierBarcode);
        sb.append(", hubSkuSize=").append(hubSkuSize);
        sb.append(", stock=").append(stock);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", lastPullTime=").append(lastPullTime);
        sb.append(", spSkuNo=").append(spSkuNo);
        sb.append(", measurement=").append(measurement);
        sb.append(", priceState=").append(priceState);
        sb.append(", skuState=").append(skuState);
        sb.append(", hubSkuNo=").append(hubSkuNo);
        sb.append(", memo=").append(memo);
        sb.append(", dataState=").append(dataState);
        sb.append(", version=").append(version);
        sb.append(", infoFrom=").append(infoFrom);
        sb.append(", spSkuSizeState=").append(spSkuSizeState);
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
        HubSkuPending other = (HubSkuPending) that;
        return (this.getSkuPendingId() == null ? other.getSkuPendingId() == null : this.getSkuPendingId().equals(other.getSkuPendingId()))
            && (this.getSpuPendingId() == null ? other.getSpuPendingId() == null : this.getSpuPendingId().equals(other.getSpuPendingId()))
            && (this.getSupplierId() == null ? other.getSupplierId() == null : this.getSupplierId().equals(other.getSupplierId()))
            && (this.getSupplierSkuNo() == null ? other.getSupplierSkuNo() == null : this.getSupplierSkuNo().equals(other.getSupplierSkuNo()))
            && (this.getSkuName() == null ? other.getSkuName() == null : this.getSkuName().equals(other.getSkuName()))
            && (this.getMarketPrice() == null ? other.getMarketPrice() == null : this.getMarketPrice().equals(other.getMarketPrice()))
            && (this.getMarketPriceCurrencyorg() == null ? other.getMarketPriceCurrencyorg() == null : this.getMarketPriceCurrencyorg().equals(other.getMarketPriceCurrencyorg()))
            && (this.getSalesPrice() == null ? other.getSalesPrice() == null : this.getSalesPrice().equals(other.getSalesPrice()))
            && (this.getSalesPriceCurrency() == null ? other.getSalesPriceCurrency() == null : this.getSalesPriceCurrency().equals(other.getSalesPriceCurrency()))
            && (this.getSupplyPrice() == null ? other.getSupplyPrice() == null : this.getSupplyPrice().equals(other.getSupplyPrice()))
            && (this.getSupplyPriceCurrency() == null ? other.getSupplyPriceCurrency() == null : this.getSupplyPriceCurrency().equals(other.getSupplyPriceCurrency()))
            && (this.getSupplierBarcode() == null ? other.getSupplierBarcode() == null : this.getSupplierBarcode().equals(other.getSupplierBarcode()))
            && (this.getHubSkuSize() == null ? other.getHubSkuSize() == null : this.getHubSkuSize().equals(other.getHubSkuSize()))
            && (this.getStock() == null ? other.getStock() == null : this.getStock().equals(other.getStock()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getLastPullTime() == null ? other.getLastPullTime() == null : this.getLastPullTime().equals(other.getLastPullTime()))
            && (this.getSpSkuNo() == null ? other.getSpSkuNo() == null : this.getSpSkuNo().equals(other.getSpSkuNo()))
            && (this.getMeasurement() == null ? other.getMeasurement() == null : this.getMeasurement().equals(other.getMeasurement()))
            && (this.getPriceState() == null ? other.getPriceState() == null : this.getPriceState().equals(other.getPriceState()))
            && (this.getSkuState() == null ? other.getSkuState() == null : this.getSkuState().equals(other.getSkuState()))
            && (this.getHubSkuNo() == null ? other.getHubSkuNo() == null : this.getHubSkuNo().equals(other.getHubSkuNo()))
            && (this.getMemo() == null ? other.getMemo() == null : this.getMemo().equals(other.getMemo()))
            && (this.getDataState() == null ? other.getDataState() == null : this.getDataState().equals(other.getDataState()))
            && (this.getVersion() == null ? other.getVersion() == null : this.getVersion().equals(other.getVersion()))
            && (this.getInfoFrom() == null ? other.getInfoFrom() == null : this.getInfoFrom().equals(other.getInfoFrom()))
            && (this.getSpSkuSizeState() == null ? other.getSpSkuSizeState() == null : this.getSpSkuSizeState().equals(other.getSpSkuSizeState()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getSkuPendingId() == null) ? 0 : getSkuPendingId().hashCode());
        result = prime * result + ((getSpuPendingId() == null) ? 0 : getSpuPendingId().hashCode());
        result = prime * result + ((getSupplierId() == null) ? 0 : getSupplierId().hashCode());
        result = prime * result + ((getSupplierSkuNo() == null) ? 0 : getSupplierSkuNo().hashCode());
        result = prime * result + ((getSkuName() == null) ? 0 : getSkuName().hashCode());
        result = prime * result + ((getMarketPrice() == null) ? 0 : getMarketPrice().hashCode());
        result = prime * result + ((getMarketPriceCurrencyorg() == null) ? 0 : getMarketPriceCurrencyorg().hashCode());
        result = prime * result + ((getSalesPrice() == null) ? 0 : getSalesPrice().hashCode());
        result = prime * result + ((getSalesPriceCurrency() == null) ? 0 : getSalesPriceCurrency().hashCode());
        result = prime * result + ((getSupplyPrice() == null) ? 0 : getSupplyPrice().hashCode());
        result = prime * result + ((getSupplyPriceCurrency() == null) ? 0 : getSupplyPriceCurrency().hashCode());
        result = prime * result + ((getSupplierBarcode() == null) ? 0 : getSupplierBarcode().hashCode());
        result = prime * result + ((getHubSkuSize() == null) ? 0 : getHubSkuSize().hashCode());
        result = prime * result + ((getStock() == null) ? 0 : getStock().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getLastPullTime() == null) ? 0 : getLastPullTime().hashCode());
        result = prime * result + ((getSpSkuNo() == null) ? 0 : getSpSkuNo().hashCode());
        result = prime * result + ((getMeasurement() == null) ? 0 : getMeasurement().hashCode());
        result = prime * result + ((getPriceState() == null) ? 0 : getPriceState().hashCode());
        result = prime * result + ((getSkuState() == null) ? 0 : getSkuState().hashCode());
        result = prime * result + ((getHubSkuNo() == null) ? 0 : getHubSkuNo().hashCode());
        result = prime * result + ((getMemo() == null) ? 0 : getMemo().hashCode());
        result = prime * result + ((getDataState() == null) ? 0 : getDataState().hashCode());
        result = prime * result + ((getVersion() == null) ? 0 : getVersion().hashCode());
        result = prime * result + ((getInfoFrom() == null) ? 0 : getInfoFrom().hashCode());
        result = prime * result + ((getSpSkuSizeState() == null) ? 0 : getSpSkuSizeState().hashCode());
        return result;
    }
}