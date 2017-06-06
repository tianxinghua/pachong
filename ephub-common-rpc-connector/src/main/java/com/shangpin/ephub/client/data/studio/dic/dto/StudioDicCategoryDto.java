package com.shangpin.ephub.client.data.studio.dic.dto;

import java.io.Serializable;
import java.util.Date;

public class StudioDicCategoryDto implements Serializable {
    /**
     * 主键
     */
    private Long studioDicCategoryId;

    /**
     * 一级品类
     */
    private String categoryFirst;

    /**
     * 二级品类
     */
    private String categorySecond;

    /**
     * 摄影棚ID
     */
    private Long studioId;

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

    public Long getStudioDicCategoryId() {
        return studioDicCategoryId;
    }

    public void setStudioDicCategoryId(Long studioDicCategoryId) {
        this.studioDicCategoryId = studioDicCategoryId;
    }

    public String getCategoryFirst() {
        return categoryFirst;
    }

    public void setCategoryFirst(String categoryFirst) {
        this.categoryFirst = categoryFirst == null ? null : categoryFirst.trim();
    }

    public String getCategorySecond() {
        return categorySecond;
    }

    public void setCategorySecond(String categorySecond) {
        this.categorySecond = categorySecond == null ? null : categorySecond.trim();
    }

    public Long getStudioId() {
        return studioId;
    }

    public void setStudioId(Long studioId) {
        this.studioId = studioId;
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
        sb.append(", studioDicCategoryId=").append(studioDicCategoryId);
        sb.append(", categoryFirst=").append(categoryFirst);
        sb.append(", categorySecond=").append(categorySecond);
        sb.append(", studioId=").append(studioId);
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
        StudioDicCategoryDto other = (StudioDicCategoryDto) that;
        return (this.getStudioDicCategoryId() == null ? other.getStudioDicCategoryId() == null : this.getStudioDicCategoryId().equals(other.getStudioDicCategoryId()))
            && (this.getCategoryFirst() == null ? other.getCategoryFirst() == null : this.getCategoryFirst().equals(other.getCategoryFirst()))
            && (this.getCategorySecond() == null ? other.getCategorySecond() == null : this.getCategorySecond().equals(other.getCategorySecond()))
            && (this.getStudioId() == null ? other.getStudioId() == null : this.getStudioId().equals(other.getStudioId()))
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
        result = prime * result + ((getStudioDicCategoryId() == null) ? 0 : getStudioDicCategoryId().hashCode());
        result = prime * result + ((getCategoryFirst() == null) ? 0 : getCategoryFirst().hashCode());
        result = prime * result + ((getCategorySecond() == null) ? 0 : getCategorySecond().hashCode());
        result = prime * result + ((getStudioId() == null) ? 0 : getStudioId().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getCreateUser() == null) ? 0 : getCreateUser().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getUpdateUser() == null) ? 0 : getUpdateUser().hashCode());
        result = prime * result + ((getDataState() == null) ? 0 : getDataState().hashCode());
        result = prime * result + ((getVersion() == null) ? 0 : getVersion().hashCode());
        return result;
    }
}