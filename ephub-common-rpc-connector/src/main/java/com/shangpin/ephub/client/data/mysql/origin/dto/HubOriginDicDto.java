package com.shangpin.ephub.client.data.mysql.origin.dto;

import java.io.Serializable;
import java.util.Date;

public class HubOriginDicDto implements Serializable {
    /**
     * 主键
     */
    private Long originDicId;

    /**
     * 供货商产地
     */
    private String supplierOrigin;

    /**
     * hub产地
     */
    private String hubOrigin;

    /**
     * hub产地编号,希望是尚品的产地ID
     */
    private String hubOriginNo;

    /**
     * 发布状态
     */
    private Byte pushState;

    /**
     * 发布时间
     */
    private Date pushTime;

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

    private static final long serialVersionUID = 1L;

    public Long getOriginDicId() {
        return originDicId;
    }

    public void setOriginDicId(Long originDicId) {
        this.originDicId = originDicId;
    }

    public String getSupplierOrigin() {
        return supplierOrigin;
    }

    public void setSupplierOrigin(String supplierOrigin) {
        this.supplierOrigin = supplierOrigin == null ? null : supplierOrigin.trim();
    }

    public String getHubOrigin() {
        return hubOrigin;
    }

    public void setHubOrigin(String hubOrigin) {
        this.hubOrigin = hubOrigin == null ? null : hubOrigin.trim();
    }

    public String getHubOriginNo() {
        return hubOriginNo;
    }

    public void setHubOriginNo(String hubOriginNo) {
        this.hubOriginNo = hubOriginNo == null ? null : hubOriginNo.trim();
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", originDicId=").append(originDicId);
        sb.append(", supplierOrigin=").append(supplierOrigin);
        sb.append(", hubOrigin=").append(hubOrigin);
        sb.append(", hubOriginNo=").append(hubOriginNo);
        sb.append(", pushState=").append(pushState);
        sb.append(", pushTime=").append(pushTime);
        sb.append(", createTime=").append(createTime);
        sb.append(", createUser=").append(createUser);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", updateUser=").append(updateUser);
        sb.append(", dataState=").append(dataState);
        sb.append(", version=").append(version);
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
        HubOriginDicDto other = (HubOriginDicDto) that;
        return (this.getOriginDicId() == null ? other.getOriginDicId() == null : this.getOriginDicId().equals(other.getOriginDicId()))
            && (this.getSupplierOrigin() == null ? other.getSupplierOrigin() == null : this.getSupplierOrigin().equals(other.getSupplierOrigin()))
            && (this.getHubOrigin() == null ? other.getHubOrigin() == null : this.getHubOrigin().equals(other.getHubOrigin()))
            && (this.getHubOriginNo() == null ? other.getHubOriginNo() == null : this.getHubOriginNo().equals(other.getHubOriginNo()))
            && (this.getPushState() == null ? other.getPushState() == null : this.getPushState().equals(other.getPushState()))
            && (this.getPushTime() == null ? other.getPushTime() == null : this.getPushTime().equals(other.getPushTime()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getCreateUser() == null ? other.getCreateUser() == null : this.getCreateUser().equals(other.getCreateUser()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getUpdateUser() == null ? other.getUpdateUser() == null : this.getUpdateUser().equals(other.getUpdateUser()))
            && (this.getDataState() == null ? other.getDataState() == null : this.getDataState().equals(other.getDataState()))
            && (this.getVersion() == null ? other.getVersion() == null : this.getVersion().equals(other.getVersion()))
            && (this.getMemo() == null ? other.getMemo() == null : this.getMemo().equals(other.getMemo()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getOriginDicId() == null) ? 0 : getOriginDicId().hashCode());
        result = prime * result + ((getSupplierOrigin() == null) ? 0 : getSupplierOrigin().hashCode());
        result = prime * result + ((getHubOrigin() == null) ? 0 : getHubOrigin().hashCode());
        result = prime * result + ((getHubOriginNo() == null) ? 0 : getHubOriginNo().hashCode());
        result = prime * result + ((getPushState() == null) ? 0 : getPushState().hashCode());
        result = prime * result + ((getPushTime() == null) ? 0 : getPushTime().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getCreateUser() == null) ? 0 : getCreateUser().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getUpdateUser() == null) ? 0 : getUpdateUser().hashCode());
        result = prime * result + ((getDataState() == null) ? 0 : getDataState().hashCode());
        result = prime * result + ((getVersion() == null) ? 0 : getVersion().hashCode());
        result = prime * result + ((getMemo() == null) ? 0 : getMemo().hashCode());
        return result;
    }
}