package com.shangpin.ephub.client.data.studio.slot.logistic.dto;

import java.io.Serializable;
import java.util.Date;

public class StudioSlotLogistictTrackDto implements Serializable {
    /**
     * 主键
     */
    private Long studioSlotLogistictTrackId;

    /**
     * 物流公司
     */
    private String trackName;

    /**
     * 物流编号
     */
    private String trackNo;

    /**
     * 物流状态
     */
    private Byte trackStatus;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 实际收货数量
     */
    private Integer actualNumber;

    /**
     * 预计到货时间
     */
    private Date planArriveTime;

    /**
     * 签收人
     */
    private String trackReceiver;

    /**
     * 0:从供货商发往摄影棚  1： 从摄影棚发往供货商
     */
    private Byte type;

    private Long sendMasterId;

    /**
     * 备注
     */
    private String memo;

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

    public Long getStudioSlotLogistictTrackId() {
        return studioSlotLogistictTrackId;
    }

    public void setStudioSlotLogistictTrackId(Long studioSlotLogistictTrackId) {
        this.studioSlotLogistictTrackId = studioSlotLogistictTrackId;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName == null ? null : trackName.trim();
    }

    public String getTrackNo() {
        return trackNo;
    }

    public void setTrackNo(String trackNo) {
        this.trackNo = trackNo == null ? null : trackNo.trim();
    }

    public Byte getTrackStatus() {
        return trackStatus;
    }

    public void setTrackStatus(Byte trackStatus) {
        this.trackStatus = trackStatus;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getActualNumber() {
        return actualNumber;
    }

    public void setActualNumber(Integer actualNumber) {
        this.actualNumber = actualNumber;
    }

    public Date getPlanArriveTime() {
        return planArriveTime;
    }

    public void setPlanArriveTime(Date planArriveTime) {
        this.planArriveTime = planArriveTime;
    }

    public String getTrackReceiver() {
        return trackReceiver;
    }

    public void setTrackReceiver(String trackReceiver) {
        this.trackReceiver = trackReceiver == null ? null : trackReceiver.trim();
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Long getSendMasterId() {
        return sendMasterId;
    }

    public void setSendMasterId(Long sendMasterId) {
        this.sendMasterId = sendMasterId;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
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
        sb.append(", studioSlotLogistictTrackId=").append(studioSlotLogistictTrackId);
        sb.append(", trackName=").append(trackName);
        sb.append(", trackNo=").append(trackNo);
        sb.append(", trackStatus=").append(trackStatus);
        sb.append(", quantity=").append(quantity);
        sb.append(", actualNumber=").append(actualNumber);
        sb.append(", planArriveTime=").append(planArriveTime);
        sb.append(", trackReceiver=").append(trackReceiver);
        sb.append(", type=").append(type);
        sb.append(", sendMasterId=").append(sendMasterId);
        sb.append(", memo=").append(memo);
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
        StudioSlotLogistictTrackDto other = (StudioSlotLogistictTrackDto) that;
        return (this.getStudioSlotLogistictTrackId() == null ? other.getStudioSlotLogistictTrackId() == null : this.getStudioSlotLogistictTrackId().equals(other.getStudioSlotLogistictTrackId()))
            && (this.getTrackName() == null ? other.getTrackName() == null : this.getTrackName().equals(other.getTrackName()))
            && (this.getTrackNo() == null ? other.getTrackNo() == null : this.getTrackNo().equals(other.getTrackNo()))
            && (this.getTrackStatus() == null ? other.getTrackStatus() == null : this.getTrackStatus().equals(other.getTrackStatus()))
            && (this.getQuantity() == null ? other.getQuantity() == null : this.getQuantity().equals(other.getQuantity()))
            && (this.getActualNumber() == null ? other.getActualNumber() == null : this.getActualNumber().equals(other.getActualNumber()))
            && (this.getPlanArriveTime() == null ? other.getPlanArriveTime() == null : this.getPlanArriveTime().equals(other.getPlanArriveTime()))
            && (this.getTrackReceiver() == null ? other.getTrackReceiver() == null : this.getTrackReceiver().equals(other.getTrackReceiver()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getSendMasterId() == null ? other.getSendMasterId() == null : this.getSendMasterId().equals(other.getSendMasterId()))
            && (this.getMemo() == null ? other.getMemo() == null : this.getMemo().equals(other.getMemo()))
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
        result = prime * result + ((getStudioSlotLogistictTrackId() == null) ? 0 : getStudioSlotLogistictTrackId().hashCode());
        result = prime * result + ((getTrackName() == null) ? 0 : getTrackName().hashCode());
        result = prime * result + ((getTrackNo() == null) ? 0 : getTrackNo().hashCode());
        result = prime * result + ((getTrackStatus() == null) ? 0 : getTrackStatus().hashCode());
        result = prime * result + ((getQuantity() == null) ? 0 : getQuantity().hashCode());
        result = prime * result + ((getActualNumber() == null) ? 0 : getActualNumber().hashCode());
        result = prime * result + ((getPlanArriveTime() == null) ? 0 : getPlanArriveTime().hashCode());
        result = prime * result + ((getTrackReceiver() == null) ? 0 : getTrackReceiver().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getSendMasterId() == null) ? 0 : getSendMasterId().hashCode());
        result = prime * result + ((getMemo() == null) ? 0 : getMemo().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getCreateUser() == null) ? 0 : getCreateUser().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getUpdateUser() == null) ? 0 : getUpdateUser().hashCode());
        result = prime * result + ((getDataState() == null) ? 0 : getDataState().hashCode());
        result = prime * result + ((getVersion() == null) ? 0 : getVersion().hashCode());
        return result;
    }
}