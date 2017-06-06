package com.shangpin.studio.data.mysql.po;

import java.io.Serializable;
import java.util.Date;

public class StudioMatchSpu implements Serializable {
    /**
     * 主键
     */
    private Long studioMatchSpuId;

    /**
     * slot编号
     */
    private String slotNo;

    /**
     * 物流编号
     */
    private String trackNo;

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
     * slotspu编号
     */
    private String slotSpuNo;

    /**
     * 0:盘盈 1：盘亏
     */
    private Byte type;

    /**
     * 状态
     */
    private Byte state;

    /**
     * 产品名称
     */
    private String supplierSpuName;

    /**
     * 货号
     */
    private String supplierSpuModel;

    /**
     * 品牌
     */
    private String supplierBrandName;

    /**
     * 品类
     */
    private String supplierCategoryName;

    /**
     * 季节
     */
    private String supplierSeasonName;

    /**
     * 备注
     */
    private String memo;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 创建时间
     */
    private Date createTime;

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

    public Long getStudioMatchSpuId() {
        return studioMatchSpuId;
    }

    public void setStudioMatchSpuId(Long studioMatchSpuId) {
        this.studioMatchSpuId = studioMatchSpuId;
    }

    public String getSlotNo() {
        return slotNo;
    }

    public void setSlotNo(String slotNo) {
        this.slotNo = slotNo == null ? null : slotNo.trim();
    }

    public String getTrackNo() {
        return trackNo;
    }

