package com.shangpin.supplier.product.consumer.supplier.common.spinnaker.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Administrator on 2015/5/28.
 */
@Setter
@Getter
public class Items {

    private String number_of_items;
    private List<Sku> item;

}
