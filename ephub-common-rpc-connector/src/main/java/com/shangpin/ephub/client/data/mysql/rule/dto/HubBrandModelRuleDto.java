package com.shangpin.ephub.client.data.mysql.rule.dto;

import java.io.Serializable;
import java.util.Date;

public class HubBrandModelRuleDto implements Serializable {
    /**
     * 主键
     */
    private Long brandModelRuleId;

    /**
     * 尚品品牌编号
     */
    private String hubBrandNo;

    /**
     * 货号规则
     */
    private String modelRule;

    /**
     * 货号验证正则
     */
    private String modelRex;

    /**
     * =0待确认，=1已确认
     */
    private Byte ruleState;

    /**
     * 是否关联品类
     */
    private Byte isJoinCategory;

    /**
     * 品类级别
     */
    private Byte categoryType;

    /**
     * 排除特殊字符正则
     */
    private String excludeRex;

    /**
     * 分隔符
     */
    private String separator;

    /**
     * 尚品品类编号
     */
    private String hubCategoryNo;

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
     * 发布时间
     */
    private Date pushTime;

    /**
     * 备注
     */
    private String memo;

    /**
     * 数据状态：1未删除 0已删除
     */
    private Byte dataState;

    /**
     * 版本字段
     */
    private Long version;

    private static final long serialVersionUID = 1L;

    public Long getBrandModelRuleId() {
        return brandModelRuleId;
    }

    public void setBrandModelRuleId(Long brandModelRuleId) {
        this.brandModelRuleId = brandModelRuleId;
    }

    public String getHubBrandNo() {
        return hubBrandNo;
    }

    public void setHubBrandNo(String hubBrandNo) {
        this.hubBrandNo = hubBrandNo == null ? null : hubBrandNo.trim();
    }

    public String getModelRule() {
        return modelRule;
    }

    public void setModelRule(String modelRule) {
        this.modelRule = modelRule == null ? null : modelRule.trim();
    }

    public String getModelRex() {
        return modelRex;
    }

    public void setModelRex(String modelRex) {
        this.modelRex = modelRex == null ? null : modelRex.trim();
    }

    public Byte getRuleState() {
        return ruleState;
    }

    public void setRuleState(Byte ruleState) {
        this.ruleState = ruleState;
    }

    public Byte getIsJoinCategory() {
        return isJoinCategory;
    }

    public void setIsJoinCategory(Byte isJoinCategory) {
        this.isJoinCategory = isJoinCategory;
    }

    public Byte getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(Byte categoryType) {
        this.categoryType = categoryType;
    }

    public String getExcludeRex() {
        return excludeRex;
    }

    public void setExcludeRex(String excludeRex) {
        this.excludeRex = excludeRex == null ? null : excludeRex.trim();
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator == null ? null : separator.trim();
    }

    public String getHubCategoryNo() {
        return hubCategoryNo;
    }

    public void setHubCategoryNo(String hubCategoryNo) {
        this.hubCategoryNo = hubCategoryNo == null ? null : hubCategoryNo.trim();
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

    public Date getPushTime() {
        return pushTime;
    }

    public void setPushTime(Date pushTime) {
        this.pushTime = pushTime;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
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
        sb.append(", brandModelRuleId=").append(brandModelRuleId);
        sb.append(", hubBrandNo=").append(hubBrandNo);
        sb.append(", modelRule=").append(modelRule);
        sb.append(", modelRex=").append(modelRex);
        sb.append(", ruleState=").append(ruleState);
        sb.append(", isJoinCategory=").append(isJoinCategory);
        sb.append(", categoryType=").append(categoryType);
        sb.append(", excludeRex=").append(excludeRex);
        sb.append(", separator=").append(separator);
        sb.append(", hubCategoryNo=").append(hubCategoryNo);
        sb.append(", createTime=").append(createTime);
        sb.append(", createUser=").append(createUser);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", updateUser=").append(updateUser);
        sb.append(", pushTime=").append(pushTime);
        sb.append(", memo=").append(memo);
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
        HubBrandModelRuleDto other = (HubBrandModelRuleDto) that;
        return (this.getBrandModelRuleId() == null ? other.getBrandModelRuleId() == null : this.getBrandModelRuleId().equals(other.getBrandModelRuleId()))
            && (this.getHubBrandNo() == null ? other.getHubBrandNo() == null : this.getHubBrandNo().equals(other.getHubBrandNo()))
            && (this.getModelRule() == null ? other.getModelRule() == null : this.getModelRule().equals(other.getModelRule()))
            && (this.getModelRex() == null ? other.getModelRex() == null : this.getModelRex().equals(other.getModelRex()))
            && (this.getRuleState() == null ? other.getRuleState() == null : this.getRuleState().equals(other.getRuleState()))
            && (this.getIsJoinCategory() == null ? other.getIsJoinCategory() == null : this.getIsJoinCategory().equals(other.getIsJoinCategory()))
            && (this.getCategoryType() == null ? other.getCategoryType() == null : this.getCategoryType().equals(other.getCategoryType()))
            && (this.getExcludeRex() == null ? other.getExcludeRex() == null : this.getExcludeRex().equals(other.getExcludeRex()))
            && (this.getSeparator() == null ? other.getSeparator() == null : this.getSeparator().equals(other.getSeparator()))
            && (this.getHubCategoryNo() == null ? other.getHubCategoryNo() == null : this.getHubCategoryNo().equals(other.getHubCategoryNo()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getCreateUser() == null ? other.getCreateUser() == null : this.getCreateUser().equals(other.getCreateUser()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getUpdateUser() == null ? other.getUpdateUser() == null : this.getUpdateUser().equals(other.getUpdateUser()))
            && (this.getPushTime() == null ? other.getPushTime() == null : this.getPushTime().equals(other.getPushTime()))
            && (this.getMemo() == null ? other.getMemo() == null : this.getMemo().equals(other.getMemo()))
            && (this.getDataState() == null ? other.getDataState() == null : this.getDataState().equals(other.getDataState()))
            && (this.getVersion() == null ? other.getVersion() == null : this.getVersion().equals(other.getVersion()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getBrandModelRuleId() == null) ? 0 : getBrandModelRuleId().hashCode());
        result = prime * result + ((getHubBrandNo() == null) ? 0 : getHubBrandNo().hashCode());
        result = prime * result + ((getModelRule() == null) ? 0 : getModelRule().hashCode());
        result = prime * result + ((getModelRex() == null) ? 0 : getModelRex().hashCode());
        result = prime * result + ((getRuleState() == null) ? 0 : getRuleState().hashCode());
        result = prime * result + ((getIsJoinCategory() == null) ? 0 : getIsJoinCategory().hashCode());
        result = prime * result + ((getCategoryType() == null) ? 0 : getCategoryType().hashCode());
        result = prime * result + ((getExcludeRex() == null) ? 0 : getExcludeRex().hashCode());
        result = prime * result + ((getSeparator() == null) ? 0 : getSeparator().hashCode());
        result = prime * result + ((getHubCategoryNo() == null) ? 0 : getHubCategoryNo().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getCreateUser() == null) ? 0 : getCreateUser().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getUpdateUser() == null) ? 0 : getUpdateUser().hashCode());
        result = prime * result + ((getPushTime() == null) ? 0 : getPushTime().hashCode());
        result = prime * result + ((getMemo() == null) ? 0 : getMemo().hashCode());
        result = prime * result + ((getDataState() == null) ? 0 : getDataState().hashCode());
        result = prime * result + ((getVersion() == null) ? 0 : getVersion().hashCode());
        return result;
    }
}