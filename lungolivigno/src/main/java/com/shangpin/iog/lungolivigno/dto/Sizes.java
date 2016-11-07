/**
  * Copyright 2016 aTool.org 
  */
package com.shangpin.iog.lungolivigno.dto;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
/**
 * Auto-generated: 2016-05-30 11:14:41
 *
 */
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
    
}