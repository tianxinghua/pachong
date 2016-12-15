package com.shangpin.ephub.client.data.mysql.gender.dto;

import java.io.Serializable;
import java.util.Date;

public class HubGenderDicDto implements Serializable {
    /**
     * 主键
     */
    private Long genderDicId;

    /**
     * 供应商Id
     */
    private String supplierId;

    /**
     * 供应商性别
     */
    private String supplierGender;

    /**
     * hub性别
     */
    private String hubGender;

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
     * 数据状态
     */
    private Byte dataState;

    /**
     * 版本号
     */
    private Long version;

    /**
     * 备注
     */
    private String memo;

    /**
     * 发布状态
     */
    private Byte pushState;

    /**
     * 发布时间
     */
    private Date pushTime;

    private static final long serialVersionUID = 1L;

    public Long getGenderDicId() {
        return genderDicId;
    }

    public void setGenderDicId(Long genderDicId) {
        this.genderDicId = genderDicId;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId == null ? null : supplierId.trim();
    }

    public String getSupplierGender() {
        return supplierGender;
    }

    public void setSupplierGender(String supplierGender) {
        this.supplierGender = supplierGender == null ? null : supplierGender.trim();
    }

    public String getHubGender() {
        return hubGender;
    }

    public void setHubGender(String hubGender) {
        this.hubGender = hubGender == null ? null : hubGender.trim();
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

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    public Byte getPushState() {
        return pushState;
    }

    public void setPushState(Byte pushState) {
        this.pushState = pushState;
    }

    public Date getPushTime() {
        return pushTime;
    }

    public void setPushTime(Date pushTime) {
        this.pushTime = pushTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", genderDicId=").append(genderDicId);
        sb.append(", supplierId=").append(supplierId);
        sb.append(", supplierGender=").append(supplierGender);
        sb.append(", hubGender=").append(hubGender);
        sb.append(", createTime=").append(createTime);
        sb.append(", createUser=").append(createUser);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", updateUser=").append(updateUser);
        sb.append(", dataState=").append(dataState);
        sb.append(", version=").append(version);
        sb.append(", memo=").append(memo);
        sb.append(", pushState=").append(pushState);
        sb.append(", pushTime=").append(pushTime);
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
        HubGenderDicDto other = (HubGenderDicDto) that;
        return (this.getGenderDicId() == null ? other.getGenderDicId() == null : this.getGenderDicId().equals(other.getGenderDicId()))
            && (this.getSupplierId() == null ? other.getSupplierId() == null : this.getSupplierId().equals(other.getSupplierId()))
            && (this.getSupplierGender() == null ? other.getSupplierGender() == null : this.getSupplierGender().equals(other.getSupplierGender()))
            && (this.getHubGender() == null ? other.getHubGender() == null : this.getHubGender().equals(other.getHubGender()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getCreateUser() == null ? other.getCreateUser() == null : this.getCreateUser().equals(other.getCreateUser()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getUpdateUser() == null ? other.getUpdateUser() == null : this.getUpdateUser().equals(other.getUpdateUser()))
            && (this.getDataState() == null ? other.getDataState() == null : this.getDataState().equals(other.getDataState()))
            && (this.getVersion() == null ? other.getVersion() == null : this.getVersion().equals(other.getVersion()))
            && (this.getMemo() == null ? other.getMemo() == null : this.getMemo().equals(other.getMemo()))
            && (this.getPushState() == null ? other.getPushState() == null : this.getPushState().equals(other.getPushState()))
            && (this.getPushTime() == null ? other.getPushTime() == null : this.getPushTime().equals(other.getPushTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getGenderDicId() == null) ? 0 : getGenderDicId().hashCode());
        result = prime * result + ((getSupplierId() == null) ? 0 : getSupplierId().hashCode());
        result = prime * result + ((getSupplierGender() == null) ? 0 : getSupplierGender().hashCode());
        result = prime * result + ((getHubGender() == null) ? 0 : getHubGender().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getCreateUser() == null) ? 0 : getCreateUser().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getUpdateUser() == null) ? 0 : getUpdateUser().hashCode());
        result = prime * result + ((getDataState() == null) ? 0 : getDataState().hashCode());
        result = prime * result + ((getVersion() == null) ? 0 : getVersion().hashCode());
        result = prime * result + ((getMemo() == null) ? 0 : getMemo().hashCode());
        result = prime * result + ((getPushState() == null) ? 0 : getPushState().hashCode());
        result = prime * result + ((getPushTime() == null) ? 0 : getPushTime().hashCode());
        return result;
    }
}