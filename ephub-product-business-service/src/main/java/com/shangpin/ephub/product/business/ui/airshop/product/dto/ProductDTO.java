package com.shangpin.ephub.product.business.ui.airshop.product.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductDTO {
	private String supplierId;//供货商门户编号

	private Long supplierSpuId;
	private Long supplierSkuId;
    private String spuId; //必须

    private String skuId; //必须

    /**性别分类*/
    private String categoryGender;

    /**大类*/
    private String categoryName; //必须
    /**小类*/
    private String subCategoryName;


    private String seasonName;//上市季节名称

    private String brandName;//品牌 必须

    private String material;//材质  必须

    private String productOrigin;//产地

    private String productName;//产品展示名称 必须

    private BigDecimal marketPrice;//吊牌价（市场价）

    private BigDecimal salePrice;//销售价格

    private BigDecimal supplierPrice;//供货商价格  必须

    private Integer stock;//库存 必须

    private String barcode;//条形码

    private String productCode;//货号 必须

    private String color;//颜色 必须

    private String productDescription;//描述

    private String saleCurrency;//币种 必须

    private String size;//尺码 必须

    private String  spuPicture;//商品的公共图片

    private String skuPicture;//SKU的个性图片

    private String memo;    //备注
    private String status;  //1:unsubmitted   2：editing  3：submitted
    private String spSkuId;  //尚品skuId
    private String spStatus;  //1：NONE  2:pending  3:shelve  4:waiting for shelving  5:audit failed  6:unshelve 
    private Date lastTime;//修改时间
    private String sizeClass;//尺码下拉分类

    @Override
    public String toString() {
        return "ProductDTO{" +
                "supplierId='" + supplierId + '\'' +
                ", spuId='" + spuId + '\'' +
                ", skuId='" + skuId + '\'' +
                ", categoryGender='" + categoryGender + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", subCategoryName='" + subCategoryName + '\'' +
                ", seasonName='" + seasonName + '\'' +
                ", brandName='" + brandName + '\'' +
                ", material='" + material + '\'' +
                ", productOrigin='" + productOrigin + '\'' +
                ", productName='" + productName + '\'' +
                ", marketPrice=" + marketPrice +
                ", salePrice=" + salePrice +
                ", supplierPrice=" + supplierPrice +
                ", stock=" + stock +
                ", barcode='" + barcode + '\'' +
                ", productCode='" + productCode + '\'' +
                ", color='" + color + '\'' +
                ", productDescription='" + productDescription + '\'' +
                ", saleCurrency='" + saleCurrency + '\'' +
                ", size='" + size + '\'' +
                ", spuPicture='" + spuPicture + '\'' +
                ", skuPicture='" + skuPicture + '\'' +
                '}';
    }
}
