package com.shangpin.ephub.client.data.mysql.spu.dto;

import java.io.Serializable;
import java.util.Date;

public class HubSpuPendingDto implements Serializable {
    /**
     * 主键
     */
    private Long spuPendingId;

    /**
     * 供应商Id
     */
    private String supplierId;

    /**
     * 供应商编号
     */
    private String supplierNo;

    /**
     * 供应商Spu编号
     */
    private String supplierSpuNo;

    /**
     * 货号
     */
    private String spuModel;

    /**
     * 供应商原始商品名称
     */
    private String spuName;

    /**
     * 性别
     */
    private String hubGender;

    /**
     * 类目编号
     */
    private String hubCategoryNo;

    /**
     * 品牌编号
     */
    private String hubBrandNo;

    /**
     * 季节编号
     */
    private String hubSeason;

    /**
     * =0 信息待完善 =1 信息已完善 待处理 2 已处理
     */
    private Byte spuState;

    /**
     * =0:无图片 =1图片信息未完成 =2 图片信息已完成 
     */
    private Byte picState;

    /**
     * 1：代表当季    0:非当季
     */
    private Byte isCurrentSeason;

    /**
     * =0 否 =1 是
            是否是抓取的新数据不一定是应季新品
     */
    private Byte isNewData;

    /**
     * 商品材质
     */
    private String hubMaterial;

    /**
     * 商品产地
     */
    private String hubOrigin;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 商品描述
     */
    private String spuDesc;

    /**
     * hubspuno
     */
    private String hubSpuNo;

    /**
     * =0 验证不通过 =1 验证已通过
     */
    private Byte spuModelState;

    /**
     * =0 完全不匹配 =1 匹配到4级 =2 不能匹配到末级
     */
    private Byte catgoryState;

    /**
     * 原始信息主键
     */
    private Long supplierSpuId;

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

    private Byte spuBrandState;

    private Byte spuGenderState;

    private Byte spuSeasonState;

    /**
     * 颜色
     */
    private String hubColor;

    /**
     * 颜色码
     */
    private String hubColorNo;

    private Byte spuColorState;

    /**
     * =0 未全部映射成功 =1 全部映射成功
     */
    private Byte spSkuSizeState;

    /**
     * 材质映射状态
     */
    private Byte materialState;

    /**
     * 产地映射状态
     */
    private Byte originState;

    /**
     * 过滤标
     */
    private Byte filterFlag;

    private Byte infoState;

    private Byte stockState;

    private String createUser;

    private String updateUser;

    private Byte auditState;

    private Date auditDate;

    private String auditUser;

    private String auditOpinion;

    /**
     * 0：程序处理 1：手工处理
     */
    private Byte handleFrom;

    /**
     * 1：SPU属性完全匹配 ，SKU需要处理 （查询用）
     */
    private Byte handleState;

    /**
     * 0:待拍照 1：已处理  2:已寄出
     */
    private Byte slotState;

    private Date slotHandleDate;

    private String slotHandleUser;

    private static final long serialVersionUID = 1L;

    public Long getSpuPendingId() {
        return spuPendingId;
    }

    public void setSpuPendingId(Long spuPendingId) {
        this.spuPendingId = spuPendingId;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId == null ? null : supplierId.trim();
    }

    public String getSupplierNo() {
        return supplierNo;
    }

    public void setSupplierNo(String supplierNo) {
        this.supplierNo = supplierNo == null ? null : supplierNo.trim();
    }

    public String getSupplierSpuNo() {
        return supplierSpuNo;
    }

    public void setSupplierSpuNo(String supplierSpuNo) {
        this.supplierSpuNo = supplierSpuNo == null ? null : supplierSpuNo.trim();
    }

    public String getSpuModel() {
        return spuModel;
    }

    public void setSpuModel(String spuModel) {
        this.spuModel = spuModel == null ? null : spuModel.trim();
    }

    public String getSpuName() {
        return spuName;
    }

    public void setSpuName(String spuName) {
        this.spuName = spuName == null ? null : spuName.trim();
    }

