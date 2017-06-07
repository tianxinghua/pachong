package com.shangpin.ephub.data.mysql.slot.appendix.po;

import java.io.Serializable;
import java.util.Date;

public class HubSlotSpuAppendix implements Serializable {
    /**
     * 主键
     */
    private Long spuAppendixId;

    /**
     * slotspu主键
     */
    private Long slotSpuId;

    /**
     * slotspusupplier主键
     */
    private Long slotSpuSupplierId;

    /**
     * slot编号
     */
    private String slotNo;

    /**
     * slotspu编号
     */
    private String slotSpuNo;

    /**
     * 属性定义
     */
    private String attributeDefinition;

    /**
     * 属性值
     */
    private String attributeValue;

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

    public Long getSpuAppendixId() {
        return spuAppendixId;
    }

    public void setSpuAppendixId(Long spuAppendixId) {
        this.spuAppendixId = spuAppendixId;
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

    public String getAttributeDefinition() {
        return attributeDefinition;
    }

    public void setAttributeDefinition(String attributeDefinition) {
        this.attributeDefinition = attributeDefinition == null ? null : attributeDefinition.trim();
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue == null ? null : attributeValue.trim();
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
        sb.append(", spuAppendixId=").append(spuAppendixId);
        sb.append(", slotSpuId=").append(slotSpuId);
        sb.append(", slotSpuSupplierId=").append(slotSpuSupplierId);
        sb.append(", slotNo=").append(slotNo);
        sb.append(", slotSpuNo=").append(slotSpuNo);
        sb.append(", attributeDefinition=").append(attributeDefinition);
        sb.append(", attributeValue=").append(attributeValue);
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
        HubSlotSpuAppendix other = (HubSlotSpuAppendix) that;
        return (this.getSpuAppendixId() == null ? other.getSpuAppendixId() == null : this.getSpuAppendixId().equals(other.getSpuAppendixId()))
            && (this.getSlotSpuId() == null ? other.getSlotSpuId() == null : this.getSlotSpuId().equals(other.getSlotSpuId()))
            && (this.getSlotSpuSupplierId() == null ? other.getSlotSpuSupplierId() == null : this.getSlotSpuSupplierId().equals(other.getSlotSpuSupplierId()))
            && (this.getSlotNo() == null ? other.getSlotNo() == null : this.getSlotNo().equals(other.getSlotNo()))
            && (this.getSlotSpuNo() == null ? other.getSlotSpuNo() == null : this.getSlotSpuNo().equals(other.getSlotSpuNo()))
            && (this.getAttributeDefinition() == null ? other.getAttributeDefinition() == null : this.getAttributeDefinition().equals(other.getAttributeDefinition()))
            && (this.getAttributeValue() == null ? other.getAttributeValue() == null : this.getAttributeValue().equals(other.getAttributeValue()))
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
        result = prime * result + ((getSpuAppendixId() == null) ? 0 : getSpuAppendixId().hashCode());
        result = prime * result + ((getSlotSpuId() == null) ? 0 : getSlotSpuId().hashCode());
        result = prime * result + ((getSlotSpuSupplierId() == null) ? 0 : getSlotSpuSupplierId().hashCode());
        result = prime * result + ((getSlotNo() == null) ? 0 : getSlotNo().hashCode());
        result = prime * result + ((getSlotSpuNo() == null) ? 0 : getSlotSpuNo().hashCode());
        result = prime * result + ((getAttributeDefinition() == null) ? 0 : getAttributeDefinition().hashCode());
        result = prime * result + ((getAttributeValue() == null) ? 0 : getAttributeValue().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getCreateUser() == null) ? 0 : getCreateUser().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getUpdateUser() == null) ? 0 : getUpdateUser().hashCode());
        result = prime * result + ((getDataState() == null) ? 0 : getDataState().hashCode());
        result = prime * result + ((getVersion() == null) ? 0 : getVersion().hashCode());
        return result;
    }
}