package com.shangpin.ephub.product.business.ui.purchase.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class Product implements Serializable{

    private static final long serialVersionUID = -1672970955045193927L;

    private String supplierId;//供货商门户编号

    private String spuId; //必须

    private String skuId; //必须

    private String productName="";//产品展示名称 必须

    private String productCode;
    private String size="";//尺码 必须

    private String color="";//颜色 必须
}
