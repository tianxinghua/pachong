package com.shangpin.iog.spinnaker.stock.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Administrator on 2015/5/28.
 */
@Setter
@Getter
public class Items {

    private String number_of_items;
    private Sku[] item;

}
