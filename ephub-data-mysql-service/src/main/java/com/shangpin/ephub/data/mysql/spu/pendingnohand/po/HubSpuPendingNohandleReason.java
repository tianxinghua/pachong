package com.shangpin.ephub.data.mysql.spu.pendingnohand.po;

import java.io.Serializable;
import java.util.Date;

public class HubSpuPendingNohandleReason implements Serializable {
    /**
     * 主键
     */
    private Long spuPendingNohandleReasonId;

    /**
     * spupending主键
     */
    private Long spuPendingId;

    /**
     * supplierspu主键
     */
    private Long supplierSpuId;

    /**
     * supplierno
     */
    private String supplierNo;

    /**
     * supplierid
     */
    private String supplierId;

    /**
     * 错误类型 1:Item Code Error 2: Material Info Error 3:Gender Info Error
             4:Origin Info Error 5:Photo Error 6:Supplier SKU Error 7:Price Error 
            8:Size Issue 9:Brand Selection
     */
    private Byte errorType;

    /**
     * 错误原因:11:No Color Code 12:No Material Code 13:Wrong Code Rule
            21:Wrong Material Composition 22:Wrong Material Percentage 23:No Material Info
            31: Child-Adult Inversion 32:Man-Woman Inversion
            41:No Origin Info 51:Wrong Mapping of Code 52:No Photo
            61:Different SPU to Same Item Code 71:No Market Price 72:No Supplier Price
            81:Too Large / Small 82:Wrong Size 91:Unprofitable Brand
     */
    private Byte errorReason;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 数据状态
     */
    private Byte dataState;

    private static final long serialVersionUID = 1L;

    public Long getSpuPendingNohandleReasonId() {
        return spuPendingNohandleReasonId;
    }

    public void setSpuPendingNohandleReasonId(Long spuPendingNohandleReasonId) {
        this.spuPendingNohandleReasonId = spuPendingNohandleReasonId;
    }

    public Long getSpuPendingId() {
        return spuPendingId;
    }

    public void setSpuPendingId(Long spuPendingId) {
        this.spuPendingId = spuPendingId;
    }

    public Long getSupplierSpuId() {
        return supplierSpuId;
    }

    public void setSupplierSpuId(Long supplierSpuId) {
        this.supplierSpuId = supplierSpuId;
    }

    public String getSupplierNo() {
        return supplierNo;
    }

    public void setSupplierNo(String supplierNo) {
        this.supplierNo = supplierNo == null ? null : supplierNo.trim();
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId == null ? null : supplierId.trim();
    }

    public Byte getErrorType() {
        return errorType;
    }

    public void setErrorType(Byte errorType) {
        this.errorType = errorType;
    }

    public Byte getErrorReason() {
        return errorReason;
    }

    public void setErrorReason(Byte errorReason) {
        this.errorReason = errorReason;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Byte getDataState() {
        return dataState;
    }

    public void setDataState(Byte dataState) {
        this.dataState = dataState;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", spuPendingNohandleReasonId=").append(spuPendingNohandleReasonId);
        sb.append(", spuPendingId=").append(spuPendingId);
        sb.append(", supplierSpuId=").append(supplierSpuId);
        sb.append(", supplierNo=").append(supplierNo);
        sb.append(", supplierId=").append(supplierId);
        sb.append(", errorType=").append(errorType);
        sb.append(", errorReason=").append(errorReason);
        sb.append(", createUser=").append(createUser);
        sb.append(", createTime=").append(createTime);
        sb.append(", dataState=").append(dataState);
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
        HubSpuPendingNohandleReason other = (HubSpuPendingNohandleReason) that;
        return (this.getSpuPendingNohandleReasonId() == null ? other.getSpuPendingNohandleReasonId() == null : this.getSpuPendingNohandleReasonId().equals(other.getSpuPendingNohandleReasonId()))
            && (this.getSpuPendingId() == null ? other.getSpuPendingId() == null : this.getSpuPendingId().equals(other.getSpuPendingId()))
            && (this.getSupplierSpuId() == null ? other.getSupplierSpuId() == null : this.getSupplierSpuId().equals(other.getSupplierSpuId()))
            && (this.getSupplierNo() == null ? other.getSupplierNo() == null : this.getSupplierNo().equals(other.getSupplierNo()))
            && (this.getSupplierId() == null ? other.getSupplierId() == null : this.getSupplierId().equals(other.getSupplierId()))
            && (this.getErrorType() == null ? other.getErrorType() == null : this.getErrorType().equals(other.getErrorType()))
            && (this.getErrorReason() == null ? other.getErrorReason() == null : this.getErrorReason().equals(other.getErrorReason()))
            && (this.getCreateUser() == null ? other.getCreateUser() == null : this.getCreateUser().equals(other.getCreateUser()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getDataState() == null ? other.getDataState() == null : this.getDataState().equals(other.getDataState()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getSpuPendingNohandleReasonId() == null) ? 0 : getSpuPendingNohandleReasonId().hashCode());
        result = prime * result + ((getSpuPendingId() == null) ? 0 : getSpuPendingId().hashCode());
        result = prime * result + ((getSupplierSpuId() == null) ? 0 : getSupplierSpuId().hashCode());
        result = prime * result + ((getSupplierNo() == null) ? 0 : getSupplierNo().hashCode());
        result = prime * result + ((getSupplierId() == null) ? 0 : getSupplierId().hashCode());
        result = prime * result + ((getErrorType() == null) ? 0 : getErrorType().hashCode());
        result = prime * result + ((getErrorReason() == null) ? 0 : getErrorReason().hashCode());
        result = prime * result + ((getCreateUser() == null) ? 0 : getCreateUser().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getDataState() == null) ? 0 : getDataState().hashCode());
        return result;
    }
}