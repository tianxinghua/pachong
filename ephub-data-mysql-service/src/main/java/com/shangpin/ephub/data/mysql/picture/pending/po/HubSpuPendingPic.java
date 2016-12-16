package com.shangpin.ephub.data.mysql.picture.pending.po;

import java.io.Serializable;
import java.util.Date;

public class HubSpuPendingPic implements Serializable {
    /**
     * 主键
     */
    private Long spuPicId;

    /**
     * spu主键
     */
    private Long spuPendingId;

    /**
     * 图片主键
     */
    private Long picId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 最后更新时间
     */
    private Date updateTime;

    /**
     * 图片路径
     */
    private String spPicUrl;

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

    public Long getSpuPicId() {
        return spuPicId;
    }

    public void setSpuPicId(Long spuPicId) {
        this.spuPicId = spuPicId;
    }

    public Long getSpuPendingId() {
        return spuPendingId;
    }

    public void setSpuPendingId(Long spuPendingId) {
        this.spuPendingId = spuPendingId;
    }

    public Long getPicId() {
        return picId;
    }

    public void setPicId(Long picId) {
        this.picId = picId;
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

    public String getSpPicUrl() {
        return spPicUrl;
    }

    public void setSpPicUrl(String spPicUrl) {
        this.spPicUrl = spPicUrl == null ? null : spPicUrl.trim();
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
        sb.append(", spuPicId=").append(spuPicId);
        sb.append(", spuPendingId=").append(spuPendingId);
        sb.append(", picId=").append(picId);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", spPicUrl=").append(spPicUrl);
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
        HubSpuPendingPic other = (HubSpuPendingPic) that;
        return (this.getSpuPicId() == null ? other.getSpuPicId() == null : this.getSpuPicId().equals(other.getSpuPicId()))
            && (this.getSpuPendingId() == null ? other.getSpuPendingId() == null : this.getSpuPendingId().equals(other.getSpuPendingId()))
            && (this.getPicId() == null ? other.getPicId() == null : this.getPicId().equals(other.getPicId()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getSpPicUrl() == null ? other.getSpPicUrl() == null : this.getSpPicUrl().equals(other.getSpPicUrl()))
            && (this.getMemo() == null ? other.getMemo() == null : this.getMemo().equals(other.getMemo()))
            && (this.getDataState() == null ? other.getDataState() == null : this.getDataState().equals(other.getDataState()))
            && (this.getVersion() == null ? other.getVersion() == null : this.getVersion().equals(other.getVersion()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getSpuPicId() == null) ? 0 : getSpuPicId().hashCode());
        result = prime * result + ((getSpuPendingId() == null) ? 0 : getSpuPendingId().hashCode());
        result = prime * result + ((getPicId() == null) ? 0 : getPicId().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getSpPicUrl() == null) ? 0 : getSpPicUrl().hashCode());
        result = prime * result + ((getMemo() == null) ? 0 : getMemo().hashCode());
        result = prime * result + ((getDataState() == null) ? 0 : getDataState().hashCode());
        result = prime * result + ((getVersion() == null) ? 0 : getVersion().hashCode());
        return result;
    }
}