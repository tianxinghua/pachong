package com.shangpin.ephub.client.data.mysql.sp.dto;

import java.io.Serializable;
import java.util.Date;

public class SpShippingPolicyDto implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 策略名称
     */
    private String policyName;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 月日期 (1-31) 之间
     */
    private Short dayNum;

    /**
     * 0：准确时间 1:按月
     */
    private Byte dateType;

    /**
     * 0：全部商品 1：按规则 2：按商品 3：按活动
     */
    private Byte policyType;

    /**
     * 0: 基础运费 1：免运费
     */
    private Byte regularMembers;

    /**
     * 0: 基础运费 1：免运费
     */
    private Byte goldMembers;

    /**
     * 0: 基础运费 1：免运费
     */
    private Byte supremeMembers;

    /**
     * 0: 基础运费 1：免运费
     */
    private Byte goldPlusMembers;

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
     * 0: 不可用  1：可用
     */
    private Byte state;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName == null ? null : policyName.trim();
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Short getDayNum() {
        return dayNum;
    }

    public void setDayNum(Short dayNum) {
        this.dayNum = dayNum;
    }

    public Byte getDateType() {
        return dateType;
    }

    public void setDateType(Byte dateType) {
        this.dateType = dateType;
    }

    public Byte getPolicyType() {
        return policyType;
    }

    public void setPolicyType(Byte policyType) {
        this.policyType = policyType;
    }

    public Byte getRegularMembers() {
        return regularMembers;
    }

    public void setRegularMembers(Byte regularMembers) {
        this.regularMembers = regularMembers;
    }

    public Byte getGoldMembers() {
        return goldMembers;
    }

    public void setGoldMembers(Byte goldMembers) {
        this.goldMembers = goldMembers;
    }

    public Byte getSupremeMembers() {
        return supremeMembers;
    }

    public void setSupremeMembers(Byte supremeMembers) {
        this.supremeMembers = supremeMembers;
    }

    public Byte getGoldPlusMembers() {
        return goldPlusMembers;
    }

    public void setGoldPlusMembers(Byte goldPlusMembers) {
        this.goldPlusMembers = goldPlusMembers;
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

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", policyName=").append(policyName);
        sb.append(", startTime=").append(startTime);
        sb.append(", endTime=").append(endTime);
        sb.append(", dayNum=").append(dayNum);
        sb.append(", dateType=").append(dateType);
        sb.append(", policyType=").append(policyType);
        sb.append(", regularMembers=").append(regularMembers);
        sb.append(", goldMembers=").append(goldMembers);
        sb.append(", supremeMembers=").append(supremeMembers);
        sb.append(", goldPlusMembers=").append(goldPlusMembers);
        sb.append(", createTime=").append(createTime);
        sb.append(", createUser=").append(createUser);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", updateUser=").append(updateUser);
        sb.append(", state=").append(state);
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
        SpShippingPolicyDto other = (SpShippingPolicyDto) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getPolicyName() == null ? other.getPolicyName() == null : this.getPolicyName().equals(other.getPolicyName()))
            && (this.getStartTime() == null ? other.getStartTime() == null : this.getStartTime().equals(other.getStartTime()))
            && (this.getEndTime() == null ? other.getEndTime() == null : this.getEndTime().equals(other.getEndTime()))
            && (this.getDayNum() == null ? other.getDayNum() == null : this.getDayNum().equals(other.getDayNum()))
            && (this.getDateType() == null ? other.getDateType() == null : this.getDateType().equals(other.getDateType()))
            && (this.getPolicyType() == null ? other.getPolicyType() == null : this.getPolicyType().equals(other.getPolicyType()))
            && (this.getRegularMembers() == null ? other.getRegularMembers() == null : this.getRegularMembers().equals(other.getRegularMembers()))
            && (this.getGoldMembers() == null ? other.getGoldMembers() == null : this.getGoldMembers().equals(other.getGoldMembers()))
            && (this.getSupremeMembers() == null ? other.getSupremeMembers() == null : this.getSupremeMembers().equals(other.getSupremeMembers()))
            && (this.getGoldPlusMembers() == null ? other.getGoldPlusMembers() == null : this.getGoldPlusMembers().equals(other.getGoldPlusMembers()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getCreateUser() == null ? other.getCreateUser() == null : this.getCreateUser().equals(other.getCreateUser()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getUpdateUser() == null ? other.getUpdateUser() == null : this.getUpdateUser().equals(other.getUpdateUser()))
            && (this.getState() == null ? other.getState() == null : this.getState().equals(other.getState()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getPolicyName() == null) ? 0 : getPolicyName().hashCode());
        result = prime * result + ((getStartTime() == null) ? 0 : getStartTime().hashCode());
        result = prime * result + ((getEndTime() == null) ? 0 : getEndTime().hashCode());
        result = prime * result + ((getDayNum() == null) ? 0 : getDayNum().hashCode());
        result = prime * result + ((getDateType() == null) ? 0 : getDateType().hashCode());
        result = prime * result + ((getPolicyType() == null) ? 0 : getPolicyType().hashCode());
        result = prime * result + ((getRegularMembers() == null) ? 0 : getRegularMembers().hashCode());
        result = prime * result + ((getGoldMembers() == null) ? 0 : getGoldMembers().hashCode());
        result = prime * result + ((getSupremeMembers() == null) ? 0 : getSupremeMembers().hashCode());
        result = prime * result + ((getGoldPlusMembers() == null) ? 0 : getGoldPlusMembers().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getCreateUser() == null) ? 0 : getCreateUser().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getUpdateUser() == null) ? 0 : getUpdateUser().hashCode());
        result = prime * result + ((getState() == null) ? 0 : getState().hashCode());
        return result;
    }
}