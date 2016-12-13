package com.shangpin.ep.order.module.orderapiservice.impl.dto.lungolivigno;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Rows {

    private String Sku;
    private String SizeIndex;
    private int Qty;
    private double Price;
    private double FinalPrice;
    private String PickStoreCode;

}