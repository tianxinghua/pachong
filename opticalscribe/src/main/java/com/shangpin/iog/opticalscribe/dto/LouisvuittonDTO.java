package com.shangpin.iog.opticalscribe.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Company: www.shangpin.com
 * @Author wanner
 * @Date Create in 14:24 2018/7/24
 * @Description:
 */
@Setter
@Getter
@ToString
public class LouisvuittonDTO {

    private String url;
    private String material; //材质
    private String productName;
    private String brand;
    private String productModelCode;
    private String descript;
    private String barCode;
    private String made;
    private String sex;
    private String size;
    private String sizeDesc;
    private String colorName;
    private String itemprice;
    private String spuNo;
    private String category;
    private String picUrls;
    private String qty;
    private String qtyDesc;
    private String season;
    private String otherInfos;

    private String modelMeasurements;

    //供应商原始 skuNo
    private String supplierSkuNo;

}
