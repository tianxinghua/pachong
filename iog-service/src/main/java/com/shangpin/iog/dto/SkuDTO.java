package com.shangpin.iog.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by loyalty on 15/6/3.
 * SPU对象
 */
@Getter
@Setter
public class SkuDTO {
    private String id;
    private String supplierId;
    private String skuId;
    private String spuId;
    private String productName;
    private String marketPrice;//市场价
    private String salePrice;//销售价格
    private String supplierPrice;//供货商价格
    private String barcode;//条形码
    private String productCode;//货号
    private String color;
    private String productDescription;//描述
    private String saleCurrency;//币种
    private String productSize;//尺码
    private String stock;//库存
    private Date createTime = new Date();
    private Date lastTime= new Date();//修改时间

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getSpuId() {
        return spuId;
    }

    public void setSpuId(String spuId) {
        this.spuId = spuId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSaleCurrency() {
        return saleCurrency;
    }

    public void setSaleCurrency(String saleCurrency) {
        this.saleCurrency = saleCurrency;
    }

    public String getProductSize() {
        return productSize;
    }

    public void setProductSize(String productSize) {
        this.productSize = productSize;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastTime() {
        return lastTime;
    }

    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }




	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((skuId == null) ? 0 : skuId.hashCode());
		result = prime * result
				+ ((supplierId == null) ? 0 : supplierId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SkuDTO other = (SkuDTO) obj;
		if (skuId == null) {
			if (other.skuId != null)
				return false;
		} else if (!skuId.equals(other.skuId))
			return false;
		if (supplierId == null) {
			if (other.supplierId != null)
				return false;
		} else if (!supplierId.equals(other.supplierId))
			return false;
		return true;
	}

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }



	@Override
	public String toString() {
		return "SkuDTO [id=" + id + ", supplierId=" + supplierId + ", skuId="
				+ skuId + ", spuId=" + spuId + ", productName=" + productName
				+ ", salePrice=" + salePrice + ", supplierPrice="
				+ supplierPrice + ", barcode=" + barcode + ", productCode="
				+ productCode + ", color=" + color + ", productDescription="
				+ productDescription + ", saleCurrency=" + saleCurrency
				+ ", productSize=" + productSize + ", stock=" + stock
				+ ", createTime=" + createTime + ", lastTime=" + lastTime + "]";
	}
    
}
