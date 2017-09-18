package com.shangpin.ephub.client.data.mysql.sp.dto;

import java.io.Serializable;
import java.util.Date;

public class SpShippingPolicyDetailDto implements Serializable {
    /**
     * 代理主键
     */
    private Long id;

    /**
     * 运费策略ID
     */
    private Long shippingPolicyId;

    /**
     * 品类来源 0：ERP 1:商城
     */
    private Byte spCategoryFrom;

    /**
     * 品类列表二级，以逗号隔开
     */
    private String spCategoryNo;

    /**
     * 品牌列表
     */
    private String spBrandNo;

    /**
     * 促销标识 0: 全部商品 1：促销商品 2：非促销商品
     */
    private Byte promotionSign;

    /**
     * 发货地区标识 0:全部 1:海外  2:国内
     */
    private Byte deliveryAreaSign;

    /**
     * 尚品商品编号类表
     */
    private String spSkuNo;

    /**
     * 活动编号列表
     */
    private String spActivityNo;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getShippingPolicyId() {
        return shippingPolicyId;
    }

    public void setShippingPolicyId(Long shippingPolicyId) {
        this.shippingPolicyId = shippingPolicyId;
    }

    public Byte getSpCategoryFrom() {
        return spCategoryFrom;
    }

    public void setSpCategoryFrom(Byte spCategoryFrom) {
        this.spCategoryFrom = spCategoryFrom;
    }

    public String getSpCategoryNo() {
        return spCategoryNo;
    }

    public void setSpCategoryNo(String spCategoryNo) {
        this.spCategoryNo = spCategoryNo == null ? null : spCategoryNo.trim();
    }

    public String getSpBrandNo() {
        return spBrandNo;
    }

    public void setSpBrandNo(String spBrandNo) {
        this.spBrandNo = spBrandNo == null ? null : spBrandNo.trim();
    }

    public Byte getPromotionSign() {
        return promotionSign;
    }

    public void setPromotionSign(Byte promotionSign) {
        this.promotionSign = promotionSign;
    }

    public Byte getDeliveryAreaSign() {
        return deliveryAreaSign;
    }

    public void setDeliveryAreaSign(Byte deliveryAreaSign) {
        this.deliveryAreaSign = deliveryAreaSign;
    }

    public String getSpSkuNo() {
        return spSkuNo;
    }

    public void setSpSkuNo(String spSkuNo) {
        this.spSkuNo = spSkuNo == null ? null : spSkuNo.trim();
    }

    public String getSpActivityNo() {
        return spActivityNo;
    }

    public void setSpActivityNo(String spActivityNo) {
        this.spActivityNo = spActivityNo == null ? null : spActivityNo.trim();
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", shippingPolicyId=").append(shippingPolicyId);
        sb.append(", spCategoryFrom=").append(spCategoryFrom);
        sb.append(", spCategoryNo=").append(spCategoryNo);
        sb.append(", spBrandNo=").append(spBrandNo);
        sb.append(", promotionSign=").append(promotionSign);
        sb.append(", deliveryAreaSign=").append(deliveryAreaSign);
        sb.append(", spSkuNo=").append(spSkuNo);
        sb.append(", spActivityNo=").append(spActivityNo);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
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
        SpShippingPolicyDetailDto other = (SpShippingPolicyDetailDto) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getShippingPolicyId() == null ? other.getShippingPolicyId() == null : this.getShippingPolicyId().equals(other.getShippingPolicyId()))
            && (this.getSpCategoryFrom() == null ? other.getSpCategoryFrom() == null : this.getSpCategoryFrom().equals(other.getSpCategoryFrom()))
            && (this.getSpCategoryNo() == null ? other.getSpCategoryNo() == null : this.getSpCategoryNo().equals(other.getSpCategoryNo()))
            && (this.getSpBrandNo() == null ? other.getSpBrandNo() == null : this.getSpBrandNo().equals(other.getSpBrandNo()))
            && (this.getPromotionSign() == null ? other.getPromotionSign() == null : this.getPromotionSign().equals(other.getPromotionSign()))
            && (this.getDeliveryAreaSign() == null ? other.getDeliveryAreaSign() == null : this.getDeliveryAreaSign().equals(other.getDeliveryAreaSign()))
            && (this.getSpSkuNo() == null ? other.getSpSkuNo() == null : this.getSpSkuNo().equals(other.getSpSkuNo()))
            && (this.getSpActivityNo() == null ? other.getSpActivityNo() == null : this.getSpActivityNo().equals(other.getSpActivityNo()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getShippingPolicyId() == null) ? 0 : getShippingPolicyId().hashCode());
        result = prime * result + ((getSpCategoryFrom() == null) ? 0 : getSpCategoryFrom().hashCode());
        result = prime * result + ((getSpCategoryNo() == null) ? 0 : getSpCategoryNo().hashCode());
        result = prime * result + ((getSpBrandNo() == null) ? 0 : getSpBrandNo().hashCode());
        result = prime * result + ((getPromotionSign() == null) ? 0 : getPromotionSign().hashCode());
        result = prime * result + ((getDeliveryAreaSign() == null) ? 0 : getDeliveryAreaSign().hashCode());
        result = prime * result + ((getSpSkuNo() == null) ? 0 : getSpSkuNo().hashCode());
        result = prime * result + ((getSpActivityNo() == null) ? 0 : getSpActivityNo().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }
}