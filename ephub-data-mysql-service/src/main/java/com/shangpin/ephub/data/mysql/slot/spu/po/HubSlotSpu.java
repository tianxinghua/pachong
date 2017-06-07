package com.shangpin.ephub.data.mysql.slot.spu.po;

import java.io.Serializable;
import java.util.Date;

public class HubSlotSpu implements Serializable {
    /**
     * 主键
     */
    private Long slotSpuId;

    /**
     * slotspu编号
     */
    private String slotSpuNo;

    /**
     * 货号
     */
    private String spuModel;

    /**
     * 品牌编号
     */
    private String brandNo;

    /**
     * 类目编号
     */
    private String categoryNo;

    /**
     * 二级品类编号（冗余）
     */
    private String secondCategoryNo;

    /**
     * 上市时间
     */
    private String marketTime;

    /**
     * 上市季节
     */
    private String season;

    /**
     * 上市季节_英文
     */
    private String seasonEn;

    /**
     * 商品名称
     */
    private String spuName;

    /**
     * 性别
     */
    private String gender;

    /**
     * 产品描述
     */
    private String spuDesc;

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
     * 0:未寄出 1：已寄出
     */
    private Byte spuState;

    /**
     * 颜色
     */
    private String hubColor;

    /**
     * 颜色码
     */
    private String hubColorNo;

    /**
     * 主图Url(尚品)
     */
    private String picUrl;

    /**
     * 信息来源
     */
    private Byte infoFrom;

