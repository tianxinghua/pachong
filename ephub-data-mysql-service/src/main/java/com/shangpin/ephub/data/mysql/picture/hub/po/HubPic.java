package com.shangpin.ephub.data.mysql.picture.hub.po;

import java.io.Serializable;
import java.util.Date;

public class HubPic implements Serializable {
    /**
     * 主键
     */
    private Long picId;

    /**
     * 原图片Url
     */
    private String picUrl;

    /**
     * 尚品图片Url
     */
    private String spPicUrl;

    /**
     * 排序
     */
    private Byte sortValue;

    /**
     * 图片类型
     */
    private Byte picType;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 0 未选择该套图片 1 已选择该套图片
     */
    private Byte picState;

    /**
     * 尚品图片编号
     */
    private String picNo;

    /**
     * =0 待推送 =1 已推送 =2 推送失败 
     */
    private Byte picHandleState;

    /**
     * 图片扩展名.jpg
     */
    private String picExtension;

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

    public Long getPicId() {
        return picId;
    }

    public void setPicId(Long picId) {
        this.picId = picId;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl == null ? null : picUrl.trim();
    }

    public String getSpPicUrl() {
        return spPicUrl;
    }

    public void setSpPicUrl(String spPicUrl) {
        this.spPicUrl = spPicUrl == null ? null : spPicUrl.trim();
    }

    public Byte getSortValue() {
        return sortValue;
    }

    public void setSortValue(Byte sortValue) {
        this.sortValue = sortValue;
    }

    public Byte getPicType() {
        return picType;
    }

    public void setPicType(Byte picType) {
        this.picType = picType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Byte getPicState() {
        return picState;
    }

    public void setPicState(Byte picState) {
        this.picState = picState;
    }

    public String getPicNo() {
        return picNo;
    }

    public void setPicNo(String picNo) {
        this.picNo = picNo == null ? null : picNo.trim();
    }

    public Byte getPicHandleState() {
        return picHandleState;
    }

    public void setPicHandleState(Byte picHandleState) {
        this.picHandleState = picHandleState;
    }

    public String getPicExtension() {
        return picExtension;
    }

    public void setPicExtension(String picExtension) {
        this.picExtension = picExtension == null ? null : picExtension.trim();
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
        sb.append(", picId=").append(picId);
        sb.append(", picUrl=").append(picUrl);
        sb.append(", spPicUrl=").append(spPicUrl);
        sb.append(", sortValue=").append(sortValue);
        sb.append(", picType=").append(picType);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", picState=").append(picState);
        sb.append(", picNo=").append(picNo);
        sb.append(", picHandleState=").append(picHandleState);
        sb.append(", picExtension=").append(picExtension);
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
        HubPic other = (HubPic) that;
        return (this.getPicId() == null ? other.getPicId() == null : this.getPicId().equals(other.getPicId()))
            && (this.getPicUrl() == null ? other.getPicUrl() == null : this.getPicUrl().equals(other.getPicUrl()))
            && (this.getSpPicUrl() == null ? other.getSpPicUrl() == null : this.getSpPicUrl().equals(other.getSpPicUrl()))
            && (this.getSortValue() == null ? other.getSortValue() == null : this.getSortValue().equals(other.getSortValue()))
            && (this.getPicType() == null ? other.getPicType() == null : this.getPicType().equals(other.getPicType()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getPicState() == null ? other.getPicState() == null : this.getPicState().equals(other.getPicState()))
            && (this.getPicNo() == null ? other.getPicNo() == null : this.getPicNo().equals(other.getPicNo()))
            && (this.getPicHandleState() == null ? other.getPicHandleState() == null : this.getPicHandleState().equals(other.getPicHandleState()))
            && (this.getPicExtension() == null ? other.getPicExtension() == null : this.getPicExtension().equals(other.getPicExtension()))
            && (this.getMemo() == null ? other.getMemo() == null : this.getMemo().equals(other.getMemo()))
            && (this.getDataState() == null ? other.getDataState() == null : this.getDataState().equals(other.getDataState()))
            && (this.getVersion() == null ? other.getVersion() == null : this.getVersion().equals(other.getVersion()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getPicId() == null) ? 0 : getPicId().hashCode());
        result = prime * result + ((getPicUrl() == null) ? 0 : getPicUrl().hashCode());
        result = prime * result + ((getSpPicUrl() == null) ? 0 : getSpPicUrl().hashCode());
        result = prime * result + ((getSortValue() == null) ? 0 : getSortValue().hashCode());
        result = prime * result + ((getPicType() == null) ? 0 : getPicType().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getPicState() == null) ? 0 : getPicState().hashCode());
        result = prime * result + ((getPicNo() == null) ? 0 : getPicNo().hashCode());
        result = prime * result + ((getPicHandleState() == null) ? 0 : getPicHandleState().hashCode());
        result = prime * result + ((getPicExtension() == null) ? 0 : getPicExtension().hashCode());
        result = prime * result + ((getMemo() == null) ? 0 : getMemo().hashCode());
        result = prime * result + ((getDataState() == null) ? 0 : getDataState().hashCode());
        result = prime * result + ((getVersion() == null) ? 0 : getVersion().hashCode());
        return result;
    }
}