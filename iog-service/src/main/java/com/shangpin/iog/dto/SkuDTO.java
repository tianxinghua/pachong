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
public class SkuDTO {
    private String id;
    private String supplierId;
    private String skuId;
    private String spuId;
    private String productName;
    private String supplierPrice;
    private String barcode;//条形码
    private String productCode;//货号
    private String color;
    private String productDescribe;//描述
    private String saleCurrency;//币种
    private String productSize;//尺码
    private String stock;//库存
    private Date createTime;
    private Date lastTime;//修改时间



}
