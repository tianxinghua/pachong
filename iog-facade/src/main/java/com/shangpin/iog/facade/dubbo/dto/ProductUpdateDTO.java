package com.shangpin.iog.facade.dubbo.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by loyalty on 15/9/15.
 * 产品信息
 * 可修改的信息
 */
@Setter
@Getter
public class ProductUpdateDTO implements Serializable {







    private String material;//材质  必须

    private String productOrigin;//产地

    private String productName;//产品名称

    private String marketPrice;//吊牌价（市场价）

    private String salePrice;//销售价格

    private String supplierPrice;//供货商价格

    private String stock;//库存 必须

    private String barcode;//条形码

    private String productCode;//货号

    private String color;//颜色 必须

    private String productDescription;//描述

    private String saleCurrency;//币种

    private String productSize;//尺码 必须



}
