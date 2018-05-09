package com.shangpin.ephub.product.business.rest.order.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by lizhongren on 2018/3/14.
 */
@Getter
@Setter
public class SpuQueryVO implements Serializable {
    //门户编号
    private  String supplierId;
    //供货商SpuNo
    private  String supplierSpuNo;
}
