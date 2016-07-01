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
@Setter
@Getter
public class Result {

    @JsonProperty("Sku")
    private String Sku;
    @JsonProperty("Type")
    private int Type;
    @JsonProperty("Name")
    private String Name;
    @JsonProperty("Attributes")
    private List<Attributes> Attributes;
    @JsonProperty("Sizes")
    private List<Sizes> Sizes;
    @JsonProperty("Children")
    private List<String> Children;    

}