package com.shangpin.ephub.data.mysql.sku.supplier.po;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class HubSupplierSku implements Serializable {
    /**
     * 主键
     */
    private Long supplierSkuId;

    /**
     * supplierspuorgid
     */
    private Long supplierSpuId;

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
    private String supplierSkuName;

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
     * 供应商尺码
     */
    private String supplierSkuSize;

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
     * =0 带推送 =1 推送成功 =2 推送失败
     */
    private Byte pushState;

    /**
     * 推送返回信息
     */
    private String pushResult;

    /**
     * =0 正常需处理数据 =1 业务规则过滤 
     */
    private Byte filterFlag;

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

    private static final long serialVersionUID = 1L;

    public Long getSupplierSkuId() {
        return supplierSkuId;
    }

    public void setSupplierSkuId(Long supplierSkuId) {
        this.supplierSkuId = supplierSkuId;
    }

    public Long getSupplierSpuId() {
        return supplierSpuId;
    }

    public void setSupplierSpuId(Long supplierSpuId) {
        this.supplierSpuId = supplierSpuId;
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

    public String getSupplierSkuName() {
        return supplierSkuName;
    }

    public void setSupplierSkuName(String supplierSkuName) {
        this.supplierSkuName = supplierSkuName == null ? null : supplierSkuName.trim();
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

    public String getSupplierSkuSize() {
        return supplierSkuSize;
    }

    public void setSupplierSkuSize(String supplierSkuSize) {
        this.supplierSkuSize = supplierSkuSize == null ? null : supplierSkuSize.trim();
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

    public Byte getPushState() {
        return pushState;
    }

    public void setPushState(Byte pushState) {
        this.pushState = pushState;
    }

    public String getPushResult() {
        return pushResult;
    }

    public void setPushResult(String pushResult) {
        this.pushResult = pushResult == null ? null : pushResult.trim();
    }

    public Byte getFilterFlag() {
        return filterFlag;
    }

    public void setFilterFlag(Byte filterFlag) {
        this.filterFlag = filterFlag;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", supplierSkuId=").append(supplierSkuId);
        sb.append(", supplierSpuId=").append(supplierSpuId);
        sb.append(", supplierId=").append(supplierId);
        sb.append(", supplierSkuNo=").append(supplierSkuNo);
        sb.append(", supplierSkuName=").append(supplierSkuName);
        sb.append(", marketPrice=").append(marketPrice);
        sb.append(", marketPriceCurrencyorg=").append(marketPriceCurrencyorg);
        sb.append(", salesPrice=").append(salesPrice);
        sb.append(", salesPriceCurrency=").append(salesPriceCurrency);
        sb.append(", supplyPrice=").append(supplyPrice);
        sb.append(", supplyPriceCurrency=").append(supplyPriceCurrency);
        sb.append(", supplierBarcode=").append(supplierBarcode);
        sb.append(", supplierSkuSize=").append(supplierSkuSize);
        sb.append(", stock=").append(stock);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", lastPullTime=").append(lastPullTime);
        sb.append(", pushState=").append(pushState);
        sb.append(", pushResult=").append(pushResult);
        sb.append(", filterFlag=").append(filterFlag);
        sb.append(", memo=").append(memo);
        sb.append(", dataState=").append(dataState);
        sb.append(", version=").append(version);
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
        HubSupplierSku other = (HubSupplierSku) that;
        return (this.getSupplierSkuId() == null ? other.getSupplierSkuId() == null : this.getSupplierSkuId().equals(other.getSupplierSkuId()))
            && (this.getSupplierSpuId() == null ? other.getSupplierSpuId() == null : this.getSupplierSpuId().equals(other.getSupplierSpuId()))
            && (this.getSupplierId() == null ? other.getSupplierId() == null : this.getSupplierId().equals(other.getSupplierId()))
            && (this.getSupplierSkuNo() == null ? other.getSupplierSkuNo() == null : this.getSupplierSkuNo().equals(other.getSupplierSkuNo()))
            && (this.getSupplierSkuName() == null ? other.getSupplierSkuName() == null : this.getSupplierSkuName().equals(other.getSupplierSkuName()))
            && (this.getMarketPrice() == null ? other.getMarketPrice() == null : this.getMarketPrice().equals(other.getMarketPrice()))
            && (this.getMarketPriceCurrencyorg() == null ? other.getMarketPriceCurrencyorg() == null : this.getMarketPriceCurrencyorg().equals(other.getMarketPriceCurrencyorg()))
            && (this.getSalesPrice() == null ? other.getSalesPrice() == null : this.getSalesPrice().equals(other.getSalesPrice()))
            && (this.getSalesPriceCurrency() == null ? other.getSalesPriceCurrency() == null : this.getSalesPriceCurrency().equals(other.getSalesPriceCurrency()))
            && (this.getSupplyPrice() == null ? other.getSupplyPrice() == null : this.getSupplyPrice().equals(other.getSupplyPrice()))
            && (this.getSupplyPriceCurrency() == null ? other.getSupplyPriceCurrency() == null : this.getSupplyPriceCurrency().equals(other.getSupplyPriceCurrency()))
            && (this.getSupplierBarcode() == null ? other.getSupplierBarcode() == null : this.getSupplierBarcode().equals(other.getSupplierBarcode()))
            && (this.getSupplierSkuSize() == null ? other.getSupplierSkuSize() == null : this.getSupplierSkuSize().equals(other.getSupplierSkuSize()))
            && (this.getStock() == null ? other.getStock() == null : this.getStock().equals(other.getStock()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getLastPullTime() == null ? other.getLastPullTime() == null : this.getLastPullTime().equals(other.getLastPullTime()))
            && (this.getPushState() == null ? other.getPushState() == null : this.getPushState().equals(other.getPushState()))
            && (this.getPushResult() == null ? other.getPushResult() == null : this.getPushResult().equals(other.getPushResult()))
            && (this.getFilterFlag() == null ? other.getFilterFlag() == null : this.getFilterFlag().equals(other.getFilterFlag()))
            && (this.getMemo() == null ? other.getMemo() == null : this.getMemo().equals(other.getMemo()))
            && (this.getDataState() == null ? other.getDataState() == null : this.getDataState().equals(other.getDataState()))
            && (this.getVersion() == null ? other.getVersion() == null : this.getVersion().equals(other.getVersion()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getSupplierSkuId() == null) ? 0 : getSupplierSkuId().hashCode());
        result = prime * result + ((getSupplierSpuId() == null) ? 0 : getSupplierSpuId().hashCode());
        result = prime * result + ((getSupplierId() == null) ? 0 : getSupplierId().hashCode());
        result = prime * result + ((getSupplierSkuNo() == null) ? 0 : getSupplierSkuNo().hashCode());
        result = prime * result + ((getSupplierSkuName() == null) ? 0 : getSupplierSkuName().hashCode());
        result = prime * result + ((getMarketPrice() == null) ? 0 : getMarketPrice().hashCode());
        result = prime * result + ((getMarketPriceCurrencyorg() == null) ? 0 : getMarketPriceCurrencyorg().hashCode());
        result = prime * result + ((getSalesPrice() == null) ? 0 : getSalesPrice().hashCode());
        result = prime * result + ((getSalesPriceCurrency() == null) ? 0 : getSalesPriceCurrency().hashCode());
        result = prime * result + ((getSupplyPrice() == null) ? 0 : getSupplyPrice().hashCode());
        result = prime * result + ((getSupplyPriceCurrency() == null) ? 0 : getSupplyPriceCurrency().hashCode());
        result = prime * result + ((getSupplierBarcode() == null) ? 0 : getSupplierBarcode().hashCode());
        result = prime * result + ((getSupplierSkuSize() == null) ? 0 : getSupplierSkuSize().hashCode());
        result = prime * result + ((getStock() == null) ? 0 : getStock().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getLastPullTime() == null) ? 0 : getLastPullTime().hashCode());
        result = prime * result + ((getPushState() == null) ? 0 : getPushState().hashCode());
        result = prime * result + ((getPushResult() == null) ? 0 : getPushResult().hashCode());
        result = prime * result + ((getFilterFlag() == null) ? 0 : getFilterFlag().hashCode());
        result = prime * result + ((getMemo() == null) ? 0 : getMemo().hashCode());
        result = prime * result + ((getDataState() == null) ? 0 : getDataState().hashCode());
        result = prime * result + ((getVersion() == null) ? 0 : getVersion().hashCode());
        return result;
    }
}