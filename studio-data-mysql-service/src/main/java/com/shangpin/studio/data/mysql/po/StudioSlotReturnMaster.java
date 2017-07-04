package com.shangpin.studio.data.mysql.po;

import java.io.Serializable;
import java.util.Date;

public class StudioSlotReturnMaster implements Serializable {
    /**
     * 主键
     */
    private Long studioSlotReturnMasterId;

    /**
     * 发货单号
     */
    private String studioSendNo;

    private String supplierNo;

    private String supplierId;

    private Long studioId;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 实际发货数量
     */
    private Integer actualSendQuantity;

    private Integer missingQuantity;

    private Integer damagedQuantity;

    /**
     * 实际收货数量
     */
    private Integer actualQuantity;

    /**
     * 物流编号
     */
    private String trackNo;

    /**
     * 0:创建未发货 1：已发货 2：已签收 3：已验收
     */
    private Byte state;

    /**
     * 备注
     */
    private String memo;

    /**
     * 发货人
     */
    private String sendUser;

    /**
     * 发货时间
     */
    private Date sendTime;

    /**
     * 0：待发货  1:供货商发货  
     */
    private Byte sendState;

    /**
     * 到货收货人
     */
    private String arriveUser;

    /**
     * 到货时间
     */
    private Date arriveTime;

    /**
     * 0: 未收货  1:已收货   2：已验货
     */
    private Byte arriveState;

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

    public Long getStudioSlotReturnMasterId() {
        return studioSlotReturnMasterId;
    }

    public void setStudioSlotReturnMasterId(Long studioSlotReturnMasterId) {
        this.studioSlotReturnMasterId = studioSlotReturnMasterId;
    }

    public String getStudioSendNo() {
        return studioSendNo;
    }

