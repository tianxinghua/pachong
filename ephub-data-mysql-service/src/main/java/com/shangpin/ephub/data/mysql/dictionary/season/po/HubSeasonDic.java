package com.shangpin.ephub.data.mysql.dictionary.season.po;

import java.io.Serializable;
import java.util.Date;

public class HubSeasonDic implements Serializable {
    /**
     * 主键
     */
    private Long seasonDicId;

    /**
     * 供应商Id
     */
    private String supplierid;

    /**
     * 供应商上市时间及季节
     */
    private String supplierSeason;

    /**
     * hub上市时间
     */
    private String hubMarketTime;

    /**
     * hub上市季节
     */
    private String hubSeason;

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

    /**
     * =0 无效季节 =1 有效季节
     */
    private Byte filterFlag;

    private static final long serialVersionUID = 1L;

    public Long getSeasonDicId() {
        return seasonDicId;
    }

    public void setSeasonDicId(Long seasonDicId) {
        this.seasonDicId = seasonDicId;
    }

    public String getSupplierid() {
        return supplierid;
    }

    public void setSupplierid(String supplierid) {
        this.supplierid = supplierid == null ? null : supplierid.trim();
    }

    public String getSupplierSeason() {
        return supplierSeason;
    }

    public void setSupplierSeason(String supplierSeason) {
        this.supplierSeason = supplierSeason == null ? null : supplierSeason.trim();
    }

    public String getHubMarketTime() {
        return hubMarketTime;
    }

    public void setHubMarketTime(String hubMarketTime) {
        this.hubMarketTime = hubMarketTime == null ? null : hubMarketTime.trim();
    }

    public String getHubSeason() {
        return hubSeason;
    }

    public void setHubSeason(String hubSeason) {
        this.hubSeason = hubSeason == null ? null : hubSeason.trim();
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

    public Byte getFilterFlag() {
        return filterFlag;
    }

    public void setFilterFlag(Byte filterFlag) {
        this.filterFlag = filterFlag;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", seasonDicId=").append(seasonDicId);
        sb.append(", supplierid=").append(supplierid);
        sb.append(", supplierSeason=").append(supplierSeason);
        sb.append(", hubMarketTime=").append(hubMarketTime);
        sb.append(", hubSeason=").append(hubSeason);
        sb.append(", createTime=").append(createTime);
        sb.append(", createUser=").append(createUser);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", updateUser=").append(updateUser);
        sb.append(", dataState=").append(dataState);
        sb.append(", version=").append(version);
        sb.append(", memo=").append(memo);
        sb.append(", pushState=").append(pushState);
        sb.append(", pushTime=").append(pushTime);
        sb.append(", filterFlag=").append(filterFlag);
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
        HubSeasonDic other = (HubSeasonDic) that;
        return (this.getSeasonDicId() == null ? other.getSeasonDicId() == null : this.getSeasonDicId().equals(other.getSeasonDicId()))
            && (this.getSupplierid() == null ? other.getSupplierid() == null : this.getSupplierid().equals(other.getSupplierid()))
            && (this.getSupplierSeason() == null ? other.getSupplierSeason() == null : this.getSupplierSeason().equals(other.getSupplierSeason()))
            && (this.getHubMarketTime() == null ? other.getHubMarketTime() == null : this.getHubMarketTime().equals(other.getHubMarketTime()))
            && (this.getHubSeason() == null ? other.getHubSeason() == null : this.getHubSeason().equals(other.getHubSeason()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getCreateUser() == null ? other.getCreateUser() == null : this.getCreateUser().equals(other.getCreateUser()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getUpdateUser() == null ? other.getUpdateUser() == null : this.getUpdateUser().equals(other.getUpdateUser()))
            && (this.getDataState() == null ? other.getDataState() == null : this.getDataState().equals(other.getDataState()))
            && (this.getVersion() == null ? other.getVersion() == null : this.getVersion().equals(other.getVersion()))
            && (this.getMemo() == null ? other.getMemo() == null : this.getMemo().equals(other.getMemo()))
            && (this.getPushState() == null ? other.getPushState() == null : this.getPushState().equals(other.getPushState()))
            && (this.getPushTime() == null ? other.getPushTime() == null : this.getPushTime().equals(other.getPushTime()))
            && (this.getFilterFlag() == null ? other.getFilterFlag() == null : this.getFilterFlag().equals(other.getFilterFlag()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getSeasonDicId() == null) ? 0 : getSeasonDicId().hashCode());
        result = prime * result + ((getSupplierid() == null) ? 0 : getSupplierid().hashCode());
        result = prime * result + ((getSupplierSeason() == null) ? 0 : getSupplierSeason().hashCode());
        result = prime * result + ((getHubMarketTime() == null) ? 0 : getHubMarketTime().hashCode());
        result = prime * result + ((getHubSeason() == null) ? 0 : getHubSeason().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getCreateUser() == null) ? 0 : getCreateUser().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getUpdateUser() == null) ? 0 : getUpdateUser().hashCode());
        result = prime * result + ((getDataState() == null) ? 0 : getDataState().hashCode());
        result = prime * result + ((getVersion() == null) ? 0 : getVersion().hashCode());
        result = prime * result + ((getMemo() == null) ? 0 : getMemo().hashCode());
        result = prime * result + ((getPushState() == null) ? 0 : getPushState().hashCode());
        result = prime * result + ((getPushTime() == null) ? 0 : getPushTime().hashCode());
        result = prime * result + ((getFilterFlag() == null) ? 0 : getFilterFlag().hashCode());
        return result;
    }
}