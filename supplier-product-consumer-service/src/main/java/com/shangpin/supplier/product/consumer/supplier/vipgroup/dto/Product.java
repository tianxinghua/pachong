package com.shangpin.supplier.product.consumer.supplier.vipgroup.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by zhaowenjun on 2018/10/12.
 */
@Getter
@Setter
public class Product {
    private String productId;
    private String cat;
    private String brand;
    private String name;
    private String stock;
    private String listPrice;//价格表
    private String sellPrice;//销售价
    private String wholesale;//批发价
    private String picName;
    private String description;
    private String createDate;
    private String spuId;
    private String skuId;
}
