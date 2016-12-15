package com.shangpin.ephub.client.data.mysql.material.dto;

import java.io.Serializable;
import java.util.Date;

public class HubMaterialDicItemDto implements Serializable {
    /**
     * 主键
     */
    private Long materialDicItemId;

    /**
     * 映射颜色名称
     */
    private String materialItemName;

    /**
     * 颜色编号
     */
    private Long materialDicId;

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

    public Long getMaterialDicItemId() {
        return materialDicItemId;
    }

    public void setMaterialDicItemId(Long materialDicItemId) {
        this.materialDicItemId = materialDicItemId;
    }

    public String getMaterialItemName() {
        return materialItemName;
    }

    public void setMaterialItemName(String materialItemName) {
        this.materialItemName = materialItemName == null ? null : materialItemName.trim();
    }

    public Long getMaterialDicId() {
        return materialDicId;
    }

    public void setMaterialDicId(Long materialDicId) {
        this.materialDicId = materialDicId;
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
        sb.append(", materialDicItemId=").append(materialDicItemId);
        sb.append(", materialItemName=").append(materialItemName);
        sb.append(", materialDicId=").append(materialDicId);
        sb.append(", createTime=").append(createTime);
        sb.append(", createUser=").append(createUser);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", updateUser=").append(updateUser);
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
        HubMaterialDicItemDto other = (HubMaterialDicItemDto) that;
        return (this.getMaterialDicItemId() == null ? other.getMaterialDicItemId() == null : this.getMaterialDicItemId().equals(other.getMaterialDicItemId()))
            && (this.getMaterialItemName() == null ? other.getMaterialItemName() == null : this.getMaterialItemName().equals(other.getMaterialItemName()))
            && (this.getMaterialDicId() == null ? other.getMaterialDicId() == null : this.getMaterialDicId().equals(other.getMaterialDicId()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getCreateUser() == null ? other.getCreateUser() == null : this.getCreateUser().equals(other.getCreateUser()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getUpdateUser() == null ? other.getUpdateUser() == null : this.getUpdateUser().equals(other.getUpdateUser()))
            && (this.getMemo() == null ? other.getMemo() == null : this.getMemo().equals(other.getMemo()))
            && (this.getDataState() == null ? other.getDataState() == null : this.getDataState().equals(other.getDataState()))
            && (this.getVersion() == null ? other.getVersion() == null : this.getVersion().equals(other.getVersion()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getMaterialDicItemId() == null) ? 0 : getMaterialDicItemId().hashCode());
        result = prime * result + ((getMaterialItemName() == null) ? 0 : getMaterialItemName().hashCode());
        result = prime * result + ((getMaterialDicId() == null) ? 0 : getMaterialDicId().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getCreateUser() == null) ? 0 : getCreateUser().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getUpdateUser() == null) ? 0 : getUpdateUser().hashCode());
        result = prime * result + ((getMemo() == null) ? 0 : getMemo().hashCode());
        result = prime * result + ((getDataState() == null) ? 0 : getDataState().hashCode());
        result = prime * result + ((getVersion() == null) ? 0 : getVersion().hashCode());
        return result;
    }
}