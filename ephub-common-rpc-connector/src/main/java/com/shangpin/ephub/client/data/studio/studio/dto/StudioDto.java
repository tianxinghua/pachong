package com.shangpin.ephub.client.data.studio.studio.dto;

import java.io.Serializable;
import java.util.Date;

public class StudioDto implements Serializable {
    /**
     * 主键
     */
    private Long studioId;

    /**
     * 摄影棚名称
     */
    private String studioName;

    /**
     * 摄影棚编号
     */
    private String studioNo;

    /**
     * 摄影棚类型
     */
    private Byte studioType;

    /**
     * 摄影棚状态
     */
    private Byte studioStatus;

    /**
     * 联系人
     */
    private String studioContacts;

    /**
     * 联系方式
     */
    private String contactInfo;

    /**
     * 固话
     */
    private String telephone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 周期（日）
     */
    private Integer period;

    /**
     * 国家
     */
    private String country;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 备注
     */
    private String memo;

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
     * 数据状态：1未删除 0已删除
     */
    private Byte dataState;

    /**
     * 版本字段
     */
    private Long version;

    private static final long serialVersionUID = 1L;

    public Long getStudioId() {
        return studioId;
    }

    public void setStudioId(Long studioId) {
        this.studioId = studioId;
    }

    public String getStudioName() {
        return studioName;
    }

    public void setStudioName(String studioName) {
        this.studioName = studioName == null ? null : studioName.trim();
    }

    public String getStudioNo() {
        return studioNo;
    }

    public void setStudioNo(String studioNo) {
        this.studioNo = studioNo == null ? null : studioNo.trim();
    }

    public Byte getStudioType() {
        return studioType;
    }

    public void setStudioType(Byte studioType) {
        this.studioType = studioType;
    }

    public Byte getStudioStatus() {
        return studioStatus;
    }

    public void setStudioStatus(Byte studioStatus) {
        this.studioStatus = studioStatus;
    }

    public String getStudioContacts() {
        return studioContacts;
    }

    public void setStudioContacts(String studioContacts) {
        this.studioContacts = studioContacts == null ? null : studioContacts.trim();
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo == null ? null : contactInfo.trim();
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone == null ? null : telephone.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country == null ? null : country.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", studioId=").append(studioId);
        sb.append(", studioName=").append(studioName);
        sb.append(", studioNo=").append(studioNo);
        sb.append(", studioType=").append(studioType);
        sb.append(", studioStatus=").append(studioStatus);
        sb.append(", studioContacts=").append(studioContacts);
        sb.append(", contactInfo=").append(contactInfo);
        sb.append(", telephone=").append(telephone);
        sb.append(", email=").append(email);
        sb.append(", period=").append(period);
        sb.append(", country=").append(country);
        sb.append(", address=").append(address);
        sb.append(", memo=").append(memo);
        sb.append(", createTime=").append(createTime);
        sb.append(", createUser=").append(createUser);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", updateUser=").append(updateUser);
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
        StudioDto other = (StudioDto) that;
        return (this.getStudioId() == null ? other.getStudioId() == null : this.getStudioId().equals(other.getStudioId()))
            && (this.getStudioName() == null ? other.getStudioName() == null : this.getStudioName().equals(other.getStudioName()))
            && (this.getStudioNo() == null ? other.getStudioNo() == null : this.getStudioNo().equals(other.getStudioNo()))
            && (this.getStudioType() == null ? other.getStudioType() == null : this.getStudioType().equals(other.getStudioType()))
            && (this.getStudioStatus() == null ? other.getStudioStatus() == null : this.getStudioStatus().equals(other.getStudioStatus()))
            && (this.getStudioContacts() == null ? other.getStudioContacts() == null : this.getStudioContacts().equals(other.getStudioContacts()))
            && (this.getContactInfo() == null ? other.getContactInfo() == null : this.getContactInfo().equals(other.getContactInfo()))
            && (this.getTelephone() == null ? other.getTelephone() == null : this.getTelephone().equals(other.getTelephone()))
            && (this.getEmail() == null ? other.getEmail() == null : this.getEmail().equals(other.getEmail()))
            && (this.getPeriod() == null ? other.getPeriod() == null : this.getPeriod().equals(other.getPeriod()))
            && (this.getCountry() == null ? other.getCountry() == null : this.getCountry().equals(other.getCountry()))
            && (this.getAddress() == null ? other.getAddress() == null : this.getAddress().equals(other.getAddress()))
            && (this.getMemo() == null ? other.getMemo() == null : this.getMemo().equals(other.getMemo()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getCreateUser() == null ? other.getCreateUser() == null : this.getCreateUser().equals(other.getCreateUser()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getUpdateUser() == null ? other.getUpdateUser() == null : this.getUpdateUser().equals(other.getUpdateUser()))
            && (this.getDataState() == null ? other.getDataState() == null : this.getDataState().equals(other.getDataState()))
            && (this.getVersion() == null ? other.getVersion() == null : this.getVersion().equals(other.getVersion()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getStudioId() == null) ? 0 : getStudioId().hashCode());
        result = prime * result + ((getStudioName() == null) ? 0 : getStudioName().hashCode());
        result = prime * result + ((getStudioNo() == null) ? 0 : getStudioNo().hashCode());
        result = prime * result + ((getStudioType() == null) ? 0 : getStudioType().hashCode());
        result = prime * result + ((getStudioStatus() == null) ? 0 : getStudioStatus().hashCode());
        result = prime * result + ((getStudioContacts() == null) ? 0 : getStudioContacts().hashCode());
        result = prime * result + ((getContactInfo() == null) ? 0 : getContactInfo().hashCode());
        result = prime * result + ((getTelephone() == null) ? 0 : getTelephone().hashCode());
        result = prime * result + ((getEmail() == null) ? 0 : getEmail().hashCode());
        result = prime * result + ((getPeriod() == null) ? 0 : getPeriod().hashCode());
        result = prime * result + ((getCountry() == null) ? 0 : getCountry().hashCode());
        result = prime * result + ((getAddress() == null) ? 0 : getAddress().hashCode());
        result = prime * result + ((getMemo() == null) ? 0 : getMemo().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getCreateUser() == null) ? 0 : getCreateUser().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getUpdateUser() == null) ? 0 : getUpdateUser().hashCode());
        result = prime * result + ((getDataState() == null) ? 0 : getDataState().hashCode());
        result = prime * result + ((getVersion() == null) ? 0 : getVersion().hashCode());
        return result;
    }
}