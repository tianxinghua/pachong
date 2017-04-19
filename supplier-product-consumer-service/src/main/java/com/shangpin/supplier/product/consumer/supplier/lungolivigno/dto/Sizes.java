package com.shangpin.supplier.product.consumer.supplier.lungolivigno.dto;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

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