    public String getHubGender() {
        return hubGender;
    }

    public void setHubGender(String hubGender) {
        this.hubGender = hubGender == null ? null : hubGender.trim();
    }

    public String getHubCategoryNo() {
        return hubCategoryNo;
    }

    public void setHubCategoryNo(String hubCategoryNo) {
        this.hubCategoryNo = hubCategoryNo == null ? null : hubCategoryNo.trim();
    }

    public String getHubBrandNo() {
        return hubBrandNo;
    }

    public void setHubBrandNo(String hubBrandNo) {
        this.hubBrandNo = hubBrandNo == null ? null : hubBrandNo.trim();
    }

    public String getHubSeason() {
        return hubSeason;
    }

    public void setHubSeason(String hubSeason) {
        this.hubSeason = hubSeason == null ? null : hubSeason.trim();
    }

    public Byte getSpuState() {
        return spuState;
    }

    public void setSpuState(Byte spuState) {
        this.spuState = spuState;
    }

    public Byte getPicState() {
        return picState;
    }

    public void setPicState(Byte picState) {
        this.picState = picState;
    }

    public Byte getIsCurrentSeason() {
        return isCurrentSeason;
    }

    public void setIsCurrentSeason(Byte isCurrentSeason) {
        this.isCurrentSeason = isCurrentSeason;
    }

    public Byte getIsNewData() {
        return isNewData;
    }

    public void setIsNewData(Byte isNewData) {
        this.isNewData = isNewData;
    }

    public String getHubMaterial() {
        return hubMaterial;
    }

    public void setHubMaterial(String hubMaterial) {
        this.hubMaterial = hubMaterial == null ? null : hubMaterial.trim();
    }

    public String getHubOrigin() {
        return hubOrigin;
    }

