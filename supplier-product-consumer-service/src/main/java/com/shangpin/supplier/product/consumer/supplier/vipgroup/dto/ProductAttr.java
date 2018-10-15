package com.shangpin.supplier.product.consumer.supplier.vipgroup.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by zhaowenjun on 2018/10/12.
 */
@Getter
@Setter
public class ProductAttr {
    private String productId;
    private String name;
    private List<String> attributes;
}
