package com.shangpin.ephub.data.mysql.mapping.value.po;

import java.io.Serializable;
import java.util.Date;

public class HubSupplierValueMapping implements Serializable {
    /**
     * 主键
     */
    private Long hubSupplierValMappingId;

    /**
     * 列映射Id
     */
    private Long columnMappingId;

    /**
     * 供应商Id
     */
    private String supplierId;

    /**
     * 供应商值编码
     */
    private String supplierValNo;

    /**
     * 供应商值父级编码
     */
    private String supplierValParentNo;

    /**
     * 供应商值
     */
    private String supplierVal;

    /**
     * 尚品值编码
     */
    private String hubValNo;

    /**
     * 尚品值
     */
    private String hubVal;

    /**
     * 1、品牌 2、品类 3、产地 等
     */
    private Byte hubValType;

    /**
     * 1、单字段完整映射   
            2、组合映射
            3、部分映射
     */
    private Byte mappingType;

    /**
     * 映射状态
     */
    private Byte mappingState;

    /**
     * 排序
     */
    private Short sortVal;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 更新人
     */
    private String updateUser;

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

    public Long getHubSupplierValMappingId() {
        return hubSupplierValMappingId;
    }

    public void setHubSupplierValMappingId(Long hubSupplierValMappingId) {
        this.hubSupplierValMappingId = hubSupplierValMappingId;
    }

    public Long getColumnMappingId() {
        return columnMappingId;
    }

    public void setColumnMappingId(Long columnMappingId) {
        this.columnMappingId = columnMappingId;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId == null ? null : supplierId.trim();
    }

    public String getSupplierValNo() {
        return supplierValNo;
    }

    public void setSupplierValNo(String supplierValNo) {
        this.supplierValNo = supplierValNo == null ? null : supplierValNo.trim();
    }

    public String getSupplierValParentNo() {
        return supplierValParentNo;
    }

    public void setSupplierValParentNo(String supplierValParentNo) {
        this.supplierValParentNo = supplierValParentNo == null ? null : supplierValParentNo.trim();
    }

    public String getSupplierVal() {
        return supplierVal;
    }

    public void setSupplierVal(String supplierVal) {
        this.supplierVal = supplierVal == null ? null : supplierVal.trim();
    }

    public String getHubValNo() {
        return hubValNo;
    }

    public void setHubValNo(String hubValNo) {
        this.hubValNo = hubValNo == null ? null : hubValNo.trim();
    }

    public String getHubVal() {
        return hubVal;
    }

    public void setHubVal(String hubVal) {
        this.hubVal = hubVal == null ? null : hubVal.trim();
    }

    public Byte getHubValType() {
        return hubValType;
    }

    public void setHubValType(Byte hubValType) {
        this.hubValType = hubValType;
    }

    public Byte getMappingType() {
        return mappingType;
    }

    public void setMappingType(Byte mappingType) {
        this.mappingType = mappingType;
    }

    public Byte getMappingState() {
        return mappingState;
    }

    public void setMappingState(Byte mappingState) {
        this.mappingState = mappingState;
    }

    public Short getSortVal() {
        return sortVal;
    }

