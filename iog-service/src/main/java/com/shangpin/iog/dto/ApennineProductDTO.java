package com.shangpin.iog.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by sunny on 2015/6/3.
 */
@Getter
@Setter
public class ApennineProductDTO {
    private Long id;//代理主键
    private String cat;  // 品牌
    private String cat1;//季节
    private String cat2;//类别
    private String cdescript;//产品中文名称
    private String color;//颜色
    private String createdate;//时间
    private String descript;//产品英文名称
    private String model;//型号
    private String pricea;//吊牌价
    private String priceb;//零售价
    private String pricec;//供货价（此价格为提供给你们的价格）
    private String scode;//货号 skuId
    private String size;//尺寸
    private String style;//款号
}
