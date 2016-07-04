/**
  * Copyright 2016 aTool.org 
  */
package com.shangpin.iog.lungolivigno.dto;
import lombok.Getter;
import lombok.Setter;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
/**
 * Auto-generated: 2016-05-30 18:33:24
 *
 */
@Getter
@Setter
public class Sizes {

    @JsonProperty("Tag")
    private String Tag;
    @JsonProperty("SizeIndex")
    private int SizeIndex;
    @JsonProperty("Qty")
    private int Qty;
}