    public void setSortVal(Short sortVal) {
        this.sortVal = sortVal;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser == null ? null : updateUser.trim();
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
        sb.append(", hubSupplierValMappingId=").append(hubSupplierValMappingId);
        sb.append(", columnMappingId=").append(columnMappingId);
        sb.append(", supplierId=").append(supplierId);
        sb.append(", supplierValNo=").append(supplierValNo);
        sb.append(", supplierValParentNo=").append(supplierValParentNo);
        sb.append(", supplierVal=").append(supplierVal);
        sb.append(", hubValNo=").append(hubValNo);
        sb.append(", hubVal=").append(hubVal);
        sb.append(", hubValType=").append(hubValType);
        sb.append(", mappingType=").append(mappingType);
        sb.append(", mappingState=").append(mappingState);
        sb.append(", sortVal=").append(sortVal);
        sb.append(", createTime=").append(createTime);
        sb.append(", createUser=").append(createUser);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", updateUser=").append(updateUser);
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
        HubSupplierValueMapping other = (HubSupplierValueMapping) that;
        return (this.getHubSupplierValMappingId() == null ? other.getHubSupplierValMappingId() == null : this.getHubSupplierValMappingId().equals(other.getHubSupplierValMappingId()))
            && (this.getColumnMappingId() == null ? other.getColumnMappingId() == null : this.getColumnMappingId().equals(other.getColumnMappingId()))
            && (this.getSupplierId() == null ? other.getSupplierId() == null : this.getSupplierId().equals(other.getSupplierId()))
            && (this.getSupplierValNo() == null ? other.getSupplierValNo() == null : this.getSupplierValNo().equals(other.getSupplierValNo()))
            && (this.getSupplierValParentNo() == null ? other.getSupplierValParentNo() == null : this.getSupplierValParentNo().equals(other.getSupplierValParentNo()))
            && (this.getSupplierVal() == null ? other.getSupplierVal() == null : this.getSupplierVal().equals(other.getSupplierVal()))
            && (this.getHubValNo() == null ? other.getHubValNo() == null : this.getHubValNo().equals(other.getHubValNo()))
            && (this.getHubVal() == null ? other.getHubVal() == null : this.getHubVal().equals(other.getHubVal()))
            && (this.getHubValType() == null ? other.getHubValType() == null : this.getHubValType().equals(other.getHubValType()))
            && (this.getMappingType() == null ? other.getMappingType() == null : this.getMappingType().equals(other.getMappingType()))
            && (this.getMappingState() == null ? other.getMappingState() == null : this.getMappingState().equals(other.getMappingState()))
            && (this.getSortVal() == null ? other.getSortVal() == null : this.getSortVal().equals(other.getSortVal()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getCreateUser() == null ? other.getCreateUser() == null : this.getCreateUser().equals(other.getCreateUser()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getUpdateUser() == null ? other.getUpdateUser() == null : this.getUpdateUser().equals(other.getUpdateUser()))
            && (this.getMemo() == null ? other.getMemo() == null : this.getMemo().equals(other.getMemo()))
            && (this.getDataState() == null ? other.getDataState() == null : this.getDataState().equals(other.getDataState()))
            && (this.getVersion() == null ? other.getVersion() == null : this.getVersion().equals(other.getVersion()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getHubSupplierValMappingId() == null) ? 0 : getHubSupplierValMappingId().hashCode());
        result = prime * result + ((getColumnMappingId() == null) ? 0 : getColumnMappingId().hashCode());
        result = prime * result + ((getSupplierId() == null) ? 0 : getSupplierId().hashCode());
        result = prime * result + ((getSupplierValNo() == null) ? 0 : getSupplierValNo().hashCode());
        result = prime * result + ((getSupplierValParentNo() == null) ? 0 : getSupplierValParentNo().hashCode());
        result = prime * result + ((getSupplierVal() == null) ? 0 : getSupplierVal().hashCode());
        result = prime * result + ((getHubValNo() == null) ? 0 : getHubValNo().hashCode());
        result = prime * result + ((getHubVal() == null) ? 0 : getHubVal().hashCode());
        result = prime * result + ((getHubValType() == null) ? 0 : getHubValType().hashCode());
        result = prime * result + ((getMappingType() == null) ? 0 : getMappingType().hashCode());
        result = prime * result + ((getMappingState() == null) ? 0 : getMappingState().hashCode());
        result = prime * result + ((getSortVal() == null) ? 0 : getSortVal().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getCreateUser() == null) ? 0 : getCreateUser().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getUpdateUser() == null) ? 0 : getUpdateUser().hashCode());
        result = prime * result + ((getMemo() == null) ? 0 : getMemo().hashCode());
        result = prime * result + ((getDataState() == null) ? 0 : getDataState().hashCode());
        result = prime * result + ((getVersion() == null) ? 0 : getVersion().hashCode());
        return result;
    }
}