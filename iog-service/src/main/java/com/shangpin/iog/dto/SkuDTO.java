package com.shangpin.iog.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by loyalty on 15/6/3.
 * SPU对象
 */
/*@Getter
@Setter*/
public class SkuDTO implements Serializable {
	private static final long serialVersionUID = -5429770703070749059L;
	private String id;//必填
    private String supplierId;//供货商ID  必填
    private String skuId;//必填
    private String spuId;//必填
    private String productName;
    private String marketPrice;//市场价  三个价格必须有一个 需要和BD沟通 那个价格是算尚品的供货价的价格
    private String salePrice;//销售价格
    private String supplierPrice;//供货商价格
    private String barcode;//条形码
    private String productCode;//货号
    private String color;// 颜色 必填
    private String productDescription;//描述
    private String saleCurrency;//币种
    private String productSize;//尺码   必填
    private String stock;//库存  必填   如果库存等于0的 不存
    private String memo;  //备注
    private Date createTime = new Date();
    private Date lastTime= new Date();//修改时间
     
    private String newMarketPrice; //新的市场价
    private String newSalePrice;
    private String newSupplierPrice;
    private Date updateTime;
    private String eventStartDate;
    private String eventEndDate;
    private String measurement;
    private String spSkuId;
    private String spStatus;
    
	public String getSpStatus() {
		return spStatus;
	}
	public void setSpStatus(String spStatus) {
		this.spStatus = spStatus;
	}
	public String getMeasurement() {
		return measurement;
	}
	public void setMeasurement(String measurement) {
		this.measurement = measurement;
	}
	public String getEventStartDate() {
		return eventStartDate;
	}
	public void setEventStartDate(String eventStartDate) { this.eventStartDate = eventStartDate; }

	public String getEventEndDate() {
		return eventEndDate;
	}

	public void setEventEndDate(String eventEndDate) {
		this.eventEndDate = eventEndDate;
	}

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

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getNewMarketPrice() {
        return newMarketPrice;
    }

    public void setNewMarketPrice(String newMarketPrice) {
        this.newMarketPrice = newMarketPrice;
    }

    public String getNewSalePrice() {
        return newSalePrice;
    }

    public void setNewSalePrice(String newSalePrice) {
        this.newSalePrice = newSalePrice;
    }

    public String getNewSupplierPrice() {
        return newSupplierPrice;
    }

    public void setNewSupplierPrice(String newSupplierPrice) {
        this.newSupplierPrice = newSupplierPrice;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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

    public String getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(String marketPrice) {
        this.marketPrice = marketPrice;
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
	public String getSpSkuId() {
		return spSkuId;
	}
	public void setSpSkuId(String spSkuId) {
		this.spSkuId = spSkuId;
	}
    
}
