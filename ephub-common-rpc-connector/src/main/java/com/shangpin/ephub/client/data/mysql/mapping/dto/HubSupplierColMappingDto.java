package com.shangpin.ephub.client.data.mysql.mapping.dto;

import java.io.Serializable;
import java.util.Date;

public class HubSupplierColMappingDto implements Serializable {
    /**
     * 主键
     */
    private Long supplierColMappingId;

    /**
     * 供应商Id
     */
    private String supplierId;

    /**
     * 供应商列名称
     */
    private String supplierColName;

    /**
     * Hub列名称
     */
    private String hubColName;

    /**
     * 排序
     */
    private Byte sortValue;

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

    public Long getSupplierColMappingId() {
        return supplierColMappingId;
    }

    public void setSupplierColMappingId(Long supplierColMappingId) {
        this.supplierColMappingId = supplierColMappingId;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId == null ? null : supplierId.trim();
    }

    public String getSupplierColName() {
        return supplierColName;
    }

    public void setSupplierColName(String supplierColName) {
        this.supplierColName = supplierColName == null ? null : supplierColName.trim();
    }

    public String getHubColName() {
        return hubColName;
    }

    public void setHubColName(String hubColName) {
        this.hubColName = hubColName == null ? null : hubColName.trim();
    }

    public Byte getSortValue() {
        return sortValue;
    }

    public void setSortValue(Byte sortValue) {
        this.sortValue = sortValue;
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
        sb.append(", supplierColMappingId=").append(supplierColMappingId);
        sb.append(", supplierId=").append(supplierId);
        sb.append(", supplierColName=").append(supplierColName);
        sb.append(", hubColName=").append(hubColName);
        sb.append(", sortValue=").append(sortValue);
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
        HubSupplierColMappingDto other = (HubSupplierColMappingDto) that;
        return (this.getSupplierColMappingId() == null ? other.getSupplierColMappingId() == null : this.getSupplierColMappingId().equals(other.getSupplierColMappingId()))
            && (this.getSupplierId() == null ? other.getSupplierId() == null : this.getSupplierId().equals(other.getSupplierId()))
            && (this.getSupplierColName() == null ? other.getSupplierColName() == null : this.getSupplierColName().equals(other.getSupplierColName()))
            && (this.getHubColName() == null ? other.getHubColName() == null : this.getHubColName().equals(other.getHubColName()))
            && (this.getSortValue() == null ? other.getSortValue() == null : this.getSortValue().equals(other.getSortValue()))
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
        result = prime * result + ((getSupplierColMappingId() == null) ? 0 : getSupplierColMappingId().hashCode());
        result = prime * result + ((getSupplierId() == null) ? 0 : getSupplierId().hashCode());
        result = prime * result + ((getSupplierColName() == null) ? 0 : getSupplierColName().hashCode());
        result = prime * result + ((getHubColName() == null) ? 0 : getHubColName().hashCode());
        result = prime * result + ((getSortValue() == null) ? 0 : getSortValue().hashCode());
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