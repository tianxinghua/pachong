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
public class SpuDTO {
    private String id;
    private String supplierId;
    private String spuId;
    private String spuName;
    private String categoryId;
    private String categoryName;
    private String brandId;
    private String seasonId;//上市季节ID
    private String brandName;
    private String seasonName;
    private String picUrl;//图片地址
    private String material;//材质
    private String productOrigin;//产地
    private Date createTime= new Date();
    private Date lastTime= new Date();//修改时间



}
