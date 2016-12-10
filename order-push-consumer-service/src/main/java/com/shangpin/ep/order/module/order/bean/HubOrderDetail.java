package com.shangpin.ep.order.module.order.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * <p>Title:HubOrderDetail.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月17日 下午5:54:47
 */
public class HubOrderDetail implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * UUID唯一标识
     */
    private String uuid;

    /**
     * 供应商ID
     */
    private String supplierId;

    /**
     * 供应商编号
     */
    private String supplierNo;

    /**
     * 推送给供货商的订单编号
     */
    private String orderNo;

    /**
     * 尚品主单编号
     */
    private String spMasterOrderNo;

    /**
     * 尚品订单详情编号
     */
    private String spOrderDetailNo;

    /**
     * 供应商订单编号
     */
    private String supplierOrderNo;

    /**
     * 尚品sku编号
     */
    private String spSkuNo;

    /**
     * 供应商sku编号
     */
    private String supplierSkuNo;

    /**
     * sku数量
     */
    private Integer quantity;

    /**
     * 采购价格
     */
    private BigDecimal purchasePrice;

    /**
     * 订单状态
     */
    private Integer orderStatus;

    /**
     * 订单推送状态
     */
    private Integer pushStatus;

    /**
     * 异常类型(后续增加)
     */
    private Integer errorType;

    /**
     * 异常描述
     */
    private String description;

    /**
     * 采购单编号
     */
    private String purchaseNo;

    /**
     * 采购单明细编号
     */
    private String purchaseDetailNo;

    /**
     * 锁库存时间
     */
    private Date lockStockTime;

    /**
     * 未支付取消订单时间
     */
    private Date cancelTime;

    /**
     * 支付时间
     */
    private Date payTime;

    /**
     * 确认时间
     */
    private Date confirmTime;

    /**
     * 发货时间
     */
    private Date deliveryTime;

    /**
     * 退款时间
     */
    private Date refundTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 最后修改时间
     */
    private Date updateTime = new Date();

    /**
     * 逻辑删除状态：0代表未删除：1代表已删除
     */
    private Integer deleteStatus;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid == null ? null : uuid.trim();
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

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    public String getSpMasterOrderNo() {
        return spMasterOrderNo;
    }

    public void setSpMasterOrderNo(String spMasterOrderNo) {
        this.spMasterOrderNo = spMasterOrderNo == null ? null : spMasterOrderNo.trim();
    }

    public String getSpOrderDetailNo() {
        return spOrderDetailNo;
    }

    public void setSpOrderDetailNo(String spOrderDetailNo) {
        this.spOrderDetailNo = spOrderDetailNo == null ? null : spOrderDetailNo.trim();
    }

    public String getSupplierOrderNo() {
        return supplierOrderNo;
    }

    public void setSupplierOrderNo(String supplierOrderNo) {
        this.supplierOrderNo = supplierOrderNo == null ? null : supplierOrderNo.trim();
    }

    public String getSpSkuNo() {
        return spSkuNo;
    }

    public void setSpSkuNo(String spSkuNo) {
        this.spSkuNo = spSkuNo == null ? null : spSkuNo.trim();
    }

    public String getSupplierSkuNo() {
        return supplierSkuNo;
    }

    public void setSupplierSkuNo(String supplierSkuNo) {
        this.supplierSkuNo = supplierSkuNo == null ? null : supplierSkuNo.trim();
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getPushStatus() {
        return pushStatus;
    }

    public void setPushStatus(Integer pushStatus) {
        this.pushStatus = pushStatus;
    }

    public Integer getErrorType() {
        return errorType;
    }

    public void setErrorType(Integer errorType) {
        this.errorType = errorType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getPurchaseNo() {
        return purchaseNo;
    }

    public void setPurchaseNo(String purchaseNo) {
        this.purchaseNo = purchaseNo == null ? null : purchaseNo.trim();
    }

    public String getPurchaseDetailNo() {
        return purchaseDetailNo;
    }

    public void setPurchaseDetailNo(String purchaseDetailNo) {
        this.purchaseDetailNo = purchaseDetailNo == null ? null : purchaseDetailNo.trim();
    }

    public Date getLockStockTime() {
        return lockStockTime;
    }

    public void setLockStockTime(Date lockStockTime) {
        this.lockStockTime = lockStockTime;
    }

    public Date getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(Date cancelTime) {
        this.cancelTime = cancelTime;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Date getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(Date confirmTime) {
        this.confirmTime = confirmTime;
    }

    public Date getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public Date getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(Date refundTime) {
        this.refundTime = refundTime;
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

    public Integer getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(Integer deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", uuid=").append(uuid);
        sb.append(", supplierId=").append(supplierId);
        sb.append(", supplierNo=").append(supplierNo);
        sb.append(", orderNo=").append(orderNo);
        sb.append(", spMasterOrderNo=").append(spMasterOrderNo);
        sb.append(", spOrderDetailNo=").append(spOrderDetailNo);
        sb.append(", supplierOrderNo=").append(supplierOrderNo);
        sb.append(", spSkuNo=").append(spSkuNo);
        sb.append(", supplierSkuNo=").append(supplierSkuNo);
        sb.append(", quantity=").append(quantity);
        sb.append(", purchasePrice=").append(purchasePrice);
        sb.append(", orderStatus=").append(orderStatus);
        sb.append(", pushStatus=").append(pushStatus);
        sb.append(", errorType=").append(errorType);
        sb.append(", description=").append(description);
        sb.append(", purchaseNo=").append(purchaseNo);
        sb.append(", purchaseDetailNo=").append(purchaseDetailNo);
        sb.append(", lockStockTime=").append(lockStockTime);
        sb.append(", cancelTime=").append(cancelTime);
        sb.append(", payTime=").append(payTime);
        sb.append(", confirmTime=").append(confirmTime);
        sb.append(", deliveryTime=").append(deliveryTime);
        sb.append(", refundTime=").append(refundTime);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", deleteStatus=").append(deleteStatus);
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
        HubOrderDetail other = (HubOrderDetail) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUuid() == null ? other.getUuid() == null : this.getUuid().equals(other.getUuid()))
            && (this.getSupplierId() == null ? other.getSupplierId() == null : this.getSupplierId().equals(other.getSupplierId()))
            && (this.getSupplierNo() == null ? other.getSupplierNo() == null : this.getSupplierNo().equals(other.getSupplierNo()))
            && (this.getOrderNo() == null ? other.getOrderNo() == null : this.getOrderNo().equals(other.getOrderNo()))
            && (this.getSpMasterOrderNo() == null ? other.getSpMasterOrderNo() == null : this.getSpMasterOrderNo().equals(other.getSpMasterOrderNo()))
            && (this.getSpOrderDetailNo() == null ? other.getSpOrderDetailNo() == null : this.getSpOrderDetailNo().equals(other.getSpOrderDetailNo()))
            && (this.getSupplierOrderNo() == null ? other.getSupplierOrderNo() == null : this.getSupplierOrderNo().equals(other.getSupplierOrderNo()))
            && (this.getSpSkuNo() == null ? other.getSpSkuNo() == null : this.getSpSkuNo().equals(other.getSpSkuNo()))
            && (this.getSupplierSkuNo() == null ? other.getSupplierSkuNo() == null : this.getSupplierSkuNo().equals(other.getSupplierSkuNo()))
            && (this.getQuantity() == null ? other.getQuantity() == null : this.getQuantity().equals(other.getQuantity()))
            && (this.getPurchasePrice() == null ? other.getPurchasePrice() == null : this.getPurchasePrice().equals(other.getPurchasePrice()))
            && (this.getOrderStatus() == null ? other.getOrderStatus() == null : this.getOrderStatus().equals(other.getOrderStatus()))
            && (this.getPushStatus() == null ? other.getPushStatus() == null : this.getPushStatus().equals(other.getPushStatus()))
            && (this.getErrorType() == null ? other.getErrorType() == null : this.getErrorType().equals(other.getErrorType()))
            && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
            && (this.getPurchaseNo() == null ? other.getPurchaseNo() == null : this.getPurchaseNo().equals(other.getPurchaseNo()))
            && (this.getPurchaseDetailNo() == null ? other.getPurchaseDetailNo() == null : this.getPurchaseDetailNo().equals(other.getPurchaseDetailNo()))
            && (this.getLockStockTime() == null ? other.getLockStockTime() == null : this.getLockStockTime().equals(other.getLockStockTime()))
            && (this.getCancelTime() == null ? other.getCancelTime() == null : this.getCancelTime().equals(other.getCancelTime()))
            && (this.getPayTime() == null ? other.getPayTime() == null : this.getPayTime().equals(other.getPayTime()))
            && (this.getConfirmTime() == null ? other.getConfirmTime() == null : this.getConfirmTime().equals(other.getConfirmTime()))
            && (this.getDeliveryTime() == null ? other.getDeliveryTime() == null : this.getDeliveryTime().equals(other.getDeliveryTime()))
            && (this.getRefundTime() == null ? other.getRefundTime() == null : this.getRefundTime().equals(other.getRefundTime()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getDeleteStatus() == null ? other.getDeleteStatus() == null : this.getDeleteStatus().equals(other.getDeleteStatus()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUuid() == null) ? 0 : getUuid().hashCode());
        result = prime * result + ((getSupplierId() == null) ? 0 : getSupplierId().hashCode());
        result = prime * result + ((getSupplierNo() == null) ? 0 : getSupplierNo().hashCode());
        result = prime * result + ((getOrderNo() == null) ? 0 : getOrderNo().hashCode());
        result = prime * result + ((getSpMasterOrderNo() == null) ? 0 : getSpMasterOrderNo().hashCode());
        result = prime * result + ((getSpOrderDetailNo() == null) ? 0 : getSpOrderDetailNo().hashCode());
        result = prime * result + ((getSupplierOrderNo() == null) ? 0 : getSupplierOrderNo().hashCode());
        result = prime * result + ((getSpSkuNo() == null) ? 0 : getSpSkuNo().hashCode());
        result = prime * result + ((getSupplierSkuNo() == null) ? 0 : getSupplierSkuNo().hashCode());
        result = prime * result + ((getQuantity() == null) ? 0 : getQuantity().hashCode());
        result = prime * result + ((getPurchasePrice() == null) ? 0 : getPurchasePrice().hashCode());
        result = prime * result + ((getOrderStatus() == null) ? 0 : getOrderStatus().hashCode());
        result = prime * result + ((getPushStatus() == null) ? 0 : getPushStatus().hashCode());
        result = prime * result + ((getErrorType() == null) ? 0 : getErrorType().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getPurchaseNo() == null) ? 0 : getPurchaseNo().hashCode());
        result = prime * result + ((getPurchaseDetailNo() == null) ? 0 : getPurchaseDetailNo().hashCode());
        result = prime * result + ((getLockStockTime() == null) ? 0 : getLockStockTime().hashCode());
        result = prime * result + ((getCancelTime() == null) ? 0 : getCancelTime().hashCode());
        result = prime * result + ((getPayTime() == null) ? 0 : getPayTime().hashCode());
        result = prime * result + ((getConfirmTime() == null) ? 0 : getConfirmTime().hashCode());
        result = prime * result + ((getDeliveryTime() == null) ? 0 : getDeliveryTime().hashCode());
        result = prime * result + ((getRefundTime() == null) ? 0 : getRefundTime().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getDeleteStatus() == null) ? 0 : getDeleteStatus().hashCode());
        return result;
    }
}