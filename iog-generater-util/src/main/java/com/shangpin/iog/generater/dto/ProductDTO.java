package com.shangpin.iog.generater.dto;

public class ProductDTO {
	//size and stock 
	private String sizeandstock;

	private String spuId;
	private String skuId;
	private String gender;
	private String category;
	private String brand;
	private String season;
	private String picurl;
	private String origin;
	private String material;
	private String productName;
	private String marketPrice;
	private String salePrice;
	private String supplierPrice;
	private String barcode;
	private String color;
	private String currency;
	private String size;
	private String stock;
	private String productCode;
	private String description;
	
	public String getSizeandstock() {
		return sizeandstock;
	}
	public void setSizeandstock(String sizeandstock) {
		this.sizeandstock = sizeandstock;
	}
	public String getSpuId() {
		return spuId;
	}
	public void setSpuId(String spuId) {
		this.spuId = spuId;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getSeason() {
		return season;
	}
	public void setSeason(String season) {
		this.season = season;
	}
	public String getPicurl() {
		return picurl;
	}
	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getMaterial() {
		return material;
	}
	public void setMaterial(String material) {
		this.material = material;
	}
	public String getSkuId() {
		return skuId;
	}
	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getMarketPrice() {
		return marketPrice;
	}
	public void setMarketPrice(String marketPrice) {
		this.marketPrice = marketPrice;
	}
	public String getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(String salePrice) {
		this.salePrice = salePrice;
	}
	public String getSupplierPrice() {
		return supplierPrice;
	}
	public void setSupplierPrice(String supplierPrice) {
		this.supplierPrice = supplierPrice;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getStock() {
		return stock;
	}
	public void setStock(String stock) {
		this.stock = stock;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		return "ProductDTO [spuId=" + spuId + ", gender=" + gender
				+ ", category=" + category + ", brand=" + brand + ", season="
				+ season + ", picurl=" + picurl + ", origin=" + origin
				+ ", material=" + material + ", skuId=" + skuId
				+ ", productName=" + productName + ", marketPrice="
				+ marketPrice + ", salePrice=" + salePrice + ", supplierPrice="
				+ supplierPrice + ", barcode=" + barcode + ", color=" + color
				+ ", currency=" + currency + ", size=" + size + ", stock="
				+ stock + ", productCode=" + productCode + ", description="
				+ description + ", sizeandstock=" + sizeandstock + "]";
	}
	
}
