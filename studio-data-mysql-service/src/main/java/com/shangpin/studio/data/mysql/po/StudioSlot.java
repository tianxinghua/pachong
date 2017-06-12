package com.shangpin.studio.data.mysql.po;

import java.io.Serializable;
import java.util.Date;

public class StudioSlot implements Serializable {
    /**
     * 主键
     */
    private Long studioSlotId;

    /**
     * 摄影棚Id
     */
    private Long studioId;

    /**
     * 批次号
     */
    private String slotNo;

    /**
     * 批次状态
     */
    private Byte slotStatus;

    /**
     * 申请状态  0:未申请  1:已申请
     */
    private Byte applyStatus;

    /**
     * 申请方(supplierId)
     */
    private String applySupplierId;

    /**
     * 申请人
     */
    private String applyUser;

    /**
     * 申请时间
     */
    private Date applyTime;

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
     * 预计到货时间
     */
    private Date planArriveTime;

    /**
     * 到货状态 0:未到货 1:已到货
     */
    private Byte arriveStatus;

    /**
     * 到货日期
     */
    private Date arriveTime;

    /**
     * 到货签收人
     */
    private String arriveUser;

    /**
     * 拍摄状态 0:未拍摄 1:已拍摄
     */
    private Byte shotStatus;

    /**
     * 计划拍摄日期
     */
    private Date planShootTime;

    /**
     * 拍摄日期
     */
    private Date shootTime;

    /**
     * 物流编号
     */
    private String trackNo;

    private String originSlotNo;

    private String memo;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 数据状态：1未删除 0已删除
     */
    private Byte dataState;

    /**
     * 版本字段
     */
    private Long version;

    private static final long serialVersionUID = 1L;

    public Long getStudioSlotId() {
        return studioSlotId;
    }

    public void setStudioSlotId(Long studioSlotId) {
        this.studioSlotId = studioSlotId;
    }

    public Long getStudioId() {
        return studioId;
    }

    public void setStudioId(Long studioId) {
        this.studioId = studioId;
    }

    public String getSlotNo() {
        return slotNo;
    }

    public void setSlotNo(String slotNo) {
        this.slotNo = slotNo == null ? null : slotNo.trim();
    }

    public Byte getSlotStatus() {
        return slotStatus;
    }

    public void setSlotStatus(Byte slotStatus) {
        this.slotStatus = slotStatus;
    }

    public Byte getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(Byte applyStatus) {
        this.applyStatus = applyStatus;
    }

    public String getApplySupplierId() {
        return applySupplierId;
    }

    public void setApplySupplierId(String applySupplierId) {
        this.applySupplierId = applySupplierId == null ? null : applySupplierId.trim();
    }

    public String getApplyUser() {
        return applyUser;
    }