    public void setStudioSendNo(String studioSendNo) {
        this.studioSendNo = studioSendNo == null ? null : studioSendNo.trim();
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

    public Long getStudioId() {
        return studioId;
    }

    public void setStudioId(Long studioId) {
        this.studioId = studioId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getActualSendQuantity() {
        return actualSendQuantity;
    }

    public void setActualSendQuantity(Integer actualSendQuantity) {
        this.actualSendQuantity = actualSendQuantity;
    }

    public Integer getMissingQuantity() {
        return missingQuantity;
    }

    public void setMissingQuantity(Integer missingQuantity) {
        this.missingQuantity = missingQuantity;
    }

    public Integer getDamagedQuantity() {
        return damagedQuantity;
    }

    public void setDamagedQuantity(Integer damagedQuantity) {
        this.damagedQuantity = damagedQuantity;
    }

    public Integer getActualQuantity() {
        return actualQuantity;
    }

    public void setActualQuantity(Integer actualQuantity) {
        this.actualQuantity = actualQuantity;
    }

    public String getTrackNo() {
        return trackNo;
    }

    public void setTrackNo(String trackNo) {
        this.trackNo = trackNo == null ? null : trackNo.trim();
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    public String getSendUser() {
        return sendUser;
    }

    public void setSendUser(String sendUser) {
        this.sendUser = sendUser == null ? null : sendUser.trim();
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Byte getSendState() {
        return sendState;
    }

    public void setSendState(Byte sendState) {
        this.sendState = sendState;
    }

    public String getArriveUser() {
        return arriveUser;
    }

    public void setArriveUser(String arriveUser) {
        this.arriveUser = arriveUser == null ? null : arriveUser.trim();
    }

    public Date getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(Date arriveTime) {
        this.arriveTime = arriveTime;
    }

    public Byte getArriveState() {
        return arriveState;
    }

    public void setArriveState(Byte arriveState) {
        this.arriveState = arriveState;
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
        sb.append(", studioSlotReturnMasterId=").append(studioSlotReturnMasterId);
        sb.append(", studioSendNo=").append(studioSendNo);
        sb.append(", supplierNo=").append(supplierNo);
        sb.append(", supplierId=").append(supplierId);
        sb.append(", studioId=").append(studioId);
        sb.append(", quantity=").append(quantity);
        sb.append(", actualSendQuantity=").append(actualSendQuantity);
        sb.append(", missingQuantity=").append(missingQuantity);
        sb.append(", damagedQuantity=").append(damagedQuantity);
        sb.append(", actualQuantity=").append(actualQuantity);
        sb.append(", trackNo=").append(trackNo);
        sb.append(", state=").append(state);
        sb.append(", memo=").append(memo);
        sb.append(", sendUser=").append(sendUser);
        sb.append(", sendTime=").append(sendTime);
        sb.append(", sendState=").append(sendState);
        sb.append(", arriveUser=").append(arriveUser);
        sb.append(", arriveTime=").append(arriveTime);
        sb.append(", arriveState=").append(arriveState);
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
        StudioSlotReturnMaster other = (StudioSlotReturnMaster) that;
        return (this.getStudioSlotReturnMasterId() == null ? other.getStudioSlotReturnMasterId() == null : this.getStudioSlotReturnMasterId().equals(other.getStudioSlotReturnMasterId()))
            && (this.getStudioSendNo() == null ? other.getStudioSendNo() == null : this.getStudioSendNo().equals(other.getStudioSendNo()))
            && (this.getSupplierNo() == null ? other.getSupplierNo() == null : this.getSupplierNo().equals(other.getSupplierNo()))
            && (this.getSupplierId() == null ? other.getSupplierId() == null : this.getSupplierId().equals(other.getSupplierId()))
            && (this.getStudioId() == null ? other.getStudioId() == null : this.getStudioId().equals(other.getStudioId()))
            && (this.getQuantity() == null ? other.getQuantity() == null : this.getQuantity().equals(other.getQuantity()))
            && (this.getActualSendQuantity() == null ? other.getActualSendQuantity() == null : this.getActualSendQuantity().equals(other.getActualSendQuantity()))
            && (this.getMissingQuantity() == null ? other.getMissingQuantity() == null : this.getMissingQuantity().equals(other.getMissingQuantity()))
            && (this.getDamagedQuantity() == null ? other.getDamagedQuantity() == null : this.getDamagedQuantity().equals(other.getDamagedQuantity()))
            && (this.getActualQuantity() == null ? other.getActualQuantity() == null : this.getActualQuantity().equals(other.getActualQuantity()))
            && (this.getTrackNo() == null ? other.getTrackNo() == null : this.getTrackNo().equals(other.getTrackNo()))
            && (this.getState() == null ? other.getState() == null : this.getState().equals(other.getState()))
            && (this.getMemo() == null ? other.getMemo() == null : this.getMemo().equals(other.getMemo()))
            && (this.getSendUser() == null ? other.getSendUser() == null : this.getSendUser().equals(other.getSendUser()))
            && (this.getSendTime() == null ? other.getSendTime() == null : this.getSendTime().equals(other.getSendTime()))
            && (this.getSendState() == null ? other.getSendState() == null : this.getSendState().equals(other.getSendState()))
            && (this.getArriveUser() == null ? other.getArriveUser() == null : this.getArriveUser().equals(other.getArriveUser()))
            && (this.getArriveTime() == null ? other.getArriveTime() == null : this.getArriveTime().equals(other.getArriveTime()))
            && (this.getArriveState() == null ? other.getArriveState() == null : this.getArriveState().equals(other.getArriveState()))
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
        result = prime * result + ((getStudioSlotReturnMasterId() == null) ? 0 : getStudioSlotReturnMasterId().hashCode());
        result = prime * result + ((getStudioSendNo() == null) ? 0 : getStudioSendNo().hashCode());
        result = prime * result + ((getSupplierNo() == null) ? 0 : getSupplierNo().hashCode());
        result = prime * result + ((getSupplierId() == null) ? 0 : getSupplierId().hashCode());
        result = prime * result + ((getStudioId() == null) ? 0 : getStudioId().hashCode());
        result = prime * result + ((getQuantity() == null) ? 0 : getQuantity().hashCode());
        result = prime * result + ((getActualSendQuantity() == null) ? 0 : getActualSendQuantity().hashCode());
        result = prime * result + ((getMissingQuantity() == null) ? 0 : getMissingQuantity().hashCode());
        result = prime * result + ((getDamagedQuantity() == null) ? 0 : getDamagedQuantity().hashCode());
        result = prime * result + ((getActualQuantity() == null) ? 0 : getActualQuantity().hashCode());
        result = prime * result + ((getTrackNo() == null) ? 0 : getTrackNo().hashCode());
        result = prime * result + ((getState() == null) ? 0 : getState().hashCode());
        result = prime * result + ((getMemo() == null) ? 0 : getMemo().hashCode());
        result = prime * result + ((getSendUser() == null) ? 0 : getSendUser().hashCode());
        result = prime * result + ((getSendTime() == null) ? 0 : getSendTime().hashCode());
        result = prime * result + ((getSendState() == null) ? 0 : getSendState().hashCode());
        result = prime * result + ((getArriveUser() == null) ? 0 : getArriveUser().hashCode());
        result = prime * result + ((getArriveTime() == null) ? 0 : getArriveTime().hashCode());
        result = prime * result + ((getArriveState() == null) ? 0 : getArriveState().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getCreateUser() == null) ? 0 : getCreateUser().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getUpdateUser() == null) ? 0 : getUpdateUser().hashCode());
        result = prime * result + ((getDataState() == null) ? 0 : getDataState().hashCode());
        result = prime * result + ((getVersion() == null) ? 0 : getVersion().hashCode());
        return result;
    }
}