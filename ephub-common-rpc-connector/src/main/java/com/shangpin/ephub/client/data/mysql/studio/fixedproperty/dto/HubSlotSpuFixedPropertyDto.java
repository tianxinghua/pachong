package com.shangpin.ephub.client.data.mysql.studio.fixedproperty.dto;

import java.io.Serializable;
import java.util.Date;

public class HubSlotSpuFixedPropertyDto implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * slotspu主键
     */
    private Long slotSpuId;

    /**
     * slotspusupplier主键
     */
    private Long slotSpuSupplierId;

    /**
     * 供货商SPU主键
     */
    private Long supplierSpuId;

    /**
     * SPUPENDING表主键
     */
    private Long spuPendingId;

    /**
     * slot编号
     */
    private String slotNo;

    /**
     * slotspu编号
     */
    private String slotSpuNo;

    /**
     * barcode
     */
    private String barcode;

    /**
     * 颜色
     */
    private String color;

    /**
     * 材质
     */
    private String material;

    /**
     * 产地
     */
    private String origin;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getSupplierSpuId() {
        return supplierSpuId;
    }

    public void setSupplierSpuId(Long supplierSpuId) {
        this.supplierSpuId = supplierSpuId;
    }

    public Long getSpuPendingId() {
        return spuPendingId;
    }

    public void setSpuPendingId(Long spuPendingId) {
        this.spuPendingId = spuPendingId;
    }

    public String getSlotNo() {
        return slotNo;
    }

    public void setSlotNo(String slotNo) {
        this.slotNo = slotNo == null ? null : slotNo.trim();
    }

    public String getSlotSpuNo() {
        return slotSpuNo;
    }

    public void setSlotSpuNo(String slotSpuNo) {
        this.slotSpuNo = slotSpuNo == null ? null : slotSpuNo.trim();
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode == null ? null : barcode.trim();
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color == null ? null : color.trim();
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material == null ? null : material.trim();
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin == null ? null : origin.trim();
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
        sb.append(", id=").append(id);
        sb.append(", slotSpuId=").append(slotSpuId);
        sb.append(", slotSpuSupplierId=").append(slotSpuSupplierId);
        sb.append(", supplierSpuId=").append(supplierSpuId);
        sb.append(", spuPendingId=").append(spuPendingId);
        sb.append(", slotNo=").append(slotNo);
        sb.append(", slotSpuNo=").append(slotSpuNo);
        sb.append(", barcode=").append(barcode);
        sb.append(", color=").append(color);
        sb.append(", material=").append(material);
        sb.append(", origin=").append(origin);
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
        HubSlotSpuFixedPropertyDto other = (HubSlotSpuFixedPropertyDto) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getSlotSpuId() == null ? other.getSlotSpuId() == null : this.getSlotSpuId().equals(other.getSlotSpuId()))
            && (this.getSlotSpuSupplierId() == null ? other.getSlotSpuSupplierId() == null : this.getSlotSpuSupplierId().equals(other.getSlotSpuSupplierId()))
            && (this.getSupplierSpuId() == null ? other.getSupplierSpuId() == null : this.getSupplierSpuId().equals(other.getSupplierSpuId()))
            && (this.getSpuPendingId() == null ? other.getSpuPendingId() == null : this.getSpuPendingId().equals(other.getSpuPendingId()))
            && (this.getSlotNo() == null ? other.getSlotNo() == null : this.getSlotNo().equals(other.getSlotNo()))
            && (this.getSlotSpuNo() == null ? other.getSlotSpuNo() == null : this.getSlotSpuNo().equals(other.getSlotSpuNo()))
            && (this.getBarcode() == null ? other.getBarcode() == null : this.getBarcode().equals(other.getBarcode()))
            && (this.getColor() == null ? other.getColor() == null : this.getColor().equals(other.getColor()))
            && (this.getMaterial() == null ? other.getMaterial() == null : this.getMaterial().equals(other.getMaterial()))
            && (this.getOrigin() == null ? other.getOrigin() == null : this.getOrigin().equals(other.getOrigin()))
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
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getSlotSpuId() == null) ? 0 : getSlotSpuId().hashCode());
        result = prime * result + ((getSlotSpuSupplierId() == null) ? 0 : getSlotSpuSupplierId().hashCode());
        result = prime * result + ((getSupplierSpuId() == null) ? 0 : getSupplierSpuId().hashCode());
        result = prime * result + ((getSpuPendingId() == null) ? 0 : getSpuPendingId().hashCode());
        result = prime * result + ((getSlotNo() == null) ? 0 : getSlotNo().hashCode());
        result = prime * result + ((getSlotSpuNo() == null) ? 0 : getSlotSpuNo().hashCode());
        result = prime * result + ((getBarcode() == null) ? 0 : getBarcode().hashCode());
        result = prime * result + ((getColor() == null) ? 0 : getColor().hashCode());
        result = prime * result + ((getMaterial() == null) ? 0 : getMaterial().hashCode());
        result = prime * result + ((getOrigin() == null) ? 0 : getOrigin().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getCreateUser() == null) ? 0 : getCreateUser().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getUpdateUser() == null) ? 0 : getUpdateUser().hashCode());
        result = prime * result + ((getDataState() == null) ? 0 : getDataState().hashCode());
        result = prime * result + ((getVersion() == null) ? 0 : getVersion().hashCode());
        return result;
    }
}