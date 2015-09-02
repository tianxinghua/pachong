package com.shangpin.iog.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by loyalty on 15/6/3.
 * SPU价格对象
 */
@Getter
@Setter
public class SkuPriceDTO {
    private String supplierId;
    private String skuId;

    private String marketPrice;//市场价
    private String supplierPrice;//供货商价格

    private Date createTime=new Date();//创建时间

    private Date updateTime;



    
}
