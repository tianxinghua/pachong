package com.shangpin.iog.productweb;

import java.util.Date;

/**
 * Created by loyalty on 15/6/3.
 * SPU对象
 */

public class ProductDTO {
    private String id;
    private String supplierId;
    private String supplierName;
    private String spuId;

    private String spuName;
    private String skuId;
    private String productName;

    /**性别分类*/
    private String categoryGender;
    /**大类*/
    private String categoryId;
    private String categoryName;


    /**小类*/
    private String subCategoryId;
    private String subCategoryName;



    private String brandId;
    private String seasonId;//上市季节ID
    private String brandName;
    private String seasonName;
    private String productCode;//货号
    private String skuUrl;//供应商产品地址
    private String picUrl;//图片地址
    private String marketPrice;//市场价
    private String salePrice;//销售价
    private String supplierPrice;//供货价
    private String saleCurrency;//币种
    private String productDescription;//描述


    private String itemPictureUrl1="";

    private String itemPictureUrl2="";

    private String itemPictureUrl3="";

    private String itemPictureUrl4="";

    private String itemPictureUrl5="";

    private String itemPictureUrl6="";

    private String itemPictureUrl7="";

    private String itemPictureUrl8="";
    private String material;//材质
    private String productOrigin;//产地
    private String color;
    //尺码
    private String  size;
    //条形码
    private String  barcode;

    private String stock;//库存

    private Date createTime;
    private Date lastTime;//修改时间
    //活动时间添加
    private Date eventStartTime;
    private Date eventEndTime;
    //新的市场价 销售价 供货价添加
    private String newMarketPrice;
    private String newSalePrice;
    private String newSupplierPrice;
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



    public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getSpuId() {
        return spuId;
    }

    public void setSpuId(String spuId) {
        this.spuId = spuId;
    }

    public String getSpuName() {
        return spuName;
    }

