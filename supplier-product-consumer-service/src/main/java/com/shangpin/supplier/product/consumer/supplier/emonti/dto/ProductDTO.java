package com.shangpin.supplier.product.consumer.supplier.emonti.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by lizhongren on 2017/4/20.
 */
@Getter
@Setter
public class ProductDTO {
    private String supplierId;//供货商ID 必填
    private String spuId;//SPUID  必填
    private String spuName;
    /**性别分类*/
    private String categoryGender;//必填
    private String categoryId;
    /**大类*/
    private String categoryName;//必填
    private String productCode;//货号
    private String color;// 颜色 必填
    private String brandName;//必填
    private String seasonName;
    private String material;//材质 必填
    private String productOrigin;//产地
    private String productDescription;//描述
    private ProductSkuDTO productSkuDTO;  //sku
    private List<String> imageUrls;
}
