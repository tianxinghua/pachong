package com.shangpin.iog.facade.dubbo.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by loyalty on 15/9/15.
 * 产品信息
 */
@Setter
@Getter
public class ProductDTO {

    private String supplierId;//供货商门户编号

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





    @Override
    public String toString() {
        return "ProductDTO{" +
                "spuId='" + null==spuId?"":spuId + '\'' +
                ", skuId='" + null==skuId?"":skuId + '\'' +
                ", categoryGender='" + null==categoryGender?"":categoryGender + '\'' +
                ", categoryName='" + null==categoryName?"":categoryName + '\'' +
                ", subCategoryName='" + null==subCategoryName?"":subCategoryName + '\'' +

                ", seasonName='" + null==seasonName?"":seasonName + '\'' +
                ", brandName='" + brandName + '\'' +
                ", material='" + material + '\'' +
                ", productOrigin='" + productOrigin + '\'' +
                ", productName='" + productName + '\'' +
                ", marketPrice='" + (null!=marketPrice?marketPrice.toString():"") + '\'' +
                ", salePrice='" + (null!=salePrice?salePrice.toString():"") + '\'' +
                ", supplierPrice='" + (null!=supplierPrice?supplierPrice.toString():"") + '\'' +
                ", stock='" + stock + '\'' +
                ", barcode='" + barcode + '\'' +
                ", productCode='" + productCode + '\'' +
                ", color='" + color + '\'' +
                ", productDescription='" + productDescription + '\'' +
                ", saleCurrency='" + saleCurrency + '\'' +
                ", productSize='" + size + '\'' +
                ", spuPicture='" + spuPicture + '\'' +

                ", skuPicture='" + skuPicture + '\'' +

                '}';
    }
}
