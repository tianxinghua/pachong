package com.shangpin.iog.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by loyalty on 15/5/25.
 */
@Getter
@Setter
public class ProductDTO {

    private Long id;//代理主键

    //SPU ID;
    private Long  productId;
    //货号
    private String producerId;
    //类型
    private String type;
    //季节
    private String season;
    //产品名称
    private String productName;
    //描述
    private String description;
    //分类
    private String category;
    //图片地址
    private String url;
    //供货商价格
    private BigDecimal supplyPrice;
    //skuID
    private Long itemId;
    //尺码
    private String  itemSize;
    //条形码
    private String  barcode;
    //颜色
    private String color;
    //库存
    private Integer stock;
    //创建时间
    private Date createDate;
    //最后修改时间
    private Date lastDate;

}
