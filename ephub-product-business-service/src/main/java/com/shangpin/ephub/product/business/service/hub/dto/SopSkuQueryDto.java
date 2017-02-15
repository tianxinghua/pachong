package com.shangpin.ephub.product.business.service.hub.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lizhongren on 2017/2/14.
 */
@Setter
@Getter
public class SopSkuQueryDto implements Serializable{
    private String sopUserNo;
    private List<String> lstSupplierSkuNo;
}
