package com.shangpin.ephub.data.mysql.mapping.value.po;

import java.io.Serializable;
import java.util.Date;

public class HubSupplierValueMapping implements Serializable {
    /**
     * 主键
     */
    private Long valueMappingId;

    /**
     * 列映射Id
     */
    private Long colMappingId;

    /**
     * 供应商Id
     */
    private String supplierId;

    /**
     * 供应商值编码
     */
    private String supplierValueNo;

    /**
     * 供应商值父级编码
     */
    private String supplierValueParentNo;

    /**
     * 供应商值
     */
    private String supplierValue;

    /**
     * 尚品值编码
     */
    private String hubValueNo;

    /**
     * 尚品值
     */
    private String hubValue;

    /**
     * 1、品牌 2、品类 3、产地 等
     */
    private Byte hubValueType;

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
    private Short sortValue;

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

    public Long getValueMappingId() {
        return valueMappingId;
    }

    public void setValueMappingId(Long valueMappingId) {
        this.valueMappingId = valueMappingId;
    }

    public Long getColMappingId() {
        return colMappingId;
    }

    public void setColMappingId(Long colMappingId) {
        this.colMappingId = colMappingId;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId == null ? null : supplierId.trim();
    }

    public String getSupplierValueNo() {
        return supplierValueNo;
    }

    public void setSupplierValueNo(String supplierValueNo) {
        this.supplierValueNo = supplierValueNo == null ? null : supplierValueNo.trim();
    }

    public String getSupplierValueParentNo() {
        return supplierValueParentNo;
    }

    public void setSupplierValueParentNo(String supplierValueParentNo) {
        this.supplierValueParentNo = supplierValueParentNo == null ? null : supplierValueParentNo.trim();
    }

    public String getSupplierValue() {
        return supplierValue;
    }

    public void setSupplierValue(String supplierValue) {
        this.supplierValue = supplierValue == null ? null : supplierValue.trim();
    }

    public String getHubValueNo() {
        return hubValueNo;
    }

    public void setHubValueNo(String hubValueNo) {
        this.hubValueNo = hubValueNo == null ? null : hubValueNo.trim();
    }

    public String getHubValue() {
        return hubValue;
    }

    public void setHubValue(String hubValue) {
        this.hubValue = hubValue == null ? null : hubValue.trim();
    }

    public Byte getHubValueType() {
        return hubValueType;
    }

    public void setHubValueType(Byte hubValueType) {
        this.hubValueType = hubValueType;
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

    public Short getSortValue() {
        return sortValue;
    }

    public void setSortValue(Short sortValue) {
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
        sb.append(", valueMappingId=").append(valueMappingId);
        sb.append(", colMappingId=").append(colMappingId);
        sb.append(", supplierId=").append(supplierId);
        sb.append(", supplierValueNo=").append(supplierValueNo);
        sb.append(", supplierValueParentNo=").append(supplierValueParentNo);
        sb.append(", supplierValue=").append(supplierValue);
        sb.append(", hubValueNo=").append(hubValueNo);
        sb.append(", hubValue=").append(hubValue);
        sb.append(", hubValueType=").append(hubValueType);
        sb.append(", mappingType=").append(mappingType);
        sb.append(", mappingState=").append(mappingState);
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
        HubSupplierValueMapping other = (HubSupplierValueMapping) that;
        return (this.getValueMappingId() == null ? other.getValueMappingId() == null : this.getValueMappingId().equals(other.getValueMappingId()))
            && (this.getColMappingId() == null ? other.getColMappingId() == null : this.getColMappingId().equals(other.getColMappingId()))
            && (this.getSupplierId() == null ? other.getSupplierId() == null : this.getSupplierId().equals(other.getSupplierId()))
            && (this.getSupplierValueNo() == null ? other.getSupplierValueNo() == null : this.getSupplierValueNo().equals(other.getSupplierValueNo()))
            && (this.getSupplierValueParentNo() == null ? other.getSupplierValueParentNo() == null : this.getSupplierValueParentNo().equals(other.getSupplierValueParentNo()))
            && (this.getSupplierValue() == null ? other.getSupplierValue() == null : this.getSupplierValue().equals(other.getSupplierValue()))
            && (this.getHubValueNo() == null ? other.getHubValueNo() == null : this.getHubValueNo().equals(other.getHubValueNo()))
            && (this.getHubValue() == null ? other.getHubValue() == null : this.getHubValue().equals(other.getHubValue()))
            && (this.getHubValueType() == null ? other.getHubValueType() == null : this.getHubValueType().equals(other.getHubValueType()))
            && (this.getMappingType() == null ? other.getMappingType() == null : this.getMappingType().equals(other.getMappingType()))
            && (this.getMappingState() == null ? other.getMappingState() == null : this.getMappingState().equals(other.getMappingState()))
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
        result = prime * result + ((getValueMappingId() == null) ? 0 : getValueMappingId().hashCode());
        result = prime * result + ((getColMappingId() == null) ? 0 : getColMappingId().hashCode());
        result = prime * result + ((getSupplierId() == null) ? 0 : getSupplierId().hashCode());
        result = prime * result + ((getSupplierValueNo() == null) ? 0 : getSupplierValueNo().hashCode());
        result = prime * result + ((getSupplierValueParentNo() == null) ? 0 : getSupplierValueParentNo().hashCode());
        result = prime * result + ((getSupplierValue() == null) ? 0 : getSupplierValue().hashCode());
        result = prime * result + ((getHubValueNo() == null) ? 0 : getHubValueNo().hashCode());
        result = prime * result + ((getHubValue() == null) ? 0 : getHubValue().hashCode());
        result = prime * result + ((getHubValueType() == null) ? 0 : getHubValueType().hashCode());
        result = prime * result + ((getMappingType() == null) ? 0 : getMappingType().hashCode());
        result = prime * result + ((getMappingState() == null) ? 0 : getMappingState().hashCode());
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