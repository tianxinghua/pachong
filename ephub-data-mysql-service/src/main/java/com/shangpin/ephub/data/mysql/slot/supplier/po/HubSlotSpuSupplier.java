package com.shangpin.ephub.data.mysql.slot.supplier.po;

import java.io.Serializable;
import java.util.Date;

public class HubSlotSpuSupplier implements Serializable {
    /**
     * 主键
     */
    private Long slotSpuSupplierId;

    /**
     * slotspu主键
     */
    private Long slotSpuId;

    /**
     * slot编号
     */
    private String slotNo;

    /**
     * slotspuno编号
     */
    private String slotSpuNo;

    /**
     * 供货商编号
     */
    private String supplierNo;

    /**
     * 供货商门户编号
     */
    private String supplierId;

    /**
     * SPUPENDING表主键
     */
    private Long spuPendingId;

    /**
     * 供货商SPU主键
     */
    private Long supplierSpuId;

    /**
     * 0:未寄出 1：已加入发货单  2：已发货
     */
    private Byte state;

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
     * 多家供货标记  0：独家  1：多家
     */
    private Byte repeatMarker;

    /**
     * 供货商操作标记     0：自己寄出    1: 另外的供货商已寄出
     */
    private Byte supplierOperateSign;

    /**
     * 数据状态：1未删除 0已删除
     */
    private Byte dataState;

    /**
     * 版本字段
     */
    private Long version;

    private static final long serialVersionUID = 1L;

    public Long getSlotSpuSupplierId() {
        return slotSpuSupplierId;
    }

    public void setSlotSpuSupplierId(Long slotSpuSupplierId) {
        this.slotSpuSupplierId = slotSpuSupplierId;
    }

    public Long getSlotSpuId() {
        return slotSpuId;
    }

    public void setSlotSpuId(Long slotSpuId) {
        this.slotSpuId = slotSpuId;
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

    public Long getSpuPendingId() {
        return spuPendingId;
    }

    public void setSpuPendingId(Long spuPendingId) {
        this.spuPendingId = spuPendingId;
    }

    public Long getSupplierSpuId() {
        return supplierSpuId;
    }

    public void setSupplierSpuId(Long supplierSpuId) {
        this.supplierSpuId = supplierSpuId;
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
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

    public Byte getRepeatMarker() {
        return repeatMarker;
    }

    public void setRepeatMarker(Byte repeatMarker) {
        this.repeatMarker = repeatMarker;
    }

    public Byte getSupplierOperateSign() {
        return supplierOperateSign;
    }

    public void setSupplierOperateSign(Byte supplierOperateSign) {
        this.supplierOperateSign = supplierOperateSign;
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
        sb.append(", slotSpuSupplierId=").append(slotSpuSupplierId);
        sb.append(", slotSpuId=").append(slotSpuId);
        sb.append(", slotNo=").append(slotNo);
        sb.append(", slotSpuNo=").append(slotSpuNo);
        sb.append(", supplierNo=").append(supplierNo);
        sb.append(", supplierId=").append(supplierId);
        sb.append(", spuPendingId=").append(spuPendingId);
        sb.append(", supplierSpuId=").append(supplierSpuId);
        sb.append(", state=").append(state);
        sb.append(", createTime=").append(createTime);
        sb.append(", createUser=").append(createUser);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", updateUser=").append(updateUser);
        sb.append(", repeatMarker=").append(repeatMarker);
        sb.append(", supplierOperateSign=").append(supplierOperateSign);
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
        HubSlotSpuSupplier other = (HubSlotSpuSupplier) that;
        return (this.getSlotSpuSupplierId() == null ? other.getSlotSpuSupplierId() == null : this.getSlotSpuSupplierId().equals(other.getSlotSpuSupplierId()))
            && (this.getSlotSpuId() == null ? other.getSlotSpuId() == null : this.getSlotSpuId().equals(other.getSlotSpuId()))
            && (this.getSlotNo() == null ? other.getSlotNo() == null : this.getSlotNo().equals(other.getSlotNo()))
            && (this.getSlotSpuNo() == null ? other.getSlotSpuNo() == null : this.getSlotSpuNo().equals(other.getSlotSpuNo()))
            && (this.getSupplierNo() == null ? other.getSupplierNo() == null : this.getSupplierNo().equals(other.getSupplierNo()))
            && (this.getSupplierId() == null ? other.getSupplierId() == null : this.getSupplierId().equals(other.getSupplierId()))
            && (this.getSpuPendingId() == null ? other.getSpuPendingId() == null : this.getSpuPendingId().equals(other.getSpuPendingId()))
            && (this.getSupplierSpuId() == null ? other.getSupplierSpuId() == null : this.getSupplierSpuId().equals(other.getSupplierSpuId()))
            && (this.getState() == null ? other.getState() == null : this.getState().equals(other.getState()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getCreateUser() == null ? other.getCreateUser() == null : this.getCreateUser().equals(other.getCreateUser()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getUpdateUser() == null ? other.getUpdateUser() == null : this.getUpdateUser().equals(other.getUpdateUser()))
            && (this.getRepeatMarker() == null ? other.getRepeatMarker() == null : this.getRepeatMarker().equals(other.getRepeatMarker()))
            && (this.getSupplierOperateSign() == null ? other.getSupplierOperateSign() == null : this.getSupplierOperateSign().equals(other.getSupplierOperateSign()))
            && (this.getDataState() == null ? other.getDataState() == null : this.getDataState().equals(other.getDataState()))
            && (this.getVersion() == null ? other.getVersion() == null : this.getVersion().equals(other.getVersion()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getSlotSpuSupplierId() == null) ? 0 : getSlotSpuSupplierId().hashCode());
        result = prime * result + ((getSlotSpuId() == null) ? 0 : getSlotSpuId().hashCode());
        result = prime * result + ((getSlotNo() == null) ? 0 : getSlotNo().hashCode());
        result = prime * result + ((getSlotSpuNo() == null) ? 0 : getSlotSpuNo().hashCode());
        result = prime * result + ((getSupplierNo() == null) ? 0 : getSupplierNo().hashCode());
        result = prime * result + ((getSupplierId() == null) ? 0 : getSupplierId().hashCode());
        result = prime * result + ((getSpuPendingId() == null) ? 0 : getSpuPendingId().hashCode());
        result = prime * result + ((getSupplierSpuId() == null) ? 0 : getSupplierSpuId().hashCode());
        result = prime * result + ((getState() == null) ? 0 : getState().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getCreateUser() == null) ? 0 : getCreateUser().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getUpdateUser() == null) ? 0 : getUpdateUser().hashCode());
        result = prime * result + ((getRepeatMarker() == null) ? 0 : getRepeatMarker().hashCode());
        result = prime * result + ((getSupplierOperateSign() == null) ? 0 : getSupplierOperateSign().hashCode());
        result = prime * result + ((getDataState() == null) ? 0 : getDataState().hashCode());
        result = prime * result + ((getVersion() == null) ? 0 : getVersion().hashCode());
        return result;
    }
}