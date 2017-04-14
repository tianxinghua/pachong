package com.shangpin.supplier.product.consumer.supplier.lungolivigno.dto;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Result {

    private String Sku;
    private int Type;
    private String Name;
    private List<Attributes> Attributes;
    private List<Sizes> Sizes;
    private List<String> Children;    

}