    public void setSpuName(String spuName) {
        this.spuName = spuName;
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

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(String seasonId) {
        this.seasonId = seasonId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getSeasonName() {
        return seasonName;
    }

    public void setSeasonName(String seasonName) {
        this.seasonName = seasonName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getSupplierPrice() {
        return supplierPrice;
    }

    public void setSupplierPrice(String supplierPrice) {
        this.supplierPrice = supplierPrice;
    }

    public String getSaleCurrency() {
        return saleCurrency;
    }

    public void setSaleCurrency(String saleCurrency) {
        this.saleCurrency = saleCurrency;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getItemPictureUrl1() {
        return itemPictureUrl1;
    }

    public void setItemPictureUrl1(String itemPictureUrl1) {
        this.itemPictureUrl1 = itemPictureUrl1;
    }

    public String getItemPictureUrl2() {
        return itemPictureUrl2;
    }

    public void setItemPictureUrl2(String itemPictureUrl2) {
        this.itemPictureUrl2 = itemPictureUrl2;
    }

    public String getItemPictureUrl3() {
        return itemPictureUrl3;
    }

    public void setItemPictureUrl3(String itemPictureUrl3) {
        this.itemPictureUrl3 = itemPictureUrl3;
    }

    public String getItemPictureUrl4() {
        return itemPictureUrl4;
    }

    public void setItemPictureUrl4(String itemPictureUrl4) {
        this.itemPictureUrl4 = itemPictureUrl4;
    }

    public String getItemPictureUrl5() {
        return itemPictureUrl5;
    }

    public void setItemPictureUrl5(String itemPictureUrl5) {
        this.itemPictureUrl5 = itemPictureUrl5;
    }

    public String getItemPictureUrl6() {
        return itemPictureUrl6;
    }

    public void setItemPictureUrl6(String itemPictureUrl6) {
        this.itemPictureUrl6 = itemPictureUrl6;
    }

    public String getItemPictureUrl7() {
        return itemPictureUrl7;
    }

    public void setItemPictureUrl7(String itemPictureUrl7) {
        this.itemPictureUrl7 = itemPictureUrl7;
    }

    public String getItemPictureUrl8() {
        return itemPictureUrl8;
    }

    public void setItemPictureUrl8(String itemPictureUrl8) {
        this.itemPictureUrl8 = itemPictureUrl8;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getProductOrigin() {
        return productOrigin;
    }

    public void setProductOrigin(String productOrigin) {
        this.productOrigin = productOrigin;
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

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
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

    public String getCategoryGender() {
        return categoryGender;
    }

    public void setCategoryGender(String categoryGender) {
        this.categoryGender = categoryGender;
    }

    public String getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(String subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public  void setItemPic(){
        if(null==picUrl||"".equals(picUrl)){
            return ;
        }
        String[] urlArray = picUrl.split(",");
        if(null!=urlArray&&urlArray.length>0){
            for(int i=0;i<urlArray.length;i++){
                switch (i){
                    case 0:
                        itemPictureUrl1=urlArray[i];
                        break;
                    case 1:
                        itemPictureUrl2=urlArray[i];
                        break;
                    case 2:
                        itemPictureUrl3=urlArray[i];
                        break;
                    case 3:
                        itemPictureUrl4=urlArray[i];
                        break;
                    case 4:
                        itemPictureUrl5=urlArray[i];
                        break;
                    case 5:
                        itemPictureUrl5=urlArray[i];
                        break;
                    case 6:
                        itemPictureUrl7=urlArray[i];
                        break;
                    case 7:
                        itemPictureUrl8=urlArray[i];
                        break;


                }
            }
        }
    }

    public String getSkuUrl() {
        return skuUrl;
    }

    public void setSkuUrl(String skuUrl) {
        this.skuUrl = skuUrl;
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

	public Date getEventStartTime() {
		return eventStartTime;
	}

	public void setEventStartTime(Date eventStartTime) {
		this.eventStartTime = eventStartTime;
	}

	public Date getEventEndTime() {
		return eventEndTime;
	}

	public void setEventEndTime(Date eventEndTime) {
		this.eventEndTime = eventEndTime;
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

    @Override
    public String toString() {
        return "ProductDTO{" +
                "id='" + id + '\'' +
                ", supplierId='" + supplierId + '\'' +
                ", supplierName='" + supplierName + '\'' +
                ", spuId='" + spuId + '\'' +
                ", spuName='" + spuName + '\'' +
                ", skuId='" + skuId + '\'' +
                ", productName='" + productName + '\'' +
                ", categoryGender='" + categoryGender + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", subCategoryId='" + subCategoryId + '\'' +
                ", subCategoryName='" + subCategoryName + '\'' +
                ", brandId='" + brandId + '\'' +
                ", seasonId='" + seasonId + '\'' +
                ", brandName='" + brandName + '\'' +
                ", seasonName='" + seasonName + '\'' +
                ", productCode='" + productCode + '\'' +
                ", skuUrl='" + skuUrl + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", marketPrice='" + marketPrice + '\'' +
                ", salePrice='" + salePrice + '\'' +
                ", supplierPrice='" + supplierPrice + '\'' +
                ", saleCurrency='" + saleCurrency + '\'' +
                ", productDescription='" + productDescription + '\'' +
                ", itemPictureUrl1='" + itemPictureUrl1 + '\'' +
                ", itemPictureUrl2='" + itemPictureUrl2 + '\'' +
                ", itemPictureUrl3='" + itemPictureUrl3 + '\'' +
                ", itemPictureUrl4='" + itemPictureUrl4 + '\'' +
                ", itemPictureUrl5='" + itemPictureUrl5 + '\'' +
                ", itemPictureUrl6='" + itemPictureUrl6 + '\'' +
                ", itemPictureUrl7='" + itemPictureUrl7 + '\'' +
                ", itemPictureUrl8='" + itemPictureUrl8 + '\'' +
                ", material='" + material + '\'' +
                ", productOrigin='" + productOrigin + '\'' +
                ", color='" + color + '\'' +
                ", size='" + size + '\'' +
                ", barcode='" + barcode + '\'' +
                ", stock='" + stock + '\'' +
                ", createTime=" + createTime +
                ", lastTime=" + lastTime +
                ", eventStartTime=" + eventStartTime +
                ", eventEndTime=" + eventEndTime +
                ", newMarketPrice='" + newMarketPrice + '\'' +
                ", newSalePrice='" + newSalePrice + '\'' +
                ", newSupplierPrice='" + newSupplierPrice + '\'' +
                '}';
    }
}
