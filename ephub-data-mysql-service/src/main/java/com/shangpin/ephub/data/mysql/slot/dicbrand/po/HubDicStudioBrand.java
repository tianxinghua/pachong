package com.shangpin.ephub.data.mysql.slot.dicbrand.po;

import java.io.Serializable;
import java.util.Date;

public class HubDicStudioBrand implements Serializable {
    /**
     * 主键
     */
    private Long studioBrandId;

    /**
     * 尚品品牌
     */
    private String spBrandNo;

    /**
     * 品牌名称
     */
    private String brandName;

    /**
     * 品牌英文名称
     */
    private String brandNameEn;

    /**
     * 尚品品类
     */
    private String spCategoryNo;

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

    public Long getStudioBrandId() {
        return studioBrandId;
    }

    public void setStudioBrandId(Long studioBrandId) {
        this.studioBrandId = studioBrandId;
    }

    public String getSpBrandNo() {
        return spBrandNo;
    }

    public void setSpBrandNo(String spBrandNo) {
        this.spBrandNo = spBrandNo == null ? null : spBrandNo.trim();
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName == null ? null : brandName.trim();
    }

    public String getBrandNameEn() {
        return brandNameEn;
    }

    public void setBrandNameEn(String brandNameEn) {
        this.brandNameEn = brandNameEn == null ? null : brandNameEn.trim();
    }

    public String getSpCategoryNo() {
        return spCategoryNo;
    }

    public void setSpCategoryNo(String spCategoryNo) {
        this.spCategoryNo = spCategoryNo == null ? null : spCategoryNo.trim();
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
        sb.append(", studioBrandId=").append(studioBrandId);
        sb.append(", spBrandNo=").append(spBrandNo);
        sb.append(", brandName=").append(brandName);
        sb.append(", brandNameEn=").append(brandNameEn);
        sb.append(", spCategoryNo=").append(spCategoryNo);
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
        HubDicStudioBrand other = (HubDicStudioBrand) that;
        return (this.getStudioBrandId() == null ? other.getStudioBrandId() == null : this.getStudioBrandId().equals(other.getStudioBrandId()))
            && (this.getSpBrandNo() == null ? other.getSpBrandNo() == null : this.getSpBrandNo().equals(other.getSpBrandNo()))
            && (this.getBrandName() == null ? other.getBrandName() == null : this.getBrandName().equals(other.getBrandName()))
            && (this.getBrandNameEn() == null ? other.getBrandNameEn() == null : this.getBrandNameEn().equals(other.getBrandNameEn()))
            && (this.getSpCategoryNo() == null ? other.getSpCategoryNo() == null : this.getSpCategoryNo().equals(other.getSpCategoryNo()))
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
        result = prime * result + ((getStudioBrandId() == null) ? 0 : getStudioBrandId().hashCode());
        result = prime * result + ((getSpBrandNo() == null) ? 0 : getSpBrandNo().hashCode());
        result = prime * result + ((getBrandName() == null) ? 0 : getBrandName().hashCode());
        result = prime * result + ((getBrandNameEn() == null) ? 0 : getBrandNameEn().hashCode());
        result = prime * result + ((getSpCategoryNo() == null) ? 0 : getSpCategoryNo().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getCreateUser() == null) ? 0 : getCreateUser().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getUpdateUser() == null) ? 0 : getUpdateUser().hashCode());
        result = prime * result + ((getDataState() == null) ? 0 : getDataState().hashCode());
        result = prime * result + ((getVersion() == null) ? 0 : getVersion().hashCode());
        return result;
    }
}