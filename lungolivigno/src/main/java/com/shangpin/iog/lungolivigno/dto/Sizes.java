package com.shangpin.iog.lungolivigno.dto;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Sizes {

    @JsonProperty("SizeIndex")
    private int SizeIndex;
    @JsonProperty("Label")
    private String Label;
    @JsonProperty("Price")
    private String Price;
    @JsonProperty("Qty")
    private int Qty;
    @JsonProperty("StockDetail")
    private List<Stockdetail> StockDetail;
    
    private String supplierPrice;
    
}