package com.shangpin.iog.fashionesta.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FashionestaDTO {
    private String SUPPLIER_CODE;//skuid、spuid
    private String PRODUCT_NAME;//skupname
    private String CATEGORY;//categoryName
    private String BRAND;//brandname
   
    private String MATERIAL1;//material
    private String MATERIAL2;//material
    private String MATERIAL3;//material
    private String MATERIAL4;//material
//    private String MATERIAL_1_PERCENT;//material
//    private String MATERIAL_2_PERCENT;//material
//    private String MATERIAL_3_PERCENT;//material
//    private String MATERIAL_4_PERCENT;//material

    private String GENDER;//material
    private String COLOR;
    private String MADE;//country_of_origin
    private String DESCRIPTION;
    private String PRICE;//marketprice
    private String SPECIAL_PRICE;//supplierPrice
    private String CURRENCY;//币种
    private String IMAGE_URL;//图片
    private String SIMAGE_URL;//图片
    private String SIZE;//size
    private String STOCK;//库存
}
