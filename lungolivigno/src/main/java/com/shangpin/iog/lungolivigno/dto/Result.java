/**
  * Copyright 2016 aTool.org 
  */
package com.shangpin.iog.lungolivigno.dto;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
/**
 * Auto-generated: 2016-05-30 11:14:41
 *
 */
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
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

    private List<String> picUrls;

}