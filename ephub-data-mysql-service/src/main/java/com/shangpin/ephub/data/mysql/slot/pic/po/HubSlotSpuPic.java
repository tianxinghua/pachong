package com.shangpin.ephub.data.mysql.slot.pic.po;

import java.io.Serializable;
import java.util.Date;

public class HubSlotSpuPic implements Serializable {
    /**
     * 主键
     */
    private Long slotSpuPicId;

    /**
     * slotspu主键
     */
    private Long slotSpuId;

    /**
     * slotspusupplier主键
     */
    private Long slotSpuSupplierId;

    /**
     * slotspu编号
     */
    private String slotSpuNo;

    /**
     * 供货商编号
     */
    private String supplierNo;

    /**
     * 供货商ID
     */
    private String supplierId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 最后更新时间
     */
    private Date updateTime;

    /**
     * 图片路径
     */
    private String spPicUrl;

    /**
     * 图片类型
     */
    private Byte picType;

    /**
     * 排序
     */
    private Byte sortValue;

    /**
     * 图片扩展名
     */
    private String picExtension;

    /**
     * 图片处理状态
     */
    private Byte picHandleState;

    /**
     * 数据状态：1未删除 0已删除
     */
    private Byte dataState;

    /**
     * 版本字段
     */
    private Long version;

    private static final long serialVersionUID = 1L;

    public Long getSlotSpuPicId() {
        return slotSpuPicId;
    }

    public void setSlotSpuPicId(Long slotSpuPicId) {
        this.slotSpuPicId = slotSpuPicId;
    }

    public Long getSlotSpuId() {
        return slotSpuId;
    }

    public void setSlotSpuId(Long slotSpuId) {
        this.slotSpuId = slotSpuId;
    }

    public Long getSlotSpuSupplierId() {
        return slotSpuSupplierId;
    }

    public void setSlotSpuSupplierId(Long slotSpuSupplierId) {
        this.slotSpuSupplierId = slotSpuSupplierId;
    }

    public String getSlotSpuNo() {
        return slotSpuNo;
    }

    public void setSlotSpuNo(String slotSpuNo) {
        this.slotSpuNo = slotSpuNo == null ? null : slotSpuNo.trim();
    }

    public String getSupplierNo() {
        return supplierNo;
    }

    public void setSupplierNo(String supplierNo) {
        this.supplierNo = supplierNo == null ? null : supplierNo.trim();
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId == null ? null : supplierId.trim();
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

    public String getSpPicUrl() {
        return spPicUrl;
    }

    public void setSpPicUrl(String spPicUrl) {
        this.spPicUrl = spPicUrl == null ? null : spPicUrl.trim();
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
        sb.append(", slotSpuPicId=").append(slotSpuPicId);
        sb.append(", slotSpuId=").append(slotSpuId);
        sb.append(", slotSpuSupplierId=").append(slotSpuSupplierId);
        sb.append(", slotSpuNo=").append(slotSpuNo);
        sb.append(", supplierNo=").append(supplierNo);
        sb.append(", supplierId=").append(supplierId);
        sb.append(", createTime=").append(createTime);
        sb.append(", createUser=").append(createUser);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", spPicUrl=").append(spPicUrl);
        sb.append(", picType=").append(picType);
        sb.append(", sortValue=").append(sortValue);
        sb.append(", picExtension=").append(picExtension);
        sb.append(", picHandleState=").append(picHandleState);
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
        HubSlotSpuPic other = (HubSlotSpuPic) that;
        return (this.getSlotSpuPicId() == null ? other.getSlotSpuPicId() == null : this.getSlotSpuPicId().equals(other.getSlotSpuPicId()))
            && (this.getSlotSpuId() == null ? other.getSlotSpuId() == null : this.getSlotSpuId().equals(other.getSlotSpuId()))
            && (this.getSlotSpuSupplierId() == null ? other.getSlotSpuSupplierId() == null : this.getSlotSpuSupplierId().equals(other.getSlotSpuSupplierId()))
            && (this.getSlotSpuNo() == null ? other.getSlotSpuNo() == null : this.getSlotSpuNo().equals(other.getSlotSpuNo()))
            && (this.getSupplierNo() == null ? other.getSupplierNo() == null : this.getSupplierNo().equals(other.getSupplierNo()))
            && (this.getSupplierId() == null ? other.getSupplierId() == null : this.getSupplierId().equals(other.getSupplierId()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getCreateUser() == null ? other.getCreateUser() == null : this.getCreateUser().equals(other.getCreateUser()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getSpPicUrl() == null ? other.getSpPicUrl() == null : this.getSpPicUrl().equals(other.getSpPicUrl()))
            && (this.getPicType() == null ? other.getPicType() == null : this.getPicType().equals(other.getPicType()))
            && (this.getSortValue() == null ? other.getSortValue() == null : this.getSortValue().equals(other.getSortValue()))
            && (this.getPicExtension() == null ? other.getPicExtension() == null : this.getPicExtension().equals(other.getPicExtension()))
            && (this.getPicHandleState() == null ? other.getPicHandleState() == null : this.getPicHandleState().equals(other.getPicHandleState()))
            && (this.getDataState() == null ? other.getDataState() == null : this.getDataState().equals(other.getDataState()))
            && (this.getVersion() == null ? other.getVersion() == null : this.getVersion().equals(other.getVersion()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getSlotSpuPicId() == null) ? 0 : getSlotSpuPicId().hashCode());
        result = prime * result + ((getSlotSpuId() == null) ? 0 : getSlotSpuId().hashCode());
        result = prime * result + ((getSlotSpuSupplierId() == null) ? 0 : getSlotSpuSupplierId().hashCode());
        result = prime * result + ((getSlotSpuNo() == null) ? 0 : getSlotSpuNo().hashCode());
        result = prime * result + ((getSupplierNo() == null) ? 0 : getSupplierNo().hashCode());
        result = prime * result + ((getSupplierId() == null) ? 0 : getSupplierId().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getCreateUser() == null) ? 0 : getCreateUser().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getSpPicUrl() == null) ? 0 : getSpPicUrl().hashCode());
        result = prime * result + ((getPicType() == null) ? 0 : getPicType().hashCode());
        result = prime * result + ((getSortValue() == null) ? 0 : getSortValue().hashCode());
        result = prime * result + ((getPicExtension() == null) ? 0 : getPicExtension().hashCode());
        result = prime * result + ((getPicHandleState() == null) ? 0 : getPicHandleState().hashCode());
        result = prime * result + ((getDataState() == null) ? 0 : getDataState().hashCode());
        result = prime * result + ((getVersion() == null) ? 0 : getVersion().hashCode());
        return result;
    }
}