    public void setApplyUser(String applyUser) {
        this.applyUser = applyUser == null ? null : applyUser.trim();
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
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

    public Date getPlanArriveTime() {
        return planArriveTime;
    }

    public void setPlanArriveTime(Date planArriveTime) {
        this.planArriveTime = planArriveTime;
    }

    public Byte getArriveStatus() {
        return arriveStatus;
    }

    public void setArriveStatus(Byte arriveStatus) {
        this.arriveStatus = arriveStatus;
    }

    public Date getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(Date arriveTime) {
        this.arriveTime = arriveTime;
    }

    public String getArriveUser() {
        return arriveUser;
    }

    public void setArriveUser(String arriveUser) {
        this.arriveUser = arriveUser == null ? null : arriveUser.trim();
    }

    public Byte getShotStatus() {
        return shotStatus;
    }

    public void setShotStatus(Byte shotStatus) {
        this.shotStatus = shotStatus;
    }

    public Date getPlanShootTime() {
        return planShootTime;
    }

    public void setPlanShootTime(Date planShootTime) {
        this.planShootTime = planShootTime;
    }

    public Date getShootTime() {
        return shootTime;
    }

    public void setShootTime(Date shootTime) {
        this.shootTime = shootTime;
    }

    public String getTrackNo() {
        return trackNo;
    }

    public void setTrackNo(String trackNo) {
        this.trackNo = trackNo == null ? null : trackNo.trim();
    }

    public String getOriginSlotNo() {
        return originSlotNo;
    }

    public void setOriginSlotNo(String originSlotNo) {
        this.originSlotNo = originSlotNo == null ? null : originSlotNo.trim();
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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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
        sb.append(", studioSlotId=").append(studioSlotId);
        sb.append(", studioId=").append(studioId);
        sb.append(", slotNo=").append(slotNo);
        sb.append(", slotStatus=").append(slotStatus);
        sb.append(", applyStatus=").append(applyStatus);
        sb.append(", applySupplierId=").append(applySupplierId);
        sb.append(", applyUser=").append(applyUser);
        sb.append(", applyTime=").append(applyTime);
        sb.append(", sendUser=").append(sendUser);
        sb.append(", sendTime=").append(sendTime);
        sb.append(", sendState=").append(sendState);
        sb.append(", planArriveTime=").append(planArriveTime);
        sb.append(", arriveStatus=").append(arriveStatus);
        sb.append(", arriveTime=").append(arriveTime);
        sb.append(", arriveUser=").append(arriveUser);
        sb.append(", shotStatus=").append(shotStatus);
        sb.append(", planShootTime=").append(planShootTime);
        sb.append(", shootTime=").append(shootTime);
        sb.append(", trackNo=").append(trackNo);
        sb.append(", originSlotNo=").append(originSlotNo);
        sb.append(", memo=").append(memo);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
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
        StudioSlot other = (StudioSlot) that;
        return (this.getStudioSlotId() == null ? other.getStudioSlotId() == null : this.getStudioSlotId().equals(other.getStudioSlotId()))
            && (this.getStudioId() == null ? other.getStudioId() == null : this.getStudioId().equals(other.getStudioId()))
            && (this.getSlotNo() == null ? other.getSlotNo() == null : this.getSlotNo().equals(other.getSlotNo()))
            && (this.getSlotStatus() == null ? other.getSlotStatus() == null : this.getSlotStatus().equals(other.getSlotStatus()))
            && (this.getApplyStatus() == null ? other.getApplyStatus() == null : this.getApplyStatus().equals(other.getApplyStatus()))
            && (this.getApplySupplierId() == null ? other.getApplySupplierId() == null : this.getApplySupplierId().equals(other.getApplySupplierId()))
            && (this.getApplyUser() == null ? other.getApplyUser() == null : this.getApplyUser().equals(other.getApplyUser()))
            && (this.getApplyTime() == null ? other.getApplyTime() == null : this.getApplyTime().equals(other.getApplyTime()))
            && (this.getSendUser() == null ? other.getSendUser() == null : this.getSendUser().equals(other.getSendUser()))
            && (this.getSendTime() == null ? other.getSendTime() == null : this.getSendTime().equals(other.getSendTime()))
            && (this.getSendState() == null ? other.getSendState() == null : this.getSendState().equals(other.getSendState()))
            && (this.getPlanArriveTime() == null ? other.getPlanArriveTime() == null : this.getPlanArriveTime().equals(other.getPlanArriveTime()))
            && (this.getArriveStatus() == null ? other.getArriveStatus() == null : this.getArriveStatus().equals(other.getArriveStatus()))
            && (this.getArriveTime() == null ? other.getArriveTime() == null : this.getArriveTime().equals(other.getArriveTime()))
            && (this.getArriveUser() == null ? other.getArriveUser() == null : this.getArriveUser().equals(other.getArriveUser()))
            && (this.getShotStatus() == null ? other.getShotStatus() == null : this.getShotStatus().equals(other.getShotStatus()))
            && (this.getPlanShootTime() == null ? other.getPlanShootTime() == null : this.getPlanShootTime().equals(other.getPlanShootTime()))
            && (this.getShootTime() == null ? other.getShootTime() == null : this.getShootTime().equals(other.getShootTime()))
            && (this.getTrackNo() == null ? other.getTrackNo() == null : this.getTrackNo().equals(other.getTrackNo()))
            && (this.getOriginSlotNo() == null ? other.getOriginSlotNo() == null : this.getOriginSlotNo().equals(other.getOriginSlotNo()))
            && (this.getMemo() == null ? other.getMemo() == null : this.getMemo().equals(other.getMemo()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getDataState() == null ? other.getDataState() == null : this.getDataState().equals(other.getDataState()))
            && (this.getVersion() == null ? other.getVersion() == null : this.getVersion().equals(other.getVersion()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getStudioSlotId() == null) ? 0 : getStudioSlotId().hashCode());
        result = prime * result + ((getStudioId() == null) ? 0 : getStudioId().hashCode());
        result = prime * result + ((getSlotNo() == null) ? 0 : getSlotNo().hashCode());
        result = prime * result + ((getSlotStatus() == null) ? 0 : getSlotStatus().hashCode());
        result = prime * result + ((getApplyStatus() == null) ? 0 : getApplyStatus().hashCode());
        result = prime * result + ((getApplySupplierId() == null) ? 0 : getApplySupplierId().hashCode());
        result = prime * result + ((getApplyUser() == null) ? 0 : getApplyUser().hashCode());
        result = prime * result + ((getApplyTime() == null) ? 0 : getApplyTime().hashCode());
        result = prime * result + ((getSendUser() == null) ? 0 : getSendUser().hashCode());
        result = prime * result + ((getSendTime() == null) ? 0 : getSendTime().hashCode());
        result = prime * result + ((getSendState() == null) ? 0 : getSendState().hashCode());
        result = prime * result + ((getPlanArriveTime() == null) ? 0 : getPlanArriveTime().hashCode());
        result = prime * result + ((getArriveStatus() == null) ? 0 : getArriveStatus().hashCode());
        result = prime * result + ((getArriveTime() == null) ? 0 : getArriveTime().hashCode());
        result = prime * result + ((getArriveUser() == null) ? 0 : getArriveUser().hashCode());
        result = prime * result + ((getShotStatus() == null) ? 0 : getShotStatus().hashCode());
        result = prime * result + ((getPlanShootTime() == null) ? 0 : getPlanShootTime().hashCode());
        result = prime * result + ((getShootTime() == null) ? 0 : getShootTime().hashCode());
        result = prime * result + ((getTrackNo() == null) ? 0 : getTrackNo().hashCode());
        result = prime * result + ((getOriginSlotNo() == null) ? 0 : getOriginSlotNo().hashCode());
        result = prime * result + ((getMemo() == null) ? 0 : getMemo().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getDataState() == null) ? 0 : getDataState().hashCode());
        result = prime * result + ((getVersion() == null) ? 0 : getVersion().hashCode());
        return result;
    }
}