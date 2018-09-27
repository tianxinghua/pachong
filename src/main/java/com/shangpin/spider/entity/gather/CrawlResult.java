package com.shangpin.spider.entity.gather;

import java.math.BigDecimal;
import java.util.Date;

import com.shangpin.spider.config.FieldNotes;

public class CrawlResult implements Cloneable{
    private Long id;
    /**
     * 性别
     */
    @FieldNotes(name="性别")
    private String gender;
    /**
     * 品牌
     */
    @FieldNotes(name="品牌")
    private String brand;
    /**
     * 类型
     */
    @FieldNotes(name="类型")
    private String category;
    /**
     * 编号
     */
    @FieldNotes(name="编号")
    private String spu;
    /**
     * 产品型号
     */
    @FieldNotes(name="产品型号")
    private String productModel;
    /**
     * 季节
     */
    @FieldNotes(name="季节")
    private String season;
    /**
     * 材质
     */
    @FieldNotes(name="材质")
    private String material;
    /**
     * 颜色
     */
    @FieldNotes(name="颜色")
    private String color;
    /**
     * 尺寸
     */
    @FieldNotes(name="尺寸")
    private String size;
    /**
     * 商品名称
     */
    @FieldNotes(name="商品名称")
    private String proName;
    /**
     * 国外市场价
     */
    @FieldNotes(name="国外市场价")
    private BigDecimal foreignPrice;
    /**
     * 国内市场价
     */
    @FieldNotes(name="国内市场价")
    private BigDecimal domesticPrice;
    /**
     * 售价
     */
    @FieldNotes(name="售价")
    private BigDecimal salePrice;
    /**
     * 库存标识
     */
    @FieldNotes(name="库存标识")
    private Integer qty;
    /**
     * 产地
     */
    @FieldNotes(name="产地")
    private String made;
    /**
     * 商品链接
     */
    @FieldNotes(name="商品链接")
    private String detailLink;
    
    private Long sppuHash;
    /**
     * 尺寸详情
     */
    @FieldNotes(name="尺寸详情")
    private String measurement;
    
    private String supplierId;

    private String supplierNo;
    
    private String supplierSkuNo;
    
    private String channel;

    private Long whiteId;

    private Date createTime;

    private Date updateTime;
    /**
     * 商品描述
     */
    @FieldNotes(name="商品描述")
    private String description;
    /**
     * 图片
     */
    @FieldNotes(name="图片")
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