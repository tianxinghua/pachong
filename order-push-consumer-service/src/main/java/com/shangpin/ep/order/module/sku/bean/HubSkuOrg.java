package com.shangpin.ep.order.module.sku.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * <p>Title:HubSkuOrg.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月18日 下午2:44:17
 */
public class HubSkuOrg implements Serializable {
    private Long skuorgid;

    private String supplierid;

    private Long productorgid;

    private String productnoorg;

    private String supplierskunoorg;

    private String skunameorg;

    private BigDecimal marketprice;

    private String marketpricecurrencyorg;

    private BigDecimal salesprice;

    private String salespricecurrency;

    private BigDecimal supplyprice;

    private String supplypricecurrency;

    private String barcodeorg;

    private String colororg;

    private String skusizeorg;

    private Integer stock;

    private String memo;

    private Date createtime;

    private Date updatetime;

    private Date lasttime;

    private String spskuno;

    private Date eventstarttime;

    private Date eventendtime;

    private String measurement;

    /**
     * 0:待处理  1:已发送   2;已处理  3：待更新  
     */
    private String status;

    /**
     * 是否被选货   0： 未被选    1：被选  
     */
    private Integer selstatus;

    private static final long serialVersionUID = 1L;

    public Long getSkuorgid() {
        return skuorgid;
    }

    public void setSkuorgid(Long skuorgid) {
        this.skuorgid = skuorgid;
    }

    public String getSupplierid() {
        return supplierid;
    }

    public void setSupplierid(String supplierid) {
        this.supplierid = supplierid == null ? null : supplierid.trim();
    }

    public Long getProductorgid() {
        return productorgid;
    }

    public void setProductorgid(Long productorgid) {
        this.productorgid = productorgid;
    }

    public String getProductnoorg() {
        return productnoorg;
    }

    public void setProductnoorg(String productnoorg) {
        this.productnoorg = productnoorg == null ? null : productnoorg.trim();
    }

    public String getSupplierskunoorg() {
        return supplierskunoorg;
    }

    public void setSupplierskunoorg(String supplierskunoorg) {
        this.supplierskunoorg = supplierskunoorg == null ? null : supplierskunoorg.trim();
    }

    public String getSkunameorg() {
        return skunameorg;
    }

    public void setSkunameorg(String skunameorg) {
        this.skunameorg = skunameorg == null ? null : skunameorg.trim();
    }

    public BigDecimal getMarketprice() {
        return marketprice;
    }

    public void setMarketprice(BigDecimal marketprice) {
        this.marketprice = marketprice;
    }

    public String getMarketpricecurrencyorg() {
        return marketpricecurrencyorg;
    }

    public void setMarketpricecurrencyorg(String marketpricecurrencyorg) {
        this.marketpricecurrencyorg = marketpricecurrencyorg == null ? null : marketpricecurrencyorg.trim();
    }

    public BigDecimal getSalesprice() {
        return salesprice;
    }

    public void setSalesprice(BigDecimal salesprice) {
        this.salesprice = salesprice;
    }

    public String getSalespricecurrency() {
        return salespricecurrency;
    }

    public void setSalespricecurrency(String salespricecurrency) {
        this.salespricecurrency = salespricecurrency == null ? null : salespricecurrency.trim();
    }

    public BigDecimal getSupplyprice() {
        return supplyprice;
    }

    public void setSupplyprice(BigDecimal supplyprice) {
        this.supplyprice = supplyprice;
    }

    public String getSupplypricecurrency() {
        return supplypricecurrency;
    }

    public void setSupplypricecurrency(String supplypricecurrency) {
        this.supplypricecurrency = supplypricecurrency == null ? null : supplypricecurrency.trim();
    }

    public String getBarcodeorg() {
        return barcodeorg;
    }

    public void setBarcodeorg(String barcodeorg) {
        this.barcodeorg = barcodeorg == null ? null : barcodeorg.trim();
    }

    public String getColororg() {
        return colororg;
    }

    public void setColororg(String colororg) {
        this.colororg = colororg == null ? null : colororg.trim();
    }

    public String getSkusizeorg() {
        return skusizeorg;
    }

