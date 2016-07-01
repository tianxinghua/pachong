/**
  * Copyright 2016 aTool.org 
  */
package com.shangpin.iog.lungolivigno.dto;
import lombok.Getter;
import lombok.Setter;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
/**
 * Auto-generated: 2016-05-31 9:41:17
 *
 * @author aTool.org (i@aTool.org)
 * @website http://www.atool.org/json2javabean.php
 */
@Getter
@Setter
public class Rows {

    @JsonProperty("Sku")
    private String Sku;
    @JsonProperty("SizeIndex")
    private String SizeIndex;
    @JsonProperty("Qty")
    private int Qty;
    @JsonProperty("Price")
    private double Price;
    @JsonProperty("FinalPrice")
    private double FinalPrice;
    @JsonProperty("PickStoreCode")
    private String PickStoreCode;

}