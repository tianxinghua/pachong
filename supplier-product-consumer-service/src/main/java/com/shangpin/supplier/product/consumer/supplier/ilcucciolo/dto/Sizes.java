package com.shangpin.supplier.product.consumer.supplier.ilcucciolo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Sizes {

    private int SizeIndex;
    private String Label;
    private String Price;
    private int Qty;
    private List<Stockdetail> StockDetail;
    
    private String supplierPrice;
    
}