package com.shangpin.ep.order.module.mapping.bean;

import java.io.Serializable;
import java.util.Date;
/**
 * <p>Title:HubSkuRelation.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月17日 下午5:52:00
 */
public class HubSkuRelation implements Serializable {
    private String supplierId;

    private String sopNo;

    private String sopSkuId;

    private String supplierSkuId;

    private Date createTime;

    private static final long serialVersionUID = 1L;

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId == null ? null : supplierId.trim();
    }

    public String getSopNo() {
        return sopNo;
    }

    public void setSopNo(String sopNo) {
        this.sopNo = sopNo == null ? null : sopNo.trim();
    }

    public String getSopSkuId() {
        return sopSkuId;
    }

    public void setSopSkuId(String sopSkuId) {
        this.sopSkuId = sopSkuId == null ? null : sopSkuId.trim();
    }

    public String getSupplierSkuId() {
        return supplierSkuId;
    }

    public void setSupplierSkuId(String supplierSkuId) {
        this.supplierSkuId = supplierSkuId == null ? null : supplierSkuId.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", supplierId=").append(supplierId);
        sb.append(", sopNo=").append(sopNo);
        sb.append(", sopSkuId=").append(sopSkuId);
        sb.append(", supplierSkuId=").append(supplierSkuId);
        sb.append(", createTime=").append(createTime);
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
        HubSkuRelation other = (HubSkuRelation) that;
        return (this.getSupplierId() == null ? other.getSupplierId() == null : this.getSupplierId().equals(other.getSupplierId()))
            && (this.getSopNo() == null ? other.getSopNo() == null : this.getSopNo().equals(other.getSopNo()))
            && (this.getSopSkuId() == null ? other.getSopSkuId() == null : this.getSopSkuId().equals(other.getSopSkuId()))
            && (this.getSupplierSkuId() == null ? other.getSupplierSkuId() == null : this.getSupplierSkuId().equals(other.getSupplierSkuId()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getSupplierId() == null) ? 0 : getSupplierId().hashCode());
        result = prime * result + ((getSopNo() == null) ? 0 : getSopNo().hashCode());
        result = prime * result + ((getSopSkuId() == null) ? 0 : getSopSkuId().hashCode());
        result = prime * result + ((getSupplierSkuId() == null) ? 0 : getSupplierSkuId().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        return result;
    }
}