    public void setHubOrigin(String hubOrigin) {
        this.hubOrigin = hubOrigin == null ? null : hubOrigin.trim();
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

    public String getSpuDesc() {
        return spuDesc;
    }

    public void setSpuDesc(String spuDesc) {
        this.spuDesc = spuDesc == null ? null : spuDesc.trim();
    }

    public String getHubSpuNo() {
        return hubSpuNo;
    }

    public void setHubSpuNo(String hubSpuNo) {
        this.hubSpuNo = hubSpuNo == null ? null : hubSpuNo.trim();
    }

    public Byte getSpuModelState() {
        return spuModelState;
    }

    public void setSpuModelState(Byte spuModelState) {
        this.spuModelState = spuModelState;
    }

    public Byte getCatgoryState() {
        return catgoryState;
    }

    public void setCatgoryState(Byte catgoryState) {
        this.catgoryState = catgoryState;
    }

    public Long getSupplierSpuId() {
        return supplierSpuId;
    }

    public void setSupplierSpuId(Long supplierSpuId) {
        this.supplierSpuId = supplierSpuId;
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

    public Byte getSpuBrandState() {
        return spuBrandState;
    }

    public void setSpuBrandState(Byte spuBrandState) {
        this.spuBrandState = spuBrandState;
    }

    public Byte getSpuGenderState() {
        return spuGenderState;
    }

    public void setSpuGenderState(Byte spuGenderState) {
        this.spuGenderState = spuGenderState;
    }

    public Byte getSpuSeasonState() {
        return spuSeasonState;
    }

    public void setSpuSeasonState(Byte spuSeasonState) {
        this.spuSeasonState = spuSeasonState;
    }

    public String getHubColor() {
        return hubColor;
    }

    public void setHubColor(String hubColor) {
        this.hubColor = hubColor == null ? null : hubColor.trim();
    }

    public String getHubColorNo() {
        return hubColorNo;
    }

    public void setHubColorNo(String hubColorNo) {
        this.hubColorNo = hubColorNo == null ? null : hubColorNo.trim();
    }

    public Byte getSpuColorState() {
        return spuColorState;
    }

    public void setSpuColorState(Byte spuColorState) {
        this.spuColorState = spuColorState;
    }

    public Byte getSpSkuSizeState() {
        return spSkuSizeState;
    }

    public void setSpSkuSizeState(Byte spSkuSizeState) {
        this.spSkuSizeState = spSkuSizeState;
    }

    public Byte getMaterialState() {
        return materialState;
    }

    public void setMaterialState(Byte materialState) {
        this.materialState = materialState;
    }

    public Byte getOriginState() {
        return originState;
    }

    public void setOriginState(Byte originState) {
        this.originState = originState;
    }

    public Byte getFilterFlag() {
        return filterFlag;
    }

    public void setFilterFlag(Byte filterFlag) {
        this.filterFlag = filterFlag;
    }

    public Byte getInfoState() {
        return infoState;
    }

    public void setInfoState(Byte infoState) {
        this.infoState = infoState;
    }

    public Byte getStockState() {
        return stockState;
    }

    public void setStockState(Byte stockState) {
        this.stockState = stockState;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser == null ? null : updateUser.trim();
    }

    public Byte getAuditState() {
        return auditState;
    }

    public void setAuditState(Byte auditState) {
        this.auditState = auditState;
    }

    public Date getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(Date auditDate) {
        this.auditDate = auditDate;
    }

    public String getAuditUser() {
        return auditUser;
    }

    public void setAuditUser(String auditUser) {
        this.auditUser = auditUser == null ? null : auditUser.trim();
    }

    public String getAuditOpinion() {
        return auditOpinion;
    }

    public void setAuditOpinion(String auditOpinion) {
        this.auditOpinion = auditOpinion == null ? null : auditOpinion.trim();
    }

    public Byte getHandleFrom() {
        return handleFrom;
    }

    public void setHandleFrom(Byte handleFrom) {
        this.handleFrom = handleFrom;
    }

    public Byte getHandleState() {
        return handleState;
    }

    public void setHandleState(Byte handleState) {
        this.handleState = handleState;
    }

    public Byte getSlotState() {
        return slotState;
    }

    public void setSlotState(Byte slotState) {
        this.slotState = slotState;
    }

    public Date getSlotHandleDate() {
        return slotHandleDate;
    }

    public void setSlotHandleDate(Date slotHandleDate) {
        this.slotHandleDate = slotHandleDate;
    }

    public String getSlotHandleUser() {
        return slotHandleUser;
    }

    public void setSlotHandleUser(String slotHandleUser) {
        this.slotHandleUser = slotHandleUser == null ? null : slotHandleUser.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", spuPendingId=").append(spuPendingId);
        sb.append(", supplierId=").append(supplierId);
        sb.append(", supplierNo=").append(supplierNo);
        sb.append(", supplierSpuNo=").append(supplierSpuNo);
        sb.append(", spuModel=").append(spuModel);
        sb.append(", spuName=").append(spuName);
        sb.append(", hubGender=").append(hubGender);
        sb.append(", hubCategoryNo=").append(hubCategoryNo);
        sb.append(", hubBrandNo=").append(hubBrandNo);
        sb.append(", hubSeason=").append(hubSeason);
        sb.append(", spuState=").append(spuState);
        sb.append(", picState=").append(picState);
        sb.append(", isCurrentSeason=").append(isCurrentSeason);
        sb.append(", isNewData=").append(isNewData);
        sb.append(", hubMaterial=").append(hubMaterial);
        sb.append(", hubOrigin=").append(hubOrigin);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", spuDesc=").append(spuDesc);
        sb.append(", hubSpuNo=").append(hubSpuNo);
        sb.append(", spuModelState=").append(spuModelState);
        sb.append(", catgoryState=").append(catgoryState);
        sb.append(", supplierSpuId=").append(supplierSpuId);
        sb.append(", memo=").append(memo);
        sb.append(", dataState=").append(dataState);
        sb.append(", version=").append(version);
        sb.append(", spuBrandState=").append(spuBrandState);
        sb.append(", spuGenderState=").append(spuGenderState);
        sb.append(", spuSeasonState=").append(spuSeasonState);
        sb.append(", hubColor=").append(hubColor);
        sb.append(", hubColorNo=").append(hubColorNo);
        sb.append(", spuColorState=").append(spuColorState);
        sb.append(", spSkuSizeState=").append(spSkuSizeState);
        sb.append(", materialState=").append(materialState);
        sb.append(", originState=").append(originState);
        sb.append(", filterFlag=").append(filterFlag);
        sb.append(", infoState=").append(infoState);
        sb.append(", stockState=").append(stockState);
        sb.append(", createUser=").append(createUser);
        sb.append(", updateUser=").append(updateUser);
        sb.append(", auditState=").append(auditState);
        sb.append(", auditDate=").append(auditDate);
        sb.append(", auditUser=").append(auditUser);
        sb.append(", auditOpinion=").append(auditOpinion);
        sb.append(", handleFrom=").append(handleFrom);
        sb.append(", handleState=").append(handleState);
        sb.append(", slotState=").append(slotState);
        sb.append(", slotHandleDate=").append(slotHandleDate);
        sb.append(", slotHandleUser=").append(slotHandleUser);
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
        HubSpuPendingDto other = (HubSpuPendingDto) that;
        return (this.getSpuPendingId() == null ? other.getSpuPendingId() == null : this.getSpuPendingId().equals(other.getSpuPendingId()))
            && (this.getSupplierId() == null ? other.getSupplierId() == null : this.getSupplierId().equals(other.getSupplierId()))
            && (this.getSupplierNo() == null ? other.getSupplierNo() == null : this.getSupplierNo().equals(other.getSupplierNo()))
            && (this.getSupplierSpuNo() == null ? other.getSupplierSpuNo() == null : this.getSupplierSpuNo().equals(other.getSupplierSpuNo()))
            && (this.getSpuModel() == null ? other.getSpuModel() == null : this.getSpuModel().equals(other.getSpuModel()))
            && (this.getSpuName() == null ? other.getSpuName() == null : this.getSpuName().equals(other.getSpuName()))
            && (this.getHubGender() == null ? other.getHubGender() == null : this.getHubGender().equals(other.getHubGender()))
            && (this.getHubCategoryNo() == null ? other.getHubCategoryNo() == null : this.getHubCategoryNo().equals(other.getHubCategoryNo()))
            && (this.getHubBrandNo() == null ? other.getHubBrandNo() == null : this.getHubBrandNo().equals(other.getHubBrandNo()))
            && (this.getHubSeason() == null ? other.getHubSeason() == null : this.getHubSeason().equals(other.getHubSeason()))
            && (this.getSpuState() == null ? other.getSpuState() == null : this.getSpuState().equals(other.getSpuState()))
            && (this.getPicState() == null ? other.getPicState() == null : this.getPicState().equals(other.getPicState()))
            && (this.getIsCurrentSeason() == null ? other.getIsCurrentSeason() == null : this.getIsCurrentSeason().equals(other.getIsCurrentSeason()))
            && (this.getIsNewData() == null ? other.getIsNewData() == null : this.getIsNewData().equals(other.getIsNewData()))
            && (this.getHubMaterial() == null ? other.getHubMaterial() == null : this.getHubMaterial().equals(other.getHubMaterial()))
            && (this.getHubOrigin() == null ? other.getHubOrigin() == null : this.getHubOrigin().equals(other.getHubOrigin()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getSpuDesc() == null ? other.getSpuDesc() == null : this.getSpuDesc().equals(other.getSpuDesc()))
            && (this.getHubSpuNo() == null ? other.getHubSpuNo() == null : this.getHubSpuNo().equals(other.getHubSpuNo()))
            && (this.getSpuModelState() == null ? other.getSpuModelState() == null : this.getSpuModelState().equals(other.getSpuModelState()))
            && (this.getCatgoryState() == null ? other.getCatgoryState() == null : this.getCatgoryState().equals(other.getCatgoryState()))
            && (this.getSupplierSpuId() == null ? other.getSupplierSpuId() == null : this.getSupplierSpuId().equals(other.getSupplierSpuId()))
            && (this.getMemo() == null ? other.getMemo() == null : this.getMemo().equals(other.getMemo()))
            && (this.getDataState() == null ? other.getDataState() == null : this.getDataState().equals(other.getDataState()))
            && (this.getVersion() == null ? other.getVersion() == null : this.getVersion().equals(other.getVersion()))
            && (this.getSpuBrandState() == null ? other.getSpuBrandState() == null : this.getSpuBrandState().equals(other.getSpuBrandState()))
            && (this.getSpuGenderState() == null ? other.getSpuGenderState() == null : this.getSpuGenderState().equals(other.getSpuGenderState()))
            && (this.getSpuSeasonState() == null ? other.getSpuSeasonState() == null : this.getSpuSeasonState().equals(other.getSpuSeasonState()))
            && (this.getHubColor() == null ? other.getHubColor() == null : this.getHubColor().equals(other.getHubColor()))
            && (this.getHubColorNo() == null ? other.getHubColorNo() == null : this.getHubColorNo().equals(other.getHubColorNo()))
            && (this.getSpuColorState() == null ? other.getSpuColorState() == null : this.getSpuColorState().equals(other.getSpuColorState()))
            && (this.getSpSkuSizeState() == null ? other.getSpSkuSizeState() == null : this.getSpSkuSizeState().equals(other.getSpSkuSizeState()))
            && (this.getMaterialState() == null ? other.getMaterialState() == null : this.getMaterialState().equals(other.getMaterialState()))
            && (this.getOriginState() == null ? other.getOriginState() == null : this.getOriginState().equals(other.getOriginState()))
            && (this.getFilterFlag() == null ? other.getFilterFlag() == null : this.getFilterFlag().equals(other.getFilterFlag()))
            && (this.getInfoState() == null ? other.getInfoState() == null : this.getInfoState().equals(other.getInfoState()))
            && (this.getStockState() == null ? other.getStockState() == null : this.getStockState().equals(other.getStockState()))
            && (this.getCreateUser() == null ? other.getCreateUser() == null : this.getCreateUser().equals(other.getCreateUser()))
            && (this.getUpdateUser() == null ? other.getUpdateUser() == null : this.getUpdateUser().equals(other.getUpdateUser()))
            && (this.getAuditState() == null ? other.getAuditState() == null : this.getAuditState().equals(other.getAuditState()))
            && (this.getAuditDate() == null ? other.getAuditDate() == null : this.getAuditDate().equals(other.getAuditDate()))
            && (this.getAuditUser() == null ? other.getAuditUser() == null : this.getAuditUser().equals(other.getAuditUser()))
            && (this.getAuditOpinion() == null ? other.getAuditOpinion() == null : this.getAuditOpinion().equals(other.getAuditOpinion()))
            && (this.getHandleFrom() == null ? other.getHandleFrom() == null : this.getHandleFrom().equals(other.getHandleFrom()))
            && (this.getHandleState() == null ? other.getHandleState() == null : this.getHandleState().equals(other.getHandleState()))
            && (this.getSlotState() == null ? other.getSlotState() == null : this.getSlotState().equals(other.getSlotState()))
            && (this.getSlotHandleDate() == null ? other.getSlotHandleDate() == null : this.getSlotHandleDate().equals(other.getSlotHandleDate()))
            && (this.getSlotHandleUser() == null ? other.getSlotHandleUser() == null : this.getSlotHandleUser().equals(other.getSlotHandleUser()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getSpuPendingId() == null) ? 0 : getSpuPendingId().hashCode());
        result = prime * result + ((getSupplierId() == null) ? 0 : getSupplierId().hashCode());
        result = prime * result + ((getSupplierNo() == null) ? 0 : getSupplierNo().hashCode());
        result = prime * result + ((getSupplierSpuNo() == null) ? 0 : getSupplierSpuNo().hashCode());
        result = prime * result + ((getSpuModel() == null) ? 0 : getSpuModel().hashCode());
        result = prime * result + ((getSpuName() == null) ? 0 : getSpuName().hashCode());
        result = prime * result + ((getHubGender() == null) ? 0 : getHubGender().hashCode());
        result = prime * result + ((getHubCategoryNo() == null) ? 0 : getHubCategoryNo().hashCode());
        result = prime * result + ((getHubBrandNo() == null) ? 0 : getHubBrandNo().hashCode());
        result = prime * result + ((getHubSeason() == null) ? 0 : getHubSeason().hashCode());
        result = prime * result + ((getSpuState() == null) ? 0 : getSpuState().hashCode());
        result = prime * result + ((getPicState() == null) ? 0 : getPicState().hashCode());
        result = prime * result + ((getIsCurrentSeason() == null) ? 0 : getIsCurrentSeason().hashCode());
        result = prime * result + ((getIsNewData() == null) ? 0 : getIsNewData().hashCode());
        result = prime * result + ((getHubMaterial() == null) ? 0 : getHubMaterial().hashCode());
        result = prime * result + ((getHubOrigin() == null) ? 0 : getHubOrigin().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getSpuDesc() == null) ? 0 : getSpuDesc().hashCode());
        result = prime * result + ((getHubSpuNo() == null) ? 0 : getHubSpuNo().hashCode());
        result = prime * result + ((getSpuModelState() == null) ? 0 : getSpuModelState().hashCode());
        result = prime * result + ((getCatgoryState() == null) ? 0 : getCatgoryState().hashCode());
        result = prime * result + ((getSupplierSpuId() == null) ? 0 : getSupplierSpuId().hashCode());
        result = prime * result + ((getMemo() == null) ? 0 : getMemo().hashCode());
        result = prime * result + ((getDataState() == null) ? 0 : getDataState().hashCode());
        result = prime * result + ((getVersion() == null) ? 0 : getVersion().hashCode());
        result = prime * result + ((getSpuBrandState() == null) ? 0 : getSpuBrandState().hashCode());
        result = prime * result + ((getSpuGenderState() == null) ? 0 : getSpuGenderState().hashCode());
        result = prime * result + ((getSpuSeasonState() == null) ? 0 : getSpuSeasonState().hashCode());
        result = prime * result + ((getHubColor() == null) ? 0 : getHubColor().hashCode());
        result = prime * result + ((getHubColorNo() == null) ? 0 : getHubColorNo().hashCode());
        result = prime * result + ((getSpuColorState() == null) ? 0 : getSpuColorState().hashCode());
        result = prime * result + ((getSpSkuSizeState() == null) ? 0 : getSpSkuSizeState().hashCode());
        result = prime * result + ((getMaterialState() == null) ? 0 : getMaterialState().hashCode());
        result = prime * result + ((getOriginState() == null) ? 0 : getOriginState().hashCode());
        result = prime * result + ((getFilterFlag() == null) ? 0 : getFilterFlag().hashCode());
        result = prime * result + ((getInfoState() == null) ? 0 : getInfoState().hashCode());
        result = prime * result + ((getStockState() == null) ? 0 : getStockState().hashCode());
        result = prime * result + ((getCreateUser() == null) ? 0 : getCreateUser().hashCode());
        result = prime * result + ((getUpdateUser() == null) ? 0 : getUpdateUser().hashCode());
        result = prime * result + ((getAuditState() == null) ? 0 : getAuditState().hashCode());
        result = prime * result + ((getAuditDate() == null) ? 0 : getAuditDate().hashCode());
        result = prime * result + ((getAuditUser() == null) ? 0 : getAuditUser().hashCode());
        result = prime * result + ((getAuditOpinion() == null) ? 0 : getAuditOpinion().hashCode());
        result = prime * result + ((getHandleFrom() == null) ? 0 : getHandleFrom().hashCode());
        result = prime * result + ((getHandleState() == null) ? 0 : getHandleState().hashCode());
        result = prime * result + ((getSlotState() == null) ? 0 : getSlotState().hashCode());
        result = prime * result + ((getSlotHandleDate() == null) ? 0 : getSlotHandleDate().hashCode());
        result = prime * result + ((getSlotHandleUser() == null) ? 0 : getSlotHandleUser().hashCode());
        return result;
    }
}