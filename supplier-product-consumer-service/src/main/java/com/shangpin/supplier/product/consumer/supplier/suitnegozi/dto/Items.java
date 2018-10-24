package com.shangpin.supplier.product.consumer.supplier.suitnegozi.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by Administrator on 2015/5/28.
 */
@Setter
@Getter
public class Items {

    private String number_of_items;
    private List<Sku> item;

}
