package com.shangpin.supplier.product.consumer.supplier.ilcucciolo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Result {

    private String Sku;
    private int Type;
    private String Name;
    private List<com.shangpin.supplier.product.consumer.supplier.ilcucciolo.dto.Attributes> Attributes;
    private List<com.shangpin.supplier.product.consumer.supplier.ilcucciolo.dto.Sizes> Sizes;
    private List<String> Children;    
    private List<String> picUrls;
}