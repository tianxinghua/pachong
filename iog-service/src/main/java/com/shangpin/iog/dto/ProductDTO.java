package com.shangpin.iog.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by loyalty on 15/6/3.
 * SPU对象
 */
@Getter
@Setter
public class ProductDTO {
    private String id;
    private String supplierId;
    private String spuId;
    private String spuName;
    private String skuID;
    private String productName;
    private String categoryId;
    private String categoryName;
    private String brandId;
    private String seasonId;//上市季节ID
    private String brandName;
    private String seasonName;
    private String picUrl;//图片地址
    private String material;//材质
    private String productOrigin;//产地
    private String color;
    //尺码
    private String  size;
    //条形码
    private String  barcode;

    private Date createTime;
    private Date lastTime;//修改时间



}