    public void setSkusizeorg(String skusizeorg) {
        this.skusizeorg = skusizeorg == null ? null : skusizeorg.trim();
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public Date getLasttime() {
        return lasttime;
    }

    public void setLasttime(Date lasttime) {
        this.lasttime = lasttime;
    }

    public String getSpskuno() {
        return spskuno;
    }

    public void setSpskuno(String spskuno) {
        this.spskuno = spskuno == null ? null : spskuno.trim();
    }

    public Date getEventstarttime() {
        return eventstarttime;
    }

    public void setEventstarttime(Date eventstarttime) {
        this.eventstarttime = eventstarttime;
    }

    public Date getEventendtime() {
        return eventendtime;
    }

    public void setEventendtime(Date eventendtime) {
        this.eventendtime = eventendtime;
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement == null ? null : measurement.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Integer getSelstatus() {
        return selstatus;
    }

    public void setSelstatus(Integer selstatus) {
        this.selstatus = selstatus;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", skuorgid=").append(skuorgid);
        sb.append(", supplierid=").append(supplierid);
        sb.append(", productorgid=").append(productorgid);
        sb.append(", productnoorg=").append(productnoorg);
        sb.append(", supplierskunoorg=").append(supplierskunoorg);
        sb.append(", skunameorg=").append(skunameorg);
        sb.append(", marketprice=").append(marketprice);
        sb.append(", marketpricecurrencyorg=").append(marketpricecurrencyorg);
        sb.append(", salesprice=").append(salesprice);
        sb.append(", salespricecurrency=").append(salespricecurrency);
        sb.append(", supplyprice=").append(supplyprice);
        sb.append(", supplypricecurrency=").append(supplypricecurrency);
        sb.append(", barcodeorg=").append(barcodeorg);
        sb.append(", colororg=").append(colororg);
        sb.append(", skusizeorg=").append(skusizeorg);
        sb.append(", stock=").append(stock);
        sb.append(", memo=").append(memo);
        sb.append(", createtime=").append(createtime);
        sb.append(", updatetime=").append(updatetime);
        sb.append(", lasttime=").append(lasttime);
        sb.append(", spskuno=").append(spskuno);
        sb.append(", eventstarttime=").append(eventstarttime);
        sb.append(", eventendtime=").append(eventendtime);
        sb.append(", measurement=").append(measurement);
        sb.append(", status=").append(status);
        sb.append(", selstatus=").append(selstatus);
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
        HubSkuOrg other = (HubSkuOrg) that;
        return (this.getSkuorgid() == null ? other.getSkuorgid() == null : this.getSkuorgid().equals(other.getSkuorgid()))
            && (this.getSupplierid() == null ? other.getSupplierid() == null : this.getSupplierid().equals(other.getSupplierid()))
            && (this.getProductorgid() == null ? other.getProductorgid() == null : this.getProductorgid().equals(other.getProductorgid()))
            && (this.getProductnoorg() == null ? other.getProductnoorg() == null : this.getProductnoorg().equals(other.getProductnoorg()))
            && (this.getSupplierskunoorg() == null ? other.getSupplierskunoorg() == null : this.getSupplierskunoorg().equals(other.getSupplierskunoorg()))
            && (this.getSkunameorg() == null ? other.getSkunameorg() == null : this.getSkunameorg().equals(other.getSkunameorg()))
            && (this.getMarketprice() == null ? other.getMarketprice() == null : this.getMarketprice().equals(other.getMarketprice()))
            && (this.getMarketpricecurrencyorg() == null ? other.getMarketpricecurrencyorg() == null : this.getMarketpricecurrencyorg().equals(other.getMarketpricecurrencyorg()))
            && (this.getSalesprice() == null ? other.getSalesprice() == null : this.getSalesprice().equals(other.getSalesprice()))
            && (this.getSalespricecurrency() == null ? other.getSalespricecurrency() == null : this.getSalespricecurrency().equals(other.getSalespricecurrency()))
            && (this.getSupplyprice() == null ? other.getSupplyprice() == null : this.getSupplyprice().equals(other.getSupplyprice()))
            && (this.getSupplypricecurrency() == null ? other.getSupplypricecurrency() == null : this.getSupplypricecurrency().equals(other.getSupplypricecurrency()))
            && (this.getBarcodeorg() == null ? other.getBarcodeorg() == null : this.getBarcodeorg().equals(other.getBarcodeorg()))
            && (this.getColororg() == null ? other.getColororg() == null : this.getColororg().equals(other.getColororg()))
            && (this.getSkusizeorg() == null ? other.getSkusizeorg() == null : this.getSkusizeorg().equals(other.getSkusizeorg()))
            && (this.getStock() == null ? other.getStock() == null : this.getStock().equals(other.getStock()))
            && (this.getMemo() == null ? other.getMemo() == null : this.getMemo().equals(other.getMemo()))
            && (this.getCreatetime() == null ? other.getCreatetime() == null : this.getCreatetime().equals(other.getCreatetime()))
            && (this.getUpdatetime() == null ? other.getUpdatetime() == null : this.getUpdatetime().equals(other.getUpdatetime()))
            && (this.getLasttime() == null ? other.getLasttime() == null : this.getLasttime().equals(other.getLasttime()))
            && (this.getSpskuno() == null ? other.getSpskuno() == null : this.getSpskuno().equals(other.getSpskuno()))
            && (this.getEventstarttime() == null ? other.getEventstarttime() == null : this.getEventstarttime().equals(other.getEventstarttime()))
            && (this.getEventendtime() == null ? other.getEventendtime() == null : this.getEventendtime().equals(other.getEventendtime()))
            && (this.getMeasurement() == null ? other.getMeasurement() == null : this.getMeasurement().equals(other.getMeasurement()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getSelstatus() == null ? other.getSelstatus() == null : this.getSelstatus().equals(other.getSelstatus()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getSkuorgid() == null) ? 0 : getSkuorgid().hashCode());
        result = prime * result + ((getSupplierid() == null) ? 0 : getSupplierid().hashCode());
        result = prime * result + ((getProductorgid() == null) ? 0 : getProductorgid().hashCode());
        result = prime * result + ((getProductnoorg() == null) ? 0 : getProductnoorg().hashCode());
        result = prime * result + ((getSupplierskunoorg() == null) ? 0 : getSupplierskunoorg().hashCode());
        result = prime * result + ((getSkunameorg() == null) ? 0 : getSkunameorg().hashCode());
        result = prime * result + ((getMarketprice() == null) ? 0 : getMarketprice().hashCode());
        result = prime * result + ((getMarketpricecurrencyorg() == null) ? 0 : getMarketpricecurrencyorg().hashCode());
        result = prime * result + ((getSalesprice() == null) ? 0 : getSalesprice().hashCode());
        result = prime * result + ((getSalespricecurrency() == null) ? 0 : getSalespricecurrency().hashCode());
        result = prime * result + ((getSupplyprice() == null) ? 0 : getSupplyprice().hashCode());
        result = prime * result + ((getSupplypricecurrency() == null) ? 0 : getSupplypricecurrency().hashCode());
        result = prime * result + ((getBarcodeorg() == null) ? 0 : getBarcodeorg().hashCode());
        result = prime * result + ((getColororg() == null) ? 0 : getColororg().hashCode());
        result = prime * result + ((getSkusizeorg() == null) ? 0 : getSkusizeorg().hashCode());
        result = prime * result + ((getStock() == null) ? 0 : getStock().hashCode());
        result = prime * result + ((getMemo() == null) ? 0 : getMemo().hashCode());
        result = prime * result + ((getCreatetime() == null) ? 0 : getCreatetime().hashCode());
        result = prime * result + ((getUpdatetime() == null) ? 0 : getUpdatetime().hashCode());
        result = prime * result + ((getLasttime() == null) ? 0 : getLasttime().hashCode());
        result = prime * result + ((getSpskuno() == null) ? 0 : getSpskuno().hashCode());
        result = prime * result + ((getEventstarttime() == null) ? 0 : getEventstarttime().hashCode());
        result = prime * result + ((getEventendtime() == null) ? 0 : getEventendtime().hashCode());
        result = prime * result + ((getMeasurement() == null) ? 0 : getMeasurement().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getSelstatus() == null) ? 0 : getSelstatus().hashCode());
        return result;
    }
}