    /**
     * 备注（修改的货号）
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

    public Long getSlotSpuId() {
        return slotSpuId;
    }

    public void setSlotSpuId(Long slotSpuId) {
        this.slotSpuId = slotSpuId;
    }

    public String getSlotSpuNo() {
        return slotSpuNo;
    }

    public void setSlotSpuNo(String slotSpuNo) {
        this.slotSpuNo = slotSpuNo == null ? null : slotSpuNo.trim();
    }

    public String getSpuModel() {
        return spuModel;
    }

    public void setSpuModel(String spuModel) {
        this.spuModel = spuModel == null ? null : spuModel.trim();
    }

    public String getBrandNo() {
        return brandNo;
    }

    public void setBrandNo(String brandNo) {
        this.brandNo = brandNo == null ? null : brandNo.trim();
    }

    public String getCategoryNo() {
        return categoryNo;
    }

    public void setCategoryNo(String categoryNo) {
        this.categoryNo = categoryNo == null ? null : categoryNo.trim();
    }

    public String getSecondCategoryNo() {
        return secondCategoryNo;
    }

    public void setSecondCategoryNo(String secondCategoryNo) {
        this.secondCategoryNo = secondCategoryNo == null ? null : secondCategoryNo.trim();
    }

    public String getMarketTime() {
        return marketTime;
    }

    public void setMarketTime(String marketTime) {
        this.marketTime = marketTime == null ? null : marketTime.trim();
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season == null ? null : season.trim();
    }

    public String getSeasonEn() {
        return seasonEn;
    }

    public void setSeasonEn(String seasonEn) {
        this.seasonEn = seasonEn == null ? null : seasonEn.trim();
    }

    public String getSpuName() {
        return spuName;
    }

    public void setSpuName(String spuName) {
        this.spuName = spuName == null ? null : spuName.trim();
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender == null ? null : gender.trim();
    }

    public String getSpuDesc() {
        return spuDesc;
    }

    public void setSpuDesc(String spuDesc) {
        this.spuDesc = spuDesc == null ? null : spuDesc.trim();
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

    public Byte getSpuState() {
        return spuState;
    }

    public void setSpuState(Byte spuState) {
        this.spuState = spuState;
    }

    public String getHubColor() {
        return hubColor;
    }

    public void setHubColor(String hubColor) {
        this.hubColor = hubColor == null ? null : hubColor.trim();
    }

    public String getHubColorNo() {
        return hubColorNo;
    }

    public void setHubColorNo(String hubColorNo) {
        this.hubColorNo = hubColorNo == null ? null : hubColorNo.trim();
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl == null ? null : picUrl.trim();
    }

    public Byte getInfoFrom() {
        return infoFrom;
    }

    public void setInfoFrom(Byte infoFrom) {
        this.infoFrom = infoFrom;
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
        sb.append(", slotSpuId=").append(slotSpuId);
        sb.append(", slotSpuNo=").append(slotSpuNo);
        sb.append(", spuModel=").append(spuModel);
        sb.append(", brandNo=").append(brandNo);
        sb.append(", categoryNo=").append(categoryNo);
        sb.append(", secondCategoryNo=").append(secondCategoryNo);
        sb.append(", marketTime=").append(marketTime);
        sb.append(", season=").append(season);
        sb.append(", seasonEn=").append(seasonEn);
        sb.append(", spuName=").append(spuName);
        sb.append(", gender=").append(gender);
        sb.append(", spuDesc=").append(spuDesc);
        sb.append(", createTime=").append(createTime);
        sb.append(", createUser=").append(createUser);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", updateUser=").append(updateUser);
        sb.append(", spuState=").append(spuState);
        sb.append(", hubColor=").append(hubColor);
        sb.append(", hubColorNo=").append(hubColorNo);
        sb.append(", picUrl=").append(picUrl);
        sb.append(", infoFrom=").append(infoFrom);
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
        HubSlotSpu other = (HubSlotSpu) that;
        return (this.getSlotSpuId() == null ? other.getSlotSpuId() == null : this.getSlotSpuId().equals(other.getSlotSpuId()))
            && (this.getSlotSpuNo() == null ? other.getSlotSpuNo() == null : this.getSlotSpuNo().equals(other.getSlotSpuNo()))
            && (this.getSpuModel() == null ? other.getSpuModel() == null : this.getSpuModel().equals(other.getSpuModel()))
            && (this.getBrandNo() == null ? other.getBrandNo() == null : this.getBrandNo().equals(other.getBrandNo()))
            && (this.getCategoryNo() == null ? other.getCategoryNo() == null : this.getCategoryNo().equals(other.getCategoryNo()))
            && (this.getSecondCategoryNo() == null ? other.getSecondCategoryNo() == null : this.getSecondCategoryNo().equals(other.getSecondCategoryNo()))
            && (this.getMarketTime() == null ? other.getMarketTime() == null : this.getMarketTime().equals(other.getMarketTime()))
            && (this.getSeason() == null ? other.getSeason() == null : this.getSeason().equals(other.getSeason()))
            && (this.getSeasonEn() == null ? other.getSeasonEn() == null : this.getSeasonEn().equals(other.getSeasonEn()))
            && (this.getSpuName() == null ? other.getSpuName() == null : this.getSpuName().equals(other.getSpuName()))
            && (this.getGender() == null ? other.getGender() == null : this.getGender().equals(other.getGender()))
            && (this.getSpuDesc() == null ? other.getSpuDesc() == null : this.getSpuDesc().equals(other.getSpuDesc()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getCreateUser() == null ? other.getCreateUser() == null : this.getCreateUser().equals(other.getCreateUser()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getUpdateUser() == null ? other.getUpdateUser() == null : this.getUpdateUser().equals(other.getUpdateUser()))
            && (this.getSpuState() == null ? other.getSpuState() == null : this.getSpuState().equals(other.getSpuState()))
            && (this.getHubColor() == null ? other.getHubColor() == null : this.getHubColor().equals(other.getHubColor()))
            && (this.getHubColorNo() == null ? other.getHubColorNo() == null : this.getHubColorNo().equals(other.getHubColorNo()))
            && (this.getPicUrl() == null ? other.getPicUrl() == null : this.getPicUrl().equals(other.getPicUrl()))
            && (this.getInfoFrom() == null ? other.getInfoFrom() == null : this.getInfoFrom().equals(other.getInfoFrom()))
            && (this.getMemo() == null ? other.getMemo() == null : this.getMemo().equals(other.getMemo()))
            && (this.getDataState() == null ? other.getDataState() == null : this.getDataState().equals(other.getDataState()))
            && (this.getVersion() == null ? other.getVersion() == null : this.getVersion().equals(other.getVersion()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getSlotSpuId() == null) ? 0 : getSlotSpuId().hashCode());
        result = prime * result + ((getSlotSpuNo() == null) ? 0 : getSlotSpuNo().hashCode());
        result = prime * result + ((getSpuModel() == null) ? 0 : getSpuModel().hashCode());
        result = prime * result + ((getBrandNo() == null) ? 0 : getBrandNo().hashCode());
        result = prime * result + ((getCategoryNo() == null) ? 0 : getCategoryNo().hashCode());
        result = prime * result + ((getSecondCategoryNo() == null) ? 0 : getSecondCategoryNo().hashCode());
        result = prime * result + ((getMarketTime() == null) ? 0 : getMarketTime().hashCode());
        result = prime * result + ((getSeason() == null) ? 0 : getSeason().hashCode());
        result = prime * result + ((getSeasonEn() == null) ? 0 : getSeasonEn().hashCode());
        result = prime * result + ((getSpuName() == null) ? 0 : getSpuName().hashCode());
        result = prime * result + ((getGender() == null) ? 0 : getGender().hashCode());
        result = prime * result + ((getSpuDesc() == null) ? 0 : getSpuDesc().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getCreateUser() == null) ? 0 : getCreateUser().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getUpdateUser() == null) ? 0 : getUpdateUser().hashCode());
        result = prime * result + ((getSpuState() == null) ? 0 : getSpuState().hashCode());
        result = prime * result + ((getHubColor() == null) ? 0 : getHubColor().hashCode());
        result = prime * result + ((getHubColorNo() == null) ? 0 : getHubColorNo().hashCode());
        result = prime * result + ((getPicUrl() == null) ? 0 : getPicUrl().hashCode());
        result = prime * result + ((getInfoFrom() == null) ? 0 : getInfoFrom().hashCode());
        result = prime * result + ((getMemo() == null) ? 0 : getMemo().hashCode());
        result = prime * result + ((getDataState() == null) ? 0 : getDataState().hashCode());
        result = prime * result + ((getVersion() == null) ? 0 : getVersion().hashCode());
        return result;
    }
}