    public void setTrackNo(String trackNo) {
        this.trackNo = trackNo == null ? null : trackNo.trim();
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

    public String getSlotSpuNo() {
        return slotSpuNo;
    }

    public void setSlotSpuNo(String slotSpuNo) {
        this.slotSpuNo = slotSpuNo == null ? null : slotSpuNo.trim();
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public String getSupplierSpuName() {
        return supplierSpuName;
    }

    public void setSupplierSpuName(String supplierSpuName) {
        this.supplierSpuName = supplierSpuName == null ? null : supplierSpuName.trim();
    }

    public String getSupplierSpuModel() {
        return supplierSpuModel;
    }

    public void setSupplierSpuModel(String supplierSpuModel) {
        this.supplierSpuModel = supplierSpuModel == null ? null : supplierSpuModel.trim();
    }

    public String getSupplierBrandName() {
        return supplierBrandName;
    }

    public void setSupplierBrandName(String supplierBrandName) {
        this.supplierBrandName = supplierBrandName == null ? null : supplierBrandName.trim();
    }

    public String getSupplierCategoryName() {
        return supplierCategoryName;
    }

    public void setSupplierCategoryName(String supplierCategoryName) {
        this.supplierCategoryName = supplierCategoryName == null ? null : supplierCategoryName.trim();
    }

    public String getSupplierSeasonName() {
        return supplierSeasonName;
    }

    public void setSupplierSeasonName(String supplierSeasonName) {
        this.supplierSeasonName = supplierSeasonName == null ? null : supplierSeasonName.trim();
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
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
        sb.append(", studioMatchSpuId=").append(studioMatchSpuId);
        sb.append(", slotNo=").append(slotNo);
        sb.append(", trackNo=").append(trackNo);
        sb.append(", supplierNo=").append(supplierNo);
        sb.append(", supplierId=").append(supplierId);
        sb.append(", spuPendingId=").append(spuPendingId);
        sb.append(", supplierSpuId=").append(supplierSpuId);
        sb.append(", slotSpuNo=").append(slotSpuNo);
        sb.append(", type=").append(type);
        sb.append(", state=").append(state);
        sb.append(", supplierSpuName=").append(supplierSpuName);
        sb.append(", supplierSpuModel=").append(supplierSpuModel);
        sb.append(", supplierBrandName=").append(supplierBrandName);
        sb.append(", supplierCategoryName=").append(supplierCategoryName);
        sb.append(", supplierSeasonName=").append(supplierSeasonName);
        sb.append(", memo=").append(memo);
        sb.append(", createUser=").append(createUser);
        sb.append(", createTime=").append(createTime);
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
        StudioMatchSpu other = (StudioMatchSpu) that;
        return (this.getStudioMatchSpuId() == null ? other.getStudioMatchSpuId() == null : this.getStudioMatchSpuId().equals(other.getStudioMatchSpuId()))
            && (this.getSlotNo() == null ? other.getSlotNo() == null : this.getSlotNo().equals(other.getSlotNo()))
            && (this.getTrackNo() == null ? other.getTrackNo() == null : this.getTrackNo().equals(other.getTrackNo()))
            && (this.getSupplierNo() == null ? other.getSupplierNo() == null : this.getSupplierNo().equals(other.getSupplierNo()))
            && (this.getSupplierId() == null ? other.getSupplierId() == null : this.getSupplierId().equals(other.getSupplierId()))
            && (this.getSpuPendingId() == null ? other.getSpuPendingId() == null : this.getSpuPendingId().equals(other.getSpuPendingId()))
            && (this.getSupplierSpuId() == null ? other.getSupplierSpuId() == null : this.getSupplierSpuId().equals(other.getSupplierSpuId()))
            && (this.getSlotSpuNo() == null ? other.getSlotSpuNo() == null : this.getSlotSpuNo().equals(other.getSlotSpuNo()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getState() == null ? other.getState() == null : this.getState().equals(other.getState()))
            && (this.getSupplierSpuName() == null ? other.getSupplierSpuName() == null : this.getSupplierSpuName().equals(other.getSupplierSpuName()))
            && (this.getSupplierSpuModel() == null ? other.getSupplierSpuModel() == null : this.getSupplierSpuModel().equals(other.getSupplierSpuModel()))
            && (this.getSupplierBrandName() == null ? other.getSupplierBrandName() == null : this.getSupplierBrandName().equals(other.getSupplierBrandName()))
            && (this.getSupplierCategoryName() == null ? other.getSupplierCategoryName() == null : this.getSupplierCategoryName().equals(other.getSupplierCategoryName()))
            && (this.getSupplierSeasonName() == null ? other.getSupplierSeasonName() == null : this.getSupplierSeasonName().equals(other.getSupplierSeasonName()))
            && (this.getMemo() == null ? other.getMemo() == null : this.getMemo().equals(other.getMemo()))
            && (this.getCreateUser() == null ? other.getCreateUser() == null : this.getCreateUser().equals(other.getCreateUser()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getUpdateUser() == null ? other.getUpdateUser() == null : this.getUpdateUser().equals(other.getUpdateUser()))
            && (this.getDataState() == null ? other.getDataState() == null : this.getDataState().equals(other.getDataState()))
            && (this.getVersion() == null ? other.getVersion() == null : this.getVersion().equals(other.getVersion()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getStudioMatchSpuId() == null) ? 0 : getStudioMatchSpuId().hashCode());
        result = prime * result + ((getSlotNo() == null) ? 0 : getSlotNo().hashCode());
        result = prime * result + ((getTrackNo() == null) ? 0 : getTrackNo().hashCode());
        result = prime * result + ((getSupplierNo() == null) ? 0 : getSupplierNo().hashCode());
        result = prime * result + ((getSupplierId() == null) ? 0 : getSupplierId().hashCode());
        result = prime * result + ((getSpuPendingId() == null) ? 0 : getSpuPendingId().hashCode());
        result = prime * result + ((getSupplierSpuId() == null) ? 0 : getSupplierSpuId().hashCode());
        result = prime * result + ((getSlotSpuNo() == null) ? 0 : getSlotSpuNo().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getState() == null) ? 0 : getState().hashCode());
        result = prime * result + ((getSupplierSpuName() == null) ? 0 : getSupplierSpuName().hashCode());
        result = prime * result + ((getSupplierSpuModel() == null) ? 0 : getSupplierSpuModel().hashCode());
        result = prime * result + ((getSupplierBrandName() == null) ? 0 : getSupplierBrandName().hashCode());
        result = prime * result + ((getSupplierCategoryName() == null) ? 0 : getSupplierCategoryName().hashCode());
        result = prime * result + ((getSupplierSeasonName() == null) ? 0 : getSupplierSeasonName().hashCode());
        result = prime * result + ((getMemo() == null) ? 0 : getMemo().hashCode());
        result = prime * result + ((getCreateUser() == null) ? 0 : getCreateUser().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getUpdateUser() == null) ? 0 : getUpdateUser().hashCode());
        result = prime * result + ((getDataState() == null) ? 0 : getDataState().hashCode());
        result = prime * result + ((getVersion() == null) ? 0 : getVersion().hashCode());
        return result;
    }
}