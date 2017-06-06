package com.shangpin.studio.data.mysql.po.dic;

import java.io.Serializable;
import java.util.Date;

public class StudioDicSlot implements Serializable {
    private Long studioDicSlotId;

    private Long studioId;

    private Integer slotNumber;

    private Integer slotMinNumber;

    private Integer slotEfficiency;

    private Date createTime;

    private String createUser;

    private Date updateTime;

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

    public Long getStudioDicSlotId() {
        return studioDicSlotId;
    }

    public void setStudioDicSlotId(Long studioDicSlotId) {
        this.studioDicSlotId = studioDicSlotId;
    }

    public Long getStudioId() {
        return studioId;
    }

    public void setStudioId(Long studioId) {
        this.studioId = studioId;
    }

    public Integer getSlotNumber() {
        return slotNumber;
    }

    public void setSlotNumber(Integer slotNumber) {
        this.slotNumber = slotNumber;
    }

    public Integer getSlotMinNumber() {
        return slotMinNumber;
    }

    public void setSlotMinNumber(Integer slotMinNumber) {
        this.slotMinNumber = slotMinNumber;
    }

    public Integer getSlotEfficiency() {
        return slotEfficiency;
    }

    public void setSlotEfficiency(Integer slotEfficiency) {
        this.slotEfficiency = slotEfficiency;
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
        sb.append(", studioDicSlotId=").append(studioDicSlotId);
        sb.append(", studioId=").append(studioId);
        sb.append(", slotNumber=").append(slotNumber);
        sb.append(", slotMinNumber=").append(slotMinNumber);
        sb.append(", slotEfficiency=").append(slotEfficiency);
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
        StudioDicSlot other = (StudioDicSlot) that;
        return (this.getStudioDicSlotId() == null ? other.getStudioDicSlotId() == null : this.getStudioDicSlotId().equals(other.getStudioDicSlotId()))
            && (this.getStudioId() == null ? other.getStudioId() == null : this.getStudioId().equals(other.getStudioId()))
            && (this.getSlotNumber() == null ? other.getSlotNumber() == null : this.getSlotNumber().equals(other.getSlotNumber()))
            && (this.getSlotMinNumber() == null ? other.getSlotMinNumber() == null : this.getSlotMinNumber().equals(other.getSlotMinNumber()))
            && (this.getSlotEfficiency() == null ? other.getSlotEfficiency() == null : this.getSlotEfficiency().equals(other.getSlotEfficiency()))
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
        result = prime * result + ((getStudioDicSlotId() == null) ? 0 : getStudioDicSlotId().hashCode());
        result = prime * result + ((getStudioId() == null) ? 0 : getStudioId().hashCode());
        result = prime * result + ((getSlotNumber() == null) ? 0 : getSlotNumber().hashCode());
        result = prime * result + ((getSlotMinNumber() == null) ? 0 : getSlotMinNumber().hashCode());
        result = prime * result + ((getSlotEfficiency() == null) ? 0 : getSlotEfficiency().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getCreateUser() == null) ? 0 : getCreateUser().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getUpdateUser() == null) ? 0 : getUpdateUser().hashCode());
        result = prime * result + ((getDataState() == null) ? 0 : getDataState().hashCode());
        result = prime * result + ((getVersion() == null) ? 0 : getVersion().hashCode());
        return result;
    }
}