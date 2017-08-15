package com.shangpin.ephub.data.mysql.product.supplier.bean;
import java.io.Serializable;

/**
 * Created by loyalty on 15/9/15.
 * 产品信息
 */
public class HubSupplierProductRequestWithPage implements Serializable{
	
	private static final long serialVersionUID = -3125067250395773889L;
	private String Status;
	private String barcode;
	private String brandName;
	private String SpStatus;
	private String Memo;
	private String productCode;
	private String productName;
	private String skuId;
	private String spSkuId;
	private String color;//颜色 
	private String size;//尺码 
	private String categoryName;
	private String sizeClass;
	private String startDate;
	private String endDate;
	private Integer pageIndex;
	private Integer pageSize;
	private String supplierId;
	private Long supplierSpuId;
	private Long supplierSkuId;
	
	public Long getSupplierSpuId() {
		return supplierSpuId;
	}
	public void setSupplierSpuId(Long supplierSpuId) {
		this.supplierSpuId = supplierSpuId;
	}
	public Long getSupplierSkuId() {
		return supplierSkuId;
	}
	public void setSupplierSkuId(Long supplierSkuId) {
		this.supplierSkuId = supplierSkuId;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getSpStatus() {
		return SpStatus;
	}
	public void setSpStatus(String spStatus) {
		SpStatus = spStatus;
	}
	public String getMemo() {
		return Memo;
	}
	public void setMemo(String memo) {
		Memo = memo;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getSkuId() {
		return skuId;
	}
	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}
	public String getSpSkuId() {
		return spSkuId;
	}
	public void setSpSkuId(String spSkuId) {
		this.spSkuId = spSkuId;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getSizeClass() {
		return sizeClass;
	}
	public void setSizeClass(String sizeClass) {
		this.sizeClass = sizeClass;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public Integer getPageIndex() {
		return (pageIndex-1)*this.pageSize;
	}
	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public String getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}

