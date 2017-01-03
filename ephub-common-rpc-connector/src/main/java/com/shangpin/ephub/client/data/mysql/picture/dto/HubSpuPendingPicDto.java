package com.shangpin.ephub.client.data.mysql.picture.dto;

import java.io.Serializable;
import java.util.Date;

public class HubSpuPendingPicDto implements Serializable {
    /**
     * 主键
     */
    private Long spuPendingPicId;

    /**
     * spu主键
     */
    private Long spuPendingId;

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

    /**
     * 图片类型
     */
    private Byte picType;

    /**
     * 排序
     */
    private Byte sortValue;

    /**
     * 原图片Url
     */
    private String picUrl;

    /**
     * 尚品图片编号
     */
    private String picNo;

    /**
     * 图片扩展名
     */
    private String picExtension;

    /**
     * 图片处理状态
     */
    private Byte picHandleState;

    /**
     * 供应商Id
     */
    private String suupplierId;

    /**
     * 供应商Spu编号
     */
    private String supplierSpuNo;

    private static final long serialVersionUID = 1L;

    public Long getSpuPendingPicId() {
        return spuPendingPicId;
    }

    public void setSpuPendingPicId(Long spuPendingPicId) {
        this.spuPendingPicId = spuPendingPicId;
    }

    public Long getSpuPendingId() {
        return spuPendingId;
    }

    public void setSpuPendingId(Long spuPendingId) {
        this.spuPendingId = spuPendingId;
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

    public Byte getPicType() {
        return picType;
    }

    public void setPicType(Byte picType) {
        this.picType = picType;
    }

    public Byte getSortValue() {
        return sortValue;
    }

    public void setSortValue(Byte sortValue) {
        this.sortValue = sortValue;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl == null ? null : picUrl.trim();
    }

    public String getPicNo() {
        return picNo;
    }

    public void setPicNo(String picNo) {
        this.picNo = picNo == null ? null : picNo.trim();
    }

    public String getPicExtension() {
        return picExtension;
    }

    public void setPicExtension(String picExtension) {
        this.picExtension = picExtension == null ? null : picExtension.trim();
    }

    public Byte getPicHandleState() {
        return picHandleState;
    }

    public void setPicHandleState(Byte picHandleState) {
        this.picHandleState = picHandleState;
    }

    public String getSuupplierId() {
        return suupplierId;
    }

    public void setSuupplierId(String suupplierId) {
        this.suupplierId = suupplierId == null ? null : suupplierId.trim();
    }

    public String getSupplierSpuNo() {
        return supplierSpuNo;
    }

    public void setSupplierSpuNo(String supplierSpuNo) {
        this.supplierSpuNo = supplierSpuNo == null ? null : supplierSpuNo.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", spuPendingPicId=").append(spuPendingPicId);
        sb.append(", spuPendingId=").append(spuPendingId);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", spPicUrl=").append(spPicUrl);
        sb.append(", memo=").append(memo);
        sb.append(", dataState=").append(dataState);
        sb.append(", version=").append(version);
        sb.append(", picType=").append(picType);
        sb.append(", sortValue=").append(sortValue);
        sb.append(", picUrl=").append(picUrl);
        sb.append(", picNo=").append(picNo);
        sb.append(", picExtension=").append(picExtension);
        sb.append(", picHandleState=").append(picHandleState);
        sb.append(", suupplierId=").append(suupplierId);
        sb.append(", supplierSpuNo=").append(supplierSpuNo);
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
        HubSpuPendingPicDto other = (HubSpuPendingPicDto) that;
        return (this.getSpuPendingPicId() == null ? other.getSpuPendingPicId() == null : this.getSpuPendingPicId().equals(other.getSpuPendingPicId()))
            && (this.getSpuPendingId() == null ? other.getSpuPendingId() == null : this.getSpuPendingId().equals(other.getSpuPendingId()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getSpPicUrl() == null ? other.getSpPicUrl() == null : this.getSpPicUrl().equals(other.getSpPicUrl()))
            && (this.getMemo() == null ? other.getMemo() == null : this.getMemo().equals(other.getMemo()))
            && (this.getDataState() == null ? other.getDataState() == null : this.getDataState().equals(other.getDataState()))
            && (this.getVersion() == null ? other.getVersion() == null : this.getVersion().equals(other.getVersion()))
            && (this.getPicType() == null ? other.getPicType() == null : this.getPicType().equals(other.getPicType()))
            && (this.getSortValue() == null ? other.getSortValue() == null : this.getSortValue().equals(other.getSortValue()))
            && (this.getPicUrl() == null ? other.getPicUrl() == null : this.getPicUrl().equals(other.getPicUrl()))
            && (this.getPicNo() == null ? other.getPicNo() == null : this.getPicNo().equals(other.getPicNo()))
            && (this.getPicExtension() == null ? other.getPicExtension() == null : this.getPicExtension().equals(other.getPicExtension()))
            && (this.getPicHandleState() == null ? other.getPicHandleState() == null : this.getPicHandleState().equals(other.getPicHandleState()))
            && (this.getSuupplierId() == null ? other.getSuupplierId() == null : this.getSuupplierId().equals(other.getSuupplierId()))
            && (this.getSupplierSpuNo() == null ? other.getSupplierSpuNo() == null : this.getSupplierSpuNo().equals(other.getSupplierSpuNo()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getSpuPendingPicId() == null) ? 0 : getSpuPendingPicId().hashCode());
        result = prime * result + ((getSpuPendingId() == null) ? 0 : getSpuPendingId().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getSpPicUrl() == null) ? 0 : getSpPicUrl().hashCode());
        result = prime * result + ((getMemo() == null) ? 0 : getMemo().hashCode());
        result = prime * result + ((getDataState() == null) ? 0 : getDataState().hashCode());
        result = prime * result + ((getVersion() == null) ? 0 : getVersion().hashCode());
        result = prime * result + ((getPicType() == null) ? 0 : getPicType().hashCode());
        result = prime * result + ((getSortValue() == null) ? 0 : getSortValue().hashCode());
        result = prime * result + ((getPicUrl() == null) ? 0 : getPicUrl().hashCode());
        result = prime * result + ((getPicNo() == null) ? 0 : getPicNo().hashCode());
        result = prime * result + ((getPicExtension() == null) ? 0 : getPicExtension().hashCode());
        result = prime * result + ((getPicHandleState() == null) ? 0 : getPicHandleState().hashCode());
        result = prime * result + ((getSuupplierId() == null) ? 0 : getSuupplierId().hashCode());
        result = prime * result + ((getSupplierSpuNo() == null) ? 0 : getSupplierSpuNo().hashCode());
        return result;
    }
}