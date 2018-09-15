package com.shangpin.spider.entity.gather;

import java.math.BigDecimal;
import java.util.Date;

public class CrawlResult implements Cloneable{
    private Long id;

    private String gender;

    private String brand;

    private String category;

    private String spu;

    private String productModel;

    private String season;

    private String material;

    private String color;

    private String size;

    private String proName;

    private BigDecimal foreignPrice;

    private BigDecimal domesticPrice;

    private BigDecimal salePrice;

    private Integer qty;

    private String made;

    private String detailLink;

    private Long sppuHash;

    private String measurement;

    private String supplierId;

    private String supplierNo;

    private String supplierSkuNo;

    private String channel;

    private Long whiteId;

    private Date createTime;

    private Date updateTime;
    
    private String description;

    private String pics;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSpu() {
		return spu;
	}

	public void setSpu(String spu) {
		this.spu = spu;
	}

	public String getProductModel() {
		return productModel;
	}

	public void setProductModel(String productModel) {
		this.productModel = productModel;
	}

	public String getSeason() {
		return season;
	}

	public void setSeason(String season) {
		this.season = season;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
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

	public String getProName() {
		return proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}

	public BigDecimal getForeignPrice() {
		return foreignPrice;
	}

	public void setForeignPrice(BigDecimal foreignPrice) {
		this.foreignPrice = foreignPrice;
	}

	public BigDecimal getDomesticPrice() {
		return domesticPrice;
	}

	public void setDomesticPrice(BigDecimal domesticPrice) {
		this.domesticPrice = domesticPrice;
	}

	public BigDecimal getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}

	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	public String getMade() {
		return made;
	}

	public void setMade(String made) {
		this.made = made;
	}

	public String getDetailLink() {
		return detailLink;
	}

	public void setDetailLink(String detailLink) {
		this.detailLink = detailLink;
	}

	public Long getSppuHash() {
		return sppuHash;
	}

	public void setSppuHash(Long sppuHash) {
		this.sppuHash = sppuHash;
	}

	public String getMeasurement() {
		return measurement;
	}

	public void setMeasurement(String measurement) {
		this.measurement = measurement;
	}

	public String getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}

	public String getSupplierNo() {
		return supplierNo;
	}

	public void setSupplierNo(String supplierNo) {
		this.supplierNo = supplierNo;
	}

	public String getSupplierSkuNo() {
		return supplierSkuNo;
	}

	public void setSupplierSkuNo(String supplierSkuNo) {
		this.supplierSkuNo = supplierSkuNo;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannelId(String channel) {
		this.channel = channel;
	}

	public Long getWhiteId() {
		return whiteId;
	}

	public void setWhiteId(Long whiteId) {
		this.whiteId = whiteId;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPics() {
		return pics;
	}

	public void setPics(String pics) {
		this.pics = pics;
	}

	@Override
	public String toString() {
		return "CrawlResult [id=" + id + ", gender=" + gender + ", brand=" + brand + ", category=" + category + ", spu="
				+ spu + ", productModel=" + productModel + ", season=" + season + ", material=" + material + ", color="
				+ color + ", size=" + size + ", proName=" + proName + ", foreignPrice=" + foreignPrice
				+ ", domesticPrice=" + domesticPrice + ", salePrice=" + salePrice + ", qty=" + qty + ", made=" + made
				+ ", detailLink=" + detailLink + ", sppuHash=" + sppuHash + ", measurement=" + measurement
				+ ", supplierId=" + supplierId + ", supplierNo=" + supplierNo + ", supplierSkuNo=" + supplierSkuNo
				+ ", channel=" + channel + ", whiteId=" + whiteId + ", createTime=" + createTime + ", updateTime="
				+ updateTime + ", description=" + description + ", pics=" + pics + "]";
	}
	
	//重写clone
    @Override
    public Object clone(){
        Object o = null;
        try {
            o = super.clone();   //调用父类的clone
        } catch (CloneNotSupportedException e) {    //异常捕获
            e.printStackTrace();
        }
        return o;
    }
    
}