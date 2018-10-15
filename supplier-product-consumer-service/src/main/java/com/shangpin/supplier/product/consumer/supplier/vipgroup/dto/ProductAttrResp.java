package com.shangpin.supplier.product.consumer.supplier.vipgroup.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaowenjun on 2018/10/12.
 */
@Getter
@Setter
public class ProductAttrResp {
    private List<ProductAttr> data = new ArrayList<ProductAttr>();
    private String itemTotal;
    private String pageSize;
    private String pageTotal;
    private String pageNum;
    private String errorCode;
    private String errorMessage;
}

